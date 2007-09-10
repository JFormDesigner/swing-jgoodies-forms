/*
 * Copyright (c) 2002-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import java.util.Locale;

import com.jgoodies.forms.util.FormUtils;



/**
 * Specifies columns in FormLayout by their default orientation,
 * start size and resizing behavior.<p>
 *
 * <strong>Examples:</strong><br>
 * The following examples specify a column with FILL alignment, a size of
 * 10&nbsp;dlu that won't grow.
 * <pre>
 * new ColumnSpec(Sizes.dluX(10));
 * new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(10), 0.0);
 * new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(10), ColumnSpec.NO_GROW);
 * ColumnSpec.parse("10dlu");
 * ColumnSpec.parse("10dlu:0");
 * ColumnSpec.parse("fill:10dlu:0");
 * </pre><p>
 *
 * The {@link com.jgoodies.forms.factories.FormFactory} provides
 * predefined frequently used ColumnSpec instances.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.11 $
 *
 * @see     com.jgoodies.forms.factories.FormFactory
 */
public final class ColumnSpec extends FormSpec {


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
     * Constructs a ColumnSpec for the given default alignment,
     * size and resize weight.<p>
     *
     * The resize weight must be a non-negative double; you can use
     * <code>NO_GROW</code> as a convenience value for no resize.
     *
     * @param defaultAlignment the column's default alignment
     * @param size             constant, component size or bounded size
     * @param resizeWeight     the column's non-negative resize weight
     *
     * @throws NullPointerException if the {@code size} is {@code null}
     * @throws IllegalArgumentException if the size is invalid or
     *      the {@code resizeWeight} is negative
     */
    public ColumnSpec(DefaultAlignment defaultAlignment,
                        Size size,
                        double resizeWeight) {
		super(defaultAlignment, size, resizeWeight);
	}


    /**
     * Constructs a ColumnSpec for the given size using the
     * default alignment, and no resizing.
     *
     * @param size             constant size, component size, or bounded size
     * @throws IllegalArgumentException if the size is invalid
     */
    public ColumnSpec(Size size) {
        super(DEFAULT, size, NO_GROW);
    }


    /**
     * Constructs a ColumnSpec from the specified encoded description.
     * The description will be parsed to set initial values.
     * Layout variables will not be mapped.
     *
     * @param encodedDescription	the encoded description
     *
     * @deprecated Replaced by {@link #parse(String)}
     */
	public ColumnSpec(String encodedDescription) {
	    super(DEFAULT, encodedDescription);
	}


    // Factory Methods ********************************************************

    /**
     * Creates and returns a {@link ColumnSpec} that represents a gap with the
     * specified {@link ConstantSize}.
     *
     * @param gapWidth   specifies the gap width
     * @return a ColumnSpec that describes a horizontal gap
     *
     * @throws NullPointerException if {@code gapWidth} is {@code null}
     *
     * @since 1.2
     */
    public static ColumnSpec createGap(ConstantSize gapWidth) {
        return new ColumnSpec(ColumnSpec.LEFT, gapWidth, ColumnSpec.NO_GROW);
    }


    /**
     * Parses and splits encoded column specifications using the default
     * {@link LayoutMap} and returns an array of ColumnSpec objects.
     *
     * @param encodedColumnSpecs  comma separated encoded column specifications
     * @return an array of decoded column specifications
     * @throws NullPointerException if the encoded column specifications string
     *     is {@code null}
     *
     * @see ColumnSpec#ColumnSpec(String)
     * @see LayoutMap#getDefault()
     *
     * @deprecated Replaced by {@link #parseAll(String, LayoutMap)}
     */
    public static ColumnSpec[] decodeSpecs(String encodedColumnSpecs) {
       return parseAll(encodedColumnSpecs, null);
    }


    /**
     * Parses the encoded column specification and returns a ColumnSpec object
     * that represents the string. Layout variables are mapped using the
     * default LayoutMap.
     *
     * @param encodedColumnSpec    the encoded column specification
     *
     * @return a ColumnSpec instance for the given specification
     * @throws NullPointerException if {@code encodedColumnSpec} is {@code null}
     *
     * @see #parse(String, LayoutMap)
     * @see LayoutMap#getDefault()
     *
     * @since 1.2
     */
    public static ColumnSpec parse(String encodedColumnSpec) {
        return parse(encodedColumnSpec, null);
    }


    /**
     * Parses the encoded column specifications and returns a ColumnSpec object
     * that represents the string. Layout variables are mapped using the given
     * LayoutMap. If {@code null}, the default LayoutMap will be used instead.
     *
     * @param encodedColumnSpec    the encoded column specification
     * @param layoutMap            maps layout variables to ColumnSpecs,
     *                             may be {@code null}
     *
     * @return a ColumnSpec instance for the given specification
     * @throws NullPointerException if {@code encodedColumnSpec} is {@code null}
     *
     * @see #parseAll(String, LayoutMap)
     *
     * @since 1.2
     */
    public static ColumnSpec parse(String encodedColumnSpec, LayoutMap layoutMap) {
        String trimmed = encodedColumnSpec.trim();
        FormUtils.assertNotBlank(trimmed, "encoded column specification");
        if (trimmed.charAt(0) == LayoutMap.VARIABLE_PREFIX_CHAR) {
            String key = trimmed.substring(1);
            LayoutMap map = layoutMap != null
                ? layoutMap
                : LayoutMap.getDefault();
            ColumnSpec value = map.getColumnSpec(key.toLowerCase(Locale.ENGLISH));
            if (value != null) {
                return value;
            }
            throw new IllegalArgumentException("Unknown layout variable \"" + trimmed + "\"");
        }
        return new ColumnSpec(trimmed);
    }


    /**
     * Splits and parses the encoded column specifications using the given
     * {@link LayoutMap} and returns an array of ColumnSpec objects.
     *
     * @param encodedColumnSpecs  comma separated encoded column specifications
     * @param layoutMap           maps layout variables to ColumnSpecs,
     *                            may be {@code null}
     * @return an array of decoded column specifications
     * @throws NullPointerException if the encoded column specifications string
     *     is {@code null}
     *
     * @see #decodeSpecs(String)
     *
     * @since 1.2
     */
    public static ColumnSpec[] parseAll(String encodedColumnSpecs, LayoutMap layoutMap) {
        return FormSpecParser.parseColumnSpecs(encodedColumnSpecs, layoutMap);
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


}
