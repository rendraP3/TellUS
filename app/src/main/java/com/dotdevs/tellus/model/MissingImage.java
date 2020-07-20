package com.dotdevs.tellus.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MissingImage implements Parcelable {

    private String name;
    private String location;

    public MissingImage() {
    }

    public MissingImage(String name, String location) {
        this.name = name;
        this.location = location;
    }

    protected MissingImage(Parcel in) {
        name = in.readString();
        location = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(location);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MissingImage> CREATOR = new Creator<MissingImage>() {
        @Override
        public MissingImage createFromParcel(Parcel in) {
            return new MissingImage(in);
        }

        @Override
        public MissingImage[] newArray(int size) {
            return new MissingImage[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
