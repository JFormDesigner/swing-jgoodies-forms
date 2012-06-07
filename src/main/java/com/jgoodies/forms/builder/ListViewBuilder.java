/*
 * Copyright (c) 2002-2012 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.forms.builder;

import static com.jgoodies.common.base.Preconditions.checkNotNull;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.Border;

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
 * return new ListViewBuilder()
 *     .label("&Contacts:")
 *     .listView(contactsTable)
 *     .listBar(newButton, editButton, deleteButton)
 *     .build();
 *
 * return new ListViewBuilder()
 *     .border(Borders.DLU14)
 *     .labelView(contactsLabel)
 *     .filterView(contactsSearchField)
 *     .listView(contactsTable)
 *     .listBar(newButton, editButton, deleteButton, null, printButton)
 *     .detailsView(contactDetailsView)
 *     .build();
 * </pre>
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.2 $
 * 
 * @since 1.6
 */
public final class ListViewBuilder {
	
	private final ComponentFactory factory;

    private JComponent labelView;
    private JComponent filterView;
    private JComponent listView;
    private JComponent listBarView;
    private JComponent listExtrasView;
    private JComponent detailsView;
    
    private Border border;
    private String filterViewColSpec = "[100dlu, p]";
    private String listViewRowSpec   = "fill:100dlu:grow";

    /**
     * Holds the panel that has been lazily built in {@code #buildPanel}.
     */
    private JComponent panel;
    
    
    // Instance Creation ******************************************************
    
    /**
     * Constructs a ListViewBuilder using the AbstractBuilder's
     * default component factory. The factory is required by
     * {@link #label(String)} and {@link #headerLabel(String)}.
     */
    public ListViewBuilder() {
    	this(AbstractBuilder.getComponentFactoryDefault());
    }
    
    
    /**
     * Constructs a ListViewBuilder using the given component factory. 
     * The factory is required by
     * {@link #label(String)} and {@link #headerLabel(String)}.
     * 
     * @param factory   the component factory used to create labels and headers
     */
    public ListViewBuilder(ComponentFactory factory) {
    	this.factory = factory;
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
     * Sets the mandatory label view. Useful to set a bound label that updates
     * its text when the list content changes, for example to provide the
     * number of list elements.
     * 
     * @param labelView   the component that shall label the list view,
     *    often a bound label
     */
    public ListViewBuilder labelView(JComponent labelView) {
        this.labelView = labelView;
        invalidatePanel();
        return this;
    }


	/**
     * Creates a plain label for the given marked text and sets it as label view.
     * Equivalent to:
     * <pre>
     * labelView(aComponentFactory.createLabel(markedText));
     * </pre>
     * 
     * @param markedText   the label's text, may contain a mnemonic marker
     */
    public ListViewBuilder label(String markedText) {
        labelView(factory.createLabel(markedText));
        return this;
    }


    /**
     * Creates a header label for the given marked text and sets it as label view.
     * Equivalent to:
     * <pre>
     * labelView(aComponentFactory.createHeaderLabel(markedText));
     * </pre>
     * 
     * @param markedText   the label's text, may contain a mnemonic marker
     */
    public ListViewBuilder headerLabel(String markedText) {
        labelView(factory.createHeaderLabel(markedText));
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
     */
    public ListViewBuilder filterViewColSpec(String colSpec) {
    	checkNotNull(colSpec, "The filter view column specification must not be null.");
    	this.filterViewColSpec = colSpec;
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
     */
    public ListViewBuilder listViewRowSpec(String rowSpec) {
    	checkNotNull(rowSpec, "The list view row specification must not be null.");
    	this.listViewRowSpec = rowSpec;
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
    
    private void invalidatePanel() {
    	panel = null;
    }

    private JComponent buildPanel() {
    	checkNotNull(labelView, "The label must be set before #getPanel is invoked.");
    	checkNotNull(listView,  "The list view must be set before #getPanel is invoked.");
    	String columnSpec = filterView != null
    			? "default:grow, 9dlu, " + filterViewColSpec
    		    : "default:grow, 0, 0";
        FormLayout layout = new FormLayout(
                columnSpec,
                "[14dlu,p], $lcg, " + listViewRowSpec + ", p, p");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.border(border);
        builder.add(labelView,                   CC.xy (1, 1));
        builder.add(listView,   			     CC.xyw(1, 3, 3));
        if (filterView != null) {
            builder.add(filterView,              CC.xy (3, 1));
        }
        if (listBarView != null || listExtrasView != null) {
            builder.add(buildDecoratedListBarAndExtras(), CC.xyw(1, 4, 3));
        }
        if (detailsView != null) {
            builder.add(buildDecoratedDetailsView(), CC.xyw(1, 5, 3));
        }

        return builder.build();
    }


    private JComponent buildDecoratedListBarAndExtras() {
        FormLayout layout = new FormLayout(
                "left:default, 9dlu:grow, right:pref",
                "$rgap, p");
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
        PanelBuilder builder = new PanelBuilder(layout);
        builder.add(detailsView, CC.xy(1, 2));
        return builder.build();
    }

    
}
