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

import static com.jgoodies.common.base.Preconditions.checkArgument;
import static com.jgoodies.common.base.Preconditions.checkNotNull;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.Border;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Builds list/table views from a set of mandatory and optional components:
 * label, filter/search, 
 * list (table), 
 * list buttons, list extras,
 * details view (or preview).
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
     * {@link #setLabel(String)} and {@link #setHeaderLabel(String)}.
     */
    public ListViewBuilder() {
    	this(AbstractBuilder.getDefaultComponentFactory());
    }
    
    
    /**
     * Constructs a ListViewBuilder using the given component factory. 
     * The factory is required by
     * {@link #setLabel(String)} and {@link #setHeaderLabel(String)}.
     * 
     * @param factory   the component factory used to create labels and headers
     */
    public ListViewBuilder(ComponentFactory factory) {
    	this.factory = factory;
    }


    // API ********************************************************************

    /**
     * Sets the mandatory label view. Useful to set a bound label that updates
     * its text when the list content changes, for example to provide the
     * number of list elements.
     * 
     * @param labelView   the component that shall label the list view,
     *    often a bound label
     */
    public void setLabelView(JComponent labelView) {
        this.labelView = labelView;
        invalidatePanel();
    }


    /**
     * Creates a plain label for the given marked text and sets it as label view.
     * Equivalent to:
     * <pre>
     * setLabelView(aComponentFactory.createLabel(markedText));
     * </pre>
     * 
     * @param markedText   the label's text, may contain a mnemonic marker
     */
    public void setLabel(String markedText) {
        setLabelView(factory.createLabel(markedText));
    }


    /**
     * Creates a header label for the given marked text and sets it as label view.
     * Equivalent to:
     * <pre>
     * setLabelView(aComponentFactory.createHeaderLabel(markedText));
     * </pre>
     * 
     * @param markedText   the label's text, may contain a mnemonic marker
     */
    public void setHeaderLabel(String markedText) {
        setLabelView(factory.createHeaderLabel(markedText));
    }
    
    
    /**
     * Sets an optional view that will be placed in the upper right corner
     * of the built list view panel. This can be a search field, a panel
     * with filtering check boxes ("Only valid items"), etc.
     * 
     * @param filterView    the view to be added.
     */
    public void setFilterView(JComponent filterView) {
        this.filterView = filterView;
        invalidatePanel();
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
    public void setFilterViewColSpec(String colSpec) {
    	checkNotNull(colSpec, "The filter view column specification must not be null.");
    	this.filterViewColSpec = colSpec;
        invalidatePanel();
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
    public void setListView(JComponent listView) {
    	checkNotNull(listView, "The list view must not be null.");
    	if (listView instanceof JTable || listView instanceof JList || listView instanceof JTree) {
    		this.listView = new JScrollPane(listView);
    	} else {
    		this.listView = listView;
    	}
        invalidatePanel();
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
     * builder.setListViewRowSpec("fill:100dlu");  // fixed height
     * builder.setListViewRowSpec("f:100dlu:g");   // fixed start height, grows
     * builder.setListViewRowSpec("f:p");          // no minimum height
     * </pre>
     * 
     * @param rowSpec   specifies the vertical layout of the list view
     * 
     * @throws NullPointerException if {@code rowSpec} is {@code null}
     */
    public void setListViewRowSpec(String rowSpec) {
    	checkNotNull(rowSpec, "The list view row specification must not be null.");
    	this.listViewRowSpec = rowSpec;
        invalidatePanel();
    }


    /**
     * Sets an optional list bar - often a button bar - 
     * that will be located in the lower left corner of the list view. 
     * If the list bar view consists only of buttons, 
     * use {@link #setListBar(JButton...)} instead.
     * 
     * @param listBarView   the component to set
     */
    public void setListBarView(JComponent listBarView) {
        this.listBarView = listBarView;
        invalidatePanel();
    }


    /**
     * Builds a button bar using the given buttons and sets it as list bar.
     * 
     * @param buttons    the buttons in the list bar
     * 
     * @throws NullPointerException if {@code buttons} is {@code null}
     * @throws IllegalArgumentException if no buttons are provided
     */
    public void setListBar(JButton... buttons) {
        checkNotNull(buttons, "The button array must not be null.");
        checkArgument(buttons.length > 0, "You must provide at least one button.");
        ButtonBarBuilder2 builder = new ButtonBarBuilder2();
        boolean needsGap = false;
        for (JButton button : buttons) {
            if (button == null) {
                builder.addUnrelatedGap();
                needsGap = false;
                continue;
            }
            if (needsGap) {
                builder.addRelatedGap();
            }
            builder.addButton(button);
            needsGap = true;
        }
        setListBarView(builder.getPanel());
    }


    /**
     * Sets an optional view that is located in the lower right corner
     * of the list view, aligned with the list bar.
     * 
     * @param listExtrasView    the component to set
     */
    public void setListExtrasView(JComponent listExtrasView) {
        this.listExtrasView = listExtrasView;
        invalidatePanel();
    }


    /**
     * Sets an optional details view that is located under the list view.
     * Often this is the details view or preview of a master-details view.
     * 
     * @param detailsView    the component to set
     */
    public void setDetailsView(JComponent detailsView) {
        this.detailsView = detailsView;
        invalidatePanel();
    }


    /**
     * Sets an optional border that surrounds the list view including
     * the label and details.
     * 
     * @param border   the border to set
     */
    public void setBorder(Border border) {
        this.border = border;
        invalidatePanel();
    }


    /**
     * Lazily builds and returns the list view panel.
     * 
     * @return the built panel
     */
    public JComponent getPanel() {
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
        FormLayout layout = new FormLayout(
                "default:grow, 9dlu, " + filterViewColSpec,
                "[14dlu,p], $lcg, " + listViewRowSpec + ", p, p");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setOpaque(false);
        builder.setBorder(border);
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

        return builder.getPanel();
    }


    private JComponent buildDecoratedListBarAndExtras() {
        FormLayout layout = new FormLayout(
                "left:default, 9dlu:grow, right:pref",
                "$rgap, p");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setOpaque(false);
        if (listBarView != null) {
            builder.add(listBarView, CC.xy(1, 2));
        }
        if (listExtrasView != null) {
            builder.add(listExtrasView,  CC.xy(3, 2));
        }
        return builder.getPanel();
    }


    private JComponent buildDecoratedDetailsView() {
        FormLayout layout = new FormLayout(
                "fill:default:grow",
                "14, p");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setOpaque(false);
        builder.add(detailsView, CC.xy(1, 2));
        return builder.getPanel();
    }

}
