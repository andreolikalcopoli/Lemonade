package com.magma.lemonade.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.magma.lemonade.models.Product;
import com.magma.lemonade.models.Stand;
import com.magma.lemonade.utils.PrefSingleton;

import java.util.ArrayList;

public class DatabaseReader {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private PrefSingleton prefSingleton = PrefSingleton.getInstance();

    public static boolean readProducts = false;
    public static boolean readImages = false;
    public static boolean readStand = false;

    private String tag = "DBASE_READER";

    private DatabaseReference referenceOnMe() {
        return databaseReference.child("users").child(prefSingleton.getString("id"));
    }

    public void getProducts(final ProductsCallback productsCallback) {
        referenceOnMe().child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (readProducts) {
                    readProducts = false;
                    ArrayList<Product> products = new ArrayList<>();

                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            products.add(dataSnapshot.getValue(Product.class));

                        productsCallback.onCallback(products);
                    } else productsCallback.onCallback(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(tag, error.toString());
            }
        });
    }

    public interface ProductsCallback{
        void onCallback(ArrayList<Product> products);
    }

    public void getStand(final StandCallback standCallback) {
        referenceOnMe().child("stand").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (readStand) {
                    readStand = false;

                    standCallback.onCallback(snapshot.getValue(Stand.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(tag, error.toString());
            }
        });
    }

    public interface StandCallback{
        void onCallback(Stand stand);
    }

    public void getImagesNumber(final ImagesNumberCallback imagesNumberCallback) {
        referenceOnMe().child("imagesNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (readImages) {
                    readImages = false;

                    if (snapshot.exists()) imagesNumberCallback.onCallback(snapshot.getValue(Integer.class));
                    else imagesNumberCallback.onCallback(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(tag, error.toString());
            }
        });
    }

    public interface ImagesNumberCallback{
        void onCallback(int imagesNumber);
    }
}
