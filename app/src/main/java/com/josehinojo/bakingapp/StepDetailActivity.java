package com.josehinojo.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.josehinojo.bakingapp.Recipe.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {

    Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(StepListFragment.ARG_STEP_ID,
                    getIntent().getParcelableExtra(StepListFragment.ARG_STEP_ID));
            StepListFragment fragment = new StepListFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.steps_detail_container, fragment)
                    .commit();
        }


    }


}
