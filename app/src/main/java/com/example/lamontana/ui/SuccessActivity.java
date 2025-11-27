package com.example.lamontana.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lamontana.R;
import com.example.lamontana.ui.CatalogActivity;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        // Después de 2.5 segundos vuelve a catálogo
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SuccessActivity.this, CatalogActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }, 2500);
    }
}
