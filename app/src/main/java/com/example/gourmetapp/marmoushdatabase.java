package com.example.gourmetapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Order.class,MyOrder.class},version = 1)
public abstract class  marmoushdatabase extends RoomDatabase {
    public abstract OrderDAo orderDAo();

    public abstract MyOrderDAO myorderDAo();

    public static marmoushdatabase ourInstance;

    public static marmoushdatabase getInstance(Context context) {

        if (ourInstance == null) {

            ourInstance = Room.databaseBuilder(context,

                    marmoushdatabase.class, "marmoush.db")
                    .createFromAsset("database/marmoush.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return ourInstance;

    }
}
