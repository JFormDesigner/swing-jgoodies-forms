/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */

package com.jgoodies.forms.debug;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JLabel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;


/**
 * Provides static methods that help you understand and fix layout problems 
 * when using the {@link FormLayout}. Therefore it can dump information 
 * about the layout grid, layout groups and cell constraints to the console.
 *
 * @author	Karsten Lentzsch
 */
public final class FormDebugUtils {
    
    // Console Dump *********************************************************
    
    /**
     * Dumps all layout state to the console: column and row specifications,
     * column and row groups, grid bounds and cell constraints.
     */
    public static void dumpAll(Container container) {
        if (!(container.getLayout() instanceof FormLayout)) {
            System.out.println("The container's layout is not a FormLayout.");
            return;
        }
        FormLayout layout = (FormLayout) container.getLayout();
        dumpColumnSpecs(layout);
        dumpRowSpecs(layout);
        System.out.println();
        dumpColumnGroups(layout);
        dumpRowGroups(layout);
        System.out.println();
        dumpConstraints(container);
        dumpGridBounds(container);        
    }
    
    /**
     * Dumps the layout's column specifications to the console.
     * 
     * @param layout   the <code>FormLayout</code> to inspect
     */
    public static void dumpColumnSpecs(FormLayout layout) {
        System.out.print("COLUMN SPECS:");
        for (int col = 1; col <= layout.getColumnCount(); col++) {
            ColumnSpec colSpec = layout.getColumnSpec(col);
            System.out.print(colSpec.toShortString());
            if (col < layout.getColumnCount())
                System.out.print(", ");
        }
        System.out.println();
    }


    /**
     * Dumps the layout's row specifications to the console.
     * 
     * @param layout   the <code>FormLayout</code> to inspect
     */
    public static void dumpRowSpecs(FormLayout layout) {
        System.out.print("ROW SPECS:   ");
        for (int row = 1; row <= layout.getRowCount(); row++) {
            RowSpec rowSpec = layout.getRowSpec(row);
            System.out.print(rowSpec.toShortString());
            if (row < layout.getRowCount())
                System.out.print(", ");
        }
        System.out.println();
    }


    /**
     * Dumps the layout's column groups to the console.
     * 
     * @param layout   the <code>FormLayout</code> to inspect
     */
    public static void dumpColumnGroups(FormLayout layout) {
        dumpGroups("COLUMN GROUPS: ", layout.getColumnGroups());
    }


    /**
     * Dumps the layout's row groups to the console.
     * 
     * @param layout   the <code>FormLayout</code> to inspect
     */
    public static void dumpRowGroups(FormLayout layout) {
        dumpGroups("ROW GROUPS:    ", layout.getRowGroups());
    }


    /**
     * Dumps the container's grid info to the console if and only
     * if the container's layout is a <code>FormLayout</code>.
     * 
     * @param container   the container to inspect
     * @throws IllegalArgumentException   if the layout is not FormLayout
     */
    public static void dumpGridBounds(Container container) {
        System.out.println("GRID BOUNDS");
        dumpGridBounds(getLayoutInfo(container));
    }


    /**
     * Dumps the grid layout info to the console.
     * 
     * @param layoutInfo   provides the column and row origins
     */
    public static void dumpGridBounds(FormLayout.LayoutInfo layoutInfo) {
        System.out.print("COLUMN ORIGINS: ");
        for (int col = 0; col < layoutInfo.columnOrigins.length; col++) {
            System.out.print(layoutInfo.columnOrigins[col] + " ");
        }
        System.out.println();

        System.out.print("ROW ORIGINS:    ");
        for (int row = 0; row < layoutInfo.rowOrigins.length; row++) {
            System.out.print(layoutInfo.rowOrigins[row] + " ");
        }
        System.out.println();
    }


    /**
     * Dumps the component constraints to the console.
     * 
     * @param container   the layout container to inspect
     */
    public static void dumpConstraints(Container container) {
        System.out.println("COMPONENT CONSTRAINTS");
        if (!(container.getLayout() instanceof FormLayout)) {
            System.out.println("The container's layout is not a FormLayout.");
            return;
        }
        FormLayout layout = (FormLayout) container.getLayout();
        int childCount = container.getComponentCount();
        for (int i = 0; i < childCount; i++) {
            Component child = container.getComponent(i);
            CellConstraints cc = layout.getConstraints(child);
            String ccString = cc == null
                ? "no constraints"
                : cc.toShortString(layout);
            System.out.print(ccString);        
            System.out.print("; ");        
            String childType = child.getClass().getName();
            System.out.print(childType);
            if (child instanceof JLabel) {
                JLabel label = (JLabel) child;
                System.out.print("      \"" + label.getText() + "\"");
            }
            if (child.getName() != null) {
                System.out.print("; name=");
                System.out.print(child.getName());
            }
            System.out.println();
        }
        System.out.println();
    }


    // Helper Code **********************************************************

    /**
     * Dumps the given groups to the console.
     * 
     * @param title       a string title for the dump
     * @param allGroups   a two-dimensional array with all groups
     */
    private static void dumpGroups(String title, int[][] allGroups) {
        System.out.print(title + " {");
        for (int group = 0; group < allGroups.length; group++) {
            int[] groupIndices = allGroups[group];
            System.out.print(" {");
            for (int i = 0; i < groupIndices.length; i++) {
                System.out.print(groupIndices[i]);
                if (i < groupIndices.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("} ");
            if (group < allGroups.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
    
    /**
     * Computes and answers the layout's grid origins.
     * 
     * @param container   the layout container to inspect
     * @return an object that comprises the cell origins and extents
     * @throws IllegalArgumentException   if the layout is not FormLayout
     */
    public static FormLayout.LayoutInfo getLayoutInfo(Container container) {
        if (!(container.getLayout() instanceof FormLayout)) {
            throw new IllegalArgumentException("The container must use an instance of FormLayout.");
        }
        FormLayout layout = (FormLayout) container.getLayout();
        return layout.getLayoutInfo(container);
    }

}