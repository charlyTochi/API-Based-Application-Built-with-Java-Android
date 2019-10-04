package org.example.orafucharles.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Lyrics extends AppCompatActivity {

    EditText mTrack;
    EditText mText;
    TextView mResultTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics);

        mTrack = findViewById(R.id.track);
        mText = findViewById(R.id.artist);
        mResultTextView = findViewById(R.id.resultTextView);

//     DownloadTask task = new DownloadTask();
//     task.execute("https://orion.apiseeds.com/api/music/lyric/micheal%20jackson/billie%20jean?apikey=znPqZJS3vrdMLaCXqIgKdEIabKw85KKgfMAoVNfu4MMLXtcu9J9WBJ12MoC8xoGz");

    }

    public void getLyrics(View view) {

        try {


            DownloadTask task = new DownloadTask();
            String encodedTrackName = URLEncoder.encode(mTrack.getText().toString(), "UTF-8");
            String encodedTrackText = URLEncoder.encode(mText.getText().toString(), "UTF-8");
            task.execute("https://orion.apiseeds.com/api/music/lyric/"+ encodedTrackName +"/"+ encodedTrackText +"?apikey=znPqZJS3vrdMLaCXqIgKdEIabKw85KKgfMAoVNfu4MMLXtcu9J9WBJ12MoC8xoGz");
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert mgr != null;
            mgr.hideSoftInputFromWindow(mText.getWindowToken(), 0);
            mgr.hideSoftInputFromWindow(mTrack.getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not lyrics :(", Toast.LENGTH_SHORT).show();

        }

    }


    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }




        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject != null) {
                        String trackInfo = null;
                        String message = "";


                        try {

                            JSONObject result = jsonObject.getJSONObject("result");
                            JSONObject artist = result.getJSONObject("artist");
                            JSONObject track = result.getJSONObject("track");

                            String name = track.getString("name");
                            String name22 = artist.getString("name");
                            String lyrics = track.getString("text");
                            Log.i("name", name);
//                            Log.i("lyrics", lyrics);
                            Log.i("artist", name22);
                            Toast.makeText(getApplicationContext(), name22, Toast.LENGTH_SHORT).show();

                            if (!hasText(mTrack)) {
                                Toast.makeText(getApplicationContext(), "Empty value", Toast.LENGTH_SHORT).show();
                            }
                            if (!hasText(mText)) {
                                Toast.makeText(getApplicationContext(), "Empty value", Toast.LENGTH_SHORT).show();
                            }
                            //start a new activity
                            Intent intent = new Intent(Lyrics.this, ViewLyrics.class);
                            intent.putExtra("name", name);
                            intent.putExtra("lyrics", lyrics);
                            intent.putExtra("artist", name22);
//                            Toast.makeText(getApplicationContext(), name22, Toast.LENGTH_LONG).show();
                            startActivityForResult(intent, 2);
                            if (!name.equals("") && !lyrics.equals("")  && !name22.equals("")) {
                                message += name + ": " + lyrics + name22 +"\r\n";
                            }
                            if (!message.equals("")) {
//                        mResultTextView.setText(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Could not find lyrics :(", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(getApplicationContext(), "Not found :(", Toast.LENGTH_SHORT).show();

            }
        }
    }

    /* set edit text to error when empty */
    public static boolean hasText(EditText editText) {
        return hasText(editText, "Required");
    }

    /* check edit text length and set error message for required edit text
     * Custom Message */
    public static boolean hasText(EditText editText, String error_message) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(error_message);
            return false;
        }

        return true;
    }

}
