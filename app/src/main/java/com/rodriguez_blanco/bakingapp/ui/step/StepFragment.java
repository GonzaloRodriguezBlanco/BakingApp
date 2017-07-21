/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.step;

import android.arch.lifecycle.LifecycleFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rodriguez_blanco.bakingapp.R;
import com.rodriguez_blanco.bakingapp.ui.recipe.RecipeAdapter;
import com.rodriguez_blanco.bakingapp.viewmodel.RecipeViewModel;
import com.rodriguez_blanco.bakingapp.viewmodel.StepViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

public class StepFragment extends LifecycleFragment {
    private static final String PARAM_RECIPE_ID = "param_recipe_id";
    private static final String PARAM_STEP_ID = "param_step_id";

    @Inject
    StepViewModel mViewModel;

    @BindView(R.id.video_frame)
    FrameLayout mVideoFrame;

    @BindView(R.id.step_instructions)
    TextView mStepInstructions;

    private Unbinder unbinder;

    public StepFragment() {
    }

    public static StepFragment forStep(long recipeId, long stepId) {
        final StepFragment recipeFragment = new StepFragment();
        final Bundle arguments = new Bundle();
        arguments.putLong(PARAM_RECIPE_ID, recipeId);
        arguments.putLong(PARAM_STEP_ID, stepId);
        recipeFragment.setArguments(arguments);
        return recipeFragment;
    }

    private long getParamRecipeId(){
        Bundle arguments = getArguments();
        return arguments.getLong(PARAM_RECIPE_ID);
    }

    private long getParamStepId(){
        Bundle arguments = getArguments();
        return arguments.getLong(PARAM_STEP_ID);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_step, container, false);

        unbinder = ButterKnife.bind(this, viewRoot);

        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadStep(getParamRecipeId(), getParamStepId());
    }

    private void loadStep(long recipeId, long stepId) {

            mViewModel.getStep(recipeId, stepId).observe(this, step -> {
                if (step == null) {
//                    showUnknownError();
                } else {
                    mStepInstructions.setText(step.getDescription());
//                    hideLoadingIndicator();
                }

            });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
