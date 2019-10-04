package org.example.orafucharles.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewLyrics extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lyrics);



        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String lyrics = intent.getStringExtra("lyrics");
        final String artist = intent.getStringExtra("artist");


        TextView textViewName = findViewById(R.id.title);
        textViewName.setText(name + "\n By \n" +artist);

        TextView textViewLyrics = findViewById(R.id.lyrics);
        textViewLyrics.setText(lyrics);
    }
}
