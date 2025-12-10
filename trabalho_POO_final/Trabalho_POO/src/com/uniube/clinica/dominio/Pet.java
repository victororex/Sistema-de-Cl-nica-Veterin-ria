package com.uniube.clinica.dominio;

public abstract class Pet {

    private static int proximoId = 1;

    private final int id;
    private String nome;
    private int idade;
    private String raca;
    private Dono dono;

    public Pet(String nome, int idade, String raca, Dono dono) {
        this.id = proximoId++;
        this.nome = nome;
        this.idade = idade;
        this.raca = raca;
        this.dono = dono;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String n) {
        if (n == null || n.trim().isEmpty()) return;
        this.nome = n.trim();
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if (idade < 0) return;
        this.idade = idade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String r) {
        if (r == null) return;
        this.raca = r.trim();
    }

    public Dono getDono() {
        return dono;
    }

    public void setDono(Dono d) {
        if (d == null) return;
        this.dono = d;
    }

    public abstract String getTipo();

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " (" + getTipo() + ") - " + raca +
                " - Dono: " + dono.getNome();
    }
}