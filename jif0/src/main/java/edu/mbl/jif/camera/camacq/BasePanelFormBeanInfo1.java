package edu.mbl.jif.camera.camacq;

import java.beans.*;

public class BasePanelFormBeanInfo1 extends SimpleBeanInfo {
    private BeanDescriptor bd;
    public BeanDescriptor getBeanDescriptor() {
        if (bd == null) {
            bd = new BeanDescriptor(BasePanelForm.class);
            bd.setValue("containerDelegate", "getContentPane");
        }
        return bd;
    }
}
