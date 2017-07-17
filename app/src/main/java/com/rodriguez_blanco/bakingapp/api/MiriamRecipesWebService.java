/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.api;

import com.rodriguez_blanco.bakingapp.domain.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MiriamRecipesWebService {
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
