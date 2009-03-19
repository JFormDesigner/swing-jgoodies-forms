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

import javax.swing.Action;
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
 * using the {@link FormLayout}.<p>
 *
 * This builder sets a hint for narrow  margin for the gridded buttons.
 * This can reduce the button stack's width if some buttons have long texts.
 * For example, a stack with 'OK', 'Cancel', 'Configure&hellip;' will likely
 * exceed the minimum button width. The narrow margins help getting narrow
 * stacks.
 * Note that some look&amp;feels do not support the narrow margin feature,
 * and conversely, others have only narrow margins. The JGoodies look&amp;feels
 * honor the setting, the Mac Aqua l&amp;f uses narrow margins all the time.<p>
 *
 * <strong>Example:</strong><br>
 * The following example builds a button stack with <i>Close, Up</i> and
 * <i>Down</i>, where Up and Down are related, and Close is not related
 * to the other buttons, which makes a wide gap for the unrelated and
 * a smaller gap for the related buttons.
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
 * @version $Revision: 1.11 $
 *
 * @see ButtonBarBuilder2
 * @see com.jgoodies.forms.factories.ButtonBarFactory
 * @see com.jgoodies.forms.util.LayoutStyle
 */
public final class ButtonStackBuilder extends PanelBuilder {

    /**
     * Specifies the FormLayout's the single button stack column.
     */
    private static final ColumnSpec[] COL_SPECS =
        new ColumnSpec[] { FormFactory.BUTTON_COLSPEC };

    /**
     * Specifies the rows of the initial FormLayout used in constructors.
     */
    private static final RowSpec[] ROW_SPECS =
        new RowSpec[]{};

    /**
     * The client property key used to indicate that a button shall
     * get narrow margins on the left and right hand side.<p>
     *
     * This optional setting will be honored by all JGoodies Look&amp;Feel
     * implementations. The Mac Aqua l&amp;f uses narrow margins only.
     * Other look&amp;feel implementations will likely ignore this key
     * and so may render a wider button margin.
     */
    private static final String NARROW_KEY = "jgoodies.isNarrow";


    // Instance Creation ****************************************************

    /**
     * Constructs a ButtonStackBuilder  on a default JPanel
     * using a preconfigured FormLayout as layout manager.
     */
    public ButtonStackBuilder() {
        this(new JPanel(null));
    }


    /**
     * Constructs a ButtonStackBuilder on the given panel
     * using a preconfigured FormLayout as layout manager.
     *
     * @param panel   the layout container
     */
    public ButtonStackBuilder(JPanel panel) {
        this(new FormLayout(COL_SPECS, ROW_SPECS), panel);
    }


    /**
     * Constructs a ButtonStackBuilder on the given panel and layout.
     * The layout must have at least one column.
     *
     * @param layout  the FormLayout used to layout
     * @param panel   the layout container
     *
     * @since 1.2
     */
    public ButtonStackBuilder(FormLayout layout, JPanel panel) {
        super(layout, panel);
        setOpaque(false);
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
        component.putClientProperty(NARROW_KEY, Boolean.TRUE);
        add(component);
        nextRow();
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


    // Convenience Methods ***************************************************

    /**
     * Adds the given button.
     * Equivalent to:
     * <pre>addButton(new JButton[]{button});</pre>
     *
     * @param button   the button to add
     *
     * @since 1.3.0
     */
    public void addButton(JButton button) {
        addButton(new JButton[]{button});
    }


    /**
     * Adds the given buttons as a sequence of related buttons.
     * Equivalent to:
     * <pre>addButton(new JButton[]{button1, button2});</pre>
     *
     * @param button1   the first button to add
     * @param button2   the second button to add
     *
     * @since 1.3.0
     */
    public void addButton(JButton button1, JButton button2) {
        addButton(new JButton[]{button1, button2});
    }


    /**
     * Adds the given buttons as a sequence of related buttons.
     * Equivalent to:
     * <pre>addButton(new JButton[]{button1, button2, button3});</pre>
     *
     * @param button1   the first button to add
     * @param button2   the second button to add
     * @param button3   the third button to add
     *
     * @since 1.3.0
     */
    public void addButton(JButton button1, JButton button2, JButton button3) {
        addButton(new JButton[]{button1, button2, button3});
    }


    /**
     * Adds the given buttons as a sequence of related buttons.
     * Equivalent to:
     * <pre>addButton(new JButton[]{button1, button2, button3, button4});</pre>
     *
     * @param button1   the first button to add
     * @param button2   the second button to add
     * @param button3   the third button to add
     * @param button4   the fourth button to add
     *
     * @since 1.3.0
     */
    public void addButton(
            JButton button1,
            JButton button2,
            JButton button3,
            JButton button4) {
        addButton(new JButton[]{button1, button2, button3, button4});
    }


    /**
     * Adds a sequence of related buttons separated by a default gap.
     *
     * @param buttons  the array of buttons to add
     *
     * @since 1.3.0
     */
    public void addButton(JButton[] buttons) {
        addButtons(buttons);
    }


    /**
     * Creates a {@code JButton} for the given Action and adds
     * the button to this builder.
     * Equivalent to:
     * <pre>addButton(new Action[]{action});</pre>
     *
     * @param action   the Action used to create the button to add
     *
     * @since 1.3.0
     */
    public void addButton(Action action) {
        addButton(new Action[]{action});
    }


    /**
     * Creates {@code JButton}s for the given Actions and adds
     * the buttons to this builder.
     * Equivalent to:
     * <pre>addButton(new Action[]{action1, action2});</pre>
     *
     * @param action1   the Action used to create the first button to add
     * @param action2   the Action used to create the second button to add
     *
     * @since 1.3.0
     */
    public void addButton(Action action1, Action action2) {
        addButton(new Action[]{action1, action2});
    }


    /**
     * Creates {@code JButton}s for the given Actions and adds
     * the buttons to this builder.
     * Equivalent to:
     * <pre>addButton(new Action[]{action1, action2, action3});</pre>
     *
     * @param action1   the Action used to create the first button to add
     * @param action2   the Action used to create the second button to add
     * @param action3   the Action used to create the third button to add
     *
     * @since 1.3.0
     */
    public void addButton(Action action1, Action action2, Action action3) {
        addButton(new Action[]{action1, action2, action3});
    }


    /**
     * Creates {@code JButton}s for the given Actions and adds
     * the buttons to this builder.
     * Equivalent to:
     * <pre>addButton(new Action[]{action1, action2, action3, action4});</pre>
     *
     * @param action1   the Action used to create the first button to add
     * @param action2   the Action used to create the second button to add
     * @param action3   the Action used to create the third button to add
     * @param action4   the Action used to create the fourth button to add
     *
     * @since 1.3.0
     */
    public void addButton(Action action1, Action action2, Action action3, Action action4) {
        addButton(new Action[]{action1, action2, action3, action4});
    }


    /**
     * Constructs an array of JButtons from the given Action array,
     * and adds them as a sequence of related buttons separated by a default gap.
     *
     * @param actions  an array of buttons to add
     */
    public void addButton(Action[] actions) {
        JButton[] buttons = new JButton[actions.length];
        for (int i = 0; i < actions.length; i++) {
            buttons[i] = new JButton(actions[i]);
        }
        addButtons(buttons);
    }


}
