package com.example.tabletapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOrder_Page_adapter2 {
    private recycler_adapter2.RecyclerClick listener;
    List<String> prices;
    List<String> quantity;
    List<String> itemKey, addOnIdentifier;
    List<Integer> PriceTotal;

    DatabaseReference reference;
    public MyOrder_Page_adapter2( String orderTempId, List<String> prices, List<String> quantity,
                             List<String> itemKey, List<String> addOnIdentifier){

        this.prices = prices;
        this.quantity = quantity;// kapg gusto mag add new Array 88888
        this.itemKey = itemKey;
        this.addOnIdentifier = addOnIdentifier;


        PriceTotal = new ArrayList<>();
        // totalF = new TextView(context);
        int addon = addOnIdentifier.size();
        for (int i = 0; i < addon; i++) {
            String genz = addOnIdentifier.get(i);
            String item = itemKey.get(i);
            int price = Integer.valueOf(prices.get(i));
            int qty = Integer.valueOf(quantity.get(i));

            if (genz.equals("true")){
                reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
                reference.child(orderTempId).child("order").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String textLang= "";
                        int totalcount=0;


                        for (DataSnapshot snapshot1: snapshot.getChildren()){
                            String identifier = snapshot1.child("adOns_identifier").getValue().toString();
                            if (identifier.equals(item)){
                                String item = snapshot1.child("item_name").getValue().toString();
                                String qty = snapshot1.child("qty").getValue().toString();
                                String price = snapshot1.child("price").getValue().toString();

                                textLang = textLang + qty+"x   "+item +"   ₱"+price+"\n";
                                //------------
                                int qty2 = Integer.valueOf(qty);
                                int price2 =Integer.valueOf(price);
                                totalcount = totalcount + (price2 *qty2);
                            }
                        }
                        int pricewithAdd = totalcount + price;
                        int trueprice = pricewithAdd * qty ;

                        PriceTotal.add(trueprice);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }else {
                int trueprice = price * qty ;
                PriceTotal.add(trueprice);
            }

        }
    }

    public Integer returnTalaga(){

        int blah = PriceTotal.size();
        int total= 0;
        for (int i = 0; i < blah; i++) {
            //int pricez = Integer.valueOf();
            total = total + PriceTotal.get(i);
        }

        return total;
    }
}
