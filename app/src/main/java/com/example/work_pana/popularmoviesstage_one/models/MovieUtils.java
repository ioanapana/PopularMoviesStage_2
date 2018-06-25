package com.example.work_pana.popularmoviesstage_one.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieUtils implements Parcelable {
    private String mMoviePosterUrl;
    private String mOriginalTitle;
    private String mSynopsis;
    private double mUserRating;
    private String mReleaseDate;
    private int mId;

    public MovieUtils(String moviePosterUrl, String originalTitle, String synopsis, double userRating, String releaseDate, int id) {
        this.mMoviePosterUrl = moviePosterUrl;
        this.mOriginalTitle = originalTitle;
        this.mSynopsis = synopsis;
        this.mUserRating = userRating;
        this.mReleaseDate = releaseDate;
        this.mId = id;
    }

    public MovieUtils() {

    }

    protected MovieUtils(Parcel in) {
        mMoviePosterUrl = in.readString();
        mOriginalTitle = in.readString();
        mSynopsis = in.readString();
        mUserRating = in.readDouble();
        mReleaseDate = in.readString();
        mId = in.readInt();
    }

    public static final Creator<MovieUtils> CREATOR = new Creator<MovieUtils>() {
        @Override
        public MovieUtils createFromParcel(Parcel in) {
            return new MovieUtils(in);
        }

        @Override
        public MovieUtils[] newArray(int size) {
            return new MovieUtils[size];
        }
    };

    public int getId() {
        return mId;
    }

    public String getMoviePosterUrl() {
        return mMoviePosterUrl;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public double getUserRating() {
        return mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.mMoviePosterUrl = moviePosterUrl;
    }

    public void setOriginalTitle(String originalTitle) {
        this.mOriginalTitle = originalTitle;
    }

    public void setSynopsis(String synopsis) {
        this.mSynopsis = synopsis;
    }

    public void setUserRating(double userRating) {
        this.mUserRating = userRating;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMoviePosterUrl);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mSynopsis);
        parcel.writeDouble(mUserRating);
        parcel.writeString(mReleaseDate);
        parcel.writeInt(mId);
    }

//    public void setTag(int tag) {
//        this.tag = tag;
//    }
}

