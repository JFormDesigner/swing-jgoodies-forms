/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch. 
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.extras;


import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Provides a means to build consistent form-oriented panels quickly
 * using the {@link FormLayout}. This builder combines frequently used
 * panel building steps: add a new row, add a label, proceed to the next 
 * data column then add a component.
 * <p>
 * This class is not yet part of the binary Forms library; 
 * it comes with the Forms distributions as an extra.
 * <b>The API is work in progress and may change without notice.</b>
 * If you want to use this class, you may consider copying it into your codebase.
 * <p>
 * <b>Example:</b>
 * <pre>
 * public void build() {
 *     FormLayout layout = new FormLayout(
 *         "right:max(40dlu;pref), 3dlu, 80dlu, 7dlu, " // 1st major colum
 *       + "right:max(40dlu;pref), 3dlu, 80dlu",        // 2nd major column
 *         "");                                         // add rows dynamically
 *     DefaultFormBuilder builder = new DefaultFormBuilder(layout);
 *     builder.setDefaultDialogBorder();
 *
 *     builder.appendSeparator("Flange");
 *
 *     builder.append("Identifier", identifierField);
 *     builder.nextLine();
 *
 *     builder.append("PTI [kW]",   new JTextField());          
 *     builder.append("Power [kW]", new JTextField());
 *
 *     builder.append("s [mm]",     new JTextField());
 *     builder.nextLine();
 *
 *     builder.appendSeparator("Diameters");
 *
 *     builder.append("da [mm]",    new JTextField());          
 *     builder.append("di [mm]",    new JTextField());
 *
 *     builder.append("da2 [mm]",   new JTextField());          
 *     builder.append("di2 [mm]",   new JTextField());
 *
 *     builder.append("R [mm]",     new JTextField());          
 *     builder.append("D [mm]",     new JTextField());
 *
 *     builder.appendSeparator("Criteria");
 *
 *     builder.append("Location",   buildLocationComboBox());   
 *     builder.append("k-factor",   new JTextField());
 *
 *     builder.appendSeparator("Bolts");
 *
 *     builder.append("Material",   ViewerUIFactory.buildMaterialComboBox());
 *     builder.nextLine();
 *
 *     builder.append("Numbers",    new JTextField());
 *     builder.nextLine();
 *
 *     builder.append("ds [mm]",    new JTextField());
 * }
 * </pre>
 *
 * @author	Karsten Lentzsch
 * @see	com.jgoodies.forms.builder.AbstractFormBuilder
 * @see	com.jgoodies.forms.factories.FormFactory
 * @see	com.jgoodies.forms.layout.FormLayout
 */
public final class DefaultFormBuilder extends PanelBuilder {

    /**
     * Holds the row specification that is reused to describe
     * the constant gaps between component lines.
     */
    private RowSpec lineGapSpec = FormFactory.LINE_GAP_ROWSPEC;

    /**
     * Holds the row specification that describes the constant gaps 
     * between paragraphs.
     */
    private RowSpec paragraphGapSpec = FormFactory.PARAGRAPH_GAP_ROWSPEC;
    
    /**
     * Holds the offset of the leading column - often 0 or 1.
     */
    private int leadingColumnOffset = 0;
    
    /**
     * Determines wether new data rows are being grouped or not. 
     */
    private boolean rowGroupingEnabled = false;
    

    // Instance Creation ****************************************************

    /**
     * Constructs an instance of <code>DefaultFormBuilder</code> for the given
     * layout.
     * 
     * @param layout	the <code>FormLayout</code> to be used
     */    
    public DefaultFormBuilder(FormLayout layout) {
        this(new JPanel(), layout);
    }
    
    /**
     * Constructs an instance of <code>DefaultFormBuilder</code> for the given
     * panel and layout.
     * 
     * @param panel		the layout container
     * @param layout		the <code>FormLayout</code> to be used
     */    
    public DefaultFormBuilder(JPanel panel, FormLayout layout) {
        super(panel, layout);
    }
    
    
    // Settings Gap Sizes ***************************************************

    /**
     * Sets the size of gaps between component lines using the given 
     * constant size.
     *  
     * @param lineGapSize   the <coide>ConstantSize</code> that describes 
     * the size of the gaps between component lines
     */
    public void setLineGapSize(ConstantSize lineGapSize) {
        RowSpec rowSpec = FormFactory.createGapRowSpec(lineGapSize);
        this.lineGapSpec = rowSpec.asUnmodifyable();
    }
    
    /**
     * Sets the size of gaps between paragraphs using the given 
     * constant size.
     *  
     * @param paragraphGapSize   the <coide>ConstantSize</code> that describes 
     * the size of the gaps between paragraphs
     */
    public void setParagraphGapSize(ConstantSize paragraphGapSize) {
        RowSpec rowSpec = FormFactory.createGapRowSpec(paragraphGapSize);
        this.paragraphGapSpec = rowSpec.asUnmodifyable();
    }
    
    /**
     * Returns the offset of the leading column, often 0 or 1.
     * 
     * @return the offset of the leading column
     */
    public int getLeadingColumnOffset() {
        return leadingColumnOffset;
    }
    
    /**
     * Sets the offset of the leading column, often 0 or 1.
     * 
     * @param columnOffset  the new offset of the leading column
     */
    public void setLeadingColumnOffset(int columnOffset) {
        this.leadingColumnOffset = columnOffset;
    }
    
    /**
     * Returns whether new data rows are being grouped or not.
     * 
     * @return true indicates grouping enabled, false disabled
     */
    public boolean isRowGroupingEnabled() {
        return rowGroupingEnabled;
    }
    
    /**
     * Enables or disables the grouping of new data rows.
     * 
     * @param enabled  indicates grouping enabled, false disabled
     */
    public void setRowGroupingEnabled(boolean enabled) {
        rowGroupingEnabled = enabled;
    }
    

    // Filling Columns ******************************************************
    
    /**
     * Adds a component to the panel using the default constraints.
     * Proceeds to the next data column.
     * 
     * @param component	the component to add
     */
    public void append(Component component) {
        append(component, 1);
    }
    
    /**
     * Adds a component to the panel using the default constraints with
     * the given columnSpan. Proceeds to the next data column.
     * 
     * @param component the component to append
     * @param columnSpan    the column span used to add 
     */
    public void append(Component component, int columnSpan) {
        ensureCursorColumnInGrid();
        ensureHasGapRow(lineGapSpec);
        ensureHasComponentLine();
        
        setColumnSpan(columnSpan);
        add(component);
        setColumnSpan(1);
        nextColumn(columnSpan + 1);
    }

    /**
     * Adds two components to the panel; each component will span a single
     * data column. Proceeds to the next data column.
     * 
     * @param c1    the first component to add
     * @param c2    the second component to add
     */    
    public void append(Component c1, Component c2) {
        append(c1);
        append(c2);
    }

    /**
     * Adds three components to the panel; each component will span a single
     * data column. Proceeds to the next data column.
     * 
     * @param c1    the first component to add
     * @param c2    the second component to add
     * @param c3    the third component to add
     */    
    public void append(Component c1, Component c2, Component c3) {
        append(c1);
        append(c2);
        append(c3);
    }

    /**
     * Adds a text label to the panel and proceeds to the next column.
     * 
     * @param textWithMnemonic  the label's text - may mark a mnemonic
     * @return the added label
     */
    public JLabel append(String textWithMnemonic) {
        JLabel label = getComponentFactory().createLabel(textWithMnemonic);
        append(label);
        return label;
    }

    /**
     * Adds a text label and component to the panel. 
     * Then proceeds to the next data column.
     * 
     * @param textWithMnemonic  the label's text - may mark a mnemonic
     * @param component         the component to add
     * @return the added label
     */    
    public JLabel append(String textWithMnemonic, Component component) {
        return append(textWithMnemonic, component, 1);
    }

    /**
     * Adds a text label and component to the panel; the component will span
     * the specified number columns. Proceeds to the next data column.
     * <p>
     * The created label is labelling the given component; so the component
     * gets the focus if the (optional) label mnemonic is pressed.
     * 
     * @param textWithMnemonic  the label's text - may mark a mnemonic
     * @param c                 the component to add
     * @param columnSpan        number of columns the component shall span
     * @return the added label
     * @see JLabel#setLabelFor
     */    
    public JLabel append(String textWithMnemonic, Component c, int columnSpan) {
        JLabel label = append(textWithMnemonic);
        label.setLabelFor(c);
        append(c, columnSpan);
        return label;
    }

    /**
     * Adds a text label and two components to the panel; each component
     * will span a single column. Proceeds to the next data column.
     * 
     * @param textWithMnemonic  the label's text - may mark a mnemonic
     * @param c1    the first component to add
     * @param c2    the second component to add
     * @return the added label
     */    
    public JLabel append(String textWithMnemonic, Component c1, Component c2) {
        JLabel label = append(textWithMnemonic, c1);
        append(c2);
        return label;
    }

    /**
     * Adds a text label and two components to the panel; each component
     * will span a single column. Proceeds to the next data column.
     * 
     * @param textWithMnemonic  the label's text - may mark a mnemonic
     * @param c1      the first component to add
     * @param c2      the second component to add
     * @param colSpan the column span for the second component
     */    
    public void append(String textWithMnemonic, Component c1, Component c2, int colSpan) {
        append(textWithMnemonic, c1);
        append(c2, colSpan);
    }

    /**
     * Adds a text label and three components to the panel; each component
     * will span a single column. Proceeds to the next data column.
     * 
     * @param textWithMnemonic  the label's text - may mark a mnemonic
     * @param c1    the first component to add
     * @param c2    the second component to add
     * @param c3    the third component to add
     * @return the added label
     */    
    public JLabel append(String textWithMnemonic, Component c1, Component c2, Component c3) {
        JLabel label = append(textWithMnemonic, c1, c2);
        append(c3);
        return label;
    }

    /**
     * Adds a separator without text that spans all columns.
     * 
     * @return the added titled separator 
     */
    public JComponent appendSeparator() {
        return appendSeparator("");
    }

    /**
     * Adds a separator with the given text that spans all columns.
     * 
     * @param text      the separator title text
     * @return the added titled separator 
     */
    public JComponent appendSeparator(String text) {
        ensureCursorColumnInGrid();
        ensureHasGapRow(paragraphGapSpec);
        ensureHasComponentLine();
        
        setColumn(super.getLeadingColumn());
        int columnSpan = getColumnCount();
        setColumnSpan(getColumnCount());
        JComponent titledSeparator = addSeparator(text);
        setColumnSpan(1);
        nextColumn(columnSpan);
        return titledSeparator;
    }


    // Overriding Superclass Behavior ***************************************
    
    /**
     * Returns the leading column. Unlike the superclass we take a 
     * column offset into account.
     * 
     * @return the leading column
     */
    protected int getLeadingColumn() {
        int column = super.getLeadingColumn();
        return column + getLeadingColumnOffset() * getColumnIncrementSign();
    }
    
    // Adding Rows **********************************************************
    
    /**
     * Ensures that the cursor is in the grid. In case it's beyond the 
     * form's right hand side, the cursor is moved to the leading column
     * of the next line.
     */
    private void ensureCursorColumnInGrid() {
        if (getColumn() > getColumnCount()) {
            nextLine();
        }
    }
    
    /**
     * Ensures that we have a gap row before the next component row.
     * Checks if the current row is the given <code>RowSpec</code>
     * and appends this row spec if necessary.
     * 
     * @param gapRowSpec  the row specification to check for
     */
    private void ensureHasGapRow(RowSpec gapRowSpec) {
        if ((getRow() == 1) || (getRow() <= getRowCount()))
            return;
        
        if (getRow() <= getRowCount()) {
            RowSpec rowSpec = getCursorRowSpec();
            if ((rowSpec == gapRowSpec))
                return;
        }
        appendRow(gapRowSpec);
        nextLine();
    }
    
    /**
     * Ensures that the form has a component row. Adds a component row
     * if the cursor is beyond the form's bottom.
     */
    private void ensureHasComponentLine() {
        if (getRow() <= getRowCount()) return;
        appendRow(FormFactory.PREF_ROWSPEC);  
        if (isRowGroupingEnabled()) {
            getLayout().addGroupedRow(getRow());
        }      
    }
    
    /**
     * Looks up and answers the row specification of the current row.
     *  
     * @return the row specification of the current row
     */
    private RowSpec getCursorRowSpec() {
        return getLayout().getRowSpec(getRow());
    }


}
