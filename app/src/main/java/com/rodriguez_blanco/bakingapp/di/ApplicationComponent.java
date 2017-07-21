/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.di;

import android.content.Context;

import com.rodriguez_blanco.bakingapp.BakingApplication;
import com.rodriguez_blanco.bakingapp.di.activity.RecipeActivityModule;
import com.rodriguez_blanco.bakingapp.di.activity.RecipeListActivityModule;
import com.rodriguez_blanco.bakingapp.di.activity.StepActivityModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        ApplicationModule.class,
        RecipeListActivityModule.class,
        RecipeActivityModule.class,
        StepActivityModule.class
})
public interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        Builder applicationModule(ApplicationModule applicationModule);
        ApplicationComponent build();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Exposed to sub-graphs
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // From ApplicationModule
    Context context();

    void inject(BakingApplication bakingApplication);
}
