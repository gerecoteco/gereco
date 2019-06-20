package models;

public class Partida {
    private String[] idEquipes;
    private Pontuacao[] pontuacoes;

    public Partida(String[] idEquipes, Pontuacao[] pontuacoes) {
        this.idEquipes = idEquipes;
        this.pontuacoes = pontuacoes;
    }

    public String[] getIdEquipes() {
        return idEquipes;
    }
    public void setIdEquipes(String[] idEquipes) {
        this.idEquipes = idEquipes;
    }
    public Pontuacao[] getPontuacoes() {
        return pontuacoes;
    }
    public void setPontuacoes(Pontuacao[] pontuacoes) {
        this.pontuacoes = pontuacoes;
    }
}
