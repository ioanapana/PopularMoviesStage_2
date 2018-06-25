package com.example.work_pana.popularmoviesstage_one.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract;
import com.example.work_pana.popularmoviesstage_one.models.MovieUtils;

import static com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.CONTENT_URI;
import static java.lang.String.valueOf;

public class FavMoviesUtils extends ContentResolver {

    public FavMoviesUtils(Context context) {
        super(context);
    }


    public static ContentValues addMovieToFavoritesList(MovieUtils favoriteMovie) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_MOVIE_ID, favoriteMovie.getId());
        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_POSTER_URL, favoriteMovie.getMoviePosterUrl());
        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_TITLE, favoriteMovie.getOriginalTitle());
        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_OVERVIEW, favoriteMovie.getSynopsis());
        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_RELEASE_DATE, favoriteMovie.getReleaseDate());
        contentValues.put(com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.MovieEntry.COLUMN_RATING, favoriteMovie.getUserRating());
        return contentValues;
    }


    public static boolean isAlreadyFavorite(Context context, int movieId) {
        String selection = FavMovieDbContract.MovieEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = {""};
        boolean isFavorite = false;
        selectionArgs[0] = valueOf(movieId);
        Cursor cursor = context.getContentResolver().query(CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
        if (cursor.getCount() > 0) {
            isFavorite = true;
        } else {
            isFavorite = false;
        }
        return isFavorite;
    }

    public static int removeFromFavorite(Context context, int movieId) {
        Uri uri = FavMovieDbContract.MovieEntry.CONTENT_URI;
        Uri removeUri = uri.buildUpon().appendPath(valueOf(movieId)).build();
        return context.getContentResolver().delete(removeUri, null, null);
    }
}
