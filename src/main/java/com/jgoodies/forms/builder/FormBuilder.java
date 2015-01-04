/*
 * Copyright (c) 2002-2014 JGoodies Software GmbH. All Rights Reserved.
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
 *  o Neither the name of JGoodies Software GmbH nor the names of
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
import static com.jgoodies.common.base.Preconditions.checkState;
import static com.jgoodies.common.internal.Messages.MUST_NOT_BE_NULL;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.FocusTraversalPolicy;
import java.lang.ref.WeakReference;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.jgoodies.common.base.Strings;
import com.jgoodies.forms.FormsSetup;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.factories.ComponentFactory;
import com.jgoodies.forms.factories.Forms;
import com.jgoodies.forms.factories.Paddings;
import com.jgoodies.forms.internal.FocusTraversalUtilsAccessor;
import com.jgoodies.forms.internal.InternalFocusSetupUtils;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.CellConstraints.Alignment;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.LayoutMap;
import com.jgoodies.forms.layout.RowSpec;

/**
 * An general purpose form builder that uses the {@link FormLayout}
 * to lay out {@code JPanel}s. It provides convenience methods
 * to set a default border and to add labels, titles and titled separators.
 * The FormBuilder is the working horse for layouts when more specialized
 * builders like the {@link ButtonBarBuilder} are inappropriate.<p>
 *
 * The text arguments passed to the methods {@code #addLabel},
 * {@code #addTitle}, and {@code #addSeparator} can contain
 * an optional mnemonic marker. The mnemonic and mnemonic index
 * are indicated by a single ampersand (<tt>&amp;</tt>). For example
 * <tt>&quot;&amp;Save&quot</tt>, or <tt>&quot;Save&nbsp;&amp;as&quot</tt>.
 * To use the ampersand itself duplicate it, for example
 * <tt>&quot;Look&amp;&amp;Feel&quot</tt>.<p>
 *
 * <strong>Example:</strong><br>
 * This example creates a panel with 3 columns and 3 rows.
 * <pre>
 * return FormBuilder.create()
 *     .columns("pref, $lcgap, 50dlu, $rgap, default")
 *     .rows("pref, $lg, pref, $lg, pref")
 * 
 *     .add("&Title:")   .xy  (1, 1)
 *     .add(titleField)  .xywh(3, 1, 3, 1)
 *     .add("&Price:")   .xy  (1, 3)
 *     .add(priceField)  .xy  (3, 3)
 *     .add("&Author:")  .xy  (1, 5)
 *     .add(authorField) .xy  (3, 5)
 *     .add(browseButton).xy  (5, 5)
 *     .build();
 * </pre>
 *
 * @author  Karsten Lentzsch
 *
 * @see	FormLayout
 * 
 * @since 1.9
 */
public final class FormBuilder {
    

    // Constants **************************************************************

    /**
     * A JComponent client property that is used to determine the label
     * labeling a component. Copied from the JLabel class.
     */
    private static final String LABELED_BY_PROPERTY = "labeledBy";
    
    /**
     * @see #defaultLabelType
     */
    public static enum LabelType { DEFAULT, READ_ONLY }


    // Instance Fields ********************************************************

    private LayoutMap layoutMap;
    
    private ColumnSpec[] columnSpecs;
    private RowSpec[] rowSpecs;
    private FormLayout layout;
    
    private JPanel panel;
    
    private JComponent initialComponent;
    private FocusTraversalType focusTraversalType;
    
    private FocusTraversalPolicy focusTraversalPolicy;
    
    private boolean debug;

    private int offsetX = 0;
    
    private int offsetY = 0;
    
    /**
     * The instance value for the setLabelFor feature.
     * Is initialized using the global default.
     *
     * @see #setLabelForFeatureEnabled(boolean)
     * @see #setLabelForFeatureEnabledDefault(boolean)
     */
    private boolean labelForFeatureEnabled;
    
    private LabelType defaultLabelType = LabelType.DEFAULT;
    
    private ComponentFactory factory;
    
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

    private FormBuilder(){
        labelForFeatureEnabled(FormsSetup.getLabelForFeatureEnabledDefault());
        offsetX = 0;
        offsetY = 0;
    }
    
    
    public static FormBuilder create() {
        return new FormBuilder();
    }


    // Building ***************************************************************

    /**
     * Returns the panel used to build the form.
     * Intended to return the panel in build methods.
     *
     * @return the panel used by this builder to build the form
     */
    public JPanel build() {
        return getPanel();
    }

    
    // Layout Setup ***********************************************************
    
    public FormBuilder layoutMap(LayoutMap layoutMap) {
        this.layoutMap = layoutMap;
        return this;
    }
    

    public FormBuilder columns(String encodedColumnSpecs, Object... args) {
        columnSpecs = ColumnSpec.decodeSpecs(
                Strings.get(encodedColumnSpecs, args), getLayoutMap());
        return this;
    }
    

    public FormBuilder appendColumns(String encodedColumnSpecs, Object... args) {
        ColumnSpec[] newColumnSpecs = ColumnSpec.decodeSpecs(
                Strings.get(encodedColumnSpecs, args), getLayoutMap());
        for (ColumnSpec columnSpec : newColumnSpecs) {
            getLayout().appendColumn(columnSpec);
        }
        return this;
    }
    

    public FormBuilder rows(String encodedRowSpecs, Object... args) {
        rowSpecs = RowSpec.decodeSpecs(
                Strings.get(encodedRowSpecs, args), getLayoutMap());
        return this;
    }
    

    public FormBuilder appendRows(String encodedRowSpecs, Object... args) {
        RowSpec[] newRowSpecs = RowSpec.decodeSpecs(
                Strings.get(encodedRowSpecs, args), getLayoutMap());
        for (RowSpec rowSpec : newRowSpecs) {
            getLayout().appendRow(rowSpec);
        }
        return this;
    }
    

    public FormBuilder columnGroup(int... singleGroupIndices) {
        getLayout().setColumnGroup(singleGroupIndices);
        return this;
    }
    
    
    public FormBuilder columnGroups(int[]... multipleGroups) {
        getLayout().setColumnGroups(multipleGroups);
        return this;
    }
    

    public FormBuilder rowGroup(int... singleGroupIndices) {
        getLayout().setRowGroup(singleGroupIndices);
        return this;
    }
    

    public FormBuilder rowGroups(int[]... multipleGroups) {
        getLayout().setRowGroups(multipleGroups);
        return this;
    }
    

    public FormBuilder honorsVisibility(boolean b) {
        getLayout().setHonorsVisibility(b);
        return this;
    }
    

    public FormBuilder honorsVisibility(JComponent c, boolean b) {
        getLayout().setHonorsVisibility(c, b);
        return this;
    }
    

    public FormBuilder layout(FormLayout layout) {
        this.layout = checkNotNull(layout, MUST_NOT_BE_NULL, "layout");
        return this;
    }
    

    public FormBuilder panel(JPanel panel) {
        this.panel = checkNotNull(panel, MUST_NOT_BE_NULL, "panel");
        this.panel.setLayout(getLayout());
        return this;
    }
    

    public FormBuilder debug(boolean b) {
        this.debug = b;
        return this;
    }
    
    
    public FormBuilder name(String panelName) {
        getPanel().setName(panelName);
        return this;
    }
    

    // Panel Properties *******************************************************

    /**
     * Sets the panel's background color and the panel to be opaque.
     *
     * @param background  the color to set as new background
     *
     * @see JComponent#setBackground(Color)
     */
    public FormBuilder background(Color background) {
    	getPanel().setBackground(background);
        opaque(true);
        return this;
    }


    /**
     * Sets the panel's border.
     *
     * @param border	the border to set
     *
     * @see #padding(EmptyBorder)
     * @see JComponent#setBorder(Border)
     */
    public FormBuilder border(Border border) {
    	getPanel().setBorder(border);
        return this;
    }


    /**
     * Sets the panel's padding as an EmptyBorder using the given specification
     * for the top, left, bottom, right margins in DLU. For example
     * "1dlu, 2dlu, 3dlu, 4dlu" sets a padding with 1dlu in the top,
     * 2dlu in the left side, 3dlu at the bottom, and 4dlu in the right hand
     * side.<p>
     *
     * Equivalent to {@code setPadding(Paddings.createPadding(paddingSpec))}.
     *
     * @param paddingSpec   describes the top, left, bottom, right margins
     *    of the padding (an EmptyBorder) to use
     *
     * @see Paddings#createPadding(String)
     * @deprecated Use {@link #padding(String)} instead
     */
    @Deprecated
    public FormBuilder border(String paddingSpec) {
        return padding(paddingSpec);
    }


    /**
     * Sets the panel's padding, an empty border.
     *
     * @param padding    the white space around this form
     *
     * @see #border
     * 
     * @since 1.9
     */
    public FormBuilder padding(EmptyBorder padding) {
        getPanel().setBorder(padding);
        return this;
    }


    /**
     * Sets the panel's padding as an EmptyBorder using the given specification
     * for the top, left, bottom, right margins in DLU. For example
     * "1dlu, 2dlu, 3dlu, 4dlu" sets an empty border with 1dlu in the top,
     * 2dlu in the left side, 3dlu at the bottom, and 4dlu in the right hand
     * side.<p>
     *
     * Equivalent to {@code setPadding(Paddings.createPadding(paddingSpec))}.
     *
     * @param paddingSpec   describes the top, left, bottom, right margins
     *    of the padding (an EmptyBorder) to use
     *
     * @see #padding(EmptyBorder)
     * @see Paddings#createPadding(String)
     * 
     * @since 1.9
     */
    public FormBuilder padding(String paddingSpec) {
    	padding(Paddings.createPadding(paddingSpec));
    	return this;
    }


    /**
     * Sets the panel's opaque state.
     *
     * @param b   true for opaque, false for non-opaque
     *
     * @see JComponent#setOpaque(boolean)
     */
    public FormBuilder opaque(boolean b) {
        getPanel().setOpaque(b);
        return this;
    }
    
    
    /**
     * Sets a component that should receive the focus when a Window is
     * made visible for the first time. For details see
     * {@link FocusTraversalPolicy#getInitialComponent(java.awt.Window)}.
     * 
     * @param initialComponent   the component that shall receive the focus
     * @return a reference to this builder
     */
    public FormBuilder initialComponent(JComponent initialComponent) {
        checkState(this.initialComponent == null,
                "The initial component must be set once only.");
        checkValidFocusTraversalSetup();
        this.initialComponent = initialComponent;
        setupFocusTraversalPolicyAndProvider();
        return this;
    }
    
    
    /**
     * Sets either a layout or container order focus traversal policy.
     * If the commercial {@code JGContainerOrderFocusTraversalPolicy} and
     * {@code JGLayoutFocusTraversalPolicy} are in the class path,
     * these will be used. Otherwise the standard Swing
     * {@link ContainerOrderFocusTraversalPolicy} and
     * {@link LayoutFocusTraversalPolicy} respectively will be used.
     * 
     * @param focusTraversalType   specifies the type: layout or container order
     * @return a reference to this builder
     * 
     * @see #focusTraversalPolicy(FocusTraversalPolicy)
     * 
     * @throws NullPointerException if {@code focusTraversalType} is {@code null}
     */
    public FormBuilder focusTraversalType(FocusTraversalType focusTraversalType) {
        checkNotNull(focusTraversalType, MUST_NOT_BE_NULL, "focus traversal type");
        checkState(this.focusTraversalType == null,
                "The focus traversal type must be set once only.");
        checkValidFocusTraversalSetup();
        this.focusTraversalType = focusTraversalType;
        setupFocusTraversalPolicyAndProvider();
        return this;
    }
    
    
    /**
     * Sets the panel's focus traversal policy and sets the panel
     * as focus traversal policy provider. You should favor setting
     * the focus traversal policy <em>type</em> over setting a concrete
     * <em>policy</em>, because the type is toolkit-independent
     * and may be reused if your code is transferred to another toolkit.<p>
     * 
     * A call to this method is only necessary, if you set a custom Swing
     * focus traversal policy other than {@link LayoutFocusTraversalPolicy}
     * or {@link ContainerOrderFocusTraversalPolicy} (or their commercial
     * replacements {@code JGLayoutFocusTraversalPolicy} or
     * {@code JGContainerOrderFocusTraversalPolicy}).<p>
     * 
     * Call to this method are equivalent to:
     * <pre>
     * builder.getPanel().setFocusTraversalPolicy(policy);
     * builder.getPanel().setFocusTraversalPolicyProvider(true);
     * </pre>
     *
     * @param policy   the focus traversal policy that will manage
     * 	keyboard traversal of the children in this builder's panel
     *
     * @see #focusTraversalType(FocusTraversalType)
     * @see JComponent#setFocusTraversalPolicy(FocusTraversalPolicy)
     * @see JComponent#setFocusTraversalPolicyProvider(boolean)
     * 
     * @throws NullPointerException if {@code focusTraversalType} is {@code null}
     */
    public FormBuilder focusTraversalPolicy(FocusTraversalPolicy policy) {
        checkNotNull(focusTraversalPolicy, MUST_NOT_BE_NULL, "focus traversal policy");
        checkState(this.focusTraversalPolicy == null,
                "The focus traversal policy must be set once only.");
        checkValidFocusTraversalSetup();
        this.focusTraversalPolicy = policy;
        setupFocusTraversalPolicyAndProvider();
        return this;
    }
    
    
    public FormBuilder focusGroup(AbstractButton... buttons) {
        // The following call requires that the FocusTraversalUtils class
        // is in the class path. If we move the FormBuilder to a lower level
        // library, the call should be replaced by the commented line.
        // FocusTraversalUtils.group(buttons);
        FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
        return this;
    }


    /**
     * Returns the panel used to build the form.
     * Intended to access panel properties. For returning the built panel,
     * you should use {@link #build()}.
     *
     * @return the panel used by this builder to build the form
     */
    public JPanel getPanel() {
        if (panel == null) {
            // We'd like to say:
            // panel = new JPanel(getLayout);
            // but we use a null layout instead, because this method is invoked
            // early during the builder construction, where the layout is not
            // specified. As a result, we check that a layout is set in
            // #addImpl(Component).
            panel = debug ? new FormDebugPanel() : new JPanel(null);
            panel.setOpaque(FormsSetup.getOpaqueDefault());
        }
        return panel;
    }
    
    
    // Adding Components ******************************************************

    public FormBuilder factory(ComponentFactory factory) {
        this.factory = factory;
        return this;
    }
    
    
    /**
     * Enables or disables the setLabelFor feature for this builder.
     * The value is initialized from the global default value
     * {@link FormsSetup#getLabelForFeatureEnabledDefault()}.
     * It is globally disabled by default.
     *
     * @param b true for enabled, false for disabled
     */
    public FormBuilder labelForFeatureEnabled(boolean b) {
        labelForFeatureEnabled = b;
        return this;
    }
    
    
    /**
     * When adding components, the cell constraints origin are moved
     * along the X and Y axis using an offset
     * as specified by {@code offsetX} and {@code offsetY} respectively.<p>
     * 
     * This operation is not cumulative. In other words,
     * setting the offset overrides the previously set offset. For example:
     * <pre>
     * builder
     *     .offset(0, 2)
     *     .offset(1, 3)
     *     .offset(4, 8)
     * </pre> ends with an offset of (4, 8).
     * 
     * @param offsetX  the distance to move cell constraints along the X axis
     * @param offsetY  the distance to move cell constraints along the Y axis
     * 
     * @see #translate(int, int)
     */
    public FormBuilder offset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return this;
    }


    /**
     * Moves the cell constraints offset along the X and Y axis
     * as specified by {@code dx} and {@code dy} respectively.<p>
     * 
     * This operation is cumulative for the offset. In other words, every
     * translation is added to all previously set translations. For example:
     * <pre>builder
     *     .offset(0, 0)
     *     .translate(0, 2)
     *     .translate(1, 3)
     *     .translate(4, 8)</pre> ends with an offset of (5, 13).
     * 
     * @param dX  the distance to move the offset along the X axis
     * @param dY  the distance to move the offset along the Y axis
     * 
     * @see #offset(int, int)
     */
    public FormBuilder translate(int dX, int dY) {
        this.offsetX += dX;
        this.offsetY += dY;
        return this;
    }
    
    
    /**
     * Sets a new value for the default label type that is used to determine
     * whether {@link #add(String, Object...)} delegates to
     * {@link #addLabel(String, Object...)}
     * or {@link #addROLabel(String, Object...)}.
     * 
     * @param newValue   the default label type
     * @return a reference to this builder
     */
    public FormBuilder defaultLabelType(LabelType newValue) {
        this.defaultLabelType = newValue;
        return this;
    }


    /**
     * Gets a component that will be added to this builder's panel,
     * if the cell constraints are specified.<p>
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .add(nameField)   .xy(1, 1)
     *    .add(countryCombo).xy(3, 3)
     *    ...
     *    .build();
     * </pre>

     * If the label-for-feature is enabled, the most recently added label
     * is tracked and associate with the next added component
     * that is applicable for being set as component for the label.
     *
     * @param c        the component to add
     * @return the fluent interface part used to set the cell constraints
     *
     * @see #isLabelForApplicable(JLabel, Component)
     */
    public ComponentAdder add(Component c) {
        return add(true, c);
    }
    
    
    public ComponentAdder addBar(JButton... buttons) {
        return addBar(true, buttons);
    }


    public ComponentAdder addBar(JCheckBox... checkBoxes) {
        return addBar(true, checkBoxes);
    }


    public ComponentAdder addBar(JRadioButton... radioButtons) {
        return addBar(true, radioButtons);
    }


    public ComponentAdder addStack(JButton... buttons) {
        return addStack(true, buttons);
    }


    public ComponentAdder addStack(JCheckBox... checkBoxes) {
        return addStack(true, checkBoxes);
    }


    public ComponentAdder addStack(JRadioButton... radioButtons) {
        return addStack(true, radioButtons);
    }


    public ComponentAdder addRaw(Component c) {
        return addRaw(true, c);
    }
    
    
    /**
     * Wraps the given component with a JScrollPane
     * and adds it to the container using the specified constraints.
     * Layout equivalent to: {@code add(new JScrollPane(c), constraints);}
     *
     * @param c              the component to be wrapped and added
     * @return the fluent interface part used to set the cell constraints
     */
    public ComponentAdder addScrolled(Component c) {
        return addScrolled(true, c);
    }


    /**
     * Builds the given view into this FormBuilder's form.<p>
     * 
     * <b>Note: This is an experimental feature that is not yet
     * part of the public FormBuilder API.</b> It may change
     * without further notice.
     * 
     * @param view   the view to integrate
     * @return the fluent interface part used to set the view's origin
     */
    public ViewAdder add(FormBuildingView view) {
        return add(true, view);
    }


    /**
     * Adds a label; equivalent to: {@code addLabel(markedLabelText)}
     * or {@code addROLabel(markedLabelText)} depending on
     * the <em>defaultLabelType</em> property.
     * 
     * @param markedLabelText  the text of the label to be added,
     *     may contain a mnemonic marker
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see #defaultLabelType(LabelType)
     */
    public ComponentAdder add(String markedLabelText, Object... args) {
        return add(true, markedLabelText, args);
    }


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
     * @param markedText   the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the fluent interface part used to set the cell constraints
     *
     * @see ComponentFactory
     */
    public ComponentAdder addLabel(String markedText, Object... args) {
        return addLabel(true, markedText, args);
    }


    /**
     * Adds a textual label intended for labeling read-only components
     * to the form.<p>
     *
     * <pre>
     * addROLabel("Name:");       // No Mnemonic
     * addROLabel("N&ame:");      // Mnemonic is 'a'
     * addROLabel("Save &as:");   // Mnemonic is the second 'a'
     * addROLabel("Look&&Feel:"); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param markedText   the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the fluent interface part used to set the cell constraints
     */
    public ComponentAdder addROLabel(String markedText, Object... args) {
        return addROLabel(true, markedText, args);
    }


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
     * @param markedText   the title label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the fluent interface part used to set the cell constraints
     *
     * @see ComponentFactory
     */
    public ComponentAdder addTitle(String markedText, Object... args) {
        return addTitle(true, markedText, args);
    }


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
     * @param markedText   the separator label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the fluent interface part used to set the cell constraints
     */
    public ComponentAdder addSeparator(String markedText, Object... args) {
        return addSeparator(true, markedText, args);
    }
    
    
    // Adding Components Depending on an Expression ***************************
    
    /**
     * Gets a component that will be added to this builder's panel,
     * if the cell constraints are specified.<p>
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .add(nameField)               .xy(1, 1)
     *    .add(hasCountry, countryCombo).xy(3, 3)
     *    ...
     *    .build();
     * </pre>
    
     * If the label-for-feature is enabled, the most recently added label
     * is tracked and associate with the next added component
     * that is applicable for being set as component for the label.
     *
     * @param expression    the precondition to add the component
     * @param c        the component to add
     * @return the fluent interface part used to set the cell constraints
     *
     * @see #isLabelForApplicable(JLabel, Component)
     */
    public ComponentAdder add(boolean expression, Component c) {
        if (!expression || c == null) {
            return new NoOpComponentAdder(this);
        }
        if (c instanceof JTable || c instanceof JList || c instanceof JTree) {
            return addScrolled(expression, c);
        }
        return addRaw(expression, c);
    }


    public ComponentAdder addBar(boolean expression, JButton... buttons) {
        if (!expression || buttons == null) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.buttonBar(buttons));
    }


    public ComponentAdder addBar(boolean expression, JCheckBox... checkBoxes) {
        if (!expression) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.checkBoxBar(checkBoxes));
    }


    public ComponentAdder addBar(boolean expression, JRadioButton... radioButtons) {
        if (!expression) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.radioButtonBar(radioButtons));
    }


    public ComponentAdder addStack(boolean expression, JButton... buttons) {
        if (!expression || buttons == null) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.buttonStack(buttons));
    }


    public ComponentAdder addStack(boolean expression, JCheckBox... checkBoxes) {
        if (!expression) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.checkBoxStack(checkBoxes));
    }


    public ComponentAdder addStack(boolean expression, JRadioButton... radioButtons) {
        if (!expression || radioButtons == null) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(Forms.radioButtonStack(radioButtons));
    }


    public ComponentAdder addRaw(boolean expression, Component c) {
        if (!expression || c == null) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(c);
    }


    /**
     * Wraps the given component with a JScrollPane
     * and adds it to the container using the specified constraints.
     * Layout equivalent to: {@code add(new JScrollPane(c), constraints);}
     *
     * @param c              the component to be wrapped and added
     * @return the fluent interface part used to set the cell constraints
     */
    public ComponentAdder addScrolled(boolean expression, Component c) {
        if (!expression || c == null) {
            return new NoOpComponentAdder(this);
        }
        return addImpl(new JScrollPane(c));
    }


    /**
     * Builds the given view into this FormBuilder's form.
     * 
     * @param view   the view to integrate
     * @return the fluent interface part used to set the view's origin
     */
    public ViewAdder add(boolean expression, FormBuildingView view) {
        return new ViewAdder(this, expression, view);
    }


    /**
     * Adds a label; equivalent to: {@code addLabel(markedLabelText)}
     * or {@code addROLabel(markedLabelText)} depending on
     * the <em>defaultLabelType</em> property.
     * 
     * @param markedLabelText  the text of the label to be added,
     *     may contain a mnemonic marker
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see #defaultLabelType(LabelType)
     */
    public ComponentAdder add(boolean expression, String markedLabelText, Object... args) {
        return defaultLabelType == LabelType.DEFAULT
                ? addLabel  (expression, markedLabelText, args)
                : addROLabel(expression, markedLabelText, args);
    }


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
     * @param markedText   the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the fluent interface part used to set the cell constraints
     *
     * @see ComponentFactory
     */
    public ComponentAdder addLabel(boolean expression, String markedText, Object... args) {
        return addRaw(expression,
                      getFactory().createLabel(Strings.get(markedText, args)));
    }


    /**
     * Adds a textual label intended for labeling read-only components
     * to the form.<p>
     *
     * <pre>
     * addROLabel("Name:");       // No Mnemonic
     * addROLabel("N&ame:");      // Mnemonic is 'a'
     * addROLabel("Save &as:");   // Mnemonic is the second 'a'
     * addROLabel("Look&&Feel:"); // No mnemonic, text is "look&feel"
     * </pre>
     *
     * @param markedText   the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the fluent interface part used to set the cell constraints
     */
    public ComponentAdder addROLabel(boolean expression,
            String markedText, Object... args) {
        return addRaw(expression,
                getFactory().createReadOnlyLabel(Strings.get(markedText, args)));
    }


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
     * @param markedText   the title label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the fluent interface part used to set the cell constraints
     *
     * @see ComponentFactory
     */
    public ComponentAdder addTitle(boolean expression,
            String markedText, Object... args) {
        String text = Strings.get(markedText, args);
        return addRaw(expression, getFactory().createTitle(text));
    }


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
     * @param markedText   the separator label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return the fluent interface part used to set the cell constraints
     */
    public ComponentAdder addSeparator(boolean expression,
            String markedText, Object... args) {
        int alignment = isLeftToRight()
                ? SwingConstants.LEFT
                : SwingConstants.RIGHT;
        String text = Strings.get(markedText, args);
        return addRaw(expression,
                getFactory().createSeparator(text, alignment));
    }


    // Access to Lazily Created Objects ***************************************
    
    protected LayoutMap getLayoutMap() {
        if (layoutMap == null) {
            layoutMap = LayoutMap.getRoot();
        }
        return layoutMap;
    }
    
    
    protected FormLayout getLayout() {
        if (layout != null) {
            return layout;
        }
        checkNotNull(columnSpecs, "The layout columns must be specified.");
        checkNotNull(rowSpecs,    "The layout rows must be specified.");
        layout = new FormLayout(columnSpecs, rowSpecs);
        return layout;
    }
    
    
    protected ComponentFactory getFactory() {
        if (factory == null) {
            factory = FormsSetup.getComponentFactoryDefault();
        }
        return factory;
    }
    
    
    // Adding with Label For Feature ******************************************

    protected ComponentAdder addImpl(Component c) {
        // First ensure, that the panel has a layout set.
        if (getPanel().getLayout() == null) {
            panel.setLayout(getLayout());
        }
        return new ComponentAdder(this, c);
    }
    
    
    void addImpl(Component component, CellConstraints rawConstraints) {
        CellConstraints translatedConstraints = rawConstraints.translate(offsetX, offsetY);
        getPanel().add(component, translatedConstraints);
        manageLabelsAndComponents(component);
   }
    
    
    private void manageLabelsAndComponents(Component c) {
        if (!labelForFeatureEnabled) {
            return;
        }
        if (c instanceof JLabel) {
            JLabel label = (JLabel) c;
            if (label.getLabelFor() == null) {
                setMostRecentlyAddedLabel(label);
            } else {
                clearMostRecentlyAddedLabel();
            }
            return;
        }
        JLabel mostRecentlyAddedLabel = getMostRecentlyAddedLabel();
        if (   mostRecentlyAddedLabel != null
            && isLabelForApplicable(mostRecentlyAddedLabel, c)) {
            setLabelFor(mostRecentlyAddedLabel, c);
            clearMostRecentlyAddedLabel();
        }
    }


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
    private static boolean isLabelForApplicable(JLabel label, Component component) {
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
    private static void setLabelFor(JLabel label, Component component) {
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
     * - if any, {@code null}, if none has been set, or if it has
     * been cleared after setting an association before, or if it has been
     * cleared by the garbage collector.
     *
     * @return the most recently added JLabel that has a mnemonic set
     *     and has not been associated with a component applicable for this
     *     feature. {@code null} otherwise.
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
    
    
    private boolean isLeftToRight() {
        ComponentOrientation orientation = getPanel().getComponentOrientation();
        return orientation.isLeftToRight()
                  || !orientation.isHorizontal();
    }
    
    
    /**
     * Checks that if the API user has set a focus traversal policy,
     * no focus traversal type and no initial component has been set.
     */
    private void checkValidFocusTraversalSetup() {
        InternalFocusSetupUtils.checkValidFocusTraversalSetup(
                focusTraversalPolicy, focusTraversalType, initialComponent);
    }
    
    
    private void setupFocusTraversalPolicyAndProvider() {
        InternalFocusSetupUtils.setupFocusTraversalPolicyAndProvider(
                    getPanel(),
                    focusTraversalPolicy,
                    focusTraversalType,
                    initialComponent);
    }
    

    // Interfaces *************************************************************
    
    /**
     * Describes a view that can be integrated into an existing form
     * using {@link FormBuilder#add(FormBuildingView)}. It is intended
     * to reduce the effort for embedding subpanels that are aligned
     * with an outer layout context.<p>
     * 
     * <b>Note: This is an experimental interface that is not yet
     * part of the public FormBuilder API.</b> It may change without
     * further notice.
     */
    public interface FormBuildingView {
        
        /**
         * Integrates this view into the form that is built by the given builder.
         * 
         * @param builder   provides the layout, grid, panel, etc.
         */
        void buildInto(FormBuilder builder);
        
    }
    
    
    // Fluent Interface for Integrating a FormBuildingView ********************
    
    public static final class ViewAdder {
        
        private final FormBuilder builder;
        private final boolean expression;
        private final FormBuildingView view;
        
        ViewAdder(FormBuilder builder, boolean expression, FormBuildingView view) {
            this.builder = builder;
            this.expression = expression;
            this.view = view;
        }

        /**
         * Sets column and row origins of the view to integrate.
         *
         * @param col     the view's column origin
         * @param row     the view's row origin
         * @return a reference to the builder
         */
        public FormBuilder xy(int col, int row) {
            if (expression && view != null) {
                builder.translate(col, row);
                view.buildInto(builder);
                builder.translate(-col, -row);
            }
            return builder;
        }

    }
    

    // Fluent Interface for Adding Components *********************************
    
    public static class ComponentAdder {
        
        protected final FormBuilder builder;
        private final Component component;
        private boolean labelForSet;
        
        // Instance Creation --------------------------------------------------
        
        ComponentAdder(FormBuilder builder, Component component) {
            this.builder = builder;
            this.component = component;
            this.labelForSet = false;
        }
        
        
        public ComponentAdder labelFor(Component c) {
            checkArgument(component instanceof JLabel, "#labelFor is applicable only to JLabels");
            checkArgument(!labelForSet, "You must set the label-for-relation only once.");
            ((JLabel) component).setLabelFor(c);
            labelForSet = true;
            return this;
        }
        
        
        /**
         * Sets the given cell constraints.
         * @param constraints    specifies where an how to place a component
         * @return a reference to the builder
         */
        public final FormBuilder at(CellConstraints constraints) {
            return add(constraints);
        }
        
        

        // Column-Row Order ---------------------------------------------------

        /**
         * Sets column and row origins; sets width and height to 1;
         * uses the default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.xy(1, 1);
         * CC.xy(1, 3);
         * </pre>
         *
         * @param col     the new column index
         * @param row     the new row index
         * @return a reference to the builder
         */
        public FormBuilder xy(int col, int row) {
            return at(CC.xy(col, row));
        }


        /**
         * Sets column and row origins; sets width and height to 1;
         * decodes horizontal and vertical alignments from the given string.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.xy(1, 3, "left, bottom");
         * CC.xy(1, 3, "l, b");
         * CC.xy(1, 3, "center, fill");
         * CC.xy(1, 3, "c, f");
         * </pre>
         *
         * @param col                the new column index
         * @param row                the new row index
         * @param encodedAlignments  describes the horizontal and vertical alignments
         * @return a reference to the builder
         *
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public FormBuilder xy(int col, int row, String encodedAlignments) {
            return at(CC.xy(col, row, encodedAlignments));
        }

        /**
         * Sets the column and row origins; sets width and height to 1;
         * set horizontal and vertical alignment using the specified objects.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.xy(1, 3, CellConstraints.LEFT,   CellConstraints.BOTTOM);
         * CC.xy(1, 3, CellConstraints.CENTER, CellConstraints.FILL);
         * </pre>
         *
         * @param col       the new column index
         * @param row       the new row index
         * @param colAlign  horizontal component alignment
         * @param rowAlign  vertical component alignment
         * @return a reference to the builder
         */
        public FormBuilder xy(int col, int row,
                                  Alignment colAlign, Alignment rowAlign) {
            return at(CC.xy(col, row, colAlign, rowAlign));
        }


        /**
         * Sets the column, row, width, and height; uses a height (row span) of 1
         * and the horizontal and vertical default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.xyw(1, 3, 7);
         * CC.xyw(1, 3, 2);
         * </pre>
         *
         * @param col      the new column index
         * @param row      the new row index
         * @param colSpan  the column span or grid width
         * @return a reference to the builder
         */
        public FormBuilder xyw(int col, int row, int colSpan) {
            return at(CC.xyw(col, row, colSpan));
        }


        /**
         * Sets the column, row, width, and height;
         * decodes the horizontal and vertical alignments from the given string.
         * The row span (height) is set to 1.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.xyw(1, 3, 7, "left, bottom");
         * CC.xyw(1, 3, 7, "l, b");
         * CC.xyw(1, 3, 2, "center, fill");
         * CC.xyw(1, 3, 2, "c, f");
         * </pre>
         *
         * @param col                the new column index
         * @param row                the new row index
         * @param colSpan            the column span or grid width
         * @param encodedAlignments  describes the horizontal and vertical alignments
         * @return a reference to the builder
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public FormBuilder xyw(int col, int row, int colSpan,
                                     String encodedAlignments) {
            return at(CC.xyw(col, row, colSpan, encodedAlignments));
        }


        /**
         * Sets the column, row, width, and height; sets the horizontal
         * and vertical alignment using the specified alignment objects.
         * The row span (height) is set to 1.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.xyw(1, 3, 2, CellConstraints.LEFT,   CellConstraints.BOTTOM);
         * CC.xyw(1, 3, 7, CellConstraints.CENTER, CellConstraints.FILL);
         * </pre>
         *
         * @param col       the new column index
         * @param row       the new row index
         * @param colSpan   the column span or grid width
         * @param colAlign  horizontal component alignment
         * @param rowAlign  vertical component alignment
         * @return a reference to the builder
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public FormBuilder xyw(int col, int row, int colSpan,
                                     Alignment colAlign, Alignment rowAlign) {
            return at(CC.xyw(col, row, colSpan, colAlign, rowAlign));
        }


        /**
         * Sets the column, row, width, and height; uses default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.xywh(1, 3, 2, 1);
         * CC.xywh(1, 3, 7, 3);
         * </pre>
         *
         * @param col      the new column index
         * @param row      the new row index
         * @param colSpan  the column span or grid width
         * @param rowSpan  the row span or grid height
         * @return a reference to the builder
         */
        public FormBuilder xywh(int col, int row, int colSpan, int rowSpan) {
            return at(CC.xywh(col, row, colSpan, rowSpan));
        }


        /**
         * Sets the column, row, width, and height;
         * decodes the horizontal and vertical alignments from the given string.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.xywh(1, 3, 2, 1, "left, bottom");
         * CC.xywh(1, 3, 2, 1, "l, b");
         * CC.xywh(1, 3, 7, 3, "center, fill");
         * CC.xywh(1, 3, 7, 3, "c, f");
         * </pre>
         *
         * @param col                the new column index
         * @param row                the new row index
         * @param colSpan            the column span or grid width
         * @param rowSpan            the row span or grid height
         * @param encodedAlignments  describes the horizontal and vertical alignments
         * @return a reference to the builder
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public FormBuilder xywh(int col, int row, int colSpan, int rowSpan,
                                     String encodedAlignments) {
            return at(CC.xywh(col, row, colSpan, rowSpan, encodedAlignments));
        }


        /**
         * Sets the column, row, width, and height; sets the horizontal
         * and vertical alignment using the specified alignment objects.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.xywh(1, 3, 2, 1, CellConstraints.LEFT,   CellConstraints.BOTTOM);
         * CC.xywh(1, 3, 7, 3, CellConstraints.CENTER, CellConstraints.FILL);
         * </pre>
         *
         * @param col       the new column index
         * @param row       the new row index
         * @param colSpan   the column span or grid width
         * @param rowSpan   the row span or grid height
         * @param colAlign  horizontal component alignment
         * @param rowAlign  vertical component alignment
         * @return a reference to the builder
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public FormBuilder xywh(int col, int row, int colSpan, int rowSpan,
                                     Alignment colAlign, Alignment rowAlign) {
            return at(CC.xywh(col, row, colSpan, rowSpan, colAlign, rowAlign));
        }


        // Row-Column Order ---------------------------------------------------

        /**
         * Sets row and column origins; sets height and width to 1;
         * uses the default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.rc(1, 1);
         * CC.rc(3, 1);
         * </pre>
         *
         * @param row     the new row index
         * @param col     the new column index
         * @return a reference to the builder
         */
        public FormBuilder rc(int row, int col) {
            return at(CC.rc(row, col));
        }


        /**
         * Sets row and column origins; sets height and width to 1;
         * decodes vertical and horizontal alignments from the given string.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.rc(3, 1, "bottom, left");
         * CC.rc(3, 1, "b, l");
         * CC.rc(3, 1, "fill, center");
         * CC.rc(3, 1, "f, c");
         * </pre>
         *
         * @param row                the new row index
         * @param col                the new column index
         * @param encodedAlignments  describes the vertical and horizontal alignments
         * @return a reference to the builder
         *
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public FormBuilder rc(int row, int col, String encodedAlignments) {
            return at(CC.rc(row, col, encodedAlignments));
        }


        /**
         * Sets the row and column origins; sets width and height to 1;
         * set horizontal and vertical alignment using the specified objects.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.rc(3, 1, CellConstraints.BOTTOM, CellConstraints.LEFT);
         * CC.rc(3, 1, CellConstraints.FILL,   CellConstraints.CENTER);
         * </pre>
         *
         * @param row       the new row index
         * @param col       the new column index
         * @param rowAlign  vertical component alignment
         * @param colAlign  horizontal component alignment
         * @return a reference to the builder
         */
        public FormBuilder rc(int row, int col,
                                  Alignment rowAlign, Alignment colAlign) {
            return at(CC.rc(row, col, rowAlign, colAlign));
        }


        /**
         * Sets the row, column, height, and width; uses a height (row span) of 1
         * and the vertical and horizontal default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.rcw(3, 1, 7);
         * CC.rcw(3, 1, 2);
         * </pre>
         *
         * @param row      the new row index
         * @param col      the new column index
         * @param colSpan  the column span or grid width
         * @return a reference to the builder
         */
        public FormBuilder rcw(int row, int col, int colSpan) {
            return at(CC.rcw(row, col, colSpan));
        }


        /**
         * Sets the row, column, height, and width;
         * decodes the vertical and horizontal alignments from the given string.
         * The row span (height) is set to 1.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.rcw(3, 1, 7, "bottom, left");
         * CC.rcw(3, 1, 7, "b, l");
         * CC.rcw(3, 1, 2, "fill, center");
         * CC.rcw(3, 1, 2, "f, c");
         * </pre>
         *
         * @param row                the new row index
         * @param col                the new column index
         * @param colSpan            the column span or grid width
         * @param encodedAlignments  describes the vertical and horizontal alignments
         * @return a reference to the builder
         *
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public FormBuilder rcw(int row, int col, int colSpan,
                                     String encodedAlignments) {
            return at(CC.rcw(row, col, colSpan, encodedAlignments));
        }


        /**
         * Sets the row, column, height, and width; sets the vertical
         * and horizontal alignment using the specified alignment objects.
         * The row span (height) is set to 1.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.rcw(3, 1, 2, CellConstraints.BOTTOM, CellConstraints.LEFT);
         * CC.rcw(3, 1, 7, CellConstraints.FILL,   CellConstraints.CENTER);
         * </pre>
         *
         * @param row       the new row index
         * @param col       the new column index
         * @param colSpan   the column span or grid width
         * @param rowAlign  vertical component alignment
         * @param colAlign  horizontal component alignment
         * @return a reference to the builder
         *
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public FormBuilder rcw(int row, int col, int colSpan,
                                     Alignment rowAlign, Alignment colAlign) {
            return at(CC.rcw(row, col, colSpan, rowAlign, colAlign));
        }


        /**
         * Sets the row, column, height, and width; uses default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.rchw(1, 3, 2, 1);
         * CC.rchw(1, 3, 7, 3);
         * </pre>
         *
         * @param row      the new row index
         * @param col      the new column index
         * @param rowSpan  the row span or grid height
         * @param colSpan  the column span or grid width
         * @return a reference to the builder
         */
        public FormBuilder rchw(int row, int col, int rowSpan, int colSpan) {
            return at(CC.rchw(row, col, rowSpan, colSpan));
        }


        /**
         * Sets the row, column, height, and width;
         * decodes the vertical and horizontal alignments from the given string.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.rchw(3, 1, 1, 2, "bottom, left");
         * CC.rchw(3, 1, 1, 2, "b, l");
         * CC.rchw(3, 1, 3, 7, "fill, center");
         * CC.rchw(3, 1, 3, 7, "f, c");
         * </pre>
         *
         * @param row                the new row index
         * @param col                the new column index
         * @param rowSpan            the row span or grid height
         * @param colSpan            the column span or grid width
         * @param encodedAlignments  describes the vertical and horizontal alignments
         * @return a reference to the builder
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public FormBuilder rchw(int row, int col, int rowSpan, int colSpan,
                                     String encodedAlignments) {
            return at(CC.rchw(row, col, rowSpan, colSpan, encodedAlignments));
        }


        /**
         * Sets the row, column, height, and width; sets the vertical and
         * horizontal alignment using the specified alignment objects.<p>
         *
         * <strong>Examples:</strong><pre>
         * CC.rchw(3, 1, 1, 2, CellConstraints.BOTTOM, CellConstraints.LEFT);
         * CC.rchw(3, 1, 3, 7, CellConstraints.FILL,   CellConstraints.CENTER);
         * </pre>
         *
         * @param row       the new row index
         * @param col       the new column index
         * @param rowSpan   the row span or grid height
         * @param colSpan   the column span or grid width
         * @param rowAlign  vertical component alignment
         * @param colAlign  horizontal component alignment
         * @return a reference to the builder
         *
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public FormBuilder rchw(int row, int col, int rowSpan, int colSpan,
                                     Alignment rowAlign, Alignment colAlign) {
            return at(CC.rchw(col, row, rowSpan, colSpan, colAlign, rowAlign));
        }

        
        // Helper Code -------------------------------------------------------
        
        protected FormBuilder add(CellConstraints constraints) {
            builder.addImpl(component, constraints);
            return builder;
        }

    }
    
    
    private static final class NoOpComponentAdder extends ComponentAdder {
        
        NoOpComponentAdder(FormBuilder builder) {
           super(builder, null);
        }
        
        
        @Override
        protected FormBuilder add(CellConstraints constraints) {
            // Unlike the superclass, do not add anything.
            return builder;
        }
        
    }


}
