/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

/**
 * A singleton implementaton of the {@link ComponentFactory} interface
 * that creates UI components as required by the 
 * {@link com.jgoodies.forms.builder.PanelBuilder}.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.8 $
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
     * Creates and returns a label with an optional mnemonic.
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
     * and font of a <code>TitledBorder</code>.
     * 
     * @param textWithMnemonic  the title's text - may contain a mnemonic
     * @return an emphasized title label
     */
    public JLabel createTitle(String textWithMnemonic) {
        return createTitle(textWithMnemonic, 0);
    }
    
    
    /**
     * Creates and return a label that uses the foreground color
     * and font of a <code>TitledBorder</code>.
     * 
     * @param textWithMnemonic  the title's text - may contain a mnemonic
     * @param gap               the right-hand side gap
     * @return an emphasized title label
     */
    private JLabel createTitle(String textWithMnemonic, int gap) {
        JLabel label = new TitleLabel();
        setTextAndMnemonic(label, textWithMnemonic);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, gap));
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
     * <code>TitledBorder</code>.<p>
     * 
     * TODO: Honor the alignment.
     * 
     * @param text  the title's text
     * @param alignment   text alignment: left, center, right 
     * @return a separator with title label 
     */
    public JComponent createSeparator(String text, int alignment) {
        JPanel header = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        if (text != null && text.length() > 0) {
            header.add(createTitle(text, 4), gbc);
        }

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridheight = 1;
        JSeparator separator = new JSeparator();
        header.add(Box.createGlue(), gbc);
        gbc.weighty = 0.0;
        header.add(separator, gbc);
        gbc.weighty = 1.0;
        header.add(Box.createGlue(), gbc);

        return header;
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
         * TODO: It seems that the Mac Aqua l&f doesn't set the
         * TitledBorder settings in the <code>UIDefaults</code> table. 
         * Consider asking a <code>TitledBorder</code> instance for its 
         * font and font color use <code>#getTitleFont</code> and 
         * <code>#getTitleColor</code> instead.<p>
         * 
         * The same problem may appear with the Synth-based looks.
         */
        public void updateUI() {
            super.updateUI();
            Color foreground =
                UIManager.getColor("TitledBorder.titleColor");
            if (foreground != null)
                setForeground(foreground);
            setFont(UIManager.getFont("TitledBorder.font"));
        }
        
    }

}