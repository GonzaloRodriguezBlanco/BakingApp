/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.rodriguez_blanco.bakingapp.data.net.dto.RecipeDto;
import com.rodriguez_blanco.bakingapp.data.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipeListViewModel extends ViewModel {
    private LiveData<List<RecipeDto>> mRecipes;

    private RecipeRepository mRecipeRepository;

    @Inject
    public RecipeListViewModel(RecipeRepository mRecipeRepository) {
        this.mRecipeRepository = mRecipeRepository;
    }

    public LiveData<List<RecipeDto>> getRecipes() {
        if (mRecipes == null) {
            mRecipes = new MutableLiveData<List<RecipeDto>>();
            loadRecipes();
        }
        return mRecipes;
    }

    private void loadRecipes() {
        mRecipes = mRecipeRepository.getRecipes();
    }
}
