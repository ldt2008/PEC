package com.ex.unamic.pec.models;

/**
 * Created by Unamic on 9/7/2016.
 */
public class SubCategoryModel {
    long Id;
    long Category;
    String SubCategory;

    public SubCategoryModel() {
    }

    public SubCategoryModel(long id, long  category , String subCategory) {
        Id = id;
        Category = category;
        SubCategory = subCategory;
    }

    public long getId() {
        return Id;
    }
    public void setId(long id) {
        Id = id;
    }

    public String getSubCategory() {
        return SubCategory;
    }
    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public long getCategory() {
        return Category;
    }
    public void setCategory(long  category ) {
        Category = category;
    }

    @Override
    public String toString() {
        return  SubCategory;

    }
}
