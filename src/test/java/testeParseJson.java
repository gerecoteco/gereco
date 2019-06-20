import com.google.gson.Gson;
import models.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class testeParseJson {
    @Test
    public void serializePartida(){
        Pontuacao pontuacao1 = new Pontuacao(3,4,5,1,0);
        Pontuacao pontuacao2 = new Pontuacao(0,-4,1,5,0);

        Pontuacao[] pontuacoes = {pontuacao1, pontuacao2};
        Partida partida = new Partida(new Equipe("1DS", pontuacao1), pontuacoes);

        System.out.println(new Gson().toJson(partida));
    }

    @Test
    public void serializeEvento(){
        Pontuacao pontuacaoZerada = new Pontuacao(0,0,0,0,0);

        List<Equipe> equipesParticipantes = Arrays.asList(
                new Equipe("1DS", pontuacaoZerada),
                new Equipe("1A", pontuacaoZerada),
                new Equipe("1B", pontuacaoZerada),
                new Equipe("1C", pontuacaoZerada));

        List<Modalidade> modalidades = Arrays.asList(
                new Modalidade("Handebol", new Genero(equipesParticipantes, null, null), new Genero(equipesParticipantes, null, null)),
                new Modalidade("Volei", new Genero(equipesParticipantes, null, null), new Genero(equipesParticipantes, null, null)));

        Evento interclasse = new Evento("Interclasse", modalidades);
        System.out.println(new Gson().toJson(interclasse));
    }
}
