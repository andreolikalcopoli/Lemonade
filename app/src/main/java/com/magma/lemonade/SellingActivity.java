package com.magma.lemonade;

import static com.magma.lemonade.db.DatabaseReader.readStand;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.magma.lemonade.db.DatabaseReader;
import com.magma.lemonade.db.DatabaseWriter;
import com.magma.lemonade.models.Stand;
import com.magma.lemonade.utils.Utils;

public class SellingActivity extends AppCompatActivity {
    private TextView tvTitle, tvPrice, tvChange;
    private ImageView imgSellComplete;
    private Button btnKeepChange;
    private EditText etInput;

    private boolean complete = false;
    private double change;
    private Stand stand;

    private DatabaseReader databaseReader;
    private DatabaseWriter databaseWriter;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        getSupportActionBar().hide();

        init();
    }

    private void displayData() {
        readStand = true;
        databaseReader.getStand(new DatabaseReader.StandCallback() {
            @Override
            public void onCallback(Stand stand) {
                SellingActivity.this.stand = stand;
                tvTitle.setText(stand.getSelling());
                tvPrice.setText("$" + stand.getPortionPrice());
            }
        });
    }

    private void setListeners() {
        imgSellComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (complete) {
                    etInput.setText("");
                    btnKeepChange.setText("Keep the change!");
                    btnKeepChange.setBackgroundResource(R.drawable.white_button);
                    btnKeepChange.setVisibility(View.INVISIBLE);
                    imgSellComplete.setImageResource(R.drawable.btsell);
                    tvChange.setText("Change: $0");
                    complete = false;
                } else if (!utils.isEmpty(etInput)){
                    double amount = Double.parseDouble(etInput.getText().toString().trim());
                    change = amount - stand.getPortionPrice();

                    if (change < 0)
                        utils.displayMessage("Amount received is not enough!");
                    else {
                        stand.incrementSold();
                        databaseWriter.sellProduct(stand.getSoldItems());
                        btnKeepChange.setVisibility(View.VISIBLE);
                        imgSellComplete.setImageResource(R.drawable.btcomplete);
                        tvChange.setText("Change: $" + String.format("%.2f", change));
                        complete = true;
                    }
                } else utils.displayMessage("Please enter amount received!");
            }
        });

        btnKeepChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stand.incrementChange(change);
                databaseWriter.keepChange(stand.getChange());
                btnKeepChange.setText("Yeeey!");
                btnKeepChange.setBackgroundResource(R.drawable.yellow_button);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            utils.intent(MainActivity.class, null);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        utils.intent(MainActivity.class, null);
        super.onBackPressed();
    }

    private void init() {
        tvTitle = (TextView) findViewById(R.id.introTitle);
        tvPrice = (TextView) findViewById(R.id.introPrice);
        tvChange = (TextView) findViewById(R.id.tChange);
        imgSellComplete = (ImageView) findViewById(R.id.btSellComplete);
        btnKeepChange = (Button) findViewById(R.id.btKeepChange);
        etInput = (EditText) findViewById(R.id.etCboxInput);

        databaseReader = new DatabaseReader();
        databaseWriter = new DatabaseWriter();
        utils = new Utils(this);

        displayData();
        setListeners();
    }
}