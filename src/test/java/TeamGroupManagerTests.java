import helpers.MatchesGenerator;
import helpers.TeamGroupsManager;
import models.Match;
import models.Team;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.*;

public class TeamGroupManagerTests {
    private TeamGroupsManager teamGroupsManager;
    private List<Team> teams;
    private List<String> tags;
    private int numberOfTeams;
    private int maxLength;

    @Before
    public void start(){
        teamGroupsManager = new TeamGroupsManager();
        tags = Arrays.asList("1", "2", "3");
        maxLength = 4;
    }

    @Test
    public void test17withoutTags(){
        numberOfTeams = 17;
        tags = Collections.emptyList();
        teams = generateGroupsForTests();
        List<List<Team>> generatedGroups = createOrderedGroupsAndReturn();

        teamGroupsManager.getOrderedTeams();

        assertEquals(4, generatedGroups.get(0).size());
        assertEquals(4, generatedGroups.get(1).size());
        assertEquals(3, generatedGroups.get(2).size());
        assertEquals(3, generatedGroups.get(3).size());
        assertEquals(3, generatedGroups.get(4).size());
    }

    @Test
    public void test5withoutTags(){
        numberOfTeams = 5;
        tags = Collections.emptyList();
        teams = generateGroupsForTests();
        List<List<Team>> generatedGroups = createOrderedGroupsAndReturn();

        assertEquals(3, generatedGroups.get(0).size());
        assertEquals(2, generatedGroups.get(1).size());
    }

    @Test
    public void test6With3Tags(){
        numberOfTeams = 6;
        teams = generateGroupsForTests();
        List<List<Team>> generatedGroups = createOrderedGroupsAndReturn();

        assertEquals(3, generatedGroups.get(0).size());
        assertEquals(3, generatedGroups.get(1).size());
        assertEquals(3, generatedGroups.get(2).size());
        assertEquals(3, generatedGroups.get(3).size());
        assertEquals(3, generatedGroups.get(4).size());
        assertEquals(3, generatedGroups.get(5).size());
    }

    @Test
    public void testDiferentNumberOfTeamsPerTag(){
        teams = Arrays.asList(
                new Team("1A", "1"), new Team("1B", "1"), new Team("1C", "1"),
                new Team("1D", "1"), new Team("2A", "2"), new Team("2B", "2"),
                new Team("2C", "2"), new Team("2D", "2"), new Team("2E", "2"));
        List<List<Team>> generatedGroups = createOrderedGroupsAndReturn();

        assertEquals(4, generatedGroups.get(0).size());
        assertEquals(3, generatedGroups.get(1).size());
        assertEquals(2, generatedGroups.get(2).size());
    }

    @Test
    public void generateMatchs(){
        numberOfTeams = 5;
        teams = generateGroupsForTests();
        List<Match> matches = new MatchesGenerator().generateMatchesAndReturn(createOrderedGroupsAndReturn());

        matches.forEach(match -> {
            String message = match.getTeams().get(0) + " X " + match.getTeams().get(1);
            System.out.println(message);
        });
    }

    private List<List<Team>> createOrderedGroupsAndReturn(){
        teamGroupsManager.groupAllTeamsByTag(teams, tags);
        return teamGroupsManager.generateGroupsAndReturn(maxLength);
    }

    private List<Team> generateGroupsForTests(){
        return tags.isEmpty() ? generateWithoutTag() : generateWithTag();
    }

    private List<Team> generateWithTag(){
        List<Team> teams = new ArrayList<>();

        for(String tag : tags){
            char c = 'A';
            for(int x=0; x < numberOfTeams; x++) {
                teams.add(new Team(tag + c, tag));
                c++;
            }
        }

        return teams;
    }

    private List<Team> generateWithoutTag(){
        List<Team> teams = new ArrayList<>();
        char c = 'A';

        for (int x=0; x < numberOfTeams; x++){
            teams.add(new Team(String.valueOf(c), null));
            c++;
        }

        return teams;
    }
}
