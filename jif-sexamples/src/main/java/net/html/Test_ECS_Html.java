/*
 * Test_ECS_Html.java
 *
 * Created on July 20, 2006, 12:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.html;
/*
 * Copyright (c) 1999 The Java Apache Project.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. All advertising materials mentioning features or use of this
 *    software must display the following acknowledgment:
 *    "This product includes software developed by the Java Apache
 *    Project. <http://java.apache.org/>"
 *
 * 4. The names "Java Apache Element Construction Set", "Java Apache ECS" and
 *    "Java Apache Project" must not be used to endorse or promote products
 *    derived from this software without prior written permission.
 *
 * 5. Products derived from this software may not be called
 *    "Java Apache Element Construction Set" nor "Java Apache ECS" appear
 *    in their names without prior written permission of the
 *    Java Apache Project.
 *
 * 6. Redistributions of any form whatsoever must retain the following
 *    acknowledgment:
 *    "This product includes software developed by the Java Apache
 *    Project. <http://java.apache.org/>"
 *
 * THIS SOFTWARE IS PROVIDED BY THE JAVA APACHE PROJECT "AS IS" AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JAVA APACHE PROJECT OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Java Apache Project. For more information
 * on the Java Apache Project please see <http://java.apache.org/>.
 *
 */

import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import java.text.BreakIterator;
import org.apache.ecs.*;
import org.apache.ecs.xml.*;
import org.apache.ecs.html.*;
import org.apache.ecs.filter.*;
import org.apache.ecs.factory.DOMFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Attr;
import org.apache.ecs.xhtml.*;

/**
    This class is used for development/regression testing.
    Call this application with a single parameter.
    Pass the method name if you want that to be tested
    or 'all' to call all test methods in turn.

    @version $Id$
    @author <a href="mailto:snagy@servletapi.com">Stephan Nagy</a>
    @author <a href="mailto:jon@clearink.com">Jon S. Stevens</a>
    @author <a href="mailto:mlham@alphawolf.com">Mason Ham</a>
*/
public class Test_ECS_Html
{

    public Test_ECS_Html()
    {
    }

    public void testBR1()
    {
    BR br = new BR();
        br.setClass("test_class");
        br.setID("test_id");
        br.setClear("left");
        br.setClass("test2_class");
        System.out.println(br.toString());

        BR br1 = new BR();
        System.out.println(br1.toString());

        BR br2 = new BR("right");
        System.out.println(br2.toString());
    }

    public void testBR2()
    {
        BR br = new BR();
        br.setCase(Element.LOWERCASE);
        br.setClass("test_class");
        br.setID("test_id");
        br.setClear("left");
        br.setClass("test2_class");
        System.out.println(br.toString());

        BR br1 = new BR();
        br1.setCase(Element.LOWERCASE);
        System.out.println(br1.toString());

        BR br2 = new BR("right");
        br2.setCase(Element.LOWERCASE);
        System.out.println(br2.toString());
    }

    public void testBR3()
    {
        BR br = new BR();
        br.setClass("test_class");
        br.setID("test_id");
        br.setClear("left");
        br.setCase(Element.UPPERCASE);
        br.setClass("test2_class");
        System.out.println(br.toString());

        BR br1 = new BR();
        br1.setCase(Element.UPPERCASE);
        System.out.println(br1.toString());

        BR br2 = new BR("right");
        br2.setCase(Element.UPPERCASE);
        System.out.println(br2.toString());
    }

    public void testImg1()
    {
        IMG img = new IMG();
        img.setSrc("/some/source/path");
        img.setClass("test_class");
        img.setID("test_id");
        img.setBorder(1);
        System.out.println(img.toString());
    }

    public void testImg2()
    {
        IMG img = new IMG();
        img.setSrc("/some/source/path");
        img.setClass("test_class");
        img.setID("test_id");
        img.setBorder(1);
        img.setCase(Element.UPPERCASE);
        System.out.println(img.toString());
    }

    public void testImg3()
    {
        IMG img = new IMG();
        img.setSrc("/some/source/path");
        img.setClass("test_class");
        img.setID("test_id");
        img.setBorder(1);
        img.setCase(Element.LOWERCASE);
        System.out.println(img.toString());
    }

    public void testTitle1()
    {
        org.apache.ecs.html.Title title = new org.apache.ecs.html.Title();
        title.addElement("title");
        System.out.println(title.toString());

        org.apache.ecs.html.Title title1 = new org.apache.ecs.html.Title("title");
        System.out.println(title1.toString());
    }

    public void testA1()
    {
        A a = new A();
        a.setName("name");
        a.setHref("/some/uri/goes/here");
        a.addElement("test",new String("Something"));

        // little more complex example
        IMG img = new IMG("/images/test.gif","Test",0);
        a.addElement("image",img);

        System.out.println(a.toString());

        System.out.println("-----");

        a.removeElement("image");
        System.out.println(a.toString());

        System.out.println("-----");

        a.removeElement("test");
        System.out.println(a.toString());

        System.out.println("-----");

        a.addElement("image",img);
        System.out.println(a.toString());

        System.out.println("-----");

        a.addElement("test","Something");
        System.out.println(a.toString());

        System.out.println("-----");

        a.removeElement("test");
        System.out.println(a.toString());

        System.out.println(a instanceof Printable);
        System.out.println(img instanceof Printable);
    }

    public void testHtml1()
    {
        org.apache.ecs.html.Title t = new org.apache.ecs.html.Title("Test Doc");
        Meta m = new Meta();
        m.setName("keywords");
        m.setContent("test,testing,testbed");
        Head h = new Head();
        h.addElement(m);
        h.addElement(t);
        Html html = new Html(h);

        System.out.println(h.toString());

        h.removeElementFromRegistry(m);
        html.addElement(h);

        System.out.println(html.toString());
    }

    public void testHtml2()
    {
        org.apache.ecs.html.Title t = new org.apache.ecs.html.Title("Test Doc");
        Meta m = new Meta();
        m.setName("keywords");
        m.setContent("test,testing,testbed");
        Head h = new Head();
        h.addElement(m);
        h.addElement(t);
        Html html = new Html(h);
        IMG img = new IMG("/images/file.gif");
        A a = new A("/gohere/",img);
        Body body = new Body(org.apache.ecs.HtmlColor.AQUA);
        body.addElement(a);
        html.addElement(body);
        System.out.println(html.toString());
    }

    public void testHtml3()
    {
        Span span = new Span("Some Name");
        span.setClass("goober");
        Div div = new Div(span);
        org.apache.ecs.html.Title t = new org.apache.ecs.html.Title("Test Doc");
        Meta m = new Meta();
        m.setName("keywords");
        m.setContent("test,testing,testbed");
        Head h = new Head();
        h.addElement(m);
        h.addElement(t);
        Html html = new Html(h);
        IMG img = new IMG("/images/file.gif");
        A a = new A("/gohere/",img);
        Body body = new Body(org.apache.ecs.HtmlColor.AQUA);
        body.addElement(a);
        body.addElement(div);
        html.addElement(body);
        System.out.println(html.toString());
    }

    public void testHtml4()
    {
        Span span = new Span("Some Name");
        span.setClass("goober");
        Div div = new Div("Last name: "+span.toString());
        org.apache.ecs.html.Title t = new org.apache.ecs.html.Title("Test Doc");
        Meta m = new Meta();
        m.setName("keywords");
        m.setContent("test,testing,testbed");
        Head h = new Head();
        h.addElement(m);
        h.addElement(t);
        Html html = new Html(h);
        IMG img = new IMG("/images/file.gif");
        A a = new A("/gohere/",img);
        Body body = new Body(org.apache.ecs.HtmlColor.AQUA);
        body.addElement(a);
        body.addElement(div);
        html.addElement(body);
        System.out.println(html.toString());
    }

    public void testHtml5()
    {
        Span span = new Span("Some Name");
        span.setClass("goober");
        Div div = new Div("Last name: "+span.toString());
        org.apache.ecs.html.Title t = new org.apache.ecs.html.Title("Test Doc");
        Meta m = new Meta();
        m.setName("keywords");
        m.setContent("test,testing,testbed");
        Head h = new Head();
        h.addElement(m);
        h.addElement(t);
        Html html = new Html(h);
        H1 h1 = new H1("test");
        H2 h2 = new H2("test2");
        H3 h3 = new H3("test3");
        H4 h4 = new H4("test4");
        H5 h5 = new H5("test5");
        H6 h6 = new H6("test6");

        IMG img = new IMG("/images/file.gif");
        A a = new A("/gohere/",img);
        Body body = new Body(HtmlColor.AQUA);
        body.addElement(a);
        body.addElement(div);
        body.addElement(h1);
        body.addElement(h2);
        body.addElement(h3);
        body.addElement(h4);
        body.addElement(h5);
        body.addElement(h6);
        html.addElement(body);
        System.out.println(html.toString());
    }

    public void testDocument()
    {
        Document doc = new Document();
        Head h = doc.getHead();
        Body b = doc.getBody();
        b.setAlink("#555555");
        b.setVlink(HtmlColor.AQUA);
        b.setBgColor(HtmlColor.red);
        b.addElement("&nbsp");
        doc.appendTitle("Test Document");
        Meta meta = new Meta();
        meta.setContent("this,is,a,test,document");
        meta.setName("keywords");
        h.addElement(meta);

        H1 headline = new H1("Test Document");
        BR br = new BR();
        IMG img = new IMG("/images/image.gif","test",0);
        b.addElement(headline);
        b.addElement(br);
        b.addElement(img);
        doc.appendBody(new Strong("TESTING"));
        doc.appendBody("Testing");

        // For testing. OutputStream

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.output(baos);
        try
        {
            baos.writeTo(System.out);
        }
        catch(Exception e)
        {
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
        try
        {
            baos.writeTo(System.out);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }

        // For testing PrintWriter
        out = new PrintWriter(System.out);
        doc1.output(out);
        out.flush();
    }

    public void testDocument1()
    {
        Document doc = new Document();
        Head h = doc.getHead();
        Body b = doc.getBody();

        org.apache.ecs.html.Title title = new org.apache.ecs.html.Title("Test Document");
        Meta meta = new Meta();
        meta.setContent("this,is,a,test,document");
        meta.setName("keywords");
        h.addElement(title);
        h.addElement(meta);

        BR br = new BR();
        IMG img = new IMG("/images/image.gif","test",0);
        P p = new P().setAlign(AlignType.CENTER);
        HR hr = new HR();

        b.addElement(br);
        b.addElement(img);
        b.addElement(p);
        b.addElement(hr);

        System.out.println(doc.toString());
    }

    public void testFrameSetDocument()
    {
        FrameSetDocument doc = new FrameSetDocument();
        Head h = doc.getHead();
        Body b = doc.getBody();

        org.apache.ecs.html.Title title = new org.apache.ecs.html.Title("Test Document");
        Meta meta = new Meta();
        meta.setContent("this,is,a,test,document");
        meta.setName("keywords");
        h.addElement(title);
        h.addElement(meta);

        BR br = new BR();
        IMG img = new IMG("/images/image.gif","test",0);
        P p = new P().setAlign(AlignType.CENTER);
        HR hr = new HR();

        b.addElement(br);
        b.addElement(img);
        b.addElement(p);
        b.addElement(hr);

        System.out.println(doc.toString());
    }

    public static void testP()
    {
        P p = new P().setAlign(AlignType.center);
        System.out.println (p.toString());
    }

    public static void testULLI()
    {
        UL ul = new UL();
        ul.addElement (new LI().addElement ("foo"));
        System.out.println (ul.toString());
    }

    public static void testOLLI()
    {
        OL ol = new OL();
        ol.addElement (new LI().addElement ("foo"));
        System.out.println (ol.toString());
    }

    public static void testDLDTDD()
    {
        DL dl = new DL();
        dl.addElement (new DT().addElement ("foo"));
        dl.addElement (new DD().addElement ("bar"));
        System.out.println (dl.toString());
    }

    public static void testDLDTDD1()
    {
        DL dl = new DL()
                .addElement(new DT("foo"))
                .addElement(new DD("bar"));
        System.out.println (dl.toString());
    }

    public static void test()
    {
        String[] strings = {"List 1","List 2","List 3","List 4"};
        Body body =
            new Body().addElement(
            new H1().addElement("stuff")).addElement(
            new BR()).addElement(
            new IMG("/usr/local/images/image.gif")).addElement(
            new UL(new LI().addElement(strings)));

        System.out.println(body.toString());
    }

    public static void testPRE()
    {
        Document d = new Document();
        d.appendBody(new PRE("here\nthere everywhere", 5));
        System.out.println(d.toString());
    }

    public static void testNOBR()
    {
        NOBR nobr = new NOBR("here");
        System.out.println(nobr.toString());
    }

    public static void testHR()
    {
        HR hr = new HR("100%", AlignType.center, 5);
        System.out.println(hr.toString());
        HR hr1 = new HR().setNoShade(true).setAlign(AlignType.CENTER);
        System.out.println(hr1.toString());
        hr1.setNoShade(false);
        System.out.println(hr1.toString());
    }

    public static void testB()
    {
        B b = new B("foo");
        System.out.println(b.toString());
    }

    public static void testI()
    {
        I b = new I("foo");
        System.out.println(b.toString());
    }

    public static void testU()
    {
        U b = new U("foo");
        System.out.println(b.toString());
    }

    public static void testBlink()
    {
        Blink b = new Blink("i can't belive we implemented this tag! ;-)");
        System.out.println(b.toString());
    }

    public static void testCenter()
    {
        Center b = new Center("the center tag");
        System.out.println(b.toString());
    }

    public static void testBlockQuote()
    {
        BlockQuote b = new BlockQuote("the BlockQuote tag");
        System.out.println(b.toString());
    }

    public static void testMap()
    {
        Map b = new Map().setName("mapname");
        System.out.println(b.toString());
    }

    public static void testCaption()
    {
        Caption b = new Caption().setAlign(AlignType.CENTER).addElement("a caption");
        //Caption b = new Caption().addElement("a caption");
        System.out.println(b.toString());
    }

    public static void testArea()
    {
        int[] coords = {1,2,3,4};
        Area b = new Area(Area.poly, coords );
        System.out.println(b.toString());
    }

    public static void testInput()
    {
        Input b = new Input(Input.password, "forminput", "itworks!");
        Input b1 = new Input(Input.TEXT,"Goober",50);

        System.out.println(b.toString());
        System.out.println(b1.toString());
    }

    public static void testSelect()
    {
        String[] an_array = {"List 1","List 2","List 3","List 4"};
        Select b = new Select("forminput", "5");
        Select c = new Select("test",an_array);

        System.out.println(b.toString());
        System.out.println(c.toString());
    }

    public static void testSelectOption()
    {
        Select b = new Select("forminput", "5");
        b.setPrettyPrint(true);
        b.setFilter ( new CharacterFilter().addAttribute ("&", "&amp;" ) );
        b.addElement( new Option("myvalue1").addElement("showmy&value1"))
            .addElement ( new Option ("myvalue2").addElement("showmyvalue2") );
        System.out.println(b.toString());
    }

    public static void testSelectOptGroup()
    {
        Select b = new Select("forminput", "5");
        b.setFilter ( new CharacterFilter().addAttribute ("&", "&amp;" ) );
        b.addElement(
            new OptGroup ("label").addElement (
            new Option("myvalue1").addElement("showmy&value1")
            .addElement ( new Option ("myvalue2").addElement("showmyvalue2"))
                ) );
        System.out.println(b.toString());
    }

    public static void testBase()
    {
        Base b = new Base("hrefurl", "targetfoo");
        System.out.println(b.toString());
    }

    public static void testScript()
    {
        Script b = new Script("script code", "url", "javascript", "english");
        System.out.println(b.toString());
        NoScript ns = new NoScript("high");
        System.out.println(ns.toString());
    }

    public static void testTable() {
        Table t = new Table().addElement(new TR(true).addElement(new TD(true).addElement("One greate big dog > one little dog and one little dog < a cat\"")).
                                                  addElement(new TD(true).setColSpan(2).addElement("r1c2"))).
                              addElement(new TR(true).addElement(new TD(true).addElement("r2c1")).
                                                  addElement(new TD(true).addElement("r2c2")).
                                                  addElement(new TD(true).addElement("r2c3"))).
                              addElement(new TR(true).addElement(new TD(true).addElement("r3c1")).
                                                  addElement(new TD(true).addElement("r3c2")).
                                                  addElement(new TD(true).addElement("r3c3")));
        System.out.println(t.toString());
        TR tr = new TR();
        TD td = new TD();
        td.addElement("test").addElement(new BR());
        tr.addElement(td);
        System.out.println(td.toString());
        System.out.println(tr.toString());
    }

    public static void testFilter()
    {
        Document doc = new Document();
        Filter filter = new CharacterFilter();
        filter.addAttribute("$","dollar");
        filter.addAttribute("#",Entities.POUND);

        Filter filter_1 = new WordFilter();
        filter_1.addAttribute("dollar",Entities.CURREN);
        filter_1.addAttribute(Entities.POUND,"lbs");

        doc.getBody().setAttributeFilterState(true);
        doc.getBody().setBgColor(HtmlColor.AZURE);

        P p = new P();
        doc.getBody().setFilter(filter_1);
        p.setFilter(filter);
        p.setFilterState(true);
        p.addElement("Jonas said \"Greedy people make lots of ''' $  $ $ & weigh lots of # # # #\"");
        System.out.println(p.toString());
        p.output(System.out);

        doc.getBody().addElement(p);
        doc.getBody().addElement(" dollar dollar " + Entities.POUND + " dollar" );
        System.out.println(doc.toString());
    }

    public static void testForm()
    {
        Document doc = new Document();
        Form form = new Form("http://www.foo.com/foo.cgi", Form.post );
        form.addElement ( new Input(Input.text, "item_name", "myvalue" ) );
        Select b = new Select("forminput", "5");
        b.setFilter ( new CharacterFilter().addAttribute ("&", "&amp;" ) );
        b.addElement( new Option("myvalue1").addElement("showmy&value1"))
            .addElement ( new Option ("myvalue2").addElement("showmyvalue2") );
	form.addElement(b);
        doc.getBody().addElement(form);
        System.out.println(doc.toString());
    }

    public static void testComment()
    {
        Document doc = new Document();
        Form form = new Form("http://www.foo.com/foo.cgi", Form.post );
        form.addElement ( new Input(Input.text, "item_name", "myvalue" ) );
        doc.getBody().addElement(new org.apache.ecs.html.Comment().addElement(form));
        System.out.println(doc.toString());
    }

    public static void testTextArea()
    {
        Document doc = new Document();
        Form form = new Form("http://www.foo.com/foo.cgi", Form.post );
        form.addElement ( new TextArea("myname", 5, 80 ).addElement("textarea foo") );
        doc.getBody().addElement(form);
        System.out.println(doc.toString());
    }

    public static void testXML()
    {
        XML xml = new XML("XML:test");
        XML xml1 = new XML("XML:test1",false);
        P p = new P("This is a test");
        xml.addElement(p);
        xml.addXMLAttribute("attribute_1","attr1");
        xml1.addXMLAttribute("attribute_2","ECS_NO_ATTRIBUTE_VALUE");
        xml1.addElement(xml);

        System.out.println(xml1.toString());
    }

    public static void testLabel()
    {
        Document doc = new Document();
        Form form = new Form("http://www.foo.com/foo.cgi", Form.post );
        Input input = new Input(Input.text, "item_name", "myvalue" );
        Label label = new Label("item_name").addElement("First Name:");
        form.addElement (label);
        form.addElement (input);
        doc.getBody().addElement(form);
        System.out.println(doc.toString());
    }

    public static void testFieldSet()
    {
        Document doc = new Document();
        Form form = new Form("http://www.foo.com/foo.cgi", Form.post );
        Input input = new Input(Input.text, "item_name", "myvalue" );
        Label label = new Label("item_name").addElement("First Name:");

        form.addElement (new FieldSet(label).addElement(input));
        doc.getBody().addElement(form);
        System.out.println(doc.toString());
    }

    public static void testLegend()
    {
        Document doc = new Document();
        Form form = new Form("http://www.foo.com/foo.cgi", Form.post );
        Input input = new Input(Input.text, "item_name", "myvalue" );
        Label label = new Label("item_name").addElement("First Name:");
        form.addElement (new Legend("Personal Information"));
        form.addElement (new FieldSet(label).addElement(input));
        doc.getBody().addElement(form);
        System.out.println(doc.toString());
    }

    public static void testFontStuff()
    {
        Big big = new Big("foo");
        Small small = new Small ("bar");
        TT tt = new TT ("mono");
        Sub sub = new Sub("low");
        Sup sup = new Sup("high");
        Em em = new Em("high");
        Cite cite = new Cite("high");
        Dfn dfn = new Dfn("high");
        Code code = new Code("high");
        Samp samp = new Samp("high");
        Kbd kbd = new Kbd("high");
        Var var = new Var("high");
        Abbr abbr = new Abbr("high");
        Acronym acr = (Acronym) new Acronym("high").setTitle("foo");
        Q q = (Q) new Q("high").setTitle("foo");

        System.out.println(big.toString());
        System.out.println(small.toString());
        System.out.println(tt.toString());
        System.out.println(sub.toString());
        System.out.println(sup.toString());
        System.out.println(em.toString());
        System.out.println(cite.toString());
        System.out.println(dfn.toString());
        System.out.println(code.toString());
        System.out.println(samp.toString());
        System.out.println(kbd.toString());
        System.out.println(var.toString());
        System.out.println(abbr.toString());
        System.out.println(acr.toString());
        System.out.println(q.toString());
    }

    public static void moreXMLTest()
    {
        PI p = new PI().setVersion(1.0);

        XML xml = new XML("page").addElement(new XML("title").addElement("XMLPage")).addElement(new XML("stupid-tag").addElement("I like to write stupid tags"));
        p.addElement(xml);

        System.out.println("XML1   :  "+p.toString());

        XML xml2 = new XML("test");
        xml2.setEndModifier('/');
        xml2.setNeedClosingTag(false);

        System.out.println("XML2   :  "+xml2.toString());

        XML xml3 = new XML("--");
        xml3.setBeginStartModifier('!');
        xml3.setNeedClosingTag(false);

        System.out.println("XML3   :  "+xml3.toString());
    }

    public static void testFrames()
    {
        Document doc = new Document();
        doc.appendTitle("This is the title");
        NoFrames nf = new NoFrames("upgrade to a newer browser!");
        FrameSet fs1 = new FrameSet().setCols("33%,33%,33%");
        FrameSet fs2 = new FrameSet().setRows("*,200");
        fs2.addElement ( new Frame().setSrc("contents_of_frame1.html" )
            .addElement ( new Frame().setSrc("contents_of_frame2.html" ) ) );
        fs1.addElement(fs2)
            .addElement ( new Frame().setSrc("contents_of_frame3.html" ) )
            .addElement ( new Frame().setSrc("contents_of_frame4.html" ) );
        doc.getBody().addElement(fs1).addElement(nf);

        IFrame iframe = new IFrame().setSrc("foo.html")
            .setWidth(400).setHeight(500)
            .setScrolling (IFrame.auto)
            .setFrameBorder(true)
            .addElement("your browser does not support frames");

        doc.appendBody(iframe);

        System.out.println(doc.toString());
    }

    public static void testStyle()
    {
        Style style = new Style (Style.css).addElement("here!");
        System.out.println(style.toString());
    }

    public static void testLink()
    {
        Link link = new Link().setRel("Index").setHref("../index.html");
        System.out.println(link.toString());
    }

    public static void testBaseFont()
    {
        BaseFont bf = new BaseFont().setFace("arial").setColor(HtmlColor.BLUE);
        System.out.println(bf.toString());
    }

    public static void testButton()
    {
        Button b = new Button().setType(Button.SUBMIT);
        System.out.println(b.toString());
    }

    public static void testS()
    {
        S s = new S().addElement("opps");
        System.out.println(s.toString());
    }

    public static void testStrike()
    {
        Strike strike = new Strike().addElement("strike");
        System.out.println(strike.toString());
    }

    public static void testApplet()
    {
        Applet a = new Applet().addElement(new Param().setValue("dog").setName("param1"));
        System.out.println(a.toString());
    }

    public static void testObject()
    {
        ObjectElement o = new ObjectElement().setName("shit").setShapes(true).addElement(new Param().setName("param1").setValue("cat"));
        System.out.println(o.toString());
    }

    public static void test1()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                P p = new P().addElement("If you are interested in a course that is not currently scheduled or is not available in your area please contact ")
                     .addElement(new A("mailto:shunt@norrellis.com","Norrell Education Services"))
                     .addElement(
                        new Center().addElement(
                          new Form().setMethod(Form.GET).setName("education")
                            .addElement(new Input().setType(Input.HIDDEN).setName("tab").setValue("1"))
                            .addElement(new Table()
                                .addElement(new TR()
                                    .addElement(new TH().addElement("City"))
                                    .addElement(new TH().addElement("Course")))
                                .addElement(new TR()
                                    .addElement(new TD().addElement(new Select("Test1")))
                                    .addElement(new TD().addElement(new Select("Test1")))))
                        )
                     );
        p.output(baos);
        System.out.println(p.toString());
        System.out.println(baos.toString());
    }

    public static void homepageTest()
    {
        Html html = new Html()
              .addElement(new Head()
                  .addElement(new org.apache.ecs.html.Title("Demo")))
              .addElement(new Body()
              .addElement(new H1("Demo Header"))
              .addElement(new H3("Sub Header:"))
              .addElement(new Font().setSize("+1")
                         .setColor(HtmlColor.WHITE)
                         .setFace("Times")
                         .addElement("The big dog & the little cat 'chased' each other.")));
        System.out.println (html.toString());

        Document doc = (Document) new Document()
              .appendTitle("Demo")
              .appendBody(new H1("Demo Header"))
              .appendBody(new H3("Sub Header:"))
              .appendBody(new Font().setSize("+1")
                         .setColor(HtmlColor.WHITE)
                         .setFace("Times")
                         .addElement("The big dog & the little cat chased each other."));
        System.out.println (doc.toString());
    }

    public static void testWordFilter()
    {
        Filter filter = new WordFilter();
        // Filter filter = new StringFilter();
        filter.addAttribute("there","where");
        filter.addAttribute("it","is");
        filter.addAttribute("goes","it");
        P p = new P();
        p.setFilter(filter);
        p.addElement("there it goes");

        Body body = new Body();
        body.setAttributeFilter(filter);
        body.setText("there");
        body.addElement(new P().addElement("it"));
        body.addElement(p);
        System.out.println(body.toString());
    }

    public static void testElementContainer()
    {
        P p = new P().addElement("foo");
        P p1 = new P().addElement("bar");
        ElementContainer ec = new ElementContainer(p).addElement(p1);
        System.out.println(ec.toString());
    }

    public static void testStringElement()
    {   StringElement se = new StringElement("foo")
            .addElement(new P().addElement(Entities.AMP));
        se.addElement(new P());
        P p = new P().addElement(se);
        System.out.println(se.toString());
        System.out.println(p.toString());
    }

    public static void testFilter1()
    {
        P p = new P();
        p.setFilterState(false);
        p.addElement("test ',\"");
        System.out.println(p.toString());

//        p.output(System.out);
    }
    public static void testDocBug()
    {
        Document D = new Document();
        org.apache.ecs.html.Title title = new org.apache.ecs.html.Title("this is the title");
        H2 heading = new H2("this is a BugTester");
        Body body = new Body(HtmlColor.WHITE);
        body.addElement(heading);
        D.setTitle( title );
        D.setBody( body );
        System.out.println( D.toString() );
    }

    public static void  testDOMFactory()
    {
        PI p = new PI();
        p.setVersion(1.0);
        XML x = new XML("root",true);
        XML x1 = new XML("page",true);
        XML x2 = new XML("paragraph");
        XML x3 = new XML("paragraph");
        x2.addElement("This is the first Paragraph");
        x3.addElement("This is the second Paragraph");
        x.addElement(x1.addElement(x2).addElement(x3));
        p.addElement(x);
        org.apache.ecs.factory.DOMFactory d = new org.apache.ecs.factory.DOMFactory();
        p.output(d.getOutputStream());

        NodeList nl = d.createDOM().getChildNodes();
        for(int a = 0; a < nl.getLength(); a++)
        {
           traverse(nl.item(a));
        }
    }

    private static void traverse(Node node)
    {
        while( node != null)
        {
            if(!node.getNodeName().equals("#text"))
            {
                System.out.println(node.getNodeName());
            }
            else
            {
                System.out.println(node.getNodeValue());
            }
            // If it has children loop through them
            if ( node.hasChildNodes() &&
                !node.getNodeName().equals("#document"))
            {
                traverse ( node.getFirstChild() );
            }
            else
            {
                traverse ( node.getFirstChild() );
            }
            // move to the next node.
            node = node.getNextSibling();
            System.out.print("\n");
        }
    }

    public static void testPrettyPrint()
    {
        Html html = new Html();
        org.apache.ecs.html.Title title = new org.apache.ecs.html.Title("Testing Pretty Print");
        Head head = new Head();
        Body body = new Body();
        P p = new P();
        P p1 = (P)p.clone();
        P p2 = (P)p.clone();

        html.setPrettyPrint(true);
        html.addElement(head);
        head.addElement(title);
        html.addElement(body);
        body.addElement(p);
        p.addElement("Paragraph one.")
         .addElement(p2);
        p1.addElement("Sub paragraph 2");
        body.addElement(p2);
        p2.addElement(new PRE().addElement("hhhhhhhhhhhhhhhhhhhhhhhhhhhh").addElement(new B().addElement("BIG")));

        System.out.println(html.toString());
    }
    public static void testStringBug()
    {
        // there was a bug where strings were not being
        // properly added and only one instance would actually
        // be put into the element. ie: <pre>foo</pre>
        // instead of <pre>foofoofoofoofoofoofoofoofoofoo</pre>
        String foo = "foo";
        PRE pre = new PRE();
        for ( int i=0;i<10;i++ )
        {
            //pre.addElement(new StringElement(foo));
            pre.addElement(foo);
        }
//        pre.addElement(foo);pre.addElement(foo);pre.addElement(foo);
        pre.output(System.out);
    }

    public static void testBigMouseEvents()
    {
        Big big = new Big();
        big.setOnMouseOver("myscript here");
        System.out.println (big.toString());
    }

    public void testXhtmlFrameSet()
    {
        XhtmlFrameSetDocument doc = new XhtmlFrameSetDocument();
// methods not present in 1.4.1 commented out so that i can do comparative testing
        doc.setDoctype(new org.apache.ecs.Doctype.XHtml10Frameset());
        doc.appendTitle("title");
// methods not present in 1.4.1 commented out so that i can do comparative testing
//        doc.appendHead(new org.apache.ecs.xhtml.meta("keywords","test,org.apache.ecs"));
        doc.appendBody(new org.apache.ecs.xhtml.hr());
        doc.appendBody("The body part of this document goes into the noframes bit.");
        doc.appendBody(new org.apache.ecs.xhtml.hr());
        doc.appendFrameSet("This is a stringy append to the frameset bit.");
        doc.appendFrameSet(new org.apache.ecs.xhtml.frame(
            "This is the frame element bit",
            "theFrameName",
            "frame.html"));
        System.out.println(doc.toString());
    }
    
    public void testXhtml()
    {
        XhtmlDocument doc = new XhtmlDocument();
        doc.setDoctype(new org.apache.ecs.Doctype.XHtml10Strict());
        doc.appendTitle("title");
// methods not present in 1.4.1 commented out so that i can do comparative testing
//        doc.appendHead(new org.apache.ecs.xhtml.meta("keywords","test,org.apache.ecs"));
        doc.appendBody(new org.apache.ecs.xhtml.hr());
        doc.appendBody("The body part of this document.");
        doc.appendBody(new org.apache.ecs.xhtml.hr());
        System.out.println(doc.toString());
    }

    public void testHTMLDoctypes()
    {
        Document doc=new Document();
        doc.appendTitle("title");
        doc.appendBody(new org.apache.ecs.html.HR());
        doc.appendBody("The body part of this document.");
        doc.appendBody(new org.apache.ecs.html.HR());
        doc.getHtml().setPrettyPrint(true);
        
        System.out.println();
        System.out.println("********Html40Strict********");
        doc.setDoctype(new org.apache.ecs.Doctype.Html40Strict());
        doc.output(System.out);
        System.out.println();

        System.out.println();
        System.out.println("********Html401Strict********");
        doc.setDoctype(new org.apache.ecs.Doctype.Html401Strict());
        doc.output(System.out);
        System.out.println();
        
        System.out.println();
        System.out.println("********Html40Frameset********");
        doc.setDoctype(new org.apache.ecs.Doctype.Html40Frameset());
        doc.output(System.out);
        System.out.println();
        
        System.out.println();
        System.out.println("********Html401Frameset********");
        doc.setDoctype(new org.apache.ecs.Doctype.Html401Frameset());
        doc.output(System.out);
        System.out.println();
        
        System.out.println();
        System.out.println("********Html40Transitional********");
        doc.setDoctype(new org.apache.ecs.Doctype.Html40Transitional());
        doc.output(System.out);
        System.out.println();

        System.out.println();
        System.out.println("********Html401Transitional********");
        doc.setDoctype(new org.apache.ecs.Doctype.Html401Transitional());
        doc.output(System.out);
        System.out.println();        
    }

//-------------------------
    /**
    this method prints up the usage.
    */
    private static void printUsage(Test_ECS_Html instance)
    {
        System.out.println("TesBed usage:");
        System.out.println("\tjava TestBed all");
        System.out.println("OR");
        System.out.println("\tjava TestBed <method>");
        System.out.println("\twhere <method> is one of:");
        java.lang.reflect.Method[] methods= instance.getClass().getMethods();
        for (int i=0; i<methods.length;i++)
        {
            if (methods[i].getDeclaringClass().equals(instance.getClass())&&methods[i].getParameterTypes().length==0)
            {
                System.out.println("\t\t"+ methods[i].getName());
            }
        }
    }

    /**
    this calls all test methods in turn.
    the name of the method is printed out before the method is called.
    (actually it calls all methods strictly in this class which has no
    parameters - so you'll need to avoid parameter-based test methods
    if you want this method to find them!)
    */
    private static void testAll(Test_ECS_Html instance)
    {
        java.lang.reflect.Method[] methods= instance.getClass().getMethods();
        for (int i=0; i<methods.length;i++)
        {
            if (methods[i].getDeclaringClass().equals(instance.getClass())&&methods[i].getParameterTypes().length==0)
            {
                System.out.println();
                System.out.println(methods[i].getName()+":");
                try {
                    methods[i].invoke(instance, new Class[]{});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     *    pass a single parameter to this class.
     *    for example <code> java Test_ECS_Html arg</code> where <code>arg</code>
     *    is either <code>all</code> (which will call every test method in this class in turn)
     *    or <code><method></code> (which calls the test method called <method> in this class).
     */
    public static void main(String[] args)
    {
        Test_ECS_Html tb = new Test_ECS_Html();
        
//        if (args.length!=1)
//        {
//            printUsage(tb);
//        } else {
            //String method=args[0];
            String method="all";

            if (method.equalsIgnoreCase("all"))
            {
                testAll(tb);
            } else {
                try {
                    java.lang.reflect.Method meth=tb.getClass().getMethod(method, new Class[]{});
                    meth.invoke(tb,new Class[]{});
                } catch (NoSuchMethodException ex) {
                    System.out.println("Bad parameter!");
                    printUsage(tb);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        //}
    }
}
