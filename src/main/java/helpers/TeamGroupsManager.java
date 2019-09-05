package helpers;

import models.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamGroupsManager {
    private List<List<String>> groupsByTag;
    private List<List<String>> groups;

    public void groupAllTeamsByTag(List<Team> teams, List<String> tags){
        groupsByTag = new ArrayList<>();

        tags.forEach(tag -> {
            List<String> group = new ArrayList<>();
            teams.forEach(team -> {
                if(team.getTag().equals(tag)) group.add(team.getName());
            });
            groupsByTag.add(group);
        });
    }

    public void generateGroups(int minLength, int maxLength){
        groups = new ArrayList<>();

        groupsByTag.forEach(group -> {
            int restOfDivisionByMax = group.size() % maxLength;
            int restOfDivisionByMin = group.size() % minLength;

            if(restOfDivisionByMax != 0){
                if(restOfDivisionByMax > restOfDivisionByMin && restOfDivisionByMin != 0)
                    splitIntoSmallerGroups(group, maxLength);
                else
                    splitIntoSmallerGroups(group, minLength);
            } else
                splitIntoSmallerGroups(group, maxLength);
        });

        System.out.println(groups);
    }

    private void splitIntoSmallerGroups(List<String> group, int groupsLength){
        int numberOfGroups = group.size() % groupsLength == 0 ?
                group.size() / groupsLength : (group.size() / groupsLength) + 1;

        for(int x = 0; x < numberOfGroups; x++) {
            List<String> newGroup = new ArrayList<>();

            int initialIndex = x * groupsLength;
            int lastIndex = x == numberOfGroups-1 ?
                    initialIndex + (group.size() - initialIndex) : ((x+1) * groupsLength);

            for (int y = initialIndex; y < lastIndex; y++)
                newGroup.add(group.get(y));

            groups.add(newGroup);
        }
    }
}
