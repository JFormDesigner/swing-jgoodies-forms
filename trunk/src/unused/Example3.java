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

package com.jgoodies.forms.tutorial;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.extras.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Provides the details tab of the Details panel.
 *
 * @author Karsten Lentzsch
 */

public final class Example3 extends JPanel {

    private JTextField idField;
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField keywordsField;
    private JTextField builderClassNameField;
    private JTextField builderMethodNameField;
    private JTextField authorsField;
    private JTextField versionField;
    private JTextField releaseDateField;
    

    // Building *************************************************************

    public JComponent buildPanel() {
        build();
        return this;
    }
    
    
    /**
     *  Creates and configures the UI components.
     */
    private void initComponents() {
        idField = createReadOnlyTextFieldFor("id");
        nameField = createReadOnlyTextFieldFor("name");
        descriptionField = createReadOnlyTextFieldFor("description");
        keywordsField = createReadOnlyTextFieldFor("keywords");
        builderClassNameField = createReadOnlyTextFieldFor("builderClassName");
        builderMethodNameField = createReadOnlyTextFieldFor("builderMethodName");
        authorsField = createReadOnlyTextFieldFor("authors");
        versionField = createReadOnlyTextFieldFor("version");
        releaseDateField = createReadOnlyTextFieldFor("releaseDate");
    }
    
    /**
     * Builds the pane.
     */
    public void build() {
        initComponents();

        FormLayout layout = new FormLayout(
                "right:max(50dlu;pref), 4dlu, 50dlu, 150dlu",
                "");

        DefaultFormBuilder builder = new DefaultFormBuilder(this, layout);
        builder.setDefaultDialogBorder();
        
        builder.append("Name",           nameField, 2);
        builder.append("Description",    descriptionField, 2);
        builder.append("Keywords",       keywordsField, 2);
        builder.append("Form Id",        idField, 2);
        builder.append("Builder class",  builderClassNameField, 2);
        builder.append("Builder method", builderMethodNameField, 2);
        builder.append("Authors",        authorsField, 2);
        builder.append("Version",        versionField);
        builder.nextLine();
        builder.append("Release Date",   releaseDateField);
        builder.nextLine();
    }
    
    // Helper Code **********************************************************
    
    private JTextField createReadOnlyTextFieldFor(String propertyName) {
        JTextField textField = new JTextField();
        textField.setEditable(false);
//        textField.setDocument(
//            new DocumentAdapter(createPropertyAdapterFor(propertyName), true));
        return textField;
    }

}
