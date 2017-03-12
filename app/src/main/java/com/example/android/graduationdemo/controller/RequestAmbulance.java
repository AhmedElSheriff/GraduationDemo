package com.example.android.graduationdemo.controller;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.graduationdemo.Firebase.FirebaseHandler;
import com.example.android.graduationdemo.R;
import com.example.android.graduationdemo.callbacks.AddLatLng;
import com.example.android.graduationdemo.data.PendingRequests;
import com.example.android.graduationdemo.utilities.Utilites;
import com.example.android.graduationdemo.view.MyEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RequestAmbulance extends AppCompatActivity {

    private Button mAttachPhoto;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int INITIAL_REQUEST = 1337;
    private LocationManager mLocationManager;
    private ProgressDialog mProgressDialog;
    private MyEditText numberOfInjsEditText;
    private Button sendRequestBtn;
    private String mNumberOfInjs;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private Context context;
    private String userEmail;
    private static final int REQUEST_LOCATION = 0x2;
    private static final String[] INITIAL_PERMS = {

            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ambulance);
        context = RequestAmbulance.this;
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);


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
                try {
                    Log.e("LOGTAG","Send Request TRY");
                    getLocation();
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                    Log.e("LOGTAG","Send Request Exception");
                }
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(photo);
        }

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i("TAG", "User agreed to make required location settings changes.");
                        requestLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("TAG", "User chose not to make required location settings changes.");
                        Toast.makeText(this,"Location must be turned On",Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        break;
                }
                break;

        }
    }


    private void getLocation() throws Settings.SettingNotFoundException {

        Log.e("LOGTAG","Send Request getLocation");
        mNumberOfInjs = numberOfInjsEditText.getText().toString();

        int off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        if (off == 0) {
//            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            Toast.makeText(this, "Location must be turned On!", Toast.LENGTH_SHORT).show();
//            startActivity(onGPS)
            Utilites.displayLocationSettingsRequest(context,RequestAmbulance.this);
            Log.e("LOGTAG","Send Request Location Off");

                }
        else {
            Log.e("LOGTAG","Send Request Request Location");
            requestLocation();
        }

        }

    private void requestLocation()
    {

        Log.e("LOGTAG","Send Request Inside Request Location");
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(RequestAmbulance.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(RequestAmbulance.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RequestAmbulance.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
            return;
        }

        Log.e("LOGTAG","Send Request Location Manager");

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        mProgressDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation();
                } else
                    return;
            }
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            PendingRequests request = new PendingRequests();
            request.setLatPosition(Double.toString(location.getLatitude()));
            request.setLongPosition(Double.toString(location.getLongitude()));
            request.setNumberOfInjuries(mNumberOfInjs);
            Log.e("LOGTAG","Send Request Inside Location Listener");
            FirebaseHandler.addNewRequest(request, userEmail,new AddLatLng() {
                @Override
                public void onAdded() {
                    Toast.makeText(getApplicationContext(),"Added successfuly",Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }

                @Override
                public void onFailed(String exception) {
                    Toast.makeText(getApplicationContext(), "Faild To Add", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
            if (ActivityCompat.checkSelfPermission(RequestAmbulance.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(RequestAmbulance.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mLocationManager.removeUpdates(mLocationListener);
            mLocationManager = null;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };



}
