package com.example.gourmetapp;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class itemDetailsActivity extends AppCompatActivity {
    TextView titledetailitem,descdetailitem;
    ImageView imagedetailitem;
    EditText widthdetailitem,hieghtdetailitem;
    Showitem showitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_details);
        titledetailitem=findViewById(R.id.titledetailItem);
        descdetailitem=findViewById(R.id.descdetailItem);
        imagedetailitem=findViewById(R.id.imagedetailItem);
        hieghtdetailitem=findViewById(R.id.heightdetailItem);
        widthdetailitem=findViewById(R.id.widthdetailItem);

        showitem = (Showitem) getIntent().getSerializableExtra("showitem");
        titledetailitem.setText(showitem.getTitle());
        descdetailitem.setText(showitem.getDesc());
        Picasso.get().load(showitem.getImageUrl()).into(imagedetailitem);
    }

    public void addtocart(View view) {

        if (widthdetailitem.getText().length()==0||hieghtdetailitem.getText().length()==0 ||
                Integer.parseInt(widthdetailitem.getText().toString())==0
                || Integer.parseInt(hieghtdetailitem.getText().toString())==0){
            Toasty.info(this, "please enter Valid Width and Height", Toast.LENGTH_SHORT, true).show();

        }else {
            Order order = new Order();
            order.title = showitem.getTitle();
            order.code = showitem.getCode();
            order.height = Integer.parseInt(hieghtdetailitem.getText().toString());
            order.width = Integer.parseInt(widthdetailitem.getText().toString());
            order.url = showitem.getImageUrl();
            long additem = marmoushdatabase.getInstance(this).orderDAo().additem(order);
            if (additem > 0) {
                final FlatDialog flatDialog = new FlatDialog(this);
                flatDialog.setTitle("Item added to your cart")
                        .setFirstButtonText("Go to Cart")
                        .setSecondButtonText("Continue shopping")
                        .setBackgroundColor(R.color.design_default_color_background)
                        .withFirstButtonListner(v -> {
                            Intent in=new Intent(itemDetailsActivity.this, HomeActivity.class);
                            in.putExtra("Fragment", 1);
                            startActivity(in);
                            flatDialog.dismiss();
                            finish();
                        })
                        .withSecondButtonListner(v -> {
                            Intent in=new Intent(itemDetailsActivity.this, HomeActivity.class);
                            in.putExtra("Fragment", 2);
                            startActivity(in);
                            flatDialog.dismiss();
                            finish();

                        })
                        .isCancelable(true)
                        .show();

            }
        }
    }


}