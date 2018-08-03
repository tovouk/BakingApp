package com.josehinojo.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.josehinojo.bakingapp.Recipe.Ingredient;
import com.josehinojo.bakingapp.Recipe.ParcelableRecipe;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class MainAppWidget extends AppWidgetProvider {

    static ParcelableRecipe recipe;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,ParcelableRecipe recipe1) {

        String str = "";
        // Construct the RemoteViews object

        Intent intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.main_app_widget);
        ArrayList<Ingredient> ingredients= recipe1.getIngredients();
        for(int i =0;i<ingredients.size();i++){
            Ingredient ingredient = ingredients.get(i);
            str += ingredient.getQuantity() + " " + ingredient.getUnitOfMeasurement() + " " + ingredient.getIngredient() + "\n";
        }
        views.setTextViewText(R.id.appwidget_text,str);
        views.setOnClickPendingIntent(R.id.appwidget_text,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
            UpdateWidgetService.startActionSetText(context,recipe);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,
                                          ParcelableRecipe recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

