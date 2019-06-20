package models;

import java.util.List;

public class Evento {
    private String nome;
    private List<String> modalidades;
    private List<Equipe> equipesParticipantes;
    private List<Partida> listPartidas;
    private List<List<Equipe>> grupos;

}
