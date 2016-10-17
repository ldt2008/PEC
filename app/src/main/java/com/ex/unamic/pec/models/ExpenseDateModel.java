package com.ex.unamic.pec.models;

/**
 * Created by Unamic on 10/1/2016.
 */

public class ExpenseDateModel{
    String Date;
    float   TotalAmount;

    public ExpenseDateModel() {
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date  = date;
    }

    public float getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        TotalAmount = totalAmount;
    }
}