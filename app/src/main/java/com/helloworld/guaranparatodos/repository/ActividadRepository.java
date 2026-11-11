package com.helloworld.guaranparatodos.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.helloworld.guaranparatodos.database.AppDatabase;
import com.helloworld.guaranparatodos.database.dao.ActividadDao;
import com.helloworld.guaranparatodos.database.dao.NivelDao;
import com.helloworld.guaranparatodos.database.entity.Actividad;
import com.helloworld.guaranparatodos.database.entity.Nivel;

import java.util.List;

public class ActividadRepository {

    private final ActividadDao actividadDao;
    private final NivelDao nivelDao;

    private final Handler mainHandler =
            new Handler(Looper.getMainLooper());


    public ActividadRepository(Context context) {

        AppDatabase db =
                AppDatabase.getDatabase(context);

        actividadDao =
                db.actividadDao();

        nivelDao =
                db.nivelDao();
    }


    public void obtenerNiveles(
            RepositoryCallback<List<Nivel>> callback
    ) {

        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                List<Nivel> niveles =
                        nivelDao.obtenerTodos();

                mainHandler.post(() ->
                        callback.onSuccess(niveles)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }


    public void obtenerActividadesPorNivel(
            int idNivel,
            RepositoryCallback<List<Actividad>> callback
    ) {

        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                List<Actividad> actividades =
                        actividadDao.obtenerPorNivel(idNivel);

                mainHandler.post(() ->
                        callback.onSuccess(actividades)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }


    public void insertar(
            Actividad actividad,
            RepositoryCallback<Long> callback
    ) {

        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                long id =
                        actividadDao.insertar(
                                actividad
                        );

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


    public void actualizar(
            Actividad actividad,
            RepositoryCallback<Boolean> callback
    ) {

        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                actividadDao.actualizar(
                        actividad
                );

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


    public void eliminar(
            Actividad actividad,
            RepositoryCallback<Boolean> callback
    ) {

        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                actividadDao.eliminar(
                        actividad
                );

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
}