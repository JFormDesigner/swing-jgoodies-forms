/*
 * Copyright (c) 2002-2015 JGoodies Software GmbH. All Rights Reserved.
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

package com.jgoodies.forms.internal;

import static com.jgoodies.common.base.Preconditions.checkState;

import java.awt.Component;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.FocusTraversalPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.LayoutFocusTraversalPolicy;

import com.jgoodies.forms.util.FocusTraversalType;


/**
 * Provides internal convenience behavior for builders that
 * setup a focus traversal policy directly or implicitly
 * by specifying a focus traversal type plus optional initial component.<p>
 *
 * <strong>Note: This class is not part of the public Forms API.
 * It's intended for implementation purposes only.
 * The class's API may change at any time.</strong>
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 * 
 * @since 1.9
 */
public final class InternalFocusSetupUtils {


    /**
     * The name of the commercial {@code JGContainerOrderFocusTraversalPolicy}
     * from the JSDL Common library that supports grouping buttons.
     */
    private static final String JGContainerOrderFocusTraversalPolicy_NAME =
            "com.jgoodies.jsdl.common.focus.JGContainerOrderFocusTraversalPolicy";

    /**
     * The name of the commercial {@code JGLayoutFocusTraversalPolicy}
     * from the JSDL Common library that supports grouping buttons.
     */
    private static final String JGLayoutFocusTraversalPolicy_NAME =
            "com.jgoodies.jsdl.common.focus.JGLayoutFocusTraversalPolicy";

    /**
     * Holds the {@code JGContainerOrderFocusTraversalPolicy}'s constructor
     * - if in the class path.
     */
    private static Constructor<FocusTraversalPolicy> containerOrderFTPConstructor = null;

    /**
     * Holds the {@code JGLayoutFocusTraversalPolicy}'s constructor
     * - if in the class path.
     */
    private static Constructor<FocusTraversalPolicy> layoutFTPConstructor = null;

    static {
        containerOrderFTPConstructor = getContainerOrderFTPConstructor();
        layoutFTPConstructor         = getLayoutFTPConstructor();
    }


	private InternalFocusSetupUtils() {
		// Overrides default constructor; prevents instantiation.
	}


    // Implementation *********************************************************

    /**
     * Checks that if the API user has set a focus traversal policy,
     * no focus traversal type and no initial component has been set.
     */
    public static void checkValidFocusTraversalSetup(
            FocusTraversalPolicy policy,
            FocusTraversalType type,
            Component initialComponent) {
        checkState(policy != null && type == null && initialComponent == null
                || policy == null,
                "Either use #focusTraversalPolicy or #focusTraversalType plus optional #initialComponent); don't mix them.");
    }
    
    
    public static void setupFocusTraversalPolicyAndProvider(
            JComponent container,
            FocusTraversalPolicy policy,
            FocusTraversalType type,
            Component initialComponent) {
        container.setFocusTraversalPolicy(
                getOrCreateFocusTraversalPolicy(policy, type, initialComponent));
        container.setFocusTraversalPolicyProvider(true);
    }
    
    
    public static FocusTraversalPolicy getOrCreateFocusTraversalPolicy(
            FocusTraversalPolicy policy,
            FocusTraversalType type,
            Component initialComponent) {
        if (policy != null) {
            return policy;
        }
        if (type == FocusTraversalType.CONTAINER_ORDER) {
            return createContainerOrderFocusTraversalPolicy(initialComponent);
        }
        return createLayoutFocusTraversalPolicy(initialComponent);
    }
    
    
    // Helper Code ************************************************************
    
    private static FocusTraversalPolicy createContainerOrderFocusTraversalPolicy(Component initialComponent) {
        if (containerOrderFTPConstructor != null) {
            try {
                return containerOrderFTPConstructor.newInstance(initialComponent);
            } catch (IllegalArgumentException ex) {
                // Ignore
            } catch (InstantiationException ex) {
                // Ignore
            } catch (IllegalAccessException ex) {
                // Ignore
            } catch (InvocationTargetException ex) {
                // Ignore
            }
        }
        return new ContainerOrderFocusTraversalPolicy();
    }

	
    private static FocusTraversalPolicy createLayoutFocusTraversalPolicy(Component initialComponent) {
        if (layoutFTPConstructor != null) {
            try {
                return layoutFTPConstructor.newInstance(initialComponent);
            } catch (IllegalArgumentException
                    | InstantiationException
                    | IllegalAccessException
                    | InvocationTargetException ex) {
                // Ignore
            }
        }
        return new LayoutFocusTraversalPolicy();
    }

    
    private static Constructor<FocusTraversalPolicy> getContainerOrderFTPConstructor() {
        try {
            return (Constructor<FocusTraversalPolicy>) Class.forName(JGContainerOrderFocusTraversalPolicy_NAME).getConstructor(Component.class);
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException e) {
            return null;
        }
    }
    

    private static Constructor<FocusTraversalPolicy> getLayoutFTPConstructor() {
        try {
            return (Constructor<FocusTraversalPolicy>) Class.forName(JGLayoutFocusTraversalPolicy_NAME).getConstructor(Component.class);
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException e) {
            return null;
        }
    }

    
}
