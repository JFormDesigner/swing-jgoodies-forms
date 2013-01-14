/*
 * Copyright (c) 2002-2013 JGoodies Software GmbH. All Rights Reserved.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractButton;

/**
 * Provides access to the FocusTraversalUtils class that ships with the
 * JGoodies Standard Dialog Library (JSDL).
 *
 * <strong>Note: This class is not part of the public Forms API.
 * It's intended for implementation purposes only.
 * The class's API may change at any time.</strong>
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 */
public final class FocusTraversalUtilsAccessor {

	/**
	 * The name of the {@code FocusTraversalUtils} from the JSDL Common library.
	 * This utility can group buttons if the focus traversal policy supports
	 * grouping, such as the {@code JGContainerOrderFocusTraversalPolicy} and
	 * {@code JGLayoutFocusTraversalPolicy} from the JSDL Common.
	 */
	private static final String FOCUS_TRAVERSAL_UTILS_NAME =
			"com.jgoodies.jsdl.common.focus.FocusTraversalUtils";

    /**
     * Holds the public static method
     * {@code com.jgoodies.jsdl.common.focus.FocusTraversalUtils#group}.
     */
    private static Method groupMethod = null;


    static {
    	groupMethod = getGroupMethod();
    }


	private FocusTraversalUtilsAccessor() {
		// Overrides default constructor; prevents instantiation.
	}


    // Implementation *********************************************************

    /**
     * Tries to group the given buttons using the FocusTraversalUtils class
     * - if available. Does nothing, if this class is not in the class path.
     */
    public static void tryToBuildAFocusGroup(AbstractButton... buttons) {
    	if (groupMethod == null) {
    		return;
    	}
        try {
            groupMethod.invoke(null, (Object) buttons);
        } catch (IllegalAccessException e) {
            // Do nothing
        } catch (InvocationTargetException e) {
            // Do nothing
        }
    }


    // Private Helper Code ****************************************************

    private static Method getGroupMethod() {
        try {
            Class<?> clazz = Class.forName(FOCUS_TRAVERSAL_UTILS_NAME);
            return clazz.getMethod("group", new Class[] {AbstractButton[].class});
        } catch (ClassNotFoundException e) {
            // Ignore
        } catch (SecurityException e) {
        	// Ignore
        } catch (NoSuchMethodException e) {
        	// Ignore
        }
        return null;
    }


}
