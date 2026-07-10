package com.example.msstylenest.ui.admin;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.msstylenest.R;
import com.example.msstylenest.database.AppDatabase;
import com.example.msstylenest.model.Category;
import com.example.msstylenest.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AdminFragment extends Fragment {

    private TextInputEditText etName, etCategory, etPrice, etOfferPrice, etSizes, etDescription;
    private ImageView ivPreview, ivCategoryPreview;
    private SwitchMaterial switchOutOfStock;
    private MaterialButton btnPickImage, btnSave, btnManage, btnPickCategoryImage, btnUpdateCategory;
    private Spinner spinnerCategories;
    private TextView tvTitle;
    private Uri selectedProductImageUri;
    private Uri selectedCategoryImageUri;
    private int productId = -1;

    private final ActivityResultLauncher<String> mGetProductContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedProductImageUri = uri;
                    Glide.with(this).load(uri).into(ivPreview);
                }
            });

    private final ActivityResultLauncher<String> mGetCategoryContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedCategoryImageUri = uri;
                    Glide.with(this).load(uri).into(ivCategoryPreview);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin, container, false);

        tvTitle = root.findViewById(R.id.tv_admin_title);
        TextView subTitle = root.findViewById(R.id.tv_form_subtitle);

        etName = root.findViewById(R.id.et_name);
        etCategory = root.findViewById(R.id.et_category);
        etPrice = root.findViewById(R.id.et_price);
        etOfferPrice = root.findViewById(R.id.et_offer_price);
        etSizes = root.findViewById(R.id.et_sizes);
        etDescription = root.findViewById(R.id.et_description);
        ivPreview = root.findViewById(R.id.iv_preview);
        switchOutOfStock = root.findViewById(R.id.switch_out_of_stock);
        btnPickImage = root.findViewById(R.id.btn_pick_image);
        btnSave = root.findViewById(R.id.btn_save_product);
        btnManage = root.findViewById(R.id.btn_go_to_manage);

        // Category UI
        spinnerCategories = root.findViewById(R.id.spinner_categories);
        ivCategoryPreview = root.findViewById(R.id.iv_category_preview);
        btnPickCategoryImage = root.findViewById(R.id.btn_pick_category_image);
        btnUpdateCategory = root.findViewById(R.id.btn_update_category);

        loadCategories();

        if (getArguments() != null) {
            productId = getArguments().getInt("productId", -1);
        }

        if (productId != -1) {
            btnSave.setText("UPDATE PRODUCT");
            btnManage.setVisibility(View.GONE);
            if (subTitle != null) subTitle.setText("Update Product");
            
            AppDatabase.getDatabase(getContext()).productDao().getProductById(productId).observe(getViewLifecycleOwner(), product -> {
                if (product != null) {
                    populateFields(product);
                }
            });
        }

        btnPickImage.setOnClickListener(v -> mGetProductContent.launch("image/*"));
        btnSave.setOnClickListener(v -> saveProduct());
        btnManage.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_manage_products));

        btnPickCategoryImage.setOnClickListener(v -> mGetCategoryContent.launch("image/*"));
        btnUpdateCategory.setOnClickListener(v -> updateCategoryCover());

        return root;
    }

    private void loadCategories() {
        AppDatabase.getDatabase(getContext()).categoryDao().getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                List<String> categoryNames = new ArrayList<>();
                for (Category c : categories) {
                    categoryNames.add(c.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategories.setAdapter(adapter);
            }
        });
    }

    private void updateCategoryCover() {
        if (spinnerCategories.getSelectedItem() == null) {
            Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedCategoryImageUri == null) {
            Toast.makeText(getContext(), "Please pick an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String categoryName = spinnerCategories.getSelectedItem().toString();
        String imageUrl = selectedCategoryImageUri.toString();

        AppDatabase.databaseWriteExecutor.execute(() -> {
            Category category = new Category(categoryName, imageUrl);
            AppDatabase.getDatabase(getContext()).categoryDao().insert(category);
            
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Category cover updated", Toast.LENGTH_SHORT).show();
                    ivCategoryPreview.setImageResource(0);
                    selectedCategoryImageUri = null;
                });
            }
        });
    }

    private void populateFields(Product product) {
        etName.setText(product.getName());
        etCategory.setText(product.getCategory());
        etPrice.setText(String.valueOf(product.getPrice()));
        etOfferPrice.setText(String.valueOf(product.getOfferPrice()));
        etSizes.setText(product.getSizes());
        etDescription.setText(product.getDescription());
        switchOutOfStock.setChecked(product.isOutOfStock());
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            selectedProductImageUri = Uri.parse(product.getImageUrl());
            Glide.with(this).load(selectedProductImageUri).into(ivPreview);
        }
    }

    private void saveProduct() {
        String name = etName.getText().toString();
        String category = etCategory.getText().toString();
        String priceStr = etPrice.getText().toString();
        String offerPriceStr = etOfferPrice.getText().toString();
        String sizes = etSizes.getText().toString();
        String description = etDescription.getText().toString();

        if (name.isEmpty() || category.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            double offerPrice = offerPriceStr.isEmpty() ? 0 : Double.parseDouble(offerPriceStr);
            String imageUrl = selectedProductImageUri != null ? selectedProductImageUri.toString() : "";

            AppDatabase.databaseWriteExecutor.execute(() -> {
                if (productId == -1) {
                    Product product = new Product(name, description, price, imageUrl, category, sizes);
                    product.setOfferPrice(offerPrice);
                    product.setOutOfStock(switchOutOfStock.isChecked());
                    AppDatabase.getDatabase(getContext()).productDao().insert(product);
                } else {
                    Product product = new Product(name, description, price, imageUrl, category, sizes);
                    product.setId(productId);
                    product.setOfferPrice(offerPrice);
                    product.setOutOfStock(switchOutOfStock.isChecked());
                    AppDatabase.getDatabase(getContext()).productDao().update(product);
                }

                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), productId == -1 ? "Product Saved" : "Product Updated", Toast.LENGTH_SHORT).show();
                        if (productId != -1) {
                            Navigation.findNavController(getView()).popBackStack();
                        } else {
                            clearFields();
                        }
                    });
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etName.setText("");
        etCategory.setText("");
        etPrice.setText("");
        etOfferPrice.setText("");
        etDescription.setText("");
        ivPreview.setImageResource(0);
        selectedProductImageUri = null;
        switchOutOfStock.setChecked(false);
    }
}