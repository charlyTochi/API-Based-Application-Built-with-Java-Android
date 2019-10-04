package org.example.orafucharles.myfirstapplication.utilities;

import android.net.Uri;


import org.example.orafucharles.myfirstapplication.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

class NetworkUtils {

    //The Guardian endpoint url for content
    private static final String ENDPOINT_URL = "https://content.guardianapis.com/search";

    //API key from The Guardian
    private static final String API_KEY = BuildConfig.NEWS_API;

    //Query parameters
    private static final String PARAM_API_KEY = "api-key";
    private static final String PARAM_QUERY_TERM = "q";
    private static final String VALUE_SEARCH_CONTENT = "Technology";
    private static final String PARAM_ORDER_BY = "order-by";
    private static final String VALUE_ORDER_BY = "newest";
    private static final String PARAM_PAGE_SIZE = "page-size";
    private static final String VALUE_PAGE_SIZE = "20";

    /**
     * Returns query url using the content endpoint
     * from The Guardian API
     */
    public static URL buildQueryUrl() {
        URL queryUrl = null;

        //Create Uri using the specified endpoint and query parameters
        Uri builtUri = Uri.parse(ENDPOINT_URL)
                .buildUpon()
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_QUERY_TERM, VALUE_SEARCH_CONTENT)
                .appendQueryParameter(PARAM_ORDER_BY, VALUE_ORDER_BY)
                .appendQueryParameter(PARAM_PAGE_SIZE, VALUE_PAGE_SIZE)
                .build();

        //Create URL from the built Uri
        try {
            queryUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return queryUrl;

    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading.
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
