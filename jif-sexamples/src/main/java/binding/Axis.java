/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binding;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.l2fprod.common.swing.LookAndFeelTweaks;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.beans.PropertyChangeSupport;
import java.text.NumberFormat;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;

/**
 *
 * @author GBH
 */
public class Axis {

   public static final String PROP_VALUE = "PROP_VALUE";
   String label;
   JScrollBar scrollbar;
   public ValueHolder value = new ValueHolder(0);
   int min;
   int max;

   public Axis(String label, int initValue, int min, int max) {
      this.label = label;

      this.value.setValue(initValue);
      this.min = min;
      this.max = max;
      this.scrollbar = new JScrollBar();
      scrollbar.setModel((new BoundedRangeAdapter(value, 0, 0, 100)));

   }

   public JComponent getBar() {
      // create a scrollbar...
      JComponent bar = new JPanel();
      bar.setLayout(null);
      JLabel label = BasicComponentFactory.createLabel(ConverterFactory.
              createStringConverter(value, NumberFormat.getIntegerInstance()));
      JSlider slider = new JSlider();
      slider.setModel(new BoundedRangeAdapter(value, 0, 0, 100));
           
      return bar;
   }

   public static void main(String[] args) {
      Axis c = new Axis("Channel", 0, 1, 100);
      Axis z = new Axis("Z", 0, 1, 100);
      Axis t = new Axis("Time", 0, 1, 100);
      LookAndFeelTweaks.tweak();
      //String name = pm.getBean().getClass().getName();
//      JFrame frame = new JFrame(name.substring(name.length() - 20, name.length() - 1));
      JFrame frame = new JFrame("Axes");
      frame.getContentPane().setLayout(new BorderLayout());
      frame.getContentPane().add(c.getBar(), BorderLayout.NORTH);
      frame.getContentPane().add(z.getBar(), BorderLayout.CENTER);
      frame.getContentPane().add(t.getBar(), BorderLayout.SOUTH);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.pack();
      frame.setBounds(100,100, 400,400);
      frame.setVisible(true);
   }
}
