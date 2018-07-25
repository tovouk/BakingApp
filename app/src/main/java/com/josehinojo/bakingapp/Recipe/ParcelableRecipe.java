package com.josehinojo.bakingapp.Recipe;

import android.os.Parcel;
import android.os.Parcelable;

import com.josehinojo.bakingapp.Recipe.Ingredient;
import com.josehinojo.bakingapp.Recipe.Step;

import java.util.ArrayList;

public class ParcelableRecipe implements Parcelable {

    private int id;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private int servings;
    private String image;

    public ParcelableRecipe(){
        ingredients = new ArrayList<Ingredient>();
        steps = new ArrayList<Step>();
    }

    public ParcelableRecipe(int id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, int servings, String image){
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    private ParcelableRecipe(Parcel in){
        this();
        id = in.readInt();
        name = in.readString();
        //learned the proper way to do this from: https://stackoverflow.com/questions/6774645/android-how-to-use-readtypedlist-method-correctly-in-a-parcelable-class
        in.readTypedList(getIngredients(),Ingredient.CREATOR);
        in.readTypedList(getSteps(),Step.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        //learned the proper way to do this from: https://stackoverflow.com/questions/6774645/android-how-to-use-readtypedlist-method-correctly-in-a-parcelable-class
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static final Parcelable.Creator<ParcelableRecipe> CREATOR = new Parcelable.Creator<ParcelableRecipe>(){

        @Override
        public ParcelableRecipe createFromParcel(Parcel source) {
            return new ParcelableRecipe(source);
        }

        @Override
        public ParcelableRecipe[] newArray(int size) {
            return new ParcelableRecipe[size];
        }
    };
}
