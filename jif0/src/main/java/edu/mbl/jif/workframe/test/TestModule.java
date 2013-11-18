package edu.mbl.jif.workframe.test;

import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.workframe.AbstractModuleWithGui;

/**
 *
 * @author GBH
 */
public class TestModule
        extends AbstractModuleWithGui {

    /** Creates a new instance of TestModule */
    public TestModule(InstrumentController instrumentCtrl) {
        super(instrumentCtrl);
        model = new TestModel();
        presentation = new TestPresentation((TestModel) model);
        //controller; ?
        panel = new PanelTest(presentation);
    //actions;
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean startup() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
