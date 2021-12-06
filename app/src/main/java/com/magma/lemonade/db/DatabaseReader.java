package com.magma.lemonade.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.magma.lemonade.models.Product;
import com.magma.lemonade.models.Shopping;
import com.magma.lemonade.models.Stand;
import com.magma.lemonade.utils.PrefSingleton;

import java.util.ArrayList;

public class DatabaseReader {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private PrefSingleton prefSingleton = PrefSingleton.getInstance();

    public static boolean readProducts = false;
    public static boolean readStand = false;
    public static boolean readShoppings = false;

    private String tag = "DBASE_READER";

    private DatabaseReference referenceOnMe() {
        return databaseReference.child("users").child(prefSingleton.getString("id"));
    }

    public void getProducts(final ProductsCallback productsCallback) {
        referenceOnMe().child("stand").child("products").addValueEventListener(new ValueEventListener() {
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

    public void getShoppings(final ShoppingsCallback shoppingsCallback) {
        referenceOnMe().child("shoppings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (readShoppings) {
                    readShoppings = false;
                    ArrayList<Shopping> shoppings = new ArrayList<>();

                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            shoppings.add(dataSnapshot.getValue(Shopping.class));

                        shoppingsCallback.onCallback(shoppings);
                    } else shoppingsCallback.onCallback(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(tag, error.toString());
            }
        });
    }

    public interface ShoppingsCallback{
        void onCallback(ArrayList<Shopping> shoppings);
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
}
