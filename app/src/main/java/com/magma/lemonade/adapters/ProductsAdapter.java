package com.magma.lemonade.adapters;

import static com.magma.lemonade.StartActivity.newIngredient;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.magma.lemonade.R;
import com.magma.lemonade.models.Product;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsAdapterHolder> {
    private Context context;
    private ArrayList<Product> products;
    private boolean select = false;

    private String tag = "PRODUCT_ADAPTER";

    public ProductsAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    public void setSelect(boolean b){
        this.select = b;
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
        viewHolder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductsAdapterHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvDesc, tvPrice;
        private ImageView imgEquipped;
        private ConstraintLayout itemProduct;

        public ProductsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tProductName);
            tvDesc = (TextView) itemView.findViewById(R.id.tProductDesc);
            tvPrice = (TextView) itemView.findViewById(R.id.tProductPrice);
            imgEquipped = (ImageView) itemView.findViewById(R.id.imgProductChecked);
            itemProduct = (ConstraintLayout) itemView.findViewById(R.id.itemProduct);
        }

        void bind(Product product) {
            tvName.setText(product.getName());
            tvPrice.setText("$" + product.getPrice());
            tvDesc.setText(product.getDesc());
            if (product.isEquipped()) imgEquipped.setImageResource(R.drawable.checked);
            else imgEquipped.setImageResource(R.drawable.xicon);

            itemProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newIngredient = product;
                    itemProduct.setBackgroundColor(Color.parseColor("#804089FF"));
                }
            });
        }
    }
}

