package model.data;

//The 5 different stats all professionals have
public enum Stat {
    STR("strength"), // Multiplies base power of physical attacks dealt
    RES("resistance"), // Divides base power of physical attacks received
    SPS("special strength"),  // Multiplies base power of special attacks dealt
    SPR("special resistance"), // Divides base power of special attacks received
    SPE("speed"); //Used to calculate order of attacks

    private String name;

    Stat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
