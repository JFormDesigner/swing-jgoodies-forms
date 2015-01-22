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

import static com.jgoodies.common.base.Preconditions.checkNotNull;
import static com.jgoodies.common.base.Preconditions.checkState;
import static com.jgoodies.common.internal.Messages.MUST_NOT_BE_BLANK;
import static com.jgoodies.common.internal.Messages.MUST_NOT_BE_NULL;

import java.awt.Component;
import java.awt.FocusTraversalPolicy;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.jgoodies.common.base.Strings;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.Forms;
import com.jgoodies.forms.internal.InternalFocusSetupUtils;
import com.jgoodies.forms.util.FocusTraversalType;

/**
 * Builds list/table views from a set of mandatory and optional components:
 * label, filter/search,
 * list (table),
 * list buttons, list extras,
 * details view (or preview).<p>
 *
 * <strong>Examples:</strong><pre>
 * return ListViewBuilder.create()
 *     .labelText("&Contacts:")
 *     .listView(contactsTable)
 *     .listBar(newButton, editButton, deleteButton)
 *     .build();
 *
 * return ListViewBuilder.create()
 *     .padding(Paddings.DLU14)
 *     .label(contactsLabel)
 *     .filterView(contactsSearchField)
 *     .listView(contactsTable)
 *     .listBar(newButton, editButton, deleteButton, null, printButton)
 *     .detailsView(contactDetailsView)
 *     .build();
 * </pre>
 * For more examples see the JGoodies Showcase application.
 *
 * @author  Karsten Lentzsch
 *
 * @since 1.9
 */
public final class ListViewBuilder {

	private ComponentFactory factory;

    private JComponent label;
    private JComponent filterView;
    private JComponent listView;
    private JComponent listBarView;
    private JComponent listExtrasView;
    private JComponent detailsView;
    private JComponent listStackView;

    private Border border;
    private boolean honorsVisibility = true;
    private Component initialComponent;
    private FocusTraversalType focusTraversalType;
    private FocusTraversalPolicy focusTraversalPolicy;
    private String namePrefix        = "ListView";
    private String filterViewColSpec = "[100dlu, p]";
    private String listViewRowSpec   = "fill:[100dlu, d]:grow";

    /**
     * Holds the panel that has been lazily built in {@code #buildPanel}.
     */
    private JComponent panel;


    // Instance Creation ******************************************************

    /**
     * Constructs a ListViewBuilder using the global default component factory.
     * The factory is required by {@link #labelText(String, Object...)} and
     * {@link #headerText(String, Object...)}.
     */
    private ListViewBuilder() {
    	// Do nothing.
    }


    /**
     * Creates and returns a ListViewBuilder using the global default
     * component factory.
     * The factory is required by {@link #labelText(String, Object...)} and
     * {@link #headerText(String, Object...)}.
     * 
     * @return the ListViewBuilder
     */
    public static ListViewBuilder create() {
        return new ListViewBuilder();
    }


    // API ********************************************************************

    /**
     * Sets an optional border that surrounds the list view including
     * the label and details.
     *
     * @param border   the border to set
     */
    public ListViewBuilder border(Border border) {
        this.border = border;
        invalidatePanel();
        return this;
    }

    
    /**
     * Sets an optional padding (an empty border) that surrounds the list view
     * including the label and details.
     *
     * @param padding   the white space to use around the list view panel
     * 
     * @since 1.9
     */
    public ListViewBuilder padding(EmptyBorder padding) {
    	return border(padding);
    }
    
    
    /**
     * Sets the component that shall receive the focus if this panel's
     * parent is made visible the first time.
     * 
     * @param initialComponent   the component that shall receive the focus
     *    if the panel is made visible the first time
     * @return a reference to this builder
     * 
     * @see #focusTraversalType(FocusTraversalType)
     */
    public ListViewBuilder initialComponent(JComponent initialComponent) {
        checkNotNull(initialComponent, MUST_NOT_BE_NULL, "initial component");
        checkState(this.initialComponent == null,
                "The initial component must be set once only.");
        checkValidFocusTraversalSetup();
        this.initialComponent = initialComponent;
        return this;
    }
    
    
    /**
     * 
     * @param focusTraversalType   either: layout or container order
     * @return a reference to this builder
     * 
     * @see #initialComponent(JComponent)
     */
    public ListViewBuilder focusTraversalType(FocusTraversalType focusTraversalType) {
        checkNotNull(focusTraversalType, MUST_NOT_BE_NULL, "focus traversal type");
        checkState(this.focusTraversalType == null,
                "The focus traversal type must be set once only.");
        checkValidFocusTraversalSetup();
        this.focusTraversalType = focusTraversalType;
        return this;
    }
    
    
    /**
     * Sets the panel's focus traversal policy and sets the panel
     * as focus traversal policy provider. Hence, this call is equivalent to:
     * <pre>
     * builder.getPanel().setFocusTraversalPolicy(policy);
     * builder.getPanel().setFocusTraversalPolicyProvider(true);
     * </pre>
     *
     * @param policy   the focus traversal policy that will manage
     *  keyboard traversal of the children in this builder's panel
     * @return a reference to this builder
     *
     * @see JComponent#setFocusTraversalPolicy(FocusTraversalPolicy)
     * @see JComponent#setFocusTraversalPolicyProvider(boolean)
     */
    public ListViewBuilder focusTraversalPolicy(FocusTraversalPolicy policy) {
        checkNotNull(policy, MUST_NOT_BE_NULL, "focus traversal policy");
        checkState(this.focusTraversalPolicy == null,
                "The focus traversal policy must be set once only.");
        checkValidFocusTraversalSetup();
        this.focusTraversalPolicy = policy;
        return this;
    }
    
    
    /**
     * Specifies whether invisible components shall be taken into account by
     * this builder for computing the layout size and setting component bounds.
     * If set to {@code true} invisible components will be ignored by
     * the layout. If set to {@code false} components will be taken into
     * account regardless of their visibility. Visible components are always
     * used for sizing and positioning.<p>
     *
     * The default value for this setting is {@code true}.
     * It is useful to set the value to {@code false} (in other words
     * to ignore the visibility) if you switch the component visibility
     * dynamically and want the container to retain the size and
     * component positions.<p>
     * 
     * A typical use case for ignoring the visibility is here:
     * if the list selection is empty, the details view is made invisible
     * to hide the then obsolete read-only labels. If visibility is honored,
     * the list view would grow and shrink on list selection. If ignored,
     * the layout remains stable.
     *
     * @param b   {@code true} to honor the visibility, i.e. to exclude
     *    invisible components from the sizing and positioning,
     *    {@code false} to ignore the visibility, in other words to
     *    layout visible and invisible components
     */
    public ListViewBuilder honorVisibility(boolean b) {
    	this.honorsVisibility = b;
    	invalidatePanel();
    	return this;
    }
    
    
    /**
     * Sets the prefix that is prepended to the component name of components
     * that have no name set or that are are implicitly created by this builder,
     * e.g. the (header) label. The default name prefix is "ListView".

     * @param namePrefix   the prefix to be used
     * @return a reference to this builder
     */
    public ListViewBuilder namePrefix(String namePrefix) {
    	this.namePrefix = namePrefix;
    	return this;
    }
    
    
    /**
     * Sets {@code factory} as this builder's new component factory
     * that is used to create the label or header components.
     * If not called, the default factory will be used
     * that can be configured via
     * {@link FormsSetup#setComponentFactoryDefault(ComponentFactory)}.
     * 
     * @param factory    the factory to be used to create the header or label
     * @return a reference to this builder
     */
    public ListViewBuilder factory(ComponentFactory factory) {
        this.factory = factory;
        return this;
    }


    /**
     * Sets the mandatory label view. Useful to set a bound label that updates
     * its text when the list content changes, for example to provide the
     * number of list elements.
     *
     * @param labelView   the component that shall label the list view,
     *    often a bound label
     * @return a reference to this builder
     */
    public ListViewBuilder label(JComponent labelView) {
        this.label = labelView;
        overrideNameIfBlank(labelView, "label");
        invalidatePanel();
        return this;
    }


	/**
     * Creates a plain label for the given marked text and sets it as label view.
     * If no arguments are provided, the plain String is used.
     * Otherwise the string will be formatted using {@code String.format}
     * with the given arguments.
     * Equivalent to:
     * <pre>
     * label(aComponentFactory.createLabel(Strings.get(markedText, args)));
     * </pre>
     *
     * @param markedText   the label's text, may contain a mnemonic marker
     * @param args  optional format arguments forwarded to {@code String#format}
     * @return a reference to this builder
     * 
     * @see String#format(String, Object...)
     */
    public ListViewBuilder labelText(String markedText, Object... args) {
        label(getFactory().createLabel(Strings.get(markedText, args)));
        return this;
    }


    /**
     * Creates a header label for the given marked text and sets it as label view.
     * If no arguments are provided, the plain String is used.
     * Otherwise the string will be formatted using {@code String.format}
     * with the given arguments.
     * Equivalent to:
     * <pre>
     * labelView(aComponentFactory.createHeaderLabel(Strings.get(markedText, args)));
     * </pre>
     *
     * @param markedText   the label's text, may contain a mnemonic marker
     * @param args  optional format arguments forwarded to {@code String#format}
     * @return a reference to this builder
     * 
     * @see String#format(String, Object...)
     */
    public ListViewBuilder headerText(String markedText, Object... args) {
        label(getFactory().createHeaderLabel(Strings.get(markedText, args)));
        return this;
    }


    /**
     * Sets an optional view that will be placed in the upper right corner
     * of the built list view panel. This can be a search field, a panel
     * with filtering check boxes ("Only valid items"), etc.
     *
     * @param filterView    the view to be added.
     * @return a reference to this builder
     */
    public ListViewBuilder filterView(JComponent filterView) {
        this.filterView = filterView;
        overrideNameIfBlank(filterView, "filter");
        invalidatePanel();
        return this;
    }


    /**
     * Changes the FormLayout column specification used to lay out
     * the filter view.
     * The default value is {@code "[100dlu, p]"}, which is a column where
     * the width is determined by the filter view's preferred width,
     * but a minimum width of 100dlu is ensured. The filter view won't grow
     * horizontally, if the container gets more space.
     *
     * @param colSpec   specifies the horizontal layout of the filter view
     * @param args   optional {@code colSpec} format arguments
     *     forwarded to {@code String#format}
     * @return a reference to this builder
     *
     * @throws NullPointerException if {@code colSpec} is {@code null}
     */
    public ListViewBuilder filterViewColumn(String colSpec, Object... args) {
    	checkNotNull(colSpec, MUST_NOT_BE_BLANK, "filter view column specification");
    	this.filterViewColSpec = Strings.get(colSpec, args);
        invalidatePanel();
        return this;
    }


    /**
     * Sets the given component as the the mandatory list view.
     * If {@code listView} is a JTable, JList, or JTree, it is
     * automatically wrapped with a JScrollPane, before the scroll pane
     * is set as list view.
     *
     * @param listView   the component to be used as scrollable list view
     * @return a reference to this builder
     *
     * @throws NullPointerException if {@code listView} is {@code null}
     */
    public ListViewBuilder listView(JComponent listView) {
    	checkNotNull(listView, MUST_NOT_BE_BLANK, "list view");
    	if (listView instanceof JTable || listView instanceof JList || listView instanceof JTree) {
    		this.listView = new JScrollPane(listView);
    	} else {
    		this.listView = listView;
    	}
    	overrideNameIfBlank(listView, "listView");
        invalidatePanel();
        return this;
    }


    /**
     * Changes the FormLayout row specification used to lay out the list view.
     * The default value is {@code "fill:[100dlu, pref]:grow"}, which is a row
     * that is filled by the list view; the height is determined
     * by the list view's preferred height, but a minimum of 100dlu is ensured.
     * The list view grows vertically, if the container gets more vertical
     * space.<p>
     *
     * <strong>Examples:</strong>
     * <pre>
     * .listViewRow("fill:100dlu");  // fixed height
     * .listViewRow("f:100dlu:g");   // fixed start height, grows
     * .listViewRow("f:p");          // no minimum height
     * </pre>
     *
     * @param rowSpec   specifies the vertical layout of the list view
     * @param args   optional {@code rowSpec} format arguments
     *     forwarded to {@code String#format}
     * @return a reference to this builder
     *
     * @throws NullPointerException if {@code rowSpec} is {@code null}
     */
    public ListViewBuilder listViewRow(String rowSpec, Object... args) {
    	checkNotNull(rowSpec, MUST_NOT_BE_BLANK, "list view row specification");
    	this.listViewRowSpec = Strings.get(rowSpec, args);
        invalidatePanel();
        return this;
    }


    /**
     * Sets an optional list bar - often a button bar -
     * that will be located in the lower left corner of the list view.
     * If the list bar view consists only of buttons,
     * use {@link #listBar(JComponent...)} instead.
     *
     * @param listBarView   the component to set
     * @return a reference to this builder
     */
    public ListViewBuilder listBarView(JComponent listBarView) {
        this.listBarView = listBarView;
        overrideNameIfBlank(listBarView, "listBarView");
        invalidatePanel();
        return this;
    }


    /**
     * Builds a button bar using the given buttons and sets it as list bar.
     * Although JButtons are expected, any JComponent is accepted
     * to allow custom button component types.<p>
     *
     * Equivalent to {@code listBarView(Forms.buttonBar(buttons))}.
     *
     * @param buttons    the buttons in the list bar
     * @return a reference to this builder
     *
     * @throws NullPointerException if {@code buttons} is {@code null}
     * @throws IllegalArgumentException if no buttons are provided
     *
     * @see Forms#buttonBar(JComponent...)
     */
    public ListViewBuilder listBar(JComponent... buttons) {
        listBarView(Forms.buttonBar(buttons));
        return this;
    }


    /**
     * Sets an optional list stack - often a stack of buttons -
     * that will be located on the right-hand side of the list view.
     * If the list stack view consists only of buttons,
     * use {@link #listStack(JComponent...)} instead.
     *
     * @param listStackView   the component to set
     * @return a reference to this builder
     */
    public ListViewBuilder listStackView(JComponent listStackView) {
        this.listStackView = listStackView;
        overrideNameIfBlank(listStackView, "listStackView");
        invalidatePanel();
        return this;
    }


    /**
     * Builds a button stack using the given buttons and sets it as list stack.
     * Although JButtons are expected, any JComponent is accepted
     * to allow custom button component types.<p>
     *
     * Equivalent to {@code listStackView(Forms.buttonStack(buttons))}.
     *
     * @param buttons    the buttons in the list stack
     * @return a reference to this builder
     *
     * @throws NullPointerException if {@code buttons} is {@code null}
     * @throws IllegalArgumentException if no buttons are provided
     *
     * @see Forms#buttonStack(JComponent...)
     */
    public ListViewBuilder listStack(JComponent... buttons) {
        listStackView(Forms.buttonStack(buttons));
        return this;
    }


    /**
     * Sets an optional view that is located in the lower right corner
     * of the list view, aligned with the list bar.
     *
     * @param listExtrasView    the component to set
     * @return a reference to this builder
     */
    public ListViewBuilder listExtrasView(JComponent listExtrasView) {
        this.listExtrasView = listExtrasView;
        overrideNameIfBlank(listExtrasView, "listExtrasView");
        invalidatePanel();
        return this;
    }


    /**
     * Sets an optional details view that is located under the list view.
     * Often this is the details view or preview of a master-details view.
     *
     * @param detailsView    the component to set
     * @return a reference to this builder
     */
    public ListViewBuilder detailsView(JComponent detailsView) {
        this.detailsView = detailsView;
        overrideNameIfBlank(detailsView, "detailsView");
        invalidatePanel();
        return this;
    }


    /**
     * Lazily builds and returns the list view panel.
     *
     * @return the built panel
     */
    public JComponent build() {
        if (panel == null) {
            panel = buildPanel();
        }
        return panel;
    }


    // Implementation *********************************************************
    
    private ComponentFactory getFactory() {
        if (factory == null) {
            factory = FormsSetup.getComponentFactoryDefault();
        }
        return factory;
    }
    

    private void invalidatePanel() {
    	panel = null;
    }

    
    private JComponent buildPanel() {
    	checkNotNull(listView, "The list view must be set before #build is invoked.");
    	String stackGap = hasStack() ? "$rg" : "0";
    	String detailsGap = hasDetails() ? "14dlu" : "0";
        FormBuilder builder = FormBuilder.create()
                .columns("fill:default:grow, %s, p", stackGap)
                .rows("p, %1$s, p, %2$s, p", listViewRowSpec, detailsGap)
                .honorsVisibility(honorsVisibility)
                .border(border)
                .add(hasHeader(),     buildHeader())    .xy(1, 1)
                .add(true,            listView)   	    .xy(1, 2)
                .add(hasOperations(), buildOperations()).xy(1, 3)
                .add(hasStack(),      listStackView)    .xy(3, 2)
                .add(hasDetails(),    detailsView)      .xy(1, 5);

        // Set up the label-for relation - if not already set.
        if (label instanceof JLabel) {
            JLabel theLabel = (JLabel) label;
            if (theLabel.getLabelFor() == null) {
                theLabel.setLabelFor(listView);
            }
        }
        InternalFocusSetupUtils.setupFocusTraversalPolicyAndProvider(
                builder.getPanel(),
                focusTraversalPolicy,
                focusTraversalType,
                initialComponent);
        return builder.build();
    }
    
    
    private JComponent buildHeader() {
        if (!hasHeader()) {
            return null;
        }
        String columnSpec = hasFilter()
                ? "default:grow, 9dlu, %s"
                : "default:grow, 0,    0";
        return FormBuilder.create()
            .columns(columnSpec, filterViewColSpec)
            .rows("[14dlu, p], $lcg")
            .labelForFeatureEnabled(false)
            .add(hasLabel(),  label)     .xy(1, 1)
            .add(hasFilter(), filterView).xy(3, 1)
            .build();
    }


    private JComponent buildOperations() {
        if (!hasOperations()) {
            return null;
        }
        String gap =  hasListExtras() ? "9dlu" : "0";
        return FormBuilder.create()
            .columns("left:default, %s:grow, right:pref", gap)
            .rows("$rgap, p")
            .honorsVisibility(honorsVisibility)
            .add(hasListBar(),    listBarView)   .xy(1, 2)
            .add(hasListExtras(), listExtrasView).xy(3, 2)
            .build();
    }


    // Helper Code ************************************************************
    
    private boolean hasLabel() {
        return label != null;
    }
    
    
    private boolean hasFilter() {
        return filterView != null;
    }
    
    
    private boolean hasHeader() {
        return hasLabel() || hasFilter();
    }
    
    
    private boolean hasListBar() {
        return listBarView != null;
    }
    
    
    private boolean hasListExtras() {
        return listExtrasView != null;
    }
    
    
    private boolean hasOperations() {
        return hasListBar() || hasListExtras();
    }
    
    
    private boolean hasStack() {
        return listStackView != null;
    }
    
    
    private boolean hasDetails() {
        return detailsView != null;
    }
    
    
    private void overrideNameIfBlank(JComponent component, String suffix) {
        if (component != null && Strings.isBlank(component.getName())) {
    	    component.setName(namePrefix + '.' + suffix);
    	}
    }


    /**
     * Checks that if the API user has set a focus traversal policy,
     * no focus traversal type and no initial component has been set.
     */
    private void checkValidFocusTraversalSetup() {
        InternalFocusSetupUtils.checkValidFocusTraversalSetup(
                focusTraversalPolicy, focusTraversalType, initialComponent);
    }
    
    
}
