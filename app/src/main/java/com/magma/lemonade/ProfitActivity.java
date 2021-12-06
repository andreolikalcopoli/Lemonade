package com.magma.lemonade;

import static com.magma.lemonade.db.DatabaseReader.readStand;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.magma.lemonade.db.DatabaseReader;
import com.magma.lemonade.utils.DateTime;
import com.magma.lemonade.models.Product;
import com.magma.lemonade.models.Stand;
import com.magma.lemonade.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

public class ProfitActivity extends AppCompatActivity {

    private TextView tvFinal, tvExpenses, tvSales, tvIntro;

    private DatabaseReader databaseReader;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);
        getSupportActionBar().hide();

        init();
    }

    private void displayData() {
        readStand = true;
        databaseReader.getStand(new DatabaseReader.StandCallback() {
            @Override
            public void onCallback(Stand stand) {
                if (stand != null) {
                    ArrayList<Product> products = stand.getProducts();
                    double earned = 0, expenses = 0;
                    int soldItems = 0;
                    StringBuilder stringBuilder = new StringBuilder();
                    Log.d("PROFIT_ACTA", "Products : " + products.size());
                    for (int i = 0; i < products.size(); i++) {
                        Product product = products.get(i);
                        earned += product.getSoldItems() * product.getPortionPrice() + product.getChange();
                        expenses += product.getSoldItems() * product.getPortionExpenses();
                        soldItems += product.getSoldItems();
                        stringBuilder.append(product.getName() + " - " + product.getSoldItems() + " ");
                    }

                    Log.d("PROFIT_ACTA", "Earned : " + earned);

                    double profit = earned - expenses;
                    int dateDifference = new DateTime().getDateDifference(stand.getStartDate(), Locale.US);
                    dateDifference = Math.max(dateDifference, 1);
                    double earnedPerDay = earned / dateDifference;
                    double expensesPerDay = expenses / dateDifference;

                    tvIntro.setText("You've earned $" + String.format("%.2f", profit) + "!");
                    tvSales.setText("You've sold " + soldItems + " items!" + "\n" + stringBuilder.toString());
                    tvExpenses.setText("The amount you invested in your business is $" + String.format("%.2f", expenses) + ".");
                    tvFinal.setText("Since your first income, made on " + stand.getStartDate() + ", you’ve earned $" + String.format("%.2f", earned) + ". That makes it $" + String.format("%.2f", earnedPerDay) + " per day. \n" +
                            "\n" +
                            "Taking the expenses in calculation, your final profit is $" + String.format("%.2f", expensesPerDay) + " per day.\n" +
                            "\n" +
                            "Don’t forget, keep grinding!");
                } else utils.displayMessage("There is no stand yet!");
            }
        });
    }

    private void init() {
        tvFinal = (TextView) findViewById(R.id.finalText);
        tvExpenses = (TextView) findViewById(R.id.expensesText);
        tvSales = (TextView) findViewById(R.id.salesText);
        tvIntro = (TextView) findViewById(R.id.introProfit);

        databaseReader = new DatabaseReader();
        utils = new Utils(this);
        utils.playAudio(R.raw.cashbox);

        displayData();
    }
}