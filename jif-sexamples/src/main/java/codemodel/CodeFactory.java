/*
 From http://namanmehta.blogspot.com/2010/01/use-codemodel-to-generate-java-source.html
 */

package codemodel;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import java.io.File;
import java.util.logging.Level;
import javax.xml.bind.JAXBException;

/**
 *
 * @author naman
 */
public class CodeFactory {

   private JVar jvarAvpImpl;

   // Method to get JType based on any String Value
   public JType getTypeDetailsForCodeModel(JCodeModel jCodeModel, String type) {
      if (type.equals("Unsigned32")) {
         return jCodeModel.LONG;
      } else if (type.equals("Unsigned64")) {
         return jCodeModel.LONG;
      } else if (type.equals("Integer32")) {
         return jCodeModel.INT;
      } else if (type.equals("Integer64")) {
         return jCodeModel.LONG;
      } else if (type.equals("Enumerated")) {
         return jCodeModel.INT;
      } else if (type.equals("Float32")) {
         return jCodeModel.FLOAT;
      } else if (type.equals("Float64")) {
         return jCodeModel.DOUBLE;
      } else {
         return null;
      }
   }

   // Function to generate CodeModel Class
   public void writeCodeModel(String factoryPackage) {
      try {

         /* Creating java code model classes */
         JCodeModel jCodeModel = new JCodeModel();

         /* Adding packages here */
         JPackage jp = jCodeModel._package(factoryPackage);

         /* Giving Class Name to Generate */
         JDefinedClass jc = jp._class("GeneratedFactory");

         /* Adding annotation for the Class */
         jc.annotate(tests.annotations.ActionListenerFor.class);

         /* Adding class level coment */
         JDocComment jDocComment = jc.javadoc();
         jDocComment.add("Class Level Java Docs");


         /* Adding method to the Class which is public static and returns com.somclass.AnyXYZ.class */
         String methodName = "myFirstMehtod";
         JMethod jmCreate = jc.method(JMod.PUBLIC | JMod.STATIC, 
                 tests.annotations.ActionListenerFor.class, "create" + methodName);

         /* Addign java doc for method */
         jmCreate.javadoc().add("Method Level Java Docs");

         /* Adding method body */
         JBlock jBlock = jmCreate.body();

         /* Defining method parameter */
         JType jt = getTypeDetailsForCodeModel(jCodeModel, "Unsigned32");
         if (jt != null) {
            jmCreate.param(jt, "data");
         } else {
            jmCreate.param(java.lang.String.class, "data");
         }

         /* Defining some class Variable in method body */
         JClass jClassavpImpl = jCodeModel.ref(tests.annotations.ActionListenerFor.class);
         jvarAvpImpl = jBlock.decl(jClassavpImpl, "varName");
         jvarAvpImpl.init(JExpr._new(jClassavpImpl));

         /* Adding some direct statement */
         jBlock.directStatement("varName.setCode(100);");

         /* returning varibalbe */
         jBlock._return(jvarAvpImpl);

         /* Building class at given location */
         File file = new File("./generated/src");
         file.mkdirs();
         jCodeModel.build(file);

      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   // Wirte main mehtod and call writeCodeModel("com.test") function to generate class 
   public static void main(String[] args) {
      new CodeFactory().writeCodeModel("com.test");
   }
}
