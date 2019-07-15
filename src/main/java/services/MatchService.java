package services;

import models.Match;

import java.util.List;

public class MatchService {
    private GenderService genderService = new GenderService();

    public List<Match> requestAllMatchs(String eventId, int indexModality, int indexGender){
        return genderService.requestOneGender(eventId, indexModality, indexGender).getMatches();
    }

    public Match requestOneMatch(String eventId, int indexModality, int indexGender, int indexMatch){
        return genderService.requestOneGender(eventId, indexModality, indexGender).getMatches().get(indexMatch);
    }
}
