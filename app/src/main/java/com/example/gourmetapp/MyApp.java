package com.example.gourmetapp;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.backendless.Backendless;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Backendless.initApp(this,"A2B83EC3-2382-E066-FFDA-43CCF14A8B00","CE6F3A5C-20CF-4277-AD8F-3A53F1BEC34E");    }


}
