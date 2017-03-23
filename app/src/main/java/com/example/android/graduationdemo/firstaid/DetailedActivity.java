package com.example.android.graduationdemo.firstaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.graduationdemo.R;
import com.example.android.graduationdemo.data.FirstAid;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        ImageView imageAid = (ImageView) findViewById(R.id.image_aid);
        TextView signAid = (TextView) findViewById(R.id.sign_aid);
        TextView cureAid = (TextView) findViewById(R.id.cure_aid);
        Intent i = getIntent();
        FirstAid firstAid= i.getParcelableExtra("data");
        imageAid.setImageResource(firstAid.getImage());
        signAid.setText(firstAid.getSign());
        cureAid.setText(firstAid.getCure());
    }
}
