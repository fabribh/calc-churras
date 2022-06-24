package com.fabribh.churrascocalculator.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fabribh.churrascocalculator.entities.Convidado;

import java.util.List;

@Dao
public interface ConvidadoDao {

    @Insert
    long insert(Convidado convidado);

    @Delete
    void delete(Convidado convidado);

    @Update
    void update(Convidado convidado);

    @Query("SELECT * FROM convidado WHERE id = :id")
    Convidado queryForId(long id);

    @Query("SELECT * FROM convidado ORDER BY id DESC")
    List<Convidado> queryAll();
}
