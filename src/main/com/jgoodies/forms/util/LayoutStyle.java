/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.forms.util;

import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Size;

/**
 * An abstract class that describes a layout and design style guide.
 * It provides constants used to layout panels consistently.
 * <p>
 * This class is work in progress and the API may change without notice.
 * Therefore it is recommended to not write custom subclasses 
 * for production code.
 * A future version of this class will likely collaborate with a class
 * <code>LogicalSize</code> or <code>StyledSize</code>.
 *
 * @author Karsten Lentzsch
 */

public abstract class LayoutStyle {
    
    /**
     * Holds the current layout style.
     */
    private static LayoutStyle current = WindowsLayoutStyle.INSTANCE;
    
    
    // Accessing the current style ******************************************
    
    /**
     * Returns the current <code>LayoutStyle</code>.
     * 
     * @return the current <code>LayoutStyle</code>
     */
    public static LayoutStyle getCurrent() {
        return current;
    }
    
    /**
     * Set a new <code>LayoutStyle</code>
     */
    public static void setCurrent(LayoutStyle newLayoutStyle) {
        current = newLayoutStyle;
    }
    
    
    // Layout Sizes *********************************************************

    /**
     * Answers the style's default button width.
     * 
     * @return the default button width
     */
    abstract public Size getDefaultButtonWidth();

    /**
     * Answers the style's default button height.
     * 
     * @return the default button height
     */
    abstract public Size getDefaultButtonHeight();

    /**
     * Answers the style's horizontal dialog margin.
     * 
     * @return the horizontal dialog margin
     */
    abstract public ConstantSize getDialogMarginX();

    /**
     * Answers the style's vertical dialog margin.
     * 
     * @return the vertical dialog margin
     */
    abstract public ConstantSize getDialogMarginY();

    /**
     * Answers a gap used to separate a label and associated control.
     * 
     * @return a gap between label and associated control
     */
    abstract public ConstantSize getLabelComponentPadX();

    /**
     * Answers a horizontal gap used to separate related controls.
     * 
     * @return a horizontal gap between related controls
     */
    abstract public ConstantSize getRelatedComponentsPadX();

    /**
     * Answers a vertical gap used to separate related controls.
     * 
     * @return a vertical gap between related controls
     */
    abstract public ConstantSize getRelatedComponentsPadY();

    /**
     * Answers a horizontal gap used to separate unrelated controls.
     * 
     * @return a horizontal gap between unrelated controls
     */
    abstract public ConstantSize getUnrelatedComponentsPadX();

    /**
     * Answers a vertical gap used to separate unrelated controls.
     * 
     * @return a vertical gap between unrelated controls
     */
    abstract public ConstantSize getUnrelatedComponentsPadY();

    /**
     * Answers a narrow vertical pad used to separate lines.
     * 
     * @return a vertical pad used to separate lines
     */
    abstract public ConstantSize getLinePad();

    /**
     * Answers a narrow vertical pad used to separate lines.
     * 
     * @return a narrow vertical pad used to separate lines
     */
    abstract public ConstantSize getNarrowLinePad();

    /**
     * Answers a pad used to paragraphs.
     * 
     * @return a vertical pad used to separate paragraphs
     */
    abstract public ConstantSize getParagraphPad();

}