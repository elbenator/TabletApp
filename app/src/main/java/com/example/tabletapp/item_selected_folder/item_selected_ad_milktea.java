package com.example.tabletapp.item_selected_folder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletapp.MenuPageCheesemilktea;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class item_selected_ad_milktea extends AppCompatActivity {
    //Toast.makeText(getApplicationContext(), addOns.get(i) , Toast.LENGTH_SHORT).show();
    Button minus,plus,cancelBtn,AddToOrder;
    TextView itemNameDis,numberDis,descriptionDis, priceDis,textViewCat;
    RadioButton price16, price22,radioButton;
    RadioGroup PriceRG;
    RadioGroup SugarLvlGroup;
    DatabaseReference reference;
    FirebaseDatabase db;
    public String addOnQty, addOnPrice, addOnName, sample;
    com.example.tabletapp.sessionManagement sessionManagement;
    com.example.tabletapp.item_selected_adapter item_selected_adapter;
    //para sa Add ons
    CheckBox checkboxPearl, checkBoxNata, checkBoxJelly;
    List<String> addOns;
    imagesClass imageNaCLASS ;
    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_selected_ad_milktea);

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
        itemNameDis = findViewById(R.id.itemNameED);textViewCat = findViewById(R.id.textViewCat);
        price22 = findViewById(R.id.price22); price16 = findViewById(R.id.price16); PriceRG = findViewById(R.id.PriceRG);
        SugarLvlGroup = findViewById(R.id.SugarLvlGroup); //z0percent = findViewById(R.id.z0percent); z25percent = findViewById(R.id.z25percent);
        //z50percent = findViewById(R.id.z50percent);z75percent = findViewById(R.id.z75percent);z100percent = findViewById(R.id.z100percent);
        descriptionDis = findViewById(R.id.descriptionED);
        priceDis = findViewById(R.id.priceED);
        checkBoxJelly = findViewById(R.id.checkBoxJelly);
        checkboxPearl = findViewById(R.id.checkBoxPearl);
        checkBoxNata = findViewById(R.id.checkBoxNata);
        imageView3 = findViewById(R.id.imageView3);
        addOns = new ArrayList<>();
        imageNaCLASS = new imagesClass();
        sessionManagement = new sessionManagement(getApplicationContext());
        String username = "tablet01";
        String itemHidID = getIntent().getStringExtra("hiddenId_key"); //Ganito talaga to wag delete

        imageView3.setImageResource(imageNaCLASS.passthepic(itemHidID));
        SetSaSimula(itemHidID);
        //
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
                String price = snapshot.child("price16").getValue().toString();
                String price2 = snapshot.child("price22").getValue().toString();
                String descrip= snapshot.child("description").getValue().toString();
                String category= snapshot.child("category").getValue().toString();

                itemNameDis.setText(itemName);
                descriptionDis.setText(descrip);
                price16.setText("₱ "+price);
                price22.setText("₱ "+ price2);
                textViewCat.setText(category);
                item_selected_adapter = new item_selected_adapter(itemName, descrip,price,itemHidID, category, price2);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error getting data", Toast.LENGTH_LONG).show();
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
    //STEP 2 A WAla pang record=========
    public void createNewOrder(String username, String itemId){
        sessionManagement.setOrdering(true);
        Toast.makeText(getApplicationContext(), "Adding order...", Toast.LENGTH_SHORT).show();
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
        String categoryK = item_selected_adapter.getCategory();
        HashMap addOrdNeW = new HashMap();
        addOrdNeW.clear();
        addOrdNeW.put("item", itemN);
        addOrdNeW.put("qty", numQty2);
        addOrdNeW.put("itemId", itemId);
        addOrdNeW.put("description", descK);
        addOrdNeW.put("price", returnPriceK());
        addOrdNeW.put("sugarLvl",returnSugar());
        addOrdNeW.put("category",categoryK);
        addOrdNeW.put("price16", item_selected_adapter.getPrice());
        addOrdNeW.put("price22", item_selected_adapter.getPrice22());

        //CHECK IF MAY ADD ONS
        if (checkBoxNata.isChecked() || checkboxPearl.isChecked()|| checkBoxJelly.isChecked()) {
            addOrdNeW.put("adOns_identifier",true);
            String nameDapat = itemId;
            checkKungMayAddOn(orderID, itemId, nameDapat,numQty2);
            //kadugtong(tempOrderID, itemId, addOnName, addOnPrice);
        }
        else{
            addOrdNeW.put("adOns_identifier",false);
        }


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
    public void addToOrderList(String tempOrderID, String itemId, String nameDapat){

        String numQty= numberDis.getText().toString();
        item_selected_adapter.setQuantity(numQty);

        String numQty2 = item_selected_adapter.getQuantity();
        String itemN= item_selected_adapter.getItemName();
        String descK = item_selected_adapter.getDescription();
        String categoryK = item_selected_adapter.getCategory();

        HashMap addOrd = new HashMap();
        addOrd.clear();
        addOrd.put("item", itemN);
        addOrd.put("qty", numQty2);
        addOrd.put("itemId", itemId);
        addOrd.put("description", descK);
        addOrd.put("price", returnPriceK());
        addOrd.put("category", categoryK);
        addOrd.put("sugarLvl",returnSugar());
        addOrd.put("price16", item_selected_adapter.getPrice());
        addOrd.put("price22", item_selected_adapter.getPrice22());

        //CHECK IF MAY ADD ONS
        if (checkBoxNata.isChecked() || checkboxPearl.isChecked()|| checkBoxJelly.isChecked()) {
            addOrd.put("adOns_identifier",true);
            checkKungMayAddOn(tempOrderID, itemId, nameDapat,numQty2);
            //kadugtong(tempOrderID, itemId, addOnName, addOnPrice);
        }else{
            addOrd.put("adOns_identifier",false);
        }


        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(tempOrderID).child("order").child(nameDapat).updateChildren(addOrd).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Order Added", Toast.LENGTH_SHORT).show();
                back();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Item Failed to Add", Toast.LENGTH_SHORT).show();
            }
        });
       // back();
    }

    //check kung may add ons
    public void checkKungMayAddOn(String tempOrderID, String itemId, String nameDapat,String qtyParent){

        if (checkBoxNata.isChecked()) {
            addOns.add("SinkNata");
        }
        if (checkboxPearl.isChecked()) {
            addOns.add("Sinkpearl");
        }
        if (checkBoxJelly.isChecked()) {
           addOns.add("SinkCoffJelly");
        }
        addOnQty = "1";
        int size = addOns.size();
        size--;
        for (int n=0; n<=size; n++){
            //Toast.makeText(getApplicationContext(), addOns.get(i) , Toast.LENGTH_SHORT).show();
            String IdngAdd = addOns.get(n);
            reference = db.getReference("item");
            reference.child(IdngAdd).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String itemName = snapshot.child("item_name").getValue().toString();
                    String price = snapshot.child("price").getValue().toString();

                    addOnName = itemName;
                    addOnPrice = price;

                    HashMap addOnHash = new HashMap();
                    addOnHash.put("item_name", addOnName);
                    addOnHash.put("price", addOnPrice);
                    addOnHash.put("addOnId", IdngAdd);
                    addOnHash.put("qty", addOnQty);
                    addOnHash.put("adOns_identifier", nameDapat);
                    //addOnHash.put("qtyParent", qtyParent);
                    reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
                    reference.child(tempOrderID).child("order").child(nameDapat+IdngAdd).updateChildren(addOnHash);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error getting data", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void back(){
        String eto = textViewCat.getText().toString();
        if (eto.equals("classicmilktea")){
            Intent intent2 = new Intent(this, MenupageMilktea.class);
            //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent2);
            finish();
        }else {
            Intent intent2 = new Intent(this, MenuPageCheesemilktea.class);
            //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent2);
            finish();
        }
    }

    public String returnPriceK(){
        //for the radio button
        String priceK;
        int radioId =PriceRG.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String priceText = radioButton.getText().toString();
        if (priceText.equals("₱ " +item_selected_adapter.getPrice22())){
            priceK = item_selected_adapter.getPrice22();
        }else {
            priceK = item_selected_adapter.getPrice();
        }

        return priceK;
    }

    public String returnSugar(){
        //for the radio button

        int radioId = SugarLvlGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String sugar = radioButton.getText().toString();
        return sugar;
    }

}