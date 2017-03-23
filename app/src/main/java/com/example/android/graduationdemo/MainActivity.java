package com.example.android.graduationdemo;

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
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.example.android.graduationdemo.data.FirstAid;
import com.example.android.graduationdemo.firstaid.DetailedActivity;
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
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GetLocationData, GetUserData, LocationListener {

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

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
                Log.e("CustomApplication", uri.toString());
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });
        firstAidArr = new ArrayList();
        addFirstAid(firstAidArr);


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
        SecondaryDrawerItem title = new SecondaryDrawerItem().withName("First Aid").withSelectable(false);
        result = new DrawerBuilder().withActivity(this).withToolbar(toolbar).addDrawerItems(
                requestAmbulance,
                aboutUs,
                logOff,
                title,
                new DividerDrawerItem()
                , new SecondaryDrawerItem().withIdentifier(4).withName("Allergies").withIcon(R.drawable.allergiesicon)
                , new SecondaryDrawerItem().withIdentifier(5).withName("Asthma Attack").withIcon(R.drawable.asthmaicon)
                , new SecondaryDrawerItem().withIdentifier(6).withName("Bleeding").withIcon(R.drawable.bleedingicon)
                , new SecondaryDrawerItem().withIdentifier(7).withName("Broken Bones").withIcon(R.drawable.boneicon)
                , new SecondaryDrawerItem().withIdentifier(9).withName("Burns").withIcon(R.drawable.burnsicon)
                , new SecondaryDrawerItem().withIdentifier(9).withName("Choking").withIcon(R.drawable.chokingicon)
                , new SecondaryDrawerItem().withIdentifier(10).withName("Chest Pain").withIcon(R.drawable.chestpainicon)
                , new SecondaryDrawerItem().withIdentifier(11).withName("Diabets").withIcon(R.drawable.diabetesicon)
                , new SecondaryDrawerItem().withIdentifier(12).withName("Heading").withIcon(R.drawable.headingicon)
                , new SecondaryDrawerItem().withIdentifier(13).withName("Heat Stroke").withIcon(R.drawable.heatstrokeicon)
                , new SecondaryDrawerItem().withIdentifier(14).withName("Muscle Injury").withIcon(R.drawable.muscleinjuryicon)
                , new SecondaryDrawerItem().withIdentifier(15).withName("Nose Bleeding").withIcon(R.drawable.nosebleedicon)
                , new SecondaryDrawerItem().withIdentifier(16).withName("Poisoning").withIcon(R.drawable.poisoningicon)
                , new SecondaryDrawerItem().withIdentifier(17).withName("Stroke").withIcon(R.drawable.strokeicon)
                , new SecondaryDrawerItem().withIdentifier(18).withName("Unconscious").withIcon(R.drawable.unconsciousicon)
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

                        } else if (position == 5) {

                        } else if (position == 6) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(0)));
                        } else if (position == 7) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(1)));
                        } else if (position == 8) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(2)));
                        } else if (position == 9) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(3)));
                        } else if (position == 10) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(4)));
                        } else if (position == 11) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(5)));
                        } else if (position == 12) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(6)));
                        } else if (position == 13) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(7)));
                        } else if (position == 14) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(8)));
                        } else if (position == 15) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(9)));
                        } else if (position == 16) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(10)));
                        } else if (position == 17) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(11)));
                        } else if (position == 18) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(12)));
                        } else if (position == 19) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(13)));
                        } else if (position == 20) {
                            startActivity(new Intent(MainActivity.this, DetailedActivity.class).putExtra("data", firstAidArr.get(14)));
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

                if(mCenterLatLong != null)
                {
                    double lat = mCenterLatLong.latitude;
                    double lng = mCenterLatLong.longitude;
                    Intent i = new Intent(MainActivity.this,RequestAmbulance.class);
                    i.putExtra("lat",lat);
                    i.putExtra("lng",lng);
                    startActivity(i);
                }

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

        try {
            getLocation();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);



        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mCenterLatLong = cameraPosition.target;

                Log.e("mCenterLatLong = ", Double.toString(mCenterLatLong.latitude) + " " + Double.toString(mCenterLatLong.longitude));
            }
        });
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


    private void getLocation() throws Settings.SettingNotFoundException {


        int off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
        if (off == 0) {
//            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            Toast.makeText(this, "Location must be turned On!", Toast.LENGTH_SHORT).show();
//            startActivity(onGPS)
            Utilites.displayLocationSettingsRequest(getApplicationContext(), MainActivity.this);
            Log.e("LOGTAG", "Send Request Location Off");

        } else {
            Log.e("LOGTAG", "Send Request Request Location");
            requestLocation();
        }

    }

    private void requestLocation() {
        mProgressDialog.show();

        Log.e("LOGTAG", "Send Request Inside Request Location");
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
            return;
        }

        Log.e("LOGTAG", "Send Request Location Manager");

//        if(mLocation!= null)
//        {
//            Log.e("mCurrentLatLong = ",Double.toString(mLocation.getLatitude()) + " " + Double.toString(mLocation.getLongitude()));
//
//
//        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

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

//    private final LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//
////            Double lat = location.getLatitude();
////            Double lng = location.getLongitude();
////            mCurrentLatLng = new LatLng(lat,lng);
////            mProgressDialog.dismiss();
////            Log.e("mCurrentLatLong = ",Double.toString(location.getLatitude()) + " " + Double.toString(location.getLongitude()));
//
//        }
//
//        @Override
//        public void onStatusChanged(String s, int i, Bundle bundle) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String s) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String s) {
//
//        }
//    };


    private void getUserInfo() {


        mProgressDialog.show();
        Log.e("TAGKEY", "Inside GET USER INFO");
        Log.e("TAGKEY", userEmail);
        FirebaseHandler.getUserInfo(userEmail, this);

        mProgressDialog.dismiss();
    }

    @Override
    public void getUserData(User user) {
        Log.e("TAGKEY", "Inside GET USER DATA");
        Log.e("TAGKEY", "Inside GET USER DATA" + user.getUserEmail());

        profileDrawerItem.withName(user.getUserName()).withEmail(user.getUserEmail());

        FirebaseHandler.checkIfUserHasImage(userEmail, new CheckImageExistance() {
            @Override
            public void checkImageExistance(boolean isFound, String URL) {
                if (isFound) {
                    Log.e("TAGKEY", "IMAGE IS FOUND");
                    Uri uri = Uri.parse(URL);
                    Log.e("ImageURL", URL);
                    header.addProfiles(profileDrawerItem.withIcon(uri));
                } else {
                    Log.e("TAGKEY", "IMAGE NOT FOUND");
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
            Log.e("onActivityResult", "YES");
            imageUri = data.getData();
            mProgressDialog.show();
            //header.updateProfile(profileDrawerItem.withIcon(imageUri));
            FirebaseHandler.addUserImageToDatabase(imageUri, new AddUserImage() {
                @Override
                public void addUserImage(boolean bool) {
                    if (bool) {
                        mProgressDialog.dismiss();
                        header.clear();
                        header.addProfiles(profileDrawerItem.withIcon(imageUri));
                        Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        mProgressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Try again later", Toast.LENGTH_SHORT).show();
                    }
                }
            });

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
                        Toast.makeText(this, "Location must be turned On", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        break;
                }
                break;

        }

    }

    private void addFirstAid(ArrayList<FirstAid> firstAidArr) {
        FirstAid firstAidObj = new FirstAid();
        firstAidObj.setSign("Cough difficulty or irregular breathing, wheezing, itchy throat or mouth, and difficulty swallowing\n" +
                "Nausea, vomiting, abdominal pain, and diarrhea\n" +
                "Itchiness, red bumps or welts on the skin (hives), and skin redness\n" +
                "Dizziness, lightheadedness, heart palpitations, chest discomfort or tightness, mental confusion, weakness, lower blood pressure, rapid pulse, loss of consciousness, and fainting\n");
        firstAidObj.setCure("If you have severe allergies, you should keep two epinephrine injection kits with you at all times and readily available. \n" +
                "If you experience any sign of anaphylaxis, do not hesitate to use the auto-injector even if those symptoms do not appear to be allergy related.\n" +
                "Using the auto injector as a precaution will not harm you.\n");
        firstAidObj.setImage(R.drawable.allergies);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("You have increasing wheezing, cough, chest tightness or shortness of breath  \n" +
                "You are waking often at night with asthma symptoms \n");
        firstAidObj.setCure("Sit the person upright comfortably and loosen tight clothing.\n" +
                "If the person has asthma medication, such as an inhaler, assist in using it.\n" +
                "If the person doesn’t have an inhaler, use one from a first aid kit or borrow someone else’s.\n");
        firstAidObj.setImage(R.drawable.asthma);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Pain in the Abdomen\n" +
                "Blood in the stool\n" +
                "Vomiting blood\n" +
                "Coughing blood\n" +
                "Bloody Urine\n" +
                "Vaginal bleeding\n" +
                "Compartment Syndrome\n" +
                "Shock\n" +
                "Painful joints\n" +
                "Bleeding in the head\n");
        firstAidObj.setCure("Remove any obvious dirt or debris from the wound. Don't remove large or deeply embedded objects.\n" +
                " Don't probe the wound or attempt to clean it yet. Your first job is to stop the bleeding. \n" +
                "Wear disposable protective gloves if available.\n" +
                "Stop the bleeding. Place a sterile bandage or clean cloth on the wound. \n" +
                "Press the bandage firmly with your palm to control bleeding. \n" +
                "Maintain pressure by binding the wound tightly with a bandage or a piece of clean cloth.\n" +
                " Secure with adhesive tape. Use your hands if nothing else is available.\n");
        firstAidObj.setImage(R.drawable.bleeding);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Broken bone can cause one or more of the following signs and symptoms:\n" +
                "Intense pain in the injured area that gets worse when you move it\n" +
                "Numbness in the injured area\n" +
                "Bluish color, swelling, or visible deformity in the injured area\n" +
                "Bone protruding through the skin\n" +
                "Heavy bleeding at the injury site\n");
        firstAidObj.setCure("Stop any bleeding: If they’re bleeding, elevate and apply pressure to the wound using a sterile bandage, a clean cloth, or a clean piece of clothing.\n" +
                "Immobilize the injured area: If you suspect they’ve broken a bone in their neck or back, help them stay as still as possible. \n" +
                "If you suspect they’ve broken a bone in one of their limbs, immobilize the area using a splint or sling.\n" +
                "Apply cold to the area: Wrap an ice pack or bag of ice cubes in a piece of cloth and apply it to the injured area for up to 10 minutes at a time.\n" +
                "Treat them for shock: Help them get into a comfortable position, encourage them to rest, and reassure them.\n" +
                " Cover them with a blanket or clothing to keep them warm.\n");
        firstAidObj.setImage(R.drawable.brokenbone);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("");
        firstAidObj.setCure("First, stop the burning to prevent a more severe burn.\n" +
                "Cover the burn with a clean, dry cloth to reduce the risk of infection.\n" +
                "Do not put any salve or medicine on the burned area, so your doctor can properly assess your burn.\n" +
                "Do not put ice or butter on the burned area, because these measures do not help and can damage the skin tissue.\n");
        firstAidObj.setImage(R.drawable.burns);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Inability to talk\n" +
                "Difficulty breathing or noisy breathing\n" +
                "Inability to cough forcefully\n" +
                "Skin, lips and nails turning blue or dusky\n" +
                "Loss of consciousness\n");
        firstAidObj.setCure("If choking is occurring, the Red Cross recommends a \"five-and-five\" approach to delivering first aid:\n" +
                "Give 5 back blows. First, deliver five back blows between the person's shoulder blades with the heel of your hand.\n" +
                "Give 5 abdominal thrusts. Perform five abdominal thrusts (also known as the Heimlich maneuver).\n" +
                "Alternate between 5 blows and 5 thrusts until the blockage is dislodged\n");
        firstAidObj.setImage(R.drawable.choking);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Chest Discomfort\n" +
                "Nausea, Indigestion, Heartburn, or Stomach Pain\n");
        firstAidObj.setCure("If the person gets angina and has been prescribed nitroglycerin:\n" +
                "Dissolve 1 nitroglycerin tablet under the tongue (or use nitroglycerin spray under the tongue).\n" +
                "If the person has been diagnosed with chronic stable angina:\n" +
                "Dissolve 1 nitroglycerin tablet under the tongue (or use nitroglycerin spray under the tongue).\n" +
                "Repeat every 5 minutes until the person has taken 3 tablets in 15 minutes.\n");
        firstAidObj.setImage(R.drawable.chestpain1);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Increased thirst\n" +
                "Increased hunger (especially after eating)\n" +
                "Dry mouth\n" +
                "Frequent urination or urine infections\n" +
                "Unexplained weight loss (even though you are eating and feel hungry)\n" +
                "Fatigue (weak, tired feeling)\n" +
                "Blurred vision\n" +
                "Headaches\n");

        firstAidObj.setCure("*What you need to do ‒ for high blood sugar (hyperglycemia)\n" +
                "Checking their breathing, pulse and level of response.\n" +
                "If they lose responsiveness at any point, open their airway,\n" +
                "Check their breathing and prepare to treat someone who’s become unresponsive.\n" +
                "*What you need to do ‒ for low blood sugar (hypoglycemia)\n" +
                "Help them sit down. If they have their own glucose gel, help them take it.\n" +
                " If not, you need to give them something sugary like fruit juice, a fizzy drink, three teaspoons of sugar, or sugary sweets.\n" +
                "If they improve quickly, give them more sugary food or drink and let them rest. If they have their glucose testing kit with them, \n" +
                "Help them use it to check their glucose level. Stay with them until they feel completely better.\n" +
                "While waiting, keep checking their responsiveness, breathing and pulse.\n");

        firstAidObj.setImage(R.drawable.diabetes1);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Becomes very sleepy\n" +
                "Behaves abnormally\n" +
                "Develops a severe headache or stiff neck\n" +
                "Has a pupil (the dark central part of the eye) of unequal sizes\n" +
                "Is unable to move an arm or leg\n" +
                "Loses consciousness, even briefly\n" +
                "Vomits more than once\n");

        firstAidObj.setCure("Check the person's airway, breathing, and circulation. If necessary, begin rescue breathing and CPR.\n" +
                "If the person's breathing and heart rate are normal but the person is unconscious,\n" +
                "treat as if there is a spinal injury. Stabilize the head and neck by placing your hands on both sides of the person's head.\n" +
                " Keep the head in line with the spine and prevent movement. Wait for medical help.\n" +
                "Stop any bleeding by firmly pressing a clean cloth on the wound. If the injury is serious, be careful not to move the person's head. If blood soaks through the cloth, do not remove it. Place another cloth over the first one.\n");
        firstAidObj.setImage(R.drawable.heading);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Throbbing headache\n" +
                "Dizziness and light-headedness\n" +
                "Lack of sweating despite the heat\n" +
                "Red, hot, and dry skin\n" +
                "Muscle weakness or cramps\n" +
                "Nausea and vomiting\n" +
                "Rapid heartbeat, which may be either strong or weak\n" +
                "Rapid, shallow breathing\n" +
                "Behavioral changes such as confusion, disorientation, or staggering\n" +
                "Seizures\n" +
                "Unconsciousness\n");
        firstAidObj.setCure("Immediately move the person out of the heat and cool him or her by whatever means available, for example:\n" +
                "Put the person in a cool tub of water or a cool shower.\n" +
                "Spray with a garden hose.\n" +
                "Sponge with cool water.\n" +
                "Fan while misting with cool water.\n" +
                "Place ice packs or cool wet towels on the neck, armpits and groin.\n" +
                "Cover with cool damp sheets.\n");
        firstAidObj.setImage(R.drawable.heatstroke);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Swelling, bruising, or redness due to the injury\n" +
                "Pain at rest\n" +
                "Pain when the specific muscle or the joint in relation to that muscle is used\n" +
                "Weakness of the muscle or tendons\n" +
                "Inability to use the muscle at all\n");
        firstAidObj.setCure(" Control Swelling With RICE Therapy\n" +
                "RICE stands for:\n" +
                "-Rest the sprained or strained areaIf necessary, use a sling for an arm injury or crutches for a leg or foot injury. \n" +
                "Splint an injured finger or toe by taping it to an adjacent finger or toe.\n" +
                "-Ice for 20 minutes every hour. Never put ice directly against the skin or it may damage the skin. \n" +
                "Use a thin towel for protection.\n" +
                "-Compress by wrapping an elastic (Ace) bandage or sleeve lightly (not tightly) around the joint or limb. \n" +
                "Specialized braces, such as for the ankle, can work better than an elastic bandage for removing the swelling.\n" +
                "-Elevate the area above heart level if possible.\n" +
                "Let the person drink cool water or other nonalcoholic beverage without caffeine\n");
        firstAidObj.setImage(R.drawable.muscleinjury);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Tiredness\n" +
                "Weakness\n" +
                "Chills or feeling cold\n" +
                "Dizziness or feeling lightheaded\n" +
                "Easy bruising or bleeding\n");
        firstAidObj.setCure("Have the person sit up straight and lean forward slightly. \n" +
                "Don't have the person lie down or tilt the head backward.\n" +
                "With thumb and index finger, firmly pinch the nose just below the bone up against the face.\n" +
                "Apply pressure for 5 minutes. Time yourself with a clock.\n" +
                "If bleeding continues after 5 minutes, repeat the process.\n");
        firstAidObj.setImage(R.drawable.nosebleeding);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Poisoning signs and symptoms can mimic other conditions, such as seizure, alcohol intoxication, \n" +
                "Stroke and insulin reaction. Signs and symptoms of poisoning may include:\n" +
                "Burns or redness around the mouth and lips\n" +
                "Breathe that smells like chemicals, such as gasoline or paint thinner\n" +
                "Vomiting, Difficulty breathing, Drowsiness, Confusion or other altered mental status\n");
        firstAidObj.setCure("Wallowed poison. Remove anything remaining in the person's mouth. \n" +
                "If the suspected poison is a household cleaner or other chemical, read the container's label and follow instructions for accidental poisoning.\n" +
                "Poison on the skin. Remove any contaminated clothing using gloves. Rinse the skin for 15 to 20 minutes in a shower or with a hose.\n" +
                "Poison in the eye. Gently flush the eye with cool or lukewarm water for 20 minutes or until help arrives.\n");
        firstAidObj.setImage(R.drawable.poisoning);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Weakness or numbness on one side of the body, including either leg\n" +
                "Dimness, blurring or loss of vision, particularly in one eye\n" +
                "Severe headache — a bolt out of the blue — with no apparent cause\n" +
                "Unexplained dizziness, unsteadiness or a sudden fall, especially if accompanied by any of the other signs or symptoms\n");
        firstAidObj.setCure(" Assess the patient’s level of consciousness\n" +
                "1. If unconscious and breathing normally, or if not fully alert, place the patient on their side in a supported position.\n" +
                "It is important for the patient to be assessed as soon as possible because treatment must be started within 1 to 2 hours if a clot is present in the brain.\n" +
                "2.      Care for a conscious patient\n" +
                "Assist a conscious patient into the position of greatest comfort\n" +
                "Cover the patient to reduce heat loss.\n" +
                "3.      Observe the patient \n" +
                "While waiting for the ambulance to arrive, observe the patient closely for any change in condition.\n" +
                "If there is any deterioration in the patient’s conscious state, turn the patient on their side in a supported position.\n");
        firstAidObj.setImage(R.drawable.stroke);

        firstAidArr.add(firstAidObj);

        firstAidObj = new FirstAid();

        firstAidObj.setSign("Symptoms that may indicate that unconsciousness is about to occur include:\n" +
                "Sudden inability to respond\n" +
                "Slurred speech\n" +
                "A rapid heartbeat\n" +
                "Confusion\n" +
                "Dizziness or lightheadedness\n");
        firstAidObj.setCure("Check whether the person is breathing.\n" +
                "Raise the person’s legs at least 12 inches above the ground.\n" +
                "Loosen any restrictive clothing or belts. If the person doesn’t regain consciousness within one minute, \n" +
                "Check the person’s airway to make sure there’s no obstruction.\n" +
                "Check again to see if the person is breathing, coughing, or moving. These are signs of positive circulation. \n" +
                "If these signs are absent, perform cardiopulmonary resuscitation (CPR) until emergency personnel arrive.\n");
        firstAidObj.setImage(R.drawable.unconsciousness);

        firstAidArr.add(firstAidObj);
    }

    @Override
    public void onLocationChanged(Location location) {
//        mLocation = location;
        Log.e("mCurrentLatLong = ", Double.toString(location.getLatitude()) + " " + Double.toString(location.getLongitude()));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if(location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            LatLng latLng = new LatLng(lat, lng);
            mProgressDialog.dismiss();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
            mLocationManager.removeUpdates(this);
        }
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
}
