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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how to build button bars with a fixed button order 
 * or with a button order that honors the platform's style.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.1 $
 * 
 * @see     ButtonBarBuilder
 * @see     com.jgoodies.forms.factories.ButtonBarFactory
 */
public final class ButtonOrderExample {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Button Order");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JComponent panel = new ButtonOrderExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        FormLayout layout = new FormLayout("pref, 4dlu, pref:grow");
        DefaultFormBuilder rowBuilder = new DefaultFormBuilder(layout);
        rowBuilder.setDefaultDialogBorder();
        
        rowBuilder.appendSeparator("Left to Right");
        rowBuilder.append("Ordered", buildOrderedBar(createLeftToRightBuilder()));
        rowBuilder.append("Fixed",   buildFixedOrderBar(createLeftToRightBuilder()));
        
        rowBuilder.appendSeparator("Right to Left");
        rowBuilder.append("Ordered", buildOrderedBar(createRightToLeftBuilder()));
        rowBuilder.append("Fixed",   buildFixedOrderBar(createRightToLeftBuilder()));
        
        rowBuilder.appendSeparator("Platform Default Order");
        rowBuilder.append("Ordered", buildOrderedBar(new ButtonBarBuilder()));
        rowBuilder.append("Fixed",   buildFixedOrderBar(new ButtonBarBuilder()));
        
        return rowBuilder.getPanel();
    }
    
    private Component buildFixedOrderBar(ButtonBarBuilder builder) {
        builder.addGlue();
        builder.addGridded(new JButton("Left"));
        builder.addRelatedGap();
        builder.addGridded(new JButton("Middle"));
        builder.addRelatedGap();
        builder.addGridded(new JButton("Right"));
        return builder.getPanel();
    }

    private Component buildOrderedBar(ButtonBarBuilder builder) {
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] {
                new JButton("Lead"),
                new JButton("Middle"),
                new JButton("Tail")
            });
        return builder.getPanel();
    }

    private ButtonBarBuilder createLeftToRightBuilder() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.setLeftToRightButtonOrder(true);
        return builder;
    }
    
    private ButtonBarBuilder createRightToLeftBuilder() {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.setLeftToRightButtonOrder(false);
        return builder;
    }
    
}

