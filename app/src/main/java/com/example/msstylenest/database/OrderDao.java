package com.example.msstylenest.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.msstylenest.model.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insert(Order order);

    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    LiveData<List<Order>> getAllOrders();
}