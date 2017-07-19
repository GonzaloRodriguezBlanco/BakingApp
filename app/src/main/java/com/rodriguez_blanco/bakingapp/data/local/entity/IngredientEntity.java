/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.local.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ingredients",
        indices = {
            @Index(value = {"recipe_id"})
        },
        foreignKeys = @ForeignKey(
                entity = RecipeEntity.class,
                parentColumns = "id",
                childColumns = "recipe_id"
        )
)
public class IngredientEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "quantity")
    private double mQuantity;

    @ColumnInfo(name = "measure")
    private String mMeasure;

    @ColumnInfo(name = "ingredient")
    private String mName;

    @ColumnInfo(name = "recipe_id")
    private long mRecipeId;

    public IngredientEntity(double mQuantity, String mMeasure, String mName, long mRecipeId) {
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;
        this.mName = mName;
        this.mRecipeId = mRecipeId;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public double getQuantity() {
        return mQuantity;
    }

    public void setQuantity(double quantity) {
        this.mQuantity = quantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public void setMeasure(String measure) {
        this.mMeasure = measure;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public long getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(long recipeId) {
        this.mRecipeId = recipeId;
    }
}
