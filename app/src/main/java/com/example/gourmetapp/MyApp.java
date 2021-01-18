package com.example.gourmetapp;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.backendless.Backendless;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.initApp(this,"3862B5B1-0214-8D05-FF32-C64330288A00","22318AF7-72DE-40C8-BD09-067224AA07C2");
    }

}
