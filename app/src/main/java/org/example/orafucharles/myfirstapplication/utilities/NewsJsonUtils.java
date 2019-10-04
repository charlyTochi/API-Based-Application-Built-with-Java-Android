package org.example.orafucharles.myfirstapplication.utilities;

import android.text.TextUtils;

import org.example.orafucharles.myfirstapplication.model.News;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class NewsJsonUtils {

    /**
     * The date string returned in the json response is in DateTime format
     * This method converts the DateTime string to a date string
     *
     * @param dateTime The DateTime string
     * @return The date string of the news item
     */
    private static String simpleDate(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(date);
    }

    /**
     * Return a list of News objects from the Json response passed in
     *
     * @param newsJsonResponse the String response from the http url
     * @return the list of News objects
     */
    public static List<News> extractNewsFromJson(String newsJsonResponse) {

        //Return null if there is no Json response
        if (TextUtils.isEmpty(newsJsonResponse)) {
            return null;
        }

        //Create an empty array list that all news will be added to
        List<News> newsList = new ArrayList<>();

        //Create a JSONObject from the response string and extract fields
        try {
            JSONObject rootObject = new JSONObject(newsJsonResponse);

            //Get response JSONObject
            JSONObject responseObject = rootObject.optJSONObject("response");

            //Get results JSONArray
            JSONArray resultsArray = responseObject.optJSONArray("results");

            //Iterate through every object in the results array
            for (int i = 0; i < resultsArray.length(); i++) {

                //Get the News object at position i
                JSONObject currentNewsItem = resultsArray.optJSONObject(i);

                //Extract section name
                String sectionName = currentNewsItem.optString("sectionName");

                //Extract the web title of the news item
                String webTitle = currentNewsItem.optString("webTitle");

                //Extract the web url of the news item
                String webUrl = currentNewsItem.optString("webUrl");

                //Extract the publication date of the news item
                String dateTimeString = currentNewsItem.optString("webPublicationDate");
                String webPublicationDate = simpleDate(dateTimeString);

                //Add the news item to the list of news
                newsList.add(new News(sectionName, webTitle, webUrl, webPublicationDate));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newsList;
    }
}
