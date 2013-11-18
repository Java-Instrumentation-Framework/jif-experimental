//**********************************************************************
// Package
//**********************************************************************

package freixas.jwizard;

//**********************************************************************
// Import list
//**********************************************************************

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class creates a Wizard dialog.
 * <p>
 * I wrote this class because I couldn't find a good, free,
 * easy-to-use, well-documented Java Wizard dialog. I hope this one is
 * both flexible and easy-to-use.
 * <p>
 * A wizard is a dialog that leads a user through a procedure
 * step-by-step. Some Wizards only gather information until the very
 * last step, when they execute some tasks. Others will perform
 * actions as they go.
 * <p>
 * To create a Wizard, you need two deal with two classes:
 * JWizardDialog (this class) and JWizardPanel. The basic steps are:
 * <ul>
 * <li>Create a JWizardDialog subclass.
 * <li>Set the Wizard dialog title (usually constant, though it
 * needn't be).
 * <li>Set the optional Image displayed on the left side of the
 * Wizard.
 * <li>Create a series of JWizardPanel's and add them to the dialog
 * using addWizardPanel(). The order in which the panels are added
 * determine their step number, with the first step starting at 0.
 * <li>Call pack() and call setVisible(true) to display it.
 * </ul>
 * <p>
 * Each JWizardPanel consists of a step title (which should be
 * specific to each step) and a set of components which you layout as
 * you wish. If possible, you should try to create and layout all
 * panel components at construction time (so that the Wizard size can
 * be accurately determined). If you need some dynamic layout, the
 * method makingVisible() is called each time prior to making the
 * JWizardPanel step visible.
 * <p>
 * The Wizard has four buttons which always appear, although they may
 * at times be disabled. The buttons are Back, Next, Finish and Cancel.
 * You can also add a Help button with addHelpButton().
 * <p>
 * When the buttons are pressed, they call the methods back(), next(),
 * finish(), cancel() and help(), respectively. You can override any
 * of these. The default finish() and cancel() methods call dispose();
 * when you override these, you would usually finish by calling
 * super.finish() or super.cancel().
 * <p>
 * The default back(), next() and help() methods call equivalent
 * methods in the current JWizardPanel. You would not normally
 * override these methods, but the hooks are there if you need them.
 * <p>
 * In the JWizardPanel, the default back() and next() methods usally
 * go to the previous or next panel. You can change the panel they go
 * to by calling setBackPanel() or setNextPanel(), giving the index of
 * the panel to go to. A value of -1 disables the corresponding
 * button. Again, you can override these and then finish by calling
 * super.back() or super.next(). The default help() method does
 * nothing.
 * <p>
 * The Finish button is normally enabled only on the last step. If you
 * allow an early exit from the Wizard, call setEarlyFinish() and the
 * Finish button will be enabled.
 * <p>
 * The Cancel button is normally always enabled. To disable it on the
 * last step, call disableCancelAtEnd().
 * <p>
 * Just prior to switching steps, switchToStep() is called in
 * JWizardDialog. This method supplies the current and new step
 * indices. The return value is the index of the panel which will
 * actually be displayed. The default method returns the input value,
 * but you can override this if you need special flow control in the
 * JWizardDialog rather than the JWizardPanel.
 * <p>
 * When the dialog is displayed, it is normally centered over its
 * parent. If there is no Frame parent, it is centered on the screen.
 * You can disable this with disableCentering(), which must be called
 * before the JWizardDialog is made visible.
 * <p>
 * That's about all there is to it.
 * <hr>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Artistic License. You should have
 * received a copy of the Artistic License along with this program. If
 * not, a copy is available at
 * <a href="http://opensource.org/licenses/artistic-license.php">
 * opensource.org</a>.
 *
 * @see JWizardPanel
 * @author Antonio Freixas
 */

// Copyright  2004 Antonio Freixas
// All Rights Reserved.

public class JWizardDialog
    extends JDialog
{

//**********************************************************************
// Private Constants
//**********************************************************************

private static final int BACK_BUTTON		= 0;
private static final int NEXT_BUTTON		= 1;
private static final int FINISH_BUTTON		= 2;
private static final int CANCEL_BUTTON		= 3;
private static final int HELP_BUTTON		= 4;

//**********************************************************************
// Private Members
//**********************************************************************

// The label which holds the image to display on the left side of the
// wizard.

private JPanel logoPanel;
private JLabel logoLabel;

// The panel to which JWizardPanel's are added.

private JPanel workArea;

// The layout for the work area.

private CardLayout cardLayout;

// The current JWizardPanel

private JWizardPanel currentWizard = null;
private int currentStep = -1;
private int lastStep = -1;
private int panelCount = 0;

// The button panel and buttons

private JPanel buttonPanel;
private JPanel buttons;
private JButton buttonBack;
private JButton buttonNext;
private JButton buttonFinish;
private JButton buttonCancel;
private JButton buttonHelp;

// True if the finish button should be enabled all the time

private boolean enableEarlyFinish = false;

// True if the cancel button is enabled on the final step

private boolean enableCancelAtEnd = true;

// True if the dialog should be centered on display

private boolean isCentered = true;

// True if the wizard finished

private boolean isFinished = false;

// The resource bundle

private static ResourceBundle bundle =
    ResourceBundle.getBundle("org.freixas.jwizard.Bundle");

//**********************************************************************
// Constructors
//**********************************************************************

/**
 * Creates a non-modal JWizardDialog without a title and without a
 * specified Frame owner. A shared, hidden frame will be set as the
 * owner of the dialog.
 * <p>
 * This constructor sets the component's locale property to the value
 * returned by JComponent.getDefaultLocale().
 *
 * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
 */

public
JWizardDialog()
    throws HeadlessException
{
    super();
    init();
}

/**
 * Creates a non-modal JWizardDialog without a title with the
 * specified Frame as its owner. If owner  is null, a shared, hidden
 * frame will be set as the owner of the dialog.
 * <p>
 * This constructor sets the component's locale property to the value
 * returned by JComponent.getDefaultLocale().
 *
 * @param owner The Frame owning the dialog.
 * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
 */

public
JWizardDialog(
    Frame owner)
	throws HeadlessException
{
    super(owner);
    init();
}

/**
 * Creates a modal or non-modal JWizardDialog without a title and with
 * the specified owner Frame. If owner  is null, a shared, hidden
 * frame will be set as the owner of the dialog.
 *
 * This constructor sets the component's locale property to the value
 * returned by JComponent.getDefaultLocale().
 *
 * @param owner The Frame owning the dialog.
 * @param modal True for a modal dialog, false for one that allows
 * 	others windows to be active at the same time.
 * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
 */

public
JWizardDialog(
    Frame owner,
    boolean modal)
    throws HeadlessException
{
    super(owner, modal);
    init();
}

/**
 * Creates a non-modal JWizardDialog with the specified title and with
 * the specified owner frame. If owner  is null, a shared, hidden
 * frame will be set as the owner of the dialog.
 * <p>
 * This constructor sets the component's locale property to the value
 * returned by JComponent.getDefaultLocale().
 *
 * @param owner The Frame owning the dialog.
 * @param title The String to display in the dialog's title bar.
 * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
 */

public
JWizardDialog(
    Frame owner,
    String title)
    throws HeadlessException
{
    super(owner, title);
    init();
}

/**
 * Creates a modal or non-modal JWizardDialog with the specified title
 * and the specified owner Frame. If owner  is null, a shared, hidden
 * frame will be set as the owner of this dialog. All constructors
 * defer to this one.
 * <p>
 * NOTE: Any popup components (JComboBox, JPopupMenu, JMenuBar)
 * created within a modal dialog will be forced to be lightweight.
 * <p>
 * This constructor sets the component's locale property to the value
 * returned by JComponent.getDefaultLocale().
 *
 * @param owner The Frame owning the dialog.
 * @param title The String to display in the dialog's title bar.
 * @param modal True for a modal dialog, false for one that allows
 * 	others windows to be active at the same time.
 * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
 */

public
JWizardDialog(
    Frame owner,
    String title,
    boolean modal)
	throws HeadlessException
{
    super(owner, title, modal);
    init();
}

/**
 * Creates a modal or non-modal JWizardDialog with the specified
 * title, owner Frame, and GraphicsConfiguration.
 * <p>
 * NOTE: Any popup components (JComboBox, JPopupMenu, JMenuBar)
 * created within a modal dialog will be forced to be lightweight.
 * <p>
 * This constructor sets the component's locale property to the value
 * returned by JComponent.getDefaultLocale.
 *
 * @param owner The Frame owning the dialog.
 * @param title The String to display in the dialog's title bar.
 * @param modal True for a modal dialog, false for one that allows
 * 	others windows to be active at the same time.
 * @param gc The GraphicsConfiguration of the target screen device. If
 * 	gc is null, the same GraphicsConfiguration as the owning Frame
 * 	is used.
 * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
 */

public
JWizardDialog(
    Frame owner,
    String title,
    boolean modal,
    GraphicsConfiguration gc)
	throws HeadlessException
{
    super(owner, title, modal, gc);
    init();
}

/**
 * Creates a non-modal JWizardDialog without a title with the
 * specified Dialog as its owner. If owner  is null, a shared, hidden
 * frame will be set as the owner of the dialog.
 * <p>
 * This constructor sets the component's locale property to the value
 * returned by JComponent.getDefaultLocale().
 *
 * @param owner The Dialog owning the dialog.
 * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
 */

public
JWizardDialog(
    Dialog owner)
	throws HeadlessException
{
    super(owner);
    init();
}

/**
 * Creates a modal or non-modal JWizardDialog without a title and with
 * the specified owner Dialog. If owner  is null, a shared, hidden
 * frame will be set as the owner of the dialog.
 *
 * This constructor sets the component's locale property to the value
 * returned by JComponent.getDefaultLocale().
 *
 * @param owner The Dialog owning the dialog.
 * @param modal True for a modal dialog, false for one that allows
 * 	others windows to be active at the same time.
 * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
 */

public
JWizardDialog(
    Dialog owner,
    boolean modal)
    throws HeadlessException
{
    super(owner, modal);
    init();
}

/**
 * Creates a non-modal JWizardDialog with the specified title and with
 * the specified owner frame. If owner  is null, a shared, hidden
 * frame will be set as the owner of the dialog.
 * <p>
 * This constructor sets the component's locale property to the value
 * returned by JComponent.getDefaultLocale().
 *
 * @param owner The Dialog owning the dialog.
 * @param title The String to display in the dialog's title bar.
 * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
 */

public
JWizardDialog(
    Dialog owner,
    String title)
    throws HeadlessException
{
    super(owner, title);
    init();
}

/**
 * Creates a modal or non-modal JWizardDialog with the specified title
 * and the specified owner Dialog. If owner  is null, a shared, hidden
 * frame will be set as the owner of this dialog. All constructors
 * defer to this one.
 * <p>
 * NOTE: Any popup components (JComboBox, JPopupMenu, JMenuBar)
 * created within a modal dialog will be forced to be lightweight.
 * <p>
 * This constructor sets the component's locale property to the value
 * returned by JComponent.getDefaultLocale().
 *
 * @param owner The Dialog owning the dialog.
 * @param title The String to display in the dialog's title bar.
 * @param modal True for a modal dialog, false for one that allows
 * 	others windows to be active at the same time.
 * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
 */

public
JWizardDialog(
    Dialog owner,
    String title,
    boolean modal)
	throws HeadlessException
{
    super(owner, title, modal);
    init();
}

/**
 * Creates a modal or non-modal JWizardDialog with the specified
 * title, owner Dialog, and GraphicsConfiguration.
 * <p>
 * NOTE: Any popup components (JComboBox, JPopupMenu, JMenuBar)
 * created within a modal dialog will be forced to be lightweight.
 * <p>
 * This constructor sets the component's locale property to the value
 * returned by JComponent.getDefaultLocale.
 *
 * @param owner The Dialog owning the dialog.
 * @param title The String to display in the dialog's title bar.
 * @param modal True for a modal dialog, false for one that allows
 * 	others windows to be active at the same time.
 * @param gc The GraphicsConfiguration of the target screen device. If
 * 	gc is null, the same GraphicsConfiguration as the owning Dialog
 * 	is used.
 * @throws HeadlessException If GraphicsEnvironment.isHeadless() returns true.
 */

public
JWizardDialog(
    Dialog owner,
    String title,
    boolean modal,
    GraphicsConfiguration gc)
	throws HeadlessException
{
    super(owner, title, modal, gc);
    init();
}

//**********************************************************************
// Public
//**********************************************************************

/**
 * Add an image which displays on the left side of the wizard. By
 * default, no image is displayed. This must be set before the dialog
 * is made visible.
 *
 * @param icon The icon representing the image to display. If null, no
 * 	image is displayed.
 */

public void
setWizardIcon(
    Icon icon)
{
    // If null, remove any existing logo panel

    if (icon == null) {
	if (logoPanel != null) {
	    remove(logoPanel);
	    logoPanel = null;
	    logoLabel = null;
	}
    }

    // If not null, add it or replace an existing label

    else {
	if (logoPanel != null) {
	    remove(logoPanel);
	}
	logoPanel = new JPanel(new BorderLayout());
	logoLabel = new JLabel(icon);
	logoPanel.add(logoLabel, BorderLayout.NORTH);
	getContentPane().add(logoPanel, BorderLayout.WEST);
    }
}

/**
 * Add a panel representing a step in the wizard. Since removing a
 * panel would force a renumbering of the remaining panels and since
 * you have flexible sequencing control, there is no matching
 * removeWizardPanel() method.
 *
 * @param panel The JWizardPanel to add
 */

public void
addWizardPanel(
    JWizardPanel panel)
{
    if (currentWizard == null) {
	currentWizard = panel;
	currentStep = 0;
    }
    workArea.add(panel, Integer.toString(panelCount++));
    panel.setWizardParent(this);
}

/**
 * This adds a help button to the wizard. When the button is pressed,
 * the help() method is called.
 *
 * @see #help()
 */

public void
addHelpButton()
{
    if (buttonHelp == null) {
	buttonHelp =
	    new JButton(
		new ButtonAction(
		    "HelpButton", "HelpButtonMnemonic",
		    "HelpButtonAccelerator",
		    "images/Help16.gif",
		    "HelpButtonShort", "HelpButtonLong", HELP_BUTTON));
	buttons.add(buttonHelp);
    }
}

/**
 * If this method is called, the Finish button is enabled immediately.
 * By default, it is enabled only on the last step (any step where the
 * next JWizardPanel step is -1).
 */

public void
setEarlyFinish()
{
    enableEarlyFinish = true;
}

/**
 * Returns true if the wizard finished (the user pressed the Finish)
 * button). Returns false otherwise (either the wizard hasn't finished
 * or the user pressed Cancel to exit).
 *
 * @return True if the wizard finished.
 */

public boolean
isFinished()
{
    return isFinished;
}

/**
 * If this method is called, the Cancel button is disabled when on the
 * last step. If setEarlyFinish() is called, it is still disabled only
 * on the last step.
 */

public void
disableCancelAtEnd()
{
    enableCancelAtEnd = false;
}

/**
 * Don't center the dialog. This method must be called before the
 * dialog is made visible. The default behavior is to center the
 * dialog over its parent, or on the screen if no parent was given.
 */

public void
disableCentering()
{
    isCentered = false;
}

/**
 * Returns the current step being displayed by the wizard. Steps start
 * at 0. If no step is yet displayed, a -1 is returned.
 *
 * @return The current step being displayed by the wizard.
 */

public int
getCurrentStep()
{
    return currentStep;
}

/**
 * Returns the last step displayed by the wizard. Steps start at 0. If
 * there is no previous step yet, -1 is returned.
 *
 * @return The last step being displayed by the wizard.
 */

public int
getLastStep()
{
    return lastStep;
}

/**
 * @deprecated As of JDK version 1.1, replaced by setVisible(boolean).
 */

public void
show()
{
    goTo(0);

    if (isCentered) {
	Dimension screenSize = getToolkit().getScreenSize();
	Dimension parentSize = getParent().getSize();
	Point parentLocation = getParent().getLocation();

	// If the parent Frame is invisible, we center the dialog on
	// the screen

	if (!getParent().isVisible()) {
	    parentSize = getToolkit().getScreenSize();
	    parentLocation.setLocation(0, 0);
	}

	Dimension size = getSize();

	int x = parentLocation.x + (parentSize.width  - size.width ) / 2;
	int y = parentLocation.y + (parentSize.height - size.height) / 2;

	// Make sure the dialog is placed completely on the screen (as
	// long as it is smaller than the screen size)

	if (size.width < screenSize.width &&
	    x + size.width > screenSize.width) {
	    x = screenSize.width - size.width;
	}
	if (size.height < screenSize.height &&
	    y + size.height > screenSize.height) {
	    y = screenSize.height - size.height;
	}
	if (x < 0) x = 0;
	if (y < 0) y = 0;

	setBounds(x, y, size.width, size.height);
    }

    super.show();
}

// WizardDialogs are equal if they are the same object, so the default
// equals() and hashCode() methods are acceptable. I'm not using the
// paramString() method for debugging, so the default is OK

//**********************************************************************
// Package Public
//**********************************************************************

/**
 * Set the sensitivity of each button based on the back and next step
 * values. This should be called when changing steps or when the back
 * or next button values are changed.
 */

void
setButtonStates()
{
    int backStep = currentWizard.getBackStep();
    int nextStep = currentWizard.getNextStep();

    boolean atBegin = backStep < 0 || backStep >= panelCount;

    boolean atEnd = nextStep < 0 || nextStep >= panelCount;

    buttonBack.setEnabled(!atBegin);
    buttonNext.setEnabled(!atEnd);
    buttonFinish.setEnabled(enableEarlyFinish || atEnd);
    buttonCancel.setEnabled(!atEnd || enableCancelAtEnd);

    // Set the default button

    if (buttonNext.isEnabled()) {
	getRootPane().setDefaultButton(buttonNext);
    }
    else if (buttonFinish.isEnabled()) {
	getRootPane().setDefaultButton(buttonFinish);
    }
    else if (buttonBack.isEnabled()) {
	getRootPane().setDefaultButton(buttonBack);
    }
    else {
	getRootPane().setDefaultButton(null);
    }
}

/**
 * Display the JWizardPanel with the given step number. This method is
 * package public so that JWizardPanel can call it. The
 * switchToStep() method may override the step choice.
 * </code></pre>
 *
 * @param step The step number of the JWizardPanel to display.
 * @see #switchToStep(int,int)
 */

void
goTo(
    int step)
{
    // Give the user a last chance to change things

    step = switchToStep(currentStep, step);

    // We can't do anything if we're outside the valid range

    if (step < 0 || step >= panelCount) return;

    // Save the current step as the previous step

    lastStep = currentStep;

    currentWizard = (JWizardPanel)workArea.getComponent(step);
    currentStep = step;
    currentWizard.doMakingVisible();
    cardLayout.show(workArea, Integer.toString(step));

    // The panel may have just been created or modified in the
    // doMakingVisible() method. If so, the CardLayout's original
    // guess at the window size may be too small. We increase the size
    // if we have to -- but we never decrease the size

    Dimension prefSize = getPreferredSize();
    Dimension curSize = getSize();
    if (prefSize.width > curSize.width ||
	prefSize.height > curSize.height) {
	Dimension newSize =
	    new Dimension(Math.max(prefSize.width, curSize.width),
			  Math.max(prefSize.height, curSize.height));
	setSize(newSize);
	invalidate();
	validate();
    }

    // Set the button states

    setButtonStates();
}

//**********************************************************************
// Protected
//**********************************************************************

/**
 * Called when the Back button is pressed. This calls the back()
 * method in the current JWizardPanel.
 *
 * @see JWizardPanel#back()
 */

protected void
back()
{
    if (currentWizard != null) currentWizard.doBack();
}

/**
 * Called when the Next button is pressed. This calls the next()
 * method in the current JWizardPanel.
 *
 * @see JWizardPanel#next()
 */

protected void
next()
{
    if (currentWizard != null) currentWizard.doNext();
}

/**
 * Called when the Finish button is pressed. This calls dispose(). You
 * will probably want to override this.
 */

protected void
finish()
{
    isFinished = true;
    dispose();
}

/**
 * Called when the Cancel button is pressed. This calls dispose().
 */

protected void
cancel()
{
    dispose();
}

/**
 * Called when the Help button is pressed. This calls the help()
 * method in the current JWizardPanel. If the help text is the same
 * for all panels, you will want to override this.
 */

protected void
help()
{
    if (currentWizard != null) currentWizard.doHelp();
}

/**
 * This method is called just prior to switching from one step to
 * another (after any next() or back() method is called). It receives
 * the current and new indices. By default, it returns the new index.
 * You can override the method if you need to control sequencing from
 * this JWizardDialog class (normally, each step decides what the back
 * and next steps should be).
 *
 * @param currentIndex The index of the current JWizardPanel.
 * @param newIndex The index of the JWizardPanel we are about to
 * 	display.
 * @return The index of the JWizardPanel to display.
 */

protected int
switchToStep(
    int currentIndex,
    int newIndex)
{
    return newIndex;
}

//**********************************************************************
// Private
//**********************************************************************

/**
 * Initialize the JWizardDialog.
 */

private void
init()
{
    getContentPane().setLayout(new BorderLayout(5, 2));

    // If the user tries to close the wizard, the result should be the
    // same as pressing Cancel

    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

    // Window close is the same as cancel. If the cancel button is
    // disabled, then a window close does nothing

    addWindowListener(
	new WindowAdapter() {
	public void windowClosing(WindowEvent e) {
	    if (buttonCancel.isEnabled()) {
		cancel();
	    }
	}
	});

    // Work area for WizardPanel's

    workArea = new JPanel();
    cardLayout = new CardLayout();
    workArea.setLayout(cardLayout);

    // Buttons

    buttonBack =
	new JButton(
	    new ButtonAction(
		"BackButton", "BackButtonMnemonic", "BackButtonAccelerator",
		"images/Back16.gif",
		"BackButtonShort", "BackButtonLong", BACK_BUTTON));
    buttonNext =
	new JButton(
	    new ButtonAction(
		"NextButton", "NextButtonMnemonic", "NextButtonAccelerator",
		"images/Forward16.gif",
		"NextButtonShort", "NextButtonLong", NEXT_BUTTON));
    buttonFinish =
	new JButton(
	    new ButtonAction(
		"FinishButton", "FinishButtonMnemonic",
		"FinishButtonAccelerator",
		null,
		"FinishButtonShort", "FinishButtonLong", FINISH_BUTTON));
    buttonCancel =
	new JButton(
	    new ButtonAction(
		"CancelButton", "CancelButtonMnemonic",
		"CancelButtonAccelerator",
		null,
		"CancelButtonShort", "CancelButtonLong", CANCEL_BUTTON));

    buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    buttons.add(buttonBack);
    buttons.add(buttonNext);
    buttons.add(buttonFinish);
    buttons.add(buttonCancel);

    buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.add(new JSeparator(), BorderLayout.NORTH);
    buttonPanel.add(buttons);

    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    getContentPane().add(workArea);
}

//**********************************************************************
// Inner Classes
//**********************************************************************

// ButtonAction

private class ButtonAction
      extends AbstractAction
{

ButtonAction(
    String name,
    String mnemonic,
    String accelerator,
    String imageName,
    String shortDescription,
    String longDescription,
    int actionId)
{
    if (name != null) {
	putValue(Action.NAME, bundle.getString(name));
    }

    if (mnemonic != null) {
	putValue(Action.MNEMONIC_KEY,
		 new Integer(bundle.getString(mnemonic).charAt(0)));
    }

    if (accelerator != null) {
	putValue(
	    Action.ACCELERATOR_KEY,
	    KeyStroke.getKeyStroke(bundle.getString(accelerator)));
    }

    URL url = null;
    if (imageName != null) {
	url =
	    this.getClass().getResource(imageName);
	if (url != null) {
	    putValue(Action.SMALL_ICON, new ImageIcon(url));
	}
    }

    if (shortDescription != null) {
	putValue(Action.SHORT_DESCRIPTION, bundle.getString(shortDescription));
    }

    if (longDescription != null) {
	putValue(Action.LONG_DESCRIPTION, bundle.getString(longDescription));
    }
    putValue("buttonAction", new Integer(actionId));
}

public void
actionPerformed(
    ActionEvent e)
{
    Integer value = (Integer)getValue("buttonAction");
    switch (value.intValue()) {
    case BACK_BUTTON:
	back();
	break;
    case NEXT_BUTTON:
	next();
	break;
    case FINISH_BUTTON:
	finish();
	break;
    case CANCEL_BUTTON:
	cancel();
	break;
    case HELP_BUTTON:
	help();
	break;
    }
}

}

//**********************************************************************
// End Inner Classes
//**********************************************************************

}
