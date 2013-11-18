/*
 * BeanMaker.java
 *
 * Created on July 10, 2006, 1:34 PM
 *
 */
package beans;

//import edu.mbl.jif.utils.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Iterator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;


public class BeanPropSetMaker {
    static String ClassName;
    public Prop[] props;
    
    private BeanPropSetMaker() {
        // Can't construct me
    }
    
    //
    // inFile consists
    // first line: ClassName
    // followed by lines of property descriptors.
    
    // property descriptors
    // for each, a property name, accessor and mutator are created.
    // propType propName presentType
    // propType
    // propName
    // presentType =
    // prefKey = "this.class" + propName
    
    /**
     *
     * Sucks in BeanPropSet from an XML file (see below) and creates two files:
     * 
     *    ClazzModel.java and
     *    ClazzPresentation.java,
     * 
     * where Clazz is the specified class name.
     *
     * type: data type of property
     * name: field name of property
     * defaultValue:
     * enums: an ArrayList() containing the enumerated options list
     * persist: preference key to use for persisting...
     *
     * creates the enumeration used for the ObservableList
     *
     *
     * --- Example XML file format ---
     *
     * <edu.mbl.jif.beans.BeanPropSet>
     * <ClassName>Clazz</ClassName>
     * <props>
     *
     * <edu.mbl.jif.beans.Prop>
     * <type>int</type>
     * <name>number</name>
     * <defaultValue>12</defaultValue>
     * </edu.mbl.jif.beans.Prop>
     *
     * <edu.mbl.jif.beans.Prop>
     * <type>double</type>
     * <name>bigNumber</name>
     * <defaultValue>2.2222</defaultValue>
     * <persist>persistMe</persist>
     * </edu.mbl.jif.beans.Prop>
     *
     * <edu.mbl.jif.beans.Prop>
     * <type>int</type>
     * <name>selectableNumber</name>
     * <defaultValue>2</defaultValue>
     * <enums>
     * <string>Integer(1)</string>
     * <string>Integer(2)</string>
     * <string>Integer(3)</string>
     * </enums>
     * </edu.mbl.jif.beans.Prop>
     *
     * </props>
     * </edu.mbl.jif.beans.BeanPropSet>
     *
     * --- Template ---------------------------------------------------------
     *
     * <edu.mbl.jif.beans.BeanPropSet>
     * <ClassName>_</ClassName>
     * <pkg>edu.mbl.jif._/pkg>
     * <props>
     *
     * <edu.mbl.jif.beans.Prop>
     * <type>_</type>
     * <name>_</name>
     * </edu.mbl.jif.beans.Prop>
     *
     * <edu.mbl.jif.beans.Prop>
     * <type>_</type>
     * <name>_</name>
     * <defaultValue>_</defaultValue>
     * <persist>_</persist>
     * </edu.mbl.jif.beans.Prop>
     *
     *
     * <edu.mbl.jif.beans.Prop>
     * <type>_</type>
     * <name>_</name>
     * <defaultValue>_</defaultValue>
     * <enums>
     * <string>_</string>
     * <string>_</string>
     * </enums>
     * </edu.mbl.jif.beans.Prop>
     *
     * </props>
     * </edu.mbl.jif.beans.BeanPropSet>
     *
     * --- End of template ------------------------------------------------------
     */
    
    public static final void main(String[] args) {
        ArrayList properties;
        
        String    inFile = "inBean";
        String    outFile = "outBean";
        
        //makeTestFile();
        BeanPropSet bpsIn = load(".\\BeanPropSetMaker\\test.xml"); //
        //org.pf.joi.Inspector.inspectWait(bpsIn);
        
        makeModelFile(bpsIn);
        makePresentationModelFile(bpsIn);
        makeUICBFile(bpsIn);
        System.exit(0);
    }
    
    
    static void makeTestFile() {
        BeanPropSet bps = new BeanPropSet("Clazz", "edu.mbl.jif.binding;", "CamAcqJ");
        // Prop( type,  name,  defaultValue,  min, max, enums, persist)
        bps.addProp(new Prop("int", "number", "12", null, null));
        bps.addProp(new Prop("double", "bigNumber", "2.2222", null, "persistMe"));
        ArrayList e = new ArrayList();
        
        // "new xxxx()"
        e.add("Integer(1)");
        e.add("Integer(2)");
        e.add("Integer(3)");
        bps.addProp(new Prop("int", "selectableNumber", "2", e, null));
        save("testBPS.xml", bps);
    }
    
    
    static BeanPropSet loadBeanPropSet(String file) {
        BeanPropSet bps = load(file);
        return bps;
    }
    
    
    private static void makeModelFile(BeanPropSet bps) {
        try {
            File   classFile = new File(bps.ClassName + "Model.java");
            Writer out = new FileWriter(classFile);
            String ClassName = bps.ClassName;
            String pkg = bps.pkg;
            generateModelHeader(ClassName, pkg, bps.Application, out);
            generateModelDeclarations(bps, out);
            generateModelConstructor(bps, out);
            generateModelAccessors(bps, out);
            out.write("}");
            out.flush();
            out.close();
        } catch (IOException ex) {
            System.out.println("Couldn't create source file");
            ex.printStackTrace();
        }
    }
    
    
    static void generateModelHeader(String ClassName, String pkg, String Application, Writer out) {
        try {
            out.write("package " + pkg + ";\n");
            out.write("\n");
            out.write("import com.jgoodies.binding.adapter.PreferencesAdapter;\n");
            out.write("import com.jgoodies.binding.beans.Model;               \n");
            out.write("import com.jgoodies.binding.list.ArrayListModel;       \n");
            out.write("import com.jgoodies.binding.list.ObservableList;       \n");
            out.write("import java.util.Arrays;                               \n");
            out.write("import java.util.List;                                 \n");
            out.write("import java.util.prefs.Preferences;                    \n");
            out.write("import javax.swing.ListModel;                          \n");
            out.write("\n\n");
            out.write("public class " + ClassName + "Model extends Model {\n");
            out.write("\n");
            out.write("    Preferences prefs = " + Application + ".getInstance().getPreferences();\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    static void generateModelDeclarations(BeanPropSet bps, Writer out)
    throws IOException {
        out.write("    // <editor-fold defaultstate=\"collapsed\" desc=\"<<< Declarations >>>\">\n");
        Iterator propsIter = bps.props.iterator();
        while (propsIter.hasNext()) {
            Prop   thisProp = (Prop)propsIter.next();
            String type = thisProp.type;
            String Type = capitalise(type);
            String prop = thisProp.name;
            String Prop = capitalise(prop);
            String PROP = prop.toUpperCase();
            // @todo Add CodeFold here...

            out.write("\n    // --- " + prop + " ---\n");
            out.write("    public static final String " + "PROPERTYNAME_" + PROP
                    + " = \"" + prop + "\";\n");
            if (thisProp.persist == null) {
                if (thisProp.defaultValue == null) {
                    out.write("    private " + type + " " + prop + ";\n");
                } else {
                    out.write("    private " + type + " " + prop + " = "
                            + thisProp.defaultValue + ";\n");
                }
            } else {
                out.write("    private " + type + " " + prop + ";\n");
                // for Prefs
                out.write("    static final " + type + " " + PROP + "_DEFAULT = "
                        + thisProp.defaultValue + ";\n");
                out.write("    PreferencesAdapter " + prop + "PrefAdapter = \n");
                out.write("        new PreferencesAdapter(prefs, " + ClassName + "." + bps.ClassName + "Model.PROPERTYNAME_" + PROP
                        + ", " + PROP + "_DEFAULT);\n");
            }
            
            // enumerated lists
            ArrayList enums = thisProp.enums;
            if (enums != null) {
                if (type.equals("char") || type.equals("byte")
                || type.equals("short") || type.equals("long")
                || type.equals("float") || type.equals("double")
                || type.equals("String")) {
                    Type = capitalise(type);
                } else if (type.equals("int")) {
                    Type = "Integer";
                } else {
                    Type = "Object";
                }
                
                //out.write("  // enumeration for _prop_ options\n");
                int      n = 0;
                Iterator enumsIter = thisProp.enums.iterator();
                while (enumsIter.hasNext()) {
                    String thisEnum = (String)enumsIter.next();
                    n++;
                    out.write("    public static final " + Type + "  " + PROP
                            + "_ENUM_" + n + " = new " + thisEnum + ";\n");
                }
                out.write("    public static final List  " + PROP
                        + "_OPTIONS = Arrays.asList(new " + Type + "[] {\n");
                n = 0;
                Iterator enumsIter2 = thisProp.enums.iterator();
                while (enumsIter2.hasNext()) {
                    String thisEnum = (String)enumsIter2.next();
                    n++;
                    out.write("        " + PROP + "_ENUM_" + n);
                    if (enumsIter2.hasNext()) {
                        out.write(",\n");
                    }
                }
                out.write("\n    });\n");
                out.write("    private ObservableList " + prop + "ListModel;\n");
                out.write("    private Object         " + prop
                        + "ListSelection;\n");
            } // if enumerated
        } // each prop
        out.write("    // </editor-fold>\n");
    }
    
    
    // Generate default constructor
    static void generateModelConstructor(BeanPropSet bps, Writer out) {
        try {
            out.write("    // <editor-fold defaultstate=\"collapsed\" desc=\"<<< Constructor >>>\">\n");
            out.write("    InstrumentController instrumentCtrl;\n");


            out.write("\n  /**\n");
            out.write("     * Creates a new instance of " + bps.ClassName + "\n");
            out.write("     */\n");
            out.write("    public " + bps.ClassName + "Model(InstrumentController instrumentCtr) {\n\n");
            out.write("        this.instrumentCtrl = instrumentCtrl;\n\n");
            Iterator propsIter = bps.props.iterator();
            while (propsIter.hasNext()) {
                Prop thisProp = (Prop)propsIter.next();
                if (thisProp.enums != null) {
                    String prop = thisProp.name;
                    String PROP = prop.toUpperCase();
                    out.write("        // Initialize enumerated list for " + prop
                            + "\n");
                    out.write("        " + prop + "ListModel = new ArrayListModel();\n");
                    out.write("        " + prop + "ListModel.addAll(" + PROP
                            + "_OPTIONS);\n");
                    out.write("        " + prop + "ListSelection = " + prop
                            + "ListModel.get(0);\n\n");
                }
            }
            out.write("    }\n");
            out.write("    // </editor-fold>\n\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    static void generateModelAccessors(BeanPropSet bps, Writer out)
    throws IOException {
        Iterator propsIter = bps.props.iterator();
        while (propsIter.hasNext()) {
            Prop   thisProp = (Prop)propsIter.next();
            String type = thisProp.type;
            String Type = capitalise(type);
            String prop = thisProp.name;
            String Prop = capitalise(prop);
            String PROP = prop.toUpperCase();
            if (thisProp.persist == null) {
                generateAccessor(type, prop, out);
                generateMutator(type, prop, out);
            } else {
                if (thisProp.defaultValue == null) {
                    System.err.println("No default defined for " + prop);
                    return;
                } else {
                    generateAccessorPref(type, prop, out);
                    generateMutatorPref(type, prop, out);
                }
            }
            if (thisProp.enums != null) {
                generateModelListAccessors(prop, out);
            }
        }
    }
    
    
    static void generateAccessor(String type, String name, Writer out)
    throws IOException {
        String methodName = accessorMethodName(type, name);
        out.write("    /**\n");
        out.write("     * Gets the current value of " + name + "\n");
        out.write("     * @return Current value of " + name + "\n");
        out.write("     */\n");
        out.write("    public " + type + " " + methodName + "() {\n");
        out.write("        return " + name + ";\n");
        out.write("    }\n\n");
    }
    
    
    static void generateMutator(String type, String name, Writer out)
    throws IOException {
        String methodName = "set" + capitalise(name);
        out.write("    /**\n");
        out.write("     * Sets the value of " + name + "\n");
        out.write("     * @param " + name + " New value for " + name + "\n");
        out.write("     */\n");
        out.write("    public void " + methodName + "(" + type + " " + name
                + ") {\n");
        out.write("        " + type + " old" + capitalise(name) + " = "
                + accessorMethodName(type, name) + "();\n");
        out.write("        this." + name + " = " + name + ";\n");
        out.write("        firePropertyChange(" + propName(name) + ", old" + capitalise(name) + ", " + name + ");\n");
        out.write("    }\n");
        out.write("\n");
    }
    
    static String accessorMethodName(String type, String name) {
        String methodName;
        if (type.equals("boolean")) {
            methodName = "is" + capitalise(name);
        } else {
            methodName = "get" + capitalise(name);
        }
        return methodName;
    }
    
    
    static String propName(String name) {
        String p = "PROPERTYNAME_" + name.toUpperCase();
        return p;
    }
    
    // <editor-fold defaultstate="collapsed" desc="<<< Prefs >>>">
    
    static void generateAccessorPref(String type, String name, Writer out)
    throws IOException {
        String methodName = accessorMethodName(type, name);
        out.write("    /**\n");
        out.write("     * Gets the current value of " + name
                + " from preferences\n");
        out.write("     * @return Current value of " + name + "\n");
        out.write("     */\n");
        out.write("    public " + type + " " + methodName + "() {\n");
        out.write("        " + name + " = " + name + "PrefAdapter.get" + capitalise(type) + "();\n");
        out.write("         return " + name + ";\n");
        out.write("    }\n\n");
    }
    
    
    static void generateMutatorPref(String type, String name, Writer out)
    throws IOException {
        String methodName = "set" + capitalise(name);
        out.write("    /**\n");
        out.write("     * Sets the value of " + name + " to preferences\n");
        out.write("     * @param " + name + " New value for " + name + "\n");
        out.write("     */\n");
        out.write("    public void " + methodName + "(" + type + " " + name
                + ") {\n");
        out.write("        " + type + " old" + capitalise(name) + " = "
                + accessorMethodName(type, name) + "();\n");
        out.write("        this." + name + " = " + name + ";\n");
        out.write("        " + name + "PrefAdapter.set" + capitalise(type) + "("
                + name + ");\n");
        // out.write("    this." + name + " = " + name + ";\n");
        out.write("        firePropertyChange(" + propName(name) + ", old"
                + capitalise(name) + ", " + name + ");\n");
        out.write("    }\n\n");
    }
    
    static void generateModelListAccessors(String prop, Writer out)
    throws IOException {
        String Prop = capitalise(prop);
        out.write("    /**\n");
        out.write("     * ListModel accessor for " + prop + "\n");
        out.write("     * @return ListModel of " + prop + "\n");
        out.write("     */\n");
        out.write("    public ListModel get" + Prop + "ListModel() {\n");
        out.write("        return " + prop + "ListModel;\n");
        out.write("    }\n\n");
        out.write("    /**\n");
        out.write("     * ListSelection accessor for " + prop + "\n");
        out.write("     * @return ListSelection of " + prop + "\n");
        out.write("     */\n");
        out.write("    public Object get" + Prop + "ListSelection() {\n");
        out.write("        return " + prop + "ListSelection;\n");
        out.write("    }\n\n");
    }
// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="<<< PresentationModel >>>">
    private static void makePresentationModelFile(BeanPropSet bps) {
        try {
            String ClassNameP = bps.ClassName + "Presentation";
            File   classFile = new File(ClassNameP + ".java");
            Writer out = new FileWriter(classFile);
            
            generatePmHeader(ClassNameP, bps.pkg, out);
            makePresentationModel(bps, out);
            
            out.write("}");
            out.flush();
            out.close();
        } catch (IOException ex) {
            System.out.println("Couldn't create source file");
            ex.printStackTrace();
        }
    }
    
    
    static void generatePmHeader(String ClassName, String pkg, Writer out) {
        try {
            out.write("package " + pkg + ";\n");
            out.write("\n");
            out.write("import com.jgoodies.binding.PresentationModel;\n");
            out.write("import com.jgoodies.binding.list.SelectionInList;\n");
            out.write("\n\n");
            out.write("public class " + ClassName
                    + " extends PresentationModel {\n");
            out.write("\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    static void makePresentationModel(BeanPropSet bps, Writer out)
    throws IOException {
        String ClassName = bps.ClassName;
        String className = lowerCase(ClassName);
        
        // GeneratePMSelectionInList declarations
        out.write(
                "      // SelectionInList's holds the bean's list model plus a selection\n\n");
        Iterator propsIter = bps.props.iterator();
        while (propsIter.hasNext()) {
            Prop thisProp = (Prop)propsIter.next();
            if (thisProp.enums != null) {
                String type = thisProp.type;
                String Type = capitalise(type);
                String prop = thisProp.name;
                String Prop = capitalise(prop);
                String PROP = prop.toUpperCase();
                out.write("      // --- " + prop + "\n");
                out.write("      private final SelectionInList selectionIn" + Prop
                        + "List;\n");
                out.write("\n");
            }
        }
        // constructor
        out.write("    /**\n");
        out.write("     * Creates a new instance of " + ClassName
                + "Presentation\n");
        out.write("     */\n");
        out.write("    public " + ClassName + "Presentation (" + ClassName
                + "Model " + className + "Model) {\n");
        out.write("         super(" + className + "Model);\n");
        out.write("\n");
        
        // GeneratePMSelectionInLists
        propsIter = bps.props.iterator();
        while (propsIter.hasNext()) {
            Prop thisProp = (Prop)propsIter.next();
            if (thisProp.enums != null) {
                String type = thisProp.type;
                String Type = capitalise(type);
                String prop = thisProp.name;
                String Prop = capitalise(prop);
                String PROP = prop.toUpperCase();
                out.write("         // --- " + prop + "\n");
                out.write("         selectionIn" + Prop
                        + "List = new SelectionInList(" + className + "Model.get"
                        + Prop + "ListModel(),\n");
                out.write("               getModel(" + ClassName
                        + "Model.PROPERTYNAME_" + PROP + "));\n");
            }
        }
        out.write("    }\n\n\n"); // end of constructor
        
        propsIter = bps.props.iterator();
        while (propsIter.hasNext()) {
            Prop thisProp = (Prop)propsIter.next();
            if (thisProp.enums != null) {
                String prop = thisProp.name;
                String Prop = capitalise(thisProp.name);
                // For each enumerated property...
                out.write("    // --- " + prop + "\n");
                out.write("    public SelectionInList getSelectionIn" + Prop
                        + "List () {\n");
                out.write("         return selectionIn" + Prop + "List;\n");
                out.write("    }\n");
                out.write("\n");
            }
        }
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="<<< UiComponentBindings >>>">
    
    private static void makeUICBFile(BeanPropSet bps) {
        try {
            File   classFile = new File(bps.ClassName + "_UICB.java");
            Writer out = new FileWriter(classFile);
            String ClassName = bps.ClassName;
            String pkg = bps.pkg;
            
            makeUiComponentBindings(bps, out);
            
            
            out.write("}");
            out.flush();
            out.close();
        } catch (IOException ex) {
            System.out.println("Couldn't create source file");
            ex.printStackTrace();
        }
    }
    
    static void makeUiComponentBindings(BeanPropSet bps, Writer out)
    throws IOException {
        String ClassName = bps.ClassName;
        String className = lowerCase(ClassName);
        String presenta = className + "Presentation";
        String Model = ClassName + "Model";
        out.write("// UiComponentBindings for class: " +  ClassName +" \n\n");
        Iterator propsIter = bps.props.iterator();
//        while (propsIter.hasNext()) {
//            Prop thisProp = (Prop)propsIter.next();
        // if (thisProp.enums != null) {
//                String type = thisProp.type;
//                String Type = capitalise(type);
//                String prop = thisProp.name;
//                String Prop = capitalise(prop);
        String PROP = "PROP"; //prop.toUpperCase();
//                out.write("      // --- " + prop + "\n");
        
        String uic = "UIC";
        // Text Components
        // TextField =
        out.write("  " + uic + " = BasicComponentFactory.createTextField("
                + presenta + ".getModel(" + Model + ".PROPERTYNAME_" + PROP
                + "));\n");
        // TextArea
        out.write("  " + uic + " = BasicComponentFactory.createTextArea("
                + presenta + ".getModel(" + Model + ".PROPERTYNAME_" + PROP
                + "));\n");
        // PasswordField
        out.write("  " + uic
                + " = BasicComponentFactory.createPasswordField(" + presenta
                + ".getModel(" + Model + ".PROPERTYNAME_" + PROP + "));\n");
        // TextLabel
        out.write("  " + uic + " = BasicComponentFactory.createLabel("
                + presenta + ".getModel(" + Model + ".PROPERTYNAME_" + PROP
                + "));\n");
        
        // Formatted Input
        
        // DateField
        out.write("  " + uic + " = BasicComponentFactory.createDateField("
                + presenta + ".getModel(" + Model + ".PROPERTYNAME_DATE));\n");
        // IntegerField
        out.write("  " + uic
                + " = BasicComponentFactory.createIntegerField(" + presenta
                + ".getModel(" + Model + ".PROPERTYNAME_INT_VALUE));\n");
        // LongField
        out.write("  " + uic
                + " = BasicComponentFactory.createLongField(" + presenta
                + ".getModel(" + Model + ".PROPERTYNAME_LONG_VALUE));\n");
        
        
        // --- Choices---------------------
        
        // RadioButton, IntChoice
        String labelForRadioButton = "labelForRadioButton";
        out.write("  " + uic
                + " = BasicComponentFactory.createRadioButton(" + presenta
                + ".getModel(" + Model + ".PROPERTYNAME_INT_CHOICE)," + Model
                + ".CENTER_INTEGER, " + labelForRadioButton + ");\n");
        
        
        // ComboBox, IntChoice
        out.write("  " + uic
                + " = BasicComponentFactory.createComboBox(new SelectionInList("
                + Model + ".INTEGER_CHOICES, " + presenta + ".getModel(" + Model + ".PROPERTYNAME_INT_CHOICE));\n");
        
        // RadioButton, ObjectChoice
        labelForRadioButton = "labelForRadioButton";
        out.write("  " + uic
                + " = BasicComponentFactory.createRadioButton(" + presenta
                + ".getModel(" + Model + ".PROPERTYNAME_OBJECT_CHOICE), "
                + Model + ".LEFT, " + labelForRadioButton + ");\n");
        
        // ComboBox, ObjectChoice
        out.write("  " + uic
                + " = BasicComponentFactory.createComboBox(new SelectionInList("
                + Model + ".OBJECT_CHOICES, " + presenta + ".getModel(" + Model
                + ".PROPERTYNAME_OBJECT_CHOICE)));\n");
        
        
        // Lists
        // ComboBox
        out.write("  " + uic + " = BasicComponentFactory.createComboBox("
                + presenta + ".getSelectionInList(), "
                + "TutorialUtils.createModeListCellRenderer()" + ");\n");
        // list
        out.write("  list = BasicComponentFactory.createList(" + presenta
                + ".getSelectionInList(), "
                + "TutorialUtils.createModeListCellRenderer()" + ");\n");
        
        //                 table = new JTable();
        //                table.setModel(TutorialUtils.createModeTableModel(" + presenta + ".getSelectionInList()));\n");
        //                        table.setSelectionModel(new SingleListSelectionAdapter(" + presenta + ".getSelectionInList().getSelectionIndexHolder()));\n");
        
        // Misc
        String labelForCheckBox = "labelForCheckBox";
        out.write("   checkBox = BasicComponentFactory.createCheckBox("
                + presenta + ".getModel(" + Model
                + ".PROPERTYNAME_BOOLEAN_VALUE), " + labelForCheckBox + ");\n");
        
        //                colorPreview = new JPanel();\n");
        //                colorPreview.setBorder(new LineBorder(Color.GRAY));\n");
        //                updatePreviewPanel();\n");
        out.write("                   ValueModel floatModel = " + presenta
                + ".getModel(" + Model + ".PROPERTYNAME_FLOAT_VALUE);\n");
        out.write("   slider = new JSlider();\n");
        out.write(
                "   slider.setModel(new BoundedRangeAdapter(ConverterFactory.createFloatToIntegerConverter(floatModel, 100), 0, 0, 100));\n");
        
        out.write(
                "   floatLabel = BasicComponentFactory.createLabel(ConverterFactory.createStringConverter(floatModel, NumberFormat.getPercentInstance()));\n");
        
        out.write("   spinner = new JSpinner();\n");
        out.write(
                "   spinner.setModel(SpinnerAdapterFactory.createNumberAdapter("
                + presenta + ".getModel(" + Model
                + ".PROPERTYNAME_INT_LIMITED), INIT, MIN, MAX, INCR));\n");
// ADD THIS...
//                 spinInterval.setModel(
//                 SpinnerAdapterFactory.createNumberAdapter(acqPresentation.getModel(AcqModel.PROPERTYNAME_INTERVAL), 
//                  ((AcqModel)acqPresentation.getBean()).getImagesInSequence(),
//                 0.001,
//                 10000.0, 
//                 1.0));
        // defaultValue,  minValue,  maxValue,  step
        //}
        //}
    }
// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="<<< Save/Load >>>">
    public static void save(String file, Object obj) {
        XStream xstream = new XStream(new DomDriver());
        String  xml = xstream.toXML(obj);
        System.out.println(xml);
        try {
            saveTxtFile(file, xml, false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public static BeanPropSet load(String file) {
        BeanPropSet bps = null;
        
        // Deserializing an object back from XML
        XStream xstream = new XStream(new DomDriver());
        String  xml2 = null;
        try {
            xml2    = readTxtFile(file);
            bps     = (BeanPropSet)xstream.fromXML(xml2);
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return bps;
    }
    
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="<<< utils >>>">
    
    /**
     * Capitalises first character of input
     * @param name String to capitalise
     * @return Input with first character uppercased
     */
    private static String capitalise(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
    
    
    private static String capitaliseAll(String name) {
        return name.toUpperCase();
    }
    
    
    private static String lowerCase(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
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


// </editor-fold>

class BeanPropSet {
    String ClassName;
    String pkg;
    String Application;
    public ArrayList<Prop> props = new ArrayList();
    
    public BeanPropSet(String ClassName, String pkg, String Application) {
        this.ClassName    = ClassName;
        this.pkg          = pkg;
        this.Application  = Application;
    }
    
    public void addProp(Prop prop) {
        props.add(prop);
    }
    
    
    public ArrayList<Prop> getProps() {
        return props;
    }
}


class Prop {
    String    type;
    String    name;
    String    defaultValue;
    ArrayList enums = new ArrayList();
    String    persist;
    
    public Prop(String type, String name, String defaultValue, ArrayList enums,
            String persist) {
        this.type            = type;
        this.name            = name;
        this.defaultValue    = defaultValue;
        this.enums           = enums;
        this.persist         = persist;
    }
    

}
