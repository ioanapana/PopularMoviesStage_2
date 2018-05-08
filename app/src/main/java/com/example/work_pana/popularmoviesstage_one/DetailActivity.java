package com.example.work_pana.popularmoviesstage_one;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.work_pana.popularmoviesstage_one.adapters.ReviewAdapterRV;
import com.example.work_pana.popularmoviesstage_one.adapters.TrailersAdapterRV;
import com.example.work_pana.popularmoviesstage_one.models.MovieUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Work-Pana on 3/26/2018.
 */

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.iv_movie_poster)
    ImageView imageView;
    @BindView(R.id.tv_original_title)
    TextView textViewTitle;
    @BindView(R.id.tv_synopsis)
    TextView textViewSynopsis;
    @BindView(R.id.tv_user_rating)
    TextView textViewReleaseDate;
    @BindView(R.id.tv_release_date)
    TextView textViewRating;
    @BindView(R.id.favourite_star)
    CheckBox favouriteCheckBox;
    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    private static final String SAVE_STATE_FAVOURITE = "favoutite_movie";
    MovieUtils movieItem;
    private Uri mCurrentMovieUri;
    private boolean mIsFavourite;

    private RecyclerView mReviewRecycterView;
    private ReviewAdapterRV mReviewAdapterRv;
    private RecyclerView mTrailerReciclerView;
    private TrailersAdapterRV mTrailerAdapterRV;
//    private MovieAdapterRV movieAdapterRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);

        mTrailerAdapterRV = findViewById(R.id.rv_trailer);
        mReviewRecycterView = findViewById(R.id.rv_reviews);
//        movieAdapterRV = findViewById(R.id.rv_movie);


        Intent intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra(EXTRA_MOVIE)) {
                movieItem = intent.getParcelableExtra(EXTRA_MOVIE);
                // Display the current selected movie title on the Action Bar
                getSupportActionBar().setTitle(movieItem.getOriginalTitle());
                populateUI();
            }
        }

        if (savedInstanceState != null) {
            mIsFavourite = savedInstanceState.getBoolean(SAVE_STATE_FAVOURITE);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVE_STATE_FAVOURITE, mIsFavourite);
    }

    public void populateUI() {

        String imageUrl = movieItem.getMoviePosterUrl();
        String originalTitle = movieItem.getOriginalTitle();
        String synopsis = movieItem.getSynopsis();
        String releaseDate = movieItem.getReleaseDate();
        double userRatingDouble = movieItem.getUserRating();
        String userRating = Double.toString(userRatingDouble);

//        ImageView imageView = findViewById(R.id.iv_movie_poster);
//        TextView textViewTitle = findViewById(R.id.tv_original_title);
//        TextView textViewSynopsis = findViewById(R.id.tv_synopsis);
//        TextView textViewReleaseDate = findViewById(R.id.tv_release_date);
//        TextView textViewRating = findViewById(R.id.tv_user_rating);

        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.user_placeholder_error)
                .error(R.drawable.user_placeholder_error)
                .into(imageView);
        textViewTitle.setText(originalTitle);
        textViewSynopsis.setText(synopsis);
        textViewReleaseDate.setText(releaseDate);
        textViewRating.setText(userRating);
    }

    public void onFavouriteCheckboxClicked(View view) {
//        CheckBox favouriteCheckBox = findViewById(R.id.favourite_star);
        if (favouriteCheckBox.isChecked()) {
            addToFavorites();
            Toast.makeText(this,
                    "Added to favourite", Toast.LENGTH_LONG).show();

        } else {
            removeFromFavorites();
            Toast.makeText(this,
                    "Deleted from favourite", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method that adds selected movie to the database using the Content Resolver
     */
    public void addToFavorites() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_MOVIE_ID, movieItem.getId());
        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_POSTER_URL, movieItem.getMoviePosterUrl());
        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_TITLE, movieItem.getOriginalTitle());
        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_OVERVIEW, movieItem.getSynopsis());
        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_RELEASE_DATE, movieItem.getReleaseDate());
        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_RATING, movieItem.getUserRating());

        try {
            mCurrentMovieUri = getContentResolver().insert(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.CONTENT_URI,
                    contentValues);
        } catch (IllegalArgumentException e) {
            mCurrentMovieUri = null;
        }

        if (mCurrentMovieUri != null) {
            isAddedToFavorites();
        }

    }

    /**
     * Method to delete a movie from the database
     */
    private void removeFromFavorites() {
        int rowsDeleted;

        if (mCurrentMovieUri != null) {
            rowsDeleted = getContentResolver().delete(
                    mCurrentMovieUri,
                    null,
                    null);
        }
    }

    /**
     * Method that checks if the current movie is currently into the database
     * by queering it with the help of the Content resolver
     *
     * @return boolean whether it's in the database or not
     */
    private boolean isAddedToFavorites() {
        boolean isFavorite = false;

        String[] projection = {com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_MOVIE_ID};
        String selection = com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = new String[]{
                String.valueOf(movieItem.getId())};

        Cursor cursor = this.getContentResolver().query(
                com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                isFavorite = true;
                long currentIndex = cursor.getLong(cursor.getColumnIndex(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_MOVIE_ID));
                mCurrentMovieUri = ContentUris.withAppendedId(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.CONTENT_URI, currentIndex);
            } else {
                isFavorite = false;

                mCurrentMovieUri = null;
            }
            cursor.close();
        }

        return isFavorite;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

//    private void parceJson(String chosenURL) {
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, chosenURL, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray moviesResultsJasonArray = response.getJSONArray("results");
//                            for (int i = 0; i < moviesResultsJasonArray.length(); i++) {
//                                JSONObject movieResult = moviesResultsJasonArray.getJSONObject(i);
//                                MovieUtils movieItem = new MovieUtils();
//                                movieItem.setMoviePosterUrl(baseImageUrl + movieResult.getString("poster_path"));
//                                movieItem.setId(movieResult.getInt("id"));
//                                movieItem.setOriginalTitle(movieResult.getString("original_title"));
//                                movieItem.setSynopsis(movieResult.getString("overview"));
//                                movieItem.setReleaseDate(movieResult.getString("release_date"));
//                                movieItem.setUserRating(movieResult.getDouble("vote_average"));
//                                mMovieList.add(movieItem);
//                            }
//                            mMovieAdapterRv = new MovieAdapterRV(MainActivity.this, mMovieList);
//                            mRecycterView.setAdapter(mMovieAdapterRv);
//                            mMovieAdapterRv.notifyDataSetChanged();
//                            mMovieAdapterRv.setOnItemClickListener(MainActivity.this);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.getStackTrace();
//            }
//        });
//        mRequestQueue = Volley.newRequestQueue(this);
//        mRequestQueue.add(request);
//    }
}