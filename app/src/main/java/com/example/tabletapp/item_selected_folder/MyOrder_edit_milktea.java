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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletapp.LoadingDialog;
import com.example.tabletapp.MenupageMilktea;
import com.example.tabletapp.MyOrder_Page;
import com.example.tabletapp.R;
import com.example.tabletapp.imagesClass;
import com.example.tabletapp.sessionManagement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyOrder_edit_milktea extends AppCompatActivity {
    //MILKTEA
    Button minus,plus,cancelBtn,deleteMY, saveED;
    TextView itemNameDis,numberDis,descriptionDis, priceDis, textViewCat;
    DatabaseReference reference;
    FirebaseDatabase db;
    RadioButton price16, price22,radioButton; String priceK;
    RadioGroup PriceRG;
    RadioButton z0percent, z25percent, z50percent,z75percent, z100percent;
    RadioGroup SugarLvlGroup;
    TextView price22Store, price16Store;
    imagesClass imageNaCLASS ;
    ImageView imageView3;
    LoadingDialog loadingDialog;
    public String addOnId, addOnQty, addOnPrice, addOnName;
    com.example.tabletapp.sessionManagement sessionManagement;
    //para sa Add ons
    CheckBox checkboxPearl, checkBoxNata, checkBoxJelly;
    List<String> addOns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_edit_milktea);

        /// FULL SCREEN NO NAVIGATION AND SCROLL NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        db = FirebaseDatabase.getInstance();
        price22 = findViewById(R.id.price22); price16 = findViewById(R.id.price16); PriceRG = findViewById(R.id.PriceRG); price22Store = findViewById(R.id.price22Store);price16Store = findViewById(R.id.price16Store);
        SugarLvlGroup = findViewById(R.id.SugarLvlGroup); z0percent = findViewById(R.id.z0percent); z25percent = findViewById(R.id.z25percent);
        z50percent = findViewById(R.id.z50percent);z75percent = findViewById(R.id.z75percent);z100percent = findViewById(R.id.z100percent);
        minus = findViewById(R.id.minusED);textViewCat = findViewById(R.id.textViewCat);
        plus = findViewById(R.id.plusED);
        numberDis = findViewById(R.id.numberED);
        cancelBtn= findViewById(R.id.backED);
        itemNameDis = findViewById(R.id.itemNameED);
        descriptionDis = findViewById(R.id.descriptionED);
        priceDis = findViewById(R.id.priceED);
        deleteMY = findViewById(R.id.deleteMY);
        saveED =findViewById(R.id.saveED);
        sessionManagement = new sessionManagement(getApplicationContext());
        checkBoxJelly = findViewById(R.id.checkBoxJelly);
        checkboxPearl = findViewById(R.id.checkBoxPearl);
        checkBoxNata = findViewById(R.id.checkBoxNata);
        addOns = new ArrayList<>();
        imageNaCLASS = new imagesClass();
        imageView3 = findViewById(R.id.imageView3);
        String orderID= sessionManagement.getOrderID();
        String itemHiddID = getIntent().getStringExtra("MYhiddenId_key");
        String realName = getIntent().getStringExtra("realName");

        //loaddd
         loadingDialog = new LoadingDialog( MyOrder_edit_milktea.this);


        SetSaSimula2(itemHiddID, orderID);

        imageView3.setImageResource(imageNaCLASS.passthepic(realName));
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

        saveED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissDialog();
                        saveChanges(itemHiddID, orderID);

                    }
                }, 500);
               // saveChanges(itemHiddID, orderID);
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
                        deleteThis(itemHiddID, orderID);
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

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backNormal();
            }
        });
    }
//=======================================================================
    public void SetSaSimula2(String itemHidID, String orderID){
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String itemName = snapshot.child("item").getValue().toString();
                String price = snapshot.child("price").getValue().toString();
                String descrip= snapshot.child("description").getValue().toString();
                String qty = snapshot.child("qty").getValue().toString();
                String addOnId= snapshot.child("adOns_identifier").getValue().toString();
                String price16S = snapshot.child("price16").getValue().toString();
                String price22S = snapshot.child("price22").getValue().toString();
                String sugar=snapshot.child("sugarLvl").getValue().toString();
                String categ = snapshot.child("category").getValue().toString();
                textViewCat.setText(categ);
                itemNameDis.setText(itemName);
                descriptionDis.setText(descrip);
               numberDis.setText(qty);
               price16.setText("₱ "+price16S); price16Store.setText(price16S);
               price22.setText("₱ "+price22S); price22Store.setText(price22S);

               //price
                if (price.equals(price22S)){
                    price22.setChecked(true);
                }else {
                    price16.setChecked(true);
                }

                //Sugarlvl
                if (sugar.equals("0 %")){
                    z0percent.setChecked(true);
                }else if(sugar.equals("25 %")){
                    z25percent.setChecked(true);
                }else if(sugar.equals("50 %")){
                    z50percent.setChecked(true);
                }else if(sugar.equals("75 %")){
                    z75percent.setChecked(true);
                }else if(sugar.equals("100 %")){
                    z100percent.setChecked(true);
                }

               if (addOnId.equals("true")){
                   //Toast.makeText(getApplicationContext(), "umabot dito1", Toast.LENGTH_SHORT).show();
                   kapagMayAddOn(itemHidID, orderID);
               }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error getting data", Toast.LENGTH_LONG).show();
            }
        });
    }
    //Set sa simula KAPAG may addOns
    public void kapagMayAddOn(String itemHidID, String orderID){
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    String identifier = snapshot1.child("adOns_identifier").getValue().toString();

                    if (identifier.equals(itemHidID)){
                        String SinkNata = itemHidID+"SinkNata";
                        String Sinkpearl = itemHidID+"Sinkpearl";
                        String SinkCoffJelly = itemHidID+"SinkCoffJelly";

                        String zname = snapshot1.getKey();
                        if (zname.equals(SinkNata)){
                            checkBoxNata.setChecked(true);
                        }else if (zname.equals(Sinkpearl)){
                            checkboxPearl.setChecked(true);
                        }else if(zname.equals(SinkCoffJelly)){
                            checkBoxJelly.setChecked(true);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void saveChanges(String itemHidID, String orderID){
        String quanty = numberDis.getText().toString();
        HashMap hashQTY = new HashMap();
        hashQTY.clear();
        hashQTY.put("qty",quanty);
        hashQTY.put("price", returnPriceK());
        hashQTY.put("sugarLvl",returnSugar());

        //CHECK IF MAY ADD ONS
        if (checkBoxNata.isChecked() || checkboxPearl.isChecked()|| checkBoxJelly.isChecked()) {
            hashQTY.put("adOns_identifier",true);
            checkKungMayAddOn(orderID, itemHidID, quanty);
        }
        else{
            hashQTY.put("adOns_identifier",false);
            reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
            reference.child(orderID).child("order").child(itemHidID+"SinkNata").removeValue();
            reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
            reference.child(orderID).child("order").child(itemHidID+"Sinkpearl").removeValue();
            reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
            reference.child(orderID).child("order").child(itemHidID+"SinkCoffJelly").removeValue();
        }

        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID).updateChildren(hashQTY);
        backNormal();
    }
    //kapag may addOns
    //check kung may add ons
    public void checkKungMayAddOn(String tempOrderID, String itemId, String qtyParent){
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(tempOrderID).child("order").child(itemId+"SinkNata").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(tempOrderID).child("order").child(itemId+"Sinkpearl").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(tempOrderID).child("order").child(itemId+"SinkCoffJelly").removeValue();

        if (checkBoxNata.isChecked()) {
            addOns.add("SinkNata");
        }
        if (checkboxPearl.isChecked()) {
            addOns.add("Sinkpearl");
        }
        if (checkBoxJelly.isChecked()) {
            addOns.add("SinkCoffJelly");
        }
        //Toast.makeText(getApplicationContext(), addOns.get(0) + " ----"+addOns.get(1), Toast.LENGTH_LONG).show();
        addOnQty = "1";
        int size = addOns.size();
        size--;
        for (int n=0; n<=size; n++) {
            String IdngAdd = addOns.get(n);
            reference = FirebaseDatabase.getInstance().getReference("item");
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
                    addOnHash.put("adOns_identifier", itemId);
                   // addOnHash.put("qtyParent", qtyParent);
                    reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
                    reference.child(tempOrderID).child("order").child(itemId+IdngAdd).updateChildren(addOnHash);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Error getting data", Toast.LENGTH_LONG).show();
                }
            });
        }

    }


    public void deleteThis(String itemHidID, String orderID){
        loadingDialog.dismissDialog();
        backNormal();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID+"SinkNata").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID+"Sinkpearl").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID+"SinkCoffJelly").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID).removeValue();

    }

    public String returnPriceK(){
        //for the radio button
        int radioId = PriceRG.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String priceText = radioButton.getText().toString();

        String price2222= price22Store.getText().toString();
        if (priceText.equals("₱ "+price2222)){
            priceK = price22Store.getText().toString();
        }else {
            priceK = price16Store.getText().toString();
        }
        return priceK;
    }

    public String returnSugar(){
        //for the radio button

        int radioId =SugarLvlGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String sugar = radioButton.getText().toString();

        return sugar;
    }
    public void backNormal(){
        Intent intent = new Intent(MyOrder_edit_milktea.this, MyOrder_Page.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}