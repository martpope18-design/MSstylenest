package com.example.msstylenest.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msstylenest.R;
import com.example.msstylenest.database.AppDatabase;
import com.example.msstylenest.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryFragment extends Fragment {

    private RecyclerView rvOrders;
    private TextView tvNoOrders;
    private OrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order_history, container, false);

        rvOrders = root.findViewById(R.id.rv_orders);
        tvNoOrders = root.findViewById(R.id.tv_no_orders);

        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter();
        rvOrders.setAdapter(adapter);

        AppDatabase.getDatabase(getContext()).orderDao().getAllOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders == null || orders.isEmpty()) {
                tvNoOrders.setVisibility(View.VISIBLE);
                rvOrders.setVisibility(View.GONE);
            } else {
                tvNoOrders.setVisibility(View.GONE);
                rvOrders.setVisibility(View.VISIBLE);
                adapter.setOrders(orders);
            }
        });

        return root;
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
        private List<Order> orders = new ArrayList<>();

        public void setOrders(List<Order> orders) {
            this.orders = orders;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
            return new OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            Order order = orders.get(position);
            holder.tvId.setText("Order #" + order.getId());
            holder.tvAmount.setText(String.format("$%.2f", order.getTotalAmount()));
            holder.tvStatus.setText(order.getStatus());
            holder.tvSummary.setText(order.getItemsSummary());

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            holder.tvDate.setText(sdf.format(new Date(order.getTimestamp())));
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        class OrderViewHolder extends RecyclerView.ViewHolder {
            TextView tvId, tvDate, tvAmount, tvStatus, tvSummary;

            public OrderViewHolder(@NonNull View itemView) {
                super(itemView);
                tvId = itemView.findViewById(R.id.tv_order_id);
                tvDate = itemView.findViewById(R.id.tv_order_date);
                tvAmount = itemView.findViewById(R.id.tv_order_amount);
                tvStatus = itemView.findViewById(R.id.tv_order_status);
                tvSummary = itemView.findViewById(R.id.tv_order_summary);
            }
        }
    }
}