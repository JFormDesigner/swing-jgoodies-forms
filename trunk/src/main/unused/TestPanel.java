/*
 * Copyright (c) 2002-2013 JGoodies Software GmbH. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Software GmbH nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.forms.tutorial;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.Border;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Demonstrates how to configure the FormLayout and how to add components.
 *
 * @author	Karsten Lentzsch
 */
public final class TestPanel {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("FormLayout Demo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new TestPanel().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
        //System.out.println("Panel min  size=" + panel.getMinimumSize());
        //System.out.println("Panel pref size=" + panel.getPreferredSize());
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add(buildTest1Panel(),                   "Test");
        tabbedPane.add(buildHorizontalAlignmentTestPanel(), "Horizontal");
        tabbedPane.add(buildVerticalAlignmentTestPanel(),   "Vertical");
        return tabbedPane;
    }
    
    
    public JPanel buildTest1Panel() {
        Dimension small  = new Dimension( 60, 20);
        Dimension medium = new Dimension(120, 40);
        JButton button1  = createButton("min=60;pref=120", small, medium);
        JButton button2  = createButton("min=60;pref=120", small, medium);
        JButton button3  = createButton("min=60;pref=120", small, medium);
        
        
        FormLayout fl = new FormLayout(
                "l:p, 4dlu, l:min, 4dlu, l:pref, 4dlu, l:default, 4dlu, l:p:g",
                "p, 10px, p, 10px, p, 6dlu, p, 2dlu, p");
                
        fl.setColumnGroups(new int[][]{{7, 9}});
        
        JPanel panel = new JPanel(fl);
        panel.setBorder(createDefaultBorder());
        
        panel.add(new JLabel("min"),            "3, 1, center, center");
        panel.add(new JLabel("pref"),           "5, 1, center, center");
        panel.add(new JLabel("default"),        "7, 1, center, center");
        panel.add(new JLabel("pref:grow"),      "9, 1, center, center");
        
        panel.add(new JLabel ("1, 1"),          "1, 3");
        panel.add(new JButton("3, 1"),          "3, 3, right, top");
        panel.add(new JButton("3, 1"),          "5, 3, right, top");
        panel.add(new JButton("3, 1"),          "7, 3, right, top");
        panel.add(new JButton("5, 1"),          "9, 3");

        panel.add(new JLabel ("1, 3"),          "1, 5");
        panel.add(new JButton("3, 3 label"),    "3, 5, fill, fill");
        panel.add(new JButton("3, 3 label"),    "5, 5, fill, fill");
        panel.add(new JButton("3, 3 label"),    "7, 5, fill, fill");
        panel.add(new JButton("5, 3"),          "9, 5");
        
        panel.add(new JLabel("Long label"),     "1, 7");
        panel.add(button1,                      "3, 7");
        panel.add(button2,                      "5, 7");
        panel.add(button3,                      "7, 7");
        panel.add(new JButton("5, 5"),          "9, 7, fill, fill");
        
        panel.add(new JLabel("Colspan=2"),     "1, 9");
        panel.add(new JButton("3, 7, 3, 1"),   "3, 9, 3, 1");
        
        fl.removeColumn(4);
        fl.insertColumn(4, ColumnSpec.decode("20dlu"));
        fl.removeColumn(2);
        fl.insertColumn(2, ColumnSpec.decode("4dlu"));
        fl.removeRow(6);
        fl.removeRow(4);
        fl.insertRow(4, RowSpec.decode("5dlu:grow"));
        
        return panel;
    }
    
    
    public JComponent buildHorizontalAlignmentTestPanel() {
        FormLayout layout = new FormLayout(
                        "left:pref:g, center:pref:g, right:pref:g, pref:g",
                        "pref, 8dlu, pref, pref, pref, pref, pref");
        JPanel panel = new JPanel(layout);
        panel.setBorder(createDefaultBorder());
        
        panel.add(new JLabel("Left"),       "1, 1, c, c");
        panel.add(new JLabel("Center"),     "2, 1, c, c");
        panel.add(new JLabel("Right"),      "3, 1, c, c");
        panel.add(new JLabel("Default"),    "4, 1, c, c");
        
        int row = 3;
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

        row++;
        addHorizontalButton(panel, 1, row, CellConstraints.FILL);
        addHorizontalButton(panel, 2, row, CellConstraints.FILL);
        addHorizontalButton(panel, 3, row, CellConstraints.FILL);
        addHorizontalButton(panel, 4, row, CellConstraints.FILL);

        row++;
        addHorizontalButton(panel, 1, row, CellConstraints.DEFAULT);
        addHorizontalButton(panel, 2, row, CellConstraints.DEFAULT);
        addHorizontalButton(panel, 3, row, CellConstraints.DEFAULT);
        addHorizontalButton(panel, 4, row, CellConstraints.DEFAULT);

        return panel;
    }
    
    
    public JComponent buildVerticalAlignmentTestPanel() {
        FormLayout layout = new FormLayout(
                        "left:pref, 8dlu, l:p, l:p, l:p, l:p, l:p",
                        "top:pref:g, center:pref:g, bottom:pref:g, pref:g");
        JPanel panel = new JPanel(layout);
        panel.setBorder(createDefaultBorder());

        panel.add(new JLabel("Top"),        "1, 1, r, c");
        panel.add(new JLabel("Center"),     "1, 2, r, c");
        panel.add(new JLabel("Bottom"),     "1, 3, r, c");
        panel.add(new JLabel("Default"),    "1, 4, r, c");
        
        int col = 3;
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

        col++;
        addVerticalButton(panel, col, 1, CellConstraints.FILL);
        addVerticalButton(panel, col, 2, CellConstraints.FILL);
        addVerticalButton(panel, col, 3, CellConstraints.FILL);
        addVerticalButton(panel, col, 4, CellConstraints.FILL);

        col++;
        addVerticalButton(panel, col, 1, CellConstraints.DEFAULT);
        addVerticalButton(panel, col, 2, CellConstraints.DEFAULT);
        addVerticalButton(panel, col, 3, CellConstraints.DEFAULT);
        addVerticalButton(panel, col, 4, CellConstraints.DEFAULT);

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
    
    
    private JButton createButton(String text, Dimension minSize, Dimension prefSize) {
        JButton button = new JButton(text) {
            public Dimension getMinimumSize() {
                //System.out.println("#getMinimumSize requested from button" + getText());
                return super.getMinimumSize();
            }
            public Dimension getPreferredSize() {
                //System.out.println("#getPreferredSize requested from button" + getText());
                return super.getPreferredSize();
            }
        };
        button.setMinimumSize(minSize);
        button.setPreferredSize(prefSize);
        return button;
    }
    
    
    private Border createDefaultBorder() {
        return BorderFactory.createCompoundBorder(
                       Borders.DIALOG_BORDER,
                       BorderFactory.createLineBorder(Color.black));       
    }
    
    
}

