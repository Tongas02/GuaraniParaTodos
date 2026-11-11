package com.helloworld.guaranparatodos.estudiante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.auth.LoginActivity;
import com.helloworld.guaranparatodos.firebase.AuthManager;

public class MenuEstudianteActivity extends AppCompatActivity {

    private Button btnDiccionario;
    private Button btnNiveles;
    private Button btnProgreso;
    private Button btnPerfil;
    private Button btnCerrarSesion;

    private AuthManager authManager;

    private int idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_menu_estudiante
        );


        idUsuario =
                getIntent().getIntExtra(
                        "ID_USUARIO",
                        -1
                );


        authManager =
                new AuthManager();


        btnDiccionario =
                findViewById(
                        R.id.btnDiccionario
                );

        btnNiveles =
                findViewById(
                        R.id.btnNiveles
                );

        btnProgreso =
                findViewById(
                        R.id.btnProgreso
                );

        btnPerfil =
                findViewById(
                        R.id.btnPerfil
                );

        btnCerrarSesion =
                findViewById(
                        R.id.btnCerrarSesion
                );


        btnDiccionario.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MenuEstudianteActivity.this,
                            DiccionarioActivity.class
                    );

            startActivity(intent);
        });


        btnNiveles.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MenuEstudianteActivity.this,
                            NivelesActivity.class
                    );

            intent.putExtra(
                    "ID_USUARIO",
                    idUsuario
            );

            startActivity(intent);
        });


        btnProgreso.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MenuEstudianteActivity.this,
                            ProgresoActivity.class
                    );

            intent.putExtra(
                    "ID_USUARIO",
                    idUsuario
            );

            startActivity(intent);
        });


        btnPerfil.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MenuEstudianteActivity.this,
                            PerfilActivity.class
                    );

            intent.putExtra(
                    "ID_USUARIO",
                    idUsuario
            );

            startActivity(intent);
        });


        btnCerrarSesion.setOnClickListener(v -> {

            authManager.cerrarSesion();


            Intent intent =
                    new Intent(
                            MenuEstudianteActivity.this,
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