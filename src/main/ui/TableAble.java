package ui;

import model.effects.Effect;

public interface TableAble {

    //EFFECTS: returns index in corresponding class
    public int getIndex();

    public String getName();

    //EFFECTS: Converts a value to the
    public String[] toRow();



    //NOTICE: The methods below could be static but are left as is for methods using this class to be able to use them

    //EFFECT: returns the complete list of values in a TableAble class
    public TableAble[] getValues();

    //EFFECT:Return the values in a 2d-array to be displayed on a table
    default String[][] toTable() {
        TableAble[] values = getValues();
        String[][] table = new String[values.length][getHeaders().length];

        for (int i = 0; i < values.length; i++) {
            table[i] = values[i].toRow();
        }

        return table;
    }

    //EFFECT: Return the headers for the table described above
    public String[] getHeaders();
}
