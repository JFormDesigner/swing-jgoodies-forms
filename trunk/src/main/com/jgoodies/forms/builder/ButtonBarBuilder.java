/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.util.LayoutStyle;

/**
 * A non-visual builder that assists you in building consistent button bars
 * that comply with popular UI style guides. It utilizes the {@link FormLayout}.
 * This class is in turn used by the 
 * {@link com.jgoodies.forms.factories.ButtonBarFactory} that provides 
 * an even higher level of abstraction for building consistent button bars.<p>
 * 
 * Buttons added to the builder are either gridded or fixed and may fill
 * their FormLayout cell or not. All gridded buttons get the same width,
 * while fixed button use their own size. Gridded buttons honor 
 * the default minimum button width as specified by the current 
 * {@link com.jgoodies.forms.util.LayoutStyle}.<p>
 * 
 * A button can optionally be declared as narrow, so that it has 
 * narrow margins if displayed with a JGoodies look&amp;feel.
 * This is useful if you want to lay out buttons with equal width 
 * even if a button has a large label. For example, in a bar with
 * 'Add...', 'Remove', 'Properties...' you may declare the properties button 
 * to use narrow margins.<p>
 * 
 * <strong>Example:</strong><br>
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
 * @version $Revision: 1.5 $
 * 
 * @see com.jgoodies.forms.util.LayoutStyle
 */
public final class ButtonBarBuilder extends PanelBuilder {
    
    private static final ColumnSpec[] COL_SPECS  = new ColumnSpec[]{};
    private static final RowSpec      ROW_SPEC   = new RowSpec("center:pref");
    private static final RowSpec[]    ROW_SPECS  = new RowSpec[]{ROW_SPEC};
    
    private static final String       NARROW_KEY = "jgoodies.isNarrow";
    
    /**
     * Describes how sequences of buttons are added to the button bar:
     * left-to-right or right-to-left.
     * 
     * @see #isLeftToRight()
     * @see #setLeftToRight(boolean)
     */
    private boolean leftToRight;
    
    // Instance Creation ****************************************************

    /**
     * Constructs an instance of <code>ButtonBarBuilder</code> on a
     * <code>JPanel</code>.
     */
    public ButtonBarBuilder() {
        this(new JPanel());
    }


    /**
     * Constructs an instance of <code>ButtonBarBuilder</code> on the given
     * panel.
     * 
     * @param panel  the layout container
     */
    public ButtonBarBuilder(JPanel panel) {
        super(panel, new FormLayout(COL_SPECS, ROW_SPECS));
        leftToRight = LayoutStyle.getCurrent().isLeftToRightButtonOrder();
    }
    

    // Accessing Properties *************************************************
    
    /**
     * Returns whether button sequences will be ordered from 
     * left to right or from right to left. 
     * 
     * @return true if button sequences are ordered from left to right
     * @since 1.0.3
     * 
     * @see LayoutStyle#isLeftToRightButtonOrder()
     */
    public boolean isLeftToRightButtonOrder() {
        return leftToRight;
    }
    

    /**
     * Sets the order for button sequences to either left to right,
     * or right to left. 
     * 
     * @param newButtonOrder  true if button sequences shall be ordered 
     *     from left to right
     * @since 1.0.3
     * 
     * @see LayoutStyle#isLeftToRightButtonOrder()
     */
    public void setLeftToRightButtonOrder(boolean newButtonOrder) {
        leftToRight = newButtonOrder;
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
        int length = buttons.length;
        for (int i = 0; i < length; i++) {
            int index = leftToRight ? i : length -1 - i;
            addGridded(buttons[index]);
            if (i < buttons.length - 1)
                addRelatedGap();
        }
    }

    /**
     * Adds a sequence of narrow gridded buttons 
     * where each is separated by a default gap.
     * 
     * @param buttons  an array of buttons to add
     * @deprecated #addGriddedButtons already makes the borders narrow
     */
    public void addGriddedNarrowButtons(JButton[] buttons) {
        int length = buttons.length;
        for (int i = 0; i < length; i++) {
            int index = leftToRight ? i : length -1 - i;
            addGriddedNarrow(buttons[index]);
            if (i < buttons.length - 1)
                addRelatedGap();
        }
    }
    
    /**
     * Adds a sequence of gridded buttons that grow
     * where each is separated by a default gap.
     * 
     * @param buttons  an array of buttons to add
     */
    public void addGriddedGrowingButtons(JButton[] buttons) {
        int length = buttons.length;
        for (int i = 0; i < length; i++) {
            int index = leftToRight ? i : length -1 - i;
            addGriddedGrowing(buttons[index]);
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
