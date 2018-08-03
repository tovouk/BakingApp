package com.josehinojo.bakingapp.Network;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.josehinojo.bakingapp.MainAppWidget;
import com.josehinojo.bakingapp.Recipe.Ingredient;
import com.josehinojo.bakingapp.Recipe.ParcelableRecipe;
import com.josehinojo.bakingapp.Recipe.RecipeListAdapter;
import com.josehinojo.bakingapp.Recipe.Step;
import com.josehinojo.bakingapp.UpdateWidgetService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class RecipeAsyncTask extends AsyncTask<URL, Void, String> {

    Context context;
    RecipeListAdapter recipeListAdapter;
    ArrayList<ParcelableRecipe> recipes = new ArrayList<ParcelableRecipe>();

    public void setContext(Context context){
        this.context = context;
    }

    public void setRecipeParams(RecipeListAdapter recipeListAdapter, ArrayList<ParcelableRecipe> recipes){
        this.recipeListAdapter = recipeListAdapter;
        this.recipes = recipes;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL movieURL = urls[0];
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection)movieURL.openConnection();
            InputStream in = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else{
                Log.e("Error: ","no data was retrieved");
                return null;
            }
        }catch(FileNotFoundException fileNotFoundException){
            return "Error!";
        } catch (IOException e) {
            Log.e("Error: ","connection cannot be established");
            e.printStackTrace();
        }finally {
            assert httpURLConnection != null;
            httpURLConnection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try{
            JSONArray results = new JSONArray(s);
            recipes.clear();
            for(int i =0;i<results.length();i++){
                JSONObject recipe = results.getJSONObject(i);
                int id = recipe.getInt("id");
                String name = recipe.getString("name");
                int servings = recipe.getInt("servings");
                String image = recipe.getString("image");
                //ingredients
                JSONArray jsonIngredients = new JSONArray(recipe.getString("ingredients"));
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                for (int j=0;j<jsonIngredients.length();j++){
                    JSONObject ingredient = jsonIngredients.getJSONObject(j);
                    double quantity = ingredient.getDouble("quantity");
                    String measure = ingredient.getString("measure");
                    String ingredientName = ingredient.getString("ingredient");
                    Ingredient ing = new Ingredient(quantity,measure,ingredientName);
                    ingredients.add(ing);
                }
                //steps
                JSONArray jsonSteps = new JSONArray(recipe.getString("steps"));
                ArrayList<Step> steps = new ArrayList<>();
                for (int k = 0;k<jsonSteps.length();k++){
                    //gets a step JSONOBject
                    JSONObject step = jsonSteps.getJSONObject(k);
                    int stepId = step.getInt("id");
                    String shortDesc = step.getString("shortDescription");
                    String desc = step.getString("description");
                    String vidUrl = step.getString("videoURL");
                    String thumbUrl = step.getString("thumbnailURL");
                    Step step1 = new Step(stepId,shortDesc,desc,vidUrl,thumbUrl);
                    steps.add(step1);

                }
                ParcelableRecipe parcelableRecipe = new ParcelableRecipe(id,name,ingredients,steps,servings,image);
                recipes.add(parcelableRecipe);
                recipeListAdapter.notifyDataSetChanged();
            }
            UpdateWidgetService.handleWidgetUpdate(recipes.get(recipes.size()-1),context);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
