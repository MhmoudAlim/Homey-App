package com.example.gourmetapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyOrderDAO {
    @Insert
    long addMyOrderItem(MyOrder myOrder);

    @Query("SELECT * FROM `myorder`")
    List<MyOrder> myorderslist();

    @Update
    int updateMyListorders (MyOrder myOrder);
    @Delete
    int deletMyListorders(MyOrder myOrder);

}
