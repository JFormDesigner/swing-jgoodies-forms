/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */

package com.jgoodies.forms.util;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Toolkit;

/**
 * An abstract implementation of the {@link UnitConverter} that minimizes 
 * the effort required to convert font-dependent sizes to pixels. 
 * 
 * @author Karsten Lentzsch
 * @see    DefaultUnitConverter
 * @see    com.jgoodies.forms.layout.Size
 * @see    com.jgoodies.forms.layout.Sizes
 */
abstract public class AbstractUnitConverter implements UnitConverter {

    private static final int DTP_RESOLUTION = 72;
    

    // Unit Converter Implementation *********************************************

    /**
     * Converts Inches and answers pixels using the specified resolution.
     * 
     * @param in         the Inches
     * @param component  the component that provides the graphics object
     * @return the given Inches as pixels
     */
    public int inchAsPixel(double in, Component component) {
        return inchAsPixel(in, getScreenResolution(component));
    }

    /**
     * Converts Millimeters and answers pixels using the resolution of the
     * given component's graphics object.
     * 
     * @param mm            Millimeters
     * @param component    the component that provides the graphics object
     * @return the given Millimeters as pixels
     */
    public int millimeterAsPixel(double mm, Component component) {
        return millimeterAsPixel(mm, getScreenResolution(component));
    }

    /**
     * Converts Centimeters and answers pixels using the resolution of the
     * given component's graphics object.
     * 
     * @param cm            Centimeters
     * @param component    the component that provides the graphics object
     * @return the given Centimeters as pixels
     */
    public int centimeterAsPixel(double cm, Component component) {
        return centimeterAsPixel(cm, getScreenResolution(component));
    }

    /**
     * Converts DTP Points and answers pixels using the resolution of the
     * given component's graphics object.
     * 
     * @param pt            DTP Points
     * @param component    the component that provides the graphics object
     * @return the given Points as pixels
     */
    public int pointAsPixel(int pt, Component component) {
        return pointAsPixel(pt, getScreenResolution(component));
    }
    
    /**
     * Converts horizontal dialog units and answers pixels. 
     * Honors the resolution, dialog font size, platform, and l&amp;f.
     * 
     * @param dluX  the horizontal dialog units
     * @param c 	a Component that provides the font and graphics
     * @return the given horizontal dialog units as pixels
     */
    public int dialogUnitXAsPixel(int dluX, Component c) {
        return dialogUnitXAsPixel(dluX, getDialogBaseUnitsX(c));
    }
    
    /**
     * Converts vertical dialog units and answers pixels. 
     * Honors the resolution, dialog font size, platform, and l&amp;f.
     * 
     * @param dluY  the vertical dialog units
     * @param c     a Component that provides the font and graphics
     * @return the given vertical dialog units as pixels
     */
    public int dialogUnitYAsPixel(int dluY, Component c) {
        return dialogUnitYAsPixel(dluY, getDialogBaseUnitsY(c));
    }
    
    
    // Abstract Behavior *****************************************************
    
    /**
     * Answers and lazily initializes the horizontal dialog base units. 
     * 
     * @param component   a Component that provides the font and graphics
     * @return the horizontal dialog base units
     */
    abstract protected double getDialogBaseUnitsX(Component component);
    
    /**
     * Answers and lazily initializes the vertical dialog base units. 
     * 
     * @param component   a Component that provides the font and graphics
     * @return the vertical dialog base units
     */
    abstract protected double getDialogBaseUnitsY(Component component);
    
    
    // Convenience Methods ***************************************************

    /**
     * Converts Inches and answers pixels using the specified resolution.
     * 
     * @param in    the Inches
     * @param dpi   the resolution
     * @return the given Inches as pixels
     */
    protected final int inchAsPixel(double in, int dpi) {
        return (int) Math.round(dpi * in);
    }

    /**
     * Converts Millimeters and answers pixels using the specified resolution.
     * 
     * @param mm    Millimeters
     * @param dpi   the resolution
     * @return the given Millimeters as pixels
     */
    protected final int millimeterAsPixel(double mm, int dpi) {
        return (int) Math.round(dpi * mm * 10 / 254);
    }

    /**
     * Converts Centimeters and answers pixels using the specified resolution.
     * 
     * @param cm    Centimeters
     * @param dpi   the resolution
     * @return the given Centimeters as pixels
     */
    protected final int centimeterAsPixel(double cm, int dpi) {
        return (int) Math.round(dpi * cm * 100 / 254);
    }

    /**
     * Converts DTP Points and answers pixels using the specified resolution.
     * 
     * @param pt    DTP Points
     * @param dpi   the resolution in dpi
     * @return the given Points as pixels
     */
    protected final int pointAsPixel(int pt, int dpi) {
        return Math.round(dpi * pt / DTP_RESOLUTION);
    }
    
    /**
     * Converts horizontal dialog units and answers pixels.
     * 
     * @param dluX                  the horizontal dialog units
     * @param dialogBaseUnitsX      the horizontal dialog base units
     * @return the given dialog base units as pixels
     */
    protected int dialogUnitXAsPixel(int dluX, double dialogBaseUnitsX) {
        return (int) Math.round(dluX * dialogBaseUnitsX / 4);
    }

    /**
     * Converts vertical dialog units and answers pixels.
     * 
     * @param dluY                  the vertical dialog units
     * @param dialogBaseUnitsY      the vertical dialog base units
     * @return the given dialog base units as pixels
     */
    protected int dialogUnitYAsPixel(int dluY, double dialogBaseUnitsY) {
        return (int) Math.round(dluY * dialogBaseUnitsY / 8);
    }
    
    
    // Helper Code ************************************************************
    
    protected double computeAverageCharWidth(FontMetrics metrics,
     String testString) {
         int width = metrics.stringWidth(testString);
         double average = (double) width / testString.length();
         //System.out.println("Average width of '" + testString + "'=" + average);
         return average;
     }
    
    /**
     * Returns the components screen resolution or the default screen
     * resolution if the component is null or has no toolkit assigned yet.
     * 
     * @param c the component to ask for a toolkit
     * @return the component's screen resolution
     */
    protected int getScreenResolution(Component c) {
        if (c == null)
            return getDefaultScreenResolution();
            
        Toolkit toolkit = c.getToolkit();
        return toolkit != null
            ? toolkit.getScreenResolution()
            : getDefaultScreenResolution();
    }
    
    
    private static int defaultScreenResolution = -1;
    
    /**
     * Computes and answers the default resolution.
     * 
     * @return the default screen resolution
     */
    protected int getDefaultScreenResolution() {
        if (defaultScreenResolution == -1) {
            defaultScreenResolution = 
                Toolkit.getDefaultToolkit().getScreenResolution();
        }
        return defaultScreenResolution;
    }


    
}