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

package com.jgoodies.forms.factories;

import static com.jgoodies.common.base.Preconditions.checkArgument;
import static com.jgoodies.common.base.Preconditions.checkNotBlank;
import static com.jgoodies.common.base.Preconditions.checkNotNull;
import static com.jgoodies.common.internal.Messages.MUST_NOT_BE_BLANK;
import static com.jgoodies.common.internal.Messages.MUST_NOT_BE_NULL;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.ButtonStackBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.internal.FocusTraversalUtilsAccessor;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Provides convenience behavior for building forms.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 * 
 * @since 1.6
 */
public final class Forms {

	private Forms() {
		// Overrides default constructor; prevents instantiation.
	}


	// Public API *************************************************************

	/**
	 * Creates and returns a panel where {@code component} is laid out
	 * using a FormLayout with the given column and row specifications.
     * <blockquote><pre>
     * Forms.single(  "left:150dlu",      "c:p",     component);
     * Forms.single(  "fill:150dlu:grow", "f:20dlu", component);
     * Forms.single("center:150dlu",      "b:p:g",   component);
     * </pre></blockquote>
	 * 
	 * @param columnSpec    a FormLayout column specification for a single column
	 * @param rowSpec       a FormLayout row specification for a single row
	 * @param component     the component to lay out
	 * @return the built panel
	 * @throws NullPointerException if {@code columnSpec}, {@code rowSpec},
	 *   or {@code component} is {@code null}
	 * @throws IllegalArgumentException if {@code columnSpec} or {@code rowSpec}
	 *   is empty or whitespace
	 */
	public static JComponent single(String columnSpec, String rowSpec,
			JComponent component) {
		checkNotBlank(columnSpec, MUST_NOT_BE_BLANK, "column specification");
		checkNotBlank(rowSpec,    MUST_NOT_BE_BLANK, "row specification");
		checkNotNull (component,  MUST_NOT_BE_NULL, "component");
		FormLayout layout = new FormLayout(columnSpec, rowSpec);
		PanelBuilder builder = new PanelBuilder(layout);
		builder.add(component, CC.xy(1, 1));
		return builder.build();
	}


	/**
	 * Creates and returns a panel where {@code component} is centered.
	 * The panel grows both horizontally and vertically as its container grows.
     * <blockquote><pre>
     * Forms.centered(anImageLabel);
     * Forms.centered(anAnimatedPanel);
     * </pre></blockquote>
	 * 
	 * @param component     the component to center
	 * @return the built panel
	 * @throws NullPointerException if {@code component} is {@code null}
	 */
	public static JComponent centered(JComponent component) {
		return single("center:pref:grow", "c:p:g", component);
	}


	/**
	 * Creates and returns a panel where {@code component} is surrounded
	 * by the given border.
     * <blockquote><pre>
     * Forms.border(Borders.DLU14, aComponent);
     * Forms.border(new LineBorder(Color.GRAY), aComponent);
     * </pre></blockquote>
	 * 
	 * @param border        the border used to surround the component
	 * @param component     the component to wrap
	 * @return the built panel
	 * @throws NullPointerException if {@code border}  or {@code component}
	 *     is {@code null}
	 */
	public static JComponent border(Border border, JComponent component) {
		JComponent container = single("fill:pref", "f:p", component);
		container.setBorder(border);
		return container;
	}


	/**
	 * Creates and returns a panel where {@code component} is surrounded
	 * by an empty border as described by {@code emptyBorderSpec}.
     * <blockquote><pre>
     * Forms.border("4dlu, 0, 4dlu, 8dlu", aComponent);
     * Forms.border("10px 4px, 10px, 4px", aComponent);
     * </pre></blockquote>
	 * 
	 * @param component     the component to wrap
	 * @return the built panel
	 * @throws NullPointerException if {@code emptyBorderSpec}
	 *     or {@code component} is {@code null}
	 */
	public static JComponent border(String emptyBorderSpec, JComponent component) {
		return border(Borders.createEmptyBorder(emptyBorderSpec), component);
	}


    /**
     * Builds and returns a panel where the given components are laid out
     * horizontally separated by gaps as described by the given
     * FormLayout gap (column) specification.
     * <blockquote><pre>
     * Forms.horizontal("4dlu",  component1, component2);
     * Forms.horizontal("$rgap", component1, component2, component3);
     * Forms.horizontal("0",     component1, component2, component3);
     * </pre></blockquote>
     *
     * @param gapColSpec    describes the horizontal gap between the components
     * @param components    the components to be laid out
     * @return the built panel
     * @throws NullPointerException if {@code components} is {@code null}
     * @throws IllegalArgumentException if {@code components} is empty
     */
	public static JComponent horizontal(String gapColSpec, JComponent... components) {
		checkNotBlank(gapColSpec, MUST_NOT_BE_BLANK, "gap column specification");
		checkNotNull(components, MUST_NOT_BE_NULL, "component array");
        checkArgument(components.length > 1, "You must provide more than one component.");
        FormLayout layout = new FormLayout(
        		components.length - 1 + "*(pref, " + gapColSpec + "), pref",
                "p");
        PanelBuilder builder = new PanelBuilder(layout);
        int column = 1;
        for (JComponent component : components) {
            builder.add(component, CC.xy(column, 1));
            column += 2;
        }
		return builder.build();
	}


    /**
     * Builds and returns a panel where the given components are laid out
     * vertically separated by gaps as described by the given
     * FormLayout gap (row) specification.
     * <blockquote><pre>
     * Forms.vertical("4dlu",  component1, component2);
     * Forms.vertical("$rgap", component1, component2, component3);
     * Forms.vertical("0",     component1, component2, component3);
     * </pre></blockquote>
     *
     * @param gapRowSpec    describes the vertical gap between the components
     * @param components    the components to be laid out
     * @return the built panel
     * @throws NullPointerException if {@code components} is {@code null}
     * @throws IllegalArgumentException if {@code components} is empty
     */
	public static JComponent vertical(String gapRowSpec, JComponent... components) {
		checkNotBlank(gapRowSpec, MUST_NOT_BE_BLANK, "gap row specification");
		checkNotNull(components, MUST_NOT_BE_NULL, "component array");
        checkArgument(components.length > 1, "You must provide more than one component.");
        FormLayout layout = new FormLayout(
        		"pref",
        		components.length - 1 + "*(p, " + gapRowSpec + "), p");
        PanelBuilder builder = new PanelBuilder(layout);
        int row = 1;
        for (JComponent component : components) {
            builder.add(component, CC.xy(1, row));
            row += 2;
        }
		return builder.build();
	}


	/**
	 * Creates and returns a panel where the given buttons
	 * are laid out horizontally using a ButtonBarBuilder.
	 * Equivalent to:
     * <blockquote><pre>
     * ButtonBarBuilder.create().addButton(buttons).build();
     * </pre></blockquote><p>
     *
     * If class {@code com.jgoodies.jsdl.common.focus.FocusTraversalUtils}
     * from the JSDL Common library is in the class path,
     * it is used to build a focus group for the buttons. Focus is transferred
     * between the buttons with cursor-left/-right.
	 * 
	 * @param buttons  the buttons to add to the button bar
	 * @return the built button bar
     * @throws NullPointerException if {@code buttons} is {@code null}
     * @throws IllegalArgumentException if {@code buttons} is empty
	 * @see ButtonBarBuilder
	 */
	public static JComponent buttonBar(JComponent... buttons) {
		return ButtonBarBuilder.create()
			.addButton(buttons)
			.build();
	}


    /**
     * Creates and returns a panel where the given buttons
     * are laid out vertically using a ButtonStackBuilder.
     * Equivalent to:
     * <blockquote><pre>
     * ButtonStackBuilder.create().addButton(buttons).build();
     * </pre></blockquote><p>
     *
     * If class {@code com.jgoodies.jsdl.common.focus.FocusTraversalUtils}
     * from the JSDL Common library is in the class path,
     * it is used to build a focus group for the buttons. Focus is transferred
     * between the buttons with cursor-left/-right.
     * 
     * @param buttons  the buttons to add to the button stack
     * @return the built button stack
     * @throws NullPointerException if {@code buttons} is {@code null}
     * @throws IllegalArgumentException if {@code buttons} is empty
     * @see ButtonStackBuilder
     * 
     * @since 1.8
     */
    public static JComponent buttonStack(JComponent... buttons) {
        return ButtonStackBuilder.create()
            .addButton(buttons)
            .build();
    }


    /**
     * Builds and returns a panel where the given check boxes
     * are laid out horizontally.<p>
     *
     * If class {@code com.jgoodies.jsdl.common.focus.FocusTraversalUtils}
     * from the JSDL Common library is in the class path,
     * it is used to group the radio buttons. Focus is transferred to/from
     * the selected button in a group; and cursor-left/-right change
     * the selection in the group.
     *
	 * @param checkBoxes  the check boxes to lay out in a row
     * @return the built panel
     * @throws NullPointerException if {@code checkBoxes} is {@code null}
     * @throws IllegalArgumentException if {@code checkBoxes} is empty
     */
	public static JComponent checkBoxBar(JCheckBox... checkBoxes) {
	    return buildGroupedButtonBar(checkBoxes);
	}


    /**
     * Builds and returns a panel where the given check boxes
     * are laid out vertically.<p>
     *
     * If class {@code com.jgoodies.jsdl.common.focus.FocusTraversalUtils}
     * from the JSDL Common library is in the class path,
     * it is used to group the radio buttons. Focus is transferred to/from
     * the selected button in a group; and cursor-left/-right change
     * the selection in the group.
     *
     * @param checkBoxes  the check boxes to lay out in a stack
     * @return the built panel
     * @throws NullPointerException if {@code checkBoxes} is {@code null}
     * @throws IllegalArgumentException if {@code checkBoxes} is empty
     * 
     * @since 1.8
     */
    public static JComponent checkBoxStack(JCheckBox... checkBoxes) {
        return buildGroupedButtonStack(checkBoxes);
    }


    /**
     * Builds and returns a panel where the given radio buttons
     * are laid horizontally.<p>
     *
     * If class {@code com.jgoodies.jsdl.common.focus.FocusTraversalUtils}
     * from the JSDL Common library is in the class path,
     * it is used to group the radio buttons. Focus is transferred to/from
     * the selected button in a group; and cursor-left/-right change
     * the selection in the group.
     *
	 * @param radioButtons  the radio buttons to lay out in a row
     * @return the built panel
     * @throws NullPointerException if {@code radioButtons} is {@code null}
     * @throws IllegalArgumentException if {@code radioButtons} is empty
     */
    public static JComponent radioButtonBar(JRadioButton... radioButtons) {
        return buildGroupedButtonBar(radioButtons);
    }


    /**
     * Builds and returns a panel where the given radio buttons
     * are laid vertically.<p>
     *
     * If class {@code com.jgoodies.jsdl.common.focus.FocusTraversalUtils}
     * from the JSDL Common library is in the class path,
     * it is used to group the radio buttons. Focus is transferred to/from
     * the selected button in a group; and cursor-left/-right change
     * the selection in the group.
     *
     * @param radioButtons  the radio buttons to lay out in a stack
     * @return the built panel
     * @throws NullPointerException if {@code radioButtons} is {@code null}
     * @throws IllegalArgumentException if {@code radioButtons} is empty
     * 
     * @since 1.8
     */
    public static JComponent radioButtonStack(JRadioButton... radioButtons) {
        return buildGroupedButtonStack(radioButtons);
    }


    // Implementation *********************************************************

    /**
     * Builds and returns a button bar that consists of the given buttons.
     * Aims to build a focus group via the {@code FocusTraversalUtils},
     * if in the class path.
     *
     * @return the built panel
     */
    private static JComponent buildGroupedButtonBar(AbstractButton... buttons) {
        checkArgument(buttons.length > 1, "You must provide more than one button.");
        FormLayout layout = new FormLayout(
                String.format("pref, %s*($rgap, pref)", buttons.length - 1),
                "p");
        PanelBuilder builder = new PanelBuilder(layout);
        int column = 1;
        for (AbstractButton button : buttons) {
            builder.add(button, CC.xy(column, 1));
            column += 2;
        }
        FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
        return builder.build();
    }


    /**
     * Builds and returns a button bar that consists of the given buttons.
     * Aims to build a focus group via the {@code FocusTraversalUtils},
     * if in the class path.
     *
     * @return the built panel
     */
    private static JComponent buildGroupedButtonStack(AbstractButton... buttons) {
        checkArgument(buttons.length > 1, "You must provide more than one button.");
        FormLayout layout = new FormLayout(
                "pref",
                String.format("p, %s*(0, p)", buttons.length - 1));
        PanelBuilder builder = new PanelBuilder(layout);
        int row = 1;
        for (AbstractButton button : buttons) {
            builder.add(button, CC.xy(1, row));
            row += 2;
        }
        FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
        return builder.build();
    }


}
