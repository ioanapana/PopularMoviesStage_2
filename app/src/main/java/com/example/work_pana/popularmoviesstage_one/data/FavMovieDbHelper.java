package com.example.work_pana.popularmoviesstage_one.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.work_pana.popularmoviesstage_one.data.FavMovieDbContract.*;

public class FavMovieDbHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 2;

    public FavMovieDbHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS " + FavMovieDbContract.MovieEntry.TABLE_NAME + "(" +
                FavMovieDbContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavMovieDbContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                FavMovieDbContract.MovieEntry.COLUMN_POSTER_URL + " TEXT NOT NULL," +
                FavMovieDbContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                FavMovieDbContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                FavMovieDbContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                FavMovieDbContract.MovieEntry.COLUMN_RATING + " INTEGER NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavMovieDbContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
