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

package com.jgoodies.forms.layout;

import junit.framework.TestCase;

/**
 * A test case for class {@link CellConstraints}.
 *
 * @author	Karsten Lentzsch
 */
public final class CellConstraintsTest extends TestCase {
    
    /**
     * Checks that the constructor rejects non-positive origin and extent.
     */
    public void testRejectNonPositiveOriginAndExtent() {
        assertRejects( 0,  1,  1,  1);
        assertRejects(-1,  1,  1,  1);
        assertRejects( 1,  0,  1,  1);
        assertRejects( 1, -1,  1,  1);
        assertRejects( 1,  1,  0,  1);
        assertRejects( 1,  1, -1,  1);
        assertRejects( 1,  1,  1,  0);
        assertRejects( 1,  1,  1, -1);
    }
    
    /**
     * Tests the CellConstraints parser on valid encodings.
     */
    public void testValidEncodings() {
        assertEquals(new CellConstraints(),
                     new CellConstraints("1, 1"));

        assertEquals(new CellConstraints(2, 3),
                     new CellConstraints("2, 3"));

        assertEquals(new CellConstraints(3, 4, 2, 5),
                     new CellConstraints("3, 4, 2, 5"));
        
        assertEquals(new CellConstraints(5, 6, 
                                         CellConstraints.LEFT,
                                         CellConstraints.BOTTOM),
                     new CellConstraints("5, 6, left, bottom"));

        assertEquals(new CellConstraints(7, 8, 3, 2,
                                         CellConstraints.FILL,
                                         CellConstraints.DEFAULT),
                     new CellConstraints("7, 8, 3, 2, f, d"));
    }

    /**
     * Tests that the CellConstraints parser rejects invalid encodings.
     */
    public void testRejectInvalidCellConstraintsEncodings() {
        assertRejects("0, 1, 1, 1");        // Illegal bounds
        assertRejects("0, 1, 1");           // Illegal number of arguments
        assertRejects("0, 1, 1, 1, 1");     // Illegal number of arguments
        assertRejects("1");                 // Syntax error
        assertRejects("1, 1, fill");        // Syntax error
        assertRejects("1, 1, 3, 4, f");     // Syntax error
        assertRejects("1, 1, top, center"); // Illegal column alignment
        assertRejects("1, 1, fill, left");  // Illegal row alignment
        assertRejects("1, 1, 2, 3, t, c");  // Illegal column alignment
        assertRejects("1, 1, 2, 3, f, l");  // Illegal row alignment
    }

    /**
     * Tests that the CellConstraints parser rejects invalid encodings.
     */
    public void testRejectInvalidCellConstraintsAlignments() {
        try {
            new CellConstraints(1, 1, CellConstraints.BOTTOM, CellConstraints.CENTER);
            fail("The CellConstraints constructor should reject invalid orientations.");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("The constructor has thrown an unexpected exception: " + e);
        }
        try {
            new CellConstraints(1, 1, CellConstraints.CENTER, CellConstraints.RIGHT);
            fail("The CellConstraints constructor should reject invalid orientations.");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("The constructor has thrown an unexpected exception: " + e);
        }
        CellConstraints cc = new CellConstraints();
        try {
            cc.xy(1, 1, CellConstraints.BOTTOM, CellConstraints.CENTER);
            fail("The CellConstraints setter should reject invalid orientations.");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("The setter has thrown an unexpected exception: " + e);
        }
        try {
            cc.xy(1, 1, CellConstraints.BOTTOM, CellConstraints.CENTER);
            fail("The CellConstraints setter should reject invalid orientations.");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("The setter has thrown an unexpected exception: " + e);
        }
    }

    // Helper Code ***********************************************************

    /**
     * Checks if the CellConstraints constructor allows to construct
     * an instance for the specified cell bounds.
     */
    private void assertRejects(String invalidEncoding) {    
        try {
            new CellConstraints(invalidEncoding);
            fail("The parser should reject the invalid encoding: " + invalidEncoding);
        } catch (IllegalArgumentException e) {
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            fail("The parser has thrown an unexpected exception for:" 
                 + invalidEncoding 
                 + "; exception=" + e);
        }
    }
    
    /**
     * Checks if the CellConstraints constructor allows to construct
     * an instance for the specified cell bounds.
     */
    private void assertRejects(int gridX, int gridY,
                                int gridWidth, int gridHeight) {    
        try {
            new CellConstraints(gridX, gridY, gridWidth, gridHeight);
            fail("The CellConstraints constructor should reject non-positive bounds values.");
        } catch (IndexOutOfBoundsException e) {
        } catch (Exception e) {
            fail("The CellConstraints constructor has thrown an unexpected exception:" + e);
        }
    }
    
    /**
     * Checks if the given RowSpec instances are equal and throws a failure
     * if not.
     */
    private void assertEquals(CellConstraints cc1, CellConstraints cc2) {
        if (   cc1.gridX != cc2.gridX
            || cc1.gridY != cc2.gridY
            || cc1.gridWidth != cc2.gridWidth
            || cc1.gridHeight != cc2.gridHeight) {
            fail("Bounds mismatch: cc1=" + cc1 + "; cc2=" + cc2);
        }
        if (   cc1.hAlign != cc2.hAlign
            || cc1.vAlign != cc2.vAlign) {
            fail("Alignment mismatch: cc1=" + cc1 + "; cc2=" + cc2);
        }
        if (!cc1.insets.equals(cc2.insets)) {
            fail("Insets mismatch: cc1=" + cc1 + "; cc2=" + cc2);
        }
    }
    
	
}

