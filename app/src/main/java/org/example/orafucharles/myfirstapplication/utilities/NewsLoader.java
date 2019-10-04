package org.example.orafucharles.myfirstapplication.utilities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;


import org.example.orafucharles.myfirstapplication.model.News;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {


    //Class constructor
    public NewsLoader(@NonNull Context context) {
        super(context);

    }

    /**
     * Invoked by the LoaderManager when the loader starts
     */
    @Override
    protected void onStartLoading() {
        forceLoad();

    }

    /**
     * Connects to the network and makes The Guardian API request on a background thread
     *
     * @return A list of News objects
     */
    @Nullable
    @Override
    public List<News> loadInBackground() {
        String jsonResponse = null;

        URL queryUrl = NetworkUtils.buildQueryUrl();

        try {
            jsonResponse = NetworkUtils.getResponseFromHttpUrl(queryUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return NewsJsonUtils.extractNewsFromJson(jsonResponse);
    }

}
