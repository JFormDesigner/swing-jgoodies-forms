/*
 * Copyright (c) 2002-2004 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.forms.tutorial.pitfalls;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.debug.FormDebugUtils;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Exercise: 
 *    Fix the panel layout so that the JTable spans from top to bottom.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.1 $
 */

public final class VerticalGrowthExample {

    // UI Components **********************************************************
    
    private JTextComponent notesArea;
    

    // Self Launch ************************************************************

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Vertical Growth");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JComponent panel = new VerticalGrowthExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.setSize(500, 400);
        frame.show();
    }
    
    
    // Component Initialization ***********************************************
    
    /**
     * Creates and configures the UI components.
     */
    private void initComponents() {
        notesArea  = new JTextArea(
                "This text area doesn't consume the available vertical space.\n\n"
                + "The row is specified as 'pref:grow', and so the row grows.\n"
                + "It's just that the text area doesn't fill the row's vertical space.\n\n"
                + "Since the row's alignment is not explicitly defined,\n"
                + "it uses the 'center' alignment as default. But in this case\n"
                + "we want to 'fill'. The row spec should read: 'fill:pref:grow'."
                );
    }


    // Building ***************************************************************

    /**
     * Builds and returns a panel with a title and scrollable text area.<p>
     * 
     * The FormDebugUtils dumps
     * 
     * @return the built panel
     */
    public JComponent buildPanel() {
        initComponents(); 
        
        FormLayout layout = new FormLayout(
                "pref:grow",
                "pref, 3dlu, pref:grow" // Correct: "pref, 3dlu, fill:pref:grow"  
                );
        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addTitle("An Example for FAQ #3.3", cc.xy(1, 1));
        builder.add(new JScrollPane(notesArea),     cc.xy(1, 3));
        
        FormDebugUtils.dumpRowSpecs(layout);
        FormDebugUtils.dumpConstraints(builder.getPanel());
        return builder.getPanel();
    }
    
    
 }