package com.example.tabletapp.item_selected_folder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletapp.MenuPageAlacarte;
import com.example.tabletapp.MenupageMilktea;
import com.example.tabletapp.R;
import com.example.tabletapp.imagesClass;
import com.example.tabletapp.item_selected_adapter;
import com.example.tabletapp.sessionManagement;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class item_selected extends AppCompatActivity {
Button minus,plus,cancelBtn,AddToOrder;

TextView itemNameDis,numberDis,descriptionDis, priceDis,textViewCat;
DatabaseReference reference;
FirebaseDatabase db;
com.example.tabletapp.sessionManagement sessionManagement;
com.example.tabletapp.item_selected_adapter item_selected_adapter;
    imagesClass imageNaCLASS ;
    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_selected);

        /// FULL SCREEN NO NAVIGATION AND SCROLL NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        db = FirebaseDatabase.getInstance();
        minus = findViewById(R.id.minusED);
        plus = findViewById(R.id.plusED);
        numberDis = findViewById(R.id.numberED);
        cancelBtn= findViewById(R.id.backED);
        AddToOrder = findViewById(R.id.saveED);
        itemNameDis = findViewById(R.id.itemNameED); textViewCat = findViewById(R.id.textViewCat);
        descriptionDis = findViewById(R.id.descriptionED);
        priceDis = findViewById(R.id.priceED);
        imageView3 = findViewById(R.id.imageView3);
        imageNaCLASS = new imagesClass();
        sessionManagement = new sessionManagement(getApplicationContext());
        //String username = getIntent().getStringExtra("username_key");
        String username = "tablet01";
        String itemHidID = getIntent().getStringExtra("hiddenId_key");

        imageView3.setImageResource(imageNaCLASS.passthepic(itemHidID));
        SetSaSimula(itemHidID);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(numberDis.getText().toString());
                if(number<=1){
                    numberDis.setText("1");
                }else {
                    number--;
                    String numberConv = Integer.toString(number);
                    numberDis.setText(numberConv);
                }
            }});

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(numberDis.getText().toString());
                number++;
                String numberConv = Integer.toString(number);
                numberDis.setText(numberConv);
            }});

        AddToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIf(username, itemHidID);
            }});

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }});


    }
    /** ========= Functions =====================*/

    public void SetSaSimula(String itemHidID){
        reference = db.getReference("item");
        reference.child(itemHidID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String itemName = snapshot.child("item_name").getValue().toString();
                String price = snapshot.child("price").getValue().toString();
                String descrip= snapshot.child("description").getValue().toString();
                String category= snapshot.child("category").getValue().toString();

                itemNameDis.setText(itemName);
                descriptionDis.setText(descrip);
                priceDis.setText(price);
                textViewCat.setText(category);
                item_selected_adapter = new item_selected_adapter(itemName, descrip,price,itemHidID, category, "blank");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(item_selected.this, "Error getting data", Toast.LENGTH_LONG).show();
            }
        });
    }

    // STEP 1 ==========
    public void checkIf(String username, String itemId){
        sessionManagement.setTotalChange(true);
        //KAPAG umoorder na dati
        if(sessionManagement.getOrdering()){
            String tempOrderID = sessionManagement.getOrderID();

            reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
            reference.child(tempOrderID).child("order").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int Counter = 1;
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        String identifier = snapshot1.child("adOns_identifier").getValue().toString();
                        if (identifier.equals("true")||identifier.equals("false")){
                            String thisItem= snapshot1.child("itemId").getValue().toString();
                            if (thisItem.equals(itemId)){
                                Counter++;
                            }
                        }
                    }
                    String tiny = String.valueOf(Counter);
                    String namedapat = itemId+ tiny;
                    addToOrderList(tempOrderID, itemId, namedapat);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        }
        //KApag first time umorder
        else {
            createNewOrder(username, itemId);
        }
    }
    //======= STEP 2 A WAla pang record
    public void createNewOrder(String username, String itemId){
        sessionManagement.setOrdering(true);
        Toast.makeText(item_selected.this, "Adding order...", Toast.LENGTH_SHORT).show();
        //Get random order ID
        reference = db.getReference("preset_tablet").push();
        String orderID = reference.getKey();

        sessionManagement.setOrderID(orderID);
            //info hash map
        HashMap infoUser = new HashMap();
        infoUser.clear();
        infoUser.put("userId", username);
        infoUser.put("orderId", orderID);


        String numQty= numberDis.getText().toString();
        item_selected_adapter.setQuantity(numQty);

        String numQty2 = item_selected_adapter.getQuantity();
        String itemN= item_selected_adapter.getItemName();
        String descK = item_selected_adapter.getDescription();
        String priceK = item_selected_adapter.getPrice();
        String categoryK = item_selected_adapter.getCategory();
        HashMap addOrdNeW = new HashMap();
        addOrdNeW.clear();
        addOrdNeW.put("item", itemN);
        addOrdNeW.put("qty", numQty2);
        addOrdNeW.put("itemId", itemId);
        addOrdNeW.put("description", descK);
        addOrdNeW.put("price", priceK);
        addOrdNeW.put("category", categoryK);
        addOrdNeW.put("adOns_identifier",false);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("preset_tablet");
        reference.child(orderID).child("info").setValue(infoUser);
        reference.child(orderID).child("order").child(itemId).setValue(addOrdNeW).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                back();
            }
        });

    }

    //=========== STEP 2 B Meron nang record
    public void addToOrderList(String tempOrderID, String itemId, String namedapat){

        String numQty= numberDis.getText().toString();
        item_selected_adapter.setQuantity(numQty);

        String numQty2 = item_selected_adapter.getQuantity();
        String itemN= item_selected_adapter.getItemName();
        String descK = item_selected_adapter.getDescription();
        String priceK = item_selected_adapter.getPrice();
        String categoryK = item_selected_adapter.getCategory();
        HashMap addOrd = new HashMap();
        addOrd.clear();
        addOrd.put("item", itemN);
        addOrd.put("qty", numQty2);
        addOrd.put("itemId", itemId);
        addOrd.put("description", descK);
        addOrd.put("price", priceK);
        addOrd.put("category", categoryK);
        addOrd.put("adOns_identifier",false);
       // OrderList ItemsIN = new OrderList(itemname,numQty);
        db = FirebaseDatabase.getInstance();
        reference = db.getReference("preset_tablet");
        reference.child(tempOrderID).child("order").child(namedapat).setValue(addOrd).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(item_selected.this, "Order Added", Toast.LENGTH_SHORT).show();
                back();
            }
        });
    }

    public void back(){
        Intent intent2 = new Intent(this, MenuPageAlacarte.class);
        //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent2);
        finish();
    }



}