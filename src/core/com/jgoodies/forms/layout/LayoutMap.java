/*
 * Copyright (c) 2002-2007 JGoodies Karsten Lentzsch. All Rights Reserved.
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

package com.jgoodies.forms.layout;

import java.util.HashMap;
import java.util.Map;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.util.LayoutStyle;


/**
 * Provides a hierarchical map from names to column and row specifications.
 * Useful to improve layout consistency, style guide compliance, and
 * the layout readability.<p>
 *
 * Layout variables are used in the encoded column and row specifications.
 * They start with the '@' character, for example you can write:
 * {@code
 * new FormLayout("pref, @lcgap, pref, @rgap, pref",
 *                "p, @lgap, p, @myGap1");
 * }
 * In this example {@code @lcgap}, {@code @rgap}, {@code @lgap}
 * are default variables, and {@code @myGap1} is a custom variable.<p>
 *
 * The mapping from variable names to ColumnSpec/RowSpec values
 * is specified by a chain of LayoutMaps. {@link LayoutMap#getDefault()}
 * returns the default root LayoutMap. You can add and remove
 * associations (including the defaults) to the default LayoutMap;
 * these will be used for all FormLayouts. Or you can replace
 * the default LayoutMap using {@link LayoutMap#setDefault(LayoutMap)}.<p>
 *
 * LayoutMaps have an optional parent map; hence they build a chain.
 * The variable lookup starts with a given LayoutMap and continues
 * with the parent if the child has no association for a given key.
 * Associations in child maps shadow the associations in the parent
 * map, or more generally in the chain of parents. For example
 * {@code LayoutMap customMap = new LayoutMap(LayoutMap.getDefault());}
 * builds a custom map that has the default map as its parent.<p>
 *
 * The default LayoutMap provides the following default associations:
 * <table border="1">
 * <tr><td><b>Variable Name</b></td><td><b>Value</b></td></tr>
 * <tr><td>l lc lcgap</td><td>label component gap</td></tr>
 * <tr><td>r rgap rel related/td><td>related gap</td></tr>
 * <tr><td>u ugap unrel unrelated</td><td>unrelated gap</td></tr>
 * <tr><td>l lgap line</td><td>line gap</td></tr>
 * <tr><td>n ngap narrow</td><td>narrow line gap</td></tr>
 * <tr><td>p pgap paragraph</td><td>paragraph gap</td></tr>
 * </table>
 *
 * LayoutMap holds two Maps that associate Strings with ColumnSpecs and
 * RowSpec respectively. Null values are not allowed.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.4 $
 *
 * @see     FormLayout
 * @see     ColumnSpec
 * @see     RowSpec
 *
 * @since 1.2
 */
public final class LayoutMap {

    /**
     * The character used to mark a layout variable.
     * Used by the FormSpec string decoder.
     */
    static final char VARIABLE_PREFIX_CHAR = '@';


    /**
     * Holds the default LayoutMap that is used by the parsers
     * if no individual LayoutMap is provided.
     *
     * @see #setDefault(LayoutMap)
     */
    private static LayoutMap defaultMap;


    /**
     * Refers to the parent map that is used to look up values
     * if this map contains no association for a given key.
     * The parent maps can build chains.
     */
    private final LayoutMap parent;
    private final Map/*<String, ColumnSpec>*/ columnMap;
    private final Map/*<String, RowSpec>   */ rowMap;


    // Instance Creation ******************************************************

    /**
     * Constructs a LayoutMap with the given optional parent.
     *
     * @param parent   the parent LayoutMap, may be {@code null}
     */
    public LayoutMap(LayoutMap parent) {
        this.parent = parent;
        columnMap = new HashMap/*<String, ColumnSpec>*/();
        rowMap    = new HashMap/*<String, RowSpec>   */();
    }


    // Default ****************************************************************

    /**
     * Lazily initializes and returns the LayoutMap that is used by the
     * ColumnSpec and RowSpec parsers, if no custom LayoutMap is provided.
     *
     * @return the LayoutMap that is used to decode encoded column and row
     *    specification, if no custom LayoutMap is provided
     *
     * @since 1.2
     */
    public static LayoutMap getDefault() {
        if (defaultMap == null) {
            defaultMap = createDefault();
        }
        return defaultMap;
    }


    public static void setDefault(LayoutMap newDefault) {
        defaultMap = newDefault;
    }


    // Column Mapping *********************************************************

    /**
     * Returns {@code true} if this map or a parent map - if any - contains
     * a ColumnSpec mapping for the specified key.
     *
     * @param key key whose presence in this LayoutMap chain is to be tested.
     * @return {@code true} if this map contains a column spec mapping
     *         for the specified key.
     *
     * @throws NullPointerException if the key is {@code null}.
     *
     * @see Map#containsKey(Object)
     */
    public boolean containsColumnKey(String key) {
        ensureValidKey(key);
        return  (columnMap.containsKey(key))
            || ((parent != null) && (parent.containsColumnKey(key)));
    }


    /**
     * Looks up and returns the ColumnSpec associated with the given key.
     * First looks for an association in this LayoutMap. If there's no
     * association, the lookup continues with the parent map - if any.
     *
     * @param key key whose associated value is to be returned.
     * @return the column specification associated with the {@code key},
     *         or {@code null} if no LayoutMap in the parent chain
     *         contains an association.
     *
     * @throws NullPointerException  if {@code key} is {@code null}
     *
     * @see Map#get(Object)
     */
    public ColumnSpec getColumnSpec(String key) {
        ensureValidKey(key);
        ColumnSpec value = (ColumnSpec) columnMap.get(key);
        if (value != null) {
            return value;
        }
        return parent != null
            ? parent.getColumnSpec(key)
            : null;
    }


    /**
     * Associates the specified ColumnSpec with the specified key in this map.
     * If the map previously contained a mapping for this key, the old value
     * is replaced by the specified value. The ColumnSpec set in this map
     * override an association - if any - in the chain of parent LayoutMaps.<p>
     *
     * The ColumnSpec must not be {@code null}. To remove an association
     * from this map use {@link #removeColumnSpec(String)}.
     *
     * @param key key with which the specified value is to be associated.
     * @param value ColumnSpec to be associated with the specified key.
     * @return previous ColumnSpec associated with specified key,
     *         or {@code null} if there was no mapping for key.
     *
     * @throws NullPointerException if the {@code key} or {@code value}
     *         is {@code null}.
     *
     * @see Map#put(Object, Object)
     */
    public ColumnSpec putColumnSpec(String key, ColumnSpec value) {
        ensureValidKey(key);
        if (value == null) {
            throw new NullPointerException("The column spec value must not be null.");
        }
        return (ColumnSpec) columnMap.put(key, value);
    }


    /**
     * Associates a gap ColumnSpec with the given {@code gapWidth}
     * with the specified key in this map.
     * If the map previously contained a mapping for this key, the old value
     * is replaced by the specified value. The ColumnSpec set in this map
     * overrides an association - if any - in the chain of parent LayoutMaps.
     *
     * @param key key with which the specified value is to be associated.
     * @param gapWidth specifies the gap with.
     * @return previous ColumnSpec associated with specified key,
     *         or {@code null} if there was no mapping for key.
     *
     * @throws NullPointerException if the {@code key} or {@code gapSize}
     *         is {@code null}.
     *
     * @see #putColumnSpec(String, ColumnSpec)
     * @see ColumnSpec#createGap(ConstantSize)
     */
    public ColumnSpec putColumnGapSpec(String key, ConstantSize gapWidth) {
        return putColumnSpec(key, ColumnSpec.createGap(gapWidth));
    }


    /**
     * Removes the ColumnSpec mapping for this key from this map if it is
     * present.<p>
     *
     * Returns the value to which the map previously associated the key,
     * or {@code null} if the map contained no mapping for this key.
     * The map will not contain a ColumnSpec mapping for the specified key
     * once the call returns.
     *
     * @param key key whose mapping is to be removed from the map.
     * @return previous value associated with specified key, or {@code null}
     *         if there was no mapping for key.
     *
     * @throws NullPointerException if {@code key} is {@code null}.
     *
     * @see Map#remove(Object)
     */
    public ColumnSpec removeColumnSpec(String key) {
        ensureValidKey(key);
        return (ColumnSpec) columnMap.remove(key);
    }


    // Row Mapping ************************************************************

    /**
     * Returns {@code true} if this map or a parent map - if any - contains
     * a RowSpec mapping for the specified key.
     *
     * @param key key whose presence in this LayoutMap chain is to be tested.
     * @return {@code true} if this map contains a row spec mapping
     *         for the specified key.
     *
     * @throws NullPointerException if the key is {@code null}.
     *
     * @see Map#containsKey(Object)
     */
    public boolean containsRowKey(String key) {
        ensureValidKey(key);
        return  (rowMap.containsKey(key))
            || ((parent != null) && (parent.containsRowKey(key)));
    }


    /**
     * Looks up and returns the RowSpec associated with the given key.
     * First looks for an association in this LayoutMap. If there's no
     * association, the lookup continues with the parent map - if any.
     *
     * @param key key whose associated value is to be returned.
     * @return the row specification associated with the {@code key},
     *         or {@code null} if no LayoutMap in the parent chain
     *         contains an association.
     *
     * @throws NullPointerException  if {@code key} is {@code null}
     *
     * @see Map#get(Object)
     */
    public RowSpec getRowSpec(String key) {
        ensureValidKey(key);
        RowSpec value = (RowSpec) rowMap.get(key);
        if (value != null) {
            return value;
        }
        return parent != null
            ? parent.getRowSpec(key)
            : null;
    }


    /**
     * Associates the specified ColumnSpec with the specified key in this map.
     * If the map previously contained a mapping for this key, the old value
     * is replaced by the specified value. The RowSpec set in this map
     * override an association - if any - in the chain of parent LayoutMaps.<p>
     *
     * The RowSpec must not be {@code null}. To remove an association
     * from this map use {@link #removeColumnSpec(String)}.
     *
     * @param key key with which the specified value is to be associated.
     * @param value ColumnSpec to be associated with the specified key.
     * @return previous ColumnSpec associated with specified key,
     *         or {@code null} if there was no mapping for key.
     *
     * @throws NullPointerException if the {@code key} or {@code value}
     *         is {@code null}.
     *
     * @see Map#put(Object, Object)
     */
    public RowSpec putRowSpec(String key, RowSpec value) {
        ensureValidKey(key);
        if (value == null) {
            throw new NullPointerException("The row spec value must not be null.");
        }
        return (RowSpec) rowMap.put(key, value);
    }


    /**
     * Associates a gap RowSpec with the given {@code gapHeight}
     * with the specified key in this map.
     * If the map previously contained a mapping for this key, the old value
     * is replaced by the specified value. The ColumnSpec set in this map
     * overrides an association - if any - in the chain of parent LayoutMaps.
     *
     * @param key key with which the specified value is to be associated.
     * @param gapHeight specifies the gap height.
     * @return previous RowSpec associated with specified key,
     *         or {@code null} if there was no mapping for key.
     *
     * @throws NullPointerException if the {@code key} or {@code gapHeight}
     *         is {@code null}.
     *
     * @see #putRowSpec(String, RowSpec)
     * @see RowSpec#createGap(ConstantSize)
     */
    public RowSpec putRowGapSpec(String key, ConstantSize gapHeight) {
        return putRowSpec(key, RowSpec.createGap(gapHeight));
    }


    /**
     * Removes the RowSpec mapping for this key from this map if it is
     * present.<p>
     *
     * Returns the value to which the map previously associated the key,
     * or {@code null} if the map contained no mapping for this key.
     * The map will not contain a RowSpec mapping for the specified key
     * once the call returns.
     *
     * @param key key whose mapping is to be removed from the map.
     * @return previous value associated with specified key, or {@code null}
     *         if there was no mapping for key.
     *
     * @throws NullPointerException if {@code key} is {@code null}.
     *
     * @see Map#remove(Object)
     */
    public RowSpec removeRowSpec(String key) {
        ensureValidKey(key);
        return (RowSpec) rowMap.remove(key);
    }


    // Helper Code ************************************************************

    private void ensureValidKey(String key) {
        if (key == null) {
            throw new NullPointerException("The key must not be null.");
        }
    }


    private static LayoutMap createDefault() {
        LayoutMap map = new LayoutMap(null);

        // Column variables
        map.putColumnSpec(
                new String[]{"lc", "lcgap", "label-component-gap"},
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC);
        map.putColumnSpec(
                new String[]{"r", "rgap", "rel", "related"},
                FormFactory.RELATED_GAP_COLSPEC);
        map.putColumnSpec(
                new String[]{"u", "ugap", "unrel", "unrelated"},
                FormFactory.UNRELATED_GAP_COLSPEC);
        map.putColumnSpec(
                new String[]{"b", "button"},
                FormFactory.BUTTON_COLSPEC);
        map.putColumnSpec(
                new String[]{"gb", "growbutton"},
                FormFactory.GROWING_BUTTON_COLSPEC);
        map.putColumnSpec(
                new String[]{"dg", "dlggap", "dialog-gap"},
                ColumnSpec.createGap(LayoutStyle.getCurrent().getDialogMarginX()));
        map.putColumnSpec(
                new String[]{"tdg", "tbddlggap", "tabbed-dialog-gap"},
                ColumnSpec.createGap(LayoutStyle.getCurrent().getTabbedDialogMarginX()));
        map.putColumnSpec(
                "glue",
                FormFactory.GLUE_COLSPEC);

        // Row variables
        map.putRowSpec(
                new String[]{"r", "rgap", "rel", "related"},
                FormFactory.RELATED_GAP_ROWSPEC);
        map.putRowSpec(
                new String[]{"u", "ugap", "unrel", "unrelated"},
                FormFactory.UNRELATED_GAP_ROWSPEC);
        map.putRowSpec(
                new String[]{"n", "ngap", "narrow", "narrow_line"},
                FormFactory.NARROW_LINE_GAP_ROWSPEC);
        map.putRowSpec(
                new String[]{"l", "lgap", "line"},
                FormFactory.LINE_GAP_ROWSPEC);
        map.putRowSpec(
                new String[]{"p", "pgap", "para", "paragraph"},
                FormFactory.PARAGRAPH_GAP_ROWSPEC);
        map.putRowSpec(
                new String[]{"dg", "dlggap", "dialog-margin-gap"},
                RowSpec.createGap(LayoutStyle.getCurrent().getDialogMarginY()));
        map.putRowSpec(
                new String[]{"tdg", "tbddlggap", "tabbed-dialog-margin-gap"},
                RowSpec.createGap(LayoutStyle.getCurrent().getTabbedDialogMarginY()));
        map.putRowSpec(
                new String[]{"b", "but", "button"},
                FormFactory.BUTTON_ROWSPEC);
        map.putRowSpec(
                "glue",
                FormFactory.GLUE_ROWSPEC);

        return map;
    }


    private void putColumnSpec(String[] keys, ColumnSpec value) {
        for (int i=0; i < keys.length; i++) {
            putColumnSpec(keys[i], value);
        }
    }


    private void putRowSpec(String[] keys, RowSpec value) {
        for (int i=0; i < keys.length; i++) {
            putRowSpec(keys[i], value);
        }
    }


}
