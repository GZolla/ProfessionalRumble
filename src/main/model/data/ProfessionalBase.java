package model.data;


import ui.TableAble;

//Base for all professionals, stores name, initial stats and branch for professionals
public enum ProfessionalBase implements TableAble {
    CEO("CEO",70,70,85,110,100,100,Branch.CORPOR),
    QUARTERBACK("Quarterback",90,120,60,75,70,120,Branch.SPORT),
    SINGER("Singer",65,75,70,115,95,115,Branch.ENTERT),
    MINER("Miner",100,90,130,65,70,80,Branch.MANUAL),
    PHYSICIST("Physicist",85,100,90,110,100,50,Branch.SCIENCE),
    DOCTOR("Doctor",125,85,80,85,100,65,Branch.HEALTH),
    PROFESSOR("Professor",100,60,70,110,110,85,Branch.EDUCAT),
    PRESIDENT("President",80,100,100,100,100,60,Branch.GOVERN),
    MATHEMATICIAN("Mathematician",120,50,70,100,125,70,Branch.NUMBER),
    POET("Poet",90,50,80,125,85,110,Branch.LETTER),
    TEXTILE_MAKER("Textile maker",100,100,80,80,80,95,Branch.DESIGN),
    POLICEMAN("Policeman",80,120,110,65,60,105,Branch.SECURI),
    LAWYER("Lawyer",70,100,65,100,120,95,Branch.CORPOR,Branch.LETTER),
    COACH("Coach",115,110,100,70,70,70,Branch.EDUCAT,Branch.SPORT),
    PHARMACIST("Pharmacist",120,70,80,95,85,85,Branch.HEALTH,Branch.SCIENCE),
    CENTRAL_BANK_CHAIR("Central Bank chair",140,60,60,100,100,75,Branch.GOVERN,Branch.NUMBER),
    INTERIOR_DECORATOR("Interior decorator",90,100,70,85,100,100,Branch.DESIGN,Branch.MANUAL),
    JANITOR("Janitor",100,75,90,65,95,110,Branch.MANUAL),
    ARMY_GENERAL("Army general",85,120,110,100,50,70,Branch.SECURI,Branch.GOVERN),
    REALITY_COMPETITOR("Reality competitor",85,110,75,70,70,125,Branch.ENTERT,Branch.SPORT),
    INDUSTRIAL_FARMER("Industrial farmer",100,100,100,85,75,75,Branch.FOOD,Branch.CORPOR),
    CHICAGO_ECON("Chicago econ.",100,70,70,100,70,125,Branch.CORPOR,Branch.NUMBER),
    DICTATOR("Dictator",100,80,110,80,95,70,Branch.GOVERN,Branch.SECURI),
    COMEDIAN("Comedian",100,50,80,105,105,100,Branch.ENTERT,Branch.LETTER),
    CHEMIST("Chemist",80,70,70,125,100,95,Branch.SCIENCE),
    COMIC_BOOK_WRITER("Comic book writer",110,50,90,100,80,120,Branch.LETTER,Branch.DESIGN),
    POKER_PLAYER("Poker player",70,85,90,115,95,85,Branch.SPORT,Branch.NUMBER),
    CHIROPRACTOR("Chiropractor",110,110,100,50,90,80,Branch.HEALTH,Branch.MANUAL),
    WEAPONS_DEVELOPER("Weapons developer",85,100,60,110,125,70,Branch.SECURI,Branch.SCIENCE),
    MOCKUMENTARY_BOSS("Mockumentary Boss",85,120,70,110,70,85,Branch.ENTERT,Branch.CORPOR),
    JUDGE("Judge",100,80,80,100,110,70,Branch.GOVERN,Branch.LETTER),
    SELFDEFENSE_COACH("Self-defense Coach",90,80,125,60,80,115,Branch.EDUCAT,Branch.SECURI),
    PERSONAL_TRAINER("Personal Trainer",90,125,80,60,80,115,Branch.SPORT,Branch.HEALTH),
    ARCHITECT("Architect",80,95,70,110,90,90,Branch.DESIGN,Branch.NUMBER),
    LIBERAL_ARTS_PROFESSOR("Liberal Arts Professor",100,60,80,120,80,95,Branch.EDUCAT,Branch.LETTER),
    INVENTOR("Inventor",85,110,60,95,125,65,Branch.SCIENCE,Branch.DESIGN),
    CHEF("Chef",100,110,85,100,70,70,Branch.FOOD),
    TAXI_DRIVER("Taxi driver",90,130,100,50,45,120,Branch.TRANSP),
    COMPUTER_SCIENTIST("Computer scientist",100,50,60,110,140,75,Branch.TECHNO),
    ENVIRONMENTALIST("Environmentalist",85,95,50,130,80,95,Branch.ENVIRO);

    private final String name;
    private final int life;
    private final int strength;
    private final int resistance;
    private final int specialStrengh;
    private final int specialResistance;
    private final int speed;
    private final Branch branch1;
    private final Branch branch2;

    //EFFECTS: Creates ProfessionalBase with one branch
    ProfessionalBase(String name, int life, int str, int res, int sps, int spr, int spe, Branch branch1) {
        this.name = name;
        this.life = life;
        this.strength = str;
        this.resistance = res;
        this.specialStrengh = sps;
        this.specialResistance = spr;
        this.speed = spe;
        this.branch1 = branch1;
        branch2 = null;
    }

    //EFFECTS: Creates ProfessionalBase with two branches
    ProfessionalBase(String n, int lif, int str, int res, int sps, int spr, int spe, Branch b1, Branch b2) {
        this.name = n;
        this.life = lif;
        this.strength = str;
        this.resistance = res;
        this.specialStrengh = sps;
        this.specialResistance = spr;
        this.speed = spe;
        this.branch1 = b1;
        this.branch2 = b2;
    }





    public int getStrength() {
        return strength;
    }

    public int getLife() {
        return life;
    }

    public int getResistance() {
        return resistance;
    }

    public int getSpecialStrentgh() {
        return specialStrengh;
    }

    public int getSpecialResistance() {
        return specialResistance;
    }

    public int getSpeed() {
        return speed;
    }

    public Branch getBranch1() {
        return branch1;
    }

    public Branch getBranch2() {
        return branch2;
    }

    @Override
    public String toString() {
        return name;
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
    //EFFECTS: Return index, name, stats and branch in a String array
    public String[] toRow() {
        return new String[]{
                name,
                branch1.getName(),
                (branch2 != null ? branch2.getName() : ""),
                life + "",
                strength + "",
                resistance + "",
                specialStrengh + "",
                specialResistance + "",
                speed + ""
        };
    }

    @Override
    //EFFECT: Return the headers for the table displaying values of Status
    public String[] getHeaders() {
        return new String[] {
            "Name",
            "Branches",
            "",
            "Life",
            "Strength",
            "Resistance",
            "Special Strength",
            "Special Resistance",
            "Speed"
        };
    }

    @Override
    public TableAble[] getValues() {
        return ProfessionalBase.values();
    }
}
