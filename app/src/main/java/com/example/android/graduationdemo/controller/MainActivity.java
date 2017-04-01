package com.example.android.graduationdemo.controller;

import android.animation.Animator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.android.graduationdemo.R;
import com.example.android.graduationdemo.callbacks.AddUserImage;
import com.example.android.graduationdemo.callbacks.CheckImageExistance;
import com.example.android.graduationdemo.callbacks.GetUserData;
import com.example.android.graduationdemo.data.FirstAid;
import com.example.android.graduationdemo.data.User;
import com.example.android.graduationdemo.firebase.FirebaseHandler;
import com.example.android.graduationdemo.utilities.Utilites;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GetUserData, LocationListener {

    Button mRequestAmb;
    Button mBackofficeButton;
    private GoogleMap mMap;
    private String mLat, mLong;
    private Marker marker = null;
    private FirebaseAuth mAuth;
    private String userEmail;
    private String userName;
    private ProgressDialog mProgressDialog;
    private AccountHeader header;
    private ProfileDrawerItem profileDrawerItem;
    private Drawer result;
    private static final int GALLERY_REQUEST = 1123;
    private Uri imageUri = null;
    private ArrayList<FirstAid> firstAidArr;
    private LatLng mCenterLatLong;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int REQUEST_LOCATION = 0x2;
    private static final int CAMERA_REQUEST = 1888;
    private LatLng mCurrentLatLng = null;
    private LocationManager mLocationManager;
    private static final String[] INITIAL_PERMS = {

            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private Location mLocation;
    private String bestProvider;
    private Toolbar toolbar;

    @Override
    public void onBackPressed() {
        if (result.isDrawerOpen()) {
            result.closeDrawer();
        } else
            result.openDrawer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("LOGTAG", "Calling MAP");
        //Call MAP

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {

            callMap();
        }

        Log.e("LOGTAG", "Called MAP");

        try {
            Log.e("LOGTAG", "try getlocation");
            getLocation();
        } catch (Settings.SettingNotFoundException e) {
            Log.e("LOGTAG", "catch getlocation");
            e.printStackTrace();
        }

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);

            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });

        Log.e("LOGTAG", "Set toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e("LOGTAG", "Done set toolbar");
        mAuth = FirebaseAuth.getInstance();

        profileDrawerItem = new ProfileDrawerItem();
        Log.e("LOGTAG", "New instance of profiledraweritem");

//        Intent intent = getIntent();
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Log.e("LOGTAG", "Call getuserinfo");
        getUserInfo();
        Log.e("LOGTAG", "Called getuserinfo");


        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {

                        openGallary();
                        return true;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        openGallary();
                        return true;
                    }
                })
                .build();

        PrimaryDrawerItem requestAmbulance = new PrimaryDrawerItem().withIdentifier(1).withName("Request Ambulance").withIcon(R.drawable.requestambulance);
        PrimaryDrawerItem aboutUs = new PrimaryDrawerItem().withIdentifier(2).withName("About Us").withIcon(R.drawable.aboutusicon);
        PrimaryDrawerItem logOff = new PrimaryDrawerItem().withIdentifier(3).withName("Log Off").withIcon(R.drawable.logoff);
        PrimaryDrawerItem firstAid = new PrimaryDrawerItem().withIdentifier(4).withName("First Aid").withIcon(R.drawable.requestambulance);
        //SecondaryDrawerItem title = new SecondaryDrawerItem().withName("First Aid").withSelectable(false);

        result = new DrawerBuilder().withActivity(this).withToolbar(toolbar).addDrawerItems(
                requestAmbulance,
                aboutUs,
                logOff,
                firstAid

        )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (position == 1) {
                        } else if (position == 2) {
                            startActivity(new Intent(MainActivity.this, AboutUs.class));
                        } else if (position == 3) {
                            FirebaseHandler.signOut(mAuth, MainActivity.this);
                            MainActivity.this.finish();
                            startActivity(new Intent(MainActivity.this, SignIn.class));
                        } else if (position == 4) {
                            startActivity(new Intent(MainActivity.this, FirstAidActivity.class));
                        }
                        return false;
                    }
                }).withAccountHeader(header).build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        Log.e("LOGTAG", "mRequest button initalization");
        mRequestAmb = (Button) findViewById(R.id.requestambbtn);
        mRequestAmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("LOGTAG", "mRequestB button click listener");
                if(mCenterLatLong != null)
                {
                    double lat = mCenterLatLong.latitude;
                    double lng = mCenterLatLong.longitude;
                    Intent i = new Intent(MainActivity.this,RequestAmbulance.class);
                    i.putExtra("lat",lat);
                    i.putExtra("lng",lng);
                    Log.e("LOGTAG", "Starting activity");
                    startActivity(i);

                }
            }
        });


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mProgressDialog.dismiss();
        Log.e("LOGTAG", "Inside onMapReady");

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("LOGTAG", "checkSelfPermission onMapReady");
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //mMap.setPadding(0,1800,0,0);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onMapClick(LatLng latLng) {
                Log.e("LOGTAG", "Map.setonmapclicklistener");
                if(toolbar.getVisibility() == View.VISIBLE)
                {
                    hideToolBar();
                    hideButton(mRequestAmb);
                }
                else if(toolbar.getVisibility() == View.INVISIBLE)
                {
                    showToolBar();
                    showButton();
                }
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mCenterLatLong = cameraPosition.target;

                Log.e("LOGTAG = ", mCenterLatLong + Double.toString(mCenterLatLong.latitude) + " " + Double.toString(mCenterLatLong.longitude));
            }
        });


    }


    private void getLocation() throws Settings.SettingNotFoundException {


        int off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        if (off == 0) {
            Utilites.displayLocationSettingsRequest(getApplicationContext(), MainActivity.this);
            Log.e("LOGTAG", "Send Request Location Off");

        } else {
            Log.e("LOGTAG", "Send Request Request Location");
            requestLocation();
        }

    }

    private void requestLocation() {

        Log.e("LOGTAG", "Send Request Inside Request Location");
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);

            Log.e("LOGTAG", "checkSelfPermission requestLocation");
            return;
        }

        Log.e("LOGTAG", "Send Request Location Manager");


        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("LOGTAG", "onRequestPermissionsResult");
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("LOGTAG", "Permission Granted");
                    callMap();
                    requestLocation();
                }
            }
        }
    }


    private void getUserInfo() {


        Log.e("LOGTAG", "Inside GET USER INFO");
        Log.e("LOGTAG", userEmail);
        FirebaseHandler.getUserInfo(userEmail, this);

    }

    @Override
    public void getUserData(User user) {
        Log.e("LOGTAG", "Inside GET USER DATA");
        Log.e("LOGTAG", "Inside GET USER DATA" + user.getUserEmail());

        profileDrawerItem.withName(user.getUserName()).withEmail(user.getUserEmail());

        FirebaseHandler.checkIfUserHasImage(userEmail, new CheckImageExistance() {
            @Override
            public void checkImageExistance(boolean isFound, String URL) {
                if (isFound) {
                    Log.e("LOGTAG", "IMAGE IS FOUND");
                    Uri uri = Uri.parse(URL);
                    Log.e("ImageURL", URL);
                    header.addProfiles(profileDrawerItem.withIcon(uri));
                } else {
                    Log.e("LOGTAG", "IMAGE NOT FOUND");
                    header.addProfiles(profileDrawerItem.withIcon(R.drawable.ahmedelsherif));
                }
            }
        });
        userName = user.getUserName();

    }

    private void openGallary() {
        Intent gallaryIntent = new Intent();
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        gallaryIntent.setType("image/*");
        startActivityForResult(gallaryIntent, GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Log.e("LOGTAG", "onActivityResult YES");
            imageUri = data.getData();
            //header.updateProfile(profileDrawerItem.withIcon(imageUri));
            FirebaseHandler.addUserImageToDatabase(imageUri, new AddUserImage() {
                @Override
                public void addUserImage(boolean bool) {
                    if (bool) {
                        header.clear();
                        header.addProfiles(profileDrawerItem.withIcon(imageUri));
                        Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Try again later", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i("LOGTAG", "User agreed to make required location settings changes.");
                        requestLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("LOGTAG", "User chose not to make required location settings changes.");
                        Toast.makeText(this, "Location must be turned On", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;

        }

    }


    @Override
    public void onLocationChanged(Location location) {
//        mLocation = location;
        Log.e("LOGTAG", "onLocationChanged");
        Log.e("mCurrentLatLong = ", Double.toString(location.getLatitude()) + " " + Double.toString(location.getLongitude()));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e("LOGTAG", "checkSelfPermission onLocationChanged");
            return;
        }


            double lat = location.getLatitude();
            double lng = location.getLongitude();
            LatLng latLng = new LatLng(lat, lng);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
            mLocationManager.removeUpdates(this);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void hideToolBar()
    {
        YoYo.with(Techniques.FadeOutUp)
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        toolbar.setVisibility(View.INVISIBLE);
                    }
                })
                .duration(700)
                .playOn(findViewById(R.id.toolbar));

    }

    private void showToolBar()
    {

        YoYo.with(Techniques.FadeInDown)
                .onStart(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        toolbar.setVisibility(View.VISIBLE);
                    }
                })
                .duration(700)
                .playOn(findViewById(R.id.toolbar));

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void hideButton(final Button button)
    {
        if(button.getVisibility() == View.VISIBLE)
        {
            YoYo.with(Techniques.FadeOutDown)
                    .duration(700)
                    .playOn(findViewById(R.id.requestambbtn));
        }
    }

    private void showButton()
    {

                    YoYo.with(Techniques.FadeInUp)
                            .duration(700)
                            .playOn(findViewById(R.id.requestambbtn));



    }

    private void callMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
}
