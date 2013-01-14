/*
 * Copyright (c) 2002-2013 JGoodies Software GmbH. All Rights Reserved.
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

package com.jgoodies.forms.factories;

import static com.jgoodies.common.base.Preconditions.checkArgument;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.util.LayoutStyle;

/**
 * Provides constants and factory methods for {@code Border}s that use
 * instances of {@link ConstantSize} to define the margins.<p>
 *
 * <strong>Examples:</strong><br>
 * <pre>
 * Borders.DLU2
 * Borders.createEmptyBorder(Sizes.DLUY4, Sizes.DLUX2, Sizes.DLUY4, Sizes.DLUX2);
 * Borders.createEmptyBorder("4dlu, 2dlu, 4dlu, 2dlu");
 * </pre>
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.14 $
 *
 * @see     Border
 * @see     Sizes
 */
public final class Borders {

    private Borders() {
        // Overrides default constructor; prevents instantiation.
    }


    // Constant Borders *****************************************************

    /**
     * A prepared and reusable EmptyBorder without gaps.
     */
    public static final Border EMPTY =
        new javax.swing.border.EmptyBorder(0, 0, 0, 0);


    /**
     * A prepared and reusable Border with 2dlu on all sides.
     */
    public static final Border DLU2 =
        createEmptyBorder(Sizes.DLUY2,
                          Sizes.DLUX2,
                          Sizes.DLUY2,
                          Sizes.DLUX2);


    /**
     * A prepared and reusable Border with 4dlu on all sides.
     */
    public static final Border DLU4 =
        createEmptyBorder(Sizes.DLUY4,
                          Sizes.DLUX4,
                          Sizes.DLUY4,
                          Sizes.DLUX4);


    /**
     * A prepared and reusable Border with 7dlu on all sides.
     */
    public static final Border DLU7 =
        createEmptyBorder(Sizes.DLUY7,
                          Sizes.DLUX7,
                          Sizes.DLUY7,
                          Sizes.DLUX7);


    /**
     * A prepared and reusable Border with 9dlu on all sides.
     *
     * @since 1.6
     */
    public static final Border DLU9 =
        createEmptyBorder(Sizes.DLUY9,
                          Sizes.DLUX9,
                          Sizes.DLUY9,
                          Sizes.DLUX9);


    /**
     * A prepared Border with 14dlu on all sides.
     */
    public static final Border DLU14 =
        createEmptyBorder(Sizes.DLUY14,
                          Sizes.DLUX14,
                          Sizes.DLUY14,
                          Sizes.DLUX14);


    /**
     * A prepared Border with 21dlu on all sides.
     *
     * @since 1.2
     */
    public static final Border DLU21 =
        createEmptyBorder(Sizes.DLUY21,
                          Sizes.DLUX21,
                          Sizes.DLUY21,
                          Sizes.DLUX21);


    /**
     * A standardized Border that describes the gap between a component
     * and a button bar in its bottom.
     */
    public static final Border BUTTON_BAR_PAD =
        createEmptyBorder(
            LayoutStyle.getCurrent().getButtonBarPad(),
            Sizes.dluX(0),
            Sizes.dluY(0),
            Sizes.dluX(0));


    /**
     * A standardized Border that describes the border around
     * a dialog content that has no tabs.
     *
     * @see #TABBED_DIALOG
     */
    public static final Border DIALOG =
        createEmptyBorder(
            LayoutStyle.getCurrent().getDialogMarginY(),
            LayoutStyle.getCurrent().getDialogMarginX(),
            LayoutStyle.getCurrent().getDialogMarginY(),
            LayoutStyle.getCurrent().getDialogMarginX()
        );


    /**
     * A standardized Border that describes the border around
     * a dialog content that uses tabs.
     *
     * @see #DIALOG
     */
    public static final Border TABBED_DIALOG =
        createEmptyBorder(
                LayoutStyle.getCurrent().getTabbedDialogMarginY(),
                LayoutStyle.getCurrent().getTabbedDialogMarginX(),
                LayoutStyle.getCurrent().getTabbedDialogMarginY(),
                LayoutStyle.getCurrent().getTabbedDialogMarginX()
        );


    // Factory Methods ******************************************************

    /**
     * Creates and returns an {@code EmptyBorder} with the specified
     * gaps.
     *
     * @param top		the top gap
     * @param left		the left-hand side gap
     * @param bottom	the bottom gap
     * @param right	the right-hand side gap
     * @return an {@code EmptyBorder} with the specified gaps
     *
     * @throws NullPointerException if top, left, bottom, or right is {@code null}
     *
     * @see #createEmptyBorder(String)
     */
    public static Border createEmptyBorder(ConstantSize top,   ConstantSize left,
                                            ConstantSize bottom, ConstantSize right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    /**
     * Creates and returns a {@code Border} using sizes as specified by
     * the given string. This string is a comma-separated encoding of
     * 4 {@code ConstantSize}s.
     *
     * @param encodedSizes	 top, left, bottom, right gap encoded as String
     * @return an {@code EmptyBorder} with the specified gaps
     *
     * @see #createEmptyBorder(ConstantSize, ConstantSize, ConstantSize, ConstantSize)
     */
    public static Border createEmptyBorder(String encodedSizes) {
        String[] token = encodedSizes.split("\\s*,\\s*");
        int tokenCount = token.length;
        checkArgument(token.length == 4,
                "The border requires 4 sizes, but \"%s\" has %d.", encodedSizes, Integer.valueOf(tokenCount));
        ConstantSize top    = Sizes.constant(token[0], false);
        ConstantSize left   = Sizes.constant(token[1], true);
        ConstantSize bottom = Sizes.constant(token[2], false);
        ConstantSize right  = Sizes.constant(token[3], true);
        return createEmptyBorder(top, left, bottom, right);
    }


    /**
     * An empty border that uses 4 instances of {@link ConstantSize}
     * to define the top, left, bottom and right gap.
     */
    public static final class EmptyBorder extends AbstractBorder {

        private final ConstantSize top;
        private final ConstantSize left;
        private final ConstantSize bottom;
        private final ConstantSize right;

        private EmptyBorder(
                ConstantSize top,
                ConstantSize left,
                ConstantSize bottom,
                ConstantSize right) {
            if (   top == null
                || left == null
                || bottom == null
                || right == null) {
                throw new NullPointerException("The top, left, bottom, and right must not be null.");
            }
            this.top    = top;
            this.left   = left;
            this.bottom = bottom;
            this.right  = right;
        }

        /**
         * Returns the insets of the border.
         *
         * @param c      the component for which this border insets value applies
         * @param insets the insets to be reinitialized
         * @return the {@code insets} object
         */
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top    = top.getPixelSize(c);
            insets.left   = left.getPixelSize(c);
            insets.bottom = bottom.getPixelSize(c);
            insets.right  = right.getPixelSize(c);
            return insets;
        }

        /**
         * Returns the insets of the border.
         *
         * @param c the component for which this border insets value applies
         * @return the border's Insets
         */
        @Override
        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, new Insets(0, 0, 0, 0));
        }

        /**
         * Returns this border's top size.
         *
         * @return this border's top size
         */
        public ConstantSize top() {
            return top;
        }

        /**
         * Returns this border's left size.
         *
         * @return this border's left size
         */
        public ConstantSize left() {
            return left;
        }

        /**
         * Returns this border's bottom size.
         *
         * @return this border's bottom size
         */
        public ConstantSize bottom() {
            return bottom;
        }

        /**
         * Returns this border's right size.
         *
         * @return this border's right size
         */
        public ConstantSize right() {
            return right;
        }

    }


}

