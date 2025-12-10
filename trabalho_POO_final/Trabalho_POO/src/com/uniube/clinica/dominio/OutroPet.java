package com.uniube.clinica.dominio;

public class OutroPet extends Pet {
    public OutroPet(String nome, int idade, String raca, Dono dono) {
        super(nome, idade, raca, dono);
    }

    @Override
    public String getTipo() {
        return "Outro";
    }
}