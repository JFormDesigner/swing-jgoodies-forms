/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.  
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.tutorial.building;

import javax.swing.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Combines the <code>FormLayout</code> with the <code>PanelBuilder</code>. 
 * Columns and rows are specified before the panel is filled with components. 
 * The builder's cursor is used to determine the location of the next component. 
 * And the builder's convenience methods are used to add labels and separators.
 * <p> 
 * This panel building style is intended for learning purposes only.
 * The recommended style is demonstrated in the {@link FlangeEditor}. 
 *
 * @author Karsten Lentzsch
 * @see	DescriptionEditor
 * @see	ShaftEditor
 * @see	FlangeEditor
 */

public final class SegmentEditor {

    private JTextField identifierField;
    private JTextField ptiField;
    private JTextField powerField;
    private JTextField lenField;
    private JTextField daField;
    private JTextField diField;
    private JTextField da2Field;
    private JTextField di2Field;
    private JTextField rField;
    private JTextField dField;
    private JComboBox  locationCombo;
    private JTextField kFactorField;
    private JCheckBox  holesCheckBox;
    private JCheckBox  slotsCheckBox;


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Building:: Dynamic Rows");
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
        identifierField = new JTextField();
        ptiField        = new JTextField();
        powerField      = new JTextField();
        lenField        = new JTextField();
        daField         = new JTextField();
        diField         = new JTextField();
        da2Field        = new JTextField();
        di2Field        = new JTextField();
        rField          = new JTextField();
        dField          = new JTextField();
        locationCombo   = createLocationComboBox();
        kFactorField    = new JTextField();
        holesCheckBox   = new JCheckBox("Has radial holes", true);
        slotsCheckBox   = new JCheckBox("Has longitudinal slots");
    }

    /**
     * Creates and answers a combo box for the locations.
     */
    private JComboBox createLocationComboBox() {
        return new JComboBox(
            new String[] {
                "Propeller nut thread",
                "Stern tube front area",
                "Shaft taper" });
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
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
              + "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
              + "p, 3dlu, p, 3dlu, p, 3dlu, p");

        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        builder.addSeparator("Segment");
        builder.nextLine(2);

        builder.addLabel("Identifier");         builder.nextColumn(2);
        builder.add(identifierField);
        builder.nextLine(2);

        builder.addLabel("PTI [kW]");           builder.nextColumn(2);
        builder.add(ptiField);                  builder.nextColumn(2);
        builder.addLabel("Power [kW]");         builder.nextColumn(2);
        builder.add(powerField);
        builder.nextLine(2);

        builder.addLabel("len [mm]");           builder.nextColumn(2);
        builder.add(lenField);
        builder.nextLine(2);

        builder.addSeparator("Diameters");
        builder.nextLine(2);

        builder.addLabel("da [mm]");            builder.nextColumn(2);
        builder.add(daField);                   builder.nextColumn(2);
        builder.addLabel("di [mm]");            builder.nextColumn(2);
        builder.add(diField);
        builder.nextLine(2);

        builder.addLabel("da2 [mm]");           builder.nextColumn(2);
        builder.add(da2Field);                  builder.nextColumn(2);
        builder.addLabel("di2 [mm]");           builder.nextColumn(2);
        builder.add(di2Field);

        builder.nextLine(2);
        builder.addLabel("R [mm]");             builder.nextColumn(2);
        builder.add(rField);                    builder.nextColumn(2);
        builder.addLabel("D [mm]");             builder.nextColumn(2);
        builder.add(dField);
        builder.nextLine(2);

        builder.addSeparator("Criteria");
        builder.nextLine(2);

        builder.addLabel("Location");           builder.nextColumn(2);
        builder.add(locationCombo);             builder.nextColumn(2);
        builder.addLabel("k-factor");           builder.nextColumn(2);
        builder.add(kFactorField);
        builder.nextLine(2);

        builder.addLabel("Holes");              builder.nextColumn(2);
        builder.setColumnSpan(5);
        builder.add(holesCheckBox);
        builder.setColumnSpan(1);
        builder.nextLine(2);

        builder.addLabel("Slots");              builder.nextColumn(2);
        builder.setColumnSpan(5);
        builder.add(slotsCheckBox);
        builder.setColumnSpan(1);
        
        return builder.getPanel();
    }


}