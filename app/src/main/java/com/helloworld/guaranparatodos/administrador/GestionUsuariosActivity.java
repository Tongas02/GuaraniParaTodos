package com.helloworld.guaranparatodos.administrador;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.database.entity.Usuario;
import com.helloworld.guaranparatodos.repository.RepositoryCallback;
import com.helloworld.guaranparatodos.repository.UsuarioRepository;

import java.util.List;

public class GestionUsuariosActivity extends AppCompatActivity {

    private Button btnVolver;

    private TextView txtCantidadUsuarios;
    private TextView txtSinUsuarios;

    private LinearLayout contenedorUsuarios;

    private UsuarioRepository usuarioRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_gestion_usuarios
        );

        inicializarComponentes();

        usuarioRepository =
                new UsuarioRepository(this);


        btnVolver.setOnClickListener(v ->
                volverAlMenuAdministrador()
        );


        cargarUsuarios();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (usuarioRepository != null) {

            cargarUsuarios();
        }
    }


    private void inicializarComponentes() {

        btnVolver =
                findViewById(
                        R.id.btnVolver
                );

        txtCantidadUsuarios =
                findViewById(
                        R.id.txtCantidadUsuarios
                );

        txtSinUsuarios =
                findViewById(
                        R.id.txtSinUsuarios
                );

        contenedorUsuarios =
                findViewById(
                        R.id.contenedorUsuarios
                );
    }


    private void cargarUsuarios() {

        usuarioRepository.obtenerTodos(
                new RepositoryCallback<List<Usuario>>() {

                    @Override
                    public void onSuccess(
                            List<Usuario> usuarios
                    ) {

                        mostrarUsuarios(
                                usuarios
                        );
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        Toast.makeText(
                                GestionUsuariosActivity.this,
                                "No fue posible cargar los usuarios.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    private void mostrarUsuarios(
            List<Usuario> usuarios
    ) {

        contenedorUsuarios.removeAllViews();


        if (usuarios == null ||
                usuarios.isEmpty()) {

            txtCantidadUsuarios.setText(
                    "Usuarios registrados: 0"
            );

            txtSinUsuarios.setVisibility(
                    View.VISIBLE
            );

            return;
        }


        txtSinUsuarios.setVisibility(
                View.GONE
        );


        txtCantidadUsuarios.setText(
                "Usuarios registrados: "
                        + usuarios.size()
        );


        for (Usuario usuario :
                usuarios) {

            crearTarjetaUsuario(
                    usuario
            );
        }
    }


    private void crearTarjetaUsuario(
            Usuario usuario
    ) {

        LinearLayout tarjeta =
                new LinearLayout(this);

        tarjeta.setOrientation(
                LinearLayout.VERTICAL
        );

        tarjeta.setPadding(
                30,
                25,
                30,
                25
        );


        LinearLayout.LayoutParams parametros =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        parametros.setMargins(
                0,
                10,
                0,
                20
        );

        tarjeta.setLayoutParams(
                parametros
        );


        TextView txtNombre =
                new TextView(this);

        txtNombre.setText(
                usuario.getNombre()
        );

        txtNombre.setTextSize(
                20
        );

        txtNombre.setTypeface(
                null,
                Typeface.BOLD
        );


        TextView txtCorreo =
                new TextView(this);

        txtCorreo.setText(
                "Correo: "
                        + usuario.getCorreo()
        );

        txtCorreo.setTextSize(
                16
        );


        TextView txtRol =
                new TextView(this);

        txtRol.setText(
                "Rol: "
                        + usuario.getTipoUsuario()
        );

        txtRol.setTextSize(
                16
        );


        TextView txtTema =
                new TextView(this);

        String tema =
                usuario.getTema();

        if (tema == null ||
                tema.trim().isEmpty()) {

            tema = "CLARO";
        }


        txtTema.setText(
                "Tema: "
                        + tema
        );

        txtTema.setTextSize(
                16
        );


        tarjeta.addView(
                txtNombre
        );

        tarjeta.addView(
                txtCorreo
        );

        tarjeta.addView(
                txtRol
        );

        tarjeta.addView(
                txtTema
        );


        contenedorUsuarios.addView(
                tarjeta
        );
    }


    private void volverAlMenuAdministrador() {

        Intent intent =
                new Intent(
                        GestionUsuariosActivity.this,
                        MenuAdministradorActivity.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);

        finish();
    }
}