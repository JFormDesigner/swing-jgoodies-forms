/*
 * Copyright (c) 2002-2004 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * An general purpose panel builder that uses the {@link FormLayout} 
 * to lay out <code>JPanel</code>s. It provides convenience methods 
 * to set a default border and to add labels, titles and titled separators.<p>
 * 
 * The PanelBuilder is the working horse for layouts when more specialized 
 * builders like the {@link ButtonBarBuilder} or {@link DefaultFormBuilder} 
 * are inappropriate.<p>
 * 
 * The Forms tutorial includes several examples that present and compare
 * different style to build with the PanelBuilder: static row numbers
 * vs. row variable, explicit CellConstraints vs. builder cursor,
 * static rows vs. dynamically added rows. Also, you may check out the
 * Tips &amp; Tricks section of the Forms HTML documentation.<p>
 * 
 * The texts used in method <code>#addLabel</code> can contain an optional
 * mnemonic marker. The mnemonic and mnemonic index are indicated by 
 * a single ampersand (<tt>&amp;</tt>). For example <tt>&quot;&amp;Save&quot</tt>, or 
 * <tt>&quot;Save&nbsp;&amp;as&quot</tt>. To use the ampersand itself, 
 * duplicate it, for example <tt>&quot;Look&amp;&amp;Feel&quot</tt>.<p>
 * 
 * <strong>Example:</strong><br>
 * This example creates a panel with 3 columns and 3 rows.
 * <pre>
 * FormLayout layout = new FormLayout(
 *      "right:pref, 6dlu, 50dlu, 4dlu, default",  // columns 
 *      "pref, 3dlu, pref, 3dlu, pref");           // rows
 *
 * PanelBuilder builder = new PanelBuilder(layout);
 * CellConstraints cc = new CellConstraints();
 * builder.addLabel("&Title",      cc.xy  (1, 1));
 * builder.add(new JTextField(),   cc.xywh(3, 1, 3, 1));
 * builder.addLabel("&Price",      cc.xy  (1, 3));
 * builder.add(new JTextField(),   cc.xy  (3, 3));
 * builder.addLabel("&Author",     cc.xy  (1, 5));
 * builder.add(new JTextField(),   cc.xy  (3, 5));
 * builder.add(new JButton("..."), cc.xy  (5, 5));
 * return builder.getPanel();
 * </pre>
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.11 $
 * 
 * @see	com.jgoodies.forms.factories.ComponentFactory
 * @see     I15dPanelBuilder
 * @see     DefaultFormBuilder
 */
public class PanelBuilder extends AbstractFormBuilder {
    
    /**
     * Refers to a factory that is used to create labels,
     * titles and paragraph separators.
     */
    private ComponentFactory componentFactory;
    

    // Instance Creation ****************************************************

    /**
     * Constructs an instance of <code>PanelBuilder</code> for the given
     * panel and layout.
     * 
     * @param panel   the layout container to build on
     * @param layout  the form layout to use
     */
    public PanelBuilder(JPanel panel, FormLayout layout){        
        super(panel, layout);
    }
    

    /**
     * Constructs an instance of <code>PanelBuilder</code> for the given
     * layout. Uses an instance of <code>JPanel</code> as layout container.
     * 
     * @param layout  the form layout to use
     */
    public PanelBuilder(FormLayout layout){        
        this(new JPanel(), layout);
    }


    // Accessors ************************************************************

    /**
     * Returns the panel used to build the form.
     * 
     * @return the panel used by this builder to build the form
     */
    public final JPanel getPanel() { 
        return (JPanel) getContainer();  
    }


    // Borders **************************************************************
    
    /**
     * Sets the panel's border.
     * 
     * @param border	the border to set
     */
    public final void setBorder(Border border) {
        getPanel().setBorder(border);
    }
    
    
    /**
     * Sets the default dialog border.
     * 
     * @see Borders
     */
    public final void setDefaultDialogBorder() {
        setBorder(Borders.DIALOG_BORDER);
    }
    

    // Adding Labels **********************************************************
    
    /**
     * Adds a textual label to the form using the specified constraints.<p>
     * 
     * <pre>
     * addLabel("Name",       cc.xy(1, 1)); // No Mnemonic
     * addLabel("N&ame",      cc.xy(1, 1)); // Mnemonic is 'a'
     * addLabel("Look&&Feel", cc.xy(1, 1)); // No mnemonic, text is "look&feel"
     * </pre>
     * 
     * @param textWithMnemonic  the label's text - may contain a mnemonic marker
     * @param constraints       the label's cell constraints
     * @return the new label
     * 
     * @see ComponentFactory
     */
    public final JLabel addLabel(String textWithMnemonic, CellConstraints constraints) {
        JLabel label = getComponentFactory().createLabel(textWithMnemonic);
        add(label, constraints);
        return label;
    }
    
    
    /**
     * Adds a textual label to the form using the specified constraints.<p>
     * 
     * <pre>
     * addLabel("Name",       "1, 1"); // No Mnemonic
     * addLabel("N&ame",      "1, 1"); // Mnemonic is 'a'
     * addLabel("Look&&Feel", "1, 1"); // No mnemonic, text is "look&feel"
     * </pre>
     * 
     * @param textWithMnemonic    the label's text - may contain a mnemonic marker
     * @param encodedConstraints  a string representation for the constraints
     * @return the new label
     * 
     * @see ComponentFactory
     */
    public final JLabel addLabel(String textWithMnemonic, String encodedConstraints) {
        return addLabel(textWithMnemonic, new CellConstraints(encodedConstraints));
    }
    
    
    /**
     * Adds a textual label to the form using the default constraints.<p>
     * 
     * <pre>
     * addLabel("Name");       // No Mnemonic
     * addLabel("N&ame");      // Mnemonic is 'a'
     * addLabel("Look&&Feel"); // No mnemonic, text is "look&feel"
     * </pre>
     * 
     * @param textWithMnemonic  the label's text - may contain a mnemonic marker
     * @return the new label
     * 
     * @see ComponentFactory
     */
    public final JLabel addLabel(String textWithMnemonic) {
        return addLabel(textWithMnemonic, cellConstraints());
    }
    

    // Adding Label with related Component ************************************
    
    /**
     * Adds a label and component to the panel using the given cell constraints.
     * Sets the given label as <i>the</i> component label using 
     * {@link JLabel#setLabelFor(java.awt.Component)}.<p>
     * 
     * <strong>Note:</strong> The {@link CellConstraints} objects for the label 
     * and the component must be different. Cell constraints are implicitly 
     * cloned by the <code>FormLayout</code> when added to the container. 
     * However, in this case you may be tempted to reuse a 
     * <code>CellConstraints</code> object in the same way as with many other 
     * builder methods that require a single <code>CellConstraints</code> 
     * parameter. 
     * The pitfall is that the methods <code>CellConstraints.xy*(...)</code> 
     * just set the coordinates but do <em>not</em> create a new instance.
     * And so the second invocation of <code>xy*(...)</code> overrides
     * the settings performed in the first invocation before the object
     * is cloned by the <code>FormLayout</code>.<p>
     * 
     * <strong>Wrong:</strong><pre>
     * CellConstraints cc = new CellConstraints();
     * builder.add(
     *     nameLabel, 
     *     cc.xy(1, 7),         // will be modified by the code below
     *     nameField, 
     *     cc.xy(3, 7)          // sets the single instance to (3, 7)
     * );
     * </pre>
     * <strong>Correct:</strong><pre>
     * // Using a single CellConstraints instance and cloning
     * CellConstraints cc = new CellConstraints();
     * builder.add(
     *     nameLabel, 
     *     (CellConstraints) cc.xy(1, 7).clone(), // cloned before the next modification 
     *     nameField, 
     *     cc.xy(3, 7)                            // sets this instance to (3, 7)
     * );
     * 
     * // Using two CellConstraints instances 
     * CellConstraints cc1 = new CellConstraints();
     * CellConstraints cc2 = new CellConstraints();
     * builder.add(
     *     nameLabel, 
     *     cc1.xy(1, 7),       // sets instance 1 to (1, 7)
     *     nameField, 
     *     cc2.xy(3, 7)        // sets instance 2 to (3, 7)
     * );
     * </pre>
     * 
     * @param label                 the label to add
     * @param labelConstraints      the label's cell constraints
     * @param component             the component to add
     * @param componentConstraints  the component's cell constraints
     * @return the added label
     * @throws IllegalArgumentException if the same cell constraints instance
     *     is used for the label and the component
     * 
     * @see JLabel#setLabelFor(java.awt.Component)
     * @see DefaultFormBuilder
     */
    public final JLabel add(JLabel label,        CellConstraints labelConstraints,
                             Component component, CellConstraints componentConstraints) {
        if (labelConstraints == componentConstraints)
            throw new IllegalArgumentException(
                    "You must provide two CellConstraints instances, " +
                    "one for the label and one for the component.\n" +
                	"Consider using #clone(). See the JavaDocs for details.");
        
        add(label,     labelConstraints);
        add(component, componentConstraints);
        label.setLabelFor(component);   
        return label; 
    }
    
    
    /**
     * Adds a label and component to the panel using the given cell constraints.
     * Sets the given label as <i>the</i> component label using 
     * {@link JLabel#setLabelFor(java.awt.Component)}.<p>
     * 
     * <strong>Note:</strong> The {@link CellConstraints} objects for the label 
     * and the component must be different. Cell constraints are implicitly 
     * cloned by the <code>FormLayout</code> when added to the container. 
     * However, in this case you may be tempted to reuse a 
     * <code>CellConstraints</code> object in the same way as with many other 
     * builder methods that require a single <code>CellConstraints</code> 
     * parameter. 
     * The pitfall is that the methods <code>CellConstraints.xy*(...)</code> 
     * just set the coordinates but do <em>not</em> create a new instance.
     * And so the second invocation of <code>xy*(...)</code> overrides
     * the settings performed in the first invocation before the object
     * is cloned by the <code>FormLayout</code>.<p>
     * 
     * <strong>Wrong:</strong><pre>
     * builder.addLabel(
     *     "&Name:", 
     *     cc.xy(1, 7),         // will be modified by the code below
     *     nameField, 
     *     cc.xy(3, 7)          // sets the single instance to (3, 7)
     * );
     * </pre>
     * <strong>Correct:</strong><pre>
     * // Using a single CellConstraints instance and cloning
     * CellConstraints cc = new CellConstraints();
     * builder.addLabel(
     *     "&Name:", 
     *     (CellConstraints) cc.xy(1, 7).clone(), // cloned before the next modification 
     *     nameField, 
     *     cc.xy(3, 7)                            // sets this instance to (3, 7)
     * );
     * 
     * // Using two CellConstraints instances 
     * CellConstraints cc1 = new CellConstraints();
     * CellConstraints cc2 = new CellConstraints();
     * builder.addLabel(
     *     "&Name:", 
     *     cc1.xy(1, 7),       // sets instance 1 to (1, 7)
     *     nameField, 
     *     cc2.xy(3, 7)        // sets instance 2 to (3, 7)
     * );
     * </pre>
     * 
     * @param textWithMnemonic      the label's text - may contain a mnemonic marker
     * @param labelConstraints      the label's cell constraints
     * @param component             the component to add
     * @param componentConstraints  the component's cell constraints
     * @return the added label
     * @throws IllegalArgumentException if the same cell constraints instance
     *     is used for the label and the component
     * 
     * @see JLabel#setLabelFor(java.awt.Component)
     * @see ComponentFactory
     * @see DefaultFormBuilder
     */
    public final JLabel addLabel(
        String textWithMnemonic, CellConstraints labelConstraints,
        Component component,     CellConstraints componentConstraints) {

        if (labelConstraints == componentConstraints)
            throw new IllegalArgumentException(
                    "You must provide two CellConstraints instances, " +
                    "one for the label and one for the component.\n" +
                    "Consider using #clone(). See the JavaDocs for details.");
        
        JLabel label = addLabel(textWithMnemonic, labelConstraints);
        add(component, componentConstraints);
        label.setLabelFor(component);
        return label;
    }
    

    // Adding Titles ----------------------------------------------------------
     
    /**
     * Adds a title label to the form using the specified constraints.
     * 
     * @param text         the label's title text
     * @param constraints  the separator's cell constraints
     * @return the added title label
     * 
     * @see ComponentFactory
     */
    public final JLabel addTitle(String text, CellConstraints constraints) {
        JLabel titleLabel = getComponentFactory().createTitle(text);
        add(titleLabel, constraints);
        return titleLabel;
    }
    
    
    /**
     * Adds a title label to the form using the specified constraints.
     * 
     * @param text                the label's text
     * @param encodedConstraints  a string representation for the constraints
     * @return the added title label
     * 
     * @see ComponentFactory
     */
    public final JLabel addTitle(String text, String encodedConstraints) {
        return addTitle(text, new CellConstraints(encodedConstraints));
    }
     
    
    /**
     * Adds a title label to the form using the default constraints.
     * 
     * @param text   the separator titel
     * @return the added title label
     * 
     * @see ComponentFactory
     */
    public final JLabel addTitle(String text) {
        return addTitle(text, cellConstraints());
    }
     

    // Adding Separators ------------------------------------------------------
    
    /**
     * Adds a titled separator to the form using the specified constraints.
     * 
     * @param text         the separator title
     * @param constraints  the separator's cell constraints
     * @return the added separator
     */
    public final JComponent addSeparator(String text, CellConstraints constraints) {
        int titleAlignment = isLeftToRight()
                ? SwingConstants.LEFT
                : SwingConstants.RIGHT;
        JComponent titledSeparator =
            getComponentFactory().createSeparator(text, titleAlignment);
        add(titledSeparator, constraints);
        return titledSeparator;
    }
    
    
    /**
     * Adds a titled separator to the form using the specified constraints.
     * 
     * @param text                the separator titel
     * @param encodedConstraints  a string representation for the constraints
     * @return the added separator
     */
    public final JComponent addSeparator(String text, String encodedConstraints) {
        return addSeparator(text, new CellConstraints(encodedConstraints));
    }
     
    
    /**
     * Adds a titled separator to the form that spans the specified columns.
     * 
     * @param text  		the separator titel
     * @param columnSpan	the number of columns the separator spans
     * @return the added separator
     */
    public final JComponent addSeparator(String text, int columnSpan) {
        return addSeparator(
                text, 
                createLeftAdjustedConstraints(columnSpan));
    }
    
     
    /**
     * Adds a titled separator to the form that spans all columns.
     * 
     * @param text  the separator titel
     * @return the added separator
     */
    public final JComponent addSeparator(String text) {
        return addSeparator(text, getLayout().getColumnCount());
    }
    

    // Accessing the ComponentFactory *****************************************
    
    /**
     * Returns the builder's component factory. If no factory
     * has been set before, it is lazily initialized using with an instance of
     * {@link com.jgoodies.forms.factories.DefaultComponentFactory}.
     * 
     * @return the component factory
     * 
     * @see #setComponentFactory(ComponentFactory)
     */
    protected final ComponentFactory getComponentFactory() {
        if (componentFactory == null) {
            componentFactory = DefaultComponentFactory.getInstance();
        }
        return componentFactory;
    }
    
    
    /**
     * Sets a new component factory. 
     * 
     * @param newFactory   the component factory to be set
     * 
     * @see #getComponentFactory()
     */
    public final void setComponentFactory(ComponentFactory newFactory) {
        componentFactory = newFactory;
    }
    
    
}
