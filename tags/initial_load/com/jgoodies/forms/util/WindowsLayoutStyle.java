/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.util;

import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Size;
import com.jgoodies.forms.layout.Sizes;

/**
 * A {@link LayoutStyle} that aims to provide layout constants as defined by
 * Microsoft's <i>Design Specifications and Guidelines - Visual Design</i>.
 *
 * @author Karsten Lentzsch
 */

final class WindowsLayoutStyle extends LayoutStyle {
    
    static final WindowsLayoutStyle INSTANCE = new WindowsLayoutStyle();
    
    // Overrides default constructor; prevents instantiability.
    private WindowsLayoutStyle() {};
    

    // Component Sizes ******************************************************

    private static final Size         BUTTON_WIDTH              = Sizes.dluX(50);
    private static final Size         BUTTON_HEIGHT             = Sizes.dluY(14);
    

    // Gaps ******************************************************************

    private static final ConstantSize DIALOG_MARGIN_X           = Sizes.DLUX7;
    private static final ConstantSize DIALOG_MARGIN_Y           = Sizes.DLUY7;
    
    private static final ConstantSize LABEL_COMPONENT_PADX      = Sizes.DLUX3;
    private static final ConstantSize RELATED_COMPONENTS_PADX   = Sizes.DLUX4;
    private static final ConstantSize RELATED_COMPONENTS_PADY   = Sizes.DLUY4;
    private static final ConstantSize UNRELATED_COMPONENTS_PADX = Sizes.DLUX7;
    private static final ConstantSize UNRELATED_COMPONENTS_PADY = Sizes.DLUY7;
    private static final ConstantSize NARROW_LINE_PAD           = Sizes.DLUY2;
    private static final ConstantSize LINE_PAD                  = Sizes.DLUY3;
    private static final ConstantSize PARAGRAPH_PAD             = Sizes.DLUY9;


    // Layout Sizes *********************************************************

    /**
     * Answers the style's default button width.
     * 
     * @return the default button width
     */
    public Size getDefaultButtonWidth() {
        return BUTTON_WIDTH;
    }

    /**
     * Answers the style's default button height.
     * 
     * @return the default button height
     */
    public Size getDefaultButtonHeight() {
        return BUTTON_HEIGHT;
    }

    /**
     * Answers the style's horizontal dialog margin.
     * 
     * @return the horizontal dialog margin
     */
    public ConstantSize getDialogMarginX() {
        return DIALOG_MARGIN_X;
    }

    /**
     * Answers the style's vertical dialog margin.
     * 
     * @return the vertical dialog margin
     */
    public ConstantSize getDialogMarginY() {
        return DIALOG_MARGIN_Y;
    }

    /**
     * Answers a gap used to separate a label and associated control.
     * 
     * @return a gap between label and associated control
     */
    public ConstantSize getLabelComponentPadX() {
        return LABEL_COMPONENT_PADX;
    }

    /**
     * Answers a horizontal gap used to separate related controls.
     * 
     * @return a horizontal gap between related controls
     */
    public ConstantSize getRelatedComponentsPadX() {
        return RELATED_COMPONENTS_PADX;
    }

    /**
     * Answers a vertical gap used to separate related controls.
     * 
     * @return a vertical gap between related controls
     */
    public ConstantSize getRelatedComponentsPadY() {
        return RELATED_COMPONENTS_PADY;
    }

    /**
     * Answers a horizontal gap used to separate unrelated controls.
     * 
     * @return a horizontal gap between unrelated controls
     */
    public ConstantSize getUnrelatedComponentsPadX() {
        return UNRELATED_COMPONENTS_PADX;
    }

    /**
     * Answers a vertical gap used to separate unrelated controls.
     * 
     * @return a vertical gap between unrelated controls
     */
    public ConstantSize getUnrelatedComponentsPadY() {
        return UNRELATED_COMPONENTS_PADY;
    }

    /**
     * Answers a narrow vertical pad used to separate lines.
     * 
     * @return a vertical pad used to separate lines
     */
    public ConstantSize getLinePad() {
        return LINE_PAD;
    }

    /**
     * Answers a narrow vertical pad used to separate lines.
     * 
     * @return a narrow vertical pad used to separate lines
     */
    public ConstantSize getNarrowLinePad() {
        return NARROW_LINE_PAD;
    }

    /**
     * Answers a pad used to paragraphs.
     * 
     * @return a vertical pad used to separate paragraphs
     */
    public ConstantSize getParagraphPad() {
        return PARAGRAPH_PAD;
    }

}