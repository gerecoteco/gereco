package models;

public class Modalidade {
    private String nome;
    private Genero masculino;
    private Genero feminino;

    public Modalidade(String nome, Genero masculino, Genero feminino) {
        this.nome = nome;
        this.masculino = masculino;
        this.feminino = feminino;
    }

    public String getNome() {
        return nome;
    }
    public Genero getMasculino() {
        return masculino;
    }
    public Genero getFeminino() {
        return feminino;
    }
}
