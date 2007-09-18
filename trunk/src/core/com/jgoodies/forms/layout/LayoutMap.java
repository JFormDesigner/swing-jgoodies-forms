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
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.UIManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.util.LayoutStyle;


/**
 * Provides a hierarchical variable expansion useful to improve layout
 * consistency, style guide compliance, and layout readability.<p>
 *
 * A LayoutMap maps variable names to layout expression Strings. The FormLayout,
 * ColumnSpec, and RowSpec parsers expand variables before an encoded layout
 * specification is parsed and converted into ColumnSpec and RowSpec values.
 * Variables start with the '$' character. The variable name can be wrapped
 * by braces ('{' and '}'). For example, you can write:
 * <code>new FormLayout("pref, $lcg, pref",)</code> or
 * <code>new FormLayout("pref, ${lcg}, pref")</code>.<p>
 *
 * LayoutMaps build a chain; each LayoutMap has an optional parent map.
 * The root is defined by {@link LayoutMap#getRoot()}. Application-wide
 * variables should be defined in the root LayoutMap. If you want to override
 * application-wide variables locally, obtain a LayoutMap using {@code
 * new LayoutMap()}, configure it, and provide it as argument to the
 * FormLayout, ColumnSpec, and RowSpec constructors/factory methods.<p>
 *
 * By default the root LayoutMap provides the following associations:
 * <table border="1">
 * <tr><td><b>Variable Name</b><td><b>Abbreviations</b></td><td><b>Value</b></td></tr>
 * <tr><td>label-component-gap</td><td>lcg, lcgap</td><td>gap between a label and its component</td></tr>
 * <tr><td>related-gap</td><td>rg, rgap</td><td>gap between two related components</td></tr>
 * <tr><td>unrelated-gap</td><td>ug, ugap</td><td>gap between two unrelated components</td></tr>
 * <tr><td>line-gap</td><td>lg, lgap</td><td>gap between two lines</td></tr>
 * <tr><td>narrow-line-gap</td><td>nlg, nlgap</td><td>narrow gap between two lines</td></tr>
 * <tr><td>paragraph</td><td>pg, pgap</td><td>gap between two paragraphs/sections</td></tr>
 * </table><p>
 *
 * <strong>Examples:</strong>
 * <pre>
 * // Predefined variables
 * new FormLayout(
 *     "pref, $lcg, pref, $rg, pref",
 *     "p, $lg, p, $lg, p");
 *
 * // Custom variables
 * LayoutMap.getDefault().columnPut("half", "39dlu");
 * LayoutMap.getDefault().columnPut("full", "80dlu");
 * LayoutMap.getDefault().rowPut("table", "fill:0:grow");
 * LayoutMap.getDefault().rowPut("table50", "fill:50dlu:grow");
 * new FormLayout(
 *     "pref, $lcg, $half, 2dlu, $half",
 *     "p, $lcg, $table50");
 * new FormLayout(
 *     "pref, $lcg, $full",
 *     "p, $lcg, $table50");
 *
 * // Nested variables
 * LayoutMap.getDefault().columnPut("c-gap-c", "$half, 2dlu, $half");
 * new FormLayout(
 *     "pref, $lcg, ${c-gap-c}", // -> "pref, $lcg, $half, 2dlu, $half",
 *     "p, $lcg, $table");
 * </pre>
 *
 * LayoutMap holds two internal Maps that associate key Strings with expression
 * Strings for the columns and rows respectively. Null values are not allowed.<p>
 *
 * <strong>Tips:</strong><ul>
 * <li>You should carefully override predefined variables,
 *     because variable users may expect that these don't change.
 * <li>If you modify a predefined variable, take care that you override
 *     the variable definition, not an alias. For example "related-gap" may
 *     be changed, "rgap" should not be changed as it points to "$related-gap".
 * <li>Set custom variables in the root LayoutMap.
 * <li>Avoid aliases for custom variables.
 * </ul>
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.8 $
 *
 * @see     FormLayout
 * @see     ColumnSpec
 * @see     RowSpec
 *
 * @since 1.2
 */
public final class LayoutMap {

    /**
     * Marks a layout variable; used by the Forms parsers.
     */
    static final char VARIABLE_PREFIX_CHAR = '$';


    /**
     * The key used to look up the default LayoutMap from the UIManager.
     *
     * @see #setRoot(LayoutMap)
     */
    private static final String LAYOUT_MAP_KEY = "JGoodiesFormsDefaultLayoutMap";


    /**
     * Refers to the parent map that is used to look up values
     * if this map contains no association for a given key.
     * The parent maps can build chains.
     */
    private final LayoutMap parent;

    /**
     * Holds the raw associations from variable names to expressions.
     * The expression may contain variables that are not expanded.
     */
    private final Map/*<String, String>*/ columnMap;

    /**
     * Holds the cached associations from variable names to expressions.
     * The expression are fully expanded and contain no variables.
     */
    private final Map/*<String, String>*/ columnMapCache;

    /**
     * Holds the raw associations from variable names to expressions.
     * The expression may contain variables that are not expanded.
     */
    private final Map/*<String, String>   */ rowMap;

    /**
     * Holds the cached associations from variable names to expressions.
     * The expression are fully expanded and contain no variables.
     */
    private final Map/*<String, String>*/ rowMapCache;


    // Instance Creation ******************************************************

    /**
     * Constructs a LayoutMap that has the root LayoutMap as parent.
     */
    public LayoutMap() {
        this(getRoot());
    }


    /**
     * Constructs a LayoutMap with the given optional parent.
     *
     * @param parent   the parent LayoutMap, may be {@code null}
     */
    public LayoutMap(LayoutMap parent) {
        this.parent = parent;
        columnMap = new HashMap/*<String, String>*/();
        rowMap    = new HashMap/*<String, String>*/();
        columnMapCache = new HashMap/*<String, String>*/();
        rowMapCache    = new HashMap/*<String, String>*/();
    }


    // Default ****************************************************************

    /**
     * Lazily initializes and returns the LayoutMap that is used
     * for variable expansion, if no custom LayoutMap is provided.<p>
     *
     * The root LayoutMap is stored in the UIManager that in turn uses
     * an AppContext to store the values. This way applets in different
     * contexts uses different defaults.
     *
     * @return the LayoutMap that is used, if no custom LayoutMap is provided
     */
    public static LayoutMap getRoot() {
        LayoutMap root = (LayoutMap) UIManager.get(LAYOUT_MAP_KEY);
        if (root == null) {
            root = createRoot();
            setRoot(root);
        }
        return root;
    }


    /**
     * Sets the given LayoutMap as new root. The root LayoutMap is used as
     * default for variable expansion, if no custom LayoutMap is provided.<p>
     *
     * Custom variables can be set in the root LayoutMap, or in child maps
     * that are provided as argument for the FormLayout, ColumnSpec, and
     * RowSpec constructors/factory methods.<p>
     *
     * The root LayoutMap is stored using an AppContext; hence applets
     * in different contexts uses different defaults.
     *
     * @param root   the LayoutMap to become the new root
     */
    private static void setRoot(LayoutMap root) {
        UIManager.put(LAYOUT_MAP_KEY, root);
    }


    // Column Mapping *********************************************************

    /**
     * Returns {@code true} if this map or a parent map - if any - contains
     * a ColumnSpec mapping for the specified key.
     *
     * @param key key whose presence in this LayoutMap chain is to be tested.
     * @return {@code true} if this map contains a column mapping
     *         for the specified key.
     *
     * @throws NullPointerException if the key is {@code null}.
     *
     * @see Map#containsKey(Object)
     */
    public boolean columnContainsKey(String key) {
        ensureValidKey(key);
        return  (columnMap.containsKey(key))
            || ((parent != null) && (parent.columnContainsKey(key)));
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
    public String columnGet(String key) {
        ensureValidKey(key);
        String cachedValue = (String) columnMapCache.get(key);
        if (cachedValue != null) {
            return cachedValue;
        }
        String value = (String) columnMap.get(key);
        if ((value == null) && (parent != null)) {
            value = parent.columnGet(key);
        }
        if (value == null) {
            return null;
        }
        String expandedString = expand(value, true);
        columnMapCache.put(key, expandedString);
        return expandedString;
    }


    /**
     * Associates the specified column String with the specified key
     * in this map.
     * If the map previously contained a mapping for this key, the old value
     * is replaced by the specified value. The value set in this map
     * overrides an association - if any - in the chain of parent LayoutMaps.<p>
     *
     * The {@code value} must not be {@code null}. To remove
     * an association from this map use {@link #columnRemove(String)}.
     *
     * @param key key with which the specified value is to be associated.
     * @param value column expression value to be associated with the specified key.
     * @return previous String associated with specified key,
     *         or {@code null} if there was no mapping for key.
     *
     * @throws NullPointerException if the {@code key} or {@code value}
     *         is {@code null}.
     *
     * @see Map#put(Object, Object)
     */
    public String columnPut(String key, String value) {
        ensureValidKey(key);
        if (value == null) {
            throw new NullPointerException("The column expression value must not be null.");
        }
        columnMapCache.clear();
        return (String) columnMap.put(
                key.toLowerCase(Locale.ENGLISH),
                value.toLowerCase(Locale.ENGLISH));
    }


    public String columnPut(String key, ColumnSpec value) {
        if (value == null) {
            throw new NullPointerException("The column spec value must not be null.");
        }
        return columnPut(key, value.encode());
    }


    public String columnPut(String key, Size value) {
        if (value == null) {
            throw new NullPointerException("The column size value must not be null.");
        }
        return columnPut(key, value.encode());
    }


    /**
     * Removes the column value mapping for this key from this map if it is
     * present.<p>
     *
     * Returns the value to which the map previously associated the key,
     * or {@code null} if the map contained no mapping for this key.
     * The map will not contain a String mapping for the specified key
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
    public String columnRemove(String key) {
        ensureValidKey(key);
        return (String) columnMap.remove(key);
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
    public boolean rowContainsKey(String key) {
        ensureValidKey(key);
        return  (rowMap.containsKey(key))
            || ((parent != null) && (parent.rowContainsKey(key)));
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
    public String rowGet(String key) {
        ensureValidKey(key);
        String cachedValue = (String) rowMapCache.get(key);
        if (cachedValue != null) {
            return cachedValue;
        }
        String value = (String) rowMap.get(key);
        if ((value == null) && (parent != null)) {
            value = parent.rowGet(key);
        }
        if (value == null) {
            return null;
        }
        String expandedString = expand(value, false);
        rowMapCache.put(key, expandedString);
        return expandedString;
    }


    public String rowPut(String key, String value) {
        ensureValidKey(key);
        if (value == null) {
            throw new NullPointerException("The row expression value must not be null.");
        }
        rowMapCache.clear();
        return (String) rowMap.put(
                key.toLowerCase(Locale.ENGLISH),
                value.toLowerCase(Locale.ENGLISH));
    }


    /**
     * Associates the specified ColumnSpec with the specified key in this map.
     * If the map previously contained a mapping for this key, the old value
     * is replaced by the specified value. The RowSpec set in this map
     * override an association - if any - in the chain of parent LayoutMaps.<p>
     *
     * The RowSpec must not be {@code null}. To remove an association
     * from this map use {@link #rowRemove(String)}.
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
    public String rowPut(String key, RowSpec value) {
        if (value == null) {
            throw new NullPointerException("The row spec value must not be null.");
        }
        return rowPut(key, value.encode());
    }


    public String rowPut(String key, Size value) {
        if (value == null) {
            throw new NullPointerException("The row size value must not be null.");
        }
        return rowPut(key, value.encode());
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
    public RowSpec rowRemove(String key) {
        ensureValidKey(key);
        return (RowSpec) rowMap.remove(key);
    }
    
    
    // Overriding Object Behavior *********************************************

    /**
     * Returns a string representation of this LayoutMap that lists
     * the column and row associations.
     * 
     * @return a string representation 
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer("LayoutMap");
        buffer.append("\n  Column associations:");
        for (Iterator iterator = columnMap.entrySet().iterator(); iterator.hasNext();) {
            Entry name = (Entry) iterator.next();
            buffer.append("\n    ");
            buffer.append(name.getKey());
            buffer.append("->");
            buffer.append(name.getValue());
        }
        buffer.append("\n  Row associations:");
        for (Iterator iterator = rowMap.entrySet().iterator(); iterator.hasNext();) {
            Entry name = (Entry) iterator.next();
            buffer.append("\n    ");
            buffer.append(name.getKey());
            buffer.append("->");
            buffer.append(name.getValue());
        }
        return buffer.toString();
    }


    // String Expansion *******************************************************

    String expand(String expression, boolean horizontal) {
        int cursor = 0;
        int start = expression.indexOf(LayoutMap.VARIABLE_PREFIX_CHAR, cursor);
        if (start == -1) { // No variables
            return expression;
        }
        StringBuffer buffer = new StringBuffer();
        do {
            buffer.append(expression.substring(cursor, start));
            String variableName = nextVariableName(expression, start);
            buffer.append(expansion(variableName, horizontal));
            cursor = start + variableName.length() + 1;
            start = expression.indexOf(LayoutMap.VARIABLE_PREFIX_CHAR, cursor);
        } while (start != -1);
        buffer.append(expression.substring(cursor));
        return buffer.toString();
    }


    private String nextVariableName(String expression, int start) {
        int length = expression.length();
        if (length <= start) {
            FormSpecParser.fail(expression, start, "Missing variable name after variable char '$'.");
        }
        if (expression.charAt(start + 1) == '{') {
            int end = expression.indexOf('}', start + 1);
            if (end == -1) {
                FormSpecParser.fail(expression, start, "Missing closing brace '}' for variable.");
            }
            return expression.substring(start + 1, end + 1);
        }
        int end = start + 1;
        while ((end < length)
            && Character.isUnicodeIdentifierPart(expression.charAt(end))) {
            end++;
        }
        return expression.substring(start+1, end);
    }


    private String expansion(String variableName, boolean horizontal) {
        String caseSensitiveKey = variableName.charAt(0) == '{'
            ? variableName.substring(1, variableName.length() - 1)
            : variableName;
        String key = caseSensitiveKey.toLowerCase(Locale.ENGLISH);
        String expansion = horizontal ? columnGet(key) : rowGet(key);
        if (expansion == null) {
            String orientation = horizontal ? "column" : "row";
            throw new IllegalArgumentException("Unknown " + orientation + " layout variable \"" + key + "\"");
        }
        return expansion;
    }


    // Helper Code ************************************************************

    private void ensureValidKey(String key) {
        if (key == null) {
            throw new NullPointerException("The key must not be null.");
        }
    }


    private static LayoutMap createRoot() {
        LayoutMap root = new LayoutMap(null);

        // Column variables
        root.columnPut(
                "label-component-gap",
                new String[]{"lcg", "lcgap"},
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC);
        root.columnPut(
                "related-gap",
                new String[]{"r", "rgap"},
                FormFactory.RELATED_GAP_COLSPEC);
        root.columnPut(
                "unrelated-gap",
                new String[]{"ug", "ugap"},
                FormFactory.UNRELATED_GAP_COLSPEC);
        root.columnPut(
                "button",
                new String[]{"b"},
                FormFactory.BUTTON_COLSPEC);
        root.columnPut(
                "growbutton",
                new String[]{"gb"},
                FormFactory.GROWING_BUTTON_COLSPEC);
        root.columnPut(
                "dialog-margin",
                new String[]{"dm", "dmargin"},
                ColumnSpec.createGap(LayoutStyle.getCurrent().getDialogMarginX()));
        root.columnPut(
                "tabbed-dialog-margin",
                new String[]{"tdm", "tdmargin"},
                ColumnSpec.createGap(LayoutStyle.getCurrent().getTabbedDialogMarginX()));
        root.columnPut(
                "glue",
                FormFactory.GLUE_COLSPEC.toShortString());

        // Row variables
        root.rowPut(
                "related",
                new String[]{"rg", "rgap"},
                FormFactory.RELATED_GAP_ROWSPEC);
        root.rowPut(
                "unrelated",
                new String[]{"ug", "ugap"},
                FormFactory.UNRELATED_GAP_ROWSPEC);
        root.rowPut(
                "narrow-line-gap",
                new String[]{"nlg", "nlgap"},
                FormFactory.NARROW_LINE_GAP_ROWSPEC);
        root.rowPut(
                "line-gap",
                new String[]{"lg", "lgap"},
                FormFactory.LINE_GAP_ROWSPEC);
        root.rowPut(
                "paragraph-gap",
                new String[]{"pg", "pgap"},
                FormFactory.PARAGRAPH_GAP_ROWSPEC);
        root.rowPut(
                "dialog-margin",
                new String[]{"dm", "dmargin"},
                RowSpec.createGap(LayoutStyle.getCurrent().getDialogMarginY()));
        root.rowPut(
                "tabbed-dialog-margin",
                new String[]{"tdm", "tdmargin"},
                RowSpec.createGap(LayoutStyle.getCurrent().getTabbedDialogMarginY()));
        root.rowPut(
                "button",
                new String[]{"b"},
                FormFactory.BUTTON_ROWSPEC);
        root.rowPut(
                "glue",
                FormFactory.GLUE_ROWSPEC);

        return root;
    }


    private void columnPut(String key, String[] aliases, ColumnSpec value) {
        columnPut(key, value);
        for (int i=0; i < aliases.length; i++) {
            columnPut(aliases[i], "${" + key + '}');
        }
    }


    private void rowPut(String key, String[] aliases, RowSpec value) {
        rowPut(key, value);
        for (int i=0; i < aliases.length; i++) {
            rowPut(aliases[i], "${" + key + '}');
        }
    }


}