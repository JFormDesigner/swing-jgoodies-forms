/*
 * Copyright (c) 2002-2015 JGoodies Software GmbH. All Rights Reserved.
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
import static com.jgoodies.common.base.Preconditions.checkNotNull;
import static com.jgoodies.common.internal.Messages.MUST_NOT_BE_NULL;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.jgoodies.common.base.Strings;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.util.LayoutStyle;

/**
 * Provides constants and factory methods for paddings that use
 * instances of {@link ConstantSize} to define the margins.
 * Paddings are frequently used to add white space around
 * forms, panels, and more generally visual designs.<p>
 *
 * <strong>Examples:</strong><br>
 * <pre>
 * Paddings.DLU2
 * Paddings.createPadding(Sizes.DLUY4, Sizes.DLUX2, Sizes.DLUY4, Sizes.DLUX2);
 * Paddings.createPadding("4dlu, 2dlu, 4dlu, 2dlu");
 * </pre>
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.14 $
 *
 * @see     Border
 * @see     Sizes
 * 
 * @since 1.9
 */
public final class Paddings {

    private Paddings() {
        // Overrides default constructor; prevents instantiation.
    }


    // Constant Borders *****************************************************

    /**
     * A prepared and reusable EmptyBorder without gaps.
     */
    public static final EmptyBorder EMPTY =
        new javax.swing.border.EmptyBorder(0, 0, 0, 0);


    /**
     * A prepared and reusable padding with 2dlu on all sides.
     */
    public static final Padding DLU2 =
        createPadding(Sizes.DLUY2, Sizes.DLUX2, Sizes.DLUY2, Sizes.DLUX2);

    /**
     * A prepared and reusable padding with 4dlu on all sides.
     */
    public static final Padding DLU4 =
        createPadding(Sizes.DLUY4, Sizes.DLUX4, Sizes.DLUY4, Sizes.DLUX4);


    /**
     * A prepared and reusable padding with 7dlu on all sides.
     */
    public static final Padding DLU7 =
        createPadding(Sizes.DLUY7, Sizes.DLUX7, Sizes.DLUY7, Sizes.DLUX7);


    /**
     * A prepared and reusable padding with 9dlu on all sides.
     */
    public static final Padding DLU9 =
        createPadding(Sizes.DLUY9, Sizes.DLUX9, Sizes.DLUY9, Sizes.DLUX9);


    /**
     * A prepared and reusable padding with 14dlu on all sides.
     */
    public static final Padding DLU14 =
        createPadding(Sizes.DLUY14, Sizes.DLUX14, Sizes.DLUY14, Sizes.DLUX14);


    /**
     * A prepared padding with 21dlu on all sides.
     */
    public static final Padding DLU21 =
        createPadding(Sizes.DLUY21, Sizes.DLUX21, Sizes.DLUY21, Sizes.DLUX21);


    /**
     * A standardized reusable padding intended for the gap
     * between a component and a button bar in its bottom.
     */
    public static final Padding BUTTON_BAR_PAD =
        createPadding(
            LayoutStyle.getCurrent().getButtonBarPad(),
            Sizes.dluX(0),
            Sizes.dluY(0),
            Sizes.dluX(0));


    /**
     * A standardized reusable padding for dialogs without tabs.
     *
     * @see #TABBED_DIALOG
     */
    public static final Padding DIALOG =
        createPadding(
            LayoutStyle.getCurrent().getDialogMarginY(),
            LayoutStyle.getCurrent().getDialogMarginX(),
            LayoutStyle.getCurrent().getDialogMarginY(),
            LayoutStyle.getCurrent().getDialogMarginX()
        );


    /**
     * A standardized reusable padding for dialogs that have tabs.
     *
     * @see #DIALOG
     */
    public static final Padding TABBED_DIALOG =
        createPadding(
                LayoutStyle.getCurrent().getTabbedDialogMarginY(),
                LayoutStyle.getCurrent().getTabbedDialogMarginX(),
                LayoutStyle.getCurrent().getTabbedDialogMarginY(),
                LayoutStyle.getCurrent().getTabbedDialogMarginX()
        );


    // Factory Methods ******************************************************

    /**
     * Creates and returns a padding (an instance of {@link EmptyBorder})
     * with the specified margins.
     *
     * @param top		the top margin
     * @param left		the left side margin
     * @param bottom	the bottom margin
     * @param right	    the right-hand side margin
     * @return a padding with the specified margins
     *
     * @throws NullPointerException if top, left, bottom, or right is {@code null}
     *
     * @see #createPadding(String, Object...)
     */
    public static Padding createPadding(ConstantSize top,    ConstantSize left,
                                        ConstantSize bottom, ConstantSize right) {
        return new Padding(top, left, bottom, right);
    }
    

    /**
     * Creates and returns a padding (an instance of {@link EmptyBorder})
     * using sizes as specified by the given string.
     * This string is a comma-separated encoding of 4 {@code ConstantSize}s.
     *
     * @param encodedSizes	 top, left, bottom, right gap encoded as String
     * @param args           optional format arguments,
     *                       used if {@code encodedSizes} is a format string
     * @return a padding with the specified margins
     *
     * @see #createPadding(ConstantSize, ConstantSize, ConstantSize, ConstantSize)
     */
    public static Padding createPadding(String encodedSizes, Object... args) {
        String formattedSizes = Strings.get(encodedSizes, args);
        String[] token = formattedSizes.split("\\s*,\\s*");
        int tokenCount = token.length;
        checkArgument(token.length == 4,
                "The padding requires 4 sizes, but \"%s\" has %d.", formattedSizes, Integer.valueOf(tokenCount));
        ConstantSize top    = Sizes.constant(token[0]);
        ConstantSize left   = Sizes.constant(token[1]);
        ConstantSize bottom = Sizes.constant(token[2]);
        ConstantSize right  = Sizes.constant(token[3]);
        return createPadding(top, left, bottom, right);
    }


    /**
     * An {@link EmptyBorder} that uses 4 instances of {@link ConstantSize}
     * to define the top, left, bottom and right gap.
     */
    public static final class Padding extends EmptyBorder {

        private final ConstantSize topMargin;
        private final ConstantSize leftMargin;
        private final ConstantSize bottomMargin;
        private final ConstantSize rightMargin;

        private Padding(
                ConstantSize top,
                ConstantSize left,
                ConstantSize bottom,
                ConstantSize right) {
            super(0, 0, 0, 0);
            this.topMargin    = checkNotNull(top,    MUST_NOT_BE_NULL, "top");
            this.leftMargin   = checkNotNull(left,   MUST_NOT_BE_NULL, "left");
            this.bottomMargin = checkNotNull(bottom, MUST_NOT_BE_NULL, "bottom");
            this.rightMargin  = checkNotNull(right,  MUST_NOT_BE_NULL, "right");
        }

        @Override
        public Insets getBorderInsets() {
            return getBorderInsets(null);
        }
        
        
        @Override
        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, new Insets(0, 0, 0, 0));
        }
        

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top    = topMargin.getPixelSize(c, false);
            insets.left   = leftMargin.getPixelSize(c, true);
            insets.bottom = bottomMargin.getPixelSize(c, false);
            insets.right  = rightMargin.getPixelSize(c, true);
            return insets;
        }


    }


}

