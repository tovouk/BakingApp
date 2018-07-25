package com.josehinojo.bakingapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.josehinojo.bakingapp.Recipe.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListFragment extends Fragment {

    //ExoPlayer Implementation modeled after this link https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2

    public static final String ARG_STEP_ID = "step";
    private Step step;

    @BindView(R.id.video) PlayerView playerView;
    SimpleExoPlayer player;

    public StepListFragment(){}

    boolean playWhenReady = true;
    int window = 0;
    long playbackposition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        if(getActivity().getIntent().getExtras().containsKey(ARG_STEP_ID)){
            step = getActivity().getIntent().getExtras().getParcelable(ARG_STEP_ID);
        }else{
            step = getArguments().getParcelable(ARG_STEP_ID);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_STEP_ID,step);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail,container,false);
        if(step != null){
            ((TextView) rootView.findViewById(R.id.stepDescription)).setText(step.getDescription());
            playerView = rootView.findViewById(R.id.video);
            playerView.setPlayer(player);

        }
        return rootView;
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(window, playbackposition);
        Uri uri;
        if(step.getVideoUrl() != ""){
            uri = Uri.parse(step.getVideoUrl());
        }else if(step.getThumbnailUrl() != ""){
            uri = Uri.parse(step.getThumbnailUrl());
        }else{
            uri = Uri.parse("");
        }
        MediaSource mediaSource;

        if(TextUtils.isEmpty(uri.toString())){
            playerView.setVisibility(View.GONE);
        }else{
            mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
    private void releasePlayer() {
        if (player != null) {
            playbackposition = player.getCurrentPosition();
            window = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }
}
