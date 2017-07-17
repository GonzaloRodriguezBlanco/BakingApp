/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.di.fragment;

import com.rodriguez_blanco.bakingapp.ui.main.RecipeListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipeListFragmentModule {

    @ContributesAndroidInjector
    abstract RecipeListFragment contributeRecipeListFragmentInjector();
}
