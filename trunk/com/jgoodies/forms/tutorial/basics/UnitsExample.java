/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */
 
package com.jgoodies.forms.tutorial.basics;

import javax.swing.*;

import com.jgoodies.forms.extras.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the different sizing units provided by the FormLayout:
 * Pixel, Dialog Units (dlu), Point, Millimeter, Centimeter and Inches. 
 * Pt, mm, cm, in honor the screen resolution; dlus honor the font size too.
 * 
 * @author	Karsten Lentzsch
 */
public final class UnitsExample {

    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Units");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new UnitsExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add("Horizontal",     buildHorizontalUnitsPanel());
        tabbedPane.add("Horizontal Dlu", buildHorizontalDluPanel());
        tabbedPane.add("Vertical",       buildVerticalUnitsPanel());
        tabbedPane.add("Vertical Dlu",   buildVerticalDluPanel());
        return tabbedPane;
    }
    
    
    private JComponent buildHorizontalUnitsPanel() {
        FormLayout layout = new FormLayout(
            "right:pref, 1dlu, left:pref, 4dlu, left:pref",   
            ""); 
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();

        builder.append("72",   new JLabel("pt"), buildHorizontalPanel("72pt"));
        builder.append("25.4", new JLabel("mm"), buildHorizontalPanel("25.4mm"));
        builder.append("2.54", new JLabel("cm"), buildHorizontalPanel("2.54cm"));
        builder.append("1",    new JLabel("in"), buildHorizontalPanel("1in"));
        builder.append("72",   new JLabel("px"), buildHorizontalPanel("72px"));
        builder.append("96",   new JLabel("px"), buildHorizontalPanel("96px"));
        builder.append("120",  new JLabel("px"), buildHorizontalPanel("120px"));
        
        return builder.getPanel();
    }
    
    
    private JComponent buildHorizontalDluPanel() {
        FormLayout layout = new FormLayout(
            "right:pref, 1dlu, left:pref, 4dlu, left:pref",   
            ""); 
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();

        builder.append("9",  new JLabel("cols"), buildHorizontalPanel(9));
        builder.append("50", new JLabel("dlu"),  buildHorizontalPanel("50dlu"));
        builder.append("75", new JLabel("px"),   buildHorizontalPanel("75px"));
        builder.append("88", new JLabel("px"),   buildHorizontalPanel("88px"));
        builder.append("100",new JLabel("px"),   buildHorizontalPanel("100px"));
        
        return builder.getPanel();
    }
    
    
    private JComponent buildVerticalUnitsPanel() {
        FormLayout layout = new FormLayout(
            "c:p, 4dlu, c:p, 4dlu, c:p, 4dlu, c:p, 4dlu, c:p, 4dlu, c:p, 4dlu, c:p",   
            "pref, 6dlu, top:pref"); 
            
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();

        panel.add(new JLabel("72 pt"),            cc.xy(1, 1));
        panel.add(buildVerticalPanel("72pt"),     cc.xy(1, 3));
        
        panel.add(new JLabel("25.4 mm"),          cc.xy(3, 1));
        panel.add(buildVerticalPanel("25.4mm"),   cc.xy(3, 3));
        
        panel.add(new JLabel("2.54 cm"),          cc.xy(5, 1));
        panel.add(buildVerticalPanel("2.54cm"),   cc.xy(5, 3));
        
        panel.add(new JLabel("1 in"),             cc.xy(7, 1));
        panel.add(buildVerticalPanel("1in"),      cc.xy(7, 3));

        panel.add(new JLabel("72 px"),            cc.xy(9, 1));
        panel.add(buildVerticalPanel("72px"),     cc.xy(9, 3));
        
        panel.add(new JLabel("96 px"),           cc.xy(11, 1));
        panel.add(buildVerticalPanel("96px"),    cc.xy(11, 3));
        
        panel.add(new JLabel("120 px"),           cc.xy(13, 1));
        panel.add(buildVerticalPanel("120px"),    cc.xy(13, 3));
        
        return panel;
    }
    
    private JComponent buildVerticalDluPanel() {
        FormLayout layout = new FormLayout(
            "c:p, 4dlu, c:p, 4dlu, c:p, 4dlu, c:p, 4dlu, c:p, 4dlu, c:p, 4dlu, c:p",   
            "pref, 6dlu, top:pref, 25dlu, pref, 6dlu, top:pref"); 
            
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        CellConstraints cc = new CellConstraints();

        panel.add(new JLabel("4 rows"),           cc.xy(1, 1));
        panel.add(buildVerticalPanel("pref", 4),  cc.xy(1, 3));
        
        panel.add(new JLabel("42 dlu"),           cc.xy(3, 1));
        panel.add(buildVerticalPanel("42dlu", 4), cc.xy(3, 3));
        
        panel.add(new JLabel("57 px"),            cc.xy(5, 1));
        panel.add(buildVerticalPanel("57px", 4),  cc.xy(5, 3));
        
        panel.add(new JLabel("63 px"),            cc.xy(7, 1));
        panel.add(buildVerticalPanel("63px", 4),  cc.xy(7, 3));
        
        panel.add(new JLabel("68 px"),            cc.xy(9, 1));
        panel.add(buildVerticalPanel("68px", 4),  cc.xy(9, 3));
        

        panel.add(new JLabel("field"),            cc.xy(1, 5));
        panel.add(new JTextField(4),              cc.xy(1, 7));
        
        panel.add(new JLabel("14 dlu"),           cc.xy(3, 5));
        panel.add(buildVerticalPanel("14dlu"),    cc.xy(3, 7));
        
        panel.add(new JLabel("21 px"),            cc.xy(5, 5));
        panel.add(buildVerticalPanel("21px"),     cc.xy(5, 7));
        
        panel.add(new JLabel("23 px"),            cc.xy(7, 5));
        panel.add(buildVerticalPanel("23px"),     cc.xy(7, 7));
        
        panel.add(new JLabel("24 px"),            cc.xy(9, 5));
        panel.add(buildVerticalPanel("24px"),     cc.xy(9, 7));
        
        panel.add(new JLabel("button"),           cc.xy(11, 5));
        panel.add(new JButton("..."),             cc.xy(11, 7));
        
        return panel;
    }
    
    
    
    // Component Creation *****************************************************
    
    private JComponent createTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        //area.setText(text);
        return new JScrollPane(area,
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    // Helper Code ************************************************************
    
    private JComponent buildHorizontalPanel(String width) {
        FormLayout layout = new FormLayout(width, "pref");
        JPanel panel = new JPanel(layout);
        panel.add(new JTextField(), new CellConstraints(1, 1));
        return panel;
    }
    
    private JComponent buildHorizontalPanel(int columns) {
        FormLayout layout = new FormLayout("pref, 4dlu, pref", "pref");
        JPanel panel = new JPanel(layout);
        CellConstraints cc = new CellConstraints();
        panel.add(new JTextField(columns),                
                  cc.xy(1, 1));
        panel.add(new JLabel("Width of new JTextField(" + columns + ")"), 
                  cc.xy(3, 1));
        return panel;
    }
    
    private JComponent buildVerticalPanel(String height) {
        return buildVerticalPanel(height, 10);
    }
    
    private JComponent buildVerticalPanel(String height, int rows) {
        FormLayout layout = new FormLayout("pref", "fill:"+ height);
        JPanel panel = new JPanel(layout);
        CellConstraints cc = new CellConstraints();
        panel.add(createTextArea(rows, 5), cc.xy(1, 1));
        return panel;
    }
    
    
}
