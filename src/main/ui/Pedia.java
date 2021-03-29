package ui;

import model.data.Branch;
import model.data.NonVolatile;
import model.data.ProfessionalBase;
import model.moves.Damaging;
import model.moves.Move;
import ui.gui.BaseFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

import ui.gui.Menu;
import ui.gui.Style;

import static java.awt.GridBagConstraints.*;
import static ui.gui.CustomBagConstraints.customConstraint;

//A frame to display and explain the multiple branches, professionals, moves and statuses
public class Pedia extends BaseFrame {
    private final JPanel table;
    private final JTextField search;
    private final JButton prevPage;
    private final JButton nextPage;

    private final Font tableFont = new Font("sans serif",Font.PLAIN,45);

    private final int itemsPerPage = 25;
    private TableAble tableType;

    private String[] headers;
    private String[][] content;
    private int page;

    public Pedia(BaseFrame prev) {
        super("Profpedia", prev);
        setLayout(new GridBagLayout());

        buildHeaderAndReturn("Profpedia");

        Menu menu = new Menu(Style.getButtonStyle(50),0,true);
        menu.addButton("Branch",e -> changeTab(Branch.EDUCAT),0);
        menu.addButton("Professionals",e -> changeTab(ProfessionalBase.MOCKUMENTARY_BOSS),1);
        menu.addButton("Non Volatile Statuses",e -> changeTab(NonVolatile.DEMOR),2);
        menu.addButton("Moves",e -> changeTab(Damaging.BLACKOUT),3);

        GridBagConstraints constraints = customConstraint(0,1,2,2);
        constraints.anchor = NORTH;
        constraints.weighty = 1;
        add(menu,constraints);



        table = new JPanel(new GridBagLayout());
        search = new JTextField(100);
        prevPage = new JButton("<");
        nextPage = new JButton(">");

        buildTable();
        changeTab(Branch.EDUCAT);

        revalidate();
        repaint();
    }

    //MODIFIES: this
    //EFFECTS: build table container, search bar and navigation arrows
    public void buildTable() {

        GridBagConstraints constraints = customConstraint(2,2,4,1);
        constraints.fill = HORIZONTAL;
        constraints.anchor = NORTH;
        add(table, constraints);

        search.setFont(tableFont);
        constraints = customConstraint(2,1,1.0,0);
        constraints.fill = HORIZONTAL;
        add(search,constraints);

        prevPage.setFont(tableFont);
        prevPage.addActionListener(e -> changePage(-1));
        add(prevPage,customConstraint(4,1));
        nextPage.setFont(tableFont);
        nextPage.addActionListener(e -> changePage(1));
        add(nextPage,customConstraint(5,1));

        JButton ok = new JButton("SEARCH");
        ok.addActionListener(e -> changeTable());
        ok.setFont(tableFont);
        add(ok,customConstraint(3,1,new Insets(0,0,0,30)));


    }

    //MODIFIES: this
    //EFFECTS: Refreshes content and loads first page
    public void changeTable() {
        content = getContent();
        headers = tableType.getHeaders();
        page = 1;

        changePage(-1);

    }

    //MODIFIES: this
    //EFFECTS: clears search bar, change tableType to given TableAble and updates table
    public void changeTab(TableAble t) {
        search.setText("");
        this.tableType = t;

        changeTable();
    }


    //MODIFIES: this
    //EFFECTS: change page(by given change) and refresh display and enabled of navigation arrows
    public void changePage(int change) {
        page += change;
        updateTable();


        prevPage.setEnabled(page != 0);

        nextPage.setEnabled(page != content.length / itemsPerPage);

        repaint();
        revalidate();
    }

    //MODIFIES: this
    //EFFECTS: updates table content
    public void updateTable() {
        table.removeAll();

        for (int i = page * itemsPerPage; i < Math.min((page + 1) * itemsPerPage,content.length) + 1; i++) {
            String[] row = (i == page * itemsPerPage ? headers : content[i - 1]);
            for (int j = 0; j < headers.length; j++) {
                if (!(tableType instanceof ProfessionalBase && row[j].equals(""))) {
                    JLabel c = new JLabel(row[j]);
                    c.setFont(tableFont);

                    getStyle(i - page * itemsPerPage,j,row[j]).applyToComponent(c);
                    c.setOpaque(true);
                    GridBagConstraints constraints = customConstraint(j,i,1.0,0);
                    constraints.fill = HORIZONTAL;
                    if (tableType instanceof ProfessionalBase && j == 1 && row[2].equals("")) {
                        constraints.gridwidth = 2;
                    }
                    table.add(c,constraints);
                }
            }
        }
        table.revalidate();
        table.repaint();

    }

    //EFFECTS: filters content according to the search bar text and selected table Type
    public String[][] getContent() {
        String[][] values = tableType.toTable();
        ArrayList<String[]> result = new ArrayList<>();

        Pattern p = Pattern.compile("s?" + search.getText() + ".*",Pattern.CASE_INSENSITIVE);
        for (String[] value : values) {
            if (p.matcher(value[0]).matches()) {
                result.add(value);
            }
        }

        String[][] arrayRes = new String[result.size()][];
        return result.toArray(arrayRes);
    }

    //EFFECTS: Designates a colour based on the tableType class, the value, the row and the column numbers.
    public Style getStyle(int row, int column, String val) {
        Color color = Color.WHITE;
        boolean isPB = tableType instanceof ProfessionalBase;
        if (tableType instanceof Branch && !(row == 0 && column == 0)) {
            if (row == 0 || column == 0) {
                color = Branch.values()[(row == 0 ? column : row) - 1].getColor();
            } else {
                color = val.equals("1") ? new Color(100,150,200) :
                        (val.equals("2") ? new Color(60,200,60) :
                                (val.equals("0") ? Color.BLACK : new Color(200,60,60)));
            }
        } else if (row == 0) {
            color = Color.BLACK;
        } else if ((isPB && (column == 1 || column == 2)) || (tableType instanceof Move && column == 1)) {
            color = Branch.valueOf(val).getColor();
        } else if (tableType instanceof NonVolatile && column == 0) {
            color = NonVolatile.values()[row - 1].getColor();
        }

        return new Style(tableFont,color,BorderFactory.createEmptyBorder(4,6,4,6));
    }


}
