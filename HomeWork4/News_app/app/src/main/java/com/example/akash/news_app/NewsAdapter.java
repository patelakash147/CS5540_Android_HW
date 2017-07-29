package com.example.akash.news_app;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.akash.news_app.data.Contract;
import com.squareup.picasso.Picasso;

/**
 * Created by akash on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    private static final String TAG = NewsAdapter.class.getSimpleName();
    final private ItemClickListener listener;
    private Context con;

    // add field for cursor
    private Cursor cur;


    public NewsAdapter(Cursor cursor, ItemClickListener listener) {
        this.cur = cursor;
        this.listener = listener;
    }


    public interface ItemClickListener {
        void onListItemClick(Cursor cursor, int clickedItemIndex);
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        con = viewGroup.getContext();
        int layoutIdForListItem = R.layout.newslist_item;
        LayoutInflater inflater = LayoutInflater.from(con);
        boolean attachToParent = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, attachToParent);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    // update the value
    @Override
    public int getItemCount() {
        return cur.getCount();
    }

    class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mNewsTitle;
        public final TextView mNewsDescription;
        public final TextView mNewsTime;


        public final ImageView img;  // this field is for image view

        public NewsAdapterViewHolder(View itemView) {
            super(itemView);
            mNewsTitle = (TextView) itemView.findViewById(R.id.news_title);
            mNewsDescription = (TextView) itemView.findViewById(R.id.news_description);
            mNewsTime = (TextView) itemView.findViewById(R.id.news_time);
            img = (ImageView)itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

        public void bind(int pos) {
            cur.moveToPosition(pos);

            mNewsTitle.setText(cur.getString(cur.getColumnIndex(Contract.NewsItem.TITLE)));
            mNewsDescription.setText(cur.getString(cur.getColumnIndex(Contract.NewsItem.DESCRIPTION)));
            mNewsTime.setText(cur.getString(cur.getColumnIndex(Contract.NewsItem.PUBLISHED_AT)));

            // using Picasso for image thumbnail
            String urlToImage = cur.getString(cur.getColumnIndex(Contract.NewsItem.URL_TO_IMAGE));
            Log.d(TAG, urlToImage);
            if(urlToImage != null){
                Picasso.with(con)
                        .load(urlToImage)
                        .into(img);
            }
        }


        @Override
        public void onClick(View v) {
            listener.onListItemClick(cur, getAdapterPosition());
        }
    }
}