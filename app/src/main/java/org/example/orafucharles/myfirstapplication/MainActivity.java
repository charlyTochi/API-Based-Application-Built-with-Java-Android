package org.example.orafucharles.myfirstapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.example.orafucharles.myfirstapplication.contract.db.UserReaderDbHelper;

public class MainActivity extends AppCompatActivity {

    UserReaderDbHelper myDb;
    CardView mProfile;
    CardView mWeather;
    CardView mMovies;
    CardView mNews;
    CardView mLyrics;
    CardView mLogout;
    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new UserReaderDbHelper(this);


        mProfile = findViewById(R.id.profileButton);
        mWeather = findViewById(R.id.weatherButton);
        mMovies = findViewById(R.id.movies);
        mLyrics = findViewById(R.id.lyrics);
        mNews = findViewById(R.id.news);
        mLogout = findViewById(R.id.logout);
        viewAll();

        preferences = getSharedPreferences("table", MODE_PRIVATE);

        String tableVal = preferences.getString("table_data", "No value");

//        this helps to indicate that we are making use of a customized toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);







        mMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mediaPage = new Intent(MainActivity.this, MovieList.class);
                startActivity(mediaPage);
//                finish();
            }
        });




        mWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weatherPage = new Intent(MainActivity.this, Weather.class);
                startActivity(weatherPage);
//                finish();
            }
        });



        mNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsPage = new Intent(MainActivity.this, LatestNews.class);
                startActivity(newsPage);
//                finish();
            }
        });



        mLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lyricsPage = new Intent(MainActivity.this, Lyrics.class);
                startActivity(lyricsPage);
//                finish();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences =getSharedPreferences("loginPrefs",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();
                startActivity(new Intent(MainActivity.this, Login.class));
                Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();

            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.logout:
                        SharedPreferences preferences =getSharedPreferences("loginPrefs",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        finish();
                        startActivity(new Intent(this, Login.class ));
        return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void viewAll() {
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilePage = new Intent(MainActivity.this, Profile.class);
                startActivity(profilePage);

            }
        });

    }


}
