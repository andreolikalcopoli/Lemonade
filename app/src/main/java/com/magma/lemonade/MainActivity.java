package com.magma.lemonade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.magma.lemonade.db.DatabaseWriter;
import com.magma.lemonade.utils.PrefSingleton;
import com.magma.lemonade.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout consPremium;
    private Button btnSell;
    private ImageView imgList, imgSell, imgProfit, imgLearn;

    private Utils utils;
    private PrefSingleton prefSingleton;
    private DatabaseWriter databaseWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        init();
    }

    private void setListeners() {
        consPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.intent(PremiumActivity.class, null);
            }
        });

        imgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.intent(ShoppingActivity.class, null);
            }
        });

        imgLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.intent(RecipesActivity.class, null);
            }
        });

        imgProfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.intent(ProfitActivity.class, null);
            }
        });

        imgSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelling();
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSelling();
            }
        });
    }

    private void startSelling(){
        if (!prefSingleton.getBool("stand"))
            utils.intent(StartActivity.class, null);
        else
            utils.intent(SellingActivity.class, null);
    }

    private void setBanner() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void init() {
        consPremium = (ConstraintLayout) findViewById(R.id.getPremium);
        btnSell = (Button) findViewById(R.id.btStartSelling);
        imgList = (ImageView) findViewById(R.id.imgList);
        imgLearn = (ImageView) findViewById(R.id.imgLearn);
        imgProfit = (ImageView) findViewById(R.id.imgProfit);
        imgSell = (ImageView) findViewById(R.id.imgSelling);

        utils = new Utils(this);
        PrefSingleton.getInstance().Initialize(this);
        prefSingleton = PrefSingleton.getInstance();
        databaseWriter = new DatabaseWriter();

        if (prefSingleton.getString("id").length() < 1) databaseWriter.createUser();

        setBanner();
        setListeners();
        if (checkPermission()) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    utils.intent(ProductsActivity.class, null);
                else
                    utils.displayMessage("Permission denied, we can't continue.");

                return;
            }
        }
    }
}