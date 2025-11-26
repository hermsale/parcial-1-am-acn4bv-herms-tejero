package com.example.lamontana.data.user;
import androidx.annotation.Nullable;
public class UserStore {

    /**
     * UserStore
     * ----------------------------------------------
     * Mantiene en memoria los datos del usuario logueado.
     * Se completa al iniciar sesión / registrarse.
     * Se usa para rellenar la pantalla “Mis Datos”.
     * El Store es mas rapido que la respuesta a Firebase
     */



    private static UserStore instance;

    public static UserStore get() {
        if (instance == null) instance = new UserStore();
        return instance;
    }

    // ------- Datos del usuario -------
    public String uid = "";
    public String nombre = "";
    public String apellido = "";
    public String email = "";
    public String telefono = "";
    public String direccion = "";

    private UserStore() {}

    // Cargar desde Firebase Auth + Firestore
    public void setBasicData(String uid, String nombre, String email) {
        this.uid = uid;
        this.nombre = nombre;
        this.email = email;
    }

    // Completar datos opcionales
    public void setOptionalData(@Nullable String apellido,
                                @Nullable String telefono,
                                @Nullable String direccion) {

        if (apellido != null) this.apellido = apellido;
        if (telefono != null) this.telefono = telefono;
        if (direccion != null) this.direccion = direccion;
    }

    // Al cerrar sesión llamamos esto
    public void clear() {
        uid = "";
        nombre = "";
        apellido = "";
        email = "";
        telefono = "";
        direccion = "";
    }
}
