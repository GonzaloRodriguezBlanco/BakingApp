/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.widget;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rodriguez_blanco.bakingapp.BuildConfig;
import com.rodriguez_blanco.bakingapp.R;
import com.rodriguez_blanco.bakingapp.domain.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class WidgetConfigurationListAdapter extends RecyclerView.Adapter<WidgetConfigurationListAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;

    private RecipeListAdapterListener mListener;

    public interface RecipeListAdapterListener {
        void onRecipeClicked(Recipe recipe);
    }

    public WidgetConfigurationListAdapter(RecipeListAdapterListener recipeListAdapterListener) {
        this.mListener = recipeListAdapterListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);

        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        if (mRecipes != null) {
            return mRecipes.size();
        }
        return 0;
    }

    public void setRecipesData(List<Recipe> recipesData) {
        mRecipes = recipesData;
        notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(android.R.id.text1)
        TextView name;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            name.setText(recipe.getName());

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            Recipe recipe = mRecipes.get(adapterPosition);

            mListener.onRecipeClicked(recipe);
        }
    }
}
