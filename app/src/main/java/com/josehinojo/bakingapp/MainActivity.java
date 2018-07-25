package com.josehinojo.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.josehinojo.bakingapp.Network.RecipeAsyncTask;
import com.josehinojo.bakingapp.Recipe.ParcelableRecipe;
import com.josehinojo.bakingapp.Recipe.RecipeListAdapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.ListItemClickListener{

    private static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String RECIPE_LIST = "recipeList";

    private static ArrayList<ParcelableRecipe> recipeList = new ArrayList<>();
    public static RecipeListAdapter recipeAdapter;

    @BindView(R.id.mainRecyclerView)
    RecyclerView mainRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recipeAdapter = new RecipeListAdapter(recipeList,getApplicationContext(),this);

        //from https://stackoverflow.com/questions/9279111/determine-if-the-device-is-a-smartphone-or-tablet
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        RecyclerView.LayoutManager layoutManager;
        if(isTablet){
            layoutManager = new GridLayoutManager(getApplicationContext(),3);
        }else{
            layoutManager = new LinearLayoutManager(this,1,false);
        }

        mainRecyclerView.setLayoutManager(layoutManager);
        mainRecyclerView.setAdapter(recipeAdapter);
        recipeList.clear();
        try {
            URL url = new URL(RECIPE_URL);
            RecipeAsyncTask recipeAsyncTask = new RecipeAsyncTask();
            recipeAsyncTask.setContext(getApplicationContext());
            recipeAsyncTask.setRecipeParams(recipeAdapter,recipeList);
            recipeAsyncTask.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onListItemClick(ParcelableRecipe recipe) {
        Intent intent = new Intent(getApplicationContext(),StepListActivity.class);
        intent.putExtra("recipe",recipe);
        startActivity(intent);
    }
}
