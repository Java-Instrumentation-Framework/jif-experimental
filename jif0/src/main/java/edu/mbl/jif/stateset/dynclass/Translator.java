package edu.mbl.jif.stateset.dynclass;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Translator is responsible for reading a ClassDefinition and producing
 * corresponding bytecode.  Could be called "ClassTranslator" but too many
 * classes start with Class already...
 */
class Translator {

    private static final String HANDLER_NAME = "Ldynclass/Handler;";
    private static final Map rtConversionInfo;
    private static final Map rtToByte;
    private static final Collection twoWordTypes;

    static {
        HashMap hm = new HashMap();
        hm.put(Boolean.TYPE, new String[] {
            "java/lang/Boolean",
            "booleanValue",
            "Z",
        });
        hm.put(Byte.TYPE, new String[] {
            "java/lang/Byte",
            "byteValue",
            "B",
        });
        hm.put(Short.TYPE, new String[] {
            "java/lang/Short",
            "shortValue",
            "S",
        });
        hm.put(Character.TYPE, new String[] {
            "java/lang/Character",
            "charValue",
            "C",
        });
        hm.put(Integer.TYPE, new String[] {
            "java/lang/Integer",
            "intValue",
            "I",
        });
        hm.put(Float.TYPE, new String[] {
            "java/lang/Float",
            "floatValue",
            "F",
        });
        hm.put(Double.TYPE, new String[] {
            "java/lang/Double",
            "doubleValue",
            "D",
        });
        hm.put(Long.TYPE, new String[] {
            "java/lang/Long",
            "longValue",
            "J",
        });
        rtConversionInfo = Collections.unmodifiableMap(hm);

        hm = new HashMap();
        hm.put(Boolean.TYPE, new byte[] { RTCompiler.IRETURN , RTCompiler.ILOAD });
        hm.put(Byte.TYPE, new byte[] { RTCompiler.IRETURN , RTCompiler.ILOAD });
        hm.put(Short.TYPE, new byte[] { RTCompiler.IRETURN, RTCompiler.ILOAD });
        hm.put(Character.TYPE, new byte[] { RTCompiler.IRETURN, RTCompiler.ILOAD });
        hm.put(Integer.TYPE, new byte[] { RTCompiler.IRETURN , RTCompiler.ILOAD });
        hm.put(Float.TYPE, new byte[] { RTCompiler.FRETURN, RTCompiler.FLOAD });
        hm.put(Double.TYPE, new byte[] { RTCompiler.DRETURN, RTCompiler.DLOAD });
        hm.put(Long.TYPE, new byte[] { RTCompiler.LRETURN, RTCompiler.LLOAD });
        rtToByte = Collections.unmodifiableMap(hm);

        Class[] twt = { Long.TYPE, Double.TYPE };
        twoWordTypes = Collections.unmodifiableList(Arrays.asList(twt));
    }
    private String genClassName;
    private ClassDefinition desc;

    private RTCompiler rtc;
    private ConstantPool cp;

    private String currentUniqueMethodId;

    private Map idToMethod;
    private int methodCount;

    // constant pool index of field reference for the "handler" field 
    // in the generated class
    private short handlerFieldRef = 0;

    // constant pool index of class reference to java.lang.Object
    private short objectClassRef = 0;

    // constant pool index of method reference to:
    // Handler.handle(Object, String, Object[])
    private short handlerMethodRef = 0;

    Translator(String genClassName, ClassDefinition desc) {

        this.genClassName = genClassName;
        this.desc = desc;
        createClass();
    }

    String getName() {

        return genClassName;
    }

    /**
     * Return the bytecodes describing the Class generated by 
     * this creator.
     */
    byte[] getClassBytes() {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            rtc.writeClass(out);
        }
        catch(IOException ioe) {
            // shouldn't get exceptions from writing to memory
            throw new RuntimeException(ioe.getClass() + ":" + ioe.getMessage());
        }
        return out.toByteArray();
    }

    int getMethodCount() {

        return methodCount;
    }

    Map getIdToDescMap() {

        return idToMethod;
    }

    private void addAbstractMethods(List methodList) {

        // First, look for abstract methods on superclass
        Class superclass = desc.getSuperclass();
        Method[] superMethods = superclass.getMethods();
        for (int i=0; i < superMethods.length; i++) {
            Method m = superMethods[i];
            int md = m.getModifiers();
            if (Modifier.isAbstract(md)) {
                methodList.add(new MethodDefinition(m));
            }
        }

        // Now look for interface methods that aren't implemented
        // on the superclass
        Class[] infs = desc.getInterfaces();
        for (int k=0; k < infs.length; k++) {
            Class inf = infs[k];
            Method[] infMethods = inf.getMethods();
            for (int j=0; j < infMethods.length; j++) {
                Method m = infMethods[j];

                // does superclass implement this already?
                try {
                    superclass.getMethod(m.getName(), m.getParameterTypes());
                    // yes:
                    continue;
                }
                catch(NoSuchMethodException nsme) {
                    methodList.add(new MethodDefinition(m));
                }
            }
        }
    }

    private List getMethodsList() {

        List candidateMethods = new ArrayList();
        candidateMethods.addAll(desc.getMethodDefinitions());
        addAbstractMethods(candidateMethods);

        HashSet methodSet = new HashSet(); // prevent duplicates
        ArrayList l = new ArrayList();
        Iterator iter = candidateMethods.iterator();
        while (iter.hasNext()) {
            MethodDefinition def = (MethodDefinition) iter.next();
            MethodDescription desc = def.getDescription();
            if (methodSet.contains(desc)) {
                continue;
            }
            methodSet.add(desc);
            l.add(def);
        }
        return l;
    }

    private void createClass() {

        rtc = new RTCompiler(genClassName, desc.getSuperclass(), desc.getInterfaces());
        cp = rtc.getConstantPool();
        handlerFieldRef = cp.fieldRefConst(genClassName.replace('.','/'),
                                           "handler",
                                           rtc.toDescriptor(Handler.class));
        //System.out.println("handlerFieldRef="  + handlerFieldRef + 
        //"; cp=" + cp);

        objectClassRef = cp.classConst(rtc.toInternalForm(Object.class));
        handlerMethodRef = cp.methodRefConst(rtc.toInternalForm(Handler.class),
                                             "handle",
                                             "(Ljava/lang/Object;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;");

        rtc.addField("handler", Handler.class);

        createConstructor();

        List methodList = getMethodsList();
        idToMethod = new HashMap(methodList.size());

        Iterator methodIter = methodList.iterator();
        while (methodIter.hasNext()) {
            MethodDefinition def = (MethodDefinition) methodIter.next();
            MethodDescription desc = def.getDescription();

            currentUniqueMethodId = "mId" + (++methodCount);
            idToMethod.put(currentUniqueMethodId, desc);

            createMethod(desc.getName(),
                         def.getReturnType(),
                         desc.getParameterTypes(),
                         def.getExceptionTypes());
        }
    }

    private void pushIntConst(ByteArrayOutputStream mb, int value) {

        if (value <= Byte.MAX_VALUE) {
            mb.write(rtc.BIPUSH);
            mb.write((byte)value);
        }
        else {
            short valueIndex = cp.intConst(value);
            mb.write(rtc.LDC);
            writeShort(mb, valueIndex);
        }
    }

    private void loadOrStoreIndex(ByteArrayOutputStream mb,
                                  byte inst,
                                  int index) {

        if (index <= Byte.MAX_VALUE) {
            mb.write(inst);
            mb.write((byte)index);
        }
        else {
            mb.write(rtc.WIDE);
            mb.write(inst);
            writeShort(mb, (short)index);
        }
    }

    private void createMethod(String methodName, 
                              Class returnType,
                              Class[] params,
                              Class[] exceptions) {

        ByteArrayOutputStream mb = new ByteArrayOutputStream();
        
        /*
          Method looks like this:
          Object[] args = new Object[params.length];
          args[0] = <param1>;
          args[1] = <param2>;
          // etc.
          
          [return] handler.handle(this, <uniqueId>, args);
        */
        int argsArrayIndex = 1;
        for (int i=0; i < params.length; i++) {
            argsArrayIndex++;
            if (twoWordTypes.contains(params[i])) {
                argsArrayIndex++;
            }
        }
        
        // I. create array
        pushIntConst(mb, params.length);
        mb.write(rtc.ANEWARRAY);
        writeShort(mb, objectClassRef);
        loadOrStoreIndex(mb, rtc.ASTORE, argsArrayIndex);

        // II. store each parameter into array.  primitives need
        // to be converted to their object types
        int paramIndex = 1;
        for (int i=0; i < params.length; i++) {
            loadOrStoreIndex(mb, rtc.ALOAD, argsArrayIndex);
            pushIntConst(mb, i);

            Class paramType = params[i];
            boolean paramIsTwoWord = twoWordTypes.contains(paramType);
            if (paramType.isPrimitive()) {
                // construct wrapper
                String[] classMethodParam = (String[]) rtConversionInfo.get(paramType);
                String wrapperName = classMethodParam[0];
                byte lc = ((byte[]) rtToByte.get(paramType))[1];
                short wrapperClass = cp.classConst(wrapperName);
                mb.write(RTCompiler.NEW);
                writeShort(mb, wrapperClass);

                mb.write(RTCompiler.DUP);
                loadOrStoreIndex(mb, lc, paramIndex);

                short wrapperCtor = cp.methodRefConst(wrapperName,
                                                      "<init>",
                                                      "(" + classMethodParam[2] + ")V");
                mb.write(RTCompiler.INVOKESPECIAL);
                writeShort(mb, wrapperCtor);
            }
            else {
                loadOrStoreIndex(mb, rtc.ALOAD, paramIndex);
            }
            mb.write(rtc.AASTORE);

            paramIndex++;
            if (paramIsTwoWord) {
                paramIndex++;
            }
        }

        // now invoke handler.handle(this, str, args)
        mb.write(RTCompiler.ALOAD_0);
        mb.write(RTCompiler.GETFIELD);
        writeShort(mb, handlerFieldRef);

        mb.write(RTCompiler.ALOAD_0);
        short uniqueIdRef = cp.stringConst(currentUniqueMethodId);
        mb.write(RTCompiler.LDC);
        writeShort(mb, uniqueIdRef);

        loadOrStoreIndex(mb, rtc.ALOAD, argsArrayIndex);
        
        mb.write(RTCompiler.INVOKEVIRTUAL);
        writeShort(mb, handlerMethodRef);

        createReturn(returnType, mb);

        rtc.addMethod(methodName, returnType, params, exceptions,
                      (short) 6, (short) (argsArrayIndex+1), mb.toByteArray());
    }

    // this code begins with the result of handler.handle(this, name, args) 
    // on the stack
    private void createReturn(Class returnType, ByteArrayOutputStream mb) {

        if (returnType.equals(Void.TYPE)) {
            mb.write(RTCompiler.POP);
            mb.write(RTCompiler.RETURN);
        }
        else if (returnType.isPrimitive()) {
            String[] classMethodParam = (String[]) rtConversionInfo.get(returnType);
            short wrapClassRef = cp.classConst(classMethodParam[0]);
            mb.write(RTCompiler.CHECKCAST);
            writeShort(mb, wrapClassRef);
            
            short methodRef = cp.methodRefConst(classMethodParam[0],
                                                classMethodParam[1],
                                                "()" + classMethodParam[2]);
            mb.write(RTCompiler.INVOKEVIRTUAL);
            writeShort(mb, methodRef);
            byte rc = ((byte[]) rtToByte.get(returnType))[0];
            mb.write(rc);
        }
        else {
            short rtClassRef = cp.classConst(rtc.toInternalForm(returnType));
            mb.write(RTCompiler.CHECKCAST);
            writeShort(mb, rtClassRef);
            mb.write(RTCompiler.ARETURN);
        }
    }

    private byte hi(short sh) {

        return (byte) (sh >> 8);
    }

    private byte lo(short sh) {

        return (byte) (sh % 256);
    }

    private void writeShort(ByteArrayOutputStream mb, short sh) {

        mb.write(hi(sh));
        mb.write(lo(sh));
    }

    private void createConstructor() {

        String superName = rtc.toInternalForm(desc.getSuperclass());
        short superCtorRef = cp.methodRefConst(superName, "<init>", "()V");
        
        ByteArrayOutputStream ctorBytes = new ByteArrayOutputStream();
        // invoke base ctor
        ctorBytes.write(RTCompiler.ALOAD_0);
        ctorBytes.write(RTCompiler.INVOKESPECIAL);
        writeShort(ctorBytes, superCtorRef);

        boolean isNoarg = desc.getMakeCtorNoarg();
        if (!isNoarg) {
            // Save parameter in instance variable "handler"
            ctorBytes.write(RTCompiler.ALOAD_0);
            ctorBytes.write(RTCompiler.ALOAD_1);
            ctorBytes.write(RTCompiler.PUTFIELD);
            writeShort(ctorBytes, handlerFieldRef);
        }

        ctorBytes.write(RTCompiler.RETURN);

        rtc.addMethod("<init>", 
                      Void.TYPE, 
                      isNoarg? new Class[0] : new Class[] { Handler.class }, 
                      new Class[0], 
                      (short) 2, 
                      (short) 2,
                      ctorBytes.toByteArray());
    }
}
