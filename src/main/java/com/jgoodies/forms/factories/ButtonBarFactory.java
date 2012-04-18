/*
 * Copyright (c) 2002-2012 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.forms.factories;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.ButtonBarBuilder2;

/**
 * A factory class that consists only of static methods to build frequently used
 * button bars. Utilizes the {@link ButtonBarBuilder2}
 * that in turn uses the {@link com.jgoodies.forms.layout.FormLayout}
 * to lay out the bars.<p>
 *
 * The button bars returned by this builder comply with popular UI style guides.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.11 $
 *
 * @see com.jgoodies.forms.util.LayoutStyle
 * 
 * @deprecated Replaced by the button bar builders in the JGoodies Standard
 *    Dialog Library (JSDL) Core library. 
 *    <strong>This class will be removed from the JGoodies Forms version 1.6.</strong>
 */
@Deprecated
public final class ButtonBarFactory {


    private ButtonBarFactory() {
        // Suppresses default constructor, ensuring non-instantiability.
    }


    // General Purpose Factory Methods: Left Aligned ************************

    


    


    // General Purpose Factory Methods: Centered ****************************

    


    // General Purpose Factory Methods: Right Aligned ***********************

    /**
     * Builds and returns a right aligned bar with one button.
     *
     * @param button1  the first button to add
     * @return a button bar with the given button
     */
    public static JPanel buildRightAlignedBar(JButton button1) {
        return buildRightAlignedBar(new JButton[]{
                button1
        	});
    }


    /**
     * Builds and returns a right aligned button bar with the given buttons.
     *
     * @param buttons  an array of buttons to add
     * @return a right aligned button bar with the given buttons
     */
    public static JPanel buildRightAlignedBar(JButton... buttons) {
        ButtonBarBuilder2 builder = new ButtonBarBuilder2();
        builder.addGlue();
        builder.addButton(buttons);
        return builder.getPanel();
    }

    // Popular Dialog Button Bars: No Help **********************************

    /**
     * Builds and returns a button bar with Close.
     *
     * @param close   	the Close button
     * @return a panel that contains the button(s)
     */
    public static JPanel buildCloseBar(JButton close) {
        return buildRightAlignedBar(close);
    }


	/**
     * Builds and returns a button bar with OK and Cancel.
     *
     * @param ok		the OK button
     * @param cancel	the Cancel button
     * @return a panel that contains the button(s)
     */
    public static JPanel buildOKCancelBar(
            JButton ok, JButton cancel) {
        return buildRightAlignedBar(new JButton[] {ok, cancel});
    }


    /**
     * Builds and returns a button bar with OK, Cancel and Apply.
     *
     * @param ok        the OK button
     * @param cancel    the Cancel button
     * @param apply	the Apply button
     * @return a panel that contains the button(s)
     */
    public static JPanel buildOKCancelApplyBar(
            JButton ok, JButton cancel, JButton apply) {
        return buildRightAlignedBar(new JButton[] {ok, cancel, apply});
    }


    // Popular Dialog Button Bars: Help in the Left *************************



}
