package com.dotdevs.tellus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

public class People implements Parcelable {
    private String uid;
    private String uidReporter;
    private String name;
    private String age;
    private String gender;
    private String phoneNumberReporter;
    private String address;
    private String lastLocation;
    private String description;
    private List<MissingImage> imagesUrl;
    private String date;
    private boolean isReported;
    private boolean isActive;
    private boolean isFound;
    private boolean isVerify;
    private double latitude;
    private double longitude;
    @ServerTimestamp
    private Timestamp timeStamp;

    public People() {
    }

    protected People(Parcel in) {
        uid = in.readString();
        uidReporter = in.readString();
        name = in.readString();
        age = in.readString();
        gender = in.readString();
        phoneNumberReporter = in.readString();
        address = in.readString();
        lastLocation = in.readString();
        description = in.readString();
        imagesUrl = in.createTypedArrayList(MissingImage.CREATOR);
        date = in.readString();
        isReported = in.readByte() != 0;
        isActive = in.readByte() != 0;
        isFound = in.readByte() != 0;
        isVerify = in.readByte() != 0;
        latitude = in.readDouble();
        longitude = in.readDouble();
        timeStamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(uidReporter);
        dest.writeString(name);
        dest.writeString(age);
        dest.writeString(gender);
        dest.writeString(phoneNumberReporter);
        dest.writeString(address);
        dest.writeString(lastLocation);
        dest.writeString(description);
        dest.writeTypedList(imagesUrl);
        dest.writeString(date);
        dest.writeByte((byte) (isReported ? 1 : 0));
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeByte((byte) (isFound ? 1 : 0));
        dest.writeByte((byte) (isVerify ? 1 : 0));
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeParcelable(timeStamp, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<People> CREATOR = new Creator<People>() {
        @Override
        public People createFromParcel(Parcel in) {
            return new People(in);
        }

        @Override
        public People[] newArray(int size) {
            return new People[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUidReporter() {
        return uidReporter;
    }

    public void setUidReporter(String uidReporter) {
        this.uidReporter = uidReporter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumberReporter() {
        return phoneNumberReporter;
    }

    public void setPhoneNumberReporter(String phoneNumberReporter) {
        this.phoneNumberReporter = phoneNumberReporter;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MissingImage> getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(List<MissingImage> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }

    public boolean isVerify() {
        return isVerify;
    }

    public void setVerify(boolean verify) {
        isVerify = verify;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public People(String uid, String uidReporter, String name, String age, String gender,
                  String phoneNumberReporter, String address, String lastLocation,
                  String description, List<MissingImage> imagesUrl, String date, boolean isReported,
                  boolean isActive, boolean isFound, boolean isVerify, double latitude,
                  double longitude, Timestamp timeStamp) {
        this.uid = uid;
        this.uidReporter = uidReporter;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phoneNumberReporter = phoneNumberReporter;
        this.address = address;
        this.lastLocation = lastLocation;
        this.description = description;
        this.imagesUrl = imagesUrl;
        this.date = date;
        this.isReported = isReported;
        this.isActive = isActive;
        this.isFound = isFound;
        this.isVerify = isVerify;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeStamp = timeStamp;
    }
}