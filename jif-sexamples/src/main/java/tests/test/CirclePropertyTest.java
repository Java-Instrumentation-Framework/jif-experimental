 package tests.test;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.beans.*;


// **************************************************************
class ActiveObj_PropertyChangeListener
      implements PropertyChangeListener
{
   public void propertyChange (PropertyChangeEvent evt) {
      System.out.println("ActiveObj_PropertyChangeListener: "
                         + ((Circle) evt.getSource()).getRadius() + " : " +
                         +((Circle) evt.getSource()).getCircumference());
   }
}



// **************************************************************
class XCircle
      extends Circle
{

   PropertyChangeSupport pcs;

   public XCircle (double r) {
      super(r);
      pcs = new PropertyChangeSupport(this);
   }


   public void setRadius (double r) {
      super.setRadius(r);
      if (pcs != null) {
         pcs.firePropertyChange("Radius", 0, 1);
      }
   }


   public void setCircumference (double c) {
      super.setCircumference(c);
      pcs.firePropertyChange("Circumference", null, null);
   }


   public void addPropertyChangeListener (PropertyChangeListener listener) {
      pcs.addPropertyChangeListener(listener);
   }
}



// **************************************************************
class Circle
{

   double radius, circumference;

   public Circle (double r) {
      setRadius(r);
   }


   public void setRadius (double r) {
      radius = r;
      circumference = 2 * Math.PI * r;
   }


   public double getRadius () {
      return radius;
   }


   public void setCircumference (double c) {
      circumference = c;
      radius = circumference / 2 / 3.142;
   }


   public double getCircumference () {
      return circumference;
   }
}



// **************************************************************
class CircleFrame
      extends JFrame implements ActionListener, PropertyChangeListener
{

   JTextField radius;
   JTextField circumference;

   XCircle aCircle;

   public CircleFrame (XCircle circleObject) {
      setTitle("PopQuiz");
      setSize(350, 70);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      Container c = getContentPane();
      c.setLayout(new FlowLayout());

      c.add(new JLabel("       Radius:"));
      radius = new JTextField(5);
      radius.addActionListener(this);
      c.add(radius);

      c.add(new JLabel("Circumference:"));
      circumference = new JTextField(5);
      circumference.addActionListener(this);
      c.add(circumference);

      setVisible(true);

      aCircle = circleObject;
      aCircle.addPropertyChangeListener(this);
   }


   // convert given radius to circumference or vice versa
   public void actionPerformed (ActionEvent e) {

      if (e.getSource() == radius) {
         double newRadius = Double.parseDouble(radius.getText());
         aCircle.setRadius(newRadius);
      } else {
         double newCircumference = Double.parseDouble(circumference.getText());
         aCircle.setCircumference(newCircumference);
      }
   }


   public void propertyChange (PropertyChangeEvent evt) {
      DecimalFormat fmt = new DecimalFormat("0.##");
      Circle oCircle = (Circle) evt.getSource();
      if (evt.getPropertyName() == "Radius") {
         circumference.setText(fmt.format(oCircle.getCircumference()));
      } else {
         radius.setText(fmt.format(oCircle.getRadius()));
      }
   }
}



// **************************************************************
// **************************************************************
public class CirclePropertyTest
{
   public static void main (String[] args) {
      XCircle aCircle = new XCircle(0);
      CircleFrame circles = new CircleFrame(aCircle);
      aCircle.addPropertyChangeListener(new ActiveObj_PropertyChangeListener());
   }
}
