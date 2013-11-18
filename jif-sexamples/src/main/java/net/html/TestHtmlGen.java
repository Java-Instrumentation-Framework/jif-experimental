/*
 * TestHtmlGen.java
 *
 * Created on July 20, 2006, 3:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.html;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.ecs.AlignType;
import org.apache.ecs.Document;
import org.apache.ecs.HtmlColor;
import org.apache.ecs.html.A;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Body;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.H2;
import org.apache.ecs.html.H3;
import org.apache.ecs.html.H4;
import org.apache.ecs.html.H5;
import org.apache.ecs.html.H6;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.Head;
import org.apache.ecs.html.Html;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Meta;
import org.apache.ecs.html.P;
import org.apache.ecs.html.Span;
import org.apache.ecs.html.Strong;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.html.Title;


/**
 *
 * @author GBH
 */
public class TestHtmlGen {
   /** Creates a new instance of TestHtmlGen */
   public TestHtmlGen() {
   }

   public void testHtml5() {
      Span span = new Span("Some Name");
      span.setClass("goober");
      Div   div = new Div("Last name: " + span.toString());
      Title t = new Title("Test Doc");

      Meta  m = new Meta();
      m.setName("keywords");
      m.setContent("test,testing,testbed");

      Head h = new Head();
      h.addElement(m);
      h.addElement(t);
      Html html = new Html(h);

      H1   h1 = new H1("test");
      H2   h2 = new H2("test2");
      H3   h3 = new H3("test3");
      H4   h4 = new H4("test4");
      H5   h5 = new H5("test5");
      H6   h6 = new H6("test6");

      IMG  img = new IMG("/images/file.gif");

      A    a = new A("/gohere/", img);

      Body body = new Body(HtmlColor.lightyellow);
      body.addElement(a);
      body.addElement(div);
      body.addElement(h1);
      body.addElement(h2);
      body.addElement(h3);
      body.addElement(h4);
      body.addElement(h5);
      body.addElement(h6);
      Table tbl =
         new Table().addElement(new TR(true).addElement(new TD(true).addElement(
                  "One greate big dog > one little dog and one little dog < a cat\""))
                                            .addElement(new TD(true).setColSpan(2).addElement("r1c2")))
                    .addElement(new TR(true).addElement(new TD(true).addElement("r2c1"))
                                            .addElement(new TD(true).addElement("r2c2"))
                                            .addElement(new TD(true).addElement("r2c3")))
                    .addElement(new TR(true).addElement(new TD(true).addElement("r3c1"))
                                            .addElement(new TD(true).addElement("r3c2"))
                                            .addElement(new TD(true).addElement("r3c3")));
      body.addElement(tbl);
      html.addElement(body);
      writeToFile(html, "testHTML5");
      System.out.println(html.toString());
   }

   public void testDocument() {
      Document doc = new Document();
      Head     h = doc.getHead();
      Body     b = doc.getBody();
      b.setAlink("#555555");
      b.setVlink(HtmlColor.AQUA);
      b.setBgColor(HtmlColor.red);
      b.addElement("&nbsp");
      doc.appendTitle("Test Document");
      Meta meta = new Meta();
      meta.setContent("this,is,a,test,document");
      meta.setName("keywords");
      h.addElement(meta);

      H1  headline = new H1("Test Document");
      BR  br = new BR();
      IMG img = new IMG("/images/image.gif", "test", 0);
      b.addElement(headline);
      b.addElement(br);
      b.addElement(img);
      doc.appendBody(new Strong("TESTING"));
      doc.appendBody("Testing");

      // For testing. OutputStream
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      doc.output(baos);
      try {
         baos.writeTo(System.out);
      } catch (Exception e) {
         System.out.println(e.toString());
      }

      // For testing PrintWriter
      PrintWriter out = new PrintWriter(System.out);
      doc.output(out);
      out.flush();

      Html doc1 = (Html)doc.clone();
      doc1.setPrettyPrint(false);
      baos = new ByteArrayOutputStream();
      doc1.output(baos);
      try {
         baos.writeTo(System.out);
      } catch (Exception e) {
         System.out.println(e.toString());
      }

      // For testing PrintWriter
      out = new PrintWriter(System.out);
      doc1.output(out);
      out.flush();
   }

   public void testDocument1() {
      Document                  doc = new Document();
      Head                      h = doc.getHead();
      Body                      b = doc.getBody();

      org.apache.ecs.html.Title title = new org.apache.ecs.html.Title("Test Document");
      Meta                      meta = new Meta();
      meta.setContent("this,is,a,test,document");
      meta.setName("keywords");
      h.addElement(title);
      h.addElement(meta);

      BR  br = new BR();
      IMG img = new IMG("/images/image.gif", "test", 0);
      P   p = new P().setAlign(AlignType.CENTER);
      HR  hr = new HR();

      b.addElement(br);
      b.addElement(img);
      b.addElement(p);
      b.addElement(hr);

      System.out.println(doc.toString());
   }

   public static void testTable() {
      Table t =
         new Table().addElement(new TR(true).addElement(new TD(true).addElement(
                  "One greate big dog > one little dog and one little dog < a cat\""))
                                            .addElement(new TD(true).setColSpan(2).addElement("r1c2")))
                    .addElement(new TR(true).addElement(new TD(true).addElement("r2c1"))
                                            .addElement(new TD(true).addElement("r2c2"))
                                            .addElement(new TD(true).addElement("r2c3")))
                    .addElement(new TR(true).addElement(new TD(true).addElement("r3c1"))
                                            .addElement(new TD(true).addElement("r3c2"))
                                            .addElement(new TD(true).addElement("r3c3")));
      System.out.println(t.toString());
      TR tr = new TR();
      TD td = new TD();
      td.addElement("test").addElement(new BR());
      tr.addElement(td);
      System.out.println(td.toString());
      System.out.println(tr.toString());
   }

   private void writeToFile(Html html, String string) {
      try {
         saveTxtFile(string + ".html", html.toString(), false);
      } catch (IOException ex) {
         ex.printStackTrace();
      }
   }

   public static void main(String[] args) {
      (new TestHtmlGen()).testHtml5();
   }
                     /**
     * Save a string to a text file
     */
    public static void saveTxtFile(String pathname, String data, boolean append) throws IOException
      {
        saveTxtFile(new File(pathname), data, append);
      }

    /**
     * Save a string to a text file
     */
    public static void saveTxtFile(File f, String data, boolean append) throws IOException
      {
        BufferedWriter out = new BufferedWriter(new FileWriter(f, append));
        out.write(data);
        out.close();
      }

    /**
     * Read a text file into a string
     */
    public static String readTxtFile(String pathname) throws IOException
      {
        return (readTxtFile(new File(pathname)));
      }

    /**
     * Read a text file into a string
     */
    public static String readTxtFile(File f) throws IOException
      {
        BufferedReader in = new BufferedReader(new FileReader(f));
        String result = "";
        String str = null;
        while ((str = in.readLine()) != null) {
            result += str;
            result += "\n";
        }
        in.close();
        return (result);
      }
}
