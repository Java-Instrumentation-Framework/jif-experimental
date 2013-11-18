package edu.mbl.jif.gui.text;
/*

   Copyright 2006  Herve Girod

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

import edu.mbl.jif.gui.test.FrameForTest;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/** Presents a scrollable text area for user messages.
 *  @version 0.2
 */
public class StyledMessageArea extends JPanel {
    private StylableSizableArea area;
    private HTMLDocument doc;      
    private HTMLEditorKit kit = new HTMLEditorKit();
    private JFrame frame;

    private AbstractAction clearAction;
    
    /** Default height of the Message Area.
     */
    public static final int DEFAULT_ROWS = 8;
    
    /** Create a new StyledMessageArea, with a default number of rows.
     */
    public StyledMessageArea(Component parent) {    
        this(parent, DEFAULT_ROWS);
    }
  
    /** Create a new StyledMessageArea.
     * @param parent the parent component
     * @param rows the number of rows of this area
     */
    public StyledMessageArea(Component parent, int rows) {
        super();
        this.setPreferredSize(new Dimension(500,400));
        initializeHTMLContent(rows);
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(area), BorderLayout.CENTER);
        
        frame = parent instanceof JFrame ? (JFrame) parent
        : (JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, parent);
        
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resize();
            }
            public void componentShown(ComponentEvent e) {
                resize();
            }
        });
        area.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    clear(e.getX(), e.getY());
                }
            }
        });
        
        clearAction = new AbstractAction("clear") {
            public void actionPerformed(ActionEvent e) {
               clear();
            }
        };
        
    }
        
    public static void main(String[] args)
      {
        FrameForTest f = new FrameForTest();
				//f.setBounds(100,100,500,400);
        StyledMessageArea ma = new StyledMessageArea(f);
        f.addContents(ma);
        ma.append("<h1>This is a test</h1>");
				ma.append("Something with a time.", 100);
      }
    
    private void initializeHTMLContent(int rows) {
        area = new StylableSizableArea(rows);
        area.setEditorKit(kit);
        initDocument(); 
    }
    
    private void resize() {
        Dimension d = frame.getContentPane().getSize();
        Insets insets = frame.getContentPane().getInsets();
        d.setSize(d.width - insets.right - insets.left-10, 
            area.getPreferredScrollableViewportSize().height);
        area.setSize(d);
        area.setPreferredScrollableViewportSize(d);
    }
            
    /** Clear the content of the message area and resize it accordingly.
     */
    public void clear() {
        initDocument();
        resize();
    }
        
    private void initDocument() {
        doc = (HTMLDocument)kit.createDefaultDocument();
        area.setDocument(doc);
        area.setEditable(true);  
    }
    
    private void clear(int x, int y) {        
        JPopupMenu menu = new JPopupMenu();
        menu.add(new JMenuItem(clearAction));
        menu.show(area, x, y);
    }
    
    /** Append a line of text in the message area.
     */
    public void append(String txt) {
        appendText(txt+"\n");
        resize();
    }
    
    /** Append a line of text in the message area.
     */
    public void append(String txt, long time) {
        appendText(txt + " in " + time + " ms\n");
        resize();
    }    
        
    private void appendText(String text) {
        try {
	    Reader r = new StringReader(text);
            kit.read(r, doc, doc.getLength());
            area.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {      
        } catch (IOException e) { 
        }
    }
}
/** A stylable text area whose preferred scrollable size can be forced to any value.
 *  @since 0.1
 */
 class StylableSizableArea extends JTextPane {
    private Dimension d;  
    private int rows = 1;
    private int columns = 1;    

    /** Create a new SizableArea.
     * @param rows the number of rows of the area
     */
    public StylableSizableArea(int rows) {
        super();
        this.rows = rows;
        JTextArea area = new JTextArea(rows, 1);
        d = area.getPreferredSize();
    }
    
    /** Create a new SizableArea.
     * @param rows the number of rows of the area
     * @param font the Font used to set the text of the area
     */
    public StylableSizableArea(int rows, Font font) {
        super();
        this.rows = rows;
        this.setFont(font);
        JTextArea area = new JTextArea(rows, 1);        
        area.setFont(font);
        d = area.getPreferredSize();
    }    

    /** Create a new SizableArea.
     * @param columns the number of columns of the area
     * @param rows the number of rows of the area
     */    
    public StylableSizableArea(int columns, int rows) {
        super();
        this.rows = rows;
        this.columns = columns;
        initializeSize(); 
    }    

    /** Create a new SizableArea.
     * @param columns the number of columns of the area
     * @param rows the number of rows of the area
     * @param font the Font used to set the text of the area
     */        
    public StylableSizableArea(int columns, int rows, Font font) {
        super();
        this.rows = rows;
        this.columns = columns;        
        this.setFont(font);
        initializeSize();         
    }
    
    private void initializeSize() {
        JTextArea area = new JTextArea(rows, columns);        
        area.setFont(getFont());        
        d = area.getPreferredSize();        
    }
    
    public void setPreferredScrollableViewportSize(Dimension dim) {
        d = dim;
    }    
    
    public Dimension getPreferredScrollableViewportSize() {
        return d;
    }

}