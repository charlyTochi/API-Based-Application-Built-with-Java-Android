package org.example.orafucharles.myfirstapplication.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.example.orafucharles.myfirstapplication.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    //List object that holds movie data
    private final List<News> mNewsData;

    private final ItemClickListener mItemClickListener;

    //Constructor
    public NewsAdapter(List<News> data, ItemClickListener clickListener) {
        mNewsData = data;
        mItemClickListener = clickListener;
    }

    //Interface for click handling
    public interface ItemClickListener {
        void onItemClickListener(News newsItem);
    }

    @NonNull
    @Override
    public NewsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layout = R.layout.item_news;
        View view = LayoutInflater.from(context).inflate(layout, viewGroup, false);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterViewHolder holder, int position) {
        News currentNewsItem = mNewsData.get(position);
        holder.mSectionNameTextView.setText(currentNewsItem.getSectionName());
        holder.mWebTitleTextView.setText(currentNewsItem.getWebTitle());
        holder.mPublishedDateTextView.setText(currentNewsItem.getWebPublicationDate());

    }

    @Override
    public int getItemCount() {
        return mNewsData.size();
    }

    //ViewHolder class
    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //UI elements
        final TextView mSectionNameTextView;
        final TextView mWebTitleTextView;
        final TextView mPublishedDateTextView;

        //Class constructor
        NewsAdapterViewHolder(View itemView) {
            super(itemView);
            mSectionNameTextView = itemView.findViewById(R.id.tv_section_name);
            mWebTitleTextView = itemView.findViewById(R.id.tv_web_title);
            mPublishedDateTextView = itemView.findViewById(R.id.tv_publication_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mItemClickListener.onItemClickListener(mNewsData.get(position));
        }
    }
}
