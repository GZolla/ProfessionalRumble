package model;

import model.data.Volatile;
import model.effects.*;
import model.moves.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static model.data.Branch.*;
import static model.data.NonVolatile.*;
import static model.data.ProfessionalBase.*;
import static model.data.Stat.*;
import static model.data.Volatile.*;
import static model.moves.DamagingMove.*;
import static model.moves.StatusMove.*;
import static org.junit.jupiter.api.Assertions.*;
import static ui.Main.PLAYER_1;
import static ui.Main.PLAYER_2;

public class ProfessionalTest {

    @BeforeEach
    //Refresh teams to default
    public void runBefore(){
        PLAYER_1.setTeam(new Player("Paul",true).getTeam());
        PLAYER_2.setTeam(new Player("Paul",false).getTeam());
    }


    //--- MOVES --------------------------------------------------------------------------------------------------------

    @Test
    public void testUseDamagingMoveAndGetLastMoveDamage(){
        PLAYER_1.setSelectedProfessional(0);
        Professional user =  PLAYER_1.getSelectedProfessional();
        Professional foe = PLAYER_2.getSelectedProfessional();
        user.useMove(2);

        int moveDamage = user.getLastMoveDamage(foe);
        assertEquals(KEYBOARD_SLAM.getDamage(user, foe),moveDamage);
        assertEquals(foe.getBase().getLife() - moveDamage,foe.getLife());
    }
    @Test
    public void testUseDamagingMoveCritical(){
        PLAYER_2.setSelectedProfessional(3);
        PLAYER_1.setSelectedProfessional(0);
        Professional user = PLAYER_2.getSelectedProfessional();
        Professional foe = PLAYER_1.getSelectedProfessional();

        user.addVolatileStatus(CRITIC);
        assertEquals(COMPUTER_SCIENTIST.getLife(), foe.getLife());
        user.useMove(2);
        //selfDefenseCoach2 now has not got CRITICAL volatile status

        double moveDamage = Math.round(COMPUTER_SCIENTIST.getLife() - foe.getLife());
        assertEquals(Math.round(DOMINATE.getDamage(user, foe) * 1.5), moveDamage);
    }

    @Test
    public void testSetAndGetMoves(){
        Professional president = PLAYER_2.getTeam()[1];
        Move[] presiMoves = president.getMoves();

        assertEquals(PRESI_PUNCH,presiMoves[0]);
        assertEquals(ICBM_STRIKE,presiMoves[1]);
        assertEquals(SURVEILL,presiMoves[2]);
        assertEquals(OPTIMIZATION,presiMoves[3]);

        Move[] dictMoves = new Move[]{ICBM_STRIKE,HYPEREXAMINE,LONG_AWAITED_NOVEL,CORPORAL_PUNISHMENT};

        president.setMove(0,ICBM_STRIKE);
        president.setMove(3,CORPORAL_PUNISHMENT);
        assertEquals(ICBM_STRIKE,president.getMoves()[0]);
        assertEquals(CORPORAL_PUNISHMENT,president.getMoves()[3]);
    }

    //--- STATS and STAGES ---------------------------------------------------------------------------------------------

    @Test
    public void testSetStage() {
        Professional quarterback = PLAYER_1.getTeam()[5];
        quarterback.setStage(STR,1);
        quarterback.setStage(RES,6);
        assertEquals(1,quarterback.getStrengthStage());
        assertEquals(6,quarterback.getResistanceStage());

        Professional dictator = PLAYER_2.getTeam()[4];
        dictator.setStage(SPR,3);
        dictator.setStage(SPS,-2);
        dictator.setStage(SPE,-5);
        assertEquals(3,dictator.getSpecialResistanceStage());
        assertEquals(-2,dictator.getSpecialStrengthStage());
        assertEquals(-5,dictator.getSpeedStage());
    }

    @Test
    public void testGetStage() {
        Professional centralBankChair = PLAYER_1.getTeam()[4];
        centralBankChair.setStage(STR,2);
        centralBankChair.setStage(RES,4);
        assertEquals(2,centralBankChair.getStage(STR,false));
        assertEquals(4,centralBankChair.getStage(RES,false));

        Professional president2 = PLAYER_2.getTeam()[4];
        president2.setStage(SPR,-6);
        president2.setStage(SPS,-1);
        assertEquals(-6,president2.getStage(SPR,false));
        assertEquals(-1,president2.getStage(SPS,false));
        assertEquals(0,president2.getStage(SPE,false));
    }

    @Test
    public void testGetStageUserUsedCritical(){
        Professional centralBankChair = PLAYER_1.getTeam()[4];
        centralBankChair.setStage(SPS,2);
        centralBankChair.setStage(STR,4);
        centralBankChair.addVolatileStatus(CRITIC);
        assertEquals(2,centralBankChair.getStage(SPS,false));
        assertEquals(4,centralBankChair.getStage(STR,false));

        Professional president = PLAYER_2.getTeam()[4];
        president.setStage(SPS,-6);
        president.setStage(STR,-1);
        president.addVolatileStatus(CRITIC);
        assertEquals(0,president.getStage(SPS,false));
        assertEquals(0,president.getStage(STR,false));
    }


    @Test
    public void testGetStageFoeUsedCritical(){
        Professional centralBankChair = PLAYER_1.getTeam()[4];
        centralBankChair.setStage(SPR,2);
        centralBankChair.setStage(RES,4);
        assertEquals(0,centralBankChair.getStage(SPR,true));
        assertEquals(0,centralBankChair.getStage(RES,true));

        Professional president = PLAYER_2.getTeam()[4];
        president.setStage(SPR,-6);
        president.setStage(RES,-1);
        assertEquals(-6,president.getStage(SPR,true));
        assertEquals(-1,president.getStage(RES,true));
    }

    @Test
    public void testGetRealStatStage() {
        Professional selfDefenseCoach = PLAYER_2.getTeam()[3];
        selfDefenseCoach.setStage(RES,2);
        assertEquals(2*SELFDEFENSE_COACH.getResistance(),selfDefenseCoach.getRealStat(RES,false));

        Professional compScientist = PLAYER_1.getTeam()[0];
        compScientist.setStage(STR,-1);
        assertEquals(Math.round(2.0 * COMPUTER_SCIENTIST.getStrength() / 3),Math.round(compScientist.getRealStat(STR,false)));

    }

    @Test
    public void testGetRealStatNVS() {
        Professional president = PLAYER_1.getTeam()[1];
        president.setNonVolatileStatus(INJUR);
        assertEquals(PRESIDENT.getStrength()/2,president.getRealStat(STR,false));

        Professional centralBankChair = PLAYER_2.getTeam()[2];
        centralBankChair.setNonVolatileStatus(DEPRE);
        assertEquals(CENTRAL_BANK_CHAIR.getSpecialStrengh()/2,centralBankChair.getRealStat(SPS,false));

        Professional selfDefenseCoach = PLAYER_2.getTeam()[3];
        selfDefenseCoach.setNonVolatileStatus(DEMOR);
        assertEquals(SELFDEFENSE_COACH.getSpeed()/2,selfDefenseCoach.getRealStat(SPE,false));
    }

    //--- STATUS -------------------------------------------------------------------------------------------------------



    @Test
    public void testGetAddRemoveVS(){
        Professional president = PLAYER_1.getTeam()[4];
        assertEquals(new ArrayList<Volatile>(),president.getVolatileStatus());
        president.addVolatileStatus(NAUSEA);
        president.addVolatileStatus(CHARGE);
        president.addVolatileStatus(DRAIND);
        president.addVolatileStatus(NAUSEA);

        assertEquals(3,president.getVolatileStatus().size());//NAUSEA added once only
        assertTrue(president.getVolatileStatus().contains(NAUSEA));
        assertTrue(president.getVolatileStatus().contains(CHARGE));
        assertTrue(president.getVolatileStatus().contains(DRAIND));

        president.removeVolatileStatus(CHARGE);
        assertFalse(president.getVolatileStatus().contains(CHARGE));
    }

    @Test
    public void testCheckNonVolatileStatusSick(){
        Professional compScientist = PLAYER_1.getTeam()[0];
        compScientist.setNonVolatileStatus(SICK);
        int baseLife =COMPUTER_SCIENTIST.getLife();

        compScientist.checkNonVolatileStatus(); //0 dmg
        compScientist.checkNonVolatileStatus(); // 1/16 dmg

        assertEquals( baseLife - Math.round(baseLife/16),compScientist.getLife());
        compScientist.checkNonVolatileStatus();// 2/16 dmg
        assertEquals( baseLife - Math.round(baseLife * 3/16),compScientist.getLife());
    }

    @Test
    public void testCheckNonVolatileStatusInjur(){
        Professional president = PLAYER_1.getTeam()[1];
        president.setNonVolatileStatus(INJUR);
        int baseLife =PRESIDENT.getLife();

        president.checkNonVolatileStatus();
        assertEquals( baseLife - Math.floor(baseLife/16),president.getLife());
        president.checkNonVolatileStatus();
        assertEquals( baseLife - Math.floor(baseLife * 2/16),president.getLife());
    }

    @Test
    public void testCheckNonVolatileStatusDepre(){
        Professional dictator = PLAYER_1.getTeam()[4];
        dictator.setNonVolatileStatus(DEPRE);
        int baseLife =DICTATOR.getLife();

        dictator.checkNonVolatileStatus();
        assertEquals( baseLife - Math.round(baseLife/16),dictator.getLife());
        dictator.checkNonVolatileStatus();
        assertEquals( baseLife - Math.round(baseLife * 2/16),dictator.getLife());
    }

    @Test
    public void testUpdateNonVolatileStatusUnlucky() {
        PLAYER_1.setSelectedProfessional(3); //self defense coach
        Professional selfDefenseCoach = PLAYER_1.getTeam()[3];

        selfDefenseCoach.setNonVolatileStatus(UNEMP);
        assertEquals(UNEMP,selfDefenseCoach.getNonVolatileStatus());
        selfDefenseCoach.checkNonVolatileStatus();
        selfDefenseCoach.checkNonVolatileStatus();
        assertEquals(2,selfDefenseCoach.getNonVolatileTurns());
        selfDefenseCoach.checkNonVolatileStatus();
        assertNull(selfDefenseCoach.getNonVolatileStatus());
    }

    //--- COUNTERS -----------------------------------------------------------------------------------------------------

    @Test
    public void testUpdateCounters(){

        Professional compScientist = PLAYER_2.getTeam()[0];
        compScientist.setMoveCounter(0,0);
        compScientist.setMoveCounter(1,2);
        compScientist.setMoveCounter(2,4);
        compScientist.setMoveCounter(3,5);
        compScientist.updateCounters();


        assertEquals(-1,compScientist.getMoveCounters()[0]);
        assertEquals(2,compScientist.getMoveCounters()[1]);
        assertEquals(4,compScientist.getMoveCounters()[2]);
        assertEquals(5,compScientist.getMoveCounters()[3]);

    }

    //--- OTHER --------------------------------------------------------------------------------------------------------

    @Test
    public void testTakeDamage() {
        Professional president = PLAYER_1.getTeam()[1];
        president.takeDamage(50,1);
        assertEquals(PRESIDENT.getLife()-50,president.getLife());

        Professional compScientist = PLAYER_2.getTeam()[0];
        compScientist.takeDamage(60,1);
        assertEquals(COMPUTER_SCIENTIST.getLife()-60,compScientist.getLife());
    }
    @Test
    public void testTakeDamageFaints() {
        Professional president = PLAYER_1.getTeam()[1];
        president.takeDamage(PRESIDENT.getLife(),1);
        assertEquals(0,president.getLife());
        assertEquals(FAINT,president.getNonVolatileStatus());

        Professional compScientist = PLAYER_2.getTeam()[0];
        compScientist.takeDamage(COMPUTER_SCIENTIST.getLife() * 2,1);
        assertEquals(0,compScientist.getLife());
        assertEquals(FAINT,compScientist.getNonVolatileStatus());
    }

    @Test
    public void testIsBranch() {
        Professional compScientist = PLAYER_2.getTeam()[0];
        assertTrue(compScientist.isBranch(TECHNO));
        assertFalse(compScientist.isBranch(GOVERN));
    }

    @Test
    public void testIsBranchTwoBranches() {
        Professional centralBankChair = PLAYER_1.getTeam()[2];
        assertTrue(centralBankChair.isBranch(GOVERN));
        assertTrue(centralBankChair.isBranch(NUMBER));
        assertFalse(centralBankChair.isBranch(SCIENCE));
    }

    @Test
    public void testCheckEffectiveness() {
        Professional compScientist = PLAYER_2.getTeam()[0];
        Professional president = PLAYER_1.getTeam()[1];
        assertEquals(0.5,compScientist.checkEffectiveness(SPORT));
        assertEquals(2, compScientist.checkEffectiveness(MANUAL));
        assertEquals(1, president.checkEffectiveness(SCIENCE));
    }

    @Test
    public void testCheckEffectivenessTwoBranches() {
        Professional selfDefenseCoach = PLAYER_1.getTeam()[3];
        Professional centralBankChair = PLAYER_1.getTeam()[2];
        assertEquals(2,centralBankChair.checkEffectiveness(SPORT));
        assertEquals(0.25, centralBankChair.checkEffectiveness(ENTERT));
        assertEquals(4, selfDefenseCoach.checkEffectiveness(SECURI));
        assertEquals(1, centralBankChair.checkEffectiveness(EDUCAT));
    }
    @Test
    public void tesCheckEffectivenessFutile(){
        Professional selfDefenseCoach = PLAYER_1.getTeam()[3];
        assertEquals(0, selfDefenseCoach.checkEffectiveness(GOVERN));
        assertEquals(0, selfDefenseCoach.checkEffectiveness(NUMBER));
    }

    //--- ACCESSORS/MUTATORS NOT TESTED ABOVE --------------------------------------------------------------------------
    @Test
    public void testIsLedByPlayer1() {
        Professional dictator2 = PLAYER_2.getTeam()[4];
        Professional dictator = PLAYER_1.getTeam()[4];
        assertFalse(dictator2.isLedByPlayer1());
        assertTrue(dictator.isLedByPlayer1());
    }

    @Test
    public void testGetName() {

        Professional dictator2 = PLAYER_2.getTeam()[4];
        assertEquals(DICTATOR.getName(),dictator2.getName());
    }

}
