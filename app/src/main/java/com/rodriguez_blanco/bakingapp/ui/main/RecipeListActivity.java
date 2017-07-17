/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.rodriguez_blanco.bakingapp.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class RecipeListActivity extends AppCompatActivity implements
        HasSupportFragmentInjector,
        RecipeListFragment.RecipeListListener {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        ButterKnife.bind(this);

        initializeActionBar();
        initializeFragment(savedInstanceState);
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
    public void onRecipeClicked(long recipeId) {
        Toast.makeText(this, "RecipeId: " + recipeId, Toast.LENGTH_SHORT).show();
    }
}
