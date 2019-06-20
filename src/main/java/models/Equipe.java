package models;

public class Equipe {
    private String nome;
    private Pontuacao pontuacao;

    public Equipe(String nome, Pontuacao pontuacao) {
        this.nome = nome;
        this.pontuacao = pontuacao;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Pontuacao getPontuacao() {
        return pontuacao;
    }
    public void setPontuacao(Pontuacao pontuacao) {
        this.pontuacao = pontuacao;
    }
}
