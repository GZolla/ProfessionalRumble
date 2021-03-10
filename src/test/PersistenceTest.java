import model.Battle;
import model.Player;
import model.Team;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonWriter;
import persistence.SaveAble;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenceTest {
    private final Player test1 = new Player(0, "Test1");

    @BeforeEach
    //EFFECTS: Clears stored teams and battles
    public void runBefore() {
        JsonWriter writer = new JsonWriter( "battles/0.json");
        try {
            writer.open();
            writer.writeArray(new JSONArray());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        writer = new JsonWriter( "teams/0.json");
        try {
            writer.open();
            writer.writeArray(new JSONArray());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    //EFFECTS: Test both teams and battles are saved and loaded properly
    public void testSaveAndLoad() {
        SaveAble[] teams = test1.loadTeams();
        assertEquals(0,teams.length); // Test on empty
        Team team = new Team(test1,"NewTeam");
        team.save(0);
        teams = test1.loadTeams();
        assertEquals(1,teams.length); // Team was saved
        test1.setBattleProperties(0,teams);

        SaveAble[] battles = test1.loadBattles();
        assertEquals(0,battles.length);

        Player guest = new Player(-1,"Guest");
        guest.setBattleProperties(0,teams);
        new Battle(test1,guest).save();

        battles = test1.loadBattles();
        assertEquals(1,battles.length);

    }

    @Test
    //EFFECTS: Test adding multiple teams and that teams can be replaced and removed.
    public void testMultipleReplaceAndRemoveTeam() {
        Team team = new Team(test1,"NewTeam");
        team.save(0);

        new Team(test1,"Team 2").save(1);
        new Team(test1,"Team 3").save(2);
        assertEquals(3,test1.loadTeams().length);

        new Team(test1,"Team 1").save(0);//Replaces
        SaveAble[] teams = test1.loadTeams();
        assertEquals(3,teams.length);
        assertEquals("Team 1",teams[0].getName());

        test1.removeTeam(1);
        teams = test1.loadTeams();
        assertEquals(2,teams.length);
        assertEquals("Team 3",teams[1].getName());
    }

    

}
