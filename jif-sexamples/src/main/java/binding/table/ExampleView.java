package binding.table;
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

import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

/**
 * GUI that lists the users and provide actions to manage them.
 * 
 * @author Hugo Teixeira
 * @since September/2007
 */
public class ExampleView {

	private ExamplePresentationModel pm;

	public ExampleView(ExamplePresentationModel pm) {
		this.pm = pm;
		start();
	}

	private void start() {
		JFrame frame = new JFrame("JTable & Binding Example");
		pm.setParent(frame);
		frame.setSize(600, 400);
		frame.getContentPane().add(createViewComponent());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private Component createViewComponent() {
		JTable table = new JTable(pm.getTableModel());
		table.setSelectionModel(new SingleListSelectionAdapter(pm.getUserSelectionInList().getSelectionIndexHolder()));

		JPanel buttonBar = createButtonBar();

		FormLayout layout = new FormLayout("p:grow", "p, 3dlu, fill:50px:grow, 3dlu, p");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();

		JLabel titleLabel = new JLabel(" System Users");
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
		titleLabel.setBackground(new Color(227, 222, 198));
		titleLabel.setOpaque(true);

		builder.add(titleLabel, cc.xy(1, 1));
		builder.add(new JScrollPane(table), cc.xy(1, 3));
		builder.add(buttonBar, cc.xy(1, 5));
		return builder.getPanel();
	}

	private JPanel createButtonBar() {
		JButton addButton = new JButton(pm.getAddAction());
		JButton editButton = new JButton(pm.getEditAction());
		JButton removeButton = new JButton(pm.getRemoveAction());
		return ButtonBarFactory.buildLeftAlignedBar(addButton, editButton, removeButton);
	}

}
