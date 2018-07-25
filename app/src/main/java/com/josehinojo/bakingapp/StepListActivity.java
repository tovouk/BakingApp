package com.josehinojo.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josehinojo.bakingapp.Recipe.Ingredient;
import com.josehinojo.bakingapp.Recipe.ParcelableRecipe;
import com.josehinojo.bakingapp.Recipe.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    @BindView(R.id.ingredientsList)
    TextView ingredientsView;
    @BindView(R.id.step_list) RecyclerView stepList;
    ParcelableRecipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        ButterKnife.bind(this);
        recipe = getIntent().getParcelableExtra("recipe");
        setTitle(recipe.getName());
        String str = "";
        for (int i = 0; i<recipe.getIngredients().size();i++){
            Ingredient ingredient = recipe.getIngredients().get(i);
            str += ingredient.getQuantity() + " " + ingredient.getUnitOfMeasurement() + " " + ingredient.getIngredient() + "\n";
        }
        ingredientsView.setText(str);
        if (findViewById(R.id.steps_detail_container) != null) {
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, recipe.getSteps(), mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<StepListActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final StepListActivity mParentActivity;
        private final ArrayList<Step> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step step = (Step) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(StepListFragment.ARG_STEP_ID, step);
                    StepListFragment fragment = new StepListFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.steps_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepDetailActivity.class);
                    intent.putExtra(StepListFragment.ARG_STEP_ID, step);

                    context.startActivity(intent);
                }
            }
        };

        public SimpleItemRecyclerViewAdapter(StepListActivity parent,
                                      ArrayList<Step> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public StepListActivity.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new StepListActivity.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final StepListActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.step_num.setText(String.valueOf(mValues.get(position).getId() + 1));
            holder.step_description.setText(mValues.get(position).getShortDescription());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView step_num;
            final TextView step_description;

            ViewHolder(View view) {
                super(view);
                step_num = view.findViewById(R.id.step_num);
                step_description = view.findViewById(R.id.step_description);
            }
        }
    }

}
