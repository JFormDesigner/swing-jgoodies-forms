/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.extras;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A general purpose panel builder that uses the {@link FormLayout} 
 * to layout <code>JPanel</code>s. In addition to its superclass
 * {@link PanelBuilder} this class provides convenience behavior to map 
 * resource keys to their associated internationalized (i15d) strings
 * when adding labels, titles and titled separators.
 * <p>
 * This class is not yet part of the binary Forms library; 
 * it comes with the Forms distributions as an extra.
 * <b>The API is work in progress and may change without notice.</b>
 * If you want to use this class, you may consider copying it into your codebase.
 *
 * @author	Karsten Lentzsch
 * @see	ResourceBundle
 */
public class I15dPanelBuilder extends PanelBuilder {
    
    /** 
     * Holds the <code>ResourceBundle</code> used to lookup internationalized
     * (i15d) String resources.
     */
    private final ResourceBundle bundle;
    

    // Instance Creation ****************************************************

    /**
     * Constructs an instance of <code>I15dPanelBuilder</code> for the given
     * panel and layout.
     * 
     * @param panel   the layout container
     * @param layout  the <code>FormLayout</code> used to layout the container
     * @param bundle  the <code>ResourceBundle</code> used to lookup i15d
     * strings
     */
    public I15dPanelBuilder(JPanel panel, FormLayout layout, ResourceBundle bundle){        
        super(panel, layout);
        this.bundle = bundle;
    }

    /**
     * Constructs an instance of <code>I15dPanelBuilder</code> for the given
     * layout. Uses an instance of <code>JPanel</code> as layout container.
     * 
     * @param layout    the form layout used to layout the container
     * @param bundle    the resource bundle used to lookup i15d strings
     */
    public I15dPanelBuilder(FormLayout layout, ResourceBundle bundle){        
        this(new JPanel(), layout, bundle);
    }


    // Adding Labels and Separators *****************************************
    
    /**
     * Adds an internationalized (i15d) textual label to the form using the
     * specified constraints.
     * 
     * @param resourceKey	the resource key for the label's text 
     * @param constraints	the label's cell constraints
     * @return the added label
     */
    public final JLabel addI15dLabel(String resourceKey, CellConstraints constraints) {
        return addLabel(getI15dString(resourceKey), constraints);
    }

    /**
     * Adds an internationalized (i15d) textual label to the form using the
     * specified constraints.
     * 
     * @param resourceKey         the resource key for the label's text
     * @param encodedConstraints  a string representation for the constraints
     * @return the added label
     */
    public final JLabel addI15dLabel(String resourceKey, String encodedConstraints) {
        return addI15dLabel(resourceKey, new CellConstraints(encodedConstraints));
    }
    
    /**
     * Adds an internationalized (i15d) titled separator to the form using the
     * specified constraints.
     * 
     * @param resourceKey  the resource key for the separator title
     * @param constraints  the separator's cell constraints
     * @return the added titled separator
     */
    public final JComponent addI15dSeparator(String resourceKey, CellConstraints constraints) {
        return addSeparator(getI15dString(resourceKey), constraints);
    }
    
    /**
     * Adds an internationalized (i15d)  titled separator to the form using
     * the specified constraints.
     * 
     * @param resourceKey         the resource key for the separator titel
     * @param encodedConstraints  a string representation for the constraints
     * @return the added titled separator
     */
    public final JComponent addI15dSeparator(String resourceKey, String encodedConstraints) {
        return addI15dSeparator(resourceKey, new CellConstraints(encodedConstraints));
    }
     
    /**
     * Adds a title to the form using the specified constraints.
     * 
     * @param resourceKey  the resource key for  the separator title
     * @param constraints  the separator's cell constraints
     * @return the added title label
     */
    public final JLabel addI15dTitle(String resourceKey, CellConstraints constraints) {
        return addTitle(getI15dString(resourceKey), constraints);
    }
    
    /**
     * Adds a title to the form using the specified constraints.
     * 
     * @param resourceKey         the resource key for the separator titel
     * @param encodedConstraints  a string representation for the constraints
     * @return the added title label
     */
    public final JLabel add15dTitle(String resourceKey, String encodedConstraints) {
        return addI15dTitle(resourceKey, new CellConstraints(encodedConstraints));
    }
    
    
    // Helper Code **********************************************************
    
    /**
     * Looks up and answers the internationalized (i15d) string for the given
     * resource key from the <code>ResourceBundle</code>.
     * 
     * @param resourceKey  the key to look for in the resource bundle
     * @return the associated internationalized string, or the resource key
     * itself in case of a missing resource 
     */
    protected String getI15dString(String resourceKey) {
        try {
            return bundle.getString(resourceKey);
        } catch (MissingResourceException mre) {
            return resourceKey;
        }
    }
    
     

}
