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
 * Demonstrates the different FormLayout alignments.
 *
 * @author	Karsten Lentzsch
 */
public final class AlignmentExample {

    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Alignments");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new AlignmentExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add("Horizontal", buildHorizontalButtons());
        tabbedPane.add("Vertical",   buildVerticalButtons());
        return tabbedPane;
    }
    
    
    private JComponent buildHorizontalButtons() {
        FormLayout layout = new FormLayout(
            "left:pref, 25px, center:pref, 25px, right:pref, 25px, fill:pref",   
            "pref, 12px, pref, 4px, pref, 4px, pref, 4px, pref, 4px, pref"); 
            
        // Create a panel that uses the layout.
        JPanel panel = new JPanel(layout);

        // Set a default border.
        panel.setBorder(Borders.DIALOG_BORDER);
        
        // Create a reusable CellConstraints instance.
        CellConstraints cc = new CellConstraints();

        // Add components to the panel.
        panel.add(new JLabel("Left"),     cc.xy(1,  1));
        panel.add(new JButton("Name"),    cc.xy(1,  3));
        panel.add(new JButton("Phone"),   cc.xy(1,  5));
        panel.add(new JButton("Fax"),     cc.xy(1,  7));
        panel.add(new JButton("Email"),   cc.xy(1,  9));
        panel.add(new JButton("Address"), cc.xy(1, 11));

        panel.add(new JLabel("Center"),   cc.xy(3,  1));
        panel.add(new JButton("Name"),    cc.xy(3,  3));
        panel.add(new JButton("Phone"),   cc.xy(3,  5));
        panel.add(new JButton("Fax"),     cc.xy(3,  7));
        panel.add(new JButton("Email"),   cc.xy(3,  9));
        panel.add(new JButton("Address"), cc.xy(3, 11));

        panel.add(new JLabel("Right"),    cc.xy(5,  1));
        panel.add(new JButton("Name"),    cc.xy(5,  3));
        panel.add(new JButton("Phone"),   cc.xy(5,  5));
        panel.add(new JButton("Fax"),     cc.xy(5,  7));
        panel.add(new JButton("Email"),   cc.xy(5,  9));
        panel.add(new JButton("Address"), cc.xy(5, 11));

        panel.add(new JLabel("Fill"),     cc.xy(7,  1));
        panel.add(new JButton("Name"),    cc.xy(7,  3));
        panel.add(new JButton("Phone"),   cc.xy(7,  5));
        panel.add(new JButton("Fax"),     cc.xy(7,  7));
        panel.add(new JButton("Email"),   cc.xy(7,  9));
        panel.add(new JButton("Address"), cc.xy(7, 11));

        return panel;
    }
    
    
    private JComponent buildVerticalButtons() {
        FormLayout layout = new FormLayout(
            "pref, 12px, pref, 6px, pref, 6px, pref",   
            "top:pref, 15px, center:pref, 15px, bottom:pref, 15px, fill:pref"); 
            
        // Create a panel that uses the layout.
        JPanel panel = new JPanel(layout);

        // Set a default border.
        panel.setBorder(Borders.DIALOG_BORDER);
        
        // Create a reusable CellConstraints instance.
        CellConstraints cc = new CellConstraints();

        // Add components to the panel.
        panel.add(new JLabel("Top"),      cc.xy(1,  1));
        panel.add(createSmallButton(),    cc.xy(3,  1));
        panel.add(createMediumButton(),   cc.xy(5,  1));
        panel.add(createLargeButton(),    cc.xy(7,  1));

        panel.add(new JLabel("Center"),   cc.xy(1,  3));
        panel.add(createSmallButton(),    cc.xy(3,  3));
        panel.add(createMediumButton(),   cc.xy(5,  3));
        panel.add(createLargeButton(),    cc.xy(7,  3));

        panel.add(new JLabel("Bottom"),   cc.xy(1,  5));
        panel.add(createSmallButton(),    cc.xy(3,  5));
        panel.add(createMediumButton(),   cc.xy(5,  5));
        panel.add(createLargeButton(),    cc.xy(7,  5));

        panel.add(new JLabel("Fill"),     cc.xy(1,  7));
        panel.add(createSmallButton(),    cc.xy(3,  7));
        panel.add(createMediumButton(),   cc.xy(5,  7));
        panel.add(createLargeButton(),    cc.xy(7,  7));

        return panel;
    }
    
    private JButton createSmallButton() {
        return new JButton("<html>One</html>");
    }
    
    private JButton createMediumButton() {
        return new JButton("<html>One<br>Two</html>");
    }
    
    private JButton createLargeButton() {
        return new JButton("<html>One<br>Two<br>Three</html>");
    }
    
    
}

