/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */
 
package com.jgoodies.forms.tutorial.basics;

import javax.swing.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the basic FormLayout sizes: constant, minimum, preferred.
 *
 * @author	Karsten Lentzsch
 */
public final class BoundedSizesExample {

    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Basic Sizes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new BoundedSizesExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add("Jumping 1",  buildJumping1Panel());
        tabbedPane.add("Jumping 2",  buildJumping2Panel());
        tabbedPane.add("Stable 1",   buildStable1Panel());
        tabbedPane.add("Stable 2",   buildStable2Panel());
        return tabbedPane;
    }
    
        
    private JComponent buildJumping1Panel() {
        FormLayout layout = new FormLayout(
                "right:pref, 4dlu, max(35dlu;min), 2dlu, min, 2dlu, min, 2dlu, min, ",
                EDITOR_ROW_SPEC);
        return buildEditorGeneralPanel(layout);
    }
    
    private JComponent buildJumping2Panel() {
        FormLayout layout = new FormLayout(
                "right:pref, 4dlu, max(35dlu;min), 2dlu, min, 2dlu, min, 2dlu, min, ",
                EDITOR_ROW_SPEC);
        return buildEditorTransportPanel(layout);
    }
    
    private JComponent buildStable1Panel() {
        FormLayout layout = new FormLayout(
                "right:max(50dlu;pref), 4dlu, max(35dlu;min), 2dlu, min, 2dlu, min, 2dlu, min, ",
                EDITOR_ROW_SPEC);
        return buildEditorGeneralPanel(layout);
    }
    
    private JComponent buildStable2Panel() {
        FormLayout layout = new FormLayout(
                "right:max(50dlu;pref), 4dlu, max(35dlu;min), 2dlu, min, 2dlu, min, 2dlu, min, ",
                EDITOR_ROW_SPEC);
        return buildEditorTransportPanel(layout);
    }
    
    private static final String EDITOR_ROW_SPEC =
        "p, 3dlu, p, 3dlu, p, 3dlu, p";


    /**
     * Builds and answer the editor's general tab for the given layout.
     */
    private JComponent buildEditorGeneralPanel(FormLayout layout) {
        layout.setColumnGroups(new int[][] { { 3, 5, 7, 9 } });
        PanelBuilder builder = new PanelBuilder(layout);
            
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addLabel("File number:",        cc.xy  (1,  1));
        builder.add(new JTextField(),           cc.xywh(3,  1, 7, 1));
        builder.addLabel("RFQ number:",         cc.xy  (1,  3));
        builder.add(new JTextField(),           cc.xywh(3,  3, 7, 1));
        builder.addLabel("Entry date:",         cc.xy  (1,  5));
        builder.add(new JTextField(),           cc.xy  (3,  5));
        builder.addLabel("Sales Person:",       cc.xy  (1,  7));
        builder.add(new JTextField(),           cc.xywh(3,  7, 7, 1));
        
        return builder.getPanel();
    }
    
    /**
     * Builds and answer the editor's transport tab for the given layout.
     */
    private JComponent buildEditorTransportPanel(FormLayout layout) {
        layout.setColumnGroups(new int[][] { { 3, 5, 7, 9 } });
        PanelBuilder builder = new PanelBuilder(layout);
            
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Shipper:",            cc.xy  (1, 1));
        builder.add(new JTextField(),           cc.xy  (3, 1));
        builder.add(new JTextField(),           cc.xywh(5, 1, 5, 1));
        builder.addLabel("Consignee:",          cc.xy  (1, 3));
        builder.add(new JTextField(),           cc.xy  (3, 3));
        builder.add(new JTextField(),           cc.xywh(5, 3, 5, 1));
        builder.addLabel("Departure:",          cc.xy  (1, 5));
        builder.add(new JTextField(),           cc.xy  (3, 5));
        builder.add(new JTextField(),           cc.xywh(5, 5, 5, 1));
        builder.addLabel("Destination:",        cc.xy  (1, 7));
        builder.add(new JTextField(),           cc.xy  (3, 7));
        builder.add(new JTextField(),           cc.xywh(5, 7, 5, 1));
        
        return builder.getPanel();
    }
    
    
    
    
}

