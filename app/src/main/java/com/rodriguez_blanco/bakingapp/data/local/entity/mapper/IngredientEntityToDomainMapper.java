/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.local.entity.mapper;


import com.rodriguez_blanco.bakingapp.data.local.entity.IngredientEntity;
import com.rodriguez_blanco.bakingapp.domain.Ingredient;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class IngredientEntityToDomainMapper {

    @Inject
    public IngredientEntityToDomainMapper() {
    }

    public Ingredient transform(IngredientEntity ingredientEntity) {
        return new Ingredient(
                ingredientEntity.getQuantity(),
                ingredientEntity.getMeasure(),
                ingredientEntity.getName()
        );
    }

    public List<Ingredient> transform(List<IngredientEntity> ingredientEntities) {
        if (ingredientEntities == null) {
            return null;
        }

        List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientEntity ingredientEntity : ingredientEntities) {
            ingredients.add(transform(ingredientEntity));
        }
        return ingredients;
    }
}
