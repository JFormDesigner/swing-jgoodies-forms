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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses encoded column and row specifications.
 * Returns ColumnSpec or RowSpec arrays if successful,
 * and aims to provide useful information in case of a syntax error.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.1 $
 *
 * @see     ColumnSpec
 * @see     RowSpec
 */
public final class FormSpecParser {

    // Parser Patterns ******************************************************

    private static final Pattern EXPANSION_PREFIX_PATTERN =
        Pattern.compile("\\d+[x\\*]\\(");


    // Instance Fields ********************************************************

    private final String source;
    private final LayoutMap layoutMap;
    private final boolean horizontal;


    // Instance Creation ******************************************************

    /**
     * Constructs a parser for the given encoded column/row specification,
     * the given LayoutMap, and orientation.
     *
     * @param source         the raw encoded column or row specification
     *                       as provided by the user
     * @param description    describes the source, e.g. "column specification"
     * @param layoutMap      maps layout variable names to ColumnSpec and
     *                       RowSpec objects
     * @param horizontal    {@code true} for columns, {@code false} for rows
     *
     * @throws NullPointerException if {@code source} is {@code null}
     */
    private FormSpecParser(
            String source,
            String description,
            LayoutMap layoutMap,
            boolean horizontal) {
        if (source == null) {
            throw new NullPointerException("The " + description + " must not be null.");
        }
        this.source = source;
        this.layoutMap = layoutMap != null
            ? layoutMap
            : LayoutMap.getDefault();
        this.horizontal = horizontal;
    }


    // Parser API *************************************************************

    static ColumnSpec[] parseColumnSpecs(
            String encodedColumnSpecs,
            LayoutMap layoutMap) {
        FormSpecParser parser = new FormSpecParser(
                encodedColumnSpecs,
                "encoded column specifications",
                layoutMap,
                true);
        return parser.parseColumnSpecs();
    }


    static RowSpec[] parseRowSpecs(
            String encodedRowSpecs,
            LayoutMap layoutMap) {
        FormSpecParser parser = new FormSpecParser(
                encodedRowSpecs,
                "encoded column specifications",
                layoutMap,
                false);
        return parser.parseRowSpecs();
    }


    // Parser Implementation **************************************************

    private ColumnSpec[] parseColumnSpecs() {
        List tokenList = tokenize(source, 0);
        int columnCount = tokenList.size();
        ColumnSpec[] columnSpecs = new ColumnSpec[columnCount];
        for (int i = 0; i < columnCount; i++) {
            String token = (String) tokenList.get(i);
            columnSpecs[i] = new ColumnSpec(token, layoutMap);
        }
        return columnSpecs;
    }


    private RowSpec[] parseRowSpecs() {
        List tokenList = tokenize(source, 0);
        int rowCount = tokenList.size();
        RowSpec[] rowSpecs = new RowSpec[rowCount];
        for (int i = 0; i < rowCount; i++) {
            String token = (String) tokenList.get(i);
            rowSpecs[i] = new RowSpec(token, layoutMap);
        }
        return rowSpecs;
    }


    // Parser Implementation **************************************************

    private List tokenize(String expression, int offset) {
        List tokenList = new ArrayList();
        int braceLevel = 0;
        int bracketLevel = 0;
        int length = expression.length();
        int tokenStart = 0;
        char c;
        boolean lead = true;
        for (int i = 0; i < length; i++) {
            c = expression.charAt(i);
            if (lead && Character.isWhitespace(c)) {
                tokenStart++;
                continue;
            } else {
                lead = false;
            }
            if ((c == ',') && (braceLevel == 0) && (bracketLevel == 0)) {
                String token = expression.substring(tokenStart, i);
                addToken(tokenList, token, offset + tokenStart);
                tokenStart = i + 1;
                lead = true;
            } else if (c == '(') {
                if (bracketLevel > 0) {
                    fail(offset + i, "illegal '(' in [..]");
                }
                braceLevel++;
            } else if (c == ')') {
                if (bracketLevel > 0) {
                    fail(offset + i, "illegal ')' in [...]");
                }
                braceLevel--;
                if (braceLevel < 0) {
                    fail(offset + i, "missing '('");
                }
            } else if (c == '[') {
                if (bracketLevel > 0) {
                    fail(offset + i, "too many '['");
                }
                bracketLevel++;
            } else if (c == ']') {
                bracketLevel--;
                if (bracketLevel < 0) {
                    fail(offset + i, "missing '['");
                }
            }
        }
        if (braceLevel > 0) {
            fail(offset + length, "missing ')'");
        }
        if (bracketLevel > 0) {
            fail(offset + length, "missing ']");
        }
        if (tokenStart < length) {
            String token = expression.substring(tokenStart);
            addToken(tokenList, token, offset + tokenStart);
        }
        return tokenList;
    }


    private void addToken(List tokenList, String expression, int offset) {
        String trimmedExpression = expression.trim();
        Multiplier multiplier = multiplier(trimmedExpression, offset);
        if (multiplier == null) {
            tokenList.add(trimmedExpression);
            return;
        }
        List subTokenList = tokenize(multiplier.expression, offset + multiplier.offset);
        for (int i=0; i < multiplier.multiplier; i++) {
            tokenList.addAll(subTokenList);
        }
    }


    private Multiplier multiplier(String expression, int offset) {
        Matcher matcher = EXPANSION_PREFIX_PATTERN.matcher(expression);
        if (!matcher.find()) {
            return null;
        }
        if (matcher.start() > 0) {
            fail(offset + matcher.start(), "illegal multiplier position");
        }
        String groupIntStr = expression.substring(0, matcher.end() - 2);
        int groupInt = 0;
        try {
            groupInt = Integer.parseInt(groupIntStr);
        } catch (NumberFormatException e) {
            fail(offset, e);
        }
        if (groupInt <= 0) {
            fail(offset, "illegal 0 multiplier");
        }
        if (expression.charAt(expression.length()-1) != ')') {
            fail(offset + expression.length(), "missing ')'");
        }
        String subexpression = expression.substring(matcher.end(),expression.length()-1);
        return new Multiplier(groupInt, subexpression, matcher.end());
    }


    // Exceptions *************************************************************

    private void fail(int index, String description) {
        throw new FormLayoutParseException(
                message(index, description));
    }


    private void fail(int index, NumberFormatException cause) {
        throw new FormLayoutParseException(
                message(index, "Invalid multiplier"),
                cause);
    }


    private String message(int index, String description) {
        StringBuffer buffer = new StringBuffer('\n');
        buffer.append('\n');
        buffer.append(source);
        buffer.append('\n');
        for (int i = 0; i < index; i++) {
            buffer.append(' ');
        }
        buffer.append('^');
        buffer.append(description);
        String message = buffer.toString();
        throw new FormLayoutParseException(message);
    }


    public static final class FormLayoutParseException extends RuntimeException {

        FormLayoutParseException(String message) {
            super(message);
        }

        FormLayoutParseException(String message, Throwable cause) {
            super(message, cause);
        }

    }


    // Helper Class ***********************************************************

    static final class Multiplier {

        final int multiplier;
        final String expression;
        final int offset;

        Multiplier(int multiplier, String expression, int offset) {
            this.multiplier = multiplier;
            this.expression = expression;
            this.offset = offset;
        }
    }


}
