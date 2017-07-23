/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.recipe;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rodriguez_blanco.bakingapp.R;
import com.rodriguez_blanco.bakingapp.domain.Ingredient;
import com.rodriguez_blanco.bakingapp.domain.Step;
import com.rodriguez_blanco.bakingapp.ui.main.RecipeListAdapter;
import com.rodriguez_blanco.bakingapp.util.NetworkUtil;
import com.rodriguez_blanco.bakingapp.viewmodel.RecipeListViewModel;
import com.rodriguez_blanco.bakingapp.viewmodel.RecipeViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class RecipeFragment extends LifecycleFragment implements RecipeAdapter.RecipeAdapterListener {
    private static final String PARAM_RECIPE_ID = "param_recipe_id";
    private static final String INSTANCE_SELECTED_POSITION = "saved_selected_position";
    private static final String INSTANCE_LAYOUT_MANAGER = "saved_layout_manager";

    @Inject
    RecipeViewModel mViewModel;

    @BindView(R.id.recipe_recycler_view)
    RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Parcelable mLayoutManagerSavedState;

    private Unbinder unbinder;

    private RecipeListener mListener;

    private int mSelectedItem = RecyclerView.NO_POSITION;

    public interface RecipeListener {
        void onStepClicked(long stepId, int position);
    }

    public RecipeFragment() {
    }

    public static RecipeFragment forRecipe(long recipeId) {
        final RecipeFragment recipeFragment = new RecipeFragment();
        final Bundle arguments = new Bundle();
        arguments.putLong(PARAM_RECIPE_ID, recipeId);
        recipeFragment.setArguments(arguments);
        return recipeFragment;
    }

    private long getParamRecipeId(){
        Bundle arguments = getArguments();
        return arguments.getLong(PARAM_RECIPE_ID);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        try {
            mListener = (RecipeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement RecipeListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_recipe, container, false);

        unbinder = ButterKnife.bind(this, viewRoot);

        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(INSTANCE_SELECTED_POSITION);
        }

        setupRecyclerView(savedInstanceState);
        loadRecipe(getParamRecipeId());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INSTANCE_SELECTED_POSITION, mSelectedItem);
        outState.putParcelable(INSTANCE_LAYOUT_MANAGER, mRecipeRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_LAYOUT_MANAGER)) {
            mLayoutManagerSavedState = savedInstanceState.getParcelable(INSTANCE_LAYOUT_MANAGER);
        }
    }

    private void setupRecyclerView(Bundle savedInstanceState) {
        Context context = getActivity();
        int orientation = LinearLayoutManager.VERTICAL;
        boolean isReverseLayout = false;

        mLinearLayoutManager = new LinearLayoutManager(context, orientation, isReverseLayout);
        mRecipeRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecipeRecyclerView.setHasFixedSize(true);
        mRecipeAdapter = new RecipeAdapter(this);
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);

    }

    private void loadRecipe(long recipeId) {
        mViewModel.getIngredients(recipeId).observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                if (ingredients != null) {
                    mRecipeAdapter.setIngredientsData(ingredients);
                }

            }
        });

        mViewModel.getSteps(recipeId).observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                if (steps != null) {
                    mRecipeAdapter.setStepsData(steps);
                    if (mSelectedItem != RecyclerView.NO_POSITION) {
                        mRecipeAdapter.setSelected(mSelectedItem);
                    }
                }

            }
        });

    }

    public void setSelectedItem(int selectedItem) {
        mSelectedItem = selectedItem;
        if (mRecipeAdapter != null) {
            mRecipeAdapter.setSelected(mSelectedItem);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStepClicked(long stepId, int position) {
        mListener.onStepClicked(stepId, position);
    }

    @Override
    public void onAsyncDataLoaded() {
        if (mLinearLayoutManager != null && mLayoutManagerSavedState != null) {
            mLinearLayoutManager.onRestoreInstanceState(mLayoutManagerSavedState);
        }

    }
}
