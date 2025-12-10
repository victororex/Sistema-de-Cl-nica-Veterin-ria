package com.uniube.clinica.dominio;

public class Dono extends Pessoa {

    private static int proximoId = 1;
    private final int id;

    public Dono(String nome, String telefone, String endereco) {
        super(nome, telefone, endereco);
        this.id = proximoId++;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + getNome() + " - " + getTelefone();
    }
}