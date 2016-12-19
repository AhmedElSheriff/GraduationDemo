package com.example.android.graduationdemo.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.graduationdemo.MapsActivity;
import com.example.android.graduationdemo.R;

public class Backoffice extends AppCompatActivity {


    private FragmentManager fm;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backoffice);

        fm = getSupportFragmentManager();
        fragment = new MapsActivity();
        fm.beginTransaction().replace(R.id.backoffice_map_frame,fragment).commit();
    }
}
