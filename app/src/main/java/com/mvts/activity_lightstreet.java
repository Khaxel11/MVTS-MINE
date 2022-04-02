package com.mvts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_lightstreet extends AppCompatActivity {

    private LinearLayout l1, l2, l3;
    private int LightState;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightstreet);
        init();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("semaforo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    LightState = Integer.parseInt(snapshot.child("semaforo1").getValue().toString());
                    if(LightState == 0){
                        l1.setBackgroundColor(getResources().getColor(R.color.red));
                        l2.setBackgroundColor(getResources().getColor(R.color.gray));
                        l3.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    if(LightState == 1){
                        l1.setBackgroundColor(getResources().getColor(R.color.gray));
                        l2.setBackgroundColor(getResources().getColor(R.color.yellow));
                        l3.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                    if(LightState > 1){
                        l1.setBackgroundColor(getResources().getColor(R.color.gray));
                        l2.setBackgroundColor(getResources().getColor(R.color.gray));
                        l3.setBackgroundColor(getResources().getColor(R.color.green));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init(){
        l1 = findViewById(R.id.light1);
        l2 = findViewById(R.id.light2);
        l3 = findViewById(R.id.light3);
        l1.setBackgroundColor(getResources().getColor(R.color.gray));
        l2.setBackgroundColor(getResources().getColor(R.color.gray));
        l3.setBackgroundColor(getResources().getColor(R.color.gray));

    }
    //public void onClickStart(View v){

    //}


}