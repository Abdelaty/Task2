package com.example.task2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.task2.POJO.News.Article;
import com.example.task2.R;
import com.example.task2.View.DetailsNews;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private NewsAdapter.OnItemClickListener mListener;
    private ArrayList<Article> newsExampleArrayList;
    private Context context;

    public NewsAdapter(ArrayList<Article> newsList, Context context) {
        this.newsExampleArrayList = newsList;
        this.context = context;
    }

    public void setOnItemClickListener(NewsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rv_item, parent, false);

        return new NewsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsAdapter.MyViewHolder holder, final int position) {
        holder.news_title.setText(newsExampleArrayList.get(position).getTitle());

        Glide.with(context).load(newsExampleArrayList.get(position).getUrlToImage()).transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.newsImage);

        holder.newsAuthor.setText(newsExampleArrayList.get(position).getSource().getName().toString());

        holder.itemView.setOnClickListener(view -> {

            Intent myIntent = new Intent(context, DetailsNews.class);
            myIntent.putExtra("NEWS_TITLE", newsExampleArrayList.get(position).getTitle()); //Optional parameters ing
            myIntent.putExtra("NEWS_URL", newsExampleArrayList.get(position).getUrl()); //Optional parameters ing
            context.startActivity(myIntent);
        });
    }

    @Override
    public int getItemCount() {

        Log.v("getItemCount", "size is: " + newsExampleArrayList.size());

        return newsExampleArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_title)
        TextView news_title;

        @BindView(R.id.news_img)
        ImageView newsImage;

        @BindView(R.id.author)
        TextView newsAuthor;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}