package model.data;

import ui.TableAble;

import java.awt.*;

//The Branch of a given professional or move, stores index of branches against which it is effective, weak or futile
public enum Branch implements TableAble {


    CORPOR(new int[]{0,3,12,15},new int[]{5,6,7,14},-1, new Color(30,78,121)),
    SPORT(new int[]{1,8,9},new int[]{4,11,14},-1,new Color(192,0,0)),
    ENTERT(new int[]{3,6,12},new int[]{4,7,8},-1,new Color(255,102,255)),
    MANUAL(new int[]{7,8,14},new int[]{2,10,13},-1,new Color(153,102,51)),
    SCIENCE(new int[]{5,6,14},new int[]{4,15},-1,new Color(146,208,80)),
    HEALTH(new int[]{2,11,14},new int[]{1,7,13},-1,new Color(91,155,213)),
    EDUCAT(new int[]{1,7,10},new int[]{3,8,9},-1,new Color(197,224,179)),
    GOVERN(new int[]{0,5,7,10,13},new int[]{2,3,15},11, new Color(249,125,49)),
    NUMBER(new int[]{2,4,14},new int[]{9},6,new Color(29,66,10)),
    LETTER(new int[]{2,4,7},new int[]{8},6,new Color(255,217,101)),
    DESIGN(new int[]{3,4,9},new int[]{7,11,15},-1, new Color(255,255,153)),
    SECURI(new int[]{6,9,11},new int[]{0,2,5},-1, new Color(112,112,112)),
    TRANSP(new int[]{1,13,15},new int[]{0,2,14},-1, new Color(22,22,22)),
    FOOD(new int[]{2,5,15},new int[]{1,3,13},-1,new Color(211,166,30)),
    TECHNO(new int[]{3,6,8,11},new int[]{0,1},9, new Color(71,34,167)),
    ENVIRO(new int[]{0,7,13},new int[]{1,5,12},-1, new Color(84,129,53));

    private final int[] effective;
    private final int[] weak;
    private final int futile;
    private final Color color;

    Branch(int[] effective, int[] weak, int futile, Color color) {
        this.effective = effective;
        this.weak = weak;
        this.futile = futile;
        this.color = color;
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
        return name();
    }

    @Override
    //EFFECTS: Return name, and a list of effectiveness for every branch.
    public String[] toRow() {
        Branch[] branches = Branch.values();
        String[] row = new String[branches.length + 1];
        row[0] = name();
        for (int i = 0; i < branches.length; i++) {
            double eff = getEffectiveness(i);
            row[i + 1] = (eff + "").substring(0,eff == 0.5 ? 3 : 1);
        }
        return row;
    }

    @Override
    //EFFECT: Return "V Attacker \\ Defender >" followed by the name of all Branches
    public String[] getHeaders() {
        Branch[] branches = Branch.values();
        String[] headers = new String[branches.length + 1];
        headers[0] = "Attacker \\ Defender";

        for (int i = 0; i < branches.length; i++) {
            headers[i + 1] = branches[i].name().substring(0,3);
        }

        return headers;
    }

    @Override
    //EFFECTS: Returns all values in Branch
    public TableAble[] getValues() {
        return Branch.values();
    }

}
