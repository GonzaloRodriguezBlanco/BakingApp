/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.di;

import android.content.Context;

import com.rodriguez_blanco.bakingapp.AppExecutors;
import com.rodriguez_blanco.bakingapp.BakingApplication;
import com.rodriguez_blanco.bakingapp.BuildConfig;
import com.rodriguez_blanco.bakingapp.data.local.dao.IngredientDao;
import com.rodriguez_blanco.bakingapp.data.local.dao.RecipeDao;
import com.rodriguez_blanco.bakingapp.data.local.dao.StepDao;
import com.rodriguez_blanco.bakingapp.data.local.database.AppDatabase;
import com.rodriguez_blanco.bakingapp.data.local.entity.mapper.IngredientEntityToDomainMapper;
import com.rodriguez_blanco.bakingapp.data.local.entity.mapper.RecipeEntityToDomainMapper;
import com.rodriguez_blanco.bakingapp.data.local.entity.mapper.StepEntityToDomainMapper;
import com.rodriguez_blanco.bakingapp.data.mapper.IngredientMapper;
import com.rodriguez_blanco.bakingapp.data.mapper.RecipeMapper;
import com.rodriguez_blanco.bakingapp.data.mapper.StepMapper;
import com.rodriguez_blanco.bakingapp.data.net.api.MiriamRecipesWebService;
import com.rodriguez_blanco.bakingapp.data.repository.IngredientRepositoryImpl;
import com.rodriguez_blanco.bakingapp.data.repository.RecipeRepositoryImpl;
import com.rodriguez_blanco.bakingapp.data.repository.StepRepositoryImpl;
import com.rodriguez_blanco.bakingapp.domain.repository.IngredientRepository;
import com.rodriguez_blanco.bakingapp.domain.repository.RecipeRepository;
import com.rodriguez_blanco.bakingapp.domain.repository.StepRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
    private final BakingApplication application;

    public ApplicationModule(BakingApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return this.application;
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHtppClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // One of: NONE | BASIC | HEADERS | BODY
            builder.interceptors().add(loggingInterceptor);
        }

        return builder.build();
    }

    @Singleton
    @Provides
    MiriamRecipesWebService provideRecipesWebService(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(MiriamRecipesWebService.class);
    }

    @Singleton
    @Provides
    RecipeRepository provideRecipeRepository(MiriamRecipesWebService miriamRecipesWebService,
                                             AppDatabase appDatabase,
                                             RecipeDao recipeDao,
                                             IngredientDao ingredientDao,
                                             StepDao stepDao,
                                             RecipeEntityToDomainMapper recipeEntityToDomainMapper,
                                             RecipeMapper recipeMapper,
                                             IngredientMapper ingredientMapper,
                                             StepMapper stepMapper,
                                             AppExecutors appExecutors) {
        return new RecipeRepositoryImpl(miriamRecipesWebService,
                appDatabase,
                recipeDao,
                ingredientDao,
                stepDao,
                recipeEntityToDomainMapper,
                recipeMapper,
                ingredientMapper,
                stepMapper,
                appExecutors);
    }

    @Provides
    StepRepository provideStepRepository(StepDao stepDao,
                                         StepEntityToDomainMapper stepEntityToDomainMapper,
                                         AppExecutors appExecutors) {
        return new StepRepositoryImpl(stepDao, stepEntityToDomainMapper, appExecutors);
    }

    @Singleton
    @Provides
    IngredientRepository provideIngredientRepository(IngredientDao ingredientDao,
                                               IngredientEntityToDomainMapper ingredientEntityToDomainMapper,
                                               AppExecutors appExecutors) {
        return new IngredientRepositoryImpl(
                ingredientDao,
                ingredientEntityToDomainMapper,
                appExecutors);
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase(Context context) {
        return AppDatabase.getInstance(context);
    }

    @Singleton
    @Provides
    RecipeDao provideRecipeDao(AppDatabase appDatabase) {
        return appDatabase.recipeDao();
    }

    @Singleton
    @Provides
    IngredientDao provideIngredientDao(AppDatabase appDatabase) {
        return appDatabase.ingredientDao();
    }

    @Singleton
    @Provides
    StepDao provideStepDao(AppDatabase appDatabase) {
        return appDatabase.stepDao();
    }
}
