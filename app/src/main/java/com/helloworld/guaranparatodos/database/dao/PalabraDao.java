package com.helloworld.guaranparatodos.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.helloworld.guaranparatodos.database.entity.Palabra;

import java.util.List;

@Dao
public interface PalabraDao {

    @Insert
    long insertar(Palabra palabra);

    @Update
    void actualizar(Palabra palabra);

    @Delete
    void eliminar(Palabra palabra);

    @Query("SELECT * FROM diccionario ORDER BY palabra_guarani ASC")
    List<Palabra> obtenerTodas();

    @Query("SELECT * FROM diccionario WHERE id_palabra = :id LIMIT 1")
    Palabra obtenerPorId(int id);

    @Query(
            "SELECT * FROM diccionario " +
                    "WHERE palabra_guarani LIKE '%' || :texto || '%' " +
                    "OR palabra_espanol LIKE '%' || :texto || '%' " +
                    "ORDER BY palabra_guarani ASC"
    )
    List<Palabra> buscar(String texto);

    @Query("SELECT * FROM diccionario WHERE palabra_guarani = :palabra LIMIT 1")
    Palabra obtenerPorGuarani(String palabra);
}