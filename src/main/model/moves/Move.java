package model.moves;

import model.Round;
import model.effects.Effect;
import ui.TableAble;

import java.util.Arrays;
import java.util.Comparator;

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



    @Override
    //EFFECT: Return the headers displaying all values of Damaging
    default String[] getHeaders() {
        return new String[]{"Name","Branch","Power","Type","Added Effect"};
    }

    public static Move[] listAllMoves() {
        Damaging[] damaging = Damaging.values();
        NonDamaging[] nonDamaging = NonDamaging.values();
        Move[] moves = new Move[damaging.length + nonDamaging.length];

        System.arraycopy(damaging, 0, moves, 0, damaging.length);
        System.arraycopy(nonDamaging, 0, moves, damaging.length, nonDamaging.length);

        Arrays.sort(moves, Comparator.comparing(Move::getName));
        return moves;
    }

    @Override
    default TableAble[] getValues() {
        return listAllMoves();
    }



}
