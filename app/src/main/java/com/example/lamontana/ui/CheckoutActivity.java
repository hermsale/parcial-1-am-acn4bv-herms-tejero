package com.example.lamontana.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.lamontana.R;
import com.example.lamontana.data.CartStore;
import com.example.lamontana.model.CartItem;
import com.example.lamontana.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {


    // Vistas para el menú deslizante (navbar)
    private View overlay;
    private View topSheet;
    private boolean isMenuOpen = false;

    // --- Shipping form ---
    private EditText etAddress, etPostalCode, etPhone, etNotes;
    private MaterialButton btnCalculateShipping;

    // --- Product detail list ---
    private LinearLayout llDetailProducts;

    // --- Summary ---
    private TextView tvFinalTotal;
    private MaterialButton btnGoToPayment;

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_cart_detail);

        ImageView btnMenu = findViewById(R.id.btnMenu);
        overlay = findViewById(R.id.overlay);
        topSheet = findViewById(R.id.topSheet);

        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> toggleMenu());
        }
        if (overlay != null) {
            overlay.setOnClickListener(v -> closeMenu());
        }

        // Botones dentro del top sheet
        View btnMisDatos = findViewById(R.id.btnMisDatos);
        View btnMiCarrito = findViewById(R.id.btnMiCarrito);
        View btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        if (btnMisDatos != null) {
            btnMisDatos.setOnClickListener(v -> {
                closeMenu();
                startActivity(new Intent(CheckoutActivity.this, ProfileActivity.class));
            });
        }

        if (btnMiCarrito != null) {
            btnMiCarrito.setOnClickListener(v -> {
                closeMenu();
                // Ya estás en Carrito; si querés simplemente refrescar:
//                renderCart();
            });
        }

        if (btnCerrarSesion != null) {
            btnCerrarSesion.setOnClickListener(v -> {
                closeMenu();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CheckoutActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
        }

        initViews();
        renderProducts();
        updateTotal();
    }

    // ----------------------------------------------------------
    // Inicializa las views del layout
    // ----------------------------------------------------------
    private void initViews() {

        // Shipping form
        etAddress      = findViewById(R.id.etAddress);
        etPostalCode   = findViewById(R.id.etPostalCode);
        etPhone        = findViewById(R.id.etPhone);
        etNotes        = findViewById(R.id.etNotes);
        btnCalculateShipping = findViewById(R.id.btnCalculateShipping);

        // Lista de productos
        llDetailProducts = findViewById(R.id.llDetailProducts);

        // Total final
        tvFinalTotal = findViewById(R.id.tvFinalTotal);
        btnGoToPayment = findViewById(R.id.btnGoToPayment);

        // Eventos (placeholder)
        btnCalculateShipping.setOnClickListener(v -> {
            // TODO: calcular envío
        });

        btnGoToPayment.setOnClickListener(v -> {
            // TODO: navegar a pantalla de pago
        });
    }

    // ----------------------------------------------------------
    // Renderiza los items del carrito dentro de llDetailProducts
    // ----------------------------------------------------------
    private void renderProducts() {
        llDetailProducts.removeAllViews();

        List<CartItem> cartItems  = CartStore.get().getItems();

        for (CartItem  p : cartItems) {
            // Inflar cada item del carrito
            View itemView = getLayoutInflater().inflate(
                    R.layout.item_cart, // podés crear una vista especial tipo item_checkout.xml si querés
                    llDetailProducts,
                    false
            );

            // Referencias internas
            TextView tvName = itemView.findViewById(R.id.tvCartName);
            TextView tvQtyPrice = itemView.findViewById(R.id.tvCartQtyPrice);
            MaterialButton btnRemove = itemView.findViewById(R.id.btnRemove);

            // Seteo de datos
            tvName.setText(p.product.name);
            tvQtyPrice.setText("x" + p.qty + " - $" + (p.qty * p.product.price));


            btnRemove.setOnClickListener(v -> {
                CartStore.get().remove(p.product);
                renderProducts();
                updateTotal();
            });

            // Agregar vista a la lista
            llDetailProducts.addView(itemView);
        }
    }

    // ----------------------------------------------------------
    // Muestra el total del carrito
    // ----------------------------------------------------------
    private void updateTotal() {
        int total = CartStore.get().getTotalAmount();
        tvFinalTotal.setText("Total: " + currencyFormat.format(total));
    }



    // ----- Control del menú deslizante (top sheet / navbar) -----

    private void toggleMenu() {
        if (isMenuOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    private void openMenu() {
        if (topSheet == null || overlay == null) return;

        topSheet.setVisibility(View.VISIBLE);
        topSheet.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(
                        this,
                        R.anim.top_sheet_down
                )
        );
        overlay.setVisibility(View.VISIBLE);
        isMenuOpen = true;
    }

    private void closeMenu() {
        if (topSheet == null || overlay == null) return;

        topSheet.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(
                        this,
                        R.anim.top_sheet_up
                )
        );
        overlay.setVisibility(View.GONE);
        topSheet.setVisibility(View.GONE);
        isMenuOpen = false;
    }
}
