package com.example.android.graduationdemo.view;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.android.graduationdemo.FirstAidCustomAdapter;
import com.example.android.graduationdemo.R;
import com.example.android.graduationdemo.firstaid.FirstAidList;

import java.util.ArrayList;

public class FirstAidActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_aid);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new   AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == -collapsingToolbarLayout.getHeight() + toolbar.getHeight()) {
                    //toolbar is collapsed here
                    //write your code here
                    collapsingToolbarLayout.setTitle("Hello World");
                }
                else
                    collapsingToolbarLayout.setTitle(" ");
            }
        });


        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.firstaidrecyclerview);

        ArrayList<FirstAidList> arrayList = new ArrayList<>();
        FirstAidList obj = new FirstAidList();
        obj.setName("Allergies");
        obj.setImage(R.drawable.allergiesicon);
        arrayList.add(obj);
        obj = new FirstAidList();
        obj.setName("Asthma Attack");
        obj.setImage(R.drawable.asthmaicon2);
        arrayList.add(obj);
        obj = new FirstAidList();
        obj.setName("Asthma Attack");
        obj.setImage(R.drawable.asthmaicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Bleeding");
        obj.setImage(R.drawable.bleedingicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Broken Bones");
        obj.setImage(R.drawable.boneicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Burns");
        obj.setImage(R.drawable.burnsicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Choking");
        obj.setImage(R.drawable.chokingicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Chest Pain");
        obj.setImage(R.drawable.chestpainicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Diabets");
        obj.setImage(R.drawable.diabetesicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Heading");
        obj.setImage(R.drawable.headingicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Heat Stroke");
        obj.setImage(R.drawable.heatstrokeicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Muscle Injury");
        obj.setImage(R.drawable.muscleinjuryicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Nose Bleeding");
        obj.setImage(R.drawable.nosebleedicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Poisoning");
        obj.setImage(R.drawable.poisoningicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Stroke");
        obj.setImage(R.drawable.strokeicon);
        arrayList.add(obj);obj = new FirstAidList();
        obj.setName("Unconscious");
        obj.setImage(R.drawable.unconsciousicon);
        arrayList.add(obj);
        FirstAidCustomAdapter adapter = new FirstAidCustomAdapter(arrayList,getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }
}

