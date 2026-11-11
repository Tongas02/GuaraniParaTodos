package com.helloworld.guaranparatodos.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.helloworld.guaranparatodos.database.entity.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {

    @Insert
    long insertar(Usuario usuario);

    @Update
    void actualizar(Usuario usuario);

    @Delete
    void eliminar(Usuario usuario);

    @Query("SELECT * FROM usuario ORDER BY nombre ASC")
    List<Usuario> obtenerTodos();

    @Query("SELECT * FROM usuario WHERE id_usuario = :id LIMIT 1")
    Usuario obtenerPorId(int id);

    @Query("SELECT * FROM usuario WHERE firebase_uid = :firebaseUid LIMIT 1")
    Usuario obtenerPorFirebaseUid(String firebaseUid);

    @Query("SELECT * FROM usuario WHERE correo = :correo LIMIT 1")
    Usuario obtenerPorCorreo(String correo);

    @Query("SELECT * FROM usuario WHERE tipo_usuario = :tipoUsuario ORDER BY nombre ASC")
    List<Usuario> obtenerPorTipo(String tipoUsuario);
}