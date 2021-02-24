package model.data;

//Each volatile status has its own effect
public enum Volatile implements Status {
    UNLCKY("unlucky",4),
    NAUSEA("nauseated",4),
    DRAIND("drained",1),
    FLINCH("flinched",1),
    CHARGE("charged",1),
    DUGIN("dug in",1),
    CRITIC("critical",0);

    private String name;
    private int turnLimit;

    Volatile(String name, int turnLimit) {
        this.name = name;
        this.turnLimit = turnLimit;
    }

    @Override
    public boolean isVolatile() {
        return true;
    }

    public int getTurnLimit() {
        return turnLimit;
    }

    @Override
    public String getName() {
        return name;
    }
}
