/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch. 
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.layout;

import java.awt.Container;
import java.util.List;

/**
 * Describes sizes as used by the {@link com.jgoodies.forms.layout.FormLayout}
 * that provide lower and upper bounds.
 *
 * @author Karsten Lentzsch
 * @see	Sizes
 * @see	ConstantSize
 * @see	Sizes.ComponentSize
 */

final class BoundedSize implements Size {
    
    /**
     * Holds the base size.
     */ 
    private final Size basis; 
    
    /**
     * Holds an optional lower bound.
     */
    private Size lowerBound;     

    /**
     * Holds an optional upper bound.
     */
    private Size upperBound;     
    
    
    // Instance Creation ****************************************************
    
    /**
     * Constructs a <code>BoundedSize</code> for the given basis using the
     * specified lower and upper bounds.
     * 
     * @param basis  the base size
     * @param lowerBound  the lower bound size
     * @param upperBound  the upper bound size
     * @throws NullPointerException if the basis is null
     */
    BoundedSize(Size basis, Size lowerBound, Size upperBound) {
        if (basis == null) 
            throw new NullPointerException("The basis of a bounded size must not be null.");
        this.basis = basis;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    /**
     * Constructs a <code>BoundedSize</code> for the given basis having
     * no lower and upper bounds.
     * 
     * @param basis  the base size
     * @throws NullPointerException if the basis is null
     */
    BoundedSize(Size basis) {
        this(basis, null, null);
    }
    
    
    // Accessors ************************************************************
    
    /**
     * Sets the lower bound.
     * 
     * @param lowerBound  the new lower bound
     */
    void setLowerBound(Size lowerBound) {
        this.lowerBound = lowerBound;
    }
    
    /**
     * Sets the upper bound.
     * 
     * @param upperBound  the new upper bound
     */
    void setUpperBound(Size upperBound) {
        this.upperBound = upperBound;
    }
    
    // Implementation of the Size Interface *********************************
    
    /**
     * Returns this size as pixel size. Neither requires the component
     * list nor the specified measures.
     * <p> 
     * Invoked by <code>FormSpec</code> to determine the size of a column or
     * row.
     * 
     * @see FormSpec#maximumSize
     */
    public int maximumSize(Container container,
                    List components, 
                    FormLayout.Measure minMeasure,
                    FormLayout.Measure prefMeasure,
                    FormLayout.Measure defaultMeasure) {
        int size = basis.maximumSize(container,
                                     components,
                                     minMeasure,
                                     prefMeasure,
                                     defaultMeasure);
        if (lowerBound != null) {
            size = Math.max(size, lowerBound.maximumSize(
                                     container,
                                     components,
                                     minMeasure,
                                     prefMeasure,
                                     defaultMeasure));
        }
        if (upperBound != null) {
            size = Math.max(size, upperBound.maximumSize(
                                     container,
                                     components,
                                     minMeasure,
                                     prefMeasure,
                                     defaultMeasure));
        }
        return size;
    }


    // Overriding Object Behavior *******************************************
    
    /**
     * Indicates whether some other BoundedSize is "equal to" this one.
     *
     * @param size   the BoundedSize with which to compare
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise.
     * @see     java.lang.Object#hashCode()
     * @see     java.util.Hashtable
     */
    public boolean equals(Object o) {
        if (!(o instanceof BoundedSize))
            return false;
        BoundedSize size = (BoundedSize) o;
        return basis.equals(size.basis)
             && (   (lowerBound == null && size.lowerBound == null)
                 || (lowerBound != null && lowerBound.equals(size.lowerBound)))
             && (   (upperBound == null && size.upperBound == null)
                 || (upperBound != null && upperBound.equals(size.upperBound)));
    }
    
    /**
     * Returns a hash code value for the object. This method is 
     * supported for the benefit of hashtables such as those provided by 
     * <code>java.util.Hashtable</code>. 
     * 
     * @return  a hash code value for this object.
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.util.Hashtable
     */
    public int hashCode() {
        int hashValue = basis.hashCode();
        if (lowerBound != null) {
            hashValue = hashValue * 37 + lowerBound.hashCode();
        }
        if (upperBound != null) {
            hashValue = hashValue * 37 + upperBound.hashCode();
        }
        return hashValue;
    }
    
    /**
     * Returns a string representation of this size object.
     *
     * @return  a string representation of the constant size
     */
    public String toString() {
        if (lowerBound != null) {
            if (upperBound == null) {
                return "max(" + basis + ';' + lowerBound + ')';
            } else {
                return "max(" + basis + ';'
                      + "min(" + lowerBound + ';' + upperBound + "))";
            }
        } else if (upperBound != null) {
            return "min(" + basis + ';' + upperBound + ')';
        } else {
            return "bounded(" +  basis + ')';
        }
    }

    
}