/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch. 
 * Use is subject to license terms.
 *
 */
 
package com.jgoodies.forms.layout;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.StringTokenizer;

/**
 * Defines constraints for components that are layed out with the
 * {@link FormLayout}. Defines the components display area: 
 * grid&nbsp;x, grid&nbsp;y, grid width (column span), 
 * grid height (row span), horizontal alignment and vertical alignment.
 * <p>
 * Most methods return <em>this</em> object to enable method chaining.
 * <p>
 * <b>Examples</b>:<br>
 * The following cell constraints locate a component in the third
 * column of the fifth row; column and row span are 1; the component
 * will be aligned with the column's right-hand side and the row's
 * bottom.
 * <pre>
 * CellConstraints cc = new CellConstraints();
 * cc.xy  (3, 5);
 * cc.xy  (3, 5, CellConstraints.RIGHT, CellConstraints.BOTTOM);
 * cc.xy  (3, 5, "right, bottom");
 * cc.xywh(3, 5, 1, 1);
 * cc.xywh(3, 5, 1, 1, CellConstraints.RIGHT, CellConstraints.BOTTOM);
 * cc.xywh(3, 5, 1, 1, "right, bottom"); 
 * </pre>
 * See also the examples in the {@link FormLayout} class comment.
 *
 * @author	Karsten Lentzsch
 */
public final class CellConstraints implements Cloneable {
    
    // Alignment Constants *************************************************

    /**
     * Use the column's or row's default alignment.
     */
    public static final Alignment DEFAULT = new Alignment("default");

    /**
     * Fill the cell either horizontally or vertically.
     */
    public static final Alignment FILL = new Alignment("fill");

    /**
     * Put the component in the left.
     */
    public static final Alignment LEFT = new Alignment("left");

    /**
     * Put the component in the right.
     */
    public static final Alignment RIGHT = new Alignment("right");

    /**
     * Put the component in the center.
     */
    public static final Alignment CENTER = new Alignment("center");

    /**
     * Put the component in the top.
     */
    public static final Alignment TOP = new Alignment("top");

    /**
     * Put the component in the bottom.
     */
    public static final Alignment BOTTOM = new Alignment("bottom");


    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    
    
    // Fields ***************************************************************
    
    /**
     * Describes the component's horizontal grid origin (starts at 1).
     */
    public int gridX;

    /**
     * Describes the component's vertical grid origin (starts at 1).
     */
    public int gridY;

    /**
     * Describes the component's horizontal grid extend (number of cells).
     */
    public int gridWidth;

    /**
     * Describes the component's vertical grid extent (number of cells).
     */
    public int gridHeight;
    
    /**
     * Describes the component's horizontal alignment.
     */
    public Alignment hAlign;
    
    /**
     * Describes the component's vertical alignment.
     */
    public Alignment vAlign;
    
    /**
     * Describes the component's <code>Insets</code> in it's display area
     */
    public Insets insets;
    
    
    // Instance Creation ****************************************************
    
    /**
     * Constructs a default instance of <code>CellConstraints</code>.
     */
    public CellConstraints() {
        this(1, 1);
    }

    /**
     * Constructs an instance of <code>CellConstraints</code> for the given
     * cell position.
     * 
     * @param gridX	the component's horizontal grid origin
     * @param gridY	the component's vertical grid origin
     */
    public CellConstraints(int gridX, int gridY) {
        this(gridX, gridY, 1, 1);
    }

    /**
     * Constructs an instance of <code>CellConstraints</code> for the given
     * cell position, anchor, and fill.
     * 
     * @param gridX	the component's horizontal grid origin
     * @param gridY	the component's vertical grid origin
     * @param hAlign	the component's horizontal alignment
     * @param vAlign	the component's vertical alignment
     */
    public CellConstraints(int gridX, int gridY, 
                            Alignment hAlign, Alignment vAlign) {
        this(gridX, gridY, 1, 1, hAlign, vAlign, EMPTY_INSETS);
    }

    /**
     * Constructs an instance of <code>CellConstraints</code> for the given
     * cell position and size.
     * 
     * @param gridX		the component's horizontal grid origin
     * @param gridY		the component's vertical grid origin
     * @param gridWidth	the component's horizontal extent
     * @param gridHeight	the component's vertical extent
     */
    public CellConstraints(int gridX, int gridY, int gridWidth, int gridHeight) {
        this(gridX, gridY, gridWidth, gridHeight, DEFAULT, DEFAULT);
    }

    /**
     * Constructs an instance of <code>CellConstraints</code> for the given
     * cell position and size, anchor, and fill.
     * 
     * @param gridX		the component's horizontal grid origin
     * @param gridY		the component's vertical grid origin
     * @param gridWidth	the component's horizontal extent
     * @param gridHeight	the component's vertical extent
     * @param hAlign    	the component's horizontal alignment
     * @param vAlign    	the component's vertical alignment
     */
    public CellConstraints(int gridX, int gridY, int gridWidth, int gridHeight,
                            Alignment hAlign, Alignment vAlign) {
        this(gridX, gridY, gridWidth, gridHeight, hAlign, vAlign, EMPTY_INSETS);
    }

    /**
     * Constructs an instance of <code>CellConstraints</code> for 
     * the complete set of available properties.
     * 
     * @param gridX     	the component's horizontal grid origin
     * @param gridY     	the component's vertical grid origin
     * @param gridWidth 	the component's horizontal extent
     * @param gridHeight	the component's vertical extent
     * @param hAlign		the component's horizontal alignment
     * @param vAlign		the component's vertical alignment
     * @param insets		the component's display area <code>Insets</code>
     * @throws IndexOutOfBoundsException if the grid origin or extent is
     * negative
     * @throws NullPointerException if the horizontal or vertical 
     * Alignment is null
     */
    public CellConstraints(int gridX, int gridY, int gridWidth, int gridHeight, 
                            Alignment hAlign, Alignment vAlign, Insets insets) {
		this.gridX      = gridX;
        this.gridY      = gridY;
        this.gridWidth  = gridWidth;
        this.gridHeight = gridHeight;
        this.hAlign     = hAlign;
        this.vAlign     = vAlign;
        this.insets     = insets;
        if (gridX <= 0)
            throw new IndexOutOfBoundsException("The grid x must be a positive number.");
        if (gridY <= 0)
            throw new IndexOutOfBoundsException("The grid y must be a positive number.");
        if (gridWidth <= 0)
            throw new IndexOutOfBoundsException("The grid width must be a positive number.");
        if (gridHeight <= 0)
            throw new IndexOutOfBoundsException("The grid height must be a positive number.");
        if (hAlign == null)
            throw new NullPointerException("The horizontal alignment must not be null.");
        if (vAlign == null)
            throw new NullPointerException("The vertical alignment must not be null.");
    }
    
    /**
     * Constructs an instance of <code>CellConstraints</code> from
     * the given encoded string properties.
     * 
     * @param encodedConstraints	the constraints encoded as string
     */
    public CellConstraints(String encodedConstraints) {
        this();
        initFromConstraints(encodedConstraints);
    }        
	

    // Setters **************************************************************

    /**
     * Sets row and column origins; sets width and height to 1; 
     * uses the default alignments.
     * 
     * @param col     the new column index
     * @param row     the new row index
     * @return this
     */
    public CellConstraints xy(int col, int row) {
        return xywh(col, row, 1, 1);
    }
	
    /**
     * Sets row and column origins; sets width and height to 1; 
     * decodes alignments from the given string description.
     * 
     * @param col                the new column index
     * @param row                the new row index
     * @param encodedAlignments  string describing the alignments
     * @return this
     */
    public CellConstraints xy(int col, int row, String encodedAlignments) {
        return xywh(col, row, 1, 1, encodedAlignments);
    }

    /**
     * Sets the row and column origins; sets width and height to 1;
     * set horizontal and vertical alignment using the specified objects.
     *
     * @param col       the new column index
     * @param row       the new row index
     * @param colAlign  horizontal component alignment
     * @param rowAlign  vertical component alignment     
     * @return this
     */
    public CellConstraints xy(int col, int row, 
                              Alignment colAlign, Alignment rowAlign) {
        return xywh(col, row, 1, 1, colAlign, rowAlign);
    }
    

    /**
     * Sets the row, column, width, and height; 
     * uses default alignments.
     * 
     * @param col      the new column index
     * @param row      the new row index
     * @param colSpan  the column span or grid width
     * @param rowSpan  the row span or grid height
     * @return this
     */
    public CellConstraints xywh(int col, int row, int colSpan, int rowSpan) {
        return xywh(col, row, colSpan, rowSpan, DEFAULT, DEFAULT);
    }
	
    /**
     * Sets the row, column, width, and height; 
     * decodes the horizontal and vertical alignments from the given string.
     *  
     * @param col                the new column index
     * @param row                the new row index
     * @param colSpan            the column span or grid width
     * @param rowSpan            the row span or grid height
     * @param encodedAlignments  string describing the alignments
     * @return this
     */
    public CellConstraints xywh(int col, int row, int colSpan, int rowSpan, 
                                 String encodedAlignments) {
        CellConstraints result = xywh(col, row, colSpan, rowSpan);
        result.setAlignments(encodedAlignments);
        return result;
    }

    /**
     * Sets the row, column, width, and height; sets the horizontal 
     * and vertical aligment using the specified alignment objects.
     *
     * @param col       the new column index
     * @param row       the new row index
     * @param colSpan   the column span or grid width
     * @param rowSpan   the row span or grid height
     * @param colAlign  horizontal component alignment
     * @param rowAlign  vertical component alignment     
     * @return this
     */
    public CellConstraints xywh(int col, int row, int colSpan, int rowSpan, 
                                 Alignment colAlign, Alignment rowAlign) {
        this.gridX      = col;
        this.gridY      = row;
        this.gridWidth  = colSpan;
        this.gridHeight = rowSpan;
        this.hAlign     = colAlign;
        this.vAlign     = rowAlign;
        return this;
    }
    

    // Parsing and Decoding String Descriptions *****************************
    
    /**
     * Decodes and answers the grid bounds and alignments for this 
     * constraints as an array of six integers. The string representation
     * is a comma separated sequence, one of
     * <pre>
     * "x, y"
     * "x, y, w, h"
     * "x, y, hAlign, vAlign"
     * "x, y, w, h, hAlign, vAlign"
     * </pre> 
     * 
     * @param encodedAlignments represents horizontal and vertical alignment
     * @throws IllegalArgumentException if the encoded constraints do not
     * follow the constraint syntax
     */
    private void initFromConstraints(String encodedConstraints) {
        StringTokenizer tokenizer = new StringTokenizer(encodedConstraints, " ,");
        int argCount = tokenizer.countTokens();
        if (!(argCount == 2 || argCount == 4 || argCount == 6))
           throw new IllegalArgumentException(
                    "You must provide 2, 4 or 6 arguments.");
        
        Integer nextInt = decodeInt(tokenizer.nextToken());
        if (nextInt == null) { 
            throw new IllegalArgumentException(
                    "First cell constraint element must be a number.");
        }
        gridX = nextInt.intValue();
        if (gridX <= 0)
            throw new IndexOutOfBoundsException("The grid x must be a positive number.");
            
        nextInt = decodeInt(tokenizer.nextToken());
        if (nextInt == null) {
            throw new IllegalArgumentException(
                    "Second cell constraint element must be a number.");
        }
        gridY = nextInt.intValue();
        if (gridY <= 0)
            throw new IndexOutOfBoundsException(
                    "The grid y must be a positive number.");

        if (!tokenizer.hasMoreTokens())
            return;
            
        String token = tokenizer.nextToken();
        nextInt = decodeInt(token);
        if (nextInt != null) {
            // Case: "x, y, w, h" or
            //       "x, y, w, h, hAlign, vAlign"
            gridWidth = nextInt.intValue();
            if (gridWidth <= 0)
                throw new IndexOutOfBoundsException(
                    "The grid width must be a positive number.");
            nextInt = decodeInt(tokenizer.nextToken());
            if (nextInt == null)
                throw new IllegalArgumentException(
                    "Fourth cell constraint element must be like third.");
            gridHeight = nextInt.intValue();
            if (gridHeight <= 0)
                throw new IndexOutOfBoundsException(
                    "The grid height must be a positive number.");

            if (!tokenizer.hasMoreTokens())
                return;
            token = tokenizer.nextToken();
        }
            
        hAlign = decodeAlignment(token);
        vAlign = decodeAlignment(tokenizer.nextToken());
    }
    
    
    /**
     * Decodes a string description for the horizontal and vertical 
     * alignment and set the alignment values.
     * <p>
     * Valid horizontal aligmnents are: left, middle, right, default, and fill.
     * Valid vertical alignments are: top, center, bottom, default, and fill.
     * The anchor's string representation abbreviates the alignment:
     * l, m, r, d, f, t, c, and b. 
     * <p>
     * Anchor examples:
     * "mc" is centered, "lt" is northwest, "mt" is north, "rc" east.
     * "md" is horizontally centered and uses the row's default alignment.
     * "dt" is on top of the cell and uses the column's default alignment.
     * <p>
     *
     * @param encodedAlignments represents horizontal and vertical alignment
     */
    private void setAlignments(String encodedAlignments) {
        StringTokenizer tokenizer = new StringTokenizer(encodedAlignments, " ,");
        hAlign = decodeAlignment(tokenizer.nextToken());
        vAlign = decodeAlignment(tokenizer.nextToken());
    }

    /**
     * Decodes an integer string representation and answers the
     * associated Integer or null in case of an invalid number format.
     * 
     * @param token		the encoded integer
     * @return the decoded Integer or null
     */
    private Integer decodeInt(String token) {
        try {
            return Integer.decode(token);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Parses an alignment string description and 
     * answers the corresponding alignment value.
     * 
     * @param encodedAlignment	the encoded alignment
     * @return the associated <code>Alignment</code> instance
     */
    private Alignment decodeAlignment(String encodedAlignment) {
        return Alignment.valueOf(encodedAlignment);
    }

    /**
     * Checks and verifies that this constraints object has valid grid 
     * index values, i. e. the display area cells are inside the form's grid. 
     * 
     * @param colCount  number of columns in the grid
     * @param rowCount  number of rows in the grid
     * @throws IndexOutOfBoundsException if the cell is outside the grid
     */
    void ensureValidGridBounds(int colCount, int rowCount) {
        if (gridX <= 0) {
            throw new IndexOutOfBoundsException(
                "The column index " + gridX + " must be positive.");
        }
        if (gridX > colCount) {
            throw new IndexOutOfBoundsException(
                "The column index " + gridX + " must be less than or equal to " 
                    + colCount + ".");
        }
        if (gridX + gridWidth - 1 > colCount) {
            throw new IndexOutOfBoundsException(
                "The grid width " + gridWidth + " must be less than or equal to "
                    + (colCount - gridX + 1) + ".");
        }
        if (gridY <= 0) {
            throw new IndexOutOfBoundsException(
                "The row index " + gridY + " must be positive.");
        }
        if (gridY > rowCount) {
            throw new IndexOutOfBoundsException(
                "The row index " + gridY + " must be less than or equal to "
                    + rowCount + ".");
        }
        if (gridY + gridHeight - 1 > rowCount) {
            throw new IndexOutOfBoundsException(
                "The grid height " + gridHeight + " must be less than or equal to "
                    + (rowCount - gridY + 1) + ".");
        }
    }
    
    
    // Settings Component Bounds ********************************************
    
    /**
     * Sets the component's bounds using the given component and cell bounds.
     * 
     * @param c		  		the component to set bounds
     * @param colSpec	  		the specification for the component's column
     * @param rowSpec	  		the specification for the component's row
     * @param cellBounds 		the cell's bounds
     * @param minWidthMeasure		measures the minimum width
     * @param minHeightMeasure		measures the minimum height
     * @param prefWidthMeasure		measures the preferred width
     * @param prefHeightMeasure	measures the preferred height
     */
    void setBounds(Component c, FormLayout layout, 
                   Rectangle cellBounds, 
                   FormLayout.Measure minWidthMeasure,
                   FormLayout.Measure minHeightMeasure,
                   FormLayout.Measure prefWidthMeasure,
                   FormLayout.Measure prefHeightMeasure) {
        ColumnSpec colSpec = gridWidth  == 1 ? layout.getColumnSpec(gridX) : null;
        RowSpec    rowSpec = gridHeight == 1 ? layout.getRowSpec(gridY) : null;
        Alignment concreteHAlign = concreteAlignment(this.hAlign, colSpec);
        Alignment concreteVAlign = concreteAlignment(this.vAlign, rowSpec);
        Insets concreteInsets = this.insets != null ? this.insets : EMPTY_INSETS;
        int cellX = cellBounds.x + concreteInsets.left;
        int cellY = cellBounds.y + concreteInsets.top;
        int cellW = cellBounds.width  - concreteInsets.left - concreteInsets.right;
        int cellH = cellBounds.height - concreteInsets.top  - concreteInsets.bottom;
        int compW = componentSize(c, colSpec, cellW, minWidthMeasure,
                                                     prefWidthMeasure);
        int compH = componentSize(c, rowSpec, cellH, minHeightMeasure,
                                                     prefHeightMeasure);
        int x = origin(concreteHAlign, cellX, cellW, compW); 
        int y = origin(concreteVAlign, cellY, cellH, compH);
        int w = extent(concreteHAlign, cellW, compW);
        int h = extent(concreteVAlign, cellH, compH);
        c.setBounds(x, y, w, h);
    }
    
    
    /**
     * Computes and answers the concrete alignment. Takes into account
     * the cell alignment and <i>the</i> <code>FormSpec</code> if applicable.
     * <p>
     * If this constraints object doesn't belong to a single column or row,
     * the <code>formSpec</code> parameter is <code>null</code>.
     * In this case the cell alignment is answered, but <code>DEFAULT</code>
     * is mapped to <code>FILL</code>.
     * <p>
     * If the cell belongs to a single column or row, we use the cell
     * alignment, unless it is <code>DEFAULT</code>, where the alignment 
     * is inherited from the column or row resp.
     * 
     * @param alignment   this cell's aligment
     * @param formSpec    the associated column or row specification
     * @return the concrete alignment
     */
    private Alignment concreteAlignment(Alignment cellAlignment, FormSpec formSpec) {
        return formSpec == null 
            ? (cellAlignment == DEFAULT ? FILL : cellAlignment)
            : usedAlignment(cellAlignment, formSpec);
    }

    /**
     * Answers the alignment used for a given form constraints object.
     * The cell alignment overrides the column or row default, unless
     * it is <code>DEFAULT</code>. In the latter case, we use the
     * column or row alignment.
     * 
     * @param cellAlignment   this cell constraint's alignment
     * @param formSpec        the associated column or row specification
     * @return the alignment used
     */
    private Alignment usedAlignment(Alignment cellAlignment, FormSpec formSpec) {
        if (cellAlignment != CellConstraints.DEFAULT) {
            // Cell alignments other than DEFAULT override col/row alignments 
            return cellAlignment;
        }
        FormSpec.DefaultAlignment defaultAlignment = formSpec.getDefaultAlignment();
        if (defaultAlignment == FormSpec.FILL_ALIGN) 
            return FILL;
        if (defaultAlignment == ColumnSpec.LEFT) 
            return LEFT;
        else if (defaultAlignment == FormSpec.CENTER_ALIGN)
            return CENTER;
        else if (defaultAlignment == ColumnSpec.RIGHT) 
            return RIGHT;
        else if (defaultAlignment == RowSpec.TOP) 
            return TOP;
        else 
            return BOTTOM;    
    }
    
    /**
     * Computes and answers the pixel size of the given component using the
     * given form specification, measures, and cell size.
     * 
     * @param component	the component to measure 
     * @param formSpec		the specification of the component's column/row
     * @param minMeasure	the measure for the minimum size
     * @param prefMeasure	the measure for the preferred size
     * @param cellSize		the cell size
     * @return the component size as measured or a constant
     */
    private int componentSize(Component component,
                               FormSpec formSpec,
                               int cellSize,
                               FormLayout.Measure minMeasure,
                               FormLayout.Measure prefMeasure) {
        if (formSpec == null) {
            return prefMeasure.sizeOf(component);
        } else if (formSpec.getSize() == Sizes.MINIMUM) {
            return minMeasure.sizeOf(component);
        } else if (formSpec.getSize() == Sizes.PREFERRED) {
            return prefMeasure.sizeOf(component);
        } else {  // default mode
            return Math.min(cellSize, prefMeasure.sizeOf(component)); 
        }
    }
    
    /**
     * Computes and answers the component's pixel origin.
     * 
     * @param alignment		the component's alignment
     * @param cellOrigin		the origin of the display area
     * @param cellSize			the extent of the display area
     * @param componentSize
     * @return the component's pixel origin
     */
    private int origin(Alignment alignment, 
                        int cellOrigin, 
                        int cellSize, 
                        int componentSize) {
        if (alignment == RIGHT || alignment == BOTTOM) {
            return cellOrigin + cellSize - componentSize;
        } else if (alignment == CENTER) {
            return cellOrigin + (cellSize - componentSize) / 2;
        } else {  // left, top, fill
            return cellOrigin;
        }
    }
    
    /**
     * Answers the component's pixel extent.
     * 
     * @param alignment		the component's alignment
     * @param cellSize			the size of the display area
     * @param componentSize	the component's size
     * @return the component's pixel extent
     */
    private int extent(Alignment alignment, int cellSize, int componentSize) {
        return alignment == FILL
                    ? cellSize
                    : componentSize;
    }
    
    
    
    // Misc *****************************************************************
    
    /**
     * Creates a copy of this form constraints.
     * 
     * @return		a copy of this form constraints
     */
    public Object clone() {
        try {
            CellConstraints c = (CellConstraints) super.clone();
            c.insets = (Insets) insets.clone();
            return c;
        } catch (CloneNotSupportedException e) {
            // This shouldn't happen, since we are Cloneable.
            throw new InternalError();
        }
    }
    
    
    /**
     * Constructs and answers a string representation of this constraints object.
     * 
     * @return	string representation of this constraints object
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer("CellConstraints");
        buffer.append("[x=");
        buffer.append(gridX);
        buffer.append("; y=");
        buffer.append(gridY);
        buffer.append("; w=");
        buffer.append(gridWidth);
        buffer.append("; h=");
        buffer.append(gridHeight);
        buffer.append("; hAlign=");
        buffer.append(hAlign);
        buffer.append("; vAlign=");
        buffer.append(vAlign);
        if (!(EMPTY_INSETS.equals(insets))) {
          buffer.append("; insets=");
          buffer.append(insets);
        }
        
        buffer.append(']');
        return buffer.toString();
    }  
    
    /**
     * Answers a short string representation of this constraints object.
     * 
     * @return a short string representation of this constraints object
     */
    public String toShortString() {
        return toShortString(null);
    }
    
    
    /**
     * Answers a short string representation of this constraints object.
     * This method can use the given <code>FormLayout</code> 
     * to display extra information how default alignments
     * are mapped to concrete alignments. Therefore it asks the 
     * related column and row as specified by this constraints object. 
     * 
     * @return a short string representation of this constraints object
     */
    public String toShortString(FormLayout layout) {
        StringBuffer buffer = new StringBuffer("(");
        buffer.append(formatInt(gridX));
        buffer.append(", ");
        buffer.append(formatInt(gridY));
        buffer.append(", ");
        buffer.append(formatInt(gridWidth));
        buffer.append(", ");
        buffer.append(formatInt(gridHeight));
        buffer.append(", \"");
        buffer.append(hAlign.abbreviation());
        if (hAlign == DEFAULT && layout != null) {
            buffer.append('=');
            ColumnSpec colSpec = gridWidth == 1
                ? layout.getColumnSpec(gridX)
                : null;
            buffer.append(concreteAlignment(hAlign, colSpec).abbreviation());
        }
        buffer.append(", ");
        buffer.append(vAlign.abbreviation());
        if (vAlign == DEFAULT && layout != null) {
            buffer.append('=');
            RowSpec rowSpec = gridHeight == 1
                ? layout.getRowSpec(gridY)
                : null;
            buffer.append(concreteAlignment(vAlign, rowSpec).abbreviation());
        }
        buffer.append("\"");
        if (!(EMPTY_INSETS.equals(insets))) {
          buffer.append(", ");
          buffer.append(insets);
        }
        
        buffer.append(')');
        return buffer.toString();
    }  
    
    
    
    // Helper Class *********************************************************
    
    /**
     * A typesafe enumeration for component alignment types as used by
     * the {@link FormLayout}.
     */
    public static final class Alignment {
        private final String name;

        private Alignment(String name) { 
            this.name = name; 
        }
        
        static Alignment valueOf(String nameOrAbbreviation) {
            String str = nameOrAbbreviation.toLowerCase();
            if (str.equals("d") || str.equals("default"))
                return DEFAULT;
            else if (str.equals("f") || str.equals("fill"))
                return FILL;
            else if (str.equals("c") || str.equals("center"))
                return CENTER;
            else if (str.equals("l") || str.equals("left"))
                return LEFT;
            else if (str.equals("r") || str.equals("right"))
                return RIGHT;
            else if (str.equals("t") || str.equals("top"))
                return TOP;
            else if (str.equals("b") || str.equals("bottom"))
                return BOTTOM;
            else
                throw new IllegalArgumentException(
                    "Invalid alignment " + nameOrAbbreviation
                    + ". Must be one of: left, center, right, top, bottom, "
                    + "fill, default, l, c, r, t, b, f, d.");
        }

        public String toString()  { return name; }
        
        public char abbreviation() { return name.charAt(0); }

    }


    /**
     * Answers an integer that has a minimum of two characters.
     * @param number   the number to format
     * @return a string representation for a number with a minum of two chars
     */
	private String formatInt(int number) {
        String str = Integer.toString(number);
        return number < 10 ? " " + str : str;
    }
}

