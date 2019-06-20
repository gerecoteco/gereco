import com.google.gson.Gson;
import models.Equipe;
import models.Evento;
import models.Partida;
import models.Pontuacao;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class testeParseJson {
    @Test
    public void serializePartida(){
        Pontuacao pontuacao1 = new Pontuacao(3,4,5,1,0);
        Pontuacao pontuacao2 = new Pontuacao(0,-4,1,5,0);

        Pontuacao[] pontuacoes = {pontuacao1, pontuacao2};
        Partida partida = new Partida(new Equipe("1DS", "Futsal", "m", pontuacao1), pontuacoes);

        System.out.println(new Gson().toJson(partida));
    }

    @Test
    public void serializeEvento(){
        List<String> modalidades = Arrays.asList("Handebol", "Futsal", "Volei", "Basquete");
        Pontuacao pontuacaoZerada = new Pontuacao(0,0,0,0,0);
        List<Equipe> equipesParticipantes = Arrays.asList(
                new Equipe("1DS", modalidades.get(0), "m", pontuacaoZerada),
                new Equipe("1A", modalidades.get(0), "m", pontuacaoZerada),
                new Equipe("1B", modalidades.get(0), "m", pontuacaoZerada),
                new Equipe("1C", modalidades.get(0), "m", pontuacaoZerada));

        Evento interclasse = new Evento("Interclasse", modalidades, equipesParticipantes, null, null);
        System.out.println(new Gson().toJson(interclasse));
    }
}
