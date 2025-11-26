package com.example.lamontana.data.auth;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// -----------------------------------------------------------------------------
// Archivo: AuthRepository.java
// Paquete: com.example.lamontana.data.auth
//
// Responsabilidad:
//   - Encapsular el acceso a FirebaseAuth
//   - Proveer métodos de alto nivel para login, registro, sesión actual y logout.
//
// Alcance:
//   - Es utilizado por los ViewModels de Login y Signup.
//   - No conoce detalles de la UI ni de Activities/Fragments.
//   - Se apoya en FirebaseAuth.getInstance(), inicializado previamente
//     por LaMontanaApp (clase Application).
//
// Métodos presentes:
//   - getInstance(): patrón singleton para obtener una única instancia del repositorio.
//   - login(String email, String password): inicia sesión en Firebase Auth.
//   - signup(String email, String password): registra un nuevo usuario en Firebase Auth.
//   - getCurrentUser(): devuelve el usuario actualmente logueado (o null).
//   - isLoggedIn(): indica si hay un usuario logueado actualmente.
//   - logout(): cierra la sesión actual.
//
// Notas:
//   - Devuelve objetos Task<AuthResult> para permitir que el ViewModel
//     maneje callbacks y actualice LiveData según éxito o error.
// -----------------------------------------------------------------------------
public class AuthRepository {

    // Singleton: una única instancia para toda la app
    private static AuthRepository instance;

    // Referencia a FirebaseAuth (servicio de autenticación de Firebase)
    private final FirebaseAuth firebaseAuth;

    // Constructor privado
    private AuthRepository() {
        // Obtiene la instancia global de FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    // Punto de acceso público al singleton
    public static synchronized AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    /**
     * Inicia sesión con email y contraseña en Firebase Auth.
     *
     * @param email    Email del usuario.
     * @param password Contraseña del usuario.
     * @return Task<AuthResult> para registrar callbacks onSuccess/onFailure
     *         desde el ViewModel.
     */
    public Task<AuthResult> login(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

    /**
     * Registra un nuevo usuario con email y contraseña en Firebase Auth.
     *
     * @param email    Email del usuario a registrar.
     * @param password Contraseña del nuevo usuario.
     * @return Task<AuthResult> para registrar callbacks onSuccess/onFailure
     *         desde el ViewModel.
     */
    public Task<AuthResult> signup(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }

    /**
     * Devuelve el usuario actualmente logueado, o null si no hay sesión.
     *
     * @return FirebaseUser o null.
     */
    @Nullable
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    /**
     * Indica si existe un usuario actualmente logueado.
     *
     * @return true si hay sesión activa, false en caso contrario.
     */
    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    /**
     * Cierra la sesión actual en Firebase Auth.
     */
    public void logout() {
        firebaseAuth.signOut();
    }
}
