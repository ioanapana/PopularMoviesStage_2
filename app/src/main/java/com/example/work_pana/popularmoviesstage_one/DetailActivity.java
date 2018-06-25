package com.example.work_pana.popularmoviesstage_one;

import android.annotation.SuppressLint;
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
import android.support.design.widget.FloatingActionButton;

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
import com.example.work_pana.popularmoviesstage_one.utils.FavMoviesUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.CONTENT_URI;

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
    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";
    private static final String SAVE_STATE_FAVOURITE = "favoutite_movie";
    MovieUtils movieItem;

    private RecyclerView mReviewRecycterView;
    private ReviewAdapterRV mReviewAdapterRv;
    private RecyclerView mTrailerRecyclerView;
    private TrailersAdapterRV mTrailerAdapterRV;
    private List<ReviewObject> mReviewList;
    private List<TrailerObject> mTrailerList;
    private RequestQueue mReviewsRequestQueue;
    private RequestQueue mTrailersRequestQueue;
    private String API_KEY = "api_key=df27df00b2916dd211edca6241aea543";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private String MOVIE_ID;
    private static String reviewsEndPoint = "/reviews?";
    private static String trailersEndPoint = "/videos?";
    private String reviewsURL;
    private String trailersURL;
    FloatingActionButton favoriteViewDetailsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        ButterKnife.bind(this);

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
                getSupportActionBar().setTitle(movieItem.getOriginalTitle());
                populateUI();
            }
        }

        //favorite button that will show if a movie is in the favorite list or not
        favoriteViewDetailsContent = findViewById(R.id.favorite_details_content);
        if (FavMoviesUtils.isAlreadyFavorite(this, movieItem.getId())) {
            favoriteViewDetailsContent.setImageResource(R.drawable.ic_favourite_star_yellow);
        } else {
            favoriteViewDetailsContent.setImageResource(R.drawable.ic_favourite_star);
        }

        //onClickListener pe favorite Button that will help to put or remove a movie in/from the
        //favorite movie list
        favoriteViewDetailsContent.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (FavMoviesUtils.isAlreadyFavorite(DetailActivity.this,
                        movieItem.getId())) {
                    favoriteViewDetailsContent.setImageResource(R.drawable.ic_favourite_star);
                    int moviesRemoved = FavMoviesUtils.removeFromFavorite(
                            DetailActivity.this, movieItem.getId());
                    if (moviesRemoved > 0) Toast.makeText(DetailActivity.this,
                            getString(R.string.movie_deleted), Toast.LENGTH_SHORT).show();

                } else {
                    favoriteViewDetailsContent.setImageResource(R.drawable.ic_favourite_star_yellow);
                    ContentValues cv = FavMoviesUtils.addMovieToFavoritesList(movieItem);
                    Uri uri = getContentResolver().insert(CONTENT_URI, cv);
                    if (uri != null)
                        Toast.makeText(DetailActivity.this, getString(R.string.movie_added),
                                Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //Metgod for searching the trailers

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

    // Method for searching the reviews
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

    // Methot that populates the UI

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
        textViewSynopsis.setText(synopsis);
        textViewReleaseDate.setText(releaseDate);
        textViewRating.setText(userRating);

        MOVIE_ID = Integer.toString(id);
        reviewsURL = BASE_URL + MOVIE_ID + reviewsEndPoint + API_KEY;
        trailersURL = BASE_URL + MOVIE_ID + trailersEndPoint + API_KEY;
        parceTrailersJson();
        parceReviewsJson();
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
}