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

package com.jgoodies.forms.factories;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * An extension to the ComponentFactory interface that describes
 * the describes the read-only label creation introduced by the
 * Forms 1.3. In version 1.4 it has been further extended to create
 * buttons for Actions.<p>
 *
 * <strong>Note: This interface shall be merged with the ComponentFactory
 * interface in the Forms 2.0</strong>.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.5 $
 *
 * @since 1.3
 *
 * @see    DefaultComponentFactory
 * @see    com.jgoodies.forms.builder.PanelBuilder
 */
public interface ComponentFactory2 extends ComponentFactory {

    /**
     * Creates and returns a label with an optional mnemonic
     * that is intended to label a read-only component.<p>
     *
     * <pre>
     * createReadOnlyLabel("Name");       // No mnemonic
     * createReadOnlyLabel("N&ame");      // Mnemonic is 'a'
     * createReadOnlyLabel("Save &as");   // Mnemonic is the second 'a'
     * createReadOnlyLabel("Look&&Feel"); // No mnemonic, text is Look&Feel
     * </pre>
     *
     * @param textWithMnemonic  the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return an label with optional mnemonic intended for read-only
     *     components
     *
     * @since 1.3
     */
    JLabel createReadOnlyLabel(String textWithMnemonic);


    /**
     * Creates and returns a button that is bound to the given Action.
     * Useful to return customized buttons, for example, the JGoodies
     * {@code JGButton} is bound to some custom Action properties.
     *
     * @param action    provides [bound] visual properties for the button
     * @return the created button
     *
     * @since 1.4
     */
    JButton createButton(Action action);


}
