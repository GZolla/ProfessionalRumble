package model;

import model.data.Stat;
import model.data.Volatile;
import model.moves.Damaging;
import model.moves.Move;
import model.moves.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.data.NonVolatile.*;
import static model.data.ProfessionalBase.*;
import static model.data.ProfessionalBase.QUARTERBACK;
import static model.moves.Damaging.*;
import static model.moves.Status.*;
import static model.moves.Status.ACCELERATE;
import static org.junit.jupiter.api.Assertions.*;
import static ui.Main.PLAYER_1;
import static ui.Main.PLAYER_2;


public class MovesTest {

    @BeforeEach
    public void runBefore(){
        Move[] compSciMoves = new Move[]{CABLE_WHIP,COMPROMISE_NETWORK,KEYBOARD_SLAM,WORKOUT};
        Move[] envMoves = new Move[]{PUNCHLINE,STARVE,SHAME,STAMPEDE};
        Move[] cbcMoves = new Move[]{PRESI_PUNCH,STATIC_SLAP,CONFUSING_PROBLEM,BATON_SMACK};
        Move[] coachMoves = new Move[]{BACKPACK_SMACK,SHAME, DOOR_SLAM,REDCARD_FOUL};
        Move[] dictMoves = new Move[]{ICBM_STRIKE,HYPEREXAMINE,LONG_AWAITED_NOVEL,CORPORAL_PUNISHMENT};
        Move[] qbMoves = new Move[]{WORKOUT,ACCELERATE,FRIENDLY_MATCH,MOMENTUM_COLLISION};

        Professional compScientist = new Professional(true, COMPUTER_SCIENTIST,compSciMoves);
        Professional environmentalist = new Professional(true, ENVIRONMENTALIST,envMoves);
        Professional centralBankChair = new Professional(true, CENTRAL_BANK_CHAIR,cbcMoves);
        Professional selfDefenseCoach = new Professional(true, SELFDEFENSE_COACH,coachMoves);
        Professional dictator = new Professional(true,DICTATOR,dictMoves);
        Professional quarterback = new Professional(true, QUARTERBACK,qbMoves);

        PLAYER_1.setTeam(new Professional[]{compScientist,environmentalist,centralBankChair,selfDefenseCoach,dictator,quarterback});

        Professional environmentalist2 = new Professional(false, ENVIRONMENTALIST,envMoves);
        Professional centralBankChair2 = new Professional(false, CENTRAL_BANK_CHAIR,cbcMoves);
        Professional compScientist2 = new Professional(false, COMPUTER_SCIENTIST,compSciMoves);
        Professional selfDefenseCoach2 = new Professional(false, SELFDEFENSE_COACH,coachMoves);
        Professional dictator2 = new Professional(false,DICTATOR,dictMoves);
        Professional quarterback2 = new Professional(false, QUARTERBACK,qbMoves);
        PLAYER_2.setTeam(new Professional[]{environmentalist2,centralBankChair2,compScientist2,dictator2,selfDefenseCoach2,quarterback2});
    }

    @Test
    public void testDamagingMoveNull(){
        PLAYER_1.setSelectedProfessional(2);//CENTRAL BANK CHAIR

        Professional centralBankChair = PLAYER_1.getSelectedProfessional();
        centralBankChair.useMove(3); //BATON_SMACK

        Professional foe = PLAYER_2.getSelectedProfessional();
        assertEquals(foe.getBase().getLife() - centralBankChair.getLastMoveDamage(foe), foe.getLife());
    }

    @Test
    public void testCritModifierSelfIncreases(){
        //Test that moves that use CritModifier Effect add critPoints of its own leader


        //Damaging
        PLAYER_2.setSelectedProfessional(5);//QUARTERBACK
        PLAYER_2.setCritCounter(0);

        PLAYER_2.getSelectedProfessional().useMove(2);//FRIENDLY MATCH +6
        assertEquals(7,PLAYER_2.getCritCounter());//6+1 from move

        //StatusMove + limited to 8 points
        PLAYER_1.setSelectedProfessional(4);//Dictator
        PLAYER_1.setCritCounter(4);

        PLAYER_1.getSelectedProfessional().useMove(1); //HYPER EXAMINE +8

        assertEquals(8,PLAYER_1.getCritCounter());
    }

    @Test
    public void testCritModifierSelfLowers(){
        //Test that moves that use CritModifier Effect reduce critPoints of its own leader

        //Damaging + limit to 0
        PLAYER_1.setSelectedProfessional(4);//Dictator
        PLAYER_1.setCritCounter(2);

        PLAYER_1.getSelectedProfessional().useMove(3);//CORPORAL PUNISHMENT -2
        assertEquals(1,PLAYER_1.getCritCounter());//2 - 2 -> 0 + 1 from move

        PLAYER_1.getSelectedProfessional().useMove(3);//CORPORAL PUNISHMENT -2
        assertEquals(0,PLAYER_1.getCritCounter()); // 1 - 2 -> -1(limit) +1 from move

        //No Status move reduces own critPoints

    }

    @Test
    public void testCritModifierFoeLowers(){
        //Test that moves that use CritModifier Effect reduce critPoints of its own leader

        //Status + limit to 0
        PLAYER_1.setSelectedProfessional(3);//Self defense coach
        PLAYER_2.setCritCounter(8);
        PLAYER_1.setCritCounter(3);

        PLAYER_1.getSelectedProfessional().useMove(1);//SHAME -8

        assertEquals(0,PLAYER_2.getCritCounter());

        //No Damaging move reduces foe critPoints

    }

    @Test
    public void testDefeatConditionSucceeds(){
        //Test damaging moves with defeat condition cause no consequence if foe faints

        PLAYER_2.setSelectedProfessional(5);//Quarterback
        PLAYER_1.setSelectedProfessional(0);//CompScientist

        Professional user = PLAYER_2.getSelectedProfessional();
        user.useMove(3); //MOMENTUM COLLISION

        assertEquals(FAINT,PLAYER_1.getSelectedProfessional().getNonVolatileStatus());
        assertEquals(user.getBase().getLife(),user.getLife());
        assertNotEquals(UNEMP,user.getNonVolatileStatus());

    }
    @Test
    public void testDefeatConditionRecoilFails(){
        //Test damaging moves with defeat condition can cause recoil on failure to faint opponent

        PLAYER_2.setSelectedProfessional(5);//Quarterback
        PLAYER_1.setSelectedProfessional(1);//Environmentalist

        Professional user = PLAYER_2.getSelectedProfessional();
        Professional foe = PLAYER_1.getSelectedProfessional();
        user.useMove(3);//MOMENTUM COLLISION

        assertNotEquals(FAINT , foe.getNonVolatileStatus());
        int damage = (int) Math.floor(2.0 * user.getLastMoveDamage(foe) / 3);
        assertEquals(user.getBase().getLife() - damage,user.getLife());

    }

    @Test
    public void testDefeatConditionUMPFails(){
        //Test damaging moves with defeat condition can inflict UNEMP on failure to faint opponent

        PLAYER_1.setSelectedProfessional(1);//Environmentalist
        PLAYER_2.setSelectedProfessional(2);//CentralBankChair


        Professional user = PLAYER_1.getSelectedProfessional();
        Professional foe = PLAYER_2.getSelectedProfessional();
        user.useMove(0);//Punchline

        assertNotEquals(FAINT , foe.getNonVolatileStatus());
        assertEquals(UNEMP,user.getNonVolatileStatus());

    }

    @Test
    public void testDamMoveStatModifierOnUser(){
        //Test stat modified on user when no counter is being set
        // (Only damaging with this effect exist)

        PLAYER_2.setSelectedProfessional(3);//Dictator
        PLAYER_1.setSelectedProfessional(1);//Environmentalist

        Professional user = PLAYER_2.getSelectedProfessional();
        user.useMove(0); //ICBM STRIKE

        assertEquals(-2,user.getSpecialStrengthStage());
        assertEquals(0,user.getStrengthStage());
    }

    @Test
    public void testDamMoveStatModifierOnFoe(){
        //Test stat modified on foe + test setting counter and applying final effect
        // (Status moves set stats directly, damaging set counters)
        PLAYER_2.setSelectedProfessional(2);//Computer Scientist
        PLAYER_1.setSelectedProfessional(1);//Environmentalist

        Professional user = PLAYER_2.getSelectedProfessional();
        Professional foe = PLAYER_1.getSelectedProfessional();
        user.useMove(0); //CABLE WHIP
        assertEquals(0,user.getMoveCounters()[0]);//Already charged
        assertEquals(0,user.getResistanceStage());
        user.useMove(0); // Effect -1 RES

        foe.useMove(1);//STARVE

        assertEquals(-2,user.getSpecialResistanceStage());

        assertEquals(-1,foe.getResistanceStage());
        assertEquals(0,user.getSpeedStage());
    }

    @Test
    public void testDamagingMoveMultipleStatModifiers(){
        PLAYER_1.setSelectedProfessional(2);//Central Bank Chair
        PLAYER_2.setSelectedProfessional(0);//Environmentalist

        Professional user = PLAYER_1.getSelectedProfessional();
        user.useMove(0); //PRESIDENTIAL PUNCH

        assertEquals(0,user.getSpecialStrengthStage());
        assertEquals(0,user.getSpeedStage());
        assertEquals(-1,user.getSpecialResistanceStage());
        assertEquals(-1,user.getResistanceStage());
    }

    @Test
    public void testDamagingMoveStatModifiersSetCounter(){
        PLAYER_1.setSelectedProfessional(3);//Self Defense Coach
        PLAYER_2.setSelectedProfessional(2);//Computer Scientist

        Professional user = PLAYER_1.getSelectedProfessional();
        user.useMove(2); //DOOR SLAM

        assertEquals(0,user.getStrengthStage());
        assertEquals(0,user.getSpecialStrengthStage());
        assertEquals(0,user.getResistanceStage());
        assertEquals(0,user.getSpecialResistanceStage());
        assertEquals(0,user.getSpeedStage());

        assertEquals(0,user.getMoveCounters()[2]);
    }

    @Test
    public void testInflictVolatileStatus(){
        PLAYER_1.setSelectedProfessional(2);//Central Bank Chair
        PLAYER_2.setSelectedProfessional(1);//Central Bank Chair

        Professional p1 = PLAYER_1.getSelectedProfessional();
        Professional p2 = PLAYER_2.getSelectedProfessional();

        p2.useMove(2);//CONFUSING_PROBLEM (status) inflicts NAUSEA
        p1.useMove(1); //STATIC_SLAP (damaging) inflicts FLINCH
        p1.useMove(1); //Charge final
        p1.useMove(1); //Use effect


        assertTrue(p1.getVolatileStatus().contains(Volatile.NAUSEA));
        assertTrue(p2.getVolatileStatus().contains(Volatile.FLINCH));
    }

    @Test
    public void testInflictNonVolatileStatus(){
        PLAYER_1.setSelectedProfessional(1);//ENVIRONMENTALIST
        PLAYER_2.setSelectedProfessional(1);//Central Bank Chair

        Professional p1 = PLAYER_1.getSelectedProfessional();
        Professional p2 = PLAYER_2.getSelectedProfessional();

        p1.useMove(3);//STAMPEDE
        p2.takeDamage(-100,1);//to prevent faint
        p1.useMove(3);//charge final
        p2.takeDamage(-100,1);//to prevent faint
        p1.useMove(3);//use effect

        assertEquals(INJUR,p2.getNonVolatileStatus());

    }

    @Test
    public void testDamagingMoveDrained(){
        PLAYER_2.setSelectedProfessional(4);//Self Defense Coach
        PLAYER_1.setSelectedProfessional(2);//Central bank chair

        Professional user = PLAYER_2.getSelectedProfessional();
        user.useMove(3); //RED CARD FOUL

        assertTrue(user.getVolatileStatus().contains(Volatile.DRAIND));
    }

    @Test
    public void testDamagingMoveCharge(){
        PLAYER_1.setSelectedProfessional(4);//DICTATOR
        PLAYER_2.setSelectedProfessional(2);//Computer Scientist

        Professional user = PLAYER_1.getSelectedProfessional();
        Professional foe = PLAYER_2.getSelectedProfessional();
        user.useMove(2); //Long awaited novel

        int foeBaseLife = foe.getBase().getLife();
        assertEquals(foeBaseLife,foe.getLife());
        assertTrue(user.getVolatileStatus().contains(Volatile.CHARGE));

        user.useMove(2);

        assertEquals(foeBaseLife - LONG_AWAITED_NOVEL.getDamage(user,foe),foe.getLife());
    }

    @Test
    public void testGotNauseated(){
        PLAYER_1.setSelectedProfessional(2);//Central Bank Chair
        PLAYER_2.setSelectedProfessional(1);//Central Bank Chair

        Professional p1 = PLAYER_1.getSelectedProfessional();
        Professional p2 = PLAYER_2.getSelectedProfessional();

        p2.useMove(2);//CONFUSING_PROBLEM (status) inflicts NAUSEA
        p1.updateVolatileStatus(); //This turn would get hit by nausea
        p1.useMove(1); //STATIC_SLAP (damaging) inflicts FLINCH

        assertEquals(-1,p1.getMoveCounters()[1]);//The counter was not set
        int moveDMG =(int) Math.round(10 * p1.getRealStat(Stat.STR,false) / p1.getRealStat(Stat.RES,false));
        assertEquals(CENTRAL_BANK_CHAIR.getLife() - p1.getLife(), moveDMG);
    }

    @Test
    public void testToTableAndGetHeadersLength(){
        String[][] status = Status.toTable();
        String[][] damaging = Damaging.toTable();
        String[] statusH = Status.getHeaders();
        String[] damagingH = Damaging.getHeaders();

        assertEquals(Status.values().length,status.length);
        assertEquals(Damaging.values().length,damaging.length);

        assertEquals(status[0].length,statusH.length);
        assertEquals(damaging[0].length,damagingH.length);

    }

    @Test
    public void testToTableOrder() {
        String[][] status = Status.toTable();
        String[][] damaging = Damaging.toTable();

        assertEquals(5 + "",status[5][0]);
        assertEquals(Status.values()[2].getName(),status[2][1]);
        assertEquals(Status.values()[0].getBranch().name(),status[0][2]);

        assertEquals(15 + "",damaging[15][0]);
        assertEquals(Damaging.values()[30].getName(),damaging[30][1]);
        assertEquals(Damaging.values()[21].getBranch().name(),damaging[21][2]);
        assertEquals(Damaging.values()[27].getPower() + "",damaging[27][3]);
        assertEquals(Damaging.values()[18].getType(),damaging[18][4]);
        assertEquals(Damaging.values()[3].getEffect().getDescription(),damaging[3][5]);
    }

}
