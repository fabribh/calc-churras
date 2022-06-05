package com.fabribh.churrascocalculator.entities;

import java.util.List;

public class Convidado {

    private String nome;
    private String phone;
    private String sexo;
    private String acompanhante;
    private List<Item> item;

    public Convidado(String nome, String phone, List<Item> item) {
        this.nome = nome;
        this.phone = phone;
        this.item = item;
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

    public List<Item> getItem() {
        return item;
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
