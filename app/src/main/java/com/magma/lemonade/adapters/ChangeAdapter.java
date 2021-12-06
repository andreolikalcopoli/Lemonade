package com.magma.lemonade.adapters;

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
import com.magma.lemonade.models.Change;

import java.util.ArrayList;

public class ChangeAdapter extends RecyclerView.Adapter<ChangeAdapter.ChangeAdapterHolder> {
    private Context context;
    private ArrayList<Change> changes;

    private String tag = "CHANGE_ADAPTER";

    public ChangeAdapter(ArrayList<Change> changes) {
        this.changes = changes;
    }

    @NonNull
    @Override
    public ChangeAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_change, null);
        return new ChangeAdapterHolder(layout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ChangeAdapterHolder viewHolder, final int i) {
        viewHolder.bind(changes.get(i));
    }

    @Override
    public int getItemCount() {
        return changes.size();
    }

    public static class ChangeAdapterHolder extends RecyclerView.ViewHolder {

        private ImageView imgImage;
        private TextView tvText;

        public ChangeAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imgImage = (ImageView) itemView.findViewById(R.id.imgChange);
            tvText = (TextView) itemView.findViewById(R.id.tChangeItem);
        }

        public void bind(Change change) {
            imgImage.setImageResource(change.getImage());
            tvText.setText(change.getText());
        }
    }
}

