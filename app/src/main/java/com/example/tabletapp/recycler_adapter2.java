package com.example.tabletapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class recycler_adapter2 extends RecyclerView.Adapter<recycler_adapter2.ViewHolder> {
    private RecyclerClick listener;
    List<String> titles;
    List<Integer> images;
    List<String> hiddenId;
    List<String> prices;
    List<String> quantity;
    List<String> description;
    List<String> category;
    List<String> itemKey; List<Integer> PriceTotal;
    List<String> addOndText, addOnIdentifier;
    String orderTempId;
    Context context;
    LayoutInflater inflater;
    sessionManagement sessionManagement;
    DatabaseReference reference;


    // kapg gusto mag add new Array88888
    public recycler_adapter2(Context ctx, String orderTempId, List<String> titles, List<Integer> images, List<String> hiddenId, List<String> prices, List<String> quantity,
                             List<String> description, List<String> category, List<String> itemKey, List<String>addOndText, List<String> addOnIdentifier, RecyclerClick listener){
        sessionManagement = new sessionManagement(ctx);
        this.orderTempId = orderTempId;
        this.titles = titles;
        this.images=images;
        this.hiddenId = hiddenId;
        this.prices = prices;
        this.quantity = quantity;// kapg gusto mag add new Array 88888
        this.description  = description;
        this.inflater = LayoutInflater.from(ctx);
        this.listener = listener;
        this.category = category;
        this.itemKey = itemKey;
        this.addOndText = addOndText;
        this.addOnIdentifier = addOnIdentifier;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customlayout2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.hiddenId.setText(hiddenId.get(position));
        holder.gridIcon.setImageResource(images.get(position));
        holder.quantity.setText(quantity.get(position)+" x");// 88888 kapg gusto mag add new Array 88888
        holder.descriptionTV.setText(description.get(position));
        holder.categoryText.setText(category.get(position));


        String addon = addOnIdentifier.get(position);
        if (addon.equals("true")){
            holder.AddOnsVisi.setVisibility(View.VISIBLE);
           //holder.addOnsText.setText(addOndText.get(position));

            reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
            reference.child(orderTempId).child("order").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String textLang= "";
                    int totalcount=0;

                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        String identifier = snapshot1.child("adOns_identifier").getValue().toString();


                        if (identifier.equals(itemKey.get(position))){
                            String item = snapshot1.child("item_name").getValue().toString();
                            String qty = snapshot1.child("qty").getValue().toString();
                            String price = snapshot1.child("price").getValue().toString();

                            textLang = textLang + qty+"x   "+item +"   ₱"+price+"\n";
                            //------------
                            int qty2 = Integer.valueOf(qty);
                            int price2 =Integer.valueOf(price);
                            totalcount = totalcount + (price2 * qty2);


                        }
                    }
                    int pricewithAdd = totalcount + Integer.valueOf(prices.get(position));
                    int trueprice = pricewithAdd * Integer.valueOf(quantity.get(position));
                    holder.addOnsText.setText(textLang);
                    holder.getPrice.setText("₱ "+trueprice);
                   // PriceTotal.add(trueprice);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else {
            holder.AddOnsVisi.setVisibility(View.INVISIBLE);
            holder.addOnsText.setVisibility(View.INVISIBLE);
            int trueprice = Integer.valueOf(prices.get(position)) * Integer.valueOf(quantity.get(position));
            //PriceTotal.add(trueprice);
            holder.getPrice.setText("₱" + trueprice);
        }
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{//======For the click 3:10
        public TextView title, getPrice, hiddenId, quantity, descriptionTV, categoryText,AddOnsVisi,addOnsText; // kapg gusto mag add new Array
        public ImageView gridIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_nameF);
            gridIcon = itemView.findViewById(R.id.imageViewF);
            getPrice = itemView.findViewById(R.id.priceF);
            hiddenId = itemView.findViewById(R.id.hidden_idF);
            quantity = itemView.findViewById(R.id.quantityMY);// kapg gusto mag add new Array 88888
            descriptionTV = itemView.findViewById(R.id.descriptionF);
            categoryText = itemView.findViewById(R.id.categoryText);
            addOnsText= itemView.findViewById(R.id.addOnsText);
            AddOnsVisi = itemView.findViewById(R.id.AddOnsVisi);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //======For the click
            listener.onClick(itemView, getAdapterPosition());
        }
    }
    //======For the click
    public interface RecyclerClick{
        void onClick(View v, int position);
    }

}