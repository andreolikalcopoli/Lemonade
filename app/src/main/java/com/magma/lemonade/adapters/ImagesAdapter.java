package com.magma.lemonade.adapters;

import static com.magma.lemonade.utils.Constants.imageExtension;
import static com.magma.lemonade.utils.Constants.imagePath;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.magma.lemonade.R;
import com.magma.lemonade.db.StorageWR;
import com.magma.lemonade.models.Product;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesAdapterHolder> {
    private Context context;
    private int length;

    private StorageWR storageWR;

    private String tag = "IMAGE_ADAPTER";

    public ImagesAdapter(Context context, int length) {
        this.context = context;
        this.length = length;
        this.storageWR = new StorageWR(context);
    }

    @NonNull
    @Override
    public ImagesAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_images, null);
        return new ImagesAdapterHolder(layout);
    }

    public void showNewImage() {
        length += 1;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ImagesAdapterHolder viewHolder, final int i) {
        int ind = i + 1;
        String path = imagePath + ind + imageExtension;
        storageWR.loadGlide(path, viewHolder.imgImage);
    }

    @Override
    public int getItemCount() {
        return length;
    }

    public static class ImagesAdapterHolder extends RecyclerView.ViewHolder {

        private ImageView imgImage;

        public ImagesAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imgImage = (ImageView) itemView.findViewById(R.id.imgImage);
        }
    }
}

