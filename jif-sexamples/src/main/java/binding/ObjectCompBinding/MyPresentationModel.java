package binding.ObjectCompBinding;

import com.jgoodies.binding.PresentationModel;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

public class MyPresentationModel extends PresentationModel {
    // ....
    ApplyAction applyAction;
    ResetAction resetAction;

    public MyPresentationModel(CustomerBean customerBean) {
        super(customerBean);
        applyAction = new ApplyAction();
        resetAction = new ResetAction();
    }

    public Action getApplyAction() {
        return applyAction;
    }

    public Action getResetAction() {
        return resetAction;
    }

    private class ApplyAction extends AbstractAction {

        public ApplyAction() {
            super("Apply");
        }

        public void actionPerformed(ActionEvent e) {
            triggerCommit();
        }
    }

    private class ResetAction extends AbstractAction {

        public ResetAction() {
            super("Reset");
        }

        public void actionPerformed(ActionEvent e) {
            triggerFlush();
        }
    }
}
