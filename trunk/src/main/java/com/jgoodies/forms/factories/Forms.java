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

import static com.jgoodies.common.base.Preconditions.checkArgument;
import static com.jgoodies.common.base.Preconditions.checkNotBlank;
import static com.jgoodies.common.base.Preconditions.checkNotNull;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.internal.FocusTraversalUtilsAccessor;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Provides convenience behavior for building forms.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 */
public class Forms {
	
	private Forms() {
		// Overrides default constructor; prevents instantiation.
	}


	// Public API *************************************************************
	
	public static JComponent single(String columnSpec, String rowSpec, 
			JComponent component) {
		checkNotBlank(columnSpec, "The column specification must not be null, empty, or white space.");
		checkNotBlank(rowSpec,    "The row specification must not be null, empty, or white space.");
		checkNotNull (component,  "The component must not be null.");
		FormLayout layout = new FormLayout(columnSpec, rowSpec);
		PanelBuilder builder = new PanelBuilder(layout);
		builder.add(component, CC.xy(1, 1));
		return builder.build();
	}
	
	
	public static JComponent centered(JComponent component) {
		return single("fill:pref:grow", "f:p:g", component);
	}
	
	
	public static JComponent border(Border border, JComponent component) {
		JComponent container = single("fill:pref", "f:p", component);
		container.setBorder(border);
		return container;
	}
	
	
	public static JComponent border(String emptyBorderSpec, JComponent component) {
		return border(Borders.createEmptyBorder(emptyBorderSpec), component);
	}
	
	
	public static JComponent horizontal(String gapColSpec, JComponent... components) {
		checkNotBlank(gapColSpec, "The gap column specification must not be null, empty, or white space.");
		checkNotNull(components, "The component array must not be null.");
		FormLayout layout = new FormLayout();
		PanelBuilder builder = new PanelBuilder(layout);
		return builder.build();
	}
	
	
	public static JComponent vertical(String gapRowSpec, JComponent... components) {
		checkNotBlank(gapRowSpec, "The gap row specification must not be null, empty, or white space.");
		checkNotNull(components, "The component array must not be null.");
		FormLayout layout = new FormLayout();
		PanelBuilder builder = new PanelBuilder(layout);
		return builder.build();
	}
	

	public static JComponent buttonBar(JComponent... buttons) {
		return new ButtonBarBuilder()
			.addButton(buttons)
			.build();
	}


    /**
     * Builds and returns a panel where the given check boxes are laid out
     * in a row.<p>
     * 
     * If class {@code com.jgoodies.jsdl.common.focus.FocusTraversalUtils} 
     * from the JSDL Common library is in the class path, 
     * it is used to group the radio buttons. Focus is transferred to/from 
     * the selected button in a group; and cursor-left/-right change 
     * the selection in the group.
     *
     * @return the built panel
     */
	public static JComponent checkBoxBar(JCheckBox... checkBoxes) {
	    return buildGroupedButtonBar(checkBoxes);
	}


    /**
     * Builds and returns a panel where the given radio buttons are laid out
     * in a row.<p>
     * 
     * If class {@code com.jgoodies.jsdl.common.focus.FocusTraversalUtils} 
     * from the JSDL Common library is in the class path, 
     * it is used to group the radio buttons. Focus is transferred to/from 
     * the selected button in a group; and cursor-left/-right change 
     * the selection in the group.
     *
     * @return the built panel
     */
    public static JComponent radioButtonBar(JRadioButton... radioButtons) {
        return buildGroupedButtonBar(radioButtons);
    }


    // Implementation *********************************************************

    /**
     * Builds and returns a button bar that consists of the given buttons.
     * Aims to build a focus group via the {@code FocusTraversalUtils},
     * if in the classpath.
     *
     * @return the built panel
     */
    protected static JComponent buildGroupedButtonBar(AbstractButton... buttons) {
        checkArgument(buttons.length > 1, "You must provide more than one button.");
        FormLayout layout = new FormLayout(
                buttons.length-1 + "*(pref, $rgap), pref",
                "p");
        PanelBuilder builder = new PanelBuilder(layout);
        int columnCount = 1;
        for (AbstractButton button : buttons) {
            builder.add(button, CC.xy(columnCount, 1));
            columnCount += 2;
        }
        FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
        return builder.build();
    }
    
    
}
