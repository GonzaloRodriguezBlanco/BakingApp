/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.rodriguez_blanco.bakingapp.R;
import com.rodriguez_blanco.bakingapp.ui.step.StepActivity;
import com.rodriguez_blanco.bakingapp.ui.step.StepViewPagerFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity implements
        HasSupportFragmentInjector,
        RecipeFragment.RecipeListener,
        StepViewPagerFragment.StepViewPagerListener {
    private static final String INTENT_EXTRA_PARAM_RECIPE_ID = "com.rodriguez_blanco.INTENT_PARAM_RECIPE_ID";
    private static final String INTENT_EXTRA_PARAM_RECIPE_NAME = "com.rodriguez_blanco.INTENT_PARAM_RECIPE_NAME";
    private static final String MASTER_FRAGMENT_TAG = "master_fragment_tag";
    private static final String DETAIL_FRAGMENT_TAG = "detail_fragment_tag";

    private boolean mTwoPane;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static Intent getCallingIntent(Context context, long recipeId, String recipeName) {
        Intent callingIntent =  new Intent(context, RecipeActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_RECIPE_ID, recipeId);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_RECIPE_NAME, recipeName);

        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if (findViewById(R.id.container_detail) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        ButterKnife.bind(this);

        initializeActionBar();
        initializeFragment(savedInstanceState);
    }

    private void initializeActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        if (ab != null) ab.setTitle(getIntent().getStringExtra(INTENT_EXTRA_PARAM_RECIPE_NAME));
    }

    private void initializeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            long recipeId = getIntent().getLongExtra(INTENT_EXTRA_PARAM_RECIPE_ID, -1L);

            RecipeFragment recipeFragment = RecipeFragment.forRecipe(recipeId);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, recipeFragment, MASTER_FRAGMENT_TAG)
                    .commit();

            if (mTwoPane) {
                int initSelectedPositionInTwoPane = 1;
                recipeFragment.setSelectedItem(initSelectedPositionInTwoPane);

                StepViewPagerFragment stepViewPagerFragment = StepViewPagerFragment.newInstance(recipeId, -1L, false);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_detail, stepViewPagerFragment, DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    public void onStepClicked(long stepId, int position) {
        Long recipeId = getIntent().getLongExtra(INTENT_EXTRA_PARAM_RECIPE_ID, -1L);
        String recipeName = getIntent().getStringExtra(INTENT_EXTRA_PARAM_RECIPE_NAME);

        if(mTwoPane) {
            RecipeFragment recipeFragment = (RecipeFragment) getSupportFragmentManager().findFragmentByTag(MASTER_FRAGMENT_TAG);

            recipeFragment.setSelectedItem(position);

            StepViewPagerFragment stepViewPagerFragment = (StepViewPagerFragment) getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG);

            int pagerPositionForClickedStep = position - 1; // Here the first position is for instructions
            stepViewPagerFragment.selectPagerPosition(pagerPositionForClickedStep);
        } else {
            Intent intent = StepActivity.getCallingIntent(this, recipeId, stepId, recipeName);

            startActivity(intent);
        }
    }

    @Override
    public void onStepSelected(int position) {
        RecipeFragment recipeFragment = (RecipeFragment) getSupportFragmentManager().findFragmentByTag(MASTER_FRAGMENT_TAG);

        recipeFragment.setSelectedItem(position + 1); // We must add 1 to the ViewPager position due to the instructions itemr
    }
}
