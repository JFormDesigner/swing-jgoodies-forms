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

import java.io.*;

import javax.swing.JPanel;

import junit.framework.TestCase;

/**
 * Serializes and deserializes FormLayout. 
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.1 $
 */

public final class FormLayoutSerializationTest extends TestCase {
    
    //private CellConstraints cc = new CellConstraints();


    /**
     */
    public void testSerializeBeforeLayout() {
        FormLayout layout = createLayout();
        OutputStream out = new ByteArrayOutputStream();
        serialize(out, layout);
    }
    
    /**
     */
    public void testSerializeAfterLayout() {
        FormLayout layout = createLayout();
        doLayout(layout);
        OutputStream out = new ByteArrayOutputStream();
        serialize(out, layout);
    }
    
    /**
     */
    public void testDeserializeBeforeLayout() {
        FormLayout layout = createLayout();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serialize(out, layout);
        byte[] bytes = out.toByteArray();
        InputStream in = new ByteArrayInputStream(bytes);
        deserialize(in);
    }
    
    
    /**
     */
    public void testDeserializeAfterLayout() {
        FormLayout layout = createLayout();
        doLayout(layout);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serialize(out, layout);
        byte[] bytes = out.toByteArray();
        InputStream in = new ByteArrayInputStream(bytes);
        deserialize(in);
    }
    

    /**
     */
    public void testLayoutSerializeDeserializeLayout() {
        FormLayout layout = createLayout();
        doLayout(layout);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serialize(out, layout);
        byte[] bytes = out.toByteArray();
        InputStream in = new ByteArrayInputStream(bytes);
        FormLayout layout2 = deserialize(in);
        doLayout(layout2);
    }
    

    // Helper Code *********************************************************
    
    private FormLayout createLayout() {
        return new FormLayout(
                "l:1px, c:2dlu, r:3mm, f:m, p, d, max(p;3dlu), min(p;7px)",
                "t:1px, c:2dlu, b:3mm, f:m, p, d, max(p;3dlu), min(p;7px)");
    }

    private FormLayout.LayoutInfo doLayout(FormLayout layout) {
        JPanel panel = new JPanel(layout);
        panel.doLayout();
        FormLayout.LayoutInfo info = layout.getLayoutInfo(panel);
        return info;
    }
    
    private void serialize(OutputStream out, FormLayout layout) {
        ObjectOutputStream objectOut = null;
        try {
            objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(layout);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IO Exception");
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private FormLayout deserialize(InputStream in) {
        ObjectInputStream objectIn = null;
        try {
            objectIn = new ObjectInputStream(in);
            return (FormLayout) objectIn.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            fail("IO Exception");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("Class not found");
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    
}