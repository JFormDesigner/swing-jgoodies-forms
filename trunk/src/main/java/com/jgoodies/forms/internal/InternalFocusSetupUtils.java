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

package com.jgoodies.forms.internal;

import static com.jgoodies.common.base.Preconditions.checkState;

import java.awt.Component;
import java.awt.FocusTraversalPolicy;

import javax.swing.JComponent;

import com.jgoodies.forms.builder.FocusTraversalType;


/**
 * Provides internal convenience behavior for builders that
 * setup a focus traversal policies directly or implicitly 
 * by specifying a focus traversal type and an optional initial component.<p>
 *
 * <strong>Note: This class is not part of the public Forms API.
 * It's intended for implementation purposes only.
 * The class's API may change at any time.</strong>
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 */
public final class InternalFocusSetupUtils {


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
        checkState((policy != null && type == null && initialComponent == null)
                || (policy == null), 
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
            return InternalJSDLFocusClassesAccessor
                .createContainerOrderFocusOrderTraversalPolicy(initialComponent);
        }
        return InternalJSDLFocusClassesAccessor
                .createLayoutFocusOrderTraversalPolicy(initialComponent);
    }


	
	
}
