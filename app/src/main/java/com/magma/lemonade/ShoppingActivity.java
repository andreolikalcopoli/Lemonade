package com.magma.lemonade;

import static com.magma.lemonade.db.DatabaseReader.readProducts;
import static com.magma.lemonade.db.DatabaseReader.readShoppings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.magma.lemonade.adapters.ShoppingAdapter;
import com.magma.lemonade.db.DatabaseReader;
import com.magma.lemonade.db.DatabaseWriter;
import com.magma.lemonade.models.Shopping;
import com.magma.lemonade.utils.Utils;

import java.util.ArrayList;

public class ShoppingActivity extends AppCompatActivity {
    private RecyclerView recShopping;
    private Button btnProducts, btnNewItem;

    private ArrayList<Shopping> shoppings = new ArrayList<>();

    private Utils utils;
    private DatabaseWriter databaseWriter;
    private DatabaseReader databaseReader;
    private ShoppingAdapter shoppingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        getSupportActionBar().hide();

        init();
    }

    private void setListeners() {
        btnNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();
            }
        });

        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.intent(ProductsActivity.class, null);
            }
        });
    }

    private void addNewItem() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_new_product);
        dialog.setCancelable(false);
        dialog.show();

        EditText etName = (EditText) dialog.findViewById(R.id.etNewTitle);
        EditText etDesc = (EditText) dialog.findViewById(R.id.etNewDesc);
        EditText etPrice = (EditText) dialog.findViewById(R.id.etNewPrice);
        Button btnAdd = (Button) dialog.findViewById(R.id.btAddProduct);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if (!utils.isEmpty(etName) && !utils.isEmpty(etPrice)){
                    Shopping shopping = new Shopping();
                    shopping.setName(etName.getText().toString().trim());
                    shopping.setDesc(etDesc.getText().toString().trim());
                    shopping.setPrice(Integer.parseInt(etPrice.getText().toString().trim()));

                    databaseWriter.addShopping(shopping);
                    shoppings.add(shopping);
                    shoppingAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                } else utils.displayMessage("Please fill in all the fields");
            }
        });
    }

    private void showRecycler() {
        readShoppings = true;
        databaseReader.getShoppings(new DatabaseReader.ShoppingsCallback() {
            @Override
            public void onCallback(ArrayList<Shopping> shoppings) {
                ShoppingActivity.this.shoppings = shoppings;
                shoppingAdapter = new ShoppingAdapter(ShoppingActivity.this, shoppings);
                utils.setRecycler(recShopping, shoppingAdapter, 1);
            }
        });
    }

    private void init() {
        recShopping = (RecyclerView) findViewById(R.id.recShopping);
        btnProducts = (Button) findViewById(R.id.btProductsList);
        btnNewItem = (Button) findViewById(R.id.btNewShopping);

        utils = new Utils(this);
        databaseWriter = new DatabaseWriter();
        databaseReader = new DatabaseReader();

        setListeners();
        showRecycler();
    }
}