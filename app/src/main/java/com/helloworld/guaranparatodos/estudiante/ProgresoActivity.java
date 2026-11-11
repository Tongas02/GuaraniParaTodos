package com.helloworld.guaranparatodos.estudiante;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.database.entity.Nivel;
import com.helloworld.guaranparatodos.repository.ActividadRepository;
import com.helloworld.guaranparatodos.repository.ProgresoRepository;
import com.helloworld.guaranparatodos.repository.RepositoryCallback;

import java.util.List;
import java.util.Locale;

public class ProgresoActivity extends AppCompatActivity {

    private Button btnVolver;

    private ProgressBar progresoBasico;
    private ProgressBar progresoIntermedio;
    private ProgressBar progresoAvanzado;

    private TextView txtProgresoBasico;
    private TextView txtProgresoIntermedio;
    private TextView txtProgresoAvanzado;

    private TextView txtEstadoBasico;
    private TextView txtEstadoIntermedio;
    private TextView txtEstadoAvanzado;

    private TextView txtActividadesCompletadas;

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
                R.layout.activity_progreso
        );

        inicializarComponentes();


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


        if (idUsuario == -1) {

            Toast.makeText(
                    this,
                    "No se pudo identificar al estudiante.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        cargarNiveles();

        cargarCantidadCompletadas();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (nivelBasico != null) {

            cargarProgreso();
        }
    }


    private void inicializarComponentes() {

        btnVolver =
                findViewById(
                        R.id.btnVolver
                );


        progresoBasico =
                findViewById(
                        R.id.progresoBasico
                );

        progresoIntermedio =
                findViewById(
                        R.id.progresoIntermedio
                );

        progresoAvanzado =
                findViewById(
                        R.id.progresoAvanzado
                );


        txtProgresoBasico =
                findViewById(
                        R.id.txtProgresoBasico
                );

        txtProgresoIntermedio =
                findViewById(
                        R.id.txtProgresoIntermedio
                );

        txtProgresoAvanzado =
                findViewById(
                        R.id.txtProgresoAvanzado
                );


        txtEstadoBasico =
                findViewById(
                        R.id.txtEstadoBasico
                );

        txtEstadoIntermedio =
                findViewById(
                        R.id.txtEstadoIntermedio
                );

        txtEstadoAvanzado =
                findViewById(
                        R.id.txtEstadoAvanzado
                );


        txtActividadesCompletadas =
                findViewById(
                        R.id.txtActividadesCompletadas
                );
    }


    private void cargarNiveles() {

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

                                nivelBasico =
                                        nivel;
                            }


                            else if (nivel.getNombre()
                                    .equalsIgnoreCase(
                                            "Intermedio"
                                    )) {

                                nivelIntermedio =
                                        nivel;
                            }


                            else if (nivel.getNombre()
                                    .equalsIgnoreCase(
                                            "Avanzado"
                                    )) {

                                nivelAvanzado =
                                        nivel;
                            }
                        }


                        cargarProgreso();
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        Toast.makeText(
                                ProgresoActivity.this,
                                "No fue posible cargar los niveles.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    private void cargarProgreso() {

        if (nivelBasico == null) {
            return;
        }


        progresoRepository.obtenerPromedioNivel(
                idUsuario,
                nivelBasico.getIdNivel(),
                new RepositoryCallback<Double>() {

                    @Override
                    public void onSuccess(
                            Double promedioBasico
                    ) {

                        mostrarBasico(
                                promedioBasico
                        );


                        boolean intermedioDesbloqueado =
                                promedioBasico >= 70.0;


                        if (!intermedioDesbloqueado) {

                            mostrarIntermedioBloqueado();

                            mostrarAvanzadoBloqueado();

                            return;
                        }


                        cargarIntermedio();
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        mostrarBasico(
                                0.0
                        );
                    }
                }
        );
    }


    private void cargarIntermedio() {

        if (nivelIntermedio == null) {

            return;
        }


        progresoRepository.obtenerPromedioNivel(
                idUsuario,
                nivelIntermedio.getIdNivel(),
                new RepositoryCallback<Double>() {

                    @Override
                    public void onSuccess(
                            Double promedioIntermedio
                    ) {

                        mostrarIntermedio(
                                promedioIntermedio
                        );


                        boolean avanzadoDesbloqueado =
                                promedioIntermedio >= 70.0;


                        if (!avanzadoDesbloqueado) {

                            mostrarAvanzadoBloqueado();

                            return;
                        }


                        cargarAvanzado();
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        mostrarIntermedio(
                                0.0
                        );

                        mostrarAvanzadoBloqueado();
                    }
                }
        );
    }


    private void cargarAvanzado() {

        if (nivelAvanzado == null) {

            return;
        }


        progresoRepository.obtenerPromedioNivel(
                idUsuario,
                nivelAvanzado.getIdNivel(),
                new RepositoryCallback<Double>() {

                    @Override
                    public void onSuccess(
                            Double promedio
                    ) {

                        mostrarAvanzado(
                                promedio
                        );
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        mostrarAvanzado(
                                0.0
                        );
                    }
                }
        );
    }


    private void mostrarBasico(
            double promedio
    ) {

        int porcentaje =
                (int) Math.round(
                        promedio
                );


        progresoBasico.setProgress(
                porcentaje
        );


        txtProgresoBasico.setText(
                porcentaje + "%"
        );


        if (porcentaje >= 70) {

            txtEstadoBasico.setText(
                    "✅ Aprobado"
            );

        } else if (porcentaje > 0) {

            txtEstadoBasico.setText(
                    "En progreso"
            );

        } else {

            txtEstadoBasico.setText(
                    "Pendiente"
            );
        }
    }


    private void mostrarIntermedio(
            double promedio
    ) {

        int porcentaje =
                (int) Math.round(
                        promedio
                );


        progresoIntermedio.setProgress(
                porcentaje
        );


        txtProgresoIntermedio.setText(
                porcentaje + "%"
        );


        if (porcentaje >= 70) {

            txtEstadoIntermedio.setText(
                    "✅ Aprobado"
            );

        } else if (porcentaje > 0) {

            txtEstadoIntermedio.setText(
                    "En progreso"
            );

        } else {

            txtEstadoIntermedio.setText(
                    "Disponible"
            );
        }
    }


    private void mostrarAvanzado(
            double promedio
    ) {

        int porcentaje =
                (int) Math.round(
                        promedio
                );


        progresoAvanzado.setProgress(
                porcentaje
        );


        txtProgresoAvanzado.setText(
                porcentaje + "%"
        );


        if (porcentaje >= 70) {

            txtEstadoAvanzado.setText(
                    "✅ Aprobado"
            );

        } else if (porcentaje > 0) {

            txtEstadoAvanzado.setText(
                    "En progreso"
            );

        } else {

            txtEstadoAvanzado.setText(
                    "Disponible"
            );
        }
    }


    private void mostrarIntermedioBloqueado() {

        progresoIntermedio.setProgress(
                0
        );

        txtProgresoIntermedio.setText(
                "0%"
        );

        txtEstadoIntermedio.setText(
                "🔒 Bloqueado"
        );
    }


    private void mostrarAvanzadoBloqueado() {

        progresoAvanzado.setProgress(
                0
        );

        txtProgresoAvanzado.setText(
                "0%"
        );

        txtEstadoAvanzado.setText(
                "🔒 Bloqueado"
        );
    }


    private void cargarCantidadCompletadas() {

        progresoRepository.contarCompletadas(
                idUsuario,
                new RepositoryCallback<Integer>() {

                    @Override
                    public void onSuccess(
                            Integer cantidad
                    ) {

                        txtActividadesCompletadas
                                .setText(
                                        "Actividades completadas: "
                                                + cantidad
                                );
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        txtActividadesCompletadas
                                .setText(
                                        "Actividades completadas: 0"
                                );
                    }
                }
        );
    }
}