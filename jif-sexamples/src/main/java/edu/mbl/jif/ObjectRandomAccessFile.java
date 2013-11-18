package edu.mbl.jif;

import java.io.*;

public class ObjectRandomAccessFile
    extends RandomAccessFile {

  /*
     File format:
     header: 32 bytes
     block:
            int count    4 bytes
            object       count bytes
   */

  protected ByteArrayOutputStream bout;
  protected byte[] buf;

  public ObjectRandomAccessFile(String name, String mode) throws IOException {
    super(name, mode);
    bout = new ByteArrayOutputStream();
  }

  public ObjectRandomAccessFile(File file, String mode) throws IOException {
    super(file, mode);
    bout = new ByteArrayOutputStream();
  }

  public int writeObject(Object obj) throws IOException {
    if (obj != null &&
        obj instanceof Serializable) {
      ObjectOutputStream objout = new ObjectOutputStream(bout);
      objout.writeObject(obj);
      int count = bout.size();
      byte[] buf = bout.toByteArray();
      writeInt(count);
      write(buf, 0, count);
      bout.reset();
      return count + 4;
    }
    else {
      return 0;
    }
  }

  public Object readObject() throws IOException, ClassNotFoundException {
    int count = readInt();
    if (buf == null ||
        count > buf.length) {
      buf = new byte[count];
    }
    read(buf, 0, count);
    ObjectInputStream objin =
        new ObjectInputStream(new ByteArrayInputStream(buf, 0, count));
    Object obj = objin.readObject();
    objin.close();
    return obj;
  }

  public static void main(String[] args) {
    //if (args.length > 0) {
    try {
      ObjectRandomAccessFile out = new ObjectRandomAccessFile("testObj", "rw");
      Object[] obj = {
          "Tic", "Tac", "Toe"};
      long offset = 0;
      int count;
      for (int i = 0; i < obj.length; i++) {
        count = out.writeObject(obj[i]);
        System.out.println(obj[i] + " written at offset " + offset +
                           " size = " + count);
        offset += count;
      }
    }
    catch (IOException e) {}
    // }
  }
}
