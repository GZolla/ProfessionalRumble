package model.moves;

import model.Professional;
import model.Round;
import model.data.Branch;
import model.data.Status;
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

    public static String[] listAllMoves() {
        Damaging[] damaging = Damaging.values();
        NonDamaging[] nonDamaging = NonDamaging.values();
        String[] moves = new String[damaging.length + nonDamaging.length];

        for (int i = 0; i < damaging.length; i++) {
            moves[i] = damaging[i].getName();
        }
        for (int i = 0; i < nonDamaging.length; i++) {
            moves[i + damaging.length] = nonDamaging[i].getName();
        }

        return moves;
    }



}
