/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.domain.repository;


import android.arch.lifecycle.LiveData;

import com.rodriguez_blanco.bakingapp.domain.Ingredient;

import java.util.List;

public interface IngredientRepository {
    LiveData<List<Ingredient>> ingredients(final Long recipeId);
}
