package models;

import java.util.List;

public class Evento {
    private String nome;
    private List<String> modalidades;
    private List<Equipe> equipesParticipantes;
    private List<Partida> partidas;
    private List<List<Equipe>> grupos;

    public Evento(String nome, List<String> modalidades, List<Equipe> equipesParticipantes, List<Partida> partidas, List<List<Equipe>> grupos) {
        this.nome = nome;
        this.modalidades = modalidades;
        this.equipesParticipantes = equipesParticipantes;
        this.partidas = partidas;
        this.grupos = grupos;
    }
}
