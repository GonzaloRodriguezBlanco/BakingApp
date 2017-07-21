/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.rodriguez_blanco.bakingapp.domain.Ingredient;
import com.rodriguez_blanco.bakingapp.domain.Step;
import com.rodriguez_blanco.bakingapp.domain.repository.IngredientRepository;
import com.rodriguez_blanco.bakingapp.domain.repository.StepRepository;

import java.util.List;

import javax.inject.Inject;

public class StepViewModel extends ViewModel {
    private LiveData<Step> mStep;

    private StepRepository mStepRepository;

    @Inject
    public StepViewModel(StepRepository stepRepository) {
        this.mStepRepository = stepRepository;
    }

    public LiveData<Step> getStep(long recipeId, long stepId) {
        if (mStep == null) {
            mStep = new MutableLiveData<Step>();
            mStep = mStepRepository.step(recipeId, stepId);
        }
        return mStep;
    }
}
