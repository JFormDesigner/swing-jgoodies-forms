/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.tutorial.building;

import javax.swing.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.debug.FormDebugUtils;
import com.jgoodies.forms.extras.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how to find bugs in the layout using 
 * the {@link FormDebugPanel} and the {@link FormDebugUtils}.
 * <p>
 * The example also demonstrates efficient panel building
 * with the DefaultFormBuilder. 
 * The builder has been configured to use a leading indent column.
 *
 * @author Karsten Lentzsch
 */

public final class FormDebugExample {
    

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Building :: Debug a Form");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new ShaftEditor().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    // Building *************************************************************

    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add(buildEditor(new JPanel()),         "Plain");
        tabbedPane.add(buildEditor(new FormDebugPanel()), "Debug");
        return tabbedPane;
    }
    
    /**
     * Builds the pane.
     */
    private JComponent buildEditor(JPanel panel) {
        FormLayout layout = new FormLayout(
                "12dlu, pref, 3dlu, max(45dlu;min), 2dlu, min, 2dlu, min, 2dlu, min, ",
                "");
        layout.setColumnGroups(new int[][] { { 4, 6, 8, 10 } });
        
        DefaultFormBuilder builder = new DefaultFormBuilder(panel, layout);
        builder.setDefaultDialogBorder();
        builder.setLeadingColumnOffset(1);

        builder.appendSeparator("General");
        builder.append("File Number",    new JTextField(), 7);
        builder.append("RFQ Number",     new JTextField(), 7);
        builder.append("BL/MBL",         new JTextField(), new JTextField()); builder.nextLine();

        builder.appendSeparator("Addresses");
        builder.append("Customer",       new JTextField(), new JTextField(), 5);
        builder.append("Shipper",        new JTextField(), new JTextField(), 5);
        builder.append("Consignee",      new JTextField(), new JTextField(), 5);

        builder.appendSeparator("Transport");
        builder.append("Departure",      new JTextField(),  new JTextField(), 5);
        builder.append("Destination",    new JTextField(),  new JTextField(), 5);
        builder.append("Delivery Date",  new JTextField()); builder.nextLine();
        
        if (panel instanceof FormDebugPanel)
            FormDebugUtils.dumpAll(panel);
        
        return builder.getPanel();
    }


}