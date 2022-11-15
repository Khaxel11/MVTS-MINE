package com.mvts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_PERMISSION_CAMERA = 101;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    private FusedLocationProviderClient mFusedLocationClient;

    FirebaseDatabase mdatabase;
    DatabaseReference mreference;
    private Button mbtnMaps;
    private Button mbntSetPoint;

    private int TO_REQUEST_CODE = 1;



    private TextView lblNewUser;

    private EditText txtNombre, txtContraseña;

    private Uri img;
    private String imagen;
    CircleImageView imgCirculo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermisosCamara();
        checkPermissionsRead_EXTERNAL_STORAGE(this);

        mdatabase = FirebaseDatabase.getInstance();
        mreference = mdatabase.getReference("ubicaciones");


        loadLatLongFirebase();
        mbtnMaps = findViewById(R.id.btnMaps);
        mbtnMaps.setOnClickListener(this);
        lblNewUser = findViewById(R.id.lblNewUser);
        //mbntSetPoint = findViewById(R.id.btnPoint);
        //mbntSetPoint.setOnClickListener(this);
        lblNewUser.setOnClickListener(this);

        txtNombre = (EditText) findViewById(R.id.txtNombreU);
        txtContraseña = (EditText) findViewById(R.id.txtPasswordU);

        imgCirculo = (CircleImageView) findViewById(R.id.imgCirculo);


    }

    private void
    loadLatLongFirebase() {

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
                boolean ok = BuscarNombre();

                if(ok == true){
                    Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                    //exist = false;
                }else{
                    //Toast.makeText(getApplicationContext(), "No se encontro el usuario", Toast.LENGTH_SHORT).show();

                }
                //exist = false;
                //BuscarNombre();

                            break;
            case R.id.lblNewUser :
                Intent inted2 = new Intent(MainActivity.this, activity_registro.class);
                startActivity(inted2);
                //Toast.makeText(getApplicationContext(), "No se encontro el usuario", Toast.LENGTH_SHORT).show();

                break;
        }
    }
    boolean exist;

    public boolean BuscarNombre() {
        String username = txtNombre.getText().toString();
        //exist = false;
        mreference = FirebaseDatabase.getInstance().getReference("minero");
        mreference.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String nombre = snapshot.child("nombre").getValue().toString();


                if (snapshot.exists()) {

                    String Nombre = snapshot.child("nombre").getValue().toString();
                    //txtNombre.setText(Nombre);


                    //String nombre = snapshot.child("nombre").getValue().toString();
                    String apellido = snapshot.child("apellido").getValue().toString();
                    String correo = snapshot.child("correo").getValue().toString();
                    String contraseña= snapshot.child("contraseña").getValue().toString();
                    String vehiculo = snapshot.child("vehiculo").getValue().toString();
                    String imagen2 = snapshot.child("imagen").getValue().toString();
                    //minero min = snapshot.getValue(minero.class);
                    //Toast.makeText(getApplicationContext(), imagen, Toast.LENGTH_SHORT).show();


                    //Glide.with(getApplicationContext()).load(img).into(imgCirculo);
                    try {

                        StorageReference folder = FirebaseStorage.getInstance().getReference().child(username);
                        folder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imagen = uri.toString();
                                Glide.with(getApplicationContext()).load(imagen).into(imgCirculo);
                                Intent inted = new Intent(MainActivity.this, MapsActivity.class);
                                startActivity(inted);
                                //Toast.makeText(getApplicationContext(), imagen, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                        /*folder.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                StorageReference dateRef = storageRef.child(username);
                                dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUrl) {
                                        String photoLink = downloadUrl.toString();
                                        Glide.with(getApplicationContext()).load(photoLink).into(imgCirculo);
                                    }
                                });
                                txtNombre.setText("");
                                txtContraseña.setText("");

                                Toast.makeText(getApplicationContext(), "¡BIENVENIDO " + Nombre + "!", Toast.LENGTH_SHORT).show();
                            }
                        });*/


                    }catch (IllegalArgumentException ex){

                    }
                    exist = true;

                } else {

                    Toast.makeText(getApplicationContext(), "No se encontro el usuario", Toast.LENGTH_SHORT).show();
                    imgCirculo.setImageDrawable(getResources().getDrawable(R.drawable.user_miner));
                    exist = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //exist = false;
            }
        });
        return exist;
    }






        public void PermisosCamara(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
            }
        }else{

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int [] grantResults) {

        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                //ToastWar
                Toast.makeText(getApplicationContext(), "Necesitas habilitar permisos de camara", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public boolean checkPermissionsRead_EXTERNAL_STORAGE(final Context context){
        int currentAPIVersions = Build.VERSION.SDK_INT;
        if (currentAPIVersions >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    showDialog("Permisos de almacenamiento interno necesario", context, Manifest.permission.READ_EXTERNAL_STORAGE);
                }else{
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            }else {
                return true;
            }
        }else{
            return true;
        }
    }

    public void showDialog(final String msg, final Context context, final String permissions){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permisos Necesarios");
        alertBuilder.setMessage(msg);
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permissions}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

}
