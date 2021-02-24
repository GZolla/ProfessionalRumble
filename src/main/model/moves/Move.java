package model.moves;

import model.Professional;
import model.Round;
import model.data.Branch;
import model.effects.Effect;

public interface Move {
    public void use(Round round, boolean movedFirst);

    public String getName();

    public Effect getEffect();

    public Branch getBranch();

    public int getPriority();

    public String getType();


}
