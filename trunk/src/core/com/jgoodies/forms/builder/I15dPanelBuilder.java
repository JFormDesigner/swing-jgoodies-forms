/*
 * Copyright (c) 2002-2011 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.swing.MnemonicUtils;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A general purpose panel builder that uses the FormLayout
 * to lay out JPanels. In addition to its superclass
 * {@link PanelBuilder} this class provides convenience behavior to map
 * resource keys to their associated internationalized (i15d) strings
 * when adding labels, titles and titled separators.<p>
 *
 * The localized texts used in methods {@code #addI15d*} can be
 * <em>marked texts</em>, i.e. strings with an optional mnemonic marker.
 * See the {@link MnemonicUtils} class comment for details.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.11 $
 * @since 1.0.3
 *
 * @see	ResourceBundle
 */
public class I15dPanelBuilder extends AbstractI15dPanelBuilder {

    /**
     * Holds the ResourceBundle used to look up internationalized
     * (i15d) String resources.
     */
    private final ResourceBundle bundle;


    // Instance Creation ****************************************************

    /**
     * Constructs an I15dPanelBuilder for the given layout and resource bundle.
     * Uses an instance of JPanel as layout container.
     *
     * @param layout    the FormLayout used to layout the container
     * @param bundle    the ResourceBundle used to look up i15d strings
     *
     * @throws NullPointerException if {@code layout} is {@code null}
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
     * @throws NullPointerException if {@code layout} or {@code container} is {@code null}
     */
    public I15dPanelBuilder(FormLayout layout, ResourceBundle bundle, JPanel container){
        super(layout, container);
        this.bundle = bundle;
    }


    // Implementing Abstract Behavior *****************************************

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
    protected String getI15dString(String resourceKey) {
        Preconditions.checkState(bundle != null,
                "To use the internationalization support " +
                "a ResourceBundle must be provided during the builder construction.");
        try {
            return bundle.getString(resourceKey);
        } catch (MissingResourceException mre) {
            return resourceKey;
        }
    }



}
