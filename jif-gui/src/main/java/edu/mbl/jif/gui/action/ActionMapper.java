/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.gui.action;

/**
 * <p>Configurable prioritized mappings from user key and mouse actions to
 * handlers.</p>
 *
 * <p>Copyright (C) 2007 Marsette A. Vona, III</p>
 *
 * <p>This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.</p>
 *
 * <p>This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.</p>
 *
 * <p>You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place - Suite 330, Boston, MA 02111-1307, USA.</p>
 **/


import java.io.PrintStream;

import java.lang.ref.WeakReference;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import static java.awt.event.KeyEvent.*;
import static java.awt.event.MouseEvent.*;

//import net.jcip.annotations.*;

/**
 * <p>Configurable prioritized mappings from user key and mouse actions to
 * handlers.</p>
 *
 * <p>An ActionMapper maintains zero or more ordered {@link ActionMap}s which
 * map <i>action specs</i> to {@link Action}s.  The set of maps and the content
 * of each map are mutable, and maps and actions can be individually
 * disabled/enabled.</p>
 *
 * <p>An ActionMapper can be added to zero or more AWT components, and itself
 * can be enabled/disabled.</p>
 *
 * <p>The only effect of disabling an {@link Action}, {@link ActionMap}, or 
 * {@link ActionMapper} is that corresponding {@link Action#run} calls are not
 * made.</p>
 *
 * <p>Normal action specs (see below for wildcard specs) are case-sensitive
 * strings of the pattern
 *
 * <pre>[S-][C-][A-]{<i>ks</i>|{1|2|3}-mc|[1-][2-][3-]mm|mw}</pre>
 *
 * where <ul>
 *
 * <li>the [S-][C-][A-] prefix indicates the state of the Shift, Ctrl, and Alt
 * modifiers to match, respectively; the presence of the character indicates
 * that the corresponding key must be pressed, and the absence of the character
 * indicates that the corresponding key must <i>not</i> be pressed;</li>
 *
 * <li><i>ks</i>, if included, is a <i>key spec</i> to match, which is
 * either<ul><li>the string returned by {@code KeyEvent.getKeyText()} for the
 * desired key code, converted to lower case and with spaces replaced by
 * underscores</li><li>OR, the string equivalent of the char returned by {@code
 * KeyEvent.getKeyChar()}, in which case the S- prefix is not
 * allowed;</li></ul></li>
 *
 * <li><i>i</i>-mc matches a click of mouse button <i>i</i>;</li>
 *
 * <li>mm matches a mouse move while holding the indicated, possibly empty, set
 * of buttons;</li>
 *
 * <li>mw matches a mouse wheel move.</li>
 *
 * </ul></p>
 *
 * <p>There are three wildcard action specs:
 *
 * <pre>any other key|*-mc|*-mm</pre>
 *
 * If present, these match any keypress, mouse click, or mouse move action,
 * respectively, that is not matched by any non-wildcard spec.</p>
 *
 * <p>A user key or mouse action on any attached AWT component generates an
 * action spec which is looked up in each {@link ActionMap} in order.  The
 * first enabled {@link Action} found, if any, is executed, in the AWT event
 * thread.  {@link Action}s in earlier maps therefore take priority over those
 * with the same spec in later maps.  This can be used to implement
 * e.g. layered input modes and submodes.</p>
 *
 * <h2>Synchronization Policies</h2>
 *
 * <p>This class is designed to be thread safe:<ul>
 *
 * <li>{@link Action#run} is always called from within the AWT event thread,
 * and the passed {@link ComponentState}s (from {@link #componentStateMap}) are
 * also confined to the AWT event thread.</li>
 *
 * <li>The set of {@link #actionMaps} is internally synchronized.</li>
 *
 * <li>The contents of each {@link ActionMap} are guarded by that map.</li>
 *
 * <li>The only mutable state of an {@link Action} is {@link Action#enabled},
 * which is {@code volatile}.</li>
 *
 * </ul></p>
 *
 * <h2>TBD</h2>
 *
 * <p><ul>
 *
 * <li>TBD workaround for bug ID 4153069 (open) "keyReleased behaviour
 * incorrect" to normalize/neutralize platform typematic systems (cf {@link
 * RXSInteractor}), or wait for official bugfix</li>
 *
 * <li>TBD refactor {@link RXSInteractor} to use ActionMapper</li>
 *
 * </ul></p>
 *
 * <p>Copyright (C) 2007 Marsette A. Vona, III</p>
 *
 * <p>This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.</p>
 *
 * <p>This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.</p>
 *
 * <p>You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc., 59
 * Temple Place - Suite 330, Boston, MA 02111-1307, USA.</p>
 *
 * @author Marsette (Marty) A. Vona, III
 **/
//@ThreadSafe
public class ActionMapper {

  private static final String cvsid =
  "$Id: ActionMapper.java 271 2008-11-02 23:03:25Z vona $";

  /** {@link #dbgFlags} **/
  public static final int
  DBG_ENABLED = 1<<1, DBG_MOUSE_MOTION = 1<<2, DBG_COMPONENT_STATE = 1<<3,
    DBG_ALL = ~0;

  /** enable for debug spew **/
  public volatile int dbgFlags = 0;

  /** enable for this entire ActionMapper **/
  public volatile boolean enabled = true;

  /** event type indices **/
  public static final int
  ET_KEYPRESS = 0, ET_MOUSE_MOTION = 1, ET_MOUSE_CLICK = 2, ET_MOUSE_WHEEL = 3;

  /** number of event types **/
  public static final int NUM_EVENT_TYPES = 4;

  /** event type names indexed by event type **/
  public static final String[] EVENT_TYPE_NAME =
  {"key press", "mouse motion", "mouse click", "mouse wheel"};

  /** wildcard action specs indexed by event type **/
  public static final String[] WILDCARD_SPEC =
    new String[] {"any other key", "*-mm", "*-mc", null};

  /** mapping from AWT event ID to human readable event type **/
  public static final Map<Integer,String> eventTypeFromID =
    Collections.unmodifiableMap(new HashMap<Integer,String>() {
        {
          put(KeyEvent.KEY_PRESSED, "key pressed");
          put(KeyEvent.KEY_RELEASED, "key released");
          put(KeyEvent.KEY_TYPED, "key typed");
          put(MouseEvent.MOUSE_CLICKED, "mouse clicked");
          put(MouseEvent.MOUSE_DRAGGED, "mouse dragged");
          put(MouseEvent.MOUSE_ENTERED, "mouse entered");
          put(MouseEvent.MOUSE_EXITED, "mouse exited");
          put(MouseEvent.MOUSE_FIRST, "mouse first");
          put(MouseEvent.MOUSE_LAST, "mouse last");
          put(MouseEvent.MOUSE_MOVED, "mouse moved");
          put(MouseEvent.MOUSE_PRESSED, "mouse pressed");
          put(MouseEvent.MOUSE_RELEASED, "mouse released");
          put(MouseEvent.MOUSE_WHEEL, "mouse wheel");
        }
      });

  /** 
   * <p>The most recently handled component for each event type, or null if
   * none.</p>
   *
   * <p>Confined to the GUI event thread.</p>
   **/
  @SuppressWarnings("unchecked")
  protected WeakReference<Component>[] mostRecentComponent =
    new WeakReference[4];

    {
      for (int i = 0; i < mostRecentComponent.length; i++)
        mostRecentComponent[i] = new WeakReference<Component>(null);
    }

  /** 
   * <p>The most recently handled action spec for each event type, or null if
   * none.</p>
   *
   * <p>Confined to the GUI event thread.</p>
   **/
  protected String[] mostRecentActionSpec = new String[4];

  /** 
   * <p>The most recent event ID for each event type for each event type.</p>
   *
   * <p>Confined to the GUI event thread.</p>
   **/
  protected int[] mostRecentEventID = new int[4];

  /** 
   * <p>The most recent timestamp for each event type.</p>
   *
   * <p>Confined to the GUI event thread.</p>
   **/
  protected long[] mostRecentTimestamp = new long[4];

  /** 
   * <p>The most recent action result for each event type.</p>
   *
   * <p>Confined to the GUI event thread.</p>
   **/
  protected boolean[] mostRecentActionResult = new boolean[4];

  /**
   * <p>An action.</p>
   *
   * <p>See {@link ActionMapper} class header doc for details.</p>
   **/
  public static abstract class Action {

    /** enable for this action **/
    public volatile boolean enabled = true;

    /** a short description string **/
    public volatile String description;

    /** 
     * <p>If set then this Action will only appear in {@link ActionMap} and
     * {@link ActionMapper} dumps for the given spec, even though other specs
     * may also map to it.</p>
     **/
    public volatile String dumpOnlyForSpec = null;

    /**
     * <p>Whether the action event that resulted in a call to {@link #run} that
     * returned true should be consumed.</p>
     **/
    public volatile boolean consuming = true;

    /** gives a canonical ordering for Actions based on their spec **/
    public static final Comparator<String> COMPARATOR =
      new Comparator<String>() {
      public int compare(String spec0, String spec1) {
        String bs0 = getBaseSpec(spec0), bs1 = getBaseSpec(spec1);
        boolean wc0 = isWildcard(spec0), wc1 = isWildcard(spec1);
        if (wc0 && !wc1) {
          return +1; //wildcards at end
        } else if (!wc0 && wc1) {
          return -1; //wildcards at end
        } else if (bs0.length() > bs1.length()) {
          return +1; //longer at end
        } else if (bs0.length() < bs1.length()) {
          return -1; //longer at end
        } else {
          //dictionary order for same length base specs
          int c = bs0.compareTo(bs1);
          //if base specs same then order full spec
          return (c != 0) ? c : spec0.compareTo(spec1);
        }
      }
    };

    /** construct a new Action with <i>spec</i> and <i>description</i> **/
    public Action(String description) {
      this.description = description;
    }

    /** dump this action **/
    public void dump(String spec, PrintStream s) {

      if (s == null)
        s = System.out;

      s.printf("%14s %s\n", spec, description);

      s.flush();
    }

    /** {@link #dump(String, PrintStream)} to System.out **/
    public void dump(String spec) {
      dump(spec, null);
    }

    /**
     * <p>Execute the action.</p>
     *
     * <p>This will only be called from the AWT event thread, and only for
     * {@link #enabled} Actions.</p>
     *
     * @param component the AWT {@code Component} on which the event occurred
     * @param componentState the {@link ComponentState} of <i>component</i>, as
     * tracked by the containing {@link ActionMapper}
     * @param actionSpec the action spec string that invoked the action, see
     * {@link ActionMapper} class header doc
     *
     * @return true unless the implementation decided not to execute the action
     **/
    public abstract boolean run(String actionSpec,
                                Component component,
                                ComponentState componentState);
  }

  /**
   * <p>Generic {@link Action} impl that delegates to an object implementing an
   * {@code apply(Object[])} method.</p>
   *
   * <p>The {@code apply(Object[])} method is passed the argument vector
   * <code>{this, component, cs, actionSpec, cs.shiftDown, cs.ctrlDown,
   * cs.altDown, cs.button1Down, cs.button2Down, cs.button3Down, cs.width,
   * cs.height, cs.mousePressedX, cs.mousePressedY, cs.mouseX, cs.mouseY,
   * cs.mouseWheelClicks}</code> where <code>cs</code> is the {@link
   * ComponentState} and <code>component</code> is the Component passed to
   * {@link #run}.  If it returns a Boolean or a boolean then the value has the
   * same semantics as {@link Action#run}; other return types are treated as
   * {@code true}.</p>
   *
   * <p>This is primarily for integration with higher-level scripting
   * environments such as JScheme.</p>
   **/
  public static class ApplyingAction extends Action {

    /** the delegate **/
    protected final Object appliccable;

    /** the apply method on the delegate **/
    protected final java.lang.reflect.Method applyMethod;

    /** 
     * <p>Make a new delegating action.</p>
     *
     * @param applyMethodName the name of the {@code apply(Object[])} method,
     * or null to use "apply"
     **/
    public ApplyingAction(String description,
                          Object appliccable, String applyMethodName) {
      super(description);

      if (applyMethodName == null)
        applyMethodName = "apply";

      this.appliccable = appliccable;

      try {
        applyMethod =
          appliccable.getClass().getMethod("apply", Object[].class);
      } catch (Exception e) {
        throw new IllegalArgumentException(
          "could not access method apply(Object[])");
      }
    }
   
    /** uses default <i>applyMethodName</i> **/
    public ApplyingAction(String description, Object appliccable) {
      this(description, appliccable, null);
    }

    /** 
     * <p>Delegates to {@link #appliccable}<code>.apply(Object[])</code>, see
     * class header doc.</p>
     **/
    public boolean run(String actionSpec,
                       Component component, ComponentState cs) {
      try {
        Object ret = 
          applyMethod.invoke(appliccable,
                             new Object[] {
                               new Object[] {
                                 ApplyingAction.this, actionSpec,
                                 component, cs, 
                                 cs.shiftDown, cs.ctrlDown, cs.altDown,
                                 cs.button1Down, cs.button2Down, cs.button3Down,
                                 cs.width, cs.height,
                                 cs.mousePressedX, cs.mousePressedY,
                                 cs.mouseX, cs.mouseY,
                                 cs.mouseWheelClicks}});

        if (ret instanceof Boolean)
          return (Boolean) ret;
        else
          return true;

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
     
  /** a map taking action specs to {@link Action}s **/
  public static class ActionMap extends LinkedHashMap<String, Action> {

    /** enable for this map **/
    public volatile boolean enabled = true;
    
    /**
     * <p>Whether to issue a warning to stderr if an action is replaced.</p>
     *
     * <p>This only applies to actions added via {@link #add}.</p>
     **/
    public volatile boolean warnDuplicate = false;

    /** name for this map **/
    public final String name;
   
    /** make a new map with the given <i>name</i> **/
    public ActionMap(String name) {
      this.name = name;
    }
    
    /**
     * <p>Convenience to add/replace the mapping(s) from <i>spec</i>(s) to
     * <i>action</i>.</p>
     *
     * @return the number of existing mappings that were replaced
     **/
    public synchronized int add(Action action, String... spec) {

      if (spec == null)
        return 0;

      int numReplaced = 0;
      for (String s : spec) {
        
        Action dead = put(s, action);
        
        if (dead != null) {

          numReplaced++;

          if (warnDuplicate)
            System.err.println("W: replaced "+s+" action");
        }
      }
      
      return numReplaced;
    }
  
    /**
     * <p>{@link #add} a new {@link Action} consed up to run <i>task</i>.</p>
     *
     * <p>This can make for shorter syntax when the action doesn't need any of
     * the {@link Action#run} parameters and always returns true.</p>
     **/
    public synchronized int addSimpleAction(String description,
                                            final Runnable task,
                                            String... spec) {
      return add(new Action(description) {
          public boolean run(
            String actionSpec, Component c, ActionMapper.ComponentState cs) {
            task.run(); return true;
          } }, spec);
    }

    /**
     * <p>Convenience to remove all mappings to an action.</p>
     *
     * @return the number of removed mappings
     **/
    public synchronized int remove(Action action) {

      int numRemoved = 0;

      for (Iterator<Map.Entry<String, Action>> it = entrySet().iterator();
           it.hasNext(); ) {
        Map.Entry<String, Action> e = it.next();
        if (e.getValue() == action) {
          it.remove();
          numRemoved++;
        }
      }

      return numRemoved;
    }

    /** 
     * <p>Convenience to remove a set of mappings.</p>
     *
     * @return the number of removed mappings
     **/
    public synchronized int remove(String... spec) {

      if (spec == null)
        return 0;
     
      int numRemoved = 0;
      for (String s : spec)
        if (super.remove(s) != null)
          numRemoved++;

      return numRemoved;
    }

    /** dump map contents **/
    public synchronized void dump(PrintStream s,
                                  boolean skipDisabled, boolean sort) {

      if (s == null)
        s = System.out;
      
      Collection<String> specs = keySet();
      
      if (sort) {
        java.util.List<String> l = new ArrayList<String>(specs);
        Collections.sort(l, Action.COMPARATOR);
        specs = l;
      }

      for (String spec : specs) {

        Action a = get(spec);

        assert (a != null);

        if ((a.dumpOnlyForSpec != null) && !a.dumpOnlyForSpec.equals(spec))
          continue;

        if (!skipDisabled)
          s.printf("%8s ", (a.enabled) ? "enabled" : "disabled");

        if (a.enabled || !skipDisabled) a.dump(spec, s);
      }
      
      s.flush();
    }
    
    /** {@link #dump(PrintStream, boolean, boolean)} to System.out **/
    public synchronized void dump(boolean skipDisabled, boolean sort) {
      dump(null, skipDisabled, sort);
    }

    /** 
     * <p>{@link #dump(PrintStream, boolean, boolean)} to System.out, skipping
     * disabled and sorting.</p>
     **/
    public synchronized void dump() {
      dump(null, true, true);
    }
  }

  /**
   * <p>The currently installed {@link ActionMap}s, in insertion order from
   * highest to lowest priority.</p>
   **/
  protected final java.util.List<ActionMap> actionMaps =
    Collections.synchronizedList(new LinkedList<ActionMap>());

  /**
   * <p>Collects the state of an AWT {@code Component}.</p>
   *
   * <p>The fields are set automatically by the {@link ActionMapper} event
   * handlers and are available for use by action handlers.  There is no
   * explicit synchronization; the fields are intended to be confined to the
   * AWT event thread.</p>
   **/
  public static class ComponentState {

    /** current status of the indicated key **/
    public boolean shiftDown = false, ctrlDown = false, altDown = false;
    
    /** current status of the indicated button **/
    public boolean
    button1Down = false, button2Down = false, button3Down = false;
    
    /** component dimension, negative if none **/
    public int width = -1, height = -1;
    
    /** coord of most recent mouse press, negative if none **/
    public int mousePressedX = -1, mousePressedY = -1;

    /** coord of most recent mouse event, negative if none **/
    public int mouseX = -1, mouseY = -1;

    /**
     * <p>Clicks of a mouse wheel event, negative if none.</p>
     *
     * <p>This will only be non-negative while handling the corresponding wheel
     * event.</p>
     **/
    public int mouseWheelClicks = -1;

    /** dumps all fields **/
    public void dump(PrintStream s) {

      if (s == null)
        s = System.out;

      s.println("modifiers down: "+
                ((shiftDown) ? "S" : "")+
                ((ctrlDown) ? "C" : "")+
                ((altDown) ? "A" : ""));
      s.println("buttons down: "+
                ((button1Down) ? "1" : "")+
                ((button2Down) ? "2" : "")+
                ((button3Down) ? "3" : ""));
      s.println("component size: ("+width+", "+height+")");
      s.println("mouse pressed at: ("+mousePressedX+", "+mousePressedY+")");
      s.println("mouse at: ("+mouseX+", "+mouseY+")");
      s.println("mouse wheel clicks: "+mouseWheelClicks);

      s.flush();
    }

    /** {@link #dump(PrintStream)} to System.out **/
    public void dump() {
      dump(null);
    }

    /** resets all fields **/
    protected void reset() {
      shiftDown = ctrlDown = altDown = false;
      mousePressedX = mousePressedY = -1;
      mouseX = mouseY = -1;
      mouseWheelClicks = -1;
    }
  }

  /** map from AWT {@code Component} to {@link ComponentState} **/
  protected final Map<Component, ComponentState> componentStateMap =
    Collections.
    synchronizedMap(new LinkedHashMap<Component, ComponentState>());

  /** listener for {@code ComponentEvent}s **/
  protected final ComponentListener componentListener =
    new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        Component c = e.getComponent();
        ComponentState s = lookupComponentState(e.getComponent());
        if (s != null) {
          s.width = c.getWidth();
          s.height = c.getHeight();
        }
      }
    };

  /** listener for {@code FocusEvent}s **/
  protected final FocusListener focusListener = new FocusListener() {
      
      public void focusGained(FocusEvent e) {
        //ignored
      }

      public void focusLost(FocusEvent e) {
        ComponentState s = lookupComponentState(e.getComponent());
        if (s != null)
          s.reset();
      }
    };

  /** listener for {@code KeyEvent}s **/
  protected final KeyListener keyListener = new KeyListener() {
      
      public void keyPressed(KeyEvent e) {
        if (e.isConsumed()) return; //I don't know why, but this happens
        ComponentState s = lookupComponentState(e.getComponent());
        if (s != null) {
          switch (e.getKeyCode()) {
          case VK_SHIFT: s.shiftDown = true; break;
          case VK_CONTROL: s.ctrlDown = true; break;
          case VK_META: //pass through
          case VK_ALT: s.altDown = true; break;
          }
        }
      }

      public void keyReleased(KeyEvent e) {
//        System.err.println("keyReleased("+e+")");
        if (e.isConsumed()) return; //I don't know why, but this happens
        Component c = e.getComponent();
        ComponentState s = lookupComponentState(c);
        if (s != null) {
          int code = e.getKeyCode();
          if (dbgFlags != 0)
            System.err.println("key released, key code "+code);
          switch (code) {
          case VK_SHIFT: s.shiftDown = false; break;
          case VK_CONTROL: s.ctrlDown = false; break;
          case VK_META: //pass through
          case VK_ALT: s.altDown = false; break;
          default: 
            if (handleKeyReleased(c, s, code, e.getID(), e.getWhen()))
              e.consume();
          }
        }
      }

      public void keyTyped(KeyEvent e) {
//        System.err.println("keyTyped("+e+")");
        if (e.isConsumed()) return; //I don't know why, but this happens
        Component c = e.getComponent();
        ComponentState s = lookupComponentState(c);
        char ch = e.getKeyChar();
        if ((s != null) && (ch != KeyEvent.CHAR_UNDEFINED) &&
            handleKeyTyped(c, s, ch, e.getID(), e.getWhen()))
          e.consume();
      }
    };
 
  /** listener for {@code MouseEvent}s **/
  protected final MouseListener mouseListener = new MouseListener() {
      
      public void mouseClicked(MouseEvent e) {
        if (e.isConsumed()) return; //I don't know why, but this happens
        Component c = e.getComponent();
        ComponentState s = lookupComponentState(c);
        if ((s != null) &&
            handleMouseClick(c, s, e.getButton(), e.getID(), e.getWhen()))
          e.consume();
      }

      public void mouseEntered(MouseEvent e) {
        //ignore
      }

      public void mouseExited(MouseEvent e) {
        ComponentState s = lookupComponentState(e.getComponent());
        if (s != null) { s.mouseX = s.mouseY = -1; }
      }

      public void mousePressed(MouseEvent e) {
        ComponentState s = lookupComponentState(e.getComponent());
        if (s != null) {
          s.mousePressedX = e.getX();
          s.mousePressedY = e.getY();
          int m = e.getModifiersEx();
          s.button1Down = ((m&BUTTON1_DOWN_MASK) == BUTTON1_DOWN_MASK);
          s.button2Down = ((m&BUTTON2_DOWN_MASK) == BUTTON2_DOWN_MASK);
          s.button3Down = ((m&BUTTON3_DOWN_MASK) == BUTTON3_DOWN_MASK);
        }
      }

      public void mouseReleased(MouseEvent e) {
        if (e.isConsumed()) return; //I don't know why, but this happens
        ComponentState s = lookupComponentState(e.getComponent());
        if (s != null) {
          int m = e.getModifiersEx();
          s.button1Down = ((m&BUTTON1_DOWN_MASK) == BUTTON1_DOWN_MASK);
          s.button2Down = ((m&BUTTON2_DOWN_MASK) == BUTTON2_DOWN_MASK);
          s.button3Down = ((m&BUTTON3_DOWN_MASK) == BUTTON3_DOWN_MASK);
        }
        //note: mouse click actions are handled in mouseClicked()
      }
    };

  /** listener for {@code MouseEvent}s related to motion **/
  protected final MouseMotionListener mouseMotionListener = 
    new MouseMotionListener() {

      public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
      }

      public void mouseMoved(MouseEvent e) {
        if (e.isConsumed()) return; //I don't know why, but this happens
        Component c = e.getComponent();
        ComponentState s = lookupComponentState(c);
        if (s != null) {
          s.mouseX = e.getX();
          s.mouseY = e.getY();
          if (handleMouseMove(c, s, e.getID(), e.getWhen()))
            e.consume();
        }
      }
    }; 

  /** listener for {@code MouseWheelEvent}s **/
  protected final MouseWheelListener mouseWheelListener =
    new MouseWheelListener() {
      public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isConsumed()) return; //I don't know why, but this happens
        Component c = e.getComponent();
        ComponentState s = lookupComponentState(c);
        if (s != null) {
          s.mouseWheelClicks = e.getWheelRotation();
          if (handleMouseWheelMove(c, s, e.getID(), e.getWhen()))
            e.consume();
          s.mouseWheelClicks = -1;
        }
      }
    };

  /**
   * <p>Add an AWT {@code Component} to the listen list.</p>
   *
   * @return true iff <i>c</i> was not already present
   **/
  public boolean addComponent(Component c) {

    if (c == null)
      throw new IllegalArgumentException("null Component");

    synchronized (componentStateMap) {

      ComponentState s = componentStateMap.get(c);
      
      if (s != null)
        return false;
    }
      
    componentStateMap.put(c, new ComponentState());

    c.addComponentListener(componentListener);
    c.addFocusListener(focusListener);
    c.addKeyListener(keyListener);
    c.addMouseListener(mouseListener);
    c.addMouseMotionListener(mouseMotionListener);
    c.addMouseWheelListener(mouseWheelListener);
    
    return true;
  }

  /**
   * <p>Remove an AWT {@code Component} from the listen list.</p>
   *
   * @return true iff <i>c</i> was present
   **/
  public boolean removeComponent(Component c) {

    if (c == null)
      throw new IllegalArgumentException("null Component");

    ComponentState s = componentStateMap.remove(c);

    if (s == null)
      System.err.println("W: no state for "+c);

    c.removeComponentListener(componentListener);
    c.removeFocusListener(focusListener);
    c.removeKeyListener(keyListener);
    c.removeMouseListener(mouseListener);
    c.removeMouseMotionListener(mouseMotionListener);
    c.removeMouseWheelListener(mouseWheelListener);

    return (s != null);
  }

  /**
   * <p>Add an {@link ActionMap} as the first (i.e. highest priority) map in
   * the list.</p>
   **/
  public void pushMap(ActionMap m) {
    if (m == null)
      throw new IllegalArgumentException("null map");
    actionMaps.add(0, m);
  }

  /**
   * <p>Remove the first (i.e. highest priority) {@link ActionMap}, if any.</p>
   *
   * @return the removed map, or null if none
   **/
  public ActionMap popMap() {
    synchronized (actionMaps) {
      if (actionMaps.isEmpty())
        return null;
      else
        return actionMaps.remove(0);
    }
  }

  /** get the current number of maps **/
  public int numMaps() {
    return actionMaps.size();
  }

  /** get the map at the specified index **/
  public ActionMap getMap(int index) {
    return actionMaps.get(index);
  }

  /** get the current number of components **/
  public int numComponents() {
    return componentStateMap.size();
  }

  /**
   * <p>Match an action spec to the corresponding {@link Action}.</p>
   *
   * <p>See class header doc for detailed semantics.</p>
   *
   * @return the Action corresponding to <i>spec</i> or null if none
   **/
  public Action lookupAction(String spec, boolean useWildcards) {

    if (!enabled) return null;

    synchronized (actionMaps) {
      for (ActionMap m : actionMaps) {
        if (m.enabled) {
          Action a = m.get(spec);
          if ((a != null) && a.enabled) {
            return a;
          } else if (useWildcards) {
            a = m.get(WILDCARD_SPEC[getEventType(spec)]);
            if ((a != null) && a.enabled) return a;
          }
        }
      }
    }

    return null;
  }

  /** {@link #lookupAction(String, boolean)}, use wildcards **/
  public Action lookupAction(String spec) {
    return lookupAction(spec, true);
  }

  /**
   * <p>Dump all and only enabled {@link Action}s.</p>
   *
   * <p>An {@link Action} is enabled only if its own enable flag, the enable
   * flag of its enclosing {@link ActionMap}, and the {@link #enabled} flag of
   * this ActionMapper are all set.</p>
   *
   * <p>If more than one {@link Action} is enabled for a given spec then only
   * the highest-priority {@link Action} is dumped.</p>
   *
   * @param actionMapName if non-null then this is the application-specific
   * nomenclature for an {@link ActionMap} (e.g. "mode"); if null then the
   * default nomenclature "action map" is used
   * @param sort whether to sort the actions by insertion order ({@code sort =
   * false}) or using {@link Action#COMPARATOR} ({@code sort = true})
   **/
  public void dumpActions(PrintStream s, String actionMapName, boolean sort) {

    if (s == null)
      s = System.out;

    if (actionMapName == null)
      actionMapName = "action map";

    if (!enabled) {
      s.println("no enabled "+actionMapName+"(s)");
      return;
    }

    synchronized (actionMaps) {

      int numMaps = 0; for (ActionMap m : actionMaps) if (m.enabled) numMaps++;
      s.println(numMaps+" enabled "+actionMapName+"(s)\n");

      //TBD this is broken when multiple specs for same physical action
      //(e.g. specs "S-g" and "G")
      Set<String> dumpedSpecs = new HashSet<String>();
      Collection<String> mapSpecs;

      boolean[] dumpedWildcard = new boolean[NUM_EVENT_TYPES];

      for (ActionMap m : actionMaps) {

        if (!(m.enabled)) continue;

        s.print(m.name+" "+actionMapName);
        
        mapSpecs = m.keySet();
        if (sort) {
          mapSpecs = new ArrayList<String>();
          mapSpecs.addAll(m.keySet());
          Collections.sort((ArrayList<String>) mapSpecs, Action.COMPARATOR);
        }

        int n = 0;
        for (String spec : mapSpecs) {

          if (dumpedSpecs.contains(spec)) continue;
          dumpedSpecs.add(spec);

          Action a = m.get(spec);
          assert (a != null);

          if (!(a.enabled)) continue;

          if ((a.dumpOnlyForSpec != null) && !a.dumpOnlyForSpec.equals(spec))
            continue;

          int et = getEventType(spec);

          if (dumpedWildcard[et]) continue;

          if (isWildcard(spec)) dumpedWildcard[et] = true;

          if (n == 0) s.println();
          a.dump(spec, s);

          n++;
        }

        if (n == 0) s.println(" (no active actions)");

        s.println();
      }
    }

    s.flush();
  }

  /**
   * <p>Covers {@link #dumpActions(PrintStream, String, boolean)}, uses default
   * nomenclature.</p>
   **/
  public void dumpActions(PrintStream s, boolean sort) {
    dumpActions(s, null, sort);
  }
 
  /**
   * <p>Covers {@link #dumpActions(PrintStream, String, boolean)}, uses default
   * nomenclature and sorts.</p>
   **/
  public void dumpActions(PrintStream s) {
    dumpActions(s, null, true);
  }

  /** covers {@link #dumpActions(PrintStream)} **/
  public void dump(PrintStream s) {
    dumpActions(s);
  }

  /**
   * <p>Covers {@link #dumpActions(PrintStream, String, boolean)}, uses {@code
   * System.out}.</p>
   **/
  public void dumpActions(String actionMapName, boolean sort) {
    dumpActions(System.out, actionMapName, sort);
  }

  /**
   * <p>Covers {@link #dumpActions(PrintStream, String, boolean)}, uses {@code
   * System.out} and sorts.</p>
   **/
  public void dumpActions(String actionMapName) {
    dumpActions(System.out, actionMapName, true);
  }

  /**
   * <p>Covers {@link #dumpActions(PrintStream, String, boolean)}, uses default
   * nomenclature and {@code System.out}.</p>
   **/
  public void dumpActions(boolean sort) {
    dumpActions(System.out, null, sort);
  }

  /**
   * <p>Covers {@link #dumpActions(PrintStream, String, boolean)}, uses default
   * nomenclature, {@code System.out}, and sorts.</p>
   **/
  public void dumpActions() {
    dumpActions(System.out, null, true);
  }

  /** covers {@link #dumpActions()} **/
  public void dump() {
    dumpActions();
  }

  /**
   * <p>Fetch the {@link ComponentState} for AWT {@code Component} <i>c</i>
   * from {@link #componentStateMap}.</p>
   *
   * <p>Issues a warning if the state is not found.</p>
   *
   * <p>Sets the component dimensions if unset.</p>
   *
   * @return the {@link ComponentState} corresponding to <i>c</i> or null if
   * not found
   **/
  protected ComponentState lookupComponentState(Component c) {

    ComponentState s = componentStateMap.get(c);

    if (s == null) {
      System.err.println("W: no state for "+c);
      return null;
    }

    if (s.width < 0)
      s.width = c.getWidth();

    if (s.height < 0)
      s.height = c.getHeight();

    return s;
  }
       
  /**
   * <p>Fix {@code KeyEvent.getKeyText()} on OS X.</p>
   **/
  protected static String getKeyText(int code) {
    switch (code) {
    case KeyEvent.VK_ESCAPE: return "escape";
    case KeyEvent.VK_TAB: return "tab";
    case KeyEvent.VK_SPACE: return "space";
    case KeyEvent.VK_BACK_SPACE: return "backspace";
    case KeyEvent.VK_ENTER: return "enter";
    case KeyEvent.VK_INSERT: return "insert";
    case KeyEvent.VK_HOME: return "home";
    case KeyEvent.VK_PAGE_UP: return "page_up";
    case KeyEvent.VK_PAGE_DOWN: return "page_down";
    case KeyEvent.VK_END: return "end";
    case KeyEvent.VK_DELETE: return "delete";
    case KeyEvent.VK_LEFT: return "left";
    case KeyEvent.VK_RIGHT: return "right";
    case KeyEvent.VK_UP: return "up";
    case KeyEvent.VK_DOWN: return "down";
      //TBD numpad
    default: return KeyEvent.getKeyText(code);
    }
  }

  /** {@link #handleAction} for a key release **/
  protected boolean handleKeyReleased(Component c, ComponentState s,
                                      int code, int eventID, long timestamp) {
    return handleAction(getKeyText(code).toLowerCase().replace(' ', '_'),
                        c, s, eventID, timestamp, ET_KEYPRESS);
  }

  /** {@link #handleAction} for a key typed **/
  protected boolean handleKeyTyped(Component c, ComponentState s, char ch, 
                                   int eventID, long timestamp) {
    return handleAction(Character.toString(ch), c, s,
                        eventID, timestamp, ET_KEYPRESS,
                        true); //ignoreShiftDown
  }

  /** {@link #handleAction} for a mouse click **/
  protected boolean handleMouseClick(Component c, ComponentState s, int btn,
                                     int eventID, long timestamp) {
    return handleAction(Integer.toString(btn)+"-mc", c, s,
                        eventID, timestamp, ET_MOUSE_CLICK);

  }

  /** {@link #handleAction} for a mouse move **/
  protected boolean handleMouseMove(Component c, ComponentState s,
                                    int eventID, long timestamp) {
    return handleAction(((s.button1Down) ? "1-" : "")+
                        ((s.button2Down) ? "2-" : "")+
                        ((s.button3Down) ? "3-" : "")+
                        "mm", c, s,
                        eventID, timestamp, ET_MOUSE_MOTION);
  }

  /** {@link #handleAction} for a mouse wheel move **/
  protected boolean handleMouseWheelMove(Component c, ComponentState s,
                                         int eventID, long timestamp) {
    return handleAction("mw", c, s,
                        eventID, timestamp, ET_MOUSE_WHEEL);
  }

  /**
   * <p>Add modifier specs to make an action spec from <i>baseSpec</i>, {@link
   * #lookupAction}, and execute the {@link Action}, if any.</p>
   *
   * @return true iff {@link Action#run} returns true and {@link
   * Action#consuming}.  false if no {@link Action} was found, {@link
   * Action#run} returned false, {@link Action#consuming} false, or if the
   * action spec is a repeat of the most recent one but with a different event
   * ID (this avoids duplication in cases where a single physical action
   * triggers multiple events, such as a keypress that triggers both
   * KeyReleased and KeyTyped events)
   **/
  public boolean handleAction(String baseSpec,
                              Component c, ComponentState s,
                              int eventID, long timestamp, int eventType,
                              boolean ignoreShiftDown) {

    boolean dbgEnabled = 
      ((dbgFlags != 0) &&
       (((dbgFlags&DBG_MOUSE_MOTION) != 0) || (eventType != ET_MOUSE_MOTION)));

    String spec =
      ((s.shiftDown && !ignoreShiftDown) ? "S-" : "")+
      ((s.ctrlDown) ? "C-" : "")+
      ((s.altDown) ? "A-" : "")+
      baseSpec;

    if (dbgEnabled) {
      System.err.println(
        "handling action \""+spec+"\""+
        ", event ID "+eventID+" ("+eventTypeFromID.get(eventID)+")"+
        ", timestamp "+timestamp+", event type "+EVENT_TYPE_NAME[eventType]);
//      System.err.println(
//        "prior action \""+mostRecentActionSpec[eventType]+
//        "\", prior event ID "+mostRecentEventID[eventType]+
//        ", prior timestamp "+mostRecentTimestamp[eventType]);
      if ((dbgFlags&DBG_COMPONENT_STATE) != 0)
        s.dump(System.err);
    }

    boolean ignore =
      ((c == mostRecentComponent[eventType].get()) &&
       mostRecentActionResult[eventType] &&
       ((spec.equals(mostRecentActionSpec[eventType]) &&
         (eventID != mostRecentEventID[eventType])) ||
        ((eventID == KEY_RELEASED) &&
         (mostRecentEventID[eventType] == KEY_TYPED))));
    
    mostRecentComponent[eventType] = new WeakReference<Component>(c);
    mostRecentActionSpec[eventType] = spec;
    mostRecentEventID[eventType] = eventID;
    mostRecentTimestamp[eventType] = timestamp;
    mostRecentActionResult[eventType] = false;
    
    if (ignore) {
      if (dbgEnabled)
        System.err.println("ignoring duplicate "+
                           EVENT_TYPE_NAME[eventType]+" action");
      return false;
    }

    Action a = lookupAction(spec);

    if (a != null) {

      boolean retval = a.run(spec, c, s);

      if (dbgEnabled)
        System.err.println("action returned "+retval);

      mostRecentActionResult[eventType] = retval;

      return (retval && a.consuming);

    } else {
      
      if (dbgEnabled) 
        System.err.println("no action found for spec");
      
      return false;
    }
  }

  /** 
   * <p>Covers {@link #handleAction(String, Component, ComponentState,
   * int, long, int, boolean)}, does not ignore shift down.</p>
   **/
  public boolean handleAction(String baseSpec,
                              Component c, ComponentState s,
                              int eventID, long timestamp, int eventType) {
    return handleAction(baseSpec, c, s, eventID, timestamp, eventType, false);
  }

  /** extract the base spec **/
  public static String getBaseSpec(String spec) {
    int i = spec.lastIndexOf('-');
    if (i < 0)
      return spec;
    return spec.substring(i+1);
  }

  /** check if <i>spec</i> is a wildcard **/
  public static boolean isWildcard(String spec) {
    for (String s : WILDCARD_SPEC)
      if ((s != null) && s.equals(spec))
        return true;
    return false;
  }

  /** get the event type for <i>spec</i> **/
  public static int getEventType(String spec) {

    if (spec == null)
      throw new IllegalArgumentException("null spec");

    //TBD check if any key texts end in mc, mm, or mw...
    if (spec.endsWith("mw"))
      return ET_MOUSE_WHEEL;
    else if (spec.endsWith("mm"))
      return ET_MOUSE_MOTION;
    else if (spec.endsWith("mc"))
      return ET_MOUSE_CLICK;
    else
      return ET_KEYPRESS;
  }
}
