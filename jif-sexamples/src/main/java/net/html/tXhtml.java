/*
 * tXhtml.java
 *
 * Created on July 20, 2006, 12:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.html;

import org.apache.ecs.html.Body;
import org.apache.ecs.html.Html;

import org.apache.ecs.html.A;
import org.apache.ecs.html.Acronym;
import org.apache.ecs.html.B;
import org.apache.ecs.html.Body;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.Head;
import org.apache.ecs.html.Html;
import org.apache.ecs.html.Link;
import org.apache.ecs.html.P;
import org.apache.ecs.html.Title;
import org.apache.ecs.wml.Img;

/**
 * This class tests the org.apache.ecs.html.Body element, as well as the method <br>
 * calls using unchainable methods, and chainable methods via innerclasses, outside <br>
 * of the same element context. <br>
 */
public class tXhtml
{
   public tXhtml()
   {
   }

   public static void main(String args[])
   {
//      Html html = new Html();
//      Body body = new Body();
//      html.setClass("test"); // returns a void
//      body.setTitle("The body"); // returns a void
//      /* Add the body to the html element and use chainable methods to configure both elements. */
//      Html htmlRef = html.element.add(body.element.setLang("english")).element.setLang("english");;
//      body.addElement("Some text in the body"); // returns a void
//      html.setPrettyPrint(true); // turn pretty print on in the entire tree.
//      html.output(System.out);

/*
 Html html = new Html();
      Body body = new Body();
      html.setLang("en");
      //html.setXmlLang("en");
      
      html.setXmlns("http://www.w3.org/1999/xhtml");
      html.addElement(
        new Head()
            .addElement(new Title("HTML Home Page"))..addElement(new Link().setElementType("text/css").element.setRel("stylesheet").element.setHref("markup.css")));
      html.addElement(body);
      
      body.addElement(
        new P()
            .element.setClassName("banner")
            .element.add(
                new A()
                    .element.setHref("../")
                    .element.add(	
                        new Img()
                            .element.setAlt("W3C")
                            .element.setWidth("72")
                            .element.setHeight("48")
                            .element.setSrc("../Icons/w3c_home")))
            .element.add(
                new A()
                    .element.setHref("../DF/")
                    .element.add(	
                        new Img()
                            .element.setAlt("Document Formats Domain")
                            .element.setWidth("212")
                            .element.setHeight("48")
                            .element.setSrc("../Icons/df")))
            .element.add(
                new Img()
                    .element.setSrc("../DF/")
                    .element.setAlt("../DF/"))
            ); // returns a void
        body.addElement(
            new H1("HyperText Markup Language")
                .element.setClassName("title")
                .element.add(new Br())
                .element.add(new B("Home Page"))
            );
        body.addElement(
            new Div()
                .element.setClassName("preface")
                .element.add(
                    new P("This is ")
                        .element.add(
                            new Acronym("W3C")
                                .element.setTitle("World Wide Web Consortium"))
                        .element.add("'s home page for ")
                        .element.add(
                            new Acronym("HTML")
                                .element.setTitle("HyperText Markup Language"))
                        .element.add(". Here you will find pointers to our specifications for HTML, ")
                        .element.add(" guidelines on how to use HTML to the best effect, and pointers to related work")
                        .element.add(" at W3C. When W3C decides to become involved in an area of Web technology or")
                        .element.add(" policy, it initiates an activity in that area. HTML is one of")
                        .element.add(" many Activities currently being pursued. You can learn more about")
                        .element.add(" the HTML Activity from the")
                        .element.add(
                            new A("HTML Activity Statement")
                                .element.setHref("Activity")))
            );
        body.addElement(
            new P()
        
            );
      html.setPrettyPrint(true); // turn pretty print on in the entire tree.
      html.output(System.out);
 */
 }
}