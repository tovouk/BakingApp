package com.josehinojo.bakingapp;

import android.app.PictureInPictureParams;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.josehinojo.bakingapp.Recipe.Step;

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity {

    Step step;
    ArrayList<Step> stepList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null) {
            step = getIntent().getParcelableExtra(StepListFragment.ARG_STEP_ID);
            stepList = getIntent().getParcelableArrayListExtra("stepList");
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(StepListFragment.ARG_STEP_ID,step);
            arguments.putParcelableArrayList("stepList",stepList);
            StepListFragment fragment = new StepListFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.steps_detail_container, fragment)
                    .commit();

        }else{

        }


    }

//    //per suggestion in first review to enable PIP mode from https://medium.com/google-developers/building-a-video-player-app-in-android-part-5-5-725c1ec2557a
//    //unable to figure out a parameter at time of typing
//    @Override
//    protected void onUserLeaveHint() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            //Having trouble figuring out how PictureInPictureParams.Builder works at 2 AM
//            //enterPictureInPictureMode(new PictureInPictureParams(16,9));
//        }else {
//            //super.onUserLeaveHint();
//        }
//        super.onUserLeaveHint();
//    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("step",step);
        outState.putParcelableArrayList("stepList",stepList);
    }
}
