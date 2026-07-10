package com.example.msstylenest.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msstylenest.R;
import com.example.msstylenest.database.AppDatabase;
import com.example.msstylenest.model.Product;
import com.example.msstylenest.ui.ProductAdapter;

public class HomeFragment extends Fragment {

    private RecyclerView rvFeatured;
    private ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        rvFeatured = root.findViewById(R.id.rv_featured);
        rvFeatured.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        adapter = new ProductAdapter(product -> {
            Bundle bundle = new Bundle();
            bundle.putInt("productId", product.getId());
            Navigation.findNavController(root).navigate(R.id.navigation_product_detail, bundle);
        });
        
        rvFeatured.setAdapter(adapter);

        AppDatabase.getDatabase(getContext()).productDao().getFeaturedProducts().observe(getViewLifecycleOwner(), products -> {
            adapter.setProducts(products);
        });

        return root;
    }
}