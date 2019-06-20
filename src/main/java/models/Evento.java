package models;

import java.util.List;

public class Evento {
    private String nome;
    private List<Modalidade> modalidades;

    public Evento(String nome, List<Modalidade> modalidades) {
        this.nome = nome;
        this.modalidades = modalidades;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public List<Modalidade> getModalidades() {
        return modalidades;
    }
    public void setModalidades(List<Modalidade> modalidades) {
        this.modalidades = modalidades;
    }
}
