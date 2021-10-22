package com.magma.lemonade;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PremiumActivity extends AppCompatActivity {

    private ImageView imgSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        getSupportActionBar().hide();

        imgSub = (ImageView) findViewById(R.id.imgSub);

        imgSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kupi
            }
        });
    }
}