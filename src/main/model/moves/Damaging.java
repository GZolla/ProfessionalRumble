package model.moves;

import model.Round;
import model.data.Branch;
import model.Professional;
import model.data.Stat;
import model.effects.*;
import ui.Main;

import static model.data.Branch.*;
import static model.data.NonVolatile.*;
import static model.data.Stat.*;
import static model.data.Volatile.*;

//Moves that cause damage on target, can have added effects, can be physical or special
public enum Damaging implements Move {
//null
    DOMINATE("Dominate",SPORT,false,100,null),
    BATON_SMACK("Baton smack",SECURI,true,100,null),
    KEYBOARD_SLAM("Keyboard slam",TECHNO,true,100,null),
//CriticalModifier

    CONTROVERSIAL_MARKETING("Controversial marketing",DESIGN,false,115,new CriticalModifier(-8)),
    EXPERIMENTAL_THERAPY("Experimental therapy",HEALTH,true,115,new CriticalModifier(-8)),
    STATISTICAL_STRIKE("Statistical strike",NUMBER,false,115,new CriticalModifier(-8)),
    BIASED_REVISIONISM("Biased revisionism",LETTER,false,115,new CriticalModifier(-8)),
    GMO_GUT_PUNCH("GMO gut punch",FOOD,true,115,new CriticalModifier(-8)),
    CORPORAL_PUNISHMENT("Corporal punishment",EDUCAT,true,115,new CriticalModifier(-8)),
    CERTIFICATE_SLASH("Certificate slash",EDUCAT,true,90,new CriticalModifier(8)),
    FRIENDLY_MATCH("Friendly match",SPORT,false,40,new CriticalModifier(24)),
    AWARENESS_CAMPAIGN("Awareness campaign",ENVIRO,false,40,new CriticalModifier(24)),
    TEST_CRASH("Test crash",TRANSP,true,40,new CriticalModifier(24)),


//DefeatCondition

    UNDERCUT_UPPERCUT("Undercut uppercut",CORPOR,true,130,new DefeatCondition(false)),
    MOMENTUM_COLLISION("Momentum collision",SCIENCE,true,130,new DefeatCondition(false)),
    KIDNAP("Kidnap",TRANSP,false,130,new DefeatCondition(false)),
    PREDATORY_PRICING("Predatory pricing",CORPOR,false,140,new DefeatCondition(true)),
    HUMANE_EXECUTION("Humane execution",HEALTH,true,140,new DefeatCondition(true)),
    PUNCHLINE("Punchline",ENTERT,false,140,new DefeatCondition(true)),
    PSYCHOLOGICAL_TORTURE("Psychological torture",SECURI,false,140,new DefeatCondition(true)),
    POISONED_BEVERAGE("Poisoned beverage",FOOD,false,140,new DefeatCondition(true)),
//FailCondition

    BREAKTHROUGH_BLAST("Breakthrough blast",SCIENCE,false,120,new FailCondition(true,0)),
    LONG_AWAITED_NOVEL("Long awaited novel",LETTER,false,120,new FailCondition(true,0)),
    UNDERGROUND_UPPERCUT("Underground uppercut",MANUAL,true,80,new FailCondition(false,0)),
    PRIME_PUNCH("Prime punch",NUMBER,true,120,new FailCondition(true,0)),
    INSIDER_TRADING("Insider trading",CORPOR,false,70,new FailCondition(true,1)),
    LANDMINE("Landmine",SECURI,false,150,new FailCondition(true,-3)),
    HARVEST_HAVOC("Harvest havoc",FOOD,true,150,new FailCondition(false,-3)),
//Priority

    SPONTANEOUS_REACTION("Spontaneous reaction",SCIENCE,false,40,new Priority(1)),
    SURPRISE_EXAM("Surprise exam",EDUCAT,false,40,new Priority(1)),
    AMATEUR_FIRST_AID("Amateur first-aid",HEALTH,true,40,new Priority(1)),
    OFFICE_PRANK("Office prank",CORPOR,true,40,new Priority(1)),
    PROTOTYPE_PUNCH("Prototype punch",DESIGN,true,40,new Priority(1)),
    POP_UP_PUNCH("Pop-up Punch",TECHNO,true,40,new Priority(1)),
    LIGHTSPE_KICK("LightSPE kick",SCIENCE,true,80,new Priority(2)),
    SLAPSTICK_SMACK("Slapstick smack",ENTERT,true,40,new Priority(3)),

//StatModifier

    PRESIDENTIAL_PUNCH("Presidential punch",GOVERN,true,120,new StatModifier(false,new Stat[]{RES,SPR},-1,0)),
    MARBLE_SMASH("Marble smash",DESIGN,true,120,new StatModifier(false,new Stat[]{RES,SPR},-1,0)),
    BRICK_LAUNCH("Brick launch",MANUAL,false,90,new StatModifier(false,RES,-1,2)),
    SPINACH_SMACK("Spinach smack",FOOD,true,40,new StatModifier(false,RES,1,0)),
    DOOR_SLAM("Door slam",TRANSP,true,70,new StatModifier(false,RES,1,1)),
    ICBM_STRIKE("ICBM strike",GOVERN,false,130,new StatModifier(false,SPS,-2,0)),
    CASHFLOW_BURNOUT("Cashflow burnout",CORPOR,false,130,new StatModifier(false,SPS,-2,0)),
    FERTILIZER_FUME("Fertilizer fume",FOOD,false,70,new StatModifier(false,SPS,1,1)),
    TAX_SPIKE("Tax spike",GOVERN,false,90,new StatModifier(false,SPS,1,2)),

    DANCE_KICK("Dance kick",ENTERT,true,55,new StatModifier(false,SPE,1,0)),
    BACKPACK_SMACK("Backpack smack",EDUCAT,true,55,new StatModifier(false,SPE,1,0)),

    COREOGRAPHED_ASSAULT("Coreographed assault",ENTERT,true,120,new StatModifier(false,new Stat[]{STR,RES},-1,0)),
    REITERATIVE_PUNCH("Reiterative punch",NUMBER,true,40,new StatModifier(false,STR,1,0)),

    CUBIST_CRUSH("Cubist crush",DESIGN,true,70,new StatModifier(true,RES,-1,1)),
    CABLE_WHIP("Cable whip",TECHNO,true,70,new StatModifier(true,RES,-1,1)),
    DIVIDING_CHOP("Dividing chop",NUMBER,true,90,new StatModifier(true,RES,-1,2)),
    KNIFE_SLICE("Knife slice",FOOD,true,90,new StatModifier(true,RES,-1,2)),
    HARDCOVER_SLAM("Hardcover slam",LETTER,true,90,new StatModifier(true,RES,-1,2)),
    TOOL_SLAM("Tool slam",MANUAL,true,90,new StatModifier(true,RES,-1,2)),
    COMPROMISE_NETWORK("Compromise network",TECHNO,false,40,new StatModifier(true,SPR,-1,0)),
    LAWSUIT("Lawsuit",CORPOR,false,90,new StatModifier(true,SPR,-1,2)),
    VENGEFUL_SONG("Vengeful song",ENTERT,false,90,new StatModifier(true,SPR,-1,2)),


    DISOBEDIENCE_SLAP("Disobedience slap",EDUCAT,true,75,new StatModifier(true,SPE,-1,1)),
    CEMENT_BLAST("Cement blast",MANUAL,false,75,new StatModifier(true,SPE,-1,1)),
    PRECISE_PIERCE("Precise pierce",DESIGN,true,95,new StatModifier(true,SPE,-1,2)),
    TRAFFIC_JAM("Traffic jam",TRANSP,false,95,new StatModifier(true,SPE,-1,2)),
    PAPER_SLASH("Paper slash",LETTER,true,70,new StatModifier(true,STR,-1,1)),
    TWISTER("Twister",ENVIRO,false,90,new StatModifier(true,STR,-1,2)),
//StatusInflictor


    TRASH_TALK("Trash talk",SPORT,false,80,new StatusInflictor(DEMOR,2)),
    REGULATORY_STRANGLE("Regulatory strangle",GOVERN,true,80,new StatusInflictor(DEMOR,2)),

    COURSE_FLUNK("Course flunk",EDUCAT,false,80,new StatusInflictor(DEPRE,2)),
    BACKSTAB("Backstab",CORPOR,true,80,new StatusInflictor(DEPRE,2)),
    RED_CARD_FOUL("Red-card foul",SPORT,true,150,new StatusInflictor(DRAIND,0)),
    DAM_FLOOD("Dam flood",ENVIRO,false,150,new StatusInflictor(DRAIND,0)),
    STATIC_SLAP("Static slap",SCIENCE,true,50,new StatusInflictor(FLINCH,1)),
    REFLEX_HAMMER_TAP("Reflex hammer tap",HEALTH,true,50,new StatusInflictor(FLINCH,1)),
    BLACKOUT("Blackout",MANUAL,false,50,new StatusInflictor(FLINCH,1)),
    HEADLIGHTS_STUN("Headlights stun",TRANSP,false,50,new StatusInflictor(FLINCH,1)),
    STUNT_DOUBLE_SMACKDOWN("Stunt double smackdown",ENTERT,true,85,new StatusInflictor(FLINCH,2)),
    DISLOCATE("Dislocate",HEALTH,true,45,new StatusInflictor(INJUR,1)),
    TACKLE("Tackle",SPORT,true,80,new StatusInflictor(INJUR,2)),
    RIFLE_SHOT("Rifle shot",SECURI,false,80,new StatusInflictor(INJUR,2)),
    STAMPEDE("Stampede",ENVIRO,true,80,new StatusInflictor(INJUR,2)),
    VEHICLE_SMASH("Vehicle smash",TRANSP,true,80,new StatusInflictor(INJUR,2)),

    GRAVITY_SLAM("Gravity slam",SCIENCE,true,85,new StatusInflictor(NAUSEA,2)),
    SINUSOIDAL_BEAM("Sinusoidal beam",NUMBER,false,85,new StatusInflictor(NAUSEA,2)),
    COLOR_OVERLOAD("Color overload",DESIGN,false,85,new StatusInflictor(NAUSEA,2)),
    WRONG_MEDICINE("Wrong medicine",HEALTH,false,80,new StatusInflictor(SICK,2)),
    ACID_SPLASH("Acid splash",SCIENCE,false,80,new StatusInflictor(SICK,2)),
    MISGUIDED_NUTRITION("Misguided nutrition",FOOD,false,80,new StatusInflictor(SICK,2)),
    ARREST_RAID("Arrest raid",SECURI,true,45,new StatusInflictor(UNEMP,1)),
    RECKLESS_DRIVING("Reckless driving",TRANSP,true,45,new StatusInflictor(UNEMP,1)),
    SLANDER_PIECE("Slander piece",LETTER,false,80,new StatusInflictor(UNEMP,2)),
    MALWARE("Malware",TECHNO,false,80,new StatusInflictor(UNEMP,2));
    
    
    private final String name;
    private final Branch branch;
    private final boolean physical;
    private final Effect effect;
    private final int power;


    Damaging(String name, Branch branch, boolean physical, int power, Effect effect) {
        this.name = name;
        this.branch = branch;
        this.physical = physical;
        this.power = power;
        this.effect = effect;
    }


    //MODIFIES: user, foe
    //EFFECT: If move needs charging and user is not charged
    //            charge user
    //        Otherwise
    //            Calculate and apply damage to foe
    //            If move has an extra effect and move caused damage apply the effect
    @Override
    public void use(Round round, boolean movedFirst) {
        Professional user = round.getUser(movedFirst);
        Professional foe = round.getUser(!movedFirst);

        if ((effect == null || !effect.fails(round,movedFirst)) && !foeIsProtected(foe)) {
            int dmg = getDamage(user, foe);
            if (dmg > 0) {
                foe.takeDamage(dmg, foe.checkEffectiveness(branch));
                if (user.hasVolatileStatus(CRITIC)) {
                    Main.BATTLEMGR.log("It was a critical hit!");
                }

                if (effect != null && foe.getNonVolatileStatus() != FAINT) {
                    effect.apply(round, movedFirst);
                }
                round.getPlayer(movedFirst).raiseCriticals();
            }


        }
    }

    //EFFECT: return the damage of a move depending on user and foe stats, effectiveness and the same branch multiplier
    //        if the move is physical use user"s strength and foe"s resistance, otherwise use special stats
    //        multiply by branch efficiency
    //        if player has volatile status critical
    //              ignore negative stage changes(lower user attack/raise foe defense) and multiply by 1.5
    //        round down result to nearest integer
    public int getDamage(Professional user, Professional foe) {

        boolean critical = user.hasVolatileStatus(CRITIC);
        double userStrength = user.getRealStat(physical ? STR : SPS, false);
        double foeResistance = foe.getRealStat(physical ? RES : SPR, critical);
        double baseMultiplier = (critical ? 1.5 : 1) * power * userStrength / (4 * foeResistance);

        double sameBranchMultiplier = user.isBranch(branch) ? 1.5 : 1;

        double effectiveness = foe.checkEffectiveness(branch);

        return (int) Math.floor(baseMultiplier * sameBranchMultiplier * effectiveness);


    }

    //EFFECTS: checks if foe has volatile status DUGIN, it announces it and returns true, otherwise returns false
    public boolean foeIsProtected(Professional foe) {
        if (foe.hasVolatileStatus(DUGIN)) {
            Main.BATTLEMGR.log(foe.getFullName() + " dug in and avoided the move.");
        } else if (foe.hasVolatileStatus(PROTECTED)) {
            Main.BATTLEMGR.log(foe.getFullName() + " protected itself.");
        } else {
            return false;
        }

        return true;
    }

    //EFFECT: Return "Physical" if physical, else "Special"
    public String getType() {
        return physical ? "Physical" : "Special";
    }

    @Override
    public Effect getEffect() {
        return effect;
    }

    @Override
    public String toString() {
        return name;
    }

//--- TABLE ABLE METHODS -----------------------------------------------------------------------------------------------
    @Override
    public int getIndex() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    //EFFECT:Return name, branch, power, type, and effect description in a String array
    public String[] toRow() {
        return new String[]{
                name,
                branch.getName(),
                power + "",
                getType(),
                effect == null ? "-" : effect.getDescription()
        };
    }
}
