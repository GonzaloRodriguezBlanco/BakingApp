/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.mapper;

import com.rodriguez_blanco.bakingapp.data.local.entity.IngredientEntity;
import com.rodriguez_blanco.bakingapp.data.net.dto.IngredientDto;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class IngredientMapper {

    @Inject
    public IngredientMapper() {
    }

    public IngredientEntity map(IngredientDto ingredientDto, long recipeId) {
        return new IngredientEntity(
                ingredientDto.getQuantity(),
                ingredientDto.getMeasure(),
                ingredientDto.getName(),
                recipeId
        );
    }

    public List<IngredientEntity> map(List<IngredientDto> ingredientDtos, long recipeId) {
        ArrayList<IngredientEntity> ingredientEntities = new ArrayList<>();
        for (IngredientDto ingredientDto : ingredientDtos) {
            ingredientEntities.add(map(ingredientDto, recipeId));
        }
        return ingredientEntities;
    }
}
