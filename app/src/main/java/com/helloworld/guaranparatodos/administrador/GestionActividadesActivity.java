package com.helloworld.guaranparatodos.administrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.database.entity.Actividad;
import com.helloworld.guaranparatodos.database.entity.Nivel;
import com.helloworld.guaranparatodos.repository.ActividadRepository;
import com.helloworld.guaranparatodos.repository.RepositoryCallback;

import java.util.ArrayList;
import java.util.List;

public class GestionActividadesActivity extends AppCompatActivity {

    private Button btnVolver;
    private Button btnGuardarActividad;

    private Spinner spinnerNivel;
    private Spinner spinnerTipo;

    private EditText edtTitulo;
    private EditText edtDescripcion;

    private LinearLayout contenedorCompletar;
    private LinearLayout contenedorRellenar;
    private LinearLayout contenedorSeleccion;

    private EditText edtEnunciadoCompletar;
    private EditText edtRespuestaCompletar;

    private EditText edtEnunciadoRellenar;
    private EditText edtRespuestaRellenar;

    private EditText edtEnunciadoSeleccion;

    private EditText edtOpcion1;
    private EditText edtOpcion2;
    private EditText edtOpcion3;
    private EditText edtOpcion4;
    private EditText edtOpcion5;
    private EditText edtOpcion6;

    private CheckBox chkOpcion1;
    private CheckBox chkOpcion2;
    private CheckBox chkOpcion3;
    private CheckBox chkOpcion4;
    private CheckBox chkOpcion5;
    private CheckBox chkOpcion6;

    private LinearLayout contenedorActividades;
    private TextView txtSinActividades;

    private ActividadRepository actividadRepository;

    private final List<Nivel> listaNiveles =
            new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_gestion_actividades
        );

        inicializarComponentes();

        actividadRepository =
                new ActividadRepository(this);

        configurarTipos();

        cargarNiveles();


        btnVolver.setOnClickListener(v ->
                volverAlMenuAdministrador()
        );


        btnGuardarActividad.setOnClickListener(v ->
                guardarActividad()
        );


        spinnerTipo.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        mostrarFormulario();
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {

                    }
                }
        );


        spinnerNivel.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        cargarActividades();
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {

                    }
                }
        );
    }


    private void inicializarComponentes() {

        btnVolver =
                findViewById(R.id.btnVolver);

        btnGuardarActividad =
                findViewById(R.id.btnGuardarActividad);

        spinnerNivel =
                findViewById(R.id.spinnerNivel);

        spinnerTipo =
                findViewById(R.id.spinnerTipo);

        edtTitulo =
                findViewById(R.id.edtTitulo);

        edtDescripcion =
                findViewById(R.id.edtDescripcion);


        contenedorCompletar =
                findViewById(R.id.contenedorCompletar);

        contenedorRellenar =
                findViewById(R.id.contenedorRellenar);

        contenedorSeleccion =
                findViewById(R.id.contenedorSeleccion);


        edtEnunciadoCompletar =
                findViewById(R.id.edtEnunciadoCompletar);

        edtRespuestaCompletar =
                findViewById(R.id.edtRespuestaCompletar);


        edtEnunciadoRellenar =
                findViewById(R.id.edtEnunciadoRellenar);

        edtRespuestaRellenar =
                findViewById(R.id.edtRespuestaRellenar);


        edtEnunciadoSeleccion =
                findViewById(R.id.edtEnunciadoSeleccion);


        edtOpcion1 =
                findViewById(R.id.edtOpcion1);

        edtOpcion2 =
                findViewById(R.id.edtOpcion2);

        edtOpcion3 =
                findViewById(R.id.edtOpcion3);

        edtOpcion4 =
                findViewById(R.id.edtOpcion4);

        edtOpcion5 =
                findViewById(R.id.edtOpcion5);

        edtOpcion6 =
                findViewById(R.id.edtOpcion6);


        chkOpcion1 =
                findViewById(R.id.chkOpcion1);

        chkOpcion2 =
                findViewById(R.id.chkOpcion2);

        chkOpcion3 =
                findViewById(R.id.chkOpcion3);

        chkOpcion4 =
                findViewById(R.id.chkOpcion4);

        chkOpcion5 =
                findViewById(R.id.chkOpcion5);

        chkOpcion6 =
                findViewById(R.id.chkOpcion6);


        contenedorActividades =
                findViewById(R.id.contenedorActividades);

        txtSinActividades =
                findViewById(R.id.txtSinActividades);
    }


    private void configurarTipos() {

        String[] tipos = {
                "COMPLETAR",
                "RELLENAR",
                "SELECCIÓN MÚLTIPLE"
        };


        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        tipos
                );


        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );


        spinnerTipo.setAdapter(adapter);
    }


    private void cargarNiveles() {

        actividadRepository.obtenerNiveles(
                new RepositoryCallback<List<Nivel>>() {

                    @Override
                    public void onSuccess(
                            List<Nivel> niveles
                    ) {

                        listaNiveles.clear();


                        if (niveles != null) {

                            listaNiveles.addAll(
                                    niveles
                            );
                        }


                        List<String> nombres =
                                new ArrayList<>();


                        for (Nivel nivel :
                                listaNiveles) {

                            nombres.add(
                                    nivel.getNombre()
                            );
                        }


                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<>(
                                        GestionActividadesActivity.this,
                                        android.R.layout.simple_spinner_item,
                                        nombres
                                );


                        adapter.setDropDownViewResource(
                                android.R.layout.simple_spinner_dropdown_item
                        );


                        spinnerNivel.setAdapter(
                                adapter
                        );
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        Toast.makeText(
                                GestionActividadesActivity.this,
                                "No fue posible cargar los niveles",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    private void mostrarFormulario() {

        String tipo =
                spinnerTipo
                        .getSelectedItem()
                        .toString();


        contenedorCompletar.setVisibility(
                View.GONE
        );

        contenedorRellenar.setVisibility(
                View.GONE
        );

        contenedorSeleccion.setVisibility(
                View.GONE
        );


        if (tipo.equals("COMPLETAR")) {

            contenedorCompletar.setVisibility(
                    View.VISIBLE
            );

        } else if (tipo.equals("RELLENAR")) {

            contenedorRellenar.setVisibility(
                    View.VISIBLE
            );

        } else {

            contenedorSeleccion.setVisibility(
                    View.VISIBLE
            );
        }
    }


    private void guardarActividad() {

        String titulo =
                edtTitulo
                        .getText()
                        .toString()
                        .trim();

        String descripcion =
                edtDescripcion
                        .getText()
                        .toString()
                        .trim();


        if (titulo.isEmpty()) {

            edtTitulo.setError(
                    "Ingrese el título"
            );

            edtTitulo.requestFocus();

            return;
        }


        if (descripcion.isEmpty()) {

            edtDescripcion.setError(
                    "Ingrese la descripción"
            );

            edtDescripcion.requestFocus();

            return;
        }


        if (listaNiveles.isEmpty()) {

            Toast.makeText(
                    this,
                    "No hay niveles disponibles",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        int posicionNivel =
                spinnerNivel
                        .getSelectedItemPosition();


        if (posicionNivel < 0 ||
                posicionNivel >= listaNiveles.size()) {

            return;
        }


        Nivel nivel =
                listaNiveles.get(
                        posicionNivel
                );


        String tipo =
                spinnerTipo
                        .getSelectedItem()
                        .toString();


        String enunciado;
        String respuesta = null;


        if (tipo.equals("COMPLETAR")) {

            enunciado =
                    edtEnunciadoCompletar
                            .getText()
                            .toString()
                            .trim();

            respuesta =
                    edtRespuestaCompletar
                            .getText()
                            .toString()
                            .trim();


            if (enunciado.isEmpty()) {

                edtEnunciadoCompletar.setError(
                        "Ingrese el enunciado"
                );

                return;
            }


            if (respuesta.isEmpty()) {

                edtRespuestaCompletar.setError(
                        "Ingrese la respuesta correcta"
                );

                return;
            }


        } else if (tipo.equals("RELLENAR")) {

            enunciado =
                    edtEnunciadoRellenar
                            .getText()
                            .toString()
                            .trim();

            respuesta =
                    edtRespuestaRellenar
                            .getText()
                            .toString()
                            .trim();


            if (enunciado.isEmpty()) {

                edtEnunciadoRellenar.setError(
                        "Ingrese el enunciado"
                );

                return;
            }


            if (respuesta.isEmpty()) {

                edtRespuestaRellenar.setError(
                        "Ingrese la respuesta correcta"
                );

                return;
            }


        } else {

            enunciado =
                    edtEnunciadoSeleccion
                            .getText()
                            .toString()
                            .trim();


            if (enunciado.isEmpty()) {

                edtEnunciadoSeleccion.setError(
                        "Ingrese la pregunta"
                );

                return;
            }
        }


        Actividad actividad =
                new Actividad(
                        titulo,
                        descripcion,
                        tipo,
                        nivel.getIdNivel(),
                        enunciado,
                        respuesta
                );


        if (tipo.equals(
                "SELECCIÓN MÚLTIPLE"
        )) {

            cargarOpciones(
                    actividad
            );


            int cantidadOpciones =
                    contarOpciones(
                            actividad
                    );


            if (cantidadOpciones < 2) {

                Toast.makeText(
                        this,
                        "Ingrese al menos dos opciones",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }


            if (!tieneRespuestaCorrecta(
                    actividad
            )) {

                Toast.makeText(
                        this,
                        "Marque al menos una opción correcta",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }
        }


        btnGuardarActividad.setEnabled(
                false
        );


        actividadRepository.insertar(
                actividad,
                new RepositoryCallback<Long>() {

                    @Override
                    public void onSuccess(
                            Long resultado
                    ) {

                        btnGuardarActividad
                                .setEnabled(
                                        true
                                );


                        Toast.makeText(
                                GestionActividadesActivity.this,
                                "Actividad registrada correctamente",
                                Toast.LENGTH_SHORT
                        ).show();


                        limpiarFormulario();

                        cargarActividades();
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        btnGuardarActividad
                                .setEnabled(
                                        true
                                );


                        Toast.makeText(
                                GestionActividadesActivity.this,
                                "No fue posible guardar la actividad",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    private void cargarOpciones(
            Actividad actividad
    ) {

        actividad.setOpcion1(
                texto(edtOpcion1)
        );

        actividad.setOpcion2(
                texto(edtOpcion2)
        );

        actividad.setOpcion3(
                texto(edtOpcion3)
        );

        actividad.setOpcion4(
                texto(edtOpcion4)
        );

        actividad.setOpcion5(
                texto(edtOpcion5)
        );

        actividad.setOpcion6(
                texto(edtOpcion6)
        );


        actividad.setOpcion1Correcta(
                chkOpcion1.isChecked()
        );

        actividad.setOpcion2Correcta(
                chkOpcion2.isChecked()
        );

        actividad.setOpcion3Correcta(
                chkOpcion3.isChecked()
        );

        actividad.setOpcion4Correcta(
                chkOpcion4.isChecked()
        );

        actividad.setOpcion5Correcta(
                chkOpcion5.isChecked()
        );

        actividad.setOpcion6Correcta(
                chkOpcion6.isChecked()
        );
    }


    private String texto(
            EditText campo
    ) {

        String valor =
                campo.getText()
                        .toString()
                        .trim();


        if (valor.isEmpty()) {

            return null;
        }


        return valor;
    }


    private int contarOpciones(
            Actividad actividad
    ) {

        int cantidad = 0;


        if (actividad.getOpcion1() != null) {
            cantidad++;
        }

        if (actividad.getOpcion2() != null) {
            cantidad++;
        }

        if (actividad.getOpcion3() != null) {
            cantidad++;
        }

        if (actividad.getOpcion4() != null) {
            cantidad++;
        }

        if (actividad.getOpcion5() != null) {
            cantidad++;
        }

        if (actividad.getOpcion6() != null) {
            cantidad++;
        }


        return cantidad;
    }


    private boolean tieneRespuestaCorrecta(
            Actividad actividad
    ) {

        return actividad.isOpcion1Correcta()
                || actividad.isOpcion2Correcta()
                || actividad.isOpcion3Correcta()
                || actividad.isOpcion4Correcta()
                || actividad.isOpcion5Correcta()
                || actividad.isOpcion6Correcta();
    }


    private void cargarActividades() {

        if (listaNiveles.isEmpty()) {

            return;
        }


        int posicion =
                spinnerNivel
                        .getSelectedItemPosition();


        if (posicion < 0 ||
                posicion >= listaNiveles.size()) {

            return;
        }


        Nivel nivel =
                listaNiveles.get(
                        posicion
                );


        actividadRepository.obtenerActividadesPorNivel(
                nivel.getIdNivel(),
                new RepositoryCallback<List<Actividad>>() {

                    @Override
                    public void onSuccess(
                            List<Actividad> actividades
                    ) {

                        mostrarActividades(
                                actividades
                        );
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        txtSinActividades
                                .setVisibility(
                                        View.VISIBLE
                                );
                    }
                }
        );
    }


    private void mostrarActividades(
            List<Actividad> actividades
    ) {

        contenedorActividades
                .removeAllViews();


        if (actividades == null ||
                actividades.isEmpty()) {

            txtSinActividades.setVisibility(
                    View.VISIBLE
            );

            return;
        }


        txtSinActividades.setVisibility(
                View.GONE
        );


        for (Actividad actividad :
                actividades) {

            TextView item =
                    new TextView(this);


            item.setText(
                    actividad.getTitulo()
                            + "\nTipo: "
                            + actividad.getTipo()
                            + "\n"
                            + actividad.getEnunciado()
            );


            item.setTextSize(
                    17
            );


            item.setPadding(
                    15,
                    20,
                    15,
                    20
            );


            contenedorActividades.addView(
                    item
            );
        }
    }


    private void limpiarFormulario() {

        edtTitulo.setText("");
        edtDescripcion.setText("");

        edtEnunciadoCompletar.setText("");
        edtRespuestaCompletar.setText("");

        edtEnunciadoRellenar.setText("");
        edtRespuestaRellenar.setText("");

        edtEnunciadoSeleccion.setText("");

        edtOpcion1.setText("");
        edtOpcion2.setText("");
        edtOpcion3.setText("");
        edtOpcion4.setText("");
        edtOpcion5.setText("");
        edtOpcion6.setText("");

        chkOpcion1.setChecked(false);
        chkOpcion2.setChecked(false);
        chkOpcion3.setChecked(false);
        chkOpcion4.setChecked(false);
        chkOpcion5.setChecked(false);
        chkOpcion6.setChecked(false);

        edtTitulo.requestFocus();
    }


    private void volverAlMenuAdministrador() {

        Intent intent =
                new Intent(
                        GestionActividadesActivity.this,
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