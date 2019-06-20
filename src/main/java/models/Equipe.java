package models;

public class Equipe {
    private String nome;
    private String modalidade;
    private String genero;
    private Pontuacao pontuacao;

    public Equipe(String nome, String modalidade, String genero, Pontuacao pontuacao) {
        this.nome = nome;
        this.modalidade = modalidade;
        this.genero = genero;
        this.pontuacao = pontuacao;
    }
}
