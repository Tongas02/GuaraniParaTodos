package com.helloworld.guaranparatodos.estudiante;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.helloworld.guaranparatodos.repository.ProgresoRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.database.entity.Actividad;
import com.helloworld.guaranparatodos.repository.ActividadRepository;
import com.helloworld.guaranparatodos.repository.RepositoryCallback;

import java.util.ArrayList;
import java.util.List;

public class ActividadesActivity extends AppCompatActivity {

    private TextView txtNivel;
    private TextView txtContador;
    private TextView txtTitulo;
    private TextView txtDescripcion;
    private TextView txtEnunciado;
    private TextView txtResultado;
    private TextView txtSinActividades;
    private ProgresoRepository progresoRepository;

    private int idUsuario;
    private EditText edtRespuesta;

    private LinearLayout contenedorEscritura;
    private LinearLayout contenedorOpciones;

    private CheckBox chkRespuesta1;
    private CheckBox chkRespuesta2;
    private CheckBox chkRespuesta3;
    private CheckBox chkRespuesta4;
    private CheckBox chkRespuesta5;
    private CheckBox chkRespuesta6;

    private Button btnVolver;
    private Button btnComprobar;
    private Button btnAnterior;
    private Button btnSiguiente;

    private ActividadRepository actividadRepository;

    private final List<Actividad> actividades =
            new ArrayList<>();

    private int posicionActual = 0;

    private boolean[] actividadesEvaluadas;
    private boolean[] actividadesCorrectas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_actividades
        );

        inicializarComponentes();


        actividadRepository =
                new ActividadRepository(this);
        progresoRepository =
                new ProgresoRepository(this);

        idUsuario =
                getIntent().getIntExtra(
                        "ID_USUARIO",
                        -1
                );

        int idNivel =
                getIntent().getIntExtra(
                        "ID_NIVEL",
                        -1
                );


        String nombreNivel =
                getIntent().getStringExtra(
                        "NOMBRE_NIVEL"
                );


        if (nombreNivel != null) {

            txtNivel.setText(
                    "Nivel " + nombreNivel
            );
        }


        btnVolver.setOnClickListener(v ->
                finish()
        );


        btnComprobar.setOnClickListener(v ->
                comprobarRespuesta()
        );


        btnAnterior.setOnClickListener(v ->
                actividadAnterior()
        );


        btnSiguiente.setOnClickListener(v ->
                actividadSiguiente()
        );


        if (idNivel != -1) {

            cargarActividades(
                    idNivel
            );
        }
    }


    private void inicializarComponentes() {

        txtNivel =
                findViewById(R.id.txtNivel);

        txtContador =
                findViewById(R.id.txtContador);

        txtTitulo =
                findViewById(R.id.txtTitulo);

        txtDescripcion =
                findViewById(R.id.txtDescripcion);

        txtEnunciado =
                findViewById(R.id.txtEnunciado);

        txtResultado =
                findViewById(R.id.txtResultado);

        txtSinActividades =
                findViewById(R.id.txtSinActividades);


        edtRespuesta =
                findViewById(R.id.edtRespuesta);


        contenedorEscritura =
                findViewById(
                        R.id.contenedorEscritura
                );

        contenedorOpciones =
                findViewById(
                        R.id.contenedorOpciones
                );


        chkRespuesta1 =
                findViewById(R.id.chkRespuesta1);

        chkRespuesta2 =
                findViewById(R.id.chkRespuesta2);

        chkRespuesta3 =
                findViewById(R.id.chkRespuesta3);

        chkRespuesta4 =
                findViewById(R.id.chkRespuesta4);

        chkRespuesta5 =
                findViewById(R.id.chkRespuesta5);

        chkRespuesta6 =
                findViewById(R.id.chkRespuesta6);


        btnVolver =
                findViewById(R.id.btnVolver);

        btnComprobar =
                findViewById(R.id.btnComprobar);

        btnAnterior =
                findViewById(R.id.btnAnterior);

        btnSiguiente =
                findViewById(R.id.btnSiguiente);
    }


    private void cargarActividades(
            int idNivel
    ) {

        actividadRepository
                .obtenerActividadesPorNivel(
                        idNivel,
                        new RepositoryCallback<List<Actividad>>() {

                            @Override
                            public void onSuccess(
                                    List<Actividad> resultado
                            ) {

                                actividades.clear();


                                if (resultado != null) {

                                    actividades.addAll(
                                            resultado
                                    );
                                }


                                if (actividades.isEmpty()) {

                                    mostrarSinActividades();

                                    return;
                                }


                                actividadesEvaluadas =
                                        new boolean[
                                                actividades.size()
                                                ];

                                actividadesCorrectas =
                                        new boolean[
                                                actividades.size()
                                                ];


                                posicionActual = 0;

                                mostrarActividadActual();
                            }


                            @Override
                            public void onError(
                                    Exception e
                            ) {

                                Toast.makeText(
                                        ActividadesActivity.this,
                                        "No fue posible cargar las actividades",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }


    private void mostrarActividadActual() {

        if (actividades.isEmpty()) {

            return;
        }


        txtSinActividades.setVisibility(
                View.GONE
        );


        Actividad actividad =
                actividades.get(
                        posicionActual
                );


        txtContador.setText(
                (posicionActual + 1)
                        + " / "
                        + actividades.size()
        );


        txtTitulo.setText(
                actividad.getTitulo()
        );


        txtDescripcion.setText(
                actividad.getDescripcion()
        );


        txtEnunciado.setText(
                actividad.getEnunciado()
        );


        txtResultado.setText("");

        edtRespuesta.setText("");


        limpiarOpciones();


        String tipo =
                actividad.getTipo();


        if (tipo.equals(
                "SELECCIÓN MÚLTIPLE"
        )) {

            contenedorEscritura.setVisibility(
                    View.GONE
            );

            contenedorOpciones.setVisibility(
                    View.VISIBLE
            );


            configurarOpcion(
                    chkRespuesta1,
                    actividad.getOpcion1()
            );

            configurarOpcion(
                    chkRespuesta2,
                    actividad.getOpcion2()
            );

            configurarOpcion(
                    chkRespuesta3,
                    actividad.getOpcion3()
            );

            configurarOpcion(
                    chkRespuesta4,
                    actividad.getOpcion4()
            );

            configurarOpcion(
                    chkRespuesta5,
                    actividad.getOpcion5()
            );

            configurarOpcion(
                    chkRespuesta6,
                    actividad.getOpcion6()
            );


        } else {

            contenedorOpciones.setVisibility(
                    View.GONE
            );

            contenedorEscritura.setVisibility(
                    View.VISIBLE
            );
        }


        actualizarBotones();
    }


    private void configurarOpcion(
            CheckBox checkBox,
            String texto
    ) {

        checkBox.setChecked(false);


        if (texto == null ||
                texto.trim().isEmpty()) {

            checkBox.setVisibility(
                    View.GONE
            );

        } else {

            checkBox.setText(
                    texto
            );

            checkBox.setVisibility(
                    View.VISIBLE
            );
        }
    }


    private void comprobarRespuesta() {

        if (actividades.isEmpty()) {

            return;
        }


        Actividad actividad =
                actividades.get(
                        posicionActual
                );


        boolean correcta;


        if (actividad.getTipo()
                .equals(
                        "SELECCIÓN MÚLTIPLE"
                )) {

            correcta =
                    comprobarSeleccionMultiple(
                            actividad
                    );


        } else {

            String respuestaUsuario =
                    edtRespuesta
                            .getText()
                            .toString()
                            .trim();


            if (respuestaUsuario.isEmpty()) {

                edtRespuesta.setError(
                        "Ingrese una respuesta"
                );

                edtRespuesta.requestFocus();

                return;
            }


            String respuestaCorrecta =
                    actividad
                            .getRespuestaCorrecta();


            correcta =
                    respuestaCorrecta != null
                            &&
                            respuestaUsuario
                                    .equalsIgnoreCase(
                                            respuestaCorrecta.trim()
                                    );
        }


        actividadesEvaluadas[
                posicionActual
                ] = true;


        actividadesCorrectas[
                posicionActual
                ] = correcta;


        if (correcta) {

            txtResultado.setText(
                    "✓ Respuesta correcta"
            );

        } else {

            txtResultado.setText(
                    "✗ Respuesta incorrecta"
            );
        }
    }


    private boolean comprobarSeleccionMultiple(
            Actividad actividad
    ) {

        if (actividad.getOpcion1() != null &&
                chkRespuesta1.isChecked()
                        != actividad.isOpcion1Correcta()) {

            return false;
        }


        if (actividad.getOpcion2() != null &&
                chkRespuesta2.isChecked()
                        != actividad.isOpcion2Correcta()) {

            return false;
        }


        if (actividad.getOpcion3() != null &&
                chkRespuesta3.isChecked()
                        != actividad.isOpcion3Correcta()) {

            return false;
        }


        if (actividad.getOpcion4() != null &&
                chkRespuesta4.isChecked()
                        != actividad.isOpcion4Correcta()) {

            return false;
        }


        if (actividad.getOpcion5() != null &&
                chkRespuesta5.isChecked()
                        != actividad.isOpcion5Correcta()) {

            return false;
        }


        if (actividad.getOpcion6() != null &&
                chkRespuesta6.isChecked()
                        != actividad.isOpcion6Correcta()) {

            return false;
        }


        return true;
    }


    private void actividadAnterior() {

        if (posicionActual > 0) {

            posicionActual--;

            mostrarActividadActual();
        }
    }


    private void actividadSiguiente() {

        /*
         * No permitimos avanzar sin comprobar
         * primero la actividad actual.
         */
        if (!actividadesEvaluadas[
                posicionActual
                ]) {

            Toast.makeText(
                    this,
                    "Primero compruebe su respuesta.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        if (posicionActual <
                actividades.size() - 1) {

            posicionActual++;

            mostrarActividadActual();

        } else {

            mostrarResultadoFinal();
        }
    }

    private void mostrarResultadoFinal() {

        if (idUsuario == -1) {

            Toast.makeText(
                    this,
                    "No se pudo identificar al estudiante.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        /*
         * Verificamos que TODAS las actividades
         * hayan sido respondidas.
         */
        for (boolean evaluada :
                actividadesEvaluadas) {

            if (!evaluada) {

                Toast.makeText(
                        this,
                        "Debe completar todas las actividades.",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }
        }


        SimpleDateFormat formato =
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm",
                        Locale.getDefault()
                );


        String fecha =
                formato.format(
                        new Date()
                );


        progresoRepository.guardarResultadoNivel(
                idUsuario,
                actividades,
                actividadesCorrectas,
                fecha,
                new RepositoryCallback<Double>() {

                    @Override
                    public void onSuccess(
                            Double porcentaje
                    ) {

                        String mensaje;


                        if (porcentaje >= 70.0) {

                            mensaje =
                                    "¡Nivel aprobado!\n"
                                            + String.format(
                                            Locale.getDefault(),
                                            "%.0f%%",
                                            porcentaje
                                    )
                                            + "\nSe desbloqueó el siguiente nivel.";

                        } else {

                            mensaje =
                                    "Resultado: "
                                            + String.format(
                                            Locale.getDefault(),
                                            "%.0f%%",
                                            porcentaje
                                    )
                                            + "\nNecesita 70% para aprobar.";
                        }


                        Toast.makeText(
                                ActividadesActivity.this,
                                mensaje,
                                Toast.LENGTH_LONG
                        ).show();


                        /*
                         * Volvemos a la pantalla
                         * de niveles.
                         *
                         * onResume() comprobará
                         * si corresponde desbloquear
                         * el siguiente nivel.
                         */
                        finish();
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        Toast.makeText(
                                ActividadesActivity.this,
                                "No fue posible guardar el progreso.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    private void actualizarBotones() {

        btnAnterior.setEnabled(
                posicionActual > 0
        );


        if (posicionActual ==
                actividades.size() - 1) {

            btnSiguiente.setText(
                    "Finalizar"
            );

        } else {

            btnSiguiente.setText(
                    "Siguiente →"
            );
        }
    }


    private void limpiarOpciones() {

        chkRespuesta1.setChecked(false);
        chkRespuesta2.setChecked(false);
        chkRespuesta3.setChecked(false);
        chkRespuesta4.setChecked(false);
        chkRespuesta5.setChecked(false);
        chkRespuesta6.setChecked(false);
    }


    private void mostrarSinActividades() {

        txtSinActividades.setVisibility(
                View.VISIBLE
        );

        txtContador.setText("");
        txtTitulo.setText("");
        txtDescripcion.setText("");
        txtEnunciado.setText("");
        txtResultado.setText("");

        contenedorEscritura.setVisibility(
                View.GONE
        );

        contenedorOpciones.setVisibility(
                View.GONE
        );

        btnComprobar.setVisibility(
                View.GONE
        );

        btnAnterior.setVisibility(
                View.GONE
        );

        btnSiguiente.setVisibility(
                View.GONE
        );
    }
}