/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */

package com.jgoodies.forms.layout;

import java.awt.Component;
import java.awt.Container;
import java.util.Iterator;
import java.util.List;

import com.jgoodies.forms.util.*;


/**
 * Consists only of static methods that create and convert sizes 
 * as required by the {@link FormLayout}. The conversion of sizes
 * that are not based on pixel is delegated to an implementation 
 * of {@link UnitConverter}. The conversion methods require the 
 * layout container as parameter to read its current font and resolution.
 *
 * @author  Karsten Lentzsch
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
    public static final ConstantSize DLUX7  = dluX( 7);
    public static final ConstantSize DLUX9  = dluX( 9);
    public static final ConstantSize DLUX11 = dluX(11);
    public static final ConstantSize DLUX14 = dluX(14);

    public static final ConstantSize DLUY1  = dluY( 1);
    public static final ConstantSize DLUY2  = dluY( 2);
    public static final ConstantSize DLUY3  = dluY( 3);
    public static final ConstantSize DLUY4  = dluY( 4);
    public static final ConstantSize DLUY6  = dluY( 6);
    public static final ConstantSize DLUY7  = dluY( 7);
    public static final ConstantSize DLUY9  = dluY( 9);
    public static final ConstantSize DLUY11 = dluY(11);
    public static final ConstantSize DLUY14 = dluY(14);
    

    // Static Component Sizes ***********************************************
     
    /**
     * Use the maximum of all component minimum sizes as column or row size.
     */
    public static final ComponentSize MINIMUM  = new ComponentSize("minimum"); 

    /**
     * Use the maximum of all component preferred sizes as column or row size.
     */
    public static final ComponentSize PREFERRED = new ComponentSize("preferred");

    /**
     * Use the maximum of all component sizes as column or row size;
     * measures preferred sizes when asked for the preferred size
     * and minimum sizes when asked for the minimum size.
     */
    public static final ComponentSize DEFAULT = new ComponentSize("default");


    // Singleton State *******************************************************
     
    /**
     * Holds the current converter that maps non-pixel sizes to pixels.
     */
    private static UnitConverter unitConverter;
    
    
    // Instance Creation ******************************************************
     
    // Override default constructor; prevents instantiability
    private Sizes() {}


    // Creation of Size Instances *********************************************
    
    /**
     * Creates and answers an instance of <code>ConstantSize</code> from the
     * given encoded size and unit description.
     * 
     * @param encodedValueAndUnit  value and unit in string representation
     * @param horizontal			true for horizontal, false for vertical
     * @return a <code>ConstantSize</code> for the given value and unit
     */
    public static ConstantSize constant(String encodedValueAndUnit, 
                                         boolean horizontal) {
        return ConstantSize.valueOf(encodedValueAndUnit, horizontal);
    }
        
    /**
     * Answers an instance of <code>Size</code> for the specified value
     * in horizontal dialog units.
     * 
     * @param value	size value in horizontal dialog units	
     * @return the associated <code>ConstantSize</code>
     */
    public static ConstantSize dluX(int value) {
        return ConstantSize.dluX(value);
    }
    
    /**
     * Answers an instance of <code>Size</code> for the specified value
     * in vertical dialog units.
     * 
     * @param value 	size value in vertical dialog units   
     * @return the associated <code>ConstantSize</code>
     */
    public static ConstantSize dluY(int value) {
        return ConstantSize.dluY(value);
    }
    
    /**
     * Answers an instance of <code>Size</code> for the specified pixel value.
     * 
     * @param value  value in pixel
     * @return the associated <code>ConstantSize</code>
     */
    public static ConstantSize pixel(int value) {
        return new ConstantSize(value, ConstantSize.PIXEL);
    }
    
    /**
     * Creates and answers a <code>BoundedSize</code> for the given basis
     * using the specified lower and upper bounds.
     * 
     * @param basis  		the base size
     * @param lowerBound  	the lower bound size
     * @param upperBound  	the upper bound size
     * @return a <code>BoundedSize</code> for the given basis and bounds
     * @throws NullPointerException if basis is null
     */
    public static Size bounded(Size basis, Size lowerBound, Size upperBound) {
        return new BoundedSize(basis, lowerBound, upperBound);
    }
    

    // Unit Conversion ******************************************************
    
    /**
     * Converts Inches and answers pixels using the specified resolution.
     * 
     * @param in           the Inches
     * @param component    the component that provides the graphics object
     * @return the given Inches as pixels
     */
    public static int inchAsPixel(double in, Component component) {
        return getUnitConverter().inchAsPixel(in, component);
    }

    /**
     * Converts Millimeters and answers pixels using the resolution of the
     * given component's graphics object.
     * 
     * @param mm	        Millimeters
     * @param component    the component that provides the graphics object
     * @return the given Millimeters as pixels
     */
    public static int millimeterAsPixel(double mm, Component component) {
        return getUnitConverter().millimeterAsPixel(mm, component);
    }

    /**
     * Converts Centimeters and answers pixels using the resolution of the
     * given component's graphics object.
     * 
     * @param cm	        Centimeters
     * @param component    the component that provides the graphics object
     * @return the given Centimeters as pixels
     */
    public static int centimeterAsPixel(double cm, Component component) {
        return getUnitConverter().centimeterAsPixel(cm, component);
    }

    /**
     * Converts DTP Points and answers pixels using the resolution of the
     * given component's graphics object.
     * 
     * @param pt	        DTP Points
     * @param component    the component that provides the graphics object
     * @return the given Points as pixels
     */
    public static int pointAsPixel(int pt, Component component) {
        return getUnitConverter().pointAsPixel(pt, component);
    }
    
    /**
     * Converts horizontal dialog units and answers pixels. 
     * Honors the resolution, dialog font size, platform, and l&amp;f.
     * 
     * @param dluX         the horizontal dialog units
     * @param component    the component that provides the graphics object
     * @return the given horizontal dialog units as pixels
     */
    public static int dialogUnitXAsPixel(int dluX, Component component) {
        return getUnitConverter().dialogUnitXAsPixel(dluX, component);
    }

    /**
     * Converts vertical dialog units and answers pixels. 
     * Honors the resolution, dialog font size, platform, and l&amp;f.
     * 
     * @param dluY         the vertical dialog units
     * @param component    the component that provides the graphics object
     * @return the given vertical dialog units as pixels
     */
    public static int dialogUnitYAsPixel(int dluY, Component component) {
        return getUnitConverter().dialogUnitYAsPixel(dluY, component);
    }
    
    
    // Accessing the Unit Converter *******************************************
    
    /**
     * Returns the current {@link UnitConverter}. If it has not been initialized 
     * before it will get an instance of {@link DefaultUnitConverter}.
     */
    public static UnitConverter getUnitConverter() {
        if (unitConverter == null) {
            unitConverter = DefaultUnitConverter.getInstance();
        }
        return unitConverter;
    }
    
    /**
     * Sets a new {@link UnitConverter} that will be used to convert
     * font-dependent sizes to pixel sizes.
     * 
     * @param newUnitConverter  the unit converter to be set
     */
    public static void setUnitConverter(UnitConverter newUnitConverter) {
        unitConverter = newUnitConverter;
    }


    // Helper Class *********************************************************
    
    /**
     * A {@link Size} interface implementation for the component sizes.
     */
    static final class ComponentSize implements Size {
        
        private final String name;

        private ComponentSize(String name) { 
            this.name = name;
        }

        /**
         * Answers an instance of <code>ComponentSize</code> that corresponds
         * to the specified string.
         * @param str   		the encoded component size
         * @return the corresponding ComponentSize or null if none matches
         */
        static ComponentSize valueOf(String str) {
            if (str.equals("m") || str.equals("min"))
                return MINIMUM;
            if (str.equals("p") || str.equals("pref"))
                return PREFERRED;
            if (str.equals("d") || str.equals("default"))
                return DEFAULT;
            else
                return null;
        }
        
        /**
         * Computes the maximum size for the given list of components, using
         * this form spec and the specified measure. 
         * <p>
         * Invoked by FormLayout to determine the size of one of my elements
         * 
         * @return the maximum size for the given list of components
         */
        public int maximumSize(
            Container container,
            List components,
            FormLayout.Measure minMeasure,
            FormLayout.Measure prefMeasure,
            FormLayout.Measure defaultMeasure) {
                
            FormLayout.Measure measure = this == MINIMUM
                    ? minMeasure
                    : (this == PREFERRED ? prefMeasure : defaultMeasure);
            int maximum = 0;
            for (Iterator i = components.iterator(); i.hasNext();) {
                Component c = (Component) i.next();
                maximum = Math.max(maximum, measure.sizeOf(c));
            }
            return maximum;
        }

        public String toString()  { return name.substring(0, 1); }

    }
    
    
}