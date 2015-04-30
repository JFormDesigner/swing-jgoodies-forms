/*
 * Copyright (c) 2002-2015 JGoodies Software GmbH. All Rights Reserved.
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
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Icon;
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
import com.jgoodies.common.swing.MnemonicUtils;
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
import com.jgoodies.forms.util.FocusTraversalType;

/**
 * An abstract superclass that reduces the effort for adding features
 * to the FormBuilder. This class is generified with the builder's
 * return type. In contrast, the concrete FormBuilder doesn't need
 * to be generified.
 * 
 * @param <B>   the type of this builder
 * 
 * @since 1.10
 */
public class AbstractFormBuilder<B extends AbstractFormBuilder<B>> {
    

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
    private WeakReference<JLabel> mostRecentlyAddedLabelReference = null;


    // Instance Creation ******************************************************

    protected AbstractFormBuilder() {
        labelForFeatureEnabled(FormsSetup.getLabelForFeatureEnabledDefault());
        offsetX = 0;
        offsetY = 0;
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
    
    /**
     * Configures this builder's FormLayout to use the given layout map
     * for expanding layout variables.<p>
     * 
     * <strong>Example:</strong><pre>
     * return FormBuilder.create()
     *     .columns("left:pref, $lcgap, 200dlu")
     *     .rows("p, $lg, p, $lg, p")
     *     .layoutMap(aCustomLayoutMap)
     *     ...
     * </pre>
     * 
     * @param layoutMap     expands layout column and row variables
     * @return a reference to this builder
     */
    public B layoutMap(LayoutMap layoutMap) {
        this.layoutMap = layoutMap;
        return (B) this;
    }
    

    /**
     * Configures this builder's layout columns using a comma-separated
     * string of column specifications. The string can be a format string
     * and will then use the optional format arguments, see
     * {@link String#format(String, Object...)}.<p>
     * 
     * <strong>Examples:</strong><br>
     * <pre>
     * .columns("left:90dlu, 3dlu, 200dlu")
     * .columns("left:90dlu, 3dlu, %sdlu", "200")  // Format string
     * .columns("$label, $lcgap, 200dlu")          // Layout variables
     * </pre>
     * 
     * @param encodedColumnSpecs    a comma-separated list of column specifications
     * @param args                  optional format arguments
     * @return a reference to this builder
     * 
     * @see ColumnSpec
     */
    public B columns(String encodedColumnSpecs, Object... args) {
        columnSpecs = ColumnSpec.decodeSpecs(
                Strings.get(encodedColumnSpecs, args), getLayoutMap());
        return (B) this;
    }
    

    /**
     * Appends the given columns to this builder's layout.
     * The columns to append are provided as a comma-separated
     * string of column specifications. The string can be a format string
     * and will then use the optional format arguments, see
     * {@link String#format(String, Object...)}.<p>
     * 
     * <strong>Examples:</strong><br>
     * <pre>
     * .appendColumns("50dlu, 3dlu, 50dlu")
     * .appendColumns("%sdlu, 3dlu, %sdlu", "50")    // Format string
     * .appendColumns("$button, $rgap, $button")     // Layout variable
     * </pre>
     * 
     * @param encodedColumnSpecs    a comma-separated list of column specifications
     * @param args                  optional format arguments
     * @return a reference to this builder
     * 
     * @see ColumnSpec
     */
    public B appendColumns(String encodedColumnSpecs, Object... args) {
        ColumnSpec[] newColumnSpecs = ColumnSpec.decodeSpecs(
                Strings.get(encodedColumnSpecs, args), getLayoutMap());
        for (ColumnSpec columnSpec : newColumnSpecs) {
            getLayout().appendColumn(columnSpec);
        }
        return (B) this;
    }
    

    /**
     * Configures this builder's layout rows using a comma-separated
     * string of row specifications.The string can be a format string
     * and will then use the optional format arguments, see
     * {@link String#format(String, Object...)}.<p>
     * 
     * <strong>Examples:</strong><br>
     * <pre>
     * .rows("p, 3dlu, p, 14dlu, p")
     * .rows("p, 3dlu, p, %sdlu, p", "14")  // Format string
     * .rows("p, $pg, p, $pg, p")           // Layout variables
     * </pre>
     * 
     * @param encodedRowSpecs    a comma-separated list of row specifications
     * @param args               optional format arguments
     * @return a reference to this builder
     * 
     * @see RowSpec
     */
    public B rows(String encodedRowSpecs, Object... args) {
        rowSpecs = RowSpec.decodeSpecs(
                Strings.get(encodedRowSpecs, args), getLayoutMap());
        return (B) this;
    }
    

    /**
     * Appends the given rows to this builder's layout.
     * The rows to append are provided as a comma-separated
     * string of row specifications. The string can be a format string
     * and will then use the optional format arguments, see
     * {@link String#format(String, Object...)}.<p>
     * 
     * <strong>Examples:</strong><br>
     * <pre>
     * .appendRows("10dlu, p, 3dlu, p")
     * .appendRows("%sdlu, p, 3dlu, p", "10")    // Format string
     * .appendRows("10dlu, p, $lg,  p")          // Layout variable
     * </pre>
     * 
     * @param encodedRowSpecs       a comma-separated list of row specifications
     * @param args                  optional format arguments
     * @return a reference to this builder
     * 
     * @see RowSpec
     */
    public B appendRows(String encodedRowSpecs, Object... args) {
        RowSpec[] newRowSpecs = RowSpec.decodeSpecs(
                Strings.get(encodedRowSpecs, args), getLayoutMap());
        for (RowSpec rowSpec : newRowSpecs) {
            getLayout().appendRow(rowSpec);
        }
        return (B) this;
    }
    

    /**
     * Configures this builder's layout to group (make equally wide)
     * the columns with the given indices.<p>
     * 
     * <strong>Examples:</strong><br>
     * <pre>
     * .columnGroup(3, 5)
     * </pre>
     * 
     * @param columnIndices   the indices of the columns to group
     * @return a reference to this builder
     */
    public B columnGroup(int... columnIndices) {
        getLayout().setColumnGroup(columnIndices);
        return (B) this;
    }
    
    
    /**
     * Configures this builder's layout to group (make equally wide)
     * the columns per array of column indices.<p>
     * 
     * <strong>Examples:</strong><br>
     * <pre>
     * .columnGroups(new int[]{3, 5}, new int[]{7, 9})
     * </pre>
     * 
     * @param multipleColumnGroups  multiple arrays of column indices
     * @return a reference to this builder
     */
    public B columnGroups(int[]... multipleColumnGroups) {
        getLayout().setColumnGroups(multipleColumnGroups);
        return (B) this;
    }
    

    /**
     * Configures this builder's layout to group (make equally high)
     * the rows with the given indices.<p>
     * 
     * <strong>Examples:</strong><br>
     * <pre>
     * .rowGroup(3, 5)
     * </pre>
     * 
     * @param rowIndices   the indices of the rows to group
     * @return a reference to this builder
     */
    public B rowGroup(int... rowIndices) {
        getLayout().setRowGroup(rowIndices);
        return (B) this;
    }
    

    /**
     * Configures this builder's layout to group (make equally wide)
     * the rows per array of row indices.<p>
     * 
     * <strong>Examples:</strong><br>
     * <pre>
     * .rowGroups(new int[]{3, 5}, new int[]{7, 9})
     * </pre>
     * 
     * @param multipleRowGroups  multiple arrays of row indices
     * @return a reference to this builder
     */
    public B rowGroups(int[]... multipleRowGroups) {
        getLayout().setRowGroups(multipleRowGroups);
        return (B) this;
    }
    

    /**
     * Specifies whether invisible components shall be taken into account by
     * this builder for computing the layout size and setting component bounds.
     * If set to {@code true} invisible components will be ignored by
     * the layout. If set to {@code false} components will be taken into
     * account regardless of their visibility. Visible components are always
     * used for sizing and positioning.<p>
     *
     * The default value for this setting is {@code true}.
     * It is useful to set the value to {@code false} (in other words
     * to ignore the visibility) if you switch the component visibility
     * dynamically and want the container to retain the size and
     * component positions.
     * 
     * @param  b   {@code true} to honor the visibility, i.e. to exclude
     *    invisible components from the sizing and positioning,
     *    {@code false} to ignore the visibility, in other words to
     *    layout visible and invisible components
     * @return a reference to this builder
     * 
     * @see FormLayout#setHonorsVisibility(boolean)
     */
    public B honorsVisibility(boolean b) {
        getLayout().setHonorsVisibility(b);
        return (B) this;
    }
    

    /**
     * Configures how this builder's layout shall handle the visibility
     * of the given component.
     * 
     * @param c    the component to configure
     * @param b    {@code true} to use {@code c} for layout computations only if visible,
     *             {@code false} to take {@code c} into account even if invisible
     * @return a reference to this builder
     * 
     * @see FormLayout#setHonorsVisibility(Component, Boolean)
     */
    public B honorsVisibility(JComponent c, boolean b) {
        getLayout().setHonorsVisibility(c, b);
        return (B) this;
    }
    

    /**
     * Sets {@code layout} as the layout to use by this builder.
     * 
     * @param layout    the layout to be used by this builder
     * @return a reference to this builder
     */
    public B layout(FormLayout layout) {
        this.layout = checkNotNull(layout, MUST_NOT_BE_NULL, "layout");
        return (B) this;
    }
    

    /**
     * Sets {@code panel} as the panel that this builder shall work with.
     * 
     * @param panel     the panel to work with
     * @return a reference to this builder
     */
    public B panel(JPanel panel) {
        this.panel = checkNotNull(panel, MUST_NOT_BE_NULL, "panel");
        this.panel.setLayout(getLayout());
        return (B) this;
    }
    

    /**
     * Enables or disables the display of layout debug information.
     * If enabled, the layout grid lines will be painted with red lines.
     * By default the debug mode is disabled.
     * 
     * @param b      {@code true} to paint grid lines, {@code false} to disable it
     * @return a reference to this builder
     * 
     * @see FormDebugPanel
     */
    public B debug(boolean b) {
        this.debug = b;
        return (B) this;
    }
    
    
    /**
     * Sets the name of the panel this builder works with.
     * 
     * @param panelName     the name to set
     * @return a reference to this builder
     */
    public B name(String panelName) {
        getPanel().setName(panelName);
        return (B) this;
    }
    

    // Panel Properties *******************************************************

    /**
     * Sets the panel's background color and the panel to be opaque.
     *
     * @param background  the color to set as new background
     * @return a reference to this builder
     *
     * @see JComponent#setBackground(Color)
     */
    public B background(Color background) {
    	getPanel().setBackground(background);
        opaque(true);
        return (B) this;
    }


    /**
     * Sets the panel's border. If you just want to wrap a panel
     * with white space, use {@link #padding(EmptyBorder)} instead.
     *
     * @param border	the border to set
     * @return a reference to this builder
     *
     * @see #padding(EmptyBorder)
     * @see JComponent#setBorder(Border)
     */
    public B border(Border border) {
    	getPanel().setBorder(border);
        return (B) this;
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
     * @return a reference to this builder
     *
     * @see Paddings#createPadding(String, Object...)
     * @deprecated Use {@link #padding(String, Object...)} instead
     */
    @Deprecated
    public B border(String paddingSpec) {
        return padding(paddingSpec);
    }


    /**
     * Sets the panel's padding, an empty border.
     *
     * @param padding    the white space around this form
     * @return a reference to this builder
     *
     * @see #border
     * 
     * @since 1.9
     */
    public B padding(EmptyBorder padding) {
        getPanel().setBorder(padding);
        return (B) this;
    }


    /**
     * Sets the panel's padding as an EmptyBorder using the given specification
     * for the top, left, bottom, right margins in DLU. For example
     * "1dlu, 2dlu, 3dlu, 4dlu" sets an empty border with 1dlu in the top,
     * 2dlu in the left side, 3dlu at the bottom, and 4dlu in the right hand
     * side.<p>
     *
     * Equivalent to {@code padding(Paddings.createPadding(paddingSpec, args))}.
     *
     * @param paddingSpec   describes the top, left, bottom, right margins
     *    of the padding (an EmptyBorder) to use
     * @param args          optional format arguments,
     *                      used if {@code paddingSpec} is a format string
     * @return a reference to this builder
     *
     * @see #padding(EmptyBorder)
     * @see Paddings#createPadding(String, Object...)
     * 
     * @since 1.9
     */
    public B padding(String paddingSpec, Object... args) {
    	padding(Paddings.createPadding(paddingSpec, args));
    	return (B) this;
    }


    /**
     * Sets the panel's opaque state.
     *
     * @param b   true for opaque, false for non-opaque
     * @return a reference to this builder
     *
     * @see JComponent#setOpaque(boolean)
     */
    public B opaque(boolean b) {
        getPanel().setOpaque(b);
        return (B) this;
    }
    
    
    /**
     * Sets a component that should receive the focus when a Window is
     * made visible for the first time. For details see
     * {@link FocusTraversalPolicy#getInitialComponent(java.awt.Window)}.
     * 
     * @param initialComponent   the component that shall receive the focus
     * @return a reference to this builder
     */
    public B initialComponent(JComponent initialComponent) {
        checkState(this.initialComponent == null,
                "The initial component must be set once only.");
        checkValidFocusTraversalSetup();
        this.initialComponent = initialComponent;
        setupFocusTraversalPolicyAndProvider();
        return (B) this;
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
    public B focusTraversalType(FocusTraversalType focusTraversalType) {
        checkNotNull(focusTraversalType, MUST_NOT_BE_NULL, "focus traversal type");
        checkState(this.focusTraversalType == null,
                "The focus traversal type must be set once only.");
        checkValidFocusTraversalSetup();
        this.focusTraversalType = focusTraversalType;
        setupFocusTraversalPolicyAndProvider();
        return (B) this;
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
     * @return a reference to this builder
     *
     * @see #focusTraversalType(FocusTraversalType)
     * @see JComponent#setFocusTraversalPolicy(FocusTraversalPolicy)
     * @see JComponent#setFocusTraversalPolicyProvider(boolean)
     * 
     * @throws NullPointerException if {@code focusTraversalType} is {@code null}
     */
    public B focusTraversalPolicy(FocusTraversalPolicy policy) {
        checkNotNull(policy, MUST_NOT_BE_NULL, "focus traversal policy");
        checkState(this.focusTraversalPolicy == null,
                "The focus traversal policy must be set once only.");
        checkValidFocusTraversalSetup();
        this.focusTraversalPolicy = policy;
        setupFocusTraversalPolicyAndProvider();
        return (B) this;
    }
    

    /**
     * Tries to build a focus group for the given buttons.
     * Within a focus group, focus can be transferred from one group member
     * to another using the arrow keys.<p>
     * 
     * To succeed, the commercial {@code FocusTraversalUtils} class must be
     * in the class path. To make focus grouping work, a focus traversal policy
     * must be set that is capable of transferring focus with the arrow keys
     * such as {@code JGContainerOrderFocusTraversalPolicy} or
     * {@code JGLayoutFocusTraversalPolicy}.
     * 
     * @param buttons   the buttons to be grouped
     * @return a reference to this builder
     */
    public B focusGroup(AbstractButton... buttons) {
        FocusTraversalUtilsAccessor.tryToBuildAFocusGroup(buttons);
        return (B) this;
    }


    /**
     * Tries to build a focus group for the given buttons.
     * Within a focus group, focus can be transferred from one group member
     * to another using the arrow keys.<p>
     * 
     * To succeed, the commercial {@code FocusTraversalUtils} class must be
     * in the class path. To make focus grouping work, a focus traversal policy
     * must be set that is capable of transferring focus with the arrow keys
     * such as {@code JGContainerOrderFocusTraversalPolicy} or
     * {@code JGLayoutFocusTraversalPolicy}.
     * 
     * @param buttons   the buttons to be grouped
     * @return a reference to this builder
     * 
     * @since 1.11
     */
    public B focusGroup(List<AbstractButton> buttons) {
        return focusGroup(buttons.toArray(new AbstractButton[buttons.size()]));
    }


    /**
     * Returns the panel used to build the form.
     * Intended to access panel properties. For returning the built panel
     * use {@link #build()} instead.
     *
     * @return the panel used by this builder to build the form
     * 
     * @see #build()
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

    /**
     * Sets {@code factory} as this builder's new component factory
     * that is used when adding implicitly created components such as
     * labels, titles, or titled separators.
     * If not called, the default factory will be used
     * that can be configured via
     * {@link FormsSetup#setComponentFactoryDefault(ComponentFactory)}.
     * 
     * @param factory    the factory to be used to create components
     * @return a reference to this builder
     */
    public B factory(ComponentFactory factory) {
        this.factory = factory;
        return (B) this;
    }
    
    
    /**
     * Enables or disables the setLabelFor feature for this builder.
     * If enabled, a label that has just been added by this builder
     * will be set as the label for the next component added by this builder.<p>
     * 
     * The value is initialized from the global default value
     * {@link FormsSetup#getLabelForFeatureEnabledDefault()}.
     * It is globally disabled by default.<p>
     * 
     * @param b {@code true} for enabled, {@code false} for disabled
     * @return a reference to this builder
     * 
     * @see JLabel#setLabelFor(Component)
     */
    public B labelForFeatureEnabled(boolean b) {
        labelForFeatureEnabled = b;
        return (B) this;
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
     * @return a reference to this builder
     * 
     * @see #translate(int, int)
     */
    public B offset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return (B) this;
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
     * @return a reference to this builder
     * 
     * @see #offset(int, int)
     */
    public B translate(int dX, int dY) {
        this.offsetX += dX;
        this.offsetY += dY;
        return (B) this;
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
    public B defaultLabelType(LabelType newValue) {
        this.defaultLabelType = newValue;
        return (B) this;
    }
    
    
    /**
     * Sets the default label type to {@code LabelType#READ_ONLY}.
     * Equivalent to:
     * <pre>
     * builder.defaultLabelType(LabelType.READ_ONLY);
     * </pre>
     * 
     * @return a reference to this builder
     * 
     * @since 1.11
     */
    public B readOnlyLabels() {
        return defaultLabelType(LabelType.READ_ONLY);
    }


    /**
     * The first of two steps for adding a component to this builder's panel.
     * This component will be added, once the cell constraints are specified.<p>
     * 
     * JTables, JLists, and JTrees will be automatically wrapped
     * by a default JScrollPane. If no scroll pane is desired, use
     * {@link #addRaw(Component)} instead. If a scroll pane is desired
     * for other components (frequent case are JTextAreas) use
     * {@link #addScrolled(Component)}.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .add(nameField)   .xy(1, 1)
     *    .add(countryCombo).xy(3, 3)
     *    ...
     *    .build();
     * </pre>
     * 
     * If the label-for-feature is enabled, the most recently added label
     * is tracked and associated with the next added component
     * that is applicable for being set as component for the label.
     *
     * @param c        the component to add; will be wrapped if it is an
     *                 instance of JTable, JList, or JTree
     * @return the fluent interface part used to set the cell constraints
     *
     * @see #isLabelForApplicable(JLabel, Component)
     */
    public ComponentAdder<B> add(Component c) {
        return add(true, c);
    }
    
    
    /**
     * The first of two steps for adding a component to this builder's panel.
     * This component will be added, once the cell constraints are specified.<p>
     * 
     * Unlike {@link #add(Component)}, this method won't wrap
     * JTables, JLists, and JTrees automatically with a JScrollPane.
     * Useful for tables, list, and trees that either need no scroll pane,
     * or have another kind of decoration.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addRaw(aTreeThatNeedsNoScrollPane).xy(1, 1)
     *    ...
     *    .build();
     * </pre>
     * 
     * If the label-for-feature is enabled, the most recently added label
     * is tracked and associated with the next added component
     * that is applicable for being set as component for the label.
     *
     * @param c        the component to add
     * @return the fluent interface part used to set the cell constraints
     *
     * @see #isLabelForApplicable(JLabel, Component)
     */
    public ComponentAdder<B> addRaw(Component c) {
        return addRaw(true, c);
    }


    /**
     * The first of two steps for adding the given component wrapped
     * with a JScrollPane to this builder's panel. The wrapped component
     * will be added once the cell constraints have been specified.
     * 
     * A frequent case for this method are JTextAreas that shall be scrolled.<p>
     * 
     * The layout is equivalent to:
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .add(new JScrollPane(c)).xy(..., ...)
     *    ...
     *    .build();
     * </pre>
     *
     * @param c              the component to be wrapped and added
     * @return the fluent interface part used to set the cell constraints
     */
    public ComponentAdder<B> addScrolled(Component c) {
        return addScrolled(true, c);
    }


    /**
     * The first of two steps for adding a button bar to this builder's panel.
     * This bar will be added, once the cell constraints are specified.<p>
     * 
     * The buttons will be laid out horizontally in a subpanel, where all buttons
     * use the platform's minimum width. If focus grouping is possible,
     * focus can be transferred between buttons using the arrow keys.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addBar(newButton, editButton, deleteButton).xy(1, 9)
     *    ...
     *    .build();
     * </pre>
     *
     * @param buttons        the buttons to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#buttonBar(JComponent...)
     */
    public ComponentAdder<B> addBar(JButton... buttons) {
        return addBar(true, buttons);
    }


    /**
     * The first of two steps for adding a check box bar to this builder's panel.
     * This bar will be added, once the cell constraints are specified.<p>
     * 
     * The check boxes will be laid out as a row in a subpanel.
     * If focus grouping is possible, focus can be transferred
     * between the check boxes using the arrow keys.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addBar(visibleBox, editableBox, enabledBox).xy(1, 9)
     *    ...
     *    .build();
     * </pre>
     *
     * @param checkBoxes        the check boxes to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#checkBoxBar(JCheckBox...)
     */
    public ComponentAdder<B> addBar(JCheckBox... checkBoxes) {
        return addBar(true, checkBoxes);
    }


    /**
     * The first of two steps for adding a radio button bar to this builder's panel.
     * This bar will be added, once the cell constraints are specified.<p>
     * 
     * The radio buttons will be laid out as a row in a subpanel.
     * If focus grouping is possible, focus can be transferred
     * between the radio buttons using the arrow keys. Also, focus will be
     * transferred to/from the selected radio button of the group - if any.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addBar(verticalRadio, horizontalRadio).xy(1, 9)
     *    ...
     *    .build();
     * </pre>
     *
     * @param radioButtons        the radio buttons to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#radioButtonBar(JRadioButton...)
     */
    public ComponentAdder<B> addBar(JRadioButton... radioButtons) {
        return addBar(true, radioButtons);
    }


    /**
     * The first of two steps for adding a button stack to this builder's panel.
     * This stack will be added, once the cell constraints are specified.<p>
     * 
     * The buttons will be laid out vertically in a subpanel, where all buttons
     * use the platform's minimum width. If focus grouping is possible,
     * focus can be transferred between buttons using the arrow keys.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addStack(newButton, editButton, deleteButton).xywh(5, 1, 1, 7)
     *    ...
     *    .build();
     * </pre>
     *
     * @param buttons        the buttons to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#buttonStack(JComponent...)
     */
    public ComponentAdder<B> addStack(JButton... buttons) {
        return addStack(true, buttons);
    }


    /**
     * The first of two steps for adding a check box stack to this builder's panel.
     * This stack will be added, once the cell constraints are specified.<p>
     * 
     * The check boxes will be laid out vertically in a subpanel.
     * If focus grouping is possible,
     * focus can be transferred between the check boxes using the arrow keys.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addStack(visibleBox, editableBox, enabledBox).xywh(5, 1, 1, 7)
     *    ...
     *    .build();
     * </pre>
     *
     * @param checkBoxes        the check boxes to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#checkBoxStack(JCheckBox...)
     */
    public ComponentAdder<B> addStack(JCheckBox... checkBoxes) {
        return addStack(true, checkBoxes);
    }


    /**
     * The first of two steps for adding a radio button stack to this builder's panel.
     * This stack will be added, once the cell constraints are specified.<p>
     * 
     * The radio buttons will be laid out vertically in a subpanel.
     * If focus grouping is possible,
     * focus can be transferred between the check boxes using the arrow keys.
     * Also, focus will be
     * transferred to/from the selected radio button of the group - if any.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addStack(verticalRadio, horizontalRadio).xywh(5, 1, 1, 7)
     *    ...
     *    .build();
     * </pre>
     *
     * @param radioButtons        the radio buttons to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#radioButtonStack(JRadioButton...)
     */
    public ComponentAdder<B> addStack(JRadioButton... radioButtons) {
        return addStack(true, radioButtons);
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
    public ViewAdder<B> add(FormBuildingView<B> view) {
        return add(true, view);
    }


    /**
     * The first of two steps for adding a textual label to the form.
     * Equivalent to: {@code addLabel(markedLabelText, args)}
     * or {@code addROLabel(markedLabelText, args)} depending on
     * the current <em>defaultLabelType</em>.
     * The label will be created and added,
     * once the cell constraints are specified.<p>
     *
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .add("Name:")      .xy(1, 1) // No Mnemonic
     *    .add("N_ame:")     .xy(1, 1) // Mnemonic is 'a'
     *    .add("Save _as:")  .xy(1, 1) // Mnemonic is the second 'a'
     *    ...
     *    .build();
     * </pre>
     * 
     * @param markedLabelText  the text of the label to be added,
     *     may contain a mnemonic marker, and it may be a format string
     * @param args             optional format arguments
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see #defaultLabelType(LabelType)
     * @see MnemonicUtils
     * @see String#format(String, Object...)
     */
    public ComponentAdder<B> add(String markedLabelText, Object... args) {
        return add(true, markedLabelText, args);
    }


    /**
     * The first of two steps for adding a plain label to the form.
     * The label will be created and added,
     * once the cell constraints are specified.
     *
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addLabel("Name:")      .xy(1, 1) // No Mnemonic
     *    .addLabel("N_ame:")     .xy(1, 1) // Mnemonic is 'a'
     *    .addLabel("Save _as:")  .xy(1, 1) // Mnemonic is the second 'a'
     *    ...
     *    .build();
     * </pre>
     *
     * @param markedText   the label's text -
     *     may contain a mnemonic marker, and it may be a format string
     * @param args             optional format arguments
     * @return the fluent interface part used to set the cell constraints
     *
     * @see MnemonicUtils
     * @see ComponentFactory
     * @see String#format(String, Object...)
     */
    public ComponentAdder<B> addLabel(String markedText, Object... args) {
        return addLabel(true, markedText, args);
    }


    /**
     * The first of two steps for adding a textual label to the form
     * that is intended for labeling read-only components.
     * The label will be created and added,
     * once the cell constraints are specified.<p>
     * 
     * The read-only labels created by the default component factory
     * are slightly lighter than plain labels. This makes it easier
     * to differ between the labeling text and the text value that is labeled.
     *
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addROLabel("Name:")      .xy(1, 1) // No Mnemonic
     *    .addROLabel("N_ame:")     .xy(1, 1) // Mnemonic is 'a'
     *    .addROLabel("Save _as:")  .xy(1, 1) // Mnemonic is the second 'a'
     *    ...
     *    .build();
     * </pre>
     *
     * @param markedText   the label's text -
     *     may contain a mnemonic marker, and it may be a format string
     * @param args             optional format arguments
     * @return the fluent interface part used to set the cell constraints
     *
     * @see MnemonicUtils
     * @see ComponentFactory
     * @see String#format(String, Object...)
     */
    public ComponentAdder<B> addROLabel(String markedText, Object... args) {
        return addROLabel(true, markedText, args);
    }


    /**
     * The first of two steps for adding a title label to the form.
     * The title label will be created and added,
     * once the cell constraints are specified.
     *
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addTitle("Name")      .xy(1, 1) // No mnemonic
     *    .addTitle("N_ame")     .xy(1, 1) // Mnemonic is 'a'
     *    ...
     *    .build();
     * </pre>
     *
     * @param markedText   the title label's text -
     *     may contain a mnemonic marker, and it may be a format string
     * @param args             optional format arguments
     * @return the fluent interface part used to set the cell constraints
     *
     * @see MnemonicUtils
     * @see ComponentFactory
     * @see String#format(String, Object...)
     */
    public ComponentAdder<B> addTitle(String markedText, Object... args) {
        return addTitle(true, markedText, args);
    }


    /**
     * The first of two steps for adding a titled separator to the form.
     * The separator will be created and added,
     * once the cell constraints are specified.
     *
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addSeparator("Name")      .xyw(1, 1, 3) // No Mnemonic
     *    .addSeparator("N_ame")     .xyw(1, 1, 3) // Mnemonic is 'a'
     *    ...
     *    .build();
     * </pre>
     *
     * @param markedText   the separator label's text -
     *     may contain a mnemonic marker, and it may be a format string
     * @param args             optional format arguments
     * @return the fluent interface part used to set the cell constraints
     *
     * @see MnemonicUtils
     * @see ComponentFactory
     * @see String#format(String, Object...)
     */
    public ComponentAdder<B> addSeparator(String markedText, Object... args) {
        return addSeparator(true, markedText, args);
    }
    
    
    /**
     * The first of two steps for adding an icon label to the form.
     * The icon label will be added, once the cell constraints are specified.
     * If {@code image} is null, nothing will be added.
     * 
     * @param image   the image to be displayed by the added label
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see JLabel#JLabel(Icon)
     * 
     * @since 1.9
     */
    public ComponentAdder<B> add(Icon image) {
        return add(true, image);
    }
    
    
    // Adding Components Depending on an Expression ***************************
    
    /**
     * The first of two steps for conditionally adding a component to the form.
     * The component will be added, once the cell constraints are specified,
     * but only if {@code expression} is {@code true}.<p>
     * 
     * JTables, JLists, and JTrees will be automatically wrapped
     * by a default JScrollPane. If no scroll pane is desired, use
     * {@link #addRaw(boolean, Component)} instead. If a scroll pane is desired
     * for other components (frequent case are JTextAreas) use
     * {@link #addScrolled(boolean, Component)}.
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
     * @param expression    the precondition for adding the component
     * @param c             the component to add
     * @return the fluent interface part used to set the cell constraints
     *
     * @see #isLabelForApplicable(JLabel, Component)
     */
    public ComponentAdder<B> add(boolean expression, Component c) {
        if (!expression || c == null) {
            return new NoOpComponentAdder<>((B)this);
        }
        if (c instanceof JTable || c instanceof JList || c instanceof JTree) {
            return addScrolled(expression, c);
        }
        return addRaw(expression, c);
    }


    /**
     * The first of two steps for conditionally adding a component to the form.
     * This component will be added, once the cell constraints are specified.<p>
     * 
     * Unlike {@link #add(boolean, Component)}, this method won't wrap
     * JTables, JLists, and JTrees automatically with a JScrollPane.
     * Useful for tables, list, and trees that either need no scroll pane,
     * or have another kind of decoration.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addRaw(showTree, aTreeThatNeedsNoScrollPane).xy(1, 1)
     *    ...
     *    .build();
     * </pre>
     * 
     * If the label-for-feature is enabled, the most recently added label
     * is tracked and associated with the next added component
     * that is applicable for being set as component for the label.
     *
     * @param expression    the precondition for adding the component
     * @param c             the component to add
     * @return the fluent interface part used to set the cell constraints
     *
     * @see #isLabelForApplicable(JLabel, Component)
     */
    public ComponentAdder<B> addRaw(boolean expression, Component c) {
        if (!expression || c == null) {
            return new NoOpComponentAdder<>((B) this);
        }
        return addImpl(c);
    }


    /**
     * The first of two steps for conditionally adding the given component
     * wrapped with a JScrollPane to this builder's panel. The wrapped component
     * will be added once the cell constraints have been specified.
     * 
     * A frequent case for this method are JTextAreas that shall be scrolled.<p>
     * 
     * The layout is equivalent to:
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .add(expression, new JScrollPane(c)).xy(..., ...)
     *    ...
     *    .build();
     * </pre>
     *
     * @param expression    the precondition for adding the component
     * @param c             the component to be wrapped and added
     * @return the fluent interface part used to set the cell constraints
     */
    public ComponentAdder<B> addScrolled(boolean expression, Component c) {
        if (!expression || c == null) {
            return new NoOpComponentAdder<>((B) this);
        }
        return addImpl(new JScrollPane(c));
    }


    /**
     * The first of two steps for conditionally adding a button bar to the form.
     * This bar will be added, once the cell constraints are specified.<p>
     * 
     * The buttons will be laid out horizontally in a subpanel, where all buttons
     * use the platform's minimum width. If focus grouping is possible,
     * focus can be transferred between buttons using the arrow keys.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addBar(!readOnly, newButton, editButton, deleteButton).xy(1, 9)
     *    ...
     *    .build();
     * </pre>
     *
     * @param expression    the precondition for adding the bar
     * @param buttons       the buttons to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#buttonBar(JComponent...)
     */
    public ComponentAdder<B> addBar(boolean expression, JButton... buttons) {
        if (!expression || buttons == null) {
            return new NoOpComponentAdder<>((B) this);
        }
        return addImpl(Forms.buttonBar(buttons));
    }


    /**
     * The first of two steps for conditionally adding a check box bar to the form.
     * This bar will be added, once the cell constraints are specified.<p>
     * 
     * The check boxes will be laid out as a row in a subpanel.
     * If focus grouping is possible, focus can be transferred
     * between the check boxes using the arrow keys.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addBar(!readOnly, visibleBox, editableBox, enabledBox).xy(1, 9)
     *    ...
     *    .build();
     * </pre>
     *
     * @param expression    the precondition for adding the bar
     * @param checkBoxes    the check boxes to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#checkBoxBar(JCheckBox...)
     */
    public ComponentAdder<B> addBar(boolean expression, JCheckBox... checkBoxes) {
        if (!expression) {
            return new NoOpComponentAdder<>((B) this);
        }
        return addImpl(Forms.checkBoxBar(checkBoxes));
    }


    /**
     * The first of two steps for conditionally adding a radio button bar
     * to this builder's panel. This bar will be added,
     * once the cell constraints are specified.<p>
     * 
     * The radio buttons will be laid out as a row in a subpanel.
     * If focus grouping is possible, focus can be transferred
     * between the radio buttons using the arrow keys. Also, focus will be
     * transferred to/from the selected radio button of the group - if any.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .add   ( readOnly, orientationText)               .xy(1, 9)
     *    .addBar(!readOnly, verticalRadio, horizontalRadio).xy(1, 9)
     *    ...
     *    .build();
     * </pre>
     *
     * @param expression          the precondition for adding the bar
     * @param radioButtons        the radio buttons to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#radioButtonBar(JRadioButton...)
     */
    public ComponentAdder<B> addBar(boolean expression, JRadioButton... radioButtons) {
        if (!expression) {
            return new NoOpComponentAdder<>((B) this);
        }
        return addImpl(Forms.radioButtonBar(radioButtons));
    }


    /**
     * The first of two steps for conditionally adding a button stack
     * to this builder's panel.
     * This stack will be added, once the cell constraints are specified.<p>
     * 
     * The buttons will be laid out vertically in a subpanel, where all buttons
     * use the platform's minimum width. If focus grouping is possible,
     * focus can be transferred between buttons using the arrow keys.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addStack(!readOnly, newButton, editButton, deleteButton).xywh(5, 1, 1, 7)
     *    ...
     *    .build();
     * </pre>
     *
     * @param expression     the precondition for adding the stack
     * @param buttons        the buttons to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#buttonStack(JComponent...)
     */
    public ComponentAdder<B> addStack(boolean expression, JButton... buttons) {
        if (!expression || buttons == null) {
            return new NoOpComponentAdder<>((B) this);
        }
        return addImpl(Forms.buttonStack(buttons));
    }


    /**
     * The first of two steps for conditionally adding a check box stack
     * to this builder's panel.
     * This stack will be added, once the cell constraints are specified.<p>
     * 
     * The check boxes will be laid out vertically in a subpanel.
     * If focus grouping is possible,
     * focus can be transferred between the check boxes using the arrow keys.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addStack(!readOnly, visibleBox, editableBox, enabledBox).xywh(5, 1, 1, 7)
     *    ...
     *    .build();
     * </pre>
     *
     * @param expression        the precondition for adding the stack
     * @param checkBoxes        the check boxes to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#checkBoxStack(JCheckBox...)
     */
    public ComponentAdder<B> addStack(boolean expression, JCheckBox... checkBoxes) {
        if (!expression) {
            return new NoOpComponentAdder<>((B) this);
        }
        return addImpl(Forms.checkBoxStack(checkBoxes));
    }


    /**
     * The first of two steps for conditionally adding a radio button stack
     * to this builder's panel.
     * This stack will be added, once the cell constraints are specified.<p>
     * 
     * The radio buttons will be laid out vertically in a subpanel.
     * If focus grouping is possible,
     * focus can be transferred between the check boxes using the arrow keys.
     * Also, focus will be
     * transferred to/from the selected radio button of the group - if any.
     * 
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addStack(!readOnly, verticalRadio, horizontalRadio).xywh(5, 1, 1, 7)
     *    ...
     *    .build();
     * </pre>
     *
     * @param expression          the precondition for adding the stack
     * @param radioButtons        the radio buttons to add
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see Forms#radioButtonStack(JRadioButton...)
     */
    public ComponentAdder<B> addStack(boolean expression, JRadioButton... radioButtons) {
        if (!expression || radioButtons == null) {
            return new NoOpComponentAdder<>((B) this);
        }
        return addImpl(Forms.radioButtonStack(radioButtons));
    }


    /**
     * Builds the given view into this FormBuilder's form,
     * if {@code expression} is {@code true}.
     * 
     * @param expression   the precondition for adding the view
     * @param view         the view to integrate
     * @return the fluent interface part used to set the view's origin
     */
    public ViewAdder<B> add(boolean expression, FormBuildingView<B> view) {
        return new ViewAdder<>((B) this, expression, view);
    }


    /**
     * The first of two steps for conditionally adding a textual label
     * to the form.
     * Equivalent to: {@code addLabel(expression, markedLabelText, args)}
     * or {@code addROLabel(expression, markedLabelText, args)} depending on
     * the current <em>defaultLabelType</em>.
     * The label will be created and added,
     * once the cell constraints are specified.<p>
     *
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .add(condition, "Name:")      .xy(1, 1) // No Mnemonic
     *    .add(condition, "N_ame:")     .xy(1, 1) // Mnemonic is 'a'
     *    .add(condition, "Save _as:")  .xy(1, 1) // Mnemonic is the second 'a'
     *    ...
     *    .build();
     * </pre>
     * 
     * @param expression   the precondition for adding the label
     * @param markedLabelText  the text of the label to be added,
     *     may contain a mnemonic marker, and it may be a format string
     * @param args             optional format arguments
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see #defaultLabelType(LabelType)
     * @see MnemonicUtils
     * @see ComponentFactory
     * @see String#format(String, Object...)
     */
    public ComponentAdder<B> add(boolean expression, String markedLabelText, Object... args) {
        return defaultLabelType == LabelType.DEFAULT
                ? addLabel  (expression, markedLabelText, args)
                : addROLabel(expression, markedLabelText, args);
    }


    /**
     * The first of two steps for conditionally adding a plain label to the form.
     * The label will be created and added,
     * once the cell constraints are specified.
     *
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addLabel(condition, "Name:")      .xy(1, 1) // No Mnemonic
     *    .addLabel(condition, "N_ame:")     .xy(1, 1) // Mnemonic is 'a'
     *    .addLabel(condition, "Save _as:")  .xy(1, 1) // Mnemonic is the second 'a'
     *    ...
     *    .build();
     * </pre>
     *
     * @param expression   the precondition for adding the label
     * @param markedText   the label's text -
     *     may contain a mnemonic marker, and it may be a format string
     * @param args             optional format arguments
     * @return the fluent interface part used to set the cell constraints
     *
     * @see MnemonicUtils
     * @see ComponentFactory
     * @see String#format(String, Object...)
     */
    public ComponentAdder<B> addLabel(boolean expression, String markedText, Object... args) {
        return addRaw(expression,
                      getFactory().createLabel(Strings.get(markedText, args)));
    }


    /**
     * The first of two steps for conditionally adding a textual label to the form
     * that is intended for labeling read-only components.
     * The label will be created and added,
     * once the cell constraints are specified.<p>
     * 
     * The read-only labels created by the default component factory
     * are slightly lighter than plain labels. This makes it easier
     * to differ between the labeling text and the text value that is labeled.
     *
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addROLabel(condition, "Name:")      .xy(1, 1) // No Mnemonic
     *    .addROLabel(condition, "N_ame:")     .xy(1, 1) // Mnemonic is 'a'
     *    .addROLabel(condition, "Save _as:")  .xy(1, 1) // Mnemonic is the second 'a'
     *    ...
     *    .build();
     * </pre>
     *
     * @param expression   the precondition for adding the read-only label
     * @param markedText   the label's text -
     *     may contain a mnemonic marker, and it may be a format string
     * @param args             optional format arguments
     * @return the fluent interface part used to set the cell constraints
     *
     * @see MnemonicUtils
     * @see ComponentFactory
     * @see String#format(String, Object...)
     */
    public ComponentAdder<B> addROLabel(boolean expression,
            String markedText, Object... args) {
        return addRaw(expression,
                getFactory().createReadOnlyLabel(Strings.get(markedText, args)));
    }


    /**
     * The first of two steps for conditionally adding a title label to the form.
     * The title label will be created and added,
     * once the cell constraints are specified.
     *
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addTitle(condition, "Name")      .xy(1, 1) // No mnemonic
     *    .addTitle(condition, "N_ame")     .xy(1, 1) // Mnemonic is 'a'
     *    ...
     *    .build();
     * </pre>
     *
     * @param expression   the precondition for adding the title
     * @param markedText   the title label's text -
     *     may contain a mnemonic marker, and it may be a format string
     * @param args             optional format arguments
     * @return the fluent interface part used to set the cell constraints
     *
     * @see MnemonicUtils
     * @see ComponentFactory
     * @see String#format(String, Object...)
     */
    public ComponentAdder<B> addTitle(boolean expression,
            String markedText, Object... args) {
        String text = Strings.get(markedText, args);
        return addRaw(expression, getFactory().createTitle(text));
    }


    /**
     * The first of two steps for conditionally adding a titled separator to the form.
     * The separator will be created and added,
     * once the cell constraints are specified.
     *
     * <pre>
     * return FormBuilder.create()
     *    ...
     *    .addSeparator(condition, "Name")   .xyw(1, 1, 3) // No Mnemonic
     *    .addSeparator(condition, "N_ame")  .xyw(1, 1, 3) // Mnemonic is 'a'
     *    ...
     *    .build();
     * </pre>
     *
     * @param expression   the precondition for adding the separator
     * @param markedText   the separator label's text -
     *     may contain a mnemonic marker, and it may be a format string
     * @param args             optional format arguments
     * @return the fluent interface part used to set the cell constraints
     *
     * @see MnemonicUtils
     * @see ComponentFactory
     * @see String#format(String, Object...)
     */
    public ComponentAdder<B> addSeparator(boolean expression,
            String markedText, Object... args) {
        int alignment = isLeftToRight()
                ? SwingConstants.LEFT
                : SwingConstants.RIGHT;
        String text = Strings.get(markedText, args);
        return addRaw(expression,
                getFactory().createSeparator(text, alignment));
    }


    /**
     * The first of two steps for conditionally adding an icon label to the form.
     * The icon label will be added, once the cell constraints are specified.
     * If {@code image} is null, nothing will be added.
     * 
     * @param expression   the precondition for adding the icon
     * @param image   the image to be displayed by the added label
     * @return the fluent interface part used to set the cell constraints
     * 
     * @see JLabel#JLabel(Icon)
     * 
     * @since 1.9
     */
    public ComponentAdder<B> add(boolean expression, Icon image) {
        if (!expression || image == null) {
            return new NoOpComponentAdder<>((B) this);
        }
        return addImpl(new JLabel(image));
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

    protected ComponentAdder<B> addImpl(Component c) {
        // First ensure, that the panel has a layout set.
        if (getPanel().getLayout() == null) {
            panel.setLayout(getLayout());
        }
        return new ComponentAdder<>((B) this, c);
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
        JLabel label = mostRecentlyAddedLabelReference.get();
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
        mostRecentlyAddedLabelReference = new WeakReference<>(label);
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
     * using {@link AbstractFormBuilder#add(FormBuildingView)}. It is intended
     * to reduce the effort for embedding subpanels that are aligned
     * with an outer layout context.<p>
     * 
     * <b>Note: This is an experimental interface that is not yet
     * part of the public FormBuilder API.</b> It may change without
     * further notice.
     * 
     * @param <T> the type of the builder
     */
    public interface FormBuildingView<T> {
        
        /**
         * Integrates this view into the form that is built by the given builder.
         * 
         * @param builder   provides the layout, grid, panel, etc.
         */
        void buildInto(T builder);
        
    }
    
    
    // Fluent Interface for Integrating a FormBuildingView ********************
    
    public static final class ViewAdder<T extends AbstractFormBuilder<T>> {
        
        private final T builder;
        private final boolean expression;
        private final FormBuildingView<T> view;
        
        ViewAdder(T builder, boolean expression, FormBuildingView<T> view) {
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
        public T xy(int col, int row) {
            if (expression && view != null) {
                builder.translate(col, row);
                view.buildInto(builder);
                builder.translate(-col, -row);
            }
            return builder;
        }

    }
    

    // Fluent Interface for Adding Components *********************************
    
    public static class ComponentAdder<T extends AbstractFormBuilder<T>> {
        
        protected final T builder;
        private final Component component;
        private boolean labelForSet;
        
        // Instance Creation --------------------------------------------------
        
        ComponentAdder(T builder, Component component) {
            this.builder = builder;
            this.component = component;
            this.labelForSet = false;
        }
        
        
        public final ComponentAdder<T> labelFor(Component c) {
            checkArgument(component instanceof JLabel, "#labelFor is applicable only to JLabels");
            checkArgument(!labelForSet, "You must set the label-for-relation only once.");
            ((JLabel) component).setLabelFor(c);
            labelForSet = true;
            return this;
        }
        
        
        /**
         * Sets the given cell constraints.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).at(cellConstraints)
         * </pre>
         * 
         * @param constraints    specifies where an how to place a component
         * @return a reference to the builder
         */
        public final T at(CellConstraints constraints) {
            return add(constraints);
        }
        
        
        // Column-Row Order ---------------------------------------------------

        /**
         * Sets column and row origins; sets width and height to 1;
         * uses the default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).xy(1, 1)
         * .add(aComponent).xy(1, 3)
         * </pre>
         *
         * @param col     the column index
         * @param row     the row index
         * @return a reference to the builder
         */
        public final T xy(int col, int row) {
            return at(CC.xy(col, row));
        }


        /**
         * Sets column and row origins; sets width and height to 1;
         * decodes horizontal and vertical alignments from the given string.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).xy(1, 3, "left, bottom");
         * .add(aComponent).xy(1, 3, "l, b");
         * .add(aComponent).xy(1, 3, "center, fill");
         * .add(aComponent).xy(1, 3, "c, f");
         * </pre>
         *
         * @param col                the column index
         * @param row                the  row index
         * @param encodedAlignments  describes the horizontal and vertical alignments
         * @return a reference to the builder
         *
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public final T xy(int col, int row, String encodedAlignments) {
            return at(CC.xy(col, row, encodedAlignments));
        }

        
        /**
         * Sets the column and row origins; sets width and height to 1;
         * set horizontal and vertical alignment using the specified objects.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).xy(1, 3, CellConstraints.LEFT,   CellConstraints.BOTTOM);
         * .add(aComponent).xy(1, 3, CellConstraints.CENTER, CellConstraints.FILL);
         * </pre>
         *
         * @param col       the column index
         * @param row       the row index
         * @param colAlign  horizontal component alignment
         * @param rowAlign  vertical component alignment
         * @return a reference to the builder
         */
        public final T xy(int col, int row,
                                  Alignment colAlign, Alignment rowAlign) {
            return at(CC.xy(col, row, colAlign, rowAlign));
        }


        /**
         * Sets the column, row, width, and height; uses a height (row span) of 1
         * and the horizontal and vertical default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).xyw(1, 3, 7);
         * .add(aComponent).xyw(1, 3, 2);
         * </pre>
         *
         * @param col      the column index
         * @param row      the row index
         * @param colSpan  the column span or grid width
         * @return a reference to the builder
         */
        public final T xyw(int col, int row, int colSpan) {
            return at(CC.xyw(col, row, colSpan));
        }


        /**
         * Sets the column, row, width, and height;
         * decodes the horizontal and vertical alignments from the given string.
         * The row span (height) is set to 1.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).xyw(1, 3, 7, "left, bottom")
         * .add(aComponent).xyw(1, 3, 7, "l, b");
         * .add(aComponent).xyw(1, 3, 2, "center, fill");
         * .add(aComponent).xyw(1, 3, 2, "c, f");
         * </pre>
         *
         * @param col                the column index
         * @param row                the row index
         * @param colSpan            the column span or grid width
         * @param encodedAlignments  describes the horizontal and vertical alignments
         * @return a reference to the builder
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public final T xyw(int col, int row, int colSpan,
                                     String encodedAlignments) {
            return at(CC.xyw(col, row, colSpan, encodedAlignments));
        }


        /**
         * Sets the column, row, width, and height; sets the horizontal
         * and vertical alignment using the specified alignment objects.
         * The row span (height) is set to 1.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).xyw(1, 3, 2, CellConstraints.LEFT,   CellConstraints.BOTTOM);
         * .add(aComponent).xyw(1, 3, 7, CellConstraints.CENTER, CellConstraints.FILL);
         * </pre>
         *
         * @param col       the column index
         * @param row       the row index
         * @param colSpan   the column span or grid width
         * @param colAlign  horizontal component alignment
         * @param rowAlign  vertical component alignment
         * @return a reference to the builder
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public final T xyw(int col, int row, int colSpan,
                                     Alignment colAlign, Alignment rowAlign) {
            return at(CC.xyw(col, row, colSpan, colAlign, rowAlign));
        }


        /**
         * Sets the column, row, width, and height; uses default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).xywh(1, 3, 2, 1);
         * .add(aComponent).xywh(1, 3, 7, 3);
         * </pre>
         *
         * @param col      the column index
         * @param row      the row index
         * @param colSpan  the column span or grid width
         * @param rowSpan  the row span or grid height
         * @return a reference to the builder
         */
        public final T xywh(int col, int row, int colSpan, int rowSpan) {
            return at(CC.xywh(col, row, colSpan, rowSpan));
        }


        /**
         * Sets the column, row, width, and height;
         * decodes the horizontal and vertical alignments from the given string.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).xywh(1, 3, 2, 1, "left, bottom");
         * .add(aComponent).xywh(1, 3, 2, 1, "l, b");
         * .add(aComponent).xywh(1, 3, 7, 3, "center, fill");
         * .add(aComponent).xywh(1, 3, 7, 3, "c, f");
         * </pre>
         *
         * @param col                the column index
         * @param row                the row index
         * @param colSpan            the column span or grid width
         * @param rowSpan            the row span or grid height
         * @param encodedAlignments  describes the horizontal and vertical alignments
         * @return a reference to the builder
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public final T xywh(int col, int row, int colSpan, int rowSpan,
                                     String encodedAlignments) {
            return at(CC.xywh(col, row, colSpan, rowSpan, encodedAlignments));
        }


        /**
         * Sets the column, row, width, and height; sets the horizontal
         * and vertical alignment using the specified alignment objects.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).xywh(1, 3, 2, 1, CellConstraints.LEFT,   CellConstraints.BOTTOM);
         * .add(aComponent).xywh(1, 3, 7, 3, CellConstraints.CENTER, CellConstraints.FILL);
         * </pre>
         *
         * @param col       the column index
         * @param row       the row index
         * @param colSpan   the column span or grid width
         * @param rowSpan   the row span or grid height
         * @param colAlign  horizontal component alignment
         * @param rowAlign  vertical component alignment
         * @return a reference to the builder
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public final T xywh(int col, int row, int colSpan, int rowSpan,
                                     Alignment colAlign, Alignment rowAlign) {
            return at(CC.xywh(col, row, colSpan, rowSpan, colAlign, rowAlign));
        }


        // Row-Column Order ---------------------------------------------------

        /**
         * Sets row and column origins; sets height and width to 1;
         * uses the default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).rc(1, 1)
         * .add(aComponent).rc(3, 1)
         * </pre>
         *
         * @param row     the row index
         * @param col     the column index
         * @return a reference to the builder
         */
        public final T rc(int row, int col) {
            return at(CC.rc(row, col));
        }


        /**
         * Sets row and column origins; sets height and width to 1;
         * decodes vertical and horizontal alignments from the given string.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).rc(3, 1, "bottom, left")
         * .add(aComponent).rc(3, 1, "b, l")
         * .add(aComponent).rc(3, 1, "fill, center")
         * .add(aComponent).rc(3, 1, "f, c")
         * </pre>
         *
         * @param row                the row index
         * @param col                the column index
         * @param encodedAlignments  describes the vertical and horizontal alignments
         * @return a reference to the builder
         *
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public final T rc(int row, int col, String encodedAlignments) {
            return at(CC.rc(row, col, encodedAlignments));
        }


        /**
         * Sets the row and column origins; sets width and height to 1;
         * set horizontal and vertical alignment using the specified objects.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).rc(3, 1, CellConstraints.BOTTOM, CellConstraints.LEFT);
         * .add(aComponent).rc(3, 1, CellConstraints.FILL,   CellConstraints.CENTER);
         * </pre>
         *
         * @param row       the row index
         * @param col       the column index
         * @param rowAlign  vertical component alignment
         * @param colAlign  horizontal component alignment
         * @return a reference to the builder
         */
        public final T rc(int row, int col,
                                  Alignment rowAlign, Alignment colAlign) {
            return at(CC.rc(row, col, rowAlign, colAlign));
        }


        /**
         * Sets the row, column, height, and width; uses a height (row span) of 1
         * and the vertical and horizontal default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).rcw(3, 1, 7);
         * .add(aComponent).rcw(3, 1, 2);
         * </pre>
         *
         * @param row      the row index
         * @param col      the column index
         * @param colSpan  the column span or grid width
         * @return a reference to the builder
         */
        public final T rcw(int row, int col, int colSpan) {
            return at(CC.rcw(row, col, colSpan));
        }


        /**
         * Sets the row, column, height, and width;
         * decodes the vertical and horizontal alignments from the given string.
         * The row span (height) is set to 1.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).rcw(3, 1, 7, "bottom, left");
         * .add(aComponent).rcw(3, 1, 7, "b, l");
         * .add(aComponent).rcw(3, 1, 2, "fill, center");
         * .add(aComponent).rcw(3, 1, 2, "f, c");
         * </pre>
         *
         * @param row                the row index
         * @param col                the column index
         * @param colSpan            the column span or grid width
         * @param encodedAlignments  describes the vertical and horizontal alignments
         * @return a reference to the builder
         *
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public final T rcw(int row, int col, int colSpan,
                                     String encodedAlignments) {
            return at(CC.rcw(row, col, colSpan, encodedAlignments));
        }


        /**
         * Sets the row, column, height, and width; sets the vertical
         * and horizontal alignment using the specified alignment objects.
         * The row span (height) is set to 1.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).rcw(3, 1, 2, CellConstraints.BOTTOM, CellConstraints.LEFT);
         * .add(aComponent).rcw(3, 1, 7, CellConstraints.FILL,   CellConstraints.CENTER);
         * </pre>
         *
         * @param row       the row index
         * @param col       the column index
         * @param colSpan   the column span or grid width
         * @param rowAlign  vertical component alignment
         * @param colAlign  horizontal component alignment
         * @return a reference to the builder
         *
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public final T rcw(int row, int col, int colSpan,
                                     Alignment rowAlign, Alignment colAlign) {
            return at(CC.rcw(row, col, colSpan, rowAlign, colAlign));
        }


        /**
         * Sets the row, column, height, and width; uses default alignments.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).rchw(1, 3, 2, 1);
         * .add(aComponent).rchw(1, 3, 7, 3);
         * </pre>
         *
         * @param row      the row index
         * @param col      the column index
         * @param rowSpan  the row span or grid height
         * @param colSpan  the column span or grid width
         * @return a reference to the builder
         */
        public final T rchw(int row, int col, int rowSpan, int colSpan) {
            return at(CC.rchw(row, col, rowSpan, colSpan));
        }


        /**
         * Sets the row, column, height, and width;
         * decodes the vertical and horizontal alignments from the given string.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).rchw(3, 1, 1, 2, "bottom, left");
         * .add(aComponent).rchw(3, 1, 1, 2, "b, l");
         * .add(aComponent).rchw(3, 1, 3, 7, "fill, center");
         * .add(aComponent).rchw(3, 1, 3, 7, "f, c");
         * </pre>
         *
         * @param row                the row index
         * @param col                the column index
         * @param rowSpan            the row span or grid height
         * @param colSpan            the column span or grid width
         * @param encodedAlignments  describes the vertical and horizontal alignments
         * @return a reference to the builder
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public final T rchw(int row, int col, int rowSpan, int colSpan,
                                     String encodedAlignments) {
            return at(CC.rchw(row, col, rowSpan, colSpan, encodedAlignments));
        }


        /**
         * Sets the row, column, height, and width; sets the vertical and
         * horizontal alignment using the specified alignment objects.<p>
         *
         * <strong>Examples:</strong><pre>
         * .add(aComponent).rchw(3, 1, 1, 2, CellConstraints.BOTTOM, CellConstraints.LEFT);
         * .add(aComponent).rchw(3, 1, 3, 7, CellConstraints.FILL,   CellConstraints.CENTER);
         * </pre>
         *
         * @param row       the row index
         * @param col       the column index
         * @param rowSpan   the row span or grid height
         * @param colSpan   the column span or grid width
         * @param rowAlign  vertical component alignment
         * @param colAlign  horizontal component alignment
         * @return a reference to the builder
         *
         * @throws IllegalArgumentException if an alignment orientation is invalid
         */
        public final T rchw(int row, int col, int rowSpan, int colSpan,
                                     Alignment rowAlign, Alignment colAlign) {
            return at(CC.rchw(col, row, rowSpan, colSpan, colAlign, rowAlign));
        }

        
        // Helper Code -------------------------------------------------------
        
        protected T add(CellConstraints constraints) {
            builder.addImpl(component, constraints);
            return builder;
        }

    }
    
    
    private static final class NoOpComponentAdder<T extends AbstractFormBuilder<T>> extends ComponentAdder<T> {
        
        NoOpComponentAdder(T builder) {
           super(builder, null);
        }
        
        
        @Override
        protected T add(CellConstraints constraints) {
            // Unlike the superclass, do not add anything.
            return builder;
        }
        
    }


}
