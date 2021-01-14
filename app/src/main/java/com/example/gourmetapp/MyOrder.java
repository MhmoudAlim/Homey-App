package com.example.gourmetapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "MyOrder")
public class MyOrder {
    @PrimaryKey(autoGenerate = true)
    public int id;

public String title,objectId,state;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
