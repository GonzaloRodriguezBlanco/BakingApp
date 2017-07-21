/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.domain.repository;


import android.arch.lifecycle.LiveData;

import com.rodriguez_blanco.bakingapp.domain.Recipe;

import java.util.List;

public interface RecipeRepository {
    LiveData<List<Recipe>> recipes();

    LiveData<Recipe> recipe(final Long id);
}
