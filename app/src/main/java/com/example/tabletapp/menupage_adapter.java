package com.example.tabletapp;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class menupage_adapter {

    Context ctx;
    DatabaseReference reference, reference1;
    sessionManagement sessionManagement;

    public menupage_adapter(Context ctx) {
        this.ctx = ctx;
    }

    public void blah(Context ctx, int indicator){
        if(indicator==1){
            Intent intent = new Intent(ctx, MenupageMilktea.class);
            ctx.startActivity(intent);
            //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }
        else if(indicator == 2){
            Intent intent = new Intent(ctx, MenupageNoodles.class);
            ctx.startActivity(intent);
        }
        else if(indicator==3){
            Intent intent = new Intent(ctx, MenupageRicebowl.class);
            ctx.startActivity(intent);
        }
    }
    public void backClear(Context ctx){
        String idorder= sessionManagement.getOrderID();
        reference1 = FirebaseDatabase.getInstance().getReference("preset_order");
        reference1.child(idorder).child("order").setValue(null);
        //sessionManagement.setOrderID("");
        //sessionManagement.setOrdering(false);
        Intent intent = new Intent(ctx,MainActivity.class);
        ctx.startActivity(intent);

    }

    public void myOrderPage1(Context ctx){
        //KAPAG MAy laman na yung Orders
        if(sessionManagement.getOrdering()){
            Intent intent = new Intent(ctx,MyOrder_Page.class);
            ctx.startActivity(intent);

        } else {
            Toast.makeText(ctx, "My Orders is Empty", Toast.LENGTH_SHORT).show();
        }
    }
}
