/*
 * Copyright (c) 2002-2014 JGoodies Software GmbH. All Rights Reserved.
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
 *  o Neither the name of JGoodies Software GmbH nor the names of 
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

package com.jgoodies.forms.tutorial.factories;

import java.awt.Component;

import javax.swing.*;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.extras.WizardBarFactory;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates the use of Factories as provided by the Forms framework.
 *
 * @author	Karsten Lentzsch
 * @see	ButtonBarFactory
 * @see	WizardBarFactory
 */
public final class WizardBarFactoryExample {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {}
        JFrame frame = new JFrame();
        frame.setTitle("Forms Demo :: WizardBarFactory");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent panel = new WizardBarFactoryExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.show();
    }


    public JComponent buildPanel() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

        tabbedPane.add(buildWizardButtonBarTestPanel(),     "Wizard 1");
        tabbedPane.add(buildWizardHelpButtonBarTestPanel(), "Wizard 2");
        tabbedPane.add(buildWizard3ButtonBarTestPanel(),    "Wizard 3");
        return tabbedPane;
    }
    
    private Component buildWizardButtonBarTestPanel() {
        FormLayout layout = new FormLayout(
                        "default:grow",
                        "p, 4dlu, p, 4dlu, p, " +
                        "14dlu, " + 
                        "p, 4dlu, p, 4dlu, p, " +
                        "14dlu, " +
                        "p, 4dlu, p, 4dlu, p, " +
                        "14dlu, " + 
                        "p, 4dlu, p, 4dlu, p, " +
                        "0:grow");
                        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.border(Borders.DIALOG);
        
        // Wizard Bars: No Help, No Cancel, Finish replaces Next
        builder.add(WizardBarFactory.createNextBar(
            new JButton("Next >")
        ));                 
        builder.nextRow(2);
        builder.add(WizardBarFactory.createBackNextBar(
            new JButton("< Back"), new JButton("Next >")
        ));             
        builder.nextRow(2);
        builder.add(WizardBarFactory.createBackFinishBar(
            new JButton("< Back"), new JButton("Finish")
        ));             

        // Wizard Bars: No Help, No Cancel, Finish coexists with Next
        builder.nextRow(2);
        builder.add(WizardBarFactory.createFinishBar(
            new JButton("Finish")
        ));             
        builder.nextRow(2);
        builder.add(WizardBarFactory.createNextFinishBar(
            new JButton("Next >"), new JButton("Finish")
        ));             
        builder.nextRow(2);
        builder.add(WizardBarFactory.createBackNextFinishBar(
            new JButton("< Back"), new JButton("Next >"), new JButton("Finish")
        ));             
        
        // Wizard Bars: No Help, Cancel right, Finish replaces Next
        builder.nextRow(2);
        builder.add(WizardBarFactory.createNextCancelBar(
            new JButton("Next >"), new JButton("Cancel")
        ));                 
        builder.nextRow(2);
        builder.add(WizardBarFactory.createBackNextCancelBar(
            new JButton("< Back"), new JButton("Next >"), new JButton("Cancel")
        ));             
        builder.nextRow(2);
        builder.add(WizardBarFactory.createBackFinishCancelBar(
            new JButton("< Back"), new JButton("Finish"), new JButton("Cancel")
        ));        
           
        // Wizard Bars: No Help, Cancel right, Finish coexists with Next
        builder.nextRow(2);
        builder.add(WizardBarFactory.createFinishCancelBar(
            new JButton("Finish"), new JButton("Cancel")
        ));       
        builder.nextRow(2);
        builder.add(WizardBarFactory.createNextFinishCancelBar(
            new JButton("Next >"), new JButton("Finish"), new JButton("Cancel")
        ));       
        builder.nextRow(2);
        builder.add(WizardBarFactory.createBackNextFinishCancelBar(
            new JButton("< Back"), new JButton("Next >"), new JButton("Finish"), new JButton("Cancel")
        ));       

        return builder.getContainer();
    }
    


    private Component buildWizardHelpButtonBarTestPanel() {
        FormLayout layout = new FormLayout(
                        "default:grow",
                        "p, 4dlu, p, 4dlu, p, " +
                        "14dlu, " + 
                        "p, 4dlu, p, 4dlu, p, " +
                        "14dlu, " +
                        "p, 4dlu, p, 4dlu, p, " +
                        "14dlu, " + 
                        "p, 4dlu, p, 4dlu, p, " +
                        "0:grow");
                        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.border(Borders.DIALOG);
        
        // Wizard Bars: Help, No Cancel, Finish replaces Next
        builder.add(WizardBarFactory.createHelpNextBar(
            new JButton("Help"), new JButton("Next >")
        ));                 
        builder.nextRow(2);
        builder.add(WizardBarFactory.createHelpBackNextBar(
            new JButton("Help"), new JButton("< Back"), new JButton("Next >")
        ));             
        builder.nextRow(2);
        builder.add(WizardBarFactory.createHelpBackFinishBar(
            new JButton("Help"), new JButton("< Back"), new JButton("Finish")
        ));             

        // Wizard Bars: Help, No Cancel, Finish coexists with Next
        builder.nextRow(2);
        builder.add(WizardBarFactory.createHelpFinishBar(
            new JButton("Help"), new JButton("Finish")
        ));             
        builder.nextRow(2);
        builder.add(WizardBarFactory.createHelpNextFinishBar(
            new JButton("Help"), new JButton("Next >"), new JButton("Finish")
        ));             
        builder.nextRow(2);
        builder.add(WizardBarFactory.createHelpBackNextFinishBar(
            new JButton("Help"), new JButton("< Back"), new JButton("Next >"), new JButton("Finish")
        ));             
        
        // Wizard Bars: Help, Cancel right, Finish replaces Next
        builder.nextRow(2);
        builder.add(WizardBarFactory.createHelpNextCancelBar(
            new JButton("Help"), new JButton("Next >"), new JButton("Cancel")
        ));                 
        builder.nextRow(2);
        builder.add(WizardBarFactory.createHelpBackNextCancelBar(
            new JButton("Help"), new JButton("< Back"), new JButton("Next >"), new JButton("Cancel")
        ));             
        builder.nextRow(2);
        builder.add(WizardBarFactory.createHelpBackFinishCancelBar(
            new JButton("Help"), new JButton("< Back"), new JButton("Finish"), new JButton("Cancel")
        ));        
           
        // Wizard Bars: Help, Cancel right, Finish coexists with Next
        builder.nextRow(2);
        builder.add(WizardBarFactory.createHelpFinishCancelBar(
            new JButton("Help"), new JButton("Finish"), new JButton("Cancel")
        ));       
        builder.nextRow(2);
        builder.add(WizardBarFactory.createHelpNextFinishCancelBar(
            new JButton("Help"), new JButton("Next >"), new JButton("Finish"), new JButton("Cancel")
        ));       
        builder.nextRow(2);
        builder.add(WizardBarFactory.createHelpBackNextFinishCancelBar(
            new JButton("Help"), new JButton("< Back"), new JButton("Next >"), new JButton("Finish"), new JButton("Cancel")
        ));       

        return builder.getContainer();
    }
    


    private Component buildWizard3ButtonBarTestPanel() {
        FormLayout layout = new FormLayout(
                        "default:grow",
                        "p, 4dlu, p, 4dlu, p, " +
                        "14dlu, " + 
                        "p, 4dlu, p, 4dlu, p, " +
                        "0:grow");
                        
        PanelBuilder builder = new PanelBuilder(layout);
        builder.border(Borders.DIALOG);
        
        // Wizard Bars: No Help, Cancel in left, Finish replaces Next
        builder.add(WizardBarFactory.createCancelNextBar(
            new JButton("Cancel"), new JButton("Next >")
        ));                 
        builder.nextRow(2);
        builder.add(WizardBarFactory.createCancelBackNextBar(
            new JButton("Cancel"), new JButton("< Back"), new JButton("Next >")
        ));             
        builder.nextRow(2);
        builder.add(WizardBarFactory.createCancelBackFinishBar(
            new JButton("Cancel"), new JButton("< Back"), new JButton("Finish")
        ));             

        // Wizard Bars: No Help, Cancel in the left, Finish coexists with Next
        builder.nextRow(2);
        builder.add(WizardBarFactory.createCancelFinishBar(
            new JButton("Cancel"), new JButton("Finish")
        ));             
        builder.nextRow(2);
        builder.add(WizardBarFactory.createCancelNextFinishBar(
            new JButton("Cancel"), new JButton("Next >"), new JButton("Finish")
        ));             
        builder.nextRow(2);
        builder.add(WizardBarFactory.createCancelBackNextFinishBar(
            new JButton("Cancel"), new JButton("< Back"), new JButton("Next >"), new JButton("Finish")
        ));             
        
        return builder.getContainer();
    }
    


}

