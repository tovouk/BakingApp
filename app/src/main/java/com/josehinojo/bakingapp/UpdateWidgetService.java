package com.josehinojo.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.josehinojo.bakingapp.Recipe.ParcelableRecipe;

public class UpdateWidgetService extends IntentService {

    public static final String SET_TEXT = "com.josehinojo.bakingapp.action.SET_TEXT";

    public UpdateWidgetService(){
        super("UpdateWidgetService");
    }

    public static void startActionSetText(Context context, ParcelableRecipe recipe){
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(SET_TEXT);
        intent.putExtra("recipe",recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (SET_TEXT.equals(action)) {
                ParcelableRecipe recipe = intent.getExtras().getParcelable("recipe");
                handleWidgetUpdate(recipe,getApplicationContext());
            }
        }
    }

    public static void handleWidgetUpdate(ParcelableRecipe recipe,Context context){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, MainAppWidget.class));
        MainAppWidget.updateAppWidgets(context,appWidgetManager,appWidgetIds,recipe);
    }

}
