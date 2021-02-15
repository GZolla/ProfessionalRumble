package ui;

import model.Player;
import model.Professional;
import model.data.NonVolatile;
import model.moves.Move;

import static model.data.ProfessionalBase.*;
import static model.data.ProfessionalBase.QUARTERBACK;
import static model.moves.DamagingMove.*;
import static model.moves.DamagingMove.MOMENTUM_COLLISION;
import static model.moves.StatusMove.*;
import static model.moves.StatusMove.ACCELERATE;

public class Main {

    public static final Player PLAYER_1 = new Player("Paul",true);
    public static final Player PLAYER_2 = new Player("John",false);


    //EFFECT
    public static void main(String[] args) {
        System.out.println("Welcome to Professional's Rumble!");
        while (true) {
            String[] options = new String[]{"Start Battle","Edit player info","Exit"};
            int choice = UiManager.chooseOptions("Select an action.",options,false);
            if (choice == 2) {
                break;
            } else if (choice == 1) {
                TeamBuilder.main(null);
            } else {
                Battle.main(null);
            }
        }

    }

    //EFFECT: Return the selected Professional from PLAYER_1(if usedByPlayer1) or PLAYER_2
    public static Professional getUser(boolean usedByPlayer1) {
        return (usedByPlayer1 ? PLAYER_1 : PLAYER_2).getSelectedProfessional();
    }
}
