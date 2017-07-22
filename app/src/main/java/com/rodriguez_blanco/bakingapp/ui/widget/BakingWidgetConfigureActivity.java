/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.rodriguez_blanco.bakingapp.R;
import com.rodriguez_blanco.bakingapp.domain.Ingredient;
import com.rodriguez_blanco.bakingapp.domain.Recipe;
import com.rodriguez_blanco.bakingapp.util.NetworkUtil;
import com.rodriguez_blanco.bakingapp.util.RecipeUtil;
import com.rodriguez_blanco.bakingapp.viewmodel.BakingWidgetConfigurationListViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import timber.log.Timber;

/**
 * The configuration screen for the {@link BakingWidget BakingWidget} AppWidget.
 */
public class BakingWidgetConfigureActivity extends LifecycleActivity implements WidgetConfigurationListAdapter.RecipeListAdapterListener {

    @Inject
    BakingWidgetConfigurationListViewModel mViewModel;

    private static final String PREFS_NAME = "com.rodriguez_blanco.bakingapp.ui.widget.BakingWidget";
    private static final String PREF_PREFIX_KEY_RECIPE_NAME = "appwidget_recipe_name_";
    private static final String PREF_PREFIX_KEY_RECIPE_INGREDIENTS = "appwidget_recipe_ingredients_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    @BindView(R.id.recipes_recycler_view)
    RecyclerView mRecipeListRecyclerView;
    private WidgetConfigurationListAdapter mAdapter;

    private Recipe mRecipe;

    public BakingWidgetConfigureActivity() {
        super();
    }

    static String loadRecipeNamePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY_RECIPE_NAME + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static String loadIngredientsPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY_RECIPE_INGREDIENTS + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return "";
        }
    }

    static void deletePrefs(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY_RECIPE_NAME + appWidgetId);
        prefs.remove(PREF_PREFIX_KEY_RECIPE_INGREDIENTS + appWidgetId);
        prefs.apply();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        setContentView(R.layout.baking_widget_configure);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        initialize();
    }

    private void initialize() {
        setupRecyclerView();
        loadRecipes();
    }

    private void loadRecipes() {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            Timber.d("No network connection!");
        } else {
            mViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
                @Override
                public void onChanged(@Nullable List<Recipe> recipes) {
                    if (recipes != null) {
                        mAdapter.setRecipesData(recipes);
                    }

                }
            });
        }

    }

    private void loadIngredients(long recipeId) {
        mViewModel.getIngredients(recipeId).observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                if (ingredients != null) {
                    mRecipe.setIngredients(ingredients);
                }

                saveRecipePref(BakingWidgetConfigureActivity.this, mAppWidgetId, mRecipe);

                BakingWidgetConfigureActivity.this.sendResult();
            }
        });

    }

    private void setupRecyclerView() {
        int orientation = LinearLayoutManager.VERTICAL;
        boolean isReverseLayout = false;

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, orientation, isReverseLayout);
        mRecipeListRecyclerView.setLayoutManager(linearLayoutManager);
        mRecipeListRecyclerView.setHasFixedSize(true);
        mAdapter = new WidgetConfigurationListAdapter(this);
        mRecipeListRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        mRecipe = recipe;
        loadIngredients(recipe.getId());
    }

    static void saveRecipePref(Context context, int appWidgetId, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY_RECIPE_NAME + appWidgetId, recipe.getName());
        prefs.putString(PREF_PREFIX_KEY_RECIPE_INGREDIENTS + appWidgetId,
                RecipeUtil.ingredientListToString(context, recipe.getIngredients()));
        prefs.apply();
    }

    public void sendResult() {
        final Context context = BakingWidgetConfigureActivity.this;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        BakingWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

