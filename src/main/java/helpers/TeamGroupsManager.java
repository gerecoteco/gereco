package helpers;

import models.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TeamGroupsManager {
    private List<Team> teams;
    private List<List<String>> groupsByTag;
    private List<List<String>> groups;

    public void groupAllTeamsByTag(List<Team> teams, List<String> tags){
        groupsByTag = new ArrayList<>();
        this.teams = teams;

        tags.forEach(tag -> groupsByTag.add(filterTeamsByTagAndReturn(tag)));
    }

    private List<String> filterTeamsByTagAndReturn(String tag){
        return teams.stream().filter(team ->
                team.getTag().equals(tag)).map(Team::getName).collect(Collectors.toList());
    }

    public List<List<String>> generateGroupsAndReturn(int minLength, int maxLength){
        groups = new ArrayList<>();

        groupsByTag.forEach(group -> {
            int restOfDivisionByMax = group.size() % maxLength;
            int restOfDivisionByMin = group.size() % minLength;
            boolean createGroupWithMaxLength = (restOfDivisionByMax > restOfDivisionByMin
                    && restOfDivisionByMin != 0) || restOfDivisionByMax == 0;

            splitIntoSmallerGroups(group, createGroupWithMaxLength ? maxLength : minLength);
        });

        System.out.println(groups);
        return groups;
    }

    private void splitIntoSmallerGroups(List<String> group, int groupsLength){
        int numberOfGroups = group.size() / groupsLength + (group.size() % groupsLength != 0 ? 1 : 0);

        Collections.shuffle(group);
        for(int x = 0; x < numberOfGroups; x++) {
            List<String> newGroup = new ArrayList<>();
            int initialIndex = x * groupsLength;
            int lastIndex = x == (numberOfGroups - 1) ? group.size() : initialIndex + groupsLength;

            for (int y = initialIndex; y < lastIndex; y++) newGroup.add(group.get(y));
            groups.add(newGroup);
        }
    }
}
