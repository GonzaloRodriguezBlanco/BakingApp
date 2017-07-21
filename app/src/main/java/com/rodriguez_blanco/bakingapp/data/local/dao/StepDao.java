/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.rodriguez_blanco.bakingapp.data.local.entity.StepEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StepDao {
    @Query("SELECT * FROM steps")
    List<StepEntity> getAll();

    @Query("SELECT * FROM steps WHERE recipe_id = :recipeId")
    List<StepEntity> getAllByRecipe(long recipeId);

    @Query("SELECT * FROM steps WHERE recipe_id = :recipeId and id = :stepId")
    StepEntity findById(long recipeId, long stepId);

    @Insert(onConflict = REPLACE)
    void insertAll(StepEntity... stepEntities);

    @Delete
    void delete(StepEntity stepEntity);
}
