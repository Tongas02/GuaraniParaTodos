package com.helloworld.guaranparatodos.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.helloworld.guaranparatodos.database.entity.Ejemplo;

import java.util.List;

@Dao
public interface EjemploDao {

    @Insert
    long insertar(Ejemplo ejemplo);

    @Update
    void actualizar(Ejemplo ejemplo);

    @Delete
    void eliminar(Ejemplo ejemplo);

    @Query("SELECT * FROM ejemplo ORDER BY id_ejemplo ASC")
    List<Ejemplo> obtenerTodos();

    @Query("SELECT * FROM ejemplo WHERE id_ejemplo = :id LIMIT 1")
    Ejemplo obtenerPorId(int id);

    @Query("SELECT * FROM ejemplo WHERE id_palabra = :idPalabra ORDER BY id_ejemplo ASC")
    List<Ejemplo> obtenerPorPalabra(int idPalabra);
}