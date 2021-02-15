package model.moves;

import model.Professional;
import model.data.Branch;
import model.effects.Effect;

public interface Move {
    public void use(boolean usedByPlayer1);

    public String getName();

    public Effect getEffect();

    public Branch getBranch();

    public int getPriority();

}
