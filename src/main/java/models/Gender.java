package models;

import java.util.List;

public class Gender {
    private List<Team> teams;
    private List<List<String>> groups;
    private List<Match> matches;

    public Gender(List<Team> teams, List<List<String>> groups, List<Match> matches) {
        this.teams = teams;
        this.groups = groups;
        this.matches = matches;
    }

    public List<Team> getTeams() {
        return teams;
    }
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
    public List<List<String>> getGroups() {
        return groups;
    }
    public void setGroups(List<List<String>> groups) {
        this.groups = groups;
    }
    public List<Match> getMatches() {
        return matches;
    }
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
