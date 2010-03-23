/*
 * Copyright (c) 2002-2010 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import java.awt.ComponentOrientation;
import java.awt.Container;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * An abstract class that minimizes the effort required to implement
 * non-visual builders that use the {@link FormLayout}.<p>
 *
 * Builders hide details of the FormLayout and provide convenience behavior
 * that assists you in constructing a form, bar, stack.
 * This class provides a cell cursor that helps you traverse a form while
 * you add components. Also, it offers several methods to append custom
 * and logical columns and rows.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 *
 * @see    ButtonBarBuilder2
 * @see    ButtonStackBuilder
 * @see    PanelBuilder
 * @see    I15dPanelBuilder
 * @see    DefaultFormBuilder
 */
public abstract class AbstractBuilder {

    /**
     * Holds the layout container that we are building.
     */
    private final Container  container;

    /**
     * Holds the instance of <code>FormLayout</code> that is used
     * to specify, fill and layout this form.
     */
    private final FormLayout layout;

    /**
     * Holds an instance of <code>CellConstraints</code> that will be used to
     * specify the location, extent and alignments of the component to be
     * added next.
     */
    private final CellConstraints currentCellConstraints;

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



    // Instance Creation ****************************************************

    /**
     * Constructs an AbstractFormBuilder
     * for the given FormLayout and layout container.
     *
     * @param layout     the FormLayout to use
     * @param container  the layout container
     *
     * @throws NullPointerException if {@code layout} or {@code container} is {@code null}
     */
    protected AbstractBuilder(FormLayout layout, Container container) {
        this.layout    = checkNotNull(layout, "The layout must not be null.");
        this.container = checkNotNull(container, "The layout container must not be null.");
        container.setLayout(layout);
        currentCellConstraints = new CellConstraints();
        ComponentOrientation orientation = container.getComponentOrientation();
        leftToRight = orientation.isLeftToRight()
                  || !orientation.isHorizontal();
    }


    // Accessors ************************************************************

    /**
     * Returns the container used to build the form.
     *
     * @return the layout container
     */
    public final Container getContainer() {
        return container;
    }


    /**
     * Returns the instance of {@link FormLayout} used to build this form.
     *
     * @return the FormLayout
     */
    public final FormLayout getLayout() {
        return layout;
    }


    /**
     * Returns the number of columns in the form.
     *
     * @return the number of columns
     */
    public final int getColumnCount() {
        return getLayout().getColumnCount();
    }


    /**
     * Returns the number of rows in the form.
     *
     * @return the number of rows
     */
    public final int getRowCount() {
        return getLayout().getRowCount();
    }


    // Accessing the Cursor Direction ***************************************

    /**
     * Returns whether this builder fills the form left-to-right
     * or right-to-left. The initial value of this property is set
     * during the builder construction from the layout container's
     * <code>componentOrientation</code> property.
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
     * from the layout container's <code>componentOrientation</code> property.
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
     * Returns the cursor's column.
     *
     * @return the cursor's column
     */
    public final int getColumn() {
        return currentCellConstraints.gridX;
    }


    /**
     * Sets the cursor to the given column.
     *
     * @param column    the cursor's new column index
     */
    public final void setColumn(int column) {
        currentCellConstraints.gridX = column;
    }


    /**
     * Returns the cursor's row.
     *
     * @return the cursor's row
     */
    public final int getRow() {
        return currentCellConstraints.gridY;
    }


    /**
     * Sets the cursor to the given row.
     *
     * @param row       the cursor's new row index
     */
    public final void setRow(int row) {
        currentCellConstraints.gridY = row;
    }


    /**
     * Sets the cursor's column span.
     *
     * @param columnSpan    the cursor's new column span (grid width)
     */
    public final void setColumnSpan(int columnSpan) {
        currentCellConstraints.gridWidth = columnSpan;
    }


    /**
     * Sets the cursor's row span.
     *
     * @param rowSpan    the cursor's new row span (grid height)
     */
    public final void setRowSpan(int rowSpan) {
        currentCellConstraints.gridHeight = rowSpan;
    }


    /**
     * Sets the cursor's origin to the given column and row.
     *
     * @param column 	the new column index
     * @param row		the new row index
     */
    public final void setOrigin(int column, int row) {
        setColumn(column);
        setRow(row);
    }


    /**
     * Sets the cursor's extent to the given column span and row span.
     *
     * @param columnSpan    the new column span (grid width)
     * @param rowSpan       the new row span (grid height)
     */
    public final void setExtent(int columnSpan, int rowSpan) {
        setColumnSpan(columnSpan);
        setRowSpan(rowSpan);
    }


    /**
     * Sets the cell bounds (location and extent) to the given column, row,
     * column span and row span.
     *
     * @param column       the new column index (grid x)
     * @param row          the new row index	 (grid y)
     * @param columnSpan   the new column span  (grid width)
     * @param rowSpan      the new row span     (grid height)
     */
    public final void setBounds(int column, int row, int columnSpan, int rowSpan) {
        setColumn(column);
        setRow(row);
        setColumnSpan(columnSpan);
        setRowSpan(rowSpan);
    }


    /**
     * Moves to the next column, does the same as #nextColumn(1).
     */
    public final void nextColumn() {
        nextColumn(1);
    }


    /**
     * Moves to the next column.
     *
     * @param columns	 number of columns to move
     */
    public final void nextColumn(int columns) {
        currentCellConstraints.gridX += columns * getColumnIncrementSign();
    }


    /**
     * Increases the row by one; does the same as #nextRow(1).
     */
    public final void nextRow() {
        nextRow(1);
    }


    /**
     * Increases the row by the specified rows.
     *
     * @param rows	 number of rows to move
     */
    public final void nextRow(int rows) {
        currentCellConstraints.gridY += rows;
    }


    /**
     * Moves to the next line: increases the row and resets the column;
     * does the same as #nextLine(1).
     */
    public final void nextLine() {
        nextLine(1);
    }


    /**
     * Moves the cursor down several lines: increases the row by the
     * specified number of lines and sets the cursor to the leading column.
     *
     * @param lines  number of rows to move
     */
    public final void nextLine(int lines) {
        nextRow(lines);
        setColumn(getLeadingColumn());
    }


    // Form Constraints Alignment *******************************************

    /**
     * Returns the CellConstraints object that is used as a cursor and
     * holds the current column span and row span.
     *
     * @return the builder's current {@link CellConstraints} object
     */
    protected final CellConstraints cellConstraints() {
        return currentCellConstraints;
    }


    /**
     * Returns the index of the leading column.<p>
     *
     * Subclasses may override this method, for example, if the form
     * has a leading gap column that should not be filled with components.
     *
     * @return the leading column
     */
    protected int getLeadingColumn() {
        return isLeftToRight() ? 1 : getColumnCount();
    }


    /**
     * Returns the sign (-1 or 1) used to increment the cursor's column
     * when moving to the next column.
     *
     * @return -1 for right-to-left, 1 for left-to-right
     */
    protected final int getColumnIncrementSign() {
        return isLeftToRight() ? 1 : -1;
    }


    /**
     * Creates and returns a <code>CellConstraints</code> object at
     * the current cursor position that uses the given column span
     * and is adjusted to the left. Useful when building from right to left.
     *
     * @param columnSpan   the column span to be used in the constraints
     * @return CellConstraints adjusted to the left hand side
     */
    protected final CellConstraints createLeftAdjustedConstraints(int columnSpan) {
        int firstColumn = isLeftToRight()
                            ? getColumn()
                            : getColumn() + 1 - columnSpan;
        return new CellConstraints(firstColumn, getRow(),
                                    columnSpan,
                                    cellConstraints().gridHeight);
    }


}
