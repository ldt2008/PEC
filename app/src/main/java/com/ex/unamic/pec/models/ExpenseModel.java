package com.ex.unamic.pec.models;

import com.ex.unamic.pec.utils.Utils;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Unamic on 9/30/2016.
 */
public class ExpenseModel {
    String ExpenseTime;
    float TotalAmount;
    List<String> Categories;

    public ExpenseModel() {
    }

    public ExpenseModel(String expenseTime, float totalAmount, List<String> categories) {
        ExpenseTime = expenseTime;
        TotalAmount = totalAmount;
        Categories = categories;
    }

    public String getExpenseTime() {
        return ExpenseTime;
    }

    public void setExpenseTime(String expenseTime) {
        ExpenseTime = expenseTime;
    }

    public float getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        TotalAmount = totalAmount;
    }

    public List<String> getCategories() {
        return Categories;
    }

    public void setCategories(List<String> categories) {
        Categories = categories;
    }
}
