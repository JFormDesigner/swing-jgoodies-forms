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

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * A non-visual builder that assists you in building consistent button stacks
 * using the {@link FormLayout}.
 *
 * <p>
 * <b>Example:</b><br>
 * The following example builds a button with <i>Help</i> button 
 * on the left-hand side and <i>OK, Cancel, Apply</i> on the right-hand side.
 * <pre>
 * private JPanel createCloseUpDownButtonStack(
 *         JButton close, JButton up, JButton down) {
 *     ButtonStackBuilder builder = new ButtonStackBuilder();
 *     builder.addGridded(close);
 *     builder.addUnrelatedGap();
 *     builder.addGridded(up);
 *     builder.addRelatedGap();
 *     builder.addGridded(down);
 *     return builder.getPanel();
 * }
 * </pre>
 * 
 * @author	Karsten Lentzsch
 */
public final class ButtonStackBuilder extends PanelBuilder {
    
    private static final ColumnSpec[] COL_SPECS =
        new ColumnSpec[] { FormFactory.BUTTON_COLSPEC };
        
    private static final RowSpec[]    ROW_SPECS = 
        new RowSpec[]{};
    
    private static final String       NARROW_KEY = "jgoodies.isNarrow";
    
    
    // Instance Creation ****************************************************

    /**
     * Constructs an instance of <code>ButtonStackBuilder</code> on the given
     * panel.
     * 
     * @param panel   the layout container
     */
    public ButtonStackBuilder(JPanel panel) {
        super(panel, new FormLayout(COL_SPECS, ROW_SPECS));
    }

    /**
     * Constructs an instance of <code>ButtonStackBuilder</code> on a default
     * <code>JPanel</code>.
     */
    public ButtonStackBuilder() {
        this(new JPanel());
    }


    // Adding Components ****************************************************
    
    /**
     * Adds a sequence of related buttons separated by a default gap.
     * 
     * @param buttons  an array of buttons to add
     */
    public void addButtons(JButton[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            addGridded(buttons[i]);
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
        getLayout().appendRow(FormFactory.PREF_ROWSPEC);
        add(component);
        nextRow();
    }

    /**
     * Adds a gridded component.
     * 
     * @param component  the component to add
     */
    public void addGridded(JComponent component) {
        getLayout().appendRow(FormFactory.PREF_ROWSPEC);
        getLayout().addGroupedRow(getRow());
        add(component);
        nextRow();
    }

    /**
     * Adds a gridded narrow component.
     * 
     * @param component  the component to add
     */
    public void addGriddedNarrow(JComponent component) {
        component.putClientProperty(NARROW_KEY, Boolean.TRUE);
        addGridded(component);
    }

    /**
     * Adds a glue that will be given the extra space, 
     * if this box is larger than its preferred size.
     */
    public void addGlue() {
        appendGlueRow();
        nextRow();
    }

    /**
     * Adds the standard gap for related components.
     */
    public void addRelatedGap() {
        appendRelatedComponentsGapRow();
        nextRow();
    }

    /**
     * Adds the standard gap for unrelated components.
     */
    public void addUnrelatedGap() {
        appendUnrelatedComponentsGapRow();
        nextRow();
    }

    /** 
     * Adds a strut of a specified size.
     * 
     * @param size  a constant that describes the gap
     */
    public void addStrut(ConstantSize size) {
        getLayout().appendRow(new RowSpec(RowSpec.TOP, 
                                          size, 
                                          RowSpec.NO_GROW));
        nextRow();
    }
    

}
