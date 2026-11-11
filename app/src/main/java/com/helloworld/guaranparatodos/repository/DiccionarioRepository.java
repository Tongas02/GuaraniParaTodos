package com.helloworld.guaranparatodos.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.helloworld.guaranparatodos.database.AppDatabase;
import com.helloworld.guaranparatodos.database.dao.ConjugacionDao;
import com.helloworld.guaranparatodos.database.dao.EjemploDao;
import com.helloworld.guaranparatodos.database.dao.PalabraDao;
import com.helloworld.guaranparatodos.database.entity.Conjugacion;
import com.helloworld.guaranparatodos.database.entity.Ejemplo;
import com.helloworld.guaranparatodos.database.entity.Palabra;

import java.util.List;
import java.util.ArrayList;


public class DiccionarioRepository {

    private final PalabraDao palabraDao;
    private final EjemploDao ejemploDao;
    private final ConjugacionDao conjugacionDao;
    private final Handler mainHandler;

    public DiccionarioRepository(Context context) {

        AppDatabase db =
                AppDatabase.getDatabase(context);

        palabraDao = db.palabraDao();
        ejemploDao = db.ejemploDao();
        conjugacionDao = db.conjugacionDao();

        mainHandler =
                new Handler(Looper.getMainLooper());
    }

    public void buscar(
            String texto,
            RepositoryCallback<List<Palabra>> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                List<Palabra> resultado =
                        palabraDao.buscar(texto);

                mainHandler.post(() ->
                        callback.onSuccess(resultado)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }

    public void obtenerTodas(
            RepositoryCallback<List<Palabra>> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                List<Palabra> palabras =
                        palabraDao.obtenerTodas();

                mainHandler.post(() ->
                        callback.onSuccess(palabras)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }

    public void insertarPalabra(
            Palabra palabra,
            RepositoryCallback<Long> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                long id =
                        palabraDao.insertar(palabra);

                mainHandler.post(() ->
                        callback.onSuccess(id)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }

    public void actualizarPalabra(
            Palabra palabra,
            RepositoryCallback<Boolean> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                palabraDao.actualizar(palabra);

                mainHandler.post(() ->
                        callback.onSuccess(true)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }

    public void eliminarPalabra(
            Palabra palabra,
            RepositoryCallback<Boolean> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                palabraDao.eliminar(palabra);

                mainHandler.post(() ->
                        callback.onSuccess(true)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }

    public void obtenerEjemplos(
            int idPalabra,
            RepositoryCallback<List<Ejemplo>> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                List<Ejemplo> ejemplos =
                        ejemploDao.obtenerPorPalabra(idPalabra);

                mainHandler.post(() ->
                        callback.onSuccess(ejemplos)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }

    public void obtenerConjugaciones(
            int idPalabra,
            RepositoryCallback<List<Conjugacion>> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                List<Conjugacion> conjugaciones =
                        conjugacionDao.obtenerPorPalabra(idPalabra);

                mainHandler.post(() ->
                        callback.onSuccess(conjugaciones)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }

    public void insertarEjemplo(
            Ejemplo ejemplo,
            RepositoryCallback<Long> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                long id =
                        ejemploDao.insertar(ejemplo);

                mainHandler.post(() ->
                        callback.onSuccess(id)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }

    public void insertarConjugacion(
            Conjugacion conjugacion,
            RepositoryCallback<Long> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                long id =
                        conjugacionDao.insertar(conjugacion);

                mainHandler.post(() ->
                        callback.onSuccess(id)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }
    public void insertarPalabraCompleta(
            Palabra palabra,
            List<String> textosEjemplos,
            List<Conjugacion> conjugaciones,
            RepositoryCallback<Long> callback
    ) {

        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                long idPalabra = palabraDao.insertar(palabra);

                for (String texto : textosEjemplos) {

                    Ejemplo ejemplo = new Ejemplo(
                            texto,
                            (int) idPalabra
                    );

                    ejemploDao.insertar(ejemplo);
                }

                for (Conjugacion conjugacion : conjugaciones) {

                    conjugacion.setIdPalabra(
                            (int) idPalabra
                    );

                    conjugacionDao.insertar(conjugacion);
                }

                mainHandler.post(() ->
                        callback.onSuccess(idPalabra)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }
}