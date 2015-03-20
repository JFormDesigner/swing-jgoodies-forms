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


/**
 * An enumeration for units as used in instances of {@link ConstantSize}.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.21 $
 *
 * @see ConstantSize
 * 
 * @since 1.10
 */
public enum Unit {


    PIXEL       ("px",  true),
    POINT       ("pt",  true),
    DIALOG_UNITS("dlu", true),
    MILLIMETER  ("mm",  false),
    CENTIMETER  ("cm",  false),
    INCH        ("in",  false);


    private final String abbreviation;
            final boolean requiresIntegers;

    private Unit(String abbreviation, boolean requiresIntegers) {
        this.abbreviation = abbreviation;
        this.requiresIntegers = requiresIntegers;
    }


    /**
     * Returns a Unit that corresponds to the specified string.
     *
     * @param name   the encoded unit, trimmed and in lower case
     * @return the corresponding Unit
     * @throws IllegalArgumentException if no Unit exists for the string
     */
    static Unit decode(String name) {
        if (name.length() == 0) {
            return Sizes.getDefaultUnit();
        }
        switch (name) {
        case "px":
            return PIXEL;
        case "dlu":
            return DIALOG_UNITS;
        case "pt":
            return POINT;
        case "in":
            return INCH;
        case "mm":
            return MILLIMETER;
        case "cm":
            return CENTIMETER;
        default:
            throw new IllegalArgumentException(
                "Invalid unit name '" + name + "'. Must be one of: " +
                "px, dlu, pt, mm, cm, in");
        }
    }


    boolean requiresIntegers() {
        return requiresIntegers;
    }
    
    
    /**
     * Returns a parseable string representation of this unit.
     *
     * @return a String that can be parsed by the Forms parser
     *
     * @since 1.2
     */
    public String encode() {
        return abbreviation;
    }


    public String abbreviation() {
        return abbreviation;
    }


}
