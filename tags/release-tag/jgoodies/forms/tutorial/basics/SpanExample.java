/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */
 
package com.jgoodies.forms.tutorial.basics;

import java.awt.Component;

import javax.swing.*;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how components can span multiple columns and rows.
 *
 * @author	Karsten Lentzsch
 */
public final class SpanExample {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Span");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new SpanExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add("Column Span", buildColumnSpanExample());
        tabbedPane.add("Row Span",    buildRowSpanExample());
        return tabbedPane;
    }
    
    
    private JComponent buildColumnSpanExample() {
        FormLayout layout = new FormLayout(
            "pref, 8px, 100px, 4px, 200px",   
            "pref, 6px, pref, 6px, pref, 6px, pref"); 
            
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();

        panel.add(new JLabel("Name:"),     cc.xy  (1, 1));
        panel.add(new JTextField(),        cc.xywh(3, 1, 3, 1));
        
        panel.add(new JLabel("Phone:"),    cc.xy  (1, 3));
        panel.add(new JTextField(),        cc.xywh(3, 3, 3, 1));
        
        panel.add(new JLabel("ZIP/City:"), cc.xy  (1, 5));
        panel.add(new JTextField(),        cc.xy  (3, 5));
        panel.add(new JTextField(),        cc.xy  (5, 5));
        
        panel.add(new JLabel("Country:"),  cc.xy  (1, 7));
        panel.add(new JTextField(),        cc.xywh(3, 7, 3, 1));
        
        return panel;
    }
    
    
    private JComponent buildRowSpanExample() {
        FormLayout layout = new FormLayout(
            "200px, 25px, 200px",   
            "pref, 2px, pref, 9px, " +
            "pref, 2px, pref, 9px, " +
            "pref, 2px, pref"); 
            
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();

        Component addressArea = new JScrollPane(new JTextArea());

        panel.add(new JLabel("Name"),     cc.xy  (1,  1));
        panel.add(new JTextField(),       cc.xy  (1,  3));
        
        panel.add(new JLabel("Phone"),    cc.xy  (1,  5));
        panel.add(new JTextField(),       cc.xy  (1,  7));
        
        panel.add(new JLabel("Fax"),      cc.xy  (1,  9));
        panel.add(new JTextField(),       cc.xy  (1, 11));
        
        panel.add(new JLabel("Notes"),    cc.xy  (3, 1));
        panel.add(addressArea,            cc.xywh(3, 3, 1, 9));
        
        return panel;
    }
    
    
}

