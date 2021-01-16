package com.example.gourmetapp;


import android.content.Context;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.flatdialoglibrary.dialog.FlatDialog;

import es.dmoral.toasty.Toasty;


public class NewUserFragment extends Fragment implements AsyncCallback<BackendlessUser> {

   private EditText newUserMail_ET, newUserName_ET, newUSerPass_ET;
    private Button register;
    private TextView alreadyUSer_tv;
    private String name, email, password;

    public NewUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_user, container, false);
        newUserMail_ET = view.findViewById(R.id.newUserMail_ET);
        newUserName_ET = view.findViewById(R.id.newUserName_ET);
        newUSerPass_ET = view.findViewById(R.id.newUSerPass_ET);
        alreadyUSer_tv = view.findViewById(R.id.alreadyUSer_tv);
       EditText editTextPhone = view.findViewById(R.id.editTextPhone);
        EditText editTextAddress = view.findViewById(R.id.editTextAddress);

        register = view.findViewById(R.id.register_btn);
        Log.i("xxx", "fragment new user opened");


        register.setOnClickListener(v -> {
            closeKeyBoard();
            String nameText = newUserName_ET.getText().toString().trim();
            String emailText = newUserMail_ET.getText().toString().trim();
            String passwordText = newUSerPass_ET.getText().toString().trim();
            String Phone = editTextPhone.getText().toString().trim();
            String Address = editTextAddress.getText().toString().trim();


            if (emailText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                newUserMail_ET.setError("please enter a a valid Email");
                YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(view.findViewById(R.id.newUserMail_ET));
                return;
            } else
                email = emailText;

            if (passwordText.isEmpty() ||  passwordText.length() < 4 || passwordText.length() > 7) {
                newUSerPass_ET.setError("password must be between 4 & 7 characters");
                YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(view.findViewById(R.id.newUSerPass_ET));                return;
            } else
                password = passwordText;

            if (nameText.isEmpty()) {
                newUserName_ET.setError("Name can't be Empty");
                YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(view.findViewById(R.id.newUserName_ET));
                return;
            }else{
                name = nameText;
            }

            BackendlessUser user = new BackendlessUser();
            if (email != null) user.setEmail(email);
            if (password != null) user.setPassword(password);
            if (name != null) user.setProperty("name", name);
            user.setProperty("phone" ,Phone );
            user.setProperty("Adress" , Address);

            Log.i("xxx", "name" + user.getProperties());

            Backendless.UserService.register(user, this);

        });

        alreadyUSer_tv.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, new LoginFragment()).commit();
            Log.i("xxx", "fragment new user to open");
        });


        return view;
    }


    @Override
    public void handleResponse(BackendlessUser response) {
        final FlatDialog flatDialog = new FlatDialog(getActivity());
        flatDialog.setTitle("Registered Successfully")
                .setFirstButtonText("Login now")
                .setSecondButtonText("CANCEL")
                .setBackgroundColor(R.color.design_default_color_background)
                .withFirstButtonListner(view -> {
                    assert NewUserFragment.this.getFragmentManager() != null;
                    FragmentTransaction transaction = NewUserFragment.this.getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayout, new LoginFragment()).commit();
                    flatDialog.cancel();
                })
                .withSecondButtonListner(view -> flatDialog.dismiss())
                .isCancelable(true)
                .show();

    }

    @Override
    public void handleFault(BackendlessFault fault) {
        final FlatDialog flatDialog;
        flatDialog = new FlatDialog(getActivity());
        if (fault.getCode().equals("3033")) {
            flatDialog.setTitle("user Already exists")
                    .setFirstButtonText("Login now")
                    .setSecondButtonText("Try Again!")
                    .setBackgroundColor(R.color.design_default_color_background)
                    .withFirstButtonListner(view -> {
                        assert NewUserFragment.this.getFragmentManager() != null;
                        FragmentTransaction transaction = NewUserFragment.this.getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameLayout, new LoginFragment()).commit();
                        flatDialog.dismiss();
                    })
                    .withSecondButtonListner(v -> flatDialog.dismiss())
                    .isCancelable(true)
                    .show();
        } else
            Toasty.error(getActivity(), "Failed to Register,Please Try Again!", Toast.LENGTH_SHORT, true).show();

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