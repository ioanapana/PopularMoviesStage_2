package com.example.work_pana.popularmoviesstage_one.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewObject implements Parcelable {
    //This variable stores the name of the author of the review
    private String mAuthor;
    //This variable stores the content of the review
    private String mContent;

    public ReviewObject(){

    }
    private ReviewObject(Parcel in) {
        mAuthor = in.readString();
        mContent = in.readString();
    }

    public static final Creator<ReviewObject> CREATOR = new Creator<ReviewObject>() {
        @Override
        public ReviewObject createFromParcel(Parcel in) {
            return new ReviewObject(in);
        }

        @Override
        public ReviewObject[] newArray(int size) {
            return new ReviewObject[size];
        }
    };

    public ReviewObject(String author, String content) {
        mAuthor = author;
        mContent = content;
    }


    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }
}