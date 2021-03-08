package model.moves;

import model.Professional;
import model.Round;
import model.data.Branch;
import model.effects.Effect;
import ui.TableAble;

public interface Move extends TableAble {
    public void use(Round round, boolean movedFirst);

    public Effect getEffect();

    default int getPriority() {
        Effect eff = getEffect();
        if (eff == null) {
            return 0;
        } else {
            return eff.getPriority();
        }
    }



}
