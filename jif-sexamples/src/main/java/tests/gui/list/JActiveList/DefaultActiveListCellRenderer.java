/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jactivelist;

import java.awt.Component;
import javax.swing.JLabel;

/**
 * Default JActiveListCellRenderer implementation for <code>JAciveList</code>
 * @author wert
 */
public class DefaultActiveListCellRenderer implements JActiveListCellRenderer {

	@Override
	public Component getComponent(JActiveList list, Object value, int index) {
		return new JLabel(value.toString());
	}

	
	
}
