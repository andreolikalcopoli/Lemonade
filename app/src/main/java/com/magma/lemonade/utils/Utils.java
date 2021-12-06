package com.magma.lemonade.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.io.IOException;

public class Utils {
    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public void displayMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void setRecycler(RecyclerView recyclerView, RecyclerView.Adapter adapter, int columns) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, columns);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    public void intent(Class c, Bundle bundle) {
        Intent intent = new Intent(context, c);
        if (bundle != null)
            intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    public boolean isEmpty(EditText et){
        return et.getText().toString().trim().equals("");
    }

    public void playAudio(int audio) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, audio);
        mediaPlayer.start();
    }
}
