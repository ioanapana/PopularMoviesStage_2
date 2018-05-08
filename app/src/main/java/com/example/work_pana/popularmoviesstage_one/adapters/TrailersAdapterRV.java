package com.example.work_pana.popularmoviesstage_one.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.work_pana.popularmoviesstage_one.R;
import com.example.work_pana.popularmoviesstage_one.models.MovieUtils;
import com.example.work_pana.popularmoviesstage_one.models.TrailerObject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Work-Pana on 3/12/2018.
 */

public class TrailersAdapterRV extends RecyclerView.Adapter<TrailersAdapterRV.TrailerViewHolder> {
    private Context mContext;
    private List<TrailerObject> mTrailerList;
    private OnItemClickListener mListener;
    TrailerObject currentTrailer;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public TrailersAdapterRV(Context context, List<TrailerObject> movieList) {
        mContext = context;
        mTrailerList = movieList;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

       currentTrailer = mTrailerList.get(position);

        TextView trailerName = holder.trailerName;
        trailerName.setText(currentTrailer.getNameTrailer());

    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        TextView trailerName;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerName = itemView.findViewById(R.id.tv_name_video);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentTrailer.getSiteTrailer()));
                mContext.startActivities(new Intent[]{youtubeIntent});
                    }
                }
            );

//            playVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentTrailer.getSiteTrailer()));
//                mContext.startActivities(new Intent[]{youtubeIntent});
//            }
//        });

        }
    }
}
