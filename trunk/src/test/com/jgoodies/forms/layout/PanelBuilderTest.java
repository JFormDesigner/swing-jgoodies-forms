/*
 * Copyright (c) 2002-2009 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.forms.layout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import junit.framework.TestCase;

import com.jgoodies.forms.builder.PanelBuilder;

/**
 * A test case for class {@link PanelBuilder}.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.2 $
 */
public final class PanelBuilderTest extends TestCase {

    private JLabel labelWithMnemonic;
    private JLabel labelWithoutMnemonic;
    private JLabel labelingLabel;

    private JTextField plainField;
    private JTextField unfocusableField;
    private JTextField labeledField;

    private PanelBuilder builder;

    private CellConstraints cc;


    // Setup ******************************************************************

    protected void setUp() {
        labelWithMnemonic = new JLabel("Label1:");
        labelWithMnemonic.setDisplayedMnemonic('L');

        labelWithoutMnemonic = new JLabel("Label2:");

        plainField = new JTextField("plain");
        unfocusableField = new JTextField("unfocusable");
        unfocusableField.setFocusable(false);

        labeledField = new JTextField("labeled");
        labelingLabel = new JLabel("Label3");
        labelingLabel.setLabelFor(labeledField);

        builder = createBuilder();
        cc = new CellConstraints();
    }


    // LabelFor Feature *******************************************************

    /**
     * Checks the association between mnemonic label and next component.
     */
    public void testLabelForWithMnemonic() {
        builder.add(labelWithMnemonic, cc.xy(1, 1));
        builder.add(plainField,        cc.xy(3, 1));
        assertSame("Labeling label",
                plainField,
                labelWithMnemonic.getLabelFor());
    }


    /**
     * Checks the association between plain label and next component.
     */
    public void testLabelForWithoutMnemonic() {
        builder.add(labelWithoutMnemonic, cc.xy(1, 1));
        builder.add(plainField,           cc.xy(3, 1));
        assertSame("Labeling label",
                plainField,
                labelWithoutMnemonic.getLabelFor());
    }


    /**
     * Checks the association between label and unfocusable component.
     */
    public void testLabelForUnfocusableField() {
        builder.add(labelWithoutMnemonic, cc.xy(1, 1));
        builder.add(unfocusableField,     cc.xy(3, 1));
        assertNull("Labeling label",
                labelWithoutMnemonic.getLabelFor());
    }


    /**
     * Checks the association between labeling label and next component.
     */
    public void testLabelForLabelingLabel() {
        builder.add(labelingLabel,     cc.xy(1, 1));
        builder.add(plainField,        cc.xy(3, 1));
        builder.add(labelWithMnemonic, cc.xy(1, 3));
        builder.add(new JTextField(),  cc.xy(3, 3));
        assertSame("Labeling label",
                labeledField,
                labelingLabel.getLabelFor());
    }


    // Helper Code ************************************************************

    private PanelBuilder createBuilder() {
        FormLayout layout = new FormLayout(
                "p, $lcgap, p",
                "10*(p, $lg), p");
        PanelBuilder aBuilder = new PanelBuilder(layout);
        aBuilder.setLabelForFeatureEnabled(true);
        return aBuilder;
    }

}
