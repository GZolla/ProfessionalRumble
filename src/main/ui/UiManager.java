package ui;

import persistence.Writable;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

import static javax.swing.JOptionPane.showMessageDialog;

//A set of tools for command line ui
public class UiManager {







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

    //EFFECT: From an array of Writable, return their names
    public static String[] getNames(Writable[] objects) {
        String[] names = new String[objects.length];
        for (int i = 0; i < objects.length; i++) {
            names[i] = objects[i].getName();
        }
        return names;
    }

    //CITATION: https://stackoverflow.com/questions/4627553/show-jframe-in-a-specific-screen-in-dual-monitor-configuration
    //MODIFIES: frame
    //EFFECTS: Show JFrame in second monitor
    public static void showOnSecondScreen(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        if (gd.length > 0) {
            GraphicsConfiguration a = gd[1 < gd.length ? 1 : 0].getDefaultConfiguration();
            frame.setLocation(a.getBounds().x, a.getBounds().y + frame.getY());
            frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        } else {
            throw new RuntimeException("No Screens Found");
        }
    }

    //EFFECTS: show the message with the given title
    public static void showMessage(JFrame parent,String message, String title) {
        showMessageDialog(parent,message,title,JOptionPane.PLAIN_MESSAGE);
    }



}
