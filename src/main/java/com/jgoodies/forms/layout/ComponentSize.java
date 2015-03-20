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

import java.awt.Component;
import java.awt.Container;
import java.util.List;
import java.util.Locale;

/**
 * A {@link Size} implementation for the component sizes:
 * <em>min, pref, default</em>.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.21 $
 *
 * @see Sizes
 * @see ConstantSize
 * 
 * @since 1.10
 */
public enum ComponentSize implements Size {

    /**
     * Use the maximum of all component minimum sizes as column or row size.
     */
    MINIMUM(false),

    /**
     * Use the maximum of all component preferred sizes as column or row size.
     */
    PREFERRED(false),

    /**
     * Use the maximum of all component sizes as column or row size;
     * measures preferred sizes when asked for the preferred size
     * and minimum sizes when asked for the minimum size.
     */
    DEFAULT(true);

    
    private final boolean compressible;
    
    private ComponentSize(boolean compressible) {
        this.compressible = compressible;
    }

    /**
     * Returns an instance of {@code ComponentSize} that corresponds
     * to the specified string.
     * @param str   		the encoded component size
     * @return the corresponding ComponentSize or null if none matches
     */
    static ComponentSize decode(String str) {
        switch(str) {
        case "pref":
        case "p":
            return PREFERRED;
        case "default":
        case "d":
            return DEFAULT;
        case "min":
        case "m":
            return MINIMUM;
        default:
            return null;
        }
    }

    @Override
	public int maximumSize(
        Container container,
        List<Component> components,
        FormLayout.Measure minMeasure,
        FormLayout.Measure prefMeasure,
        FormLayout.Measure defaultMeasure,
        boolean horizontal) {

        FormLayout.Measure measure = this == MINIMUM
                ? minMeasure
                : this == PREFERRED ? prefMeasure : defaultMeasure;
        int maximum = 0;
        for (Component c : components) {
            maximum = Math.max(maximum, measure.sizeOf(c));
        }
        return maximum;
    }

    /**
     * {@inheritDoc}
     *
     * The DEFAULT ComponentSize is compressible, MINIMUM and PREFERRED
     * are incompressible.
     *
     * @return {@code true} for the DEFAULT size,
     *      {@code false} otherwise
     *
     * @since 1.1
     */
    @Override
	public boolean compressible() {
        return compressible;
    }


    @Override
    public String toString() {
        return encode();
    }


    /**
     * Returns a parseable string representation of this ComponentSize.
     *
     * @return a String that can be parsed by the Forms parser
     *
     * @since 1.2
     */
    @Override
	public String encode() {
        return name().substring(0, 1).toLowerCase(Locale.ENGLISH);
    }


}
