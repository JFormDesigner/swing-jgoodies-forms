/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.workinprogress.LabelStyle;

/**
 * Provides a means to build consistent form-oriented panels using the
 * {@link FormLayout}.<p>
 * 
 * This class is work in progress and in the early stages of development; 
 * the API may change without notice.
 *
 * @author	Karsten Lentzsch
 * @see	com.jgoodies.forms.builder.AbstractFormBuilder
 * @see	com.jgoodies.forms.factories.FormFactory
 * @see	com.jgoodies.forms.layout.FormLayout
 */
public final class DefaultFormBuilder extends PanelBuilder {

    private final LabelStyle labelStyle;
    private final RowSpec    lineGap;
    private final boolean   indented;

    // Instance Creation ****************************************************

    /**
     * Constructs an instance of <code>DefaultFormBuilder</code> for the
     * layout as specified by major columns, minor columns, and label style.
     * 
     * @param majorColumns		the number of major columns
     * @param minorColumn		the number of minor columns
     * @param labelStyle		the label style
     */    
    public DefaultFormBuilder(int majorColumns, int minorColumns, 
                              LabelStyle labelStyle) {
        this(new JPanel(), majorColumns, minorColumns, labelStyle);
    }
    
    /**
     * Constructs an instance of <code>DefaultFormBuilder</code> for the given
     * panel, and layout as specified by major columns, minor columns,
     * and label style.
     *
     * @param panel			the layout container
     * @param majorColumns      the number of major columns
     * @param minorColumn       the number of minor columns
     * @param labelStyle        the label style
     */    
    public DefaultFormBuilder(JPanel panel,
                              int majorColumns, int minorColumns, 
                              LabelStyle labelStyle) {
        this(panel, 
             majorColumns, minorColumns, labelStyle, true, Sizes.DLUX2);
    }
    
    /**
     * Constructs an instance of <code>DefaultFormBuilder</code> for the given
     * panel, and layout as specified by major columns, minor columns,
     * labeled and indented properties, and the minor column gap.
     *
     * @param panel         	the layout container
     * @param majorColumns      the number of major columns
     * @param minorColumn       the number of minor columns
     * @param labelStyle        the label style
     * @param indented			true for indented, false for no indent
     * @param minorColumnGap	the gap between minor columns
     */    
    public DefaultFormBuilder(JPanel panel,
                              int majorColumns, int minorColumns, 
                              LabelStyle labelStyle, 
                              boolean indented,
                              ConstantSize minorColumnGap) {
        this(panel, FormFactory.createColumnLayout(
                        majorColumns, 
                        minorColumns,
                        labelStyle,
                        indented,
                        minorColumnGap), labelStyle, indented);
    }
    
    /**
     * Constructs an instance of <code>DefaultFormBuilder</code> for the given
     * panel, layout, and label style.
     * 
     * @param panel		the layout container
     * @param layout		the layout to use
     * @param labelStyle	the label style
     * @param indented     true for indented, false for no indent
     */    
    public DefaultFormBuilder(JPanel panel, FormLayout layout, 
                               LabelStyle labelStyle,
                               boolean indented) {
        super(panel, layout);
        this.labelStyle = labelStyle;
        this.indented   = indented;
        this.lineGap    = FormFactory.LINE_GAP_ROWSPEC;
    }
    
    
    // Adding Rows **********************************************************
    
    /**
     * Appends an ungrouped line and sets the location to the new line.
     */
    public void addLine() {
        addLine(true);
    }
    
    /**
     * Appends an ungrouped line and sets the location to the new line.
     * 
     * @param addGap  true adds a gap line, false adds no gap line
     */
    public void addLine(boolean addGap) {
        if (lineGap != null && addGap) {
            appendRow(lineGap);
            nextLine();
        }
        appendRow(FormFactory.PREF_ROWSPEC);
        resetColumn();
    }
    
    /**
     * Appends a separator line with the given text.
     * 
     * @param text		the separator's title text
     */
    public void appendSeparatorLine(String text) {
        appendRow(FormFactory.PREF_ROWSPEC);
        xy(1, getRow());
        appendSeparator(text);
        nextLine();
    }
    
    /**
     * Appends a grouped line and sets the location to the new line.
     */
    public void addGroupedLine() {
        
    }
    
    /**
     * Appends a gap between two paragraphs. Then sets the location to the new
     * line.
     */
    public void appendParagraphGap() {
        appendRow(FormFactory.PARAGRAPH_GAP_ROWSPEC);
        nextLine();
    }
    
    // Filling Columns ******************************************************
    
    /**
     * Adds a component to the panel using the default constraints with
     * the given minorColumnSpan. Proceeds to the next data column.
     * 
     * @param component		the component to append
     * @param dataColumnSpan	the span of data columns 
     */
    public void append(Component component, int dataColumnSpan) {
        int columnSpan = dataColumnSpan * 2 - 1;
        w(columnSpan);
        add(component);
        w(1);
        nextColumn(columnSpan + 1);
    }

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
     * Adds three components to the panel; each component will span a single
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
     * @param text		the label's text
     */
    public void append(String text) {
        append(createDecoratedLabel(text), 1);
    }

    /**
     * Adds a text label and component to the panel; the component will span
     * the specified number of data columns. Proceeds to the next data column.
     * 
     * @param text            the text to add
     * @param c               the component to add
     * @param dataColumnSpan  columns the component shall span
     */    
    public void append(String text, Component c, int dataColumnSpan) {
        append(text);
        append(c, dataColumnSpan);
    }

    /**
     * Adds a text label and component to the panel. 
     * Then proceeds to the next data column.
     * 
     * @param text       the text to add
     * @param component  the component to add
     */    
    public void append(String text, Component component) {
        append(text, component, 1);
    }

    /**
     * Adds a text label and two components to the panel; each component
     * will span a single data column. Proceeds to the next data column.
     * 
     * @param text  the text to add
     * @param c1    the first component to add
     * @param c2    the second component to add
     */    
    public void append(String text, Component c1, Component c2) {
        append(text);
        append(c1);
        append(c2);
    }

    /**
     * Adds a text label and three components to the panel; each component
     * will span a single data column. Proceeds to the next data column.
     * 
     * @param text  the text to add
     * @param c1    the first component to add
     * @param c2    the second component to add
     * @param c3    the third component to add
     */    
    public void append(String text, Component c1, Component c2, Component c3) {
        append(text);
        append(c1);
        append(c2);
        append(c3);
    }

    /**
     * Adds a separator without text that spans all columns.
     */
    public void appendSeparator() {
        appendSeparator(null);
    }

    /**
     * Adds a separator with the given text that spans all columns.
     * 
     * @param text		the separator title text
     */
    public void appendSeparator(String text) {
        appendSeparator(text, getLayout().getColumnCount());
    }

    /**
     * Adds a separator with the given text that spans the specified number
     * of columns.
     * 
     * @param text			the separator's title text
     * @param columnSpan 	the separator's column span
     */
    public void appendSeparator(String text, int columnSpan) {
        addSeparator(text, columnSpan);
        nextColumn(columnSpan);
    }


    // Component Creation ***************************************************
    
    /**
     * Creates and returns a decorated <code>JLabel</code>.
     * 
     * @param text		the label's text
     */
    private JLabel createDecoratedLabel(String text) {
        return labelStyle.createDecoratedLabel(text);
    }


    // Overriding Superclass Behavior ***************************************
    
    protected void resetColumn() {
         setLocation(indented ? 2 : 1, getRow());
    }


}
