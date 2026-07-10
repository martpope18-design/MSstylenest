package com.example.msstylenest.ui;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msstylenest.R;
import com.example.msstylenest.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products = new ArrayList<>();
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvCategory, tvPrice, tvOriginalPrice, tvOutOfStock;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvOriginalPrice = itemView.findViewById(R.id.tv_original_price);
            tvOutOfStock = itemView.findViewById(R.id.tv_out_of_stock);
        }

        public void bind(final Product product, final OnProductClickListener listener) {
            tvName.setText(product.getName());
            tvCategory.setText(product.getCategory());
            
            if (product.getOfferPrice() > 0) {
                tvPrice.setText(String.format("$%.2f", product.getOfferPrice()));
                tvOriginalPrice.setText(String.format("$%.2f", product.getPrice()));
                tvOriginalPrice.setVisibility(View.VISIBLE);
                tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                tvPrice.setText(String.format("$%.2f", product.getPrice()));
                tvOriginalPrice.setVisibility(View.GONE);
            }

            if (product.isOutOfStock()) {
                tvOutOfStock.setVisibility(View.VISIBLE);
                itemView.setAlpha(0.6f);
            } else {
                tvOutOfStock.setVisibility(View.GONE);
                itemView.setAlpha(1.0f);
            }

            Glide.with(itemView.getContext())
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.ic_gallery)
                    .error(R.drawable.ic_error)
                    .into(ivProduct);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
        }
    }
}