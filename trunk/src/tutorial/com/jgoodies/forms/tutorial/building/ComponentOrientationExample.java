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

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how to build panels that honor or ignore the current
 * component orientation: left-to-right vs. right-to-left.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.1 $
 * 
 * @see     com.jgoodies.forms.builder.AbstractFormBuilder
 * @see     com.jgoodies.forms.builder.DefaultFormBuilder
 */
public final class ComponentOrientationExample {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Component Orientation");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JComponent panel = new ComponentOrientationExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        FormLayout layout = new FormLayout("pref:grow");
        DefaultFormBuilder rowBuilder = new DefaultFormBuilder(layout);
        rowBuilder.setDefaultDialogBorder();
        
        rowBuilder.append(buildSample("Left to Right",       true));
        rowBuilder.append(buildSample("Right to Left",       false));
        rowBuilder.append(buildSample("Default Orientation", 
                new PanelBuilder(layout).isLeftToRight()));
        
        return rowBuilder.getPanel();
    }
    
    private Component buildSample(String title, boolean leftToRight) {
        FormLayout layout = new FormLayout(
                leftToRight 
                   ? "right:pref, 4dlu, pref:grow, 3dlu, pref:grow"
                   : "pref:grow,  3dlu, pref:grow, 4dlu, left:pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setLeftToRight(leftToRight);
        builder.setDefaultDialogBorder();
        
        builder.appendSeparator(title);
        builder.append("Anna"); builder.append(new JTextField(10), 3);
        builder.append("Level", new JTextField(10));
        builder.append(new JTextField(10));
        builder.append("Radar"); builder.append(new JTextField(10), 3); 
        return builder.getPanel();
    }

}

