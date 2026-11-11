package com.helloworld.guaranparatodos.administrador;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.auth.LoginActivity;
import com.helloworld.guaranparatodos.firebase.AuthManager;

public class MenuAdministradorActivity extends AppCompatActivity {

    private Button btnGestionDiccionario;
    private Button btnGestionActividades;
    private Button btnGestionUsuarios;
    private Button btnCerrarSesion;

    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_administrador);

        authManager = new AuthManager();

        btnGestionDiccionario =
                findViewById(R.id.btnGestionDiccionario);

        btnGestionActividades =
                findViewById(R.id.btnGestionActividades);

        btnGestionUsuarios =
                findViewById(R.id.btnGestionUsuarios);

        btnCerrarSesion =
                findViewById(R.id.btnCerrarSesion);


        btnGestionDiccionario.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MenuAdministradorActivity.this,
                    GestionDiccionarioActivity.class
            );

            startActivity(intent);
        });


        btnGestionActividades.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MenuAdministradorActivity.this,
                    GestionActividadesActivity.class
            );

            startActivity(intent);
        });


        btnGestionUsuarios.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MenuAdministradorActivity.this,
                    GestionUsuariosActivity.class
            );

            startActivity(intent);
        });


        btnCerrarSesion.setOnClickListener(v -> {

            authManager.cerrarSesion();

            Intent intent = new Intent(
                    MenuAdministradorActivity.this,
                    LoginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();
        });
    }
}