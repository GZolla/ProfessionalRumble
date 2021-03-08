package ui;

import persistence.Writable;

import java.util.Scanner;

//A set of tools for command line ui
public class UiManager {

    //EFFECTS: Prints prompt and asks for nextLine
    public static String prompt(String prompt) {
        System.out.print(prompt);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    //EFFECTS: Displays options, returns answer of user(insists on valid input)
    public static int chooseOptions(String prompt,String[] options,boolean addReturn) {
        String optionDisplay = "";
        for (int i = 0; i < options.length; i++) {
            optionDisplay += i + ": " + options[i] + "\n";
        }
        if (addReturn) {
            optionDisplay += options.length + ": Return \n";
        }
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println(prompt);
            System.out.print(optionDisplay);
            try {
                int response = sc.nextInt();
                if (response >= 0 && response < options.length + (addReturn ? 1 : 0)) {
                    return response;
                } else {
                    System.out.println(response + " is not one of the options.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Input must be an integer");
                sc.next();
            }
        }
    }

    //EFFECTS: Calls chooseOptions but based on a array of TableAble
    public static int chooseOptions(String prompt,TableAble[] options,boolean addReturn) {
        String[] stringArray = new String[options.length];
        for (int i = 0; i < options.length; i++) {
            stringArray[i] = options[i].getName();
        }
        return chooseOptions(prompt,stringArray,addReturn);
    }

    //EFFECTS: Asks for an input in [0,limit] or -1 to signify cancel if set addCancel == true
    public static int largeOptions(String prompt, int limit,boolean addCancel) {
        if (addCancel) {
            prompt += " Or input -1 to cancel.";
        }
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println(prompt);
            try {
                int response = sc.nextInt();
                if (response >= (addCancel ? -1 : 0) && response < limit) {
                    return response;
                } else {
                    System.out.println(response + " is not one of the options.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Input must be an integer");
                sc.next();
            }
        }
    }






    //EFFECTS: prints a table to command line based on a TableAble class(instance given, any instance works)
    public static void printTable(TableAble data) {
        printTable(data.toTable(),data.getHeaders());
    }

    //EFFECTS: prints a table to command line
    public static void printTable(String[][] table, String[] headers) {
        int[] columnSizes = getColumnSizes(table, headers);
        String hl = "-";
        for (int i : columnSizes) {
            //add "-" to hl i + 5(4 padding + 1 separator) times
            for (int j = 0; j < i + 5; j++) {
                hl += "-";
            }
        }
        System.out.println(hl);
        System.out.println(getRowDisplay(columnSizes,headers));
        System.out.println(hl);
        for (int i = 0; i < table.length; i++) {
            System.out.println(getRowDisplay(columnSizes,table[i]));
        }
        System.out.println(hl);
    }

    //EFFECT: get the largest string length for each column(including headers)
    public static int[] getColumnSizes(String[][] table, String[] headers) {
        int[] columnSizes = new int[headers.length];

        for (int i = 0; i < headers.length; i++) {
            columnSizes[i] = headers[i].length();
        }


        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < headers.length; j++) {
                if (table[i][j].length() > columnSizes[j]) {
                    columnSizes[j] = table[i][j].length();
                }

            }
        }
        return columnSizes;
    }

    //EFFECT: return value adding spaces for its length to match columnSize and add two spaces as left and right padding
    public static String getValueDisplay(int columnSize, String value) {
        String display = "  " + value; //Left padding and value
        for (int i = 0; i < columnSize - value.length() + 2; i++) {
            //Add " " to match size of column +right padding
            display += " ";
        }
        return display;
    }

    //EFFECT: return entire row display, separating columns with | and displaying values as produced by getValueDisplay
    public static String getRowDisplay(int[] columnSize, String[] row) {
        String display = "|";
        for (int i = 0; i < row.length; i++) {
            display += getValueDisplay(columnSize[i],row[i]) + "|";
        }
        return display;
    }

    //EFFECT: From an array of Named, return their names
    public static String[] getNames(Writable[] objects) {
        String[] names = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            names[i] = objects[i].getName();
        }
        return names;
    }
}
