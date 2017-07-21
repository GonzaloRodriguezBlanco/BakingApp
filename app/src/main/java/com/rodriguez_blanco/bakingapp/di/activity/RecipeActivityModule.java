/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.di.activity;

import com.rodriguez_blanco.bakingapp.di.fragment.RecipeFragmentModule;
import com.rodriguez_blanco.bakingapp.di.fragment.StepFragmentModule;
import com.rodriguez_blanco.bakingapp.di.fragment.StepViewPagerFragmentModule;
import com.rodriguez_blanco.bakingapp.ui.recipe.RecipeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipeActivityModule {

    @ContributesAndroidInjector(modules = {RecipeFragmentModule.class, StepFragmentModule.class, StepViewPagerFragmentModule.class})
    abstract RecipeActivity contributeRecipeActivityInjector();
}
