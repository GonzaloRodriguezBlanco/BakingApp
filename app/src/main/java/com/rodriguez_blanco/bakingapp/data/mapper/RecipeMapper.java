/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.mapper;

import com.rodriguez_blanco.bakingapp.data.local.entity.RecipeEntity;
import com.rodriguez_blanco.bakingapp.data.net.dto.RecipeDto;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipeMapper {

    @Inject
    public RecipeMapper() {
    }

    public RecipeEntity map(RecipeDto recipeDto) {
        return new RecipeEntity(
                recipeDto.getId(),
                recipeDto.getName(),
                recipeDto.getServings(),
                recipeDto.getImage()
        );
    }

    public List<RecipeEntity> map(List<RecipeDto> recipeDtos) {
        ArrayList<RecipeEntity> recipeEntities = new ArrayList<>();
        for (RecipeDto recipeDto : recipeDtos) {
            recipeEntities.add(map(recipeDto));
        }
        return recipeEntities;
    }
}
