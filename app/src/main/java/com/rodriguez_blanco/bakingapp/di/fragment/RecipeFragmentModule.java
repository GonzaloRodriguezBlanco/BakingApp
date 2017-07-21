/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.di.fragment;

import com.rodriguez_blanco.bakingapp.ui.recipe.RecipeFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipeFragmentModule {

    @ContributesAndroidInjector
    abstract RecipeFragment contributeRecipeFragmentInjector();
}
