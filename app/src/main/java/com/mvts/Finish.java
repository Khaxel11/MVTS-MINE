package com.mvts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Finish extends AppCompatActivity {
    Button btnTerminar;
    EditText txtID;
    EditText txtNombre;
    Spinner spMateriales;
    Spinner spInicioRuta;
    Spinner spFinRuta;
    Date fecha;
    DatabaseReference mDatabase;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        String[] Materials = {"Carbon", "Hierro", "Lapizazuli", "Oro", "Esmeralda", "Diamante"};
        String[] Rutas = {"Mina Carbon", "Mina Hierro", "Mina Lapizazuli", "Mina Oro", "Mina Esmeralda", "Mina Diamante", "Ruta Transportes", "Ruta proveedores"};
        ArrayAdapter<String> adapterMat = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,Materials);
        ArrayAdapter<String> adapterRut = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Rutas);
        btnTerminar = (Button) findViewById(R.id.btnTerminar);
        txtID = (EditText) findViewById(R.id.txtID);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        spMateriales = (Spinner) findViewById(R.id.spMateriales);
        spInicioRuta = (Spinner) findViewById(R.id.spInRuta);
        spFinRuta = (Spinner) findViewById(R.id.spFinRuta);

        spMateriales.setAdapter(adapterMat);
        spInicioRuta.setAdapter(adapterRut);
        spFinRuta.setAdapter(adapterRut);

        //for(DataSnapshot snapshot : dataSnapshot.getChildren()){



            btnTerminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registerTravel();
                    finish();
                }
            });
    }

    private void registerTravel(){
        //int id = Integer.parseInt(txtID.getText().toString());
        String id = txtID.getText().toString();
        String nombre = txtNombre.getText().toString();
        String materialz = spMateriales.getSelectedItem().toString();
        String start = spInicioRuta.getSelectedItem().toString();
        String finish = spFinRuta.getSelectedItem().toString();
        Calendar cal = new GregorianCalendar();
        if(start.equals(finish) || finish.equals(start)){
            Toast.makeText(this, "No puedes iniciar o terminar donde mismo", Toast.LENGTH_LONG).show();
            //finish();
        }
        else{
            fecha = cal.getTime();
            String today = fecha.toString();
            mDatabase = FirebaseDatabase.getInstance().getReference("viajes");
            viaje travel = new viaje(nombre, materialz, start, finish, today);

            mDatabase.child(id).setValue(travel);
            Toast.makeText(this, "REGISTRADO", Toast.LENGTH_LONG).show();
        }




    }
}