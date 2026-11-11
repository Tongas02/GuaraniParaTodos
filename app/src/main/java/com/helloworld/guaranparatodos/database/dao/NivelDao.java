package com.helloworld.guaranparatodos.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.helloworld.guaranparatodos.database.entity.Nivel;

import java.util.List;

@Dao
public interface NivelDao {

    @Insert
    long insertar(Nivel nivel);

    @Update
    void actualizar(Nivel nivel);

    @Delete
    void eliminar(Nivel nivel);

    @Query("SELECT * FROM nivel ORDER BY id_nivel ASC")
    List<Nivel> obtenerTodos();

    @Query("SELECT * FROM nivel WHERE id_nivel = :id LIMIT 1")
    Nivel obtenerPorId(int id);

    @Query("SELECT * FROM nivel WHERE nombre = :nombre LIMIT 1")
    Nivel obtenerPorNombre(String nombre);
}