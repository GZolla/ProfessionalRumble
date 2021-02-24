package model.effects;

import model.Professional;
import model.Round;
import model.data.Volatile;

public class Charge implements Effect {
    private boolean dugIn;


    public Charge(boolean dugIn) {
        this.dugIn = dugIn;
    }

    @Override
    public void apply(Round round, boolean movedFirst) {
        Volatile status = dugIn ? Volatile.DUGIN : Volatile.CHARGE;
        Professional user = round.getUser(movedFirst);

        if (user.getVolatileStatus().contains(status)) {
            user.removeVolatileStatus(status);
        } else {
            System.out.println(user.getFullName() + " " + (dugIn ? "dug itself in." : " charged itself."));
            user.addVolatileStatus(status);
        }

    }

    @Override
    public String getDescription() {
        if (dugIn) {
            return "User digs in before attacking. Is immune to attacks when dug in.";
        } else {
            return "User needs to charge before attacking.";
        }
    }

    @Override
    public boolean fails(Round round, boolean movedFirst) {
        return ;
    }
}
