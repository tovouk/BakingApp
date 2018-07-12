package com.josehinojo.bakingapp.Network;

import android.os.AsyncTask;

import java.net.URL;

public class RecipeAsyncTask extends AsyncTask<URL, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(URL... urls) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
