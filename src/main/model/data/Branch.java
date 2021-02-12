package model.data;

//The Branch of a given professional or move, stores index of branches against which it is effective, weak or futile
public enum Branch {


    CORPOR(0,new int[]{0,3,12,15},new int[]{5,6,7,14},-1),
    SPORT(1,new int[]{1,8,9},new int[]{4,11,14},-1),
    ENTERT(2,new int[]{3,6,12},new int[]{4,7,8},-1),
    MANUAL(3,new int[]{0,7,14},new int[]{2,10,13},-1),
    SCIENCE(4,new int[]{5,6,14},new int[]{4,15},-1),
    HEALTH(5,new int[]{2,11,14},new int[]{1,7,13},-1),
    EDUCAT(6,new int[]{1,7,10},new int[]{3,8,9},-1),
    GOVERN(7,new int[]{0,5,7,10,13},new int[]{2,3,15},11),
    NUMBER(8,new int[]{2,4,14},new int[]{9},6),
    LETTER(9,new int[]{2,4,7},new int[]{8},6),
    DESIGN(10,new int[]{3,4,9},new int[]{7,11,15},-1),
    SECURI(11,new int[]{6,9,11},new int[]{0,2,5},-1),
    TRANSP(12,new int[]{1,13,15},new int[]{0,2,14},-1),
    FOOD(13,new int[]{2,5,15},new int[]{1,3,13},-1),
    TECHNO(14,new int[]{3,6,8,11},new int[]{0,1},9),
    ENVIRO(15,new int[]{0,7,13},new int[]{1,5,12},-1);

    private int index;
    private int[] effective;
    private int[] weak;
    private int futile;

    Branch(int index, int[] effective, int[] weak, int futile) {
        this.index = index;
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

    public int getIndex() {
        return index;
    }
}
