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
import com.jgoodies.forms.layout.RowSpec;

/**
 * A test case for class {@link RowSpec}.
 * 
 * @author	Karsten Lentzsch
 */
public final class RowSpecTest extends TestCase {
    
    /**
     * Checks that the constructor rejects negative resize weights.
     */
    public void testRejectNegativeResizeWeight() {
        try {
            new RowSpec(RowSpec.DEFAULT, Sizes.DEFAULT, -1);
            fail("The RowSpec constructor should reject negative resize weights.");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("The RowSpec constructor has thrown an unexpected exception.");
        }
    }
    
    /**
     * Checks that the constructor rejects negative resize weights.
     */
    public void testRejectParsedNegativeResizeWeight() {
        try {
            new RowSpec("right:default:-1");
            fail("The RowSpec parser constructor should reject negative resize weights.");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("The RowSpec constructor has thrown an unexpected exception.");
        }
    }

    /**
     * Tests the RowSpec parser on valid encodings.
     */
    public void testValidRowSpecEncodings() {
        RowSpec spec;
        spec = new RowSpec(RowSpec.TOP, Sizes.PREFERRED, RowSpec.NO_GROW);
        assertEquals(spec, new RowSpec("t:p"));
        assertEquals(spec, new RowSpec("top:p"));
        assertEquals(spec, new RowSpec("t:pref"));
        assertEquals(spec, new RowSpec("top:pref"));
        
        spec = new RowSpec(RowSpec.DEFAULT, Sizes.MINIMUM, RowSpec.NO_GROW);
        assertEquals(spec, new RowSpec("min"));
        assertEquals(spec, new RowSpec("c:min"));
        assertEquals(spec, new RowSpec("center:min"));
        assertEquals(spec, new RowSpec("c:min:none"));
        assertEquals(spec, new RowSpec("center:min:grow(0)"));
        
        spec = new RowSpec(RowSpec.FILL, Sizes.DEFAULT, RowSpec.NO_GROW);
        assertEquals(spec, new RowSpec("f:default"));
        assertEquals(spec, new RowSpec("fill:default"));
        assertEquals(spec, new RowSpec("f:default:none"));
        assertEquals(spec, new RowSpec("fill:default:grow(0)"));
        
        spec = new RowSpec(RowSpec.BOTTOM, Sizes.pixel(10), RowSpec.NO_GROW);
        assertEquals(spec, new RowSpec("b:10px"));
        assertEquals(spec, new RowSpec("bottom:10px"));
        assertEquals(spec, new RowSpec("bottom:10px:none"));
        assertEquals(spec, new RowSpec("bottom:10px:grow(0)"));
        assertEquals(spec, new RowSpec("bottom:10px:g(0)"));
        
        Size size = Sizes.bounded(Sizes.PREFERRED, Sizes.pixel(10), null);
        spec = new RowSpec(RowSpec.BOTTOM, size, RowSpec.NO_GROW);
        assertEquals(spec, new RowSpec("bottom:max(10px;pref)"));
        assertEquals(spec, new RowSpec("bottom:max(pref;10px)"));
        
        size = Sizes.bounded(Sizes.PREFERRED, null, Sizes.pixel(10));
        spec = new RowSpec(RowSpec.BOTTOM, size, RowSpec.NO_GROW);
        assertEquals(spec, new RowSpec("bottom:min(10px;pref)"));
        assertEquals(spec, new RowSpec("bottom:min(pref;10px)"));

        size = Sizes.bounded(Sizes.DEFAULT, null, Sizes.pixel(10));
        spec = new RowSpec(RowSpec.DEFAULT, size, RowSpec.NO_GROW);
        assertEquals(spec, new RowSpec("min(10px;default)"));
        assertEquals(spec, new RowSpec("min(10px;d)"));
        assertEquals(spec, new RowSpec("min(default;10px)"));
        assertEquals(spec, new RowSpec("min(d;10px)"));
        
        spec = new RowSpec(RowSpec.DEFAULT, Sizes.DEFAULT, RowSpec.DEFAULT_GROW);
        assertEquals(spec, new RowSpec("d:grow"));
        assertEquals(spec, new RowSpec("default:grow(1)"));
        assertEquals(spec, new RowSpec("c:d:g"));
        assertEquals(spec, new RowSpec("c:d:grow(1.0)"));
        assertEquals(spec, new RowSpec("c:d:g(1.0)"));
        
        spec = new RowSpec(RowSpec.DEFAULT, Sizes.DEFAULT, 0.75);
        assertEquals(spec, new RowSpec("d:grow(0.75)"));
        assertEquals(spec, new RowSpec("default:grow(0.75)"));
        assertEquals(spec, new RowSpec("c:d:grow(0.75)"));
        assertEquals(spec, new RowSpec("center:default:grow(0.75)"));
    }
    
    
    /**
     * Tests that the RowSpec parser rejects invalid encodings.
     */
    public void testRejectInvalidRowSpecEncodings() {
        assertRejects("karsten");
        assertRejects("d:a:b:");
        assertRejects("right:default:grow");
        assertRejects("left:20dlu");
    }


    /**
     * Checks that an unmodifyable RowSpec rejects all modifications.
     */
    public void testUnmodifyable() {
        RowSpec unmodifyableSpec = new RowSpec("p").asUnmodifyable();
        try {
            unmodifyableSpec.setDefaultAlignment(RowSpec.CENTER);
            fail("An unmodifyable RowSpec should reject alignment changes.");
        } catch (Exception e) {}
        try {
            unmodifyableSpec.setSize(Sizes.MINIMUM);
            fail("An unmodifyable RowSpec should reject size changes.");
        } catch (Exception e) {}
        try {
            unmodifyableSpec.setResizeWeight(5.5);
            fail("An unmodifyable RowSpec should reject resize weight changes.");
        } catch (Exception e) {}
    }

    
    // Helper Code ***********************************************************
    
    /**
     * Checks if the given RowSpec instances are equal and throws a failure
     * if not.
     */
    private void assertEquals(RowSpec spec1, RowSpec spec2) {
        if (!spec1.getDefaultAlignment().equals(spec2.getDefaultAlignment())) {
            fail("Alignment mismatch: spec1=" + spec1 + "; spec2=" + spec2);
        }
        if (!spec1.getSize().equals(spec2.getSize())) {
            fail("Size mismatch: spec1=" + spec1 + "; spec2=" + spec2);
        }
        if (!(spec1.getResizeWeight() == spec2.getResizeWeight())) {
            fail("Resize weight mismatch: spec1=" + spec1 + "; spec2=" + spec2);
        }
    }
    
    /**
     * Asserts that the specified column spec encoding is rejected.
     * 
     * @param encodedRowSpec  an encoded column spec
     */
    private void assertRejects(String encodedRowSpec) {
        try {
            new RowSpec(encodedRowSpec);
            fail("The parser should reject encoding:" + encodedRowSpec);
        } catch (Exception e) {}
    }

}