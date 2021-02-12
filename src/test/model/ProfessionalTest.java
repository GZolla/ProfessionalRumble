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

    private Professional compScientist;
    private Professional president;
    private Professional centralBankChair;
    private Professional selfDefenseCoach;
    private Professional dictator;
    private Professional quarterback;

    private Professional compScientist2;
    private Professional president2;
    private Professional centralBankChair2;
    private Professional selfDefenseCoach2;
    private Professional dictator2;
    private Professional quarterback2;

    @BeforeEach
    public void runBefore(){
        Move[] compSciMoves = new Move[]{CABLE_WHIP,COMPROMISE_NETWORK,KEYBOARD_SLAM,WORKOUT};
        Move[] presiMoves = new Move[]{PRESI_PUNCH,ICBM_STRIKE,SURVEILL,OPTIMIZATION};
        Move[] cbcMoves = new Move[]{REITERATIVE_PUNCH,ICBM_STRIKE,SURVEILL,OPTIMIZATION};
        Move[] coachMoves = new Move[]{BACKPACK_SMACK,DISOBEDIENCE_SLAP, DOMINATE,REDCARD_FOUL};
        Move[] dictMoves = new Move[]{ICBM_STRIKE,HYPEREXAMINE,LONG_AWAITED_NOVEL,CORPORAL_PUNISHMENT};
        Move[] qbMoves = new Move[]{WORKOUT,ACCELERATE,FRIENDLY_MATCH,MOMENTUM_COLLISION};

        compScientist = new Professional(true, COMPUTER_SCIENTIST,compSciMoves);
        president = new Professional(true, PRESIDENT,presiMoves);
        centralBankChair = new Professional(true, CENTRAL_BANK_CHAIR,cbcMoves);
        selfDefenseCoach = new Professional(true, SELFDEFENSE_COACH,coachMoves);
        dictator = new Professional(true,DICTATOR,dictMoves);
        quarterback = new Professional(true, QUARTERBACK,qbMoves);

        PLAYER_1.setTeam(new Professional[]{compScientist,president,centralBankChair,selfDefenseCoach,dictator,quarterback2});

        president2 = new Professional(false, PRESIDENT,presiMoves);
        centralBankChair2 = new Professional(false, CENTRAL_BANK_CHAIR,cbcMoves);
        compScientist2 = new Professional(false, COMPUTER_SCIENTIST,compSciMoves);
        selfDefenseCoach2 = new Professional(false, SELFDEFENSE_COACH,coachMoves);
        dictator2 = new Professional(false,DICTATOR,dictMoves);
        quarterback2 = new Professional(false, QUARTERBACK,qbMoves);
        PLAYER_2.setTeam(new Professional[]{compScientist2,president2,centralBankChair2,selfDefenseCoach2,dictator2,quarterback2});
    }

    //--- MOVES --------------------------------------------------------------------------------------------------------

    @Test
    public void testUseDamagingMoveAndGetLastMoveDamage(){
        Professional foe = PLAYER_2.getSelectedProfessional();

        PLAYER_1.setSelectedProfessional(0);
        compScientist.useMove(2);

        int moveDamage = compScientist.getLastMoveDamage(foe);
        assertEquals(KEYBOARD_SLAM.getDamage(compScientist, foe),moveDamage);
        assertEquals(foe.getBase().getLife() - moveDamage,foe.getLife());
    }
    @Test
    public void testUseDamagingMoveCritical(){
        PLAYER_2.setSelectedProfessional(3);
        Professional user = PLAYER_2.getSelectedProfessional();
        Professional foe = PLAYER_1.getSelectedProfessional();

        user.addVolatileStatus(CRITIC);
        user.useMove(2);
        //selfDefenseCoach2 now hasUsedCritical == false
        double moveDamage = Math.round(COMPUTER_SCIENTIST.getLife() - foe.getLife());
        assertEquals(Math.round(DOMINATE.getDamage(user, foe) * 1.5), moveDamage);
    }

    @Test
    public void testSetAndGetMoves(){

        Move[] presiMoves = president.getMoves();

        assertEquals(PRESI_PUNCH,presiMoves[0]);
        assertEquals(ICBM_STRIKE,presiMoves[1]);
        assertEquals(SURVEILL,presiMoves[2]);
        assertEquals(OPTIMIZATION,presiMoves[3]);

        Move[] dictMoves = new Move[]{ICBM_STRIKE,HYPEREXAMINE,LONG_AWAITED_NOVEL,CORPORAL_PUNISHMENT};

        president.setMoves(dictMoves);
        assertEquals(ICBM_STRIKE,president.getMoves()[0]);
        assertEquals(HYPEREXAMINE,president.getMoves()[1]);
        assertEquals(LONG_AWAITED_NOVEL,president.getMoves()[2]);
        assertEquals(CORPORAL_PUNISHMENT,president.getMoves()[3]);
    }

    //--- STATS and STAGES ---------------------------------------------------------------------------------------------

    @Test
    public void testSetStage() {
        quarterback.setStage(STR,1);
        quarterback.setStage(RES,6);
        assertEquals(1,quarterback.getStrengthStage());
        assertEquals(6,quarterback.getResistanceStage());

        dictator2.setStage(SPR,3);
        dictator2.setStage(SPS,-2);
        dictator2.setStage(SPE,-5);
        assertEquals(3,dictator2.getSpecialResistanceStage());
        assertEquals(-2,dictator2.getSpecialStrengthStage());
        assertEquals(-5,dictator2.getSpeedStage());
    }

    @Test
    public void testGetStage() {
        centralBankChair.setStage(STR,2);
        centralBankChair.setStage(RES,4);
        assertEquals(2,centralBankChair.getStage(STR,false));
        assertEquals(4,centralBankChair.getStage(RES,false));

        president2.setStage(SPR,-6);
        president2.setStage(SPS,-1);
        assertEquals(-6,president2.getStage(SPR,false));
        assertEquals(-1,president2.getStage(SPS,false));
        assertEquals(0,president2.getStage(SPE,false));
    }

    @Test
    public void testGetStageUserUsedCritical(){
        centralBankChair.setStage(SPS,2);
        centralBankChair.setStage(STR,4);
        centralBankChair.addVolatileStatus(CRITIC);
        assertEquals(2,centralBankChair.getStage(SPS,false));
        assertEquals(4,centralBankChair.getStage(STR,false));

        president.setStage(SPS,-6);
        president.setStage(STR,-1);
        president.addVolatileStatus(CRITIC);
        assertEquals(0,president.getStage(SPS,false));
        assertEquals(0,president.getStage(STR,false));
    }


    @Test
    public void testGetStageFoeUsedCritical(){
        centralBankChair.setStage(SPR,2);
        centralBankChair.setStage(RES,4);
        assertEquals(0,centralBankChair.getStage(SPR,true));
        assertEquals(0,centralBankChair.getStage(RES,true));

        president.setStage(SPR,-6);
        president.setStage(RES,-1);
        assertEquals(-6,president.getStage(SPR,true));
        assertEquals(-1,president.getStage(RES,true));
    }

    @Test
    public void testGetRealStatStage() {
        selfDefenseCoach.setStage(RES,2);
        assertEquals(2*SELFDEFENSE_COACH.getResistance(),selfDefenseCoach.getRealStat(RES,false));

        compScientist.setStage(STR,-1);
        assertEquals(Math.round(2.0 * COMPUTER_SCIENTIST.getStrength() / 3),Math.round(compScientist.getRealStat(STR,false)));

    }

    @Test
    public void testGetRealStatNVS() {
        president.setNonVolatileStatus(INJUR);
        assertEquals(PRESIDENT.getStrength()/2,president.getRealStat(STR,false));

        centralBankChair.setNonVolatileStatus(DEPRE);
        assertEquals(CENTRAL_BANK_CHAIR.getSpecialStrengh()/2,centralBankChair.getRealStat(SPS,false));

        selfDefenseCoach.setNonVolatileStatus(DEMOR);
        assertEquals(SELFDEFENSE_COACH.getSpeed()/2,selfDefenseCoach.getRealStat(SPE,false));
    }

    //--- STATUS -------------------------------------------------------------------------------------------------------



    @Test
    public void testGetAddRemoveVS(){
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
        president.setNonVolatileStatus(INJUR);
        int baseLife =PRESIDENT.getLife();

        president.checkNonVolatileStatus();
        assertEquals( baseLife - Math.round(baseLife/16),president.getLife());
        president.checkNonVolatileStatus();
        assertEquals( baseLife - Math.round(baseLife * 2/16),president.getLife());
    }

    @Test
    public void testCheckNonVolatileStatusDepre(){
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


        compScientist.addCounter(new StatModifier(true,STR,1,1));
        compScientist.addCounter(new StatModifier(true,RES,2,2));
        compScientist.addCounter(new StatusInflictor(INJUR,3));
        compScientist.updateCounters();

        ArrayList<Counter> updateCounters = compScientist.getCounters();
        assertEquals(0,updateCounters.get(0).getWaitTurns());
        assertEquals(1,updateCounters.get(1).getWaitTurns());
        assertEquals(2,updateCounters.get(2).getWaitTurns());
        assertTrue(updateCounters.get(0).isAble());

    }

    @Test
    public void testUpdateCountersRemove(){
        compScientist.addCounter(new StatModifier(true,STR,1,0));
        compScientist.addCounter(new StatModifier(true,STR,1,1));
        compScientist.updateCounters();

        ArrayList<Counter> updateCounters = compScientist.getCounters();
        assertEquals(1,updateCounters.size());
        assertEquals(1,updateCounters.get(0).getWindowTurns());
    }
    @Test
    public void testUseCounter(){
        PLAYER_1.setSelectedProfessional(4);
        dictator.addCounter(new StatusInflictor(DRAIND,0));
        dictator.addCounter(new StatModifier(true,STR,2,0));

        dictator.useCounter(0);
        assertTrue(dictator.getVolatileStatus().contains(DRAIND));
    }

    @Test
    public void testGetAbleCounters(){
        dictator.addCounter(new StatusInflictor(DRAIND,1));
        dictator.addCounter(new StatusInflictor(UNEMP,1));
        dictator.addCounter(new StatusInflictor(UNEMP,3));
        dictator.addCounter(new StatModifier(true,STR,2,2));

        assertEquals(0,dictator.getAbleCounters().size());
        dictator.updateCounters();
        assertEquals(2,dictator.getAbleCounters().size());
    }

    //--- OTHER --------------------------------------------------------------------------------------------------------

    @Test
    public void testTakeDamage() {
        president.takeDamage(50);
        assertEquals(PRESIDENT.getLife()-50,president.getLife());

        compScientist.takeDamage(60);
        assertEquals(COMPUTER_SCIENTIST.getLife()-60,compScientist.getLife());
    }
    @Test
    public void testTakeDamageFaints() {
        president.takeDamage(PRESIDENT.getLife());
        assertEquals(0,president.getLife());
        assertEquals(FAINT,president.getNonVolatileStatus());

        compScientist.takeDamage(COMPUTER_SCIENTIST.getLife() * 2);
        assertEquals(0,compScientist.getLife());
        assertEquals(FAINT,compScientist.getNonVolatileStatus());
    }

    @Test
    public void testIsBranch() {
        assertTrue(compScientist.isBranch(TECHNO));
        assertFalse(compScientist.isBranch(GOVERN));
    }

    @Test
    public void testIsBranchTwoBranches() {
        assertTrue(centralBankChair.isBranch(GOVERN));
        assertTrue(centralBankChair.isBranch(NUMBER));
        assertFalse(centralBankChair.isBranch(SCIENCE));
    }

    @Test
    public void testCheckEffectiveness() {
        assertEquals(0.5,compScientist.checkEffectiveness(SPORT));
        assertEquals(2, compScientist.checkEffectiveness(MANUAL));
        assertEquals(1, president.checkEffectiveness(SCIENCE));
    }

    @Test
    public void testCheckEffectivenessTwoBranches() {
        assertEquals(2,centralBankChair.checkEffectiveness(SPORT));
        assertEquals(0.25, centralBankChair.checkEffectiveness(ENTERT));
        assertEquals(4, selfDefenseCoach.checkEffectiveness(SECURI));
        assertEquals(1, centralBankChair.checkEffectiveness(EDUCAT));
    }
    @Test
    public void tesCheckEffectivenessFutile(){
        assertEquals(0, selfDefenseCoach.checkEffectiveness(GOVERN));
        assertEquals(0, selfDefenseCoach.checkEffectiveness(NUMBER));
    }

    //--- ACCESSORS/MUTATORS NOT TESTED ABOVE --------------------------------------------------------------------------
    @Test
    public void testIsLedByPlayer1() {
        assertFalse(dictator2.isLedByPlayer1());
        assertTrue(dictator.isLedByPlayer1());
    }

    @Test
    public void testGetName() {
        assertEquals(DICTATOR.getName(),dictator2.getName());
    }

}
