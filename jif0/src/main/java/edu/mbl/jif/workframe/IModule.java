package edu.mbl.jif.workframe;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.Model;

import javax.swing.Action;
import javax.swing.Icon;

/**
 * Module Interfdce
 *
 * @author GBH
 */
public interface IModule {
   /* Fields:
    private boolean enabled;
    private String key;
    private String name;
    private Icon icon;
    private Model model;
    private PresentationModel presentation;
    private Object controller;
    private java.awt.Container panel;
    private Action[] actions;
    */

   /*
    ++getHelp();
    ++getAbout();
    */
   /* 
    ApplicationController#addModule(IModule module) 
    */
   public String getKey();

   public String getName();

   public boolean isEnabled();

   // Life cycle ...
   // constructs module objects...
   void initialize();

   // when startup() is called, all modules have been initialize()'d... 
   // so any dependencies to other modules ought be resolved here...
   boolean startup();

   void ready();

   void exit();

   void shutdown();
   // ... Life cycle
   //

   public Model getModel();

   //   In Model... public PresentationModel getPresentation();
   public Object getController();

   public Action[] getActions();


}
