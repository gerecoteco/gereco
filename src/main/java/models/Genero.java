package models;

import java.util.List;

public class Genero {
    private List<Equipe> equipes;
    private List<List<String>> grupos;
    private List<Partida> partidas;

    public Genero(List<Equipe> equipes, List<List<String>> grupos, List<Partida> partidas) {
        this.equipes = equipes;
        this.grupos = grupos;
        this.partidas = partidas;
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }
    public void setEquipes(List<Equipe> equipes) {
        this.equipes = equipes;
    }
    public List<List<String>> getGrupos() {
        return grupos;
    }
    public void setGrupos(List<List<String>> grupos) {
        this.grupos = grupos;
    }
    public List<Partida> getPartidas() {
        return partidas;
    }
    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }
}
