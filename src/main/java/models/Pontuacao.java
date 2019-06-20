package models;

public class Pontuacao {
    private int pontuacao;
    private int saldo;
    private int pontosProprios;
    private int pontosContra;
    private int pontosFaltas;

    public Pontuacao(int pontuacao, int saldo, int pontosProprios, int pontosContra, int pontosFaltas) {
        this.pontuacao = pontuacao;
        this.saldo = saldo;
        this.pontosProprios = pontosProprios;
        this.pontosContra = pontosContra;
        this.pontosFaltas = pontosFaltas;
    }
}
