package model.data;

//Each volatile status has its own effect
public enum Volatile implements Status {
    UNLCKY("unlucky",4),
    NAUSEA("nauseated",4),
    DRAIND("drained",1),
    CHARGE("charged",1),
    DUGIN("dug in",1),
    CRITIC("critical",0),
    FLINCH("flinched",0),
    PROTECTED("protected",0);

    private final String name;
    private final int turnLimit;

    Volatile(String name, int turnLimit) {
        this.name = name;
        this.turnLimit = turnLimit;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isVolatile() {
        return true;
    }

    public int getTurnLimit() {
        return turnLimit;
    }

}
