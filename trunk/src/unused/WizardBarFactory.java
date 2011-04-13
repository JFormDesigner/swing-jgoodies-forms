/*
 * Copyright (c) 2002-2011 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.forms.extras;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.ButtonBarBuilder;

/**
 * A factory class that consists only of static methods 
 * to create frequently used wizard button bars.
 * <p> 
 * This class ships only with the JGoodies Forms source
 * distribution and is not yet part of the binary Forms library. 
 * <b>The API is work in progress and may change without notice.</b>
 * If you want to use this class, you may consider copying it into your codebase.
 * <p>
 * To help you build consistent wizard bars I consider removing
 * all methods #createCancel*.
 *
 * @author Karsten Lentzsch
 */

public final class WizardBarFactory {

    // Override default constructor; prevents instantiability.
    private WizardBarFactory() {
    }

    // Wizard Bars: No Help, No Cancel, Finish replaces Next*****************

    /**
     * Creates and returns a button bar with 
     * Next.
     */
    public static JPanel createNextBar(JButton next) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { next });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with
     * Back and Next.
     */
    public static JPanel createBackNextBar(JButton back, JButton next) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGridded(next);
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with
     * Back and Finish.
     */
    public static JPanel createBackFinishBar(JButton back, JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGridded(finish);
        return builder.getPanel();
    }

    // Wizard Bars: No Help, No Cancel, Finish coexists with Next************

    /**
     * Creates and returns a button bar with 
     * Finish.
     */
    public static JPanel createFinishBar(JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { finish });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with
     * Next and Finish.
     */
    public static JPanel createNextFinishBar(JButton next, JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { next, finish });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createBackNextFinishBar(
        JButton back,
        JButton next,
        JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGriddedButtons(new JButton[] { next, finish });
        return builder.getPanel();
    }

    // Wizard Bars: No Help, Cancel in the right hand side ******************    

    /**
     * Creates and returns a button bar with Next and Cancel.
     */
    public static JPanel createNextCancelBar(JButton next, JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { next, cancel });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createBackNextCancelBar(
        JButton back,
        JButton next,
        JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGriddedButtons(new JButton[] { next, cancel });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createBackFinishCancelBar(
        JButton back,
        JButton finish,
        JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGriddedButtons(new JButton[] { finish, cancel });
        return builder.getPanel();
    }

    // Wizard Bars: No Help, Cancel in the right hand side, Finish and Next *

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createFinishCancelBar(
        JButton finish,
        JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { finish, cancel });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createNextFinishCancelBar(
        JButton next,
        JButton finish,
        JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { next, finish, cancel });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createBackNextFinishCancelBar(
        JButton back,
        JButton next,
        JButton finish,
        JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGriddedButtons(new JButton[] { next, finish, cancel });
        return builder.getPanel();
    }

    // Wizard Bars: Help, No Cancel, Finish replaces Next*****************

    /**
     * Creates and returns a button bar with 
     * Help and Next.
     */
    public static JPanel createHelpNextBar(JButton help, JButton next) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { next });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with
     * Help, Back and Next.
     */
    public static JPanel createHelpBackNextBar(
        JButton help,
        JButton back,
        JButton next) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGridded(next);
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with
     * Back and Finish.
     */
    public static JPanel createHelpBackFinishBar(
        JButton help,
        JButton back,
        JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGridded(finish);
        return builder.getPanel();
    }

    // Wizard Bars: Help, No Cancel, Finish coexists with Next************

    /**
     * Creates and returns a button bar with 
     * Finish.
     */
    public static JPanel createHelpFinishBar(JButton help, JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { finish });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with
     * Next and Finish.
     */
    public static JPanel createHelpNextFinishBar(
        JButton help,
        JButton next,
        JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { next, finish });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createHelpBackNextFinishBar(
        JButton help,
        JButton back,
        JButton next,
        JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGriddedButtons(new JButton[] { next, finish });
        return builder.getPanel();
    }

    // Wizard Bars: No Help, Cancel in the left *****************************

    /**
     * Creates and returns a button bar with 
     * Help, Back, Next and Cancel.
     */
    public static JPanel createHelpBackNextCancelBar(
        JButton help,
        JButton back,
        JButton next,
        JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGriddedButtons(new JButton[] { next, cancel });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with 
     * Help, Back, Next and Finish.
     */
    public static JPanel createHelpBackFinishCancelBar(
        JButton help,
        JButton back,
        JButton finish,
        JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGriddedButtons(new JButton[] { finish, cancel });
        return builder.getPanel();
    }

    // Wizard Bars: Help, Cancel in the right hand side, Finish and Next *

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createHelpNextCancelBar(
        JButton help,
        JButton next,
        JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { next, cancel });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createHelpFinishCancelBar(
        JButton help,
        JButton finish,
        JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { finish, cancel });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createHelpNextFinishCancelBar(
        JButton help,
        JButton next,
        JButton finish,
        JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { next, finish, cancel });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createHelpBackNextFinishCancelBar(
        JButton help,
        JButton back,
        JButton next,
        JButton finish,
        JButton cancel) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGriddedButtons(new JButton[] { next, finish, cancel });
        return builder.getPanel();
    }

    // Wizard Bars: No Help, Cancel in left, Finish replaces Next ***********

    /**
     * Creates and returns a button bar with 
     * Next.
     */
    public static JPanel createCancelNextBar(JButton cancel, JButton next) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(cancel);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { next });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with
     * Back and Next.
     */
    public static JPanel createCancelBackNextBar(
        JButton cancel,
        JButton back,
        JButton next) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(cancel);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGridded(next);
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with
     * Back and Finish.
     */
    public static JPanel createCancelBackFinishBar(
        JButton cancel,
        JButton back,
        JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(cancel);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGridded(finish);
        return builder.getPanel();
    }

    // Wizard Bars: No Help, Cancel in the left, Finish coexists with Next************

    /**
     * Creates and returns a button bar with 
     * Finish.
     */
    public static JPanel createCancelFinishBar(
        JButton cancel,
        JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(cancel);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { finish });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with
     * Next and Finish.
     */
    public static JPanel createCancelNextFinishBar(
        JButton cancel,
        JButton next,
        JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(cancel);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[] { next, finish });
        return builder.getPanel();
    }

    /**
     * Creates and returns a button bar with Next and Finish.
     */
    public static JPanel createCancelBackNextFinishBar(
        JButton cancel,
        JButton back,
        JButton next,
        JButton finish) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(cancel);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGridded(back);
        builder.addGriddedButtons(new JButton[] { next, finish });
        return builder.getPanel();
    }

    
}
