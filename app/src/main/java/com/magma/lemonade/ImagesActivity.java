package com.magma.lemonade;

import static com.magma.lemonade.db.DatabaseReader.readImages;
import static com.magma.lemonade.utils.Constants.imageExtension;
import static com.magma.lemonade.utils.Constants.imagePath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.magma.lemonade.adapters.ImagesAdapter;
import com.magma.lemonade.db.DatabaseReader;
import com.magma.lemonade.db.DatabaseWriter;
import com.magma.lemonade.db.StorageWR;
import com.magma.lemonade.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImagesActivity extends AppCompatActivity {
    private RecyclerView recImages;
    private Button btnProducts, btnNewImage;
    private ProgressDialog progressDialog;

    private Bitmap bitmap;
    private byte[] bytes;
    private int imagesNumber = 0;

    private Utils utils;
    private ImagesAdapter imagesAdapter;
    private DatabaseReader databaseReader;
    private DatabaseWriter databaseWriter;
    private StorageWR storageWR;

    private String tag = "IMAGES_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        getSupportActionBar().hide();

        init();
    }

    private void loadImageFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 5) {
                try {
                    Uri filePath = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                    bytes = baos.toByteArray();

                    progressDialog.show();

                    imagesNumber++;
                    String path = imagePath + imagesNumber + imageExtension;
                    storageWR.uploadImage(bytes, path);
                    databaseWriter.setValue("imagesNumber", imagesNumber);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            imagesAdapter.showNewImage();
                        }
                    }, 2500);
                } catch (IOException e) {
                    Log.d(tag, e.toString());
                }
            }
        }
    }

    private void setListeners() {
        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnNewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImageFromGallery();
            }
        });
    }

    private void showRecycler() {
        readImages = true;
        databaseReader.getImagesNumber(new DatabaseReader.ImagesNumberCallback() {
            @Override
            public void onCallback(int imagesNumber) {
                ImagesActivity.this.imagesNumber = imagesNumber;
                imagesAdapter = new ImagesAdapter(ImagesActivity.this, imagesNumber);
                utils.setRecycler(recImages, imagesAdapter, 2);
            }
        });
    }

    private void init() {
        recImages = (RecyclerView) findViewById(R.id.recImages);
        btnNewImage = (Button) findViewById(R.id.btNewImage);
        btnProducts = (Button) findViewById(R.id.btProductsList);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image...");

        utils = new Utils(this);
        databaseReader = new DatabaseReader();
        databaseWriter = new DatabaseWriter();
        storageWR = new StorageWR(this);

        setListeners();
        showRecycler();
    }
}