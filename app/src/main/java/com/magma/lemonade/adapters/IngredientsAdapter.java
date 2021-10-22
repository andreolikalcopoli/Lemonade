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
import com.magma.lemonade.models.Product;

import java.util.ArrayList;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterHolder> {
    private Context context;
    private ArrayList<Product> ingredients;

    private String tag = "INGREDIENT_ADAPTER";

    public IngredientsAdapter(Context context, ArrayList<Product> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ingredient, null);
        return new IngredientsAdapterHolder(layout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final IngredientsAdapterHolder viewHolder, final int i) {
        Product ingredient = ingredients.get(i);
        viewHolder.bind(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class IngredientsAdapterHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvPrice;

        public IngredientsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.ingredientName);
            tvPrice = (TextView) itemView.findViewById(R.id.ingredientPrice);
        }

        void bind(Product product) {
            tvName.setText(product.getName());
            tvPrice.setText("$" + product.getPrice());
        }
    }
}

