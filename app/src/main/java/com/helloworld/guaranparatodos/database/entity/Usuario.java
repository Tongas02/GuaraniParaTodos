package com.helloworld.guaranparatodos.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "usuario",
        indices = {
                @Index(value = {"firebase_uid"}, unique = true),
                @Index(value = {"correo"}, unique = true)
        }
)
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_usuario")
    private int idUsuario;

    @NonNull
    @ColumnInfo(name = "firebase_uid")
    private String firebaseUid;

    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;

    @NonNull
    @ColumnInfo(name = "correo")
    private String correo;

    @NonNull
    @ColumnInfo(name = "tipo_usuario")
    private String tipoUsuario;

    @ColumnInfo(name = "tema")
    private String tema;

    public Usuario(
            @NonNull String firebaseUid,
            @NonNull String nombre,
            @NonNull String correo,
            @NonNull String tipoUsuario,
            String tema
    ) {
        this.firebaseUid = firebaseUid;
        this.nombre = nombre;
        this.correo = correo;
        this.tipoUsuario = tipoUsuario;
        this.tema = tema;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @NonNull
    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(@NonNull String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(@NonNull String correo) {
        this.correo = correo;
    }

    @NonNull
    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(@NonNull String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
}