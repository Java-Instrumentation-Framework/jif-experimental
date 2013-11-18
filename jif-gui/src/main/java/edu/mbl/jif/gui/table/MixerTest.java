package edu.mbl.jif.gui.table;

/*
Java Swing, 2nd Edition
By Marc Loy, Robert Eckstein, Dave Wood, James Elliott, Brian Cole
ISBN: 0-596-00408-7
Publisher: O'Reilly
*/
// MixerTest.java
//A test application for showing Volume data in a JTable using the
//custom VolumneRenderer class.
//

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class MixerTest extends JFrame {

  public MixerTest() {
    super("Customer Editor Test");
    setSize(600, 160);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    MixerModel test = new MixerModel();
    test.dump();
    JTable jt = new JTable(test);
    jt.setDefaultRenderer(Volume.class, new VolumeRenderer());
    JScrollPane jsp = new JScrollPane(jt);
    getContentPane().add(jsp, BorderLayout.CENTER);
  }

  public static void main(String args[]) {
    MixerTest mt = new MixerTest();
    mt.setVisible(true);
  }
}
//MixerModel.java
//An audio mixer table data model. This model contains the following columns:
//<br> + Track name (String)
//<br> + Track start time (String)
//<br> + Track stop time (String)
//<br> + Left channel volume (Volume, 0 . . 100)
//<br> + Right channel volume (Volume, 0 . . 100)
//

class MixerModel extends AbstractTableModel {

  String headers[] = { "Track", "Start", "Stop", "Left Volume",
      "Right Volume" };

  Class columnClasses[] = { String.class, String.class, String.class,
      Volume.class, Volume.class };

  Object data[][] = {
      { "Bass", "0:00:000", "1:00:000", new Volume(56), new Volume(56) },
      { "Strings", "0:00:000", "0:52:010", new Volume(72), new Volume(52) },
      { "Brass", "0:08:000", "1:00:000", new Volume(99), new Volume(0) },
      { "Wind", "0:08:000", "1:00:000", new Volume(0), new Volume(99) }, };

  public int getRowCount() {
    return data.length;
  }

  public int getColumnCount() {
    return headers.length;
  }

  public Class getColumnClass(int c) {
    return columnClasses[c];
  }

  public String getColumnName(int c) {
    return headers[c];
  }

  public boolean isCellEditable(int r, int c) {
    return true;
  }

  public Object getValueAt(int r, int c) {
    return data[r][c];
  }

  // Ok, do something extra here so that if we get a String object back (from
  // a
  // text field editor) we can still store that as a valid Volume object. If
  // it's just a string, then stick it directly into our data array.
  public void setValueAt(Object value, int r, int c) {
    if (c >= 3) {
      ((Volume) data[r][c]).setVolume(value);
    } else {
      data[r][c] = value;
    }
  }

  // A quick debugging utility to dump out the contents of our data structure
  public void dump() {
    for (int i = 0; i < data.length; i++) {
      System.out.print("|");
      for (int j = 0; j < data[0].length; j++) {
        System.out.print(data[i][j] + "|");
      }
      System.out.println();
    }
  }
}

//Volume.java
//A simple data structure for track volumes on a mixer.
//

class Volume {
  private int volume;

  public Volume(int v) {
    setVolume(v);
  }

  public Volume() {
    this(50);
  }

  public void setVolume(int v) {
    volume = (v < 0 ? 0 : v > 100 ? 100 : v);
  }

  public void setVolume(Object v) {
    if (v instanceof String) {
      setVolume(Integer.parseInt((String) v));
    } else if (v instanceof Number) {
      setVolume(((Number) v).intValue());
    } else if (v instanceof Volume) {
      setVolume(((Volume) v).getVolume());
    }
  }

  public int getVolume() {
    return volume;
  }

  public String toString() {
    return String.valueOf(volume);
  }
}

//VolumeRenderer.java
//A slider renderer for volume values in a table.
//

class VolumeRenderer extends JSlider implements TableCellRenderer {

  public VolumeRenderer() {
    super(SwingConstants.HORIZONTAL);
    // set a starting size...some 1.2/1.3 systems need this
    setSize(115, 15);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    if (value == null) {
      return this;
    }
    if (value instanceof Volume) {
      setValue(((Volume) value).getVolume());
    } else {
      setValue(0);
    }
    return this;
  }
}
