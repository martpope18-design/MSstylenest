package com.example.msstylenest.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long timestamp;
    private double totalAmount;
    private String status;
    private String itemsSummary; // e.g. "2 items: Urban Hoodie, Sneakers"

    public Order(long timestamp, double totalAmount, String status, String itemsSummary) {
        this.timestamp = timestamp;
        this.totalAmount = totalAmount;
        this.status = status;
        this.itemsSummary = itemsSummary;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public long getTimestamp() { return timestamp; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getItemsSummary() { return itemsSummary; }
}