package binding;

//import edu.mbl.jif.camera.*;
//import edu.mbl.jif.utils.FileUtil;

import java.awt.Color;

import java.io.IOException;

import java.util.*;

import javax.swing.ListModel;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.common.collect.ArrayListModel;
import com.jgoodies.common.collect.ObservableList;

import com.jgoodies.forms.layout.ColumnSpec;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ExampleModelGBH extends Model {
   
   // Names of the Bound Bean Properties *************************************
   public static final String PROPERTYNAME_BOOLEAN_VALUE = "booleanValue";
   public static final String PROPERTYNAME_COLOR = "color";
   public static final String PROPERTYNAME_DATE = "date";
   public static final String PROPERTYNAME_FLOAT_VALUE = "floatValue";
   public static final String PROPERTYNAME_INT_CHOICE = "intChoice";
   public static final String PROPERTYNAME_INT_LIMITED = "intLimited";
   public static final String PROPERTYNAME_INT_VALUE = "intValue";
   public static final String PROPERTYNAME_LIST_SELECTION = "listSelection";
   public static final String PROPERTYNAME_LONG_VALUE = "longValue";
   public static final String PROPERTYNAME_OBJECT_CHOICE = "objectChoice";
   public static final String PROPERTYNAME_TEXT = "text";

   // Constants **************************************************************

   // An int based enumeration.
   public static final Integer LEFT_INTEGER = new Integer(0);
   public static final Integer CENTER_INTEGER = new Integer(1);
   public static final Integer RIGHT_INTEGER = new Integer(2);
   public static final Integer[]      INTEGER_CHOICES = { LEFT_INTEGER, CENTER_INTEGER, RIGHT_INTEGER };

   // An object based enumeration (using an enum from the JGoodies Forms)
   public static final Object LEFT = ColumnSpec.LEFT;
   public static final Object CENTER = ColumnSpec.CENTER;
   public static final Object RIGHT = ColumnSpec.RIGHT;
   public static final Object[]      OBJECT_CHOICES = { LEFT, CENTER, RIGHT };
   private static final int   NO_DATE = -1;

   // Fields *****************************************************************
   private boolean        booleanValue;
   private Color          color;
   private long           date;
   private float         floatValue;
   private int            intChoice;
   private int            intLimited; // for a spinner
   private int            intValue;
   private long           longValue;
   private Object         objectChoice;
   private String         text;
   private ObservableList listModel;
   private Object         listSelection;


   // Instance Creation *****************************************************
// CameraInterface camera;
   
//   public ExampleModelGBH(CameraInterface camera) {
//   this.camera = camera;
//   }
//      
   public ExampleModelGBH() {
      // initial values...
      booleanValue    = true;
      color           = Color.WHITE;
      date            = new GregorianCalendar(1967, 11, 5).getTime().getTime();
      floatValue      = 0.5f;
      intChoice       = LEFT_INTEGER.intValue();
      intLimited      = 15;
      intValue        = 42;
      longValue       = 42L;
      objectChoice    = LEFT;
      text            = "Text";
      listModel       = new ArrayListModel();
      //listModel.addAll(Collections.list(Modes));
      ArrayList list = new ArrayList();
      list.add("First");
      list.add("Second");
      list.add("Third");
      listModel.addAll(list);
      listSelection = listModel.get(0);
   }
   
//   public CameraInterface getCamera() {
//      return camera;
//   }

   // Accessors **************************************************************
   public boolean getBooleanValue() {
      return booleanValue;
   }

   public void setBooleanValue(boolean newBooleanValue) {
      boolean oldBooleanValue = getBooleanValue();
      booleanValue = newBooleanValue;
      firePropertyChange(PROPERTYNAME_BOOLEAN_VALUE, oldBooleanValue, newBooleanValue);
   }

   public Color getColor() {
      return color;
   }

   public void setColor(Color newColor) {
      Color oldColor = getColor();
      color = newColor;
      firePropertyChange(PROPERTYNAME_COLOR, oldColor, newColor);
   }

   public Date getDate() {
      return (date == NO_DATE) ? null : new Date(date);
   }

   public void setDate(Date newDate) {
      Date oldDate = getDate();
      date = (newDate == null) ? NO_DATE : newDate.getTime();
      firePropertyChange(PROPERTYNAME_DATE, oldDate, newDate);
   }

   public float getFloatValue() {
      return floatValue;
   }

   public void setFloatValue(float newFloatValue) {
      float oldFloatValue = getFloatValue();
      floatValue = newFloatValue;
      firePropertyChange(PROPERTYNAME_FLOAT_VALUE, oldFloatValue, newFloatValue);
   }

   public int getIntChoice() {
      return intChoice;
   }

   public void setIntChoice(int newIntChoice) {
      int oldIntChoice = getIntChoice();
      intChoice = newIntChoice;
      firePropertyChange(PROPERTYNAME_INT_CHOICE, oldIntChoice, newIntChoice);
   }

   public int getIntLimited() {
      return intLimited;
   }

   public void setIntLimited(int newIntLimited) {
      int oldIntLimited = getIntLimited();
      intLimited = newIntLimited;
      firePropertyChange(PROPERTYNAME_INT_LIMITED, oldIntLimited, newIntLimited);
   }

   public int getIntValue() {
      return intValue;
   }

   public void setIntValue(int newIntValue) {
      int oldIntValue = getIntValue();
      intValue = newIntValue;
      firePropertyChange(PROPERTYNAME_INT_VALUE, oldIntValue, newIntValue);
   }

   public long getLongValue() {
      return longValue;
   }

   public void setLongValue(long newLongValue) {
      long oldLongValue = getLongValue();
      longValue = newLongValue;
      firePropertyChange(PROPERTYNAME_LONG_VALUE, oldLongValue, newLongValue);
   }

   public Object getObjectChoice() {
      return objectChoice;
   }

   public void setObjectChoice(Object newObjectChoice) {
      Object oldObjectChoice = getObjectChoice();
      objectChoice = newObjectChoice;
      firePropertyChange(PROPERTYNAME_OBJECT_CHOICE, oldObjectChoice, newObjectChoice);
   }

   public String getText() {
      return text;
   }

   public void setText(String newText) {
      String oldText = getText();
      text = newText;
      firePropertyChange(PROPERTYNAME_TEXT, oldText, newText);
   }

   public ListModel getListModel() {
      return listModel;
   }

   public Object getListSelection() {
      return listSelection;
   }

   public void setListSelection(Object newListSelection) {
      Object oldListSelection = getListSelection();
      listSelection = newListSelection;
      firePropertyChange(PROPERTYNAME_LIST_SELECTION, oldListSelection, newListSelection);
   }

// Save -----------------------------------------------------
   // Save to XML using XStream
   // ** added by GBH
//   public void save(String file) {
//      XStream xstream = new XStream(new DomDriver());
//      String  xml = xstream.toXML(this);
//      System.out.println(xml);
//      try {
//         FileUtil.saveTxtFile(file, xml, false);
//      } catch (IOException ex) {
//         ex.printStackTrace();
//      }
//   }
//
// public void load(String file) {
//      XStream xstream = new XStream(new DomDriver());
//      String  xml = xstream.toXML(this);
//      System.out.println(xml);
//      try {
//         FileUtil.saveTxtFile(file, xml, false);
//      } catch (IOException ex) {
//         ex.printStackTrace();
//      }
//   }
 

}
