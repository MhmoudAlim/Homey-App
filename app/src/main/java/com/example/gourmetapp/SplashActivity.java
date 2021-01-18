package com.example.gourmetapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {
    int splashTimeOut = 1800;
    private LottieAnimationView lott;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("xxx", "Splash OnCreate");
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        lott = findViewById(R.id.lott);
        final TextView textView = findViewById(R.id.textView2);
        Backendless.initApp(this,"3862B5B1-0214-8D05-FF32-C64330288A00","22318AF7-72DE-40C8-BD09-067224AA07C2");
        new Handler().postDelayed(() -> {
            Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
                @Override
                public void handleResponse(Boolean response) {
                    Log.i("xxx", " valid Logged is  : " + response.toString());
                    if (response) {
                        final Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        Log.i("xxx", "Splash >> user is already logged");
                        intent.putExtra("Fragment", 2);
                        startActivity(intent);
                        Log.i("xxx", "Splash >> to  Home Activity");


                    } else {
                        Log.i("xxx", "splash >> user is not logged");
                        final Intent intent2 = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent2);
                        Log.i("xxx", "Splash >> to  Main Activity");

                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    final Intent intent2 = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent2);
                    Log.i("xxx", fault.getCode());

                }
            });

        }, splashTimeOut);
    }

    @Override
    protected void onResume() {
        Log.i("xxx", "Splash OnRsume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}