 /*
 * ValueHolderExample.java
 *
 * Created on December 14, 2006, 4:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package binding;

/*
Code revised from Desktop Java Live:
http://www.sourcebeat.com/downloads/
*/
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class ValueHolderExample extends JPanel {
    private ValueHolder valueHolder;

    public ValueHolderExample() {
        DefaultFormBuilder defaultFormBuilder = new DefaultFormBuilder(new FormLayout("p, 2dlu, p:g"));
        defaultFormBuilder.setDefaultDialogBorder();

        this.valueHolder = new ValueHolder("Text Value");
        JTextField textField = BasicComponentFactory.createTextField(valueHolder);
        valueHolder.setValue("Something else");
        defaultFormBuilder.append("TextField: ", textField);
        defaultFormBuilder.append(new JButton(new ShowValueHolderValueAction()), 3);

        add(defaultFormBuilder.getPanel());
    }

    private class ShowValueHolderValueAction extends AbstractAction {
        public ShowValueHolderValueAction() {
            super("Show Value");
        }

        public void actionPerformed(ActionEvent event) {
            StringBuffer message = new StringBuffer();
            message.append("<html>");
            message.append("<b>Value:</b> ");
            message.append(valueHolder.getValue());
            message.append("</html>");

            JOptionPane.showMessageDialog(null, message.toString());
        }
    }


    public static void main(String[] a){
      JFrame f = new JFrame("ValueHolder Example");
      f.setDefaultCloseOperation(2);
      f.add(new ValueHolderExample());
      f.pack();
      f.setVisible(true);
    }
}
