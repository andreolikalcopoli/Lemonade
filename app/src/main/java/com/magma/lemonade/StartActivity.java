package com.magma.lemonade;

import static com.magma.lemonade.db.DatabaseReader.readProducts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.magma.lemonade.adapters.IngredientsAdapter;
import com.magma.lemonade.adapters.ProductsAdapter;
import com.magma.lemonade.db.DatabaseReader;
import com.magma.lemonade.db.DatabaseWriter;
import com.magma.lemonade.models.DateTime;
import com.magma.lemonade.models.Product;
import com.magma.lemonade.models.Stand;
import com.magma.lemonade.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

public class StartActivity extends AppCompatActivity {
    private EditText etName, etLocation, etNote;
    private TextView tvCostTotal, tvPercent, tvProfitTotal, tvCostPortion, tvProfitPortion;
    private EditText etAmount;
    private ImageView imgAddIngredients;
    private RecyclerView recIngredients;
    private Button btnStart;

    private ArrayList<Product> ingredients = new ArrayList<>();
    double portionPrice, portionExpense;
    public static Product newIngredient = null;

    private Utils utils;
    private DatabaseWriter databaseWriter;
    private DatabaseReader databaseReader;
    private IngredientsAdapter ingredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        init();
    }

    private void calculatePrice() {
        double expenses = 0;
        for (Product i:ingredients)
            expenses+=i.getPrice();

        int amount = Integer.parseInt(etAmount.getText().toString().trim());
        expenses/=amount;
        portionPrice = expenses * 1.1;
        portionExpense = expenses;
        double portionProfit = portionPrice - expenses;
        double profit = portionProfit * amount;

        tvCostTotal.setText(String.valueOf(String.format("%.2f", expenses)));
        tvProfitTotal.setText(String.valueOf(String.format("%.2f", portionPrice)));
        tvCostPortion.setText(String.valueOf(String.format("%.2f", portionProfit)));
        tvProfitPortion.setText(String.valueOf(String.format("%.2f", profit)));
    }

    private void setListeners() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnStart.getText().toString().toLowerCase(Locale.ROOT).equals("calculate price")) {
                    if (!utils.isEmpty(etAmount)) {
                        calculatePrice();
                        btnStart.setText("START SELLING");
                    } else utils.displayMessage("Please enter the amount");
                } else {
                    if (checkData()) {
                        Stand stand = new Stand();
                        stand.setPortionExpenses(portionExpense);
                        stand.setPortionPrice(portionPrice);
                        stand.setLocation(etLocation.getText().toString().trim());
                        stand.setSelling(etName.getText().toString().trim());
                        stand.setNotes(etNote.getText().toString().trim());
                        stand.setProducts(ingredients);
                        stand.setStartDate(new DateTime().getCurrentDate());
                        stand.setSoldItems(0);
                        stand.setChange(0);

                        databaseWriter.createStand(stand);

                        utils.intent(SellingActivity.class, null);
                    } else utils.displayMessage("Please fill in all the fields!");
                }
            }
        });

        imgAddIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngredient();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addIngredient() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_ingredients);
        dialog.setCancelable(false);
        dialog.show();

        RecyclerView recProducts = (RecyclerView) dialog.findViewById(R.id.recPopupIngredients);
        Button btnConfirm = (Button) dialog.findViewById(R.id.btConfirmPopupIngredients);

        readProducts = true;
        databaseReader.getProducts(new DatabaseReader.ProductsCallback() {
            @Override
            public void onCallback(ArrayList<Product> products) {
                ProductsAdapter productsAdapter = new ProductsAdapter(StartActivity.this, products);
                productsAdapter.setSelect(true);
                utils.setRecycler(recProducts, productsAdapter, 1);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredients.add(newIngredient);
                ingredientsAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

    private boolean checkData(){
        return !utils.isEmpty(etName) && !utils.isEmpty(etLocation);
    }
    
    private void showRecycler() {
        ingredientsAdapter = new IngredientsAdapter(StartActivity.this, ingredients);
        utils.setRecycler(recIngredients, ingredientsAdapter, 1);
    }

    private void init() {
        etName = (EditText) findViewById(R.id.etProductName);
        etLocation = (EditText) findViewById(R.id.etLocation);
        etNote = (EditText) findViewById(R.id.etNotes);
        tvCostTotal = (TextView) findViewById(R.id.tvCost1);
        tvCostPortion = (TextView) findViewById(R.id.tvCost2);
        tvPercent = (TextView) findViewById(R.id.tvPercent);
        tvProfitTotal = (TextView) findViewById(R.id.tvProfit);
        tvProfitPortion = (TextView) findViewById(R.id.tvProfit2);
        imgAddIngredients = (ImageView) findViewById(R.id.imgAddIngredients);
        etAmount = (EditText) findViewById(R.id.etAmount);
        recIngredients = (RecyclerView) findViewById(R.id.recIngredients);
        btnStart = (Button) findViewById(R.id.btStartStand);

        utils = new Utils(this);
        databaseWriter = new DatabaseWriter();
        databaseReader = new DatabaseReader();

        setListeners();
        showRecycler();
    }
}