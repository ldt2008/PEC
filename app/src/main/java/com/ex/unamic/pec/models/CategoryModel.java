package com.ex.unamic.pec.models;

/**
 * Created by Unamic on 9/7/2016.
 */
public class CategoryModel {
    long Id;
    String Category;
    long User;

    public CategoryModel() {
    }

    public CategoryModel(int id, String category, long  user) {
        Id = id;
        Category = category;
        User = user;
    }

    public long getId() {
        return Id;
    }
    public void setId(long id) {
        Id = id;
    }

    public String getCategory() {
        return Category;
    }
    public void setCategory(String category) {
        Category = category;
    }

    public long getUser() {
        return User;
    }
    public void setUser(long user ) {
        User = user;
    }

    @Override
    public String toString() {
        return  Category;

    }
}
