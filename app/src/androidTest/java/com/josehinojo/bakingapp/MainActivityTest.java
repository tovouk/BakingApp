package com.josehinojo.bakingapp;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.josehinojo.bakingapp.Recipe.Ingredient;
import com.josehinojo.bakingapp.Recipe.ParcelableRecipe;
import com.josehinojo.bakingapp.Recipe.Step;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecyclerViewItem(){


        Intent recipeDetails = new Intent(testRule.getActivity().getApplicationContext(),StepListActivity.class);
        ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
        ArrayList<Step> stepArrayList = new ArrayList<>();
        populateIngredients(ingredientArrayList);
        populateSteps(stepArrayList);
        ParcelableRecipe recipe = new ParcelableRecipe(1,"Nutella Pie",ingredientArrayList,
                stepArrayList,8,"");
        recipeDetails.putExtra("recipe",recipe);
        onView(withId(R.id.mainRecyclerView)).perform(actionOnItemAtPosition(1,click()));
        String str = "";
        for (int i = 0; i<recipe.getIngredients().size();i++){
            Ingredient ingredient = recipe.getIngredients().get(i);
            str += ingredient.getQuantity() + " " + ingredient.getUnitOfMeasurement() + " " + ingredient.getIngredient() + "\n";
        }
        onView(withId(R.id.ingredientsList)).check(matches(withText(str)));




    }

    void populateIngredients(ArrayList<Ingredient> ingredientArrayList){
        ingredientArrayList.add(new Ingredient(350,"G","Bittersweet chocolate (60-70% cacao)"));
        ingredientArrayList.add(new Ingredient(226,"G","unsalted butter"));
        ingredientArrayList.add(new Ingredient(300,"G","granulated sugar"));
        ingredientArrayList.add(new Ingredient(100,"G","light brown sugar"));
        ingredientArrayList.add(new Ingredient(5,"UNIT","large eggs"));
        ingredientArrayList.add(new Ingredient(1,"TBLSP","vanilla extract"));
        ingredientArrayList.add(new Ingredient(140,"G","all purpose flour"));
        ingredientArrayList.add(new Ingredient(40,"G","cocoa powder"));
        ingredientArrayList.add(new Ingredient(1.5,"TSP","salt"));
        ingredientArrayList.add(new Ingredient(350,"G","semisweet chocolate chips"));
    }

    void populateSteps(ArrayList<Step> stepArrayList){
        stepArrayList.add(new Step(0,"Recipe Introduction","Recipe Introduction","https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc33_-intro-brownies/-intro-brownies.mp4",""));
    }
}
