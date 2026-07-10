package com.example.msstylenest.ui.admin;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.msstylenest.R;
import com.example.msstylenest.database.AppDatabase;
import com.example.msstylenest.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ManageProductsFragment extends Fragment {

    private RecyclerView rvManage;
    private ManageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manage_products, container, false);

        rvManage = root.findViewById(R.id.rv_manage_products);
        rvManage.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ManageAdapter();
        rvManage.setAdapter(adapter);

        AppDatabase.getDatabase(getContext()).productDao().getAllProducts().observe(getViewLifecycleOwner(), products -> {
            adapter.setProducts(products);
        });

        return root;
    }

    private class ManageAdapter extends RecyclerView.Adapter<ManageAdapter.ManageViewHolder> {
        private List<Product> products = new ArrayList<>();

        public void setProducts(List<Product> products) {
            this.products = products;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ManageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_product, parent, false);
            return new ManageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ManageViewHolder holder, int position) {
            Product product = products.get(position);
            holder.tvName.setText(product.getName());
            holder.tvCategory.setText(product.getCategory());
            
            Glide.with(holder.itemView.getContext())
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.ic_gallery)
                    .into(holder.ivProduct);

            holder.btnDelete.setOnClickListener(v -> {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    AppDatabase.getDatabase(getContext()).productDao().delete(product);
                });
                Toast.makeText(getContext(), "Product Deleted", Toast.LENGTH_SHORT).show();
            });

            holder.btnEdit.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt("productId", product.getId());
                Navigation.findNavController(v).navigate(R.id.navigation_admin, bundle);
            });
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        class ManageViewHolder extends RecyclerView.ViewHolder {
            ImageView ivProduct;
            TextView tvName, tvCategory;
            View btnEdit, btnDelete;

            public ManageViewHolder(@NonNull View itemView) {
                super(itemView);
                ivProduct = itemView.findViewById(R.id.iv_manage_product);
                tvName = itemView.findViewById(R.id.tv_manage_name);
                tvCategory = itemView.findViewById(R.id.tv_manage_category);
                btnEdit = itemView.findViewById(R.id.btn_manage_edit);
                btnDelete = itemView.findViewById(R.id.btn_manage_delete);
            }
        }
    }
}