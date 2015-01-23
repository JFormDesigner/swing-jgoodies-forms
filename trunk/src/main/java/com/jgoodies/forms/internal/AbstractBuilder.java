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

import static com.jgoodies.common.base.Preconditions.checkNotNull;
import static com.jgoodies.common.internal.Messages.MUST_NOT_BE_NULL;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.Paddings;
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
 * @param <B>  the type of the builder, e.g. ButtonBarBuilder
 */
public abstract class AbstractBuilder<B extends AbstractBuilder<B>> {

    /**
     * Holds the layout container that we are building.
     */
    private final JPanel panel;

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
     * Constructs an AbstractBuilder for the given layout and panel.
     *
     * @param layout     the FormLayout to use
     * @param panel      the layout container
     *
     * @throws NullPointerException if {@code layout} or {@code panel} is {@code null}
     */
    protected AbstractBuilder(FormLayout layout, JPanel panel) {
        this.layout    = checkNotNull(layout, MUST_NOT_BE_NULL, "layout");
        this.panel = checkNotNull(panel,  MUST_NOT_BE_NULL, "panel");
        panel.setLayout(layout);
        currentCellConstraints = new CellConstraints();
    }


    // Accessors ************************************************************

    /**
     * Returns the panel used to build the form.
     * Intended to access panel properties. For returning the built panel,
     * you should use {@link #build()}.
     *
     * @return the panel used by this builder to build the form
     */
    public final JPanel getPanel() {
        return panel;
    }
    
    
    public abstract JPanel build();


    /**
     * Returns the container used to build the form.
     *
     * @return the layout container
     * 
     * @deprecated Replaced by {@link #getPanel()}
     */
    @Deprecated
    public final Container getContainer() {
        return panel;
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


    // Frequently Used Panel Properties ***************************************

    /**
     * Sets the panel's background color and makes the panel opaque.
     *
     * @param background  the color to set as new background
     *
     * @see JComponent#setBackground(Color)
     */
    public B background(Color background) {
        getPanel().setBackground(background);
        opaque(true);
        return (B) this;
    }


    /**
     * Sets the panel's border.
     *
     * @param border    the border to set
     *
     * @see JComponent#setBorder(Border)
     */
    public B border(Border border) {
        getPanel().setBorder(border);
        return (B) this;
    }


    /**
     * Sets the panel's border as an EmptyBorder using the given specification
     * for the top, left, bottom, right in DLU. For example
     * "1dlu, 2dlu, 3dlu, 4dlu" sets an empty border with 1dlu in the top,
     * 2dlu in the left side, 3dlu at the bottom, and 4dlu in the right hand
     * side.<p>
     *
     * Equivalent to {@code padding(Paddings.createPadding(paddingSpec))}.
     *
     * @param paddingSpec   describes the top, left, bottom, right sizes
     *    of the EmptyBorder to create
     *
     * @see Paddings#createPadding(String, Object...)
     *
     * @since 1.6
     * 
     * @deprecated Replaced by {@link #padding(String, Object...)}
     */
    @Deprecated
    public B border(String paddingSpec) {
        padding(Paddings.createPadding(paddingSpec));
        return (B) this;
    }


    /**
     * Sets a padding around this builder's panel.
     *
     * @param padding    the empty border to set
     *
     * @see JComponent#setBorder(Border)
     * 
     * @since 1.9
     */
    public B padding(EmptyBorder padding) {
        getPanel().setBorder(padding);
        return (B) this;
    }


    /**
     * Sets the panel's padding as an EmptyBorder using the given specification
     * for the top, left, bottom, right margins in DLU. For example
     * "1dlu, 2dlu, 3dlu, 4dlu" sets an empty border with 1dlu in the top,
     * 2dlu in the left side, 3dlu at the bottom, and 4dlu in the right hand
     * side.<p>
     *
     * Equivalent to {@code setPadding(Paddings.createPadding(paddingSpec))}.
     *
     * @param paddingSpec   describes the top, left, bottom, right margins
     *    of the padding (an EmptyBorder) to use
     * @param args          optional format arguments,
     *  used if {@code paddingSpec} is a format string
     * @return a reference to this builder
     *
     * @see #padding(EmptyBorder)
     * @see Paddings#createPadding(String, Object...)
     * 
     * @since 1.9
     */
    public B padding(String paddingSpec, Object... args) {
        padding(Paddings.createPadding(paddingSpec, args));
        return (B) this;
    }


    /**
     * Sets the panel's opaque state.
     *
     * @param b   true for opaque, false for non-opaque
     *
     * @see JComponent#setOpaque(boolean)
     */
    public B opaque(boolean b) {
        getPanel().setOpaque(b);
        return (B) this;
    }


    // Factory Access and Setup ***********************************************
    
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
