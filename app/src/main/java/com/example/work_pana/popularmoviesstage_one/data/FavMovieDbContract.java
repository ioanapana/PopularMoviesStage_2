package com.example.work_pana.popularmoviesstage_one.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavMovieDbContract {

    // The authority, which is how your code knows which Content Provider to access
    public final static String CONTENT_AUTHORITY = "com.example.work_pana.popularmoviesstage_one";

    // The base content URI = "content://" + <authority>
    public final static String BASE_CONTENT_URI = "content://" + CONTENT_AUTHORITY;

    // This is the path for the "movies" directory
    public final static String PATH_MOVIES = "movies";

    /* MovieEntry is an inner class that defines the contents of the task table */
    public static class MovieEntry implements BaseColumns {

        // MovieEntry content URI = base content URI + path
        public final static Uri CONTENT_URI =
                Uri.parse(BASE_CONTENT_URI)
                        .buildUpon()
                        .appendPath(PATH_MOVIES)
                        .build();

        // Table name
        public final static String TABLE_NAME = "favourite_movies";

        // Unique ID number for the movie (only for use in the database table).

        public final static String _ID = BaseColumns._ID;

        // Columns name
        public final static String COLUMN_MOVIE_ID = "movie_id";
        public final static String COLUMN_POSTER_URL = "poster_url";
        public final static String COLUMN_TITLE = "name";
        public final static String COLUMN_OVERVIEW = "overview";
        public final static String COLUMN_RELEASE_DATE = "release_date";
        public final static String COLUMN_RATING = "rating";
    }
}