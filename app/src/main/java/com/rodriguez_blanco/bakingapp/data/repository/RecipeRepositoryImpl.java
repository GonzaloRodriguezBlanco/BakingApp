/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.rodriguez_blanco.bakingapp.AppExecutors;
import com.rodriguez_blanco.bakingapp.data.local.dao.IngredientDao;
import com.rodriguez_blanco.bakingapp.data.local.dao.RecipeDao;
import com.rodriguez_blanco.bakingapp.data.local.dao.StepDao;
import com.rodriguez_blanco.bakingapp.data.local.database.AppDatabase;
import com.rodriguez_blanco.bakingapp.data.local.entity.IngredientEntity;
import com.rodriguez_blanco.bakingapp.data.local.entity.RecipeEntity;
import com.rodriguez_blanco.bakingapp.data.local.entity.StepEntity;
import com.rodriguez_blanco.bakingapp.data.local.entity.mapper.RecipeEntityToDomainMapper;
import com.rodriguez_blanco.bakingapp.data.mapper.IngredientMapper;
import com.rodriguez_blanco.bakingapp.data.mapper.RecipeMapper;
import com.rodriguez_blanco.bakingapp.data.mapper.StepMapper;
import com.rodriguez_blanco.bakingapp.data.net.api.MiriamRecipesWebService;
import com.rodriguez_blanco.bakingapp.data.net.dto.RecipeDto;
import com.rodriguez_blanco.bakingapp.domain.Recipe;
import com.rodriguez_blanco.bakingapp.domain.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class RecipeRepositoryImpl implements RecipeRepository {
    private MiriamRecipesWebService mMiriamRecipesWebService;
    private AppDatabase mAppDatabase;
    private RecipeDao mRecipeDao;
    private IngredientDao mIngredientDao;
    private StepDao mStepDao;
    private AppExecutors mAppExecutors;
    private RecipeEntityToDomainMapper mRecipeEntityToDomainMapper;
    private RecipeMapper mRecipeMapper;
    private IngredientMapper mIngredientMapper;
    private StepMapper mStepMapper;

    final MutableLiveData<List<Recipe>> mRecipes = new MutableLiveData<>();

    @Inject
    public RecipeRepositoryImpl(MiriamRecipesWebService miriamRecipesWebService,
                                AppDatabase appDatabase,
                                RecipeDao recipeDao,
                                IngredientDao ingredientDao,
                                StepDao stepDao,
                                RecipeEntityToDomainMapper recipeEntityToDomainMapper,
                                RecipeMapper recipeMapper,
                                IngredientMapper ingredientMapper,
                                StepMapper stepMapper,
                                AppExecutors appExecutors) {
        this.mMiriamRecipesWebService = miriamRecipesWebService;
        this.mAppDatabase = appDatabase;
        this.mRecipeDao = recipeDao;
        this.mIngredientDao = ingredientDao;
        this.mStepDao = stepDao;
        this.mRecipeEntityToDomainMapper = recipeEntityToDomainMapper;
        this.mRecipeMapper = recipeMapper;
        this.mIngredientMapper = ingredientMapper;
        this.mStepMapper = stepMapper;
        this.mAppExecutors = appExecutors;
    }

    public void fetchFromNetwork() {
        Timber.i("Fetch from network");
        mMiriamRecipesWebService.getRecipes()
                .enqueue(new Callback<List<RecipeDto>>() {
                    @Override
                    public void onResponse(Call<List<RecipeDto>> call, Response<List<RecipeDto>> response) {
                        boolean isSuccessful = response.isSuccessful();
                        int code = response.code();

                        if (isSuccessful) {
                            final List<RecipeDto> recipes = response.body();

                            mAppExecutors.diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mAppDatabase.beginTransaction();
                                    try {

                                        for (RecipeDto recipeDto : recipes) {
                                            long recipeDtoId = recipeDto.getId();
                                            mRecipeDao.insert(mRecipeMapper.map(recipeDto));

                                            List<IngredientEntity> ingredientEntities = mIngredientMapper.map(recipeDto.getIngredients(), recipeDtoId);
                                            mIngredientDao.insertAll(ingredientEntities.toArray(new IngredientEntity[ingredientEntities.size()]));

                                            List<StepEntity> stepEntities = mStepMapper.map(recipeDto.getSteps(), recipeDtoId);
                                            mStepDao.insertAll(stepEntities.toArray(new StepEntity[stepEntities.size()]));
                                        }
                                        mAppDatabase.setTransactionSuccessful();
                                    } catch (Exception e) {
                                        Timber.e(e, e.getMessage());
                                    } finally {
                                        mAppDatabase.endTransaction();
                                    }

                                    loadFromDb();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RecipeDto>> call, Throwable t) {
                        Timber.e(t, t.getMessage());

                        mRecipes.setValue(null);
                    }
                });
    }

    @Override
    public LiveData<List<Recipe>> recipes() {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                RecipeRepositoryImpl.this.loadFromDb();
            }
        });

        return mRecipes;
    }

    private void loadFromDb() {
        Timber.i("Load from db");
        List<RecipeEntity> recipeEntities = mRecipeDao.getAll();

        if (recipeEntities == null || recipeEntities.size() == 0) {
            fetchFromNetwork();
        } else {
            final List<Recipe> recipes = new ArrayList<>();
            for (RecipeEntity recipeEntity:recipeEntities) {
                recipes.add(mRecipeEntityToDomainMapper.transform(recipeEntity, null, null));
            }
            mAppExecutors.mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    mRecipes.setValue(recipes);
                }
            });
        }
    }

    @Override
    public LiveData<Recipe> recipe(Long id) {
        return null;
    }
}
