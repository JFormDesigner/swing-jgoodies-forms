/*
 * Copyright (c) 2003 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * This software is the proprietary information of JGoodies Karsten Lentzsch.
 * Use is subject to license terms.
 *
 */
 
package com.jgoodies.forms.factories;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.ButtonBarBuilder;

/**
 * A factory class that consists only of static methods to build frequently
 * used button bars. Utilizes the 
 * {@link com.jgoodies.forms.builder.ButtonBarBuilder} that in turn uses
 * the {@link com.jgoodies.forms.layout.FormLayout} to layout the bars.
 * <p>
 * The button bars answered by this build comply with popular UI style guides.
 *
 * @author Karsten Lentzsch
 */

public final class ButtonBarFactory {
    
    // Override default constructor; prevents instantiability.
    private ButtonBarFactory() {}

	
    // General Purpose Factory Methods: Left Aligned ************************

    /**
     * Builds and answers a left aligned bar with one button.
     * 
     * @param button1  the first button to add
     * @return a button bar with the given button
     */
    public static JPanel buildLeftAlignedBar(JButton button1) {
        return buildLeftAlignedBar(new JButton[]{
            button1
        });
    }
    
    /**
     * Builds and answers a left aligned bar with two buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildLeftAlignedBar(
            JButton button1, JButton button2) {
        return buildLeftAlignedBar(new JButton[]{
            button1, button2
        });
    }
    
    /**
     * Builds and answers a left aligned bar with three buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildLeftAlignedBar(
            JButton button1, JButton button2, JButton button3) {
        return buildLeftAlignedBar(new JButton[]{
            button1, button2, button3
        });
    }
    
    /**
     * Builds and answers a left aligned bar with four buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @param button4  the fourth button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildLeftAlignedBar(
            JButton button1, JButton button2, JButton button3, JButton button4) {
        return buildLeftAlignedBar(new JButton[]{
            button1, button2, button3, button4
        });
    }
    
    /**
     * Builds and answers a left aligned bar with five buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @param button4  the fourth button to add
     * @param button5  the fifth button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildLeftAlignedBar(
            JButton button1, JButton button2, JButton button3, 
            JButton button4, JButton button5) {
        return buildLeftAlignedBar(new JButton[]{
            button1, button2, button3, button4, button5
        });
    }
    
    /**
     * Builds and answers a left aligned button bar with the given buttons.
     * 
     * @param buttons  an array of buttons to add
     * @return a left aligned button bar with the given buttons
     */
    public static JPanel buildLeftAlignedBar(JButton[] buttons) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGriddedButtons(buttons);
        builder.addGlue();
        return builder.getPanel();
    }

    // General Purpose Factory Methods: Centered ****************************

    /**
     * Builds and answers a centered bar with one button.
     * 
     * @param button1  the first button to add
     * @return a button bar with the given button
     */
    public static JPanel buildCenteredBar(JButton button1) {
        return buildCenteredBar(new JButton[]{
            button1
        });
    }
    
    /**
     * Builds and answers a centered bar with two buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildCenteredBar(
            JButton button1, JButton button2) {
        return buildCenteredBar(new JButton[]{
            button1, button2
        });
    }
    
    /**
     * Builds and answers a centered bar with three buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildCenteredBar(
            JButton button1, JButton button2, JButton button3) {
        return buildCenteredBar(new JButton[]{
            button1, button2, button3
        });
    }
    
    /**
     * Builds and answers a centered bar with four buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @param button4  the fourth button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildCenteredBar(
            JButton button1, JButton button2, JButton button3, JButton button4) {
        return buildCenteredBar(new JButton[]{
            button1, button2, button3, button4
        });
    }
    
    /**
     * Builds and answers a centered bar with five buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @param button4  the fourth button to add
     * @param button5  the fifth button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildCenteredBar(
            JButton button1, JButton button2, JButton button3, 
            JButton button4, JButton button5) {
        return buildCenteredBar(new JButton[]{
            button1, button2, button3, button4, button5
        });
    }
    
    /**
     * Builds and answers a centered button bar with the given buttons.
     * 
     * @param buttons  an array of buttons to add
     * @return a centered button bar with the given buttons
     */
    public static JPanel buildCenteredBar(JButton[] buttons) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGriddedButtons(buttons);
        builder.addGlue();
        return builder.getPanel();
    }
    

    /**
     * Builds and answers a filled bar with one button.
     * 
     * @param button1  the first button to add
     * @return a button bar with the given button
     */
    public static JPanel buildGrowingBar(JButton button1) {
        return buildGrowingBar(new JButton[]{
            button1
        });
    }
    
    /**
     * Builds and answers a filled button bar with two buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildGrowingBar(
            JButton button1, JButton button2) {
        return buildGrowingBar(new JButton[]{
            button1, button2
        });
    }
    
    /**
     * Builds and answers a filled bar with three buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildGrowingBar(
            JButton button1, JButton button2, JButton button3) {
        return buildGrowingBar(new JButton[]{
            button1, button2, button3
        });
    }
    
    /**
     * Builds and answers a filled bar with four buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @param button4  the fourth button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildGrowingBar(
            JButton button1, JButton button2, JButton button3, JButton button4) {
        return buildGrowingBar(new JButton[]{
            button1, button2, button3, button4
        });
    }
    
    /**
     * Builds and answers a filled bar with five buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @param button4  the fourth button to add
     * @param button5  the fifth button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildGrowingBar(
            JButton button1, JButton button2, JButton button3, 
            JButton button4, JButton button5) {
        return buildGrowingBar(new JButton[]{
            button1, button2, button3, button4, button5
        });
    }
    
    /**
     * Builds and answers a button bar with the given buttons. All button
     * columns will grow with the bar.
     * 
     * @param buttons  an array of buttons to add
     * @return a filled button bar with the given buttons
     */
    public static JPanel buildGrowingBar(JButton[] buttons) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGriddedGrowingButtons(buttons);
        return builder.getPanel();
    }


    // General Purpose Factory Methods: Right Aligned ***********************

    /**
     * Builds and answers a right aligned bar with one button.
     * 
     * @param button1  the first button to add
     * @return a button bar with the given button
     */
    public static JPanel buildRightAlignedBar(JButton button1) {
        return buildRightAlignedBar(new JButton[]{
            button1
        });
    }
    
    /**
     * Builds and answers a right aligned bar with two buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildRightAlignedBar(
            JButton button1, JButton button2) {
        return buildRightAlignedBar(new JButton[]{
            button1, button2
        });
    }
    
    /**
     * Builds and answers a right aligned bar with three buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildRightAlignedBar(
            JButton button1, JButton button2, JButton button3) {
        return buildRightAlignedBar(new JButton[]{
            button1, button2, button3
        });
    }
    
    /**
     * Builds and answers a right aligned bar with four buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @param button4  the fourth button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildRightAlignedBar(
            JButton button1, JButton button2, JButton button3, JButton button4) {
        return buildRightAlignedBar(new JButton[]{
            button1, button2, button3, button4
        });
    }
    
    /**
     * Builds and answers a right aligned bar with five buttons.
     * 
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @param button4  the fourth button to add
     * @param button5  the fifth button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildRightAlignedBar(
            JButton button1, JButton button2, JButton button3, 
            JButton button4, JButton button5) {
        return buildRightAlignedBar(new JButton[]{
            button1, button2, button3, button4, button5
        });
    }
    
    /**
     * Builds and answers a right aligned button bar with the given buttons.
     * 
     * @param buttons  an array of buttons to add
     * @return a right aligned button bar with the given buttons
     */
    public static JPanel buildRightAlignedBar(JButton[] buttons) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGriddedButtons(buttons);
        return builder.getPanel();
    }
    

    // Right Aligned Button Bars with Help in the Left **********************    
    
    /**
     * Builds and answers a right aligned bar with help and one button.
     * 
     * @param help     the help button to add on the left side
     * @param button1  the first button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildHelpBar(JButton help, 
            JButton button1) {
        return buildHelpBar(help, new JButton[]{
            button1
        });
    }
    
    /**
     * Builds and answers a right aligned bar with help and two buttons.
     * 
     * @param help     the help button to add on the left side
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildHelpBar(JButton help,
            JButton button1, JButton button2) {
        return buildHelpBar(help, new JButton[]{
            button1, button2
        });
    }
    
    /**
     * Builds and answers a right aligned bar with help and three buttons.
     * 
     * @param help     the help button to add on the left side
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildHelpBar(JButton help,
            JButton button1, JButton button2, JButton button3) {
        return buildHelpBar(help, new JButton[]{
            button1, button2, button3
        });
    }
    
    /**
     * Builds and answers a right aligned bar with help and four buttons.
     * 
     * @param help     the help button to add on the left side
     * @param button1  the first button to add
     * @param button2  the second button to add
     * @param button3  the third button to add
     * @param button4  the fourth button to add
     * @return a button bar with the given buttons
     */
    public static JPanel buildHelpBar(JButton help,
            JButton button1, JButton button2, JButton button3, JButton button4) {
        return buildHelpBar(help, new JButton[]{
            button1, button2, button3, button4
        });
    }
    
    /**
     * Builds and answers a right aligned bar with help and other buttons.
     * 
     * @param help     the help button to add on the left side
     * @param buttons  an array of buttons to add
     * @return a right aligned button bar with the given buttons
     */
    public static JPanel buildHelpBar(JButton help, JButton[] buttons) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGridded(help);
        builder.addRelatedGap();
        builder.addGlue();
        builder.addGriddedButtons(buttons);
        return builder.getPanel();
    }


    // Popular Dialog Button Bars: No Help **********************************    
	
    /**
     * Builds and answers a button bar with Close.
     * 
     * @param close   	the Close button
     */
    public static JPanel buildCloseBar(JButton close) {
        return buildRightAlignedBar(close);
    }
    
	/**
	 * Builds and answers a button bar with OK.
     * 
     * @param ok   	the OK button
	 */
	public static JPanel buildOKBar(JButton ok) {
        return buildRightAlignedBar(ok);
    }
	
    /**
     * Builds and answers a button bar with OK and Cancel.
     * 
     * @param ok		the OK button
     * @param cancel	the Cancel button
     */
    public static JPanel buildOKCancelBar(
            JButton ok, JButton cancel) {
        return buildRightAlignedBar(ok, cancel);
    }
    
    /**
     * Builds and answers a button bar with OK, Cancel and Apply.
     * 
     * @param ok        the OK button
     * @param cancel    the Cancel button
     * @param apply	the Apply button
     */
    public static JPanel buildOKCancelApplyBar(
            JButton ok, JButton cancel, JButton apply) {
        return buildRightAlignedBar(ok, cancel, apply);
    }
    
    // Popular Dialog Button Bars: Help in the Left *************************    
    
    /**
     * Builds and answers a button bar with 
     * Help and Close.
     * 
     * @param help     the Help button
     * @param close    the Close button
     */
    public static JPanel buildHelpCloseBar(
            JButton help, JButton close) {
        return buildHelpBar(help, close);
    }
    
    /**
     * Builds and answers a button bar with 
     * Help and OK.
     * 
     * @param help     the Help button
     * @param ok	    the OK button
     */
    public static JPanel buildHelpOKBar(
            JButton help, JButton ok) {
        return buildHelpBar(help, ok);
    }
    
    /**
     * Builds and answers a button bar with 
     * Help, OK and Cancel.
     * 
     * @param help     the Help button
     * @param ok       the OK button
     * @param cancel	the Cancel button
     */
    public static JPanel buildHelpOKCancelBar(
            JButton help, JButton ok, JButton cancel) {
        return buildHelpBar(help, ok, cancel);
    }
    
    /**
     * Builds and answers a button bar with 
     * Help, OK, Cancel and Apply.
     * 
     * @param help     the Help button
     * @param ok       the OK button
     * @param cancel   the Cancel button
     * @param apply	the Apply button
     */
    public static JPanel buildHelpOKCancelApplyBar(
            JButton help, JButton ok, JButton cancel, JButton apply) {
        return buildHelpBar(help, ok, cancel, apply);
    }
    
    
    // Popular Dialog Button Bars: Help in the Right Hand Side **************    
    
    /**
     * Builds and answers a button bar with 
     * Close and Help.
     * 
     * @param close	the Close button
     * @param help     the Help button
     */
    public static JPanel buildCloseHelpBar(
            JButton close, JButton help) {
        return buildRightAlignedBar(close, help);
    }
    
    /**
     * Builds and answers a button bar with 
     * OK and Help.
     * 
     * @param ok		the OK button
     * @param help     the Help button
     */
    public static JPanel buildOKHelpBar(
            JButton ok, JButton help) {
        return buildRightAlignedBar(ok, help);
    }
    
    /**
     * Builds and answers a button bar with 
     * OK, Cancel, and Help.
     * 
     * @param ok       the OK button
     * @param cancel	the Cancel button
     * @param help     the Help button
     */
    public static JPanel buildOKCancelHelpBar(
            JButton ok, JButton cancel, JButton help) {
        return buildRightAlignedBar(ok, cancel, help);
    }
    
    /**
     * Builds and answers a button bar with 
     * OK, Cancel, Apply and Help.
     * 
     * @param ok       the OK button
     * @param cancel	the Cancel button
     * @param apply	the Apply button
     * @param help     the Help button
     */
    public static JPanel buildOKCancelApplyHelpBar(
            JButton ok, JButton cancel, JButton apply, JButton help) {
        return buildRightAlignedBar(ok, cancel, apply, help);
    }
    
    
    // Add..., Remove *******************************************************
        
    /**
     * Builds and answers a left aligned button bar with 
     * Add and Remove.
     * 
     * @param add		the Add button
     * @param remove	the Remove button
     */
    public static JPanel buildAddRemoveLeftBar(
            JButton add, JButton remove) {
        return buildLeftAlignedBar(add, remove);
    }
    
    /**
     * Builds and answers a filled button bar with Add and Remove.
     * 
     * @param add       the Add button
     * @param remove    the Remove button
     */
    public static JPanel buildAddRemoveBar(
            JButton add, JButton remove) {
        return buildGrowingBar(add, remove);
    }
    
    /**
     * Builds and answers a right aligned button bar with 
     * Add and Remove.
     * 
     * @param add       the Add button
     * @param remove    the Remove button
     */
    public static JPanel buildAddRemoveRightBar(
            JButton add, JButton remove) {
        return buildRightAlignedBar(add, remove);
    }
    

    // Add..., Remove, Properties... ****************************************
        
    /**
     * Builds and answers a left aligned button bar with 
     * Add, Remove, and Properties.
     * 
     * @param add       	the Add button
     * @param remove    	the Remove button
     * @param properties	the Properties button
     */
    public static JPanel buildAddRemovePropertiesLeftBar(
            JButton add, JButton remove, JButton properties) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGriddedButtons(new JButton[]{
            add, remove, properties
        });
        builder.addGlue();
        return builder.getPanel();
    }
    
    /**
     * Builds and answers a filled button bar with Add, Remove, and
     * Properties.
     * 
     * @param add           the Add button
     * @param remove        the Remove button
     * @param properties    the Properties button
     */
    public static JPanel buildAddRemovePropertiesBar(
            JButton add, JButton remove, JButton properties) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGriddedGrowing(add);
        builder.addRelatedGap();
        builder.addGriddedGrowing(remove);
        builder.addRelatedGap();
        builder.addGriddedGrowingNarrow(properties);
        return builder.getPanel();
    }
    
    /**
     * Builds and answers a right aligned button bar with 
     * Add, Remove, and Properties.
     * 
     * @param add           the Add button
     * @param remove        the Remove button
     * @param properties    the Properties button
     */
    public static JPanel buildAddRemovePropertiesRightBar(
            JButton add, JButton remove, JButton properties) {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addGriddedButtons(new JButton[]{
            add, remove, properties
        });
        return builder.getPanel();
    }
    
    // Wizard Bars **********************************************************
    
    /**
     * Builds and answers a wizard button bar with 
     * Back, Next, Finish, Cancel
     *
     * @param back		the Back button
     * @param next		the Next button
     * @param finish	the Finish button
     * @param cancel	the Cancel button
     * @return a wizard button bar for back, next, finish, cancel
     */
    public static JPanel buildWizardBar(
            JButton back, JButton next, JButton finish, JButton cancel) {
        return buildWizardBar(back, next, new JButton[]{finish, cancel});
    }
    
    /**
     * Builds and answers a wizard button bar with 
     * Help and Back, Next, Finish, Cancel
     * 
     * @param help		the Help button
     * @param back      the Back button
     * @param next      the Next button
     * @param finish    the Finish button
     * @param cancel    the Cancel button
     * @return a wizard button bar for help, back, next, finish, cancel
     */
    public static JPanel buildWizardBar(
            JButton help, JButton back, JButton next, JButton finish, JButton cancel) {
        return buildWizardBar(new JButton[]{help},
                               back, next, 
                               new JButton[]{finish, cancel});
    }
    
    /**
     * Builds and answers a wizard button bar that consists of the back and
     * next buttons, and some right aligned buttons.
     * 
     * @param back                the mandatory back button
     * @param next                the mandatory next button
     * @param rightAlignedButtons an optional array of buttons that will be
     * located in the bar's right hand side
     * @return a wizard button bar with back, next and a bunch of buttons
     */
    public static JPanel buildWizardBar(JButton back, JButton next,
                                         JButton[] rightAlignedButtons) {
        return buildWizardBar(null, back, next, rightAlignedButtons);
    }
    
    /**
     * Builds and answers a wizard button bar. It consists of some left
     * aligned buttons, the back and next buttons, and some right aligned
     * buttons. 
     * 
     * @param leftAlignedButtons  an optional array of buttons that will be
     * positioned in the bar's left hand side
     * @param back                the mandatory back button
     * @param next                the mandatory next button
     * @param rightAlignedButtons an optional array of buttons that will be
     * located in the bar's right hand side
     * @return a wizard button bar with back, next and a bunch of buttons
     */
    public static JPanel buildWizardBar(JButton[] leftAlignedButtons,
                                         JButton back, JButton next,
                                         JButton[] rightAlignedButtons) {
        return buildWizardBar(leftAlignedButtons, 
                               back, next, null, 
                               rightAlignedButtons);
    }
    
    /**
     * Builds and answers a wizard button bar. It consists of some left
     * aligned buttons, the back, next group, and some right aligned buttons.
     * To allow the finish button to overlay the next button, you can
     * optionally provide the <code>overlayedFinish</code> parameter.
     * 
     * @param leftAlignedButtons  an optional array of buttons that will be
     * positioned in the bar's left hand side
     * @param back                the mandatory back button
     * @param next                the mandatory next button
     * @param overlayedFinish     the optional overlayed finish button
     * @param rightAlignedButtons an optional array of buttons that will be
     * located in the bar's right hand side
     * @return a wizard button bar with back, next and a bunch of buttons
     */
    public static JPanel buildWizardBar(JButton[] leftAlignedButtons,
                                         JButton back, 
                                         JButton next, 
                                         JButton overlayedFinish,
                                         JButton[] rightAlignedButtons) {
                                            
        ButtonBarBuilder builder = new ButtonBarBuilder();
        if (leftAlignedButtons != null) {
            builder.addGriddedButtons(leftAlignedButtons);
            builder.addRelatedGap();
        } 
        builder.addGlue();
        builder.addGridded(back);
        builder.addGridded(next);
        
        // Optionally overlay the finish and next button.
        if (overlayedFinish != null) {
            builder.nextColumn(-1);
            builder.add(overlayedFinish);
            builder.nextColumn();
        }
        
        if (rightAlignedButtons != null) {
            builder.addRelatedGap();
            builder.addGriddedButtons(rightAlignedButtons);
        } 
        return builder.getPanel();
    }
    
	
}