package com.helloworld.guaranparatodos.estudiante;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.database.entity.Nivel;
import com.helloworld.guaranparatodos.repository.ActividadRepository;
import com.helloworld.guaranparatodos.repository.ProgresoRepository;
import com.helloworld.guaranparatodos.repository.RepositoryCallback;

import java.util.List;

public class NivelesActivity extends AppCompatActivity {

    private Button btnVolver;
    private Button btnBasico;
    private Button btnIntermedio;
    private Button btnAvanzado;

    private ActividadRepository actividadRepository;
    private ProgresoRepository progresoRepository;

    private Nivel nivelBasico;
    private Nivel nivelIntermedio;
    private Nivel nivelAvanzado;

    private int idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_niveles
        );


        btnVolver =
                findViewById(R.id.btnVolver);

        btnBasico =
                findViewById(R.id.btnBasico);

        btnIntermedio =
                findViewById(R.id.btnIntermedio);

        btnAvanzado =
                findViewById(R.id.btnAvanzado);


        idUsuario =
                getIntent().getIntExtra(
                        "ID_USUARIO",
                        -1
                );


        actividadRepository =
                new ActividadRepository(this);

        progresoRepository =
                new ProgresoRepository(this);


        btnVolver.setOnClickListener(v ->
                finish()
        );


        btnBasico.setOnClickListener(v -> {

            if (nivelBasico != null) {

                abrirNivel(
                        nivelBasico
                );
            }
        });


        btnIntermedio.setOnClickListener(v -> {

            if (nivelIntermedio != null &&
                    btnIntermedio.isEnabled()) {

                abrirNivel(
                        nivelIntermedio
                );

            } else {

                Toast.makeText(
                        this,
                        "Necesita obtener al menos 70% en el nivel Básico.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });


        btnAvanzado.setOnClickListener(v -> {

            if (nivelAvanzado != null &&
                    btnAvanzado.isEnabled()) {

                abrirNivel(
                        nivelAvanzado
                );

            } else {

                Toast.makeText(
                        this,
                        "Necesita obtener al menos 70% en el nivel Intermedio.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });


        cargarNiveles();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (nivelBasico != null) {

            actualizarDesbloqueos();
        }
    }


    private void cargarNiveles() {

        btnBasico.setEnabled(false);
        btnIntermedio.setEnabled(false);
        btnAvanzado.setEnabled(false);


        actividadRepository.obtenerNiveles(
                new RepositoryCallback<List<Nivel>>() {

                    @Override
                    public void onSuccess(
                            List<Nivel> niveles
                    ) {

                        for (Nivel nivel :
                                niveles) {

                            if (nivel.getNombre()
                                    .equalsIgnoreCase(
                                            "Básico"
                                    )) {

                                nivelBasico = nivel;
                            }


                            else if (nivel.getNombre()
                                    .equalsIgnoreCase(
                                            "Intermedio"
                                    )) {

                                nivelIntermedio = nivel;
                            }


                            else if (nivel.getNombre()
                                    .equalsIgnoreCase(
                                            "Avanzado"
                                    )) {

                                nivelAvanzado = nivel;
                            }
                        }


                        btnBasico.setEnabled(
                                nivelBasico != null
                        );


                        actualizarDesbloqueos();
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        Toast.makeText(
                                NivelesActivity.this,
                                "No fue posible cargar los niveles",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    private void actualizarDesbloqueos() {

        if (idUsuario == -1) {
            return;
        }


        /*
         * INTERMEDIO:
         * necesita 70% en BÁSICO.
         */
        if (nivelBasico != null) {

            progresoRepository.obtenerPromedioNivel(
                    idUsuario,
                    nivelBasico.getIdNivel(),
                    new RepositoryCallback<Double>() {

                        @Override
                        public void onSuccess(
                                Double promedio
                        ) {

                            boolean desbloqueado =
                                    promedio >= 70.0;


                            btnIntermedio.setEnabled(
                                    desbloqueado
                            );


                            if (desbloqueado) {

                                btnIntermedio.setText(
                                        "2   Nivel Intermedio"
                                );

                            } else {

                                btnIntermedio.setText(
                                        "🔒 2   Nivel Intermedio"
                                );
                            }
                        }


                        @Override
                        public void onError(
                                Exception e
                        ) {

                            btnIntermedio.setEnabled(
                                    false
                            );
                        }
                    }
            );
        }


        /*
         * AVANZADO:
         * necesita 70% en INTERMEDIO.
         */
        if (nivelIntermedio != null) {

            progresoRepository.obtenerPromedioNivel(
                    idUsuario,
                    nivelIntermedio.getIdNivel(),
                    new RepositoryCallback<Double>() {

                        @Override
                        public void onSuccess(
                                Double promedio
                        ) {

                            boolean desbloqueado =
                                    promedio >= 70.0;


                            btnAvanzado.setEnabled(
                                    desbloqueado
                            );


                            if (desbloqueado) {

                                btnAvanzado.setText(
                                        "3   Nivel Avanzado"
                                );

                            } else {

                                btnAvanzado.setText(
                                        "🔒 3   Nivel Avanzado"
                                );
                            }
                        }


                        @Override
                        public void onError(
                                Exception e
                        ) {

                            btnAvanzado.setEnabled(
                                    false
                            );
                        }
                    }
            );
        }
    }


    private void abrirNivel(
            Nivel nivel
    ) {

        Intent intent =
                new Intent(
                        NivelesActivity.this,
                        ActividadesActivity.class
                );


        intent.putExtra(
                "ID_USUARIO",
                idUsuario
        );


        intent.putExtra(
                "ID_NIVEL",
                nivel.getIdNivel()
        );


        intent.putExtra(
                "NOMBRE_NIVEL",
                nivel.getNombre()
        );


        startActivity(intent);
    }
}