package com.example.parcial_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parcial_1.data.CartStore;
import com.example.parcial_1.model.CartItem;
import com.example.parcial_1.model.Category;
import com.example.parcial_1.model.Product;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * MainActivity - Catálogo + Carrito (resumen arriba)
 */
public class MainActivity extends AppCompatActivity {

    private LinearLayout llCatalogContainer;
    private TextView tvTotal;
    private TextView tvCartCount;

    private MaterialButton btnAll, btnPrint, btnBinding;
    private MaterialButton btnClearCart, btnViewCart;

    private final List<Product> allProducts = new ArrayList<>();
    private LayoutInflater inflater;
    private final NumberFormat ars = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.appToolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setLogo(R.drawable.logo_lamontana);
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                getSupportActionBar().setTitle("La Montaña");
                getSupportActionBar().setSubtitle("Impresiones");
            }
        }

        inflater = LayoutInflater.from(this);
        llCatalogContainer = findViewById(R.id.llCatalogContainer);
        tvTotal = findViewById(R.id.tvTotal);
        tvCartCount = findViewById(R.id.tvCartCount);

        btnAll = findViewById(R.id.btnFilterAll);
        btnPrint = findViewById(R.id.btnFilterPrint);
        btnBinding = findViewById(R.id.btnFilterBinding);
        btnClearCart = findViewById(R.id.btnClearCart);
        btnViewCart = findViewById(R.id.btnViewCart);

        seedMockData();
        renderCatalog(allProducts);

        btnAll.setOnClickListener(v -> renderCatalog(allProducts));
        btnPrint.setOnClickListener(v -> {
            List<Product> filtered = new ArrayList<>();
            for (Product p : allProducts) if (p.category == Category.PRINT) filtered.add(p);
            renderCatalog(filtered);
        });
        btnBinding.setOnClickListener(v -> {
            List<Product> filtered = new ArrayList<>();
            for (Product p : allProducts) if (p.category == Category.BINDING) filtered.add(p);
            renderCatalog(filtered);
        });

        btnClearCart.setOnClickListener(v -> {
            CartStore.get().clear();
            updateCartUi();
        });

        btnViewCart.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CartActivity.class))
        );

        updateCartUi();
    }

    private void seedMockData() {
        allProducts.add(new Product(
                "Impresión B/N", "Cara simple · A4", 100,
                Category.PRINT, R.drawable.sample_print_bw, /*copyBased*/ true
        ));
        allProducts.add(new Product(
                "Impresión color", "Doble cara · A4", 200,
                Category.PRINT, R.drawable.sample_print_color, /*copyBased*/ true
        ));
        allProducts.add(new Product(
                "Anillado A4", "Tapa plástica + contratapa", 800,
                Category.BINDING, R.drawable.sample_binding, /*copyBased*/ false
        ));
    }

    private void renderCatalog(List<Product> list) {
        llCatalogContainer.removeAllViews();
        for (Product p : list) {
            View item = inflater.inflate(R.layout.item_catalog, llCatalogContainer, false);

            ImageView iv = item.findViewById(R.id.ivThumb);
            TextView tvName = item.findViewById(R.id.tvName);
            TextView tvDesc = item.findViewById(R.id.tvDesc);
            TextView tvPrice = item.findViewById(R.id.tvPrice);
            MaterialButton btnAdd = item.findViewById(R.id.btnAdd);

            iv.setImageResource(p.imageRes);
            tvName.setText(p.name);
            tvDesc.setText(p.desc);
            tvPrice.setText(ars.format(p.price));

            btnAdd.setOnClickListener(v -> {
                CartStore.get().add(p);
                updateCartUi();
            });

            llCatalogContainer.addView(item);
        }
    }

    private void updateCartUi() {
        int items = CartStore.get().getTotalQty();
        int total = CartStore.get().getTotalAmount();
        tvCartCount.setText(getString(R.string.cart_items_format, items));
        tvTotal.setText(ars.format(total));
    }
}
