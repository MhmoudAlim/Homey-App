package com.example.gourmetapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.fragment.app.Fragment;


import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.gourmetapp.MainActivity;
import com.example.gourmetapp.MyOrder;
import com.example.gourmetapp.Order;
import com.example.gourmetapp.R;
import com.example.gourmetapp.marmoushdatabase;
import com.example.kloadingspin.KLoadingSpin;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.mateware.snacky.Snacky;
import es.dmoral.toasty.Toasty;

public class CartFragment extends Fragment {
    TextView cm_myCartHeight, cm_myCartWidth, Cm_myCartTitle;
    ImageView cm_myCartiamge, plusHeight, minusHeight, plusWidth, minusWidth;
    ListView lv_myCart;
    List<Order> myorders;
    myyadapter adapter;
    Flow removeitem;
    int heighta;
    int widtha;
    ExtendedFloatingActionButton completeOrder_btn;
    TextView emptyTV;
    ImageView emptyCart;
    KLoadingSpin progress;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        completeOrder_btn = view.findViewById(R.id.completeOrder_btn);
        lv_myCart = view.findViewById(R.id.lv_myCart);
        emptyCart = view.findViewById(R.id.emptyCart);
        emptyTV = view.findViewById(R.id.emptyTv);
        progress = view.findViewById(R.id.KLoadingSpin);

        //get order from room
        myorders = marmoushdatabase.getInstance(getActivity()).orderDAo().myorders();
        adapter = new myyadapter(getActivity(), myorders);
        lv_myCart.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (adapter.getCount() > 0) {
            emptyCart.setVisibility(View.GONE);
            emptyTV.setVisibility(View.GONE);
            lv_myCart.setVisibility(View.VISIBLE);
            completeOrder_btn.setVisibility(View.VISIBLE);
        }
        else if (adapter.getCount() == 0){
            emptyCart.setVisibility(View.VISIBLE);
            emptyTV.setVisibility(View.VISIBLE);
            lv_myCart.setVisibility(View.GONE);
            completeOrder_btn.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();

        completeOrder_btn.setOnClickListener(v -> {
            lv_myCart.setVisibility(View.GONE);
            completeOrder_btn.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            progress.startAnimation();
            progress.setIsVisible(true);
            Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
                @Override
                public void handleResponse(Boolean response) {
                    if (response) {
                        if (myorders.isEmpty())
                            Toasty.error(getActivity(), "NO Order to Submit", Toast.LENGTH_SHORT, true).show();
                        else {
                            Backendless.Data.of(Order.class).create(myorders, new AsyncCallback<List<String>>() {
                                @Override
                                public void handleResponse(List<String> response) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Order Submited")
                                            .setIcon(R.drawable.ic_done_all_24px)
                                            .setCancelable(false)
                                            .setPositiveButton("SKIP", (dialog, id) -> {
                                                //this for skip dialog
                                                dialog.cancel();
                                            });
                                    Toasty.success(getActivity(), "Order Submited", Toast.LENGTH_SHORT, true).show();
                                    for (Order myorder : myorders) {
                                        MyOrder myOrderlist=new MyOrder();
                                        myOrderlist.objectId=myorder.getObjectId();
                                        myOrderlist.state="Processing";
                                        myOrderlist.title=myorder.title;
                                        marmoushdatabase.getInstance(getActivity()).myorderDAo().addMyOrderItem(myOrderlist);

                                    }

                                    for (Order myorder : myorders) {
                                        marmoushdatabase.getInstance(getActivity()).orderDAo().deletorders(myorder);
                                    }
                                    myorders.clear();
                                    adapter.notifyDataSetChanged();
                                    progress.stopAnimation();
                                    progress.setIsVisible(false);
                                        emptyCart.setVisibility(View.VISIBLE);
                                        emptyTV.setVisibility(View.VISIBLE);
                                }
                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    progress.setVisibility(View.GONE);
                                    lv_myCart.setVisibility(View.VISIBLE);
                                    completeOrder_btn.setVisibility(View.VISIBLE);
                                    Log.i("wwww", fault.getCode());
                                    Snacky.builder().setActivity(getActivity())
                                            .setDuration(Snacky.LENGTH_LONG)
                                            .setActionText("Wifi Setting").setActionClickListener(v1 -> {
                                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); })
                                            .setText("please check your internet connection!")
                                            .info().show();
                                }
                            });
                        }
                    } else {
                        Intent in = new Intent(getActivity(), MainActivity.class);
                        startActivity(in);
                    }
                }
                @Override
                public void handleFault(BackendlessFault fault) {
                }
            });
            adapter.notifyDataSetChanged();
        });


        return view;
    }




    class myyadapter extends ArrayAdapter<Order> {
        public myyadapter(@NonNull Context context, List<Order> myorders) {
            super(context, 0, myorders);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.cm_cartactivity, parent, false);

            Cm_myCartTitle = convertView.findViewById(R.id.cm_TitleMycart);
            cm_myCartHeight = convertView.findViewById(R.id.cm_HeightMycart);
            cm_myCartWidth = convertView.findViewById(R.id.cm_WidthMycart);
            cm_myCartiamge = convertView.findViewById(R.id.cm_imageMycart);
            plusHeight = convertView.findViewById(R.id.PlusHeight);
            minusHeight = convertView.findViewById(R.id.minusHeight);
            plusWidth = convertView.findViewById(R.id.plusWidth);
            minusWidth = convertView.findViewById(R.id.minusWidth);
            removeitem = convertView.findViewById(R.id.removeItem);

            plusHeight.setOnClickListener(v -> {
                heighta = getItem(position).height;
                heighta++;
                getItem(position).setHeight(heighta);//habdet amanyyyyyy
                marmoushdatabase.getInstance(getActivity()).orderDAo().update(getItem(position));
                adapter.notifyDataSetChanged();
            });
            plusWidth.setOnClickListener(v -> {
                widtha = getItem(position).width;
                widtha++;
                getItem(position).setWidth(widtha);
                marmoushdatabase.getInstance(getActivity()).orderDAo().update(getItem(position));
                adapter.notifyDataSetChanged();
            });

            minusHeight.setOnClickListener(v -> {
                heighta = getItem(position).height;
                if (heighta<=1){
                    Snacky.builder().setActivity(getActivity())
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setText("The Height Can't be 0")
                            .info().show();
                }else{
                    heighta--;
                    getItem(position).setHeight(heighta);
                    marmoushdatabase.getInstance(getActivity()).orderDAo().update(getItem(position));
                    adapter.notifyDataSetChanged();}
            });
            minusWidth.setOnClickListener(v -> {
                widtha = getItem(position).width;
                if (widtha<=1){
                    Snacky.builder().setActivity(getActivity())
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setText("The Width Can't be 0")
                            .info().show();
                }else{
                    widtha--;
                    getItem(position).setWidth(widtha);
                    marmoushdatabase.getInstance(getActivity()).orderDAo().update(getItem(position));
                    adapter.notifyDataSetChanged();}
            });
            removeitem.setOnClickListener(v -> {
                int deletorders = marmoushdatabase.getInstance(getActivity()).orderDAo().deletorders(getItem(position));
                if (deletorders>0)
                    Toast.makeText(getActivity(), "order deleted", Toast.LENGTH_SHORT).show();
                myorders.remove(getItem(position));
                adapter.notifyDataSetChanged();
                if (adapter.getCount() > 0) {
                    emptyCart.setVisibility(View.GONE);
                    emptyTV.setVisibility(View.GONE);
                    lv_myCart.setVisibility(View.VISIBLE);
                    completeOrder_btn.setVisibility(View.VISIBLE);
                }
                else if (adapter.getCount() == 0){
                    emptyCart.setVisibility(View.VISIBLE);
                    emptyTV.setVisibility(View.VISIBLE);
                    lv_myCart.setVisibility(View.GONE);
                    completeOrder_btn.setVisibility(View.GONE);
                }
            });

            adapter.notifyDataSetChanged();

            Cm_myCartTitle.setText(getItem(position).title);
            cm_myCartHeight.setText(getItem(position).height + "");
            cm_myCartWidth.setText(getItem(position).width + "");
            Picasso.get().load(getItem(position).url).resize(150,150).into(cm_myCartiamge);

            return convertView;
        }
    }

}