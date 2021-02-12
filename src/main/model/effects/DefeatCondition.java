package model.effects;

import model.Professional;

import static model.data.NonVolatile.FAINT;
import static model.data.NonVolatile.UNEMP;
import static ui.Main.getUser;


//Effect that punishes the user if it fails to faint the foe, either by inflicting unemployment, or recoil damage
public class DefeatCondition implements Effect {

    private boolean causeUMP;

    public DefeatCondition(boolean causeUMP) {
        this.causeUMP = causeUMP;
    }

    @Override
    //EFFECT: if foe has not fainted either set UNEMP as non volatile status or take 2/3 the damage of the move used
    public void apply(boolean usedByPlayer1) {
        Professional user = getUser(usedByPlayer1);
        Professional foe = getUser(!usedByPlayer1);
        if (foe.getNonVolatileStatus() != FAINT) {
            if (causeUMP) {
                user.setNonVolatileStatus(UNEMP);
            } else {
                user.takeDamage((int) Math.floor(2.0 * user.getLastMoveDamage(foe) / 3));
            }
        }
    }
}
