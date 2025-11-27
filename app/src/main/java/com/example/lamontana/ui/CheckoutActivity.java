package com.example.lamontana.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.example.lamontana.R;
import com.example.lamontana.data.CartStore;
import com.example.lamontana.model.CartItem;
import com.example.lamontana.model.Product;
import com.example.lamontana.ui.navbar.MenuDesplegableHelper;
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


    // Formateador de moneda en ARS
    private final NumberFormat ars =
            NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_cart_detail);

        // ---------- Navbar / Menú deslizante ----------
        ImageView btnMenu = findViewById(R.id.btnMenu);
        View overlay = findViewById(R.id.overlay);
        View topSheet = findViewById(R.id.topSheet);

        View btnInicio = findViewById(R.id.btnInicio);
        View btnMisDatos = findViewById(R.id.btnMisDatos);
        View btnMiCarrito = findViewById(R.id.btnMiCarrito);
        View btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

// Usar el helper centralizado - del NAVBAR
        MenuDesplegableHelper menuHelper =
                new MenuDesplegableHelper(
                        this,
                        btnMenu,
                        overlay,
                        topSheet,
                        btnInicio,
                        btnMisDatos,
                        btnMiCarrito,
                        btnCerrarSesion
                );

        menuHelper.initMenu();

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

//        boton de pago
        btnGoToPayment.setOnClickListener(v -> onPlaceAllOrders());
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

    /**
     * Acción “Realizar todos los pedidos”.
     */
    private void onPlaceAllOrders() {
        int items = CartStore.get().getTotalQty();
        int total = CartStore.get().getTotalAmount();
        if (items <= 0) return;


        new AlertDialog.Builder(this)
                .setTitle("Confirmar compra")
                .setMessage("Vas a realizar " + items + " pedidos por un total de " + ars.format(total))
                .setPositiveButton("Confirmar", (dialog, which) -> {

                    // Vaciar carrito
                    CartStore.get().clear();

                    // Ir a pantalla de éxito
                    Intent i = new Intent(CheckoutActivity.this, SuccessActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

}
