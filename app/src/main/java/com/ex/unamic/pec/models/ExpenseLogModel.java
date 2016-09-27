package com.ex.unamic.pec.models;

import com.ex.unamic.pec.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Unamic on 9/18/2016.
 */
public class ExpenseLogModel {
    long Id;
    long User;
    long SubCategoryId;
    String SubCategory;
    String Category;
    String LogDate;
    float Amount;
    String Note;

    public ExpenseLogModel() {
    }

    public ExpenseLogModel(long id, long user, long subCategoryId, String logDate, float amount, String note) {
        Id = id;
        SubCategoryId = subCategoryId;
        LogDate = logDate;
        Amount = amount;
        Note = note;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getUser() {
        return User;
    }

    public void setUser(long user) {
        User = user;
    }

    public long getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(long category) {
        SubCategoryId = category;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDate() {
        return LogDate;
    }

    public Date getDateDate() {
        if (LogDate != null) {
            return Utils.convertStringToDate(LogDate);
        }
        return null;
    }

    public void setDate(String date) {
        LogDate = date;
    }

    public float getAmount() {
        return Amount;
    }

    public void setAmount(float amount) {
        Amount = amount;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
