/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.tutorial.building;

import javax.swing.*;

import com.jgoodies.forms.extras.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Uses the <code>FormLayout</code> and the <code>DefaultFormBuilder</code>. 
 * Columns are specified before the panel is filled with components, 
 * rows are added dynamically. The builder is used to hold a cursor, 
 * to add rows dynamically, and to fill components. 
 * The builder's convenience methods are used to add labels and separators.
 * <p> 
 * This panel building style is recommended unless you have a more
 * powerful builder or don't want to add rows dynamically. 
 * See the {@link SegmentEditor} for an implementation that specifies 
 * rows before the panel is filled with components. 
 *
 * @author Karsten Lentzsch
 * @see	DescriptionEditor
 * @see	ShaftEditor
 * @see	SegmentEditor
 */

public final class FlangeEditor {

    private JTextField identifierField;
    private JTextField ptiField;
    private JTextField powerField;
    private JTextField sField;
    private JTextField daField;
    private JTextField diField;
    private JTextField da2Field;
    private JTextField di2Field;
    private JTextField rField;
    private JTextField dField;
    private JComboBox  locationCombo;
    private JTextField kFactorField;
    

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Building:: DefaultFormBuilder");
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
        sField          = new JTextField();
        daField         = new JTextField();
        diField         = new JTextField();
        da2Field        = new JTextField();
        di2Field        = new JTextField();
        rField          = new JTextField();
        dField          = new JTextField();
        locationCombo   = createLocationComboBox();
        kFactorField    = new JTextField();
    }

    /**
     * Creates and returns a combo box for the locations.
     */
    private JComboBox createLocationComboBox() {
        return new JComboBox(
            new String[] {
                "Propeller nut thread",
                "Stern tube front area",
                "Shaft taper" });
    }


    // Building ***************************************************************

    /**
     * Builds the flange editor panel. 
     * Columns are specified before components are added to the form, 
     * rows are added dynamically using the {@link DefaultFormBuilder}.
     * <p>
     * The builder combines a step that is done again and again:
     * add a label, proceed to the next data column and add a component.
     */
    public JComponent buildPanel() {
        initComponents();

        FormLayout layout = new FormLayout(
                "right:max(40dlu;pref), 3dlu, 70dlu, 7dlu, "
              + "right:max(40dlu;pref), 3dlu, 70dlu",
                "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();


        builder.appendSeparator("Flange");

        builder.append("&Identifier", identifierField);
        builder.nextLine();

        builder.append("PTI [kW]",   ptiField);          
        builder.append("Power [kW]", powerField);

        builder.append("s [mm]",     sField);
        builder.nextLine();


        builder.appendSeparator("Diameters");

        builder.append("&da [mm]",   daField);          
        builder.append("di [mm]",    diField);

        builder.append("da2 [mm]",   da2Field);          
        builder.append("di2 [mm]",   di2Field);

        builder.append("R [mm]",     rField);          
        builder.append("D [mm]",     dField);


        builder.appendSeparator("Criteria");

        builder.append("&Location",  locationCombo);   
        builder.append("k-factor",   kFactorField);

        return builder.getPanel();
    }

}