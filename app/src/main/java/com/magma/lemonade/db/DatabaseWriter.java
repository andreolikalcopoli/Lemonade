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

public class DatabaseWriter {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private PrefSingleton prefSingleton = PrefSingleton.getInstance();
    private boolean sellItem = false;

    public void createUser() {
        String id = databaseReference.child("users").push().getKey();
        prefSingleton.saveString("id", id);
        referenceOnMe().child("id").setValue(id);
    }

    private DatabaseReference referenceOnMe() {
        return databaseReference.child("users").child(prefSingleton.getString("id"));
    }

    public void createStand(Stand stand){
        referenceOnMe().child("stand").setValue(stand);
        prefSingleton.saveBool("stand", true);
    }

    public void addProduct(Product product) {
        referenceOnMe().child("products").push().setValue(product);
    }

    public void setValue(String path, Object value) {
        referenceOnMe().child(path).setValue(value);
    }

    public void sellProduct(int soldItems) {
        referenceOnMe().child("stand").child("soldItems").setValue(soldItems);
//        sellItem = true;
//        referenceOnMe().child("stand").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (sellItem) {
//                    sellItem = false;
//
//                    Stand stand = snapshot.getValue(Stand.class);
//                    int sold = stand.getSoldItems() + 1;
//                    int newChange = stand.getChange() + change;
//                    referenceOnMe().child("stand").child("soldItems").setValue(sold);
//                    referenceOnMe().child("stand").child("change").setValue(newChange);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
    }

    public void keepChange(double change) {
        referenceOnMe().child("stand").child("change").setValue(change);
    }
}
