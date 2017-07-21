/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.rodriguez_blanco.bakingapp.domain.Ingredient;
import com.rodriguez_blanco.bakingapp.domain.Step;
import com.rodriguez_blanco.bakingapp.domain.repository.IngredientRepository;
import com.rodriguez_blanco.bakingapp.domain.repository.StepRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipeViewModel extends ViewModel {
    private LiveData<List<Ingredient>> mIngredients;
    private LiveData<List<Step>> mSteps;

    private IngredientRepository mIngredientRepository;
    private StepRepository mStepRepository;

    @Inject
    public RecipeViewModel(IngredientRepository ingredientRepository,
                           StepRepository stepRepository) {
        this.mIngredientRepository = ingredientRepository;
        this.mStepRepository = stepRepository;
    }

    public LiveData<List<Ingredient>> getIngredients(long recipeId) {
        if (mIngredients == null) {
            mIngredients = new MutableLiveData<List<Ingredient>>();
            mIngredients = mIngredientRepository.ingredients(recipeId);
        }
        return mIngredients;
    }

    public LiveData<List<Step>> getSteps(long recipeId) {
        if (mSteps == null) {
            mSteps = new MutableLiveData<List<Step>>();
            mSteps = mStepRepository.steps(recipeId);
        }
        return mSteps;
    }
}
