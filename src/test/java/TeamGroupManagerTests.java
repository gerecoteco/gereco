import helpers.TeamGroupsManager;
import models.Team;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamGroupManagerTests {
    TeamGroupsManager teamGroupsManager = new TeamGroupsManager();

    @Test
    public void createGroups(){
        List<Team> teams = new ArrayList<>();
        teams.add(new Team("1A", "1"));
        teams.add(new Team("1B", "1"));
        teams.add(new Team("1C", "1"));
        teams.add(new Team("1D", "1"));
        teams.add(new Team("1E", "1"));
        teams.add(new Team("1F", "1"));

        teams.add(new Team("2A", "2"));
        teams.add(new Team("2B", "2"));
        teams.add(new Team("2C", "2"));
        teams.add(new Team("2D", "2"));
        teams.add(new Team("2E", "2"));
        teams.add(new Team("2F", "2"));

        teams.add(new Team("3A", "3"));
        teams.add(new Team("3B", "3"));
        teams.add(new Team("3C", "3"));
        teams.add(new Team("3D", "3"));
        teams.add(new Team("3E", "3"));
        teams.add(new Team("3F", "3"));

        teamGroupsManager.groupAllTeamsByTag(teams,
                Arrays.asList("1", "2", "3"));
        teamGroupsManager.generateGroups(4, 4);
    }
}
