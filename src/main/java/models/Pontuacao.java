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

    public int getPontuacao() {
        return pontuacao;
    }
    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }
    public int getSaldo() {
        return saldo;
    }
    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
    public int getPontosProprios() {
        return pontosProprios;
    }
    public void setPontosProprios(int pontosProprios) {
        this.pontosProprios = pontosProprios;
    }
    public int getPontosContra() {
        return pontosContra;
    }
    public void setPontosContra(int pontosContra) {
        this.pontosContra = pontosContra;
    }
    public int getPontosFaltas() {
        return pontosFaltas;
    }
    public void setPontosFaltas(int pontosFaltas) {
        this.pontosFaltas = pontosFaltas;
    }
}
