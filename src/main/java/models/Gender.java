package models;

import java.util.List;

public class Gender {
    private List<Team> teams;
    private List<Match> matches;
    private List<List<String>> groups;

    public Gender(List<Team> teams, List<Match> matches, List<List<String>> groups) {
        this.teams = teams;
        this.matches = matches;
        this.groups = groups;
    }

    public List<Team> getTeams() {
        return teams;
    }
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
    public List<Match> getMatches() {
        return matches;
    }
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
    public List<List<String>> getGroups() {
        return groups;
    }
    public void setGroups(List<List<String>> groups) {
        this.groups = groups;
    }
}
