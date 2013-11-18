package edu.mbl.jif.workframe;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.Model;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.imaging.util.ImageFactoryGrayScale;
import edu.mbl.jif.workframe.test.PanelTest;
import edu.mbl.jif.workframe.test.TestModel;
import edu.mbl.jif.workframe.test.TestPresentation;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop.Action;
import java.awt.Image;
import java.util.EventObject;
import javax.swing.Icon;
import javax.swing.JPanel;

/**
 *
 * @author GBH
 */
public class AbstractModuleWithGui
        implements ModuleWithGui {

    protected String key = "test";
    protected final String name = "testModule";
    protected String iconFile = "icon.png";  // default module icon
    protected Model model;
    protected PresentationModel presentation;
    protected java.awt.Container panel;
    protected Object controller;
    protected Action[] actions;


    /** Creates a new instance of TestModule */
    public AbstractModuleWithGui(InstrumentController instrumentCtrl) {
//    model = new TestModel();
//    presentation = new TestPresentation((TestModel) model);
//    //controller; ?
//    panel = new PanelTest(instrumentCtrl);
        //actions;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Icon getIcon() {
        Icon icon = new javax.swing.ImageIcon(getClass().getResource(iconFile));
        if (icon == null) {
            icon = new javax.swing.ImageIcon((Image) ImageFactoryGrayScale.testImageByte(16,
                    16));
        }
        return icon;
    }

    @Override
    public Color getColor() {
       return Color.lightGray;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public java.awt.Container getPanel() {
        return panel;
    }

//      public SuuntoInterface.SunntoConfigurationPanel getConfigurationPanel() {
//        if (configurationPanel == null) {
//            configurationPanel = new SunntoConfigurationPanel();
//        }
//        return configurationPanel;
//    }
    
    @Override
    public Object getController() {
        return controller;
    }


    @Override
    public javax.swing.Action[] getActions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean startup() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public void initialize()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public void ready()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public void exit()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    @Override
    public void shutdown()
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    public boolean canExit(EventObject arg0)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

    public void willExit(EventObject arg0)
      {
        throw new UnsupportedOperationException("Not supported yet.");
      }

   @Override
   public Container getPreferencesPanel() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public String getCatagory() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
   

}
