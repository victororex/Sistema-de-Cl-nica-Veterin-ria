package com.uniube.clinica.dominio;

public class Gato extends Pet {
    public Gato(String nome, int idade, String raca, Dono dono) {
        super(nome, idade, raca, dono);
    }

    @Override
    public String getTipo() {
        return "Gato";
    }
}
