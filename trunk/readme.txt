
                                README

                    JGoodies Forms(tm) Version 0.9.9
                        

CONTENTS

   * Introduction
   * Features
   * Distribution Contents
   * Prerequisites
   * Known Limitations
   * Support



INTRODUCTION

     The JGoodies Forms framework helps you layout and implement 
     Swing panels quickly, consistently and in a high quality. 
     It consists of a layout manager, builder classes that fill
     the panels and factories that speed up the UI construction.
     
     The Forms package ships with a demo application and tutorial; 
     both utilize, demonstrate and explain the Forms features. 
     You can download additional sample applications from 
     http://www.JGoodies.com/freeware/.
     
     JGoodies Forms is a library of JGoodies Karsten Lentzsch 
     and is protected by German and international copyright laws; 
     please refer to the license agreement for legal terms of use.



FEATURES
     
     The layout manager has been designed to be powerful, flexible,
     precise and easy to learn and understand. It can significantly
     reduce the time to describe a form and to fill it with components.
     The layout manager introduces a unique layout feature: 
     it honors the screen resolution and dialog font size to retain
     the layout proportions in different environments.
     
     Also, we have seperated the layout task from the panel construction.
     Therefore we provide a set of non-visual builders that assist you 
     in defining common panel layouts and in filling a form with components.
     The JGoodies Forms ships with general purpose builders and builders
     for specialized layout tasks. For example, the FormBuilder allows
     to build form oriented panels with one, two, three, or four columns.
     The ButtonBarBuilder specializes in building button bars.
     
     On top of these non-visual builders the JGoodies Forms provides
     factories that create the most frequently used layouts, panels,
     bars, and stacks. We recommend to use the factory methods
     whenever possible; future releases may map a logical panel
     creation to a concrete creation method that honors the platform
     and look-and-feel. For example the Mac vs. Windows button bar
     layout, where Mac has the default button in the right-hand side
     and Windows in the left-hand side.
     


DISTRIBUTION CONTENTS

     The JGoodies Forms package comes as a ZIP archive that contains:
       o docs/api          - JavaDocs directory
       o docs/forms.pdf    - a background article about the Forms
       
       o formsdemo-0.9.9   - Forms Demo directory
       
       o lib/forms.jar     - a signed JAR for the core classes
       
       o changes.txt       - the change history
       o license.pdf       - the license agreement in PDF
       o license.txt       - the license agreement as plain text
       o readme.txt        - this readme file
       o forms-src.zip     - library source code 
       o tutorial-src.zip  - tutorial sources
       o extras-src.zip    - additional sources *)
       
     *) The extras package contains sources for useful classes 
        that are work in progress and that may change without notice
        or may even be removed from future distributions.
       

       
PREREQUISITES

     The Forms framework requires Java 1.3 or later. 
     
     
     
KNOWN LIMITATIONS

     JBuilder and JDeveloper report a bad class file in 
     the Forms binary library jar. This seems to be a bug 
     in the compilers of the JBuilder and JDeveloper.
     Oracle's JDeveloper team already provides a patch 
     that will make it into the 9.0.4 release. 
     I haven't heard from Borland so far.
     
     I provide an older build that works around the problem, see
     http://www.jgoodies.com/download/commons/forms-20030421-temp.jar
     


SUPPORT 

     This products comes with time-limited mail and phone support; 
     don't hesitate to contact us if you have any question. 
     You may consider directing your questions to the unmoderated
     mailing list, so others can benefit from your solutions.
     
     You can find the mailing lists at
     http://groups.yahoo.com/group/JGoodies/             (low traffic)
     http://groups.yahoo.com/group/JGoodies_unmoderated/


------------------------------------------------------------------------
Copyright (c)¸ 2003 JGoodies Karsten Lentzsch, Wilhelmshavener Str. 25, 
24105 Kiel, Germany. All rights reserved.

Java and all Java-based trademarks and logos are trademarks or 
registered trademarks of Sun Microsystems, Inc. in the United States 
and other countries. 
Windows is a registered trademark of Microsoft Corporation in the
United States and other countries.
