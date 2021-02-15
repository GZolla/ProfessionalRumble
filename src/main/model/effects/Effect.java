package model.effects;

import model.Professional;


//Additional effects that moves may have
public interface Effect {
    public void apply(boolean usedByPlayer1);

    public String getDescription();
}
