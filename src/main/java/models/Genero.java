package models;

import java.util.List;

public class Genero {
    private List<Equipe> equipes;
    private List<List<Equipe>> grupos;
    private List<Partida> partidas;

    public Genero(List<Equipe> equipes, List<List<Equipe>> grupos, List<Partida> partidas) {
        this.equipes = equipes;
        this.grupos = grupos;
        this.partidas = partidas;
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }
    public List<List<Equipe>> getGrupos() {
        return grupos;
    }
    public List<Partida> getPartidas() {
        return partidas;
    }
}
