package com.helloworld.guaranparatodos.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.administrador.MenuAdministradorActivity;
import com.helloworld.guaranparatodos.database.entity.Usuario;
import com.helloworld.guaranparatodos.estudiante.MenuEstudianteActivity;
import com.helloworld.guaranparatodos.repository.RepositoryCallback;
import com.helloworld.guaranparatodos.repository.UsuarioRepository;

public class LoginActivity extends AppCompatActivity {

    /*
     * UID DEFINITIVO DEL ADMINISTRADOR MAESTRO
     *
     * Correo:
     * maestroadmin@gmail.com
     */
    private static final String UID_ADMIN_MAESTRO =
            "75U4jGT5SXQTlvaPfUfoWT6hT6y1";


    private EditText edtCorreo;
    private EditText edtContrasena;

    private Button btnIniciarSesion;
    private Button btnCrearCuenta;

    private FirebaseAuth firebaseAuth;

    private UsuarioRepository usuarioRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_login
        );


        inicializarComponentes();


        firebaseAuth =
                FirebaseAuth.getInstance();


        usuarioRepository =
                new UsuarioRepository(this);


        btnIniciarSesion.setOnClickListener(v ->
                iniciarSesion()
        );


        btnCrearCuenta.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            LoginActivity.this,
                            RegistroActivity.class
                    );

            startActivity(intent);
        });
    }


    private void inicializarComponentes() {

        edtCorreo =
                findViewById(
                        R.id.edtCorreo
                );


        edtContrasena =
                findViewById(
                        R.id.edtContrasena
                );


        btnIniciarSesion =
                findViewById(
                        R.id.btnIniciarSesion
                );


        btnCrearCuenta =
                findViewById(
                        R.id.btnCrearCuenta
                );
    }


    private void iniciarSesion() {

        String correo =
                edtCorreo
                        .getText()
                        .toString()
                        .trim();


        String contrasena =
                edtContrasena
                        .getText()
                        .toString();


        /*
         * VALIDAR CORREO
         */
        if (correo.isEmpty()) {

            edtCorreo.setError(
                    "Ingrese su correo electrónico"
            );

            edtCorreo.requestFocus();

            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {

            edtCorreo.setError(
                    "Ingrese un correo electrónico válido"
            );

            edtCorreo.requestFocus();

            return;
        }


        /*
         * VALIDAR CONTRASEÑA
         */
        if (contrasena.isEmpty()) {

            edtContrasena.setError(
                    "Ingrese su contraseña"
            );

            edtContrasena.requestFocus();

            return;
        }


        btnIniciarSesion.setEnabled(
                false
        );


        btnIniciarSesion.setText(
                "INGRESANDO..."
        );


        /*
         * LOGIN CON FIREBASE
         */
        firebaseAuth
                .signInWithEmailAndPassword(
                        correo,
                        contrasena
                )
                .addOnCompleteListener(task -> {

                    btnIniciarSesion.setEnabled(
                            true
                    );

                    btnIniciarSesion.setText(
                            "INICIAR SESIÓN"
                    );


                    if (!task.isSuccessful()) {

                        Toast.makeText(
                                LoginActivity.this,
                                "Correo o contraseña incorrectos.",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }


                    FirebaseUser firebaseUser =
                            firebaseAuth.getCurrentUser();


                    if (firebaseUser == null) {

                        Toast.makeText(
                                LoginActivity.this,
                                "No fue posible obtener el usuario.",
                                Toast.LENGTH_LONG
                        ).show();

                        return;
                    }


                    /*
                     * ======================================
                     * ADMINISTRADOR MAESTRO
                     * ======================================
                     *
                     * Este usuario NO depende del rol
                     * guardado localmente en Room.
                     *
                     * Firebase devuelve siempre el mismo UID,
                     * incluso en otro celular.
                     */
                    if (UID_ADMIN_MAESTRO.equals(
                            firebaseUser.getUid()
                    )) {

                        ingresarComoAdministradorMaestro();

                        return;
                    }


                    /*
                     * USUARIOS NORMALES
                     */
                    buscarUsuarioLocal(
                            firebaseUser.getUid()
                    );
                });
    }


    /*
     * ADMINISTRADOR MAESTRO
     */
    private void ingresarComoAdministradorMaestro() {

        /*
         * Tema predeterminado del administrador.
         */
        aplicarTema(
                "CLARO"
        );


        Toast.makeText(
                this,
                "Bienvenido, Administrador Maestro",
                Toast.LENGTH_SHORT
        ).show();


        Intent intent =
                new Intent(
                        LoginActivity.this,
                        MenuAdministradorActivity.class
                );


        /*
         * Limpiamos las pantallas anteriores
         * para evitar volver al Login con Atrás.
         */
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );


        startActivity(intent);

        finish();
    }


    /*
     * BUSCAR USUARIO NORMAL EN ROOM
     */
    private void buscarUsuarioLocal(
            String firebaseUid
    ) {

        usuarioRepository
                .obtenerPorFirebaseUid(
                        firebaseUid,
                        new RepositoryCallback<Usuario>() {

                            @Override
                            public void onSuccess(
                                    Usuario usuario
                            ) {

                                if (usuario == null) {

                                    Toast.makeText(
                                            LoginActivity.this,
                                            "El usuario no se encuentra registrado localmente.",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    firebaseAuth.signOut();

                                    return;
                                }


                                /*
                                 * APLICAR TEMA GUARDADO
                                 */
                                aplicarTema(
                                        usuario.getTema()
                                );


                                /*
                                 * REDIRECCIONAR SEGÚN ROL
                                 */
                                redirigirSegunRol(
                                        usuario
                                );
                            }


                            @Override
                            public void onError(
                                    Exception e
                            ) {

                                Toast.makeText(
                                        LoginActivity.this,
                                        "No fue posible verificar el usuario.",
                                        Toast.LENGTH_LONG
                                ).show();

                                firebaseAuth.signOut();
                            }
                        }
                );
    }


    /*
     * TEMA CLARO / OSCURO
     */
    private void aplicarTema(
            String tema
    ) {

        if (tema != null &&
                tema.equalsIgnoreCase(
                        "OSCURO"
                )) {

            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_YES
                    );

        } else {

            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate.MODE_NIGHT_NO
                    );
        }
    }


    /*
     * REDIRECCIÓN PARA USUARIOS NORMALES
     */
    private void redirigirSegunRol(
            Usuario usuario
    ) {

        String tipoUsuario =
                usuario.getTipoUsuario();


        if (tipoUsuario == null) {

            Toast.makeText(
                    this,
                    "El usuario no posee un rol asignado.",
                    Toast.LENGTH_LONG
            ).show();

            firebaseAuth.signOut();

            return;
        }


        Intent intent;


        /*
         * ADMINISTRADOR NORMAL
         */
        if (tipoUsuario.equalsIgnoreCase(
                "ADMINISTRADOR"
        )) {

            intent =
                    new Intent(
                            LoginActivity.this,
                            MenuAdministradorActivity.class
                    );


            /*
             * ESTUDIANTE
             */
        } else if (tipoUsuario.equalsIgnoreCase(
                "ESTUDIANTE"
        )) {

            intent =
                    new Intent(
                            LoginActivity.this,
                            MenuEstudianteActivity.class
                    );


        } else {

            Toast.makeText(
                    this,
                    "Tipo de usuario no reconocido.",
                    Toast.LENGTH_LONG
            ).show();

            firebaseAuth.signOut();

            return;
        }


        /*
         * Enviamos el ID local de Room.
         */
        intent.putExtra(
                "ID_USUARIO",
                usuario.getIdUsuario()
        );


        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );


        startActivity(intent);

        finish();
    }
}