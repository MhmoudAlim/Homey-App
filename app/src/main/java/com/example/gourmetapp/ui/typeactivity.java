package com.example.gourmetapp.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;


import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.gourmetapp.R;
import com.example.gourmetapp.Showitem;
import com.example.gourmetapp.itemDetailsActivity;
import com.example.kloadingspin.KLoadingSpin;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.Inflater;

import es.dmoral.toasty.Toasty;

public class typeactivity extends AppCompatActivity {
    ListView lv_types;
    TextView cm_title, cm_desc;
    ImageView cm_image;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    String type;
    Showitem showitem;
    private List<Showitem> responsee;
    myadapter adapter;
    KLoadingSpin progress;
    String finalDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeactivity);

        progress = findViewById(R.id.KLoadingSpin);
        progress.startAnimation();
        progress.setIsVisible(true);
        lv_types = findViewById(R.id.lv_types);
        type = getIntent().getStringExtra("type");
        Objects.requireNonNull(getSupportActionBar()).setTitle(type);
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause("title='" + type + "'");
        queryBuilder.setPageSize(100);
        Backendless.Data.of(Showitem.class).find(queryBuilder, new AsyncCallback<List<Showitem>>() {

            @Override
            public void handleResponse(List<Showitem> response) {
                responsee = response;
                progress.stopAnimation();
                progress.setIsVisible(false);
                progress.setVisibility(View.GONE);
                lv_types.setVisibility(View.VISIBLE);
                adapter = new myadapter(typeactivity.this, response);
                if (adapter.getCount() == 0)
                    Toasty.error(typeactivity.this, "Nothing to show", Toast.LENGTH_SHORT, true).show();
                else {
                    lv_types.setAdapter(adapter);
                    lv_types.setOnItemClickListener((parent, view, position, id) -> {
                        showitem = response.get(position);
                        Intent in = new Intent(typeactivity.this, itemDetailsActivity.class);
                        in.putExtra("showitem", showitem);
                        startActivity(in);


                });
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                progress.stopAnimation();
                progress.setIsVisible(false);
                progress.setVisibility(View.GONE);
                Toasty.error(typeactivity.this, "Network Error", Toast.LENGTH_SHORT, true).show();
            }
        });

    }

    class myadapter extends ArrayAdapter<Showitem> {
        public myadapter(@NonNull Context context, List<Showitem> response) {
            super(context, 0, response);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.cm_type, parent, false);

            cm_title = convertView.findViewById(R.id.cm_title);
            cm_desc = convertView.findViewById(R.id.cm_decr);
            cm_image = convertView.findViewById(R.id.cm_image);

            cm_title.setText(getItem(position).getTitle());
            String desc = getItem(position).getDesc();
            if (desc.length() > 50)
                cm_desc.setText(getItem(position).getDesc().substring(0,50) + " ......");
            else
                cm_desc.setText(getItem(position).getDesc());


            Picasso.get().load(getItem(position).getImageUrl()).resize(150, 150).into(cm_image);

            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("search for " + type + "s");
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("xxx", newText);
                    List<Showitem> searchList = responsee.stream().filter(x -> x.getDesc().contains(newText)).collect(Collectors.toList());
                    adapter = new myadapter(typeactivity.this, searchList);
                    lv_types.setAdapter(adapter);
                    lv_types.setOnItemClickListener((parent, view, position, id) -> {
                        showitem = searchList.get(position);
                        Intent in = new Intent(typeactivity.this, itemDetailsActivity.class);
                        in.putExtra("showitem", showitem);
                        startActivity(in);
                        adapter.notifyDataSetChanged();
                    });

                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("xxx", query);

                    List<Showitem> searchList = responsee.stream().filter(x -> x.getDesc().contains(query)).collect(Collectors.toList());
                    adapter = new myadapter(typeactivity.this, searchList);
                    lv_types.setAdapter(adapter);
                    lv_types.setOnItemClickListener((parent, view, position, id) -> {
                        showitem = searchList.get(position);
                        Intent in = new Intent(typeactivity.this, itemDetailsActivity.class);
                        in.putExtra("showitem", showitem);
                        startActivity(in);
                        adapter.notifyDataSetChanged();

                    });
                    adapter.notifyDataSetChanged();

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);

        }


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
}