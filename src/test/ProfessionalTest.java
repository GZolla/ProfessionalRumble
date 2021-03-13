import model.Player;
import model.Professional;
import model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.data.NonVolatile.*;
import static model.data.ProfessionalBase.*;
import static model.data.Volatile.*;
import static model.moves.Damaging.*;
import static model.moves.NonDamaging.HYPEREXAMINE;
import static model.moves.NonDamaging.PROTECT;
import static org.junit.jupiter.api.Assertions.*;

public class ProfessionalTest {
    private Player player1;
    private Player player2;
    private Team team1;
    private Team team2;


    @BeforeEach
    public void runBefore() {
        player1 = new Player(-1,"Test1");
        player2 = new Player(-2,"Test2");
        Team[] teamSetter = new Team[] {new Team(player1,"Team1"),new Team(player2,"Team2")};
        player1.setBattleProperties(0,teamSetter);
        player2.setBattleProperties(1,teamSetter);

        team1 = player1.getTeam();

        team2 = player2.getTeam();
    }


//--- MOVES ------------------------------------------------------------------------------------------------------------

    @Test
    //EFFECTS: test canMove returns false if and only if it has any status that prevents moving
    public void testCanMove() {
        Professional testSubject = team1.getMembers()[0];
        assertTrue(testSubject.canMove());

        testSubject.addVolatileStatus(FLINCH);
        assertFalse(testSubject.canMove());

        testSubject.removeVolatileStatus(FLINCH);
        testSubject.addVolatileStatus(DRAIND);
        assertFalse(testSubject.canMove());

        testSubject.removeVolatileStatus(DRAIND);
        testSubject.setNonVolatileStatus(UNEMP);
        assertFalse(testSubject.canMove());

        testSubject.setNonVolatileStatus(DEMOR);//nonVolatileTurns starts at 0
        assertTrue(testSubject.canMove());

        testSubject.checkNonVolatileStatus();//Now nonVolatileTurns is 1
        assertFalse(testSubject.canMove());
    }

    @Test
    //EFFECTS: tests gotNauseatedOnMove only returns true if prof. is nauseated and has been for an odd # of turns
    public void testGotNauseatedOnMove() {
        Professional testSubject = team1.getMembers()[5];
        assertFalse(testSubject.gotNauseatedOnMove());

        testSubject.addVolatileStatus(NAUSEA);
        assertFalse(testSubject.gotNauseatedOnMove());

        testSubject.updateVolatileStatus();
        assertTrue(testSubject.gotNauseatedOnMove());

        int expectedDMG = (10 * testSubject.getBase().getStrength()) / testSubject.getBase().getResistance();
        assertEquals(expectedDMG,testSubject.getBase().getLife()- testSubject.getLife());
    }

    @Test
    //EFFECTS: tests hasMove returns true only if it has a move
    public void testHasMove() {
        Professional testSubject = team2.getMembers()[3];
        assertFalse(testSubject.hasMove(LONG_AWAITED_NOVEL));
        assertTrue(testSubject.hasMove(LIGHTSPE_KICK));
        assertTrue(testSubject.hasMove(GRAVITY_SLAM));
        assertTrue(testSubject.hasMove(DOMINATE));
        assertTrue(testSubject.hasMove(RED_CARD_FOUL));

        Professional testSubject2 = team1.getMembers()[4];

        assertFalse(testSubject2.hasMove(BACKPACK_SMACK));
        assertTrue(testSubject2.hasMove(POP_UP_PUNCH));
        assertTrue(testSubject2.hasMove(LANDMINE));
        assertTrue(testSubject2.hasMove(PROTECT));
        assertTrue(testSubject2.hasMove(HYPEREXAMINE));
    }

    //useMove method tested extensively in MoveTestClass

//--- STATUS -----------------------------------------------------------------------------------------------------------

    @Test
    public void testCheckNonVolatileStatus () {
        Professional compSci = team1.getMembers()[0];
        compSci.setNonVolatileStatus(SICK);
        compSci.checkNonVolatileStatus();
        int compBaseLife = COMPUTER_SCIENTIST.getLife();
        assertEquals(compBaseLife / 16,compBaseLife - compSci.getLife());

        compSci.checkNonVolatileStatus();
        assertEquals(3 * (compBaseLife / 16),compBaseLife - compSci.getLife());


        compSci.setNonVolatileStatus(INJUR);
        compSci.setBase(COMPUTER_SCIENTIST);//Refresh life to full
        compSci.checkNonVolatileStatus();
        assertEquals( compBaseLife / 16,compBaseLife - compSci.getLife());
        compSci.checkNonVolatileStatus();
        assertEquals( 2 * (compBaseLife / 16),compBaseLife - compSci.getLife());

        compSci.setNonVolatileStatus(DEPRE);
        compSci.setBase(COMPUTER_SCIENTIST);//Refresh life to full
        compSci.checkNonVolatileStatus();
        assertEquals( compBaseLife / 16,compBaseLife - compSci.getLife());
        compSci.checkNonVolatileStatus();
        assertEquals( 2 * (compBaseLife / 16),compBaseLife - compSci.getLife());


        compSci.setNonVolatileStatus(UNEMP);
        compSci.checkNonVolatileStatus();
        compSci.checkNonVolatileStatus();
        compSci.checkNonVolatileStatus();
        assertEquals(null,compSci.getNonVolatileStatus());
    }


    @Test
    //EFFECTS: test getLeader method
    public void testGetLeader() {
        assertEquals(player1.getId(),team1.getMembers()[0].getLeader().getId());
        assertEquals(player1.getId(),team1.getMembers()[5].getLeader().getId());

        assertEquals(player2.getId(),team2.getMembers()[2].getLeader().getId());
        assertEquals(player2.getId(),team2.getMembers()[3].getLeader().getId());
    }

    @Test
    //EFFECTS: test getMoveNames and setMove methods
    public void testGetMoveNamesAndSetMove() {
        TableAbleTest.areEqual(
                new String[]{"Breakthrough blast","Compromise network","Keyboard slam","Slapstick smack"},
                team2.getMembers()[0].getMoveNames()
        );
        TableAbleTest.areEqual(
                new String[]{"Psychological torture","Accelerate","Friendly match","Momentum collision"},
                team1.getMembers()[5].getMoveNames()
        );
        team1.getMembers()[5].setMove(3,LANDMINE);
        TableAbleTest.areEqual(
                new String[]{"Psychological torture","Accelerate","Friendly match","Landmine"},
                team1.getMembers()[5].getMoveNames()
        );
    }

    @Test
    //EFFECTS: test setBase changes professional base and restores life to new base's max
    public void testSetBase () {
        Professional prof = team2.getMembers()[3];
        assertEquals(SELFDEFENSE_COACH,prof.getBase());
        assertEquals(SELFDEFENSE_COACH.getLife(),prof.getLife());
        prof.setBase(JUDGE);
        assertEquals(JUDGE,prof.getBase());
        assertEquals(JUDGE.getLife(),prof.getLife());

        prof.setBase(LAWYER);
        assertEquals(LAWYER,prof.getBase());
        assertEquals(LAWYER.getLife(),prof.getLife());
    }
}
