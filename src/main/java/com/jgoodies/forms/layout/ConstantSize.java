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

import static com.jgoodies.common.base.Preconditions.checkArgument;

import java.awt.Component;
import java.awt.Container;
import java.io.Serializable;
import java.util.List;

/**
 * An implementation of the {@link Size} interface that represents constant
 * sizes described by a value and unit, for example:
 * 10&nbsp;pixel, 15&nbsp;point or 4&nbsp;dialog units.
 * You can get instances of {@code ConstantSize} using
 * the factory methods and constants in the {@link Sizes} class.
 * Logical constant sizes that vary with the current layout style
 * are delivered by the {@link com.jgoodies.forms.util.LayoutStyle} class.<p>
 *
 * This class supports different size units:
 * <table>
 * <tr><td><b>Unit</b>&nbsp;
 * </td><td>&nbsp;<b>Abbreviation</b>&nbsp;</td><td>&nbsp;
 * <b>Size</b></td></tr>
 * <tr><td>Millimeter</td><td>mm</td><td>0.1 cm</td></tr>
 * <tr><td>Centimeter</td><td>cm</td><td>10.0 mm</td></tr>
 * <tr><td>Inch</td><td>in</td><td>25.4 mm</td></tr>
 * <tr><td>DTP Point</td><td>pt</td><td>1/72 in</td></tr>
 * <tr><td>Pixel</td><td>px</td><td>1/(resolution in dpi) in</td></tr>
 * <tr><td>Dialog Unit</td><td>dlu</td><td>honors l&amp;f, resolution, and
 * dialog font size</td></tr>
 * </table><p>
 *
 * <strong>Examples:</strong><pre>
 * Sizes.ZERO;
 * Sizes.DLUX9;
 * Sizes.dluX(42);
 * Sizes.pixel(99);
 * </pre>
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.21 $
 *
 * @see	Size
 * @see	Sizes
 */
public final class ConstantSize implements Size, Serializable {

    // Fields ***************************************************************

    private final double value;
    private final Unit    unit;


    // Instance Creation ****************************************************

    /**
     * Constructs a ConstantSize for the given size and unit.
     *
     * @param value     the size value interpreted in the given units
     * @param unit		the size's unit
     *
     * @since 1.1
     */
    public ConstantSize(int value, Unit unit) {
        this.value = value;
        this.unit  = unit;
    }


    /**
     * Constructs a ConstantSize for the given size and unit.
     *
     * @param value     the size value interpreted in the given units
     * @param unit      the size's unit
     *
     * @since 1.1
     */
    public ConstantSize(double value, Unit unit) {
        this.value = value;
        this.unit  = unit;
    }


    /**
     * Creates and returns a ConstantSize from the given encoded size
     * and unit description.
     *
     * @param encodedValueAndUnit  the size's value and unit as string,
     *                             trimmed and in lower case
     * @param horizontal		   true for horizontal, false for vertical
     * @return a constant size for the given encoding and unit description
     *
     * @throws IllegalArgumentException   if the unit requires integer
     *    but the value is not an integer
     */
    static ConstantSize decode(String encodedValueAndUnit) {
        String[] split = ConstantSize.splitValueAndUnit(encodedValueAndUnit);
        String encodedValue = split[0];
        String encodedUnit  = split[1];
        Unit unit = Unit.decode(encodedUnit);
        double value = Double.parseDouble(encodedValue);
        if (unit.requiresIntegers) {
            checkArgument(value == (int) value,
                    "%s value %s must be an integer.", unit, encodedValue);
        }
        return new ConstantSize(value, unit);
    }


    /**
     * Creates and returns a ConstantSize for the specified size value
     * in dialog units.
     *
     * @param value    size value in dialog units
     * @return the associated Size instance
     */
    static ConstantSize dlu(int value) {
        return new ConstantSize(value, Unit.DIALOG_UNITS);
    }
    
    
    /**
     * Creates and returns a ConstantSize for the specified size value
     * in horizontal dialog units.
     *
     * @param value	size value in horizontal dialog units
     * @return the associated Size instance
     * @deprecated Replaced by {@link #dlu(int)}
     */
    @Deprecated
    static ConstantSize dluX(int value) {
        return dlu(value);
    }


    /**
     * Creates and returns a ConstantSize for the specified size value
     * in vertical dialog units.
     *
     * @param value    size value in vertical dialog units
     * @return the associated Size instance
     * @deprecated Replaced by {@link #dlu(int)}
     */
    @Deprecated
    static ConstantSize dluY(int value) {
        return dlu(value);
    }


    // Accessors ************************************************************

    /**
     * Returns this size's value.
     *
     * @return the size value
     *
     * @since 1.1
     */
    public double getValue() {
        return value;
    }


    /**
     * Returns this size's unit.
     *
     * @return the size unit
     *
     * @since 1.1
     */
    public Unit getUnit() {
        return unit;
    }


    // Accessing the Value **************************************************

    /**
     * Converts the size if necessary and returns the value in pixels.
     *
     * @param component  the associated component
     * @return the size in pixels
     */
    public int getPixelSize(Component component, boolean horizontal) {
        switch (unit) {
        case PIXEL:
            return intValue();
        case DIALOG_UNITS:
            return horizontal
                 ? Sizes.dialogUnitXAsPixel(intValue(), component)
                 : Sizes.dialogUnitYAsPixel(intValue(), component);
        case POINT:
            return Sizes.pointAsPixel(intValue(), component);
        case INCH:
            return Sizes.inchAsPixel(value, component);
        case MILLIMETER:
            return Sizes.millimeterAsPixel(value, component);
        case CENTIMETER:
            return Sizes.centimeterAsPixel(value, component);
        default:
            throw new IllegalStateException("Invalid unit " + unit);
        }
    }


    // Implementing the Size Interface **************************************

    @Override
	public int maximumSize(Container container,
                    List<Component> components,
                    FormLayout.Measure minMeasure,
                    FormLayout.Measure prefMeasure,
                    FormLayout.Measure defaultMeasure,
                    boolean horizontal) {
        return getPixelSize(container, horizontal);
    }


    /**
     * Describes if this Size can be compressed, if container space gets scarce.
     * Used by the FormLayout size computations in {@code #compressedSizes}
     * to check whether a column or row can be compressed or not.<p>
     *
     * ConstantSizes are incompressible.
     *
     * @return {@code false}
     *
     * @since 1.1
     */
    @Override
	public boolean compressible() {
        return false;
    }


    // Overriding Object Behavior *******************************************

    /**
     * Indicates whether some other ConstantSize is "equal to" this one.
     *
     * @param o   the Object with which to compare
     * @return {@code true} if this object is the same as the obj
     *     argument; {@code false} otherwise.
     *
     * @see     java.lang.Object#hashCode()
     * @see     java.util.Hashtable
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstantSize)) {
            return false;
        }
        ConstantSize size = (ConstantSize) o;
        return this.value == size.value
             && this.unit  == size.unit;
    }


    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     * {@code java.util.Hashtable}.
     *
     * @return  a hash code value for this object.
     *
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.util.Hashtable
     */
    @Override
    public int hashCode() {
        return new Double(value).hashCode() + 37 * unit.hashCode();
    }


    /**
     * Returns a string representation of this size object.
     *
     * <strong>Note:</strong> This string representation may change
     * at any time. It is intended for debugging purposes. For parsing,
     * use {@link #encode()} instead.
     *
     * @return  a string representation of the constant size
     */
    @Override
    public String toString() {
        return value == intValue()
            ? Integer.toString(intValue()) + unit.abbreviation()
            : Double.toString(value) + unit.abbreviation();
    }


    /**
     * Returns a parseable string representation of this constant size.
     *
     * @return a String that can be parsed by the Forms parser
     *
     * @since 1.2
     */
    @Override
	public String encode() {
        return value == intValue()
            ? Integer.toString(intValue()) + unit.encode()
            : Double.toString(value) + unit.encode();
    }


    // Helper Code **********************************************************

    private int intValue() {
        return (int) Math.round(value);
    }


    /**
     * Splits a string that encodes size with unit into the size and unit
     * substrings. Returns an array of two strings.
     *
     * @param encodedValueAndUnit  a strings that represents a size with unit,
     *                             trimmed and in lower case
     * @return the first element is size, the second is unit
     */
    private static String[] splitValueAndUnit(String encodedValueAndUnit) {
        String[] result = new String[2];
        int len = encodedValueAndUnit.length();
        int firstLetterIndex = len;
        while (firstLetterIndex > 0
                && Character.isLetter(encodedValueAndUnit.charAt(firstLetterIndex - 1))) {
                firstLetterIndex--;
        }
        result[0] = encodedValueAndUnit.substring(0, firstLetterIndex);
        result[1] = encodedValueAndUnit.substring(firstLetterIndex);
        return result;
    }


    // Helper Class *********************************************************



}
