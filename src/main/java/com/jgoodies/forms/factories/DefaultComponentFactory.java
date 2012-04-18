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

package com.jgoodies.forms.factories;

import static com.jgoodies.common.base.Preconditions.checkArgument;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.accessibility.AccessibleContext;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.base.Strings;
import com.jgoodies.common.swing.MnemonicUtils;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.util.FormUtils;

/**
 * A singleton implementation of the {@link ComponentFactory} interface
 * that creates UI components as required by the
 * {@link com.jgoodies.forms.builder.PanelBuilder}.<p>
 *
 * The texts used in methods {@code #createLabel(String)} and
 * {@code #createTitle(String)} can contain an optional mnemonic marker.
 * The mnemonic and mnemonic index are indicated by a single ampersand
 * (<tt>&amp;</tt>). For example <tt>&quot;&amp;Save&quot</tt>,
 * or <tt>&quot;Save&nbsp;&amp;as&quot</tt>. To use the ampersand itself
 * duplicate it, for example <tt>&quot;Look&amp;&amp;Feel&quot</tt>.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.21 $
 */
public class DefaultComponentFactory implements ComponentFactory {

    /**
     * Holds the single instance of this class.
     */
    private static final DefaultComponentFactory INSTANCE =
        new DefaultComponentFactory();


    // Instance *************************************************************

    /**
     * Returns the sole instance of this factory class.
     *
     * @return the sole instance of this factory class
     */
    public static DefaultComponentFactory getInstance() {
        return INSTANCE;
    }


    // Component Creation ***************************************************

    /**
     * Creates and returns a label with an optional mnemonic.<p>
     *
     * <pre>
     * createLabel("Name");       // No mnemonic
     * createLabel("N&ame");      // Mnemonic is 'a'
     * createLabel("Save &as");   // Mnemonic is the second 'a'
     * createLabel("Look&&Feel"); // No mnemonic, text is Look&Feel
     * </pre>
     *
     * @param textWithMnemonic  the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return an label with optional mnemonic
     */
    @Override
	public JLabel createLabel(String textWithMnemonic) {
        JLabel label = new FormsLabel();
        MnemonicUtils.configure(label, textWithMnemonic);
        return label;
    }


    /**
     * Creates and returns a label with an optional mnemonic
     * that is intended to label a read-only component.<p>
     *
     * <pre>
     * createReadOnlyLabel("Name");       // No mnemonic
     * createReadOnlyLabel("N&ame");      // Mnemonic is 'a'
     * createReadOnlyLabel("Save &as");   // Mnemonic is the second 'a'
     * createReadOnlyLabel("Look&&Feel"); // No mnemonic, text is Look&Feel
     * </pre>
     *
     * @param textWithMnemonic  the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return an label with optional mnemonic intended for read-only
     *     components
     *
     * @since 1.3
     */
    @Override
	public JLabel createReadOnlyLabel(String textWithMnemonic) {
        JLabel label = new ReadOnlyLabel();
        MnemonicUtils.configure(label, textWithMnemonic);
        return label;
    }


    /**
     * Creates and returns a button that is bound to the given Action.
     * Useful to return customized buttons, for example, the JGoodies
     * {@code JGButton} is bound to some custom Action properties.<p>
     *
     * This default implementation just returns a {@code JButton}.
     *
     * @param action    provides [bound] visual properties for the button
     * @return the created button
     *
     * @since 1.4
     */
    @Override
	public JButton createButton(Action action) {
        return new JButton(action);
    }


    /**
     * Creates and returns a title label that uses the foreground color
     * and font of a {@code TitledBorder}.<p>
     *
     * <pre>
     * createTitle("Name");       // No mnemonic
     * createTitle("N&ame");      // Mnemonic is 'a'
     * createTitle("Save &as");   // Mnemonic is the second 'a'
     * createTitle("Look&&Feel"); // No mnemonic, text is Look&Feel
     * </pre>
     *
     * @param textWithMnemonic  the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return an emphasized title label
     */
    @Override
	public JLabel createTitle(String textWithMnemonic) {
        JLabel label = new TitleLabel();
        MnemonicUtils.configure(label, textWithMnemonic);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    
    @Override
    public JLabel createHeaderLabel(String markedText) {
    	return createTitle(markedText);
    }


    /**
     * Creates and returns a labeled separator with the label in the left-hand
     * side. Useful to separate paragraphs in a panel; often a better choice
     * than a {@code TitledBorder}.<p>
     *
     * <pre>
     * createSeparator("Name");       // No mnemonic
     * createSeparator("N&ame");      // Mnemonic is 'a'
     * createSeparator("Save &as");   // Mnemonic is the second 'a'
     * createSeparator("Look&&Feel"); // No mnemonic, text is Look&Feel
     * </pre>
     *
     * @param textWithMnemonic  the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @return a title label with separator on the side
     */
    public JComponent createSeparator(String textWithMnemonic) {
        return createSeparator(textWithMnemonic, SwingConstants.LEFT);
    }


    /**
     * Creates and returns a labeled separator. Useful to separate
     * paragraphs in a panel, which is often a better choice than a
     * {@code TitledBorder}.<p>
     *
     * <pre>
     * final int LEFT = SwingConstants.LEFT;
     * createSeparator("Name",       LEFT); // No mnemonic
     * createSeparator("N&ame",      LEFT); // Mnemonic is 'a'
     * createSeparator("Save &as",   LEFT); // Mnemonic is the second 'a'
     * createSeparator("Look&&Feel", LEFT); // No mnemonic, text is Look&Feel
     * </pre>
     *
     * @param textWithMnemonic  the label's text -
     *     may contain an ampersand (<tt>&amp;</tt>) to mark a mnemonic
     * @param alignment text alignment, one of {@code SwingConstants.LEFT},
     *     {@code SwingConstants.CENTER}, {@code SwingConstants.RIGHT}
     * @return a separator with title label
     */
    @Override
	public JComponent createSeparator(String textWithMnemonic, int alignment) {
        if (Strings.isBlank(textWithMnemonic)) {
            return new JSeparator();
        }
        JLabel title = createTitle(textWithMnemonic);
        title.setHorizontalAlignment(alignment);
        return createSeparator(title);
    }


    /**
     * Creates and returns a labeled separator. Useful to separate
     * paragraphs in a panel, which is often a better choice than a
     * {@code TitledBorder}.<p>
     *
     * The label's position is determined by the label's horizontal alignment,
     * which must be one of:
     * {@code SwingConstants.LEFT},
     * {@code SwingConstants.CENTER},
     * {@code SwingConstants.RIGHT}.<p>
     *
     * TODO: Since this method has been marked public in version 1.0.6,
     * we need to precisely describe the semantic of this method.<p>
     *
     * TODO: Check if we can relax the constraint for the label alignment
     * and also accept LEADING and TRAILING.
     *
     * @param label       the title label component
     * @return a separator with title label
     *
     * @throws NullPointerException       if the label is {@code null}
     * @throws IllegalArgumentException   if the label's horizontal alignment
     *      is not one of: {@code SwingConstants.LEFT},
     *      {@code SwingConstants.CENTER},
     *      {@code SwingConstants.RIGHT}.
     *
     * @since 1.0.6
     */
    public JComponent createSeparator(JLabel label) {
        Preconditions.checkNotNull(label, "The label must not be null.");
        int horizontalAlignment = label.getHorizontalAlignment();
        checkArgument(
                horizontalAlignment == SwingConstants.LEFT
             || horizontalAlignment == SwingConstants.CENTER
             || horizontalAlignment == SwingConstants.RIGHT,
            "The label's horizontal alignment must be one of: LEFT, CENTER, RIGHT.");
        JPanel panel = new JPanel(new TitledSeparatorLayout(!FormUtils.isLafAqua()));
        panel.setOpaque(false);
        panel.add(label);
        panel.add(new JSeparator());
        if (horizontalAlignment == SwingConstants.CENTER) {
            panel.add(new JSeparator());
        }
        return panel;
    }


    // Helper Classes *********************************************************

    /**
     * Differs from its superclass JLabel in that it removes a trailing
     * colon (':') - if any - from the label's accessible name.
     * This in turn leads to improved accessible names for components
     * labeled by FormLabels via {@code #setLabelFor}.
     */
    private static class FormsLabel extends JLabel {


        // Accessibility Support ----------------------------------------------

        /**
         * Lazily creates and returns the {@link AccessibleContext} associated
         * with this label, an instance of {@code AccessibleFormsLabel}.
         *
         * @return this link's AccessibleContext
         */
        @Override
        public AccessibleContext getAccessibleContext() {
            if (accessibleContext == null) {
                accessibleContext = new AccessibleFormsLabel();
            }
            return accessibleContext;
        }


        /**
         * This class implements accessibility support for FormsLabels.
         * Cuts off trailing colons from the accessible name - if any.
         */
        private final class AccessibleFormsLabel extends AccessibleJLabel {

            /**
             * Returns the accessible name of this label.
             * Unlike the superclass behavior, this implementation
             * cuts off a trailing colon (':') - if any.
             *
             * @return the label name
             *
             * @see AccessibleContext#setAccessibleName
             */
            @Override
            public String getAccessibleName() {
                if (accessibleName != null) {
                    return accessibleName;
                }
                String text = FormsLabel.this.getText();
                if (text == null) {
                    return super.getAccessibleName();
                }
                return text.endsWith(":")
                    ? text.substring(0, text.length()-1)
                    : text;
            }

        }

    }


    /**
     * A FormsLabel subclasses intended to label read-only components.
     * Aims to set the disabled foreground color.
     */
    private static final class ReadOnlyLabel extends FormsLabel {

        private static final String[] UIMANAGER_KEYS =
            {"Label.disabledForeground",
             "Label.disabledText",
             "Label[Disabled].textForeground",
             "textInactiveText"};

        @Override
        public void updateUI() {
            super.updateUI();
            setForeground(getDisabledForeground());
        }

        private static Color getDisabledForeground() {
            Color foreground;
            for (String key : UIMANAGER_KEYS) {
                foreground = UIManager.getColor(key);
                if (foreground != null) {
                    // System.out.println("Matching key=" + key + "; color=" + foreground);
                    return foreground;
                }
            }
            return null;
        }
    }


    /**
     * A label that uses the TitleBorder font and color.
     */
    private static final class TitleLabel extends FormsLabel {

        private TitleLabel() {
            // Just invoke the super constructor.
        }

        /**
         * TODO: For the Synth-based L&amp;f we should consider asking
         * a {@code TitledBorder} instance for its font and color using
         * {@code #getTitleFont} and {@code #getTitleColor} resp.
         */
        @Override
        public void updateUI() {
            super.updateUI();
            Color foreground = getTitleColor();
            if (foreground != null) {
                setForeground(foreground);
            }
            setFont(getTitleFont());
        }

        private static Color getTitleColor() {
            return UIManager.getColor("TitledBorder.titleColor");
        }

        /**
         * Looks up and returns the font used for title labels.
         * Since Mac Aqua uses an inappropriate titled border font,
         * we use a bold label font instead. Actually if the title
         * is used in a titled separator, the bold weight is questionable.
         * It seems that most native Aqua tools use a plain label in
         * titled separators.
         *
         * @return the font used for title labels
         */
        private static Font getTitleFont() {
            return FormUtils.isLafAqua()
            	? UIManager.getFont("Label.font").deriveFont(Font.BOLD)
                : UIManager.getFont("TitledBorder.font");
        }

    }


    /**
     * A layout for the title label and separator(s) in titled separators.
     */
    private static final class TitledSeparatorLayout implements LayoutManager {

        private final boolean centerSeparators;

        /**
         * Constructs a TitledSeparatorLayout that either centers the separators
         * or aligns them along the font baseline of the title label.
         *
         * @param centerSeparators  true to center, false to align along
         *     the font baseline of the title label
         */
        private TitledSeparatorLayout(boolean centerSeparators) {
            this.centerSeparators = centerSeparators;
        }

        /**
         * Does nothing. This layout manager looks up the components
         * from the layout container and used the component's index
         * in the child array to identify the label and separators.
         *
         * @param name the string to be associated with the component
         * @param comp the component to be added
         */
        @Override
		public void addLayoutComponent(String name, Component comp) {
            // Does nothing.
        }

        /**
         * Does nothing. This layout manager looks up the components
         * from the layout container and used the component's index
         * in the child array to identify the label and separators.
         *
         * @param comp the component to be removed
         */
        @Override
		public void removeLayoutComponent(Component comp) {
            // Does nothing.
        }

        /**
         * Computes and returns the minimum size dimensions
         * for the specified container. Forwards this request
         * to {@code #preferredLayoutSize}.
         *
         * @param parent the component to be laid out
         * @return the container's minimum size.
         * @see #preferredLayoutSize(Container)
         */
        @Override
		public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        /**
         * Computes and returns the preferred size dimensions
         * for the specified container. Returns the title label's
         * preferred size.
         *
         * @param parent the component to be laid out
         * @return the container's preferred size.
         * @see #minimumLayoutSize(Container)
         */
        @Override
		public Dimension preferredLayoutSize(Container parent) {
            Component label = getLabel(parent);
            Dimension labelSize = label.getPreferredSize();
            Insets insets = parent.getInsets();
            int width  = labelSize.width  + insets.left + insets.right;
            int height = labelSize.height + insets.top  + insets.bottom;
            return new Dimension(width, height);
        }

        /**
         * Lays out the specified container.
         *
         * @param parent the container to be laid out
         */
        @Override
		public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                // Look up the parent size and insets
                Dimension size = parent.getSize();
                Insets insets = parent.getInsets();
                int width  = size.width - insets.left - insets.right;

                // Look up components and their sizes
                JLabel label = getLabel(parent);
                Dimension labelSize = label.getPreferredSize();
                int labelWidth  = labelSize.width;
                int labelHeight = labelSize.height;
                Component separator1 = parent.getComponent(1);
                int separatorHeight  = separator1.getPreferredSize().height;

                FontMetrics metrics = label.getFontMetrics(label.getFont());
                int ascent  = metrics.getMaxAscent();
                int hGapDlu = centerSeparators ? 3 : 1;
                int hGap    = Sizes.dialogUnitXAsPixel(hGapDlu, label);
                int vOffset = centerSeparators
                    ? 1 + (labelHeight - separatorHeight) / 2
                    : ascent - separatorHeight / 2;

                int alignment = label.getHorizontalAlignment();
                int y = insets.top;
                if (alignment == SwingConstants.LEFT) {
                    int x = insets.left;
                    label.setBounds(x, y, labelWidth, labelHeight);
                    x+= labelWidth;
                    x+= hGap;
                    int separatorWidth = size.width - insets.right - x;
                    separator1.setBounds(x, y + vOffset, separatorWidth, separatorHeight);
                } else if (alignment == SwingConstants.RIGHT) {
                    int x = insets.left + width - labelWidth;
                    label.setBounds(x, y, labelWidth, labelHeight);
                    x -= hGap;
                    x--;
                    int separatorWidth = x - insets.left;
                    separator1.setBounds(insets.left, y + vOffset, separatorWidth, separatorHeight);
                } else {
                    int xOffset = (width - labelWidth - 2*hGap) / 2;
                    int x = insets.left;
                    separator1.setBounds(x, y + vOffset, xOffset-1, separatorHeight);
                    x += xOffset;
                    x += hGap;
                    label.setBounds(x, y, labelWidth, labelHeight);
                    x += labelWidth;
                    x += hGap;
                    Component separator2 = parent.getComponent(2);
                    int separatorWidth = size.width - insets.right - x;
                    separator2.setBounds(x, y + vOffset,separatorWidth ,separatorHeight);
                }
            }
        }

        private static JLabel getLabel(Container parent) {
            return (JLabel) parent.getComponent(0);
        }

    }


}
