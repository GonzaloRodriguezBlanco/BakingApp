/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.step;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.rodriguez_blanco.bakingapp.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class StepActivity extends AppCompatActivity implements
        StepViewPagerFragment.StepViewPagerListener,
        HasSupportFragmentInjector {
    private static final String INTENT_EXTRA_PARAM_STEP_RECIPE_ID = "com.rodriguez_blanco.INTENT_EXTRA_PARAM_STEP_RECIPE_ID";
    private static final String INTENT_EXTRA_PARAM_STEP_ID = "com.rodriguez_blanco.INTENT_PARAM_STEP_ID";
    private static final String INTENT_EXTRA_PARAM_RECIPE_NAME = "com.rodriguez_blanco.INTENT_PARAM_RECIPE_NAME";

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static Intent getCallingIntent(Context context, Long recipeId, Long stepId, String recipeName) {
        Intent callingIntent =  new Intent(context, StepActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_STEP_RECIPE_ID, recipeId);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_STEP_ID, stepId);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_RECIPE_NAME, recipeName);

        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Bundle extras = getIntent().getExtras();
        long recipeId = extras.getLong(INTENT_EXTRA_PARAM_STEP_RECIPE_ID);
        long stepId = extras.getLong(INTENT_EXTRA_PARAM_STEP_ID);
        String recipeName = getIntent().getStringExtra(INTENT_EXTRA_PARAM_RECIPE_NAME);

        ButterKnife.bind(this);

        initializeActionBar();
        initializeFragment(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(android.R.id.home == item.getItemId()) {
            onBackPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        if (ab != null) ab.setTitle(getIntent().getStringExtra(INTENT_EXTRA_PARAM_RECIPE_NAME));
    }

    private void initializeFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            long recipeId = getIntent().getLongExtra(INTENT_EXTRA_PARAM_STEP_RECIPE_ID, -1L);
            long stepId = getIntent().getLongExtra(INTENT_EXTRA_PARAM_STEP_ID, -1L);

            Timber.d("Replace StepViewPagerFragment for recipeId: %d, stepId: %d", recipeId, stepId);

            StepViewPagerFragment stepViewPagerFragment = StepViewPagerFragment.newInstance(recipeId, stepId, true);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, stepViewPagerFragment)
                    .commit();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    public void onStepSelected(int position) {
        // Not used in mobile mode
    }
}
