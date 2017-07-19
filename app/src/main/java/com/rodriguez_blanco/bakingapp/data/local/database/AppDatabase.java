/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.local.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.rodriguez_blanco.bakingapp.data.local.dao.IngredientDao;
import com.rodriguez_blanco.bakingapp.data.local.dao.RecipeDao;
import com.rodriguez_blanco.bakingapp.data.local.dao.StepDao;
import com.rodriguez_blanco.bakingapp.data.local.entity.IngredientEntity;
import com.rodriguez_blanco.bakingapp.data.local.entity.RecipeEntity;
import com.rodriguez_blanco.bakingapp.data.local.entity.StepEntity;

@Database(entities = {
            RecipeEntity.class,
            IngredientEntity.class,
            StepEntity.class
        },
        version = 1)
public abstract class AppDatabase extends RoomDatabase {

        static final String DATABASE_NAME = "baking-app-db";

        private static AppDatabase sInstance;

        private static final Object sLock = new Object();

        public abstract RecipeDao recipeDao();

        public abstract IngredientDao ingredientDao();

        public abstract StepDao stepDao();

        static final Migration MIGRATION_1_2 = new Migration(1, 2) {
                @Override
                public void migrate(SupportSQLiteDatabase database) {
                }
        };

        public static AppDatabase getInstance(Context context) {
                synchronized (sLock) {
                        if (sInstance == null) {
                                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                        AppDatabase.class, DATABASE_NAME)
                                        .addMigrations(MIGRATION_1_2)
                                        .build();
                        }

                        return sInstance;
                }
        }
}
