package com.helloworld.guaranparatodos.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nivel")
public class Nivel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_nivel")
    private int idNivel;

    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;

    @NonNull
    @ColumnInfo(name = "descripcion")
    private String descripcion;

    public Nivel(@NonNull String nombre, @NonNull String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@NonNull String descripcion) {
        this.descripcion = descripcion;
    }
}