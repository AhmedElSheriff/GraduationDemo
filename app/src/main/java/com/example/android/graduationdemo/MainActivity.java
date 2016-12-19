package com.example.android.graduationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.android.graduationdemo.controller.Backoffice;
import com.example.android.graduationdemo.controller.ERActivity;

public class MainActivity extends AppCompatActivity {

    Button mErButton;
    Button mBackofficeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErButton = (Button) findViewById(R.id.er_button);
        mBackofficeButton = (Button) findViewById(R.id.backoffice_button);

        mErButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ERActivity.class));
            }
        });

        mBackofficeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Backoffice.class));
            }
        });
    }
}
