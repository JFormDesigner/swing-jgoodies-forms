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

package com.jgoodies.forms.debug;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;


/**
 * A panel that paints grid bounds if and only if the panel's layout manager 
 * is a {@link FormLayout}. You can tweak the debug paint process by setting
 * a custom grid color, painting optional diagonals and painting the grid
 * in the background.<p>
 * 
 * This class is not intended to be extended. However, it is not
 * marked as <code>final</code> to allow users to subclass it for 
 * debugging purposes. In general it is recommended to <em>use</em> JPanel
 * instances, not <em>extend</em> them. You can see this implementation style
 * in the Forms tutorial classes. Rarely there's a need to extend JPanel; 
 * for example if you provide a custom behavior for 
 * <code>#paintComponent</code> or <code>#updateUI</code>.  
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.5 $
 * 
 * @see     FormDebugUtils
 */
public class FormDebugPanel extends JPanel {
    
    /** 
     * The default color used to paint the form's debug grid. 
     */
    private static final Color DEFAULT_GRID_COLOR = Color.red;


    /** 
     * Specifies whether the grid shall be painted in the background. 
     * Is off by default and so the grid is painted in the foreground.
     */
    private boolean paintInBackground;
    
    
    /**
     * Specifies whether the container's diagonals should be painted.
     */
    private boolean paintDiagonals;


    /**
     * Holds the color used to paint the debug grid.
     */
    private Color gridColor = DEFAULT_GRID_COLOR;

    
    // Instance Creation ****************************************************
    
    /**
     * Constructs a <code>FormDebugPanel</code> with all options turned off. 
     */
    public FormDebugPanel() {
        this(null);
    }
    

    /**
     * Constructs a <code>FormDebugPanel</code> on the given 
     * <code>FormLayout</code> instance with all options turned off.
     * 
     * @param layout  the panel's FormLayout instance 
     */
    public FormDebugPanel(FormLayout layout) {
        this(layout, false, false);
    }


    /**
     * Constructs a <code>FormDebugPanel</code> on the given
     * <code>FormLayout</code> using the specified settings that are
     * otherwise turned off.
     * 
     * @param paintInBackground true to paint grid lines in the background
     * @param paintDiagonals    true to paint diagonals, false to not paint them 
     */
    public FormDebugPanel(boolean paintInBackground, 
                           boolean paintDiagonals) {
        this(null, paintInBackground, paintDiagonals);
    }
    

    /**
     * Constructs a <code>FormDebugPanel</code> on the given
     * <code>FormLayout</code> using the specified settings that are
     * otherwise turned off.
     * 
     * @param layout  the panel's FormLayout instance
     * @param paintInBackground true to paint grid lines in the background
     * @param paintDiagonals    true to paint diagonals, false to not paint them 
     */
    public FormDebugPanel(FormLayout layout,
                           boolean paintInBackground, 
                           boolean paintDiagonals) {
        super(layout);
        setPaintInBackground(paintInBackground);
        setPaintDiagonals(paintDiagonals);
        setGridColor(DEFAULT_GRID_COLOR);
    }
    

    // Accessors ************************************************************
    
    /**
     * Specifies to paint in background or foreground.
     * 
     * @param b    true to paint in the background, false for the foreground
     */
    public void setPaintInBackground(boolean b) { 
        paintInBackground = b; 
    }

    /**
     * Enables or disables to paint the panel's diagonals.
     * 
     * @param b    true to paint diagonals, false to not paint them
     */
    public void setPaintDiagonals(boolean b) { 
        paintDiagonals = b; 
    }

    /**
     * Sets the debug grid's color.
     * 
     * @param color  the color used to paint the debug grid
     */
    public void setGridColor(Color color) { 
        gridColor = color; 
    }


    // Painting *************************************************************

    /**
     * Paints the component and - if background painting is enabled - the grid
     * 
     * @param g   the Graphics object to paint on 
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (paintInBackground) {
            paintGrid(g);
        }
    }


    /**
     * Paints the panel. If the panel's layout manager is a 
     * <code>FormLayout</code> it paints the form's grid lines.
     * 
     * @param g   the Graphics object to paint on 
     */
    public void paint(Graphics g) {
        super.paint(g);
        if (!paintInBackground) {
            paintGrid(g);
        }
    }
    
    
    /**
     * Paints the form's grid lines and diagonals.
     * 
     * @param g    the Graphics object used to paint
     */
    private void paintGrid(Graphics g) {
        if (!(getLayout() instanceof FormLayout)) {
            return;
        }
        FormLayout.LayoutInfo layoutInfo = FormDebugUtils.getLayoutInfo(this);
        int left   = layoutInfo.getX();
        int top    = layoutInfo.getY();
        int width  = layoutInfo.getWidth();
        int height = layoutInfo.getHeight();

        g.setColor(gridColor);
        // Paint the column bounds.
        for (int col = 0; col < layoutInfo.columnOrigins.length; col++) {
            g.fillRect(layoutInfo.columnOrigins[col], top, 1, height);
        }

        // Paint the row bounds.
        for (int row = 0; row < layoutInfo.rowOrigins.length; row++) {
            g.fillRect(left, layoutInfo.rowOrigins[row], width, 1);
        }
        
        if (paintDiagonals) {
            g.drawLine(left, top,          left + width, top + height);
            g.drawLine(left, top + height, left + width, top);
        }
    }
    
    
}