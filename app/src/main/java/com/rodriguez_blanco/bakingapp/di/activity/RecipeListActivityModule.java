/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.di.activity;

import com.rodriguez_blanco.bakingapp.di.fragment.RecipeListFragmentModule;
import com.rodriguez_blanco.bakingapp.ui.main.RecipeListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipeListActivityModule {

    @ContributesAndroidInjector(modules = RecipeListFragmentModule.class)
    abstract RecipeListActivity contributeRecipeListActivityInjector();
}
