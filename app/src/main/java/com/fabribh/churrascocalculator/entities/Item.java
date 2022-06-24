package com.fabribh.churrascocalculator.entities;

import java.util.Objects;

public class Item {

    private String nome;

    private String quantidade;

    public Item(String nome, String quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public Item() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return nome
                + " - " +
                (Objects.isNull(quantidade) ? "0" : quantidade);
    }
}
