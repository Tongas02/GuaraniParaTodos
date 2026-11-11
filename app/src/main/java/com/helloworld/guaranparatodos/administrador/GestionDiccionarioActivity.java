package com.helloworld.guaranparatodos.administrador;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.guaranparatodos.R;
import com.helloworld.guaranparatodos.database.entity.Conjugacion;
import com.helloworld.guaranparatodos.database.entity.Palabra;
import com.helloworld.guaranparatodos.repository.DiccionarioRepository;
import com.helloworld.guaranparatodos.repository.RepositoryCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GestionDiccionarioActivity extends AppCompatActivity {

    private EditText edtGuarani;
    private EditText edtEspanol;

    private EditText edtEjemplo1;
    private EditText edtEjemplo2;
    private EditText edtEjemplo3;

    private Button btnVolver;
    private Button btnGuardarPalabra;

    private Button btnSeleccionarAudio;
    private Button btnEscucharAudio;

    private TextView txtAudioSeleccionado;

    private CheckBox chkConjugacion;

    private LinearLayout contenedorConjugacion;

    private DiccionarioRepository diccionarioRepository;

    /*
     * URI del archivo MP3 seleccionado.
     */
    private Uri audioUriSeleccionado;

    /*
     * Nombre visible del archivo.
     * Ejemplo: oga.mp3
     */
    private String nombreAudioSeleccionado;

    /*
     * Reproductor usado para escuchar
     * el audio antes de guardarlo.
     */
    private MediaPlayer mediaPlayer;


    private final List<EditText[]> camposConjugacion =
            new ArrayList<>();


    private final String[] pronombres = {
            "Che / Yo",
            "Nde / Tú",
            "Ha'e / Él/Ella",
            "Ñande / Nosotros",
            "Ore / Nosotros",
            "Peẽ / Ustedes",
            "Ha'ekuéra / Ellos"
    };


    /*
     * Selector de archivos de Android.
     *
     * Se utilizará cuando el administrador
     * presione "Seleccionar archivo MP3".
     */
    private final ActivityResultLauncher<String[]> selectorAudio =
            registerForActivityResult(
                    new ActivityResultContracts.OpenDocument(),
                    uri -> {

                        if (uri == null) {
                            return;
                        }

                        procesarAudioSeleccionado(uri);
                    }
            );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_gestion_diccionario
        );


        inicializarComponentes();


        diccionarioRepository =
                new DiccionarioRepository(this);


        crearCamposConjugacion();


        /*
         * BOTÓN VOLVER
         */
        btnVolver.setOnClickListener(v ->
                volverAlMenuAdministrador()
        );


        /*
         * SELECCIONAR MP3
         */
        btnSeleccionarAudio.setOnClickListener(v ->
                abrirSelectorAudio()
        );


        /*
         * ESCUCHAR AUDIO SELECCIONADO
         */
        btnEscucharAudio.setOnClickListener(v ->
                escucharAudioSeleccionado()
        );


        /*
         * MOSTRAR / OCULTAR CONJUGACIÓN
         */
        chkConjugacion.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {

                        contenedorConjugacion.setVisibility(
                                View.VISIBLE
                        );

                    } else {

                        contenedorConjugacion.setVisibility(
                                View.GONE
                        );
                    }
                }
        );


        /*
         * GUARDAR PALABRA
         */
        btnGuardarPalabra.setOnClickListener(v ->
                guardarPalabra()
        );
    }


    private void inicializarComponentes() {

        btnVolver =
                findViewById(
                        R.id.btnVolver
                );


        edtGuarani =
                findViewById(
                        R.id.edtGuarani
                );


        edtEspanol =
                findViewById(
                        R.id.edtEspanol
                );


        /*
         * NUEVOS CONTROLES DEL AUDIO
         */
        btnSeleccionarAudio =
                findViewById(
                        R.id.btnSeleccionarAudio
                );


        txtAudioSeleccionado =
                findViewById(
                        R.id.txtAudioSeleccionado
                );


        btnEscucharAudio =
                findViewById(
                        R.id.btnEscucharAudio
                );


        edtEjemplo1 =
                findViewById(
                        R.id.edtEjemplo1
                );


        edtEjemplo2 =
                findViewById(
                        R.id.edtEjemplo2
                );


        edtEjemplo3 =
                findViewById(
                        R.id.edtEjemplo3
                );


        chkConjugacion =
                findViewById(
                        R.id.chkConjugacion
                );


        contenedorConjugacion =
                findViewById(
                        R.id.contenedorConjugacion
                );


        btnGuardarPalabra =
                findViewById(
                        R.id.btnGuardarPalabra
                );
    }


    /*
     * ABRIR EXPLORADOR DE ARCHIVOS
     */
    private void abrirSelectorAudio() {

        selectorAudio.launch(
                new String[]{
                        "audio/mpeg",
                        "audio/*"
                }
        );
    }


    /*
     * PROCESAR ARCHIVO SELECCIONADO
     */
    private void procesarAudioSeleccionado(
            Uri uri
    ) {

        String nombreArchivo =
                obtenerNombreArchivo(uri);


        String tipoMime =
                getContentResolver()
                        .getType(uri);


        boolean esMp3PorNombre =
                nombreArchivo != null
                        &&
                        nombreArchivo
                                .toLowerCase()
                                .endsWith(".mp3");


        boolean esMp3PorMime =
                tipoMime != null
                        &&
                        tipoMime.equalsIgnoreCase(
                                "audio/mpeg"
                        );


        /*
         * Solo aceptamos MP3.
         */
        if (!esMp3PorNombre &&
                !esMp3PorMime) {

            Toast.makeText(
                    this,
                    "Seleccione un archivo MP3.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        /*
         * Detenemos cualquier audio anterior.
         */
        detenerAudio();


        audioUriSeleccionado =
                uri;


        nombreAudioSeleccionado =
                nombreArchivo;


        /*
         * Intentamos mantener permiso
         * para leer este archivo.
         */
        try {

            getContentResolver()
                    .takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

        } catch (Exception ignored) {

        }


        if (nombreAudioSeleccionado == null ||
                nombreAudioSeleccionado.isEmpty()) {

            nombreAudioSeleccionado =
                    "audio.mp3";
        }


        txtAudioSeleccionado.setText(
                "Archivo seleccionado: "
                        + nombreAudioSeleccionado
        );


        btnEscucharAudio.setEnabled(
                true
        );


        btnEscucharAudio.setText(
                "▶ Escuchar audio"
        );


        Toast.makeText(
                this,
                "Audio seleccionado correctamente",
                Toast.LENGTH_SHORT
        ).show();
    }


    /*
     * OBTENER NOMBRE REAL DEL ARCHIVO
     */
    private String obtenerNombreArchivo(
            Uri uri
    ) {

        String nombre = null;


        Cursor cursor = null;


        try {

            cursor =
                    getContentResolver()
                            .query(
                                    uri,
                                    null,
                                    null,
                                    null,
                                    null
                            );


            if (cursor != null &&
                    cursor.moveToFirst()) {

                int indiceNombre =
                        cursor.getColumnIndex(
                                OpenableColumns.DISPLAY_NAME
                        );


                if (indiceNombre >= 0) {

                    nombre =
                            cursor.getString(
                                    indiceNombre
                            );
                }
            }

        } catch (Exception ignored) {

        } finally {

            if (cursor != null) {

                cursor.close();
            }
        }


        return nombre;
    }


    /*
     * REPRODUCIR AUDIO ANTES DE GUARDAR
     */
    private void escucharAudioSeleccionado() {

        if (audioUriSeleccionado == null) {

            Toast.makeText(
                    this,
                    "Primero seleccione un archivo MP3.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }


        /*
         * Si ya se está reproduciendo,
         * el botón funciona como detener.
         */
        if (mediaPlayer != null &&
                mediaPlayer.isPlaying()) {

            detenerAudio();

            return;
        }


        detenerAudio();


        try {

            mediaPlayer =
                    MediaPlayer.create(
                            this,
                            audioUriSeleccionado
                    );


            if (mediaPlayer == null) {

                Toast.makeText(
                        this,
                        "No fue posible reproducir el audio.",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }


            mediaPlayer.setOnCompletionListener(mp -> {

                detenerAudio();
            });


            mediaPlayer.start();


            btnEscucharAudio.setText(
                    "■ Detener audio"
            );


        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "No fue posible reproducir el archivo MP3.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }


    /*
     * DETENER Y LIBERAR MEDIAPLAYER
     */
    private void detenerAudio() {

        if (mediaPlayer != null) {

            try {

                if (mediaPlayer.isPlaying()) {

                    mediaPlayer.stop();
                }

            } catch (Exception ignored) {

            }


            mediaPlayer.release();

            mediaPlayer = null;
        }


        if (btnEscucharAudio != null) {

            btnEscucharAudio.setText(
                    "▶ Escuchar audio"
            );
        }
    }


    private void volverAlMenuAdministrador() {

        detenerAudio();


        Intent intent =
                new Intent(
                        GestionDiccionarioActivity.this,
                        MenuAdministradorActivity.class
                );


        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );


        startActivity(intent);

        finish();
    }


    /*
     * CAMPOS DE CONJUGACIÓN
     */
    private void crearCamposConjugacion() {

        camposConjugacion.clear();

        contenedorConjugacion.removeAllViews();


        for (String pronombre :
                pronombres) {

            TextView txtPronombre =
                    new TextView(this);


            txtPronombre.setText(
                    pronombre
            );


            txtPronombre.setTextSize(
                    17
            );


            txtPronombre.setPadding(
                    0,
                    20,
                    0,
                    5
            );


            contenedorConjugacion.addView(
                    txtPronombre
            );


            /*
             * PASADO
             */
            EditText edtPasado =
                    new EditText(this);


            edtPasado.setHint(
                    "Pasado"
            );


            contenedorConjugacion.addView(
                    edtPasado
            );


            /*
             * PRESENTE
             */
            EditText edtPresente =
                    new EditText(this);


            edtPresente.setHint(
                    "Presente"
            );


            contenedorConjugacion.addView(
                    edtPresente
            );


            /*
             * FUTURO
             */
            EditText edtFuturo =
                    new EditText(this);


            edtFuturo.setHint(
                    "Futuro"
            );


            contenedorConjugacion.addView(
                    edtFuturo
            );


            camposConjugacion.add(
                    new EditText[]{
                            edtPasado,
                            edtPresente,
                            edtFuturo
                    }
            );
        }
    }


    /*
     * GUARDAR PALABRA
     */
    private void guardarPalabra() {

        String guarani =
                edtGuarani
                        .getText()
                        .toString()
                        .trim();


        String espanol =
                edtEspanol
                        .getText()
                        .toString()
                        .trim();


        String ejemplo1 =
                edtEjemplo1
                        .getText()
                        .toString()
                        .trim();


        String ejemplo2 =
                edtEjemplo2
                        .getText()
                        .toString()
                        .trim();


        String ejemplo3 =
                edtEjemplo3
                        .getText()
                        .toString()
                        .trim();


        /*
         * VALIDAR GUARANÍ
         */
        if (guarani.isEmpty()) {

            edtGuarani.setError(
                    "Ingrese la palabra en guaraní"
            );

            edtGuarani.requestFocus();

            return;
        }


        /*
         * VALIDAR ESPAÑOL
         */
        if (espanol.isEmpty()) {

            edtEspanol.setError(
                    "Ingrese el significado en español"
            );

            edtEspanol.requestFocus();

            return;
        }


        /*
         * VALIDAR EJEMPLO 1
         */
        if (ejemplo1.isEmpty()) {

            edtEjemplo1.setError(
                    "Ingrese el primer ejemplo"
            );

            edtEjemplo1.requestFocus();

            return;
        }


        /*
         * VALIDAR EJEMPLO 2
         */
        if (ejemplo2.isEmpty()) {

            edtEjemplo2.setError(
                    "Ingrese el segundo ejemplo"
            );

            edtEjemplo2.requestFocus();

            return;
        }


        /*
         * VALIDAR EJEMPLO 3
         */
        if (ejemplo3.isEmpty()) {

            edtEjemplo3.setError(
                    "Ingrese el tercer ejemplo"
            );

            edtEjemplo3.requestFocus();

            return;
        }


        /*
         * OBTENER CONJUGACIONES
         */
        List<Conjugacion> conjugaciones =
                obtenerConjugaciones();


        if (conjugaciones == null) {

            return;
        }


        /*
         * LISTA DE EJEMPLOS
         */
        List<String> ejemplos =
                new ArrayList<>();


        ejemplos.add(
                ejemplo1
        );


        ejemplos.add(
                ejemplo2
        );


        ejemplos.add(
                ejemplo3
        );


        btnGuardarPalabra.setEnabled(
                false
        );


        /*
         * Si seleccionó un MP3,
         * primero hacemos una copia dentro
         * del almacenamiento privado de la app.
         */
        if (audioUriSeleccionado != null) {

            copiarAudioYGuardar(
                    guarani,
                    espanol,
                    ejemplos,
                    conjugaciones
            );

        } else {

            /*
             * El audio sigue siendo opcional.
             */
            guardarEnRoom(
                    guarani,
                    espanol,
                    null,
                    ejemplos,
                    conjugaciones
            );
        }
    }


    /*
     * COPIAR MP3 AL ALMACENAMIENTO
     * INTERNO DE LA APLICACIÓN.
     */
    private void copiarAudioYGuardar(
            String guarani,
            String espanol,
            List<String> ejemplos,
            List<Conjugacion> conjugaciones
    ) {

        new Thread(() -> {

            try {

                String rutaAudio =
                        copiarAudioAlmacenamientoInterno(
                                audioUriSeleccionado,
                                nombreAudioSeleccionado
                        );


                runOnUiThread(() ->
                        guardarEnRoom(
                                guarani,
                                espanol,
                                rutaAudio,
                                ejemplos,
                                conjugaciones
                        )
                );


            } catch (Exception e) {

                runOnUiThread(() -> {

                    btnGuardarPalabra.setEnabled(
                            true
                    );


                    Toast.makeText(
                            GestionDiccionarioActivity.this,
                            "No fue posible copiar el archivo MP3.",
                            Toast.LENGTH_LONG
                    ).show();
                });
            }

        }).start();
    }


    /*
     * CREAR COPIA DEL AUDIO.
     *
     * Ejemplo:
     * /files/audios/1731340000_oga.mp3
     */
    private String copiarAudioAlmacenamientoInterno(
            Uri uri,
            String nombreOriginal
    ) throws Exception {

        File carpetaAudios =
                new File(
                        getFilesDir(),
                        "audios"
                );


        if (!carpetaAudios.exists()) {

            boolean creada =
                    carpetaAudios.mkdirs();


            if (!creada &&
                    !carpetaAudios.exists()) {

                throw new Exception(
                        "No fue posible crear la carpeta de audios."
                );
            }
        }


        String nombreSeguro =
                nombreOriginal;


        if (nombreSeguro == null ||
                nombreSeguro.trim().isEmpty()) {

            nombreSeguro =
                    "audio.mp3";
        }


        /*
         * Quitamos caracteres problemáticos.
         */
        nombreSeguro =
                nombreSeguro.replaceAll(
                        "[^a-zA-Z0-9._-]",
                        "_"
                );


        /*
         * Agregamos la fecha/hora para evitar
         * que dos audios tengan el mismo nombre.
         */
        String nombreFinal =
                System.currentTimeMillis()
                        + "_"
                        + nombreSeguro;


        File archivoDestino =
                new File(
                        carpetaAudios,
                        nombreFinal
                );


        InputStream entrada =
                getContentResolver()
                        .openInputStream(uri);


        if (entrada == null) {

            throw new Exception(
                    "No fue posible leer el archivo."
            );
        }


        FileOutputStream salida =
                new FileOutputStream(
                        archivoDestino
                );


        byte[] buffer =
                new byte[8192];


        int cantidad;


        while ((cantidad =
                entrada.read(buffer)) != -1) {

            salida.write(
                    buffer,
                    0,
                    cantidad
            );
        }


        salida.flush();

        salida.close();

        entrada.close();


        /*
         * Esta ruta es la que guardaremos
         * en Palabra.audio.
         */
        return archivoDestino
                .getAbsolutePath();
    }


    /*
     * GUARDAR PALABRA + AUDIO + EJEMPLOS
     * + CONJUGACIONES EN ROOM.
     */
    private void guardarEnRoom(
            String guarani,
            String espanol,
            String rutaAudio,
            List<String> ejemplos,
            List<Conjugacion> conjugaciones
    ) {

        Palabra palabra =
                new Palabra(
                        guarani,
                        espanol,
                        rutaAudio
                );


        diccionarioRepository
                .insertarPalabraCompleta(
                        palabra,
                        ejemplos,
                        conjugaciones,
                        new RepositoryCallback<Long>() {

                            @Override
                            public void onSuccess(
                                    Long idPalabra
                            ) {

                                btnGuardarPalabra.setEnabled(
                                        true
                                );


                                Toast.makeText(
                                        GestionDiccionarioActivity.this,
                                        "Palabra registrada correctamente",
                                        Toast.LENGTH_SHORT
                                ).show();


                                limpiarFormulario();
                            }


                            @Override
                            public void onError(
                                    Exception e
                            ) {

                                btnGuardarPalabra.setEnabled(
                                        true
                                );


                                Toast.makeText(
                                        GestionDiccionarioActivity.this,
                                        "No fue posible registrar la palabra",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }


    /*
     * OBTENER CONJUGACIONES
     */
    private List<Conjugacion> obtenerConjugaciones() {

        List<Conjugacion> conjugaciones =
                new ArrayList<>();


        /*
         * SI NO TIENE CONJUGACIÓN
         */
        if (!chkConjugacion.isChecked()) {

            return conjugaciones;
        }


        /*
         * SI TIENE CONJUGACIÓN
         */
        for (int i = 0;
             i < pronombres.length;
             i++) {

            EditText[] campos =
                    camposConjugacion.get(
                            i
                    );


            String pasado =
                    campos[0]
                            .getText()
                            .toString()
                            .trim();


            String presente =
                    campos[1]
                            .getText()
                            .toString()
                            .trim();


            String futuro =
                    campos[2]
                            .getText()
                            .toString()
                            .trim();


            /*
             * VALIDACIÓN
             */
            if (pasado.isEmpty() ||
                    presente.isEmpty() ||
                    futuro.isEmpty()) {

                Toast.makeText(
                        this,
                        "Complete la conjugación de "
                                + pronombres[i],
                        Toast.LENGTH_LONG
                ).show();


                campos[0].requestFocus();

                return null;
            }


            Conjugacion conjugacion =
                    new Conjugacion(
                            0,
                            pronombres[i],
                            pasado,
                            presente,
                            futuro
                    );


            conjugaciones.add(
                    conjugacion
            );
        }


        return conjugaciones;
    }


    /*
     * LIMPIAR TODO EL FORMULARIO
     */
    private void limpiarFormulario() {

        detenerAudio();


        edtGuarani.setText(
                ""
        );


        edtEspanol.setText(
                ""
        );


        edtEjemplo1.setText(
                ""
        );


        edtEjemplo2.setText(
                ""
        );


        edtEjemplo3.setText(
                ""
        );


        /*
         * LIMPIAR AUDIO
         */
        audioUriSeleccionado =
                null;


        nombreAudioSeleccionado =
                null;


        txtAudioSeleccionado.setText(
                "Ningún archivo seleccionado"
        );


        btnEscucharAudio.setEnabled(
                false
        );


        btnEscucharAudio.setText(
                "▶ Escuchar audio"
        );


        chkConjugacion.setChecked(
                false
        );


        for (EditText[] campos :
                camposConjugacion) {

            campos[0].setText(
                    ""
            );


            campos[1].setText(
                    ""
            );


            campos[2].setText(
                    ""
            );
        }


        edtGuarani.requestFocus();
    }


    @Override
    protected void onDestroy() {

        detenerAudio();

        super.onDestroy();
    }
}