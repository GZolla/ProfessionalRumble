package model.effects;

import model.Professional;
import model.Round;


//Additional effects that moves may have
public interface Effect {
    public void apply(Round round, boolean movedFirst);

    public String getDescription();

    default int getPriority() {
        return 0;
    }

    default boolean fails(Round round, boolean movedFirst) {
        return false;
    }
}
