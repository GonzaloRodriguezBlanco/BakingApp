/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.rodriguez_blanco.bakingapp.AppExecutors;
import com.rodriguez_blanco.bakingapp.data.local.dao.IngredientDao;
import com.rodriguez_blanco.bakingapp.data.local.entity.IngredientEntity;
import com.rodriguez_blanco.bakingapp.data.local.entity.mapper.IngredientEntityToDomainMapper;
import com.rodriguez_blanco.bakingapp.domain.Ingredient;
import com.rodriguez_blanco.bakingapp.domain.repository.IngredientRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class IngredientRepositoryImpl implements IngredientRepository {
    private IngredientDao mIngredientDao;
    private IngredientEntityToDomainMapper mIngredientEntityToDomainMapper;
    private AppExecutors mAppExecutors;

    final MutableLiveData<List<Ingredient>> mIngredients = new MutableLiveData<>();

    @Inject
    public IngredientRepositoryImpl(IngredientDao ingredientDao,
                                    IngredientEntityToDomainMapper ingredientEntityToDomainMapper,
                                    AppExecutors appExecutors) {
        this.mIngredientDao = ingredientDao;
        this.mIngredientEntityToDomainMapper = ingredientEntityToDomainMapper;
        this.mAppExecutors = appExecutors;
    }

    @Override
    public LiveData<List<Ingredient>> ingredients(final Long recipeId) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                IngredientRepositoryImpl.this.loadFromDb(recipeId);
            }
        });

        return mIngredients;
    }

    private void loadFromDb(Long recipeId) {
        Timber.i("Load from db");
        List<IngredientEntity> ingredientEntities = mIngredientDao.getAllByRecipe(recipeId);

        final List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientEntity stepEntity:ingredientEntities) {
            ingredients.add(mIngredientEntityToDomainMapper.transform(stepEntity));
        }

        mAppExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mIngredients.setValue(ingredients);
            }
        });
    }
}
