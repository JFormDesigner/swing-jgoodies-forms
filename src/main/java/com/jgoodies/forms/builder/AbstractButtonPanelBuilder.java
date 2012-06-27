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

import static com.jgoodies.common.base.Preconditions.checkArgument;
import static com.jgoodies.common.base.Preconditions.checkNotNull;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.internal.FocusTraversalUtilsAccessor;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.util.LayoutStyle;

/**
 * The abstract superclass for {@link ButtonBarBuilder}.
 * Provides a cell cursor for traversing
 * the button bar/stack while components are added. It also offers
 * convenience methods to append logical columns and rows.<p>
 *
 * TODO: Mention the ButtonStackBuilder2 subclass as soon as it is available.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.11 $
 *
 * @since 1.2
 */
public abstract class AbstractButtonPanelBuilder extends AbstractBuilder {


    // Instance Fields ********************************************************

    /**
     * Specifies if we fill the grid from left to right or right to left.
     * This value is initialized during the construction from the layout
     * container's component orientation.
     *
     * @see #isLeftToRight()
     * @see #setLeftToRight(boolean)
     * @see ComponentOrientation
     */
    private boolean leftToRight;


    /**
     * Indicates whether a focus group has been built in {@link #build()}.
     * Reset to {@code false} whenever a component is added.
     */
    protected boolean focusGrouped = false;


    // Instance Creation ****************************************************

    /**
     * Constructs an AbstractButtonPanelBuilder
     * for the given FormLayout and layout container.
     *
     * @param layout     the FormLayout to use
     * @param container  the layout container
     *
     * @throws NullPointerException if {@code layout} or {@code container} is {@code null}
     */
    protected AbstractButtonPanelBuilder(FormLayout layout, JPanel container) {
        super(layout, container);
        opaque(false);
        ComponentOrientation orientation = container.getComponentOrientation();
        leftToRight = orientation.isLeftToRight()
                  || !orientation.isHorizontal();
    }


    // Accessors **************************************************************
    
    /**
     * Returns the panel used to build the form and lazily builds
     * a focus traversal group for all contained AbstractButtons.
     *
     * @return the panel used by this builder to build the form
     */
    public JPanel getPanel() {
    	return build();
    }
    

    /**
     * Returns the panel used to build the form and lazily builds
     * a focus traversal group for all contained AbstractButtons.
     *
     * @return the panel used by this builder to build the form
     */
    public JPanel build() {
    	if (!focusGrouped) {
	    	List<AbstractButton> buttons = new ArrayList<AbstractButton>();
	    	for (Component component : getContainer().getComponents()) {
				if (component instanceof AbstractButton) {
					buttons.add((AbstractButton) component);
				}
			}
	    	FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons.toArray(new AbstractButton[0]));
	    	focusGrouped = true;
    	}
        return (JPanel) getContainer();
    }
    
    
    // Frequently Used Panel Properties ***************************************

    /**
     * Sets the panel's background color and makes the panel opaque.
     *
     * @param background  the color to set as new background
     *
     * @see JComponent#setBackground(Color)
     */
    protected AbstractButtonPanelBuilder background(Color background) {
        getPanel().setBackground(background);
        opaque(true);
        return this;
    }


    /**
     * Sets the panel's border.
     *
     * @param border    the border to set
     *
     * @see JComponent#setBorder(Border)
     */
    protected AbstractButtonPanelBuilder border(Border border) {
    	getPanel().setBorder(border);
        return this;
    }


    /**
     * Sets the panel's opaque state.
     *
     * @param b   true for opaque, false for non-opaque
     *
     * @see JComponent#setOpaque(boolean)
     */
    protected AbstractButtonPanelBuilder opaque(boolean b) {
    	getPanel().setOpaque(b);
        return this;
    }


    /**
     * Sets the panel's background color and makes the panel opaque.
     *
     * @param background  the color to set as new background
     *
     * @see JComponent#setBackground(Color)
     */
    public void setBackground(Color background) {
        getPanel().setBackground(background);
        opaque(true);
    }


    /**
     * Sets the panel's border.
     *
     * @param border    the border to set
     *
     * @see JComponent#setBorder(Border)
     */
    public void setBorder(Border border) {
    	getPanel().setBorder(border);
    }


    /**
     * Sets the panel's opaque state.
     *
     * @param b   true for opaque, false for non-opaque
     *
     * @see JComponent#setOpaque(boolean)
     *
     * @since 1.1
     */
    public void setOpaque(boolean b) {
    	getPanel().setOpaque(b);
    }


    // Accessing the Cursor Direction ***************************************

    /**
     * Returns whether this builder fills the form left-to-right
     * or right-to-left. The initial value of this property is set
     * during the builder construction from the layout container's
     * {@code componentOrientation} property.
     *
     * @return true indicates left-to-right, false indicates right-to-left
     *
     * @see #setLeftToRight(boolean)
     * @see ComponentOrientation
     */
    public final boolean isLeftToRight() {
        return leftToRight;
    }


    /**
     * Sets the form fill direction to left-to-right or right-to-left.
     * The initial value of this property is set during the builder construction
     * from the layout container's {@code componentOrientation} property.
     *
     * @param b   true indicates left-to-right, false right-to-left
     *
     * @see #isLeftToRight()
     * @see ComponentOrientation
     */
    public final void setLeftToRight(boolean b) {
        leftToRight = b;
    }


    // Accessing the Cursor Location and Extent *****************************

    /**
     * Moves to the next column, does the same as #nextColumn(1).
     */
    protected final void nextColumn() {
        nextColumn(1);
    }


    /**
     * Moves to the next column.
     *
     * @param columns	 number of columns to move
     */
    private void nextColumn(int columns) {
        currentCellConstraints.gridX += columns * getColumnIncrementSign();
    }


    protected final int getColumn() {
        return currentCellConstraints.gridX;
    }


    /**
     * Returns the cursor's row.
     *
     * @return the cursor's row
     */
    protected final int getRow() {
        return currentCellConstraints.gridY;
    }


    /**
     * Increases the row by one; does the same as #nextRow(1).
     */
    protected final void nextRow() {
        nextRow(1);
    }


    /**
     * Increases the row by the specified rows.
     *
     * @param rows	 number of rows to move
     */
    private void nextRow(int rows) {
        currentCellConstraints.gridY += rows;
    }


    // Appending Columns ******************************************************

    /**
     * Appends the given column specification to the builder's layout.
     *
     * @param columnSpec  the column specification object to append
     */
    protected final void appendColumn(ColumnSpec columnSpec) {
        getLayout().appendColumn(columnSpec);
    }


    /**
     * Appends a glue column.
     *
     * @see #appendRelatedComponentsGapColumn()
     * @see #appendUnrelatedComponentsGapColumn()
     */
    protected final void appendGlueColumn() {
        appendColumn(FormSpecs.GLUE_COLSPEC);
    }


    /**
     * Appends a column that is the default gap for related components.
     *
     * @see #appendGlueColumn()
     * @see #appendUnrelatedComponentsGapColumn()
     */
    protected final void appendRelatedComponentsGapColumn() {
        appendColumn(FormSpecs.RELATED_GAP_COLSPEC);
    }


    /**
     * Appends a column that is the default gap for unrelated components.
     *
     * @see #appendGlueColumn()
     * @see #appendRelatedComponentsGapColumn()
     */
    protected final void appendUnrelatedComponentsGapColumn() {
        appendColumn(FormSpecs.UNRELATED_GAP_COLSPEC);
    }


    // Appending Rows ********************************************************

    /**
     * Appends the given row specification to the builder's layout.
     *
     * @param rowSpec  the row specification object to append
     */
    protected final void appendRow(RowSpec rowSpec) {
        getLayout().appendRow(rowSpec);
    }


    /**
     * Appends a glue row.
     *
     * @see #appendRelatedComponentsGapRow()
     * @see #appendUnrelatedComponentsGapRow()
     */
    protected final void appendGlueRow() {
        appendRow(FormSpecs.GLUE_ROWSPEC);
    }


    /**
     * Appends a row that is the default gap for related components.
     *
     * @see #appendGlueRow()
     * @see #appendUnrelatedComponentsGapRow()
     */
    protected final void appendRelatedComponentsGapRow() {
        appendRow(FormSpecs.RELATED_GAP_ROWSPEC);
    }


    /**
     * Appends a row that is the default gap for unrelated components.
     *
     * @see #appendGlueRow()
     * @see #appendRelatedComponentsGapRow()
     */
    protected final void appendUnrelatedComponentsGapRow() {
        appendRow(FormSpecs.UNRELATED_GAP_ROWSPEC);
    }


    // Adding Components ****************************************************

    /**
     * Adds a component to the container using the default cell constraints.
     * Note that when building from left to right, this method won't adjust
     * the cell constraints if the column span is larger than 1.
     *
     * @param component	the component to add
     * @return the added component
     */
    protected Component add(Component component) {
        getContainer().add(component, currentCellConstraints);
       focusGrouped = false;
       return component;
    }
    
    
    abstract protected AbstractButtonPanelBuilder addButton(JComponent button);


    /**
     * Adds one or many sequences of related buttons. A new sequence starts
     * when a button is {@code null}. The next sequence is separated by an
     * unrelated gap. 
     * Each button has the minimum width as specified by
     * {@link LayoutStyle#getDefaultButtonWidth()}. The gap width between
     * the buttons is {@link LayoutStyle#getRelatedComponentsPadX()}.<p>
     *
     * Although JButtons are expected, general JComponents are accepted
     * to allow custom button component types.<p>
     * 
     * <strong>Examples:</strong>
     * <pre>
     * builder.addButtons(newButton, editButton, deleteButton);
     * builder.addButtons(newButton, editButton, deleteButton, null, printButton);
     * </pre>
     *
     * @param buttons  the buttons to add
     *
     * @return this builder
     *
     * @throws NullPointerException if {@code buttons} is {@code null}
     * @throws IllegalArgumentException if {@code buttons} is empty
     *
     * @see #addButton(JComponent)
     */
    protected AbstractButtonPanelBuilder addButton(JComponent... buttons) {
        checkNotNull(buttons, "The button array must not be null.");
        checkArgument(buttons.length > 0, "The button array must not be empty.");
        boolean needsGap = false;
        for (JComponent button : buttons) {
            if (button == null) {
                addUnrelatedGap();
                needsGap = false;
                continue;
            }
            if (needsGap) {
                addRelatedGap();
            }
            addButton(button);
            needsGap = true;
        }
        return this;
    }


	/**
	 * Constructs an array of JButtons from the given Action array,
	 * and adds them as a sequence of related buttons separated by a default gap.
	 *
	 * @param actions  an array of buttons to add
	 */
	protected AbstractButtonPanelBuilder addButton(Action... actions) {
	    checkNotNull(actions, "The Action array must not be null.");
	    int length = actions.length;
	    checkArgument(length > 0, "The Action array must not be empty.");
	    JButton[] buttons = new JButton[length];
	    for (int i = 0; i < length; i++) {
	    	Action action = actions[i];
	        buttons[i] = action == null ? null : createButton(action);
	    }
	    return addButton(buttons);
	}
	
	
    /**
     * Adds the standard gap for related components.
     */
	abstract protected AbstractButtonPanelBuilder addRelatedGap();


    /**
     * Adds the standard gap for unrelated components.
     */
    abstract protected AbstractButtonPanelBuilder addUnrelatedGap();


    /**
     * Creates and returns a button that is bound to the given Action.
     * This is a hook that allows to return customized buttons.
     * For example, the JGoodies {@code JGButton} configures
     * the accessible name and accessible description from Actions
     * that provide these information.<p>
     *
     * This default implementation delegates the button creation 
     * to this builder's component factory, 
     * see {@link ComponentFactory#createButton(Action)}).
     *
     * @param action    provides bound visual properties for the button
     * @return the created button
     *
     * @since 1.4
     */
    protected JButton createButton(Action action) {
        return getComponentFactory().createButton(action);
    }


    // Misc *****************************************************************

    /**
     * Returns the sign (-1 or 1) used to increment the cursor's column
     * when moving to the next column.
     *
     * @return -1 for right-to-left, 1 for left-to-right
     */
    private int getColumnIncrementSign() {
        return isLeftToRight() ? 1 : -1;
    }


}
