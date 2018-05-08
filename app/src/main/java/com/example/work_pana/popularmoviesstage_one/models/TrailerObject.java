package com.example.work_pana.popularmoviesstage_one.models;


import android.os.Parcel;
import android.os.Parcelable;

public class TrailerObject implements Parcelable {

        //This is the YouTube BASE for getting the trailer site
        private static final String BASE_YOUTUBE = "https://www.youtube.com/watch?v=";
        //This variable stores the key of the trailer
        private String mKeyTrailer;
        //This variable stores the name of the trailer
        private String mNameTrailer;
        //This variable stores the YouTube site for each trailer
        private String mSiteTrailer;

        public TrailerObject(String keyTrailer, String nameTrailer) {
            mKeyTrailer = keyTrailer;
            mNameTrailer = nameTrailer;
        }

        protected TrailerObject(Parcel in) {
            mKeyTrailer = in.readString();
            mNameTrailer = in.readString();
            mSiteTrailer = in.readString();
        }

    public static final Creator<TrailerObject> CREATOR = new Creator<TrailerObject>() {
        @Override
        public TrailerObject createFromParcel(Parcel in) {
            return new TrailerObject(in);
        }

        @Override
        public TrailerObject[] newArray(int size) {
            return new TrailerObject[size];
        }
    };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mKeyTrailer);
            dest.writeString(mNameTrailer);
            dest.writeString(mSiteTrailer);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public String getKeyTrailer() {
            return mKeyTrailer;
        }

        public void setKeyTrailer(String keyTrailer) {
            this.mKeyTrailer = keyTrailer;
        }

        public String getNameTrailer() {
            return mNameTrailer;
        }

        public void setNameTrailer(String nameTrailer) {
            this.mNameTrailer = nameTrailer;
        }

        public String getSiteTrailer() {
            mNameTrailer = BASE_YOUTUBE + getKeyTrailer();
            return mNameTrailer;
        }

        public void setSiteTrailer(String siteTrailer) {
            this.mSiteTrailer = siteTrailer;
        }
    }

