package com.helloworld.guaranparatodos.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthManager {

    private final FirebaseAuth firebaseAuth;

    public AuthManager() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public interface AuthCallback {

        void onSuccess(FirebaseUser usuario);

        void onError(String mensaje);
    }

    public void registrar(
            String correo,
            String contrasena,
            AuthCallback callback
    ) {

        firebaseAuth
                .createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser usuario =
                                firebaseAuth.getCurrentUser();

                        callback.onSuccess(usuario);

                    } else {

                        String mensaje = "Error al registrar usuario.";

                        if (task.getException() != null) {
                            mensaje = task.getException().getMessage();
                        }

                        callback.onError(mensaje);
                    }
                });
    }

    public void iniciarSesion(
            String correo,
            String contrasena,
            AuthCallback callback
    ) {

        firebaseAuth
                .signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser usuario =
                                firebaseAuth.getCurrentUser();

                        callback.onSuccess(usuario);

                    } else {

                        String mensaje =
                                "Correo o contraseña incorrectos.";

                        if (task.getException() != null) {
                            mensaje = task.getException().getMessage();
                        }

                        callback.onError(mensaje);
                    }
                });
    }

    public FirebaseUser obtenerUsuarioActual() {
        return firebaseAuth.getCurrentUser();
    }

    public String obtenerUidActual() {

        FirebaseUser usuario =
                firebaseAuth.getCurrentUser();

        if (usuario != null) {
            return usuario.getUid();
        }

        return null;
    }

    public boolean haySesionActiva() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public void cerrarSesion() {
        firebaseAuth.signOut();
    }
}