/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of 
 * JGoodies Karsten Lentzsch. Use is subject to license terms.
 *
 */
 
package com.jgoodies.forms.layout;

import java.awt.Container;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Specifies columns and rows in {@link FormLayout} by their default
 * alignment, start size and resizing behavior.
 *
 * @author	Karsten Lentzsch
 * @see	FormLayout
 * @see	CellConstraints
 */
abstract class FormSpec {
    
    private static boolean detectDeprecatedGrowEncodings = false;
    
    
    // Horizontal and Vertical Default Alignments ***************************
    
    /**
     * By default put components in the left.
     */
    static final DefaultAlignment LEFT_ALIGN = new DefaultAlignment("left");

    /**
     * By default put components in the right.
     */
    static final DefaultAlignment RIGHT_ALIGN = new DefaultAlignment("right");

    /**
     * By default put the components in the top.
     */
    static final DefaultAlignment TOP_ALIGN = new DefaultAlignment("top");

    /**
     * By default put the components in the bottom.
     */
    static final DefaultAlignment BOTTOM_ALIGN = new DefaultAlignment("bottom");

    /**
     * By default put the components in the center.
     */
    static final DefaultAlignment CENTER_ALIGN = new DefaultAlignment("center");

    /**
     * By default fill the column or row.
     */
    static final DefaultAlignment FILL_ALIGN = new DefaultAlignment("fill");


    // Resizing Weights *****************************************************

    /**
     * An outdated constant to give a column or row a fixed size.
     * <b>Note: This constant will be removed in a future release</b>
     * 
     * @deprecated use <code>NO_GROW</code> instead
     */
    public static final double NO_FILL = 0.0d;

    /**
     * A default resize weight.
     * <b>Note: This constant will be removed in a future release</b>
     * 
     * @deprecated use <code>DEFAULT_GROW</code> instead
     */
    public static final double DEFAULT_FILL = 1.0d;
    
    /**
     * Gives a column or row a fixed size.
     */
    public static final double NO_GROW = NO_FILL;

    /**
     * The default resize weight.
     */
    public static final double DEFAULT_GROW = DEFAULT_FILL;
    

    // Fields ***************************************************************

    /**
     * Holds the default alignment that will be used if a cell does not
     * override this default.
     */ 
    private DefaultAlignment defaultAlignment;
    
    /**
     * Holds the size that describes how to size this column or row.
     */ 
    private Size size; 
    
    /**
     * Holds the resize weight; is 0 if not used.
     */
    private double resizeWeight;
    
    
    // Instance Creation ****************************************************

    /**
     * Constructs a <code>FormSpec</code> for the given default alignment,
     * size, and resize weight. The resize weight must be a non-negative
     * double; you can use <code>NONE</code> as a convenience value for no
     * resize.
     * 
     * @param defaultAlignment the spec's default alignment
     * @param size             a constant, component or bounded size
     * @param resizeWeight     the spec resize weight      
     * @throws IllegalArgumentException if the resize weight is negative
     */
    protected FormSpec(DefaultAlignment defaultAlignment, 
                        Size size, 
                        double resizeWeight) {
    	this.defaultAlignment = defaultAlignment;
        this.size             = size;
        this.resizeWeight     = resizeWeight;
        if (resizeWeight < 0) 
            throw new IllegalArgumentException("The resize weight must be non-negative.");
    }
	
    /**
     * Constructs a <code>FormSpec</code> from the specified encoded
     * description. The description will be parsed to set initial values.
     * 
     * @param defaultAlignment 	the default alignment
     * @param encodedDescription	the encoded description
     */
    protected FormSpec(DefaultAlignment defaultAlignment, String encodedDescription) {
        this(defaultAlignment, Sizes.DEFAULT, NO_GROW);
        parseAndInitValues(encodedDescription.toLowerCase());
    }
    
    // Temporary Code *******************************************************
    
    /**
     * Activates the detection of deprecated grow encodings.
     * <p>
     * The string encoding for growing columns and rows has recently
     * been changed from <i>fill</i> to <i>grow</i>, and <i>f</i> to <i>g</i>.
     * Invoking this methods activates a detection that will print
     * a stack trace, each time a deprecated grow encoding is used.
     * This helps developers detect such outdated encodings.
     */
    public static void detectDeprecatedGrowEncodings() {
        detectDeprecatedGrowEncodings = true;
    }


    // Public API ***********************************************************
    
    /**
     * Answers the default alignment.
     */
    public final DefaultAlignment getDefaultAlignment() {
        return defaultAlignment;
    }
    
    /**
     * Sets the default alignment.
     * 
     * @param newDefaultAlignment	the new default alignment
     */
    public void setDefaultAlignment(DefaultAlignment newDefaultAlignment) {
        defaultAlignment = newDefaultAlignment;
    }
    
    /**
     * Returns the size.
     *  
     * @return the size
     */
    public final Size getSize() {
        return size;
    }
    
    /**
     * Sets the size.
     *  
     * @param size	the new size
     */
    public void setSize(Size size) {
        this.size = size;
    }
    
    
    /**
     * Answers the current resize weight.
     * @return the resize weight.
     */
    public final double getResizeWeight() {
        return resizeWeight;
    }
    
    /**
     * Sets a new resize weight.
     * @param weight	the new resize weight
     */
    public void setResizeWeight(double weight) {
        resizeWeight = weight;
    }


    // Parsing **************************************************************

    /**
     * Parses an encoded form spec and initializes all required fields.
     * The encoded description must be in lower case.
     * 
     * @throws IllegalArgumentException if the string is empty, has no size,
     * or is otherwise invalid
     */
    private void parseAndInitValues(String encodedDescription) {
        StringTokenizer tokenizer = new StringTokenizer(encodedDescription, ":");
        if (!tokenizer.hasMoreTokens()) {
            throw new IllegalArgumentException(
                                    "The form spec must not be empty.");
        }
        String token = tokenizer.nextToken();
        
        // Check if the first token is an orientation.
        DefaultAlignment alignment = DefaultAlignment.valueOf(token, isHorizontal());
        if (alignment != null) {
            setDefaultAlignment(alignment);
            if (!tokenizer.hasMoreTokens()) {
                throw new IllegalArgumentException(
                                    "The form spec must provide a size.");
            }
            token = tokenizer.nextToken();
        }
        
        parseAndInitSize(token);
        
        if (tokenizer.hasMoreTokens()) {
            setResizeWeight(decodeResize(tokenizer.nextToken()));
        }  
    }
    

    /**
     * Parses an encoded size spec and initializes the size fields.
     */
    private void parseAndInitSize(String token) {
        if (token.startsWith("max(") && token.endsWith(")")) {
            setSize(parseAndInitBoundedSize(token, false));
            return;
        } 
        if (token.startsWith("min(") && token.endsWith(")")) {
            setSize(parseAndInitBoundedSize(token, true));
            return;
        }
        setSize(decodeAtomicSize(token));
    }


    /**
     * Parses an encoded compound size and sets the size fields.
     * The compound size has format: 
     * max(<atomic size>;<atomic size2>) | min(<atomic size1>;<atomic size2>)
     * One of the two atomic sizes must be a logical size, the other must
     * be a size constant.
     * @param token
     * @param isMax
     */
    private Size parseAndInitBoundedSize(String token, boolean setMax) {
        int semicolonIndex = token.indexOf(';');
        String sizeToken1 = token.substring(4, semicolonIndex);
        String sizeToken2 = token.substring(semicolonIndex+1, token.length()-1);
        
        Size size1 = decodeAtomicSize(sizeToken1);
        Size size2 = decodeAtomicSize(sizeToken2);
        
        // Check valid combinations and set min or max.
        if (size1 instanceof ConstantSize) {
            if (size2 instanceof Sizes.ComponentSize) {
                return new BoundedSize(size2, setMax ? null : size1,
                                               setMax ? size1 : null);
            } else
                throw new IllegalArgumentException(
                                "Bounded sizes must not be both constants.");
        } else {
            if (size2 instanceof ConstantSize) {
                return new BoundedSize(size1, setMax ? null : size2,
                                               setMax ? size2 : null);
            } else
                throw new IllegalArgumentException(
                                "Bounded sizes must not be both logical.");
        } 
    }
    
    
    /**
     * Decodes and answers an atomic size that is either a constant size or a
     * component size.
     * @param token	the encoded size 
     * @return the decoded size either a constant or component size
     */
    private Size decodeAtomicSize(String token) {
        Sizes.ComponentSize componentSize = Sizes.ComponentSize.valueOf(token);
        if (componentSize != null)
            return componentSize;
        else
            return ConstantSize.valueOf(token, isHorizontal());
    }
    
    
    /**
     * Decodes an encoded resize mode and resize weight and answers
     * the resize weight.
     * @param token	the encoded resize weight
     * @return the decoded resize weight
     * @throws IllegalArgumentException if the string description is an
     * invalid string representation
     */
    private double decodeResize(String token) {
        if (token.equals("g") || token.equals("grow")) {
            return DEFAULT_GROW;
        } 
        if (token.equals("f") || token.equals("fill")) {
            warnDeprecatedGrowEncoding();
            return DEFAULT_GROW;
        } 
        if (token.equals("n") || token.equals("nogrow") || token.equals("none")) {
            return NO_GROW;
        }
        // Must have format: grow(<double>)
        if ((token.startsWith("grow(") || token.startsWith("g("))
             && token.endsWith(")")) {
            int leftParen  = token.indexOf('(');  
            int rightParen = token.indexOf(')');
            String substring = token.substring(leftParen + 1, rightParen);
            return Double.parseDouble(substring);
        } else if ((token.startsWith("fill(") || token.startsWith("f("))
             && token.endsWith(")")) {
            warnDeprecatedGrowEncoding();
            int leftParen  = token.indexOf('(');  
            int rightParen = token.indexOf(')');
            String substring = token.substring(leftParen + 1, rightParen);
            return Double.parseDouble(substring);
        } else {        
            throw new IllegalArgumentException(
                    "The resize argument '" + token + "' is invalid. " +
                    " Must be one of: grow, g, none, n, grow(<double>), g(<double>)");
        }
    }
    
    
    // Misc *****************************************************************
    
    /**
     * Returns a string representation of this form specification.
     * The string representation consists of three elements separated by
     * a colon (<tt>":"</tt>), first the alignment, second the size,
     * and third the resize spec.
     * 
     * @return	a string representation of the form specification.
     */
    public final String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(defaultAlignment);
        
        buffer.append(":");
        buffer.append(size.toString());
        buffer.append(':');
        if (resizeWeight == NO_GROW) {
            buffer.append("noGrow");
        } else if (resizeWeight == DEFAULT_GROW) {
            buffer.append("grow");
        } else {
            buffer.append("grow(");
            buffer.append(resizeWeight);
            buffer.append(')');
        }
        return buffer.toString();
    }
    
    /**
     * Returns a string representation of this form specification.
     * The string representation consists of three elements separated by
     * a colon (<tt>":"</tt>), first the alignment, second the size,
     * and third the resize spec.
     * 
     * @return  a string representation of the form specification.
     */
    public final String toShortString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(defaultAlignment.abbreviation());
        
        buffer.append(":");
        buffer.append(size.toString());
        buffer.append(':');
        if (resizeWeight == NO_GROW) {
            buffer.append("n");
        } else if (resizeWeight == DEFAULT_GROW) {
            buffer.append("g");
        } else {
            buffer.append("g(");
            buffer.append(resizeWeight);
            buffer.append(')');
        }
        return buffer.toString();
    }
    
    
    // Abstract Behavior ****************************************************

    /**
     * Returns if this is a horizontal specification (vs. vertical).
     * Used to distinct between horizontal and vertical dialog units,
     * which have different conversion factors.
     * @return true for horizontal, false for vertical
     */
    abstract boolean isHorizontal();


    // Helper Code **********************************************************
    
    /**
     * Computes the maximum size for the given list of components, using
     * this form spec and the specified measure. 
     * <p>
     * Invoked by FormLayout to determine the size of one of my elements
     */
    final int maximumSize(Container container,
                    List components, 
                    FormLayout.Measure minMeasure,
                    FormLayout.Measure prefMeasure,
                    FormLayout.Measure defaultMeasure) {
        return size.maximumSize(container,
                                 components, 
                                 minMeasure,
                                 prefMeasure,
                                 defaultMeasure);
    }
    
    
    /**
     * A typesafe enumeration for the column and row default alignment types.
     */
    public static final class DefaultAlignment {
        
        private final String name;

        private DefaultAlignment(String name) { 
            this.name = name; 
        }

        /**
         * Answers a DefaultAlignment that corresponds to the specified
         * string, null if no such aignment exists.
         * @param str	the encoded alignment
         * @param isHorizontal   indicates the values orientation
         * @return the corresponding DefaultAlignment or null
         */
        static DefaultAlignment valueOf(String str, boolean isHorizontal) {
            if (str.equals("f") || str.equals("fill"))
                return FILL_ALIGN;
            else if (str.equals("c") || str.equals("center"))
                return CENTER_ALIGN;
            else if (isHorizontal) {
                if (str.equals("r") || str.equals("right"))
                    return RIGHT_ALIGN;
                else if (str.equals("l") || str.equals("left"))
                    return LEFT_ALIGN;
                else 
                    return null;
            } else {
                if (str.equals("t") || str.equals("top"))
                    return TOP_ALIGN;
                else if (str.equals("b") || str.equals("bottom"))
                    return BOTTOM_ALIGN;
                else
                    return null;
            }
        }

        public String toString()  { return name; }

        public char abbreviation() { return name.charAt(0); }

    }
    
    
    /**
     * Prints a stack trace if the detection of deprecated grow encodings
     * is active, that is if <code>detectDeprecatedGrowEncodings==true</code>.
     */
    private void warnDeprecatedGrowEncoding() {
        if (detectDeprecatedGrowEncodings) {
            Exception e =
                new IllegalArgumentException("The resize arguments 'fill' and 'f' are deprecated and have been replaced by 'grow' and 'g'.");
            e.printStackTrace();
        }
    }

    	
}

