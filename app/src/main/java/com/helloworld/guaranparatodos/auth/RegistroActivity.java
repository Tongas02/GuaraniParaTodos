package com.helloworld.guaranparatodos.auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseUser;
import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.database.entity.Usuario;
import com.helloworld.guaranparatodos.firebase.AuthManager;
import com.helloworld.guaranparatodos.repository.RepositoryCallback;
import com.helloworld.guaranparatodos.repository.UsuarioRepository;

public class RegistroActivity extends AppCompatActivity {

    private EditText edtNombre;
    private EditText edtCorreo;
    private EditText edtContrasena;
    private EditText edtConfirmarContrasena;

    private Button btnRegistrar;
    private Button btnVolverLogin;

    private AuthManager authManager;
    private UsuarioRepository usuarioRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * La pantalla de registro siempre se muestra
         * en modo claro.
         */
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
        );

        setContentView(R.layout.activity_registro);

        inicializarComponentes();

        authManager = new AuthManager();

        usuarioRepository =
                new UsuarioRepository(this);

        btnRegistrar.setOnClickListener(v ->
                registrarUsuario()
        );

        btnVolverLogin.setOnClickListener(v ->
                finish()
        );
    }

    private void inicializarComponentes() {

        edtNombre =
                findViewById(
                        R.id.edtNombre
                );

        edtCorreo =
                findViewById(
                        R.id.edtCorreo
                );

        edtContrasena =
                findViewById(
                        R.id.edtContrasena
                );

        edtConfirmarContrasena =
                findViewById(
                        R.id.edtConfirmarContrasena
                );

        btnRegistrar =
                findViewById(
                        R.id.btnRegistrar
                );

        btnVolverLogin =
                findViewById(
                        R.id.btnVolverLogin
                );
    }

    private void registrarUsuario() {

        String nombre =
                edtNombre
                        .getText()
                        .toString()
                        .trim();

        String correo =
                edtCorreo
                        .getText()
                        .toString()
                        .trim();

        String contrasena =
                edtContrasena
                        .getText()
                        .toString();

        String confirmarContrasena =
                edtConfirmarContrasena
                        .getText()
                        .toString();


        /*
         * VALIDAR NOMBRE
         */
        if (nombre.isEmpty()) {

            edtNombre.setError(
                    "Ingrese su nombre"
            );

            edtNombre.requestFocus();

            return;
        }


        /*
         * VALIDAR CORREO
         */
        if (correo.isEmpty()) {

            edtCorreo.setError(
                    "Ingrese su correo"
            );

            edtCorreo.requestFocus();

            return;
        }


        /*
         * VALIDAR CONTRASEÑA
         */
        if (contrasena.isEmpty()) {

            edtContrasena.setError(
                    "Ingrese una contraseña"
            );

            edtContrasena.requestFocus();

            return;
        }


        if (contrasena.length() < 6) {

            edtContrasena.setError(
                    "La contraseña debe tener al menos 6 caracteres"
            );

            edtContrasena.requestFocus();

            return;
        }


        /*
         * CONFIRMAR CONTRASEÑA
         */
        if (!contrasena.equals(
                confirmarContrasena
        )) {

            edtConfirmarContrasena.setError(
                    "Las contraseñas no coinciden"
            );

            edtConfirmarContrasena.requestFocus();

            return;
        }


        /*
         * EVITAR DOBLE CLIC
         */
        btnRegistrar.setEnabled(
                false
        );


        /*
         * REGISTRAR EN FIREBASE
         */
        authManager.registrar(
                correo,
                contrasena,
                new AuthManager.AuthCallback() {

                    @Override
                    public void onSuccess(
                            FirebaseUser firebaseUser
                    ) {

                        if (firebaseUser == null) {

                            btnRegistrar.setEnabled(
                                    true
                            );

                            Toast.makeText(
                                    RegistroActivity.this,
                                    "No se pudo obtener el usuario.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }


                        guardarUsuarioEnRoom(
                                firebaseUser,
                                nombre,
                                correo
                        );
                    }


                    @Override
                    public void onError(
                            String mensaje
                    ) {

                        btnRegistrar.setEnabled(
                                true
                        );

                        Toast.makeText(
                                RegistroActivity.this,
                                mensaje,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    private void guardarUsuarioEnRoom(
            FirebaseUser firebaseUser,
            String nombre,
            String correo
    ) {

        /*
         * Los usuarios registrados públicamente
         * siempre son ESTUDIANTES.
         *
         * El tema predeterminado es CLARO.
         */
        Usuario usuario =
                new Usuario(
                        firebaseUser.getUid(),
                        nombre,
                        correo,
                        "ESTUDIANTE",
                        "CLARO"
                );


        usuarioRepository.insertar(
                usuario,
                new RepositoryCallback<Long>() {

                    @Override
                    public void onSuccess(
                            Long idUsuario
                    ) {

                        Toast.makeText(
                                RegistroActivity.this,
                                "Usuario registrado correctamente",
                                Toast.LENGTH_SHORT
                        ).show();


                        /*
                         * Firebase inicia sesión automáticamente
                         * después de crear una cuenta.
                         *
                         * Cerramos esa sesión para regresar
                         * al Login.
                         */
                        authManager.cerrarSesion();


                        /*
                         * Aseguramos que las pantallas públicas
                         * vuelvan al modo claro.
                         */
                        AppCompatDelegate
                                .setDefaultNightMode(
                                        AppCompatDelegate.MODE_NIGHT_NO
                                );


                        finish();
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        btnRegistrar.setEnabled(
                                true
                        );

                        Toast.makeText(
                                RegistroActivity.this,
                                "La cuenta fue creada, pero ocurrió un error al guardar el perfil.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }
}