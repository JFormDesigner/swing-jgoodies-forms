/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.factories;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * An interface that defines the factory methods as used 
 * by the {@link com.jgoodies.forms.builder.PanelBuilder}.
 *
 * @author Karsten Lentzsch
 * @see    DefaultComponentFactory
 * @see    com.jgoodies.forms.builder.PanelBuilder
 */

public interface ComponentFactory {
    
    /**
     * Creates and answers a label with an optional mnemonic.
     * 
     * @param textWithMnemonic  the label's text - may contain a mnemonic 
     * @return an label with optional mnemonic
     */
    public JLabel createLabel(String textWithMnemonic);
  
    
    /**
     * Creates and answers a label that uses the foreground color
     * and font of a <code>TitledBorder</code>.
     * 
     * @param textWithMnemonic  the title's text - may contain a mnemonic
     * @return an emphasized title label
     */
    public JLabel createTitle(String textWithMnemonic);
    

    /**
     * Creates and answers a label with separator on the left hand side. 
     * Useful to separate paragraphs in a panel. This is often a better choice
     * than a <code>TitledBorder</code>.
     * 
     * @param text        the title's text
     * @param alignment   text alignment: left, center, right 
     * @return a title label with separator on the side
     */
    public JComponent createSeparator(String text, int alignment);
    
    
}