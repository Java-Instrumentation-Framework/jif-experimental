/*
 * Copyright (c) 2007 Component House (Hugo Vidal Teixeira). All Rights Reserved.
 * Visit: http://www.componenthouse.com
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
 *  o Neither the name of Component House (Hugo Vidal Teixeira) nor the names of
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
package binding.table;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.WindowConstants;

/**
 * GUI Editor to edit a user object.
 *
 * @author Hugo Teixeira
 * @since September/2007
 */
public class Editor {

	public static final byte BUTTON_OK = 1;
	public static final byte BUTTON_CANCEL = 2;
	private byte buttonPressed;

	private JDialog dialog;
	private User originalUser;
	private User modifiedUser;
	private BeanAdapter<User> userBeanAdapter;

	public Editor(Frame owner, User user) {
		buttonPressed = 0;
		originalUser = user;
		modifiedUser = new User(); // Create a copy
		originalUser.copyTo(modifiedUser);
		userBeanAdapter = new BeanAdapter<User>(modifiedUser,  true);
		start(owner);
	}

	public byte getButtonPressed() { return buttonPressed; }

	private void start(Frame owner) {
		dialog = new JDialog(owner, true /* modal */);
		dialog.setTitle("User Editor");
		dialog.getContentPane().add(createViewComponent());
		dialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private Component createViewComponent() {
		FormLayout layout = new FormLayout("p, 3dlu, 200px:grow", "p, 3dlu, p, 3dlu, p, 5dlu, p");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.add(new JLabel("Name:"), cc.xy(1, 1));
		builder.add(BasicComponentFactory.createTextField(userBeanAdapter.getValueModel(User.PROPERTY_NAME)), cc.xy(3, 1));
		builder.add(new JLabel("E-mail:"), cc.xy(1, 3));
		builder.add(BasicComponentFactory.createTextField(userBeanAdapter.getValueModel(User.PROPERTY_EMAIL)), cc.xy(3, 3));
		builder.add(new JLabel("Weight:"), cc.xy(1, 5));
		builder.add(BasicComponentFactory.createIntegerField(userBeanAdapter.getValueModel(User.PROPERTY_WEIGHT)), cc.xy(3, 5));		
		builder.add(getButtonBar(), cc.xyw(1, 7, 3));
		return builder.getPanel();
	}

	private Component getButtonBar() {
		JButton okButton = new JButton(new OKAction());
		JButton cancelButton = new JButton(new CancelAction());		
		return ButtonBarFactory.buildCenteredBar(okButton, cancelButton);
	}

	private class OKAction extends AbstractAction {
		public OKAction() { super("OK"); }

		public void actionPerformed(ActionEvent e) {
			modifiedUser.copyTo(originalUser);
			buttonPressed = BUTTON_OK;
			dialog.setVisible(false);
		}
	}

	private class CancelAction extends AbstractAction {
		public CancelAction() { super("Cancel"); }

		public void actionPerformed(ActionEvent e) {
			buttonPressed = BUTTON_CANCEL;
			dialog.setVisible(false);
		}
	}

}
