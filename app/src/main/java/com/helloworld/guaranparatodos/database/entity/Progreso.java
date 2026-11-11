package com.helloworld.guaranparatodos.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "progreso",
        foreignKeys = {
                @ForeignKey(
                        entity = Usuario.class,
                        parentColumns = "id_usuario",
                        childColumns = "id_usuario",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Actividad.class,
                        parentColumns = "id_actividad",
                        childColumns = "id_actividad",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("id_usuario"),
                @Index("id_actividad"),
                @Index(
                        value = {
                                "id_usuario",
                                "id_actividad"
                        },
                        unique = true
                )
        }
)
public class Progreso {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_progreso")
    private int idProgreso;

    @ColumnInfo(name = "id_usuario")
    private int idUsuario;

    @ColumnInfo(name = "id_actividad")
    private int idActividad;

    @NonNull
    @ColumnInfo(name = "estado")
    private String estado;

    @ColumnInfo(name = "resultado")
    private int resultado;

    @ColumnInfo(name = "porcentaje")
    private double porcentaje;

    @NonNull
    @ColumnInfo(name = "fecha_realizacion")
    private String fechaRealizacion;


    public Progreso(
            int idUsuario,
            int idActividad,
            @NonNull String estado,
            int resultado,
            double porcentaje,
            @NonNull String fechaRealizacion
    ) {

        this.idUsuario = idUsuario;
        this.idActividad = idActividad;
        this.estado = estado;
        this.resultado = resultado;
        this.porcentaje = porcentaje;
        this.fechaRealizacion = fechaRealizacion;
    }


    public int getIdProgreso() {
        return idProgreso;
    }

    public void setIdProgreso(int idProgreso) {
        this.idProgreso = idProgreso;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    @NonNull
    public String getEstado() {
        return estado;
    }

    public void setEstado(@NonNull String estado) {
        this.estado = estado;
    }

    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    @NonNull
    public String getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(
            @NonNull String fechaRealizacion
    ) {
        this.fechaRealizacion = fechaRealizacion;
    }
}