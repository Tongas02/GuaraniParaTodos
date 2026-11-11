package com.helloworld.guaranparatodos.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "conjugacion",
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
public class Conjugacion {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_conjugacion")
    private int idConjugacion;

    @ColumnInfo(name = "id_palabra")
    private int idPalabra;

    @NonNull
    @ColumnInfo(name = "pronombre")
    private String pronombre;

    @NonNull
    @ColumnInfo(name = "pasado")
    private String pasado;

    @NonNull
    @ColumnInfo(name = "presente")
    private String presente;

    @NonNull
    @ColumnInfo(name = "futuro")
    private String futuro;

    public Conjugacion(
            int idPalabra,
            @NonNull String pronombre,
            @NonNull String pasado,
            @NonNull String presente,
            @NonNull String futuro
    ) {
        this.idPalabra = idPalabra;
        this.pronombre = pronombre;
        this.pasado = pasado;
        this.presente = presente;
        this.futuro = futuro;
    }

    public int getIdConjugacion() {
        return idConjugacion;
    }

    public void setIdConjugacion(int idConjugacion) {
        this.idConjugacion = idConjugacion;
    }

    public int getIdPalabra() {
        return idPalabra;
    }

    public void setIdPalabra(int idPalabra) {
        this.idPalabra = idPalabra;
    }

    @NonNull
    public String getPronombre() {
        return pronombre;
    }

    public void setPronombre(@NonNull String pronombre) {
        this.pronombre = pronombre;
    }

    @NonNull
    public String getPasado() {
        return pasado;
    }

    public void setPasado(@NonNull String pasado) {
        this.pasado = pasado;
    }

    @NonNull
    public String getPresente() {
        return presente;
    }

    public void setPresente(@NonNull String presente) {
        this.presente = presente;
    }

    @NonNull
    public String getFuturo() {
        return futuro;
    }

    public void setFuturo(@NonNull String futuro) {
        this.futuro = futuro;
    }
}