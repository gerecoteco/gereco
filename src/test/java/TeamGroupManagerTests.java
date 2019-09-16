import com.google.gson.Gson;
import helpers.TeamGroupsManager;
import models.Team;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.*;

public class TeamGroupManagerTests {
    TeamGroupsManager teamGroupsManager = new TeamGroupsManager();

    @Test
    public void createGroups(){
        teamGroupsManager.groupAllTeamsByTag(generateGroupAndReturn(Arrays.asList("1", "2", "3"), 22),
                Arrays.asList("1", "2", "3"));
        List<List<Team>> generatedGroups = teamGroupsManager.generateGroupsAndReturn(4);

        generatedGroups.forEach(group -> {
            group.forEach(team -> System.out.print(team.getName() + " "));
            System.out.println();
        });
    }

    private List<Team> generateGroupAndReturn(List<String> tags, int groupLength){
        List<Team> teams = new ArrayList<>();

        for(String tag : tags){
            char c = 'A';
            for(int x=0; x<groupLength; x++) {
                teams.add(new Team(tag + c, tag));
                c++;
            }
        }

        return teams;
    }
}
