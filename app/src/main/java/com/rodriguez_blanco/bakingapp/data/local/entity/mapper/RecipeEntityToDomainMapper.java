/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.local.entity.mapper;

import com.rodriguez_blanco.bakingapp.data.local.entity.IngredientEntity;
import com.rodriguez_blanco.bakingapp.data.local.entity.RecipeEntity;
import com.rodriguez_blanco.bakingapp.data.local.entity.StepEntity;
import com.rodriguez_blanco.bakingapp.domain.Recipe;

import java.util.List;

import javax.inject.Inject;

public class RecipeEntityToDomainMapper {
    private IngredientEntityToDomainMapper mIngredientEntityToDomainMapper;
    private StepEntityToDomainMapper mStepEntityToDomainMapper;

    @Inject
    public RecipeEntityToDomainMapper(IngredientEntityToDomainMapper ingredientEntityToDomainMapper,
                                      StepEntityToDomainMapper stepEntityToDomainMapper) {
        this.mIngredientEntityToDomainMapper = ingredientEntityToDomainMapper;
        this.mStepEntityToDomainMapper = stepEntityToDomainMapper;
    }

    public Recipe transform(RecipeEntity recipeEntity,
                     List<IngredientEntity> ingredientEntities,
                     List<StepEntity> stepEntities) {

        return new Recipe(
                recipeEntity.getId(),
                recipeEntity.getName(),
                mIngredientEntityToDomainMapper.transform(ingredientEntities),
                mStepEntityToDomainMapper.transform(stepEntities),
                recipeEntity.getServings(),
                recipeEntity.getImage()
        );
    }
}
