/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rodriguez_blanco.bakingapp.data.local.entity.RecipeEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipes")
    LiveData<List<RecipeEntity>> getAll();

    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<RecipeEntity> findById(long id);

    @Insert(onConflict = REPLACE)
    void insertAll(RecipeEntity... recipeEntities);

    @Delete
    void delete(RecipeEntity recipeEntity);
}
