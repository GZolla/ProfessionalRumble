package model.data;

import ui.TableAble;

//NonVolatile Status that a professional can have, each has its own effect
public enum NonVolatile implements Status, TableAble {
    UNEMP("Unemployed","Cannot move, lasts 2 turns"),
    SICK("Sick","After every turn, x/16 of its base life is lost where x is turns since sick"),
    DEMOR("Demoralised","Cannot move every other turn, speed is halved"),
    INJUR("Injured","After every turn 1/16 of its base life is lost, strength is halved"),
    DEPRE("Depressed","After every turn 1/16 of its base life is lost, special strength is halved"),
    FAINT("Fainted","Represents a defeated professional");

    private final String name;
    private final String description;

    NonVolatile(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean isVolatile() {
        return false;
    }

    public String getDescription() {
        return description;
    }

//--- TABLE ABLE METHODS -----------------------------------------------------------------------------------------------
    @Override
    public int getIndex() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    //EFFECTS: Return name, and description in String array
    public String[] toRow() {
        return new String[]{name,description};
    }

    @Override
    public String[] getHeaders() {
        return new String[]{"Name","Description"};
    }

    @Override
    public TableAble[] getValues() {
        return NonVolatile.values();
    }
}
