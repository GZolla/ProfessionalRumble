package ui;

import java.awt.*;

import static java.awt.GridBagConstraints.*;

public enum DefaultBagConstraints {
    HEADER_BANNER(new GridBagConstraints(
            0, 0,REMAINDER,1,0,0,WEST,NONE,new Insets(0, 0, 0, 0), 0,0
    ));

    private GridBagConstraints constraint;

    DefaultBagConstraints(GridBagConstraints gridBagConstraints) {
        this.constraint = gridBagConstraints;
    }

    public GridBagConstraints getConstraint() {
        return constraint;
    }
}
