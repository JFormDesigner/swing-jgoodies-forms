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

package com.jgoodies.forms.tutorial.basics;

import javax.swing.*;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the very basic steps to work with the FormLayout:
 * create a layout, create a panel, then add components to the container.
 *
 * @author	Karsten Lentzsch
 */
public final class FirstStepsExample {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: First Steps");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JComponent panel = new FirstStepsExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }

    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add("Example 1a", buildExample1a());
        tabbedPane.add("Example 1b", buildExample1b());
        return tabbedPane;
    }

    /** 
     * Builds and answers a basic FormLayout example.
     * The panel consists of a two visible columns:
     * one for text labels, another for text fields.
     */
    private JComponent buildExample1a() {
        
        // Create a FormLayout instance. 
        FormLayout layout = new FormLayout(
            "pref, 8px, pref",                                  // columns
            "pref, 6px, pref, 6px, pref, 6px, pref, 6px, pref"); // rows

        // Create a panel that uses the layout.
        JPanel panel = new JPanel(layout);

        // Set a default border.
        panel.setBorder(Borders.DIALOG_BORDER);
        
        // Create a reusable CellConstraints instance.
        CellConstraints cc = new CellConstraints();

        // Add components to the panel.
        panel.add(new JLabel("Name:"),    cc.xy(1, 1));
        panel.add(new JTextField(20),     cc.xy(3, 1)); 

        panel.add(new JLabel("Phone:"),   cc.xy(1, 3));
        panel.add(new JTextField(20),     cc.xy(3, 3)); 
        
        panel.add(new JLabel("Fax:"),     cc.xy(1, 5));
        panel.add(new JTextField(20),     cc.xy(3, 5)); 

        panel.add(new JLabel("Email:"),   cc.xy(1, 7));
        panel.add(new JTextField(20),     cc.xy(3, 7)); 

        panel.add(new JLabel("Address:"), cc.xy(1, 9));
        panel.add(new JTextField(20),     cc.xy(3, 9)); 

        return panel;
    }
    
    
    /** 
     * Builds and answers a basic FormLayout example.
     * The panel consists of a two visible columns:
     * one for text labels, another for text fields.
     */
    private JComponent buildExample1b() {
        
        // Create a FormLayout instance. 
        FormLayout layout = new FormLayout(
            "right:pref, 8px, pref",                             // columns
            "pref, 6px, pref, 6px, pref, 6px, pref, 6px, pref"); // rows

        // Create a panel that uses the layout.
        JPanel panel = new JPanel(layout);

        // Set a default border.
        panel.setBorder(Borders.DIALOG_BORDER);
        
        // Create a reusable CellConstraints instance.
        CellConstraints cc = new CellConstraints();

        // Add components to the panel.
        panel.add(new JLabel("Name:"),    cc.xy(1, 1));
        panel.add(new JTextField(20),     cc.xy(3, 1)); 

        panel.add(new JLabel("Phone:"),   cc.xy(1, 3));
        panel.add(new JTextField(20),     cc.xy(3, 3)); 
        
        panel.add(new JLabel("Fax:"),     cc.xy(1, 5));
        panel.add(new JTextField(20),     cc.xy(3, 5)); 

        panel.add(new JLabel("Email:"),   cc.xy(1, 7));
        panel.add(new JTextField(20),     cc.xy(3, 7)); 

        panel.add(new JLabel("Address:"), cc.xy(1, 9));
        panel.add(new JTextField(20),     cc.xy(3, 9)); 

        return panel;
    }
    
    
}
