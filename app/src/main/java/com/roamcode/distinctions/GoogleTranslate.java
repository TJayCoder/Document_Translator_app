package com.roamcode.distinctions;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.FirebaseApp;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class GoogleTranslate extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AdView mAdView;
    private ProgressBar progressbar;

    Spinner spinnerfrom,spinnerTo;
    Button buttontranslate;
    TextInputEditText etSource;
    TextView tvloader;

    String fromlang[]={"From","English","French","Hebrew","Haitian","Arabic","Belarusian","Bengali","Catalan","Czech","Welsh","Danish","German","Greek","Esperanto"
            ,"Spanish","Persian","Estonian","Finnish","Galician","Gujarati","Hindi","Croatian","Hungarian","Indonesian","Icelandic","Italian","Japanese","Swedish","Portuguese"
            ,"Georgian","Kannada","Korean","Lithuanian","Latvian","Marathi","Maltese","Malay","Macedonian","Dutch","Norwegian","Chinese","Vietnamese","Urdu","Ukrainian","Turkish"
            ,"Tagalog","Thai","Russian","Polish","Romanian","Slovak","Slovenian","Albanian","Swedish","Tamil","Telugu","Afrikaans"};

    String tolang[]={"To","English","French","Hebrew","Haitian","Arabic","Belarusian","Bengali","Catalan","Czech","Welsh","Danish","German","Greek","Esperanto"
            ,"Spanish","Persian","Estonian","Finnish","Galician","Gujarati","Hindi","Croatian","Hungarian","Indonesian","Icelandic","Italian","Japanese","Swedish","Portuguese"
            ,"Georgian","Kannada","Korean","Lithuanian","Latvian","Marathi","Maltese","Malay","Macedonian","Dutch","Norwegian","Chinese","Vietnamese","Urdu","Ukrainian","Turkish"
            ,"Tagalog","Thai","Russian","Polish","Romanian","Slovak","Slovenian","Albanian","Swedish","Tamil","Telugu","Afrikaans"};

    private static final int Request_Permission_Code=1;
    int Languagecode,fromLanguageCode,tolanguageCode=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_translate);

        ads();
        interstitialads();
        progressbar =  findViewById(R.id.ProgressBar);

        spinnerfrom=findViewById(R.id.spinnerFrom);
        spinnerTo=findViewById(R.id.spinnerTo);
        buttontranslate=findViewById(R.id.btnTranslate);
        etSource=findViewById(R.id.editTextTextMultiLine);
        tvloader=findViewById(R.id.tvloader);

        Intent intent=getIntent();
        etSource.setText(intent.getStringExtra("Extractpdf"));


       // String extractword=intent.getStringExtra("Extractword");
       // etSource.setText(intent.getStringExtra("Extractword"));




        try {
            FirebaseApp.initializeApp(getApplicationContext());
        }
        catch (Exception e) {
            Toast.makeText(this, "ERROR"+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        spinnerfrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode=getLanguage(fromlang[position]);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter fromAdapter =new ArrayAdapter(this,R.layout.spinner_item,fromlang);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerfrom.setAdapter(fromAdapter);


        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tolanguageCode=getLanguage(tolang[position]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter ToAdapter =new ArrayAdapter(this,R.layout.spinner_item,tolang);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(ToAdapter);


        buttontranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvloader.setText("");
                if(etSource.getText().toString().isEmpty()){
                    Toast.makeText(GoogleTranslate.this, "Please Enter text to be translated", Toast.LENGTH_SHORT).show();
                }else if(fromLanguageCode==0){
                    Toast.makeText(GoogleTranslate.this, "Please selected Source Language", Toast.LENGTH_SHORT).show();
                }else if(tolanguageCode==0){
                    Toast.makeText(GoogleTranslate.this, "Please Select the language to make Translation", Toast.LENGTH_SHORT).show();
                }else{


                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(GoogleTranslate.this);
                    } else {
                        Log.d(TAG, "The interstitial ad wasn't ready yet.");
                    }

                    String ParagraphText =etSource.getText().toString();
                    translateText(fromLanguageCode,tolanguageCode,ParagraphText);
                    progressbar.setVisibility(View.VISIBLE);
                }
            }
        });

    }



    public void ads(){

        mAdView = findViewById(R.id.adView3);

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(this);


        mAdView.loadAd(adRequest);
        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-4146389542308743/2030131617");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });



    }

    private InterstitialAd mInterstitialAd;

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

    @MainThread
    public void onBackPressed() {
        getOnBackPressedDispatcher().onBackPressed();



        if (mInterstitialAd != null) {
            mInterstitialAd.show(GoogleTranslate.this);
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.");
        }


    }

    public void translateText(int fromlanguageCode, int tolanguageCode, String tpesource)
    {


        if(isConnected()) {
            tvloader.setText("Downloading....");
            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                    .setSourceLanguage(fromlanguageCode)
                    .setTargetLanguage(tolanguageCode)
                    .build();
            FirebaseApp.getInstance();
            FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().requireWifi().build();
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    tvloader.setText("Translating...");
                    progressbar.setVisibility(View.GONE);
                    translator.translate(tpesource).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            progressbar.setVisibility(View.GONE);
                            tvloader.setText("Results Below.");
                            etSource.setText(s+ "\n");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            tvloader.setText("Failed to translate."+ "\n"+"Please Try again.");
                            progressbar.setVisibility(View.GONE);
                           // Toast.makeText(GoogleTranslate.this, "Failed to translate" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    tvloader.setText("Failed to download language model. "+ "\n"+"Please Try again.");
                    progressbar.setVisibility(View.GONE);
                   // Toast.makeText(GoogleTranslate.this, "Failed to download language model" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            progressbar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Please Check you internet connection. Thank you", Toast.LENGTH_SHORT).show();
            tvloader.setText("Please Check you internet connection. "+"\n"+"Thank you");


        }


    }




    public int   getLanguage(String language)
    {

        int Languagecode=0;

        switch (language){

            case "Telugu":
                Languagecode= FirebaseTranslateLanguage.TE;
                break;

            case "Tamil":
                Languagecode= FirebaseTranslateLanguage.TA;
                break;

            case "Swedish":
                Languagecode= FirebaseTranslateLanguage.SV;
                break;

            case "Albanian":
                Languagecode= FirebaseTranslateLanguage.SQ;
                break;

            case "Slovenian":
                Languagecode= FirebaseTranslateLanguage.SL;
                break;

            case "Slovak":
                Languagecode= FirebaseTranslateLanguage.RU;
                break;

            case "Romanian":
                Languagecode= FirebaseTranslateLanguage.RO;
                break;

            case "Polish":
                Languagecode= FirebaseTranslateLanguage.PL;
                break;

            case "Russian":
                Languagecode= FirebaseTranslateLanguage.SK;
                break;

            case "Thai":
                Languagecode= FirebaseTranslateLanguage.TH;
                break;

            case "Tagalog":
                Languagecode= FirebaseTranslateLanguage.TL;
                break;

            case "Turkish":
                Languagecode= FirebaseTranslateLanguage.TR;
                break;

            case "Ukrainian":
                Languagecode= FirebaseTranslateLanguage.UK;
                break;

            case "Urdu":
                Languagecode= FirebaseTranslateLanguage.UR;
                break;
            case "Vietnamese":
                Languagecode= FirebaseTranslateLanguage.VI;
                break;

            case "Chinese":
                Languagecode= FirebaseTranslateLanguage.ZH;
                break;

            case "Norwegian":
                Languagecode= FirebaseTranslateLanguage.NO;
                break;

            case "Dutch":
                Languagecode= FirebaseTranslateLanguage.NL;
                break;
            case "Maltese":
                Languagecode= FirebaseTranslateLanguage.MS;
                break;

            case "Malay":
                Languagecode= FirebaseTranslateLanguage.MS;
                break;
            case "Marathi":
                Languagecode= FirebaseTranslateLanguage.MR;
                break;
            case "Macedonian":
                Languagecode= FirebaseTranslateLanguage.MK;
                break;

            case "Latvian":
                Languagecode= FirebaseTranslateLanguage.LV;
                break;

            case "Lithuanian":
                Languagecode= FirebaseTranslateLanguage.LT;
                break;

            case "Korean":
                Languagecode= FirebaseTranslateLanguage.KO;
                break;

            case "Kannada":
                Languagecode= FirebaseTranslateLanguage.KN;
                break;

            case "Georgian":
                Languagecode= FirebaseTranslateLanguage.KA;
                break;

            case "Portuguese":
                Languagecode= FirebaseTranslateLanguage.PT;
                break;


            case "Swahili":
                Languagecode= FirebaseTranslateLanguage.SW;
                break;

            case "Japanese":
                Languagecode= FirebaseTranslateLanguage.JA;
                break;

            case "Italian":
                Languagecode= FirebaseTranslateLanguage.IT;
                break;

            case "Icelandic":
                Languagecode= FirebaseTranslateLanguage.IS;
                break;

            case "Indonesian":
                Languagecode= FirebaseTranslateLanguage.ID;
                break;

            case "Hungarian":
                Languagecode= FirebaseTranslateLanguage.HU;
                break;

            case "Haitian":
                Languagecode= FirebaseTranslateLanguage.HT;
                break;

            case "Croatian":
                Languagecode= FirebaseTranslateLanguage.HR;
                break;

            case "Hindi":
                Languagecode= FirebaseTranslateLanguage.HI;
                break;

            case "Hebrew":
                Languagecode= FirebaseTranslateLanguage.HE;
                break;

            case "Gujarati":
                Languagecode= FirebaseTranslateLanguage.GU;
                break;

            case "Galician":
                Languagecode= FirebaseTranslateLanguage.GL;
                break;

            case "Irish":
                Languagecode= FirebaseTranslateLanguage.GA;
                break;

            case "French":
                Languagecode= FirebaseTranslateLanguage.FR;
                break;



            case "Finnish":
                Languagecode= FirebaseTranslateLanguage.FI;
                break;

            case "Persian":
                Languagecode= FirebaseTranslateLanguage.FA;
                break;


            case "Estonian":
                Languagecode= FirebaseTranslateLanguage.ET;
                break;

            case "Spanish":
                Languagecode= FirebaseTranslateLanguage.ES;
                break;

            case "Esperanto":
                Languagecode= FirebaseTranslateLanguage.EO;
                break;

            case "English":
                Languagecode= FirebaseTranslateLanguage.EN;
                break;

            case "Greek":
                Languagecode= FirebaseTranslateLanguage.EL;
                break;

            case "German":
                Languagecode= FirebaseTranslateLanguage.DE;
                break;

            case "Danish":
                Languagecode= FirebaseTranslateLanguage.DA;
                break;


            case "Welsh":
                Languagecode= FirebaseTranslateLanguage.CY;
                break;


            case "Czech":
                Languagecode= FirebaseTranslateLanguage.CS;
                break;

            case "Catalan":
                Languagecode= FirebaseTranslateLanguage.CA;
                break;
            case "Bengali":
                Languagecode= FirebaseTranslateLanguage.BN;
                break;

            case "Belarusian":
                Languagecode= FirebaseTranslateLanguage.BE;
                break;

            case "Arabic":
                Languagecode= FirebaseTranslateLanguage.AR;
                break;

            case "Afrikaans":
                Languagecode= FirebaseTranslateLanguage.AF;
                break;


            default:
                Languagecode=0;
                break;
        }

    return Languagecode;
    }

    public boolean isConnected() {
        boolean connected = false;
        try {


            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();


            return connected;

            } catch (Exception e)
                {
                    Log.e("Connectivity Exception", e.getMessage());
                }

        return connected;
    }


}