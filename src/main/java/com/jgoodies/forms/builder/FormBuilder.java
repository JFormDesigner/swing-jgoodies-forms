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

package com.jgoodies.forms.builder;

import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.layout.FormLayout;

/**
 * An general purpose form builder that uses the {@link FormLayout}
 * to lay out and populate {@code JPanel}s. It provides the following features:
 * <ul>
 * <li>Short code, good readability.</li>
 * <li>Layout and panel building in a single class.</li>
 * <li>Layout construction easier to understand (compared to FormLayout constructors).</li>
 * <li>Implicitly creates frequently used components such as labels.</li>
 * <li>Convenience code for adding button bars, radio button groups, etc.</li>
 * <li>Can add components only if a condition evaluates to {@code true}.</li>
 * <li>Toolkit-independent code, see {@link #focusTraversalType} vs.
 * {@link #focusTraversalPolicy}.</li>
 * </ul>
 * See also the feature overview below.<p>
 * 
 * The FormBuilder is the working horse
 * for forms and panels where more specialized builders such as the
 * {@link ListViewBuilder} or the {@link ButtonBarBuilder} are inappropriate.
 * Since FormBuilder supports the frequently used methods for setting up
 * and configuring a FormLayout, the vast majority of forms can be built
 * with just the FormBuilder. In other words, you will typically not
 * work with FormLayout instances directly.<p>
 * 
 * Forms are built as a two-step process:
 * first, you setup and configure the layout, then add the components.<p>
 * <strong>Example:</strong> (creates a panel with 3 columns and 3 rows)
 * <pre>
 * return FormBuilder.create()
 *     .columns("left:pref, $lcgap, 50dlu, $rgap, default")
 *     .rows("p, $lg, p, $lg, p")
 *     .padding(Paddings.DIALOG)
 * 
 *     .add("&Title:")   .xy  (1, 1)
 *     .add(titleField)  .xywh(3, 1, 3, 1)
 *     .add("&Price:")   .xy  (1, 3)
 *     .add(priceField)  .xy  (3, 3)
 *     .add("&Author:")  .xy  (1, 5)
 *     .add(authorField) .xy  (3, 5)
 *     .add(browseButton).xy  (5, 5)
 *     .build();
 * </pre>
 * 
 * FormBuilder provides convenience methods for adding labels, titles, and
 * titled separators. These components will be created by the builder's
 * component factory that can be set via {@link #factory(ComponentFactory)},
 * and that is by default initialized from
 * {@link FormsSetup#getComponentFactoryDefault()}.<p>
 *
 * The text arguments passed to the methods {@code #addLabel},
 * {@code #addTitle}, and {@code #addSeparator} can contain
 * an optional mnemonic marker. The mnemonic and mnemonic index
 * are indicated by a single ampersand (<tt>&amp;</tt>). For example
 * <tt>&quot;&amp;Save&quot</tt>, or <tt>&quot;Save&nbsp;&amp;as&quot</tt>.
 * To use the ampersand itself duplicate it, for example
 * <tt>&quot;Look&amp;&amp;Feel&quot</tt>.<p>
 * 
 * <strong>Feature Overview:</strong>
 * <pre>
 *     .columns("pref, $lcgap, %sdlu, p, p", "50")  // Format string
 *     .columnGroup(4, 5)                           // Grouping short hand
 *     .debug(true)                                 // Installs FormDebugPanel
 * 
 *     .add("Title:")         .xy(1, 1)             // Implicitly created label
 *     .add("&Price:")        .xy(1, 1)             // Label with mnemonic
 * 
 *     .add(hasCountry, combo).xy(3, 1)             // Conditional adding
 * 
 *     .add(aTable)           .xywh(1, 1, 3, 5)    // Auto-wrapped with scrollpane
 *     .addScrolled(aTextArea).xywh(1, 1, 1, 3)    // scrollpane shorthand
 * 
 *     .addBar(newBtn, editBtn, deleteBtn).xy(1, 5) // button bar
 *     .addBar(landscapeRadio, portraitRadio).xy(1, 1) // Radio button bar
 * </pre><p>
 * 
 * TODO: Consider moving almost everything from this class to an abstract
 * and generified superclass that reduces the effort to implement subclasses
 * with extra features. FormBuilder has been designed and documented as
 * final class. The final marker has been removed to allow API users to add
 * an internationalization feature similar to PanelBuilder/I15PanelBuilder.
 * But other extensions and complements may show up.<p>
 * 
 * TODO: Consider changing the {@link #build()} signature to return
 * a JComponent, so subclasses can return a component other than the
 * panel, the builder adds components to.
 *
 * @author  Karsten Lentzsch
 *
 * @see	FormLayout
 * 
 * @since 1.9
 */
public class FormBuilder extends AbstractFormBuilder<FormBuilder> {
    

    // Instance Creation ******************************************************

    /**
     * Creates and return a new FormBuilder instance.
     */
    public static FormBuilder create() {
        return new FormBuilder();
    }



}
