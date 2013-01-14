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

package com.jgoodies.forms.workinprogress;

import javax.swing.JLabel;

import com.jgoodies.forms.layout.ColumnSpec;

/**
 * A typesafe enumeration for label layout styles that describes the alignment
 * and whether a colon should be added or not. This class provides constants
 * for different fixed styles, platform styles for Windows and Mac,
 * and a logical style that honors the platform.
 * <p>
 * These label styles can be used in the 
 * {@link com.jgoodies.forms.factories.FormFactory} to specify the label
 * alignment of a {@link com.jgoodies.forms.layout.FormLayout}. Instances of
 * {@code LabelStyle} are used in the 
 * {@link com.jgoodies.forms.workinprogress.DefaultFormBuilder} too.
 * <p>
 * This class is work in progress and in the early stages of development; 
 * the API may change without notice.
 *
 * @author Karsten Lentzsch
 * @see	com.jgoodies.forms.factories.FormFactory
 * @see	com.jgoodies.forms.layout.FormLayout
 * @see	com.jgoodies.forms.workinprogress.DefaultFormBuilder
 */

public final class LabelStyle {

    // Public Styles *********************************************************
    
    public static final LabelStyle LEFT_WITH_COLON  = new LabelStyle("Left", true);
    public static final LabelStyle LEFT_NO_COLON    = new LabelStyle("Left", false);
    public static final LabelStyle RIGHT_WITH_COLON = new LabelStyle("Right", true);
    public static final LabelStyle RIGHT_NO_COLON   = new LabelStyle("Right", false);
    public static final LabelStyle WINDOWS          = LEFT_WITH_COLON;
    public static final LabelStyle MAC              = RIGHT_WITH_COLON;
    public static final LabelStyle PLATFORM_DEFAULT = getPlatformDefaultStyle();
    
    
    private final String   alignment;
    private final boolean hasColon;


    // Instance Creation ****************************************************
    
    /**
     * Constructs a {@code LabelStyle} with the given alignment
     * and colon flag.
     * 
     * @param alignment  the label's alignment
     * @param hasColon   true indicates that a colon will be added 
     */
    private LabelStyle(String alignment, boolean hasColon) { 
        this.alignment = alignment;
        this.hasColon  = hasColon;
    }


    // Label and Text Creation **********************************************
    
    /**
     * Creates and returns a decorated {@code JLabel} that has a colon or
     * not, depending on the {@code colon} flag of this style.
     * 
     * @param text    the label's text
     * @return JLabel  the decorated label with or without extra colon
     */
    public JLabel createDecoratedLabel(String text) {
        return new JLabel(getDecoratedText(text));
    }

    /**
     * Returns the given text, unchanged if the colon property is false,
     * and extended by a colon if it's true.
     * 
     * @param text   the unchanged text
     * @param colon  true inidcates that a colon shall be added
     * @return the decorated text with or without extra colon
     */
    public String getDecoratedText(String text) {
        return hasColon ? text + ':' : text;
    }
    

    /**
     * Computes and returns the column default alignment that is associated
     * with this label style.
     * 
     * @return the label style's associated column alignment
     */    
    public ColumnSpec.DefaultAlignment getColumnAlignment() {
        return this == LEFT_NO_COLON || this == LEFT_WITH_COLON
            ? ColumnSpec.LEFT
            : ColumnSpec.RIGHT;
    }

    /**
     * Returns a string representation for this label style.
     * 
     * @return  a string representation for this label style.
     */
    public String toString() { 
        return alignment + (hasColon ? ":" : ""); 
    }
    
    // Helper Code **********************************************************
    
    /**
     * Computes and returns the platform's default style.
     * 
     * @return the platform's default {@code LabelStyle}
     */
    private static LabelStyle getPlatformDefaultStyle() {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows"))
            return WINDOWS;
        else if (osName.startsWith("Mac"))
            return MAC;
        else
            return RIGHT_NO_COLON;
    }

       
}
