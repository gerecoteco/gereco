import com.google.gson.Gson;
import models.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class testeParseJson {
    @Test
    public void serializePartida(){
        Pontuacao pontuacao1 = new Pontuacao(3,4,5,1,0);
        Pontuacao pontuacao2 = new Pontuacao(0,-4,1,5,0);

        Partida partida = new Partida(new String[]{"2839123fsd", "31231gfda"}, new Pontuacao[]{pontuacao1, pontuacao2});
        System.out.println(new Gson().toJson(partida));
    }

    @Test
    public void serializeEvento(){
        // Pontuacoes
        Pontuacao pontuacaoZerada = new Pontuacao(0,0,0,0,0);
        Pontuacao pontuacao1 = new Pontuacao(3,4,5,1,0);
        Pontuacao pontuacao2 = new Pontuacao(0,-4,1,5,0);

        // Partidas
        Partida partida = new Partida(new String[]{"2839123fsd", "31231gfda"}, new Pontuacao[]{pontuacao1, pontuacao2});
        Partida partida2 = new Partida(new String[]{"2839123fsd", "31231gfda"}, new Pontuacao[]{pontuacao1, pontuacao2});
        Partida partida3 = new Partida(new String[]{"2839123fsd", "31231gfda"}, new Pontuacao[]{pontuacao1, pontuacao2});

        // Equipes
        List<Equipe> equipesParticipantes = Arrays.asList(
                new Equipe("1DS", pontuacaoZerada),
                new Equipe("1A", pontuacaoZerada),
                new Equipe("1B", pontuacaoZerada),
                new Equipe("1C", pontuacaoZerada));

        List<List<String>> grupos = new ArrayList<>();
        grupos.add(Arrays.asList("1DS", "1A", "1B", "1C"));
        grupos.add(Arrays.asList("2DS", "2A", "2B", "2C"));
        grupos.add(Arrays.asList("3DS", "3A", "3B", "3C"));

        List<Modalidade> modalidades = Arrays.asList(
                new Modalidade("Handebol", new Genero[]{new Genero(equipesParticipantes, grupos, Arrays.asList(partida, partida2, partida3)),
                        new Genero(equipesParticipantes, grupos, Arrays.asList(partida, partida2, partida3))}),
                new Modalidade("Volei", new Genero[]{new Genero(equipesParticipantes, grupos, Arrays.asList(partida, partida2, partida3)),
                        new Genero(equipesParticipantes, grupos, Arrays.asList(partida, partida2, partida3))}));

        Evento interclasse = new Evento("Interclasse", modalidades);
        System.out.println(new Gson().toJson(interclasse));

        interclasse.getModalidades().get(0).getGeneros()[0].getEquipes().get(0).getPontuacao().getSaldo();
        interclasse.getModalidades().get(0).getGeneros()[1].getPartidas().get(0).getIdEquipes();
    }
}
