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

package com.jgoodies.forms.tutorial;

import javax.swing.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the very basic steps to work with the FormLayout:
 * create a FormLayout and add components to the container.
 *
 * @author	Karsten Lentzsch
 */
public final class Example4 {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Example 1");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new Example4().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add("Example 1a", buildExample1aPanel());
        tabbedPane.add("Example 1b", buildExample1bPanel());
        return tabbedPane;
    }
    
    
    public JComponent buildExample1aPanel() {
        FormLayout fl = new FormLayout("r:p, 4dlu, 50dlu, 4dlu, d", 
                                        "c:p, 2dlu, c:p, 2dlu, c:p");       
        fl.setRowGroups   (new int[][]{ {1, 3, 5} });

        JPanel panel = new JPanel(fl);
        panel.setBorder(Borders.DIALOG_BORDER);
        
        panel.add(new JLabel("Label1"),     "1, 1");
        panel.add(new JTextField(),         "3, 1, 3, 1");

        panel.add(new JLabel("Label2"),     "1, 3");
        panel.add(new JTextField(),         "3, 3");

        panel.add(new JLabel("Label3"),     "1, 5");
        panel.add(new JTextField(),         "3, 5");
        panel.add(new JButton("..."),       "5, 5");
        
        return panel;
        
    }


    public JComponent buildExample1bPanel() {
        FormLayout fl = new FormLayout("r:p, 4dlu, 50dlu, 4dlu, d", 
                                        "c:p, 2dlu, c:p, 2dlu, c:p");       
        fl.setRowGroups   (new int[][]{ {1, 3, 5} });

        PanelBuilder builder = new PanelBuilder(fl);
        builder.setDefaultDialogBorder();
        
        builder.add(new JLabel("Label1"));  
        builder.nextColumn(2);
        builder.setColumnSpan(3);
        builder.add(new JTextField());
        builder.setColumnSpan(1);

        builder.nextLine(2);
        builder.add(new JLabel("Label2"));
        builder.nextColumn(2);
        builder.add(new JTextField());

        builder.nextLine(2);
        builder.add(new JLabel("Label3"));
        builder.nextColumn(2);
        builder.add(new JTextField());
        builder.nextColumn(2);
        builder.add(new JButton("..."));
        
        return builder.getPanel();
        
    }

    
}

