package com.magma.lemonade;

import static com.magma.lemonade.db.DatabaseReader.readProducts;

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
import android.widget.Toast;

import com.magma.lemonade.adapters.ProductsAdapter;
import com.magma.lemonade.db.DatabaseReader;
import com.magma.lemonade.db.DatabaseWriter;
import com.magma.lemonade.models.Product;
import com.magma.lemonade.utils.Utils;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {
    private RecyclerView recProducts;
    private Button btnImages, btnNewProduct;

    private ArrayList<Product> products = new ArrayList<>();

    private Utils utils;
    private DatabaseWriter databaseWriter;
    private DatabaseReader databaseReader;
    private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        getSupportActionBar().hide();

        init();
    }

    private void setListeners() {
        btnNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewProduct();
            }
        });

        btnImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) utils.intent(ImagesActivity.class, null);
                else {
                    ActivityCompat.requestPermissions(ProductsActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
            }
        });
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
                    utils.intent(ImagesActivity.class, null);
                else
                    utils.displayMessage("Permission denied, we can't continue.");

                return;
            }
        }
    }

    private void addNewProduct() {
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
                    Product product = new Product();
                    product.setName(etName.getText().toString().trim());
                    product.setDesc(etDesc.getText().toString().trim());
                    product.setPrice(Integer.parseInt(etPrice.getText().toString().trim()));

                    databaseWriter.addProduct(product);
                    products.add(product);
                    productsAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                } else utils.displayMessage("Please fill in all the fields");
            }
        });
    }

    private void showRecycler() {
        readProducts = true;
        databaseReader.getProducts(new DatabaseReader.ProductsCallback() {
            @Override
            public void onCallback(ArrayList<Product> products) {
                ProductsActivity.this.products = products;
                productsAdapter = new ProductsAdapter(ProductsActivity.this, products);
                utils.setRecycler(recProducts, productsAdapter, 1);
            }
        });

    }

    private void init() {
        recProducts = (RecyclerView) findViewById(R.id.recProducts);
        btnImages = (Button) findViewById(R.id.btImagesList);
        btnNewProduct = (Button) findViewById(R.id.btNewProduct);

        utils = new Utils(this);
        databaseWriter = new DatabaseWriter();
        databaseReader = new DatabaseReader();

        setListeners();
        showRecycler();
    }
}