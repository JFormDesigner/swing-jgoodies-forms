/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.tutorial.building;

import java.awt.Component;

import javax.swing.*;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates a <i>pure</i> use of the <code>FormLayout</code>.
 * Columns and rows are specified before the panel is filled with
 * components. And the panel is filled without a builder. 
 * <p>
 * This panel building style is simple but not recommended. Other panel
 * building styles use a builder to fill the panel and/or create
 * form rows dynamically. See the {@link ShaftEditor} for a slightly better
 * panel building style. 
 *
 * @author Karsten Lentzsch
 * @see	ShaftEditor
 * @see	SegmentEditor
 * @see	FlangeEditor
 */

public final class DescriptionEditor {

    private JTextField companyNameField;
    private JTextField contactPersonField;
    private JTextField orderNoField;
    private JTextField inspectorField;
    private JTextField referenceNoField;
    private JComboBox  approvalStatusComboBox;
    private JTextField shipYardField;
    private JTextField registerNoField;
    private JTextField hullNumbersField;
    private JComboBox  projectTypeComboBox;

 
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Building:: Plain");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new ShaftEditor().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }
    

    // Component Creation and Initialization **********************************

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        companyNameField       = new JTextField();
        contactPersonField     = new JTextField();
        orderNoField           = new JTextField();
        inspectorField         = new JTextField();
        referenceNoField       = new JTextField();
        approvalStatusComboBox = buildApprovalStatusComboBox();
        shipYardField          = new JTextField();
        registerNoField        = new JTextField();
        hullNumbersField       = new JTextField();
        projectTypeComboBox    = buildProjectTypeComboBox();
    }

    /**
     * Creates and returns a combo box for the approval states.
     */
    private JComboBox buildApprovalStatusComboBox() {
        return new JComboBox(
            new String[] { "In Progress", "Finished", "Released" });
    }

    /**
     * Creates and returns a combo box for the project types.
     */
    private JComboBox buildProjectTypeComboBox() {
        return new JComboBox(
            new String[] { "New Building", "Conversion", "Repair" });
    }


    // Building *************************************************************

    /**
     * Builds the pane.
     */
    public JComponent buildPanel() {
        initComponents();

        FormLayout layout = new FormLayout(
                "right:max(40dlu;pref), 3dlu, 70dlu, 7dlu, "
              + "right:max(40dlu;pref), 3dlu, 70dlu",
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " +
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, " +
                "p, 3dlu, p, 3dlu, p, 3dlu, p");
                
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);

        // Fill the table with labels and components.
        CellConstraints cc = new CellConstraints();
        panel.add(createSeparator("Manufacturer"),  cc.xywh(1,  1, 7, 1));
        panel.add(new JLabel("Company"),            cc.xy  (1,  3));
        panel.add(companyNameField,                 cc.xywh(3,  3, 5, 1));
        panel.add(new JLabel("Contact"),            cc.xy  (1,  5));
        panel.add(contactPersonField,               cc.xywh(3,  5, 5, 1));
        panel.add(new JLabel("Order No"),           cc.xy  (1, 7));
        panel.add(orderNoField,                     cc.xy  (3, 7));

        panel.add(createSeparator("Inspector"),     cc.xywh(1, 9, 7, 1));
        panel.add(new JLabel("Name"),               cc.xy  (1, 11));
        panel.add(inspectorField,                   cc.xywh(3, 11, 5, 1));
        panel.add(new JLabel("Reference No"),       cc.xy  (1, 13));
        panel.add(referenceNoField,                 cc.xy  (3, 13));
        panel.add(new JLabel("Status"),             cc.xy  (1, 15));
        panel.add(approvalStatusComboBox,           cc.xy  (3, 15));
        
        panel.add(createSeparator("Ship"),          cc.xywh(1, 17, 7, 1));
        panel.add(new JLabel("Shipyard"),           cc.xy  (1, 19));
        panel.add(shipYardField,                    cc.xywh(3, 19, 5, 1));
        panel.add(new JLabel("Register No"),        cc.xy  (1, 21));
        panel.add(registerNoField,                  cc.xy  (3, 21));
        panel.add(new JLabel("Hull No"),            cc.xy  (5, 21));
        panel.add(hullNumbersField,                 cc.xy  (7, 21));
        panel.add(new JLabel("Project Type"),       cc.xy  (1, 23));
        panel.add(projectTypeComboBox,              cc.xy  (3, 23));
        
        return panel;
    }
    
    /**
     * Creates and answer a separator with a label in the left hand side.
     * 
     * @param text   the label's text
     * @return a separator with label in the left hand side
     */
    private Component createSeparator(String text) {
        return DefaultComponentFactory.getInstance().createSeparator(text);
    }

}