/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.forms.tutorial.building;

import java.awt.Component;

import javax.swing.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the <code>FormLayout</code> with a <code>PanelBuilder</code>. 
 * Columns and rows are specified before the panel is filled with components.
 * Unlike the {@link PlainExample} this class uses a local variable
 * to keep track of the current row. The advantage over fixed numbers is, 
 * that it's easier to insert new rows later.
 * <p> 
 * This panel building style is simple and works quite well. However, you may
 * consider using a more sophisticated form builder to fill the panel and/or 
 * add rows dynamically; see the {@link DynamicRowsExample} for this alternative.
 *
 * @author Karsten Lentzsch
 * @see	PlainExample
 * @see	DynamicRowsExample
 * @see	DefaultFormBuilderExample
 */

public final class RowCounterExample {

    private JTextField identifierField;
    private JTextField powerField;
    private JTextField speedField;
    private JComboBox  materialComboBox;
    private JComboBox  iceClassComboBox;
    private JTextArea  machineryCommentArea;
    private JTextArea  inspectionCommentArea;


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Row Counter");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JComponent panel = new RowCounterExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    // Component Creation and Initialization **********************************

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        identifierField         = new JTextField();
        powerField              = new JTextField();
        speedField              = new JTextField();
        materialComboBox        = createMaterialComboBox();
        iceClassComboBox        = createIceClassComboBox();
        machineryCommentArea    = new JTextArea();
        inspectionCommentArea   = new JTextArea();
    }

    /**
     * Builds and returns a combo box for materials.
     */
    private JComboBox createMaterialComboBox() {
        return new JComboBox(new String[] {"C45E, ReH=600", 
                                            "Bolt Material, ReH=800"});
    }
    /**
     * Builds and returns a combo box for ice classes.
     */
    private JComboBox createIceClassComboBox() {
        return new JComboBox(new String[] { "E", "E1", "E2", "E3", "E4" });
    }


    // Building *************************************************************

    /**
     * Builds the content pane.
     */
    public JComponent buildPanel() {
        initComponents();
        Component machineryPane  = new JScrollPane(machineryCommentArea);
        Component inspectionPane = new JScrollPane(inspectionCommentArea);

        FormLayout layout = new FormLayout(
                "right:max(40dlu;pref), 3dlu, 70dlu, 7dlu, "
              + "right:max(40dlu;pref), 3dlu, 70dlu",
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 9dlu, "
              + "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");
        layout.setRowGroups(new int[][] { { 3, 13, 15, 17 } });
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();

        CellConstraints cc = new CellConstraints();
        int row = 1;

        builder.addSeparator("Shaft",     cc.xywh(1, row++, 7, 1));
        row++;
        
        builder.addLabel("Identifier",    cc.xy  (1, row));
        builder.add(identifierField,      cc.xy  (3, row++));
        row++;

        builder.addLabel("Power",         cc.xy  (1, row));
        builder.add(powerField,           cc.xy  (3, row));
        builder.addLabel("Speed",         cc.xy  (5, row));
        builder.add(speedField,           cc.xy  (7, row++));
        row++;

        builder.addLabel("Material",      cc.xy  (1, row));
        builder.add(materialComboBox,     cc.xy  (3, row));
        builder.addLabel("Ice Class",     cc.xy  (5, row));
        builder.add(iceClassComboBox,     cc.xy  (7, row++));
        row++;

        builder.addSeparator("Comments",  cc.xywh(1, row++, 7, 1));
        row++;
        
        builder.addLabel("Machinery",     cc.xy  (1, row));
        builder.add(machineryPane,        cc.xywh(3, row++, 5, 3, "f, f"));
        row += 3;

        builder.addLabel("Inspection",    cc.xy  (1, row));
        builder.add(inspectionPane,       cc.xywh(3, row++, 5, 3, "f, f"));
        
        return builder.getPanel();
    }


}