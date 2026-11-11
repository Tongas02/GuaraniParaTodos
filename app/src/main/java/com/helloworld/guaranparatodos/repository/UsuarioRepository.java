package com.helloworld.guaranparatodos.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.helloworld.guaranparatodos.database.AppDatabase;
import com.helloworld.guaranparatodos.database.dao.UsuarioDao;
import com.helloworld.guaranparatodos.database.entity.Usuario;

import java.util.List;

public class UsuarioRepository {

    private final UsuarioDao usuarioDao;
    private final Handler mainHandler;

    public UsuarioRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        usuarioDao = db.usuarioDao();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void insertar(
            Usuario usuario,
            RepositoryCallback<Long> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                long id = usuarioDao.insertar(usuario);

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
            Usuario usuario,
            RepositoryCallback<Boolean> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                usuarioDao.actualizar(usuario);

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
            Usuario usuario,
            RepositoryCallback<Boolean> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                usuarioDao.eliminar(usuario);

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

    public void obtenerTodos(
            RepositoryCallback<List<Usuario>> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                List<Usuario> usuarios =
                        usuarioDao.obtenerTodos();

                mainHandler.post(() ->
                        callback.onSuccess(usuarios)
                );

            } catch (Exception e) {
                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }

    public void obtenerPorFirebaseUid(
            String firebaseUid,
            RepositoryCallback<Usuario> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                Usuario usuario =
                        usuarioDao.obtenerPorFirebaseUid(firebaseUid);

                mainHandler.post(() ->
                        callback.onSuccess(usuario)
                );

            } catch (Exception e) {
                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }

    public void obtenerPorCorreo(
            String correo,
            RepositoryCallback<Usuario> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                Usuario usuario =
                        usuarioDao.obtenerPorCorreo(correo);

                mainHandler.post(() ->
                        callback.onSuccess(usuario)
                );

            } catch (Exception e) {
                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }

    public void obtenerPorTipo(
            String tipoUsuario,
            RepositoryCallback<List<Usuario>> callback
    ) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                List<Usuario> usuarios =
                        usuarioDao.obtenerPorTipo(tipoUsuario);

                mainHandler.post(() ->
                        callback.onSuccess(usuarios)
                );

            } catch (Exception e) {
                mainHandler.post(() ->
                        callback.onError(e)
                );
            }
        });
    }
}