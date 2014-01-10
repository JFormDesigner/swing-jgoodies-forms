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

package com.jgoodies.forms.builder;

import static com.jgoodies.common.base.Preconditions.checkNotNull;

import java.awt.Container;

import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * An abstract class that minimizes the effort required to implement
 * non-visual builders that use the {@link FormLayout}.<p>
 *
 * Builders hide details of the FormLayout and provide convenience behavior
 * that assists you in constructing a form, bar, stack.
 * This class provides a cell cursor that helps you traverse a form while
 * you add components. Also, it offers several methods to append custom
 * and logical columns and rows.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.3 $
 *
 * @see    ButtonBarBuilder
 * @see    ButtonStackBuilder
 * @see    PanelBuilder
 * @see    I15dPanelBuilder
 * @see    DefaultFormBuilder
 */
public abstract class AbstractBuilder {

    /**
     * Holds the layout container that we are building.
     */
    private final Container container;

    /**
     * Holds the FormLayout instance that is used
     * to specify, fill and layout this form.
     */
    private final FormLayout layout;

    /**
     * Holds an instance of {@link CellConstraints} that will be used to
     * specify the location, extent and alignments of the component to be
     * added next.
     */
    protected final CellConstraints currentCellConstraints;

    /**
     * Refers to a factory that is used to create labels, titles,
     * separators, and buttons.
     */
    private ComponentFactory componentFactory;


    // Instance Creation ****************************************************

    /**
     * Constructs an AbstractBuilder
     * for the given FormLayout and layout container.
     *
     * @param layout     the FormLayout to use
     * @param container  the layout container
     *
     * @throws NullPointerException if {@code layout} or {@code container} is {@code null}
     */
    protected AbstractBuilder(FormLayout layout, Container container) {
        this.layout    = checkNotNull(layout, "The layout must not be null.");
        this.container = checkNotNull(container, "The layout container must not be null.");
        container.setLayout(layout);
        currentCellConstraints = new CellConstraints();
    }


    // Accessors ************************************************************

    /**
     * Returns the container used to build the form.
     *
     * @return the layout container
     */
    public final Container getContainer() {
        return container;
    }


    /**
     * Returns the FormLayout instance used to build this form.
     *
     * @return the FormLayout
     */
    public final FormLayout getLayout() {
        return layout;
    }


    /**
     * Returns the number of columns in the form.
     *
     * @return the number of columns
     */
    public final int getColumnCount() {
        return getLayout().getColumnCount();
    }


    /**
     * Returns the number of rows in the form.
     *
     * @return the number of rows
     */
    public final int getRowCount() {
        return getLayout().getRowCount();
    }


    /**
     * Returns this builder's component factory. If no factory
     * has been set before, it is lazily initialized from the global
     * default as returned by {@link FormsSetup#getComponentFactoryDefault()}.
     *
     * @return the component factory
     *
     * @see #setComponentFactory(ComponentFactory)
     */
    public final ComponentFactory getComponentFactory() {
        if (componentFactory == null) {
            componentFactory = createComponentFactory();
        }
        return componentFactory;
    }


    /**
     * Sets a new component factory for this builder,
     * overriding the default as provided by
     * {@link FormsSetup#getComponentFactoryDefault()}.
     *
     * @param newFactory   the component factory to be used for this builder
     *
     * @see #getComponentFactory()
     */
    public final void setComponentFactory(ComponentFactory newFactory) {
        componentFactory = newFactory;
    }


    /**
     * Invoked when the per-instance component factory is lazily initialized.
     * This implementation returns the global default factory.<p>
     *
     * Subclasses may override to use a factory other than the global default.
     * However, in most cases it is sufficient to just set a new global default
     * using {@link FormsSetup#setComponentFactoryDefault(ComponentFactory)}.
     *
     * @return the factory used during the lazy initialization of
     *    the per-instance component factory
     */
    protected ComponentFactory createComponentFactory() {
        return FormsSetup.getComponentFactoryDefault();
    }


}
