import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Main;

import static model.data.ProfessionalBase.COMPUTER_SCIENTIST;
import static model.data.ProfessionalBase.DICTATOR;
import static model.moves.Damaging.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BattleTest {
    private Player player1;
    private Player player2;
    private Battle battle;

    @BeforeEach
    public void runBefore() {
        player1 = new Player(0,"Test1");
        player2 = new Player(-1,"Test2");
        player1.setBattleProperties(new Team(player1,"Team1"));
        player2.setBattleProperties(new Team(player2,"Team2"));

    }



    @Test
    public void testLogRounds() {
        setBattle(4,3);

        addRound(3,0);
        Professional dictator = player1.getSelectedProfessional();
        Professional coach = player2.getSelectedProfessional();
        addRound(3,0);//dictator uses protect, coach uses light speed kick
        addRound(0,-1); //dictator uses pop-up punch
        addRound(0,3);
        addRound(0,3);

        battle.logRounds();
        Professional compSci = player2.getSelectedProfessional();
        int dmg2 = POP_UP_PUNCH.getDamage(dictator,compSci);
        int dmg = LIGHTSPE_KICK.getDamage(coach,dictator) + SLAPSTICK_SMACK.getDamage(compSci,dictator);
        assertEquals(dmg,DICTATOR.getLife() - dictator.getLife()); //SLAPSTICK_SMACK did not work

        assertEquals(2 * dmg2,COMPUTER_SCIENTIST.getLife() - compSci.getLife());// dictator did not flinch

    }

    @Test
    //EFFECTS: test log rounds ends and updates the result when p1 wins
    public void testLogRoundsEnd() {
        Professional[] team = player2.getTeam().getMembers();
        for (int i = 1; i < team.length; i++) {
            team[i].takeDamage(team[i].getBase().getLife(),1);
        }
        team[0].takeDamage(team[0].getBase().getLife() - 1,1);
        setBattle(0,0);
        assertEquals("Ongoing",battle.getResultString());
        addRound(3,0);
        battle.logRounds();

        assertEquals("Test1 defeated Test2!",battle.getResultString());
    }

    @Test
    //EFFECTS: test log rounds ends and updates the result when p1 wins
    public void testLogRoundsEndP2() {
        Professional[] team = player1.getTeam().getMembers();
        for (int i = 1; i < team.length; i++) {
            team[i].takeDamage(team[i].getBase().getLife(),1);
        }
        team[0].takeDamage(team[0].getBase().getLife() - 1,1);
        setBattle(0,0);
        assertEquals("Ongoing",battle.getResultString());
        addRound(0,3);
        battle.logRounds();

        assertEquals( "Test2 defeated Test1!",battle.getResultString());
    }


    //EFFECTS: sets the selected professional of both to given index and returns a battle between th two players
    public void setBattle(int p1Prof, int p2Prof) {
        player1.setSelectedProfessional(p1Prof);
        player2.setSelectedProfessional(p2Prof);
        battle = new Battle(player1,player2);
        Main.BATTLEMGR.setBattle(battle);
    }

    //EFFECTS: runs a single round selecting moves of the given indexes
    public Round addRound(int move1, int move2) {
        int ind1 = move1 < 0 ? 1 : 0;
        int ind2 = move2 < 0 ? 1 : 0;
        int[] action1 = new int[]{ind1,Math.abs(move1) - ind1};
        Round round = new Round(battle,action1,new int[]{ind2,Math.abs(move2) - ind2});
        battle.addRound(round);

        return round;
    }




}
