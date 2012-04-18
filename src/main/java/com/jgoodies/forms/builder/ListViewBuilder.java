/*
 * Copyright (c) 2006-2012 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.builder;

import static com.jgoodies.common.base.Preconditions.checkArgument;
import static com.jgoodies.common.base.Preconditions.checkNotNull;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
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
 */
public final class ListViewBuilder {
	
	private final ComponentFactory factory;

    private JComponent labelView;
    private JComponent filterView;
    private JComponent listView;
    private JComponent listBarView;
    private JComponent listExtrasView;
    private JComponent detailsView;
    
    private Border     border;
    private int filterViewMinimumWidthDLU = 100;
    private int listViewMinimumHeightDLU  = 100;

    /**
     * Holds the panel that has been lazily built in {@code #buildPanel}.
     */
    private JComponent panel;
    
    
    // Instance Creation ******************************************************
    
    public ListViewBuilder() {
    	this(AbstractBuilder.getDefaultComponentFactory());
    }
    
    
    public ListViewBuilder(ComponentFactory factory) {
    	this.factory = factory;
    }


    // API ********************************************************************

    public void setLabelView(JComponent labelView) {
        this.labelView = labelView;
    }


    public void setLabel(String markedLabel) {
        setLabelView(factory.createLabel(markedLabel));
    }


    public void setHeaderLabel(String markedLabel) {
        setLabelView(factory.createHeaderLabel(markedLabel));
    }
    
    
    public void setFilterView(JComponent filterView) {
        this.filterView = filterView;
    }


    public void setFilterViewMinimumWidth(int minimumWidthtDLU) {
    	checkArgument(minimumWidthtDLU > 0, "The filter view minimum width must be positive.");
    	this.listViewMinimumHeightDLU = minimumWidthtDLU;
    }


    public void setListView(JComponent listView) {
        setListView(new JScrollPane(listView));
    }
    
    
    public void setListView(JScrollPane listView) {
    	this.listView = listView;
    }
    
    
    public void setListViewMinimumHeight(int minimumHeightDLU) {
    	checkArgument(minimumHeightDLU > 0, "The list view minimum height must be positive.");
    	this.listViewMinimumHeightDLU = minimumHeightDLU;
    }


    public void setListBarView(JComponent listBarView) {
        this.listBarView = listBarView;
    }


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


    public void setListExtrasView(JComponent listExtrasView) {
        this.listExtrasView = listExtrasView;
    }


    public void setDetailsView(JComponent detailsView) {
        this.detailsView = detailsView;
    }


    public void setBorder(Border border) {
        this.border = border;
    }


    public JComponent getPanel() {
        if (panel == null) {
            panel = buildPanel();
        }
        return panel;
    }


    // Implementation *********************************************************

    private JComponent buildPanel() {
    	checkNotNull(labelView, "The label must be set before #getPanel is invoked.");
        FormLayout layout = new FormLayout(
                "default:grow, 9dlu, [" + filterViewMinimumWidthDLU + "dlu,p]",
                "[14dlu,p], $lcg, fill:" + listViewMinimumHeightDLU + "dlu:grow, p, p");
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
