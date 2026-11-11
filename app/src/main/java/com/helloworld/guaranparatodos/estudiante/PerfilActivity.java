package com.helloworld.guaranparatodos.estudiante;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.database.entity.Usuario;
import com.helloworld.guaranparatodos.firebase.AuthManager;
import com.helloworld.guaranparatodos.repository.RepositoryCallback;
import com.helloworld.guaranparatodos.repository.UsuarioRepository;

public class PerfilActivity extends AppCompatActivity {

    private Button btnVolver;
    private Button btnGuardarCambios;

    private EditText edtNombre;

    private TextView txtCorreo;
    private TextView txtTipoUsuario;

    private Spinner spinnerTema;

    private AuthManager authManager;
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_perfil
        );

        inicializarComponentes();

        authManager =
                new AuthManager();

        usuarioRepository =
                new UsuarioRepository(this);

        configurarTemas();


        btnVolver.setOnClickListener(v ->
                finish()
        );


        btnGuardarCambios.setOnClickListener(v ->
                guardarCambios()
        );


        cargarPerfil();
    }


    private void inicializarComponentes() {

        btnVolver =
                findViewById(
                        R.id.btnVolver
                );

        btnGuardarCambios =
                findViewById(
                        R.id.btnGuardarCambios
                );

        edtNombre =
                findViewById(
                        R.id.edtNombre
                );

        txtCorreo =
                findViewById(
                        R.id.txtCorreo
                );

        txtTipoUsuario =
                findViewById(
                        R.id.txtTipoUsuario
                );

        spinnerTema =
                findViewById(
                        R.id.spinnerTema
                );
    }


    private void configurarTemas() {

        String[] temas = {
                "CLARO",
                "OSCURO"
        };


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        temas
                );


        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerTema.setAdapter(
                adapter
        );
    }


    private void cargarPerfil() {

        String uid =
                authManager.obtenerUidActual();


        if (uid == null) {

            Toast.makeText(
                    this,
                    "No se encontró una sesión activa.",
                    Toast.LENGTH_LONG
            ).show();

            finish();

            return;
        }


        usuarioRepository.obtenerPorFirebaseUid(
                uid,
                new RepositoryCallback<Usuario>() {

                    @Override
                    public void onSuccess(
                            Usuario usuario
                    ) {

                        if (usuario == null) {

                            Toast.makeText(
                                    PerfilActivity.this,
                                    "No se encontró el perfil del usuario.",
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }


                        usuarioActual =
                                usuario;


                        mostrarDatos();
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        Toast.makeText(
                                PerfilActivity.this,
                                "No fue posible cargar el perfil.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    private void mostrarDatos() {

        if (usuarioActual == null) {
            return;
        }


        edtNombre.setText(
                usuarioActual.getNombre()
        );


        txtCorreo.setText(
                usuarioActual.getCorreo()
        );


        txtTipoUsuario.setText(
                usuarioActual.getTipoUsuario()
        );


        String tema =
                usuarioActual.getTema();


        if (tema != null &&
                tema.equalsIgnoreCase("OSCURO")) {

            spinnerTema.setSelection(1);

        } else {

            spinnerTema.setSelection(0);
        }
    }


    private void guardarCambios() {

        if (usuarioActual == null) {

            Toast.makeText(
                    this,
                    "No se pudo cargar el usuario.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        String nombre =
                edtNombre
                        .getText()
                        .toString()
                        .trim();


        if (nombre.isEmpty()) {

            edtNombre.setError(
                    "Ingrese su nombre"
            );

            edtNombre.requestFocus();

            return;
        }


        String tema =
                spinnerTema
                        .getSelectedItem()
                        .toString();


        usuarioActual.setNombre(
                nombre
        );


        usuarioActual.setTema(
                tema
        );


        btnGuardarCambios.setEnabled(
                false
        );


        usuarioRepository.actualizar(
                usuarioActual,
                new RepositoryCallback<Boolean>() {

                    @Override
                    public void onSuccess(
                            Boolean resultado
                    ) {

                        btnGuardarCambios.setEnabled(
                                true
                        );


                        Toast.makeText(
                                PerfilActivity.this,
                                "Perfil actualizado correctamente",
                                Toast.LENGTH_SHORT
                        ).show();


                        aplicarTema(
                                tema
                        );
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        btnGuardarCambios.setEnabled(
                                true
                        );


                        Toast.makeText(
                                PerfilActivity.this,
                                "No fue posible actualizar el perfil.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    private void aplicarTema(
            String tema
    ) {

        if (tema.equalsIgnoreCase(
                "OSCURO"
        )) {

            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
            );

        } else {

            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
            );
        }
    }
}