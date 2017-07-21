/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.main;

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
import com.rodriguez_blanco.bakingapp.data.net.dto.RecipeDto;
import com.rodriguez_blanco.bakingapp.domain.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;

    private RecipeListAdapterListener mListener;

    public interface RecipeListAdapterListener {
        void onRecipeClicked(long recipeId, String recipeName);
    }

    public RecipeListAdapter(RecipeListAdapterListener recipeListAdapterListener) {
        this.mListener = recipeListAdapterListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);

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
        @BindView(R.id.recipe_name_text_view)
        TextView name;
        @BindView(R.id.recipe_servings_text_view)
        TextView servings;
        @BindView(R.id.recipe_image_view)
        ImageView image;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            Context context = this.itemView.getContext();
            name.setText(recipe.getName());
            servings.setText(context.getString(R.string.text_servings, recipe.getServings()));
            String recipeImage = recipe.getImage();

            Uri imageUri;
            if (recipeImage != null && recipeImage.length() > 0) {
                imageUri = Uri.parse(recipeImage);
            } else {
                imageUri = Uri.parse(BuildConfig.IMAGE_URI_DEFAULT);
            }

            Picasso.with(context)
                    .load(imageUri)
                    .placeholder(R.drawable.recipe_placeholder)
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into(image);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            Recipe recipe = mRecipes.get(adapterPosition);

            Timber.d("Clicked recipe with recipeId: %d", recipe.getId());
            mListener.onRecipeClicked(recipe.getId(), recipe.getName());
        }
    }
}
