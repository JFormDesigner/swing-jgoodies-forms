/*
 * Copyright (c) 2002-2014 JGoodies Software GmbH. All Rights Reserved.
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
 *  o Neither the name of JGoodies Software GmbH nor the names of
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

import javax.swing.border.Border;

import junit.framework.TestCase;

import com.jgoodies.forms.factories.Paddings;

/**
 * A test case for class {@link Paddings}.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public final class PaddingsTest extends TestCase {


    /**
     * Checks that the constructor rejects negative resize weights.
     */
    public static void testValidEncodings() {
        assertInsetsEquals(
                Paddings.DLU14,
                Paddings.createPadding("14dlu, 14dlu, 14dlu, 14dlu"));
        assertInsetsEquals(
                Paddings.DLU14,
                Paddings.createPadding("   14dlu , 14dlu , 14dlu , 14dlu "));
        assertInsetsEquals(
                Paddings.createPadding(Sizes.DLUY1, Sizes.DLUX2, Sizes.DLUY3, Sizes.DLUX4),
                Paddings.createPadding("   1dlu , 2dlu , 3dlu , 4dlu "));
    }


    // Helper Code ************************************************************

    /**
     * Checks if the given CellConstraints instances are equal
     * and throws a failure if not.
     *
     * @param expected the expected constraints object to be compared
     * @param actual   the actual constraints object to be compared
     */
    private static void assertInsetsEquals(Border expected, Border actual) {
        assertEquals(expected.getBorderInsets(null), actual.getBorderInsets(null));
    }



}