package model.data;

import ui.UiManager;

//NonVolatile Status that a professional can have, each has its own effect
public enum NonVolatile implements Status {
    UNEMP("Unemployed","Cannot move, lasts 2 turns"),
    SICK("Sick","After every turn, x/16 of its base life is lost where x is turns since sick"),
    DEMOR("Demoralised","Cannot move every other turn, speed is halved"),
    INJUR("Injured","After every turn 1/16 of its base life is lost, strength is halved"),
    DEPRE("Depressed","After every turn 1/16 of its base life is lost, special strength is halved"),
    FAINT("Fainted","Represents a defeated professional");

    private String name;
    private String description;

    NonVolatile(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean isVolatile() {
        return false;
    }

    public static void print() {
        String[][] table = new String[6][2];
        NonVolatile[] values = NonVolatile.values();
        for (int i = 0; i < 6; i++) {
            table[i][0] = values[i].name;
            table[i][1] = values[i].description;
        }

        UiManager.printTable(table,new String[]{"Name","Description"});
    }

    @Override
    public String getName() {
        return name;
    }
}
