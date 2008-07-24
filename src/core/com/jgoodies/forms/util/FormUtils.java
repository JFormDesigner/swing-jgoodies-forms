/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.forms.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

import javax.swing.UIManager;

/**
 * Consists only of static utility methods.
 *
 * This class may be merged with the FormLayoutUtils extra - or not.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.6 $
 *
 * @since 1.2
 */
public final class FormUtils {


    // Instance *************************************************************

    private FormUtils() {
        // Suppresses default constructor, prevents instantiation.
    }


    // API ********************************************************************

    /**
     * Throws an exception if the specified text is blank using the given
     * text description.
     *
     * @param text          the text to check
     * @param description   describes the text, used in the exception message
     *
     * @throws NullPointerException if {@code text} is {@code null}
     * @throws IllegalArgumentException if {@code text} is empty, or blank
     */
    public static void assertNotBlank(String text, String description) {
        if (text == null)
            throw new NullPointerException("The " + description + " must not be null.");
        if (FormUtils.isBlank(text)) {
            throw new IllegalArgumentException(
                    "The " + description + " must not be empty, or whitespace. " +
                    "See FormUtils.isBlank(String)");
        }
    }


    /**
     * Throws an NPE if the given object is {@code null} that uses
     * the specified text to describe the object.
     *
     * @param object        the text to check
     * @param description   describes the object, used in the exception message
     *
     * @throws NullPointerException if {@code object} is {@code null}
     */
    public static void assertNotNull(Object object, String description) {
        if (object == null)
            throw new NullPointerException("The " + description + " must not be null.");
    }


    /**
     * Checks and answers if the two objects are
     * both {@code null} or equal.
     *
     * <pre>
     * #equals(null, null)  == true
     * #equals("Hi", "Hi")  == true
     * #equals("Hi", null)  == false
     * #equals(null, "Hi")  == false
     * #equals("Hi", "Ho")  == false
     * </pre>
     *
     * @param o1        the first object to compare
     * @param o2        the second object to compare
     * @return boolean  {@code true} if and only if
     *    both objects are {@code null} or equal
     */
    public static boolean equals(Object o1, Object o2) {
        return    ((o1 != null) && (o2 != null) && (o1.equals(o2)))
               || ((o1 == null) && (o2 == null));
    }


    /**
     * Checks and answers if the given string is whitespace, empty (""),
     * or {@code null}.
     *
     * <pre>
     * FormUtils.isBlank(null)    == true
     * FormUtils.isBlank("")      == true
     * FormUtils.isBlank(" ")     == true
     * FormUtils.isBlank(" abc")  == false
     * FormUtils.isBlank("abc ")  == false
     * FormUtils.isBlank(" abc ") == false
     * </pre>
     *
     * @param str   the string to check, may be{@code null}
     * @return {@code true} if the string is whitespace, empty, or {@code null}
     */
    public static boolean isBlank(String str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }
        for (int i = length - 1; i >= 0; i--) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * Checks and answers if the given string is not empty (""),
     * not {@code null} and not whitespace only.
     *
     * <pre>
     * FormUtils.isNotBlank(null)    == false
     * FormUtils.isNotBlank("")      == false
     * FormUtils.isNotBlank(" ")     == false
     * FormUtils.isNotBlank(" abc")  == true
     * FormUtils.isNotBlank("abc ")  == true
     * FormUtils.isNotBlank(" abc ") == true
     * </pre>
     *
     * @param str   the string to check, may be {@code null}
     * @return {@code true} if the string is not empty
     *    and not {@code null} and not whitespace only
     */
    public static boolean isNotBlank(String str) {
        int length;
        if ((str == null) || ((length = str.length()) == 0))
            return false;
        for (int i = length-1; i >= 0; i--) {
            if (!Character.isWhitespace(str.charAt(i)))
                return true;
        }
        return false;
    }


    /**
     * Lazily checks and answers whether the Aqua look&amp;feel is active.
     *
     * @return {@code true} if the current look&amp;feel is Aqua
     */
    public static boolean isLafAqua() {
        if (cachedIsLafAqua == null) {
            cachedIsLafAqua = Boolean.valueOf(computeIsLafAqua());
            ensureLookAndFeelChangeHandlerRegistered();
        }
        return cachedIsLafAqua.booleanValue();
    }


    // Caching and Lazily Computing the Laf State *****************************

    /**
     * Holds the cached result of the Aqua l&amp;f check.
     * Is invalidated by the <code>LookAndFeelChangeHandler</code>
     * if the look&amp;feel changes.
     */
    private static Boolean cachedIsLafAqua;

    /**
     * Describes whether the <code>LookAndFeelChangeHandler</code>
     * has been registered with the <code>UIManager</code> or not.
     * It is registered lazily when the first cached l&amp;f state is computed.
     */
    private static boolean lafChangeHandlerRegistered = false;

    private static synchronized void ensureLookAndFeelChangeHandlerRegistered() {
        if (!lafChangeHandlerRegistered) {
            addWeakUIManagerPropertyChangeListener(new LookAndFeelChangeHandler());
            lafChangeHandlerRegistered = true;
        }
    }

    private static void clearLookAndFeelBasedCaches() {
        cachedIsLafAqua = null;
        DefaultUnitConverter.getInstance().clearCache();
    }


    /**
     * Computes and answers whether an Aqua look&amp;feel is active.
     * This may be Apple's Aqua L&amp;f, or a sub-L&amp;f that
     * uses the same ID, because it doesn't substantially change the look.
     *
     * @return true if the current look&amp;feel is Aqua
     */
    private static boolean computeIsLafAqua() {
        return UIManager.getLookAndFeel().getID().equals("Aqua");
    }


    /**
     * Listens to changes of the Look and Feel and invalidates the cache.
     */
    private static final class LookAndFeelChangeHandler implements PropertyChangeListener {

        /**
         * Invalidates the cached laf states, if the UIManager has fired
         * any property change event. Since we need to handle look&amp;feel
         * changes only, we check the event's property name to be
         * "lookAndFeel" or <code>null</code>. The check for null is necessary
         * to handle the special event where property name, old and new value
         * are all <code>null</code> to indicate that multiple properties
         * have changed.
         *
         * @param evt  describes the property change
         */
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if ((propertyName == null) || propertyName.equals("lookAndFeel")) {
                clearLookAndFeelBasedCaches();
            }
        }
    }

    // Weak UIManager PropertyChangeListener **********************************

    /**
     * Creates a wrapper PropertyChangeListener that holds {@code listener}
     * holds it in a {@link WeakReference}, and registers the wrapper
     * with the UIManager. If the reference to the wrapped listener
     * is cleared, the wrapper will be removed as listener from the UIManager.
     * Used to avoid memory leaks when registering PropertyChangeListeners
     * with the UIManager.
     *
     * @param listener       the wrapped listener that handles the events
     *
     * @throws NullPointerException if {@code listener} is {@code null}.
     *
     * @see WeakReference
     */
    private static void addWeakUIManagerPropertyChangeListener(PropertyChangeListener listener) {
        if (listener == null) {
            throw new NullPointerException("The wrapped listener must not be null.");
        }
        UIManager.addPropertyChangeListener(new WeakUIManagerListener(listener));
    }


    private static final class WeakUIManagerListener implements PropertyChangeListener {

        private final WeakReference listenerReference;

        private WeakUIManagerListener(PropertyChangeListener listener){
            listenerReference = new WeakReference(listener);
        }

        public void propertyChange(PropertyChangeEvent evt){
            PropertyChangeListener listener =
                (PropertyChangeListener) listenerReference.get();
            if (listener != null) {
                listener.propertyChange(evt);
                return;
            }
            UIManager.removePropertyChangeListener(this);
        }

    }


}
