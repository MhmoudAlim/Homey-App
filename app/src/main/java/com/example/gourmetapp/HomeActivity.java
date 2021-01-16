package com.example.gourmetapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity  {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.app_name);

        preferences = getSharedPreferences("name", MODE_PRIVATE);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

////
        androidx.work.Constraints.Builder conBuilder=new androidx.work.Constraints.Builder();
        conBuilder.setRequiredNetworkType(NetworkType.CONNECTED);

        Constraints constraints = conBuilder.build();

        PeriodicWorkRequest.Builder workBuilder=new PeriodicWorkRequest.Builder(MyWork.class,6, TimeUnit.HOURS);

        workBuilder.setConstraints(constraints);
        PeriodicWorkRequest request = workBuilder.build();
        WorkManager.getInstance(this).enqueue(request);

        ///////
        Log.i("xxx", "Home Activity OnCreate Started");

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHost.getNavController();
        NavigationUI.setupWithNavController(navView, navController);
        NavInflater navInflater = navController.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);

        int intentFragment = getIntent().getExtras().getInt("Fragment");
        if (intentFragment == 1) {
            graph.setStartDestination(R.id.navigation_cart);
        } else {
            graph.setStartDestination(R.id.navigation_home);
        }
        navController.setGraph(graph);


        BackendlessUser currentUser = Backendless.UserService.CurrentUser();
        if (currentUser != null) {
            String userName = (String) currentUser.getProperty("name");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name" , userName);
            editor.apply();

            Log.i("xxx", "Home Activity user : " + userName);
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
//        finish();
    }

    @Override
    protected void onPause() {
        Log.i("xxx", "Home Activity onPause called");
        super.onPause();
//         finish();
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.i("xxx", "Home Activity onStop called");

    }



}