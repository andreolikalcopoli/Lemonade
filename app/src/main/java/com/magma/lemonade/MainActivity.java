package com.magma.lemonade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.magma.lemonade.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout consPremium;
    private Button btnSell;
    private ImageView imgList, imgSell, imgProfit, imgLearn;

    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                utils.intent(ProductsActivity.class, null);
            }
        });
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

        setBanner();
        setListeners();
    }
}