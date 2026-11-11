package com.helloworld.guaranparatodos.estudiante;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.database.entity.Conjugacion;
import com.helloworld.guaranparatodos.database.entity.Ejemplo;
import com.helloworld.guaranparatodos.database.entity.Palabra;
import com.helloworld.guaranparatodos.repository.DiccionarioRepository;
import com.helloworld.guaranparatodos.repository.RepositoryCallback;

import java.io.File;
import java.util.List;

public class DiccionarioActivity extends AppCompatActivity {

    private EditText edtBuscarPalabra;

    private Button btnBuscar;
    private Button btnReproducirAudio;
    private Button btnVolver;

    private LinearLayout contenedorResultado;

    private TextView txtMensaje;
    private TextView txtPalabraBuscada;
    private TextView txtTipoSignificado;
    private TextView txtSignificado;
    private TextView txtEjemplos;
    private TextView txtSinConjugacion;

    private HorizontalScrollView scrollConjugacion;
    private TableLayout tablaConjugacion;

    private DiccionarioRepository diccionarioRepository;

    private Palabra palabraActual;

    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_diccionario
        );

        inicializarComponentes();

        diccionarioRepository =
                new DiccionarioRepository(this);


        btnBuscar.setOnClickListener(v ->
                buscarPalabra()
        );


        btnReproducirAudio.setOnClickListener(v ->
                reproducirAudio()
        );


        btnVolver.setOnClickListener(v -> {

            liberarAudio();

            finish();
        });
    }


    private void inicializarComponentes() {

        edtBuscarPalabra =
                findViewById(
                        R.id.edtBuscarPalabra
                );


        btnBuscar =
                findViewById(
                        R.id.btnBuscar
                );


        btnReproducirAudio =
                findViewById(
                        R.id.btnReproducirAudio
                );


        btnVolver =
                findViewById(
                        R.id.btnVolver
                );


        contenedorResultado =
                findViewById(
                        R.id.contenedorResultado
                );


        txtMensaje =
                findViewById(
                        R.id.txtMensaje
                );


        txtPalabraBuscada =
                findViewById(
                        R.id.txtPalabraBuscada
                );


        txtTipoSignificado =
                findViewById(
                        R.id.txtTipoSignificado
                );


        txtSignificado =
                findViewById(
                        R.id.txtSignificado
                );


        txtEjemplos =
                findViewById(
                        R.id.txtEjemplos
                );


        txtSinConjugacion =
                findViewById(
                        R.id.txtSinConjugacion
                );


        scrollConjugacion =
                findViewById(
                        R.id.scrollConjugacion
                );


        tablaConjugacion =
                findViewById(
                        R.id.tablaConjugacion
                );


        /*
         * Al principio no se puede reproducir
         * porque todavía no hay palabra seleccionada.
         */
        btnReproducirAudio.setEnabled(false);

        btnReproducirAudio.setText(
                "▶ Reproducir audio"
        );
    }


    private void buscarPalabra() {

        String texto =
                edtBuscarPalabra
                        .getText()
                        .toString()
                        .trim();


        if (texto.isEmpty()) {

            edtBuscarPalabra.setError(
                    "Ingrese una palabra"
            );

            edtBuscarPalabra.requestFocus();

            return;
        }


        /*
         * Detenemos cualquier audio anterior.
         */
        liberarAudio();


        palabraActual = null;


        btnReproducirAudio.setEnabled(
                false
        );


        contenedorResultado.setVisibility(
                View.GONE
        );


        txtMensaje.setText(
                "Buscando..."
        );


        diccionarioRepository.buscar(
                texto,
                new RepositoryCallback<List<Palabra>>() {

                    @Override
                    public void onSuccess(
                            List<Palabra> palabras
                    ) {

                        if (palabras == null ||
                                palabras.isEmpty()) {

                            txtMensaje.setText(
                                    "No se encontraron resultados."
                            );

                            return;
                        }


                        palabraActual =
                                seleccionarMejorResultado(
                                        palabras,
                                        texto
                                );


                        txtMensaje.setText(
                                ""
                        );


                        mostrarResultado(
                                palabraActual,
                                texto
                        );
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        txtMensaje.setText(
                                ""
                        );


                        Toast.makeText(
                                DiccionarioActivity.this,
                                "No fue posible realizar la búsqueda.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }


    private Palabra seleccionarMejorResultado(
            List<Palabra> palabras,
            String busqueda
    ) {

        for (Palabra palabra :
                palabras) {

            if (palabra
                    .getPalabraGuarani()
                    .equalsIgnoreCase(
                            busqueda
                    )) {

                return palabra;
            }


            if (palabra
                    .getPalabraEspanol()
                    .equalsIgnoreCase(
                            busqueda
                    )) {

                return palabra;
            }
        }


        return palabras.get(0);
    }


    private void mostrarResultado(
            Palabra palabra,
            String busqueda
    ) {

        contenedorResultado.setVisibility(
                View.VISIBLE
        );


        boolean buscoEnEspanol =
                palabra
                        .getPalabraEspanol()
                        .equalsIgnoreCase(
                                busqueda
                        );


        if (buscoEnEspanol) {

            txtPalabraBuscada.setText(
                    palabra.getPalabraEspanol()
            );


            txtTipoSignificado.setText(
                    "Significado en guaraní:"
            );


            txtSignificado.setText(
                    palabra.getPalabraGuarani()
            );


        } else {

            txtPalabraBuscada.setText(
                    palabra.getPalabraGuarani()
            );


            txtTipoSignificado.setText(
                    "Significado en español:"
            );


            txtSignificado.setText(
                    palabra.getPalabraEspanol()
            );
        }


        /*
         * COMPROBAR AUDIO
         */
        String rutaAudio =
                palabra.getAudio();


        boolean tieneAudio =
                rutaAudio != null &&
                        !rutaAudio.trim().isEmpty();


        btnReproducirAudio.setEnabled(
                tieneAudio
        );


        if (tieneAudio) {

            btnReproducirAudio.setText(
                    "▶ Reproducir audio"
            );

        } else {

            btnReproducirAudio.setText(
                    "Sin audio disponible"
            );
        }


        cargarEjemplos(
                palabra.getIdPalabra()
        );


        cargarConjugaciones(
                palabra.getIdPalabra()
        );
    }


    private void cargarEjemplos(
            int idPalabra
    ) {

        txtEjemplos.setText(
                "Cargando..."
        );


        diccionarioRepository.obtenerEjemplos(
                idPalabra,
                new RepositoryCallback<List<Ejemplo>>() {

                    @Override
                    public void onSuccess(
                            List<Ejemplo> ejemplos
                    ) {

                        if (ejemplos == null ||
                                ejemplos.isEmpty()) {

                            txtEjemplos.setText(
                                    "No hay ejemplos disponibles."
                            );

                            return;
                        }


                        StringBuilder resultado =
                                new StringBuilder();


                        int cantidad =
                                Math.min(
                                        ejemplos.size(),
                                        3
                                );


                        for (int i = 0;
                             i < cantidad;
                             i++) {

                            resultado
                                    .append(i + 1)
                                    .append(". ")
                                    .append(
                                            ejemplos
                                                    .get(i)
                                                    .getEjemplo()
                                    )
                                    .append("\n\n");
                        }


                        txtEjemplos.setText(
                                resultado
                                        .toString()
                                        .trim()
                        );
                    }


                    @Override
                    public void onError(
                            Exception e
                    ) {

                        txtEjemplos.setText(
                                "No fue posible cargar los ejemplos."
                        );
                    }
                }
        );
    }


    private void cargarConjugaciones(
            int idPalabra
    ) {

        tablaConjugacion.removeAllViews();


        txtSinConjugacion.setVisibility(
                View.GONE
        );


        scrollConjugacion.setVisibility(
                View.GONE
        );


        diccionarioRepository
                .obtenerConjugaciones(
                        idPalabra,
                        new RepositoryCallback<List<Conjugacion>>() {

                            @Override
                            public void onSuccess(
                                    List<Conjugacion> conjugaciones
                            ) {

                                if (conjugaciones == null ||
                                        conjugaciones.isEmpty()) {

                                    txtSinConjugacion.setText(
                                            "Esta palabra no posee conjugación."
                                    );


                                    txtSinConjugacion.setVisibility(
                                            View.VISIBLE
                                    );

                                    return;
                                }


                                crearEncabezadoTabla();


                                for (Conjugacion conjugacion :
                                        conjugaciones) {

                                    agregarFilaConjugacion(
                                            conjugacion
                                    );
                                }


                                scrollConjugacion.setVisibility(
                                        View.VISIBLE
                                );
                            }


                            @Override
                            public void onError(
                                    Exception e
                            ) {

                                txtSinConjugacion.setText(
                                        "No fue posible cargar la conjugación."
                                );


                                txtSinConjugacion.setVisibility(
                                        View.VISIBLE
                                );
                            }
                        }
                );
    }


    private void crearEncabezadoTabla() {

        TableRow fila =
                new TableRow(this);


        fila.addView(
                crearCelda(
                        "PRONOMBRE",
                        true
                )
        );


        fila.addView(
                crearCelda(
                        "PASADO",
                        true
                )
        );


        fila.addView(
                crearCelda(
                        "PRESENTE",
                        true
                )
        );


        fila.addView(
                crearCelda(
                        "FUTURO",
                        true
                )
        );


        tablaConjugacion.addView(
                fila
        );
    }


    private void agregarFilaConjugacion(
            Conjugacion conjugacion
    ) {

        TableRow fila =
                new TableRow(this);


        fila.addView(
                crearCelda(
                        conjugacion.getPronombre(),
                        true
                )
        );


        fila.addView(
                crearCelda(
                        conjugacion.getPasado(),
                        false
                )
        );


        fila.addView(
                crearCelda(
                        conjugacion.getPresente(),
                        false
                )
        );


        fila.addView(
                crearCelda(
                        conjugacion.getFuturo(),
                        false
                )
        );


        tablaConjugacion.addView(
                fila
        );
    }


    private TextView crearCelda(
            String texto,
            boolean negrita
    ) {

        TextView celda =
                new TextView(this);


        celda.setText(
                texto
        );


        celda.setTextSize(
                15
        );


        celda.setPadding(
                24,
                18,
                24,
                18
        );


        celda.setGravity(
                Gravity.CENTER_VERTICAL
        );


        celda.setMinWidth(
                220
        );


        if (negrita) {

            celda.setTypeface(
                    null,
                    Typeface.BOLD
            );
        }


        return celda;
    }


    /*
     * REPRODUCIR MP3 GUARDADO LOCALMENTE
     */
    private void reproducirAudio() {

        if (palabraActual == null) {

            Toast.makeText(
                    this,
                    "Primero busque una palabra.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        /*
         * Si ya existe un audio reproduciéndose,
         * al volver a tocar el botón lo detenemos.
         */
        if (mediaPlayer != null) {

            liberarAudio();

            return;
        }


        String rutaAudio =
                palabraActual.getAudio();


        if (rutaAudio == null ||
                rutaAudio.trim().isEmpty()) {

            Toast.makeText(
                    this,
                    "Esta palabra no posee audio.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        rutaAudio =
                rutaAudio.trim();


        /*
         * El administrador guardó la ruta
         * completa del archivo MP3.
         */
        File archivoAudio =
                new File(
                        rutaAudio
                );


        if (!archivoAudio.exists()) {

            Toast.makeText(
                    this,
                    "No se encontró el archivo de pronunciación.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        try {

            mediaPlayer =
                    new MediaPlayer();


            mediaPlayer.setDataSource(
                    archivoAudio.getAbsolutePath()
            );


            mediaPlayer.prepare();


            mediaPlayer.setOnCompletionListener(
                    mp -> liberarAudio()
            );


            mediaPlayer.setOnErrorListener(
                    (mp, what, extra) -> {

                        liberarAudio();

                        Toast.makeText(
                                DiccionarioActivity.this,
                                "Ocurrió un error al reproducir el audio.",
                                Toast.LENGTH_LONG
                        ).show();

                        return true;
                    }
            );


            mediaPlayer.start();


            btnReproducirAudio.setText(
                    "■ Detener audio"
            );


        } catch (Exception e) {

            liberarAudio();


            Toast.makeText(
                    this,
                    "No fue posible reproducir el audio.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    /*
     * DETENER Y LIBERAR AUDIO
     */
    private void liberarAudio() {

        if (mediaPlayer != null) {

            try {

                if (mediaPlayer.isPlaying()) {

                    mediaPlayer.stop();
                }

            } catch (Exception ignored) {

            }


            try {

                mediaPlayer.release();

            } catch (Exception ignored) {

            }


            mediaPlayer = null;
        }


        if (btnReproducirAudio != null) {

            if (palabraActual != null &&
                    palabraActual.getAudio() != null &&
                    !palabraActual
                            .getAudio()
                            .trim()
                            .isEmpty()) {

                btnReproducirAudio.setText(
                        "▶ Reproducir audio"
                );

            } else {

                btnReproducirAudio.setText(
                        "Sin audio disponible"
                );
            }
        }
    }


    @Override
    protected void onDestroy() {

        liberarAudio();

        super.onDestroy();
    }
}