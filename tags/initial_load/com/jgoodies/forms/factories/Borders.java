/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */

package com.jgoodies.forms.factories;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.StringTokenizer;

import javax.swing.border.Border;

import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Sizes;

/**
 * Provides constants and factory methods for <code>Border</code>s that use
 * instances of {@link ConstantSize} to define the margins.
 * <p>
 * <b>Examples:</b><br>
 * <pre>
 * Borders.DLU2_BORDER
 * Borders.createEmptyBorder(Sizes.DLUY4, Sizes.DLUX2, Sizes.DLUY4, Sizes.DLUX2);
 * Borders.createEmptyBorder("4dlu, 2dlu, 4dlu, 2dlu");
 * </pre>
 *
 * @author  Karsten Lentzsch
 * @see     Border
 * @see     Sizes
 */
public final class Borders {


    // Constant Borders *****************************************************

    public static final Border EMPTY_BORDER = 
        new javax.swing.border.EmptyBorder(0, 0, 0, 0);
        
    public static final Border DLU2_BORDER = 
        createEmptyBorder(Sizes.DLUY2, 
                          Sizes.DLUX2, 
                          Sizes.DLUY2, 
                          Sizes.DLUX2);
                        
    public static final Border DLU4_BORDER = 
        createEmptyBorder(Sizes.DLUY4, 
                          Sizes.DLUX4, 
                          Sizes.DLUY4, 
                          Sizes.DLUX4);
                        
    public static final Border DLU7_BORDER = 
        createEmptyBorder(Sizes.DLUY7, 
                          Sizes.DLUX7, 
                          Sizes.DLUY7, 
                          Sizes.DLUX7);
                        
    public static final Border DLU14_BORDER = 
        createEmptyBorder(Sizes.DLUY14, 
                          Sizes.DLUX14, 
                          Sizes.DLUY14, 
                          Sizes.DLUX14);
                        
    /*
     * The following three constants use logical sizes that change with the
     * layout style. A future release will likely define them using a class
     * <code>LogicalSize</code> or <code>StyledSize</code>.
     */
     
    public static final Border BUTTON_BAR_GAP_BORDER = 
        createEmptyBorder(Sizes.DLUY6, Sizes.ZERO, Sizes.ZERO, Sizes.ZERO);
                        
    public static final Border DIALOG_BORDER = 
        DLU7_BORDER;
        
    public static final Border TABBED_DIALOG_BORDER = 
        DLU4_BORDER;
    
    
    // Factory Methods ******************************************************
    
    /**
     * Creates and answers an <code>EmptyBorder</code> with the specified
     * gaps.
     * 
     * @param top		the top gap
     * @param left		the left-hand side gap
     * @param bottom	the bottom gap
     * @param right	the right-hand side gap
     * @return an <code>EmptyBorder</code> with the specified gaps
     */
    public static Border createEmptyBorder(ConstantSize top,   ConstantSize left, 
                                            ConstantSize bottom, ConstantSize right) {
        return new EmptyBorder(top, left, bottom, right);
    }
    
    /**
     * Creates and answers a <code>Border</code> using sizes as specified by
     * the given string. This string is a comma-separated encoding of
     * 4 <code>ConstantSize</code>s.
     * 
     * @param encodedSizes	 top, left, bottom, right gap encoded as String
     * @return an <code>EmptyBorder</code> with the specified gaps
     */
    public static Border createEmptyBorder(String encodedSizes) {
        StringTokenizer tokenizer = new StringTokenizer(encodedSizes, ", ");
        int tokenCount = tokenizer.countTokens();
        if (tokenCount != 4) {
            throw new IllegalArgumentException(
                "The border requires 4 sizes, but '" + encodedSizes + 
                "' has " + tokenCount + ".");
        }
        ConstantSize top    = Sizes.constant(tokenizer.nextToken(), false);
        ConstantSize left   = Sizes.constant(tokenizer.nextToken(), true);
        ConstantSize bottom = Sizes.constant(tokenizer.nextToken(), false);
        ConstantSize right  = Sizes.constant(tokenizer.nextToken(), true);
        return createEmptyBorder(top, left, bottom, right);
    }

    /**
     * An empty border that uses 4 instances of {@link ConstantSize} 
     * to define the gaps on all sides.
     */    
    public static final class EmptyBorder implements Border {
        
        private final ConstantSize top;
        private final ConstantSize left;
        private final ConstantSize bottom;
        private final ConstantSize right;
        
        EmptyBorder(ConstantSize top, 
                                ConstantSize left,
                                ConstantSize bottom,
                                ConstantSize right) {
            this.top    = top;
            this.left   = left;
            this.bottom = bottom;
            this.right  = right;
        }

        /**
         * Paints the border for the specified component with the specified 
         * position and size.
         * @param c the component for which this border is being painted
         * @param g the paint graphics
         * @param x the x position of the painted border
         * @param y the y position of the painted border
         * @param width the width of the painted border
         * @param height the height of the painted border
         */
        public void paintBorder(Component c, Graphics g, 
                                int x, int y, int width, int height) {
        }
    
        /**
         * Returns the insets of the border.  
         * @param c the component for which this border insets value applies
         */
        public Insets getBorderInsets(Component c) {
            return new Insets(top.getPixelSize(c),
                               left.getPixelSize(c),
                               bottom.getPixelSize(c),
                               right.getPixelSize(c));
        }
    
        /**
         * Returns whether or not the border is opaque.  If the border
         * is opaque, it is responsible for filling in it's own
         * background when painting.
         */
        public boolean isBorderOpaque() {
            return false;
        }

    }


}
