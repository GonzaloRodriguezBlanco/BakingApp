/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.di.activity;

import com.rodriguez_blanco.bakingapp.di.fragment.StepFragmentModule;
import com.rodriguez_blanco.bakingapp.di.fragment.StepViewPagerFragmentModule;
import com.rodriguez_blanco.bakingapp.ui.step.StepActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class StepActivityModule {

    @ContributesAndroidInjector(modules = {StepFragmentModule.class, StepViewPagerFragmentModule.class})
    abstract StepActivity contributeStepActivityInjector();
}
