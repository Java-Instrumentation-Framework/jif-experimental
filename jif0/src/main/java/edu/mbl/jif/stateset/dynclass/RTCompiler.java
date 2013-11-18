package edu.mbl.jif.stateset.dynclass;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Iterator;

/**
 * My own class-file builder.  Not as nifty as others out there but
 * not weighed down with licenses either.
 */
class RTCompiler {

    public static final byte LDC = (byte) 0x13;
    public static final byte BIPUSH = (byte) 0x10;

    public static final byte ALOAD = (byte) 0x19;
    public static final byte ALOAD_0 = (byte) 0x2a;
    public static final byte ALOAD_1 = (byte) 0x2b;
    public static final byte ASTORE = (byte) 0x3a;

    public static final byte ILOAD = (byte) 0x15;
    public static final byte LLOAD = (byte) 0x16;
    public static final byte FLOAD = (byte) 0x17;
    public static final byte DLOAD = (byte) 0x18;

    public static final byte INVOKESPECIAL = (byte) 0xb7;
    public static final byte INVOKEVIRTUAL = (byte) 0xb6;

    public static final byte POP = (byte) 0x57;
    public static final byte RETURN = (byte) 0xb1;
    public static final byte IRETURN = (byte) 0xac;
    public static final byte LRETURN = (byte) 0xad;
    public static final byte FRETURN = (byte) 0xae;
    public static final byte DRETURN = (byte) 0xaf;
    public static final byte ARETURN = (byte) 0xb0;
    public static final byte CHECKCAST = (byte) 0xc0;

    public static final byte NEW = (byte) 0xbb;
    public static final byte DUP = (byte) 0x59;

    public static final byte GETFIELD = (byte) 0xb4;
    public static final byte PUTFIELD = (byte) 0xb5;

    public static final byte ANEWARRAY = (byte) 0xbd;
    public static final byte AASTORE = (byte) 0x53;

    public static final byte WIDE = (byte) 0xc4;

    private static final Map primitiveToDescriptor;
    static {
        HashMap hm = new HashMap();
        hm.put(Boolean.TYPE, "Z");
        hm.put(Byte.TYPE, "B");
        hm.put(Short.TYPE, "S");
        hm.put(Character.TYPE, "C");
        hm.put(Integer.TYPE, "I");
        hm.put(Float.TYPE, "F");
        hm.put(Double.TYPE, "D");
        hm.put(Long.TYPE, "J");
        hm.put(Void.TYPE, "V");
        primitiveToDescriptor = Collections.unmodifiableMap(hm);
    }
    private ConstantPool cp = new ConstantPool();

    // constPool indices
    private short thisClass;
    private short superClass;
    private short[] infs;

    private List fieldData = new ArrayList();

    private List methodData = new ArrayList();

    static String toInternalForm(Class cls) {

        if (cls.isArray()) {
            return "[" + toInternalForm(cls.getComponentType());
        }
        return cls.getName().replace('.', '/');
    }

    static String toDescriptor(Class cls) {

        if (cls.isArray()) {
            return "[" + toDescriptor(cls.getComponentType());
        }
        if (cls.isPrimitive()) {
            return (String) primitiveToDescriptor.get(cls);
        }
        return "L" + toInternalForm(cls) + ";";
    }

    static String toMethodDescriptor(Class returnType,
                                     Class[] paramTypes) {

        StringBuffer descBuffer = new StringBuffer();
        descBuffer.append('(');
        for (int i=0; i < paramTypes.length; i++) {
            descBuffer.append(toDescriptor(paramTypes[i]));
        }
        descBuffer.append(')');
        descBuffer.append(toDescriptor(returnType));
        return descBuffer.toString();
    }

    public RTCompiler(String className,
                      Class superclass,
                      Class[] interfaces) {

        thisClass = cp.classConst(className.replace('.', '/'));
        superClass = cp.classConst(toInternalForm(superclass));
        infs = new short[interfaces.length];
        for (int i=0; i < infs.length; i++) {
            infs[i] = cp.classConst(toInternalForm(interfaces[i]));
        }
    }

    public ConstantPool getConstantPool() {

        return cp;
    }

    public void addField(String name, Class type) {

        if (type.equals(Void.TYPE)) {
            throw new IllegalArgumentException("void fields not allowed");
        }
        short nameIndex = cp.utf8Const(name);
        short typeIndex = cp.utf8Const(toDescriptor(type));
        fieldData.add(new Short(nameIndex));
        fieldData.add(new Short(typeIndex));
    }

    public void writeClass(OutputStream os) throws IOException {

        DataOutputStream data = new DataOutputStream(os);
        data.writeInt(0xcafebabe); // magic
        data.writeShort(3); // got these from hex-dumping a class file
        data.writeShort(0x2d);
        data.writeShort(cp.getCount());
        cp.writePool(data);
        data.writeShort(1); // public flag
        data.writeShort(thisClass);
        data.writeShort(superClass);
        data.writeShort((short)infs.length);
        for (int i=0; i < infs.length; i++) {
            data.writeShort(infs[i]);
        }

        writeFields(data);

        // method land
        data.writeShort((short)methodData.size());
        Iterator methodIter = methodData.iterator();
        while (methodIter.hasNext()) {
            data.write((byte[])methodIter.next());
        }
        
        data.writeShort(0); // no attrs
    }

    private void writeFields(DataOutputStream data) throws IOException {

        data.writeShort((short)(fieldData.size() / 2));
        Iterator fdi = fieldData.iterator();
        while (fdi.hasNext()) {
            data.writeShort(2); // private
            data.writeShort(((Short)fdi.next()).shortValue());
            data.writeShort(((Short)fdi.next()).shortValue());
            data.writeShort(0);
        }
    }

    public void addMethod(String name,
                          Class returnType,
                          Class[] paramTypes,
                          Class[] exceptionTypes,
                          short maxStack,
                          short maxLocals,
                          byte[] code) {

        short methodName = cp.utf8Const(name);
        short methodDesc = cp.utf8Const(toMethodDescriptor(returnType, paramTypes));

        ByteArrayOutputStream methodBytes = new ByteArrayOutputStream();
        DataOutputStream methodInfo = new DataOutputStream(methodBytes);

        try {
            methodInfo.writeShort(1); // public
            methodInfo.writeShort(methodName);
            methodInfo.writeShort(methodDesc);
            methodInfo.writeShort(exceptionTypes.length > 0? 2 : 1); // # attrs

            // write code attribute
            methodInfo.writeShort(cp.utf8Const("Code"));
            methodInfo.writeInt(code.length + 12);
            methodInfo.writeShort(maxStack);
            methodInfo.writeShort(maxLocals);
            methodInfo.writeInt(code.length);
            methodInfo.write(code);
            methodInfo.writeShort(0); // no exception table
            methodInfo.writeShort(0); // no attributes

            if (exceptionTypes.length > 0) {

                methodInfo.writeShort(cp.utf8Const("Exceptions"));
                methodInfo.writeInt((exceptionTypes.length + 1) * 2);
                methodInfo.writeShort((short)exceptionTypes.length);
                for (int i=0; i < exceptionTypes.length; i++) {
                    String exName = toInternalForm(exceptionTypes[i]);
                    methodInfo.writeShort(cp.classConst(exName));
                }
            }
        }
        catch(IOException ioe) {
            throw new RuntimeException("Exception writing to byte array: " + ioe.getMessage());
        }

        methodData.add(methodBytes.toByteArray());
    }
}
