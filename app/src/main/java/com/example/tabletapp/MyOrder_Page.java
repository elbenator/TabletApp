package com.example.tabletapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tabletapp.item_selected_folder.MyOrder_edit;
import com.example.tabletapp.item_selected_folder.MyOrder_edit_Ricebowl;
import com.example.tabletapp.item_selected_folder.MyOrder_edit_milktea;
import com.example.tabletapp.item_selected_folder.MyOrder_edit_noodles;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOrder_Page extends AppCompatActivity {
    DatabaseReference reference;
    Button checkOutBtnF, backBtnF;
    TextView totalF;
    RecyclerView dataList2;
    List<String> titles;
    List<Integer> images;
    List<String> hiddenId;
    List<String> prices;
    List<String> quantity; // kapg gusto mag add new Array
    List<String> description;
    List<String> category;
    List<String> PriceTotal;
    List<String> QtyTotal;
    List<String> SummaryWithAddon;
    List<String> SummarySpaces;
    List<String> itemKey, QtyParent;
    List<String> addOndText, addOnIdentifier;
    recycler_adapter2 adapter;
    MyOrder_Page_adapter2 adapter_pangalawa;
    imagesClass imageNaCLASS ;
    LoadingDialog loadingDialog;

    sessionManagement sessionManagement;
    private recycler_adapter2.RecyclerClick listener; // Para sa Clickable recycler view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order__page);

        /// FULL SCREEN NO NAVIGATION AND SCROLL NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        //loaddd
         loadingDialog = new LoadingDialog( MyOrder_Page.this);
        loadingDialog.startLoadingDialog();

        sessionManagement = new sessionManagement(getApplicationContext());
        dataList2 =findViewById(R.id.dataList2);

        String username = "tablet01";
        checkOutBtnF = findViewById(R.id.checkOutBtnF);
        backBtnF = findViewById(R.id.backBtnF);
        totalF = findViewById(R.id.totalF);
        titles = new ArrayList<>(); hiddenId = new ArrayList<>(); quantity = new ArrayList<>();
        images = new ArrayList<>(); prices = new ArrayList<>(); description = new ArrayList<>();
        category = new ArrayList<>();
        addOnIdentifier = new ArrayList<>(); addOndText = new ArrayList<>();

        imageNaCLASS = new imagesClass();
        PriceTotal = new ArrayList<>();
        QtyTotal = new ArrayList<>();
        itemKey = new ArrayList<>(); QtyParent = new ArrayList<>();
        SummaryWithAddon = new ArrayList<>(); SummarySpaces = new ArrayList<>();
        setOnClickListener(username); //kailangan sa taas to

        backBtnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               back();
            }
        });

        checkOutBtnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
              builder.setTitle("Checking Out");
              builder.setMessage("Are you sure you want to checkout?");
              builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      loadingDialog.startLoadingDialog();
                      Handler handler = new Handler();
                      handler.postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              String orderTempID = sessionManagement.getOrderID();
                              reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
                              reference.child(orderTempID).child("order").addListenerForSingleValueEvent(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                                      if (snapshot.exists()){
                                          checkOut();
                                      }else {
                                          Toast.makeText(MyOrder_Page.this, "Order is Empty ", Toast.LENGTH_SHORT).show();
                                      }
                                  }
                                  @Override
                                  public void onCancelled(@NonNull DatabaseError error) {
                                  }
                              });
                              loadingDialog.dismissDialog();
                          }
                      }, 500);

                  }
              });
               builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                   }
               });
               AlertDialog alertDialog = builder.create();alertDialog.show();
            }
        });

        //SET Sa simula
        String orderTempID = sessionManagement.getOrderID();
        reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
        ValueEventListener lovethis = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 0;
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    String additionalString = "", additionalString2 = "";
                    String identifier = snapshot1.child("adOns_identifier").getValue().toString();
                    if (identifier.equals("true")||identifier.equals("false")){
                        counter++;
                        String itemName = snapshot1.child("item").getValue().toString();
                        String qty = snapshot1.child("qty").getValue().toString();
                        String itemIdKey= snapshot1.getKey();
                        String itemId = snapshot1.child("itemId").getValue().toString();
                        String priceK = snapshot1.child("price").getValue().toString();
                        String descK= snapshot1.child("description").getValue().toString();
                        String categ = snapshot1.child("category").getValue().toString();

                        if (snapshot1.child("sugarLvl").exists()){
                            String sugarLvl = snapshot1.child("sugarLvl").getValue().toString();
                            String price22 = snapshot1.child("price22").getValue().toString();
                            String ozz = "  16oz Small";
                            if (priceK.equals(price22)){
                                ozz = "  24oz Large";
                            }

                            additionalString = "SgrLvl: "+sugarLvl+"\n";
                            additionalString2="\nSgr"+sugarLvl+ozz;
                        }
                        titles.add(itemName + "        Php"+priceK);
                        images.add(imageNaCLASS.passthepic(itemId));
                        quantity.add(qty); // kapg gusto mag add new Array
                        hiddenId.add(itemId);
                        description.add(additionalString + descK);
                        prices.add(priceK);
                        category.add(categ);
                        itemKey.add(itemIdKey);

                        if (identifier.equals("true")){
                            addOnIdentifier.add("true");
                            addOndText.add(" ");

                        }else {
                            addOndText.add(" ");
                            addOnIdentifier.add("false");
                        }

                        SummarySpaces.add("\n"+qty+"x "+categ+":");
                        SummaryWithAddon.add(itemName + additionalString2);

                    }
                    else {
                        String itemName = snapshot1.child("item_name").getValue().toString();
                        String qty = snapshot1.child("qty").getValue().toString();
                        // PriceTotal.add("0");
                        SummarySpaces.add("  -- "+qty);
                        SummaryWithAddon.add(itemName );
                    }
                }
                adapter.notifyDataSetChanged();
                loadingDialog.dismissDialog();
                adapter_pangalawa=new MyOrder_Page_adapter2(orderTempID, prices, quantity, itemKey, addOnIdentifier);
                int times = counter * 75;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int happier = adapter_pangalawa.returnTalaga();
                        String blah1 = "" + happier;
                        totalF.setText(blah1);
                    }
                }, times);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        reference.child(orderTempID).child("order").addListenerForSingleValueEvent(lovethis);
        adapter = new recycler_adapter2(getApplicationContext(),orderTempID, titles, images, hiddenId, prices, quantity, description, category, itemKey,
                addOndText,addOnIdentifier, listener); // kapg gusto mag add new Array
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1, GridLayoutManager.VERTICAL, false);
        dataList2.setLayoutManager(gridLayoutManager);
        dataList2.setAdapter(adapter);
        reference.removeEventListener(lovethis);


}
    /**=========== functions =================*/
    private void setOnClickListener(String username) {
        // Para sa Clickable recycler view 4:51
        listener = new recycler_adapter2.RecyclerClick(){
            @Override
            public void onClick(View v, int position) {
                String compare= category.get(position);
               //alacarte, friednoodles, cheesecakemt, classicmilktea, ricebowl

                String friednoodles = "friednoodles";
                String cheesecakmt = "cheesecakemt";
                String classicmilktea = "classicmilktea";
                String ricebowl ="ricebowl";

                if (compare.equals(cheesecakmt)|| compare.equals(classicmilktea)){
                    Intent intent = new Intent(getApplicationContext(), MyOrder_edit_milktea.class);
                    String id= itemKey.get(position);
                    String real = hiddenId.get(position);
                    intent.putExtra("MYhiddenId_key", id);
                    intent.putExtra("realName", real);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else if(compare.equals(friednoodles)){
                    Intent intent = new Intent(getApplicationContext(), MyOrder_edit_noodles.class);
                    String id= itemKey.get(position);
                    String real = hiddenId.get(position);
                    intent.putExtra("MYhiddenId_key", id);
                    intent.putExtra("realName", real);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else if (compare.equals(ricebowl)){
                    Intent intent = new Intent(getApplicationContext(), MyOrder_edit_Ricebowl.class);
                    String id= itemKey.get(position);
                    String real = hiddenId.get(position);
                    intent.putExtra("MYhiddenId_key", id);
                    intent.putExtra("realName", real);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), MyOrder_edit.class);
                    String id= itemKey.get(position);
                    String real = hiddenId.get(position);
                    intent.putExtra("MYhiddenId_key", id);
                    intent.putExtra("realName", real);
                   // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    // >>>>>>>>> My order page 2 Checkout ===============
    public void checkOut(){
        sessionManagement.setSummary("");
        sessionManagement.setSummary(returnString());
        String blah1= totalF.getText().toString();
        sessionManagement.setTotal(blah1);
       // Random random = new Random();
      //  int val = random.nextInt(5000);
       // sessionManagement.setRandomnumberr("Order Number: " + Integer.toString(val));
        Intent intent2 = new Intent(MyOrder_Page.this, MyOrder_page2.class);
        startActivity(intent2);
        finish();
    }
    //summarry
    public String returnString(){
        int blah = SummaryWithAddon.size();
        //blah--;
        String summaryNg = "";
        for (int i = 0; i < blah; i++) {
            String item = SummaryWithAddon.get(i);
            String spaces = SummarySpaces.get(i);
            summaryNg = summaryNg + spaces+" "+item + "\n";
        }
        return summaryNg;
    }


    public void back(){
       Intent intent2 = new Intent(this, MenupageRicebowl.class);
       startActivity(intent2);
        finish();

    }
}