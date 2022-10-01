package com.example.tabletapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletapp.item_selected_folder.item_selected_ad_milktea;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenupageMilktea extends AppCompatActivity{
    Button MyOrderBtn, clearButt;
    CardView ricebowl,chmilktea,alacarte,fd;
    RecyclerView dataList;
    List<String> titles;
    List<Integer> images;
    List<String> hiddenId;
    List<String> prices;
    ArrayList<String> orderList;
    recycler_adapter adapter;
    TextView totalMenu; MyOrder_Page_adapter2 adapter_pangalawa;
    List<String> quantityFor, itemKey, addOnIdentifier, pricesFor;
    DatabaseReference reference1, referenceze, reference;
    FirebaseDatabase db;
    imagesClass imageNaCLASS ;
    sessionManagement sessionManagement;
   // imagesClass imageNaCLASS ;
    item_selected_ad_milktea item_selected_ad_milktea;
    private recycler_adapter.RecyclerClick listener; // Para sa Clickable recycler view
    String zComp= "classicmilktea"; // ETo yung category !!!!!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page_milktea);

        /// FULL SCREEN NO NAVIGATION AND SCROLL NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        LoadingDialog loadingDialog = new LoadingDialog( MenupageMilktea.this);

        sessionManagement = new sessionManagement(getApplicationContext());
        dataList =findViewById(R.id.dataList);
        titles = new ArrayList<>();
        images = new ArrayList<>();
        hiddenId = new ArrayList<>();
        prices = new ArrayList<>();
        orderList = new ArrayList<>();
        clearButt = findViewById(R.id.backBtn1);
        MyOrderBtn = findViewById(R.id.MyOrderBtn);
        ricebowl = findViewById(R.id.materialCardView2);
        fd = findViewById(R.id.materialCardView3);
        chmilktea = findViewById(R.id.chcake);
        alacarte = findViewById(R.id.carte);
        totalMenu = findViewById(R.id.totalMenu); addOnIdentifier = new ArrayList<>();
        pricesFor = new ArrayList<>(); quantityFor= new ArrayList<>(); itemKey = new ArrayList<>();
        imageNaCLASS = new imagesClass();
        setOnClickListener(); //kailangan sa taas to
      //  imageNaCLASS = new imagesClass();
        MyOrderBtn.setVisibility(View.INVISIBLE);
        clearButt.setVisibility(View.INVISIBLE);
       fd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenupageNoodles.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);finish();
            }
        });

       alacarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuPageAlacarte.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);finish();
            }
        });
        chmilktea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuPageCheesemilktea.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);finish();
            }
        });


        ricebowl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenupageRicebowl.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);finish();
            }
        });

        clearButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear MyOrders
                sessionManagement.setTotalString("Php 0.00");
                sessionManagement.setTotalChange(false);
                String idorder= sessionManagement.getOrderID();
                reference1 = FirebaseDatabase.getInstance().getReference("preset_tablet");
                reference1.child(idorder).child("order").setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });

            }});

        MyOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                checkKungMerongOrdering();
                loadingDialog.dismissDialog();
            }});
        //Buttons

        db = FirebaseDatabase.getInstance();
        reference1 = db.getReference("item");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    String zcat = snapshot1.child("category").getValue().toString();
                    String show = snapshot1.child("display").getValue().toString();
                    if (zcat.equals(zComp) && show.equals("show")){
                        String zitemId= snapshot1.child("item_id").getValue().toString();
                        String zname = snapshot1.child("item_name").getValue().toString();
                        String zprice = snapshot1.child("price16").getValue().toString();

                        images.add(imageNaCLASS.passthepic(zitemId));
                        titles.add(zname);
                        prices.add("₱ "+zprice);
                        hiddenId.add(zitemId);
                    }
                }
                adapter.notifyDataSetChanged();
                boolean somethingchange = sessionManagement.getTotalChange();
                if(somethingchange){
                    setTotal();
                    sessionManagement.setTotalChange(false);
                    MyOrderBtn.setVisibility(View.VISIBLE);
                    clearButt.setVisibility(View.VISIBLE);
                }else{
                    totalMenu.setText(sessionManagement.getTotalString());
                    MyOrderBtn.setVisibility(View.VISIBLE);
                    clearButt.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MenupageMilktea.this, "Database Error", Toast.LENGTH_LONG).show();
            }});

        adapter = new recycler_adapter(this,titles, images, hiddenId, prices, listener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL, false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);
    }
    /**==================== FUNCTIONS =======================**/
    private void setOnClickListener() {
        // Para sa Clickable recycler view 4:51
        listener = new recycler_adapter.RecyclerClick(){
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), item_selected_ad_milktea.class);
               intent.putExtra("hiddenId_key",hiddenId.get(position));
                startActivity(intent);finish();
            }
        };
    }
    private void checkKungMerongOrdering(){
        if(sessionManagement.getOrdering()){
            reference1 = db.getReference("preset_tablet");
            reference1.child(sessionManagement.getOrderID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String spit = totalMenu.getText().toString();
                    if (snapshot.exists()&&!spit.equals("Php 0.00")){
                        Intent intent = new Intent(MenupageMilktea.this,MyOrder_Page.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), "My Orders is Empty", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "My Orders is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void setTotal(){
        //Toast.makeText(getApplicationContext(), "Dat--"+sessionManagement.getOrdering(), Toast.LENGTH_LONG).show();
        if(sessionManagement.getOrdering()){
            referenceze = FirebaseDatabase.getInstance().getReference("preset_tablet");
            referenceze.child(sessionManagement.getOrderID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot3) {
                    if (snapshot3.exists()){

                        String orderTempID = sessionManagement.getOrderID();
                        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
                        reference.child(orderTempID).child("order").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int counter = 0;

                                for (DataSnapshot snapshot1: snapshot.getChildren()){
                                    counter++;
                                    String identifier = snapshot1.child("adOns_identifier").getValue().toString();
                                    if (identifier.equals("true")||identifier.equals("false")){
                                        String qty = snapshot1.child("qty").getValue().toString();
                                        String itemIdKey= snapshot1.getKey();
                                        String itemId = snapshot1.child("itemId").getValue().toString();
                                        String priceK = snapshot1.child("price").getValue().toString();


                                        quantityFor.add(qty);
                                        hiddenId.add(itemId);
                                        pricesFor.add(priceK);
                                        itemKey.add(itemIdKey);

                                        if (identifier.equals("true")){
                                            addOnIdentifier.add("true");

                                        }else {
                                            addOnIdentifier.add("false");
                                        }

                                    }
                                }
                                adapter_pangalawa=new MyOrder_Page_adapter2(orderTempID, pricesFor, quantityFor, itemKey, addOnIdentifier);
                                int times = counter * 40;
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        int happier = adapter_pangalawa.returnTalaga();
                                        totalMenu.setText("Php "+happier+".00");
                                        sessionManagement.setTotalString("Php "+happier+".00");
                                    }
                                }, times);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    }

}
