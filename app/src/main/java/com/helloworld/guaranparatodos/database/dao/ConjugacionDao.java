package com.helloworld.guaranparatodos.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.helloworld.guaranparatodos.database.entity.Conjugacion;

import java.util.List;

@Dao
public interface ConjugacionDao {

    @Insert
    long insertar(Conjugacion conjugacion);

    @Update
    void actualizar(Conjugacion conjugacion);

    @Delete
    void eliminar(Conjugacion conjugacion);

    @Query("SELECT * FROM conjugacion ORDER BY id_conjugacion ASC")
    List<Conjugacion> obtenerTodas();

    @Query("SELECT * FROM conjugacion WHERE id_conjugacion = :id LIMIT 1")
    Conjugacion obtenerPorId(int id);

    @Query("SELECT * FROM conjugacion WHERE id_palabra = :idPalabra")
    List<Conjugacion> obtenerPorPalabra(int idPalabra);
}