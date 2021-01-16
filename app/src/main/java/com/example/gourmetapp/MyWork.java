package com.example.gourmetapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.rt.data.EventHandler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyWork extends Worker {


    private static final String TAG = "MyWork";
    private Context context;
    int p = Runtime.getRuntime().availableProcessors();
    ExecutorService pool = Executors.newFixedThreadPool(p);
    public MyWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        Log.i("hhhh", "myword started 1 ");
    }

    @NonNull
    @Override
    public Result doWork() {


        Log.i("hhhh", "myword started 2");

        Backendless.Data.of(Order.class).find(new AsyncCallback<List<Order>>() {
            @Override
            public void handleResponse(List<Order> response) {
//
                List<MyOrder> myorderslist = marmoushdatabase.getInstance(context).myorderDAo().myorderslist();

                if (myorderslist.isEmpty()||myorderslist.size()!=response.size()){

                    for (MyOrder myOrder : myorderslist) {
                        marmoushdatabase.getInstance(context).myorderDAo().deletMyListorders(myOrder);
                    }
                    for (Order order : response) {
                        MyOrder myOrder = new MyOrder();
                        myOrder.objectId = order.getObjectId();
                        myOrder.state = order.state;
                        myOrder.title = order.title;
                        marmoushdatabase.getInstance(context).myorderDAo().addMyOrderItem(myOrder);}
                }else {
                    for (int i = 0; i < response.size(); i++) {
                        if (!response.get(i).state.equalsIgnoreCase(myorderslist.get(i).state)) {

                            Intent in = new Intent(context, HistoryActivity.class);

                            PendingIntent pe = PendingIntent.getActivity(getApplicationContext(), 0, in, 0);

                            createNotificationChannel();

                            //show notification
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Homey");
                            builder.setTicker("Good News")
                                    .setContentTitle("Homey")
                                    .setContentText("new update on your orders")
                                    .setSmallIcon(R.mipmap.ic_launcher_round)
                                    .setAutoCancel(true)
                                    .setContentIntent(pe);

                            Notification notification = builder.build();

                            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.notify(1, notification);

                            for (MyOrder myOrder : myorderslist) {
                                marmoushdatabase.getInstance(context).myorderDAo().deletMyListorders(myOrder);
                            }

                            for (Order order : response) {
                                MyOrder myOrder = new MyOrder();
                                myOrder.objectId = order.getObjectId();
                                myOrder.state = order.state;
                                myOrder.title = order.title;
                                marmoushdatabase.getInstance(context).myorderDAo().addMyOrderItem(myOrder);
                            }

                            Log.i("sizeeee",response.size()+"");
                            return;
                        }
                    }
                }

                Log.i("sizeeee",response.size()+"");
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

        return Result.success();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Homey";
            String description = "Homey";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Homey", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
