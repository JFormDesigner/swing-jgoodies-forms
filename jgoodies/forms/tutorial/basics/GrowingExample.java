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
 * Demonstrates the FormLayout growing options: none, default, weighted.
 *
 * @author	Karsten Lentzsch
 */
public final class GrowingExample {

    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Growing");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new GrowingExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add("Horizontal 1", buildHorizontalGrowing1Panel());
        tabbedPane.add("Horizontal 2", buildHorizontalGrowing2Panel());
        tabbedPane.add("Horizontal 3", buildHorizontalGrowing3Panel());
        tabbedPane.add("Horizontal 4", buildHorizontalGrowing4Panel());
        tabbedPane.add("Vertical 1",   buildVerticalGrowing1Panel());
        tabbedPane.add("Vertical 2",   buildVerticalGrowing2Panel());
        return tabbedPane;
    }
    
    
    private JComponent buildHorizontalGrowing1Panel() {
        FormLayout layout = new FormLayout(
            "pref, 6px, pref:grow",   
            "pref, 12px, pref"); 
            
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();

        panel.add(new JLabel("Fixed"),  cc.xy(1, 1));
        panel.add(new JLabel("Gets all extra space"),  cc.xy(3, 1));
        
        panel.add(new JTextField(5),   cc.xy(1, 3));
        panel.add(new JTextField(5),   cc.xy(3, 3));

        return panel;
    }
    
    
    private JComponent buildHorizontalGrowing2Panel() {
        FormLayout layout = new FormLayout(
            "pref, 6px, 0:grow, 6px, 0:grow",   
            "pref, 12px, pref"); 
            
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();

        panel.add(new JLabel("Fixed"),  cc.xy(1, 1));
        panel.add(new JLabel("Gets half of extra space"),  cc.xy(3, 1));
        panel.add(new JLabel("gets half of extra space"),  cc.xy(5, 1));
        
        panel.add(new JTextField(5),   cc.xy(1, 3));
        panel.add(new JTextField(5),   cc.xy(3, 3));
        panel.add(new JTextField(5),   cc.xy(5, 3));

        return panel;
    }
    
    
    private JComponent buildHorizontalGrowing3Panel() {
        FormLayout layout = new FormLayout(
            "pref, 6px, 0:grow(0.25), 6px, 0:grow(0.75)",   
            "pref, 12px, pref"); 
            
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();

        panel.add(new JLabel("Fixed"),       cc.xy(1, 1));
        panel.add(new JLabel("Gets 25% of extra space"),  cc.xy(3, 1));
        panel.add(new JLabel("Gets 75% of extra space"),  cc.xy(5, 1));
        
        panel.add(new JTextField(5),        cc.xy(1, 3));
        panel.add(new JTextField(5),        cc.xy(3, 3));
        panel.add(new JTextField(5),        cc.xy(5, 3));

        return panel;
    }
    
    
    private JComponent buildHorizontalGrowing4Panel() {
        FormLayout layout = new FormLayout(
            "pref:grow(0.33), 6px, pref:grow(0.67)",   
            "pref, 12px, pref"); 
            
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();

        panel.add(new JLabel("Gets 33% of the space"),    cc.xy(1, 1));
        panel.add(new JLabel("Gets 67% of the space"),    cc.xy(3, 1));
        
        panel.add(new JTextField(5),   cc.xy(1, 3));
        panel.add(new JTextField(5),   cc.xy(3, 3));

        return panel;
    }
    
    private JComponent buildVerticalGrowing1Panel() {
        FormLayout layout = new FormLayout(
            "pref, 12px, pref",   
            "pref, 6px, fill:0:grow(0.25), 6px, fill:0:grow(0.75)"); 
            
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();

        panel.add(new JLabel("Fixed"),                   cc.xy(1, 1));
        panel.add(new JLabel("Gets 25% of extra space"), cc.xy(1, 3));
        panel.add(new JLabel("Gets 75% of extra space"), cc.xy(1, 5));
        
        panel.add(createTextArea(4, 30), cc.xy(3, 1));
        panel.add(createTextArea(4, 30), cc.xy(3, 3));
        panel.add(createTextArea(4, 30), cc.xy(3, 5));

        return panel;
    }
    
    private JComponent buildVerticalGrowing2Panel() {
        FormLayout layout = new FormLayout(
            "pref, 12px, pref",   
            "fill:0:grow(0.25), 6px, fill:0:grow(0.75)"); 
            
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();

        panel.add(new JLabel("Gets 25% of extra space"), cc.xy(1, 1));
        panel.add(new JLabel("Gets 75% of extra space"), cc.xy(1, 3));
        
        panel.add(createTextArea(4, 30), cc.xy(3, 1));
        panel.add(createTextArea(4, 30), cc.xy(3, 3));

        return panel;
    }
    
    
    // Component Creation *****************************************************
    
    private JComponent createTextArea(int rows, int cols) {
        return new JScrollPane(new JTextArea(rows, cols),
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    
}
