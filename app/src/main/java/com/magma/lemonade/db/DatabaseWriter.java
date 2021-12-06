package com.magma.lemonade.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.magma.lemonade.models.Shopping;
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

    public void addShopping(Shopping shopping) {
        referenceOnMe().child("shoppings").push().setValue(shopping);
    }

    public void setValue(String path, Object value) {
        referenceOnMe().child(path).setValue(value);
    }

    public void sellProduct(int soldItems, String productID) {
        referenceOnMe().child("stand").child("products").child(productID).child("soldItems").setValue(soldItems);
    }

    public void keepChange(double change, String productID) {
        referenceOnMe().child("stand").child("products").child(productID).child("change").setValue(change);
    }
}
