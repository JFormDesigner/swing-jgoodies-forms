/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */

package com.jgoodies.forms.factories;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.util.LayoutStyle;

/**
 * A factory that creates instances of 
 * {@link com.jgoodies.forms.layout.FormLayout} for frequently used
 * form layouts. It makes form creation easier and more consistent.
 * <p>
 * <b>The <code>FormLayout</code> factory methods are work in progress 
 * and may change without notice.</b> If you want to use these methods, 
 * you may consider copying them into your codebase.
 * <p> 
 * The forms are described by major and minor columns. Major columns
 * consist of a leading label and a set of related components, for example: a
 * label plus textfield, or label plus textfield plus button. The component
 * part of a major column is divided into minor columns as shown in this
 * layout:
 * <pre>
 * <-    major column 1        ->  <-     major column 2       -> 
 * label1 textfield1a textfield1b  label2 textfield2a textfield2b
 * label3 textfield3a textfield3b  label4 textfield4
 * label5 textfield5               label6 textfield6
 * </pre>
 * <p>
 * Most forms use 1, 2, 3 or 4 major columns, which in turn are often split
 * into 1, 2, 3 or 4 minor columns.
 * 
 * @author	Karsten Lentzsch
 * @see	com.jgoodies.forms.layout.FormLayout
 * @see	ColumnSpec
 */
public final class FormFactory {


    // Frequently used Column Specifications ********************************

    public static final ColumnSpec MIN_COLSPEC = 
        new ColumnSpec("min").asUnmodifyable();   
                                    
    public static final ColumnSpec PREF_COLSPEC = 
        new ColumnSpec("pref").asUnmodifyable();   
                                    
    public static final ColumnSpec DEFAULT_COLSPEC = 
        new ColumnSpec("default").asUnmodifyable();   
                                    
    public static final ColumnSpec GLUE_COLSPEC = 
        new ColumnSpec("0px:grow").asUnmodifyable();                               

    /*
     * The following four constants use logical sizes that change with the
     * layout style. A future release will likely defined them using 
     * a class <code>LogicalSize</code> or <code>StyledSize</code>.
     */
    public static final ColumnSpec RELATED_GAP_COLSPEC =
        createGapColumnSpec(LayoutStyle.getCurrent().getRelatedComponentsPadX());
        
    public static final ColumnSpec UNRELATED_GAP_COLSPEC =
        createGapColumnSpec(LayoutStyle.getCurrent().getUnrelatedComponentsPadX()); 
        
    public static final ColumnSpec BUTTON_COLSPEC = 
        new ColumnSpec(Sizes.bounded(LayoutStyle.getCurrent().getDefaultButtonWidth(),
                                      null,
                                      Sizes.PREFERRED));
        
    public static final ColumnSpec GROWING_BUTTON_COLSPEC = 
        new ColumnSpec("max(50dlu;pref):grow").asUnmodifyable();
        
    
    // Frequently used Row Specifications ***********************************
    
    public static final RowSpec MIN_ROWSPEC = 
        new RowSpec("min").asUnmodifyable();   
                                    
    public static final RowSpec PREF_ROWSPEC = 
        new RowSpec("pref").asUnmodifyable();   
                                    
    public static final RowSpec DEFAULT_ROWSPEC = 
        new RowSpec("default").asUnmodifyable();   
                                    
    public static final RowSpec GLUE_ROWSPEC = 
        new RowSpec("0px:grow").asUnmodifyable();                               

    /*
     * The following five constants use logical sizes that change with the
     * layout style. A future release will likely defined them using a class
     * <code>LogicalSize</code> or <code>StyledSize</code>.
     */
    public static final RowSpec RELATED_GAP_ROWSPEC =
        createGapRowSpec(LayoutStyle.getCurrent().getRelatedComponentsPadY());
        
    public static final RowSpec UNRELATED_GAP_ROWSPEC =
        createGapRowSpec(LayoutStyle.getCurrent().getUnrelatedComponentsPadY());
        
    public static final RowSpec LINE_GAP_ROWSPEC =
        createGapRowSpec(LayoutStyle.getCurrent().getLinePad());

    public static final RowSpec NARROW_LINE_GAP_ROWSPEC =
        createGapRowSpec(LayoutStyle.getCurrent().getNarrowLinePad());

    public static final RowSpec PARAGRAPH_GAP_ROWSPEC =
        createGapRowSpec(LayoutStyle.getCurrent().getParagraphPad());
        
    
    
    // Factory Methods ******************************************************

    /**
     * Creates and answers an instance of <code>FormLayout</code>
     * to build forms with the specified number of major and minor columns.
     * <p>
     * The layout will use default values for all gaps.
     * <p>
     * <b>This method is work in progress and may be moved, removed, 
     * or change without notice.</b>
     *
     * @param majorColumns     the number of used major columns
     * @param minorColumns     the number of used minor columns
     * @param labelColumnSpec  specifies the label columns
     * @return a prepared <code>FormLayout</code>
     */
    public static FormLayout createColumnLayout(
            int majorColumns,
            int minorColumns,
            ColumnSpec labelColumnSpec) {
        return createColumnLayout(
            majorColumns,
            minorColumns,
            labelColumnSpec,
            Sizes.DLUX14,
            Sizes.DLUX2);
    }

    /**
     * Creates and answers an instance of <code>FormLayout</code>
     * to build forms with the given number of major columns. 
     * Major columns consists of a label and a component section, where each 
     * component section is divided into the given number of minor columns.
     * <p>
     * The layout will use the specified gaps to separate major columns, 
     * and the label and component section.
     * <p>
     * <b>This method is work in progress and may be moved, removed, 
     * or change without notice.</b>
     * 
     * @param majorColumns         the number of major columns
     * @param minorColumns         the number of minor columns
     * @param labelColumnSpec      specifies the label columns
     * @param indent               an optional <code>ConstantSize</code> 
     * that describes the width of the leading indent column
     * @param minorColumnGap       a <code>ConstantSize</code> that describes
     * the gap between minor columns
     * @return a prepared <code>FormLayout</code>
     */
    public static FormLayout createColumnLayout(
            int majorColumns,
            int minorColumns,
            ColumnSpec labelColumnSpec,
            ConstantSize indent,
            ConstantSize minorColumnGap) {
        return createColumnLayout(
            majorColumns, 
            minorColumns, 
            labelColumnSpec, 
            PREF_COLSPEC, 
            indent, 
            Sizes.DLUX14, 
            minorColumnGap);
    }
                
    /**
     * Creates and answers an instance of <code>FormLayout</code>
     * to build forms with the given number of major columns. 
     * Major columns consists of a label and a component section, where each 
     * component section is divided into the given number of minor columns.
     * <p>
     * The layout will use the specified gaps to separate major columns, 
     * minor columns, and the label and component section.
     * <p>
     * <b>This method is work in progress and may be moved, removed, 
     * or change without notice.</b>
     * 
     * @param majorColumns         the number of major columns
     * @param minorColumns         the number of minor columns
     * @param labelColumnSpec      specifies the label columns
     * @param componentColumnSpec  specifies the label columns
     * @param indent               an optional <code>ConstantSize</code> 
     * that describes the width of the leading indent column
     * @param majorColumnGap       a <code>ConstantSize</code> that describes
     * the gap between major columns
     * @param minorColumnGap       a <code>ConstantSize</code> that describes
     * the gap between minor columns
     * @return a prepared <code>FormLayout</code>
     */
    public static FormLayout createColumnLayout(
            int majorColumns,
            int minorColumns,
            ColumnSpec labelColumnSpec,
            ColumnSpec componentColumnSpec,
            ConstantSize indent,
            ConstantSize majorColumnGap,
            ConstantSize minorColumnGap) {
                
        ColumnSpec majorGapColSpec = createGapColumnSpec(majorColumnGap);
        ColumnSpec minorGapColSpec = createGapColumnSpec(minorColumnGap);
        FormLayout layout = new FormLayout("", "");

        // Add the optional leading indent.
        if (indent != null) { 
            layout.appendColumn(createGapColumnSpec(indent));
        }
        for (int i = 0; i < majorColumns; i++) {
            // Add the optional label column with gap.
            if (labelColumnSpec != null) {
                layout.appendColumn(labelColumnSpec);
                layout.appendColumn(RELATED_GAP_COLSPEC);
            }
            // Add the minor columns with separating gaps.
            for (int j = 0; j < minorColumns; j++) {
                layout.appendColumn(componentColumnSpec);
                layout.addGroupedColumn(layout.getColumnCount());
                if (j < minorColumns - 1) {
                    layout.appendColumn(minorGapColSpec);
                }
            }
            // Add a gap between major columns.
            if (i < majorColumns - 1) { 
                layout.appendColumn(majorGapColSpec); 
            }
        }
        return layout;
    }

    
    // Helper Code **********************************************************
    
    /**
     * Creates and answers a {@link ColumnSpec} that represents a gap with the
     * specified {@link ConstantSize}.
     * 
     * @param gapSize	a <code>ConstantSize</code> that specifies the gap
     * @return an unmodifyable <code>ColumnSpec</code> that describes a horizontal gap
     */
    public static ColumnSpec createGapColumnSpec(ConstantSize gapSize) {
        ColumnSpec spec = new ColumnSpec(ColumnSpec.LEFT, gapSize, ColumnSpec.NO_GROW);
        return spec.asUnmodifyable();
    }

    /**
     * Creates and answers a {@link RowSpec} that represents a gap with the
     * specified {@link ConstantSize}.
     * 
     * @param gapSize   a <code>ConstantSize</code> that specifies the gap
     * @return an unmodifyable <code>RowSpec</code> that describes a vertical gap
     */
    public static RowSpec createGapRowSpec(ConstantSize gapSize) {
        RowSpec spec = new RowSpec(RowSpec.TOP, gapSize, RowSpec.NO_GROW);
        return spec.asUnmodifyable();
    }
    
    
}
