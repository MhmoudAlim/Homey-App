package com.example.gourmetapp.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;


import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.gourmetapp.R;
import com.example.gourmetapp.Showitem;

import java.util.List;

public class CategoryFragment extends Fragment {

    ImageView receptionsImage,corridorsImage,bedroomsImage,kitchensImage,bathroomsImage,UnitsAndLibraryImage,livingroomsImage;
    TextView bedRoomsTV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categroy, container, false);
        receptionsImage=view.findViewById(R.id.ImageReceptions);
        corridorsImage=view.findViewById(R.id.imageCorridors);
        bedroomsImage=view.findViewById(R.id.imageBedrooms);
        kitchensImage=view.findViewById(R.id.imageKitchens);
        bathroomsImage=view.findViewById(R.id.imageBathrooms);
        UnitsAndLibraryImage=view.findViewById(R.id.imageUnitsAndLibrary);
        livingroomsImage=view.findViewById(R.id.imagelivingrooms);
        bedRoomsTV=view.findViewById(R.id.textView3);

        receptionsImage.setImageResource(R.drawable.reception);
        corridorsImage.setImageResource(R.drawable.coridor);
        bedroomsImage.setImageResource(R.drawable.bedroom);
        kitchensImage.setImageResource(R.drawable.kitchen);
        bathroomsImage.setImageResource(R.drawable.bathroom);
        UnitsAndLibraryImage.setImageResource(R.drawable.tvunits);
        livingroomsImage.setImageResource(R.drawable.living);

        receptionsImage.setOnClickListener(v -> {
            Intent in=new Intent(getActivity(),typeactivity.class);
            in.putExtra("type","Receptions");
            startActivity(in);
        });

        bedroomsImage.setOnClickListener(v -> {
            Intent in=new Intent(getActivity(),typeactivity.class);
            in.putExtra("type","Bedrooms");
            startActivity(in);
        });

        kitchensImage.setOnClickListener(v -> {
            Intent in=new Intent(getActivity(),typeactivity.class);
            in.putExtra("type","Kitchens");
            startActivity(in);
        });

        bathroomsImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(),typeactivity.class);
                in.putExtra("type","Bathrooms");
                startActivity(in);
            }
        });


        UnitsAndLibraryImage.setOnClickListener(v -> {
            Intent in=new Intent(getActivity(),typeactivity.class);
            in.putExtra("type","UnitsAndLibrarys");
            startActivity(in);
        });

        livingroomsImage.setOnClickListener(v -> {
            Intent in=new Intent(getActivity(),typeactivity.class);
            in.putExtra("type","Livingrooms");
            startActivity(in);
        });

        corridorsImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getActivity(),typeactivity.class);
                in.putExtra("type","Corridors");
                startActivity(in);
            }
        });

        return view;
    }



}