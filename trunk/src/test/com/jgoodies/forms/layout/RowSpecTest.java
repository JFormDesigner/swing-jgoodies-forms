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

import java.util.Locale;

import junit.framework.TestCase;

import com.jgoodies.forms.factories.FormFactory;

/**
 * A test case for class {@link RowSpec}.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.20 $
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
            // The expected behavior
        } catch (Exception e) {
            fail("The RowSpec constructor has thrown an unexpected exception.");
        }
    }


    /**
     * Checks that the constructor rejects negative resize weights.
     */
    public void testRejectParsedNegativeResizeWeight() {
        try {
            RowSpec.parse("right:default:-1");
            fail("The RowSpec parser constructor should reject negative resize weights.");
        } catch (IllegalArgumentException e) {
            // The expected behavior
        } catch (Exception e) {
            fail("The RowSpec constructor has thrown an unexpected exception.");
        }
    }


    /**
     * Tests the RowSpec parser on valid encodings with different locales.
     */
    public void testValidRowSpecEncodings() {
        testValidRowSpecEncodings(Locale.ENGLISH);
        testValidRowSpecEncodings(AllFormsTests.TURKISH);
    }


    /**
     * Tests that the RowSpec parser rejects invalid encodings for a given Locale.
     */
    public void testRejectInvalidRowSpecEncodings() {
        testRejectInvalidRowSpecEncodings(Locale.ENGLISH);
        testRejectInvalidRowSpecEncodings(AllFormsTests.TURKISH);
    }


    public void testDefaultVariables() {
        assertEquals(
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.parse("$rgap"));
        assertEquals(
                FormFactory.UNRELATED_GAP_ROWSPEC,
                RowSpec.parse("$ugap"));
        assertEquals(
                FormFactory.NARROW_LINE_GAP_ROWSPEC,
                RowSpec.parse("$ngap"));
        assertEquals(
                FormFactory.LINE_GAP_ROWSPEC,
                RowSpec.parse("$lgap"));
        assertEquals(
                FormFactory.PARAGRAPH_GAP_ROWSPEC,
                RowSpec.parse("$pgap"));
    }


    public void testCustomVariable() {
        ConstantSize gapHeight = Sizes.DLUY21;
        RowSpec largeGap = RowSpec.createGap(gapHeight);
        LayoutMap layoutMap = new LayoutMap(null);
        layoutMap.rowPut("large", largeGap);
        assertEquals(
                largeGap,
                RowSpec.parse("$large", layoutMap));
    }


    public void testOverrideDefaultVariable() {
        ConstantSize gapHeight = Sizes.DLUY1;
        RowSpec lineSpec = RowSpec.createGap(gapHeight);
        LayoutMap layoutMap = new LayoutMap(LayoutMap.getRoot());
        layoutMap.rowPut("lgap", lineSpec);
        assertEquals(
                lineSpec,
                RowSpec.parse("$lgap", layoutMap));
    }


    public void testVariableExpression() {
        RowSpec spec0 = new RowSpec(RowSpec.TOP_ALIGN, Sizes.PREFERRED, RowSpec.NO_GROW);
        RowSpec spec1 = RowSpec.createGap(Sizes.DLUY3);
        RowSpec spec2 = new RowSpec(Sizes.PREFERRED);
        LayoutMap layoutMap = new LayoutMap(LayoutMap.getRoot());
        layoutMap.rowPut("test name", "t:p, 3dlu, p");
        RowSpec[] specs = RowSpec.parseAll("${test name}, 3dlu, ${test Name}", layoutMap);
        assertEquals(spec0, specs[0]);
        assertEquals(spec1, specs[1]);
        assertEquals(spec2, specs[2]);

        assertEquals(spec1, specs[3]);

        assertEquals(spec0, specs[4]);
        assertEquals(spec1, specs[5]);
        assertEquals(spec2, specs[6]);
    }


    public void testMissingColumnSpecVariable() {
        String variable = "$rumpelstilzchen";
        try {
            RowSpec.parse(variable);
            fail("The parser should reject the missing variable:" + variable);
        } catch (Exception e) {
            // The expected behavior
        }
    }


    /**
     * Tests the RowSpec parser on valid encodings for a given locale.
     *
     * @param locale    the Locale used while parsing the strings
     */
    private void testValidRowSpecEncodings(Locale locale) {
        Locale oldDefault = Locale.getDefault();
        Locale.setDefault(locale);
        try {
            RowSpec spec;
            spec = new RowSpec(RowSpec.TOP, Sizes.PREFERRED, FormSpec.NO_GROW);
            assertEquals(spec, RowSpec.parse("t:p"));
            assertEquals(spec, RowSpec.parse("top:p"));
            assertEquals(spec, RowSpec.parse("t:pref"));
            assertEquals(spec, RowSpec.parse("top:pref"));

            spec = new RowSpec(RowSpec.DEFAULT, Sizes.MINIMUM, FormSpec.NO_GROW);
            assertEquals(spec, RowSpec.parse("min"));
            assertEquals(spec, RowSpec.parse("c:min"));
            assertEquals(spec, RowSpec.parse("center:min"));
            assertEquals(spec, RowSpec.parse("c:min:none"));
            assertEquals(spec, RowSpec.parse("center:min:grow(0)"));

            spec = new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW);
            assertEquals(spec, RowSpec.parse("f:default"));
            assertEquals(spec, RowSpec.parse("fill:default"));
            assertEquals(spec, RowSpec.parse("FILL:DEFAULT"));
            assertEquals(spec, RowSpec.parse("f:default:none"));
            assertEquals(spec, RowSpec.parse("F:DEFAULT:NONE"));
            assertEquals(spec, RowSpec.parse("fill:default:grow(0)"));
            assertEquals(spec, RowSpec.parse("FILL:DEFAULT:GROW(0)"));

            spec = new RowSpec(RowSpec.BOTTOM, Sizes.pixel(10), FormSpec.NO_GROW);
            assertEquals(spec, RowSpec.parse("b:10px"));
            assertEquals(spec, RowSpec.parse("bottom:10px"));
            assertEquals(spec, RowSpec.parse("BOTTOM:10PX"));
            assertEquals(spec, RowSpec.parse("bottom:10px:none"));
            assertEquals(spec, RowSpec.parse("bottom:10px:grow(0)"));
            assertEquals(spec, RowSpec.parse("bottom:10px:g(0)"));

            Size size = Sizes.bounded(Sizes.PREFERRED, Sizes.pixel(10), null);
            spec = new RowSpec(RowSpec.BOTTOM, size, FormSpec.NO_GROW);
            assertEquals(spec, RowSpec.parse("bottom:max(10px;pref)"));
            assertEquals(spec, RowSpec.parse("bottom:max(pref;10px)"));
            assertEquals(spec, RowSpec.parse("bottom:[10px,pref]"));

            size = Sizes.bounded(Sizes.PREFERRED, null, Sizes.pixel(10));
            spec = new RowSpec(RowSpec.BOTTOM, size, FormSpec.NO_GROW);
            assertEquals(spec, RowSpec.parse("bottom:min(10px;pref)"));
            assertEquals(spec, RowSpec.parse("BOTTOM:MIN(10PX;PREF)"));
            assertEquals(spec, RowSpec.parse("bottom:min(pref;10px)"));
            assertEquals(spec, RowSpec.parse("bottom:[pref,10px]"));

            size = Sizes.bounded(Sizes.DEFAULT, null, Sizes.pixel(10));
            spec = new RowSpec(RowSpec.DEFAULT, size, FormSpec.NO_GROW);
            assertEquals(spec, RowSpec.parse("min(10px;default)"));
            assertEquals(spec, RowSpec.parse("min(10px;d)"));
            assertEquals(spec, RowSpec.parse("min(default;10px)"));
            assertEquals(spec, RowSpec.parse("min(d;10px)"));
            assertEquals(spec, RowSpec.parse("[d,10px]"));

            size = Sizes.bounded(Sizes.PREFERRED, Sizes.pixel(50), Sizes.pixel(200));
            spec = new RowSpec(RowSpec.DEFAULT, size, FormSpec.NO_GROW);
            assertEquals(spec, RowSpec.parse("[50px,pref,200px]"));

            spec = new RowSpec(RowSpec.DEFAULT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW);
            assertEquals(spec, RowSpec.parse("d:grow"));
            assertEquals(spec, RowSpec.parse("default:grow(1)"));
            assertEquals(spec, RowSpec.parse("c:d:g"));
            assertEquals(spec, RowSpec.parse("c:d:grow(1.0)"));
            assertEquals(spec, RowSpec.parse("c:d:g(1.0)"));

            spec = new RowSpec(RowSpec.DEFAULT, Sizes.DEFAULT, 0.75);
            assertEquals(spec, RowSpec.parse("d:grow(0.75)"));
            assertEquals(spec, RowSpec.parse("default:grow(0.75)"));
            assertEquals(spec, RowSpec.parse("c:d:grow(0.75)"));
            assertEquals(spec, RowSpec.parse("center:default:grow(0.75)"));

            RowSpec spec1 = new RowSpec(RowSpec.TOP, Sizes.PREFERRED, FormSpec.NO_GROW);
            RowSpec spec2 = new RowSpec(RowSpec.BOTTOM, Sizes.DEFAULT, 1.0);
            RowSpec[] specs = RowSpec.parseAll(
                    "top:pref:none , bottom:default:grow", null);
            assertEquals(2, specs.length);
            assertEquals(spec1, specs[0]);
            assertEquals(spec2, specs[1]);
        } finally {
            Locale.setDefault(oldDefault);
        }
    }


    /**
     * Tests that the RowSpec parser rejects invalid encodings for a given Locale.
     *
     * @param locale    the Locale used while parsing the strings
     */
    private void testRejectInvalidRowSpecEncodings(Locale locale) {
        Locale oldDefault = Locale.getDefault();
        Locale.setDefault(locale);
        try {
            assertRejects("karsten");
            assertRejects("d:a:b:");
            assertRejects("right:default:grow"); // invalid alignment
            assertRejects("left:20dlu");
            assertRejects("max(10px;20px)");
            assertRejects("min(10px;20px)");
            assertRejects("[10px,20px]");
            assertRejects("max(pref;pref)");
            assertRejects("min(pref;pref)");
            assertRejects("[pref,pref]");
            assertRejects("[pref,pref,200px]");  // lower bound must be constant
            assertRejects("[10px,50px,200px]");  // basis must be logical
            assertRejects("[10px,pref,pref]");   // upper bound must be constant
        } finally {
            Locale.setDefault(oldDefault);
        }
    }


    // Helper Code ***********************************************************

    /**
     * Checks if the given RowSpec instances are equal and throws a failure
     * if not.
     *
     * @param spec1    the first row spec object to be compared
     * @param spec2    the second row spec object to be compared
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
     * Asserts that the specified row spec encoding is rejected.
     *
     * @param encodedRowSpec  an encoded row spec
     */
    private void assertRejects(String encodedRowSpec) {
        try {
            RowSpec.parse(encodedRowSpec);
            fail("The parser should reject encoding:" + encodedRowSpec);
        } catch (Exception e) {
            // The expected behavior
        }
    }

}
