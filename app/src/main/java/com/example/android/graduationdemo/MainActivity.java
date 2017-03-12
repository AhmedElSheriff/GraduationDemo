package com.example.android.graduationdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.graduationdemo.Firebase.FirebaseHandler;
import com.example.android.graduationdemo.callbacks.AddUserImage;
import com.example.android.graduationdemo.callbacks.CheckImageExistance;
import com.example.android.graduationdemo.callbacks.GetLocationData;
import com.example.android.graduationdemo.callbacks.GetUserData;
import com.example.android.graduationdemo.controller.RequestAmbulance;
import com.example.android.graduationdemo.data.EmergencyLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GetLocationData, GetUserData {

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



    @Override
    public void onBackPressed() {
        if(result.isDrawerOpen())
        {
            result.closeDrawer();
        }
        else
            result.openDrawer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
                Log.e("CustomApplication",uri.toString());
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading");
        mProgressDialog.setCancelable(false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        profileDrawerItem = new ProfileDrawerItem();

//        Intent intent = getIntent();
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        getUserInfo();

        //Call MAP

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



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
        SecondaryDrawerItem generalFirstAid = new SecondaryDrawerItem().withIdentifier(4).withName("General First-Aid Advices").withIcon(R.drawable.generalicon);
        SecondaryDrawerItem title = new SecondaryDrawerItem().withName("First Aid").withSelectable(false);
        result = new DrawerBuilder().withActivity(this).withToolbar(toolbar).addDrawerItems(
                requestAmbulance,
                aboutUs,
                logOff,
                title,
                new DividerDrawerItem(),
                generalFirstAid, new SecondaryDrawerItem().withIdentifier(5).withName("Allergies").withIcon(R.drawable.allergiesicon)
                , new SecondaryDrawerItem().withIdentifier(6).withName("Asthma Attack").withIcon(R.drawable.asthmaicon)
                , new SecondaryDrawerItem().withIdentifier(7).withName("Bleeding").withIcon(R.drawable.bleedingicon)
                , new SecondaryDrawerItem().withIdentifier(8).withName("Broken Bones").withIcon(R.drawable.boneicon)
                , new SecondaryDrawerItem().withIdentifier(9).withName("Burns").withIcon(R.drawable.burnsicon)
                , new SecondaryDrawerItem().withIdentifier(10).withName("Choking").withIcon(R.drawable.chokingicon)
                , new SecondaryDrawerItem().withIdentifier(11).withName("Chest Pain").withIcon(R.drawable.chestpainicon)
                , new SecondaryDrawerItem().withIdentifier(12).withName("Diabets").withIcon(R.drawable.diabetesicon)
                , new SecondaryDrawerItem().withIdentifier(13).withName("Emotional Upset").withIcon(R.drawable.emotionalupseticon)
                , new SecondaryDrawerItem().withIdentifier(14).withName("Heading").withIcon(R.drawable.headingicon)
                , new SecondaryDrawerItem().withIdentifier(15).withName("Heat Stroke").withIcon(R.drawable.heatstrokeicon)
                , new SecondaryDrawerItem().withIdentifier(16).withName("Muscle Injury").withIcon(R.drawable.muscleinjuryicon)
                , new SecondaryDrawerItem().withIdentifier(17).withName("Nose Bleeding").withIcon(R.drawable.nosebleedicon)
                , new SecondaryDrawerItem().withIdentifier(18).withName("Poisoning").withIcon(R.drawable.poisoningicon)
                , new SecondaryDrawerItem().withIdentifier(19).withName("Sting").withIcon(R.drawable.stingicon)
                , new SecondaryDrawerItem().withIdentifier(20).withName("Stroke").withIcon(R.drawable.strokeicon)
                , new SecondaryDrawerItem().withIdentifier(21).withName("Unconscious").withIcon(R.drawable.unconsciousicon)
        )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(position == 1)
                        {
                            startActivity(new Intent(MainActivity.this,RequestAmbulance.class));
                        }

                        else if(position == 2)
                        {
                            startActivity(new Intent(MainActivity.this,AboutUs.class));
                        }

                        else if(position == 3)
                        {
                            FirebaseHandler.signOut(mAuth,MainActivity.this);
                            MainActivity.this.finish();
                            startActivity(new Intent(MainActivity.this,SignIn.class));
                        }
                        return false;
                    }
                }).withAccountHeader(header).build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        mRequestAmb = (Button) findViewById(R.id.requestambbtn);
        mRequestAmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RequestAmbulance.class));
            }
        });


    }


    @Override
    public void getLocationData(EmergencyLocation emergencyLocation) {
//        mLat = emergencyLocation.getLatPosition();
//        mLong = emergencyLocation.getLongPosition();
//        LatLng case2 = new LatLng(Double.parseDouble(mLat),Double.parseDouble(mLong));
//        if (marker!=null) {
//            marker.remove();
//            marker=null;
//        }
//
//        if (marker==null) {
//            marker = mMap.addMarker(new MarkerOptions().position(case2).title("My Case").icon(BitmapDescriptorFactory.fromResource(R.drawable.patient)));
//        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        FirebaseHandler.getEmergencyLocation(this);

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29.9556449,30.9134569),16.0f));
//        LatLng car1 = new LatLng(29.9540458,30.9130367);
//        LatLng car2 = new LatLng(29.9532644,30.9134779);
//        LatLng car3 = new LatLng(29.9555664,30.9174799);
//        LatLng case1 = new LatLng(29.955388,30.9146463);
//        String lattest = mLat;
//        String longtest = mLong;
//        // LatLng case2 = new LatLng(Double.parseDouble(mLat),Double.parseDouble(mLong));
//        BitmapDescriptor patient = BitmapDescriptorFactory.fromResource(R.drawable.patient);
//        BitmapDescriptor badAmbulances = BitmapDescriptorFactory.fromResource(R.drawable.badambulance);
//        BitmapDescriptor goodAmbulance = BitmapDescriptorFactory.fromResource(R.drawable.goodambulance);
//        BitmapDescriptor busyAmbulances = BitmapDescriptorFactory.fromResource(R.drawable.busyambulance);
//
//
//
//        mMap.addMarker(new MarkerOptions().position(car1).title("Good Ambulance").icon(goodAmbulance));
//        mMap.addMarker(new MarkerOptions().position(car2).title("Bad Ambulance").icon(badAmbulances));
//        mMap.addMarker(new MarkerOptions().position(car3).title("Busy Ambulance").icon(busyAmbulances));
//        mMap.addMarker(new MarkerOptions().position(case1).title("Patient").icon(patient));
    }


    private void getUserInfo()
    {


            mProgressDialog.show();
        Log.e("TAGKEY","Inside GET USER INFO");
        Log.e("TAGKEY",userEmail);
            FirebaseHandler.getUserInfo(userEmail,this);

            mProgressDialog.dismiss();
    }

    @Override
    public void getUserData(User user) {
        Log.e("TAGKEY","Inside GET USER DATA");
        Log.e("TAGKEY","Inside GET USER DATA" + user.getUserEmail());

        profileDrawerItem.withName(user.getUserName()).withEmail(user.getUserEmail());

        FirebaseHandler.checkIfUserHasImage(userEmail, new CheckImageExistance() {
            @Override
            public void checkImageExistance(boolean isFound, String URL) {
                if(isFound)
                {
                    Log.e("TAGKEY","IMAGE IS FOUND");
                    Uri uri = Uri.parse(URL);
                    Log.e("ImageURL",URL);
                    header.addProfiles(profileDrawerItem.withIcon(uri));
                }
                else
                {
                    Log.e("TAGKEY","IMAGE NOT FOUND");
                    header.addProfiles(profileDrawerItem.withIcon(R.drawable.ahmedelsherif));
                }
            }
        });
        userName = user.getUserName();
        //header.addProfiles(profileDrawerItem.withName(user.getUserName()).withEmail(user.getUserEmail())
        //.withIcon(R.drawable.ahmedelsherif));


//        FirebaseHandler.getUserImageFromDatabase(new GetUserImage() {
//            @Override
//            public void getImage(URL url) {
//                header.addProfiles(profileDrawerItem.withName(userName).withEmail(userEmail)
//                        .withIcon("https://upload.wikimedia.org/wikipedia/commons/thumb/8/85/Smiley.svg/800px-Smiley.svg.png"));
//            }
//        });

    }

    private void openGallary()
    {
        Intent gallaryIntent = new Intent();
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        gallaryIntent.setType("image/*");
        startActivityForResult(gallaryIntent,GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
            Log.e("onActivityResult","YES");
            imageUri = data.getData();
            mProgressDialog.show();
            //header.updateProfile(profileDrawerItem.withIcon(imageUri));
            FirebaseHandler.addUserImageToDatabase(imageUri, new AddUserImage() {
                @Override
                public void addUserImage(boolean bool) {
                    if(bool){
                        mProgressDialog.dismiss();
                        header.clear();
                        header.addProfiles(profileDrawerItem.withIcon(imageUri));
                        Toast.makeText(MainActivity.this, "Added Successfully",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        mProgressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Try again later",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}
