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

package com.jgoodies.forms.factories;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

/**
 * A singleton implementaton of the {@link ComponentFactory} interface
 * that creates UI components as required by the 
 * {@link com.jgoodies.forms.builder.PanelBuilder}.<p>
 * 
 * The texts used in methods <code>#createLabel(String)</code> and 
 * <code>#createTitle(String)</code> can contain an optional mnemonic marker. 
 * The mnemonic and mnemonic index are indicated by a single ampersand 
 * (<tt>&amp;</tt>). For example <tt>&quot;&amp;Save&quot</tt>, 
 * or <tt>&quot;Save&nbsp;&amp;as&quot</tt>. To use the ampersand itself 
 * duplicate it, for example <tt>&quot;Look&amp;&amp;Feel&quot</tt>.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.13 $
 */

public class DefaultComponentFactory implements ComponentFactory {
    
    /**
     * Holds the single instance of this class.
     */
    private static final DefaultComponentFactory INSTANCE =
        new DefaultComponentFactory();

    /** 
     * The character used to indicate the mnemonic position for labels. 
     */
    private static final char MNEMONIC_MARKER = '&';
    
    private static final ColumnSpec PREF_GROWING_COL_SPEC = 
        new ColumnSpec(ColumnSpec.FILL, Sizes.PREFERRED, ColumnSpec.DEFAULT_GROW);
        
        
    // Instance *************************************************************
        
    private DefaultComponentFactory() {
        // Suppresses default constructor, ensuring non-instantiability.
    }
    
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
     * @param textWithMnemonic  the label's text - may contain a mnemonic 
     * @return an label with optional mnemonic
     */
    public JLabel createLabel(String textWithMnemonic) {
        JLabel label = new JLabel();
        setTextAndMnemonic(label, textWithMnemonic);
        return label;
    }
    
    
    /**
     * Creates and returns a label that uses the foreground color
     * and font of a <code>TitledBorder</code>.<p>
     * 
     * <pre>
     * createTitle("Name");       // No mnemonic
     * createTitle("N&ame");      // Mnemonic is 'a'
     * createTitle("Save &as");   // Mnemonic is the second 'a'
     * createTitle("Look&&Feel"); // No mnemonic, text is Look&Feel
     * </pre>
     * 
     * @param textWithMnemonic  the title's text - may contain a mnemonic
     * @return an emphasized title label
     */
    public JLabel createTitle(String textWithMnemonic) {
        JLabel label = new TitleLabel();
        setTextAndMnemonic(label, textWithMnemonic);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    
    /**
     * Creates and returns a labeled separator with the label in the left-hand
     * side. Useful to separate paragraphs in a panel; often a better choice 
     * than a <code>TitledBorder</code>.<p>
     * 
     * @param text  the title's text
     * @return a title label with separator on the side
     */
    public JComponent createSeparator(String text) {
        return createSeparator(text, SwingConstants.LEFT);
    }
    
    /**
     * Creates and returns a labeled separator. Useful to separate paragraphs 
     * in a panel, which is often a better choice than a 
     * <code>TitledBorder</code>.
     * 
     * @param text  the title's text
     * @param alignment   text alignment: left, center, right 
     * @return a separator with title label 
     */
    public JComponent createSeparator(String text, int alignment) {
        if (text == null || text.length() == 0) {
            return new JSeparator();
        }
        JLabel title = createTitle(text);
        JPanel panel = new JPanel();
        ColumnSpec gapSpec = new ColumnSpec(
                isLafAqua() ? Sizes.DLUX1 : Sizes.DLUX3);
        FormLayout layout;
        CellConstraints cc = new CellConstraints();
        if (alignment == SwingConstants.LEFT) {
            layout = new FormLayout(
                    new ColumnSpec[]{
                            FormFactory.PREF_COLSPEC,
                            gapSpec,
                            PREF_GROWING_COL_SPEC},
                    new RowSpec[] {
                            FormFactory.PREF_ROWSPEC});
            panel.setLayout(layout);
            panel.add(title,            cc.xy(1, 1));
            panel.add(new JSeparator(), cc.xy(3, 1));
        } else if (alignment == SwingConstants.RIGHT) {
            layout = new FormLayout(
                    new ColumnSpec[]{
                            PREF_GROWING_COL_SPEC,
                            gapSpec,
                            FormFactory.PREF_COLSPEC},
                    new RowSpec[] {
                            FormFactory.PREF_ROWSPEC});
            panel.setLayout(layout);
            panel.add(new JSeparator(), cc.xy(1, 1));
            panel.add(title,            cc.xy(3, 1));
        } else if (alignment == SwingConstants.CENTER) {
            layout = new FormLayout(
                    new ColumnSpec[]{
                            PREF_GROWING_COL_SPEC,
                            gapSpec,
                            FormFactory.PREF_COLSPEC,
                            gapSpec,
                            PREF_GROWING_COL_SPEC},
                    new RowSpec[] {
                            FormFactory.PREF_ROWSPEC});
            panel.setLayout(layout);
            panel.add(new JSeparator(), cc.xy(1, 1));
            panel.add(title,            cc.xy(3, 1));
            panel.add(new JSeparator(), cc.xy(5, 1));
        } 
        return panel;
    }
    
    
    // Helper Code ***********************************************************
    
    /**
     * Sets the text of the given label and optionally a mnemonic.
     * The given text may contain mnemonic markers <b>&&</b>,
     * where such a marker indicates that the following character
     * shall be the mnemonic. If you want to use the <b>\&</b> 
     * charachter, just put two together, for example &quot;&&&&&quot;.
     *  
     * @param label             the label that gets a mnemonic
     * @param textWithMnemonic  the text with optional mnemonic marker
     */
    private static void setTextAndMnemonic(
        JLabel label,
        String textWithMnemonic) {
        int markerIndex = textWithMnemonic.indexOf(MNEMONIC_MARKER);
        // No marker at all
        if (markerIndex == -1) {
            label.setText(textWithMnemonic);
            return;
        }
        int mnemonicIndex = -1;
        int begin = 0;
        int end;
        int length = textWithMnemonic.length();
        StringBuffer buffer = new StringBuffer();
        do {
            // Check whether the next index has a mnemonic marker, too
            if (markerIndex + 1 < length 
                && textWithMnemonic.charAt(markerIndex + 1) == MNEMONIC_MARKER) {
                end = markerIndex + 1;
            } else {
                end = markerIndex;
                if (mnemonicIndex == -1)
                    mnemonicIndex = markerIndex;
            }
            buffer.append(textWithMnemonic.substring(begin, end));
            begin = end + 1;
            markerIndex =  begin < length 
                ? textWithMnemonic.indexOf(MNEMONIC_MARKER, begin)
                : -1;
        } while (markerIndex != -1);
        buffer.append(textWithMnemonic.substring(begin));

        label.setText(buffer.toString());
        if ((mnemonicIndex != -1) && (mnemonicIndex + 1 < length)) {
            label.setDisplayedMnemonic(
                textWithMnemonic.charAt(mnemonicIndex + 1));
            label.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    // A label that uses the TitleBorder font and color
    private static class TitleLabel extends JLabel {
        
        private TitleLabel() {
            // Just invoke the super constructor.
        }
        
        private TitleLabel(String text) {
            super(text);
        }
        
        /**
         * TODO: Consider asking a <code>TitledBorder</code> instance for its 
         * font and font color use <code>#getTitleFont</code> and 
         * <code>#getTitleColor</code> for the Synth-based looks.
         */
        public void updateUI() {
            super.updateUI();
            Color foreground =
                UIManager.getColor("TitledBorder.titleColor");
            if (foreground != null)
                setForeground(foreground);
            setFont(getTitleFont());
        }
        
        private Font getTitleFont() {
            return isLafAqua()
            	? UIManager.getFont("Label.font").deriveFont(Font.BOLD) 
                : UIManager.getFont("TitledBorder.font");
        }
        
    }
    
    
    // TODO: Move the code below this line to a new class 
    // com.jgoodies.forms.util.Utilities 

    // Caching and Lazily Computing the Laf State *****************************
    
    /**
     * Holds the cached result of the Aqua l&amp;f check.
     * Is invalidated by the <code>LookAndFeelChangeHandler</code>
     * if the look&amp;feel changes.
     */
    private static Boolean cachedIsLafAqua;
    
    /**
     * Describes whether the <code>LookAndFeelChangeHandler</code>
     * has been registered with the <code>UIManager</code> or not.
     * It is registered lazily when the first cached l&amp;f state is computed.
     */
    private static boolean changeHandlerRegistered = false;
    
    private synchronized static void ensureLookAndFeelChangeHandlerRegistered() {
        if (!changeHandlerRegistered)
            UIManager.addPropertyChangeListener(new LookAndFeelChangeHandler());
    }
    
    /**
     * Lazily checks and answers whether the Aqua look&amp;feel is active.
     * 
     * @return true if the current look&amp;feel is Aqua
     */
    private static boolean isLafAqua() {
        if (cachedIsLafAqua == null) {
            cachedIsLafAqua = Boolean.valueOf(computeIsLafAqua());
            ensureLookAndFeelChangeHandlerRegistered();
        }
        return cachedIsLafAqua.booleanValue();
    }
    
    /**
     * Computes and answers whether the Aqua look&amp;feel is active.
     * 
     * @return true if the current look&amp;feel is Aqua
     */
    private static boolean computeIsLafAqua() {
        LookAndFeel laf = UIManager.getLookAndFeel();
        return laf.getName().startsWith("Mac OS X Aqua");
    }

    // Handles l&amp;f changes
    private static class LookAndFeelChangeHandler implements PropertyChangeListener {
        
        /**
         * Invalidates the cached laf states if the look&amp;feel changes.
         * 
         * @param evt  describes the property change
         */
        public void propertyChange(PropertyChangeEvent evt) {
            cachedIsLafAqua = null;
        }
    }
    
}