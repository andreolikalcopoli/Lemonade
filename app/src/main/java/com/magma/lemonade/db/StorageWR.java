package com.magma.lemonade.db;

import static com.magma.lemonade.utils.Constants.storageBase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.magma.lemonade.utils.Utils;

import java.io.File;
import java.io.IOException;

public class StorageWR {

    private Context context;
    private StorageReference storageReference;

    private Utils utils;

    private String tag = "STOWR";

    public StorageWR(Context context) {
        storageReference = FirebaseStorage.getInstance(storageBase).getReference();
        this.context = context;
        utils = new Utils(context);
    }

    public void uploadImage(byte[] data, final String path) {
        final StorageReference ref = storageReference.child(path);

        ref.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //utils.displayMessage("Image uploaded successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //progressDialog.dismiss();
                        Log.d(tag, "Fail " + e.toString());
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                    }
                });
    }

    public void loadGlide(String path, final ImageView imageView) {
        StorageReference imageReference = storageReference.child(path);
        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri.toString())
                        .centerCrop()
                        .thumbnail(0.05f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
            }
        });
    }
}
