/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jactivelist;

import java.awt.Component;

/**
 *
 * @author wert
 */
public interface JActiveListCellRenderer {

	Component getComponent(
							JActiveList list,
							Object value,
							int index);
}
