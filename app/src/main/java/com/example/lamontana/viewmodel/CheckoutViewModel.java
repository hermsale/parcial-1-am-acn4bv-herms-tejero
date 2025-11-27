package com.example.lamontana.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lamontana.data.auth.AuthRepository;
import com.example.lamontana.data.user.UserStore;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/*
 * ============================================================
 * Archivo: CheckoutViewModel.java
 * Paquete: com.example.lamontana.viewmodel
 * ------------------------------------------------------------
 * Responsabilidad:
 *   - Cargar desde Firestore los datos básicos de envío
 *     del usuario logueado:
 *       · direccion
 *       · telefono
 *   - Exponerlos como LiveData para que CheckoutActivity
 *     pueda rellenar el formulario.
 *   - Actualizar también el UserStore como cache en memoria.
 *
 * Colección usada en Firestore:
 *   - "usuarios" con documentos usuarios/{uid}
 *     (uid = FirebaseAuth.getCurrentUser().getUid())
 *
 * Campos esperados:
 *   - direccion: string
 *   - telefono: string
 *   - apellido: string (opcional, solo para completar el UserStore)
 * ============================================================
 */
public class CheckoutViewModel extends ViewModel {

    private final MutableLiveData<String> addressLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> phoneLiveData   = new MutableLiveData<>();

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final AuthRepository authRepository = AuthRepository.getInstance();

    // Getters para la Activity
    public LiveData<String> getAddress() {
        return addressLiveData;
    }

    public LiveData<String> getPhone() {
        return phoneLiveData;
    }


    public void loadUserAddressFromFirestore() {
        FirebaseUser currentUser = authRepository.getCurrentUser();
        if (currentUser == null) {

            addressLiveData.setValue("");
            phoneLiveData.setValue("");
            return;
        }

        final String uid = currentUser.getUid();

        firestore.collection("usuarios")
                .document(uid)
                .get()
                .addOnSuccessListener(this::onUserDocLoaded)
                .addOnFailureListener(e -> {
                    // Ante error, no reventamos la app:
                    // dejamos los valores actuales o vacíos.
                    addressLiveData.setValue("");
                    phoneLiveData.setValue("");
                });
    }

    // ----------------------------------------------------------
    // Mapeo de documento Firestore -> LiveData + UserStore
    // ----------------------------------------------------------
    private void onUserDocLoaded(DocumentSnapshot doc) {
        if (doc == null || !doc.exists()) {
            addressLiveData.setValue("");
            phoneLiveData.setValue("");
            return;
        }

        String direccion = doc.getString("direccion");
        String telefono  = doc.getString("telefono");
        String apellido  = doc.getString("apellido");

        if (direccion == null) direccion = "";
        if (telefono  == null) telefono  = "";
        if (apellido  == null) apellido  = "";

        // Actualizamos LiveData (será observado desde CheckoutActivity)
        addressLiveData.setValue(direccion);
        phoneLiveData.setValue(telefono);

        // Actualizamos también el UserStore en memoria
        UserStore.get().setOptionalData(
                apellido,
                telefono,
                direccion
        );
    }
}
