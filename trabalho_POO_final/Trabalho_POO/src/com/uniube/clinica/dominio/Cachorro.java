package com.uniube.clinica.dominio;

public class Cachorro extends Pet {
    public Cachorro(String nome, int idade, String raca, Dono dono) {
        super(nome, idade, raca, dono);
    }

    @Override
    public String getTipo() {
        return "Cão";
    }
}