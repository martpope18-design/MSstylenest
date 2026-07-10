package com.example.msstylenest.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msstylenest.R;
import com.example.msstylenest.database.AppDatabase;
import com.example.msstylenest.ui.ProductAdapter;

public class CategoryProductsFragment extends Fragment {

    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private String categoryName;
    private TextView tvTitle;
    private ImageView ivBanner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_list, container, false);

        categoryName = getArguments() != null ? getArguments().getString("categoryName") : "Products";
        
        tvTitle = root.findViewById(R.id.tv_category_title);
        ivBanner = root.findViewById(R.id.iv_category_banner);
        
        tvTitle.setText(categoryName);
        
        // Fetch and load the category banner image from database
        AppDatabase.getDatabase(getContext()).categoryDao().getCategoryByName(categoryName).observe(getViewLifecycleOwner(), category -> {
            if (category != null && category.getImageUrl() != null) {
                Glide.with(this)
                        .load(category.getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.ic_gallery)
                        .into(ivBanner);
            }
        });

        rvProducts = root.findViewById(R.id.rv_products);
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        adapter = new ProductAdapter(product -> {
            Bundle bundle = new Bundle();
            bundle.putInt("productId", product.getId());
            Navigation.findNavController(root).navigate(R.id.navigation_product_detail, bundle);
        });
        rvProducts.setAdapter(adapter);

        AppDatabase.getDatabase(getContext()).productDao().getProductsByCategory(categoryName).observe(getViewLifecycleOwner(), products -> {
            adapter.setProducts(products);
        });

        return root;
    }
}