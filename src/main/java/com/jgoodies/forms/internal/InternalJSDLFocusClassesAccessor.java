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

import java.awt.Component;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.FocusTraversalPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractButton;
import javax.swing.LayoutFocusTraversalPolicy;

/**
 * Provides access to focus classes that ship with
 * the JGoodies Standard Dialog Library (JSDL).
 * <ul>
 * <li>com.jgoodies.jsdl.common.focus.FocusTraversalUtils#group(AbstractButton...)</li>
 * <li>com.jgoodies.jsdl.common.focus.JGLayoutFocusTraversalPolicy(Component initialComponent)</li>
 * <li>com.jgoodies.jsdl.common.focus.JGContainerOrderFocusTraversalPolicy(Component initialComponent)</li>
 * </ul>
 *
 * <strong>Note: This class is not part of the public Forms API.
 * It's intended for implementation purposes only.
 * The class's API may change at any time.</strong>
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 */
public final class InternalJSDLFocusClassesAccessor {
    
    private static final String PACKAGE_PREFIX = 
            "com.jgoodies.jsdl.common.focus.";

	/**
	 * The name of the {@code FocusTraversalUtils} class 
	 * from the JSDL Common library.
	 * This utility can group buttons if the focus traversal policy supports
	 * grouping, such as the {@code JGContainerOrderFocusTraversalPolicy} and
	 * {@code JGLayoutFocusTraversalPolicy} from the JSDL Common.
	 */
	private static final String FOCUS_TRAVERSAL_UTILS_CLASS_NAME =
	        "FocusTraversalUtils";

    private static final String GROUP_METHOD_NAME =
            "group";

    /**
     * The name of the {@code JGLayoutFocusTraversalPolicy} class
     * from the JSDL Common library. In addition to its superclass
     * {@link LayoutFocusTraversalPolicy}, it supports grouping 
     * and an initially focused component.
     */
    private static final String JG_LAYOUT_FOCUS_TRAVERSAL_POLICY_NAME =
            "JGLayoutFocusTraversalPolicy";


    /**
     * The name of the {@code JGContainerOrderFocusTraversalPolicy} class
     * from the JSDL Common library. In addition to its superclass
     * {@link LayoutFocusTraversalPolicy}, it supports grouping 
     * and an initially focused component.
     */
    private static final String JG_CONTAINER_ORDER_FOCUS_TRAVERSAL_POLICY_NAME =
            "JGContainerOrderFocusTraversalPolicy";

    /**
     * Holds the public static method
     * {@code com.jgoodies.jsdl.common.focus.FocusTraversalUtils#group}.
     */
    private static Method groupMethod = null;


    /**
     * Holds the public static constructor
     * {@code com.jgoodies.jsdl.common.focus.JGLayoutFocusTraversalPolicy(Component)}.
     */
    private static Constructor<FocusTraversalPolicy> layoutFTPConstructor = null;


    /**
     * Holds the public static constructor
     * {@code com.jgoodies.jsdl.common.focus.JGContainerOrderFocusTraversalPolicy(Component)}.
     */
    private static Constructor<FocusTraversalPolicy> containerOrderFTPConstructor = null;


    static {
    	groupMethod = getGroupMethod();
    	layoutFTPConstructor = getJGLayoutFocusTraversalPolicyConstructor();
    	containerOrderFTPConstructor = getJGContainerOrderFocusTraversalPolicyConstructor();
    }


	private InternalJSDLFocusClassesAccessor() {
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
        } catch (IllegalAccessException ex) {
            // Do nothing
        } catch (InvocationTargetException ex) {
            // Do nothing
        }
    }


    /**
     * Tries to construct a {@code JGLayoutFocusTraversalPolicy} with
     * the given initial component. If this class is not in the class path,
     * a {@link LayoutFocusTraversalPolicy} is created and returned.
     * In the latter case, the {@code initialComponent} is ignored.
     *
     * @param initialComponent    the component that shall receive the focus
     *    if it is transferred to the container the first time
     * @return the created focus traversal policy
     */
    static FocusTraversalPolicy createLayoutFocusOrderTraversalPolicy(Component initialComponent) {
        if (layoutFTPConstructor != null) {
            try {
                return layoutFTPConstructor.newInstance(initialComponent);
            } catch (IllegalAccessException ex) {
                // Do nothing
            } catch (InvocationTargetException ex) {
                // Do nothing
            } catch (IllegalArgumentException ex) {
                // Do nothing
            } catch (InstantiationException ex) {
                // Do nothing
            }
        }
        return new ContainerOrderFocusTraversalPolicy();
    }


    /**
     * Tries to construct a {@code JGLayoutFocusTraversalPolicy} with
     * the given initial component. If this class is not in the class path,
     * a {@link LayoutFocusTraversalPolicy} is created and returned.
     * In the latter case, the {@code initialComponent} is ignored.
     *
     * @param initialComponent    the component that shall receive the focus
     *    if it is transferred to the container the first time
     * @return the created focus traversal policy
     */
    static FocusTraversalPolicy createContainerOrderFocusOrderTraversalPolicy(Component initialComponent) {
        if (containerOrderFTPConstructor != null) {
            try {
                return containerOrderFTPConstructor.newInstance(initialComponent);
            } catch (IllegalAccessException ex) {
                // Do nothing
            } catch (InvocationTargetException ex) {
                // Do nothing
            } catch (IllegalArgumentException ex) {
                // Do nothing
            } catch (InstantiationException ex) {
                // Do nothing
            }
        }
        return new ContainerOrderFocusTraversalPolicy();
    }


    // Private Helper Code ****************************************************

    private static Method getGroupMethod() {
        try {
            Class<?> clazz = Class.forName(PACKAGE_PREFIX + FOCUS_TRAVERSAL_UTILS_CLASS_NAME);
            return clazz.getMethod(GROUP_METHOD_NAME, new Class[] {AbstractButton[].class});
        } catch (ClassNotFoundException ex) {
            // Ignore
        } catch (SecurityException ex) {
        	// Ignore
        } catch (NoSuchMethodException ex) {
        	// Ignore
        }
        return null;
    }


    private static Constructor<FocusTraversalPolicy> getJGLayoutFocusTraversalPolicyConstructor() {
        try {
            Class<?> clazz = Class.forName(PACKAGE_PREFIX + JG_LAYOUT_FOCUS_TRAVERSAL_POLICY_NAME);
            return (Constructor<FocusTraversalPolicy>) clazz.getConstructor(new Class[] {Component.class});
        } catch (ClassNotFoundException ex) {
            // Ignore
        } catch (SecurityException ex) {
            // Ignore
        } catch (NoSuchMethodException ex) {
            // Ignore
        }
        return null;
    }


    private static Constructor<FocusTraversalPolicy> getJGContainerOrderFocusTraversalPolicyConstructor() {
        try {
            Class<?> clazz = Class.forName(PACKAGE_PREFIX + JG_CONTAINER_ORDER_FOCUS_TRAVERSAL_POLICY_NAME);
            return (Constructor<FocusTraversalPolicy>) clazz.getConstructor(new Class[] {Component.class});
        } catch (ClassNotFoundException ex) {
            // Ignore
        } catch (SecurityException ex) {
            // Ignore
        } catch (NoSuchMethodException ex) {
            // Ignore
        }
        System.out.println("could not construct the JGContainerOrderFocusTraversalPolicy");
        return null;
    }


}
