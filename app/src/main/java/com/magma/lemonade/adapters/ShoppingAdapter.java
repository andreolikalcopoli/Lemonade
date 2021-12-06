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

import java.util.ArrayList;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ShoppingAdapterHolder> {
    private Context context;
    private ArrayList<com.magma.lemonade.models.Shopping> Shopping;
    private boolean select = false;

    private String tag = "PRODUCT_ADAPTER";

    public ShoppingAdapter(Context context, ArrayList<com.magma.lemonade.models.Shopping> Shopping) {
        this.context = context;
        this.Shopping = Shopping;
    }

    public void setSelect(boolean b){
        this.select = b;
    }

    @NonNull
    @Override
    public ShoppingAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shopping, null);
        return new ShoppingAdapterHolder(layout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ShoppingAdapterHolder viewHolder, final int i) {
        com.magma.lemonade.models.Shopping shopping = Shopping.get(i);
        viewHolder.bind(shopping);
    }

    @Override
    public int getItemCount() {
        return Shopping.size();
    }

    public static class ShoppingAdapterHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvDesc, tvPrice;
        private ImageView imgEquipped;
        private ConstraintLayout itemProduct;

        public ShoppingAdapterHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tShoppingName);
            tvDesc = (TextView) itemView.findViewById(R.id.tShoppingDesc);
            tvPrice = (TextView) itemView.findViewById(R.id.tShoppingPrice);
            imgEquipped = (ImageView) itemView.findViewById(R.id.imgShoppingChecked);
            itemProduct = (ConstraintLayout) itemView.findViewById(R.id.itemProduct);
        }

        void bind(com.magma.lemonade.models.Shopping shopping) {
            tvName.setText(shopping.getName());
            tvPrice.setText("$" + shopping.getPrice());
            tvDesc.setText(shopping.getDesc());
            if (shopping.isEquipped()) imgEquipped.setImageResource(R.drawable.checked);
            else imgEquipped.setImageResource(R.drawable.xicon);

            itemProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newIngredient = shopping;
                    itemProduct.setBackgroundColor(Color.parseColor("#804089FF"));
                }
            });
        }
    }
}

