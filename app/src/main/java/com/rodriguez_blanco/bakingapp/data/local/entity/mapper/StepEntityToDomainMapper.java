/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.local.entity.mapper;


import com.rodriguez_blanco.bakingapp.data.local.entity.StepEntity;
import com.rodriguez_blanco.bakingapp.domain.Step;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class StepEntityToDomainMapper {

    @Inject
    public StepEntityToDomainMapper() {
    }

    public Step transform(StepEntity stepEntity) {
        return new Step(
                stepEntity.getId(),
                stepEntity.getShortDescription(),
                stepEntity.getDescription(),
                stepEntity.getVideoUrl(),
                stepEntity.getThumbnailUrl()
        );
    }

    public List<Step> transform(List<StepEntity> stepEntities) {
        if (stepEntities == null) {
            return null;
        }
        List<Step> steps = new ArrayList<>();
        for (StepEntity stepEntity:stepEntities) {
            steps.add(transform(stepEntity));
        }
        return steps;
    }
}
