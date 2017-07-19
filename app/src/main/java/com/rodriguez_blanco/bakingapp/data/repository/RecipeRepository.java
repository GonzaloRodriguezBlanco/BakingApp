/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.rodriguez_blanco.bakingapp.data.net.api.MiriamRecipesWebService;
import com.rodriguez_blanco.bakingapp.data.net.dto.RecipeDto;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RecipeRepository {
    private MiriamRecipesWebService mMiriamRecipesWebService;

    final MutableLiveData<List<RecipeDto>> mRecipes = new MutableLiveData<>();

    @Inject
    public RecipeRepository(MiriamRecipesWebService miriamRecipesWebService) {
        this.mMiriamRecipesWebService = miriamRecipesWebService;
    }

    public LiveData<List<RecipeDto>> getRecipes() {
        mMiriamRecipesWebService.getRecipes()
                .enqueue(new Callback<List<RecipeDto>>() {
                    @Override
                    public void onResponse(Call<List<RecipeDto>> call, Response<List<RecipeDto>> response) {
                        boolean isSuccessful = response.isSuccessful();
                        int code = response.code();

                        if (isSuccessful) {
                            List<RecipeDto> recipes = response.body();

                            mRecipes.setValue(recipes);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RecipeDto>> call, Throwable t) {
                        Timber.e(t, t.getMessage());

                        mRecipes.setValue(null);
                    }
                });
        return mRecipes;
    }
}
