/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.recipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rodriguez_blanco.bakingapp.R;
import com.rodriguez_blanco.bakingapp.domain.Ingredient;
import com.rodriguez_blanco.bakingapp.domain.Step;
import com.rodriguez_blanco.bakingapp.util.RecipeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_INGREDIENTS = 0;
    public static final int TYPE_STEP = 1;

    private List<Object> mData;
    private List<Step> mSteps;
    private List<Ingredient> mIngredients;

    private RecipeAdapterListener mListener;

    private int selectedStepPosition = RecyclerView.NO_POSITION;

    public void setSelected(int position) {
        notifyItemChanged(selectedStepPosition);
        selectedStepPosition = position;
        notifyItemChanged(selectedStepPosition);
    }

    public interface RecipeAdapterListener {
        void onStepClicked(long stepId, int position);
        void onAsyncDataLoaded();
    }

    public RecipeAdapter(RecipeAdapterListener recipeAdapterListener) {
        this.mListener = recipeAdapterListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        if(viewType == TYPE_INGREDIENTS) {
            View view = inflater.inflate(R.layout.recipe_ingredients_item, parent, shouldAttachToParentImmediately);

            return new IngredientsViewHolder(view);

        } else if (viewType == TYPE_STEP) {
            View view = inflater.inflate(R.layout.recipe_steps_item, parent, shouldAttachToParentImmediately);

            return new StepViewHolder(view);
        }

        throw new RuntimeException("There is no type that matches " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof IngredientsViewHolder) {
            IngredientsViewHolder ingredientsHolder = (IngredientsViewHolder) holder;
            List<Ingredient> ingredients = (List<Ingredient>) mData.get(position);

            ingredientsHolder.bind(ingredients);

        } else if(holder instanceof StepViewHolder) {
            holder.itemView.setSelected(selectedStepPosition == position);

            StepViewHolder stepHolder = (StepViewHolder) holder;
            Step step = (Step) mData.get(position);

            stepHolder.bind(step);
        }

    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionStep(position)) {
            return TYPE_STEP;
        } else {
            return TYPE_INGREDIENTS;
        }
    }

    private boolean isPositionStep(int position) {
        return mData.get(position) instanceof Step;
    }

    public void setStepsData(List<Step> stepsData) {
        mSteps = stepsData;
        dataChanged();
    }

    public void setIngredientsData(List<Ingredient> ingredientsData) {
        mIngredients = ingredientsData;
        dataChanged();
    }

    private void dataChanged() {
        if (mSteps != null && mIngredients != null) {
            mData = new ArrayList<>();
            int firstPosition = 0;
            mData.add(firstPosition, mIngredients);
            mData.addAll(mSteps);
            notifyDataSetChanged();
            mListener.onAsyncDataLoaded();
        }
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_text_view)
        TextView stepDisplay;

        public StepViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void bind(Step step) {
            Context context = this.itemView.getContext();
            int stepNumber = getAdapterPosition() - 1; // The first position is the ingredient list
            String stepTitle;
            String stepShortDescription = step.getShortDescription();
            if (stepNumber == 0) {
                stepTitle = stepShortDescription;
            } else {
                stepTitle = context.getString(R.string.text_step_item, stepNumber, stepShortDescription);
            }
            stepDisplay.setText(stepTitle);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();

            Step step = (Step) mData.get(adapterPosition);

            mListener.onStepClicked(step.getId(), adapterPosition);
        }
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredients_text_view)
        TextView mIngredientsDisplay;

        public IngredientsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(List<Ingredient> ingredients) {
            Context context = this.itemView.getContext();

            mIngredientsDisplay.setText(RecipeUtil.ingredientListToString(context, ingredients));

        }
    }
}
