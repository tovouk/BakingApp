package com.josehinojo.bakingapp.Recipe;

public class Ingredient {

    private int quantity;
    private String unitOfMeasurement;
    private String ingredient;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public String getIngredient() {
        return ingredient;
    }
}
