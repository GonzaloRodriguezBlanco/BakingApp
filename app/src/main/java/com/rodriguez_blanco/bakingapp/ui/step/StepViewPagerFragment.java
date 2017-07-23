/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.step;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.rodriguez_blanco.bakingapp.R;
import com.rodriguez_blanco.bakingapp.domain.Step;
import com.rodriguez_blanco.bakingapp.viewmodel.StepViewPagerViewModel;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class StepViewPagerFragment extends LifecycleFragment {
    private static final String PARAM_RECIPE_ID = "param_recipe_id";
    private static final String PARAM_STEP_ID = "param_step_id";
    private static final String PARAM_HAS_BOTTOM_NAVIGATION = "param_has_bottom_navigation";
    private static final String INSTANCE_STATE_PAGER_POSITION = "saved_pager_position";

    @Inject
    StepViewPagerViewModel mViewModel;

    @BindView(R.id.steps_view_pager)
    ViewPager mViewPager;
    StepsPagerAdapter mPagerAdapter;

    @BindView(R.id.steps_navigation_view)
    RelativeLayout mBottomNavigationView;

    @BindView(R.id.bt_prev)
    Button mPrevButton;

    @BindView(R.id.bt_next)
    Button mNextButton;

    private Unbinder unbinder;

    private StepViewPagerListener mListener;

    public interface StepViewPagerListener {
        void onStepSelected(int position);
    }

    public StepViewPagerFragment() {
    }

    public static StepViewPagerFragment newInstance(long recipeId, long stepId, boolean hasBottomNavigation) {
        final StepViewPagerFragment recipeFragment = new StepViewPagerFragment();
        final Bundle arguments = new Bundle();
        arguments.putLong(PARAM_RECIPE_ID, recipeId);
        arguments.putLong(PARAM_STEP_ID, stepId);
        arguments.putBoolean(PARAM_HAS_BOTTOM_NAVIGATION, hasBottomNavigation);
        recipeFragment.setArguments(arguments);
        return recipeFragment;
    }

    private long getParamRecipeId(){
        Bundle arguments = getArguments();
        return arguments.getLong(PARAM_RECIPE_ID);
    }

    private long getParamStepId(){
        Bundle arguments = getArguments();
        return arguments.getLong(PARAM_STEP_ID);
    }

    private boolean getParamHasBottomNavigation(){
        Bundle arguments = getArguments();
        return arguments.getBoolean(PARAM_HAS_BOTTOM_NAVIGATION);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        try {
            mListener = (StepViewPagerFragment.StepViewPagerListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement StepViewPagerListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_step_pager, container, false);

        unbinder = ButterKnife.bind(this, viewRoot);

        boolean hasBottomNavigation = getParamHasBottomNavigation();
        if (!hasBottomNavigation) {
            mBottomNavigationView.setVisibility(View.GONE);
        }

        return viewRoot;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INSTANCE_STATE_PAGER_POSITION, mViewPager.getCurrentItem());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViewPager();

        loadStep(getParamRecipeId(), getParamStepId(), savedInstanceState);
    }

    private void initializeViewPager() {
        mPagerAdapter = new StepsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ((StepFragment) mPagerAdapter.getItem(currentPosition)).pausePlayer();
                currentPosition = position;
                setNavigationButtonsVisibility(position);
                if (mListener != null) {
                    mListener.onStepSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void loadStep(long recipeId, final long selectedStepId, final Bundle savedInstanceState) {

            mViewModel.getSteps(recipeId).observe(this, new Observer<List<Step>>() {
                @Override
                public void onChanged(@Nullable List<Step> steps) {
                    if (steps != null) {
                        mPagerAdapter.setStepsData(steps);
                        if (savedInstanceState != null) {
                            int viewPagerPosition = savedInstanceState.getInt(INSTANCE_STATE_PAGER_POSITION);
                            mViewModel.initializeSelectedPosition(viewPagerPosition);
                        }
                        mViewModel.getSelectedPosition(selectedStepId).observe(StepViewPagerFragment.this, new Observer<Integer>() {
                            @Override
                            public void onChanged(@Nullable Integer selectedPosition) {
                                Timber.d("Selected ViewPager position: %d", selectedPosition);
                                if (selectedPosition != null) {
                                    mViewPager.setCurrentItem(selectedPosition, false);
                                    StepViewPagerFragment.this.setNavigationButtonsVisibility(selectedPosition);
                                }

                            }
                        });
                    }
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.bt_prev)
    public void onClickPrev(View view) {
        int position = mViewPager.getCurrentItem() - 1;

        selectPagerPosition(position);
    }

    @OnClick(R.id.bt_next)
    public void onClickNext(View view) {
        int position = mViewPager.getCurrentItem() + 1;

        selectPagerPosition(position);
    }

    public void selectPagerPosition(int position) {
        if (!(position < 0 || position > mPagerAdapter.getCount() - 1)) {
            mViewPager.setCurrentItem(position);
        }
    }

    public void setNavigationButtonsVisibility(Integer position) {
        if (position == 0) {
            mPrevButton.setVisibility(View.INVISIBLE);
        } else if (position == mPagerAdapter.getCount() - 1){
            mNextButton.setVisibility(View.INVISIBLE);
        } else {
            mPrevButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
        }
    }

    public class StepsPagerAdapter extends FragmentPagerAdapter {

        private List<Step> mSteps;

        private HashMap<Integer,StepFragment> mFragments;

        public StepsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        public void setStepsData(List<Step> steps) {
            mSteps = steps;
            mFragments = null;
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            if (mSteps != null) {
                Step step = mSteps.get(position);
                if (mFragments == null) {
                    mFragments = new HashMap<>();
                }
                if (!mFragments.containsKey(position)) {
                    mFragments.put(position, StepFragment.forStep(getParamRecipeId(),step.getId()));
                }
                return mFragments.get(position);
            }

            return null;
        }

        @Override
        public int getCount() {
            if (mSteps != null) {
                return mSteps.size();
            }

            return 0;
        }
    }
}
