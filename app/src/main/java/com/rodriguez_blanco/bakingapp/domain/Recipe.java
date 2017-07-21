/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.domain;

import java.util.List;

public class Recipe {
    private long mId;

    private String mName;

    private List<Ingredient> mIngredients;

    private List<Step> mSteps;

    private int mServings;

    private String mImage;

    public Recipe(long id,
                  String name,
                  List<Ingredient> ingredients,
                  List<Step> steps,
                  int servings,
                  String image) {
        this.mId = id;
        this.mName = name;
        this.mIngredients = ingredients;
        this.mSteps = steps;
        this.mServings = servings;
        this.mImage = image;
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public List<Step> getSteps() {
        return mSteps;
    }

    public int getServings() {
        return mServings;
    }

    public String getImage() {
        return mImage;
    }
}
