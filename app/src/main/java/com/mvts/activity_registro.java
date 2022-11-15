package com.mvts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_registro extends AppCompatActivity {

    EditText txtNombreUser, txtNombre, txtApellido, txtCorreo, txtPassword, txtConfirmPassword;
    Spinner spVehiculo;
    Button btnNewCuenta;

    CircleImageView imgCirculoPerfil;
    FloatingActionButton fabCamara, fabGaleria;

    FirebaseDatabase mdatabase;
    DatabaseReference mreference;

    private static final int REQUEST_PERMISSION_CAMERA = 100;
    //private static final int REQUEST_IMAGE_CAMERA = 101;

    private static final int REQUEST_IMAGE_CAMERA = 101;
    private static final int File = 1;
    Uri FileUri;

    boolean imagen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        String[] veh = {"Cheyene", "Lobo", "Nissan", "Ford", "Chevrolet", "Jeppeta"};
        ArrayAdapter<String> adapterMat = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,veh);
        mdatabase = FirebaseDatabase.getInstance();

        mreference = mdatabase.getReference("minero");
        txtNombreUser = (EditText) findViewById(R.id.txtNombreUser);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellido = (EditText) findViewById(R.id.txtApellido);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtConfirmPassword = (EditText) findViewById(R.id.txtConfirmPassword);
        spVehiculo = (Spinner) findViewById(R.id.spVehiculo);

        fabCamara = (FloatingActionButton) findViewById(R.id.fabCamara);
        fabGaleria = (FloatingActionButton) findViewById(R.id.fabGaleria);
        imgCirculoPerfil = (CircleImageView) findViewById(R.id.imgCirculoPerfil);

        spVehiculo.setAdapter(adapterMat);
        btnNewCuenta = (Button) findViewById(R.id.btnNewCuenta);
        btnNewCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registrar();
            }
        });

        fabCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TomarFoto();
            }
        });
        fabGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUpload();
            }
        });
    }
    private void Registrar(){
        //int i = 0;
        if(txtNombreUser.getText().toString().trim().toString().equals("") ||
                txtNombre.getText().toString().trim().toString().equals("") ||
                txtApellido.getText().toString().trim().toString().equals("")  ||
                txtCorreo.getText().toString().trim().toString().equals("") ||
                txtPassword.getText().toString().trim().toString().equals("")  ||
                txtConfirmPassword.getText().toString().trim().toString().equals("") || imagen == false){
                //showDialog("Faltan datos por rellenar", "Registro no Exitoso", getApplicationContext());
            Toast.makeText(this, "Faltan datos por rellenar", Toast.LENGTH_LONG).show();
        }else{
            String username = txtNombreUser.getText().toString();
            String nombre = txtNombre.getText().toString();
            String apellido = txtApellido.getText().toString();
            String correo = txtCorreo.getText().toString();
            String password = txtPassword.getText().toString();
            String confirmpassword = txtConfirmPassword.getText().toString();
            String vehiculo = spVehiculo.getSelectedItem().toString();
            String imagen = "gs://mvts-5855f.appspot.com/"+username;
            if(!(password.equals(confirmpassword))){
                //showDialog("Las contraseñas no coinciden", "Error Contraseñas", getApplicationContext());
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }else{
                minero miner = new minero(nombre, apellido, correo, password, vehiculo, imagen);
                mreference.child(username).setValue(miner);
                try {

                    StorageReference folder = FirebaseStorage.getInstance().getReference().child(username);
                    folder.putFile(FileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            StorageReference dateRef = storageRef.child(username);
                            dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    String photoLink = downloadUrl.toString();
                                    //Glide.with(getContext()).load(photoLink).into(imgUser);
                                }
                            });
                            txtNombreUser.setText("");
                            txtNombre.setText("");
                            txtApellido.setText("");
                            txtCorreo.setText("");
                            txtPassword.setText("");
                            txtConfirmPassword.setText("");
                            spVehiculo.setSelection(0);
                            txtNombreUser.requestFocus();
                            imgCirculoPerfil.setImageDrawable(getResources().getDrawable(R.drawable.user_miner));
                            Toast.makeText(getApplicationContext(), "¡REGISTRADO!", Toast.LENGTH_SHORT).show();
                        }
                    });


                }catch (IllegalArgumentException ex){

                }


            }
        }


    }

    /*public void addImage(Uri uri){
        //referencia hacia el nodo padre de Storage (NO EXISTE NINGUNA CARPETA), nombre de la foto -->prueba.jpg
        final StorageReference reference = FirebaseStorage.getInstance().getReference().child("prueba"+".jpg");
        UploadTask uploadTask = reference.putFile(uri);// insertas la foto en Storage.

        //continuo con la operación para obtener la ruta de Storage
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return reference.getDownloadUrl(); //RETORNO LA  URL DE DESCARGA DE LA FOTO
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri uri = task.getResult();  //AQUI YA TENGO LA RUTA DE LA FOTO LISTA PARA INSERTRLA EN DATABASE
                    assert uri != null;
                    addImagetodDatabase(uri);  //método para insertar url de la foto en Database

                }
            }
        });
    }*/



    private void TomarFoto(){
        Intent inte = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(inte, REQUEST_IMAGE_CAMERA);
    }

    private void FileUpload(){

        Intent inte = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(inte, File);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == File){
            if(resultCode == RESULT_OK){
                FileUri = data.getData();
                Glide.with(getApplicationContext()).load(FileUri).into(imgCirculoPerfil);
                imagen = true;
            }
       }
        if(requestCode == REQUEST_IMAGE_CAMERA){
            if(resultCode == Activity.RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
                String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "Imagen", null);

                FileUri = Uri.parse(path);
                Glide.with(getApplicationContext()).load(FileUri).into(imgCirculoPerfil);
                imagen = true;
            }
        }
    }
    public void showDialog(String mensaje, String titulo, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}