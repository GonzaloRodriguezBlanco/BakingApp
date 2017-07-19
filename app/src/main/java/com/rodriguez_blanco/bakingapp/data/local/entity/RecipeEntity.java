/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.local.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.rodriguez_blanco.bakingapp.data.net.dto.IngredientDto;
import com.rodriguez_blanco.bakingapp.data.net.dto.StepDto;

import java.util.List;

@Entity(tableName = "recipes")
public class RecipeEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "servings")
    private int mServings;

    @ColumnInfo(name = "image")
    private String mImage;

    public RecipeEntity(long id, String name, int servings, String image) {
        this.mId = id;
        this.mName = name;
        this.mServings = servings;
        this.mImage = image;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getServings() {
        return mServings;
    }

    public void setServings(int servings) {
        this.mServings = servings;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }
}
