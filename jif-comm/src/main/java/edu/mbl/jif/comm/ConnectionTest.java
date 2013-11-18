package edu.mbl.jif.comm;

import gnu.io.*;
import java.util.Enumeration;
import java.util.Vector;
import java.io.*;

/**
 * A simple test-program that queries the GPS for a description of itself. <br/>
 * A good way to initially test the connection to the GPS. <br/>
 * Attempts to turn off the GPS after retrieving the description.
 */

public class ConnectionTest {
  /** The communication port being used. */
  String portname;
  BufferedReader inuser;
  CommPort port;
  BufferedInputStream input;
  BufferedOutputStream output;

  public static void main(String args[]) {
    new ConnectionTest();
  }

  public ConnectionTest() {
    inuser = new BufferedReader(new InputStreamReader(System.in));
    String portname = ListPorts();
    try {
      port = CommPortIdentifier.getPortIdentifier(port).open("ConnectionTest",
          3000);
    }
    catch (NoSuchPortException e) {
      System.out.println("Port not found!");
      return;
    }
    catch (PortInUseException e) {
      System.out.println("Port already in use by " + e.currentOwner);
      return;
    }
    try {
      input = new BufferedInputStream(port.getInputStream());
      output = new BufferedOutputStream(port.getOutputStream());
    }
    catch (IOException e) {
      System.out.println("Error opening port " + portname);
      return;
    }
    try {
      Thread.sleep(1500);
    }
    catch (InterruptedException e) {}
    System.out.println("Connected to :");
  }

  /**
   * Ok. Ridiculous method, but I'm hoping that more GPS-types will be added later.
   * Lists the types of GPS that are available and lets the user pick one.
   */

  private String ListPorts() {
    Vector names = null;
    int index = -1;
    while (index == -1) {
      int j = 1;
      names = new Vector();
      System.out.println("Available ports: ");
      CommPortIdentifier c;
      for (Enumeration i = CommPortIdentifier.getPortIdentifiers();
           i.hasMoreElements(); ) {
        c = (CommPortIdentifier) i.nextElement();
        System.out.print(j++ +". " + c.getName());
        names.add(c.getName());
        if (c.getPortType() == CommPortIdentifier.PORT_SERIAL) {
          System.out.print("\t SERIAL\n");
        }
        if (c.getPortType() == CommPortIdentifier.PORT_PARALLEL) {
          System.out.print("\t PARALLEL\n");
        }
      }
      System.out.print("Select port: ");
      String input = readFromUser();

      try {
        index = Integer.parseInt(input);
      }
      catch (NumberFormatException e) {
        index = -1;
        continue;
      }

      if ( (index < 1) || (index >= names.size())) {
        index = -1;
        continue;
      }
    }

    return names.elementAt(index - 1).toString();
  }

  public String readFromUser() {
    try {
      return inuser.readLine();
    }
    catch (IOException e) {
      return "";
    }
  }
}