package com.example.msstylenest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.msstylenest.database.AppDatabase;
import com.example.msstylenest.model.Category;
import com.example.msstylenest.model.Product;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView ivBackground = findViewById(R.id.iv_splash_bg);
        TextView tvTitle = findViewById(R.id.tv_splash_title);

        String bgUrl = "https://images.pexels.com/photos/298863/pexels-photo-298863.jpeg";
        Glide.with(this)
                .load(bgUrl)
                .centerCrop()
                .into(ivBackground);

        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeIn.setDuration(1500);
        tvTitle.startAnimation(fadeIn);

        AppDatabase db = AppDatabase.getDatabase(this);
        SharedPreferences prefs = getSharedPreferences("MSStyleNestPrefs", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("is_first_run", true);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Ensure Categories exist (crucial for supporting custom covers)
            List<Category> categories = db.categoryDao().getAllCategoriesSync();
            if (categories == null || categories.isEmpty()) {
                db.categoryDao().insert(new Category("Shirts", "https://images.pexels.com/photos/1232459/pexels-photo-1232459.jpeg"));
                db.categoryDao().insert(new Category("Trousers", "https://images.pexels.com/photos/1598507/pexels-photo-1598507.jpeg"));
                db.categoryDao().insert(new Category("Dresses", "https://images.pexels.com/photos/985635/pexels-photo-985635.jpeg"));
                db.categoryDao().insert(new Category("Shoes", "https://images.pexels.com/photos/1456706/pexels-photo-1456706.jpeg"));
                db.categoryDao().insert(new Category("Accessories", "https://images.pexels.com/photos/277390/pexels-photo-277390.jpeg"));
            }

            // Populate initial Products only on first run
            if (isFirstRun) {
                if (db.productDao().getProductCount() == 0) {
                    // SHIRTS (11 Items)
                    db.productDao().insert(new Product("Urban Black Hoodie", "Premium cotton hoodie for daily wear.", 45.00, "https://images.pexels.com/photos/1183266/pexels-photo-1183266.jpeg", "Shirts", "S,M,L,XL"));
                    db.productDao().insert(new Product("White Linen Shirt", "Breathable linen shirt for summer.", 35.00, "https://images.pexels.com/photos/1043474/pexels-photo-1043474.jpeg", "Shirts", "S,M,L,XL"));
                    db.productDao().insert(new Product("Checked Flannel Shirt", "Classic lumberjack style flannel.", 40.00, "https://images.pexels.com/photos/1018911/pexels-photo-1018911.jpeg", "Shirts", "M,L,XL"));
                    db.productDao().insert(new Product("Graphic Tee - Neon", "Retro graphic t-shirt.", 25.00, "https://images.pexels.com/photos/2294342/pexels-photo-2294342.jpeg", "Shirts", "S,M,L"));
                    db.productDao().insert(new Product("Denim Shirt - Vintage", "A timeless denim shirt.", 50.00, "https://images.pexels.com/photos/4066293/pexels-photo-4066293.jpeg", "Shirts", "S,M,L,XL"));
                    db.productDao().insert(new Product("Slim Fit Polo - Navy", "Elegant polo for casual Fridays.", 30.00, "https://images.pexels.com/photos/1232459/pexels-photo-1232459.jpeg", "Shirts", "S,M,L"));
                    db.productDao().insert(new Product("Oversized Beige Tee", "Trendy oversized fit.", 22.00, "https://images.pexels.com/photos/3758105/pexels-photo-3758105.jpeg", "Shirts", "M,L,XL"));
                    db.productDao().insert(new Product("Striped Formal Shirt", "Sharp look for the office.", 45.00, "https://images.pexels.com/photos/1124468/pexels-photo-1124468.jpeg", "Shirts", "S,M,L,XL"));
                    db.productDao().insert(new Product("Silk Blouse - Emerald", "Luxurious silk blouse.", 65.00, "https://images.pexels.com/photos/4609386/pexels-photo-4609386.jpeg", "Shirts", "S,M,L"));
                    db.productDao().insert(new Product("Pocket Tee - Charcoal", "Essential cotton pocket tee.", 20.00, "https://images.pexels.com/photos/1656684/pexels-photo-1656684.jpeg", "Shirts", "S,M,L,XL"));
                    db.productDao().insert(new Product("Oxford Button-Down", "Classic Oxford shirt in blue.", 42.00, "https://images.pexels.com/photos/4946399/pexels-photo-4946399.jpeg", "Shirts", "S,M,L,XL"));

                    // TROUSERS (11 Items)
                    db.productDao().insert(new Product("Classic Denim Trousers", "Durable and stylish slim-fit denim.", 60.00, "https://images.pexels.com/photos/1598507/pexels-photo-1598507.jpeg", "Trousers", "30,32,34,36"));
                    db.productDao().insert(new Product("Cargo Pants - Olive", "Utility pants with multiple pockets.", 55.00, "https://images.pexels.com/photos/1892490/pexels-photo-1892490.jpeg", "Trousers", "32,34,36"));
                    db.productDao().insert(new Product("Slim Fit Chinos - Khaki", "Versatile chinos for any occasion.", 45.00, "https://images.pexels.com/photos/3540809/pexels-photo-3540809.jpeg", "Trousers", "30,32,34"));
                    db.productDao().insert(new Product("Black Skinny Jeans", "Essential black jeans for every wardrobe.", 50.00, "https://images.pexels.com/photos/1082529/pexels-photo-1082529.jpeg", "Trousers", "28,30,32"));
                    db.productDao().insert(new Product("Joggers - Tech Fleece", "Comfortable joggers for training.", 40.00, "https://images.pexels.com/photos/1183266/pexels-photo-1183266.jpeg", "Trousers", "S,M,L"));
                    db.productDao().insert(new Product("Formal Slacks - Grey", "Tailored slacks for formal events.", 70.00, "https://images.pexels.com/photos/3760613/pexels-photo-3760613.jpeg", "Trousers", "32,34,36"));
                    db.productDao().insert(new Product("Distressed Denim", "Rugged look with distressed details.", 65.00, "https://images.pexels.com/photos/1082528/pexels-photo-1082528.jpeg", "Trousers", "30,32,34"));
                    db.productDao().insert(new Product("Corduroy Trousers", "Vintage style corduroy pants.", 48.00, "https://images.pexels.com/photos/1144834/pexels-photo-1144834.jpeg", "Trousers", "30,32,34"));
                    db.productDao().insert(new Product("Linen Beach Pants", "Relaxed fit linen trousers.", 40.00, "https://images.pexels.com/photos/1598507/pexels-photo-1598507.jpeg", "Trousers", "S,M,L"));
                    db.productDao().insert(new Product("Wide Leg Culottes", "Trendy wide leg trousers.", 55.00, "https://images.pexels.com/photos/1082528/pexels-photo-1082528.jpeg", "Trousers", "S,M,L"));
                    db.productDao().insert(new Product("Biker Jeans - Black", "Edgy biker style jeans.", 75.00, "https://images.pexels.com/photos/1082529/pexels-photo-1082529.jpeg", "Trousers", "30,32,34"));

                    // DRESSES (11 Items)
                    db.productDao().insert(new Product("Floral Summer Dress", "Light and airy dress for warm days.", 55.00, "https://images.pexels.com/photos/1055691/pexels-photo-1055691.jpeg", "Dresses", "S,M,L"));
                    db.productDao().insert(new Product("Little Black Dress", "Essential for any evening event.", 80.00, "https://images.pexels.com/photos/985635/pexels-photo-985635.jpeg", "Dresses", "S,M,L"));
                    db.productDao().insert(new Product("Maxi Silk Gown", "Elegant gown for red carpet looks.", 150.00, "https://images.pexels.com/photos/1488507/pexels-photo-1488507.jpeg", "Dresses", "S,M,L"));
                    db.productDao().insert(new Product("Polka Dot Wrap Dress", "Classic wrap dress with polka dots.", 60.00, "https://images.pexels.com/photos/1382734/pexels-photo-1382734.jpeg", "Dresses", "S,M,L"));
                    db.productDao().insert(new Product("Bohemian Midi Dress", "Free-spirited boho style.", 45.00, "https://images.pexels.com/photos/1055691/pexels-photo-1055691.jpeg", "Dresses", "S,M,L"));
                    db.productDao().insert(new Product("Velvet Evening Dress", "Rich velvet for a sophisticated look.", 90.00, "https://images.pexels.com/photos/157675/fashion-men-s-individuality-black-and-white-157675.jpeg", "Dresses", "S,M,L"));
                    db.productDao().insert(new Product("Sundress - Sunflower", "Bright and cheery summer dress.", 35.00, "https://images.pexels.com/photos/1055691/pexels-photo-1055691.jpeg", "Dresses", "S,M"));
                    db.productDao().insert(new Product("Bodycon Cocktail Dress", "Hugs the curves for a bold statement.", 70.00, "https://images.pexels.com/photos/985635/pexels-photo-985635.jpeg", "Dresses", "XS,S,M"));
                    db.productDao().insert(new Product("Denim Overall Dress", "Casual denim dress for everyday.", 40.00, "https://images.pexels.com/photos/1055691/pexels-photo-1055691.jpeg", "Dresses", "S,M,L"));
                    db.productDao().insert(new Product("Lace Wedding Guest Dress", "Beautiful lace detail for special occasions.", 110.00, "https://images.pexels.com/photos/1488507/pexels-photo-1488507.jpeg", "Dresses", "S,M,L"));
                    db.productDao().insert(new Product("Shift Dress - Geometric", "Modern geometric pattern shift dress.", 50.00, "https://images.pexels.com/photos/1382734/pexels-photo-1382734.jpeg", "Dresses", "S,M,L"));

                    // SHOES (11 Items)
                    db.productDao().insert(new Product("White Urban Sneakers", "Comfortable city walking shoes.", 80.00, "https://images.pexels.com/photos/1456706/pexels-photo-1456706.jpeg", "Shoes", "7,8,9,10,11"));
                    db.productDao().insert(new Product("Leather Chelsea Boots", "Classic leather boots for any outfit.", 120.00, "https://images.pexels.com/photos/92733/pexels-photo-92733.jpeg", "Shoes", "8,9,10,11"));
                    db.productDao().insert(new Product("High-Top Canvas Shoes", "Iconic high-top style.", 60.00, "https://images.pexels.com/photos/1598505/pexels-photo-1598505.jpeg", "Shoes", "7,8,9,10"));
                    db.productDao().insert(new Product("Formal Loafers - Brown", "Smart loafers in genuine leather.", 110.00, "https://images.pexels.com/photos/298863/pexels-photo-298863.jpeg", "Shoes", "9,10,11"));
                    db.productDao().insert(new Product("Suede Desert Boots", "Casual suede boots.", 95.00, "https://images.pexels.com/photos/1159670/pexels-photo-1159670.jpeg", "Shoes", "8,9,10"));
                    db.productDao().insert(new Product("Running Trainers - Volt", "High-performance running shoes.", 130.00, "https://images.pexels.com/photos/2529148/pexels-photo-2529148.jpeg", "Shoes", "8,9,10,11"));
                    db.productDao().insert(new Product("Strappy Sandals - Gold", "Elegant sandals for evening wear.", 75.00, "https://images.pexels.com/photos/1456706/pexels-photo-1456706.jpeg", "Shoes", "6,7,8"));
                    db.productDao().insert(new Product("Platform Combat Boots", "Edgy platform boots.", 140.00, "https://images.pexels.com/photos/92733/pexels-photo-92733.jpeg", "Shoes", "7,8,9,10"));
                    db.productDao().insert(new Product("Slip-on Vans Style", "Classic canvas slip-ons.", 55.00, "https://images.pexels.com/photos/1598505/pexels-photo-1598505.jpeg", "Shoes", "7,8,9,10"));
                    db.productDao().insert(new Product("Classic Brogues", "Traditional leather brogues.", 115.00, "https://images.pexels.com/photos/298863/pexels-photo-298863.jpeg", "Shoes", "9,10,11"));
                    db.productDao().insert(new Product("Espadrilles - Summer", "Relaxed summer espadrilles.", 45.00, "https://images.pexels.com/photos/1159670/pexels-photo-1159670.jpeg", "Shoes", "7,8,9"));

                    // ACCESSORIES (11 Items)
                    db.productDao().insert(new Product("Gold Accent Watch", "Elegant gold-plated watch for special occasions.", 120.00, "https://images.pexels.com/photos/277390/pexels-photo-277390.jpeg", "Accessories", "One Size"));
                    db.productDao().insert(new Product("Leather Messenger Bag", "Spacious and durable leather bag.", 150.00, "https://images.pexels.com/photos/1152077/pexels-photo-1152077.jpeg", "Accessories", "One Size"));
                    db.productDao().insert(new Product("Aviator Sunglasses", "Classic shades for a cool look.", 85.00, "https://images.pexels.com/photos/46710/pexels-photo-46710.jpeg", "Accessories", "One Size"));
                    db.productDao().insert(new Product("Silver Chain Necklace", "Minimalist silver chain.", 45.00, "https://images.pexels.com/photos/2850487/pexels-photo-2850487.jpeg", "Accessories", "One Size"));
                    db.productDao().insert(new Product("Wool Fedora Hat", "Stylish hat for a sophisticated touch.", 55.00, "https://images.pexels.com/photos/1018911/pexels-photo-1018911.jpeg", "Accessories", "M,L"));
                    db.productDao().insert(new Product("Leather Belt - Tan", "High-quality leather belt.", 30.00, "https://images.pexels.com/photos/1152077/pexels-photo-1152077.jpeg", "Accessories", "32,34,36"));
                    db.productDao().insert(new Product("Silk Scarf - Patterned", "Elegant silk scarf.", 40.00, "https://images.pexels.com/photos/4609386/pexels-photo-4609386.jpeg", "Accessories", "One Size"));
                    db.productDao().insert(new Product("Beanie - Mustard", "Cozy knit beanie.", 20.00, "https://images.pexels.com/photos/1183266/pexels-photo-1183266.jpeg", "Accessories", "One Size"));
                    db.productDao().insert(new Product("Minimalist Wallet", "Slim leather wallet.", 35.00, "https://images.pexels.com/photos/1152077/pexels-photo-1152077.jpeg", "Accessories", "One Size"));
                    db.productDao().insert(new Product("Hoop Earrings - Gold", "Timeless gold hoops.", 25.00, "https://images.pexels.com/photos/2850487/pexels-photo-2850487.jpeg", "Accessories", "One Size"));
                    db.productDao().insert(new Product("Canvas Backpack", "Durable backpack for travel.", 65.00, "https://images.pexels.com/photos/1152077/pexels-photo-1152077.jpeg", "Accessories", "One Size"));
                }
                prefs.edit().putBoolean("is_first_run", false).apply();
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 3000);
    }
}