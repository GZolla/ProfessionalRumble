package model.data;

//The 5 different stats all professionals
public enum Stat {
    STR("strength"),
    RES("resistance"),
    SPS("special strength"),
    SPR("special resistance"),
    SPE("speed");

    private String name;

    Stat(String name) {
        this.name = name;
    }

}
