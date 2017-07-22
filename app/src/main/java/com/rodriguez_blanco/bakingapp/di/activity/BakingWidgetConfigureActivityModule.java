/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.di.activity;

import com.rodriguez_blanco.bakingapp.ui.widget.BakingWidgetConfigureActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BakingWidgetConfigureActivityModule {

    @ContributesAndroidInjector
    abstract BakingWidgetConfigureActivity contributeBakingWidgetConfigureActivityInjector();
}
