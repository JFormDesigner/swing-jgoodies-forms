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

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how a JTextArea's preferred size grows with the container
 * if no columns and rows are set.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.1 $
 */
public final class GrowingTextAreaExample {
    
    private JTextArea growingArea;
    private JTextArea constantArea;

    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Growing Text Area");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new GrowingTextAreaExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    
    private void initComponents() {
        growingArea  = new JTextArea("The preferred size of this area grows with the container.");
        //growingArea.setLineWrap(true);
        constantArea = new JTextArea("The preferred size\nof this area\nis constant.", 5, 60);
    }
    
    private void initEventHandling() {
        growingArea.addComponentListener(new SizeChangeHandler("Growing", growingArea));
        constantArea.addComponentListener(new SizeChangeHandler("Constant", constantArea));
    }


    public JComponent buildPanel() {
        initComponents();
        initEventHandling();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add("Growing",    buildGrowingTextArea());
        tabbedPane.add("Constant",   buildConstantTextArea());
        return tabbedPane;
    }
    
    
    private JComponent buildGrowingTextArea0() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(Borders.DIALOG_BORDER);
        panel.add(growingArea, BorderLayout.CENTER);
        return panel;
    }
    
    
    private JComponent buildGrowingTextArea() {
        FormLayout layout = new FormLayout("fill:pref:grow", "fill:pref:grow");
        JPanel panel = new JPanel(layout);
        panel.setBorder(Borders.DIALOG_BORDER);
        panel.add(growingArea, new CellConstraints(1, 1));
        return panel;
    }
    
    
    private JComponent buildConstantTextArea() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(Borders.DIALOG_BORDER);
        panel.add(constantArea, BorderLayout.CENTER);
        return panel;
    }
    
    
    // Writes sizes to the system.
    private class SizeChangeHandler extends ComponentAdapter {
        
        private final String     name;
        private final JTextComponent component;
        
        private SizeChangeHandler(String name, JTextComponent component) {
            this.name      = name;
            this.component = component;
        }
        
        public void componentResized(ComponentEvent evt) {
            System.out.println(name + " size        =" + component.getSize());
            System.out.println(name + " min  size   =" + component.getMinimumSize());
            System.out.println(name + " pref size   =" + component.getPreferredSize());
            System.out.println(name + " UI pref size=" + component.getUI().getPreferredSize(component));
        }
        
    }

    
    
}

