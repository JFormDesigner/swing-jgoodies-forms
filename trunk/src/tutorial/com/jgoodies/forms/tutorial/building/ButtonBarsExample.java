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

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how to build button bars using a ButtonBarBuilder.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.5 $
 * @see     ButtonBarBuilder
 * @see     ButtonBarFactory
 */
public final class ButtonBarsExample {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Button Bars");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JComponent panel = new ButtonBarsExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add(buildButtonBar1Panel(),      "No Builder");
        tabbedPane.add(buildButtonBar2Panel(),      "With Builder");
        tabbedPane.add(buildButtonBar3Panel(),      "Related");
        tabbedPane.add(buildButtonBar4Panel(),      "Unrelated ");
        tabbedPane.add(buildButtonMixedBar1Panel(), "Mix 1");
        tabbedPane.add(buildButtonMixedBar2Panel(), "Mix 2");
        return tabbedPane;
    }
    
    private Component buildButtonBar1Panel() {
        JPanel buttonBar = new JPanel(
            new FormLayout("0:grow, p, 4px, p", "p"));
        buttonBar.add(new JButton("Yes"), "2, 1");                      
        buttonBar.add(new JButton("No"),  "4, 1");   
        
        return wrap(buttonBar, 
            "\nThis bar has been built without a ButtonBarBuilder.\n" +            " o the buttons have no minimum widths and\n" +            " o gaps may be inconsistent between team members.");
    }

    private Component buildButtonBar2Panel() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGridded(new JButton("Yes"));                      
        builder.addRelatedGap();                   
        builder.addGridded(new JButton("No"));  
         
        return wrap(builder.getPanel(),
            "\nThis bar has been built with a ButtonBarBuilder.\n" +
            " o The buttons have a minimum widths and\n" +
            " o the button gap is a logical size that follows a style guide.");
    }
    
    private Component buildButtonBar3Panel() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGridded(new JButton("Related"));   
        builder.addRelatedGap();                   
        builder.addGridded(new JButton("Related"));   
        builder.addRelatedGap();                   
        builder.addGridded(new JButton("Related"));   

        return wrap(builder.getPanel(),
            "\nThis bar uses the logical gap for related buttons.\n");    }
    
    private Component buildButtonBar4Panel() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGridded(new JButton("Unrelated"));   
        builder.addUnrelatedGap();                   
        builder.addGridded(new JButton("Unrelated"));   
        builder.addUnrelatedGap();                   
        builder.addGridded(new JButton("Unrelated"));   

        return wrap(builder.getPanel(),
            "\nThis bar uses the logical gap for unrelated buttons.\n" +            "It is a little bit wider than the related gap.");
    }
    
    private Component buildButtonMixedBar1Panel() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(new JButton("Help"));
        builder.addGlue();
        builder.addUnrelatedGap();
        builder.addFixed(new JButton("Copy to Clipboard"));
        builder.addUnrelatedGap();
        builder.addGridded(new JButton("OK"));   
        builder.addRelatedGap();                   
        builder.addGridded(new JButton("Cancel"));   

        return wrap(builder.getPanel(),
            "\nDemonstrates a glue (between Help and the rest),\n" +            "has related and unrelated buttons and an ungridded button\n" +            "with a default margin (Copy to Clipboard).");
    }
    
    private Component buildButtonMixedBar2Panel() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(new JButton("Help"));
        builder.addGlue();
        builder.addUnrelatedGap();
        builder.addFixedNarrow(new JButton("Copy to Clipboard"));
        builder.addUnrelatedGap();
        builder.addGridded(new JButton("OK"));   
        builder.addRelatedGap();                   
        builder.addGridded(new JButton("Cancel"));   

        return wrap(builder.getPanel(),
            "\nDemonstrates a glue (between Help and the rest),\n" +
            "has related and unrelated buttons and an ungridded button\n" +
            "with a narrow margin (Copy to Clipboard).");
    }
    
    
    // Helper Code ************************************************************
    
    private Component wrap(Component buttonBar, String text) {
        Component textPane = new JScrollPane(new JTextArea(text));
        
        FormLayout layout = new FormLayout(
                        "fill:default:grow",
                        "fill:p:grow, 4dlu, p");
        JPanel panel = new JPanel(layout);
        CellConstraints cc = new CellConstraints();
        panel.setBorder(Borders.DIALOG_BORDER);
        panel.add(textPane,    cc.xy(1, 1));
        panel.add(buttonBar,   cc.xy(1, 3));                   
        return panel;
    }
    
}

