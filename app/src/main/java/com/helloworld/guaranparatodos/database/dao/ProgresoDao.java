package com.helloworld.guaranparatodos.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.helloworld.guaranparatodos.database.entity.Progreso;

import java.util.List;

@Dao
public interface ProgresoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertarOActualizar(Progreso progreso);

    @Update
    void actualizar(Progreso progreso);

    @Delete
    void eliminar(Progreso progreso);


    @Query(
            "SELECT * FROM progreso " +
                    "ORDER BY fecha_realizacion DESC"
    )
    List<Progreso> obtenerTodos();


    @Query(
            "SELECT * FROM progreso " +
                    "WHERE id_usuario = :idUsuario " +
                    "ORDER BY fecha_realizacion DESC"
    )
    List<Progreso> obtenerPorUsuario(
            int idUsuario
    );


    @Query(
            "SELECT * FROM progreso " +
                    "WHERE id_usuario = :idUsuario " +
                    "AND id_actividad = :idActividad " +
                    "LIMIT 1"
    )
    Progreso obtenerProgresoActividad(
            int idUsuario,
            int idActividad
    );


    @Query(
            "SELECT AVG(p.porcentaje) " +
                    "FROM progreso p " +
                    "INNER JOIN actividad a " +
                    "ON p.id_actividad = a.id_actividad " +
                    "WHERE p.id_usuario = :idUsuario " +
                    "AND a.id_nivel = :idNivel"
    )
    Double obtenerPromedioNivel(
            int idUsuario,
            int idNivel
    );


    @Query(
            "SELECT COUNT(*) " +
                    "FROM progreso " +
                    "WHERE id_usuario = :idUsuario " +
                    "AND estado = 'COMPLETADA'"
    )
    int contarActividadesCompletadas(
            int idUsuario
    );
}