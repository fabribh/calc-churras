package com.fabribh.churrascocalculator.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Convidado {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String nome;
    @NonNull
    private String phone;
    private String sexo;
    private String acompanhante;
    private String item;

    public Convidado(String nome, String phone, String sexo, String acompanhante, String item) {
        this.nome = nome;
        this.phone = phone;
        this.sexo = sexo;
        this.acompanhante = acompanhante;
        this.item = item;
    }

    public Convidado() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getAcompanhante() {
        return acompanhante;
    }

    public void setAcompanhante(String acompanhante) {
        this.acompanhante = acompanhante;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return nome
                + " - " +
                phone
                + " - " +
                sexo
                + " - " +
                acompanhante
                + " - " +
                item;
    }
}
