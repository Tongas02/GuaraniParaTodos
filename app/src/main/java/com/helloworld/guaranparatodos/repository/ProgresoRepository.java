package com.helloworld.guaranparatodos.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.helloworld.guaranparatodos.database.AppDatabase;
import com.helloworld.guaranparatodos.database.dao.ProgresoDao;
import com.helloworld.guaranparatodos.database.entity.Actividad;
import com.helloworld.guaranparatodos.database.entity.Progreso;

import java.util.List;

public class ProgresoRepository {

    private final ProgresoDao progresoDao;

    private final Handler mainHandler =
            new Handler(Looper.getMainLooper());


    public ProgresoRepository(Context context) {

        AppDatabase db =
                AppDatabase.getDatabase(context);

        progresoDao =
                db.progresoDao();
    }


    public void guardar(
            Progreso progreso,
            RepositoryCallback<Long> callback
    ) {

        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                long resultado =
                        progresoDao.insertarOActualizar(
                                progreso
                        );

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


    public void obtenerPorUsuario(
            int idUsuario,
            RepositoryCallback<List<Progreso>> callback
    ) {

        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                List<Progreso> progresos =
                        progresoDao.obtenerPorUsuario(
                                idUsuario
                        );

                mainHandler.post(() ->
                        callback.onSuccess(progresos)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }


    public void obtenerPromedioNivel(
            int idUsuario,
            int idNivel,
            RepositoryCallback<Double> callback
    ) {

        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                Double promedio =
                        progresoDao.obtenerPromedioNivel(
                                idUsuario,
                                idNivel
                        );

                if (promedio == null) {
                    promedio = 0.0;
                }

                double resultado =
                        promedio;

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


    public void contarCompletadas(
            int idUsuario,
            RepositoryCallback<Integer> callback
    ) {

        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                int cantidad =
                        progresoDao
                                .contarActividadesCompletadas(
                                        idUsuario
                                );

                mainHandler.post(() ->
                        callback.onSuccess(cantidad)
                );

            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }


    public void puedeDesbloquearSiguienteNivel(
            int idUsuario,
            int idNivel,
            RepositoryCallback<Boolean> callback
    ) {

        obtenerPromedioNivel(
                idUsuario,
                idNivel,
                new RepositoryCallback<Double>() {

                    @Override
                    public void onSuccess(
                            Double promedio
                    ) {

                        callback.onSuccess(
                                promedio >= 70.0
                        );
                    }

                    @Override
                    public void onError(
                            Exception e
                    ) {

                        callback.onError(e);
                    }
                }
        );
    }


    public void guardarResultadoNivel(
            int idUsuario,
            List<Actividad> actividades,
            boolean[] respuestasCorrectas,
            String fecha,
            RepositoryCallback<Double> callback
    ) {

        AppDatabase.databaseWriteExecutor.execute(() -> {

            try {

                int correctas = 0;


                for (int i = 0;
                     i < actividades.size();
                     i++) {

                    boolean correcta =
                            respuestasCorrectas[i];


                    if (correcta) {
                        correctas++;
                    }


                    Progreso progreso =
                            new Progreso(
                                    idUsuario,
                                    actividades
                                            .get(i)
                                            .getIdActividad(),
                                    "COMPLETADA",
                                    correcta ? 1 : 0,
                                    correcta
                                            ? 100.0
                                            : 0.0,
                                    fecha
                            );


                    progresoDao
                            .insertarOActualizar(
                                    progreso
                            );
                }


                double porcentajeFinal =
                        actividades.isEmpty()
                                ? 0
                                : correctas
                                  * 100.0
                                  / actividades.size();


                mainHandler.post(() ->
                        callback.onSuccess(
                                porcentajeFinal
                        )
                );


            } catch (Exception e) {

                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }
}