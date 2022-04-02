package com.mvts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    private FusedLocationProviderClient mFusedLocationClient;

    FirebaseDatabase mdatabase;
    DatabaseReference mreference;
    private Button mbtnMaps;
    private Button mbntSetPoint;

    private int TO_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mdatabase = FirebaseDatabase.getInstance();
        mreference = mdatabase.getReference("usuarios");


        loadLatLongFirebase();
        mbtnMaps = findViewById(R.id.btnMaps);
        mbtnMaps.setOnClickListener(this);
        mbntSetPoint = findViewById(R.id.btnPoint);
        mbntSetPoint.setOnClickListener(this);
    }

    private void loadLatLongFirebase() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            }
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = null;

            LocationListener mlocListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

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
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            if (locationManager != null) {
                //Existe GPS_PROVIDER obtiene ubicación
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (location == null) { //Trata con NETWORK_PROVIDER
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                if (locationManager != null) {
                    //Existe NETWORK_PROVIDER obtiene ubicación
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
            if (location != null) {
                Double latitud = location.getLatitude();
                Double longitud = location.getLongitude();

                //Toast.makeText(MainActivity.this, "latitud" + latitud.toString() + " longitud" + longitud.toString(),Toast.LENGTH_LONG).show();
                Mapainicio mMap = new Mapainicio(latitud, longitud);
                mreference.push().setValue(mMap);
                Toast.makeText(this, "PUNTO LOCALIZADO", Toast.LENGTH_LONG).show();


            } else {
                //Mapainicio mMap = new Mapainicio(6666,12221);
                //mreference.push().setValue(mMap);
                Toast.makeText(this, "No se pudo obtener geolocalización", Toast.LENGTH_LONG).show();
            }

        }
    }


    private void autocompletedTo(){
        //ArrayList <Object> fields
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMaps :
                Intent inted = new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(inted);

                            break;
            case R.id.btnPoint :
                Intent inted2 = new Intent(MainActivity.this, activity_lightstreet.class);
                startActivity(inted2);

                break;
        }
    }

}
