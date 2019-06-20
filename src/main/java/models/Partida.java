package models;

public class Partida {
    private Equipe vencedor;
    private Pontuacao[] pontuacoes;

    public Partida(Equipe vencedor, Pontuacao[] pontuacoes) {
        this.vencedor = vencedor;
        this.pontuacoes = pontuacoes;
    }
}
