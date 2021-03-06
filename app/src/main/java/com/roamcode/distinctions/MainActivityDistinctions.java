package com.roamcode.distinctions;

import static com.itextpdf.text.pdf.parser.PdfTextExtractor.getTextFromPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivityDistinctions extends AppCompatActivity {

    private final int choose_pdf_from_device=1001;
    private final int choose_word_from_device=1002;
    private static final String TAG = "MainActivityDistinctions";
    ImageView choosefile;
    TextView checktext;
    InputStream inputStream;

    private AdView mAdView;

    Button btnpdf,btnlive,btnword,btnskip;

    private ProgressBar progressbar;

    private InterstitialAd mInterstitialAd;

    final int STORAGE_PERMISSION_CODE=2022;

    Toolbar mtoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        interstitialads();

        //check permission
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

        mtoolbar=findViewById(R.id.toolbar);
        mtoolbar.setTitle("Home");
        setSupportActionBar(mtoolbar);

        checktext=findViewById(R.id.txtupload);

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-4146389542308743/9581156873");


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        btnskip=findViewById(R.id.btnskip);
        btnskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivityDistinctions.this,GoogleTranslate.class));
            }
        });

        choosefile=findViewById(R.id.ivUpload);

        choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // callchooseAnyFromDevice();
                callchoosepdfFromDevice();

            }
        });


       /* btnword=findViewById(R.id.btnworddoc);
        btnword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callchoosewordFromDevice();
            }
        });*/

        btnpdf=findViewById(R.id.btnpdfsubmit);
        btnpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callchoosepdfFromDevice();
            }
        });

        btnlive=findViewById(R.id.btnClasses);
        btnlive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivityDistinctions.this,Login.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.Home:
                startActivity(new Intent(MainActivityDistinctions.this,MainActivityDistinctions.class));
                return true;

            case R.id.policy:

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivityDistinctions.this);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/documenttranslator/home")));
                } else {
                    Log.d(TAG, "The interstitial ad wasn't ready yet.");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/documenttranslator/home")));
                }

                return true;

            case R.id.rate:

                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(MainActivityDistinctions.this);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=9133669126769704698")));
                    } else {
                        Log.d(TAG, "The interstitial ad wasn't ready yet.");
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=9133669126769704698")));
                    }


                return true;

          /*  case R.id.help:
                startActivity(new Intent(MainActivityDistinctions.this,helpDistinct.class));
                return true;*/

            case R.id.AboutUs:
                 if (mInterstitialAd != null) {
                    mInterstitialAd.show(MainActivityDistinctions.this);
                     startActivity(new Intent(MainActivityDistinctions.this,AboutusDistinct.class));

                 } else {
                        Log.d(TAG, "The interstitial ad wasn't ready yet.");
                        startActivity(new Intent(MainActivityDistinctions.this,AboutusDistinct.class));
                 }
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }

    }

    public void callchooseAnyFromDevice(){

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document","application/pdf", "application/msword"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(intent, choose_word_from_device);
    }

    public void callchoosepdfFromDevice(){

        Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent,choose_pdf_from_device);
    }


    public void callchoosewordFromDevice(){

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] mimetypes = {"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(intent, choose_word_from_device);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==choose_pdf_from_device && resultCode== Activity.RESULT_OK){
            if(data !=null){
                Log.d(TAG, "OnActivityResults: "+ data.getData());

               extratextpdffile(data.getData());

            }
        }


    /*    if(requestCode==choose_word_from_device && resultCode== Activity.RESULT_OK){
            if(data !=null){
                Log.d(TAG, "OnActivityResults: "+ data.getData().toString());
                extratextWordfile(data.getData());

            }
        }*/

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

 /*   private void extratextWordfile(Uri uri) {

        try {
            inputStream = MainActivityDistinctions.this.getContentResolver().openInputStream(uri);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                String fileContent ="";
                PdfReader reader=null;
                StringBuilder builder = new StringBuilder();

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        reader = new PdfReader(inputStream);

                        int pages = reader.getNumberOfPages();


                        for (int i = 1; i <= pages; i++) {
                            fileContent = fileContent+ getTextFromPage(reader, i).trim()+ "\n";

                        }
                        builder.append(fileContent);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //checktext.setText(builder.toString());
                                Intent intent=new Intent(MainActivityDistinctions.this,TranslateWord.class);
                                intent.putExtra("Extractword",builder.toString());
                                startActivity(intent);

                            }
                        });
                        reader.close();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
*/



    private void extratextpdffile(Uri uri) {

        try {
            inputStream = MainActivityDistinctions.this.getContentResolver().openInputStream(uri);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

        String fileContent ="";
        PdfReader reader=null;
        StringBuilder builder = new StringBuilder();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                reader = new PdfReader(inputStream);

                int pages = reader.getNumberOfPages();
                for (int i = 1; i <= pages; i++) {
                    fileContent = fileContent+ getTextFromPage(reader, i).trim()+ "\n";

                }
                builder.append(fileContent);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //checktext.setText(builder.toString());
                      Intent intent1=new Intent(MainActivityDistinctions.this,GoogleTranslate.class);
                        intent1.putExtra("Extractpdf",builder.toString());
                        startActivity(intent1);


                    }
                });
                reader.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        }).start();
    }


    // This function is called when user accept or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

   if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              //  Toast.makeText(MainActivityDistinctions.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivityDistinctions.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivityDistinctions.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivityDistinctions.this, new String[] { permission }, requestCode);

            Toast.makeText(MainActivityDistinctions.this, "Storage Permission not Granted", Toast.LENGTH_SHORT).show();
        }
        else {
           // Toast.makeText(MainActivityDistinctions.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }


}