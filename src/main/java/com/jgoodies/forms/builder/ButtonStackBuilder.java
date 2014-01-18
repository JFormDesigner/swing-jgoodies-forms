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

package com.jgoodies.forms.builder;

import static com.jgoodies.common.base.Preconditions.checkNotNull;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.util.LayoutStyle;

/**
 * Builds consistent button stacks that comply with popular style guides.
 * Utilizes the JGoodies {@link FormLayout} and honors the platform's
 * {@link LayoutStyle} regarding button sizes, and gaps.<p>
 *
 * This builder sets a hint for narrow  margin for the gridded buttons.
 * This can reduce the button stack's width if some buttons have long texts.
 * For example, a stack with 'OK', 'Cancel', 'Configure&hellip;' will likely
 * exceed the minimum button width. The narrow margins help getting narrow
 * stacks.
 * Note that some look&amp;feels do not support the narrow margin feature,
 * and conversely, others have only narrow margins. The JGoodies look&amp;feels
 * honor the setting, the Mac Aqua l&amp;f uses narrow margins all the time.<p>
 *
 * <strong>Example:</strong><br>
 * The following example builds a button stack with <i>Close, Up</i> and
 * <i>Down</i>, where Up and Down are related, and Close is not related
 * to the other buttons, which makes a wide gap for the unrelated and
 * a smaller gap for the related buttons.
 * <pre>
 * private JPanel createCloseUpDownButtonStack(
 *         JButton close, JButton up, JButton down) {
 *     return ButtonStackBuilder().create()
 *     		.addGridded(close)
 *     		.addUnrelatedGap()
 *     		.addGridded(up)
 *     		.addRelatedGap()
 *     		.addGridded(down)
 *     		.build();
 * }
 * </pre>
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.15 $
 *
 * @see ButtonBarBuilder
 * @see com.jgoodies.forms.util.LayoutStyle
 */
public final class ButtonStackBuilder extends AbstractButtonPanelBuilder {

    /**
     * Specifies the FormLayout's the single button stack column.
     */
    private static final ColumnSpec[] COL_SPECS =
        new ColumnSpec[] { FormSpecs.BUTTON_COLSPEC };

    /**
     * Specifies the rows of the initial FormLayout used in constructors.
     */
    private static final RowSpec[] ROW_SPECS =
        new RowSpec[]{};


    // Instance Creation ****************************************************

    /**
     * Constructs a ButtonStackBuilder  on a default JPanel
     * using a pre-configured FormLayout as layout manager.
     */
    public ButtonStackBuilder() {
        this(new JPanel(null));
    }


    /**
     * Constructs a ButtonStackBuilder on the given panel
     * using a pre-configured FormLayout as layout manager.
     *
     * @param panel   the layout container
     */
    public ButtonStackBuilder(JPanel panel) {
        super(new FormLayout(COL_SPECS, ROW_SPECS), panel);
    }
    
    
    /**
     * Creates and returns an empty ButtonStackBuilder.
     * 
     * @return the created builder
     * @since 1.8
     */
    public static ButtonStackBuilder create() {
        return new ButtonStackBuilder();
    }


    // Adding Components ****************************************************

    /**
     * Adds a button component that has a minimum width
     * specified by the {@link LayoutStyle#getDefaultButtonWidth()}.<p>
     *
     * Although a JButton is expected, any JComponent is accepted
     * to allow custom button component types.
     *
     * @param button  the component to add
     *
     * @return this builder
     *
     * @throws NullPointerException if {@code button} is {@code null}
     */
    @Override
	public ButtonStackBuilder addButton(JComponent button) {
        checkNotNull(button, "The button must not be null.");
	    getLayout().appendRow(FormSpecs.PREF_ROWSPEC);
	    add(button);
	    nextRow();
	    return this;
	}


	@Override
	public ButtonStackBuilder addButton(JComponent... buttons) {
        super.addButton(buttons);
        return this;
	}


	// Convenience Methods ***************************************************

	@Override
	public ButtonStackBuilder addButton(Action... actions) {
	    super.addButton(actions);
	    return this;
	}


    /**
     * Adds a fixed size component.
     *
     * @param component  the component to add
     */
    public ButtonStackBuilder addFixed(JComponent component) {
        getLayout().appendRow(FormSpecs.PREF_ROWSPEC);
        add(component);
        nextRow();
        return this;
    }


    // Spacing ****************************************************************

    /**
     * Adds a glue that will be given the extra space,
     * if this box is larger than its preferred size.
     */
    public ButtonStackBuilder addGlue() {
        appendGlueRow();
        nextRow();
        return this;
    }


    @Override
    public ButtonStackBuilder addRelatedGap() {
        appendRelatedComponentsGapRow();
        nextRow();
        return this;
    }


    @Override
    public ButtonStackBuilder addUnrelatedGap() {
        appendUnrelatedComponentsGapRow();
        nextRow();
        return this;
    }


    /**
     * Adds a strut of a specified size.
     *
     * @param size  a constant that describes the gap
     */
    public ButtonStackBuilder addStrut(ConstantSize size) {
        getLayout().appendRow(new RowSpec(RowSpec.TOP,
                                          size,
                                          FormSpec.NO_GROW));
        nextRow();
        return this;
    }


    // Configuration **********************************************************

    @Override
    public ButtonStackBuilder background(Color background) {
        super.background(background);
        return this;
    }


    @Override
    public ButtonStackBuilder border(Border border) {
        super.border(border);
        return this;
    }


    @Override
    public ButtonStackBuilder opaque(boolean b) {
    	super.opaque(b);
    	return this;
    }


}
