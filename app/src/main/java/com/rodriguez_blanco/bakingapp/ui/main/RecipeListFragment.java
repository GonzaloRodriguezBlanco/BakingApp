/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.main;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rodriguez_blanco.bakingapp.R;
import com.rodriguez_blanco.bakingapp.domain.Recipe;
import com.rodriguez_blanco.bakingapp.util.NetworkUtil;
import com.rodriguez_blanco.bakingapp.viewmodel.RecipeListViewModel;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class RecipeListFragment extends LifecycleFragment implements RecipeListAdapter.RecipeListAdapterListener {

    @Inject
    RecipeListViewModel mViewModel;

    @BindView(R.id.recipe_list_recycler_view)
    RecyclerView mRecipeListRecyclerView;
    private RecipeListAdapter mRecipeListAdapter;

    private Unbinder unbinder;

    private RecipeListListener mListener;

    public interface RecipeListListener {
        void onRecipeClicked(long recipeId, String recipeName);
        void onFinishLoading();
    }

    public RecipeListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        try {
            mListener = (RecipeListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement RecipeListListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        unbinder = ButterKnife.bind(this, viewRoot);

        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        loadRecipes();
    }

    private void setupRecyclerView() {
        Context context = getActivity();
        int orientation = GridLayoutManager.VERTICAL;
        boolean isReverseLayout = false;
        int spanCount = getResources().getInteger(R.integer.recipe_list_grid_columns);

        GridLayoutManager gridLayoutManager
                = new GridLayoutManager(context, spanCount, orientation, isReverseLayout);
        mRecipeListRecyclerView.setLayoutManager(gridLayoutManager);
        mRecipeListRecyclerView.setHasFixedSize(true);
        mRecipeListAdapter = new RecipeListAdapter(this);
        mRecipeListRecyclerView.setAdapter(mRecipeListAdapter);

    }

    private void loadRecipes() {
        Context context = getActivity();
        if (!NetworkUtil.isNetworkAvailable(context)) {
            String noNetworkMessage = getString(R.string.no_network_error);
            Timber.d(noNetworkMessage);
            Snackbar snackbar = Snackbar.make(
                    getActivity().findViewById(R.id.coordinator_layout),
                    noNetworkMessage,
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.network_error_try_again, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecipeListFragment.this.loadRecipes();
                }
            });

            snackbar.show();
        } else {
            mViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
                @Override
                public void onChanged(@Nullable List<Recipe> recipes) {
                    if (recipes != null) {
                        mRecipeListAdapter.setRecipesData(recipes);
                        mListener.onFinishLoading();
                    }
                }
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRecipeClicked(long recipeId, String recipeName) {
        mListener.onRecipeClicked(recipeId, recipeName);
    }
}
