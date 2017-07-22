/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.util;


import android.content.Context;

import com.rodriguez_blanco.bakingapp.R;
import com.rodriguez_blanco.bakingapp.domain.Ingredient;

import java.util.List;

public abstract class RecipeUtil {

    public static String ingredientListToString(Context context, List<Ingredient> ingredients) {
        String ingredientsText = "";
        for (Ingredient ingredient:ingredients) {
            ingredientsText += context.getString(
                    R.string.text_ingredient,
                    ingredient.getName(),
                    ingredient.getQuantity(),
                    ingredient.getMeasure());
            boolean isLast = ingredients.indexOf(ingredient) == ingredients.size() -1;
            if (!isLast) {
                ingredientsText += "\n";
            }
        }
        return ingredientsText;
    }
}
