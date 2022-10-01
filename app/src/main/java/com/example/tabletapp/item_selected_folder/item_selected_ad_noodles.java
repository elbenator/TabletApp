package com.example.tabletapp.item_selected_folder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletapp.MenupageNoodles;
import com.example.tabletapp.MenupageRicebowl;
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

public class item_selected_ad_noodles extends AppCompatActivity {
    //FOR MILKTEA
    Button minus,plus,cancelBtn,AddToOrder;
    TextView itemNameDis,numberDis,descriptionDis, priceDis,textViewCat;
    DatabaseReference reference;
    FirebaseDatabase db;
    public String addOnId, addOnQty, addOnPrice, addOnName;
    com.example.tabletapp.sessionManagement sessionManagement;
    com.example.tabletapp.item_selected_adapter item_selected_adapter;
    //for add ons
    CheckBox addOntapaCheck, addNoodCheck;
    Button addSiomaiDown, addSiomaiUp,addShanghaiDown, addShanghaiUp, addSharkDown, addSharkUp;
    TextView addShark, addSiomai, addShanghai;
    List<String> addOns;
    List<String> QtyaddOns;
    imagesClass imageNaCLASS ;
    ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_selected_ad_noodles);

        /// FULL SCREEN NO NAVIGATION AND SCROLL NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        addOntapaCheck = findViewById(R.id.addTapaCheck);
        addNoodCheck = findViewById(R.id.addNoodCheck);
        addSiomaiDown = findViewById(R.id.addSiomaiDown); addSiomaiUp = findViewById(R.id.addSiomaiUp); addSiomai = findViewById(R.id.addSiomai);
        addShanghaiDown = findViewById(R.id.addShanghaiDown); addShanghaiUp = findViewById(R.id.addShanghaiUp); addShanghai =findViewById(R.id.addShanghai);
        addSharkDown = findViewById(R.id.addSharkDown); addSharkUp = findViewById(R.id.addSharkUp); addShark = findViewById(R.id.addShark);
        db = FirebaseDatabase.getInstance();
        minus = findViewById(R.id.minusED); plus = findViewById(R.id.plusED);
        numberDis = findViewById(R.id.numberED);
        cancelBtn= findViewById(R.id.backED);
        AddToOrder = findViewById(R.id.saveED);
        itemNameDis = findViewById(R.id.itemNameED);
        descriptionDis = findViewById(R.id.descriptionED);
        priceDis = findViewById(R.id.priceED);
        imageNaCLASS = new imagesClass();
        imageView2 = findViewById(R.id.imageView2);
        textViewCat = findViewById(R.id.textViewCat);
        sessionManagement = new sessionManagement(getApplicationContext());
        String username = "tablet01";
        String itemHidID = getIntent().getStringExtra("hiddenId_key"); //Ganito talaga to wag delete
        addOns = new ArrayList<>();
        QtyaddOns = new ArrayList<>();

        imageView2.setImageResource(imageNaCLASS.passthepic(itemHidID));
        SetSaSimula(itemHidID);

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
                if(number<=1){numberDis.setText("1"); }
                else { number--;String numberConv = Integer.toString(number); numberDis.setText(numberConv); }
            }});

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number= Integer.parseInt(numberDis.getText().toString());number++; String numberConv = Integer.toString(number);
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
                Toast.makeText(getApplicationContext(), "Error getting data", Toast.LENGTH_LONG).show();
            }
        });

    }

    // STEP 1 =========
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

    //======= STEP 2 WAla pang record
    public void createNewOrder(String username, String itemId){
        sessionManagement.setOrdering(true);
        //Toast.makeText(getApplicationContext(), "Adding order...", Toast.LENGTH_SHORT).show();
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
        addOrdNeW.put("category",categoryK);
        //CHECK IF MAY ADD ONS +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        String zero= "0";
        String shark =  addShark.getText().toString();
        String shang =  addShanghai.getText().toString();
        String siomai =  addSiomai.getText().toString();

        if (addOntapaCheck.isChecked() || addNoodCheck.isChecked()|| !shark.equals(zero)|| !shang.equals(zero)|| !siomai.equals(zero)) {
            addOrdNeW.put("adOns_identifier",true);
            String nameDapat = itemId;
            checkKungMayAddOn(orderID, itemId, shark, shang, siomai, nameDapat,numQty2);
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

        //CHECK IF MAY ADD ONS +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        String zero= "0";
        String shark =  addShark.getText().toString();
        String shang =  addShanghai.getText().toString();
        String siomai =  addSiomai.getText().toString();

        if (addOntapaCheck.isChecked() || addNoodCheck.isChecked()|| !shark.equals(zero)|| !shang.equals(zero)|| !siomai.equals(zero)) {
            addOrd.put("adOns_identifier",true);
            checkKungMayAddOn(tempOrderID,  itemId,shark, shang, siomai, nameDapat, numQty2);
        }
        else{
            addOrd.put("adOns_identifier",false);
        }


        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        reference.child(tempOrderID).child("order").child(nameDapat).setValue(addOrd).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        //Toast.makeText(getApplicationContext(), "umabot dito2", Toast.LENGTH_SHORT).show();
    }

    //check
    public void checkKungMayAddOn(String tempOrderID, String itemId,String shark,String shang,String siomai,String nameDapat, String qtyParent){
        String zero= "0";
        if (addOntapaCheck.isChecked()) {
            addOns.add("AddTapa");
            QtyaddOns.add("1");
        }
        if (addNoodCheck.isChecked()) {
            addOns.add("AddNoodles");
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
        //Toast.makeText(getApplicationContext(), addOns.get(0) + " ----"+addOns.get(1), Toast.LENGTH_LONG).show();
        int size = addOns.size();
        size--;
        for (int n=0; n<=size; n++){
            //Toast.makeText(getApplicationContext(), addOns.get(i) , Toast.LENGTH_SHORT).show();
            String IdngAdd = addOns.get(n);
            String ilanQty= QtyaddOns.get(n);
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
                    addOnHash.put("qty", ilanQty);
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
        Intent intent2 = new Intent(this, MenupageNoodles.class);
        //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent2);
        finish();
    }
}