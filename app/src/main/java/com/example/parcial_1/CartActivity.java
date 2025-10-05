package com.example.parcial_1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.parcial_1.data.CartStore;
import com.example.parcial_1.model.CartItem;
import com.example.parcial_1.model.Product;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * CartActivity - lista los ítems del carrito con:
 * - precio unitario
 * - control de cantidad (+/- o detección desde PDF para "copias")
 * - botón Realizar pedido (simula redirección a pagos)
 * - botón para volver al catálogo
 */
public class CartActivity extends AppCompatActivity {

    private LinearLayout llCartListContainer;
    private TextView tvCartGrandTotal;
    private final NumberFormat ars = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.appToolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setTitle("Tu carrito");
        }

        llCartListContainer = findViewById(R.id.llCartListContainer);
        tvCartGrandTotal = findViewById(R.id.tvCartGrandTotal);

        MaterialButton btnBack = findViewById(R.id.btnBackToCatalog);
        btnBack.setOnClickListener(v -> finish()); // volver a catálogo

        renderCart();
    }

    private void renderCart() {
        llCartListContainer.removeAllViews();
        List<CartItem> items = CartStore.get().getItems();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (CartItem ci : items) {
            View row = inflater.inflate(R.layout.item_cart_detail, llCartListContainer, false);

            ImageView iv = row.findViewById(R.id.ivThumb);
            TextView tvName = row.findViewById(R.id.tvName);
            TextView tvDesc = row.findViewById(R.id.tvDesc);
            TextView tvUnitPrice = row.findViewById(R.id.tvUnitPrice);
            TextView tvQty = row.findViewById(R.id.tvQty);
            MaterialButton btnMinus = row.findViewById(R.id.btnMinus);
            MaterialButton btnPlus = row.findViewById(R.id.btnPlus);
            MaterialButton btnDetectFromPdf = row.findViewById(R.id.btnDetectFromPdf);
            MaterialButton btnPlaceOrder = row.findViewById(R.id.btnPlaceOrder);

            Product p = ci.product;

            iv.setImageResource(p.imageRes);
            tvName.setText(p.name);
            tvDesc.setText(p.desc);
            tvUnitPrice.setText(getString(R.string.unit_price_format, ars.format(p.price)));
            tvQty.setText(String.valueOf(ci.qty));

            // +/- cantidad
            btnMinus.setOnClickListener(v -> {
                CartStore.get().dec(p);
                // si qty queda en 0, re-render para quitar la fila
                renderCart();
                updateGrandTotal();
            });
            btnPlus.setOnClickListener(v -> {
                CartStore.get().inc(p);
                tvQty.setText(String.valueOf(CartStore.get().getItems().stream()
                        .filter(x -> x.product.name.equals(p.name))
                        .findFirst().orElse(ci).qty));
                updateGrandTotal();
            });

            // Solo para productos "de copias" mostramos "Detectar desde PDF"
            if (p.copyBased) {
                btnDetectFromPdf.setVisibility(View.VISIBLE);
                btnDetectFromPdf.setOnClickListener(v -> {
                    // Simulación: detectamos páginas de un PDF "Titulo.pdf"
                    String fakeFileName = "Trabajo.pdf";
                    int fakePages = 37; // valor simulado
                    CartStore.get().setQty(p, fakePages);
                    tvQty.setText(String.valueOf(fakePages));

                    new AlertDialog.Builder(this)
                            .setTitle("PDF detectado")
                            .setMessage(fakeFileName + " (" + fakePages + " hojas)\nCantidad actualizada.")
                            .setPositiveButton("OK", null)
                            .show();

                    updateGrandTotal();
                });
            }

            // Realizar pedido (simulación)
            btnPlaceOrder.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Procesando pedido")
                        .setMessage("Redireccionado a vista Pagos… (simulado)")
                        .setPositiveButton("OK", null)
                        .show();
            });

            llCartListContainer.addView(row);
        }

        updateGrandTotal();
    }

    private void updateGrandTotal() {
        tvCartGrandTotal.setText(getString(
                R.string.cart_total_label_format, ars.format(CartStore.get().getTotalAmount())
        ));
    }
}
