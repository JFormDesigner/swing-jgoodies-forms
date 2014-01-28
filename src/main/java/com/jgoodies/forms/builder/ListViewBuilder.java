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
import static com.jgoodies.common.base.Preconditions.checkState;

import java.awt.FocusTraversalPolicy;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.Border;

import com.jgoodies.common.base.Strings;
import com.jgoodies.common.internal.ResourceBundleAccessor;
import com.jgoodies.common.internal.StringResourceAccessor;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.Forms;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Builds list/table views from a set of mandatory and optional components:
 * label, filter/search,
 * list (table),
 * list buttons, list extras,
 * details view (or preview).<p>
 *
 * <strong>Examples:</strong><pre>
 * return ListViewBuilder.create()
 *     .label("&Contacts:")
 *     .listView(contactsTable)
 *     .listBar(newButton, editButton, deleteButton)
 *     .build();
 *
 * return ListViewBuilder.create()
 *     .border(Borders.DLU14)
 *     .labelView(contactsLabel)
 *     .filterView(contactsSearchField)
 *     .listView(contactsTable)
 *     .listBar(newButton, editButton, deleteButton, null, printButton)
 *     .detailsView(contactDetailsView)
 *     .build();
 * </pre>
 * For more examples see the JGoodies Showcase application.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.2 $
 *
 * @since 1.6
 */
public final class ListViewBuilder {

	private ComponentFactory factory;

    private StringResourceAccessor resources;

    private JComponent labelView;
    private JComponent filterView;
    private JComponent listView;
    private JComponent listBarView;
    private JComponent listExtrasView;
    private JComponent detailsView;

    private Border border;
    private boolean honorsVisibility = true;
    private FocusTraversalPolicy focusTraversalPolicy;
    private String namePrefix        = "ListView";
    private String filterViewColSpec = "[100dlu, p]";
    private String listViewRowSpec   = "fill:100dlu:grow";

    /**
     * Holds the panel that has been lazily built in {@code #buildPanel}.
     */
    private JComponent panel;


    // Instance Creation ******************************************************

    /**
     * Constructs a ListViewBuilder using the global default component factory.
     * The factory is required by {@link #label(String, Object...)} and
     * {@link #headerLabel(String, Object...)}.
     */
    public ListViewBuilder() {
    	// Do nothing.
    }


    /**
     * Constructs a ListViewBuilder using the given component factory.
     * The factory is required by
     * {@link #label(String, Object...)} and {@link #headerLabel(String, Object...)}.
     *
     * @param factory   the component factory used to create labels and headers
     */
    public ListViewBuilder(ComponentFactory factory) {
    	this.factory = factory;
    }
    
    
    /**
     * Creates and returns a ListViewBuilder using the global default
     * component factory.
     * The factory is required by {@link #label(String, Object...)} and
     * {@link #headerLabel(String, Object...)}.
     * 
     * @return the ListViewBuilder
     * 
     * @since 1.8
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
     * Sets the panel's focus traversal policy and sets the panel
     * as focus traversal policy provider. Hence, this call is equivalent to:
     * <pre>
     * builder.getPanel().setFocusTraversalPolicy(policy);
     * builder.getPanel().setFocusTraversalPolicyProvider(true);
     * </pre>
     *
     * @param policy   the focus traversal policy that will manage
     *  keyboard traversal of the children in this builder's panel
     *
     * @see JComponent#setFocusTraversalPolicy(FocusTraversalPolicy)
     * @see JComponent#setFocusTraversalPolicyProvider(boolean)
     *
     * @since 1.7.2
     * @deprecated Use {@link #focusTraversalPolicy(FocusTraversalPolicy)} instead
     */
    @Deprecated
    public ListViewBuilder focusTraversal(FocusTraversalPolicy policy) {
        return focusTraversalPolicy(policy);
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
     *
     * @see JComponent#setFocusTraversalPolicy(FocusTraversalPolicy)
     * @see JComponent#setFocusTraversalPolicyProvider(boolean)
     *
     * @since 1.8
     */
    public ListViewBuilder focusTraversalPolicy(FocusTraversalPolicy policy) {
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
     * component positions.
     *
     * @param b   {@code true} to honor the visibility, i.e. to exclude
     *    invisible components from the sizing and positioning,
     *    {@code false} to ignore the visibility, in other words to
     *    layout visible and invisible components
     *
     * @since 1.7.1
     */
    public ListViewBuilder honorVisibility(boolean b) {
    	this.honorsVisibility = b;
    	invalidatePanel();
    	return this;
    }
    
    
    /**
     * Sets the prefix that is prepended to component name of components
     * that have no name set or that are are implicitly created by this builder,
     * e.g. the (header) label. The default name prefix is "ListView".

     * @param namePrefix   the prefix to be used
     * 
     * @since 1.7.1
     */
    public ListViewBuilder namePrefix(String namePrefix) {
    	this.namePrefix = namePrefix;
    	return this;
    }
    
    
    public ListViewBuilder factory(ComponentFactory factory) {
        this.factory = factory;
        return this;
    }


    /**
     * Sets the accessor that is used to localize label and header texts.
     * @param resources    maps resource keys to Strings
     * @return a reference to this builder
     * 
     * @since 1.8
     */
    public ListViewBuilder resources(StringResourceAccessor resources) {
        this.resources = resources;
        return this;
    }


    /**
     * Sets the accessor that is used to localize label and header texts.
     * @param bundle    maps resource keys to Strings
     * @return a reference to this builder
     * 
     * @since 1.8
     */
    public ListViewBuilder resources(ResourceBundle bundle) {
        this.resources = new ResourceBundleAccessor(bundle);
        return this;
    }
    
    
    /**
     * Sets the mandatory label view. Useful to set a bound label that updates
     * its text when the list content changes, for example to provide the
     * number of list elements.
     *
     * @param labelView   the component that shall label the list view,
     *    often a bound label
     */
    public ListViewBuilder labelView(JComponent labelView) {
        this.labelView = labelView;
        setName(labelView, "label");
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
     * labelView(aComponentFactory.createLabel(Strings.get(markedText, args)));
     * </pre>
     *
     * @param markedText   the label's text, may contain a mnemonic marker
     * @param args  optional format arguments forwarded to {@code String#format}
     * 
     * @see String#format(String, Object...)
     */
    public ListViewBuilder label(String markedText, Object... args) {
        labelView(getFactory().createLabel(Strings.get(markedText, args)));
        return this;
    }


    /**
     * Looks up the String associated with the given resource key
     * and creates a plain label for it and sets it as label view.
     * If no arguments are provided, the plain String resource is used.
     * Otherwise the string will be formatted using {@code String.format}
     * with the given arguments.
     *
     * @param key   the key used to look up the label text resource
     * @param args  optional format arguments forwarded to {@code String#format}
     * 
     * @see String#format(String, Object...)
     * 
     * @since 1.8
     */
    public ListViewBuilder labelKey(String key, Object... args) {
        label(getResourceString(key, args));
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
     * 
     * @see String#format(String, Object...)
     */
    public ListViewBuilder headerLabel(String markedText, Object... args) {
        labelView(getFactory().createHeaderLabel(Strings.get(markedText, args)));
        return this;
    }


    /**
     * Looks up the String associated with the given resource key
     * and creates a header label for it and sets it as label view.
     * If no arguments are provided, the plain String resource is used.
     * Otherwise the string will be formatted using {@code String.format}
     * with the given arguments.
     *
     * @param key   the key used to look up the header label text resource
     * @param args  optional format arguments forwarded to {@code String#format}
     * 
     * @see String#format(String, Object...)
     * 
     * @since 1.8
     */
    public ListViewBuilder headerLabelKey(String key, Object... args) {
        headerLabel(getResourceString(key, args));
        return this;
    }


    /**
     * Sets an optional view that will be placed in the upper right corner
     * of the built list view panel. This can be a search field, a panel
     * with filtering check boxes ("Only valid items"), etc.
     *
     * @param filterView    the view to be added.
     */
    public ListViewBuilder filterView(JComponent filterView) {
        this.filterView = filterView;
        setName(filterView, "filter");
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
     *
     * @throws NullPointerException if {@code colSpec} is {@code null}
     * @deprecated Use {@link #filterViewColumn(String, Object...)} instead
     */
    @Deprecated
    public ListViewBuilder filterViewColSpec(String colSpec) {
        return filterViewColumn(colSpec);
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
     *
     * @throws NullPointerException if {@code colSpec} is {@code null}
     */
    public ListViewBuilder filterViewColumn(String colSpec, Object... args) {
    	checkNotNull(colSpec, "The filter view column specification must not be null.");
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
     *
     * @throws NullPointerException if {@code listView} is {@code null}
     */
    public ListViewBuilder listView(JComponent listView) {
    	checkNotNull(listView, "The list view must not be null.");
    	if (listView instanceof JTable || listView instanceof JList || listView instanceof JTree) {
    		this.listView = new JScrollPane(listView);
    	} else {
    		this.listView = listView;
    	}
    	setName(listView, "listView");
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
     * .listViewRowSpec("fill:100dlu");  // fixed height
     * .listViewRowSpec("f:100dlu:g");   // fixed start height, grows
     * .listViewRowSpec("f:p");          // no minimum height
     * </pre>
     *
     * @param rowSpec   specifies the vertical layout of the list view
     *
     * @throws NullPointerException if {@code rowSpec} is {@code null}
     * @deprecated Use {@link #listViewRow(String, Object...)} instead
     */
    @Deprecated
    public ListViewBuilder listViewRowSpec(String rowSpec) {
        return listViewRow(rowSpec);
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
     *
     * @throws NullPointerException if {@code rowSpec} is {@code null}
     */
    public ListViewBuilder listViewRow(String rowSpec, Object... args) {
    	checkNotNull(rowSpec, "The list view row specification must not be null.");
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
     */
    public ListViewBuilder listBarView(JComponent listBarView) {
        this.listBarView = listBarView;
        setName(listBarView, "listBarView");
        invalidatePanel();
        return this;
    }


    /**
     * Builds a button bar using the given buttons and sets it as list bar.
     * Although JButtons are expected, any JComponent is accepted
     * to allow custom button component types.<p>
     *
     * Equivalent to {@code listBarView(Forms.buildButtonBar(buttons))}.
     *
     * @param buttons    the buttons in the list bar
     *
     * @throws NullPointerException if {@code buttons} is {@code null}
     * @throws IllegalArgumentException if no buttons are provided
     *
     * @see ButtonBarBuilder#addButton(JComponent...)
     */
    public ListViewBuilder listBar(JComponent... buttons) {
        listBarView(Forms.buttonBar(buttons));
        return this;
    }


    /**
     * Sets an optional view that is located in the lower right corner
     * of the list view, aligned with the list bar.
     *
     * @param listExtrasView    the component to set
     */
    public ListViewBuilder listExtrasView(JComponent listExtrasView) {
        this.listExtrasView = listExtrasView;
        setName(listExtrasView, "listExtrasView");
        invalidatePanel();
        return this;
    }


    /**
     * Sets an optional details view that is located under the list view.
     * Often this is the details view or preview of a master-details view.
     *
     * @param detailsView    the component to set
     */
    public ListViewBuilder detailsView(JComponent detailsView) {
        this.detailsView = detailsView;
        setName(detailsView, "detailsView");
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
    	checkNotNull(listView,  "The list view must be set before #build is invoked.");
        FormLayout layout = new FormLayout(
                "fill:default:grow",
                "p, " + listViewRowSpec + ", p, p");
        layout.setHonorsVisibility(honorsVisibility);
        PanelBuilder builder = new PanelBuilder(layout);
        builder.border(border);
        if (focusTraversalPolicy != null) {
            builder.focusTraversal(focusTraversalPolicy);
        }
        if (labelView != null || filterView != null) {
            builder.add(buildDecoratedHeaderView(),       CC.xy(1, 1));
        }
        builder.add(listView,   			              CC.xy(1, 2));
        if (listBarView != null || listExtrasView != null) {
            builder.add(buildDecoratedListBarAndExtras(), CC.xy(1, 3));
        }
        if (detailsView != null) {
            builder.add(buildDecoratedDetailsView(),      CC.xy(1, 4));
        }

        // Set up the label-for relation - if not already set.
        if (labelView instanceof JLabel) {
            JLabel theLabelView = (JLabel) labelView;
            if (theLabelView.getLabelFor() == null) {
                theLabelView.setLabelFor(listView);
            }
        }
        return builder.build();
    }
    
    
    private JComponent buildDecoratedHeaderView() {
        String columnSpec = filterView != null
                ? "default:grow, 9dlu, " + filterViewColSpec
                : "default:grow, 0, 0";
        FormLayout layout = new FormLayout(
                columnSpec,
                "[14dlu,p], $lcg");
        PanelBuilder builder = new PanelBuilder(layout)
            .labelForFeatureEnabled(false);
        if (labelView != null) {
            builder.add(labelView,               CC.xy (1, 1));
        }
        if (filterView != null) {
            builder.add(filterView,              CC.xy (3, 1));
        }
        return builder.build();
    }


    private JComponent buildDecoratedListBarAndExtras() {
        String gap = listBarView != null && listExtrasView != null
                ? "9dlu:grow"
                : "0:grow";
        FormLayout layout = new FormLayout(
                String.format("left:default, %s, right:pref", gap),
                "$rgap, p");
        layout.setHonorsVisibility(honorsVisibility);
        PanelBuilder builder = new PanelBuilder(layout);
        if (listBarView != null) {
            builder.add(listBarView, CC.xy(1, 2));
        }
        if (listExtrasView != null) {
            builder.add(listExtrasView,  CC.xy(3, 2));
        }
        return builder.build();
    }


    private JComponent buildDecoratedDetailsView() {
        FormLayout layout = new FormLayout(
                "fill:default:grow",
                "14, p");
        layout.setHonorsVisibility(honorsVisibility);
        PanelBuilder builder = new PanelBuilder(layout);
        builder.add(detailsView, CC.xy(1, 2));
        return builder.build();
    }
    
    
    // Helper Code ************************************************************
    
    private void setName(JComponent component, String suffix) {
    	if (Strings.isNotBlank(component.getName())) {
    		return;
    	}
    	component.setName(namePrefix + '.' + suffix);
    }


    /**
     * Returns a localized String for the given key and format arguments - if any.
     */
    private String getResourceString(String key, Object... args) {
        checkState(resources != null,
                "To use the internationalization support " +
                "a ResourceBundle, ResourceMap, or a StringResourceAccessor " +
                "must be provided. See ListViewBuilder#resources.");
        try {
            return resources.getString(key, args);
        } catch (MissingResourceException ex) {
            Logger.getLogger(getClass().getName()).log(
                    Level.WARNING, "Missing internationalized label", ex);
            return key;
        }
    }


}
