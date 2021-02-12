package model;

import model.moves.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.data.ProfessionalBase.*;
import static model.data.Volatile.CRITIC;
import static model.moves.DamagingMove.*;
import static model.moves.StatusMove.*;
import static org.junit.jupiter.api.Assertions.*;
import static ui.Main.PLAYER_1;
import static ui.Main.PLAYER_2;

public class PlayerTest {



    @BeforeEach
    public void runBefore(){
        Move[] compSciMoves = new Move[]{CABLE_WHIP,COMPROMISE_NETWORK,KEYBOARD_SLAM,WORKOUT};
        Move[] presiMoves = new Move[]{PRESI_PUNCH,ICBM_STRIKE,SURVEILL,OPTIMIZATION};
        Move[] cbcMoves = new Move[]{REITERATIVE_PUNCH,ICBM_STRIKE,SURVEILL,OPTIMIZATION};
        Move[] coachMoves = new Move[]{BACKPACK_SMACK,DISOBEDIENCE_SLAP, DOMINATE,REDCARD_FOUL};
        Move[] dictMoves = new Move[]{ICBM_STRIKE,HYPEREXAMINE,LONG_AWAITED_NOVEL,CORPORAL_PUNISHMENT};
        Move[] qbMoves = new Move[]{WORKOUT,ACCELERATE,FRIENDLY_MATCH,MOMENTUM_COLLISION};

        Professional compScientist = new Professional(true, COMPUTER_SCIENTIST,compSciMoves);
        Professional president = new Professional(true, PRESIDENT,presiMoves);
        Professional centralBankChair = new Professional(true, CENTRAL_BANK_CHAIR,cbcMoves);
        Professional selfDefenseCoach = new Professional(true, SELFDEFENSE_COACH,coachMoves);
        Professional dictator = new Professional(true,DICTATOR,dictMoves);
        Professional quarterback = new Professional(true, QUARTERBACK,qbMoves);

        PLAYER_1.setTeam(new Professional[]{compScientist,president,centralBankChair,selfDefenseCoach,dictator,quarterback});

        Professional president2 = new Professional(false, PRESIDENT,presiMoves);
        Professional centralBankChair2 = new Professional(false, CENTRAL_BANK_CHAIR,cbcMoves);
        Professional compScientist2 = new Professional(false, COMPUTER_SCIENTIST,compSciMoves);
        Professional selfDefenseCoach2 = new Professional(false, SELFDEFENSE_COACH,coachMoves);
        Professional dictator2 = new Professional(false,DICTATOR,dictMoves);
        Professional quarterback2 = new Professional(true, QUARTERBACK,qbMoves);
        PLAYER_2.setTeam(new Professional[]{president2,centralBankChair2,compScientist2,dictator2,selfDefenseCoach2,quarterback2});
    }

    //--- BUILD TEAM ---------------------------------------------------------------------------------------------------

    @Test
    public void testGetTeam(){
        assertEquals(COMPUTER_SCIENTIST.getName(), PLAYER_1.getTeam()[0].getName());
        assertEquals(SELFDEFENSE_COACH.getName(), PLAYER_1.getTeam()[3].getName());
    }

    //--- CRITICAL POINTS ----------------------------------------------------------------------------------------------

    @Test
    public void testSetAndGetCritCounters(){
        PLAYER_1.setCritCounter(4);
        PLAYER_2.setCritCounter(8);
        assertEquals(8,PLAYER_2.getCritCounter());
        assertEquals(4,PLAYER_1.getCritCounter());

    }

    @Test
    public void testCanUseCritical(){
        PLAYER_1.setCritCounter(4);
        PLAYER_2.setCritCounter(8);
        assertFalse(PLAYER_1.canUseCritical());
        assertTrue(PLAYER_2.canUseCritical());
    }

    @Test
    public void useCritical(){
        PLAYER_1.setCritCounter(8);
        PLAYER_1.useCritical();

        assertTrue(PLAYER_1.getSelectedProfessional().getVolatileStatus().contains(CRITIC));
    }

    @Test
    public void useSetAndGetName(){
        assertEquals("Paul",PLAYER_1.getName());

        PLAYER_1.setName("Ringo");
        assertEquals("Ringo",PLAYER_1.getName());
    }
}
