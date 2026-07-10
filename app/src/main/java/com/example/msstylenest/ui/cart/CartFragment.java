package com.example.msstylenest.ui.cart;

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
import com.example.msstylenest.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView rvCart;
    private TextView tvTotal;
    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCart = root.findViewById(R.id.rv_cart);
        tvTotal = root.findViewById(R.id.tv_total_price);
        
        root.findViewById(R.id.btn_checkout).setOnClickListener(v -> {
            if (adapter.getItemCount() > 0) {
                Navigation.findNavController(root).navigate(R.id.navigation_checkout);
            } else {
                Toast.makeText(getContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });

        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter();
        rvCart.setAdapter(adapter);

        AppDatabase.getDatabase(getContext()).cartDao().getAllCartItems().observe(getViewLifecycleOwner(), cartItems -> {
            adapter.setCartItems(cartItems);
            updateTotal(cartItems);
        });

        return root;
    }

    private void updateTotal(List<CartItem> items) {
        double total = 0;
        for (CartItem item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        tvTotal.setText(String.format("$%.2f", total));
    }

    private class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
        private List<CartItem> items = new ArrayList<>();

        public void setCartItems(List<CartItem> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
            return new CartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
            CartItem item = items.get(position);
            holder.tvName.setText(item.getProductName());
            holder.tvPrice.setText(String.format("$%.2f", item.getPrice()));
            holder.tvSize.setText("Size: " + item.getSelectedSize());
            holder.tvQuantity.setText("Qty: " + item.getQuantity());
            
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_gallery)
                    .error(R.drawable.ic_error)
                    .into(holder.ivProduct);

            holder.btnDelete.setOnClickListener(v -> {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    AppDatabase.getDatabase(getContext()).cartDao().delete(item);
                });
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class CartViewHolder extends RecyclerView.ViewHolder {
            ImageView ivProduct, btnDelete;
            TextView tvName, tvPrice, tvSize, tvQuantity;

            public CartViewHolder(@NonNull View itemView) {
                super(itemView);
                ivProduct = itemView.findViewById(R.id.iv_cart_product);
                tvName = itemView.findViewById(R.id.tv_cart_name);
                tvPrice = itemView.findViewById(R.id.tv_cart_price);
                tvSize = itemView.findViewById(R.id.tv_cart_size);
                tvQuantity = itemView.findViewById(R.id.tv_cart_quantity);
                btnDelete = itemView.findViewById(R.id.btn_cart_delete);
            }
        }
    }
}