package com.example.work_pana.popularmoviesstage_one;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.work_pana.popularmoviesstage_one.adapters.CostumCursorAdapterFavM;
import com.example.work_pana.popularmoviesstage_one.adapters.MovieAdapterRV;
import com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract;
import com.example.work_pana.popularmoviesstage_one.models.MovieUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.work_pana.popularmoviesstage_one.DetailActivity.EXTRA_MOVIE;

public class MainActivity extends AppCompatActivity implements com.example.work_pana.popularmoviesstage_one.adapters.MovieAdapterRV.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{
    private RecyclerView mRecycterView;
    private MovieAdapterRV mMovieAdapterRv;
    private CostumCursorAdapterFavM mAdapterFavM;

    private List<MovieUtils> mMovieList;
    private RequestQueue mRequestQueue;
    private Spinner spinner;
    private String movieListBaseURL = "http://api.themoviedb.org/3";
    private String popularMoviesEndpoint = "/movie/popular?";
    private String topRatedMoviesEndpoint = "/movie/top_rated?";
    private String apiKey = "api_key=df27df00b2916dd211edca6241aea543";
    private String popularMoviesURL = movieListBaseURL + popularMoviesEndpoint + apiKey;
    private String topRatedMoviesURL = movieListBaseURL + topRatedMoviesEndpoint + apiKey;
    private String baseImageUrl = "http://image.tmdb.org/t/p/w185";
    private static final int MOVIE_LOADER_ID = 0;
    // Constants for logging and referring to a unique loader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FAV_MOVIE_LOADER_ID = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecycterView = findViewById(R.id.rv_movie);
        spinner = findViewById(R.id.spinner_sort_by);
        mMovieList = new ArrayList<>();
        mMovieAdapterRv = new MovieAdapterRV(getApplicationContext(), mMovieList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        mRecycterView.setLayoutManager(layoutManager);
        mRecycterView.setAdapter(mMovieAdapterRv);
        // Initialize the favourite movie adapter
        mAdapterFavM = new CostumCursorAdapterFavM(getApplicationContext(), mMovieList);
        mRecycterView.setHasFixedSize(true);
        parceJson(popularMoviesURL);
        setupSort();

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
            }
        }).attachToRecyclerView(mRecycterView);

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */

        getSupportLoaderManager().initLoader(FAV_MOVIE_LOADER_ID, null, this);
    }

    private void parceJson(String chosenURL) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, chosenURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray moviesResultsJasonArray = response.getJSONArray("results");
                            for (int i = 0; i < moviesResultsJasonArray.length(); i++) {
                                JSONObject movieResult = moviesResultsJasonArray.getJSONObject(i);
                                MovieUtils movieItem = new MovieUtils();
                                movieItem.setMoviePosterUrl(baseImageUrl + movieResult.getString("poster_path"));
                                movieItem.setId(movieResult.getInt("id"));
                                movieItem.setOriginalTitle(movieResult.getString("original_title"));
                                movieItem.setSynopsis(movieResult.getString("overview"));
                                movieItem.setReleaseDate(movieResult.getString("release_date"));
                                movieItem.setUserRating(movieResult.getDouble("vote_average"));
                                mMovieList.add(movieItem);
                            }
                            mMovieAdapterRv = new MovieAdapterRV(MainActivity.this, mMovieList);
                            mRecycterView.setAdapter(mMovieAdapterRv);
                            mMovieAdapterRv.notifyDataSetChanged();
                            mMovieAdapterRv.setOnItemClickListener(MainActivity.this);
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
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(request);
    }

    public void setupSort() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.range_by,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                adapterView.getItemAtPosition(position);
                clearList();
                switch (position) {
                    case 0:
                        parceJson(popularMoviesURL);
                        mMovieAdapterRv.notifyDataSetChanged();
                        break;


                    case 1:
                        parceJson(topRatedMoviesURL);
                        mMovieAdapterRv.notifyDataSetChanged();
                        break;

                    case 2:
                        mRecycterView.setAdapter(mAdapterFavM);
                        getSupportLoaderManager().initLoader(FAV_MOVIE_LOADER_ID, null, MainActivity.this);
                        mAdapterFavM.notifyDataSetChanged();
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void clearList() {
        mMovieList.clear();
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, com.example.work_pana.popularmoviesstage_one.DetailActivity.class);
        detailIntent.putExtra(EXTRA_MOVIE, mMovieList.get(position));
        startActivity(detailIntent);

    }

    ///////////////////////////////////////// L O A D E R ///////////////////
    /**
     * This method is called after this activity has been paused or restarted.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(FAV_MOVIE_LOADER_ID, null, this);
    }


    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return movie data as a Cursor or null if an error occurs.
     *
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the movie data
            Cursor mFavMovieData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mFavMovieData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mFavMovieData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                try {
                    return getContentResolver().query(FavMovieDbContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            FavMovieDbContract.MovieEntry._ID);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mFavMovieData = data;
                super.deliverResult(data);
            }
        };

    }


    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapterFavM.swapCursor(data);
    }


    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapterFavM.swapCursor(null);
    }

}