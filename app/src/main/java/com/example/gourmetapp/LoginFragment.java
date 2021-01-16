package com.example.gourmetapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.daimajia.androidanimations.library.attention.FlashAnimator;
import com.example.kloadingspin.KLoadingSpin;
import com.google.android.material.snackbar.Snackbar;

import de.mateware.snacky.Snacky;


public class LoginFragment extends Fragment {

    EditText username_ET, password_ET;
    TextView newUser_TV, Guest_tv;
    Button Login_bt;
    private String password, username;
    boolean rememberLogin;
    KLoadingSpin progress;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        username_ET = view.findViewById(R.id.userName_ET);
        password_ET = view.findViewById(R.id.password_ET);
        newUser_TV = view.findViewById(R.id.newuser_TV);
        Login_bt = view.findViewById(R.id.login_bt);
        progress = view.findViewById(R.id.KLoadingSpin2);

        Guest_tv = view.findViewById(R.id.guest_TV);
        CheckBox rememberLoginBox = view.findViewById(R.id.rememberLoginBox);
        Log.i("xxx", "Login Fragment onCreate View called");


        newUser_TV.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, new NewUserFragment()).commit();
            Log.i("xxx", "fragment new user to open");
        });


        Guest_tv.setOnClickListener(v -> {
            Snacky.builder().setActivity(getActivity())
                    .setDuration(Snacky.LENGTH_LONG)
                    .setText("This Feature is coming soon")
                    .info().show();
        });

        rememberLoginBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rememberLogin = true;
                Log.i("xxx", " isValid : " + rememberLogin);
            } else {
                rememberLogin = false;
                Log.i("xxx", " isValid : " + rememberLogin);

            }
        });

        Login_bt.setOnClickListener(v -> {
            closeKeyBoard();

            username = username_ET.getText().toString();
            password = password_ET.getText().toString();

            if (username.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                username_ET.setError("please enter a valid email");
                YoYo.with(Techniques.Tada).duration(700).repeat(1).playOn(view.findViewById(R.id.userName_ET));

            } else if (password.length() < 4 || password.length() > 7) {
                password_ET.setError("password must be between 4 & 7 chars");
                YoYo.with(Techniques.Tada).duration(700).repeat(1).playOn(view.findViewById(R.id.password_ET));

            } else {
                progress.setVisibility(View.VISIBLE);
                progress.startAnimation();
                progress.setIsVisible(true);
                Login_bt.setVisibility(View.GONE);
                Backendless.UserService.login(username, password, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        Login_bt.setVisibility(View.VISIBLE);
                        progress.stopAnimation();
                        Intent in = new Intent(getActivity(), HomeActivity.class);
                        in.putExtra("Fragment", 2);
                        startActivity(in);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Login_bt.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        progress.stopAnimation();
                        Log.i("xxx", fault.getCode().toLowerCase());
                        if (fault.getCode().equals("3003"))
                            Snacky.builder().setActivity(getActivity())
                                    .setDuration(Snacky.LENGTH_SHORT)
                                    .setText("invalid credentials!")
                                    .info().show();
                        else
                            Snacky.builder().setActivity(getActivity())
                                    .setDuration(Snacky.LENGTH_LONG)
                                    .setActionText("Wifi Setting").setActionClickListener(v1 -> {
                                          startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); })
                                    .setText("please check your internet connection!")
                                    .info().show();
                    }
                }, rememberLogin);
            }
        });


        return view;
    }

    private void closeKeyBoard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}