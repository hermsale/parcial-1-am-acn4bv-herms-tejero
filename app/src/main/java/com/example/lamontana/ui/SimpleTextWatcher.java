package com.example.lamontana.ui;

import android.text.Editable;
import android.text.TextWatcher;

//Helper para escuchar si hay cambios en el formulario de pedido
public abstract class SimpleTextWatcher implements TextWatcher {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void afterTextChanged(Editable s) {}
}