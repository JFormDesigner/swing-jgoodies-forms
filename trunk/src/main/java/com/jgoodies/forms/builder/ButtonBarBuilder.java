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
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.util.LayoutStyle;

/**
 * A non-visual builder for building consistent button bars that comply
 * with popular style guides. Utilizes the JGoodies {@link FormLayout}
 * and honors the platform's {@link LayoutStyle} regarding button sizes,
 * and gaps.<p>
 *
 * <strong>Examples:</strong><pre>
 * // 1) Build and return a bar with three related buttons 
 * return new ButtonBarBuilder()
 *     .addButton(newButton)
 *     .addRelatedGap()
 *     .addButton(editButton)
 *     .addRelatedGap()
 *     .addButton(deleteButton)
 *     .build();
 *
 * // 2) Short hand for example 1) 
 * return new ButtonBarBuilder()
 *     .addButton(newButton, editButton, deleteButton)
 *     .build();
 *
 * // 3) Build and return a bar with two sections
 * return new ButtonBarBuilder()
 *     .addButton(newButton, editButton, deleteButton)
 *     .addUnrelatedGap()
 *     .addButton(moveUpButton, moveDownButton)
 *     .build();
 *
 * // 4) Short hand for example 3)
 * return new ButtonBarBuilder()
 *     .addButton(newButton, editButton, deleteButton, 
 *                null, 
 *                moveUpButton, moveDownButton)
 *     .build();
 *
 * // 5) Build and return a complex button bar
 * return new ButtonBarBuilder()
 *     .addButton(newButton, editButton, deleteButton)
 *     .addUnrelatedGap()
 *     .addButton(moveUpButton, moveDownButton)
 *     .addGlue()
 *     .addGrowing(legendComponent)
 *     .build();
 * </pre>
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.18 $
 *
 * @see ButtonStackBuilder
 * @see com.jgoodies.forms.util.LayoutStyle
 */
public final class ButtonBarBuilder extends AbstractButtonPanelBuilder {

    /**
     * Specifies the columns of the initial FormLayout used in constructors.
     */
    private static final ColumnSpec[] COL_SPECS  =
        new ColumnSpec[]{};

    /**
     * Specifies the FormLayout's the single button bar row.
     */
    private static final RowSpec[] ROW_SPECS  =
        new RowSpec[]{ RowSpec.decode("center:pref") };
    
    
    // Instance Creation ******************************************************

    /**
     * Constructs an empty ButtonBarBuilder on a JPanel.
     */
    public ButtonBarBuilder() {
        this(new JPanel(null));
    }


    /**
     * Constructs an empty ButtonBarBuilder on the given panel.
     *
     * @param panel  the layout container
     */
    public ButtonBarBuilder(JPanel panel) {
        super(new FormLayout(COL_SPECS, ROW_SPECS), panel);
    }


    // Buttons ****************************************************************

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
    public ButtonBarBuilder addButton(JComponent button) {
        checkNotNull(button, "The button to add must not be null.");
        getLayout().appendColumn(FormSpecs.BUTTON_COLSPEC);
        add(button);
        nextColumn();
        return this;
    }


    @Override
    public ButtonBarBuilder addButton(JComponent... buttons) {
        super.addButton(buttons);
        return this;
    }


    @Override
	public ButtonBarBuilder addButton(Action... actions) {
	    super.addButton(actions);
	    return this;
	}



    // Other ******************************************************************

    /**
     * Adds a fixed size component with narrow margin. Unlike the buttons,
     * this component is laid out without a minimum width. In other words,
     * the width is determined only by the component's preferred width.
     *
     * @param component  the component to add
     *
     * @return this builder
     */
    public ButtonBarBuilder addFixed(JComponent component) {
        getLayout().appendColumn(FormSpecs.PREF_COLSPEC);
        add(component);
        nextColumn();
        return this;
    }


	/**
     * Adds a component that grows if the container grows.
     * The component's initial size (before it grows) is specified
     * by the {@link LayoutStyle#getDefaultButtonWidth()}.
     *
     * @param component  the component to add
     *
     * @return this builder
     */
    public ButtonBarBuilder addGrowing(JComponent component) {
        getLayout().appendColumn(FormSpecs.GROWING_BUTTON_COLSPEC);
        add(component);
        nextColumn();
        return this;
    }


    // Spacing ****************************************************************

    /**
     * Adds a glue that will be given the extra space,
     * if this button bar is larger than its preferred size.
     *
     * @return this builder
     */
    public ButtonBarBuilder addGlue() {
        appendGlueColumn();
        nextColumn();
        return this;
    }


    /**
     * Adds the standard horizontal gap for related components.
     *
     * @return this builder
     *
     * @see LayoutStyle#getRelatedComponentsPadX()
     */
    @Override
    public ButtonBarBuilder addRelatedGap() {
        appendRelatedComponentsGapColumn();
        nextColumn();
        return this;
    }


    /**
     * Adds the standard horizontal gap for unrelated components.
     *
     * @return this builder
     *
     * @see LayoutStyle#getUnrelatedComponentsPadX()
     */
   @Override
   public ButtonBarBuilder addUnrelatedGap() {
        appendUnrelatedComponentsGapColumn();
        nextColumn();
        return this;
    }


    /**
     * Adds a horizontal strut of the specified width.
     * For related and unrelated components use {@link #addRelatedGap()}
     * and {@link #addUnrelatedGap()} respectively.
     *
     * @param width  describes the gap width
     *
     * @return this builder
     *
     * @see ColumnSpec#createGap(ConstantSize)
     */
    public ButtonBarBuilder addStrut(ConstantSize width) {
        getLayout().appendColumn(ColumnSpec.createGap(width));
        nextColumn();
        return this;
    }


    // Configuration **********************************************************
    
    @Override
    public ButtonBarBuilder background(Color background) {
        super.background(background);
        return this;
    }


    @Override
    public ButtonBarBuilder border(Border border) {
        super.border(border);
        return this;
    }


    @Override
    public ButtonBarBuilder opaque(boolean b) {
    	super.opaque(b);
    	return this;
    }
    
    
}
