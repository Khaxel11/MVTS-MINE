package com.mvts;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDatabase;

    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();

    private ArrayList<Marker> tmpRealTimeSemaforo = new ArrayList<>();
    private ArrayList<Marker> realTimeSemaforo = new ArrayList<>();

    private ArrayList<Marker> tmpRealTimeMina = new ArrayList<>();
    private ArrayList<Marker> realTimeMina = new ArrayList<>();


    private ArrayList<Marker> tmpRealTimeInter = new ArrayList<>();
    private ArrayList<Marker> realTimeInter = new ArrayList<>();

    private LocationManager locationManager;
    private Location mLocation;
    private int LightState;
    private int stateColor;
    private int stateCongest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fin = findViewById(R.id.finish);
        Intent inted = new Intent(this, Finish.class);
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "ACTIVO", Toast.LENGTH_LONG).show();

                startActivity(inted);
            }
        });
        //SEMAFORO ESTABLECEDOR DE VALOR LIGHTSTATE
        mDatabase = FirebaseDatabase.getInstance().getReference();
        /*mDatabase.child("semaforo").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    LightState = Integer.parseInt(snapshot.child("semaforo1").getValue().toString());
                    if(LightState == 0){
                        stateColor = 0;

                    }
                    if(LightState == 1){
                        stateColor = 1;

                    }
                    if(LightState > 1){
                        stateColor = 2;

                    }
                }
                onMapReady(mMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        mDatabase.child("congestion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshotc : snapshot.getChildren()) {
                        stateCongest = Integer.parseInt(snapshot.child("estado").getValue().toString());
                     }
                    if(stateCongest == 1 ){
                        AlertDialog.Builder builder = new AlertDialog.Builder(mapFragment.getContext());
                        builder.setTitle("CUIDADO");
                        builder.setMessage("CONGESTIÓN REPORTADA");
                        builder.setPositiveButton("Aceptar", null);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                if(stateCongest == 0 ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mapFragment.getContext());
                    builder.setTitle("CONTINUA");
                    builder.setMessage("CONGESTIÓN DESAPARECIDA");
                    builder.setPositiveButton("Aceptar", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
/*
        mDatabase.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (Marker marker : realTimeSemaforo){
                    marker.remove();
                }

                for(DataSnapshot snapshot2 : snapshot.getChildren()) {
                    semaforo sf = snapshot2.getValue(semaforo.class);
                    Double latitud = sf.getLatitud();
                    Double longitud = sf.getLongitud();
                    String description = sf.getDescription();
                    int estado = sf.getEstado();

                    //MARKER
                    MarkerOptions markerOptions2 = new MarkerOptions();
                    markerOptions2.position(new LatLng(latitud, longitud)).title(description);

                    if (estado == 0) {
                        //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                    if (estado == 1) {
                        //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    }
                    if (estado == 2) {
                        //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }


                    //----------->
                    tmpRealTimeSemaforo.add(mMap.addMarker(markerOptions2));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
        LatLng [] coordsInter1 = new LatLng[]{new LatLng(27.102915, -109.414175),new LatLng(27.102762, -109.414352), new LatLng(27.102698, -109.414017)};
        //INTERSECCIONES
        mDatabase.child("intersecciones/1/Semaforos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (Marker marker : realTimeInter){
                    marker.remove();
                }
                    int i = 0;
                for(DataSnapshot snapshot2 : snapshot.getChildren()) {


                    //MARKER
                    MarkerOptions markerOptions2 = new MarkerOptions();
                    //Double lati = Double.parseDouble(latitud);
                    //Double longi = Double.parseDouble(longitud);
                    String estado = snapshot2.getValue(String.class);
                    markerOptions2.position(coordsInter1[i]).title("1");
                    Log.i("info", "ESTADO" + estado);

                    if (estado == null ) {
                        //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    }
                    if (estado.equals("0")) {
                        //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    }
                    if (estado.equals("1")) {
                        //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    }
                    if (estado.equals("2")) {
                        //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }
                    if (estado.equals("3")) {
                        //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    }
                    if (estado.equals("4")) {
                        //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }


                    //----------->
                    tmpRealTimeInter.add(mMap.addMarker(markerOptions2));
                    i++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
        mMap = googleMap;
       mDatabase.child("ubicaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (Marker marker : realTimeMarkers){
                    marker.remove();
                }

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Mapainicio mp = snapshot.getValue(Mapainicio.class);
                    Double latitud = mp.getLatitud();
                    Double longitud = mp.getLongitud();
                    String description = mp.getDescription();

                    //MARKER
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitud,longitud)).title(description);

                    if(stateColor == 0){
                        markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                    if(stateColor == 1){
                        markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    }
                    if(stateColor == 2){
                        markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }


                    //----------->*/
                    tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));

                    /*Polyline line = mMap.addPolyline(new PolylineOptions()
                            .clickable(true)
                            .add(new LatLng(27.072264098926592, -109.43858350114631), new LatLng(27.078893211569195, -109.4422326083126))
                            .width(5)
                            .color(Color.RED));*/

                   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud), 35));
                   mMap.animateCamera(CameraUpdateFactory.zoomIn());
                   mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.child("minas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (Marker marker : realTimeMina){
                    marker.remove();
                }

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mina mn = snapshot.getValue(mina.class);
                    Double latitud = mn.getLatitud();
                    Double longitud = mn.getLongitud();
                    String nombre = mn.getNombre();
                    //Log.i("info", mn.getNombre());

                    Log.i("info", mn.getNombre()+" AHORA");

                    //MARKER
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitud,longitud)).title(nombre);
                    if(mn.getNombre().toString().equals("Mina Carbón")){
                        Log.i("info", mn.getNombre()+"CORRECTO");

                        markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_material, "carbon"));
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                    if(mn.getNombre().toString().equals("Mina Diamantes")){
                        markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_material, "diamantes"));
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        Log.i("info", mn.getNombre()+"CORRECTO");
                    }
                    if(mn.getNombre().toString().equals("Mina Hierro")){
                        markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_material, "hierro"));
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        Log.i("info", mn.getNombre()+"CORRECTO");
                    }
                    if(mn.getNombre().toString().equals("Mina Esmeralda")){
                        markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_material, "esmeralda"));
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        Log.i("info", mn.getNombre()+"CORRECTO");
                    }
                    if(mn.getNombre().toString().equals("Mina Oro")){
                        markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_material, "oro"));
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        Log.i("info", mn.getNombre()+"CORRECTO");
                    }


                    //----------->*/
                    tmpRealTimeMina.add(mMap.addMarker(markerOptions));

                    /*Polyline line = mMap.addPolyline(new PolylineOptions()
                            .clickable(true)
                            .add(new LatLng(27.072264098926592, -109.43858350114631), new LatLng(27.078893211569195, -109.4422326083126))
                            .width(5)
                            .color(Color.RED));*/

                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud,longitud), 200));

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



      /*mDatabase.child("usuarios").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (Marker marker : realTimeSemaforo){
                   marker.remove();
               }
               int estado = 5;
               for(DataSnapshot snapshot2 : snapshot.getChildren()) {
                   semaforo sf = snapshot2.getValue(semaforo.class);
                   Double latitud = sf.getLatitud();
                   Double longitud = sf.getLongitud();
                   String description = sf.getDescription();
                   estado = sf.getEstado();

                   //MARKER
                   MarkerOptions markerOptions2 = new MarkerOptions();
                   markerOptions2.position(new LatLng(latitud, longitud)).title(description);
                   markerOptions2.draggable(false);
                   //markerOptions2.isDraggable();

                   if (estado == 0) {
                       //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                       markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                   }
                   if (estado == 1) {
                       //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                       markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                   }
                   if (estado == 2) {
                       //markerOptions.icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_car));
                       markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                   }


                   //----------->
                   tmpRealTimeSemaforo.add(mMap.addMarker(markerOptions2));

               }
                    estado = 5;
               }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });*/
    }

    public void obtainRouteIN(){
        String url;
        try {
            url = "";
        }catch (Exception e){

        }
    }
    public void fish(){

    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_map_pin_filled_blue_48dp);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(20, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId, String mina) {
        Drawable background = null;

        if(mina == "carbon"){
            int fondo = R.drawable.ic_carbon;
            background = ContextCompat.getDrawable(context, fondo);
        }
        if(mina == "diamantes"){
            int fondo = R.drawable.ic_diamantes;
            background = ContextCompat.getDrawable(context, fondo);
        }
        if(mina == "hierro"){
            int fondo = R.drawable.ic_hierro;
            background = ContextCompat.getDrawable(context, fondo);
        }
        if(mina == "oro"){
            int fondo = R.drawable.ic_oro;
            background = ContextCompat.getDrawable(context, fondo);
        }
        if(mina == "esmeralda"){
            int fondo = R.drawable.ic_esmeralda;
            background = ContextCompat.getDrawable(context, fondo);
        }
        //Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_map_pin_filled_blue_48dp);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(20, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        Log.i("info", "PUNTO BIEN" + mina);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}