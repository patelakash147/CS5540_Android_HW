package com.example.akash.news_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by akash on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private static final String TAG = NewsAdapter.class.getSimpleName();

    private ArrayList<NewsItem> data;

    final private ItemClickListener listener;

    public NewsAdapter(ArrayList<NewsItem> data, ItemClickListener listener) {
        this.data = data;

        this.listener = listener;
    }


    public interface ItemClickListener {

        void onListItemClick(int clickedItemIndex);
    }
    class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mNewsTitle;
        public final TextView mNewsDescription;
        public final TextView mNewsTime;



        public void bind(int pos) {
            NewsItem newsItem = data.get(pos);
            mNewsTitle.setText(newsItem.getTitle());
            mNewsDescription.setText(newsItem.getDescription());
            mNewsTime.setText(newsItem.getPublishedAt());
        }

        public NewsAdapterViewHolder(View itemView) {
            super(itemView);
            mNewsTitle = (TextView) itemView.findViewById(R.id.news_title);
            mNewsDescription = (TextView) itemView.findViewById(R.id.news);
            mNewsTime = (TextView) itemView.findViewById(R.id.news_time);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            listener.onListItemClick(getAdapterPosition());
        }
    }



    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        holder.bind(position);
    }
    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.newslist_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attachToParent = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, attachToParent);

        return new NewsAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public void setData(ArrayList<NewsItem> data) {
        this.data = data;
    }
}
