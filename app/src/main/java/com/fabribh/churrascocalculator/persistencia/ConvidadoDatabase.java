package com.fabribh.churrascocalculator.persistencia;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.fabribh.churrascocalculator.entities.Convidado;

@Database(entities = {Convidado.class},
        version = 2,
        autoMigrations = {
                @AutoMigration(from = 1, to = 2)
        }
       )
public abstract class ConvidadoDatabase extends RoomDatabase {

    public abstract ConvidadoDao convidadoDao();

    private static ConvidadoDatabase instance;

    public static ConvidadoDatabase getDatabase(final Context context){
        if (instance == null) {

            synchronized (ConvidadoDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                                                    ConvidadoDatabase.class,
                                                    "calc-churras.db")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}
