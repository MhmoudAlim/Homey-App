package com.example.gourmetapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.fragment.app.Fragment;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.example.gourmetapp.HistoryActivity;
import com.example.gourmetapp.HomeActivity;
import com.example.gourmetapp.MainActivity;
import com.example.gourmetapp.R;
import com.example.gourmetapp.itemDetailsActivity;
import com.example.kloadingspin.KLoadingSpin;

import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.mateware.snacky.Snacky;
import es.dmoral.toasty.Toasty;


public class MyProfileFragment extends Fragment {

    TextView currentUser_tv;
    BackendlessUser currentUser;
    SharedPreferences preferences;
    KLoadingSpin progress;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.i("xxx", "Profile fragment On CreatView called");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        preferences = this.getActivity().getSharedPreferences("name", Context.MODE_PRIVATE);
        currentUser_tv = view.findViewById(R.id.currentUser_tv);
        final Button logOut_btn = view.findViewById(R.id.logOut_btn);
        final Button history_btn = view.findViewById(R.id.history_btn);
        final Button Contact_Us = view.findViewById(R.id.Contact_Us);
        Button test1 = view.findViewById(R.id.test1);
        Button test2 = view.findViewById(R.id.test2);
        Button test3 = view.findViewById(R.id.test3);

        progress = view.findViewById(R.id.KLoadingSpin);


        test1.setOnClickListener(v -> Toasty.info(getActivity(), "It's a test App, don't expect much xD", Toast.LENGTH_SHORT, true).show());
        test2.setOnClickListener(v -> Toasty.info(getActivity(), "It's a test App, don't expect much xD", Toast.LENGTH_SHORT, true).show());
        test3.setOnClickListener(v -> Toasty.info(getActivity(), "It's a test App, don't expect much xD", Toast.LENGTH_SHORT, true).show());


        logOut_btn.setOnClickListener(v -> {
            final FlatDialog flatDialog = new FlatDialog(getActivity());
            flatDialog.setTitle("Are you sure you want to Logout?")
                    .setFirstButtonText("Yes")
                    .setSecondButtonText("No")
                    .setBackgroundColor(R.color.design_default_color_background)
                    .withFirstButtonListner(v12 -> {
                        flatDialog.dismiss();
                        progress.setVisibility(View.VISIBLE);
                        progress.startAnimation();
                        progress.setIsVisible(true);
                        logOut_btn.setVisibility(View.GONE);
                        history_btn.setVisibility(View.GONE);
                        Contact_Us.setVisibility(View.GONE);
                        test1.setVisibility(View.GONE);
                        test2.setVisibility(View.GONE);
                        test3.setVisibility(View.GONE);

                        Backendless.UserService.logout(new AsyncCallback<Void>() {
                            @SuppressLint("CommitPrefEdits")
                            @Override
                            public void handleResponse(Void response) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                preferences.edit().clear();
                                Toasty.success(getActivity(), "You Logged out Successfully", Toast.LENGTH_SHORT, true).show();
                                try {
                                    Thread.sleep(400);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                getActivity().finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                progress.setVisibility(View.GONE);
                                progress.stopAnimation();
                                logOut_btn.setVisibility(View.VISIBLE);
                                history_btn.setVisibility(View.VISIBLE);
                                Contact_Us.setVisibility(View.VISIBLE);
                                test1.setVisibility(View.VISIBLE);
                                test2.setVisibility(View.VISIBLE);
                                test3.setVisibility(View.VISIBLE);
                                Snacky.builder().setActivity(getActivity())
                                        .setDuration(Snacky.LENGTH_LONG)
                                        .setActionText("Wifi Setting").setActionClickListener(v1 -> {
                                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); })
                                        .setText("please check your internet connection!")
                                        .info().show();
                            }
                        });
                    })
                    .withSecondButtonListner(v1 -> flatDialog.dismiss())
                    .isCancelable(true)
                    .show();
        });

        Contact_Us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FlatDialog flatDialog = new FlatDialog(getActivity());

                flatDialog.setTitle("This is a Project App\n Developed by :")
                        .setFirstButtonText("Mahmoud Hussein")
                        .setSecondButtonText("Contact me")
                        .setSecondButtonColor(R.color.teal_200)
                        .withSecondButtonListner(v15 -> {
                            flatDialog.dismiss();
                            Snacky.builder().setActivity(getActivity())
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .setText("Let's have a Chat" )
                                    .setActionText("Email me")
                                    .setActionClickListener(new View.OnClickListener() {
                                        @SuppressLint("IntentReset")
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                                            intent.setType("*/*");
                                            intent.setData(Uri.parse("mailto:"+"MahmoudHusseinAlim@gmail.com"));
                                            intent.putExtra(Intent.EXTRA_EMAIL, "MahmoudHusseinAlim@gmail.com");
                                            if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                                                startActivity(intent);
                                        }
                                    })
                                    .info().show();
                        }).isCancelable(true)
                        .show();
            }
        });

            history_btn.setOnClickListener(v -> {
                Intent in = new Intent(getActivity(), HistoryActivity.class);
                    startActivity(in);
            });



        currentUser = Backendless.UserService.CurrentUser();
        MessageEvent m;
        m = new MessageEvent();
        if (currentUser != null) {
            String userName = (String) currentUser.getProperty("name");
            Log.i("xxx", "user : " + userName);
            currentUser_tv.setText(userName);
            m.name = userName;
            EventBus.getDefault().postSticky(m);
            Log.i("xxx", m.name + "  : name sent to  Eventbus");
            currentUser_tv.setText(userName);
        } else {
            String name = preferences.getString("name", "");
            currentUser_tv.setText(name);
            Log.i("xxx", "username from shared prefrence" + name);
        }
        return view;
    }




    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent m) {
        currentUser_tv.setText(m.name);
        Log.i("xxx", currentUser_tv.getText() + " name form the Eventbus");
    }

    public static class MessageEvent {
        public String name;

    }

    @Override
    public void onPause() {
        Log.i("xxx", "Profile fragment On pause called");
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("xxx", "Profile fragment On Resumed called");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.i("xxx", "Profile fragment On Stop called");

    }
}