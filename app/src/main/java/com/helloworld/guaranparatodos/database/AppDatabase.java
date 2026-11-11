package com.helloworld.guaranparatodos.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.helloworld.guaranparatodos.database.dao.ActividadDao;
import com.helloworld.guaranparatodos.database.dao.ConjugacionDao;
import com.helloworld.guaranparatodos.database.dao.EjemploDao;
import com.helloworld.guaranparatodos.database.dao.NivelDao;
import com.helloworld.guaranparatodos.database.dao.PalabraDao;
import com.helloworld.guaranparatodos.database.dao.ProgresoDao;
import com.helloworld.guaranparatodos.database.dao.UsuarioDao;

import com.helloworld.guaranparatodos.database.entity.Actividad;
import com.helloworld.guaranparatodos.database.entity.Conjugacion;
import com.helloworld.guaranparatodos.database.entity.Ejemplo;
import com.helloworld.guaranparatodos.database.entity.Nivel;
import com.helloworld.guaranparatodos.database.entity.Palabra;
import com.helloworld.guaranparatodos.database.entity.Progreso;
import com.helloworld.guaranparatodos.database.entity.Usuario;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                Usuario.class,
                Nivel.class,
                Actividad.class,
                Palabra.class,
                Ejemplo.class,
                Conjugacion.class,
                Progreso.class
        },
        version = 5,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();

    public abstract NivelDao nivelDao();

    public abstract ActividadDao actividadDao();

    public abstract PalabraDao palabraDao();

    public abstract EjemploDao ejemploDao();

    public abstract ConjugacionDao conjugacionDao();

    public abstract ProgresoDao progresoDao();


    private static volatile AppDatabase INSTANCE;


    private static final int NUMERO_HILOS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMERO_HILOS);


    /*
     * Para quien todavía tenga la versión 3.
     */
    private static final Migration MIGRATION_3_5 =
            new Migration(3, 5) {

                @Override
                public void migrate(
                        @NonNull SupportSQLiteDatabase db
                ) {

                    agregarCamposActividad(db);
                }
            };


    /*
     * Para quien haya llegado a la versión 4 anterior.
     */
    private static final Migration MIGRATION_4_5 =
            new Migration(4, 5) {

                @Override
                public void migrate(
                        @NonNull SupportSQLiteDatabase db
                ) {

                    agregarCamposActividad(db);

                    db.execSQL(
                            "DROP TABLE IF EXISTS opcion"
                    );

                    db.execSQL(
                            "DROP TABLE IF EXISTS pregunta"
                    );
                }
            };


    private static void agregarCamposActividad(
            SupportSQLiteDatabase db
    ) {

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN enunciado TEXT NOT NULL DEFAULT ''"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN respuesta_correcta TEXT"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_1 TEXT"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_2 TEXT"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_3 TEXT"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_4 TEXT"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_5 TEXT"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_6 TEXT"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_1_correcta INTEGER NOT NULL DEFAULT 0"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_2_correcta INTEGER NOT NULL DEFAULT 0"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_3_correcta INTEGER NOT NULL DEFAULT 0"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_4_correcta INTEGER NOT NULL DEFAULT 0"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_5_correcta INTEGER NOT NULL DEFAULT 0"
        );

        db.execSQL(
                "ALTER TABLE actividad " +
                        "ADD COLUMN opcion_6_correcta INTEGER NOT NULL DEFAULT 0"
        );
    }


    public static AppDatabase getDatabase(
            final Context context
    ) {

        if (INSTANCE == null) {

            synchronized (AppDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE =
                            Room.databaseBuilder(
                                            context.getApplicationContext(),
                                            AppDatabase.class,
                                            "guarani_para_todos_db"
                                    )
                                    .addMigrations(
                                            MIGRATION_3_5,
                                            MIGRATION_4_5
                                    )
                                    .fallbackToDestructiveMigration()
                                    .addCallback(
                                            roomDatabaseCallback
                                    )
                                    .build();
                }
            }
        }

        return INSTANCE;
    }


    private static final RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(
                        @NonNull SupportSQLiteDatabase db
                ) {

                    super.onOpen(db);


                    db.execSQL(
                            "INSERT INTO nivel " +
                                    "(nombre, descripcion) " +
                                    "SELECT 'Básico', " +
                                    "'Nivel inicial para el aprendizaje del idioma guaraní.' " +
                                    "WHERE NOT EXISTS (" +
                                    "SELECT 1 FROM nivel " +
                                    "WHERE nombre = 'Básico')"
                    );


                    db.execSQL(
                            "INSERT INTO nivel " +
                                    "(nombre, descripcion) " +
                                    "SELECT 'Intermedio', " +
                                    "'Nivel intermedio para reforzar los conocimientos adquiridos.' " +
                                    "WHERE NOT EXISTS (" +
                                    "SELECT 1 FROM nivel " +
                                    "WHERE nombre = 'Intermedio')"
                    );


                    db.execSQL(
                            "INSERT INTO nivel " +
                                    "(nombre, descripcion) " +
                                    "SELECT 'Avanzado', " +
                                    "'Nivel avanzado para continuar profundizando el aprendizaje.' " +
                                    "WHERE NOT EXISTS (" +
                                    "SELECT 1 FROM nivel " +
                                    "WHERE nombre = 'Avanzado')"
                    );
                }
            };
}