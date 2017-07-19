/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.di;

import android.content.Context;

import com.rodriguez_blanco.bakingapp.BakingApplication;
import com.rodriguez_blanco.bakingapp.BuildConfig;
import com.rodriguez_blanco.bakingapp.data.net.api.MiriamRecipesWebService;

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
}
