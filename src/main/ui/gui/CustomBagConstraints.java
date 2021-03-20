package ui.gui;

import java.awt.*;

import static java.awt.GridBagConstraints.*;

public class CustomBagConstraints {


    //EFFECTS: returns a GridBagConstraints with default values except for given gridx and gridy
    public static GridBagConstraints customConstraint(int gridx, int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        return constraints;
    }

    //EFFECTS: returns a GridBagConstraints with default values except for given gridx, gridy, gridwidth and gridheight
    public static GridBagConstraints customConstraint(int gridx, int gridy, int gridwidth, int gridheight) {
        GridBagConstraints constraints = customConstraint(gridx,gridy);
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        return constraints;
    }

    //EFFECTS: returns a GridBagConstraints with default values except for given gridx, gridy, gridwidth and gridheight
    public static GridBagConstraints customConstraint(int gridx, int gridy, double weightx, double weighty) {
        GridBagConstraints constraints = customConstraint(gridx,gridy);
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        return constraints;
    }

    //EFFECTS: returns a GridBagConstraints with default values except for given gridx, gridy and inset
    public static GridBagConstraints customConstraint(int gridx, int gridy, Insets insets) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.insets = insets;
        return constraints;
    }
}
