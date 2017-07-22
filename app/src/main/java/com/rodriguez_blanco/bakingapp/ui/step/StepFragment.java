/*
 * Copyright (c) 2017. Gonzalo Rodriguez Blanco
 */

package com.rodriguez_blanco.bakingapp.ui.step;

import android.arch.lifecycle.LifecycleFragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.rodriguez_blanco.bakingapp.R;
import com.rodriguez_blanco.bakingapp.ui.recipe.RecipeAdapter;
import com.rodriguez_blanco.bakingapp.viewmodel.RecipeViewModel;
import com.rodriguez_blanco.bakingapp.viewmodel.StepViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.URI;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class StepFragment extends LifecycleFragment implements ExoPlayer.EventListener {
    private static final String PARAM_RECIPE_ID = "param_recipe_id";
    private static final String PARAM_STEP_ID = "param_step_id";

    @Inject
    StepViewModel mViewModel;

    @BindView(R.id.video_player_view)
    SimpleExoPlayerView mVideoPlayerView;
    SimpleExoPlayer mExoPlayer;

    @BindView(R.id.step_instructions)
    TextView mStepInstructions;

    private Unbinder unbinder;

    public StepFragment() {
    }

    public static StepFragment forStep(long recipeId, long stepId) {
        final StepFragment recipeFragment = new StepFragment();
        final Bundle arguments = new Bundle();
        arguments.putLong(PARAM_RECIPE_ID, recipeId);
        arguments.putLong(PARAM_STEP_ID, stepId);
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

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_step, container, false);

        unbinder = ButterKnife.bind(this, viewRoot);

        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadStep(getParamRecipeId(), getParamStepId());
    }

    private void loadStep(long recipeId, long stepId) {

            mViewModel.getStep(recipeId, stepId).observe(this, step -> {
                if (step == null) {
//                    showUnknownError();
                } else {
                    mStepInstructions.setText(step.getDescription());
                    String videoUrl = step.getVideoUrl();
                    String thumbnailUrl = step.getThumbnailUrl();

                    if (videoUrl != null && (videoUrl.length() > 0)) {
                        initializePlayer(Uri.parse(videoUrl));
                    } else if (thumbnailUrl != null && (thumbnailUrl.length() > 0)){
                        initializePlayer(Uri.parse(thumbnailUrl));
                    } else {
                        mVideoPlayerView.setVisibility(View.GONE);
                    }
//                    hideLoadingIndicator();
                }

            });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        unbinder.unbind();
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            Context context = getActivity();
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory rf = new DefaultRenderersFactory(context);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(rf, trackSelector, loadControl);
            mVideoPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(context, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /** ExoPLayer events **/
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
