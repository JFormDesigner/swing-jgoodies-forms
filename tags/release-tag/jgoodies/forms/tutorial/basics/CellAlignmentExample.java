/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */
 
package com.jgoodies.forms.tutorial.basics;

import javax.swing.*;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how FormLayout applies the default column and row
 * alignments to cells, and how to override the defaults.
 *
 * @author	Karsten Lentzsch
 */
public final class CellAlignmentExample {

    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Cell Alignments");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new CellAlignmentExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add(buildHorizontalPanel(), "Horizontal");
        tabbedPane.add(buildVerticalPanel(),   "Vertical");
        return tabbedPane;
    }
    
    
    private JComponent buildHorizontalPanel() {
        FormLayout layout = new FormLayout(
                        "left:pref:g, center:pref:g, right:pref:g, pref:g",
                        "pref, 8dlu, pref, pref, pref, pref, pref");
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        
        panel.add(new JLabel("Left"),       "1, 1, c, c");
        panel.add(new JLabel("Center"),     "2, 1, c, c");
        panel.add(new JLabel("Right"),      "3, 1, c, c");
        panel.add(new JLabel("Default"),    "4, 1, c, c");
        
        int row = 3;
        addHorizontalButton(panel, 1, row, CellConstraints.DEFAULT);
        addHorizontalButton(panel, 2, row, CellConstraints.DEFAULT);
        addHorizontalButton(panel, 3, row, CellConstraints.DEFAULT);
        addHorizontalButton(panel, 4, row, CellConstraints.DEFAULT);

        row++;
        addHorizontalButton(panel, 1, row, CellConstraints.FILL);
        addHorizontalButton(panel, 2, row, CellConstraints.FILL);
        addHorizontalButton(panel, 3, row, CellConstraints.FILL);
        addHorizontalButton(panel, 4, row, CellConstraints.FILL);

        row++;
        addHorizontalButton(panel, 1, row, CellConstraints.LEFT);
        addHorizontalButton(panel, 2, row, CellConstraints.LEFT);
        addHorizontalButton(panel, 3, row, CellConstraints.LEFT);
        addHorizontalButton(panel, 4, row, CellConstraints.LEFT);

        row++;
        addHorizontalButton(panel, 1, row, CellConstraints.CENTER);
        addHorizontalButton(panel, 2, row, CellConstraints.CENTER);
        addHorizontalButton(panel, 3, row, CellConstraints.CENTER);
        addHorizontalButton(panel, 4, row, CellConstraints.CENTER);

        row++;
        addHorizontalButton(panel, 1, row, CellConstraints.RIGHT);
        addHorizontalButton(panel, 2, row, CellConstraints.RIGHT);
        addHorizontalButton(panel, 3, row, CellConstraints.RIGHT);
        addHorizontalButton(panel, 4, row, CellConstraints.RIGHT);

        return panel;
    }
    
    
    private JComponent buildVerticalPanel() {
        FormLayout layout = new FormLayout(
                        "left:pref, 8dlu, l:p, l:p, l:p, l:p, l:p",
                        "top:pref:g, center:pref:g, bottom:pref:g, pref:g");
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);

        panel.add(new JLabel("Top"),        "1, 1, r, c");
        panel.add(new JLabel("Center"),     "1, 2, r, c");
        panel.add(new JLabel("Bottom"),     "1, 3, r, c");
        panel.add(new JLabel("Default"),    "1, 4, r, c");
        
        int col = 3;
        addVerticalButton(panel, col, 1, CellConstraints.DEFAULT);
        addVerticalButton(panel, col, 2, CellConstraints.DEFAULT);
        addVerticalButton(panel, col, 3, CellConstraints.DEFAULT);
        addVerticalButton(panel, col, 4, CellConstraints.DEFAULT);

        col++;
        addVerticalButton(panel, col, 1, CellConstraints.FILL);
        addVerticalButton(panel, col, 2, CellConstraints.FILL);
        addVerticalButton(panel, col, 3, CellConstraints.FILL);
        addVerticalButton(panel, col, 4, CellConstraints.FILL);

        col++;
        addVerticalButton(panel, col, 1, CellConstraints.TOP);
        addVerticalButton(panel, col, 2, CellConstraints.TOP);
        addVerticalButton(panel, col, 3, CellConstraints.TOP);
        addVerticalButton(panel, col, 4, CellConstraints.TOP);

        col++;
        addVerticalButton(panel, col, 1, CellConstraints.CENTER);
        addVerticalButton(panel, col, 2, CellConstraints.CENTER);
        addVerticalButton(panel, col, 3, CellConstraints.CENTER);
        addVerticalButton(panel, col, 4, CellConstraints.CENTER);

        col++;
        addVerticalButton(panel, col, 1, CellConstraints.BOTTOM);
        addVerticalButton(panel, col, 2, CellConstraints.BOTTOM);
        addVerticalButton(panel, col, 3, CellConstraints.BOTTOM);
        addVerticalButton(panel, col, 4, CellConstraints.BOTTOM);

        return panel;
    }
    
    
    private void addHorizontalButton(JPanel panel, int col, int row, 
                                    CellConstraints.Alignment hAlignment) {
        JButton button = new JButton(hAlignment.toString());
        panel.add(button, new CellConstraints(col, row, 
                                              hAlignment, 
                                              CellConstraints.DEFAULT));
    }
    
    
    private void addVerticalButton(JPanel panel, int col, int row, 
                                    CellConstraints.Alignment vAlignment) {
        JButton button = new JButton(vAlignment.toString());
        panel.add(button, new CellConstraints(col, row, 
                                              CellConstraints.DEFAULT,
                                              vAlignment));
    }
    
    
}

