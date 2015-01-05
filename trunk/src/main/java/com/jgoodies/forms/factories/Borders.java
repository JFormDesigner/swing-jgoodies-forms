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

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.Paddings.Padding;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Sizes;

/**
 * Provides constants and factory methods for paddings (empty borders)
 * that use instances of {@link ConstantSize} to define the margins.<p>
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
 * 
 * @deprecated Replaced by {@link Paddings}.
 */
@Deprecated
public final class Borders {

    private Borders() {
        // Overrides default constructor; prevents instantiation.
    }


    // Constant Borders *****************************************************

    /**
     * A prepared and reusable EmptyBorder without gaps.
     */
    public static final EmptyBorder EMPTY =
        new javax.swing.border.EmptyBorder(0, 0, 0, 0);


    /**
     * A prepared and reusable Border with 2dlu on all sides.
     */
    public static final Padding DLU2 = Paddings.DLU2;


    /**
     * A prepared and reusable Border with 4dlu on all sides.
     */
    public static final Padding DLU4 = Paddings.DLU4;


    /**
     * A prepared and reusable Border with 7dlu on all sides.
     */
    public static final Padding DLU7 = Paddings.DLU7;


    /**
     * A prepared and reusable Border with 9dlu on all sides.
     *
     * @since 1.6
     */
    public static final Padding DLU9 = Paddings.DLU9;


    /**
     * A prepared Border with 14dlu on all sides.
     */
    public static final Padding DLU14 = Paddings.DLU14;


    /**
     * A prepared Border with 21dlu on all sides.
     *
     * @since 1.2
     */
    public static final Padding DLU21 = Paddings.DLU21;


    /**
     * A standardized Border that describes the gap between a component
     * and a button bar in its bottom.
     */
    public static final Padding BUTTON_BAR_PAD = Paddings.BUTTON_BAR_PAD;


    /**
     * A standardized Border that describes the border around
     * a dialog content that has no tabs.
     *
     * @see #TABBED_DIALOG
     */
    public static final Padding DIALOG = Paddings.DIALOG;


    /**
     * A standardized Border that describes the border around
     * a dialog content that uses tabs.
     *
     * @see #DIALOG
     */
    public static final Padding TABBED_DIALOG = Paddings.TABBED_DIALOG;


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
     * 
     * @deprecated Replaced by {@link Paddings#createPadding(ConstantSize, ConstantSize, ConstantSize, ConstantSize)}.
     */
    @Deprecated
    public static Padding createEmptyBorder(ConstantSize top,   ConstantSize left,
                                            ConstantSize bottom, ConstantSize right) {
        return Paddings.createPadding(top, left, bottom, right);
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
     * 
     * @deprecated Replaced by {@link Paddings#createPadding(String)}.
     */
    @Deprecated
    public static Padding createEmptyBorder(String encodedSizes) {
       return Paddings.createPadding(encodedSizes);
    }


}

