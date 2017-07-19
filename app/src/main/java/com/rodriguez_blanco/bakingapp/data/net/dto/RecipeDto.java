/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.net.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeDto {
    @SerializedName("id")
    private long mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("ingredients")
    private List<IngredientDto> mIngredients;

    @SerializedName("steps")
    private List<StepDto> mSteps;

    @SerializedName("servings")
    private int mServings;

    @SerializedName("image")
    private String mImage;

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public List<IngredientDto> getIngredients() {
        return mIngredients;
    }

    public List<StepDto> getSteps() {
        return mSteps;
    }

    public int getServings() {
        return mServings;
    }

    public String getImage() {
        return mImage;
    }
}
