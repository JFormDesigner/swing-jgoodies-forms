/*
 * Copyright (c) 2002-2012 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.forms.builder;

import static com.jgoodies.common.base.Preconditions.checkState;

import java.awt.Color;
import java.awt.Component;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.internal.StringLocalizer;
import com.jgoodies.common.swing.MnemonicUtils;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A general purpose builder class that uses the FormLayout to lay out JPanels. 
 * In addition to its superclass {@link PanelBuilder} this class provides 
 * convenience behavior to map
 * resource keys to their associated internationalized (i15d) strings
 * when adding labels, titles and titled separators.<p>
 *
 * The localized texts used in methods {@code #addI15d*} can be
 * <em>marked texts</em>, i.e. strings with an optional mnemonic marker.
 * See the {@link MnemonicUtils} class comment for details.<p>
 *
 * For debugging purposes you can automatically set a tooltip for the
 * created labels that show its resource key. In case of an inproper
 * resource localization, the label will show the wrong text, and the tooltip
 * will help you identify the resource key with the broken localization.
 * This feature can be enabled by calling {@code setDebugToolTipsEnabled}.
 * If you want to enable it in a deployed application, you can set the system
 * parameter {@code I15dPanelBuilder.debugToolTipsEnabled} to "true".<p>
 *
 * Subclasses must implement the conversion from resource key
 * to the localized string in {@code #getI15dString(String)}.
 * For example class I15dPanelBuilder gets a ResourceBundle during
 * construction, and requests strings from that bundle.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.12 $
 * 
 * @see ResourceBundle
 *
 * @since 1.1
 */
public class I15dPanelBuilder extends PanelBuilder {

    private static final String DEBUG_TOOL_TIPS_ENABLED_KEY =
            "I15dPanelBuilder.debugToolTipsEnabled";

    private static boolean debugToolTipsEnabled =
            getDebugToolTipSystemProperty();


    /**
     * Holds the ResourceBundle used to look up internationalized
     * (i15d) String resources.
     */
    private final StringLocalizer localizer;


    // Instance Creation ****************************************************

    /**
     * Constructs an I15dPanelBuilder for the given layout and resource bundle.
     * Uses an instance of JPanel as layout container.
     *
     * @param layout    the FormLayout used to layout the container
     * @param bundle    the ResourceBundle used to look up i15d strings
     *
     * @throws NullPointerException if {@code layout} or {@code bundle},
     *    is {@code null}
     */
    public I15dPanelBuilder(FormLayout layout, ResourceBundle bundle){
        this(layout, bundle, new JPanel(null));
    }


    /**
     * Constructs an I15dPanelBuilder for the given FormLayout, resource bundle,
     * and layout container.
     *
     * @param layout    the FormLayout used to layout the container
     * @param bundle    the ResourceBundle used to lookup i15d strings
     * @param container the layout container
     *
     * @throws NullPointerException if {@code layout}, {@code bundle},
     *    or {@code container} is {@code null}
     */
    public I15dPanelBuilder(FormLayout layout, ResourceBundle bundle, JPanel container){
        this(layout, new ResourceBundleLocalizer(bundle), container);
    }


    /**
     * Constructs an I15dPanelBuilder for the given FormLayout, resource bundle,
     * and layout container.
     *
     * @param layout     the FormLayout used to layout the container
     * @param localizer  used to lookup i15d strings
     *
     * @throws NullPointerException if {@code layout} is {@code null}
     */
    public I15dPanelBuilder(FormLayout layout, StringLocalizer localizer){
        this(layout, localizer, new JPanel(null));
    }


    /**
     * Constructs an I15dPanelBuilder for the given FormLayout, resource bundle,
     * and layout container.
     *
     * @param layout     the FormLayout used to layout the container
     * @param localizer  used to lookup i15d strings
     * @param container  the layout container
     *
     * @throws NullPointerException if {@code layout} or {@code container} is {@code null}
     */
    public I15dPanelBuilder(FormLayout layout, StringLocalizer localizer, JPanel container){
        super(layout, container);
        this.localizer = localizer;
    }


    // Debug ToolTip Settings *************************************************

    /**
     * Returns whether the debug tool tips are enabled or not.
     *
     * @return true if debug tool tips are enabled, false if disabled
     */
    public static boolean isDebugToolTipsEnabled() {
        return debugToolTipsEnabled;
    }


    /**
     * Enables or disables the debug tool tips.
     *
     * @param b true to enable, false to disable
     */
    public static void setDebugToolTipsEnabled(boolean b) {
        debugToolTipsEnabled = b;
    }


    // Frequently Used Panel Properties ***************************************

    @Override
    public I15dPanelBuilder background(Color background) {
        super.background(background);
        return this;
    }


    @Override
    public I15dPanelBuilder border(Border border) {
        super.border(border);
        return this;
    }
    
    
    @Override
    public I15dPanelBuilder border(String emptyBorderSpec) {
    	super.border(emptyBorderSpec);
    	return this;
    }


    @Override
    public I15dPanelBuilder opaque(boolean b) {
        super.opaque(b);
        return this;
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
        JLabel label = addLabel(getI15dString(resourceKey), constraints);
        if (isDebugToolTipsEnabled()) {
            label.setToolTipText(resourceKey);
        }
        return label;
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
     * Adds an internationalized (i15d) label and component to the panel using
     * the given cell constraints. Sets the label as <i>the</i> component label
     * using {@link JLabel#setLabelFor(java.awt.Component)}.<p>
     *
     * <strong>Note:</strong> The {@link CellConstraints} objects for the label
     * and the component must be different. Cell constraints are implicitly
     * cloned by the {@code FormLayout} when added to the container.
     * However, in this case you may be tempted to reuse a
     * {@code CellConstraints} object in the same way as with many other
     * builder methods that require a single {@code CellConstraints}
     * parameter.
     * The pitfall is that the methods {@code CellConstraints.xy**(...)}
     * just set the coordinates but do <em>not</em> create a new instance.
     * And so the second invocation of {@code xy***(...)} overrides
     * the settings performed in the first invocation before the object
     * is cloned by the {@code FormLayout}.<p>
     *
     * <strong>Wrong:</strong><pre>
     * builder.addI15dLabel("name.key",
     *             CC.xy(1, 7),         // will be modified by the code below
     *             nameField,
     *             CC.xy(3, 7)          // sets the single instance to (3, 7)
     *            );
     * </pre>
     * <strong>Correct:</strong><pre>
     * builder.addI15dLabel("name.key",
     *             CC.xy(1, 7).clone(), // cloned before the next modification
     *             nameField,
     *             CC.xy(3, 7)          // sets this instance to (3, 7)
     *            );
     * </pre>
     *
     * @param resourceKey           the resource key for the label
     * @param labelConstraints      the label's cell constraints
     * @param component             the component to add
     * @param componentConstraints  the component's cell constraints
     * @return the added label
     * @throws IllegalArgumentException if the same cell constraints instance
     *     is used for the label and the component
     * @see JLabel#setLabelFor(java.awt.Component)
     */
    public final JLabel addI15dLabel(
            String resourceKey,   CellConstraints labelConstraints,
			Component component,  CellConstraints componentConstraints) {

        JLabel label = addLabel(getI15dString(resourceKey), labelConstraints,
                        component, componentConstraints);
        if (isDebugToolTipsEnabled()) {
            label.setToolTipText(resourceKey);
        }
        return label;
    }


    // Adding Labels for Read-Only Fields *************************************

    /**
     * Adds an internationalized (i15d) textual label to the form using the
     * specified constraints that is intended to label a read-only component.
     *
     * @param resourceKey   the resource key for the label's text
     * @param constraints   the label's cell constraints
     * @return the added label
     *
     * @since 1.3
     */
    public final JLabel addI15dROLabel(String resourceKey, CellConstraints constraints) {
        JLabel label = addROLabel(getI15dString(resourceKey), constraints);
        if (isDebugToolTipsEnabled()) {
            label.setToolTipText(resourceKey);
        }
        return label;
    }


    /**
     * Adds an internationalized (i15d) textual label to the form using the
     * specified constraints that is intended to label a read-only component.
     *
     * @param resourceKey         the resource key for the label's text
     * @param encodedConstraints  a string representation for the constraints
     * @return the added label
     *
     * @since 1.3
     */
    public final JLabel addI15dROLabel(String resourceKey, String encodedConstraints) {
        return addI15dROLabel(resourceKey, new CellConstraints(encodedConstraints));
    }


    /**
     * Adds an internationalized (i15d) label and component to the panel using
     * the given cell constraints. Intended for read-only components.
     * Sets the label as <i>the</i> component label
     * using {@link JLabel#setLabelFor(java.awt.Component)}.<p>
     *
     * <strong>Note:</strong> The {@link CellConstraints} objects for the label
     * and the component must be different. Cell constraints are implicitly
     * cloned by the {@code FormLayout} when added to the container.
     * However, in this case you may be tempted to reuse a
     * {@code CellConstraints} object in the same way as with many other
     * builder methods that require a single {@code CellConstraints}
     * parameter.
     * The pitfall is that the methods {@code CellConstraints.xy**(...)}
     * just set the coordinates but do <em>not</em> create a new instance.
     * And so the second invocation of {@code xy***(...)} overrides
     * the settings performed in the first invocation before the object
     * is cloned by the {@code FormLayout}.<p>
     *
     * <strong>Wrong:</strong><pre>
     * builder.addI15dROLabel("name.key",
     *             CC.xy(1, 7),         // will be modified by the code below
     *             nameField,
     *             CC.xy(3, 7)          // sets the single instance to (3, 7)
     *            );
     * </pre>
     * <strong>Correct:</strong><pre>
     * builder.addI15dROLabel("name.key",
     *             CC.xy(1, 7).clone(), // cloned before the next modification
     *             nameField,
     *             CC.xy(3, 7)          // sets this instance to (3, 7)
     *            );
     * </pre>
     * <strong>Better:</strong><pre>
     * builder.addI15dROLabel("name.key",
     *             CC.xy(1, 7)          // creates a CellConstraints object
     *             nameField,
     *             CC.xy(3, 7)          // creates another CellConstraints object
     *            );
     * </pre>
     *
     * @param resourceKey           the resource key for the label
     * @param labelConstraints      the label's cell constraints
     * @param component             the component to add
     * @param componentConstraints  the component's cell constraints
     * @return the added label
     *
     * @throws IllegalArgumentException if the same cell constraints instance
     *     is used for the label and the component
     *
     * @see JLabel#setLabelFor(java.awt.Component)
     *
     * @since 1.3
     */
    public final JLabel addI15dROLabel(
            String resourceKey,   CellConstraints labelConstraints,
            Component component,  CellConstraints componentConstraints) {
        JLabel label = addROLabel(
                getI15dString(resourceKey), labelConstraints,
                component, componentConstraints);
        if (isDebugToolTipsEnabled()) {
            label.setToolTipText(resourceKey);
        }
        return label;
    }


    // Adding Titled Separators ***********************************************

    /**
     * Adds an internationalized (i15d) titled separator to the form using the
     * specified constraints.
     *
     * @param resourceKey  the resource key for the separator title
     * @param constraints  the separator's cell constraints
     * @return the added titled separator
     */
    public final JComponent addI15dSeparator(String resourceKey, CellConstraints constraints) {
        JComponent component = addSeparator(getI15dString(resourceKey), constraints);
        if (isDebugToolTipsEnabled()) {
            component.setToolTipText(resourceKey);
        }
        return component;
    }


    /**
     * Adds an internationalized (i15d)  titled separator to the form using
     * the specified constraints.
     *
     * @param resourceKey         the resource key for the separator title
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
        JLabel label = addTitle(getI15dString(resourceKey), constraints);
        if (isDebugToolTipsEnabled()) {
            label.setToolTipText(resourceKey);
        }
        return label;
    }


    /**
     * Adds a title to the form using the specified constraints.
     *
     * @param resourceKey         the resource key for the separator title
     * @param encodedConstraints  a string representation for the constraints
     * @return the added title label
     */
    public final JLabel addI15dTitle(String resourceKey, String encodedConstraints) {
        return addI15dTitle(resourceKey, new CellConstraints(encodedConstraints));
    }


    // Helper Code ************************************************************

    /**
     * Looks up and returns the internationalized (i15d) string for the given
     * resource key, for example from a {@code ResourceBundle} or
     * {@code ResourceMap}.
     *
     * @param resourceKey  the key to look for in the resource map
     * @return the associated internationalized string, or the resource key
     *     itself in case of a missing resource
     * @throws IllegalStateException  if the localization is not possible,
     *     for example, because no ResourceBundle or StringLocalizer
     *     has been set
     */
    protected final String getI15dString(String resourceKey) {
        checkState(localizer != null,
                "To use the internationalization support " +
                "a ResourceBundle or StringLocalizer " +
                "must be provided during the builder construction.");
    	try {
			return localizer.getString(resourceKey);
		} catch (MissingResourceException ex) {
			Logger.getLogger(getClass().getName()).log(
					Level.WARNING, "Missing internationalized label", ex);
			return resourceKey;
		}
    }


    private static boolean getDebugToolTipSystemProperty() {
        try {
            String value = System.getProperty(DEBUG_TOOL_TIPS_ENABLED_KEY);
            return "true".equalsIgnoreCase(value);
        } catch (SecurityException e) {
            return false;
        }
    }


    // Helper Class **********************************************************

    private static final class ResourceBundleLocalizer implements StringLocalizer {
    	
    	private final ResourceBundle bundle;
    	
    	private ResourceBundleLocalizer(ResourceBundle bundle) {
    		this.bundle = Preconditions.checkNotNull(bundle, "The resource bundle must not be null.");
    	}
    
	    /**
	     * Looks up and returns the internationalized (i15d) string for the given
	     * resource key from the ResourceBundle that has been provided during
	     * the builder construction.
	     *
	     * @param resourceKey  the key to look for in the resource bundle
	     * @return the associated internationalized string, or the resource key
	     *     itself in case of a missing resource
	     * @throws IllegalStateException  if no ResourceBundle
	     *     has been set
	     */
	    @Override
	    public String getString(String resourceKey) {
	        try {
	            return bundle.getString(resourceKey);
	        } catch (MissingResourceException mre) {
	            return resourceKey;
	        }
	    }
	
    }
    


}
