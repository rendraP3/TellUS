package com.dotdevs.tellus;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class TellUS extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
