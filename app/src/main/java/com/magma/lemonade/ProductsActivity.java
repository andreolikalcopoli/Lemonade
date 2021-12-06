package com.magma.lemonade;


import static com.magma.lemonade.db.DatabaseReader.readProducts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.magma.lemonade.adapters.ProductsAdapter;
import com.magma.lemonade.db.DatabaseReader;
import com.magma.lemonade.db.DatabaseWriter;
import com.magma.lemonade.models.Product;
import com.magma.lemonade.utils.Utils;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {
    private RecyclerView recProducts;
    private Button btnShoppings;

    private Utils utils;
    private DatabaseReader databaseReader;

    private String tag = "PRODUCTS_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getSupportActionBar().hide();

        init();
    }

    private void setListeners() {
        btnShoppings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showRecycler() {
        readProducts = true;
        databaseReader.getProducts(new DatabaseReader.ProductsCallback() {
            @Override
            public void onCallback(ArrayList<Product> products) {
                ProductsAdapter productsAdapter = new ProductsAdapter(ProductsActivity.this, products);
                utils.setRecycler(recProducts, productsAdapter, 1);
            }
        });
    }

    private void init() {
        recProducts = (RecyclerView) findViewById(R.id.recProducts);
        btnShoppings = (Button) findViewById(R.id.btShoppingList);

        utils = new Utils(this);
        databaseReader = new DatabaseReader();

        setListeners();
        showRecycler();
    }
}