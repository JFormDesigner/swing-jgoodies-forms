/*
 * Copyright (c) 2002-2012 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import static com.jgoodies.common.base.Preconditions.checkArgument;
import static com.jgoodies.common.base.Preconditions.checkNotNull;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.util.LayoutStyle;

/**
 * A non-visual builder for building consistent button bars that comply
 * with popular style guides. Utilizes the JGoodies {@link FormLayout}
 * and honors the platform's {@link LayoutStyle} regarding button sizes,
 * gap widths, and the default button order.<p>
 *
 * This is an improved version of the older {@code ButtonBarBuilder}.
 * The ButtonBarBuilder has a simpler, safer, and more convenient API,
 * see below for a comparison.<p>
 *
 * <strong>ButtonBarBuilder vs. ButtonBarBuilder:</strong><br>
 * ButtonBarBuilder uses only 3 component types that can be added:
 * <em>button</em>, <em>standard</em>, and <em>growing button</em>, where
 * ButtonBarBuilder has <em>button</em>, <em>fixed</em>, and <em>growing</em>.
 * Also, the ButtonBarBuilder doesn't group buttons.
 * The layout of the ButtonBarBuilder and ButtonBarBuilder is the same
 * if all buttons are smaller than {@link LayoutStyle#getDefaultButtonWidth()}.
 * If some buttons are wider, ButtonBarBuilder will make only these buttons
 * wider, where the old ButtonBarBuilder makes all (gridded) buttons wider.
 *
 * <p>
 * <strong>Examples:</strong><pre>
 * // Build a right-aligned bar for: OK, Cancel, Apply
 * ButtonBarBuilder builder = new ButtonBarBuilder();
 * builder.addGlue();
 * builder.addButton(okButton);
 * builder.addRelatedGap();
 * builder.addButton(cancelButton);
 * builder.addRelatedGap();
 * builder.addButton(applyButton);
 * return builder.getPanel();
 *
 * // Add a sequence of related buttons
 * ButtonBarBuilder builder = new ButtonBarBuilder();
 * builder.addGlue();
 * builder.addButton(okButton, cancelButton, applyButton);
 * return builder.getPanel();
 *
 * // Add a sequence of related buttons for given Actions
 * ButtonBarBuilder builder = new ButtonBarBuilder();
 * builder.addGlue();
 * builder.addButton(okAction, cancelAction, applyAction);
 * return builder.getPanel();
 * </pre>
 *
 * Buttons are added to a builder individually or as a sequence.
 *
 * To honor the platform's button order (left-to-right vs. right-to-left)
 * this builder uses the <em>leftToRightButtonOrder</em> property.
 * It is initialized with the current LayoutStyle's button order,
 * which in turn is left-to-right on most platforms and right-to-left
 * on the Mac OS X. Builder methods that create sequences of buttons
 * (e.g. {@link #addButton(JComponent[])} honor the button order.
 * If you want to ignore the default button order, you can either
 * add individual buttons, or create a ButtonBarBuilder instance
 * with the order set to left-to-right. For the latter see
 * {@link #createLeftToRightBuilder()}. Also see the button order
 * example below.<p>
 *
 * <strong>Example:</strong><br>
 * The following example builds a button bar with <i>Help</i> button on the
 * left-hand side and <i>OK, Cancel, Apply</i> buttons on the right-hand side.
 * <pre>
 * private JPanel createHelpOKCancelApplyBar(
 *         JButton help, JButton ok, JButton cancel, JButton apply) {
 *     ButtonBarBuilder builder = new ButtonBarBuilder();
 *     builder.addButton(help);
 *     builder.addUnrelatedGap();
 *     builder.addGlue();
 *     builder.addButton(new JButton[]{ok, cancel, apply});
 *     return builder.getPanel();
 * }
 * </pre><p>
 *
 * <strong>Button Order Example:</strong><br>
 * The following example builds three button bars where one honors
 * the platform's button order and the other two ignore it.
 * <pre>
 * public JComponent buildPanel() {
 *     FormLayout layout = new FormLayout("pref");
 *     DefaultFormBuilder rowBuilder = new DefaultFormBuilder(layout);
 *     rowBuilder.setDefaultDialogBorder();
 *
 *     rowBuilder.append(buildButtonSequence(new ButtonBarBuilder()));
 *     rowBuilder.append(buildButtonSequence(ButtonBarBuilder.createLeftToRightBuilder()));
 *     rowBuilder.append(buildIndividualButtons(new ButtonBarBuilder()));
 *
 *     return rowBuilder.getPanel();
 * }
 *
 * private Component buildButtonSequence(ButtonBarBuilder builder) {
 *     builder.addButton(new JButton[] {
 *             new JButton("One"),
 *             new JButton("Two"),
 *             new JButton("Three")
 *     });
 *     return builder.getPanel();
 * }
 *
 * private Component buildIndividualButtons(ButtonBarBuilder builder) {
 *     builder.addButton(new JButton("One"));
 *     builder.addRelatedGap();
 *     builder.addButton(new JButton("Two"));
 *     builder.addRelatedGap();
 *     builder.addButton(new JButton("Three"));
 *     return builder.getPanel();
 * }
 * </pre>
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.18 $
 *
 * @see ButtonStackBuilder
 * @see com.jgoodies.forms.util.LayoutStyle
 */
public class ButtonBarBuilder extends AbstractButtonPanelBuilder {

    /**
     * Specifies the columns of the initial FormLayout used in constructors.
     */
    private static final ColumnSpec[] COL_SPECS  =
        new ColumnSpec[]{};

    /**
     * Specifies the FormLayout's the single button bar row.
     */
    private static final RowSpec[] ROW_SPECS  =
        new RowSpec[]{ RowSpec.decode("center:pref") };


    // Instance Creation ******************************************************

    /**
     * Constructs an empty ButtonBarBuilder on a JPanel.
     */
    public ButtonBarBuilder() {
        this(new JPanel(null));
    }


    /**
     * Constructs an empty ButtonBarBuilder on the given panel.
     *
     * @param panel  the layout container
     */
    public ButtonBarBuilder(JPanel panel) {
        super(new FormLayout(COL_SPECS, ROW_SPECS), panel);
    }


    // Spacing ****************************************************************

    /**
     * Adds a glue that will be given the extra space,
     * if this button bar is larger than its preferred size.
     *
     * @return this builder
     */
    public ButtonBarBuilder addGlue() {
        appendGlueColumn();
        nextColumn();
        return this;
    }


    /**
     * Adds the standard horizontal gap for related components.
     *
     * @return this builder
     *
     * @see LayoutStyle#getRelatedComponentsPadX()
     */
    public ButtonBarBuilder addRelatedGap() {
        appendRelatedComponentsGapColumn();
        nextColumn();
        return this;
    }


    /**
     * Adds the standard horizontal gap for unrelated components.
     *
     * @return this builder
     *
     * @see LayoutStyle#getUnrelatedComponentsPadX()
     */
    public ButtonBarBuilder addUnrelatedGap() {
        appendUnrelatedComponentsGapColumn();
        nextColumn();
        return this;
    }


    /**
     * Adds a horizontal strut of the specified width.
     * For related and unrelated components use {@link #addRelatedGap()}
     * and {@link #addUnrelatedGap()} respectively.
     *
     * @param width  describes the gap width
     *
     * @return this builder
     *
     * @see ColumnSpec#createGap(ConstantSize)
     */
    public ButtonBarBuilder addStrut(ConstantSize width) {
        getLayout().appendColumn(ColumnSpec.createGap(width));
        nextColumn();
        return this;
    }


    // Buttons ****************************************************************

    /**
     * Adds a command button component that has a minimum width
     * specified by the {@link LayoutStyle#getDefaultButtonWidth()}.<p>
     *
     * Although a JButton is expected, any JComponent is accepted
     * to allow custom button component types.
     *
     * @param button  the component to add
     *
     * @return this builder
     *
     * @throws NullPointerException if {@code button} is {@code null}
     */
    public ButtonBarBuilder addButton(JComponent button) {
        button.putClientProperty(NARROW_KEY, Boolean.TRUE);
        getLayout().appendColumn(FormSpecs.BUTTON_COLSPEC);
        add(button);
        nextColumn();
        return this;
    }


    /**
     * Adds a sequence of related button components.
     * Each button has the minimum width as specified by
     * {@link LayoutStyle#getDefaultButtonWidth()}. The gap width between
     * the buttons is {@link LayoutStyle#getRelatedComponentsPadX()}.<p>
     *
     * Although JButtons are expected, general JComponents are accepted
     * to allow custom button component types.
     *
     * @param buttons  the buttons to add
     *
     * @return this builder
     *
     * @throws NullPointerException if the button array or a button is {@code null}
     * @throws IllegalArgumentException if the button array is empty
     *
     * @see #addButton(JComponent)
     */
    public ButtonBarBuilder addButton(JComponent... buttons) {
        checkNotNull(buttons, "The button array must not be null.");
        int length = buttons.length;
        checkArgument(length > 0, "The button array must not be empty.");
        for (int i = 0; i < length; i++) {
            addButton(buttons[i]);
            if (i < buttons.length - 1) {
                addRelatedGap();
            }
        }
        return this;
    }


    /**
     * Adds a JButton for the given Action that has a minimum width
     * specified by the {@link LayoutStyle#getDefaultButtonWidth()}.
     *
     * @param action  the action that describes the button to add
     *
     * @return this builder
     *
     * @throws NullPointerException if {@code action} is {@code null}
     *
     * @see #addButton(JComponent)
     */
    public ButtonBarBuilder addButton(Action action) {
        checkNotNull(action, "The button Action must not be null.");
        return addButton(createButton(action));
    }


    /**
     * Adds a sequence of related JButtons built from the given Actions
     * that are separated by the default gap as specified by
     * {@link LayoutStyle#getRelatedComponentsPadX()}.<p>
     *
     * Uses this builder's button order (left-to-right vs. right-to-left).
     * If you  want to use a fixed order, add individual Actions instead.
     *
     * @param actions   the Actions that describe the buttons to add
     *
     * @return this builder
     *
     * @throws NullPointerException if the Action array or an Action is {@code null}
     * @throws IllegalArgumentException if the Action array is empty
     *
     * @see #addButton(JComponent[])
     */
    public ButtonBarBuilder addButton(Action... actions) {
        checkNotNull(actions, "The Action array must not be null.");
        int length = actions.length;
        checkArgument(length > 0, "The Action array must not be empty.");
        JButton[] buttons = new JButton[length];
        for (int i = 0; i < length; i++) {
            buttons[i] = createButton(actions[i]);
        }
        return addButton(buttons);
    }


    // Other ******************************************************************

    /**
     * Adds a button or other component that grows if the container grows.
     * The component's initial size (before it grows) is specified
     * by the {@link LayoutStyle#getDefaultButtonWidth()}.
     *
     * @param component  the component to add
     *
     * @return this builder
     */
    public ButtonBarBuilder addGrowing(JComponent component) {
    	component.putClientProperty(NARROW_KEY, Boolean.TRUE);
        getLayout().appendColumn(FormSpecs.GROWING_BUTTON_COLSPEC);
        add(component);
        nextColumn();
        return this;
    }


    /**
     * Adds a sequence of related growing buttons
     * where each is separated by a default gap.
     *
     * @param buttons  an array of buttons to add
     *
     * @return this builder
     *
     * @see LayoutStyle
     */
    public ButtonBarBuilder addGrowing(JComponent... buttons) {
        int length = buttons.length;
        for (int i = 0; i < length; i++) {
            addGrowing(buttons[i]);
            if (i < buttons.length - 1) {
                addRelatedGap();
            }
        }
        return this;
    }


    /**
     * Adds a fixed size component with narrow margin. Unlike the gridded
     * components, this component keeps its individual preferred dimension.
     *
     * @param component  the component to add
     *
     * @return this builder
     */
    public ButtonBarBuilder addFixed(JComponent component) {
        component.putClientProperty(NARROW_KEY, Boolean.TRUE);
        getLayout().appendColumn(FormSpecs.PREF_COLSPEC);
        add(component);
        nextColumn();
        return this;
    }


}
