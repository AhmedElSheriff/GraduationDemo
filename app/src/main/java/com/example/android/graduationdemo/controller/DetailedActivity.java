package com.example.android.graduationdemo.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.graduationdemo.R;
import com.example.android.graduationdemo.data.FirstAid;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ImageView imageAid = (ImageView) findViewById(R.id.image_aid);

        TextView signAid = (TextView) findViewById(R.id.signs_aid);
        TextView cureAid = (TextView) findViewById(R.id.cure_aid);
        Intent i = getIntent();
        FirstAid firstAid= i.getParcelableExtra("data");
        String mTitle = i.getStringExtra("title");
        getSupportActionBar().setTitle(mTitle);
        imageAid.setImageResource(firstAid.getImage());
        signAid.setText(firstAid.getSign());
        cureAid.setText(firstAid.getCure());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
