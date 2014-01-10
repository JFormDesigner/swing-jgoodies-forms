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

package com.jgoodies.forms.util;

import com.jgoodies.forms.layout.CellConstraints;


/**
 * Defines constraints for components that are layed out with the
 * {@code FormLayout}. Defines the components display area: 
 * grid x, grid y, column span, row span, horizontal alignment, 
 * and vertical alignment.
 * <p>
 * Most methods return <em>this</em> object to enabled method chaining.
 * <p>
 * This class differs from its superclass in that it describes
 * alignment and resizing much like the {@code GridBagConstraints}.
 *
 * @author 	Karsten Lentzsch
 */
public final class GridBagFormConstraints extends CellConstraints {
    
    // Anchors **************************************************************

    /**
     * Put the component in the center of its display area.
     */
    public static final int CENTER = -10;

    /**
      * Put the component at the top of its display area,
      * centered horizontally.
      */
    public static final int NORTH = -11;

    /**
     * Put the component at the top-right corner of its display area.
     */
    public static final int NORTHEAST = -12;

    /**
     * Put the component on the right side of its display area,
     * centered vertically.
     */
    public static final int EAST = -13;

    /**
     * Put the component at the bottom-right corner of its display area.
     */
    public static final int SOUTHEAST = -14;

    /**
     * Put the component at the bottom of its display area, centered
     * horizontally.
     */
    public static final int SOUTH = -15;

    /**
      * Put the component at the bottom-left corner of its display area.
      */
    public static final int SOUTHWEST = -16;

    /**
     * Put the component on the left side of its display area,
     * centered vertically.
     */
    public static final int WEST = -17;

    /**
     * Put the component at the top-left corner of its display area.
     */
    public static final int NORTHWEST = -18;


    // Resizing Constants ***************************************************

    /**
     * Do not resize the component.
     */
    public static final int NONE = -1;

    /**
     * Resize the component both horizontally and vertically.
     */
    public static final int BOTH = -2;

    /**
     * Resize the component horizontally but not vertically.
     */
    public static final int HORIZONTAL = -3;

    /**
     * Resize the component vertically but not horizontally.
     */
    public static final int VERTICAL = -4;


	// Instance Creation ****************************************************
    
    /**
     * Constructs a default instance of {@code GridBagFormConstraints}.
     */
    public GridBagFormConstraints() {
        this(1, 1);
    }

    /**
     * Constructs an instance of {@code GridBagFormConstraints} 
     * for the given grid position.
     */
    public GridBagFormConstraints(int gridX, int gridY) {
        this(gridX, gridY, 1, 1);
    }

    /**
     * Constructs an instance of {@code GridBagFormConstraints} 
     * for the given grid position and size.
     */
    public GridBagFormConstraints(int gridX, int gridY, int gridWidth, int gridHeight) {
        this(gridX, gridY, gridWidth, gridHeight, CENTER, NONE);
    }

    /**
     * Constructs an instance of {@code CellConstraints} for the given
     * cell position and size, anchor, and fill.
     */
    public GridBagFormConstraints(int gridX, int gridY, int gridWidth, int gridHeight,
                                   int anchor, int fill) {
        super(gridX, gridY, gridWidth, gridHeight);
        
    }
	

    // Setters **************************************************************

    /**
     * Sets row, column, width, and height; uses the given anchor and fill.
     *
     * @param x		column index
     * @param y		row index
     * @param w		width, column span
     * @param h		height, row span
     * @param anchor	component orientation
     * @param fill		component resize behavior      
     * @return this
     */
    public CellConstraints xywhaf(int x, int y, int w, int h, int anchor, int fill) {
        return xywh(x, y, w, h, translateHAlign(anchor, fill),
                                 translateVAlign(anchor, fill));
    }
    
    // Translating Alignments ***********************************************


    /**
     * Translates an anchor and fill as used by the 
     * {@code java.awt.GridBagConstraints} 
     * to the corresponding horizontal alignment.
     * 
     * @param anchor        the component orientation
     * @param fill          the component resize behavior
     * @return Alignment    the horizontal alignment for the form layout
     */
    private Alignment translateHAlign(int anchor, int fill) {
        if ((fill == BOTH) ||  
            (fill == HORIZONTAL))
            return FILL;

        switch (anchor) {
            case NORTHWEST:
            case WEST:
            case SOUTHWEST :
                return CellConstraints.LEFT;

            case NORTH:
            case CENTER :
            case SOUTH :
                return CellConstraints.CENTER;
                
            case NORTHEAST:
            case EAST:
            case SOUTHEAST :
                return CellConstraints.RIGHT;

            default :
                return DEFAULT;
        }
    }


    /**
     * Translates an anchor and fill as used by the 
     * {@code java.awt.GridBagConstraints} 
     * to the corresponding vertical alignment.
     * 
     * @param anchor        the component orientation
     * @param fill          the component resize behavior
     * @return Alignment    the vertical alignment for the form layout
     */
    private Alignment translateVAlign(int anchor, int fill) {
        if ((fill == BOTH) || (fill == HORIZONTAL))
            return FILL;
            
        switch (anchor) {
            case NORTHWEST:
            case NORTH:
            case NORTHEAST :
                return CellConstraints.TOP;

            case WEST:
            case CENTER:
            case EAST :
                return CellConstraints.CENTER;
                
            case SOUTHWEST:
            case SOUTH:
            case SOUTHEAST :
                return CellConstraints.BOTTOM;

            default :
                return DEFAULT;
        }
    }


}

