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
 * Demonstrates the basic FormLayout sizes: constant, minimum, preferred.
 *
 * @author	Karsten Lentzsch
 */
public final class BasicSizesExample {

    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Basic Sizes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new BasicSizesExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add("Horizontal", buildHorizontalSizesPanel());
        tabbedPane.add("Vertical",   buildVerticalSizesPanel());
        return tabbedPane;
    }
    
    
    private JComponent buildHorizontalSizesPanel() {
        FormLayout layout = new FormLayout(
            "pref, 12px, " + "75px, 25px, min, 25px, pref",   
            "pref, 12px, pref"); 
            
        // Create a panel that uses the layout.
        JPanel panel = new JPanel(layout);

        // Set a default border.
        panel.setBorder(Borders.DIALOG_BORDER);
        
        // Create a reusable CellConstraints instance.
        CellConstraints cc = new CellConstraints();

        // Add components to the panel.
        panel.add(new JLabel("75px"),  cc.xy(3, 1));
        panel.add(new JLabel("Min"),   cc.xy(5, 1));
        panel.add(new JLabel("Pref"),  cc.xy(7, 1));
        
        panel.add(new JLabel("new JTextField(15)"),  cc.xy(1, 3));

        panel.add(new JTextField(15),  cc.xy(3, 3));
        panel.add(new JTextField(15),  cc.xy(5, 3));
        panel.add(new JTextField(15),  cc.xy(7, 3));

        return panel;
    }
    
    
    private JComponent buildVerticalSizesPanel() {
        FormLayout layout = new FormLayout(
            "pref, 12px, pref",   
            "pref, 12px, 45px, 12px, min, 12px, pref"); 
            
        // Create a panel that uses the layout.
        JPanel panel = new JPanel(layout);

        // Set a default border.
        panel.setBorder(Borders.DIALOG_BORDER);
        
        // Create a reusable CellConstraints instance.
        CellConstraints cc = new CellConstraints();

        // Add components to the panel.
        panel.add(new JLabel("new JTextArea(10, 40)"), cc.xy(3, 1));
        
        panel.add(new JLabel("45px"),     cc.xy(1, 3));
        panel.add(new JLabel("Min"),      cc.xy(1, 5));
        panel.add(new JLabel("Pref"),     cc.xy(1, 7));
        
        panel.add(createTextArea(10, 40), cc.xy(3, 3));
        panel.add(createTextArea(10, 40), cc.xy(3, 5));
        panel.add(createTextArea(10, 40), cc.xy(3, 7));

        return panel;
    }
    
    private JComponent createTextArea(int rows, int cols) {
        return new JScrollPane(new JTextArea(rows, cols),
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    
    // Helper Classes *********************************************************
    
    /**
     * Creates and answers a button that can have predefined minimum 
     * and preferred sizes. In the constructor you can specify or omit
     * the minimum width, height and preferred width/height.
     */
//    private static class SpecialSizeButton extends JButton {
//        
//        private final Dimension fixedMinimumSize;
//        private final Dimension fixedPreferredSize;
//        
//        private SpecialSizeButton(
//            String text,
//            Dimension fixedMinimumSize,
//            Dimension fixedPreferredSize) {
//            super(text);
//            this.fixedMinimumSize   = fixedMinimumSize;
//            this.fixedPreferredSize = fixedPreferredSize;
//            //putClientProperty("jgoodies.isNarrow", Boolean.TRUE);
//        }
//        
//        public Dimension getMinimumSize() {
//            Dimension d = super.getMinimumSize();
//            return new Dimension(
//                fixedMinimumSize.width  == -1 
//                    ? d.width 
//                    : fixedMinimumSize.width,
//                fixedMinimumSize.height == -1
//                    ? d.height
//                    : fixedMinimumSize.height);
//        }
//
//        public Dimension getPreferredSize() {
//            Dimension d = super.getPreferredSize();
//            return new Dimension(
//                fixedPreferredSize.width  == -1 
//                    ? d.width 
//                    : fixedPreferredSize.width,
//                fixedPreferredSize.height == -1
//                    ? d.height
//                    : fixedPreferredSize.height);
//        }
//    }
    
    
}

