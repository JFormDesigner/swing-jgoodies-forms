/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.forms.layout;

/**
 * Specifies rows in in {@link FormLayout} by their default orientation, start
 * size and resizing behavior.
 * <p>
 * <b>Examples:</b><br>
 * The following examples specify a centered row with a size of 14&nbsp;dlu 
 * that won't grow.
 * <pre>
 * new RowSpec(Sizes.dluX(14));
 * new RowSpec(RowSpec.CENTER, Sizes.dluX(14), 0.0);
 * new RowSpec(rowSpec.CENTER, Sizes.dluX(14), RowSpec.NO_GROW);
 * new RowSpec("14dlu");
 * new RowSpec("14dlu:0");
 * new RowSpec("center:14dlu:0");
 * </pre>
 * <p>
 * The {@link com.jgoodies.forms.factories.FormFactory} provides
 * predefined frequently used <code>RowSpec</code> instances.
 *
 * @author	Karsten Lentzsch
 * @see     com.jgoodies.forms.factories.FormFactory
 */

public class RowSpec extends FormSpec {
    
    
    // Vertical Orientations ************************************************
    
    /**
     * By default put the components in the top.
     */
    public static final DefaultAlignment TOP = FormSpec.TOP_ALIGN;

    /**
     * By default put the components in the center.
     */
    public static final DefaultAlignment CENTER = FormSpec.CENTER_ALIGN;

    /**
     * By default put the components in the bottom.
     */
    public static final DefaultAlignment BOTTOM = FormSpec.BOTTOM_ALIGN;

    /**
     * By default fill the component into the row.
     */
    public static final DefaultAlignment FILL = FormSpec.FILL_ALIGN;
    
    /**
     * Unless overridden the default alignment for a row is CENTER.
     */
    public static final DefaultAlignment DEFAULT = CENTER;


    // Instance Creation ****************************************************

    /**
     * Constructs a <code>RowSpec</code> from the given default orientation,
     * size, and resize weight.
     * <p> 
     * The resize weight must be a non-negative double; you can use
     * <code>NO_FILL</code> as a convenience value for no resize.
     * 
     * @param defaultAlignment  the row's default alignment
     * @param size              the row's size as value with unit
     * @param resizeWeight      the row's resize weight
     */
    public RowSpec(DefaultAlignment defaultAlignment,
                    Size size, 
                    double resizeWeight) {
        super(defaultAlignment, size, resizeWeight);
    }

    /**
     * Constructs a <code>RowSpec</code> for the given size using the
     * default alignment, and no resizing.
     * 
     * @param size             constant size, component size, or bounded size
     * @throws IllegalArgumentException if the pixel size is invalid or the
     * resize weight is negative
     */
    public RowSpec(Size size) {
        super(DEFAULT, size, NO_GROW);
    }
    
    /**
     * Constructs a <code>RowSpec</code> from the specified encoded
     * description. The description will be parsed to set initial values.
     * 
     * @param encodedDescription	the encoded description
     */
	public RowSpec(String encodedDescription) {
        super(DEFAULT, encodedDescription);
	}
    
    
    /**
     * Creates and answers an unmodifyable version of this
     * <code>RowSpec</code>.
     * 
     * @return an unmodifyable version of this <code>RowSpec</code>
     */
    public RowSpec asUnmodifyable() {
        return new UnmodifyableRowSpec(this);
    }
    

    // Implementing Abstract Behavior ***************************************

    /**
     * Returns if this is a horizontal specification (vs. vertical).
     * Used to distinct between horizontal and vertical dialog units,
     * which have different conversion factors.
     * 
     * @return true for horizontal, false for vertical
     */
    protected final boolean isHorizontal() { return false; }


    // An Unmodifyable Version of RowSpec ***********************************
    
    private static final class UnmodifyableRowSpec extends RowSpec {
        
        private UnmodifyableRowSpec(RowSpec rowSpec) {
            super(rowSpec.getDefaultAlignment(), 
                   rowSpec.getSize(),
                   rowSpec.getResizeWeight());
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
         * Returns this <code>RowSpec</code>; it already is unmodifyable.
         * 
         * @return this <code>RowSpec</code>
         */
        public RowSpec asUnmodifyable() {
            return this;
        }
    
    }
    
    
    	
}

