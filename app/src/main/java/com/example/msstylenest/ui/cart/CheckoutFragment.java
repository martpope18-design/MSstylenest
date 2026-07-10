package com.example.msstylenest.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.msstylenest.R;
import com.example.msstylenest.database.AppDatabase;
import com.example.msstylenest.model.CartItem;
import com.example.msstylenest.model.Order;

import java.util.List;

public class CheckoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_checkout, container, false);

        root.findViewById(R.id.btn_place_order).setOnClickListener(v -> placeOrder(root));

        return root;
    }

    private void placeOrder(View root) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        db.cartDao().getAllCartItems().observe(getViewLifecycleOwner(), cartItems -> {
            if (cartItems != null && !cartItems.isEmpty()) {
                double total = 0;
                StringBuilder summary = new StringBuilder();
                summary.append(cartItems.size()).append(" items: ");
                
                for (int i = 0; i < cartItems.size(); i++) {
                    CartItem item = cartItems.get(i);
                    total += item.getPrice() * item.getQuantity();
                    summary.append(item.getProductName());
                    if (i < cartItems.size() - 1) summary.append(", ");
                }

                Order order = new Order(System.currentTimeMillis(), total, "Processing", summary.toString());

                AppDatabase.databaseWriteExecutor.execute(() -> {
                    db.orderDao().insert(order);
                    db.cartDao().clearCart();
                    
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Order Placed Successfully!", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(root).navigate(R.id.navigation_home);
                        });
                    }
                });
            }
        });
    }
}