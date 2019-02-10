package com.example.fahrizal.pemilikmotor;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private final LatLng mDefaultLocation = new LatLng(5.553991, 95.317409);
    private static final int DEFAULT_ZOOM = 15;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    private Double lat, lng;
    private String namaTambal;
    private ArrayList<Profil> data;
    private DatabaseReference ref, rootRef;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private LatLng lokasiUser;
    private static final int CALL_PHONE_PERMISSION = 97;
    SupportMapFragment mapFragment;
    Date now, jamBuka, jamTutup;
    SimpleDateFormat sdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        now = Calendar.getInstance().getTime();
        jamBuka = Calendar.getInstance().getTime();
        jamTutup = Calendar.getInstance().getTime();
        sdf = new SimpleDateFormat("HH:mm");
        //jamBuka = new Date();
        //jamTutup = new Date();
        rootRef = FirebaseDatabase.getInstance().getReference().child("Profil");
        getLocationPermission();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_PERMISSION);
        }
//        ambil();
    }

    public void telpon(String hp) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + hp));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }
    }

    private void getDeviceLocation(){
        Log.d("error", "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d("error", "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            lokasiUser = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                            ambil();

                        }else{
                            Log.d("error", "onComplete: current location is null");
                            Toast.makeText(MainActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("error", "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
    public void ambil(){
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = new ArrayList<>();
                data.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Profil profil = ds.getValue(Profil.class);
                    String key = ds.getKey();

                    try {
                        Date jamBuka2 = sdf.parse(profil.getTxtbuka());
                        Date jamTutup2 = sdf.parse(profil.getTxttutup());
                        jamBuka.setHours(jamBuka2.getHours());
                        jamBuka.setMinutes(jamBuka2.getMinutes());
                        jamTutup.setHours(jamTutup2.getHours());
                        jamTutup.setMinutes(jamTutup2.getMinutes());
                        //Toast.makeText(MainActivity.this, "Jam : "+jamBuka, Toast.LENGTH_SHORT).show();
                        if (profil.getStatus() != null){
                            if (profil.getStatus().equals("0")){

                            }else {
                                if (now.after(jamBuka)&&now.before(jamTutup)){
                                    profil.setKey(key);
                                    data.add(profil);
                                }
                            }
                        }else {
                            if (now.after(jamBuka)&&now.before(jamTutup)){
                                profil.setKey(key);
                                data.add(profil);
                            }
                        }

                    }catch (Exception tx){
                        Toast.makeText(MainActivity.this, "Error"+tx.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                   // Toast.makeText(MainActivity.this, "Now : "+now, Toast.LENGTH_SHORT).show();
                }
                tampil();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void tampil(){
        mapFragment.getMapAsync(this);
        if (data!= null && !data.isEmpty()){
            Toast.makeText(this, "Ada", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Kosong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        Marker terdekat = null;
        if (mLocationPermissionsGranted){
            if (lokasiUser != null){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasiUser,DEFAULT_ZOOM));

            }else {
                googleMap.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            }

//            getDeviceLocation();
            float jarakDekat = Float.MAX_VALUE;

            for (int a = 0; a < data.size(); a++){
                if (data.get(a).getLatitude() != null && data.get(a).getLongitude() != null){

                    CustomInfoWindow customInfoWindow = new CustomInfoWindow(this);
                    mMap.setInfoWindowAdapter(customInfoWindow);
                    MarkerOptions markerOptions = new MarkerOptions();

                    LatLng koord = new LatLng(data.get(a).getLatitude(),data.get(a).getLongitude());

                    // Setting the position for the marker
                    markerOptions.position(koord);

                    // Setting the title for the marker.
                    // This will be displayed on taping the marker
                    markerOptions.title(data.get(a).getNamaTambal());

                    // Clears the previously touched position
                    //googleMap.clear();

                    // Animating to the touched position
                    //googleMap.animateCamera(CameraUpdateFactory.newLatLng(koord));

                    // Placing a marker on the touched position
                    Marker marker = googleMap.addMarker(markerOptions);
                    marker.setTag(data.get(a));
//                    marker.showInfoWindow();
//method untuk mengetahui jarak dari titik terdekat
                    if (lokasiUser != null){
                        float[] jarak = new float[1];
                        Location.distanceBetween(lokasiUser.latitude,lokasiUser.longitude,data.get(a).getLatitude(),data.get(a).getLongitude(),jarak);
                        if (jarakDekat > jarak[0]){
//                            Toast.makeText(this, "Ada", Toast.LENGTH_SHORT).show();
                            jarakDekat = jarak[0];
                            terdekat = marker;
                            terdekat.setTag(data.get(a));

                        }
//                        Toast.makeText(this, ""+jarak, Toast.LENGTH_SHORT).show();
                    }
                }
                if (terdekat != null){
                    terdekat.showInfoWindow();
                }
            }

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                getLocationPermission();
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            mMap.setOnInfoWindowClickListener(this);

        }
    }

    private void getLocationPermission(){
        Log.d("error", "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                getDeviceLocation();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("error", "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d("error", "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d("error", "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            Profil profil = (Profil) marker.getTag();
            Toast.makeText(this, "Telpon"+profil.getNomorHp(), Toast.LENGTH_SHORT).show();
            callIntent.setData(Uri.parse("tel:" + profil.getNomorHp()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }
    }
    /*private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Atur Akses Lokasi")
                        .setMessage("OK untuk atur lokasi")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
        }
    }*/
}
