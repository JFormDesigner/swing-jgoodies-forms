package com.jgoodies.forms.builder;

/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.ConstantSize;

/**
 * A non-visual builder that assists you in building consistent button bars
 * that comply with popular UI style guides. It utilizes the {@link FormLayout}.
 * This class is in turn used by the 
 * {@link com.jgoodies.forms.factories.ButtonBarFactory} that provides 
 * an even higher level of abstraction for building consistent button bars.
 * <p>
 * Buttons added to the builder are either gridded or fixed and may fill
 * their FormLayout cell or not. All gridded buttons get the same width,
 * while fixed button use their own size. Gridded buttons honor 
 * the default minimum button width as specified by the current 
 * {@link com.jgoodies.forms.util.LayoutStyle}.
 * <p>
 * A button can optionally be declared as narrow, so that it has 
 * narrow margins if displayed with a JGoodies look&amp;feel.
 * This is useful if you want to layout buttons with equal width 
 * even if a button has a large label. For example, in a bar with
 * 'Add...', 'Remove', 'Properties...' you may declare the properties button 
 * to use narrow margins.
 * <p>
 * <b>Example:</b><br>
 * The following example builds a button bar with <i>Help</i> button on the 
 * left-hand side and <i>OK, Cancel, Apply</i> buttons on the right-hand side.
 * <pre>
 * private JPanel createHelpOKCancelApplyBar(
 *         JButton help, JButton ok, JButton cancel, JButton apply) {
 *     ButtonBarBuilder builder = new ButtonBarBuilder();
 *     builder.addGridded(help);
 *     builder.addRelatedGap();
 *     builder.addGlue();
 *     builder.addGriddedButtons(new JButton[]{ok, cancel, apply});
 *     return builder.getPanel();
 * }
 * </pre> 
 *
 * @author	Karsten Lentzsch
 */
public final class ButtonBarBuilder extends PanelBuilder {
    
    private static final ColumnSpec[] COL_SPECS  = new ColumnSpec[]{};
    private static final RowSpec      ROW_SPEC   = new RowSpec("center:pref");
    private static final RowSpec[]    ROW_SPECS  = new RowSpec[]{ROW_SPEC};
    
    private static final String       NARROW_KEY = "jgoodies.isNarrow";
    
    
    // Instance Creation ****************************************************

    /**
     * Constructs an instance of <code>ButtonBarBuilder</code> on the given
     * panel.
     * 
     * @param panel  the layout container
     */
    public ButtonBarBuilder(JPanel panel) {
        super(panel, new FormLayout(COL_SPECS, ROW_SPECS));
    }

    /**
     * Constructs an instance of <code>ButtonBarBuilder</code> on a
     * <code>JPanel</code>.
     */
    public ButtonBarBuilder() {
        this(new JPanel());
    }


    // Default Borders ******************************************************
    
    /**
     * Sets a default border that has a gap in the bar's north.
     */
    public void setDefaultButtonBarGapBorder() {
        getPanel().setBorder(Borders.BUTTON_BAR_GAP_BORDER);
    }
    
    
    // Adding Components ****************************************************
    
    /**
     * Adds a sequence of related gridded buttons separated by a default gap.
     * 
     * @param buttons  an array of buttons to add
     */
    public void addGriddedButtons(JButton[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            addGridded(buttons[i]);
            if (i < buttons.length - 1)
                addRelatedGap();
        }
    }

    /**
     * Adds a sequence of narrow gridded buttons separated by a default gap.
     * 
     * @param buttons  an array of buttons to add
     * @deprecated #addGriddedButtons already makes the borders narrow
     */
    public void addGriddedNarrowButtons(JButton[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            addGriddedNarrow(buttons[i]);
            if (i < buttons.length - 1)
                addRelatedGap();
        }
    }
    
    /**
     * Adds a sequence of gridded buttons that grow. 
     * The buttongs are separated by a default gap.
     * 
     * @param buttons  an array of buttons to add
     */
    public void addGriddedGrowingButtons(JButton[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            addGriddedGrowing(buttons[i]);
            if (i < buttons.length - 1)
                addRelatedGap();
        }
    }
    
    /**
     * Adds a fixed size component.
     * 
     * @param component  the component to add
     */
    public void addFixed(JComponent component) {
        getLayout().appendColumn(FormFactory.PREF_COLSPEC);
        add(component);
        nextColumn();
    }

    /**
     * Adds a fixed size component with narrow margins.
     * 
     * @param component  the component to add
     */
    public void addFixedNarrow(JComponent component) {
        component.putClientProperty(NARROW_KEY, Boolean.TRUE);
        addFixed(component);
    }

    /**
     * Adds a gridded component.
     * 
     * @param component  the component to add
     */
    public void addGridded(JComponent component) {
        getLayout().appendColumn(FormFactory.BUTTON_COLSPEC);
        getLayout().addGroupedColumn(getColumn());
        component.putClientProperty(NARROW_KEY, Boolean.TRUE);
        add(component);
        nextColumn();
    }

    /**
     * Adds a gridded narrow component.
     * 
     * @param component  the component to add
     * @deprecated #addGridded(JComponent) already makes the border narrow
     */
    public void addGriddedNarrow(JComponent component) {
        addGridded(component);
    }

    /**
     * Adds a gridded component that grows.
     * 
     * @param component  the component to add
     */
    public void addGriddedGrowing(JComponent component) {
        getLayout().appendColumn(FormFactory.GROWING_BUTTON_COLSPEC);
        getLayout().addGroupedColumn(getColumn());
        component.putClientProperty(NARROW_KEY, Boolean.TRUE);
        add(component);
        nextColumn();
    }

    /**
     * Adds a gridded, narrow component that grows.
     * 
     * @param component  the component to add
     */
    public void addGriddedGrowingNarrow(JComponent component) {
        component.putClientProperty(NARROW_KEY, Boolean.TRUE);
        addGriddedGrowing(component);
    }

    /**
     * Adds a glue that will be given the extra space, 
     * if this box is larger than its preferred size.
     */
    public void addGlue() {
        appendGlueColumn();
        nextColumn();
    }

    /**
     * Adds the standard gap for related components.
     */
    public void addRelatedGap() {
        appendRelatedComponentsGapColumn();
        nextColumn();
    }

    /**
     * Adds the standard gap for unrelated components.
     */
    public void addUnrelatedGap() {
        appendUnrelatedComponentsGapColumn();
        nextColumn();
    }

    /** 
     * Adds a strut of a specified size.
     * 
     * @param size  a <code>ConstantSize</code> that describes the gap's size
     */
    public void addStrut(ConstantSize size) {
        getLayout().appendColumn(new ColumnSpec(ColumnSpec.LEFT, 
                                                size, 
                                                ColumnSpec.NO_GROW));
        nextColumn();
    }
    

}
