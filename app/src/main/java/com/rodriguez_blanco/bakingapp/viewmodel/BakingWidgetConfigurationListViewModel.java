/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.rodriguez_blanco.bakingapp.domain.Ingredient;
import com.rodriguez_blanco.bakingapp.domain.Recipe;
import com.rodriguez_blanco.bakingapp.domain.repository.IngredientRepository;
import com.rodriguez_blanco.bakingapp.domain.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

public class BakingWidgetConfigurationListViewModel extends ViewModel {
    private LiveData<List<Recipe>> mRecipes;
    private LiveData<List<Ingredient>> mIngredients;

    private RecipeRepository mRecipeRepository;
    private IngredientRepository mIngredientRepository;

    @Inject
    public BakingWidgetConfigurationListViewModel(RecipeRepository recipeRepository,
                                                  IngredientRepository ingredientRepository) {
        this.mRecipeRepository = recipeRepository;
        this.mIngredientRepository = ingredientRepository;
    }

    public LiveData<List<Recipe>> getRecipes() {
        if (mRecipes == null) {
            mRecipes = new MutableLiveData<List<Recipe>>();
            loadRecipes();
        }
        return mRecipes;
    }

    public LiveData<List<Ingredient>> getIngredients(long recipeId) {
        if (mIngredients == null) {
            mIngredients = new MutableLiveData<List<Ingredient>>();
            mIngredients = mIngredientRepository.ingredients(recipeId);
        }
        return mIngredients;
    }

    private void loadRecipes() {
        mRecipes = mRecipeRepository.recipes();
    }
}
