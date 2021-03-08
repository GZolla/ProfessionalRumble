package model.effects;

import model.Professional;
import model.Round;

import static model.data.NonVolatile.FAINT;
import static model.data.NonVolatile.UNEMP;


//Effect that punishes the user if it fails to faint the foe, either by inflicting unemployment, or recoil damage
public class DefeatCondition implements Effect {

    private boolean causeUMP;

    public DefeatCondition(boolean causeUMP) {
        this.causeUMP = causeUMP;
    }

    @Override
    //EFFECT: if foe has not fainted either set UNEMP as non volatile status or take 2/3 the damage of the move used
    public void apply(Round round, boolean movedFirst) {
        Professional user = round.getUser(movedFirst);
        Professional foe = round.getUser(!movedFirst);
        if (foe.getNonVolatileStatus() != FAINT) {
            String pre = "After failing to defeat " + foe.getName() + " " + user.getName();
            if (causeUMP) {
                System.out.println(pre + " became unemployed.");
                user.setNonVolatileStatus(UNEMP);
            } else {
                System.out.println(pre + " suffered recoil.");
                user.takeDamage((2 * round.getUsedMoveDamage(movedFirst)) / 3,1);
            }
        }
    }

    @Override
    //EFFECTS: returns description of effect based on if it causes UNEMP
    public String getDescription() {
        String action = causeUMP ? "becomes unemployed" : "takes 2/3 of the damage caused to foe as recoil";
        return "If the foe is not defeated after this move, user " + action + ".";
    }
}
