/*
 * Copyright (c) 2002-2014 JGoodies Software GmbH. All Rights Reserved.
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
 *  o Neither the name of JGoodies Software GmbH nor the names of
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

package com.jgoodies.forms;

import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.DefaultComponentFactory;


/**
 * Provides access to global Forms settings.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.21 $
 *
 * @since 1.8
 */
public class FormsSetup {


    // Constants **************************************************************
    
    private static final String DEBUG_TOOL_TIPS_ENABLED_KEY =
            "FormsSetup.debugToolTipsEnabled";


    // Static Fields **********************************************************

    /**
     * Holds the global factory that is used as default for the
     * per-instance component factory.
     */
    private static ComponentFactory componentFactoryDefault;


    /**
     * The global default for the enablement of the setLabelFor feature.
     * Turned on by default.
     *
     * @see #setLabelForFeatureEnabledDefault(boolean)
     * @see #setLabelForFeatureEnabledDefault(boolean)
     */
    private static boolean labelForFeatureEnabledDefault = true;


    /**
     * Holds the global default opaque state that can be overridden
     * per builder. Since the Forms 1.6, the default value is {@code false},
     * in other words, panels will not be opaque.
     */
    private static boolean opaqueDefault = false;


    private static boolean debugToolTipsEnabled =
            getDebugToolTipSystemProperty();

    
   // Instance Creation ******************************************************

    private FormsSetup() {
        // Overrides default constructor; prevents instantiation.
    }


    // Global Defaults ********************************************************

    /**
     * Returns the factory that is used as default for new builder's
     * as they are created. This default itself is lazily initialized
     * as the {@link DefaultComponentFactory}.
     *
     * @return the factory that is used as default for new builder instances
     */
    public static ComponentFactory getComponentFactoryDefault() {
        if (componentFactoryDefault == null) {
            componentFactoryDefault = new DefaultComponentFactory();
        }
        return componentFactoryDefault;
    }


    /**
     * Sets the global default that is used to initialize the per-instance
     * component factory.
     *
     * @param factory  the factory to be used for all new builder instances
     *    that do not override the default
     */
    public static void setComponentFactoryDefault(ComponentFactory factory) {
        componentFactoryDefault = factory;
    }


    /**
     * Returns the global default for the enablement of the setLabelFor feature.
     * This can be overridden per builder - where applicable -  using
     * {@code #labelForFeatureEnabled(boolean)}.
     * The feature is globally disabled by default.
     *
     * @return true for globally enabled, false for globally disabled
     */
    public static boolean getLabelForFeatureEnabledDefault() {
        return labelForFeatureEnabledDefault;
    }


    /**
     * Sets the default value for the setLabelFor feature enablement.
     * This can be overridden per builder - where applicable - using
     * {@code #labelForFeatureEnabled(boolean)}.
     * The default value is used to set the initial PanelBuilder
     * setting for this feature.
     * The feature is globally disabled by default.
     *
     * @param b true for globally enabled, false for globally disabled
     */
    public static void setLabelForFeatureEnabledDefault(boolean b) {
        labelForFeatureEnabledDefault = b;
    }


    /**
     * @return the global default value for a builder's opaque state
     *     that can be overridden per builder
     */
    public static boolean getOpaqueDefault() {
    	return opaqueDefault;
    }


    /**
     * Sets the global default value for a builder's opaque state
     * that can be overridden per builder.
     * Since the Forms 1.6, the default value is {@code false},
     * in other words, panels will not be opaque.
     *
     * @param b   the new value
     */
    public static void setOpaqueDefault(boolean b) {
    	opaqueDefault = b;
    }


    /**
     * Returns whether the debug tool tips are enabled or not.
     *
     * @return true if debug tool tips are enabled, false if disabled
     */
    public static boolean getDebugToolTipsEnabledDefault() {
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


    private static boolean getDebugToolTipSystemProperty() {
        try {
            String value = System.getProperty(DEBUG_TOOL_TIPS_ENABLED_KEY);
            return "true".equalsIgnoreCase(value);
        } catch (SecurityException e) {
            return false;
        }
    }


}
