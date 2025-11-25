package com.example.lamontana.ui;

/* -----------------------------------------------------------------------------
  Archivo: SignupActivity.java
  Responsabilidad:
    - Mostrar el formulario de creación de usuario.
    - Validar los datos ingresados por el usuario para crear una nueva cuenta.
    - Realizar un "registro local" temporal hasta integrar Firebase Auth.
    - Navegar nuevamente al Login o a la pantalla principal si el registro es válido.

  Alcance:
    - Forma parte del flujo de autenticación inicial de la app.
    - Reemplazable luego por registro real con backend/Firebase.

  Lista de métodos públicos:
    - onCreate(Bundle savedInstanceState)
        * Inicializa la UI, enlaza vistas y listeners.

  Lista de métodos privados:
    - setupViews()
        * Vincula las vistas XML con variables.
    - setupListeners()
        * Configura botones y acciones.
    - attemptSignup()
        * Valida los campos del formulario y simula la creación de usuario.
----------------------------------------------------------------------------- */

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lamontana.MainActivity;
import com.example.lamontana.R;

public class SignupActivity extends AppCompatActivity {

    // -------------------------------------------------------------------------
    // Atributos de la UI
    // -------------------------------------------------------------------------
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnCreateAccount;

    //    este atributo lleva a la vista para logiarse
    private Button btnGoToLogin;
    // -------------------------------------------------------------------------
    // Ciclo de vida
    // -------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupViews();
        setupListeners();
    }

    // -------------------------------------------------------------------------
    // Inicialización de vistas
    // -------------------------------------------------------------------------

    /**
     * Vincula los elementos visuales del XML con las variables de la actividad.
     * IMPORTANTE: los IDs deben coincidir con los definidos en activity_signup.xml.
     */
    private void setupViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etSignupEmail);
        etPassword = findViewById(R.id.etSignupPassword);
        etConfirmPassword = findViewById(R.id.etSignupConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);
    }

    /**
     * Configura listeners para los botones y componentes interactivos.
     */
    private void setupListeners() {
        if (btnCreateAccount != null) {
            btnCreateAccount.setOnClickListener(v -> attemptSignup());
        }

        // Listener para ir al Signup
        if (btnGoToLogin != null) {
            btnGoToLogin.setOnClickListener(view -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            });
        }
    }

    // -------------------------------------------------------------------------
    // Lógica de negocio (simulada)
    // -------------------------------------------------------------------------

    /**
     * Valida los campos del formulario y realiza un "registro local" simple.
     * Más adelante se integrará Firebase Authentication para registro real.
     */
    private void attemptSignup() {
        String name = etName != null ? etName.getText().toString().trim() : "";
        String email = etEmail != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword != null ? etPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword != null ? etConfirmPassword.getText().toString().trim() : "";

        // Validaciones básicas -------------------------------------------------

        if (TextUtils.isEmpty(name)) {
            etName.setError("Ingrese su nombre completo");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Ingrese un email válido");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Ingrese una contraseña");
            etPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirme su contraseña");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            etConfirmPassword.requestFocus();
            return;
        }

        // ---------------------------------------------------------------------
        // Registro local "de mentira" para el parcial
        // ---------------------------------------------------------------------
        Toast.makeText(this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();

        // Podés enviarlo al Login o directo al MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("name", name);
        startActivity(intent);

        finish(); // Cierra actividad de registro
    }
}
