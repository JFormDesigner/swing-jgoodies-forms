/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */

package com.jgoodies.forms.util;

import java.awt.Component;

/**
 * An interface that describes how to convert general sizes to pixel sizes.
 * For example, <i>dialog units</i> require a conversion that honors 
 * the font and resolution. The {@link com.jgoodies.forms.layout.Sizes} class 
 * delegates all size conversions to an implementation of this interface. 
 *
 * @author Karsten Lentzsch
 * @see    com.jgoodies.forms.layout.Sizes
 * @see    com.jgoodies.forms.layout.ConstantSize
 * @see    AbstractUnitConverter
 * @see    DefaultUnitConverter
 */
public interface UnitConverter {
    
    
    /**
     * Converts Inches and answers pixels using the specified resolution.
     * 
     * @param in         the Inches
     * @param component  the component that provides the graphics object
     * @return the given Inches as pixels
     */
    public int inchAsPixel(double in, Component component);
    

    /**
     * Converts Millimeters and answers pixels using the resolution of the
     * given component's graphics object.
     * 
     * @param mm         Millimeters
     * @param component  the component that provides the graphics object
     * @return the given Millimeters as pixels
     */
    public int millimeterAsPixel(double mm, Component component);


    /**
     * Converts Centimeters and answers pixels using the resolution of the
     * given component's graphics object.
     * 
     * @param cm         Centimeters
     * @param component  the component that provides the graphics object
     * @return the given Centimeters as pixels
     */
    public int centimeterAsPixel(double cm, Component component);
    

    /**
     * Converts DTP Points and answers pixels using the resolution of the
     * given component's graphics object.
     * 
     * @param pt          DTP Points
     * @param component   the component that provides the graphics object
     * @return the given Points as pixels
     */
    public int pointAsPixel(int pt, Component component);
    
    
    /**
     * Converts horizontal dialog units and answers pixels. 
     * Honors the resolution, dialog font size, platform and look&amp;feel.
     * 
     * @param dluX       the horizontal dialog units
     * @param component  a component that provides the font and graphics
     * @return the given horizontal dialog units as pixels
     */
    public int dialogUnitXAsPixel(int dluX, Component component);
            
                    
    /**
     * Converts vertical dialog units and answers pixels. 
     * Honors the resolution, dialog font size, platform and look&amp;feel.
     * 
     * @param dluY       the vertical dialog units
     * @param component  a component that provides the font and graphics
     * @return the given vertical dialog units as pixels
     */
    public int dialogUnitYAsPixel(int dluY, Component component);
    
    
}