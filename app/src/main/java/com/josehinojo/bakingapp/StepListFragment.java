package com.josehinojo.bakingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.josehinojo.bakingapp.Recipe.Step;

import java.util.ArrayList;

public class StepListFragment extends Fragment {

    //ExoPlayer Implementation modeled after this link https://codelabs.developers.google.com/codelabs/exoplayer-intro/#2
    //as lesson code is deprecated at the time of writing

    public static final String ARG_STEP_ID = "step";
    private Step step;
    ArrayList<Step> stepList = new ArrayList<>();

    PlayerView playerView;
    SimpleExoPlayer player;

    public StepListFragment(){}

    boolean playWhenReady = true;
    int window = 0;
    long playbackposition = 0;
    boolean tablet;
    int rootviewWidth;
    int rootViewHeight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        if(getActivity().getIntent().getExtras().containsKey(ARG_STEP_ID)){
            step = getActivity().getIntent().getExtras().getParcelable(ARG_STEP_ID);
            stepList = getActivity().getIntent().getExtras().getParcelableArrayList("stepList");
            tablet = false;
        }else{
            step = getArguments().getParcelable(ARG_STEP_ID);
            stepList = getArguments().getParcelableArrayList("stepList");
            tablet = true;
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
            rootviewWidth = rootView.getWidth();
            rootViewHeight = rootView.getHeight();
            playerView.setPlayer(player);
            Button nextButton = rootView.findViewById(R.id.nextButton);
            Button prevButton = rootView.findViewById(R.id.prevButton);
            for (int i = 0;i<stepList.size();i++){
                if(stepList.get(i).getId() == step.getId()){
                    if(i != 0){
                        step.setPrevStep(stepList.get(i-1));
                    }else{
                        prevButton.setVisibility(View.INVISIBLE);
                    }
                    if(i < stepList.size()-1){
                        step.setNextStep(stepList.get(i+1));
                    }else {
                        nextButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tablet) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(StepListFragment.ARG_STEP_ID, step.getNextStep());
                        arguments.putParcelableArrayList("stepList",stepList);
                        StepListFragment fragment = new StepListFragment();
                        fragment.setArguments(arguments);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.steps_detail_container, fragment)
                                .commit();
                    }else {
                        Intent intent = new Intent(getContext(), StepDetailActivity.class);
                        intent.putExtra(StepListFragment.ARG_STEP_ID, step.getNextStep());
                        intent.putParcelableArrayListExtra("stepList", stepList);
                        startActivity(intent);
                    }
                }
            });
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tablet) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(StepListFragment.ARG_STEP_ID, step.getPrevStep());
                        arguments.putParcelableArrayList("stepList",stepList);
                        StepListFragment fragment = new StepListFragment();
                        fragment.setArguments(arguments);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.steps_detail_container, fragment)
                                .commit();
                    }else {

                        Intent intent = new Intent(getContext(), StepDetailActivity.class);
                        intent.putExtra(StepListFragment.ARG_STEP_ID, step.getPrevStep());
                        intent.putParcelableArrayListExtra("stepList", stepList);
                        startActivity(intent);
                    }
                }
            });
        }
        return rootView;
    }



    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        playerView.setPlayer(player);
        if(!tablet){
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        }
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(window, playbackposition);
        Uri uri = Uri.parse("");
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo != null && netInfo.isConnected()) {
            if (!step.getVideoUrl().equals("")) {
                uri = Uri.parse(step.getVideoUrl());
            } else if (!step.getThumbnailUrl().equals("")) {
                uri = Uri.parse(step.getThumbnailUrl());
            }
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

    //The following two methods originate from https://medium.com/fungjai/playing-video-by-exoplayer-b97903be0b33
    //The purpose is to make the video fullscreen on landscape mode
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentOrientation = getResources().getConfiguration().orientation;
        if(tablet){
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                hideSystemUiFullScreen();

            }

        }else{
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                setPlayerViewDimens(1);
                hideSystemUiFullScreen();
            } else {
                setPlayerViewDimens(3);
            }
        }


    }

    @SuppressLint("InlinedApi")
    private void hideSystemUiFullScreen() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public void setPlayerViewDimens(int dividedBy){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        playerView.setMinimumWidth(size.x);
        playerView.setMinimumHeight(size.y / dividedBy);

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("Baking App")).
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
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
