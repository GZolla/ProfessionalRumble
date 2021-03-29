import model.*;
import model.data.ProfessionalBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Main;

import static model.data.NonVolatile.DEPRE;
import static model.data.NonVolatile.UNEMP;
import static model.data.ProfessionalBase.*;
import static model.data.Stat.*;
import static model.data.Volatile.*;
import static model.effects.CriticalModifier.MAXCRITS;
import static model.moves.Damaging.*;
import static model.moves.Damaging.UNDERGROUND_UPPERCUT;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveTest {

    private Player player1;
    private Player player2;
    private Battle battle;


    @BeforeEach
    public void runBefore() {
        player1 = new Player(-1,"Test1");
        player2 = new Player(-2,"Test2");
        player1.setBattleProperties(new Team(player1,"Team1"));
        player2.setBattleProperties(new Team(player2,"Team2"));

        battle = null;
    }

    @Test
    //EFFECTS: Checks that damage-only moves works
    public void testUseDamageOnlyMove() {
        setBattle(0,3);
        Round round = new Round(battle,new int[]{0,2},new int[]{0,2});
        round.handleAll();

        Professional user = player1.getSelectedProfessional();
        Professional target = player2.getSelectedProfessional();
        assertEquals(KEYBOARD_SLAM.getDamage(user,target),target.getBase().getLife() - target.getLife());
        assertEquals(DOMINATE.getDamage(target,user),user.getBase().getLife() - user.getLife());
        assertEquals(4,player1.getCritCounter());
        assertEquals(4,player2.getCritCounter());

    }

    @Test
    //EFFECTS: Checks that criticalModifier works
    public void testUseCriticalModifier() {
        setBattle(5,4);
        runRound(2,1);

        Professional user = player1.getSelectedProfessional();
        Professional target = player2.getSelectedProfessional();
        assertEquals(FRIENDLY_MATCH.getDamage(user,target),target.getBase().getLife() - target.getLife());

        assertEquals(28,player1.getCritCounter());
        assertEquals(MAXCRITS,player2.getCritCounter());
    }

    @Test
    //EFFECTS: Checks that DefeatCondition causes UNEMP/receives recoil if and only if opponent is not defeated
    public void testDefeatCondition() {
        setBattle(3,5);
        runRound(1,8);

        //TEST p1's coach fainted and computer scientist tapped in
        assertEquals(player1.getSelectedProfessional().getBase(), ProfessionalBase.COMPUTER_SCIENTIST);
        //Test no UNEMP
        Professional p2Quarterback = player2.getSelectedProfessional();
        assertNotEquals(UNEMP,p2Quarterback.getNonVolatileStatus());

        runRound(0,0);
        assertEquals(player1.getSelectedProfessional().getBase(), ProfessionalBase.COMPUTER_SCIENTIST);//p1CompSci did not faint
        //Test UNEMP is applied
        assertEquals(UNEMP,p2Quarterback.getNonVolatileStatus());

        p2Quarterback.setNonVolatileStatus(null);
        runRound(1,19);
        assertEquals(ENVIRONMENTALIST , player1.getSelectedProfessional().getBase());
        //No recoil applied
        assertEquals(QUARTERBACK.getLife(), p2Quarterback.getLife());

        Round round = runRound(2,3);
        assertEquals(player1.getSelectedProfessional().getBase(), ProfessionalBase.ENVIRONMENTALIST);//Did not change
        //Recoil applied
        assertEquals((2 * round.getUsedMoveDamage(true)) / 3,QUARTERBACK.getLife() - p2Quarterback.getLife());
    }


    @Test
    //EFFECTS: Checks failCondition moves with priority 0, need 1 turn charge before being used
    //         Also check that if identifier is false, user becomes DUGIN and cannot be harmed
    public void testFailConditionPriority0() {
        setBattle(0,2);
        runRound(0,1);

        Professional compSci1 = player1.getSelectedProfessional();
        Professional environmentalist2 = player2.getSelectedProfessional();
        //no professional was damaged in the making of this round
        assertEquals(COMPUTER_SCIENTIST.getLife(), compSci1.getLife());
        assertEquals(ENVIRONMENTALIST.getLife(), environmentalist2.getLife());
        //compsci is charged and environmentalist is dug in
        assertTrue(compSci1.hasVolatileStatus(CHARGE));
        assertTrue(environmentalist2.hasVolatileStatus(DUGIN));


        environmentalist2.setStage(SPE,-6);
        runRound(0,1);
        assertEquals(ENVIRONMENTALIST.getLife(), environmentalist2.getLife());

        int dmg = UNDERGROUND_UPPERCUT.getDamage(environmentalist2,compSci1);
        assertEquals(COMPUTER_SCIENTIST.getLife() - dmg, compSci1.getLife());


    }

    @Test
    //EFFECTS: Checks failCondition moves with and without priority 0
    //         fail if foe does not select a damaging move, or if priority is 1 and user moves second
    public void testFailConditionOther() {
        setBattle(4,5);

        runRound(2,-3);
        Professional environmentalist = player2.getSelectedProfessional();
        assertEquals(ENVIRONMENTALIST,environmentalist.getBase());
        assertEquals(ENVIRONMENTALIST.getLife(),environmentalist.getLife());//Switched making landmine fail
        environmentalist.setStage(SPR,-1);

        runRound(0,2);
        Professional dictator = player1.getSelectedProfessional();
        assertEquals(DICTATOR.getLife(),dictator.getLife());

        runRound(1,2);
        int hhDmg = HARVEST_HAVOC.getDamage(environmentalist,dictator);
        assertEquals(DICTATOR.getLife() - hhDmg,dictator.getLife());

        runRound(14,2);
        assertEquals(DICTATOR.getLife() - 2 * hhDmg,dictator.getLife());
        assertEquals(PRESIDENT,player2.getSelectedProfessional().getBase());

        runRound( 1,1);
        assertEquals(DICTATOR.getLife() - 2 * hhDmg,dictator.getLife());

        runRound( 0,1);
        assertEquals(DICTATOR.getLife() - 2 * hhDmg,dictator.getLife());
        runRound( 2,1);
        int itDmg = INSIDER_TRADING.getDamage(player2.getSelectedProfessional(),dictator);
        assertEquals(DICTATOR.getLife() - 2 * hhDmg - itDmg,dictator.getLife());
    }

    @Test
    //EFFECTS: Checks StatModifying moves modify all stats, can target both user and foe and can reduce and increase
    public void testStatModifier() {
        setBattle(1,2);
        runRound(0,3);
        Professional president = player1.getSelectedProfessional();
        assertEquals(-1,president.getSpecialResistanceStage());
        assertEquals(-1,president.getResistanceStage());
        assertEquals(2, player2.getSelectedProfessional().getSpecialStrengthStage());

        runRound(3,-6);
        Professional quarterback = player2.getSelectedProfessional();
        assertEquals(0,quarterback.getStrengthStage());//No added effect

        runRound(3,1);
        assertEquals(-1,quarterback.getStrengthStage());//Now there is an effect
        assertEquals(1,quarterback.getSpeedStage());//Accelerate applied stat change
    }

    @Test
    //EFFECTS: Test determine order takes into account priority
    //         test that priority fail under their conditions and apply their effects(described in Priority class)
    public void testPriority() {
        setBattle(4,3);

        player2.setCritCounter(10);
        Round round = runRound(3,0);
        Professional dictator = player1.getSelectedProfessional();
        Professional coach = player2.getSelectedProfessional();
        assertTrue(round.wentFirst(player1));//priority 6 > priority 1
        assertEquals(10,player2.getCritCounter());//No reduction in crit points + raiseCriticals was not called
        assertEquals(DICTATOR.getLife(),dictator.getLife());//Dictator was protected

        runRound(3,0);
        int dmg = LIGHTSPE_KICK.getDamage(coach,dictator);
        assertEquals(4,player2.getCritCounter());//Reduction of crit points + raiseCriticals was not called
        assertEquals(dmg,DICTATOR.getLife() - dictator.getLife());

        round = runRound(0,-1);
        assertTrue(round.wentFirst(player2));//Switch still before priority
        Professional compSci = player2.getSelectedProfessional();
        int dmg2 = POP_UP_PUNCH.getDamage(dictator,compSci);
        assertEquals(dmg,DICTATOR.getLife() - dictator.getLife());

        runRound(0,3);
        assertTrue(round.wentFirst(player2));//priority 3> priority 1

        dmg += SLAPSTICK_SMACK.getDamage(compSci,dictator);
        assertEquals(dmg,DICTATOR.getLife() - dictator.getLife()); //SLAPSTICK_SMACK worked
        assertEquals(dmg2,COMPUTER_SCIENTIST.getLife() - compSci.getLife());

        runRound(0,3);
        assertEquals(dmg,DICTATOR.getLife() - dictator.getLife()); //SLAPSTICK_SMACK did not work

        assertEquals(2 * dmg2,COMPUTER_SCIENTIST.getLife() - compSci.getLife());// dictator did not flinch
    }

    @Test
    //EFFECTS: test that status inflict is inflicted after charge turns, test both volatile/nonvolatile are inflicted
    //         test that DRAIND is inflicted on user and not target
    public void testStatusInlfictor() {
        setBattle(3,0);

        runRound(1,-4);
        assertFalse(player2.getSelectedProfessional().hasVolatileStatus(NAUSEA));
        runRound(1,-6);
        assertFalse(player2.getSelectedProfessional().hasVolatileStatus(NAUSEA));
        runRound(1,-5);
        assertTrue(player2.getSelectedProfessional().hasVolatileStatus(NAUSEA));

        runRound(3,-2);
        Professional coach = player1.getSelectedProfessional();
        int dmg = RED_CARD_FOUL.getDamage(coach,player2.getSelectedProfessional());
        assertEquals(dmg,PRESIDENT.getLife() - player2.getSelectedProfessional().getLife());
        assertTrue(coach.hasVolatileStatus(DRAIND));

        runRound(1,2);
        assertEquals(DEPRE,coach.getNonVolatileStatus());
        assertEquals(dmg,PRESIDENT.getLife() - player2.getSelectedProfessional().getLife());//Move did not run
        assertFalse(coach.hasVolatileStatus(DRAIND));
    }

    @Test
    //EFFECTS: Test that if windowTurns pass the move no longer applies it special effect on next use
    public void testWindowTurnsPass() {
        setBattle(3,0);
        runRound(1,-4);
        assertFalse(player2.getSelectedProfessional().hasVolatileStatus(NAUSEA));
        runRound(1,-6);
        assertFalse(player2.getSelectedProfessional().hasVolatileStatus(NAUSEA));
        runRound(2,-1);
        assertFalse(player2.getSelectedProfessional().hasVolatileStatus(NAUSEA));
        runRound(1,-5);
        assertFalse(player2.getSelectedProfessional().hasVolatileStatus(NAUSEA));
    }

    //EFFECTS: sets the selected professional of both to given index and returns a battle between th two players
    public void setBattle(int prof1, int prof2) {
        player1.setSelectedProfessional(prof1);
        player2.setSelectedProfessional(prof2);
        battle = new Battle(player1,player2);
        Main.BATTLEMGR.setBattle(battle);
    }

    //EFFECTS: runs a single round selecting moves of the given indexes
    public Round runRound(int move1, int move2) {
        int ind1 = move1 < 0 ? 1 : 0;
        int ind2 = move2 < 0 ? 1 : 0;
        int[] action1 = new int[]{ind1,Math.abs(move1) - ind1};
        Round round = new Round(battle,action1,new int[]{ind2,Math.abs(move2) - ind2});
        battle.getRounds().add(round);
        round.handleAll();


        return round;
    }
    
}
