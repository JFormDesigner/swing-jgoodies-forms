/*
 * Copyright (c) 2002-2006 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package com.jgoodies.forms.tutorial.factories;

import java.awt.Component;

import javax.swing.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.Sizes;

/**
 * Demonstrates the use of Factories as provided by the Forms framework.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.13 $
 * 
 * @see	com.jgoodies.forms.factories.ButtonBarFactory
 */
public final class FormFactoryExample {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: FormFactory");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new FormFactoryExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
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
        "da/mm", "ds/mm", "kl/cm", "Weight/Kg", "Size/mm",
        "da2/mm", "ds2/mm", "cv/cm", "pl/cm", "mt/mm",
        "ep/mm", "cvn/mm", "nz/cm", "Power/kW", "Length/cm",
        "R/cm", "D/mm", "PTI/kW"
    };

}
