/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */
 
package com.jgoodies.forms.tutorial.building;

import java.awt.Component;

import javax.swing.*;

import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the use of Factories as provided by the Forms framework.
 *
 * @author	Karsten Lentzsch
 * @see	ButtonBarFactory
 * @see	WizardBarFactory
 */
public final class ButtonStacksExample {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Demo :: Button Bars");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new ButtonStacksExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add(buildButtonStackNoBuilder(),    "No Builder");
        tabbedPane.add(buildButtonStackWithBuilder(),  "With Builder");
        tabbedPane.add(buildButtonStackRelated(),      "Gaps - Related");
        tabbedPane.add(buildButtonStackUnrelated(),    "Gaps - Unrelated ");
        tabbedPane.add(buildButtonStackMixedDefault(), "Mix Default");
        tabbedPane.add(buildButtonStackMixedNarrow(),  "Mix Narrow");
        return tabbedPane;
    }
    
    private Component buildButtonStackNoBuilder() {
        JPanel buttonStack = new JPanel(
            new FormLayout("p", "p, 4px, p"));
        buttonStack.add(new JButton("Yes"), "1, 1");                      
        buttonStack.add(new JButton("No"),  "1, 3");   
        
        return wrap(buttonStack, 
            "\nThis stack has been built without a ButtonStackBuilder.\n" +
            " o The buttons have no minimum widths and\n" +
            " o gaps may be inconsistent between team members.");
    }

    private Component buildButtonStackWithBuilder() {
        ButtonStackBuilder builder = new ButtonStackBuilder();
        builder.addGridded(new JButton("Yes"));                      
        builder.addRelatedGap();                   
        builder.addGridded(new JButton("No"));   
        return wrap(builder.getPanel(),
            "\nThis stack has been built with a ButtonStackBuilder.\n" +
            " o The buttons have a minimum widths and\n" +
            " o the button gap is a logical size that follows a style guide.");
    }
    
    private Component buildButtonStackRelated() {
        ButtonStackBuilder builder = new ButtonStackBuilder();
        builder.addGridded(new JButton("Related"));   
        builder.addRelatedGap();                   
        builder.addGridded(new JButton("Related"));   
        builder.addRelatedGap();                   
        builder.addGridded(new JButton("Related"));   

        return wrap(builder.getPanel(),
            "\nThis stack uses the logical gap for related buttons.\n");
    }
    
    private Component buildButtonStackUnrelated() {
        ButtonStackBuilder builder = new ButtonStackBuilder();
        builder.addGridded(new JButton("Unrelated"));   
        builder.addUnrelatedGap();                   
        builder.addGridded(new JButton("Unrelated"));   
        builder.addUnrelatedGap();                   
        builder.addGridded(new JButton("Unrelated"));   

        return wrap(builder.getPanel(),
            "\nThis stack uses the logical gap for unrelated buttons.\n");
    }
    
    private Component buildButtonStackMixedDefault() {
        ButtonStackBuilder builder = new ButtonStackBuilder();
        builder.addGridded(new JButton("OK"));   
        builder.addRelatedGap();                   
        builder.addGridded(new JButton("Cancel"));   
        builder.addUnrelatedGap();
        builder.addGridded(new JButton("Help"));
        builder.addUnrelatedGap();
        builder.addGlue();
        builder.addFixed(new JButton("Copy to Clipboard"));

        return wrap(builder.getPanel(),
            "\nDemonstrates a glue (between Help and Copy),\n" +
            "has related and unrelated buttons and\n" +
            "a button with long label with the default margin.");
    }
    
    private Component buildButtonStackMixedNarrow() {
        ButtonStackBuilder builder = new ButtonStackBuilder();
        builder.addGridded(new JButton("OK"));   
        builder.addRelatedGap();                   
        builder.addGridded(new JButton("Cancel"));   
        builder.addUnrelatedGap();
        builder.addGridded(new JButton("Help"));
        builder.addUnrelatedGap();
        builder.addGlue();
        builder.addGriddedNarrow(new JButton("Copy to Clipboard"));

        return wrap(builder.getPanel(),
            "\nDemonstrates a glue (between Help and Copy),\n" +
            "has related and unrelated buttons and\n" +
            "a button with long label with a narrow margin.");
    }
    
    
    // Helper Code ************************************************************
    
    private Component wrap(Component buttonStack, String text) {
        Component textPane = new JScrollPane(new JTextArea(text));
        
        FormLayout layout = new FormLayout(
                        "fill:p:grow, 6dlu, p",
                        "fill:p:grow");
        JPanel panel = new JPanel(layout);
        CellConstraints cc = new CellConstraints();
        panel.setBorder(Borders.DIALOG_BORDER);
        panel.add(textPane,     cc.xy(1, 1));
        panel.add(buttonStack,  cc.xy(3, 1));                   
        return panel;
    }

    
}

