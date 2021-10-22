package com.magma.lemonade;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class RecipesActivity extends AppCompatActivity {
    private TextView tvRecipeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        getSupportActionBar().hide();

        tvRecipeText = (TextView) findViewById(R.id.recipeText);
        tvRecipeText.setText("INGREDIENTS\n" +
                "1 kg of sugar\n" +
                "10 lemons\n" +
                "5 liters of water\u2028\n" +
                "INSTRUCTIONS\n" +
                "In a small saucepan, combine sugar and 1 cup water. Bring to boil and stir to dissolve sugar. Allow to cool to room temperature, then cover and refrigerate until chilled.\n" +
                "Remove seeds from lemon juice, but leave pulp. In pitcher, stir together chilled syrup, lemon juice and remaining 7 cups water.\n");
    }
}