package com.example.lamontana.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lamontana.data.auth.AuthRepository;
import com.example.lamontana.data.user.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

// -----------------------------------------------------------------------------
// Archivo: SignupViewModel.java
// Paquete: com.example.lamontana.viewmodel
//
// Responsabilidad:
//   - Actuar como capa intermedia (ViewModel) entre la UI de SignupActivity y
//     las capas de datos:
//       * AuthRepository (FirebaseAuth) para registrar al usuario.
//       * UserRepository (Firestore) para guardar el perfil en la colección
//         "usuarios".
//
//   - Exponer estados observables (LiveData) para:
//       * loading: indica si se está procesando el registro completo.
//       * signupSuccess: indica si el registro fue exitoso (Auth + Firestore).
//       * errorMessage: mensaje de error a mostrar en la UI.
//
// Alcance:
//   - Usado únicamente por SignupActivity dentro del flujo de registro.
//   - No conoce detalles de la UI (Views, Toasts, Intents).
//
// Métodos presentes:
//   - getLoading(): LiveData<Boolean> para observar el estado de carga.
//   - getSignupSuccess(): LiveData<Boolean> para observar si el registro fue
//                         exitoso.
//   - getErrorMessage(): LiveData<String> para observar mensajes de error.
//   - signup(String name, String email, String password):
//       * Crea el usuario en Firebase Auth.
//       * Si Auth es exitoso, guarda el perfil en Firestore (UserRepository).
//       * Actualiza los LiveData según el resultado.
// -----------------------------------------------------------------------------
public class SignupViewModel extends ViewModel {

    // Repositorios
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    // LiveData internos
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> signupSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SignupViewModel() {
        authRepository = AuthRepository.getInstance();
        userRepository = UserRepository.getInstance();
    }

    // Getters públicos para exponer LiveData a la Activity
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getSignupSuccess() {
        return signupSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Inicia el proceso de registro de usuario:
     *   1) Crea el usuario en Firebase Auth con email y contraseña.
     *   2) Si Auth es exitoso, crea/actualiza el documento en la colección
     *      "usuarios" usando el uid como ID de documento.
     *
     * @param name     Nombre completo del usuario.
     * @param email    Email del usuario a registrar.
     * @param password Contraseña del usuario.
     */
    public void signup(String name, String email, String password) {
        // Marcamos que el registro comenzó
        loading.setValue(true);
        signupSuccess.setValue(false);
        errorMessage.setValue(null);

        authRepository.signup(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> authTask) {
                        if (!authTask.isSuccessful()) {
                            // Falló el registro en Auth
                            loading.setValue(false);
                            signupSuccess.setValue(false);

                            String message = "Error al crear la cuenta";
                            if (authTask.getException() != null &&
                                    authTask.getException().getMessage() != null) {
                                message = authTask.getException().getMessage();
                            }
                            errorMessage.setValue(message);
                            return;
                        }

                        // Auth OK: obtenemos el usuario recién creado
                        AuthResult result = authTask.getResult();
                        FirebaseUser firebaseUser = result != null ? result.getUser() : null;

                        if (firebaseUser == null) {
                            // Caso muy raro: Auth dijo OK pero no hay usuario
                            loading.setValue(false);
                            signupSuccess.setValue(false);
                            errorMessage.setValue("Error interno: usuario no disponible tras el registro");
                            return;
                        }

                        String uid = firebaseUser.getUid();
                        String emailFromAuth = firebaseUser.getEmail();
                        if (emailFromAuth == null) {
                            emailFromAuth = email; // fallback al email recibido
                        }

                        // Paso 2: crear/actualizar perfil en Firestore
                        userRepository.createUserProfile(uid, name, emailFromAuth)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> userTask) {
                                        // Ya terminó todo el flujo (Auth + Firestore)
                                        loading.setValue(false);

                                        if (userTask.isSuccessful()) {
                                            signupSuccess.setValue(true);
                                            errorMessage.setValue(null);
                                        } else {
                                            signupSuccess.setValue(false);

                                            String msg = "Cuenta creada pero hubo un error al guardar los datos";
                                            if (userTask.getException() != null &&
                                                    userTask.getException().getMessage() != null) {
                                                msg = userTask.getException().getMessage();
                                            }
                                            errorMessage.setValue(msg);
                                        }
                                    }
                                });
                    }
                });
    }
}
