package com.dotdevs.tellus.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class People {
    private String uid;
    private String uidReporter;
    private String name;
    private String age;
    private String gender;
    private String religion;
    private String address;
    private String lastLocation;
    private String description;
    private String imageUrl;
    private String date;
    private boolean isReported;
    private boolean isActive;
    private boolean isFound;
    @ServerTimestamp
    private Timestamp timeStamp;

    public People() {
    }

    public People(String uid, String uidReporter, String name, String age, String gender,
                  String religion, String address, String lastLocation, String description,
                  String imageUrl, String date, boolean isReported, boolean isActive,
                  boolean isFound,
                  Timestamp timeStamp) {
        this.uid = uid;
        this.uidReporter = uidReporter;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.religion = religion;
        this.address = address;
        this.lastLocation = lastLocation;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.isReported = isReported;
        this.isActive = isActive;
        this.isFound = isFound;
        this.timeStamp = timeStamp;
    }

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

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

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
