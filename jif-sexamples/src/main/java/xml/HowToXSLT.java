/*
 * from: http://www.rgagnon.com/javadetails/java-0407.html
 */
package xml;

import javax.xml.transform.*;
import java.net.*;
import java.io.*;

public class HowToXSLT {
public static void main(String[] args) {
  try {

    TransformerFactory tFactory = TransformerFactory.newInstance();

    Transformer transformer =
      tFactory.newTransformer
         (new javax.xml.transform.stream.StreamSource
            ("howto.xsl"));

    transformer.transform
      (new javax.xml.transform.stream.StreamSource
            ("howto.xml"),
       new javax.xml.transform.stream.StreamResult
            ( new FileOutputStream("howto.html")));
    }
  catch (Exception e) {
    e.printStackTrace( );
    }
  }
}