package com.example.work_pana.popularmoviesstage_one.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.work_pana.popularmoviesstage_one.models.MovieUtils;
import com.example.work_pana.popularmoviesstage_one.R;
import com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Work-Pana on 3/12/2018.
 */

public class CostumCursorAdapterFavM extends RecyclerView.Adapter<CostumCursorAdapterFavM.FavMovieViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private android.database.Cursor mCursor;
    private Context mContext;
    private List<MovieUtils> mFavMovieList;
    private OnItemClickListener mFavListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mFavListener = listener;
    }

    public CostumCursorAdapterFavM(Context context, List<MovieUtils> movieList) {
        mContext = context;
        mFavMovieList = movieList;
    }

    @Override
    public FavMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_item, parent, false);
        return new FavMovieViewHolder(v);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(FavMovieViewHolder holder, int position) {
// Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(FavMovieDbContract.MovieEntry._ID);
        int posterIndex = mCursor.getColumnIndex(FavMovieDbContract.MovieEntry.COLUMN_POSTER_URL);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String moviePosterUrl = mCursor.getString(posterIndex);

        //Set values
        holder.itemView.setTag(id);
        Picasso.with(mContext).load(moviePosterUrl).into(holder.itemFavMovieImageView);


    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class FavMovieViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the movie poster ImageView
        ImageView itemFavMovieImageView;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public FavMovieViewHolder(View itemView) {
            super(itemView);
            itemFavMovieImageView = itemView.findViewById(R.id.iv_movie);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mFavListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mFavListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
