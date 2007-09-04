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


/**
 * Provides a hierarchical map from names to column and row specifications.
 * Useful to improve layout consistency and style guide compliance.<p>
 *
 * LayoutMap holds two Maps that associate Strings with ColumnSpecs and
 * RowSpec respectively. Null values are not allowed.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.1 $
 *
 * @see     FormLayout
 * @see     ColumnSpec
 * @see     RowSpec
 */
public final class LayoutMap {

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
        return  (rowMap.containsKey(key))
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


}
