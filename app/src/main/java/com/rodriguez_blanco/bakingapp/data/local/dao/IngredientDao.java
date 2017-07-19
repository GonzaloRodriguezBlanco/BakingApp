/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rodriguez_blanco.bakingapp.data.local.entity.IngredientEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface IngredientDao {
    @Query("SELECT * FROM ingredients WHERE recipe_id = :recipeId")
    LiveData<List<IngredientEntity>> getAllByRecipe(long recipeId);

    @Insert(onConflict = REPLACE)
    void insertAll(IngredientEntity... ingredientEntities);

    @Delete
    void delete(IngredientEntity ingredientEntity);
}
