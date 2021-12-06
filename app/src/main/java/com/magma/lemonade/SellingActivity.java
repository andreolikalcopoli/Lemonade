package com.magma.lemonade;

import static com.magma.lemonade.db.DatabaseReader.readProducts;
import static com.magma.lemonade.db.DatabaseReader.readStand;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.magma.lemonade.adapters.ChangeAdapter;
import com.magma.lemonade.db.DatabaseReader;
import com.magma.lemonade.db.DatabaseWriter;
import com.magma.lemonade.models.Change;
import com.magma.lemonade.models.Product;
import com.magma.lemonade.models.Stand;
import com.magma.lemonade.utils.ChangeCalculator;
import com.magma.lemonade.utils.Rotate3DAnimation;
import com.magma.lemonade.utils.Utils;

import java.util.ArrayList;

public class SellingActivity extends AppCompatActivity {
    private TextView tvPrice, tvChange;
    private ConstraintLayout consChange;
    private ImageView imgSellComplete, imgFlip;
    private Button btnKeepChange, btnHelp, btnClose;
    private EditText etInput, etAmount;
    private RecyclerView recChange;
    private Spinner spinner;

    private boolean complete = false;
    private double change;
    private Product product;
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Change> changes = new ArrayList<>();

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
        readProducts = true;
        databaseReader.getProducts(new DatabaseReader.ProductsCallback() {
            @Override
            public void onCallback(ArrayList<Product> products) {
                SellingActivity.this.products = products;
                product = products.get(0);
                setSpinner();
                tvPrice.setText("$" + String.format("%.2f", product.getPortionPrice()));
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
                    etAmount.setText("");
                    btnHelp.setVisibility(View.GONE);
                    complete = false;
                } else if (!utils.isEmpty(etInput) && !utils.isEmpty(etAmount)){
                    double money = Double.parseDouble(etInput.getText().toString().trim());
                    int amount = Integer.parseInt(etAmount.getText().toString());
                    change = money - product.getPortionPrice() * amount;

                    if (change < 0)
                        utils.displayMessage("Money received is not enough!");
                    else {
                        product.incrementSold();
                        databaseWriter.sellProduct(product.getSoldItems(), String.valueOf(products.indexOf(product)));
                        btnKeepChange.setVisibility(View.VISIBLE);
                        btnHelp.setVisibility(View.VISIBLE);
                        imgSellComplete.setImageResource(R.drawable.btcomplete);
                        tvChange.setText("Change: $" + String.format("%.2f", change));
                        complete = true;
                        utils.playAudio(R.raw.cashbox);
                    }
                } else utils.displayMessage("Please enter amount received!");
            }
        });

        btnKeepChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.incrementChange(change);
                databaseWriter.keepChange(product.getChange(), String.valueOf(products.indexOf(product)));
                btnKeepChange.setText("Yeeey!");
                btnKeepChange.setBackgroundResource(R.drawable.yellow_button);
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.playAudio(R.raw.coin);
                animateCoin();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consChange.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void animateCoin() {
        Rotate3DAnimation animation;
        animation = new Rotate3DAnimation(imgFlip, R.drawable.coinflip, R.drawable.coinflip, 0, 180, 0, 0, 0, 0);
        animation.setRepeatCount(5);
        animation.setDuration(110);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showChange();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imgFlip.setVisibility(View.VISIBLE);
        imgFlip.startAnimation(animation);
    }

    private void showChange() {
        consChange.setVisibility(View.VISIBLE);
        imgFlip.setVisibility(View.INVISIBLE);
        changes = new ChangeCalculator().calculate(change);
        ChangeAdapter changeAdapter = new ChangeAdapter(changes);
        utils.setRecycler(recChange, changeAdapter, 1);
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

    private void setSpinner() {
        ArrayList<String> names = new ArrayList<>();
        for (Product p:products)
            names.add(p.getName());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, names);
        spinner.setAdapter(arrayAdapter);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                product = products.get(i);
                tvPrice.setText("$" + String.format("%.2f", product.getPortionPrice()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void init() {
        consChange = (ConstraintLayout) findViewById(R.id.consChange);
        spinner = (Spinner) findViewById(R.id.spinner);
        tvPrice = (TextView) findViewById(R.id.introPrice);
        tvChange = (TextView) findViewById(R.id.tChange);
        imgSellComplete = (ImageView) findViewById(R.id.btSellComplete);
        imgFlip = (ImageView) findViewById(R.id.imgFlip);
        btnKeepChange = (Button) findViewById(R.id.btKeepChange);
        btnHelp = (Button) findViewById(R.id.btnHelp);
        btnClose = (Button) findViewById(R.id.btCloseChange);
        etInput = (EditText) findViewById(R.id.etCboxInput);
        etAmount = (EditText) findViewById(R.id.etAmount);
        recChange = (RecyclerView) findViewById(R.id.recChange);

        databaseReader = new DatabaseReader();
        databaseWriter = new DatabaseWriter();
        utils = new Utils(this);

        displayData();
        setListeners();
    }
}