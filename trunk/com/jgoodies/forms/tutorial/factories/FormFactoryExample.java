/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */

package com.jgoodies.forms.tutorial.factories;

import java.awt.Component;

import javax.swing.*;

import com.jgoodies.forms.extras.DefaultFormBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.Sizes;

/**
 * Demonstrates the use of Factories as provided by the Forms framework.
 *
 * @author	Karsten Lentzsch
 * @see	ButtonBarFactory
 * @see	WizardBarFactory
 */
public final class FormFactoryExample {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                "com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
        }
        JFrame frame = new JFrame();
        frame.setTitle("Forms Demo :: ButtonBarFactory");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new FormFactoryExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }

    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add(buildDefaultForm(1, 1),  "1 - 1");
        tabbedPane.add(buildDefaultForm(1, 2),  "1 - 2");
        tabbedPane.add(buildDefaultForm(1, 3),  "1 - 3");
        tabbedPane.add(buildDefaultForm(2, 1),  "2 - 1");
        tabbedPane.add(buildDefaultForm(2, 2),  "2 - 2");
        tabbedPane.add(buildDefaultForm(3, 1),  "3 - 1");
        return tabbedPane;
    }


    private Component buildDefaultForm(int majorCols, int minorCols) {
        FormLayout layout =
            FormFactory.createColumnLayout(
                majorCols,
                minorCols,
                new ColumnSpec("right:p"),
                Sizes.DLUX9,
                Sizes.DLUX1);

        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setLeadingColumnOffset(1);

        buildParagraph(builder, 4, majorCols, minorCols, "Propeller Shaft");
        buildParagraph(builder, 3, majorCols, minorCols, "Intermediate Shaft");

        return builder.getContainer();
    }

    private void buildParagraph(
        DefaultFormBuilder builder,
        int rows,
        int majorCols,
        int minorCols,
        String text) {
        builder.appendSeparator(text);
        for (int row = 0; row < rows; row++) {
            buildRow(builder, majorCols, minorCols);
        }
    }

    private void buildRow(
        DefaultFormBuilder builder,
        int majorCols,
        int minorCols) {
        int charCols = 50 / (majorCols * (1 + minorCols));
        for (int majorCol = 0; majorCol < majorCols; majorCol++) {
            buildSection(builder, minorCols, charCols);
        }
        builder.nextLine();
    }

    private void buildSection(DefaultFormBuilder builder, int minorCols, int charCols) {
        builder.append(nextLabel(), new JTextField(charCols));
        for (int minorCol = 1; minorCol < minorCols; minorCol++) {
            builder.append(new JTextField(charCols));
        }
    }

    // Helper Code ************************************************************

    private static int nextInt = 0;

    private String nextLabel() {
        if (nextInt++ == LABELS.length - 1)
            nextInt = 0;
        return LABELS[nextInt];
    }
    
    private static final String[] LABELS = {
        "da [mm]", "ds [mm]", "kl [cm]", "Weight [Kg]", "Size [mm]",
        "da2 [mm]", "ds2 [mm]", "cv [cm]", "pl [cm]", "mt [mm]",
        "ep [mm]", "cvn [mm]", "nz [cm]", "Power [kW]", "Length [cm]",
        "R [cm]", "D [mm]", "PTI [kW]"
    };

}
