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
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JComponent panel = new ShaftEditor().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    // Building *************************************************************

    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add(buildEditor(new FormDebugPanel()), "Debug");
        tabbedPane.add(buildEditor(new JPanel()),         "Plain");
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