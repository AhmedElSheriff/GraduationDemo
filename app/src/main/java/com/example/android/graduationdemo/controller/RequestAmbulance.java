package com.example.android.graduationdemo.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.graduationdemo.Firebase.FirebaseHandler;
import com.example.android.graduationdemo.MainActivity;
import com.example.android.graduationdemo.R;
import com.example.android.graduationdemo.callbacks.AddLatLng;
import com.example.android.graduationdemo.data.PendingRequests;
import com.example.android.graduationdemo.view.MyEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RequestAmbulance extends AppCompatActivity {

    private Button mAttachPhoto;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private ProgressDialog mProgressDialog;
    private MyEditText numberOfInjsEditText;
    private Button sendRequestBtn;
    private String userEmail;
    private String mNumberOfInjs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ambulance);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);


        Bundle extras = getIntent().getExtras();
        final double lat = extras.getDouble("lat");
        final double lng = extras.getDouble("lng");
        Log.e("Location Lat",Double.toString(lat));
        Log.e("Location lng",Double.toString(lng));
        numberOfInjsEditText = (MyEditText) findViewById(R.id.injuriesInput);

        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        imageView = (ImageView) findViewById(R.id.imageview);
        mAttachPhoto = (Button) findViewById(R.id.attachbtn);
        mAttachPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        sendRequestBtn = (Button) findViewById(R.id.send_btn);
        sendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberOfInjs = numberOfInjsEditText.getText().toString();
                if(mNumberOfInjs.length() <= 0)
                {
                    Toast.makeText(RequestAmbulance.this,"Please enter the injuries number!",Toast.LENGTH_SHORT).show();
                    return;
                }
                PendingRequests request = new PendingRequests();
                request.setLatPosition(Double.toString(lat));
                request.setLongPosition(Double.toString(lng));
                request.setNumberOfInjuries(mNumberOfInjs);
                FirebaseHandler.addNewRequest(request, userEmail, new AddLatLng() {
                    @Override
                    public void onAdded() {
                        Toast.makeText(getApplicationContext(),"Request Sent",Toast.LENGTH_SHORT).show();
                        RequestAmbulance.this.finish();
                        startActivity(new Intent(RequestAmbulance.this, MainActivity.class));
                    }

                    @Override
                    public void onFailed(String exception) {
                        Toast.makeText(getApplicationContext(), "Faild to send request", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(photo);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }



}
