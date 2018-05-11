package com.example.work_pana.popularmoviesstage_one;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.work_pana.popularmoviesstage_one.adapters.MovieAdapterRV;
import com.example.work_pana.popularmoviesstage_one.adapters.ReviewAdapterRV;
import com.example.work_pana.popularmoviesstage_one.adapters.TrailersAdapterRV;
import com.example.work_pana.popularmoviesstage_one.models.MovieUtils;
import com.example.work_pana.popularmoviesstage_one.models.ReviewObject;
import com.example.work_pana.popularmoviesstage_one.models.TrailerObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private RecyclerView mTrailerRecyclerView;
    private TrailersAdapterRV mTrailerAdapterRV;
    private List<ReviewObject> mReviewList;
    private List<TrailerObject> mTrailerList;
    private RequestQueue mReviewsRequestQueue;
    private RequestQueue mTrailersRequestQueue;
    private String API_KEY = "api_key=df27df00b2916dd211edca6241aea543";


    /////////////////////////////////////////////////////////MODIFICA!!!!
    //Constants for creating the URL
    private static final String BASE_URL
            = "https://api.themoviedb.org/3/movie/";

    private String MOVIE_ID;
    private static String reviewsEndPoint = "/reviews?";
    private static String trailersEndPoint = "/videos?";
    private String reviewsURL;
    private String trailersURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);
//         MOVIE_ID = movieItem.getId();


        mReviewRecycterView = findViewById(R.id.rv_reviews);
        mReviewList = new ArrayList<>();
        mReviewAdapterRv = new ReviewAdapterRV(getApplicationContext(), mReviewList);
        RecyclerView.LayoutManager mReviewsLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
        mReviewRecycterView.setLayoutManager(mReviewsLayoutManager);
        mReviewRecycterView.setAdapter(mReviewAdapterRv);
        mReviewRecycterView.setHasFixedSize(true);

        mTrailerRecyclerView = findViewById(R.id.rv_trailer);
        mTrailerList = new ArrayList<>();
        mTrailerAdapterRV = new TrailersAdapterRV(getApplicationContext(), mTrailerList);
        RecyclerView.LayoutManager mTrailersLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false);
        mTrailerRecyclerView.setLayoutManager(mTrailersLayoutManager);
        mTrailerRecyclerView.setAdapter(mTrailerAdapterRV);
        mTrailerRecyclerView.setHasFixedSize(true);


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

    private void parceTrailersJson() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, trailersURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray trailersResultsJasonArray = response.getJSONArray("results");
                            for (int i = 0; i < trailersResultsJasonArray.length(); i++) {
                                JSONObject trailerResult = trailersResultsJasonArray.getJSONObject(i);
                                TrailerObject trailerItem = new TrailerObject();

                                trailerItem.setKeyTrailer(trailerResult.getString("key"));
                                trailerItem.setNameTrailer(trailerResult.getString("name"));
                                trailerItem.setSiteTrailer(trailerResult.getString("site"));

                                mTrailerList.add(trailerItem);
                            }
                            mTrailerAdapterRV = new TrailersAdapterRV(DetailActivity.this, mTrailerList);
                            mTrailerRecyclerView.setAdapter(mTrailerAdapterRV);
                            mTrailerAdapterRV.notifyDataSetChanged();
//                            mTrailerAdapterRV.setOnItemClickListener(DetailActivity.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
            }
        });
        mReviewsRequestQueue = Volley.newRequestQueue(this);
        mReviewsRequestQueue.add(request);
    }

    private void parceReviewsJson() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, reviewsURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray reviewResultsJasonArray = response.getJSONArray("results");
                            for (int i = 0; i < reviewResultsJasonArray.length(); i++) {
                                JSONObject reviewrResult = reviewResultsJasonArray.getJSONObject(i);
                                ReviewObject reviewItem = new ReviewObject();

                                reviewItem.setAuthor(reviewrResult.getString("author"));
                                reviewItem.setContent(reviewrResult.getString("content"));

                                mReviewList.add(reviewItem);
                            }
                            mReviewAdapterRv = new ReviewAdapterRV(DetailActivity.this, mReviewList);
                            mReviewRecycterView.setAdapter(mReviewAdapterRv);
                            mReviewAdapterRv.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getStackTrace();
            }
        });
        mTrailersRequestQueue = Volley.newRequestQueue(this);
        mTrailersRequestQueue.add(request);
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
        int id = movieItem.getId();


        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.user_placeholder_error)
                .error(R.drawable.user_placeholder_error)
                .into(imageView);
        textViewTitle.setText(originalTitle);
//        textViewTitle.setText(movieid);
        textViewSynopsis.setText(synopsis);
        textViewReleaseDate.setText(releaseDate);
        textViewRating.setText(userRating);

        MOVIE_ID = Integer.toString(id);
        reviewsURL = BASE_URL + MOVIE_ID + reviewsEndPoint + API_KEY;
        trailersURL = BASE_URL + MOVIE_ID + trailersEndPoint + API_KEY;
        parceTrailersJson();
        parceReviewsJson();
    }

    public void onFavouriteCheckboxClicked(View view) {
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


//    public static List<TrailerObject> parseTrailerJson(String json) {
//        List<TrailerObject> trailersList = new ArrayList<>();
//        try {
//            JSONObject rootTrailerJsonObject = new JSONObject(json);
//            JSONArray rootTrailersArray = rootTrailerJsonObject.getJSONArray(ROOT_JSON);
//            for (int i = 0; i < rootTrailersArray.length(); i++) {
//                JSONObject trailersJsonObject = rootTrailersArray.getJSONObject(i);
//
//                String trailerKey = trailersJsonObject.getString(TRAILER_KEY);
//                String trailerName = trailersJsonObject.getString(TRAILER_NAME);
//                trailersList.add(new TrailerObject(trailerKey, trailerName));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return trailersList;
//    }
}