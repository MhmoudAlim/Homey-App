package com.example.gourmetapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDAo {
    @Insert
    long additem(Order order);


@Query("SELECT * FROM `order`")
    List<Order>myorders();

@Delete
    int deletorders(Order order);
@Update
int update (Order order);


}
