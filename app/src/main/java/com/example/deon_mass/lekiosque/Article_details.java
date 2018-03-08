package com.example.deon_mass.lekiosque;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import NetClasses.Net_downloadImage;

public class Article_details extends AppCompatActivity {

    /**
     * The component "CollapsingToolbarLayout" will hepl us to set background for a big view of a selected article
     */
    CollapsingToolbarLayout Background;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        img = (ImageView) findViewById(R.id.img);
        Intent i = getIntent();
        String Title = i.getStringExtra("ARTICLE_TITRE");
        String url = i.getStringExtra("ARTICLE_URL");
        String id = i.getStringExtra("id_notice");
        setTitle(Title);
        new Net_downloadImage(Article_details.this , img).execute(url, id+".jpg");




    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }




}
