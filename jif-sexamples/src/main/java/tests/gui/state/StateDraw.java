/*
 * StateDraw.java
 *
 * Created on December 14, 2006, 4:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.gui.state;

/*
The Design Patterns Java Companion

Copyright (C) 1998, by James W. Cooper

IBM Thomas J. Watson Research Center

*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class StateDraw extends JFrame implements ActionListener {
  JToolBar tbar;

  Mediator med;

  public StateDraw() {
    super("State Drawing");
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    JPanel jp = new JPanel();
    getContentPane().add(jp);
    med = new Mediator();

    jp.setLayout(new BorderLayout());
    tbar = new JToolBar();
    tbar.setFloatable(false);
    jp.add("North", tbar);
    PickButton pick = new PickButton(this, med);
    tbar.add(pick);
    tbar.addSeparator();
    RectButton rect = new RectButton(this, med);
    tbar.add(rect);
    FillButton fill = new FillButton(this, med);
    tbar.add(fill);
    CircleButton circ = new CircleButton(this, med);
    tbar.add(circ);
    tbar.addSeparator();
    ClearButton clr = new ClearButton(this, med);
    tbar.add(clr);

    JCanvas canvas = new JCanvas(med);
    jp.add("Center", canvas);

    MouseApp map = new MouseApp(med);
    canvas.addMouseListener(map);
    MouseMoveApp mvap = new MouseMoveApp(med);
    canvas.addMouseMotionListener(mvap);
    setSize(new Dimension(400, 300));
    setVisible(true);

  }

  public void actionPerformed(ActionEvent e) {
    Command comd = (Command) e.getSource();
    comd.Execute();
  }

  static public void main(String[] argv) {
    new StateDraw();
  }
}


class MouseApp extends MouseAdapter {
  Mediator med;

  public MouseApp(Mediator md) {
    super();
    med = md;
  }

  public void mousePressed(MouseEvent e) {
    med.mouseDown(e.getX(), e.getY());
  }

  public void mouseReleased(MouseEvent e) {
    med.mouseUp(e.getX(), e.getY());
  }
}

class MouseMoveApp extends MouseMotionAdapter {
  Mediator med;

  public MouseMoveApp(Mediator md) {
    super();
    med = md;
  }

  public void mouseDragged(MouseEvent e) {
    med.mouseDrag(e.getX(), e.getY());
  }
}

class ArrowState extends State {
  private Mediator med;

  public ArrowState(Mediator md) {
    med = md;
  }

  public void mouseDown(int x, int y) {
    Vector drawings = med.getDrawings();
    for (int i = 0; i < drawings.size(); i++) {
      Drawing d = (Drawing) drawings.elementAt(i);
      if (d.contains(x, y))
        med.setSelected(d);
    }
  }
}

class CircleState extends State {
  private Mediator med; //save Mediator

  public CircleState(Mediator md) {
    med = md;
  }

  //Draw circle where mouse clicks
  public void mouseDown(int x, int y) {
    med.addDrawing(new visCircle(x, y));
  }
}

class ClearButton extends JButton implements Command {
  Mediator med;

  public ClearButton(ActionListener act, Mediator md) {
    super("C");

    setToolTipText("Clear");
    addActionListener(act);
    med = md;
  }

  public void Execute() {
    med.clear();
  }
}

class CircleButton extends JToggleButton implements Command {
  Mediator med;

  public CircleButton(ActionListener act, Mediator md) {
    super(" ");
    setSize(new Dimension(35, 35));
    setBorderPainted(true);
    setMargin(new Insets(5, 12, 5, 12));
    setToolTipText("Draw circle");
    addActionListener(act);
    med = md;
    med.registerCircleButton(this);
  }

  public Dimension getPreferredSize() {
    return new Dimension(35, 35);
  }

  public void paint(Graphics g) {
    super.paint(g);
    int h = getHeight();
    int w = getWidth();
    g.setColor(Color.black);
    g.drawArc(2, 2, h - 4, h - 4, 0, 360);
  }

  public void Execute() {
    med.startCircle();
  }

}

class State {
  public void mouseDown(int x, int y) {
  }

  public void mouseUp(int x, int y) {
  }

  public void mouseDrag(int x, int y) {
  }

  public void select(Drawing d, Color c) {
  }
}

interface Command {
  public void Execute();
}

class FillState extends State {
  private Mediator med; //save Mediator

  private Color color; //save current color

  public FillState(Mediator md) {
    med = md;
  }

  //Fill drawing if selected
  public void select(Drawing d, Color c) {
    color = c;
    if (d != null) {
      d.setFill(c); //fill that drawing
    }
  }

  //Fill drawing if you click inside one
  public void mouseDown(int x, int y) {
    Vector drawings = med.getDrawings();
    for (int i = 0; i < drawings.size(); i++) {
      Drawing d = (Drawing) drawings.elementAt(i);
      if (d.contains(x, y))
        d.setFill(color); //fill drawing
    }
  }
}

class JCanvas extends JPanel {
  Mediator med;

  public JCanvas(Mediator md) {
    med = md;
    med.registerCanvas(this);
    setBackground(Color.white);
  }

  public void paint(Graphics g) {
    super.paint(g);
    med.reDraw(g);
  }
}

class Mediator {
  boolean startRect;

  boolean dSelected;

  Vector drawings;

  Vector undoList;

  RectButton rectButton;

  FillButton fillButton;

  CircleButton circButton;

  PickButton arrowButton;

  JPanel canvas;

  Drawing selectedDrawing;

  StateManager stMgr;

  public Mediator() {
    startRect = false;
    dSelected = false;
    drawings = new Vector();
    undoList = new Vector();
    stMgr = new StateManager(this);
  }

  public void startRectangle() {
    stMgr.setRect();
    arrowButton.setSelected(false);
    circButton.setSelected(false);
    fillButton.setSelected(false);
  }

  public void startCircle() {
    stMgr.setCircle();
    rectButton.setSelected(false);
    arrowButton.setSelected(false);
    fillButton.setSelected(false);
  }

  public void startFill() {
    stMgr.setFill();
    rectButton.setSelected(false);
    circButton.setSelected(false);
    arrowButton.setSelected(false);
    stMgr.select(selectedDrawing, Color.red);
    repaint();
  }

  public void startArrow() {
    stMgr.setArrow();
    rectButton.setSelected(false);
    circButton.setSelected(false);
    fillButton.setSelected(false);
  }

  public Drawing getSelected() {
    return selectedDrawing;
  }

  public void fillSelected() {
    if (dSelected) {
      selectedDrawing.setFill(Color.red);
    }
  }

  public Vector getDrawings() {
    return drawings;
  }

  public void addDrawing(Drawing d) {
    drawings.addElement(d);
  }

  public void registerRectButton(RectButton rb) {
    rectButton = rb;
  }

  public void registerCircleButton(CircleButton cb) {
    circButton = cb;
  }

  public void registerArrowButton(PickButton ab) {
    arrowButton = ab;
  }

  public void registerFillButton(FillButton fb) {
    fillButton = fb;
  }

  public void registerCanvas(JPanel p) {
    canvas = p;
  }

  public void mouseDown(int x, int y) {
    stMgr.mouseDown(x, y);
    repaint();
  }

  public void mouseUp(int x, int y) {
    stMgr.mouseUp(x, y);
  }

  public void unpick() {
    dSelected = false;
    if (selectedDrawing != null) {
      selectedDrawing.setSelected(false);
      selectedDrawing = null;
      repaint();
    }
  }

  public void rememberPosition() {
    if (dSelected) {
      //Memento m = new Memento(d);
      //undoList.addElement(m);
    }
  }

  public void setSelected(Drawing d) {
    if (d != null) {
      dSelected = true;
      selectedDrawing = d;
      d.setSelected(true);
      repaint();
    }
  }

  public void pickRect(int x, int y) {

  }

  public void clear() {
    drawings = new Vector();
    undoList = new Vector();
    dSelected = false;
    selectedDrawing = null;
    repaint();
  }

  private void repaint() {
    canvas.repaint();
  }

  public void mouseDrag(int x, int y) {
    stMgr.mouseDrag(x, y);
  }

  public void reDraw(Graphics g) {
    g.setColor(Color.black);
    for (int i = 0; i < drawings.size(); i++) {
      Drawing v = (Drawing) drawings.elementAt(i);
      v.draw(g);
    }
  }

  public void undo() {
    if (undoList.size() > 0) {
      //get last element in undo list
      Object obj = undoList.lastElement();
      undoList.removeElement(obj); //and remove it
      //if this is an Integer, the last action was a new rectangle
      if (obj instanceof Integer) {
        //remove last created rectangle
        Object drawObj = drawings.lastElement();
        drawings.removeElement(drawObj);
      }
      //if this is a Memento, the last action was a move
      if (obj instanceof Memento) {
        //get the Memento
        Memento m = (Memento) obj;
        m.restore(); //and restore the old position
      }
      repaint();
    }
  }
}

class RectButton extends JToggleButton implements Command {
  Mediator med;

  public RectButton(ActionListener act, Mediator md) {
    super(" ");
    setSize(new Dimension(35, 35));
    setBorderPainted(true);
    setMargin(new Insets(5, 12, 5, 12));
    setToolTipText("Draw rectangle");
    addActionListener(act);
    med = md;
    med.registerRectButton(this);
  }

  public Dimension getPreferredSize() {
    return new Dimension(35, 35);
  }

  public void paint(Graphics g) {
    super.paint(g);
    int h = getHeight();
    int w = getWidth();
    g.setColor(Color.black);
    g.drawRect(4, 4, w - 8, h - 8);
  }

  public void Execute() {
    med.startRectangle();
  }

}

class UndoButton extends JButton implements Command {
  Mediator med;

  public UndoButton(ActionListener act, Mediator md) {
    super("U");
    //setSize(new Dimension(25,25));
    setMargin(new Insets(5, 12, 5, 12));
    setToolTipText("Undo");
    addActionListener(act);
    med = md;
  }

  public void Execute() {
    med.undo();
  }
}

class FillButton extends JToggleButton implements Command {
  Mediator med;

  public FillButton(ActionListener act, Mediator md) {
    super(" ");
    setSize(new Dimension(35, 35));
    setBorderPainted(true);
    setMargin(new Insets(5, 12, 5, 12));

    setToolTipText("Fill rectangle");
    addActionListener(act);
    med = md;
    med.registerFillButton(this);
  }

  public Dimension getPreferredSize() {
    return new Dimension(35, 35);
  }

  public void paint(Graphics g) {
    super.paint(g);
    int h = getHeight();
    int w = getWidth();
    g.setColor(Color.black);
    g.drawRect(4, 4, w - 8, h - 8);
    g.setColor(Color.red);
    g.fillRect(4, 10, w - 8, h - 14);
  }

  public void Execute() {
    if (isSelected()) {
      med.startFill();
    }
  }

}

class Memento {
  public Memento(Drawing d) {
  }

  public void restore() {
  }
}

class RectState extends State {
  private Mediator med; //save the Mediator here

  public RectState(Mediator md) {
    med = md;
  }

  //create a new Rectangle where mode clicks
  public void mouseDown(int x, int y) {
    med.addDrawing(new visRectangle(x, y));
  }
}

class StateManager {
  private State currentState;

  RectState rState;

  ArrowState aState;

  CircleState cState;

  FillState fState;

  public StateManager(Mediator med) {
    rState = new RectState(med);
    cState = new CircleState(med);
    aState = new ArrowState(med);
    fState = new FillState(med);
    currentState = aState;
  }

  public void setRect() {
    currentState = rState;
  }

  public void setCircle() {
    currentState = cState;
  }

  public void setFill() {
    currentState = fState;
  }

  public void setArrow() {
    currentState = aState;
  }

  public void mouseDown(int x, int y) {
    currentState.mouseDown(x, y);
  }

  public void mouseUp(int x, int y) {
    currentState.mouseUp(x, y);
  }

  public void mouseDrag(int x, int y) {
    currentState.mouseDrag(x, y);
  }

  public void select(Drawing d, Color c) {
    currentState.select(d, c);
  }
}

class Drawing {
  protected int x, y, w, h;

  protected Rectangle rect;

  protected boolean selected;

  protected boolean filled;

  protected Color fillColor;

  //-------------------------------------------
  public void setSelected(boolean b) {
    selected = b;
  }

  public void draw(Graphics g) {
  }

  public void move(int xpt, int ypt) {
    x = xpt;
    y = ypt;
  }

  public boolean contains(int x, int y) {
    return rect.contains(x, y);
  }

  protected void saveAsRect() {
    rect = new Rectangle(x - w / 2, y - h / 2, w, h);
  }

  protected void setFill(Color c) {
    filled = true;
    fillColor = c;
  }

}

class visCircle extends Drawing {

  public visCircle(int xpt, int ypt) {
    x = xpt;
    y = ypt;
    w = 40;
    h = 30;
    saveAsRect();
  }

  //-------------------------------------------
  public void draw(Graphics g) {
    g.drawArc(x, y, w, h, 0, 360);
    if (filled) {
      g.setColor(fillColor);
      g.fillArc(x, y, w, h, 0, 360);
    }
    if (selected) {
      g.setColor(Color.black);
      g.fillRect(x + w / 2, y - 2, 4, 4);
      g.fillRect(x - 2, y + h / 2, 4, 4);
      g.fillRect(x + w / 2, y + h - 2, 4, 4);
      g.fillRect(x + w - 2, y + h / 2, 4, 4);
    }
  }
}
//===============================================

class circleMemento extends Memento {
  visCircle circ;

  //saved fields- remember internal fields
  //of the specified visual rectangle
  int x, y, w, h;

  public circleMemento(visCircle r) {
    super(r);
    circ = r;
    x = circ.x;
    y = circ.y;
    w = circ.w;
    h = circ.h;
  }

  //-------------------------------------------
  public void restore() {
    //restore the internal state of
    //the specified rectangle
    circ.x = x;
    circ.y = y;
    circ.h = h;
    circ.w = w;
  }
}

class visRectangle extends Drawing {

  public visRectangle(int xpt, int ypt) {
    x = xpt;
    y = ypt;
    w = 40;
    h = 30;
    saveAsRect();
  }

  //-------------------------------------------
  public void draw(Graphics g) {
    g.drawRect(x, y, w, h);
    if (filled) {
      g.setColor(fillColor);
      g.fillRect(x, y, w, h);
    }
    if (selected) {
      g.setColor(Color.black);
      g.fillRect(x + w / 2, y - 2, 4, 4);
      g.fillRect(x - 2, y + h / 2, 4, 4);
      g.fillRect(x + w / 2, y + h - 2, 4, 4);
      g.fillRect(x + w - 2, y + h / 2, 4, 4);
    }
  }
}


class rectMemento extends Memento {
  visRectangle rect;

  //saved fields- remember internal fields
  //of the specified visual rectangle
  int x, y, w, h;

  public rectMemento(visRectangle r) {
    super(r);
    rect = r;
    x = rect.x;
    y = rect.y;
    w = rect.w;
    h = rect.h;
  }

  //-------------------------------------------
  public void restore() {
    //restore the internal state of
    //the specified rectangle
    rect.x = x;
    rect.y = y;
    rect.h = h;
    rect.w = w;
  }
}

class PickButton extends JToggleButton implements Command {
  Mediator med;

  public PickButton(ActionListener act, Mediator md) {
    super(new ImageIcon("arrow.gif"));
    setSize(new Dimension(35, 35));
    setBorderPainted(true);
    setMargin(new Insets(0, 0, 0, 0));
    setToolTipText("Select drawing element");
    addActionListener(act);
    med = md;
    med.registerArrowButton(this);
  }

  //-------------------------------------------
  public void Execute() {
    med.startArrow();
  }

}