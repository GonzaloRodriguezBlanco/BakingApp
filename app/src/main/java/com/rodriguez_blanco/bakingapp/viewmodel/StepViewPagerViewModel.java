/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.rodriguez_blanco.bakingapp.domain.Step;
import com.rodriguez_blanco.bakingapp.domain.repository.StepRepository;

import java.util.List;

import javax.inject.Inject;

public class StepViewPagerViewModel extends ViewModel {
    private LiveData<List<Step>> mSteps;
    private LiveData<Integer> mSelectedPosition;

    private StepRepository mStepRepository;

    @Inject
    public StepViewPagerViewModel(StepRepository stepRepository) {
        this.mStepRepository = stepRepository;
    }

    public LiveData<List<Step>> getSteps(long recipeId) {
        if (mSteps == null) {
            mSteps = new MutableLiveData<>();
            mSteps = mStepRepository.steps(recipeId);
        }
        return mSteps;
    }

    public LiveData<Integer> getSelectedPosition(long stepId) {
        if (mSelectedPosition == null) {
            initializeSelectedPosition(getPositionByStepId(stepId));
        }

        return mSelectedPosition;
    }

    private Integer getPositionByStepId(long stepId) {
        if (mSteps != null) {
            List<Step> steps = mSteps.getValue();
            if (steps != null) {
                for (Step step:steps) {
                    if(step.getId() == stepId) {
                        return steps.indexOf(step);
                    }
                }
            }
        }
        return -1;
    }

    public void initializeSelectedPosition(Integer selectedPosition) {
        MutableLiveData<Integer> position = new MutableLiveData<>();
        position.setValue(selectedPosition);
        mSelectedPosition = position;
    }
}
