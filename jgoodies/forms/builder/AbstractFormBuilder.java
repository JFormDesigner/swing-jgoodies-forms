/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch. 
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.builder;

import java.awt.Component;
import java.awt.Container;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * An abstract class that minimizes the effort required to implement 
 * non-visual builders that use the {@link FormLayout}.
 * <p>
 * Builders hide details of the FormLayout and provide convenience behavior 
 * that assists you in constructing a form.
 * This class provides a cell cursor that helps you traverse a form while
 * you fill in components.
 *
 * @author Karsten Lentzsch
 * @see    ButtonBarBuilder
 * @see    ButtonStackBuilder
 * @see    PanelBuilder
 * @see    com.jgoodies.forms.extras.I15dPanelBuilder
 * @see    com.jgoodies.forms.extras.DefaultFormBuilder
 */
public abstract class AbstractFormBuilder {

    /**
     * Holds the layout container that we are building.
     */
    private final Container  container;
    
    /**
     * Holds the instance of <code>FormLayout</code> that is used to
     * specifiy, fill and layout this form.
     */
    private final FormLayout layout;

    /**
     * Holds an instance of <code>CellConstraints</code> that will be used to
     * specify the location, extent and alignments of the component to be
     * added next.
     */
    private CellConstraints currentCellConstraints;
    
    /**
     * Specifies if we fill the grid from left to right or right to left.
     */
    private boolean leftToRight = true;



    // Instance Creation ****************************************************

    /**
     * Constructs an instance of <code>AbstractFormBuilder</code> for the given
     * container and form layout.
     * 
     * @param container  the layout container
     * @param layout     the {@link FormLayout} to use
     */
    public AbstractFormBuilder(Container container, FormLayout layout){        
        this.container = container;
        this.layout    = layout;
  
        container.setLayout(layout);
        currentCellConstraints = new CellConstraints();
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
     * or right-to-left.
     * 
     * @return true indicates left-to-right, false indicates right-to-left
     */
    public final boolean isLeftToRight() {
        return leftToRight;
    }
    
    /**
     * Sets the form fill direction to left-to-right or right-to-left.
     * 
     * @param b   true indicates left-to-right, false right-to-left
     */
    public final void setLeftToRight(boolean b) {
        this.leftToRight = b;
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
     * @param row          the new row index	  (grid y)
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
     * Sets the horizontal alignment.
     * 
     * @param alignment the new horizontal alignment
     */
    public final void setHAlignment(CellConstraints.Alignment alignment) {
        currentCellConstraints.hAlign = alignment;
    }

    /**
     * Sets the vertical alignment.
     * 
     * @param alignment the new vertical alignment
     */
    public final void setVAlignment(CellConstraints.Alignment alignment) {
        currentCellConstraints.vAlign = alignment;
    }
    
    /**
     * Sets the horizontal and vertical alignment.
     * 
     * @param hAlign the new horizontal alignment
     * @param vAlign the new vertical alignment
     */
    public final void setAlignment(CellConstraints.Alignment hAlign,
                                    CellConstraints.Alignment vAlign) {
        setHAlignment(hAlign);
        setVAlignment(vAlign);
    }

    
    // Adding Columns and Rows **********************************************

    /**
     * Appends the given column specification to the builder's layout.
     * 
     * @param columnSpec  the column specification to append
     */
    public final void appendColumn(ColumnSpec columnSpec) {
        getLayout().appendColumn(columnSpec);
    }

    /**
     * Appends a column specification to the builder's layout 
     * that represents the given string encoding.
     * 
     * @param encodedColumnSpec  the column specification to append
     */
    public final void appendColumn(String encodedColumnSpec) {
        appendColumn(new ColumnSpec(encodedColumnSpec));
    }

    /**
     * Appends a glue column.
     */
    public final void appendGlueColumn() {
        appendColumn(FormFactory.GLUE_COLSPEC);
    }
    
    /**
     * Appends a column that is the default gap for related components.
     */
    public final void appendRelatedComponentsGapColumn() {
        appendColumn(FormFactory.RELATED_GAP_COLSPEC);
    }
    
    /**
     * Appends a column that is the default gap for unrelated components.
     */
    public final void appendUnrelatedComponentsGapColumn() {
        appendColumn(FormFactory.UNRELATED_GAP_COLSPEC);
    }
    
    /**
     * Appends the given row specification to the builder's layout.
     * 
     * @param rowSpec  the row specification to append
     */
    public final void appendRow(RowSpec rowSpec) {
        getLayout().appendRow(rowSpec);
    }

    /**
     * Appends a row specification to the builder's layout that represents
     * the given string encoding.
     * 
     * @param encodedRowSpec  the row specification to append
     */
    public final void appendRow(String encodedRowSpec) {
        appendRow(new RowSpec(encodedRowSpec));
    }

    /**
     * Appends a glue row.
     */
    public final void appendGlueRow() {
        appendRow(FormFactory.GLUE_ROWSPEC);
    }
    
    /**
     * Appends a row that is the default gap for related components.
     */
    public final void appendRelatedComponentsGapRow() {
        appendRow(FormFactory.RELATED_GAP_ROWSPEC);
    }
    
    /**
     * Appends a row that is the default gap for unrelated components.
     */
    public final void appendUnrelatedComponentsGapRow() {
        appendRow(FormFactory.UNRELATED_GAP_ROWSPEC);
    }
    

    // Adding Components ****************************************************

    /**
     * Adds a component to the panel using the given cell constraints.
     * 
     * @param component        the component to add
     * @param cellConstraints  the component's cell constraints
     * @return the added component
     */
    public final Component add(Component component, CellConstraints cellConstraints) {
        container.add(component, cellConstraints);   
        return component; 
    }
    
    /**
     * Adds a component to the panel using the given encoded cell constraints.
     * 
     * @param component               the component to add
     * @param encodedCellConstraints  the component's encoded cell constraints
     * @return the added component
     */
    public final Component add(Component component, String encodedCellConstraints) {
        container.add(component, new CellConstraints(encodedCellConstraints));
        return component;    
    }
    
    /**
     * Adds a component to the container using the default cell constraints.
     * 
     * @param component	the component to add
     * @return the added component
     */
    public final Component add(Component component) {
        add(component, currentCellConstraints);
        return component;
    }
    
    
    // Misc *****************************************************************
    
    /**
     * Returns the cell constraints.
     * 
     * @return the builder's current {@link CellConstraints}
     */
    protected final CellConstraints cellConstraints() {
        return currentCellConstraints;
    }
    
    /**
     * Returns the leading column.
     * <p>
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
    

}
