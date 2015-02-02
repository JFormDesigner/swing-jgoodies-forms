/*
 * Copyright (c) 2002-2015 JGoodies Software GmbH. All Rights Reserved.
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

import java.awt.ContainerOrderFocusTraversalPolicy;

import javax.swing.LayoutFocusTraversalPolicy;

import org.junit.Test;

import com.jgoodies.forms.builder.FormBuilder;
import com.jgoodies.forms.util.FocusTraversalType;

/**
 * A test case for class {@link FormBuilder}.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
@SuppressWarnings("static-method")
public final class FormBuilderTest {


    // Focus Traversal ********************************************************
    
    @Test(expected=NullPointerException.class)
    public void rejectFocusTraversalTypeNull() {
        FormBuilder.create()
            .columns("pref")
            .rows("pref")
            .focusTraversalType(null)
            .build();
    }
    
    
    @Test
    public void acceptFocusTraversalTypes() {
        FormBuilder.create()
            .columns("pref")
            .rows("pref")
            .focusTraversalType(FocusTraversalType.LAYOUT)
            .build();
        FormBuilder.create()
            .columns("pref")
            .rows("pref")
            .focusTraversalType(FocusTraversalType.CONTAINER_ORDER)
            .build();
    }

    
    @Test
    public void customFocusTraversalPolicy() {
        FormBuilder.create()
            .columns("pref")
            .rows("pref")
            .focusTraversalPolicy(new ContainerOrderFocusTraversalPolicy())
            .build();
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void rejectSettingFocusTraversalPolicyTwice() {
        FormBuilder.create()
            .columns("pref")
            .rows("pref")
            .focusTraversalPolicy(new ContainerOrderFocusTraversalPolicy())
            .focusTraversalPolicy(new LayoutFocusTraversalPolicy())
            .build();
    }
    
    
}
