/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.factories;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.*;

/**
 * A singleton implementaton of the {@link ComponentFactory} interface
 * that creates UI components as required by the 
 * {@link com.jgoodies.forms.builder.PanelBuilder}.
 *
 * @author Karsten Lentzsch
 */

public class DefaultComponentFactory implements ComponentFactory {
    
    /**
     * Holds the single instance of this class.
     */
    private static final DefaultComponentFactory INSTANCE =
        new DefaultComponentFactory();

    /**
     * Indicates whether this is a J2SE 1.2 or 1.3. 
     */
    private static final boolean IS_BEFORE_14 = isBefore14();
    
    /** 
     * The character used to indicate the mnemonic position for labels. 
     */
    private static final char MNEMONIC_MARKER = '&';
        
        
    // Instance *************************************************************
        
    // Override default constructor; prevents instantiation.
    private DefaultComponentFactory() {
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
     * Creates and answers a label with an optional mnemonic.
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
     * Creates and answers a label that uses the foreground color
     * and font of a <code>TitledBorder</code>.
     * 
     * @param textWithMnemonic  the title's text - may contain a mnemonic
     * @return an emphasized title label
     */
    public JLabel createTitle(String textWithMnemonic) {
        JLabel label = new TitleLabel();
        setTextAndMnemonic(label, textWithMnemonic);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 4));
        return label;
    }

    /**
     * Creates and answers a label with separator on the left hand side. 
     * Useful to separate paragraphs in a panel. This is often a better choice
     * than a <code>TitledBorder</code>.
     * <p>
     * The current implementation doesn't support component alignments.
     * 
     * @param text  the title's text
     * @return a title label with separator on the side
     */
    public JComponent createSeparator(String text) {
        return createSeparator(text, JLabel.LEFT);
    }
    
    /**
     * Creates and answers a label with separator; useful to separate 
     * paragraphs in a panel. This is often a better choice than 
     * a <code>TitledBorder</code>.
     * <p>
     * The current implementation doesn't support component alignments.
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
            header.add(createTitle(text), gbc);
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
    public static void setTextAndMnemonic(
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
            setDisplayedMnemonicIndex(label, mnemonicIndex);
        }
    }

    /**
     * Sets the displayed mnemonic index of the given label.
     * In 1.4 environments we just delegate to 
     * <code>JLabel#setDisplayedMnemonicIndex</code>.
     * In 1.3 environments the mnemonic index is set as
     * a client property, so that a look can honor it
     * - so do the JGoodies l&amp;fs.
     * <p>
     * TODO: Obsolete in 1.4
     *  
     * @param label                   the label that gets a mnemonic
     * @param displayedMnemonicIndex  the index
     */
    private static void setDisplayedMnemonicIndex(
        JLabel label,
        int displayedMnemonicIndex) {
        Integer index = new Integer(displayedMnemonicIndex);
        if (IS_BEFORE_14) {
            label.putClientProperty("displayedMnemonicIndex", index);
            return;
        }
        try {
            Method method =
                AbstractButton
                    .class
                    .getMethod("setDisplayedMnemonicIndex", new Class[] {
            });
            method.invoke(label, new Integer[] { index });
            return;
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        }
    }
    
    /**
     * Checks and answers whether this is a J2RE 1.2 or 1.3.
     */
    private static boolean isBefore14() {
        String version = System.getProperty("java.version");
        return version.startsWith("1.2") || version.startsWith("1.3");
    }

    // A label that uses the TitleBorder font and color
    private static class TitleLabel extends JLabel {
        
        private TitleLabel() {
        }
        
        private TitleLabel(String text) {
            super(text);
        }
        
        public void updateUI() {
            super.updateUI();
            Color foreground =
                UIManager.getColor("TitledBorder.titleColor");
            if (foreground != null)
                setForeground(foreground);
            setFont(UIManager.getFont("TitledBorder.font"));
        }
        
    };

}