package com.helloworld.guaranparatodos.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "ejemplo",
        foreignKeys = @ForeignKey(
                entity = Palabra.class,
                parentColumns = "id_palabra",
                childColumns = "id_palabra",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index("id_palabra")
        }
)
public class Ejemplo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_ejemplo")
    private int idEjemplo;

    @NonNull
    @ColumnInfo(name = "ejemplo")
    private String ejemplo;

    @ColumnInfo(name = "id_palabra")
    private int idPalabra;

    public Ejemplo(@NonNull String ejemplo, int idPalabra) {
        this.ejemplo = ejemplo;
        this.idPalabra = idPalabra;
    }

    public int getIdEjemplo() {
        return idEjemplo;
    }

    public void setIdEjemplo(int idEjemplo) {
        this.idEjemplo = idEjemplo;
    }

    @NonNull
    public String getEjemplo() {
        return ejemplo;
    }

    public void setEjemplo(@NonNull String ejemplo) {
        this.ejemplo = ejemplo;
    }

    public int getIdPalabra() {
        return idPalabra;
    }

    public void setIdPalabra(int idPalabra) {
        this.idPalabra = idPalabra;
    }
}