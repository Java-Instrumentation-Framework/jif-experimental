package edu.mbl.jif.oidic;

import edu.mbl.jif.utils.FileUtil;



public class DotNetBridge {

  private native void Initialize(String localPath);

  public native String GetSerialNumber();

  public native boolean SetDACVoltage(double volts, byte dac);
  

  static {

    String path = DotNetBridge.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    int idx = path.lastIndexOf("/");
    if (idx >= 0) {
      path = path.substring(0, idx);
    }
    if (path.startsWith("/")) {
      path = path.substring(1);
    }
    System.out.println("path = " + path);

    path = FileUtil.getcwd();

    try {
      System.load(path + "/DotNetBridge.dll");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception loading: " + path + "/DotNetBridge.dll");
    }
    System.out.println("Loaded: " + path + "/DotNetBridge.dll");

    //loadDLL();

    DotNetBridge dotNetBridge = new DotNetBridge();

    dotNetBridge.Initialize(path);
  }

  public static boolean loadDLL() {
    try {
      System.loadLibrary("DotNetBridge");  // assuming .dll is in java.library.path
      System.out.println("Loaded DotNetBridge");
      return true;
    } catch (Error e) {
      System.out.println(
              "Could not load DotNetBridge.dll\njava.library.path= " + System.getProperty("java.library.path"));
      return false;
    }
  }
}

