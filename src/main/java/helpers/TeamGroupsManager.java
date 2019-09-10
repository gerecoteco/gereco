package helpers;

import models.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TeamGroupsManager {
    private List<Team> teams;
    private List<List<Team>> groupsByTag;
    private List<List<Team>> groups;
    private int minLength, maxLength;

    public List<Team> getTeams() {
        return teams;
    }
    public List<List<Team>> getGroups() {
        return groups;
    }

    public void groupAllTeamsByTag(List<Team> teams, List<String> tags){
        groupsByTag = new ArrayList<>();
        this.teams = teams;

        tags.forEach(tag -> groupsByTag.add(filterTeamsByTagAndReturn(tag)));
    }

    private List<Team> filterTeamsByTagAndReturn(String tag){
        return teams.stream().filter(team ->
                team.getTag().equals(tag)).collect(Collectors.toList());
    }

    public List<List<Team>> generateGroupsAndReturn(int minLength, int maxLength){
        groups = new ArrayList<>();
        this.maxLength = maxLength;
        this.minLength = minLength;

        groupsByTag.forEach(this::splitIntoSmallerGroups);
        showGroups();
        return groups;
    }

    private void splitIntoSmallerGroups(List<Team> group){
        int groupsLength = getBestGroupLength(group);
        int numberOfGroups = group.size() / groupsLength + (group.size() % groupsLength != 0 ? 1 : 0);

        Collections.shuffle(group);
        for(int x = 0; x < numberOfGroups; x++) {
            List<Team> newGroup = new ArrayList<>();
            int initialIndex = x * groupsLength;
            int lastIndex = x == (numberOfGroups - 1) ? group.size() : initialIndex + groupsLength;

            for (int y = initialIndex; y < lastIndex; y++) {
                Team actualTeam = group.get(y);
                teams.stream().filter(team -> team.getName().equals(
                        actualTeam.getName())).findAny().orElse(null).setGroup(groups.size());
                newGroup.add(actualTeam);
            }
            groups.add(newGroup);
        }
    }

    private int getBestGroupLength(List<Team> group){
        int lastDivisible = 0;
        int greatestRest = 0;

        for(int x = minLength; x <= maxLength; x++){
            int numberOfGroups = group.size() / x;
            lastDivisible = group.size() % x == 0 ? x : lastDivisible;
            greatestRest = (group.size() - (numberOfGroups * x)) >= greatestRest ? x : greatestRest;
        }

        return lastDivisible == 0 ? (group.size() / greatestRest) > (group.size() / maxLength) ? maxLength : greatestRest :
                group.size() / lastDivisible <= (group.size() / greatestRest) + 1 ? lastDivisible : greatestRest;
    }

    private void showGroups(){
        groups.forEach(group -> {
            group.forEach(team -> System.out.print(team.getName() + " "));
            System.out.println("\n");
        });
    }
}
