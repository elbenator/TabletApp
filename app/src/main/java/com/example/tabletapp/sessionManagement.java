package com.example.tabletapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class sessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    List<String> blah;


    public sessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences("session", 0);
        editor =sharedPreferences.edit();
        editor.apply();
        blah = new ArrayList<>();
    }


    //SET Ordering
    public void setOrdering(boolean login){
        editor.putBoolean("key_ifOrdering", login);
        editor.commit();
    }
    //GET Ordering
    public boolean getOrdering(){
        return sharedPreferences.getBoolean("key_ifOrdering",false);
    }
    //SET orderID
    public void setOrderID(String username){
        editor.putString("key_username", username);
        editor.commit();
    }
    //GET orderID
    public String getOrderID(){
        return sharedPreferences.getString("key_username","");
    }

    //SET Summary
    public void setSummary(String summary){
        editor.putString("key_summary", summary);
        editor.commit();
    }
    //GET orderID
    public String getSummary(){
        return sharedPreferences.getString("key_summary","");
    }

    //SET total
    public void setTotal(String total){
        editor.putString("key_total", total);
        editor.commit();
    }
    //GET total
    public String getTotal(){
        return sharedPreferences.getString("key_total","");
    }

    //SET random
    public void setRandomnumberr(String randomnumberr){
        editor.putString("key_randomnumber", randomnumberr);
        editor.commit();
    }
    //GET random
    public String getRandomnumberr(){
        return sharedPreferences.getString("key_randomnumber", "1");
    }

    //SET Shit
    public void setShit(String shhit){
        editor.putString("key_shit", shhit);
        editor.commit();
    }
    //GET shit
    public String getShit(){
        return sharedPreferences.getString("key_shit", "1");
    }

    //SET Shit2
    public void setShit2(String shhit){
        editor.putString("key_shit2", shhit);
        editor.commit();
    }
    //GET shit2
    public String getShit2(){
        return sharedPreferences.getString("key_shit2", "1");
    }

    //SET changes total
    public void setTotalChange(boolean login){
        editor.putBoolean("key_ifchange", login);
        editor.commit();
    }
    //GET changes total
    public boolean getTotalChange(){
        return sharedPreferences.getBoolean("key_ifchange",false);
    }

    //SET totalChange
    public void setTotalString(String shhit){
        editor.putString("key_shit3", shhit);
        editor.commit();
    }
    //GET totalChange
    public String getTotalString(){
        return sharedPreferences.getString("key_shit3", "0");
    }
}
