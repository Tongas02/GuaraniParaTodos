package com.helloworld.guaranparatodos.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "actividad",
        foreignKeys = @ForeignKey(
                entity = Nivel.class,
                parentColumns = "id_nivel",
                childColumns = "id_nivel",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index("id_nivel")
        }
)
public class Actividad {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_actividad")
    private int idActividad;

    @NonNull
    @ColumnInfo(name = "titulo")
    private String titulo;

    @NonNull
    @ColumnInfo(name = "descripcion")
    private String descripcion;

    @NonNull
    @ColumnInfo(name = "tipo")
    private String tipo;

    @ColumnInfo(name = "id_nivel")
    private int idNivel;

    @NonNull
    @ColumnInfo(name = "enunciado")
    private String enunciado;

    @ColumnInfo(name = "respuesta_correcta")
    private String respuestaCorrecta;

    @ColumnInfo(name = "opcion_1")
    private String opcion1;

    @ColumnInfo(name = "opcion_2")
    private String opcion2;

    @ColumnInfo(name = "opcion_3")
    private String opcion3;

    @ColumnInfo(name = "opcion_4")
    private String opcion4;

    @ColumnInfo(name = "opcion_5")
    private String opcion5;

    @ColumnInfo(name = "opcion_6")
    private String opcion6;

    @ColumnInfo(name = "opcion_1_correcta")
    private boolean opcion1Correcta;

    @ColumnInfo(name = "opcion_2_correcta")
    private boolean opcion2Correcta;

    @ColumnInfo(name = "opcion_3_correcta")
    private boolean opcion3Correcta;

    @ColumnInfo(name = "opcion_4_correcta")
    private boolean opcion4Correcta;

    @ColumnInfo(name = "opcion_5_correcta")
    private boolean opcion5Correcta;

    @ColumnInfo(name = "opcion_6_correcta")
    private boolean opcion6Correcta;


    public Actividad(
            @NonNull String titulo,
            @NonNull String descripcion,
            @NonNull String tipo,
            int idNivel,
            @NonNull String enunciado,
            String respuestaCorrecta
    ) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.idNivel = idNivel;
        this.enunciado = enunciado;
        this.respuestaCorrecta = respuestaCorrecta;
    }


    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    @NonNull
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(@NonNull String titulo) {
        this.titulo = titulo;
    }

    @NonNull
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@NonNull String descripcion) {
        this.descripcion = descripcion;
    }

    @NonNull
    public String getTipo() {
        return tipo;
    }

    public void setTipo(@NonNull String tipo) {
        this.tipo = tipo;
    }

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    @NonNull
    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(@NonNull String enunciado) {
        this.enunciado = enunciado;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getOpcion1() {
        return opcion1;
    }

    public void setOpcion1(String opcion1) {
        this.opcion1 = opcion1;
    }

    public String getOpcion2() {
        return opcion2;
    }

    public void setOpcion2(String opcion2) {
        this.opcion2 = opcion2;
    }

    public String getOpcion3() {
        return opcion3;
    }

    public void setOpcion3(String opcion3) {
        this.opcion3 = opcion3;
    }

    public String getOpcion4() {
        return opcion4;
    }

    public void setOpcion4(String opcion4) {
        this.opcion4 = opcion4;
    }

    public String getOpcion5() {
        return opcion5;
    }

    public void setOpcion5(String opcion5) {
        this.opcion5 = opcion5;
    }

    public String getOpcion6() {
        return opcion6;
    }

    public void setOpcion6(String opcion6) {
        this.opcion6 = opcion6;
    }

    public boolean isOpcion1Correcta() {
        return opcion1Correcta;
    }

    public void setOpcion1Correcta(boolean opcion1Correcta) {
        this.opcion1Correcta = opcion1Correcta;
    }

    public boolean isOpcion2Correcta() {
        return opcion2Correcta;
    }

    public void setOpcion2Correcta(boolean opcion2Correcta) {
        this.opcion2Correcta = opcion2Correcta;
    }

    public boolean isOpcion3Correcta() {
        return opcion3Correcta;
    }

    public void setOpcion3Correcta(boolean opcion3Correcta) {
        this.opcion3Correcta = opcion3Correcta;
    }

    public boolean isOpcion4Correcta() {
        return opcion4Correcta;
    }

    public void setOpcion4Correcta(boolean opcion4Correcta) {
        this.opcion4Correcta = opcion4Correcta;
    }

    public boolean isOpcion5Correcta() {
        return opcion5Correcta;
    }

    public void setOpcion5Correcta(boolean opcion5Correcta) {
        this.opcion5Correcta = opcion5Correcta;
    }

    public boolean isOpcion6Correcta() {
        return opcion6Correcta;
    }

    public void setOpcion6Correcta(boolean opcion6Correcta) {
        this.opcion6Correcta = opcion6Correcta;
    }
}