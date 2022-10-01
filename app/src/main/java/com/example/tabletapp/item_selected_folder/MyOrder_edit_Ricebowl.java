package com.example.tabletapp.item_selected_folder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletapp.LoadingDialog;
import com.example.tabletapp.MyOrder_Page;
import com.example.tabletapp.R;
import com.example.tabletapp.imagesClass;
import com.example.tabletapp.sessionManagement;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyOrder_edit_Ricebowl extends AppCompatActivity {
    Button minus,plus,cancelBtn,deleteMY, saveED;
    TextView itemNameDis,numberDis,descriptionDis, priceDis,textViewCat;
    DatabaseReference reference;
    FirebaseDatabase db;

    public String addOnPrice, addOnName;
    com.example.tabletapp.sessionManagement sessionManagement;
    com.example.tabletapp.item_selected_adapter item_selected_adapter;
    //for add ons
    CheckBox addOntapaCheck;
    Button addSiomaiDown, addSiomaiUp,addShanghaiDown, addShanghaiUp, addSharkDown, addSharkUp;
    TextView addShark, addSiomai, addShanghai;
    List<String> addOns;
    List<String> QtyaddOns;
    imagesClass imageNaCLASS ;
    ImageView imageView3;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_edit__ricebowl);

        /// FULL SCREEN NO NAVIGATION AND SCROLL NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        addOntapaCheck = findViewById(R.id.addTapaCheck);textViewCat = findViewById(R.id.textViewCat);
        addSiomaiDown = findViewById(R.id.addSiomaiDown); addSiomaiUp = findViewById(R.id.addSiomaiUp); addSiomai = findViewById(R.id.addSiomai);
        addShanghaiDown = findViewById(R.id.addShanghaiDown); addShanghaiUp = findViewById(R.id.addShanghaiUp); addShanghai =findViewById(R.id.addShanghai);
        addSharkDown = findViewById(R.id.addSharkDown); addSharkUp = findViewById(R.id.addSharkUp); addShark = findViewById(R.id.addShark);
        addOns = new ArrayList<>();
        QtyaddOns = new ArrayList<>();
        imageNaCLASS = new imagesClass();
        imageView3 = findViewById(R.id.imageView3);
        db = FirebaseDatabase.getInstance();
        minus = findViewById(R.id.minusED);
        plus = findViewById(R.id.plusED);
        numberDis = findViewById(R.id.numberED);
        cancelBtn= findViewById(R.id.backED);
        itemNameDis = findViewById(R.id.itemNameED);
        descriptionDis = findViewById(R.id.descriptionED);
        priceDis = findViewById(R.id.priceED);
        deleteMY = findViewById(R.id.deleteMY);
        saveED =findViewById(R.id.saveED);
        sessionManagement = new sessionManagement(getApplicationContext());
        //loaddd
        loadingDialog = new LoadingDialog( MyOrder_edit_Ricebowl.this);


        String orderID= sessionManagement.getOrderID();
        String itemHiddID = getIntent().getStringExtra("MYhiddenId_key");
        String realName = getIntent().getStringExtra("realName");
        SetSaSimula2(itemHiddID, orderID);
        imageView3.setImageResource(imageNaCLASS.passthepic(realName));
        addSiomaiDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(addSiomai.getText().toString());
                if(number<=0){
                    addSiomai.setText("0");
                }else {
                    number--;
                    String numberConv = Integer.toString(number);
                    addSiomai.setText(numberConv);
                }
            }
        });
        addSiomaiUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(addSiomai.getText().toString()); number++; String numberConv = Integer.toString(number);
                addSiomai.setText(numberConv);
            }
        });
        addShanghaiDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(addShanghai.getText().toString());
                if(number<=0){
                    addShanghai.setText("0");
                }else {
                    number--; String numberConv = Integer.toString(number);
                    addShanghai.setText(numberConv);
                }
            }
        });
        addShanghaiUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(addShanghai.getText().toString()); number++; String numberConv = Integer.toString(number);
                addShanghai.setText(numberConv);
            }
        });
        addSharkDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(addShark.getText().toString());
                if(number<=0){
                    addShark.setText("0");
                }else {
                    number--; String numberConv = Integer.toString(number);
                    addShark.setText(numberConv);
                }
            }
        });
        addSharkUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(addShark.getText().toString()); number++; String numberConv = Integer.toString(number);
                addShark.setText(numberConv);
            }
        });

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
                        saveChanges(itemHiddID, orderID);
                        loadingDialog.dismissDialog();
                    }
                }, 1000);
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
                        //Toast.makeText(getApplicationContext(), "Dat1 ", Toast.LENGTH_LONG).show();
                        loadingDialog.startLoadingDialog();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                deleteThis(itemHiddID, orderID);

                            }
                        }, 400);

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
    //=======================================

    public void SetSaSimula2(String itemHidID, String orderID){

        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String itemName = snapshot.child("item").getValue().toString();
                String price = snapshot.child("price").getValue().toString();
                String descrip= snapshot.child("description").getValue().toString();
                String qty = snapshot.child("qty").getValue().toString();
                String addOnId2= snapshot.child("adOns_identifier").getValue().toString();
                String categ = snapshot.child("category").getValue().toString();
                textViewCat.setText(categ);
                itemNameDis.setText(itemName);
                descriptionDis.setText(descrip);
                priceDis.setText(price);
                numberDis.setText(qty);
                if (addOnId2.equals("true")){
                    kapagMayAddOn(itemHidID, orderID);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error getting data", Toast.LENGTH_LONG).show();
            }
        });
    }
    //Set sa simula kapag may addOns
    public void kapagMayAddOn(String itemHidID, String orderID){
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    String identifier = snapshot1.child("adOns_identifier").getValue().toString();
                    if (identifier.equals(itemHidID)){
                        String zname = snapshot1.getKey();
                        String qty = snapshot1.child("qty").getValue().toString();

                        if (zname.equals(itemHidID+"AddTapa")){
                            addOntapaCheck.setChecked(true);
                        }else if (zname.equals(itemHidID+"AddSharks")){
                            addShark.setText(qty);
                        }else if (zname.equals(itemHidID+"AddShanghai")){
                            addShanghai.setText(qty);
                        }else if (zname.equals(itemHidID+"AddSiomai")){
                            addSiomai.setText(qty);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //Save changes
    public void saveChanges(String itemHidID, String orderID){
        String quanty = numberDis.getText().toString();
        HashMap hashQTY = new HashMap();
        hashQTY.clear();
        hashQTY.put("qty",quanty);

        //CHECK IF MAY ADD ONS
        String zero= "0";
        String shark =  addShark.getText().toString();
        String shang =  addShanghai.getText().toString();
        String siomai =  addSiomai.getText().toString();
        if (addOntapaCheck.isChecked() || !shark.equals(zero)|| !shang.equals(zero)|| !siomai.equals(zero)) {
            hashQTY.put("adOns_identifier",true);
            checkKungMayAddOn(orderID, itemHidID, shark, shang,siomai,quanty);
        }
        else{
            hashQTY.put("adOns_identifier",false);
            reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
            reference.child(orderID).child("order").child(itemHidID+"AddTapa").removeValue();
            reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
            reference.child(orderID).child("order").child(itemHidID+"AddSharks").removeValue();
            reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
            reference.child(orderID).child("order").child(itemHidID+"AddShanghai").removeValue();
            reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
            reference.child(orderID).child("order").child(itemHidID+"AddSiomai").removeValue();
        }

        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID).updateChildren(hashQTY).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                backNormal();
            }
        });

    }
    //kapag may addOns
    //check kung may add ons
    public void checkKungMayAddOn(String tempOrderID, String itemId,String shark,String shang,String siomai, String qtyParent){
        String zero= "0";
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(tempOrderID).child("order").child(itemId+"AddTapa").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(tempOrderID).child("order").child(itemId+"AddSharks").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(tempOrderID).child("order").child(itemId+"AddShanghai").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(tempOrderID).child("order").child(itemId+"AddSiomai").removeValue();

        if (addOntapaCheck.isChecked()) {
            addOns.add("AddTapa");
            QtyaddOns.add("1");
        }
        if (!shark.equals(zero)) {
            addOns.add("AddSharks");
           String ilan= addShark.getText().toString();
            QtyaddOns.add(ilan);
        }
        if (!shang.equals(zero)) {
            addOns.add("AddShanghai");
            String ilan= addShanghai.getText().toString();
            QtyaddOns.add(ilan);
        }
        if (!siomai.equals(zero)) {
            addOns.add("AddSiomai");
            String ilan= addSiomai.getText().toString();
            QtyaddOns.add(ilan);
        }
       // addOnQty = "1";
        int size = addOns.size();
        size--;
        for (int n=0; n<=size; n++){
            String IdngAdd = addOns.get(n);
            String itQTy= QtyaddOns.get(n);
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
                    addOnHash.put("qty", itQTy);
                    addOnHash.put("adOns_identifier", itemId);
                   // addOnHash.put("qtyParent", qtyParent);
                    reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
                    reference.child(tempOrderID).child("order").child(itemId+IdngAdd).updateChildren(addOnHash);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    public void deleteThis(String itemHidID, String orderID){
        //Toast.makeText(getApplicationContext(), "Dat2 ", Toast.LENGTH_LONG).show();
       // Toast.makeText(getApplicationContext(), "Dat3 ", Toast.LENGTH_LONG).show();
        loadingDialog.dismissDialog();
        backNormal();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID+"AddTapa").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID+"AddSharks").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID+"AddShanghai").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID+"AddSiomai").removeValue();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(orderID).child("order").child(itemHidID).removeValue();

    }
    public void backNormal(){
        Intent intent = new Intent(this, MyOrder_Page.class);
      // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       startActivity(intent);
        finish();
    }
}