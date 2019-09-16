package helpers;

import models.Team;
import sun.plugin.javascript.navig.Array;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeamGroupsManager {
    private List<Team> teams;
    private List<List<Team>> groupsByTag;
    private List<List<Team>> groups;

    public void groupAllTeamsByTag(List<Team> teams, List<String> tags){
        groupsByTag = new ArrayList<>();
        this.teams = teams;

        tags.forEach(tag -> groupsByTag.add(filterTeamsByTagAndReturn(tag)));
    }

    private List<Team> filterTeamsByTagAndReturn(String tag){
        return teams.stream().filter(team ->
                team.getTag().equals(tag)).collect(Collectors.toList());
    }

    public List<List<Team>> generateGroupsAndReturn(int maxLength){
        groups = new ArrayList<>();

        for(List<Team> group : groupsByTag){
            int numberOfGroups = group.size() % maxLength == 0 ?
                    group.size() / maxLength : group.size() / maxLength + 1;
            List<List<Team>> newGroups = intitializeListAndReturn(numberOfGroups);
            int z = 0;

            for(int x = 0; x < maxLength; x++){
                for(int y = 0; y < numberOfGroups && z < group.size() ; y++){
                    newGroups.get(y).add(x, group.get(z));
                    z++;
                }
            }
            groups.addAll(newGroups);
        }
        return groups;
    }

    private List<List<Team>> intitializeListAndReturn(int numberOfGroups){
        return IntStream.range(0, numberOfGroups)
                .<List<Team>>mapToObj(x -> new ArrayList<>()).collect(Collectors.toList());
    }
}
