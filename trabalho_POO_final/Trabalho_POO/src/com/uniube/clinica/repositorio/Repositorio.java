package com.uniube.clinica.repositorio;

import com.uniube.clinica.dominio.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Repositorio {

    private static final List<Dono> donos = new ArrayList<>();
    private static final List<Pet> pets = new ArrayList<>();
    private static final List<Consulta> consultas = new ArrayList<>();

    private Repositorio() { }


    public static List<Dono> getDonos() {
        return Collections.unmodifiableList(donos);
    }

    public static List<Pet> getPets() {
        return Collections.unmodifiableList(pets);
    }

    public static List<Consulta> getConsultas() {
        return Collections.unmodifiableList(consultas);
    }


    public static void adicionarDono(Dono d) {
        donos.add(d);
    }

    public static void removerDono(Dono d) {
        donos.remove(d);
    }

    public static void adicionarPet(Pet p) {
        pets.add(p);
    }

    public static void removerPet(Pet p) {
        pets.remove(p);
    }

    public static void adicionarConsulta(Consulta c) {
        consultas.add(c);
    }

    public static void removerConsulta(Consulta c) {
        consultas.remove(c);
    }


    public static Dono buscarDonoPorId(int id) {
        for (Dono d : donos) if (d.getId() == id) return d;
        return null;
    }

    public static Dono buscarDonoPorNome(String nome) {
        for (Dono d : donos)
            if (d.getNome().equalsIgnoreCase(nome))
                return d;
        return null;
    }

    public static List<Pet> buscarPetsPorNome(String nome) {
        List<Pet> lista = new ArrayList<>();
        for (Pet p : pets)
            if (p.getNome().equalsIgnoreCase(nome))
                lista.add(p);
        return lista;
    }

    public static List<Pet> buscarPetsPorNomeDono(String nomeDono) {
        List<Pet> lista = new ArrayList<>();
        for (Pet p : pets)
            if (p.getDono().getNome().equalsIgnoreCase(nomeDono))
                lista.add(p);
        return lista;
    }

    public static Pet buscarPetPorId(int id) {
        for (Pet p : pets) if (p.getId() == id) return p;
        return null;
    }

    public static Consulta buscarConsultaPorId(int id) {
        for (Consulta c : consultas) if (c.getId() == id) return c;
        return null;
    }
}
