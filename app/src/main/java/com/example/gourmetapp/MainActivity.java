package com.example.gourmetapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("xxx" , "Main OnCreate called");

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        openFragment(new LoginFragment());
        Log.i("xxx" , "Main OnCreate opened Login Fragment");


    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
        moveTaskToBack(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("xxx" , "Main onPause called");
//        finish();
    }

    @Override
    protected void onResume() {
        Log.i("xxx" , "Main OnResume called ");
        super.onResume();


    }
}