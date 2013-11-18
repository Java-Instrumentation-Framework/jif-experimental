/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binding.ObjectCompBinding;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PresentationModelExample extends JPanel {
    //....

    Action applyAction, resetAction;
    JTextField nameFld;
    JButton applyBtn, resetBtn;
    JCheckBox preferredCB;

    private void createComponents() {
        MyPresentationModel presentationModel =
                new MyPresentationModel(new CustomerBean());

        nameFld = BasicComponentFactory.createTextField(
                presentationModel.getBufferedModel(CustomerBean.NAME_PROPERTY));
        preferredCB = BasicComponentFactory.createCheckBox(
                presentationModel.getBufferedModel(CustomerBean.PREFERRED_PROPERTY), "Preferred");

        applyBtn = new JButton(presentationModel.getApplyAction());
        resetBtn = new JButton(presentationModel.getResetAction());
    }
    // ...
}
