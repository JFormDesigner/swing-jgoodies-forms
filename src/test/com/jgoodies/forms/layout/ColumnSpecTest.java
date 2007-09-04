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

import com.jgoodies.forms.factories.FormFactory;

import junit.framework.TestCase;

/**
 * A test case for class {@link ColumnSpec}.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.18 $
 */
public final class ColumnSpecTest extends TestCase {


    /**
     * Checks that the constructor rejects negative resize weights.
     */
    public void testRejectNegativeResizeWeight() {
        try {
            new ColumnSpec(ColumnSpec.DEFAULT, Sizes.DEFAULT, -1);
            fail("The ColumnSpec constructor should reject negative resize weights.");
        } catch (IllegalArgumentException e) {
            // The expected behavior
        } catch (Exception e) {
            fail("The ColumnSpec constructor has thrown an unexpected exception.");
        }
    }


    /**
     * Checks that the constructor rejects negative resize weights.
     */
    public void testRejectParsedNegativeResizeWeight() {
        try {
            new ColumnSpec("right:default:-1");
            fail("The ColumnSpec parser constructor should reject negative resize weights.");
        } catch (IllegalArgumentException e) {
            // The expected behavior
        } catch (Exception e) {
            fail("The ColumnSpec constructor has thrown an unexpected exception.");
        }
    }


    /**
     * Tests the ColumnSpec parser on valid encodings with different Locales.
     */
    public void testValidColumnSpecEncodings() {
        testValidColumnSpecEncodings(Locale.ENGLISH);
        testValidColumnSpecEncodings(AllFormsTests.TURKISH);
    }


    /**
     * Tests with different Locales that the ColumnSpec parser
     * rejects invalid encodings.
     */
    public void testRejectInvalidColumnSpecEncodings() {
        testRejectInvalidColumnSpecEncodings(Locale.ENGLISH);
        testRejectInvalidColumnSpecEncodings(AllFormsTests.TURKISH);
    }


    public void testDefaultVariables() {
        assertEquals(
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec("@lcgap"));
        assertEquals(
                FormFactory.RELATED_GAP_COLSPEC,
                new ColumnSpec("@rgap"));
        assertEquals(
                FormFactory.UNRELATED_GAP_COLSPEC,
                new ColumnSpec("@ugap"));
    }


    public void testCustomVariable() {
        ColumnSpec labelColumnSpec = new ColumnSpec("left:[80dlu;pref]");
        LayoutMap layoutMap = new LayoutMap(null);
        layoutMap.putColumnSpec("label", labelColumnSpec);
        assertEquals(
                labelColumnSpec,
                new ColumnSpec("@label", layoutMap));
    }


    public void testOverrideDefaultVariable() {
        ConstantSize gapWidth = Sizes.DLUX1;
        ColumnSpec labelComponentColumnSpec = ColumnSpec.createGap(gapWidth);
        LayoutMap layoutMap = new LayoutMap(LayoutMap.getDefault());
        layoutMap.putColumnGapSpec("lcgap", gapWidth);
        assertEquals(
                labelComponentColumnSpec,
                new ColumnSpec("@lcgap", layoutMap));
    }


    public void testMissingVariable() {
        String variable = "@rumpelstilzchen";
        try {
            new ColumnSpec(variable);
            fail("The parser should reject the missing variable:" + variable);
        } catch (Exception e) {
            // The expected behavior
        }
    }


    /**
     * Tests the ColumnSpec parser on valid encodings for a given Locale.
     *
     * @param locale    the Locale used while parsing the strings
     */
    private void testValidColumnSpecEncodings(Locale locale) {
        Locale oldDefault = Locale.getDefault();
        Locale.setDefault(locale);
        try {
            ColumnSpec spec;
            spec = new ColumnSpec(ColumnSpec.LEFT, Sizes.PREFERRED, FormSpec.NO_GROW);
            assertEquals(spec, new ColumnSpec("l:p"));
            assertEquals(spec, new ColumnSpec("L:P"));
            assertEquals(spec, new ColumnSpec("left:p"));
            assertEquals(spec, new ColumnSpec("LEFT:P"));
            assertEquals(spec, new ColumnSpec("l:pref"));
            assertEquals(spec, new ColumnSpec("L:PREF"));
            assertEquals(spec, new ColumnSpec("left:pref"));
            assertEquals(spec, new ColumnSpec("LEFT:PREF"));

            spec = new ColumnSpec(ColumnSpec.DEFAULT, Sizes.MINIMUM, FormSpec.NO_GROW);
            assertEquals(spec, new ColumnSpec("min"));
            assertEquals(spec, new ColumnSpec("MIN"));
            assertEquals(spec, new ColumnSpec("f:min"));
            assertEquals(spec, new ColumnSpec("fill:min"));
            assertEquals(spec, new ColumnSpec("FILL:MIN"));
            assertEquals(spec, new ColumnSpec("f:min:nogrow"));
            assertEquals(spec, new ColumnSpec("F:MIN:NOGROW"));
            assertEquals(spec, new ColumnSpec("fill:min:grow(0)"));

            spec = new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW);
            assertEquals(spec, new ColumnSpec("d"));
            assertEquals(spec, new ColumnSpec("default"));
            assertEquals(spec, new ColumnSpec("DEFAULT"));
            assertEquals(spec, new ColumnSpec("f:default"));
            assertEquals(spec, new ColumnSpec("fill:default"));
            assertEquals(spec, new ColumnSpec("f:default:nogrow"));
            assertEquals(spec, new ColumnSpec("fill:default:grow(0)"));
            assertEquals(spec, new ColumnSpec("FILL:DEFAULT:GROW(0)"));

            spec = new ColumnSpec(ColumnSpec.RIGHT, Sizes.pixel(10), FormSpec.NO_GROW);
            assertEquals(spec, new ColumnSpec("r:10px"));
            assertEquals(spec, new ColumnSpec("right:10px"));
            assertEquals(spec, new ColumnSpec("right:10px:nogrow"));
            assertEquals(spec, new ColumnSpec("RIGHT:10PX:NOGROW"));
            assertEquals(spec, new ColumnSpec("right:10px:grow(0)"));
            assertEquals(spec, new ColumnSpec("right:10px:g(0)"));

            Size size = Sizes.bounded(Sizes.PREFERRED, Sizes.pixel(10), null);
            spec = new ColumnSpec(ColumnSpec.RIGHT, size, FormSpec.NO_GROW);
            assertEquals(spec, new ColumnSpec("right:max(10px;pref)"));
            assertEquals(spec, new ColumnSpec("right:max(pref;10px)"));
            assertEquals(spec, new ColumnSpec("right:[10px;pref]"));

            size = Sizes.bounded(Sizes.PREFERRED, null, Sizes.pixel(10));
            spec = new ColumnSpec(ColumnSpec.RIGHT, size, FormSpec.NO_GROW);
            assertEquals(spec, new ColumnSpec("right:min(10px;pref)"));
            assertEquals(spec, new ColumnSpec("right:min(pref;10px)"));
            assertEquals(spec, new ColumnSpec("right:[pref;10px]"));

            size = Sizes.bounded(Sizes.DEFAULT, null, Sizes.pixel(10));
            spec = new ColumnSpec(ColumnSpec.DEFAULT, size, FormSpec.NO_GROW);
            assertEquals(spec, new ColumnSpec("min(10px;default)"));
            assertEquals(spec, new ColumnSpec("MIN(10PX;DEFAULT)"));
            assertEquals(spec, new ColumnSpec("min(10px;d)"));
            assertEquals(spec, new ColumnSpec("min(default;10px)"));
            assertEquals(spec, new ColumnSpec("min(d;10px)"));
            assertEquals(spec, new ColumnSpec("[d;10px]"));

            size = Sizes.bounded(Sizes.PREFERRED, Sizes.pixel(50), Sizes.pixel(200));
            spec = new ColumnSpec(ColumnSpec.DEFAULT, size, FormSpec.NO_GROW);
            assertEquals(spec, new ColumnSpec("[50px;pref;200px]"));

            spec = new ColumnSpec(ColumnSpec.DEFAULT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW);
            assertEquals(spec, new ColumnSpec("d:grow"));
            assertEquals(spec, new ColumnSpec("default:grow(1)"));
            assertEquals(spec, new ColumnSpec("f:d:g"));
            assertEquals(spec, new ColumnSpec("f:d:grow(1.0)"));
            assertEquals(spec, new ColumnSpec("f:d:g(1.0)"));

            spec = new ColumnSpec(ColumnSpec.DEFAULT, Sizes.DEFAULT, 0.75);
            assertEquals(spec, new ColumnSpec("d:grow(0.75)"));
            assertEquals(spec, new ColumnSpec("default:grow(0.75)"));
            assertEquals(spec, new ColumnSpec("f:d:grow(0.75)"));
            assertEquals(spec, new ColumnSpec("fill:default:grow(0.75)"));
            assertEquals(spec, new ColumnSpec("FILL:DEFAULT:GROW(0.75)"));

            spec = new ColumnSpec("fill:10in");
            assertEquals(spec, new ColumnSpec("FILL:10IN"));

            ColumnSpec spec1 = new ColumnSpec(ColumnSpec.LEFT, Sizes.PREFERRED, FormSpec.NO_GROW);
            ColumnSpec spec2 = new ColumnSpec(ColumnSpec.RIGHT, Sizes.DEFAULT, 1.0);
            ColumnSpec[] specs = ColumnSpec.decodeSpecs("left:pref:none , right:default:grow");
            assertEquals(2, specs.length);
            assertEquals(spec1, specs[0]);
            assertEquals(spec2, specs[1]);
        } finally {
            Locale.setDefault(oldDefault);
        }
    }


    /**
     * Tests that the ColumnSpec parser rejects invalid encodings for a given Locale.
     *
     * @param locale    the Locale used while parsing the strings
     */
    private void testRejectInvalidColumnSpecEncodings(Locale locale) {
        Locale oldDefault = Locale.getDefault();
        Locale.setDefault(locale);
        try {
            assertRejects("karsten");
            assertRejects("d:a:b:");
            assertRejects("top:default:grow");
            assertRejects("bottom:10px");
            assertRejects("max(10px;20px)");
            assertRejects("min(10px;20px)");
            assertRejects("[10px;20px]");
            assertRejects("max(pref;pref)");
            assertRejects("min(pref;pref)");
            assertRejects("[pref;pref]");
            assertRejects("[pref;pref;200px]");  // lower bound must be constant
            assertRejects("[10px;50px;200px]");  // basis must be logical
            assertRejects("[10px;pref;pref]");   // upper bound must be constant
        } finally {
            Locale.setDefault(oldDefault);
        }
    }


    // Helper Code ***********************************************************

    /**
     * Checks if the given ColumnSpec instances are equal and throws a failure
     * if not.
     *
     * @param spec1  the first spec object to be compared
     * @param spec2  the second spec object to be compared
     */
    private void assertEquals(ColumnSpec spec1, ColumnSpec spec2) {
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
     * @param invalidEncoding  the invalid encoded column spec
     */
    private void assertRejects(String invalidEncoding) {
        try {
            new ColumnSpec(invalidEncoding);
            fail("The parser should reject the invalid encoding:" + invalidEncoding);
        } catch (Exception e) {
            // The expected behavior
        }
    }


}
