package edu.mbl.jif.stateset.dynclass;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class ConstantPool {

    private final class ByIndexComparator implements Comparator {

        public int compare(Object lhs, Object rhs) {

            Integer lhsI = (Integer) constant2Index.get(lhs);
            Integer rhsI = (Integer) constant2Index.get(rhs);
            return lhsI.intValue() - rhsI.intValue();
        }
    }

    private static final Byte CPutf8 = new Byte((byte)1);
    private static final Byte CPint = new Byte((byte)3);
    private static final Byte CPfloat = new Byte((byte)4);
    private static final Byte CPlong = new Byte((byte)5);
    private static final Byte CPdouble = new Byte((byte)6);
    private static final Byte CPclass = new Byte((byte)7);
    private static final Byte CPstring = new Byte((byte)8);
    private static final Byte CPfieldRef = new Byte((byte)9);
    private static final Byte CPmethodRef = new Byte((byte)10);
    private static final Byte CPintMethodRef = new Byte((byte)11);
    private static final Byte CPnameAndType = new Byte((byte)12);

    private Map constant2Index = new HashMap();

    private int currentSize = 1;

    ConstantPool() {
    }

    int getCount() {

        return currentSize;
    }

    void writePool(DataOutputStream data) throws IOException {

        List entryList = new ArrayList(constant2Index.size());
        entryList.addAll(constant2Index.keySet());

        Collections.sort(entryList, new ByIndexComparator());

        Iterator entryIter = entryList.iterator();
        while (entryIter.hasNext()) {
            writeCpEntry(data, (List) entryIter.next());
        }
    }

    private void writeCpEntry(DataOutputStream data,
                              List entry) throws IOException {

        // put constant, write to data
        Iterator iter = entry.iterator();
        
        byte tag = ((Byte)iter.next()).byteValue();
        data.writeByte(tag);

        while (iter.hasNext()) {
            Object value = iter.next();
            if (value instanceof Short) {
                data.writeShort(((Short)value).shortValue());
            }
            else if (value instanceof Integer) {
                data.writeInt(((Integer)value).intValue());
            }
            else if (value instanceof String) {
                byte[] bytes = ((String)value).getBytes("UTF8");
                data.writeShort((short)bytes.length);
                data.write(bytes);
            }
            else if (value instanceof Float) {
                data.writeFloat(((Float)value).floatValue());
            }
            else if (value instanceof Double) {
                data.writeDouble(((Double)value).doubleValue());
            }
            else if (value instanceof Long) {
                data.writeLong(((Long)value).longValue());
            }
            else {
                throw new IOException("Invalid type: " + value);
            }
        }
    }

    private short addConst(Object[] constData) {

        List rep = Arrays.asList(constData);
        Integer index = (Integer) constant2Index.get(rep);
        if (index != null) {
            return index.shortValue();
        }
            
        short slot = (short) currentSize;
        constant2Index.put(rep, new Integer(currentSize));
        currentSize++;
        if (constData[0].equals(CPlong) || (constData[0].equals(CPdouble))) {
            currentSize++;
        }
        return slot;
    }

    short utf8Const(String src) {

        return addConst(new Object[] { CPutf8, src });
    }

    short intConst(int value) {

        return addConst(new Object[] { CPint, new Integer(value) });
    }

    short floatConst(float value) {

        return addConst(new Object[] { CPfloat, new Float(value) });
    }

    short longConst(long value) {

        return addConst(new Object[] { CPlong, new Long(value) });
    }

    short doubleConst(double value) {

        return addConst(new Object[] { CPdouble, new Double(value) });
    }

    short classConst(String classname) {

        short index = utf8Const(classname);
        return addConst(new Object[] { CPclass, new Short(index) });
    }

    short stringConst(String value) {

        short index = utf8Const(value);
        return addConst(new Object[] { CPstring, new Short(index) });
    }

    short fieldRefConst(String classname, String name, String descriptor) {

        Short classnameIndex = new Short(classConst(classname));
        Short nameTypeIndex = new Short(nameAndTypeConst(name, descriptor));
        return addConst(new Object[] { CPfieldRef, classnameIndex, nameTypeIndex });
    }

    short methodRefConst(String classname, String name, String descriptor) {

        Short classnameIndex = new Short(classConst(classname));
        Short nameTypeIndex = new Short(nameAndTypeConst(name, descriptor));
        return addConst(new Object[] { CPmethodRef, classnameIndex, nameTypeIndex });
    }

    short intMethodRefConst(String classname, String name, String descriptor) {

        Short classnameIndex = new Short(classConst(classname));
        Short nameTypeIndex = new Short(nameAndTypeConst(name, descriptor));
        return addConst(new Object[] { CPintMethodRef, classnameIndex, nameTypeIndex });
    }

    short nameAndTypeConst(String name, String descriptor) {
        
        Short nameIndex = new Short(utf8Const(name));
        Short descriptorIndex = new Short(utf8Const(descriptor));
        return addConst(new Object[] { CPnameAndType, nameIndex, descriptorIndex });
    }

    public String toString() {

        return "Constant pool: " + constant2Index;
    }
}            
