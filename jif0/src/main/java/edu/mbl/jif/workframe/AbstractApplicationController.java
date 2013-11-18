package edu.mbl.jif.workframe;

import com.jgoodies.binding.value.ValueModel;
import edu.mbl.jif.gui.action.DependentAction;
import edu.mbl.jif.gui.action.DependentActionToggle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.action.ActionManager;

/**
 *
 * @author GBH
 */
public class AbstractApplicationController
        implements ApplicationController {

   public Map<String, IModule> modules = new HashMap<String, IModule>();
   public Map models = new HashMap();
   //public Map presentations = new HashMap();
   public Map controllers = new HashMap();
   public ActionManager actionMgr;
   public List actions = new ArrayList(); // Actions to be added to the ToolBar

   public AbstractApplicationController() {
   }

   /*
    * Sequence of starting-up
    * Each module is initialise()'d as it is loaded.
    * Then, startup() is invoked on all of the modules,
    * any interdependencies to be resolved...
    */
   @Override
   public void initialize() {
      Iterator iterator = modules.keySet().iterator();
      while (iterator.hasNext()) {
         ((IModule) iterator.next()).initialize();
      }
   }

   @Override
   public void startup() {
      Iterator iterator = modules.keySet().iterator();
      while (iterator.hasNext()) {
         ((IModule) iterator.next()).startup();
      }
   }

   // <editor-fold desc=">>> jif.workframe <<<" defaultstate="collapsed">
   // This is moved to ModuleManager
   public void addModule(IModule module) {
      String key = module.getKey();
      modules.put(key, module);
      String name = module.getName();
      modules.put(name, module);
      if (module.getModel() != null) {
         models.put(name, module.getModel());
      } else {
         System.err.println("No Presentation for " + key);
      }
//        if (module.getPresentation() != null) {
//            presentations.put(name, module.getPresentation());
//        } else {
//            System.err.println("No Presentation for " + key);
//        }
      if (module.getController() != null) {
         controllers.put(name, module.getController());
      }
      if (module instanceof ModuleWithGui) {
         if (((ModuleWithGui) module).getPanel() != null) {
            controllers.put(name, ((ModuleWithGui) module).getPanel());
         }
      }
      // module.initialize();
   }

   public IModule getModule(String key) {
      return modules.get(key);
   }

   public Map<String, IModule> getModules() {
      return modules;
   }

   public void listModules() {
      Set set = modules.entrySet();
      Iterator i = set.iterator();
      while (i.hasNext()) {
         Map.Entry me = (Map.Entry) i.next();
         System.out.println(me.getKey() + " : " + me.getValue());
      }
   }

   public String getName(String key) {
      return ((IModule) models.get(key)).getName();
   }

   public Object getModel(String model) {
      return models.get(model);
   }

//    public Object getPresentation(String present) {
//        return presentations.get(present);
//    }
   public Object getController(String ctrl) {
      return controllers.get(ctrl);
   }
   // </editor-fold>  
   // <editor-fold desc=">>> Actions <<<" defaultstate="collapsed">

   public void addAction(AbstractAction action) {
      ActionManager manager = ActionManager.getInstance();
      manager.addAction(action);
      actions.add(action.getValue(Action.ACTION_COMMAND_KEY));
   }

   public AbstractActionExt getManagedAction(String actionName) {
      return ((AbstractActionExt) ActionManager.getInstance().getAction(actionName));
   }

   public void addDependentAction(String name, String commandKey,
           String iconFile, Object handler,
           String method, int key,
           ValueModel depend) {
      ActionManager manager = ActionManager.getInstance();
      DependentAction dAction = new DependentAction(name, commandKey,
              new ImageIcon(getClass().getResource(iconFile)), handler, method);
      dAction.setMnemonic(new Integer(key));
      manager.addAction(dAction);
      actions.add(dAction.getValue(Action.ACTION_COMMAND_KEY));
      //e.g. manager.getBoundAction("acqImage").setEnabled(false);
      if (depend != null) {
         dAction.addDependencyAnd(depend);
      }
      // @todo  add setSmallIcon();
   }

   public void addDependentActionToggle(String name, String commandKey,
           String iconFile, Object handler,
           String method, int key,
           ValueModel depend) {
      ActionManager manager = ActionManager.getInstance();
      DependentAction dAction = new DependentActionToggle(name, commandKey,
              new ImageIcon(getClass().getResource(iconFile)), handler, method);
      dAction.setMnemonic(new Integer(key));
      manager.addAction(dAction);
      actions.add(dAction.getValue(Action.ACTION_COMMAND_KEY));
      dAction.addDependencyAnd(depend);
      // @todo  add setSmallIcon();
   }

   // </editor-fold>
   // Returns true if the app can exit, false otherwise.
   @Override
   public boolean canExit() {
      Iterator iterator = modules.keySet().iterator();
      while (iterator.hasNext()) {
         ((IModule) modules.get(iterator.next())).shutdown();
      }
      // Close all the devices
      //        try {
      //            PersistenceHandler handler = PersistenceHandler.getHandler();
      //            model.save(handler.getOutputStream());
      //        } catch (IOException ex) {
      //            // There was an error saving, prompt the user
      //            ResourceBundle resources = Application.getInstance().
      //                    getResourceBundle();
      //            int alertResult = JOptionPane.showOptionDialog(entryList,
      //                    resources.getString("passwordList.errorSaving"),
      //                    resources.getString("passwordList.errorSavingTitle"),
      //                    JOptionPane.YES_NO_OPTION,
      //                    JOptionPane.ERROR_MESSAGE, null, null, null);
      //            // User wants to exit anyway.
      //            return (alertResult == JOptionPane.YES_OPTION);
      //        }
      return true;
   }

}
