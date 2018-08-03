package com.josehinojo.bakingapp;

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
        }else{
            step = savedInstanceState.getParcelable(StepListFragment.ARG_STEP_ID);
            stepList = savedInstanceState.getParcelableArrayList("stepList");
        }
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


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("step",step);
        outState.putParcelableArrayList("stepList",stepList);
    }
}
