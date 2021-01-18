package com.example.gourmetapp;

import android.widget.EditText;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "Order")

public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String url;

    public int code;
    public int height;
    public int width;
    public String state;

    //
    public String updated;
    public String created;
    public String objectId;

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }


}
