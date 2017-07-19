/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.mapper;

import com.rodriguez_blanco.bakingapp.data.local.entity.StepEntity;
import com.rodriguez_blanco.bakingapp.data.net.dto.StepDto;

import java.util.ArrayList;
import java.util.List;

public class stepMapper {

    public StepEntity map(StepDto stepDto, long recipeId) {
        return new StepEntity(
                stepDto.getId(),
                stepDto.getShortDescription(),
                stepDto.getDescription(),
                stepDto.getVideoUrl(),
                stepDto.getThumbnailUrl(),
                recipeId
        );
    }

    public List<StepEntity> map(List<StepDto> stepDtos, long recipeId) {
        ArrayList<StepEntity> stepEntities = new ArrayList<>();
        for (StepDto stepDto : stepDtos) {
            stepEntities.add(map(stepDto, recipeId));
        }
        return stepEntities;
    }
}
