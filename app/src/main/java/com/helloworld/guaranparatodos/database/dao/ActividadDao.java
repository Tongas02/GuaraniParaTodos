package com.helloworld.guaranparatodos.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.helloworld.guaranparatodos.database.entity.Actividad;

import java.util.List;

@Dao
public interface ActividadDao {

    @Insert
    long insertar(Actividad actividad);

    @Update
    void actualizar(Actividad actividad);

    @Delete
    void eliminar(Actividad actividad);

    @Query("SELECT * FROM actividad ORDER BY id_actividad ASC")
    List<Actividad> obtenerTodas();

    @Query("SELECT * FROM actividad WHERE id_actividad = :idActividad LIMIT 1")
    Actividad obtenerPorId(int idActividad);

    @Query("SELECT * FROM actividad WHERE id_nivel = :idNivel ORDER BY id_actividad ASC")
    List<Actividad> obtenerPorNivel(int idNivel);

    @Query("SELECT * FROM actividad WHERE tipo = :tipo ORDER BY id_actividad ASC")
    List<Actividad> obtenerPorTipo(String tipo);

    @Query("SELECT * FROM actividad WHERE id_nivel = :idNivel AND tipo = :tipo ORDER BY id_actividad ASC")
    List<Actividad> obtenerPorNivelYTipo(
            int idNivel,
            String tipo
    );
}