package com.roamcode.distinctions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {

    private static int time=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);


        //this retrieve the sharedpreference element
        SharedPreferences myPref = this.getSharedPreferences(
                "prefName", Context.MODE_PRIVATE);
        //this retrieve the boolean "firstRun", if it doesn't exists, it places "true"
        boolean firstLaunch = myPref.getBoolean("firstLaunch", true);
        //so, if it's not the first run do stuffs
        if(!firstLaunch){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(Splash.this, MainActivityDistinctions.class);
                    startActivity(intent);
                    finish();
                }
            },time);
            //start the next activity

        }
            //else, if it's the first run, add the sharedPref
        myPref.edit().putBoolean("firstLaunch", false).commit();



    }
}