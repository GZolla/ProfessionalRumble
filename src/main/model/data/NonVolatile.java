package model.data;

//NonVolatile Status that a professional can have, each has its own effect
public enum NonVolatile implements Status {
    UNEMP("unemployed"), // Cannot move, lasts 2 turns
    SICK("sick"), //After every turn, x/16 of its base life is lost where x is turns since sick
    DEMOR("demoralised"), //Cannot move every other turn, speed is halved
    INJUR("injured"), //After every turn 1/16 of its base life is lost, strength is halved
    DEPRE("depressed"),//After every turn 1/16 of its base life is lost, special strength is halved
    FAINT("fainted");//Represents a defeated professional

    private String name;

    NonVolatile(String name) {
        this.name = name;
    }

    @Override
    public boolean isVolatile() {
        return false;
    }
}
