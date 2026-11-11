package com.helloworld.guaranparatodos.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "diccionario",
        indices = {
                @Index(value = {"palabra_guarani"}, unique = true)
        }
)
public class Palabra {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_palabra")
    private int idPalabra;

    @NonNull
    @ColumnInfo(name = "palabra_guarani")
    private String palabraGuarani;

    @NonNull
    @ColumnInfo(name = "palabra_espanol")
    private String palabraEspanol;

    @ColumnInfo(name = "audio")
    private String audio;

    public Palabra(
            @NonNull String palabraGuarani,
            @NonNull String palabraEspanol,
            String audio
    ) {
        this.palabraGuarani = palabraGuarani;
        this.palabraEspanol = palabraEspanol;
        this.audio = audio;
    }

    public int getIdPalabra() {
        return idPalabra;
    }

    public void setIdPalabra(int idPalabra) {
        this.idPalabra = idPalabra;
    }

    @NonNull
    public String getPalabraGuarani() {
        return palabraGuarani;
    }

    public void setPalabraGuarani(@NonNull String palabraGuarani) {
        this.palabraGuarani = palabraGuarani;
    }

    @NonNull
    public String getPalabraEspanol() {
        return palabraEspanol;
    }

    public void setPalabraEspanol(@NonNull String palabraEspanol) {
        this.palabraEspanol = palabraEspanol;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}