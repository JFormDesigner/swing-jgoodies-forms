/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch. 
 * Use is subject to license terms.
 *
 */
 
package com.jgoodies.forms.layout;


/**
 * Specifies columns in {@link FormLayout} by their default orientation, start
 * size and resizing behavior.
 * <p>
 * <b>Examples:</b><br>
 * The following examples specify a column with FILL alignment, a size of 
 * 10&nbsp;dlu that won't grow.
 * <pre>
 * new ColumnSpec(Sizes.dluX(10));
 * new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(10), 0.0);
 * new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(10), ColumnSpec.NO_GROW);
 * new ColumnSpec("10dlu");
 * new ColumnSpec("10dlu:0");
 * new ColumnSpec("fill:10dlu:0");
 * </pre>
 * <p>
 * The {@link com.jgoodies.forms.factories.FormFactory} provides
 * predefined frequently used <code>ColumnSpec</code> instances.
 *
 * @author	Karsten Lentzsch
 * @see     com.jgoodies.forms.factories.FormFactory
 */

public class ColumnSpec extends FormSpec {
    
    
    // Horizontal Orientations *********************************************
    
    /**
     * By default put components in the left.
     */
    public static final DefaultAlignment LEFT = FormSpec.LEFT_ALIGN;

    /**
     * By default put the components in the center.
     */
    public static final DefaultAlignment CENTER = FormSpec.CENTER_ALIGN;

    /**
     * By default put components in the middle.
     */
    public static final DefaultAlignment MIDDLE = CENTER;

    /**
     * By default put components in the right.
     */
    public static final DefaultAlignment RIGHT = FormSpec.RIGHT_ALIGN;

    /**
     * By default fill the component into the column.
     */
    public static final DefaultAlignment FILL = FormSpec.FILL_ALIGN;
    
    /**
     * Unless overridden the default alignment for a column is FILL.
     */
    public static final DefaultAlignment DEFAULT = FILL;


    // Instance Creation ****************************************************

    /**
     * Constructs a <code>ColumnSpec</code> for the given default alignment,
     * size and resize weight.
     * <p> 
     * The resize weight must be a non-negative double; you can use
     * <code>NO_FILL</code> as a convenience value for no resize.
     * 
     * @param defaultAlignment the spec's default alignment
     * @param size             constant, component size or bounded size
     * @param resizeWeight     the spec resize weight      
     * @throws IllegalArgumentException if the resize weight is negative
     */
    public ColumnSpec(DefaultAlignment defaultAlignment, 
                        Size size, 
                        double resizeWeight) {
		super(defaultAlignment, size, resizeWeight);
	}
	
	
    /**
     * Constructs a <code>ColumnSpec</code> for the given size using the
     * default alignment, and no resizing.
     * 
     * @param size             constant size, component size, or bounded size
     * @throws IllegalArgumentException if the pixel size is invalid or the
     * resize weight is negative
     */
    public ColumnSpec(Size size) {
        super(DEFAULT, size, NO_GROW);
    }
    
    /**
     * Constructs a <code>ColumnSpec</code> from the specified encoded
     * description. The description will be parsed to set initial values.
     * 
     * @param encodedDescription	the encoded description
     */
	public ColumnSpec(String encodedDescription) {
        super(DEFAULT, encodedDescription);
	}

    /**
     * Creates and answers an unmodifyable version of this
     * <code>ColumnSpec</code>.
     * 
     * @return an unmodifyable version of this <code>ColumnSpec</code>
     */
    public ColumnSpec asUnmodifyable() {
        return new UnmodifyableColumnSpec(this);
    }


    // Implementing Abstract Behavior ***************************************

    /**
     * Returns if this is a horizontal specification (vs. vertical).
     * Used to distinct between horizontal and vertical dialog units,
     * which have different conversion factors.
     * 
     * @return  always true (for horizontal)
     */
    protected final boolean isHorizontal() { 
        return true; 
    }


    // An Unmodifyable Version of ColumnSpec *********************************
    
    private static final class UnmodifyableColumnSpec extends ColumnSpec {
        
        private UnmodifyableColumnSpec(ColumnSpec columnSpec) {
            super(columnSpec.getDefaultAlignment(), 
                   columnSpec.getSize(),
                   columnSpec.getResizeWeight());
        }

        /**
         * @throws UnsupportedOperationException always
         */
        public void setDefaultAlignment(DefaultAlignment newDefaultAlignment) {
            throw new UnsupportedOperationException();
        }

        /**
         * @throws UnsupportedOperationException always
         */
        public void setSize(Size size) {
            throw new UnsupportedOperationException();
        }
        
        /**
         * @throws UnsupportedOperationException always
         */
        public void setResizeWeight(double weight) {
            throw new UnsupportedOperationException();
        }
        
        /**
         * Returns this <code>ColumnSpec</code>; it already is unmodifyable.
         * 
         * @return this <code>ColumnSpec</code>
         */
        public ColumnSpec asUnmodifyable() {
            return this;
        }
    
    }
    
    	
}

