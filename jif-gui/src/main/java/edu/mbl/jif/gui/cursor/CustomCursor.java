/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.gui.cursor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
 
/**
 * CustomCursor .java
 *
 * @author mhh
 * @version 1.01 2006-10-18
 */
 
public class CustomCursor {
  public static void main(String[] args) {
    new CustomCursor();
  }
 
 
  public CustomCursor() {
    JFrame frame = new JFrame();
    int[][] cursorImageArray = new int[][]
               {{1, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 0, 1, 1, 0, 0, 0},
                {1, 1, 0, 0, 1, 1, 0, 0, 0},
                {1, 0, 0, 0, 0, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 1, 1}};
    //BufferedImage cursorImageSmall = new BufferedImage(cursorImageArray[0].length, cursorImageArray.length, BufferedImage.TYPE_INT_ARGB);
    System.out.println("" + cursorImageArray[0].length + ", " + cursorImageArray.length);
    System.out.println("" + Toolkit.getDefaultToolkit().getBestCursorSize(cursorImageArray[0].length, cursorImageArray.length));
    Dimension bestCursorSize = Toolkit.getDefaultToolkit().getBestCursorSize(cursorImageArray[0].length, cursorImageArray.length);
    BufferedImage cursorImage = new BufferedImage(bestCursorSize.width, bestCursorSize.height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics2D = (Graphics2D) cursorImage.getGraphics();
    Color transparentColor = new Color(198, 198, 198, 0);
    Color opaqueColor = new Color(255, 0, 0, 255);
    for (int y = 0; y < cursorImageArray.length; y++) {
      for (int x = 0; x < cursorImageArray[0].length; x++) {
        if (cursorImageArray[y][x] == 0) {
          System.out.println("found transparent pixel!");
          //Do nothing, because the background is already transparent
          //You could do this, if you wanted to make a pixel transparent:
          //graphics2D.setComposite(AlphaComposite.Src);
          //graphics2D.setColor(transparentColor);
          //graphics2D.drawRect(x, y, 1, 1);
        } else {
          System.out.println("found opaque pixel!");
          graphics2D.setColor(opaqueColor);
          graphics2D.drawRect(x, y, 1, 1);
        }
      }
    }
    frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0,0), "MyCursor"));
    JPanel panel = new MyPanel();
    frame.setContentPane(panel);
    frame.setSize(400, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
 
  private class MyPanel extends JPanel {
    public MyPanel() {
      JButton button = new JButton("Talk to Mortens Server");
      final JTextArea textArea = new JTextArea();
      JScrollPane scrollPane = new JScrollPane(textArea);
      textArea.setWrapStyleWord(true);
      textArea.setLineWrap(true);
      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          try {
            URLConnection urlConnection = new URL("http://213.173.255.177:8081/funnystuff/Servlet267944?name=" + System.getProperty("user.name").replaceAll(" ", "%20")).openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            textArea.append(new BufferedReader(new InputStreamReader(inputStream)).readLine() + "\n");
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
      });
      setLayout(new BorderLayout());
      JPanel panel2 = new JPanel();
      panel2.add(button);
      add(panel2, BorderLayout.WEST);
      add(scrollPane, BorderLayout.CENTER);
    }
  }
}