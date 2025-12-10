package com.uniube.clinica.dominio;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Consulta implements Agendavel {

    private static int proximoId = 1;

    private final int id;
    private final Pet pet;
    private final Date data;
    private final String descricao;

    public Consulta(Pet pet, Date data, String descricao) {
        this.id = proximoId++;
        this.pet = pet;
        this.data = data;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public Pet getPet() {
        return pet;
    }

    public Date getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String getInformacaoAgenda() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "Consulta [" + id + "] - Pet: " + pet.getNome() + " - " + sdf.format(data);
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "[" + id + "] " + pet.getNome() + " (" + pet.getDono().getNome() + ") - " +
                sdf.format(data) + " - " + descricao;
    }
}