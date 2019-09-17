package helpers;

import models.Team;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeamGroupsManager {
    private List<Team> teams;
    private List<List<Team>> groupsByTag;
    private List<List<Team>> groups;
    private int maxLength;

    public void groupAllTeamsByTag(List<Team> teams, List<String> tags){
        groupsByTag = new ArrayList<>();
        this.teams = teams;

        if(!tags.isEmpty()) tags.forEach(tag -> groupsByTag.add(filterTeamsByTagAndReturn(tag)));
        else groupsByTag.add(teams);
    }

    private List<Team> filterTeamsByTagAndReturn(String tag){
        return teams.stream().filter(team ->
                team.getTag().equals(tag)).collect(Collectors.toList());
    }

    public List<List<Team>> generateGroupsAndReturn(int maxLength){
        groups = new ArrayList<>();
        this.maxLength = maxLength;

        groupsByTag.forEach(this::splitGroups);
        showGroups();
        return groups;
    }

    private void splitGroups(List<Team> group){
        int numberOfGroups = group.size() % maxLength == 0 ? group.size() / maxLength : group.size() / maxLength + 1;
        List<List<Team>> newGroups = intitializeListAndReturn(numberOfGroups);
        int teamIndex = 0;

        Collections.shuffle(group);
        for(int lengthIndex = 0; lengthIndex < maxLength; lengthIndex++){
            for(int groupIndex = 0; groupIndex < numberOfGroups && teamIndex < group.size(); groupIndex++){
                newGroups.get(groupIndex).add(lengthIndex, group.get(teamIndex));
                teamIndex++;
            }
        }
        groups.addAll(newGroups);
    }

    private List<List<Team>> intitializeListAndReturn(int numberOfGroups){
        return IntStream.range(0, numberOfGroups)
                .<List<Team>>mapToObj(x -> new ArrayList<>()).collect(Collectors.toList());
    }

    private void showGroups(){
        groups.forEach(group -> {
            group.forEach(team -> System.out.print(team.getName() + " "));
            System.out.println();
        });
    }
}
