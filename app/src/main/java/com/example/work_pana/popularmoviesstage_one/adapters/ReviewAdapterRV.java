package com.example.work_pana.popularmoviesstage_one.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.work_pana.popularmoviesstage_one.R;
import com.example.work_pana.popularmoviesstage_one.models.ReviewObject;

import java.util.List;

public class ReviewAdapterRV extends RecyclerView.Adapter<ReviewAdapterRV.ReviewViewHolder> {
    private List<ReviewObject> reviewsList;
    private Context mContext;

    public ReviewAdapterRV(Context context, List<ReviewObject> reviewsList) {
        this.mContext = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewObject reviewItem = reviewsList.get(position);

        TextView author = holder.authorOfReview;
        author.setText(reviewItem.getAuthor());
        final TextView content = holder.contentReview;
        content.setText(reviewItem.getContent());
        final int maxLines = 4;
        content.setMaxLines(maxLines);
        content.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (content.getMaxLines() == maxLines) {
                    content.setMaxLines(content.length());
                } else {
                    content.setMaxLines(maxLines);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }


    public void addAll(List<ReviewObject> reviewsList) {
        this.reviewsList.clear();
        this.reviewsList.addAll(reviewsList);
        notifyDataSetChanged();
    }

    public void clearAll() {
        this.reviewsList.clear();
        notifyDataSetChanged();
    }


    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView authorOfReview;
        TextView contentReview;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            authorOfReview = itemView.findViewById(R.id.tv_review_author);
            contentReview = itemView.findViewById(R.id.tv_review_content);
        }
    }
}

