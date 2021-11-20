package com.example.moviedb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class activity_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String id = intent.getStringExtra(MainActivity.ID);
        String title = intent.getStringExtra(MainActivity.TITLE);
        String release_date = intent.getStringExtra(MainActivity.RELEASE_DATE);
        String rating = intent.getStringExtra(MainActivity.RATING);
        String lang = intent.getStringExtra(MainActivity.LANG);
        String desc = intent.getStringExtra(MainActivity.DESC);
        String vote = intent.getStringExtra(MainActivity.VOTE);
        String pop = intent.getStringExtra(MainActivity.POP);

        TextView txttitle = findViewById(R.id.textTitle);
        TextView txtrelease = findViewById(R.id.textRelease);
        TextView txtrating = findViewById(R.id.textRating);
        TextView txtlang = findViewById(R.id.textLang);
        TextView txtdesc = findViewById(R.id.movie_desc);
        TextView txtid = findViewById(R.id.textID);
        TextView txtvote = findViewById(R.id.textVote);
        TextView txtpop = findViewById(R.id.textPop);

        txtid.setText(id);
        txttitle.setText(title);
        txtrelease.setText(release_date);
        txtrating.setText(rating);
        txtlang.setText(lang);
        txtdesc.setText(desc);
        txtvote.setText(vote);
        txtpop.setText(pop);
    }
}