package com.example.gourmetapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Flow;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.example.gourmetapp.ui.typeactivity;
import com.example.kloadingspin.KLoadingSpin;
import com.squareup.picasso.Picasso;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;

public class HistoryActivity extends AppCompatActivity {
 ListView lv_history;
    TextView cm_HeightHIstory,cm_WidthHIstory,cm_TitleHistory,cm_DateHistory,cm_HistoryState;
    ImageView cm_imageHistory;
    List<Order> orders;
    KLoadingSpin progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("My History");
        setContentView(R.layout.activity_history);
         lv_history=findViewById(R.id.lv_History);
         Flow noHistory =findViewById(R.id.noHistory);
         noHistory.setVisibility(View.VISIBLE);
        progress = findViewById(R.id.KLoadingSpin);
        progress.startAnimation();
        progress.setIsVisible(true);

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize( 100 );
        Backendless.Data.of(Order.class).find ( queryBuilder, new AsyncCallback<List<Order>>() {
            @Override
            public void handleResponse(List<Order> response) {
                lv_history.setVisibility(View.VISIBLE);
                progress.stopAnimation();
                progress.setIsVisible(false);
                progress.setVisibility(View.GONE);
                orders = response.stream().sorted(Comparator.comparing(order -> order.created)).collect(Collectors.toList());
                historyadapter adapter=new historyadapter(HistoryActivity.this,orders);
                if (adapter.getCount()> 0 )    noHistory.setVisibility(View.GONE);
                lv_history.setAdapter(adapter);
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                ImageView errorImage = findViewById(R.id.errorImage);
                noHistory.setVisibility(View.GONE);
                progress.stopAnimation();
                progress.setIsVisible(false);
                progress.setVisibility(View.GONE);
                errorImage.setVisibility(View.VISIBLE);
            }
        });
    }
    class historyadapter extends ArrayAdapter<Order> {
        public historyadapter(@NonNull Context context,  List<Order> orders) {
            super(context,0,orders);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.cm_historyactivty, parent, false);
            cm_DateHistory = convertView.findViewById(R.id.cm_dateHistory);
            cm_TitleHistory = convertView.findViewById(R.id.cm_titleHistory);
            cm_HeightHIstory = convertView.findViewById(R.id.cm_HeightHistory);
            cm_WidthHIstory = convertView.findViewById(R.id.cm_WidthHistory);
            cm_imageHistory = convertView.findViewById(R.id.cm_imageHistory);
            cm_HistoryState=convertView.findViewById(R.id.cm_HistoryState);


            cm_TitleHistory.setText(getItem(position).title);
            cm_DateHistory.setText(getItem(position).created);
            cm_HeightHIstory.setText(getItem(position).height+"");
            cm_WidthHIstory.setText(getItem(position).width+"");
            cm_HistoryState.setText(getItem(position).state);
            Picasso.get().load(getItem(position).url).resize(150,150).into(cm_imageHistory);

            return convertView;

        }
    }
}