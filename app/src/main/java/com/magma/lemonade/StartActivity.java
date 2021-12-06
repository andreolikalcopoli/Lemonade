package com.magma.lemonade;

import static com.magma.lemonade.db.DatabaseReader.readShoppings;
import static com.magma.lemonade.utils.Constants.imageExtension;
import static com.magma.lemonade.utils.Constants.imagePath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.magma.lemonade.adapters.IngredientsAdapter;
import com.magma.lemonade.adapters.ProductsAdapter;
import com.magma.lemonade.adapters.ShoppingAdapter;
import com.magma.lemonade.db.DatabaseReader;
import com.magma.lemonade.db.DatabaseWriter;
import com.magma.lemonade.db.StorageWR;
import com.magma.lemonade.utils.DateTime;
import com.magma.lemonade.models.Product;
import com.magma.lemonade.models.Shopping;
import com.magma.lemonade.models.Stand;
import com.magma.lemonade.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {
    private EditText etName, etNumber;
    private ImageView imgAddIngredients;
    private RecyclerView recIngredients, recProducts;
    private Button btnStart, btnCreateProduct, btnGallery, btnCamera;;
    private ProgressDialog progressDialog;
    private ConstraintLayout consImageSource;

    private ArrayList<Shopping> ingredients = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    double portionPrice, portionExpense, expenses = 0;
    public static Shopping newIngredient = null;
    private Bitmap bitmap;
    private byte[] bytes;
    private String productClicked = "";

    private Utils utils;
    private DatabaseWriter databaseWriter;
    private DatabaseReader databaseReader;
    private StorageWR storageWR;
    private IngredientsAdapter ingredientsAdapter;
    private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        init();
    }

    private void setListeners() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (products.size() > 0) {
                    Stand stand = new Stand();
                    stand.setProducts(products);
                    stand.setStartDate(new DateTime().getCurrentDate());

                    databaseWriter.createStand(stand);

                    utils.playAudio(R.raw.success);
                    utils.intent(SellingActivity.class, null);
                } else utils.displayMessage("Please create at least one product!");
            }
        });

        imgAddIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngredient();
            }
        });

        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    String name = etName.getText().toString().trim();
                    int unitNumbers = Integer.parseInt(etNumber.getText().toString().trim());
                    if (unitNumbers > 0) {
                        calculatePrice(unitNumbers);
                        Product product = new Product();
                        product.setName(name);
                        product.setPortionPrice(portionPrice);
                        product.setPortionExpenses(portionExpense);
                        product.setImage(false);
                        product.setSoldItems(0);
                        product.setChange(0);
                        addProduct(product);

                        etName.setText("");
                        etNumber.setText("");
                        ingredients.clear();
                        ingredientsAdapter.notifyDataSetChanged();
                    } else utils.displayMessage("Number of units cannot be zero!");
                }
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImageFromGallery();
                consImageSource.setVisibility(View.GONE);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
                consImageSource.setVisibility(View.GONE);
            }
        });
    }

    private void calculatePrice(int unitNumber) {
        for (Shopping i:ingredients)
            expenses += i.getPrice();

        expenses /= unitNumber;
        portionPrice = expenses * 1.1;
        portionExpense = expenses;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addProduct(Product product) {
        products.add(product);
        if (productsAdapter == null){
            productsAdapter = new ProductsAdapter(this, products);
            utils.setRecycler(recProducts, productsAdapter, 1);
        } else productsAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addIngredient() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_ingredients);
        dialog.setCancelable(false);
        dialog.show();

        RecyclerView recProducts = (RecyclerView) dialog.findViewById(R.id.recPopupIngredients);
        Button btnConfirm = (Button) dialog.findViewById(R.id.btConfirmPopupIngredients);

        readShoppings = true;
        databaseReader.getShoppings(new DatabaseReader.ShoppingsCallback() {
            @Override
            public void onCallback(ArrayList<Shopping> shoppings) {
                ShoppingAdapter shoppingAdapter = new ShoppingAdapter(StartActivity.this, shoppings);
                shoppingAdapter.setSelect(true);
                utils.setRecycler(recProducts, shoppingAdapter, 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 5) {
                try {
                    Uri filePath = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    saveImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");
                saveImage();
            }
        }
    }

    public void chooseImage(String productClicked) {
        this.productClicked = productClicked;
        consImageSource.setVisibility(View.VISIBLE);
    }

    private void loadImageFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 5);
    }

    private void takePicture() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    private void saveImage(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        bytes = baos.toByteArray();

        progressDialog.show();

        String path = imagePath + productClicked + imageExtension;
        storageWR.uploadImage(bytes, path);

        new Handler().postDelayed(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                progressDialog.dismiss();
                productsAdapter.notifyDataSetChanged();
            }
        }, 2500);
    }

    private boolean checkData(){
        return !utils.isEmpty(etName) && !utils.isEmpty(etNumber) && ingredients.size() > 0;
    }
    
    private void showRecycler() {
        ingredientsAdapter = new IngredientsAdapter(StartActivity.this, ingredients);
        utils.setRecycler(recIngredients, ingredientsAdapter, 1);
    }

    private void init() {
        etName = (EditText) findViewById(R.id.etProductName);
        etNumber = (EditText) findViewById(R.id.etUnits);
        imgAddIngredients = (ImageView) findViewById(R.id.imgAddIngredients);
        recIngredients = (RecyclerView) findViewById(R.id.recIngredients);
        recProducts = (RecyclerView) findViewById(R.id.recSellingProducts);
        btnStart = (Button) findViewById(R.id.btStartStand);
        btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);
        btnCamera = (Button) findViewById(R.id.btnCamera);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        consImageSource = (ConstraintLayout) findViewById(R.id.choseImageSource);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image...");

        utils = new Utils(this);
        databaseWriter = new DatabaseWriter();
        databaseReader = new DatabaseReader();
        storageWR = new StorageWR(this);

        setListeners();
        showRecycler();
    }
}