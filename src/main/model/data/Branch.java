package model.data;

import ui.TableAble;

//The Branch of a given professional or move, stores index of branches against which it is effective, weak or futile
public enum Branch implements TableAble {


    CORPOR(new int[]{0,3,12,15},new int[]{5,6,7,14},-1),
    SPORT(new int[]{1,8,9},new int[]{4,11,14},-1),
    ENTERT(new int[]{3,6,12},new int[]{4,7,8},-1),
    MANUAL(new int[]{7,8,14},new int[]{2,10,13},-1),
    SCIENCE(new int[]{5,6,14},new int[]{4,15},-1),
    HEALTH(new int[]{2,11,14},new int[]{1,7,13},-1),
    EDUCAT(new int[]{1,7,10},new int[]{3,8,9},-1),
    GOVERN(new int[]{0,5,7,10,13},new int[]{2,3,15},11),
    NUMBER(new int[]{2,4,14},new int[]{9},6),
    LETTER(new int[]{2,4,7},new int[]{8},6),
    DESIGN(new int[]{3,4,9},new int[]{7,11,15},-1),
    SECURI(new int[]{6,9,11},new int[]{0,2,5},-1),
    TRANSP(new int[]{1,13,15},new int[]{0,2,14},-1),
    FOOD(new int[]{2,5,15},new int[]{1,3,13},-1),
    TECHNO(new int[]{3,6,8,11},new int[]{0,1},9),
    ENVIRO(new int[]{0,7,13},new int[]{1,5,12},-1);

    private final int[] effective;
    private final int[] weak;
    private final int futile;

    Branch(int[] effective, int[] weak, int futile) {
        this.effective = effective;
        this.weak = weak;
        this.futile = futile;
    }


    //EFFECT: Get the effectiveness multiplier of a move of this branch against a the Branch with given index
    //            effective: x2
    //            weak: x0.5
    //            futile: x0
    //            normal: x1
    public double getEffectiveness(int branchIndex) {
        for (int i : effective) {
            if (i == branchIndex) {
                return 2;
            }
        }
        for (int i : weak) {
            if (i == branchIndex) {
                return 0.5;
            }
        }
        if (futile == branchIndex) {
            return 0;
        }
        return 1;
    }



//--- TABLE ABLE METHODS -----------------------------------------------------------------------------------------------
    @Override
    public int getIndex() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    //EFFECTS: Return name, and a list of effectiveness for every branch.
    public String[] toRow() {
        Branch[] branches = Branch.values();
        String[] row = new String[branches.length + 1];
        row[0] = name();
        for (int i = 0; i < branches.length; i++) {
            row[i + 1] = getEffectiveness(i) + "";
        }
        return row;
    }

    @Override
    //EFFECT: Return "V Attacker \\ Defender >" followed by the name of all Branches
    public String[] getHeaders() {
        Branch[] branches = Branch.values();
        String[] headers = new String[branches.length + 1];
        headers[0] = "V Attacker \\ Defender >";

        for (int i = 0; i < branches.length; i++) {
            headers[i + 1] = branches[i].name();
        }

        return headers;
    }

    @Override
    //EFFECTS: Returns all values in Branch
    public TableAble[] getValues() {
        return Branch.values();
    }

}
