package com.example.tabletapp.item_selected_folder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletapp.LoadingDialog;
import com.example.tabletapp.MainActivity;
import com.example.tabletapp.MenuPageAlacarte;
import com.example.tabletapp.MyOrder_Page;
import com.example.tabletapp.R;
import com.example.tabletapp.imagesClass;
import com.example.tabletapp.recycler_adapter2;
import com.example.tabletapp.sessionManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MyOrder_edit extends AppCompatActivity {
    Button minusMY,plusMY, backMY, saveMY, deleteMY;
    TextView itemNameMY,numberMY,descriptionMY, priceMY;
    DatabaseReference reference;
    com.example.tabletapp.sessionManagement sessionManagement;
    ProgressBar progressBar;
    recycler_adapter2 adapter;
    imagesClass imageNaCLASS ;
    ImageView imageView3;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_edit);

        /// FULL SCREEN NO NAVIGATION AND SCROLL NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        minusMY = findViewById(R.id.minusED);
        plusMY = findViewById(R.id.plusED);
        numberMY = findViewById(R.id.numberED);
        backMY= findViewById(R.id.backFCK);
        saveMY = findViewById(R.id.saveMY);
        itemNameMY = findViewById(R.id.itemNameED);
        descriptionMY = findViewById(R.id.descriptionED);
        priceMY = findViewById(R.id.priceED);
        deleteMY = findViewById(R.id.deleteMY);
        sessionManagement = new sessionManagement(getApplicationContext());
        imageNaCLASS = new imagesClass();
        imageView3 = findViewById(R.id.imageView3);
        String orderID= sessionManagement.getOrderID();
        String itemHidID = getIntent().getStringExtra("MYhiddenId_key");
        String realName = getIntent().getStringExtra("realName");
        SetSaSimula2(itemHidID, orderID);
        imageView3.setImageResource(imageNaCLASS.passthepic(realName));
        //loaddd
         loadingDialog = new LoadingDialog( MyOrder_edit.this);


        minusMY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(numberMY.getText().toString());
                if(number<=1){
                    numberMY.setText("1");
                }else {
                    number--;
                    String numberConv = Integer.toString(number);
                    numberMY.setText(numberConv);
                }
            }});

        plusMY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(numberMY.getText().toString());
                number++;
                String numberConv = Integer.toString(number);
                numberMY.setText(numberConv);
            }});

        saveMY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(itemHidID, orderID);

            }
        });

        deleteMY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Remove Item");
                builder.setMessage("Remove this from Your Orders?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadingDialog.startLoadingDialog();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                deleteThis(itemHidID, orderID);

                            }
                        }, 400);
                       // deleteThis(itemHidID, orderID);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog =builder.create();
                alertDialog.show();
            }
        });

        backMY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backNormal();
            }
        });

    }
    /**=========== functions =================*/

    public void SetSaSimula2(String itemHidID, String orderID){

        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String itemName = snapshot.child("item").getValue().toString();
                String price = snapshot.child("price").getValue().toString();
                String descrip= snapshot.child("description").getValue().toString();
                String qty = snapshot.child("qty").getValue().toString();

                itemNameMY.setText(itemName);
                descriptionMY.setText(descrip);
                priceMY.setText(price);
                numberMY.setText(qty);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyOrder_edit.this, "Error getting data", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void saveChanges(String itemHidID, String orderID){
        String quanty = numberMY.getText().toString();
        HashMap hashQTY = new HashMap();
        hashQTY.clear();
        hashQTY.put("qty",quanty);

        backNormal();

        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID).updateChildren(hashQTY).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

            }
        });

    }

    public void deleteThis(String itemHidID, String orderID){
        loadingDialog.dismissDialog();
        backNormal();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID).removeValue();

    }
    public void backNormal(){
       Intent intent = new Intent(this, MyOrder_Page.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent);
        finish();
    }
}