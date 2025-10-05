package com.example.parcial_1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity - Paso 1 (base)
 * - Muestra el layout de "Catálogo + Carrito" sin lógica aún.
 * - En el Paso 2 agregaremos: eventos de filtros, agregar al carrito,
 *   agregado dinámico de vistas y actualización de totales.
 */
public class MainActivity extends AppCompatActivity {

    // Referencias a vistas (las usaremos en el Paso 2)
    private LinearLayout llCatalogContainer;
    private LinearLayout llCartContainer;
    private TextView tvTotal;
    private Button btnClearCart;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula con el XML de la pantalla
        setContentView(R.layout.activity_main);

        // Obtiene el inflater y referencias a contenedores
        inflater = LayoutInflater.from(this);
        llCatalogContainer = findViewById(R.id.llCatalogContainer);
        llCartContainer = findViewById(R.id.llCartContainer);
        tvTotal = findViewById(R.id.tvTotal);
        btnClearCart = findViewById(R.id.btnClearCart);

        // --- Paso 1: POV de layout listo ---
        // (Opcional) Dejar un ítem de catálogo estático de ejemplo en este paso
        // En el Paso 2 pasaremos a inflar items desde XML y agregarlos dinámicamente.
        // Aquí no agregamos lógica para cumplir "ir lento" y validar que compila.
    }
}