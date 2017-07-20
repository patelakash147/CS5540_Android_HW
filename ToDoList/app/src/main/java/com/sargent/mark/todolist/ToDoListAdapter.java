package com.sargent.mark.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sargent.mark.todolist.data.Contract;
import com.sargent.mark.todolist.data.DBHelper;
import com.sargent.mark.todolist.data.ToDoItem;

import java.util.ArrayList;

/**
 * Created by mark on 7/4/17.
 */



public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ItemHolder> {

    private Cursor cursor;
    private ItemClickListener listener;
    private String TAG = "todolistadapter";

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item, parent, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    // add category and status
    public interface ItemClickListener {
        void onItemClick(int pos, String description, String duedate, long id, String category, String status);
    }

    public ToDoListAdapter(Cursor cursor, ItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    public void swapCursor(Cursor newCursor){
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView descr;
        TextView due;
        String duedate;
        String description;
        long id;
        //  showing for category
        TextView categories;
        // showing for status
        CheckBox statues;

        // String  for category
        String category;
        // String  for status
        String status;



        ItemHolder(View view) {
            super(view);
            descr = (TextView) view.findViewById(R.id.description);
            due = (TextView) view.findViewById(R.id.dueDate);
            // find category by ID
            categories = (TextView)  view.findViewById(R.id.category);
            // find status by ID
            statues = (CheckBox) view.findViewById(R.id.status);
            view.setOnClickListener(this);
        }

        public void bind(ItemHolder holder, int pos) {
            cursor.moveToPosition(pos);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_TODO._ID));
            Log.d(TAG, "deleting id: " + id);

            duedate = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE));
            description = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION));
            // get category from database
            category = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY));
            // get status from database
            status = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_STATUS));

            due.setText(duedate);
            descr.setText(description);
            // set category text for TextView
            categories.setText(category);

            // logic for statues complete , check and unchecked.

            try {
                if (status.equals("Complete")) {
                    statues.setChecked(true);
                } else {
                    statues.setChecked(false);
                }
            } catch (NullPointerException e) {
                statues.setChecked(false);
            }


            //  click listener to status checkbox
            statues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper helper = new DBHelper(v.getContext());
                    SQLiteDatabase db = helper.getWritableDatabase();
                    MainActivity.updateTodoStatus(db, id, statues.isChecked());
                }
            });

            holder.itemView.setTag(id);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            listener.onItemClick(pos, description, duedate, id, category, status);
        }
    }

}
