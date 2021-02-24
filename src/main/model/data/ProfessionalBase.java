package model.data;


import model.Professional;

import static model.data.Branch.*;

//Base for all professionals, stores name, initial stats and branch for professionals
public enum ProfessionalBase {
    CEO("CEO",70,70,85,110,100,100,0,CORPOR),
    QUARTERBACK("Quarterback",90,120,60,75,70,120,0,SPORT),
    SINGER("Singer",65,75,70,115,95,115,0,ENTERT),
    MINER("Miner",100,90,130,65,70,80,0,MANUAL),
    PHYSICIST("Physicist",85,100,90,110,100,50,0,SCIENCE),
    DOCTOR("Doctor",125,85,80,85,100,65,0,HEALTH),
    PROFESSOR("Professor",100,60,70,110,110,85,0,EDUCAT),
    PRESIDENT("President",80,100,100,100,100,60,0,GOVERN),
    MATHEMATICIAN("Mathematician",120,50,70,100,125,70,0,NUMBER),
    POET("Poet",90,50,80,125,85,110,0,LETTER),
    TEXTILE_MAKER("Textile maker",100,100,80,80,80,95,0,DESIGN),
    POLICEMAN("Policeman",80,120,110,65,60,105,0,SECURI),
    LAWYER("Lawyer",70,100,65,100,120,95,1,CORPOR,LETTER),
    COACH("Coach",115,110,100,70,70,70,1,EDUCAT,SPORT),
    PHARMACIST("Pharmacist",120,70,80,95,85,85,1,HEALTH,SCIENCE),
    CENTRAL_BANK_CHAIR("Central Bank chair",140,60,60,100,100,75,0,GOVERN,NUMBER),
    INTERIOR_DECORATOR("Interior decorator",90,100,70,85,100,100,1,DESIGN,MANUAL),
    JANITOR("Janitor",100,75,90,65,95,110,1,MANUAL),
    ARMY_GENERAL("Army general",85,120,110,100,50,70,2,SECURI,GOVERN),
    REALITY_COMPETITOR("Reality competitor",85,110,75,70,70,125,0,ENTERT,SPORT),
    INDUSTRIAL_FARMER("Industrial farmer",100,100,100,85,75,75,1,FOOD,CORPOR),
    CHICAGO_ECON("Chicago econ.",100,70,70,100,70,125,1,CORPOR,NUMBER),
    DICTATOR("Dictator",100,80,110,80,95,70,3,GOVERN,SECURI),
    COMEDIAN("Comedian",100,50,80,105,105,100,2,ENTERT,LETTER),
    CHEMIST("Chemist",80,70,70,125,100,95,2,SCIENCE),
    COMIC_BOOK_WRITER("Comic book writer",110,50,90,100,80,120,1,LETTER,DESIGN),
    POKER_PLAYER("Poker player",70,85,90,115,95,85,2,SPORT,NUMBER),
    CHIROPRACTOR("Chiropractor",110,110,100,50,90,80,1,HEALTH,MANUAL),
    WEAPONS_DEVELOPER("Weapons developer",85,100,60,110,125,70,4,SECURI,SCIENCE),
    MOCKUMENTARY_BOSS("Mockumentary Boss",85,120,70,110,70,85,3,ENTERT,CORPOR),
    JUDGE("Judge",100,80,80,100,110,70,5,GOVERN,LETTER),
    SELFDEFENSE_COACH("Self-defense Coach",90,80,125,60,80,115,1,EDUCAT,SECURI),
    PERSONAL_TRAINER("Personal Trainer",90,125,80,60,80,115,2,SPORT,HEALTH),
    ARCHITECT("Architect",80,95,70,110,90,90,0,DESIGN,NUMBER),
    LIBERAL_ARTS_PROFESSOR("Liberal Arts Professor",100,60,80,120,80,95,3,EDUCAT,LETTER),
    INVENTOR("Inventor",85,110,60,95,125,65,1,SCIENCE,DESIGN),
    CHEF("Chef",100,110,85,100,70,70,6,FOOD),
    TAXI_DRIVER("Taxi driver",90,130,100,50,45,120,2,TRANSP),
    COMPUTER_SCIENTIST("Computer scientist",100,50,60,110,140,75,2,TECHNO),
    ENVIRONMENTALIST("Environmentalist",85,95,50,130,80,95,4,ENVIRO);

    private String name;
    private int life;
    private int strength;
    private int resistance;
    private int specialStrengh;
    private int specialResistance;
    private int speed;
    private int speedDistinct;
    private Branch branch1;
    private Branch branch2;

    ProfessionalBase(String name, int life, int str, int res, int sps, int spr, int spe, int spDis, Branch branch1) {
        this.name = name;
        this.life = life;
        this.strength = str;
        this.resistance = res;
        this.specialStrengh = sps;
        this.specialResistance = spr;
        this.speed = spe;
        this.speedDistinct = spDis;
        this.branch1 = branch1;
        branch2 = null;
    }

    //EFFECTS: Creates ProfessionalBase with two branches
    ProfessionalBase(String n, int lif, int str, int res, int sps, int spr, int spe, int spDis, Branch b1, Branch b2) {
        this.name = n;
        this.life = lif;
        this.strength = str;
        this.resistance = res;
        this.specialStrengh = sps;
        this.specialResistance = spr;
        this.speed = spe;
        this.speedDistinct = spDis;
        this.branch1 = b1;
        this.branch2 = b2;
    }

    //REQUIRES: index is inside range of existing base professionals
    //EFFECT: Get base by given index
    public static ProfessionalBase getByIndex(int index) {
        return ProfessionalBase.values()[index];
    }

    //EFFECT: returns an array with the names of all existing professionals.
    public static String[] getAllNames() {
        ProfessionalBase[] professionals = ProfessionalBase.values();
        String[] names = new String[professionals.length];

        for (int i = 0; i < professionals.length; i++) {
            names[i] = professionals[i].getName();
        }

        return names;
    }

    //EFFECT: returns a table with name, stats and branch of every professional
    public static String[][] toTable() {
        ProfessionalBase[] values = ProfessionalBase.values();
        String[][] table = new String[values.length][9];

        for (int i = 0; i < values.length; i++) {
            table[i][0] = i + "";
            table[i][1] = values[i].name;
            table[i][2] = values[i].branch1.name();
            if (values[i].branch2 != null) {
                table[i][2] += " / " + values[i].branch2.name();
            }
            table[i][3] = values[i].life + "";
            table[i][4] = values[i].strength + "";
            table[i][5] = values[i].resistance + "";
            table[i][6] = values[i].specialStrengh + "";
            table[i][7] = values[i].specialResistance + "";
            table[i][8] = values[i].speed + "";
        }

        return table;
    }

    //EFFECT: return header for table described in method above
    public static String[] getHeaders() {
        String[] headers = new String[9];
        headers[0] = "ID";
        headers[1] = "Name";
        headers[2] = "Branch";
        headers[3] = "Life";
        headers[4] = "Strength";
        headers[5] = "Resistance";
        headers[6] = "Special Strength";
        headers[7] = "Special Resistance";
        headers[8] = "Speed";
        return headers;
    }


    public String getName() {
        return name;
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

    public int getSpecialStrengh() {
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

    public int getSpeedDistinct() {
        return speedDistinct;
    }
}
