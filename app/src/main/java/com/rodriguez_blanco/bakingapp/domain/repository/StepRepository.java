/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.domain.repository;


import android.arch.lifecycle.LiveData;

import com.rodriguez_blanco.bakingapp.domain.Step;

import java.util.List;

public interface StepRepository {
    LiveData<List<Step>> steps(final Long recipeId);
    LiveData<Step> step(final Long recipeId, final Long stepId);
}
