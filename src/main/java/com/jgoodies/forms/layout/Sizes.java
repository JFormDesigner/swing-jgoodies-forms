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

package com.jgoodies.forms.layout;

import static com.jgoodies.common.internal.Messages.MUST_NOT_BE_NULL;

import java.awt.Component;
import java.util.Locale;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.util.DefaultUnitConverter;
import com.jgoodies.forms.util.UnitConverter;


/**
 * Consists only of static methods that create and convert sizes
 * as required by the FormLayout. The conversion of sizes
 * that are not based on pixel is delegated to an implementation
 * of {@link UnitConverter}. The conversion methods require the
 * layout container as parameter to read its current font and resolution.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.19 $
 *
 * @see     Size
 * @see     UnitConverter
 * @see     DefaultUnitConverter
 */
public final class Sizes {


    // Common Constant Sizes ************************************************

    public static final ConstantSize ZERO   = pixel(0);

    public static final ConstantSize DLUX1  = dluX( 1);
    public static final ConstantSize DLUX2  = dluX( 2);
    public static final ConstantSize DLUX3  = dluX( 3);
    public static final ConstantSize DLUX4  = dluX( 4);
    public static final ConstantSize DLUX5  = dluX( 5);
    public static final ConstantSize DLUX6  = dluX( 6);
    public static final ConstantSize DLUX7  = dluX( 7);
    public static final ConstantSize DLUX8  = dluX( 8);
    public static final ConstantSize DLUX9  = dluX( 9);
    public static final ConstantSize DLUX11 = dluX(11);
    public static final ConstantSize DLUX14 = dluX(14);
    /**
     * 21 horizontal dialog units.
     * @since 1.2
     */
    public static final ConstantSize DLUX21 = dluX(21);

    public static final ConstantSize DLUY1  = dluY( 1);
    public static final ConstantSize DLUY2  = dluY( 2);
    public static final ConstantSize DLUY3  = dluY( 3);
    public static final ConstantSize DLUY4  = dluY( 4);
    public static final ConstantSize DLUY5  = dluY( 5);
    public static final ConstantSize DLUY6  = dluY( 6);
    public static final ConstantSize DLUY7  = dluY( 7);
    public static final ConstantSize DLUY8  = dluY( 8);
    public static final ConstantSize DLUY9  = dluY( 9);
    public static final ConstantSize DLUY11 = dluY(11);
    public static final ConstantSize DLUY14 = dluY(14);
    /**
     * 21 vertical dialog units.
     * @since 1.2
     */
    public static final ConstantSize DLUY21 = dluY(21);


    // Singleton State *******************************************************

    /**
     * Holds the current converter that maps non-pixel sizes to pixels.
     *
     * @see #setUnitConverter(UnitConverter)
     */
    private static UnitConverter unitConverter;


    /**
     * Holds the Unit that is used if no Unit is provided in encoded
     * ConstantSizes.
     *
     * @see #setDefaultUnit(ConstantSize.Unit)
     */
    private static Unit defaultUnit = Unit.PIXEL;


    // Instance Creation ******************************************************

    private Sizes() {
        // Suppresses default constructor, prevents instantiation.
    }


    // Creation of Size Instances *********************************************

    /**
     * Creates and returns an instance of {@code ConstantSize} from the
     * given encoded size and unit description.
     *
     * @param encodedValueAndUnit  value and unit in string representation
     * @return a {@code ConstantSize} for the given value and unit
     */
    public static ConstantSize constant(String encodedValueAndUnit) {
        String lowerCase = encodedValueAndUnit.toLowerCase(Locale.ENGLISH);
        String trimmed = lowerCase.trim();
        return ConstantSize.decode(trimmed);
    }

    /**
     * Creates and returns a ConstantSize for the specified value
     * in horizontal dialog units.
     *
     * @param value	size value in horizontal dialog units
     * @return the associated {@code ConstantSize}
     */
    public static ConstantSize dluX(int value) {
        return ConstantSize.dluX(value);
    }

    /**
     * Creates and returns a ConstantSize for the specified value
     * in vertical dialog units.
     *
     * @param value 	size value in vertical dialog units
     * @return the associated {@code ConstantSize}
     */
    public static ConstantSize dluY(int value) {
        return ConstantSize.dluY(value);
    }

    /**
     * Creates and returns a ConstantSize
     * for the specified pixel value.
     *
     * @param value  value in pixel
     * @return the associated {@code ConstantSize}
     */
    public static ConstantSize pixel(int value) {
        return new ConstantSize(value, Unit.PIXEL);
    }

    /**
     * Creates and returns a BoundedSize for the given basis
     * using the specified lower and upper bounds.
     *
     * @param basis  		the base size
     * @param lowerBound  	the lower bound size
     * @param upperBound  	the upper bound size
     * @return a {@code BoundedSize} for the given basis and bounds
     * @throws NullPointerException if {@code basis} is {@code null},
     *    or if both {@code lowerBound} and {@code upperBound} are {@code null}.
     */
    public static Size bounded(Size basis, Size lowerBound, Size upperBound) {
        return new BoundedSize(basis, lowerBound, upperBound);
    }


    // Unit Conversion ******************************************************

    /**
     * Converts Inches and returns pixels using the specified resolution.
     *
     * @param in           the Inches
     * @param component    the component that provides the graphics object
     * @return the given Inches as pixels
     */
    public static int inchAsPixel(double in, Component component) {
        return in == 0d
            ? 0
            : getUnitConverter().inchAsPixel(in, component);
    }

    /**
     * Converts Millimeters and returns pixels using the resolution of the
     * given component's graphics object.
     *
     * @param mm	        Millimeters
     * @param component    the component that provides the graphics object
     * @return the given Millimeters as pixels
     */
    public static int millimeterAsPixel(double mm, Component component) {
        return mm == 0d
            ? 0
            : getUnitConverter().millimeterAsPixel(mm, component);
    }

    /**
     * Converts Centimeters and returns pixels using the resolution of the
     * given component's graphics object.
     *
     * @param cm	        Centimeters
     * @param component    the component that provides the graphics object
     * @return the given Centimeters as pixels
     */
    public static int centimeterAsPixel(double cm, Component component) {
        return cm == 0d
            ? 0
            : getUnitConverter().centimeterAsPixel(cm, component);
    }

    /**
     * Converts DTP Points and returns pixels using the resolution of the
     * given component's graphics object.
     *
     * @param pt	        DTP Points
     * @param component    the component that provides the graphics object
     * @return the given Points as pixels
     */
    public static int pointAsPixel(int pt, Component component) {
        return pt == 0
            ? 0
            : getUnitConverter().pointAsPixel(pt, component);
    }

    /**
     * Converts horizontal dialog units and returns pixels.
     * Honors the resolution, dialog font size, platform, and l&amp;f.
     *
     * @param dluX         the horizontal dialog units
     * @param component    the component that provides the graphics object
     * @return the given horizontal dialog units as pixels
     */
    public static int dialogUnitXAsPixel(int dluX, Component component) {
        return dluX == 0
            ? 0
            : getUnitConverter().dialogUnitXAsPixel(dluX, component);
    }

    /**
     * Converts vertical dialog units and returns pixels.
     * Honors the resolution, dialog font size, platform, and l&amp;f.
     *
     * @param dluY         the vertical dialog units
     * @param component    the component that provides the graphics object
     * @return the given vertical dialog units as pixels
     */
    public static int dialogUnitYAsPixel(int dluY, Component component) {
        return dluY == 0
            ? 0
            : getUnitConverter().dialogUnitYAsPixel(dluY, component);
    }


    // Accessing the Unit Converter *******************************************

    /**
     * Returns the current {@link UnitConverter}. If it has not been initialized
     * before it will get an instance of {@link DefaultUnitConverter}.
     *
     * @return the current {@code UnitConverter}
     */
    public static UnitConverter getUnitConverter() {
        if (unitConverter == null) {
            unitConverter = DefaultUnitConverter.getInstance();
        }
        return unitConverter;
    }

    /**
     * Sets a new UnitConverter that will be used to convert
     * font-dependent sizes to pixel sizes.
     *
     * @param newUnitConverter  the unit converter to be set
     */
    public static void setUnitConverter(UnitConverter newUnitConverter) {
        unitConverter = newUnitConverter;
    }


    // Default Unit ***********************************************************

    /**
     * Returns the Unit that is used if an encoded ConstantSize contains
     * no unit string.
     *
     * @return the Unit if no unit string is provided
     *
     * @since 1.2
     */
    public static Unit getDefaultUnit() {
        return defaultUnit;
    }


    /**
     * Sets the Unit that shall be used if an encoded ConstantSize
     * provides no unit string.
     *
     * @param unit    the new default Unit, {@code null} for dialog units
     *
     * @throws NullPointerException if {@code unit} is {@code null}
     *
     * @since 1.2
     */
    public static void setDefaultUnit(Unit unit) {
        defaultUnit = Preconditions.checkNotNull(unit, MUST_NOT_BE_NULL, "default unit");
    }


    // Helper Class *********************************************************



}
