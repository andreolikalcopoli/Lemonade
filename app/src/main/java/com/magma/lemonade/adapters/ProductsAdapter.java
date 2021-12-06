package com.magma.lemonade.adapters;

import static com.magma.lemonade.StartActivity.newIngredient;
import static com.magma.lemonade.utils.Constants.imageExtension;
import static com.magma.lemonade.utils.Constants.imagePath;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.magma.lemonade.R;
import com.magma.lemonade.StartActivity;
import com.magma.lemonade.db.StorageWR;
import com.magma.lemonade.models.Product;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsAdapterHolder> {
    private Context context;
    private ArrayList<Product> products;
    private boolean select = false;
    private StartActivity startActivity;

    private String tag = "PRODUCT_ADAPTER";

    public ProductsAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
        try {
            this.startActivity = (StartActivity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ProductsAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, null);
        return new ProductsAdapterHolder(layout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ProductsAdapterHolder viewHolder, final int i) {
        Product product = products.get(i);
        viewHolder.tvName.setText(product.getName());
        viewHolder.tvPrice.setText("$" + String.format("%.2f", product.getPortionPrice()));
        if (product.isImage())
            new StorageWR(context).loadGlide(imagePath + product.getName() + imageExtension, viewHolder.imgProduct);

        viewHolder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setImage(true);
                startActivity.chooseImage(product.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductsAdapterHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvPrice;
        private ImageView imgProduct;

        public ProductsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tProductName);
            tvPrice = (TextView) itemView.findViewById(R.id.tProductPrice);
            imgProduct = (ImageView) itemView.findViewById(R.id.imgProductItem);
        }
    }
}

