package com.example.android.graduationdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.M)
public class ERActivity extends AppCompatActivity{


    LocationManager mLocationManager;
    Button mAddEr;
    TextView textView;
    private static final int INITIAL_REQUEST = 1337;
    private static final int CAMERA_REQUEST = INITIAL_REQUEST + 1;
    private String mLat = "";
    private String mLong = "";
    private ProgressDialog mProgressDialog;
    private FirebaseDatabase mFirebase;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static final String[] INITIAL_PERMS={

            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er);

        mFirebase = FirebaseHelper.getDatabase();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait");


        mAddEr = (Button) findViewById(R.id.add_request_btn);
        mAddEr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getLocation();
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    private void getLocation() throws Settings.SettingNotFoundException {

        int off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        if(off==0){
            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            Toast.makeText(this,"Location must be turned On!",Toast.LENGTH_SHORT).show();
            startActivity(onGPS);
        }


        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(INITIAL_PERMS,INITIAL_REQUEST);
            return;
        }


        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);


    }

    private void addLocationToFB(final String lat, final String lng)
    {

        Log.d("thirdLat",mLat);
        Log.d("thirdLng",mLong);
        FirebaseHelper.getDatabase().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> map = new HashMap<String, String>();
                    map.put("Latitude", lat);
                    map.put("Longtitude", lng);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            textView = (TextView) findViewById(R.id.testtxtview);
            textView.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
            mLat = Double.toString(location.getLatitude());
            mLong = Double.toString(location.getLongitude());
            Log.d("firstLat",mLat);
            Log.d("firstLng",mLong);
            if(!mLat.equals("") && !mLong.equals("")) {
                Log.d("secondLat",mLat);
                Log.d("secondLng",mLong);
                addLocationToFB(mLat, mLong);
            }

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
