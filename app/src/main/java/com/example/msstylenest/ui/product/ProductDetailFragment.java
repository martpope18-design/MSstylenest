package com.example.msstylenest.ui.product;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.msstylenest.R;
import com.example.msstylenest.database.AppDatabase;
import com.example.msstylenest.model.CartItem;
import com.example.msstylenest.model.Product;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class ProductDetailFragment extends Fragment {

    private ImageView ivProduct;
    private TextView tvName, tvCategory, tvPrice, tvOriginalPrice, tvDescription;
    private ChipGroup cgSizes;
    private View btnAddToCart;
    private Product currentProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_detail, container, false);

        ivProduct = root.findViewById(R.id.iv_product_detail);
        tvName = root.findViewById(R.id.tv_detail_name);
        tvCategory = root.findViewById(R.id.tv_detail_category);
        tvPrice = root.findViewById(R.id.tv_detail_price);
        tvOriginalPrice = root.findViewById(R.id.tv_detail_original_price);
        tvDescription = root.findViewById(R.id.tv_detail_description);
        cgSizes = root.findViewById(R.id.cg_sizes);
        btnAddToCart = root.findViewById(R.id.btn_add_to_cart);

        int productId = getArguments() != null ? getArguments().getInt("productId") : -1;

        if (productId != -1) {
            AppDatabase.getDatabase(getContext()).productDao().getProductById(productId).observe(getViewLifecycleOwner(), product -> {
                if (product != null) {
                    currentProduct = product;
                    displayProduct(product);
                }
            });
        }

        btnAddToCart.setOnClickListener(v -> addToCart());

        return root;
    }

    private void displayProduct(Product product) {
        tvName.setText(product.getName());
        tvCategory.setText(product.getCategory());
        tvDescription.setText(product.getDescription());

        double displayPrice = product.getOfferPrice() > 0 ? product.getOfferPrice() : product.getPrice();
        tvPrice.setText(String.format("$%.2f", displayPrice));

        if (product.getOfferPrice() > 0) {
            tvOriginalPrice.setText(String.format("$%.2f", product.getPrice()));
            tvOriginalPrice.setVisibility(View.VISIBLE);
            tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvOriginalPrice.setVisibility(View.GONE);
        }

        Glide.with(this).load(product.getImageUrl()).placeholder(R.drawable.ic_gallery).into(ivProduct);

        cgSizes.removeAllViews();
        if (product.getSizes() != null && !product.getSizes().isEmpty()) {
            String[] sizes = product.getSizes().split(",");
            for (String size : sizes) {
                Chip chip = new Chip(getContext());
                chip.setText(size.trim());
                chip.setCheckable(true);
                cgSizes.addView(chip);
            }
        }

        if (product.isOutOfStock()) {
            btnAddToCart.setEnabled(false);
            ((TextView)btnAddToCart).setText("OUT OF STOCK");
        }
    }

    private void addToCart() {
        if (currentProduct == null) return;

        int selectedChipId = cgSizes.getCheckedChipId();
        if (selectedChipId == View.NO_ID && cgSizes.getChildCount() > 0) {
            Toast.makeText(getContext(), "Please select a size", Toast.LENGTH_SHORT).show();
            return;
        }

        String size = "";
        if (selectedChipId != View.NO_ID) {
            Chip chip = cgSizes.findViewById(selectedChipId);
            size = chip.getText().toString();
        }

        double price = currentProduct.getOfferPrice() > 0 ? currentProduct.getOfferPrice() : currentProduct.getPrice();
        CartItem cartItem = new CartItem(currentProduct.getId(), currentProduct.getName(), price, currentProduct.getImageUrl(), size, 1);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase.getDatabase(getContext()).cartDao().insert(cartItem);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Added to cart!", Toast.LENGTH_SHORT).show());
            }
        });
    }
}