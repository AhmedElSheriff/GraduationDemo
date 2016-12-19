package com.example.android.graduationdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.graduationdemo.Firebase.FirebaseHandler;
import com.example.android.graduationdemo.callbacks.GetLocationData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Fragment implements OnMapReadyCallback, GetLocationData{

    private GoogleMap mMap;
    private String mLat, mLong;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        FirebaseHandler.getEmergencyLocation(this);

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29.9556449,30.9134569),16.0f));
        LatLng car1 = new LatLng(29.9540458,30.9130367);
        LatLng car2 = new LatLng(29.9532644,30.9134779);
        LatLng car3 = new LatLng(29.9555664,30.9174799);
        LatLng case1 = new LatLng(29.955388,30.9146463);
        String lattest = mLat;
        String longtest = mLong;
       // LatLng case2 = new LatLng(Double.parseDouble(mLat),Double.parseDouble(mLong));
        BitmapDescriptor patient = BitmapDescriptorFactory.fromResource(R.drawable.patient);
        BitmapDescriptor badAmbulances = BitmapDescriptorFactory.fromResource(R.drawable.badambulance);
        BitmapDescriptor goodAmbulance = BitmapDescriptorFactory.fromResource(R.drawable.goodambulance);
        BitmapDescriptor busyAmbulances = BitmapDescriptorFactory.fromResource(R.drawable.busyambulance);



        mMap.addMarker(new MarkerOptions().position(car1).title("Good Ambulance").icon(goodAmbulance));
        mMap.addMarker(new MarkerOptions().position(car2).title("Bad Ambulance").icon(badAmbulances));
        mMap.addMarker(new MarkerOptions().position(car3).title("Busy Ambulance").icon(busyAmbulances));
        mMap.addMarker(new MarkerOptions().position(case1).title("Patient").icon(patient));





        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    @Override
    public void getLocationData(EmergencyLocation emergencyLocation) {
        mLat = emergencyLocation.getLatPosition();
        mLong = emergencyLocation.getLongPosition();
        LatLng case2 = new LatLng(Double.parseDouble(mLat),Double.parseDouble(mLong));
        mMap.addMarker(new MarkerOptions().position(case2).title("My Case").icon(BitmapDescriptorFactory.fromResource(R.drawable.patient)));
    }
}
