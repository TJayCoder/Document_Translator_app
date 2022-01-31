package com.roamcode.distinctions;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AboutusDistinct extends AppCompatActivity {

        TextView career,facebook,legal,developers,appver;


    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus_distinct);

        career=findViewById(R.id.btncareer);
        facebook=findViewById(R.id.btnsocialmedia);
        legal=findViewById(R.id.btnlegal);
        developers=findViewById(R.id.btndevelop);
        appver=findViewById(R.id.appversionabout);

        interstitialads();

        appver.setText("Build Version 1.0.0");


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (mInterstitialAd != null) {
                    mInterstitialAd.show(AboutusDistinct.this);

                    Uri uri = Uri.parse("https://www.facebook.com/RoamCode/?ref=py_c"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");

                    Uri uri = Uri.parse("https://www.facebook.com/RoamCode/?ref=py_c"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }


            }
        });

        legal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (mInterstitialAd != null) {
                    mInterstitialAd.show(AboutusDistinct.this);

                    Uri uri = Uri.parse("https://sites.google.com/view/documenttranslator/home"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");

                    Uri uri = Uri.parse("https://sites.google.com/view/documenttranslator/home"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }


            }
        });
        developers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                if (mInterstitialAd != null) {
                    mInterstitialAd.show(AboutusDistinct.this);

                    Uri uri = Uri.parse("https://www.linkedin.com/in/joseph-senyolo-22a233144/"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");

                    Uri uri = Uri.parse("https://www.linkedin.com/in/joseph-senyolo-22a233144/"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }



            }
        });

        career.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mInterstitialAd != null) {
                    mInterstitialAd.show(AboutusDistinct.this);

                    Uri uri = Uri.parse("https://roamcode.co.za/career.php"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");
                    Uri uri = Uri.parse("https://roamcode.co.za/career.php"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }




            }
        });

    }


    public void interstitialads(){


        AdRequest adRequest1 = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-4146389542308743/7650672694", adRequest1,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }
}