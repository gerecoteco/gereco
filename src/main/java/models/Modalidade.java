package models;

public class Modalidade {
    private String nome;
    private Genero[] generos;

    public Modalidade(String nome, Genero[] generos) {
        this.nome = nome;
        this.generos = generos;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Genero[] getGeneros() {
        return generos;
    }
    public void setGeneros(Genero[] generos) {
        this.generos = generos;
    }
}
