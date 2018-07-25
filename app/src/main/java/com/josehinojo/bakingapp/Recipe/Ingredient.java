package com.josehinojo.bakingapp.Recipe;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{

    private double quantity;
    private String unitOfMeasurement;
    private String ingredient;

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public String getIngredient() {
        return ingredient;
    }

    public Ingredient(){

    }

    public Ingredient(double quantity,String unitOfMeasurement,String ingredient){
        this.quantity = quantity;
        this.unitOfMeasurement = unitOfMeasurement;
        this.ingredient = ingredient;
    }

    private Ingredient(Parcel in){
        quantity = in.readDouble();
        unitOfMeasurement = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(unitOfMeasurement);
        dest.writeString(ingredient);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>(){
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
