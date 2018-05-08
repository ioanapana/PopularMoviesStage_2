package com.example.work_pana.popularmoviesstage_one.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.work_pana.popularmoviesstage_one.models.MovieUtils;
import com.example.work_pana.popularmoviesstage_one.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Work-Pana on 3/12/2018.
 */

public class MovieAdapterRV extends RecyclerView.Adapter<MovieAdapterRV.MovieViewHolder> {
    private Context mContext;
    private List<MovieUtils> mMovieList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MovieAdapterRV(Context context, List<MovieUtils> movieList) {
        mContext = context;
        mMovieList = movieList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        MovieUtils currentItem = mMovieList.get(position);

        String moviePosterUrl = currentItem.getMoviePosterUrl();
        Picasso.with(mContext).load(moviePosterUrl).into(holder.itemMovieImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView itemMovieImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            itemMovieImageView = itemView.findViewById(R.id.iv_movie);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
