/*
 * Copyright (c) 2002-2013 JGoodies Software GmbH. All Rights Reserved.
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

import javax.swing.JLabel;

import junit.framework.TestCase;

import com.jgoodies.forms.factories.DefaultComponentFactory;

/**
 * A test case for class {@link DefaultComponentFactory}.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.10 $
 */
public final class DefaultComponentFactoryTest extends TestCase {

    public static void testLabelWithoutColon() {
        testLabelAccessibleName("Label without colon",        "Name", "Name");
        testLabelAccessibleName("1 char label without colon", "A",    "A");
        testLabelAccessibleName("Empty label without colon",  "",     "");
    }


    public static void testLabelWithColon() {
        testLabelAccessibleName("Label without colon",      "Name:", "Name");
        testLabelAccessibleName("Empty label without colon", ":",    "");
    }


    public static void testLabelWithCustomAccessibleName() {
        String text = "Name:";
        String accessibleName = "The name";
        JLabel label = DefaultComponentFactory.getInstance().createLabel(text);
        label.getAccessibleContext().setAccessibleName(accessibleName);
        testLabelAccessibleName("Label with custom accessible name",
                label,
                accessibleName);
    }


    private static void testLabelAccessibleName(
            String description,
            String text,
            String expected) {
        testLabelAccessibleName(
                description,
                DefaultComponentFactory.getInstance().createLabel(text),
                expected);
    }


    private static void testLabelAccessibleName(
            String description,
            JLabel label,
            String expected) {
        assertEquals(description,
                expected,
                label.getAccessibleContext().getAccessibleName());
    }


}
