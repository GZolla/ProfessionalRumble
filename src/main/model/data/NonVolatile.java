package model.data;

import ui.TableAble;

import java.awt.*;

//NonVolatile Status that a professional can have, each has its own effect
public enum NonVolatile implements Status, TableAble {
    UNEMP("Unemployed","Cannot move, lasts 2 turns", new Color(20,20,100)),
    SICK("Sick","After every turn, x/16 of its base life is lost where x is turns since sick", new Color(20,120,250)),
    DEMOR("Demoralised","Cannot move every other turn, speed is halved", new Color(80,80,80)),
    INJUR("Injured","After every turn 1/16 of its base life is lost, strength is halved", new Color(120,20,20)),
    DEPRE(
    "Depressed","After every turn 1/16 of its base life is lost, special strength is halved", new Color(70,0,150)
    ),
    FAINT("Fainted","Represents a defeated professional", new Color(20,20,20));

    private final String name;
    private final String description;
    private final Color color;

    NonVolatile(String name, String description, Color color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    @Override
    public boolean isVolatile() {
        return false;
    }

    public String getDescription() {
        return description;
    }

    public Color getColor() {
        return color;
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
