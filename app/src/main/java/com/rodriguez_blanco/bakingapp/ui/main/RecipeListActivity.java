/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.rodriguez_blanco.bakingapp.R;
import com.rodriguez_blanco.bakingapp.ui.recipe.RecipeActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class RecipeListActivity extends AppCompatActivity implements
        HasSupportFragmentInjector,
        RecipeListFragment.RecipeListListener {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        ButterKnife.bind(this);

        initializeActionBar();
        initializeFragment(savedInstanceState);
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
    }

    private void initializeActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle(R.string.app_name);
    }

    private void initializeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            RecipeListFragment recipeListFragment = new RecipeListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, recipeListFragment)
                    .commit();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    public void onRecipeClicked(long recipeId, String recipeName) {
        Intent intent = RecipeActivity.getCallingIntent(this, recipeId, recipeName);

        Timber.d("Launch RecipeActivity for recipeId: %d", recipeId);

        startActivity(intent);
    }

    @Override
    public void onFinishLoading() {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
