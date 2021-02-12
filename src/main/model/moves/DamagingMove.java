package model.moves;

import model.data.Branch;
import model.Professional;
import model.data.Stat;
import model.effects.*;

import static model.data.Branch.*;
import static model.data.NonVolatile.*;
import static model.data.Stat.*;
import static model.data.Volatile.*;
import static ui.Main.*;

//Moves that cause damage on target, can have added effects, can be physical or special
public enum DamagingMove implements Move {
    //DAMAGE ONLY
    DOMINATE("Dominate",SPORT,false,100, null,0,false),
    BATON_SMACK("Baton smack",SECURI,true,100, null,0,false),
    KEYBOARD_SLAM("Keyboard slam",TECHNO,true,100, null,0,false),

    //STAT MODIFIERS (SELF) - Changes the stage of a stat
    PRESI_PUNCH("Presidential punch",GOVERN,true,120, new StatModifier(false,new Stat[]{RES,SPR},-1,-1), 0, false),
    MARBLE_SMASH("Marble smash",DESIGN,true,120, new StatModifier(false,new Stat[]{RES,SPR},-1,-1), 0, false),
    BRICK_LAUNCH("Brick launch",MANUAL,false,90, new StatModifier(false,RES,-1,2), 0, false),
    SPINACH_SMACK("Spinach smack",FOOD,true,40, new StatModifier(false,RES,1,1), 0, false),
    DOOR_SLAM("Door slam",TRANSP,true,70, new StatModifier(false,RES,1,1), 0, false),
    ICBM_STRIKE("ICBM strike",GOVERN,false,130, new StatModifier(false,SPS,-2,-1), 0, false),
    CASHFLOW_BURNOUT("Cashflow burnout",CORPOR,false,130, new StatModifier(false,SPS,-2,0), 0, false),
    FERTILIZER_FUME("Fertilizer fume",FOOD,false,70, new StatModifier(false,SPS,1,1), 0, false),
    TAX_SPIKE("Tax spike",GOVERN,false,90, new StatModifier(false,SPS,1,2), 0, false),
    DANCE_KICK("Dance kick",ENTERT,true,55, new StatModifier(false,SPE,1,-1), 0, false),
    BACKPACK_SMACK("Backpack smack",EDUCAT,true,55, new StatModifier(false,SPE,1,-1), 0, false),
    COREO_ASSAULT("Coreographed assault",ENTERT,true,120, new StatModifier(false,new Stat[]{STR,RES},-1,0), 0, false),
    REITERATIVE_PUNCH("Reiterative punch",NUMBER,true,40, new StatModifier(false,STR,1,-1), 0, false),

    //STAT MODIFIERS (FOE)
    CUBIST_CRUSH("Cubist crush",DESIGN,true,70, new StatModifier(true,RES,-1,1), 0, false),
    CABLE_WHIP("Cable whip",TECHNO,true,70, new StatModifier(true,RES,-1,1), 0, false),
    DIVIDING_CHOP("Dividing chop",NUMBER,true,90, new StatModifier(true,RES,-1,2), 0, false),
    KNIFE_SLICE("Knife slice",FOOD,true,90, new StatModifier(true,RES,-1,2), 0, false),
    HARDCOVER_SLAM("Hardcover slam",LETTER,true,90, new StatModifier(true,RES,-1,2), 0, false),
    TOOL_SLAM("Tool slam",MANUAL,true,90, new StatModifier(true,RES,-1,2), 0, false),
    COMPROMISE_NETWORK("Compromise network",TECHNO,false,40, new StatModifier(true,SPR,-1,-1), 0, false),
    LAWSUIT("Lawsuit",CORPOR,false,90, new StatModifier(true,SPR,-1,2), 0, false),
    VENGEFUL_SONG("Vengeful song",ENTERT,false,90, new StatModifier(true,SPR,-1,2), 0, false),
    DISOBEDIENCE_SLAP("Disobedience slap",EDUCAT,true,75, new StatModifier(true,SPE,-1,1), 0, false),
    CEMENT_BLAST("Cement blast",MANUAL,false,75, new StatModifier(true,SPE,-1,1), 0, false),
    PRECISE_PIERCE("Precise pierce",DESIGN,true,95, new StatModifier(true,SPE,-1,2), 0, false),
    TRAFFIC_JAM("Traffic jam",TRANSP,false,95, new StatModifier(true,SPE,-1,2), 0, false),
    PAPER_SLASH("Paper slash",LETTER,true,70, new StatModifier(true,STR,-1,1), 0, false),
    TWISTER("Twister",ENVIRO,false,90, new StatModifier(true,STR,-1,2), 0, false),

    //CRIT MODIFIERS (SELF) - Changes the critical points of target player
    CONTROVERSIAL_MARKETING("Controversial marketing",DESIGN,false,115, new CritModifier(false,-2),0,false),
    EXPERIMENTAL_THERAPY("Experimental therapy",HEALTH,true,115, new CritModifier(false,-2),0,false),
    STATISTICAL_STRIKE("Statistical strike",NUMBER,false,115, new CritModifier(false,-2),0,false),
    BIASED_REVISIONISM("Biased revisionism",LETTER,false,115, new CritModifier(false,-2),0,false),
    FRIENDLY_MATCH("Friendly match",SPORT,false,40, new CritModifier(false,6),0,false),
    CERTIFICATE_SLASH("Certificate slash",EDUCAT,true,90, new CritModifier(false,2),0,false),
    GMO_GUT_PUNCH("GMO gut punch",FOOD,true,115, new CritModifier(false,-2),0,false),
    AWARENESS_CAMPAIGN("Awareness campaign",ENVIRO,false,40, new CritModifier(false,6),0,false),
    CORPORAL_PUNISHMENT("Corporal punishment",EDUCAT,true,115, new CritModifier(false,-2),0,false),
    TEST_CRASH("Test crash",TRANSP,true,40, new CritModifier(false,6),0,false),

    //CHARGING MOVES - Take a turn to charge, can be used in the next one
    BREAKTHROUGH_BLAST("Breakthrough blast",SCIENCE,false,120, null,0,true),
    LONG_AWAITED_NOVEL("Long awaited novel",LETTER,false,120, null,0,true),
    UNDERGROUND_UPPERCUT("Underground uppercut",MANUAL,true,80, null,0,true),
    PRIME_PUNCH("Prime punch",NUMBER,true,120, null,0,true),

    //DEFEAT CONDITIONS - Checks if the foe is defeated after dealing damage, either causes UNEMP or recoil if it fails
    PREDATORY_PRICING("Predatory pricing",CORPOR,false,140, new DefeatCondition(true),0,false),
    HUMANE_EXECUTION("Humane execution",HEALTH,true,140, new DefeatCondition(true),0,false),
    UNDERCUT_UPPERCUT("Undercut uppercut",CORPOR,true,130, new DefeatCondition(false),0,false),
    PUNCHLINE("Punchline",ENTERT,false,140, new DefeatCondition(true),0,false),
    MOMENTUM_COLLISION("Momentum collision",SCIENCE,true,130, new DefeatCondition(false),0,false),
    PSYCHOLOGICAL_TORTURE("Psychological torture",SECURI,false,140, new DefeatCondition(true),0,false),
    KIDNAP("Kidnap",TRANSP,false,130, new DefeatCondition(false),0,false),
    POISONED_BEVERAGE("Poisoned beverage",FOOD,false,140, new DefeatCondition(true),0,false),

    //STATUS INFLICTORS - Set a status (volatile or not) on the foe, unless given DRAIND which is inflicted on itself
    TRASH_TALK("Trash talk",SPORT,false,80, new StatusInflictor(DEMOR,2),0,false),
    REGULATORY_STRANGLE("Regulatory strangle",GOVERN,true,80, new StatusInflictor(DEMOR,2),0,false),
    COURSE_FLUNK("Course flunk",EDUCAT,false,80, new StatusInflictor(DEPRE,2),0,false),
    BACKSTAB("Backstab",CORPOR,true,80, new StatusInflictor(DEPRE,2),0,false),
    REDCARD_FOUL("Red-card foul",SPORT,true,150, new StatusInflictor(DRAIND,-1),0,false),
    DAM_FLOOD("Dam flood",ENVIRO,false,150, new StatusInflictor(DRAIND,-1),0,false),
    STATIC_SLAP("Static slap",SCIENCE,true,50, new StatusInflictor(FLINCH,1),0,false),
    REFLEX_HAMMER_TAP("Reflex hammer tap",HEALTH,true,50, new StatusInflictor(FLINCH,1),0,false),
    BLACKOUT("Blackout",MANUAL,false,50, new StatusInflictor(FLINCH,1),0,false),
    HEADLIGHTS_STUN("Headlights stun",TRANSP,false,50, new StatusInflictor(FLINCH,1),0,false),
    STUNT_DOUBLE_SMACKDOWN("Stunt double smackdown",ENTERT,true,85, new StatusInflictor(FLINCH,2),0,false),
    DISLOCATE("Dislocate",HEALTH,true,45, new StatusInflictor(INJUR,1),0,false),
    TACKLE("Tackle",SPORT,true,80, new StatusInflictor(INJUR,2),0,false),
    RIFLE_SHOT("Rifle shot",SECURI,false,80, new StatusInflictor(INJUR,2),0,false),
    STAMPEDE("Stampede",ENVIRO,true,80, new StatusInflictor(INJUR,2),0,false),
    VEHICLE_SMASH("Vehicle smash",TRANSP,true,80, new StatusInflictor(INJUR,2),0,false),
    GRAVITY_SLAM("Gravity slam",SCIENCE,true,85, new StatusInflictor(NAUSEA,2),0,false),
    SINUSOIDAL_BEAM("Sinusoidal beam",NUMBER,false,85, new StatusInflictor(NAUSEA,2),0,false),
    COLOR_OVERLOAD("Color overload",DESIGN,false,85, new StatusInflictor(NAUSEA,2),0,false),
    WRONG_MEDICINE("Wrong medicine",HEALTH,false,80, new StatusInflictor(SICK,2),0,false),
    ACID_SPLASH("Acid splash",SCIENCE,false,80, new StatusInflictor(SICK,2),0,false),
    MISGUIDED_NUTRITION("Misguided nutrition",FOOD,false,80, new StatusInflictor(SICK,2),0,false),
    ARREST_RAID("Arrest raid",SECURI,true,45, new StatusInflictor(UNEMP,1),0,false),
    RECKLESS_DRIVING("Reckless driving",TRANSP,true,45, new StatusInflictor(UNEMP,1),0,false),
    SLANDER_PIECE("Slander piece",LETTER,false,80, new StatusInflictor(UNEMP,2),0,false),
    MALWARE("Malware",TECHNO,false,80, new StatusInflictor(UNEMP,2),0,false);
    
    
    private final String name;
    private final Branch branch;
    private final boolean physical;
    private final Effect effect;
    private final int power;
    private final boolean charges;
    private final int priority;


    DamagingMove(String name, Branch branch, boolean physical, int power, Effect e, int priority, boolean charges) {
        this.name = name;
        this.branch = branch;
        this.physical = physical;
        this.power = power;
        this.effect = e;
        this.charges = charges;
        this.priority = priority;
    }


    //MODIFIES: user, foe
    //EFFECT: If move needs charging and user is not charged
    //            charge user
    //        Otherwise
    //            Calculate and apply damage to foe
    //            If move has an extra effect and move caused damage apply the effect
    @Override
    public void use(boolean usedByPlayer1) {
        Professional user = getUser(usedByPlayer1);
        Professional foe = getUser(!usedByPlayer1);
        if (charges && !user.getVolatileStatus().contains(CHARGE)) {
            user.addVolatileStatus(CHARGE);
        } else {
            int dmg = getDamage(user, foe);
            foe.takeDamage(dmg);
            if (effect != null && dmg != 0) {
                effect.apply(usedByPlayer1);
            }
        }

    }

    //EFFECT: return the damage of a move depending on user and foe stats, effectiveness and the same branch multiplier
    //        if the move is physical use user's strength and foe's resistance, otherwise use special stats
    //        multiply by branch efficiency
    //        if player has volatile status critical
    //              ignore negative stage changes(lower user attack/raise foe defense) and multiply by 1.5
    //        round down result to nearest integer
    public int getDamage(Professional user, Professional foe) {

        boolean critical = user.getVolatileStatus().contains(CRITIC);
        double userStrength = user.getRealStat(physical ? STR : SPS, false);
        double foeResistance = foe.getRealStat(physical ? RES : SPR, critical);
        double baseMultiplier = (critical ? 1.5 : 1) * power * userStrength / (4 * foeResistance);

        double sameBranchMultiplier = user.isBranch(branch) ? 1.5 : 1;

        double effectiveness = foe.checkEffectiveness(branch);

        return (int) Math.floor(baseMultiplier * sameBranchMultiplier * effectiveness);

    }
}
