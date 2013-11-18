package tests.gui.NBFormExt;

import java.beans.*;

public class BasePanelFormBeanInfo extends SimpleBeanInfo {
    private BeanDescriptor bd;
    public BeanDescriptor getBeanDescriptor() {
        if (bd == null) {
            bd = new BeanDescriptor(BasePanelForm.class);
            bd.setValue("containerDelegate", "getContentPane");
        }
        return bd;
    }
}
