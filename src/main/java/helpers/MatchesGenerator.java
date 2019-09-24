package helpers;

import models.Match;
import models.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchesGenerator {
    private List<Match> matches;

    public List<Match> generateMatchesAndReturn(List<List<Team>> groups){
        matches = new ArrayList<>();
        groups.forEach(group -> matches.addAll(generateGroupMatches(group)));

        return matches;
    }

    public List<Match> generateGroupMatches(List<Team> group){
        List<Match> groupMatches = new ArrayList<>();

        for(int initialIndex = 0; initialIndex < group.size(); initialIndex++){
            for (int x = initialIndex + 1; x < group.size(); x++){
                List<String> teams = Arrays.asList(group.get(initialIndex).getName(), group.get(x).getName());
                groupMatches.add(new Match(teams));
            }
        }

        return groupMatches;
    }
}
