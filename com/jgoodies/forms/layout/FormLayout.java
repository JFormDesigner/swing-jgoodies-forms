/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch. 
 * Use is subject to license terms.
 *
 */
 
package com.jgoodies.forms.layout;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * <code>FormLayout</code> is a powerful, flexible and precise general
 * purpose layout manager. It aligns components vertically and horizontally in
 * a dynamic rectangular grid of cells, with each component occupying one or
 * more cells.
 * An article about the FormLayout is part of the product documentation
 * and is available online, see
 * <a href="http://www.jgoodies.com/articles/forms.pdf" >
 * http://www.jgoodies.com/articles/forms.pdf</a>.
 * <p>
 * To use <code>FormLayout</code> you first define the grid by specifying the
 * columns and rows. In a second step you add components to the grid. You can
 * specify columns and rows via human-readable String descriptions or via
 * arrays of {@link ColumnSpec} and {@link RowSpec} instances.
 * <p>
 * Each component managed by a FormLayout is associated with an instance of
 * {@link CellConstraints}. The constraints object specifies where a component
 * should be located on the form's grid and how the component should be
 * positioned. In addition to its constraints object the
 * <code>FormLayout</code> also considers each component's minimum and
 * preferred sizes in order to determine a component's size.
 * <p>
 * FormLayout has been designed to work with non-visual builders that help you
 * specify the layout and fill the grid. For example, the 
 * {@link com.jgoodies.forms.builder.ButtonBarBuilder} assists you in building button
 * bars; it creates a standardized FormLayout and provides a minimal API that
 * specializes in adding buttons. Other builders can create frequently used
 * panel design, for example a form that consists of rows of label-component
 * pairs.
 * <p> 
 * FormLayout has been prepared to work with different types of sizes as
 * defined by the {@link Size} interface.
 * <p>
 * <b>Example 1</b> (Plain FormLayout):<br>
 * The following example creates a panel with 3 data columns and 3 data rows; 
 * the columns and rows are specified before components are added 
 * to the form.
 * <pre>
 * FormLayout layout = new FormLayout(
 *      "right:pref, 6dlu, 50dlu, 4dlu, default",  // columns 
 *      "pref, 3dlu, pref, 3dlu, pref");           // rows
 *
 * CellConstraints cc = new CellConstraints();
 * JPanel panel = new JPanel(layout);
 * panel.add(new JLabel("Label1"),   cc.xy  (1, 1));
 * panel.add(new JTextField(),       cc.xywh(3, 1, 3, 1));
 * panel.add(new JLabel("Label2"),   cc.xy  (1, 3));
 * panel.add(new JTextField(),       cc.xy  (3, 3));
 * panel.add(new JLabel("Label3"),   cc.xy  (1, 5));
 * panel.add(new JTextField(),       cc.xy  (3, 5));
 * panel.add(new JButton("..."),     cc.xy  (5, 5));
 * return panel;
 * </pre>
 * <p>
 * <b>Example 2</b> (Using PanelBuilder):<br>
 * This example creates the same panel as above using the 
 * {@link com.jgoodies.forms.builder.PanelBuilder} to add components to the form. 
 * <pre>
 * FormLayout layout = new FormLayout(
 *      "right:pref, 6dlu, 50dlu, 4dlu, default",  // columns 
 *      "pref, 3dlu, pref, 3dlu, pref");           // rows
 *
 * PanelBuilder builder = new PanelBuilder(layout);
 * CellConstraints cc = new CellConstraints();
 * builder.addLabel("Label1",        cc.xy  (1, 1));
 * builder.add(new JTextField(),     cc.xywh(3, 1, 3, 1));
 * builder.addLabel("Label2",        cc.xy  (1, 3));
 * builder.add(new JTextField(),     cc.xy  (3, 3));
 * builder.addLabel("Label3",        cc.xy  (1, 5));
 * builder.add(new JTextField(),     cc.xy  (3, 5));
 * builder.add(new JButton("..."),   cc.xy  (5, 5));
 * return builder.getPanel();
 * </pre>
 * <p>
 * <b>Example 3</b> (Using DefaultFormBuilder):<br>
 * This example utilizes the 
 * {@link com.jgoodies.forms.extras.DefaultFormBuilder} that 
 * ships with the source distribution. 
 * <pre>
 * FormLayout layout = new FormLayout(
 *      "right:pref, 6dlu, 50dlu, 4dlu, default",  // columns 
 *      "");                                       // add rows dynamically
 *
 * DefaultFormBuilder builder = new DefaultFormBuilder(layout);
 * builder.append("Label1", new JTextField(), 3);
 * builder.append("Label2", new JTextField());
 * builder.append("Label3", new JTextField());
 * builder.append(new JButton("..."));
 * return builder.getPanel();
 * </pre>
 * 
 * @author Karsten Lentzsch
 * @see	ColumnSpec
 * @see	RowSpec
 * @see	CellConstraints
 * @see	com.jgoodies.forms.builder.AbstractFormBuilder
 * @see	com.jgoodies.forms.builder.ButtonBarBuilder
 * @see	com.jgoodies.forms.factories.FormFactory
 * @see	Size
 * @see	Sizes
 */

public final class FormLayout implements LayoutManager2 {

    /**
     * Holds the column specifications.
     * @see ColumnSpec
     */    
    private final List colSpecs;

    /**
     * Holds the row specifications.
     * @see RowSpec
     */
    private final List rowSpecs;

    /**
     * Holds the column groups as an array of arrays of column indices.
     * @see #setColGroupIndices
     */
    private int[][] colGroupIndices;

    /**
     * Holds the row groups as an array of arrays of row indices.
     * @see #setRowGroupIndices
     */
    private int[][] rowGroupIndices;

    /**
     * Maps components to their associated <code>CellConstraints</code>.
     * @see CellConstraints
     */
    private final Map constraintMap;


    // Fields used by the Layout Algorithm **********************************
 
    /**
     * Holds the components that occupy exactly one column. 
     * For each column we keep a list of these components.
     */
    private List[] colComponents;

    /**
     * Holds the components that occupy exactly one row. 
     * For each row we keep a list of these components.
     */
    private List[] rowComponents;
    
    /**
     * Caches component minimum and preferred sizes.
     * All requests for component sizes shall be directed to the cache.
     */
    private final ComponentSizeCache componentSizeCache;
    
    /**
     * These functional objects are used to measure component sizes.
     * They abstract from horizontal and vertical orientation and so,
     * allow to implement the layout algorithm for both orientations with a
     * single set of methods.
     */
    private final Measure minimumWidthMeasure;
    private final Measure minimumHeightMeasure;
    private final Measure preferredWidthMeasure;
    private final Measure preferredHeightMeasure;

 
    // Instance Creation ****************************************************
 
    /**
     * Constructs an instance of <code>FormLayout</code> using the
     * given column and row specifications.
     * 
	 * @param colSpecs	an array of column specifications.
     * @param rowSpecs	an array of row specifications.
     */
    public FormLayout(ColumnSpec[] colSpecs, RowSpec[] rowSpecs) {
        if (colSpecs == null)
            throw new NullPointerException("Column specifications must not be null.");
        if (rowSpecs == null)
            throw new NullPointerException("Row specifications must not be null.");
        
        this.colSpecs  = new ArrayList(Arrays.asList(colSpecs));
        this.rowSpecs  = new ArrayList(Arrays.asList(rowSpecs));
        colGroupIndices = new int[][]{};
        rowGroupIndices = new int[][]{};
        int initialCapacity = colSpecs.length * rowSpecs.length / 4;
        constraintMap       = new HashMap(initialCapacity);
        componentSizeCache  = new ComponentSizeCache(initialCapacity);
        minimumWidthMeasure    = new MinimumWidthMeasure(componentSizeCache);
        minimumHeightMeasure   = new MinimumHeightMeasure(componentSizeCache);
        preferredWidthMeasure  = new PreferredWidthMeasure(componentSizeCache);
        preferredHeightMeasure = new PreferredHeightMeasure(componentSizeCache);
    }
      
    /**
     * Constructs an instance of <code>FormLayout</code> using the given
     * encoded string representations for column and row specifications.
     * 
     * @param encodedColumnSpecs  comma separated encoded column specifications
     * @param encodedRowSpecs     comma separated encoded row specifications
     */
    public FormLayout(String encodedColumnSpecs, String encodedRowSpecs) {
        this(decodeColSpecs(encodedColumnSpecs),
             decodeRowSpecs(encodedRowSpecs));
    }
       
    /**
     * Constructs an instance of <code>FormLayout</code> using the given
     * encoded string representations for column and row specifications.
     * 
     * @param encodedColumnSpecs  comma separated encoded column specifications
     */
    public FormLayout(String encodedColumnSpecs) {
        this(encodedColumnSpecs, "");
    }
    
    
    // Accessing the Column and Row Specifications **************************
    
    /**
     * Returns the number of columns in this form layout.
     * 
     * @return the number of columns
     */
    public int getColumnCount() {
        return colSpecs.size();
    }
    
    /**
     * Returns the number of rows in this form layout.
     * 
     * @return the number of rows
     */
    public int getRowCount() {
        return rowSpecs.size();
    }
    
    /**
     * Returns the <code>ColumnSpec</code> at the specified column.
     * 
     * @param column   the column index of the requested <code>ColumnSpec</code>
     * @return the <code>ColumnSpec</code> at the specified column
     */
    public ColumnSpec getColumnSpec(int column) {
        return (ColumnSpec) colSpecs.get(column - 1);
    }

    /**
     * Returns the <code>RowSpec</code> at the specified row.
     * 
     * @param row   the row index of the requested <code>RowSpec</code>
     * @return the <code>RowSpec</code> at the specified row
     */
    public RowSpec getRowSpec(int row) {
        return (RowSpec) rowSpecs.get(row - 1);
    }

    /**
     * Appends the given column specification to the right hand side of all
     * columns.
     * @param columnSpec the column specification to be added 
     * @throws NullPointerException if the column speficcation is null
     */
    public void appendColumn(ColumnSpec columnSpec) {
        if (columnSpec == null) {
            throw new NullPointerException("The column spec must not be null.");
        }
        colSpecs.add(columnSpec);
    }
    
    /**
     * Inserts the specified column at the specified position. Shifts
     * components that intersect the new column to the right and readjusts
     * column groups.
     * <p> 
     * The component shift works as follows: components that were located on
     * the right hand side of the inserted column are shifted one column to
     * the right; component column span is increased by one if it intersects
     * the new column.
     * <p>
     * Column group indices that are greater or equal than the given column
     * index will be increased by one.
     * 
     * @param columnIndex  index of the column to be inserted
     * @param columnSpec   specification of the column to be inserted
     * @throws IndexOutOfBoundsException if the column index is out of range
     */
    public void insertColumn(int columnIndex, ColumnSpec columnSpec) {
        if (columnIndex < 1 || columnIndex > getColumnCount()) {
            throw new IndexOutOfBoundsException(
                    "The column index " + columnIndex + 
                    "must be in the range [1, " + getColumnCount() + "].");
        }
        colSpecs.add(columnIndex - 1, columnSpec);
        shiftComponentsHorizontally(columnIndex, false);
        adjustGroupIndices(colGroupIndices, columnIndex, false);
    }
    
    
    /**
     * Removes the column with the given column index from the layout.
     * Components will be rearranged and column groups will be readjusted.
     * Therefore, the column must not contain components and must not be part
     * of a column group.
     * <p> 
     * The component shift works as follows: components that were located on
     * the right hand side of the removed column are moved one column to the
     * left; component column span is decreased by one if it intersects the
     * removed column.
     * <p>
     * Column group indices that are greater than the column index will be
     * decreased by one.
     * 
     * @param columnIndex  index of the column to remove
     * @throws IndexOutOfBoundsException if the column index is out of range
     * @throws IllegalStateException if the column contains components
     * @throws IllegalStateException if the column is grouped
     */
    public void removeColumn(int columnIndex) {
        if (columnIndex < 1 || columnIndex > getColumnCount()) {
            throw new IndexOutOfBoundsException(
                    "The column index " + columnIndex + 
                    " must be in the range [1, " + getColumnCount() + "].");
        }
        colSpecs.remove(columnIndex - 1);
        shiftComponentsHorizontally(columnIndex, true);
        adjustGroupIndices(colGroupIndices, columnIndex, true);
    }
    
    /**
     * Appends the given row specification to the bottom of all rows.
     * 
     * @param rowSpec  the row specification to be added to the form layout
     * @throws NullPointerException if the rowSpec is null
     */
    public void appendRow(RowSpec rowSpec) {
        if (rowSpec == null) {
            throw new NullPointerException("The row spec must not be null.");
        }
        rowSpecs.add(rowSpec);
    }

    /**
     * Inserts the specified column at the specified position. Shifts
     * components that intersect the new column to the right and readjusts
     * column groups.
     * <p> 
     * The component shift works as follows: components that were located on
     * the right hand side of the inserted column are shifted one column to
     * the right; component column span is increased by one if it intersects
     * the new column.
     * <p>
     * Column group indices that are greater or equal than the given column
     * index will be increased by one.
     * 
     * @param rowIndex  index of the row to be inserted
     * @param rowSpec   specification of the row to be inserted
     * @throws IndexOutOfBoundsException if the row index is out of range
     */
    public void insertRow(int rowIndex, RowSpec rowSpec) {
        if (rowIndex < 1 || rowIndex > getRowCount()) {
            throw new IndexOutOfBoundsException(
                    "The row index " + rowIndex + 
                    " must be in the range [1, " + getRowCount() + "].");
        }
        rowSpecs.add(rowIndex - 1, rowSpec);
        shiftComponentsVertically(rowIndex, false);
        adjustGroupIndices(rowGroupIndices, rowIndex, false);
    }
        
    /**
     * Removes the row with the given row index from the layout. Components
     * will be rearranged and row groups will be readjusted. Therefore, the
     * row must not contain components and must not be part of a row group.
     * <p> 
     * The component shift works as follows: components that were located
     * below the removed row are moved up one row; component row span is
     * decreased by one if it intersects the removed row.
     * <p>
     * Row group indices that are greater than the row index will be decreased
     * by one.
     * 
     * @param rowIndex  index of the row to remove
     * @throws IndexOutOfBoundsException if the row index is out of range
     * @throws IllegalStateException if the row contains components
     * @throws IllegalStateException if the row is grouped
     */
    public void removeRow(int rowIndex) {
        if (rowIndex < 1 || rowIndex > getRowCount()) {
            throw new IndexOutOfBoundsException(
                    "The row index " + rowIndex + 
                    "must be in the range [1, " + getRowCount() + "].");
        }
        rowSpecs.remove(rowIndex - 1);
        shiftComponentsVertically(rowIndex, true);
        adjustGroupIndices(rowGroupIndices, rowIndex, true);
    }
    
    
    /**
     * Shifts components horizontally, either to the left if a column has been
     * inserted or the the right if a column has been removed.
     * 
     * @param columnIndex  index of the column to remove
     * @param remove  		true for remove, false for insert
     * @throws IllegalStateException if a removed column contains components
     */
    private void shiftComponentsHorizontally(int columnIndex, boolean remove) {
        final int offset = remove ? -1 : 1;
        for (Iterator i = constraintMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            CellConstraints constraints = (CellConstraints) entry.getValue();
            int x1 = constraints.gridX;
            int w  = constraints.gridWidth;
            int x2 = x1 + w - 1;
            if (x1 == columnIndex && remove) {
                throw new IllegalStateException(
                    "The removed column " + columnIndex + 
                    " must not contain component origins.\n" +
                    "Illegal component=" + entry.getKey());
            } else if (x1 >= columnIndex) {
                constraints.gridX += offset;
            } else if (x2 >= columnIndex) {
                constraints.gridWidth += offset;
            }
        }
    }
    
    /**
     * Shifts components horizontally, either to the left if a column has been
     * inserted or the the right if a column has been removed.
     * 
     * @param columnIndex  index of the column to remove
     * @param remove        true for remove, false for insert
     * @throws IllegalStateException if a removed column contains components
     */
    private void shiftComponentsVertically(int rowIndex, boolean remove) {
        final int offset = remove ? -1 : 1;
        for (Iterator i = constraintMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            CellConstraints constraints = (CellConstraints) entry.getValue();
            int y1 = constraints.gridY;
            int h  = constraints.gridHeight;
            int y2 = y1 + h - 1;
            if (y1 == rowIndex && remove) {
                throw new IllegalStateException(
                    "The removed row " + rowIndex + 
                    " must not contain component origins.\n" +
                    "Illegal component=" + entry.getKey());
            } else if (y1 >= rowIndex) {
                constraints.gridY += offset;
            } else if (y2 >= rowIndex) {
                constraints.gridHeight += offset;
            }
        }
    }
    
    /**
     * Adjusts group indices. Shifts the given groups to left, right, up,
     * down according to the specified remove or add flag.
     * 
     * @param allGroupIndices	the groups to be adjusted
     * @param modifiedIndex	the modified column or row index
     * @param remove			true for remove, false for add
     * @throws IllegalStateException if we remove and the index is grouped
     */
    private void adjustGroupIndices(int[][] allGroupIndices, 
                                     int modifiedIndex, boolean remove) {
        final int offset = remove ? -1 : +1;
        for (int group = 0; group < allGroupIndices.length; group++) {
            int[] groupIndices = allGroupIndices[group];
            for (int i = 0; i < groupIndices.length; i++) {
                int index = groupIndices[i];
                if (index == modifiedIndex && remove) {
                    throw new IllegalStateException(
                        "The removed index " + modifiedIndex + " must not be grouped.");
                } else if (index >= modifiedIndex) {
                    groupIndices[i] += offset;
                }
            }
        }        
    }

        
    // Accessing Constraints ************************************************
    
    /**
     * Sets the constraints for the specified component in this layout.
     * 
     * @param component	the component to be modified
     * @param constraints	the constraints to be applied
     * @throws NullPointerException if the component is null
     * @throws NullPointerException if the constraints are null
     */
    public void setConstraints(Component component, CellConstraints constraints) {
        if (component == null)
            throw new NullPointerException("Component must not be null.");
        if (constraints == null)
            throw new NullPointerException("Constraint must not be null.");
            
        constraints.ensureValidGridBounds(getColumnCount(), getRowCount());
        constraintMap.put(component, (CellConstraints) constraints.clone());
    }
    
    /**
     * Gets the constraints for the specified component. A copy of
     * the actual <code>CellConstraints</code> object is returned.
     * 
     * @param component				  the component to be queried
     * @return the <code>CellConstraints</code> for the specified component
     * @throws NullPointerException if component is null or has not been added
     * to the Container
     */
    public CellConstraints getConstraints(Component component) {
        if (component == null)
            throw new NullPointerException("Component must not be null.");
            
        CellConstraints constraints = (CellConstraints) constraintMap.get(component);
        if (constraints == null) 
            throw new NullPointerException("Component has not been added to the container.");
            
        return (CellConstraints) constraints.clone();
    }
    
    /**
     * Removes the constraints for the specified component in this layout.
     * 
     * @param component  the component to be modified
     */
    private void removeConstraints(Component component) {
        constraintMap.remove(component);
        componentSizeCache.removeEntry(component);
    }
    

    // Accessing Column and Row Groups **************************************
    
    /**
     * Answers a deep copy of the column groups.
     * 
     * @return the column groups as two-dimensional int array
     */
    public int[][] getColumnGroups() {
        return deepClone(colGroupIndices);
    }
    
    /**
     * Sets the column groups, where each column in a group gets the same
     * group wide width. Each group is described by an array of integers that
     * are interpreted as column indices. The parameter is an array of such
     * group descriptions.
     * <p>
     * Example: we build two groups, the first of columns 1, 3, and 4;
     * and a second group for columns 7 and 9.
     * <pre>
     * setColumnGroups(new int[][]{ {1, 3, 4}, {7, 9}});
     * </pre>
     * 
     * @param colGroupIndices	a two-dimensional array of column groups
     * indices.
     * @throws	IndexOutOfBoundsException if an index is outside the grid
     * @throws IllegalArgumentException if a column index is used twice
     */
    public void setColumnGroups(int[][] colGroupIndices) {
        int maxColumn = getColumnCount();
        boolean[] usedIndices = new boolean[maxColumn + 1];
        for (int group = 0; group < colGroupIndices.length; group++) {
            for (int j = 0; j < colGroupIndices[group].length; j++) {
                int colIndex = colGroupIndices[group][j];
                if (colIndex < 1 || colIndex > maxColumn) {
                    throw new IndexOutOfBoundsException(
                        "Invalid column group index " + colIndex + 
                        " in group " + (group+1)); 
                }
                if (usedIndices[colIndex]) {
                    throw new IllegalArgumentException(
                        "Column index " + colIndex + " must not be used in multiple column groups.");
                } else {
                    usedIndices[colIndex] = true;
                }
            }
        }
        this.colGroupIndices = deepClone(colGroupIndices);
    }
    
    /**
     * Adds the specified column index to the last column group. In case there
     * are no groups, a new group will be created.
     * 
     * @param columnIndex	the column index to be set grouped
     */
    public void addGroupedColumn(int columnIndex) {
        int[][] newColGroups = getColumnGroups();
        // Create a group if none exists.
        if (newColGroups.length == 0) {
            newColGroups = new int[][]{{columnIndex}};
        } else {
            int lastGroupIndex = newColGroups.length-1;
            int[] lastGroup = newColGroups[lastGroupIndex];
            int groupSize = lastGroup.length;
            int[] newLastGroup = new int[groupSize+1];
            System.arraycopy(lastGroup, 0, newLastGroup, 0, groupSize);
            newLastGroup[groupSize] = columnIndex;
            newColGroups[lastGroupIndex] = newLastGroup;
        }
        setColumnGroups(newColGroups);
    }
    
    /**
     * Answers a deep copy of the row groups.
     * 
     * @return the row groups as two-dimensional int array
     */
    public int[][] getRowGroups() {
        return deepClone(rowGroupIndices);
    }
    
    /**
     * Sets the row groups, where each row in such a group gets the same group
     * wide height. Each group is described by an array of integers that are
     * interpreted as row indices. The parameter is an array of such group
     * descriptions.
     * <p>
     * Example: we build two groups, the first of rows 1, and 2,; and a second
     * group for rows 5, 7, and 9.
     * <pre>
     * setRowGroups(new int[][]{ {1, 2}, {5, 7, 9}});
     * </pre>
     * 
     * @param rowGroupIndices a two-dimensional array of row group indices.
     * @throws IndexOutOfBoundsException if an index is outside the grid
     */
    public void setRowGroups(int[][] rowGroupIndices) {
        int rowCount = getRowCount();
        boolean[] usedIndices = new boolean[rowCount + 1];
        for (int i = 0; i < rowGroupIndices.length; i++) {
            for (int j = 0; j < rowGroupIndices[i].length; j++) {
                int rowIndex = rowGroupIndices[i][j];
                if (rowIndex < 1 || rowIndex > rowCount) {
                    throw new IndexOutOfBoundsException(
                        "Invalid row group index " + rowIndex + 
                        " in group " + (i+1)); 
                }
                if (usedIndices[rowIndex]) {
                    throw new IllegalArgumentException(
                        "Row index " + rowIndex + " must not be used in multiple row groups.");
                } else {
                    usedIndices[rowIndex] = true;
                }
            }
        }
        this.rowGroupIndices = deepClone(rowGroupIndices);
    }

    /**
     * Adds the specified row index to the last row group. In case there are
     * no groups, a new group will be created.
     * 
     * @param rowIndex   the index of the row that should be grouped
     */
    public void addGroupedRow(int rowIndex) {
        int[][] newRowGroups = getRowGroups();
        // Create a group if none exists.
        if (newRowGroups.length == 0) {
            newRowGroups = new int[][]{{rowIndex}};
        } else {
            int lastGroupIndex = newRowGroups.length-1;
            int[] lastGroup = newRowGroups[lastGroupIndex];
            int groupSize = lastGroup.length;
            int[] newLastGroup = new int[groupSize+1];
            System.arraycopy(lastGroup, 0, newLastGroup, 0, groupSize);
            newLastGroup[groupSize] = rowIndex;
            newRowGroups[lastGroupIndex] = newLastGroup;
        }
        setRowGroups(newRowGroups);
    }
    

    // Implementing the LayoutManager and LayoutManager2 Interfaces *********
    
    /**
     * Throws an <code>UnsupportedOperationException</code>. Does not add 
     * the specified component with the specified name to the layout.
     *
     * @param name         indicates entry's position and anchor
     * @param component    component to add
     * @throws UnsupportedOperationException always
     */
    public void addLayoutComponent(String name, Component component) {
        throw new UnsupportedOperationException(
                "Use #addLayoutComponent(Component, Object) instead.");
    }
    
    /**
     * Adds the specified component to the layout, using the specified
     * <code>constraints</code> object.  Note that constraints are mutable and
     * are, therefore, cloned when cached.
     *
     * @param comp         the component to be added
     * @param constraints  the component's form layout constraints
     * @throws NullPointerException if <code>constraints</code> is null
     * @throws IllegalArgumentException if <code>constraints</code> is not a
     * <code>CellConstraints</code> or a String that can be used to construct
     * a <code>CellConstraints</code>
     */
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof String) {
            setConstraints(comp, new CellConstraints((String) constraints));
        } else if (constraints instanceof CellConstraints) {
            setConstraints(comp, (CellConstraints) constraints);
        } else if (constraints == null) {
            throw new NullPointerException("Constraints must not be null.");
        } else {
            throw new IllegalArgumentException("Illegal constraint type " + constraints.getClass());
        }
    }

    /**
     * Removes the specified component from this layout.
     * <p>
     * Most applications do not call this method directly.
     * 
     * @param    comp   the component to be removed.
     * @see      java.awt.Container#remove(java.awt.Component)
     * @see      java.awt.Container#removeAll()
     */
    public void removeLayoutComponent(Component comp) {
        removeConstraints(comp);
    }
    

    // Layout Requests ******************************************************

    /**
     * Determines the minimum size of the <code>parent</code> container
     * using this form layout.
     * <p>
     * Most applications do not call this method directly.
     * @param parent   the container in which to do the layout
     * @see java.awt.Container#doLayout
     * @return the minimum size of the <code>parent</code> container
     */
    public Dimension minimumLayoutSize(Container parent) {
        return computeLayoutSize(parent,
                                 minimumWidthMeasure,
                                 minimumHeightMeasure);
    }

    /**
     * Determines the preferred size of the <code>parent</code>
     * container using this form layout.
     * <p>
     * Most applications do not call this method directly.
     *
     * @param parent   the container in which to do the layout
     * @see java.awt.Container#getPreferredSize
     * @return the preferred size of the <code>parent</code> container
     */
    public Dimension preferredLayoutSize(Container parent) {
        return computeLayoutSize(parent,
                                 preferredWidthMeasure,
                                 preferredHeightMeasure);
    }

    /**
     * Returns the maximum dimensions for this layout given the components
     * in the specified target container.
     * 
     * @param target    the container which needs to be laid out
     * @see Container
     * @see #minimumLayoutSize(Container)
     * @see #preferredLayoutSize(Container)
     * @return the maximum dimensions for this layout
     */
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     * 
     * @return the value <code>0.5f</code> to indicate center alignment
     */
    public float getLayoutAlignmentX(Container parent) {
        return 0.5f;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     * 
     * @return the value <code>0.5f</code> to indicate center alignment
     */
    public float getLayoutAlignmentY(Container parent) {
        return 0.5f;
    }


    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     */
    public void invalidateLayout(Container target) {
        invalidateCaches();
    }


    /**
     * Lays out the specified container using this form layout.  This method
     * reshapes components in the specified container in order to satisfy the
     * contraints of this <code>FormLayout</code> object.
     * <p>
     * Most applications do not call this method directly.
     * <p>
     * The form layout performs the following steps:
     * <ol>
     * <li>find components that occupy exactly one column or row
     * <li>compute minimum widths and heights 
     * <li>compute preferred widths and heights
     * <li>give cols and row equal size if they share a group
     * <li>compress default columns and rows if total is less than pref size
     * <li>give cols and row equal size if they share a group
     * <li>distribute free space
     * <li>set components bounds
     * </ol>
     * @param parent	the container in which to do the layout
     * @see java.awt.Container
     * @see java.awt.Container#doLayout
     */
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            initializeColAndRowComponentLists();
            Dimension size = parent.getSize();
            
            Insets insets = parent.getInsets();
            int totalWidth  = size.width - insets.left - insets.right;
            int totalHeight = size.height- insets.top  - insets.bottom; 

            int[] x = computeGridOrigins(parent,
                                         totalWidth, insets.left,
                                         colSpecs, 
                                         colComponents,
                                         colGroupIndices,
                                         minimumWidthMeasure,
                                         preferredWidthMeasure
                                         );
            int[] y = computeGridOrigins(parent,
                                         totalHeight, insets.top,
                                         rowSpecs, 
                                         rowComponents,
                                         rowGroupIndices,
                                         minimumHeightMeasure,
                                         preferredHeightMeasure
                                         );
                                         
            layoutComponents(x, y);
        }        
    }
    
    // Layout Algorithm *****************************************************
    
    /**
     * Initializes two lists for columns and rows that hold a column's or
     * row's components that span only this column or row.
     * <p>
     * Iterates over all components and their associated constraints; every
     * component that has a column span or row span of 1 is put into the
     * column's or row's component list.
     */
    private void initializeColAndRowComponentLists() {
        colComponents = new LinkedList[getColumnCount()];
        for (int i=0; i < getColumnCount(); i++) {
            colComponents[i] = new LinkedList();
        }

        rowComponents = new LinkedList[getRowCount()];
        for (int i=0; i < getRowCount(); i++) {
            rowComponents[i] = new LinkedList();
        }
        
        for (Iterator i = constraintMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            Component component = (Component) entry.getKey();
            if (!component.isVisible()) 
                continue;
                
            CellConstraints constraints = (CellConstraints) entry.getValue();
            if (constraints.gridWidth == 1) 
                colComponents[constraints.gridX-1].add(component);
            
            if (constraints.gridHeight == 1) 
                rowComponents[constraints.gridY-1].add(component);
        }
    }
    

    /**
     * Computes and answers the layout size of the given <code>parent</code>
     * container using the specified measures.
     *
     * @param parent   the container in which to do the layout
     * @return the layout size of the <code>parent</code> container
     */
    private Dimension computeLayoutSize(Container parent,
                                         Measure defaultWidthMeasure, 
                                         Measure defaultHeightMeasure) {
        synchronized (parent.getTreeLock()) {
            initializeColAndRowComponentLists();
            int[] colWidths  = maximumSizes(parent, colSpecs, colComponents, 
                                            minimumWidthMeasure,
                                            preferredWidthMeasure,
                                            defaultWidthMeasure);
            int[] rowHeights = maximumSizes(parent, rowSpecs, rowComponents,
                                            minimumHeightMeasure, 
                                            preferredHeightMeasure,
                                            defaultHeightMeasure);
            int[] groupedWidths  = groupedSizes(colGroupIndices, colWidths);
            int[] groupedHeights = groupedSizes(rowGroupIndices, rowHeights);

            // Convert sizes to origins.
            int[] xOrigins = computeOrigins(groupedWidths,  0);
            int[] yOrigins = computeOrigins(groupedHeights, 0);

            int width1  = sum(groupedWidths);
            int height1 = sum(groupedHeights);
            int maxWidth = width1;
            int maxHeight = height1;

            // Take components that span multiple columns or rows into account.
            for (Iterator i = constraintMap.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry entry = (Map.Entry) i.next();
                Component component = (Component) entry.getKey();
                if (!component.isVisible()) 
                    continue;
                
                CellConstraints constraints = (CellConstraints) entry.getValue();
                if (constraints.gridWidth > 1) {
                    int compWidth = preferredWidthMeasure.sizeOf(component);
                    int gridX1 = constraints.gridX-1;
                    int gridX2 = gridX1 + constraints.gridWidth;
                    int lead  = xOrigins[gridX1];
                    int trail = width1 - xOrigins[gridX2];
                    int myWidth = lead + compWidth + trail;
                    if (myWidth > maxWidth) {
                        maxWidth = myWidth;
                    }
                }
            
                if (constraints.gridHeight > 1) { 
                    int compHeight = preferredHeightMeasure.sizeOf(component);
                    int gridY1 = constraints.gridY-1;
                    int gridY2 = gridY1 + constraints.gridHeight;
                    int lead  = yOrigins[gridY1];
                    int trail = height1 - yOrigins[gridY2];
                    int myHeight = lead + compHeight + trail;
                    if (myHeight > maxHeight) {
                        maxHeight = myHeight;
                    }
                }
            }
            Insets insets = parent.getInsets();
            int width  = maxWidth  + insets.left + insets.right;
            int height = maxHeight + insets.top  + insets.bottom; 
            return new Dimension(width, height);
        }
    }

    /**
     * Computes and answers the grid's origins.
     * 
     * @param container        the layout container
     * @param totalSize		the total size to assign
     * @param offset     		the offset from left or top margin
     * @param formSpecs     	the column or row specs, resp.
     * @param componentLists	the components list for each col/row
     * @param minMeasure		the measure used to determin min sizes
     * @param prefMeasure		the measure used to determin pre sizes
     * @param groupIndices		the group specification
     * @return an int array with the origins
     */    
    private int[] computeGridOrigins(Container container,
                                      int totalSize, int offset,
                                      List formSpecs,
                                      List[] componentLists,
                                      int[][] groupIndices,
                                      Measure minMeasure,
                                      Measure prefMeasure) {
        /* For each spec compute the minimum and preferred size that is 
         * the maximum of all component minimum and preferred sizes resp.
         */
        int[] minSizes   = maximumSizes(container, formSpecs, componentLists, 
                                        minMeasure, prefMeasure, minMeasure);
        int[] prefSizes  = maximumSizes(container, formSpecs, componentLists, 
                                        minMeasure, prefMeasure, prefMeasure);

        int[] groupedMinSizes  = groupedSizes(groupIndices, minSizes);
        int[] groupedPrefSizes = groupedSizes(groupIndices, prefSizes);
        int   totalMinSize     = sum(groupedMinSizes);
        int   totalPrefSize    = sum(groupedPrefSizes);
        int[] compressedSizes  = compressedSizes(formSpecs, 
                                               totalSize, 
                                               totalMinSize,  
                                               totalPrefSize,  
                                               groupedMinSizes,  
                                               prefSizes);
        int[] groupedSizes     = groupedSizes(groupIndices, compressedSizes);
        int   totalGroupedSize = sum(groupedSizes);
        int[] sizes            = distributedSizes(formSpecs, 
                                                 totalSize, 
                                                 totalGroupedSize,  
                                                 groupedSizes);
        return computeOrigins(sizes, offset);
    }
    

    /**
     * Computes origins from sizes taking the specified offset into account.
     * 
     * @param sizes	the array of sizes
     * @param offset	an offset for the first origin
     * @return an array of origins
     */    
    private int[] computeOrigins(int[] sizes, int offset) {
        int count = sizes.length;
        int origins[] = new int[count + 1];
        origins[0] = offset;
        for (int i=1; i <= count; i++) {
            origins[i] = origins[i-1] + sizes[i-1];
        }
        return origins;
    }
    
    /**
     * Lays out the components using the given x and y origins, the column and
     * row specifications, and the component constraints.
     * <p>
     * The actual computation is done by each component's form constraint
     * object. We just compute the cell, the cell bounds and then hand over
     * the component, cell bounds, and measure to the form constraints.
     * This will allow potential subclasses of <code>CellConstraints</code>
     * to do special micro-layout corrections. For example, such a subclass
     * could map JComponent classes to visual layout bounds that may
     * lead to a slightly different bounds.
     * 
     * @param x	an int array of the horizontal origins 
     * @param y	an int array of the vertical origins
     */
    private void layoutComponents(int[] x, int[] y) {
        Rectangle cellBounds = new Rectangle();
        for (Iterator i = constraintMap.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            Component       component   = (Component)       entry.getKey();
            CellConstraints constraints = (CellConstraints) entry.getValue();
            
            int gridX      = constraints.gridX-1;
            int gridY      = constraints.gridY-1;
            int gridWidth  = constraints.gridWidth;
            int gridHeight = constraints.gridHeight;
            cellBounds.x = x[gridX];
            cellBounds.y = y[gridY];
            cellBounds.width  = x[gridX + gridWidth ] - cellBounds.x;
            cellBounds.height = y[gridY + gridHeight] - cellBounds.y;

            constraints.setBounds(component, this, cellBounds,
                            minimumWidthMeasure,   minimumHeightMeasure, 
                            preferredWidthMeasure, preferredHeightMeasure);
        }
    }
    

    /**
     * Invalidates all caches.
     */
    private void invalidateCaches() {
        componentSizeCache.invalidate();
    }
    
    
    /**
     * Computes and answers the sizes for the given form specs, component
     * lists and measures fot minimum, preferred, and default size.
     * 
     * @param container        the layout container
     * @param formSpecs		the column or row specs, resp.
     * @param componentLists	the components list for each col/row
     * @param minMeasure		the measure used to determin min sizes
     * @param prefMeasure		the measure used to determin pre sizes
     * @param defaultMeasure	the measure used to determin default sizes
     * @return the column or row sizes
     */
    private int[] maximumSizes(Container container,
                                List formSpecs,
                                List[] componentLists,
                                Measure minMeasure,
                                Measure prefMeasure,
                                Measure defaultMeasure) {
        FormSpec formSpec;
        int size = formSpecs.size();
        int result[] = new int[size];
        for (int i = 0; i < size; i++) {
            formSpec = (FormSpec) formSpecs.get(i);
            result[i] = formSpec.maximumSize(container,
                                             componentLists[i],
                                             minMeasure,
                                             prefMeasure,
                                             defaultMeasure);
        }
        return result;
    }
 

    /**
     * Computes and answers the compressed sizes. Compresses space for columns
     * and rows iff the available space is less than the total preferred size
     * but more than the total minimum size.
     * <p>
     * Only columns and row that are specified to be compressable will be
     * affected. You can specify a column and row as compressable by
     * giving it the component size <tt>default</tt>.
     * 
     * @param formSpecs	the column or row specs to use
     * @param totalSize	the total available size
     * @param totalMinSize	the sum of all minimum sizes
     * @param totalPrefSize the sum of all preferred sizes
     * @param minSizes		an int array of column/row minimum sizes
     * @param prefSizes	an int array of column/row preferred sizes
     * @return an int array of compressed column/row sizes
     */
    private int[] compressedSizes(List formSpecs, 
                                 int totalSize, int totalMinSize, int totalPrefSize,  
                                 int[] minSizes, int[] prefSizes) {
        
        // If we have less space than the total min size answers min sizes.                            
        if (totalSize < totalMinSize)
            return minSizes;
        // If we have more space than the total pref size answers pref sizes.                            
        if (totalSize >= totalPrefSize)
            return prefSizes;
            
        int count = formSpecs.size();
        int[] sizes = new int[count];

        double totalCompressionSpace = totalPrefSize - totalSize;
        double maxCompressionSpace   = totalPrefSize - totalMinSize;
        double compressionFactor     = totalCompressionSpace / maxCompressionSpace;

//      System.out.println("Total compression space=" + totalCompressionSpace);
//      System.out.println("Max compression space  =" + maxCompressionSpace);
//      System.out.println("Compression factor     =" + compressionFactor);
        
        for (int i=0; i < count; i++) {
            FormSpec formSpec = (FormSpec) formSpecs.get(i);
            sizes[i] = prefSizes[i];
            if (formSpec.getSize() == Sizes.DEFAULT) {
                sizes[i] -= (int) Math.round((prefSizes[i] - minSizes[i]) 
                                                 * compressionFactor); 
            }
        }
        return sizes;
    }
 
    
    /**
     * Computes and answers grouped sizes. Gives grouped columns or rows the
     * same size.
     * 
     * @param groups	the group specification
     * @param rawSizes	the raw sizes before the grouping 
     * @return the grouped sizes
     */
    private int[] groupedSizes(int[][] groups, int[] rawSizes) {
        // Return the compressed sizes if there are no groups.
        if (groups == null || groups.length == 0) {
            return rawSizes;
        }
        
        // Initialize the result with the given compressed sizes.
        int[] sizes = new int[rawSizes.length];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = rawSizes[i];
        }
        
        // For each group equalize the sizes.
        for (int group = 0; group < groups.length; group++) {
            int[] groupIndices = groups[group];
            int groupMaxSize = 0;
            // Compute the group's maximum size.
            for (int i = 0; i < groupIndices.length; i++) {
                int index = groupIndices[i] - 1;
                groupMaxSize = Math.max(groupMaxSize, sizes[index]);
            }
            // Set all sizes of this group to the group's maximum size.
            for (int i = 0; i < groupIndices.length; i++) {
                int index = groupIndices[i] - 1;
                sizes[index] = groupMaxSize;
            }
        }        
        return sizes;
    }
 
 
    /**
     * Distributes free space over columns and rows and answers the sizes
     * after this distribution process.
     * 
     * @param formSpecs	  the column/row specifications to work with
     * @param totalSize	  the total available size
     * @param totalPrefSize the sum of all preferred sizes 
     * @param inputSizes	 the input sizes
     * @return the distributed sizes
     */   
    private int[] distributedSizes(List formSpecs, 
                                    int totalSize, int totalPrefSize, 
                                    int[] inputSizes) {
        double totalFreeSpace = totalSize - totalPrefSize;
        // Do nothing if there's no free space.
        if (totalFreeSpace < 0)
            return inputSizes;
            
        // Compute the total weight.
        int count = formSpecs.size();
        double totalWeight   = 0.0;
        for (int i=0; i < count; i++) {
            FormSpec formSpec = (FormSpec) formSpecs.get(i);
            totalWeight += formSpec.getResizeWeight();
        }
        
        // Do nothing if there's no resizing column.
        if (totalWeight == 0.0) 
            return inputSizes;
            
        int[] sizes = new int[count];
        
        double restSpace = totalFreeSpace;
        int    roundedRestSpace = (int) totalFreeSpace;
        for (int i=0; i < count; i++) {
            FormSpec formSpec = (FormSpec) formSpecs.get(i);
            double weight = formSpec.getResizeWeight();
            if (weight == FormSpec.NO_GROW) {
                sizes[i] = inputSizes[i];
            } else {
                double roundingCorrection = restSpace - (double) roundedRestSpace;
                double extraSpace = totalFreeSpace * weight / totalWeight;
                double correctedExtraSpace = extraSpace - roundingCorrection;
                int roundedExtraSpace = (int) Math.round(correctedExtraSpace);
                sizes[i] = inputSizes[i] + roundedExtraSpace;
                restSpace -= extraSpace;
                roundedRestSpace -= roundedExtraSpace;
            }
        }
        return sizes;
    }
 
    
    /**
     * Computes and answers the sum of integers in the given array of ints.
     * 
     * @param sizes	an array of ints to sum up
     * @return the sum of ints in the array
     */
    private int sum(int[] sizes) {
        int sum = 0;
        for (int i = sizes.length - 1; i >=0; i--) {
            sum += sizes[i];
        }
        return sum;
    }
    
      
    // Measuring Component Sizes ********************************************
    
    /**
     * An interface that describes how to measure a <code>Component</code>.
     * Used to abstract from horizontal and vertical dimensions as well as
     * minimum and preferred sizes.
     */
    static interface Measure {
        
        /**
         * Computes and answers the size of the given <code>Component</code>.
         * 
         * @param component  the component to measure
         * @return the component's size
         */
        int sizeOf(Component component);
    }
    
    
    /**
     * An abstract implementation of the <code>Measure</code> interface
     * that caches component sizes.
     */
    private static abstract class CachingMeasure implements Measure {
        
        protected final ComponentSizeCache cache;
        
        private CachingMeasure(ComponentSizeCache cache) {
            this.cache = cache;
        }
        
    }
    
    // Measures a component by computing its minimum width.
    private static class MinimumWidthMeasure extends CachingMeasure {
        private MinimumWidthMeasure(ComponentSizeCache cache) {
            super(cache);
        }
        public int sizeOf(Component c) {
            return cache.getMinimumSize(c).width;
        }
    }

    // Measures a component by computing its minimum height.
    private static class MinimumHeightMeasure extends CachingMeasure {
        private MinimumHeightMeasure(ComponentSizeCache cache) {
            super(cache);
        }
        public int sizeOf(Component c) {
            return cache.getMinimumSize(c).height;
        }
    }

    // Measures a component by computing its preferred width.
    private static class PreferredWidthMeasure extends CachingMeasure {
        private PreferredWidthMeasure(ComponentSizeCache cache) {
            super(cache);
        }
        public int sizeOf(Component c) {
            return cache.getPreferredSize(c).width;
        }
    }

    // Measures a component by computing its preferred height.
    private static class PreferredHeightMeasure extends CachingMeasure {
        private PreferredHeightMeasure(ComponentSizeCache cache) {
            super(cache);
        }
        public int sizeOf(Component c) {
            return cache.getPreferredSize(c).height;
        }
    }


    // Caching Component Sizes **********************************************
    
    /*
     * A cache for component minimum and preferred sizes.
     * Used to reduce the requests to determine a component's size.
     */ 
    private static class ComponentSizeCache {
        
        /** Maps components to their minimum sizes.  */
        private final Map minimumSizes;

        /** Maps components to their preferred sizes. */
        private final Map preferredSizes; 
        
        /**
         * Constructs a <code>ComponentSizeCache</code>
         * @param initialCapacity	the initial cache capacity
         */
        private ComponentSizeCache(int initialCapacity) {
            minimumSizes   = new HashMap(initialCapacity);
            preferredSizes = new HashMap(initialCapacity);
        }
        
        /**
         * Invalidates the cache. Clears all stored size information.
         */
        void invalidate() {
            minimumSizes.clear();
            preferredSizes.clear();
        }
        
        /**
         * Answers the minimum size for the given component. Tries to look up
         * the value from the cache; lazily creates the value if it has not
         * been requested before.
         * 
         * @param component	the component to compute the minimum size
         * @return the component's minimum size
         */
        Dimension getMinimumSize(Component component) {
            Dimension size = (Dimension) minimumSizes.get(component);
            if (size == null) {
                size = component.getMinimumSize();
                minimumSizes.put(component, size);
            }
            return size;
        }

        /**
         * Answers the preferred size for the given component. Tries to look
         * up the value from the cache; lazily creates the value if it has not
         * been requested before.
         * 
         * @param component 	the component to compute the preferred size
         * @return the component's preferred size
         */
        Dimension getPreferredSize(Component component) {
            Dimension size = (Dimension) preferredSizes.get(component);
            if (size == null) {
                size = component.getPreferredSize();
                preferredSizes.put(component, size);
            }
            return size;
        }
        
        void removeEntry(Component component) {
            minimumSizes.remove(component);
            preferredSizes.remove(component);
        }
    }
    

    // Exposing the Layout Information **************************************
    
    /**
     * Computes and answers the horizontal and vertical grid origins.
     * Performs the same layout process as #layoutContainer but
     * does not layout the components.
     * <p>
     * This method has been added only to make it easier to debug
     * the form layout. <b>You must not call this method directly;
     * It may be removed in a future release or the visibility
     * may be reduced.</b> 
     * 
     * @param parent   the <code>Container</code> to inspect
     * @return an object that comprises the grid x and y origins
     */
    public LayoutInfo getLayoutInfo(Container parent) {
        synchronized (parent.getTreeLock()) {
            initializeColAndRowComponentLists();
            Dimension size = parent.getSize();
            
            Insets insets = parent.getInsets();
            int totalWidth  = size.width - insets.left - insets.right;
            int totalHeight = size.height- insets.top  - insets.bottom; 

            int[] x = computeGridOrigins(parent,
                                         totalWidth, insets.left,
                                         colSpecs, 
                                         colComponents,
                                         colGroupIndices,
                                         minimumWidthMeasure,
                                         preferredWidthMeasure
                                         );
            int[] y = computeGridOrigins(parent,
                                         totalHeight, insets.top,
                                         rowSpecs, 
                                         rowComponents,
                                         rowGroupIndices,
                                         minimumHeightMeasure,
                                         preferredHeightMeasure
                                         );
            return new LayoutInfo(x, y);
        }        
    }
    
    
    /*
     * Stores column and row origins. 
     */
    public static final class LayoutInfo {
        
        public final int[] columnOrigins;
        public final int[] rowOrigins;
        
        private LayoutInfo(int[] xOrigins, int[] yOrigins) {
            this.columnOrigins = xOrigins;
            this.rowOrigins = yOrigins;
        }
        
        public int getX() {
            return columnOrigins[0];
        }
        
        public int getY() {
            return rowOrigins[0];
        }
        
        public int getWidth() {
            return columnOrigins[columnOrigins.length-1] - columnOrigins[0];
        }
        
        public int getHeight() {
            return rowOrigins[rowOrigins.length-1] - rowOrigins[0];
        }
        
    }
    
    // Parsing and Decoding of Column and Row Descriptions ******************
    
    /**
     * Parses and splits encoded column specifications and returns an array of
     * <code>ColumnSpec</code>s.
     * 
     * @return an array of decoded column specifications
     * @throws NullPointerException if the string description is null
     */
    private static ColumnSpec[] decodeColSpecs(String description) {
        if (description == null) 
            throw new NullPointerException("The column description must not be null.");
        
        StringTokenizer tokenizer = new StringTokenizer(description, ", ");
        int columnCount = tokenizer.countTokens();
        ColumnSpec[] colSpecs = new ColumnSpec[columnCount]; 
        for (int i = 0; i < columnCount; i++) {
            colSpecs[i] = new ColumnSpec(tokenizer.nextToken());
        }
        return colSpecs;
    }


    /**
     * Parses and splits encoded row specifications and returns an array of
     * <code>RowSpec</code>s.
     * 
     * @return an array of decoded row specifications
     * @throws NullPointerException if the string description is null
     */
    private static RowSpec[] decodeRowSpecs(String description) {
        if (description == null) 
            throw new NullPointerException("The row description must not be null.");
        
        StringTokenizer tokenizer = new StringTokenizer(description, ", ");
        int rowCount = tokenizer.countTokens();
        RowSpec[] rowSpecs = new RowSpec[rowCount]; 
        for (int i = 0; i < rowCount; i++) {
            rowSpecs[i] = new RowSpec(tokenizer.nextToken());
        }
        return rowSpecs;
    }


    // Helper Code **********************************************************
    
    /**
     * Creates and answers a deep copy of the given array. Unlike #clone
     * that performs a shallow copy, this method copies both array levels.
     * 
     * @param array   the array to clone
     * @return a deep copy of the given array
     */
    private int[][] deepClone(int[][] array) {
        int[][] result = new int[array.length][];
        for (int i = 0; i < result.length; i++) {
            result[i] = (int[]) array[i].clone();
        }
        return result;
    }
    

    // Debug Helper Code ****************************************************
 
    /*
    // Prints the given column widths and row heights. 
    private void printSizes(String title, int[] colWidths, int[] rowHeights) {
        System.out.println();
        System.out.println(title);
        int totalWidth = 0;
        System.out.print("Column widths: ");
        for (int i=0; i < getColumnCount(); i++) {
            int width = colWidths[i];
            totalWidth += width;
            System.out.print(width + ", ");
        }
        System.out.println(" Total=" + totalWidth);
        
        int totalHeight = 0;
        System.out.print("Row heights:   ");
        for (int i=0; i < getRowCount(); i++) {
            int height = rowHeights[i];
            totalHeight += height;
            System.out.print(height + ", ");
        }
        System.out.println(" Total=" + totalHeight);
        System.out.println();
    }

    */

    
}
