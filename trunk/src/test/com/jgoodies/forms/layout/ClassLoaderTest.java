/*
 * Copyright (c) 2002-2008 JGoodies Karsten Lentzsch. All Rights Reserved.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.jgoodies.forms.util.FormUtils;

import junit.framework.TestCase;

/**
 * Tests working with the Forms with different class loaders.
 *
 * @author	Karsten Lentzsch
 * @version $Revision: 1.2 $
 */
public final class ClassLoaderTest extends TestCase{


    // Tests ******************************************************************

    public void testLayoutMapAccess()
        throws ClassNotFoundException, IllegalAccessException, SecurityException,
               IllegalArgumentException, NoSuchMethodException, InvocationTargetException, MalformedURLException {
        testStaticAccess(LayoutMap.class.getName(), "getRoot");
    }
    
    
    public void testLookAndFeelChangeHandlerRemainsRegistered() {
        int oldListenerCount = UIManager.getPropertyChangeListeners().length;
        FormUtils.isLafAqua();
        int newListenerCount = UIManager.getPropertyChangeListeners().length;
        assertEquals("After registration", oldListenerCount+1, newListenerCount);
        gcAndSleep();
        newListenerCount = UIManager.getPropertyChangeListeners().length;
        assertEquals("After GC", oldListenerCount+1, newListenerCount);
    }
    
    
    public void testWeakLookAndFeelChangeHandler() 
        throws SecurityException, IllegalArgumentException, ClassNotFoundException, 
               IllegalAccessException, NoSuchMethodException, InvocationTargetException, MalformedURLException {
        int oldListenerCount = UIManager.getPropertyChangeListeners().length;
        testStaticAccess(FormUtils.class.getName(), "isLafAqua");
        gcAndSleep();
        int newListenerCount = UIManager.getPropertyChangeListeners().length;
        assertEquals("UIManager listener count", oldListenerCount, newListenerCount);
    }


    // Helper Code ************************************************************

    private void gcAndSleep() {
        System.gc();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void setLookAndFeel(String lookAndFeelName) {
        try {
            UIManager.setLookAndFeel(lookAndFeelName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void testStaticAccess(String className, String methodName)
        throws ClassNotFoundException, IllegalAccessException, SecurityException,
               IllegalArgumentException, NoSuchMethodException, InvocationTargetException, MalformedURLException {
        Class classVersion1 = loadClass(className);
        staticAccess(classVersion1, methodName);
        Class classVersion2 = loadClass(className);
        staticAccess(classVersion2, methodName);
    }


    private Class loadClass(String className) throws ClassNotFoundException, MalformedURLException {
        URL url = getClass().getResource("/");
        url = new URL("file:/D:/workspaces/default/Forms/bin/");
        ClassLoader parent = ClassLoader.getSystemClassLoader().getParent();
        ClassLoader loader = new URLClassLoader(new URL[]{url}, parent);
        return loader.loadClass(className);
    }


    private void staticAccess(Class classVersion, String methodName)
        throws IllegalAccessException, SecurityException, NoSuchMethodException,
               IllegalArgumentException, InvocationTargetException {
        Method method = classVersion.getMethod(methodName, (Class[]) null);
        method.invoke(classVersion, (Object[])null);
    }


}
