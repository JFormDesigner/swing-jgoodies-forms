/*
 * Copyright (c) 2002-2009 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import java.awt.Color;
import java.awt.Component;
import java.lang.ref.WeakReference;

import javax.swing.*;
import javax.swing.border.Border;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.ComponentFactory2;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * An general purpose panel builder that uses the {@link FormLayout}
 * to lay out <code>JPanel</code>s. It provides convenience methods
 * to set a default border and to add labels, titles and titled separators.<p>
 *
 * The PanelBuilder is the working horse for layouts when more specialized
 * builders like the {@link ButtonBarBuilder2} or {@link DefaultFormBuilder}
 * are inappropriate.<p>
 *
 * The Forms tutorial includes several examples that present and compare
 * different style to build with the PanelBuilder: static row numbers
 * vs. row variable, explicit CellConstraints vs. builder cursor,
 * static rows vs. dynamically added rows. Also, you may check out the
 * Tips &amp; Tricks section of the Forms HTML documentation.<p>
 *
 * The text arguments passed to the methods <code>#addLabel</code>,
 * <code>#addTitle</code>, and <code>#addSeparator</code> can contain
 * an optional mnemonic marker. The mnemonic and mnemonic index
 * are indicated by a single ampersand (<tt>&amp;</tt>). For example
 * <tt>&quot;&amp;Save&quot</tt>, or <tt>&quot;Save&nbsp;&amp;as&quot</tt>.
 * To use the ampersand itself duplicate it, for example
 * <tt>&quot;Look&amp;&amp;Feel&quot</tt>.<p>
 *
 * <strong>Example:</strong><br>
 * This example creates a panel with 3 columns and 3 rows.
 * <pre>
 * FormLayout layout = new FormLayout(
 *      "pref, $lcgap, 50dlu, $rgap, default",  // columns
 *      "pref, $lg, pref, $lg, pref");          // rows
 *
 * PanelBuilder builder = new PanelBuilder(layout);
 * CellConstraints cc = new CellConstraints();
 * builder.addLabel("&Title:",        cc.xy  (1, 1));
 * builder.add(new JTextField(),      cc.xywh(3, 1, 3, 1));
 * builder.addLabel("&Price:",        cc.xy  (1, 3));
 * builder.add(new JTextField(),      cc.xy  (3, 3));
 * builder.addLabel("&Author:",       cc.xy  (1, 5));
 * builder.add(new JTextField(),      cc.xy  (3, 5));
 * builder.add(new JButton("\u2026"), cc.xy  (5, 5));
 * return builder.getPanel();
 * </pre>
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.15 $
 *
 * @see	com.jgoodies.forms.factories.ComponentFactory
 * @see     I15dPanelBuilder
 * @see     DefaultFormBuilder
 */
public class PanelBuilder extends AbstractFormBuilder {

    // Constants **************************************************************

    /**
     * A JComponent client property that is used to determine the label
     * labeling a component. Copied from the JLabel class.
     */
    private static final String LABELED_BY_PROPERTY = "labeledBy";


    // Static Fields **********************************************************

    /**
     * The global default for the enablement of the setLabelFor feature.
     * Turned off by default.
     *
     * @see #setLabelForFeatureEnabledDefault(boolean)
     * @see #setLabelForFeatureEnabled(boolean)
     */
    private static boolean labelForFeatureEnabledDefault = false;


    // Instance Fields ********************************************************

    /**
     * Refers to a factory that is used to create labels,
     * titles and paragraph separators.
     */
    private ComponentFactory componentFactory;


    /**
     * The instance value for the setLabelFor feature.
     * Is initialized using the global default.
     *
     * @see #setLabelForFeatureEnabled(boolean)
     * @see #setLabelForFeatureEnabledDefault(boolean)
     */
    private boolean labelForFeatureEnabled;


    /**
     * Refers to the most recently added label.
     * Used to invoke {@link JLabel#setLabelFor(java.awt.Component)}
     * for the next component added to the panel that is applicable for
     * this feature (for example focusable). After the association
     * has been set, the reference will be cleared.
     *
     * @see #add(Component, CellConstraints)
     */
    private WeakReference mostRecentlyAddedLabelReference = null;


    // Instance Creation ******************************************************

    /**
     * Constructs a <code>PanelBuilder</code> for the given
     * layout. Uses an instance of <code>JPanel</code> as layout container
     * with the given layout as layout manager.
     *
     * @param layout  the FormLayout to use
     */
    public PanelBuilder(FormLayout layout){
        this(layout, new JPanel(null));
    }

    /**
     * Constructs a <code>PanelBuilder</code> for the given
     * FormLayout and layout container.
     *
     * @param layout  the FormLayout to use
     * @param panel   the layout container to build on
     */
    public PanelBuilder(FormLayout layout, JPanel panel){
        super(layout, panel);
        labelForFeatureEnabled = labelForFeatureEnabledDefault;
    }


    // Global Defaults ********************************************************

    /**
     * Returns the global default for the enablement of the setLabelFor feature.
     * This can be overridden per PanelBuilder using
     * {@link #setLabelForFeatureEnabled(boolean)}.
     * The feature is globally disabled by default.
     *
     * @return true for globally enabled, false for globally disabled
     */
    public static boolean getLabelForFeatureEnabledDefault() {
        return labelForFeatureEnabledDefault;
    }


    /**
     * Sets the default value for the setLabelFor feature enablement.
     * This can be overridden per PanelBuilder using
     * {@link #setLabelForFeatureEnabled(boolean)}.
     * The default value is used to set the initial PanelBuilder
     * setting for this feature.
     * The feature is globally disabled by default.
     *
     * @param b true for globally enabled, false for globally disabled
     */
    public static void setLabelForFeatureEnabledDefault(boolean b) {
        labelForFeatureEnabledDefault = b;
    }


    // Configuration **********************************************************

    /**
     * Returns whether the setLabelFor feature is enabled for this PanelBuilder.
     * The value is initialized from the global default value for this feature
     * {@link #getLabelForFeatureEnabledDefault()}. It is globally disabled
     * by default.
     *
     * @return true for enabled, false for disabled
     */
    public boolean isLabelForFeatureEnabled() {
        return labelForFeatureEnabled;
    }


    /**
     * Enables or disables the setLabelFor feature for this PanelBuilder.
     * The value is initialized from the global default value
     * {@link #getLabelForFeatureEnabledDefault()}. It is globally disabled
     * by default.
     *
     * @param b true for enabled, false for disabled
     */
    public void setLabelForFeatureEnabled(boolean b) {
        labelForFeatureEnabled = b;
    }


    // Accessors **************************************************************

    /**
     * Returns the panel used to build the form.
     *
     * @return the panel used by this builder to build the form
     */
    public final JPanel getPanel() {
        return (JPanel) getContainer();
    }


    // Frequently Used Panel Properties ***************************************

    /**
     * Sets the panel's background color.
     *
     * @param background  the color to set as new background
     *
     * @see JComponent#setBackground(Color)
     *
     * @since 1.1
     */
    public final void setBackground(Color background) {
        getPanel().setBackground(background);
    }


    /**
     * Sets the panel's border.
     *
     * @param border	the border to set
     *
     * @see JComponent#setBorder(Border)
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


    /**
     * Sets the panel's opaque state.
     *
     * @param b   true for opaque, false for non-opaque
     *
     * @see JComponent#setOpaque(boolean)
     *
     * @since 1.1
     */
    public final void setOpaque(boolean b) {
        getPanel().setOpaque(b);
    }


    // Adding Labels **********************************************************

    /**
     * Adds a textual label to the form using the default constraints.<p>
     *
     * <pre>
     * addLabel("Name:");       // No Mnemonic
     * addLabel("N&ame:");      // Mnemonic is 'a'
     * addLabel("Save &as:");   // Mnemonic is the second 'a'
     * addLabel("Look&&Feel:"); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param textWithMnemonic   the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the new label
     *
     * @see ComponentFactory
     */
    public final JLabel addLabel(String textWithMnemonic) {
        return addLabel(textWithMnemonic, cellConstraints());
    }


    /**
     * Adds a textual label to the form using the specified constraints.<p>
     *
     * <pre>
     * addLabel("Name:",       cc.xy(1, 1)); // No Mnemonic
     * addLabel("N&ame:",      cc.xy(1, 1)); // Mnemonic is 'a'
     * addLabel("Save &as:",   cc.xy(1, 1)); // Mnemonic is the second 'a'
     * addLabel("Look&&Feel:", cc.xy(1, 1)); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param textWithMnemonic  the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
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
     * addLabel("Name:",       "1, 1"); // No Mnemonic
     * addLabel("N&ame:",      "1, 1"); // Mnemonic is 'a'
     * addLabel("Save &as:",   "1, 1"); // Mnemonic is the second 'a'
     * addLabel("Look&&Feel:", "1, 1"); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param textWithMnemonic    the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @param encodedConstraints  a string representation for the constraints
     * @return the new label
     *
     * @see ComponentFactory
     */
    public final JLabel addLabel(String textWithMnemonic, String encodedConstraints) {
        return addLabel(textWithMnemonic, new CellConstraints(encodedConstraints));
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
     *     "&Name:",            // Mnemonic is 'N'
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
     *     "&Name:",           // Mnemonic is 'N'
     *     cc1.xy(1, 7),       // sets instance 1 to (1, 7)
     *     nameField,
     *     cc2.xy(3, 7)        // sets instance 2 to (3, 7)
     * );
     * </pre>
     *
     * @param textWithMnemonic      the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
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


    // Adding Labels for Read-Only Components ---------------------------------

    /**
     * Adds a textual label intended for labeling read-only components
     * to the form using the default constraints.<p>
     *
     * <pre>
     * addROLabel("Name:");       // No Mnemonic
     * addROLabel("N&ame:");      // Mnemonic is 'a'
     * addROLabel("Save &as:");   // Mnemonic is the second 'a'
     * addROLabel("Look&&Feel:"); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param textWithMnemonic   the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the new label
     *
     * @see ComponentFactory2
     *
     * @since 1.3
     */
    public final JLabel addROLabel(String textWithMnemonic) {
        return addROLabel(textWithMnemonic, cellConstraints());
    }


    /**
     * Adds a textual label intended for labeling read-only components
     * to the form using the specified constraints.<p>
     *
     * <pre>
     * addROLabel("Name:",       cc.xy(1, 1)); // No Mnemonic
     * addROLabel("N&ame:",      cc.xy(1, 1)); // Mnemonic is 'a'
     * addROLabel("Save &as:",   cc.xy(1, 1)); // Mnemonic is the second 'a'
     * addROLabel("Look&&Feel:", cc.xy(1, 1)); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param textWithMnemonic  the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @param constraints       the label's cell constraints
     * @return the new label
     *
     * @see ComponentFactory2
     *
     * @since 1.3
     */
    public final JLabel addROLabel(String textWithMnemonic, CellConstraints constraints) {
        ComponentFactory factory = getComponentFactory();
        ComponentFactory2 factory2;
        if (factory instanceof ComponentFactory2) {
            factory2 = (ComponentFactory2) factory;
        } else {
            factory2 = DefaultComponentFactory.getInstance();
        }
        JLabel label = factory2.createReadOnlyLabel(textWithMnemonic);
        add(label, constraints);
        return label;
    }


    /**
     * Adds a textual label intended for labeling read-only components
     * to the form using the specified constraints.<p>
     *
     * <pre>
     * addROLabel("Name:",       "1, 1"); // No Mnemonic
     * addROLabel("N&ame:",      "1, 1"); // Mnemonic is 'a'
     * addROLabel("Save &as:",   "1, 1"); // Mnemonic is the second 'a'
     * addROLabel("Look&&Feel:", "1, 1"); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param textWithMnemonic    the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @param encodedConstraints  a string representation for the constraints
     * @return the new label
     *
     * @see ComponentFactory2
     *
     * @since 1.3
     */
    public final JLabel addROLabel(String textWithMnemonic, String encodedConstraints) {
        return addROLabel(textWithMnemonic, new CellConstraints(encodedConstraints));
    }


    /**
     * Adds a label and component to the panel using the given cell constraints.
     * Sets the given label as <i>the</i> component label using
     * {@link JLabel#setLabelFor(java.awt.Component)}.<p>
     *
     * <strong>Note:</strong> The {@link CellConstraints} objects for the label
     * and the component must be different. Cell constraints are implicitly
     * cloned by the FormLayout when added to the container.
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
     * builder.addROLabel(
     *     "&Name:",            // Mnemonic is 'N'
     *     cc.xy(1, 7),         // will be modified by the code below
     *     nameField,
     *     cc.xy(3, 7)          // sets the single instance to (3, 7)
     * );
     * </pre>
     * <strong>Correct:</strong><pre>
     * // Using a single CellConstraints instance and cloning
     * CellConstraints cc = new CellConstraints();
     * builder.addROLabel(
     *     "&Name:",
     *     (CellConstraints) cc.xy(1, 7).clone(), // cloned before the next modification
     *     nameField,
     *     cc.xy(3, 7)                            // sets this instance to (3, 7)
     * );
     *
     * // Using two CellConstraints instances
     * CellConstraints cc1 = new CellConstraints();
     * CellConstraints cc2 = new CellConstraints();
     * builder.addROLabel(
     *     "&Name:",           // Mnemonic is 'N'
     *     cc1.xy(1, 7),       // sets instance 1 to (1, 7)
     *     nameField,
     *     cc2.xy(3, 7)        // sets instance 2 to (3, 7)
     * );
     * </pre>
     *
     * @param textWithMnemonic      the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @param labelConstraints      the label's cell constraints
     * @param component             the component to add
     * @param componentConstraints  the component's cell constraints
     * @return the added label
     * @throws IllegalArgumentException if the same cell constraints instance
     *     is used for the label and the component
     *
     * @see JLabel#setLabelFor(java.awt.Component)
     * @see ComponentFactory2
     * @see DefaultFormBuilder
     *
     * @since 1.3
     */
    public final JLabel addROLabel(
        String textWithMnemonic, CellConstraints labelConstraints,
        Component component,     CellConstraints componentConstraints) {

        if (labelConstraints == componentConstraints)
            throw new IllegalArgumentException(
                    "You must provide two CellConstraints instances, " +
                    "one for the label and one for the component.\n" +
                    "Consider using #clone(). See the JavaDocs for details.");

        JLabel label = addROLabel(textWithMnemonic, labelConstraints);
        add(component, componentConstraints);
        label.setLabelFor(component);
        return label;
    }



    // Adding Titles ----------------------------------------------------------

    /**
     * Adds a title label to the form using the default constraints.<p>
     *
     * <pre>
     * addTitle("Name");       // No mnemonic
     * addTitle("N&ame");      // Mnemonic is 'a'
     * addTitle("Save &as");   // Mnemonic is the second 'a'
     * addTitle("Look&&Feel"); // No mnemonic, text is Look&Feel
     * </pre>
     *
     * @param textWithMnemonic   the title label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the added title label
     *
     * @see ComponentFactory
     */
    public final JLabel addTitle(String textWithMnemonic) {
        return addTitle(textWithMnemonic, cellConstraints());
    }


    /**
     * Adds a title label to the form using the specified constraints.<p>
     *
     * <pre>
     * addTitle("Name",       cc.xy(1, 1)); // No mnemonic
     * addTitle("N&ame",      cc.xy(1, 1)); // Mnemonic is 'a'
     * addTitle("Save &as",   cc.xy(1, 1)); // Mnemonic is the second 'a'
     * addTitle("Look&&Feel", cc.xy(1, 1)); // No mnemonic, text is Look&Feel
     * </pre>
     *
     * @param textWithMnemonic   the title label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @param constraints        the separator's cell constraints
     * @return the added title label
     *
     * @see ComponentFactory
     */
    public final JLabel addTitle(String textWithMnemonic, CellConstraints constraints) {
        JLabel titleLabel = getComponentFactory().createTitle(textWithMnemonic);
        add(titleLabel, constraints);
        return titleLabel;
    }


    /**
     * Adds a title label to the form using the specified constraints.<p>
     *
     * <pre>
     * addTitle("Name",       "1, 1"); // No mnemonic
     * addTitle("N&ame",      "1, 1"); // Mnemonic is 'a'
     * addTitle("Save &as",   "1, 1"); // Mnemonic is the second 'a'
     * addTitle("Look&&Feel", "1, 1"); // No mnemonic, text is Look&Feel
     * </pre>
     *
     * @param textWithMnemonic   the title label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @param encodedConstraints  a string representation for the constraints
     * @return the added title label
     *
     * @see ComponentFactory
     */
    public final JLabel addTitle(String textWithMnemonic, String encodedConstraints) {
        return addTitle(textWithMnemonic, new CellConstraints(encodedConstraints));
    }


    // Adding Separators ------------------------------------------------------

    /**
     * Adds a titled separator to the form that spans all columns.<p>
     *
     * <pre>
     * addSeparator("Name");       // No Mnemonic
     * addSeparator("N&ame");      // Mnemonic is 'a'
     * addSeparator("Save &as");   // Mnemonic is the second 'a'
     * addSeparator("Look&&Feel"); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param textWithMnemonic   the separator label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the added separator
     */
    public final JComponent addSeparator(String textWithMnemonic) {
        return addSeparator(textWithMnemonic, getLayout().getColumnCount());
    }


    /**
     * Adds a titled separator to the form using the specified constraints.<p>
     *
     * <pre>
     * addSeparator("Name",       cc.xy(1, 1)); // No Mnemonic
     * addSeparator("N&ame",      cc.xy(1, 1)); // Mnemonic is 'a'
     * addSeparator("Save &as",   cc.xy(1, 1)); // Mnemonic is the second 'a'
     * addSeparator("Look&&Feel", cc.xy(1, 1)); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param textWithMnemonic   the separator label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @param constraints  the separator's cell constraints
     * @return the added separator
     */
    public final JComponent addSeparator(String textWithMnemonic, CellConstraints constraints) {
        int titleAlignment = isLeftToRight()
                ? SwingConstants.LEFT
                : SwingConstants.RIGHT;
        JComponent titledSeparator =
            getComponentFactory().createSeparator(textWithMnemonic, titleAlignment);
        add(titledSeparator, constraints);
        return titledSeparator;
    }


    /**
     * Adds a titled separator to the form using the specified constraints.<p>
     *
     * <pre>
     * addSeparator("Name",       "1, 1"); // No Mnemonic
     * addSeparator("N&ame",      "1, 1"); // Mnemonic is 'a'
     * addSeparator("Save &as",   "1, 1"); // Mnemonic is the second 'a'
     * addSeparator("Look&&Feel", "1, 1"); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param textWithMnemonic   the separator label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @param encodedConstraints  a string representation for the constraints
     * @return the added separator
     */
    public final JComponent addSeparator(String textWithMnemonic, String encodedConstraints) {
        return addSeparator(textWithMnemonic, new CellConstraints(encodedConstraints));
    }


    /**
     * Adds a titled separator to the form that spans the specified columns.<p>
     *
     * <pre>
     * addSeparator("Name",       3); // No Mnemonic
     * addSeparator("N&ame",      3); // Mnemonic is 'a'
     * addSeparator("Save &as",   3); // Mnemonic is the second 'a'
     * addSeparator("Look&&Feel", 3); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param textWithMnemonic   the separator label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @param columnSpan	the number of columns the separator spans
     * @return the added separator
     */
    public final JComponent addSeparator(String textWithMnemonic, int columnSpan) {
        return addSeparator(textWithMnemonic, createLeftAdjustedConstraints(columnSpan));
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
    public final ComponentFactory getComponentFactory() {
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


    // Overriding Superclass Behavior *****************************************

    /**
     * Adds a component to the panel using the given cell constraints.
     * In addition to the superclass behavior, this implementation
     * tracks the most recently added label, and associates it with
     * the next added component that is applicable for being set as component
     * for the label.
     *
     * @param component        the component to add
     * @param cellConstraints  the component's cell constraints
     * @return the added component
     *
     * @see #isLabelForFeatureEnabled()
     * @see #isLabelForApplicable(JLabel, Component)
     */
    public Component add(Component component, CellConstraints cellConstraints) {
        Component result = super.add(component, cellConstraints);
        if (!isLabelForFeatureEnabled()) {
            return result;
        }
        JLabel mostRecentlyAddedLabel = getMostRecentlyAddedLabel();
        if (   (mostRecentlyAddedLabel != null)
            && isLabelForApplicable(mostRecentlyAddedLabel, component)) {
            setLabelFor(mostRecentlyAddedLabel, component);
            clearMostRecentlyAddedLabel();
        }
        if (component instanceof JLabel) {
            setMostRecentlyAddedLabel((JLabel) component);
        }
        return result;
    }


    // Default Behavior *******************************************************

    /**
     * Checks and answers whether the given component shall be set
     * as component for a previously added label using
     * {@link JLabel#setLabelFor(Component)}.
     *
     * This default implementation checks whether the component is focusable,
     * and - if a JComponent - whether it is already labeled by a JLabel.
     * Subclasses may override.
     *
     * @param label        the candidate for labeling {@code component}
     * @param component    the component that could be labeled by {@code label}
     * @return true if focusable, false otherwise
     */
    protected boolean isLabelForApplicable(JLabel label, Component component) {
        // 1) Is the label labeling a component?
        if (label.getLabelFor() != null) {
            return false;
        }

        // 2) Is the component focusable?
        if (!component.isFocusable()) {
            return false;
        }

        // 3) Is the component labeled by another label?
        if (!(component instanceof JComponent)) {
            return true;
        }
        JComponent c = (JComponent) component;
        return c.getClientProperty(LABELED_BY_PROPERTY) == null;
    }


    /**
     * Sets {@code label} as labeling label for {@code component} or an
     * appropriate child. In case of a JScrollPane as given component,
     * this default implementation labels the view of the scroll pane's
     * viewport.
     *
     * @param label      the labeling label
     * @param component  the component to be labeled, or the parent of
     *   the labeled component
     */
    protected void setLabelFor(JLabel label, Component component) {
        Component labeledComponent;
        if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            labeledComponent = scrollPane.getViewport().getView();
        } else {
            labeledComponent = component;
        }
        label.setLabelFor(labeledComponent);
    }


    // Helper Code ************************************************************

    /**
     * Returns the most recently added JLabel that has a mnemonic set
     * - if any, <code>null</code>, if none has been set, or if it has
     * been cleared after setting an association before, or if it has been
     * cleared by the garbage collector.
     *
     * @return the most recently added JLabel that has a mnemonic set
     *     and has not been associated with a component applicable for this
     *     feature. <code>null</code> otherwise.
     */
    private JLabel getMostRecentlyAddedLabel() {
        if (mostRecentlyAddedLabelReference == null) {
            return null;
        }
        JLabel label = (JLabel) mostRecentlyAddedLabelReference.get();
        if (label == null) {
            return null;
        }
        return label;
    }


    /**
     * Sets the given label as most recently added label using a weak reference.
     *
     * @param label  the label to be set
     */
    private void setMostRecentlyAddedLabel(JLabel label) {
        mostRecentlyAddedLabelReference = new WeakReference(label);
    }


    /**
     * Clears the reference to the most recently added mnemonic label.
     */
    private void clearMostRecentlyAddedLabel() {
        mostRecentlyAddedLabelReference = null;
    }


}
