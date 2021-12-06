package com.magma.lemonade;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.magma.lemonade.utils.Utils;

public class RecipesActivity extends AppCompatActivity {
    private TextView tvRecipeText;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        getSupportActionBar().hide();

        mediaPlayer = MediaPlayer.create(this, R.raw.calm);
        mediaPlayer.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }, 7000);

        tvRecipeText = (TextView) findViewById(R.id.recipeText);
        tvRecipeText.setText("   INGREDIENTS\n" +
                "     • 1 cup of sugar\n" +
                "     • 10 lemons\n" +
                "     • 8 cups of water\n" +
                "   INSTRUCTIONS\n" +
                "     • In a small saucepan, combine sugar and 1 cup water. Bring to boil and stir to dissolve sugar. Allow to cool to room temperature, then cover and refrigerate until chilled.\n" +
                "     • Remove seeds from lemon juice, but leave pulp. In pitcher, stir together chilled syrup, lemon juice and remaining 7 cups water.\n" +
                "     • Add sugar for taste!\n");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}