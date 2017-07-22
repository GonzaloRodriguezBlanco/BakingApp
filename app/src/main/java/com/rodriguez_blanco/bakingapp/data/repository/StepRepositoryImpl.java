/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.data.repository;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.rodriguez_blanco.bakingapp.AppExecutors;
import com.rodriguez_blanco.bakingapp.data.local.dao.StepDao;
import com.rodriguez_blanco.bakingapp.data.local.entity.StepEntity;
import com.rodriguez_blanco.bakingapp.data.local.entity.mapper.StepEntityToDomainMapper;
import com.rodriguez_blanco.bakingapp.domain.Step;
import com.rodriguez_blanco.bakingapp.domain.repository.StepRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class StepRepositoryImpl implements StepRepository {
    private StepDao mStepDao;
    private StepEntityToDomainMapper mStepEntityToDomainMapper;
    private AppExecutors mAppExecutors;

    final MutableLiveData<List<Step>> mSteps = new MutableLiveData<>();
    final MutableLiveData<Step> mStep = new MutableLiveData<>();

    @Inject
    public StepRepositoryImpl(StepDao stepDao,
                              StepEntityToDomainMapper stepEntityToDomainMapper,
                              AppExecutors appExecutors) {
        this.mStepDao = stepDao;
        this.mStepEntityToDomainMapper = stepEntityToDomainMapper;
        this.mAppExecutors = appExecutors;
    }

    @Override
    public LiveData<List<Step>> steps(final Long recipeId) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                StepRepositoryImpl.this.loadFromDb(recipeId);
            }
        });

        return mSteps;
    }

    @Override
    public LiveData<Step> step(final Long recipeId, final Long stepId) {
        mAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                StepEntity stepEntity = mStepDao.findById(recipeId, stepId);
                final Step step = mStepEntityToDomainMapper.transform(stepEntity);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mStep.setValue(step);
                    }
                });
            }
        });

        return mStep;
    }

    private void loadFromDb(Long recipeId) {
        Timber.i("Load from db");
        List<StepEntity> stepEntities = mStepDao.getAllByRecipe(recipeId);

        final List<Step> steps = new ArrayList<>();
        for (StepEntity stepEntity:stepEntities) {
            steps.add(mStepEntityToDomainMapper.transform(stepEntity));
        }
        mAppExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mSteps.setValue(steps);
            }
        });
    }
}
