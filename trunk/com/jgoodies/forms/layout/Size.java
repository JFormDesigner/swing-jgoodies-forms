/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */

package com.jgoodies.forms.layout;

import java.awt.Container;
import java.util.List;

/**
 * An interface that describes sizes as used by the {@link FormLayout}:
 * component measuring sizes, constant sizes with value and unit, 
 * and bounded sizes that provide lower and upper bounds for a size.
 * <p>
 * You can find a motivation for the different <code>Size</code> types in
 * the Forms article that is part of the product documentation and that is
 * available online too, see
 * <a href="http://www.jgoodies.com/articles/forms.pdf" >
 * http://www.jgoodies.com/articles/forms.pdf</a>.
 *
 * @author Karsten Lentzsch
 * @see	Sizes
 * @see	ConstantSize
 */

public interface Size {
    
    /**
     * Computes and answers my maximum size applied to the given list of
     * components using the specified measures.
     * <p>
     * Invoked by {@link com.jgoodies.forms.layout.FormSpec} to determine 
     * the size of a column or row.
     * 
     * @param container       the layout container
     * @param components      the list of components used to compute the size
     * @param minMeasure      the measure that determines the minimum sizes
     * @param prefMeasure     the measure that determines the preferred sizes
     * @param defaultMeasure  the measure that determines the default sizes
     */
    int maximumSize(Container container,
                    List components, 
                    FormLayout.Measure minMeasure,
                    FormLayout.Measure prefMeasure,
                    FormLayout.Measure defaultMeasure);
    
}