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

package com.jgoodies.forms.debug;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;


/**
 * A panel that paints grid bounds if and only if the panel's layout manager 
 * is a {@link FormLayout}. In addition, you can choose to dump information 
 * about the layout grid, layout groups and cell constraints to the console.
 * <p>
 * This class is not intended to be extended. However, it is no longer
 * marked as <code>final</code> to allow users to subclass it for 
 * debugging purposes. In general it is recommended to use JPanel
 * instances, not extend them. You can see this implementation style
 * in the Forms tutorial classes. Only rarely there's a need to extend
 * JPanel; for example if you provide a custom behavior for 
 * <code>#paintComponent</code> or <code>#updateUI</code>.  
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.2 $
 * @see     FormDebugUtils
 */
public class FormDebugPanel extends JPanel {
    
    /** 
     * The default color used to paint the form's grid. 
     */
    private static final Color DEFAULT_GRID_COLOR = Color.red;


    /** 
     * Specifies if the grid info should be dumped to the console.
     */
    private boolean dumpGridInfo;
    
    
    /**
     * Specifies whether the column and row groups shall be dumped to the console. 
     */
    private boolean dumpGroups;


    /**
     * Specifies whether the form constraints shall be dumped to the console. 
     */
    private boolean dumpConstraints;


    /** 
     * Specifies if the grid should be painted in the background. 
     * Is off by default and so the grid is painted in the foreground.
     */
    private boolean paintInBackground;
    
    
    /**
     * Specifies if the container's diagonals should be painted.
     */
    private boolean paintDiagonals;


    /**
     * Specifies the color used to paint the grid.
     */
    private Color gridColor = DEFAULT_GRID_COLOR;

    
    // Instance Creation ****************************************************
    
    /**
     * Constructs a <code>FormDebugPanel</code>. 
     * All options are off by default.
     */
    public FormDebugPanel() {
        this(null);
    }

    /**
     * Constructs a <code>FormDebugPanel</code>. 
     * All options are off by default.
     */
    public FormDebugPanel(FormLayout layout) {
        this(layout, false, false, false, false, false);
    }

    /**
     * Constructs a <code>FormDebugPanel</code> for the given
     * <code>FormLayout</code> and the specified settings.
     */
    public FormDebugPanel(boolean dumpGridInfo,   
                           boolean dumpGroups,   
                           boolean dumpConstraints,
                           boolean paintInBackground, 
                           boolean paintDiagonals) {
        this(null, dumpGridInfo, dumpGroups, dumpConstraints, paintInBackground, paintDiagonals);
    }
    
    /**
     * Constructs a <code>FormDebugPanel</code> for the given
     * <code>FormLayout</code> and the specified settings.
     */
    public FormDebugPanel(FormLayout layout,
                           boolean dumpGridInfo,   
                           boolean dumpGroups,   
                           boolean dumpConstraints,
                           boolean paintInBackground, 
                           boolean paintDiagonals) {
        super(layout);
        setDumpGridInfo(dumpGridInfo);
        setDumpGroups(dumpGroups);
        setDumpConstraints(dumpConstraints);
        setPaintInBackground(paintInBackground);
        setPaintDiagonals(paintDiagonals);
        setGridColor(DEFAULT_GRID_COLOR);
    }

    // Accessors ************************************************************
    
    /**
     * Enables or disables the dump of the form's grid info.
     */
    public void setDumpGridInfo(boolean b) { 
        dumpGridInfo = b; 
    }

    /**
     * Enables or disables the dump of the form's groups.
     */
    public void setDumpGroups(boolean b) { 
        dumpGroups = b; 
    }

    /**
     * Enables or disables the dump of the component's form constraints.
     */
    public void setDumpConstraints(boolean b) { 
        dumpConstraints = b; 
    }

    /**
     * Specifies to paint in background or foreground.
     */
    public void setPaintInBackground(boolean b) { 
        paintInBackground = b; 
    }

    /**
     * Enables or disables to paint the panel's diagonals.
     */
    public void setPaintDiagonals(boolean b) { 
        paintDiagonals = b; 
    }

    /**
     * Sets the grid color.
     * 
     * @param color  the grid color
     */
    public void setGridColor(Color color) { 
        gridColor = color; 
    }


    // Painting *************************************************************

    /**
     * Paints the component.
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (paintInBackground) {
            paintGrid(g);
        }
    }


    /**
     * Paints the panel. If the panel's layout manager is a 
     * <code>FormLayout</code> it paints the form's grid lines.
     */
    public void paint(Graphics g) {
        super.paint(g);
        if (!paintInBackground) {
            paintGrid(g);
        }
    }
    
    
    /**
     * Paints the form's grid lines and diagonals.
     */
    private void paintGrid(Graphics g) {
        if (!(getLayout() instanceof FormLayout)) {
            return;
        }
        FormLayout layout = (FormLayout) getLayout();
        FormLayout.LayoutInfo layoutInfo = FormDebugUtils.getLayoutInfo(this);
        
        if (dumpGridInfo) {
            FormDebugUtils.dumpGridBounds(layoutInfo);
        }
        
        if (dumpGroups) {
            FormDebugUtils.dumpColumnGroups(layout);
            FormDebugUtils.dumpRowGroups(layout);
        }
        
        if (dumpConstraints) {
            FormDebugUtils.dumpConstraints(this);
        }
        
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