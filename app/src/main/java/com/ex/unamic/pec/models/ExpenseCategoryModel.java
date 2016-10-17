package com.ex.unamic.pec.models;

/**
 * Created by Unamic on 10/1/2016.
 */
public class ExpenseCategoryModel {

    String Category;
    float   TotalAmount;

    public ExpenseCategoryModel() {
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category  = category;
    }

    public float getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        TotalAmount = totalAmount;
    }
}
