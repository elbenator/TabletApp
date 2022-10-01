package com.example.tabletapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button dineInBtn, takeOutBtn, btngene;
    DatabaseReference reference;
    sessionManagement sessionManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManagement = new sessionManagement(getApplicationContext());
        dineInBtn =findViewById(R.id.dineInBtn);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);



        dineInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           // emergencyClear();
                next();
            }
        });
    }
    public void next(){
        Intent intent = new Intent(this, MenupageRicebowl.class);
        startActivity(intent);

    }
    public void emergencyClear(){
        sessionManagement.setOrdering(false);
        sessionManagement.setOrderID("");
        Toast.makeText(this, "Yan na Clear na! ", Toast.LENGTH_LONG).show();
    }



}