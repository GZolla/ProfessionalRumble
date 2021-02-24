package model.moves;

import model.Round;
import model.data.Branch;
import model.effects.*;
import model.effects.StatModifier;

import static model.data.Branch.*;
import static model.data.NonVolatile.*;
import static model.data.Volatile.*;
import static model.data.Stat.*;


//Moves that do not cause damage but have added effects
public enum Status implements Move {
    //STAT MODIFIER
    OPTIMIZATION("Optimization",NUMBER, new StatModifier(false,SPS,2,-1),0),
    ACCELERATE("Accelerate",TRANSP,new StatModifier(false, SPE,1,-1), 0),
    WORKOUT("Workout",SPORT,new StatModifier(false,STR,2,-1), 0),
    SURVEILL("Surveill",GOVERN,new StatModifier(true,SPR,-2,-1), 0),
    STARVE("Starve",FOOD, new StatModifier(true,SPR,-2,-1), 0),

    //CRIT MODIFIERS
    HYPEREXAMINE("Hyper-examine",DESIGN,new CritModifier(false,8),0),
    SHAME("Shame",ENVIRO,new CritModifier(true,-8),0),

    //STATUS INFLICTORS
    NIHILISTIC_POEM("Nihilistic poem",LETTER,new StatusInflictor(DEMOR,-1),0),
    BAD_INFLUENCE("Bad influence",ENTERT,new StatusInflictor(DEPRE,-1),0),
    CONFUSING_PROBLEM("Confusing problem",NUMBER,new StatusInflictor(NAUSEA,-1),0);

    private String name;
    private Branch branch;
    private Effect effect;
    private int priority;


    Status(String name, Branch branch, Effect effect, int priority) {
        this.name = name;
        this.branch = branch;
        this.effect = effect;
        this.priority = priority;
    }

    @Override
    //EFFECT: apply the added effect
    public void use(Round round, boolean movedFirst) {
        effect.apply(usedByPlayer1);
    }



    //EFFECT: Return name, branch, and description of effect for all values
    public static String[][] toTable() {
        Status[] values = Status.values();
        String[][] table = new String[values.length][4];

        for (int i = 0; i < values.length; i++) {
            table[i][0] = i + "";
            table[i][1] = values[i].name;
            table[i][2] = values[i].branch.name();
            table[i][3] = values[i].effect.getDescription();
        }

        return table;
    }

    //EFFECT: Return the headers for the table described above
    public static String[] getHeaders() {
        return new String[]{"ID","Name","Branch","Added Effect"};
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Effect getEffect() {
        return effect;
    }

    @Override
    public Branch getBranch() {
        return branch;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getType() {
        return "Status";
    }
}
