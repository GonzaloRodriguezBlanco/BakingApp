/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.rodriguez_blanco.bakingapp.api.MiriamRecipesWebService;
import com.rodriguez_blanco.bakingapp.domain.Recipe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RecipeRepository {
    private MiriamRecipesWebService mMiriamRecipesWebService;

    final MutableLiveData<List<Recipe>> mRecipes = new MutableLiveData<>();

    @Inject
    public RecipeRepository(MiriamRecipesWebService miriamRecipesWebService) {
        this.mMiriamRecipesWebService = miriamRecipesWebService;
    }

    public LiveData<List<Recipe>> getRecipes() {
        mMiriamRecipesWebService.getRecipes()
                .enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                        boolean isSuccessful = response.isSuccessful();
                        int code = response.code();

                        if (isSuccessful) {
                            List<Recipe> recipes = response.body();

                            mRecipes.setValue(recipes);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Recipe>> call, Throwable t) {
                        Timber.e(t, t.getMessage());

                        mRecipes.setValue(null);
                    }
                });
        return mRecipes;
    }
}
