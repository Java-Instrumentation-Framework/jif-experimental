//**********************************************************************
// Package
//**********************************************************************

package tests.gui.table;

//**********************************************************************
// Import list
//**********************************************************************

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;


/**
 * This class allows a user to dynamically alter a set of components
 * using the TableLayout class to explore the layout's abilities.
 *
 * @author Antonio Freixas
 */

public class TableExplorer
    extends JFrame
    implements CaretListener, ActionListener, ListSelectionListener,
	       FocusListener, MenuListener
{

//**********************************************************************
// Constants
//**********************************************************************

static final String[] tablePositionList = {
    "Default",
    "tn", "tne", "tnw",
    "ts", "tse", "tsw",
    "te", "tw", "tc"
};

static final String[] tableFillList = {
    "Default", "tfh", "tfv", "tf"
};

static final String[] positionList = {
    "Default",
    "n", "ne", "nw",
    "s", "se", "sw",
    "e", "w", "c"
};

static final String[] fillList = {
    "Default", "fh", "fv", "f"
};

//**********************************************************************
// Fields
//**********************************************************************

JFrame layout;
JPanel layoutPane;
JFrame code;
JPanel codePane;

HashMap compHash = new HashMap();
HashMap attrHash = new HashMap();

JMenu fileMenu;
JMenu windowMenu;
JMenu helpMenu;

JMenuItem exitItem;
JMenuItem previewItem;
JMenuItem packItem;
JMenuItem codeItem;
JMenuItem generateItem;
JMenuItem aboutItem;

JButton upButton;
JButton downButton;
JButton removeButton;
JButton addButton;

JList compList;
DefaultListModel compListModel;
JTextField compEntryField;

JTextField columnsField;
JTextField[] tableInsetFields = new JTextField[4];
JTextField rowGapField;
JTextField colGapField;
JComboBox tablePositionBox;
JComboBox tableFillBox;

JTextField[] tableCellInsetFields = new JTextField[4];
JComboBox tableCellPositionBox;
JComboBox tableCellFillBox;
JTextField tableRowWeightField;
JTextField tableColWeightField;

JTextField tableAttributesField;

JTextField[] insetFields = new JTextField[4];
JComboBox positionBox;
JComboBox fillBox;
JTextField rowWeightField;
JTextField colWeightField;
JTextField colPositionField;
JTextField skipCellsField;
JTextField rowSpanField;
JTextField colSpanField;

JTextField cellAttributesField;

JTextArea codeTextArea;
JScrollPane codeTextScroll;

String textWhenFocusGained = null;
boolean ignoreEvents = false;

TableAttributes tableAttributes = null;

//**********************************************************************
// Main
//**********************************************************************

static public void
main(
    String[] args)
{
    new TableExplorer();
}

//**********************************************************************
// Constructors
//**********************************************************************

/**
 * Create the TableExplorer JFrame.
 */

TableExplorer()
{
    super("TableExplorer");

    // Table with three columns
    //   Row 1: Buttons for managing component list
    //   Row 2: Component list
    //   Row 3: Attribute settings

    setJMenuBar(createJMenuBar());

    getContentPane().setLayout(
	new TableLayout("cols=3 cgap=5 " +
			"titop=2 tibottom=2 tileft=2 tiright=2"));

    getContentPane().add(createButtonPane());
    getContentPane().add(createListPane(), "cweight=1");
    getContentPane().add(createAttributePane(), "n fh");

    pack();
    setVisible(true);

    layout = new JFrame("Table Layout Preview");
    layoutPane = (JPanel)layout.getContentPane();
    layoutPane.setName("DEBUG");
    layoutPane.setLayout(new TableLayout());
    layoutPane.setOpaque(true);

    code = new JFrame("Table Layout Code");
    codePane = createCodePane();
    code.setContentPane(codePane);
    code.pack();

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    compEntryField.grabFocus();

    layout.setSize(300, 300);
}

//**********************************************************************
// Public
//**********************************************************************

public void
caretUpdate(
    CaretEvent e)
{
    if (e.getSource() == compEntryField) {
	String text = compEntryField.getText();
	addButton.setEnabled(text.length() > 0);
    }
}

public void
actionPerformed(
    ActionEvent e)
{
    if (ignoreEvents) return;

    if (e.getSource() == addButton ||
	e.getSource() == compEntryField) {

	String name = compEntryField.getText();

	JButton button = new JButton(name);
	addToLayout(button);
	compHash.put(name, button);
	attrHash.put(name, new Attributes());
	compListModel.addElement(name);
	compEntryField.setText("");
	addButton.setEnabled(false);
    }

    else if (e.getSource() == removeButton) {
	String name = (String)compList.getSelectedValue();
	if (name != null) {
	    Component c = (Component)compHash.get(name);
	    layoutPane.remove(c);
	    compHash.remove(name);
	    attrHash.remove(name);
	    compListModel.removeElement(name);
	    removeButton.setEnabled(false);
	    layoutPane.revalidate();
	}
    }

    else if (e.getSource() == upButton) {
	int index = compList.getSelectedIndex();
	if (index > 0) {
	    String name = (String)compListModel.elementAt(index);
	    compListModel.removeElementAt(index);
	    compListModel.insertElementAt(name, index - 1);
	    compList.setSelectedIndex(index - 1);

	    Component c = (Component)compHash.get(name);
	    Attributes a = (Attributes)attrHash.get(name);
	    layoutPane.remove(index);
	    layoutPane.add(c, a.toString(), index - 1);
	    layoutPane.revalidate();
	}
    }

    else if (e.getSource() == downButton) {
	int index = compList.getSelectedIndex();
	if (index > -1 && index < compListModel.getSize() - 1) {
	    String name = (String)compListModel.elementAt(index);
	    compListModel.removeElementAt(index);
	    compListModel.insertElementAt(name, index + 1);
	    compList.setSelectedIndex(index + 1);

	    Component c = (Component)compHash.get(name);
	    Attributes a = (Attributes)attrHash.get(name);
	    layoutPane.remove(index);
	    layoutPane.add(c, a.toString(), index + 1);
	    layoutPane.revalidate();
	}
    }

    else if (e.getSource() == insetFields[0] ||
	     e.getSource() == insetFields[1] ||
	     e.getSource() == insetFields[2] ||
	     e.getSource() == insetFields[3] ||
	     e.getSource() == positionBox ||
	     e.getSource() == fillBox ||
	     e.getSource() == rowWeightField ||
	     e.getSource() == colWeightField ||
	     e.getSource() == colPositionField ||
	     e.getSource() == skipCellsField ||
	     e.getSource() == rowSpanField ||
	     e.getSource() == colSpanField) {
	String name = (String)compList.getSelectedValue();
	changeLayout(name);
    }

    else if (e.getSource() == columnsField ||
	     e.getSource() == tableInsetFields[0] ||
	     e.getSource() == tableInsetFields[1] ||
	     e.getSource() == tableInsetFields[2] ||
	     e.getSource() == tableInsetFields[3] ||
	     e.getSource() == rowGapField ||
	     e.getSource() == colGapField ||
	     e.getSource() == tablePositionBox ||
	     e.getSource() == tableFillBox ||
	     e.getSource() == tableCellInsetFields[0] ||
	     e.getSource() == tableCellInsetFields[1] ||
	     e.getSource() == tableCellInsetFields[2] ||
	     e.getSource() == tableCellInsetFields[3] ||
	     e.getSource() == tableCellPositionBox ||
	     e.getSource() == tableCellFillBox ||
	     e.getSource() == tableRowWeightField ||
	     e.getSource() == tableColWeightField) {
	changeTableLayout();
    }

    else if (e.getSource() == exitItem) {
	System.exit(0);
    }

    else if (e.getSource() == previewItem) {
	if (layout.isVisible()) {
	    layout.setVisible(false);
	}
	else {
	    layout.setVisible(true);
	}
    }

    else if (e.getSource() == packItem) {
	layout.pack();
    }

    else if (e.getSource() == codeItem) {
	if (code.isVisible()) {
	    code.setVisible(false);
	}
	else {
	    generateCode();
	    code.setVisible(true);
	}
    }

    else if (e.getSource() == generateItem) {
	generateCode();
    }

    else if (e.getSource() == aboutItem) {
	JOptionPane.showMessageDialog(this,
	    "<html>" +
	    "<h1><font face=Dialog>Table Explorer V1.0</font></h1>" +
            "<font face=Dialog>Written by Antonio Freixas<br>" +
	    "<a src=\"mailto:tony@freixas.org\">tonyf@freixas.org</a>",
	    "About Table Explorer</font>",
	    JOptionPane.INFORMATION_MESSAGE);
    }
}

public void
valueChanged(
    ListSelectionEvent e)
{
    if (ignoreEvents) return;

    if (e.getSource() == compList) {
	String name = (String)compList.getSelectedValue();
	int index = compList.getSelectedIndex();

	boolean hasObject = name != null;

	removeButton.setEnabled(hasObject);
	upButton.setEnabled(hasObject && index != 0);
	downButton.setEnabled(hasObject &&
			      index != compListModel.getSize() - 1);
	enableAttributes(hasObject);

	if (hasObject) {
	    Attributes attributes = (Attributes)attrHash.get(name);
	    setAttributes(attributes);
	}
    }
}

public void
focusGained(
    FocusEvent e)
{
    if (e.getSource() == insetFields[0] ||
	e.getSource() == insetFields[1] ||
	e.getSource() == insetFields[2] ||
	e.getSource() == insetFields[3] ||
	e.getSource() == rowWeightField ||
	e.getSource() == colWeightField ||
	e.getSource() == colPositionField ||
	e.getSource() == skipCellsField ||
	e.getSource() == rowSpanField ||
	e.getSource() == colSpanField||
	e.getSource() == columnsField ||
	e.getSource() == tableInsetFields[0] ||
	e.getSource() == tableInsetFields[1] ||
	e.getSource() == tableInsetFields[2] ||
	e.getSource() == tableInsetFields[3] ||
	e.getSource() == rowGapField ||
	e.getSource() == colGapField ||
	e.getSource() == tableCellInsetFields[0] ||
	e.getSource() == tableCellInsetFields[1] ||
	e.getSource() == tableCellInsetFields[2] ||
	e.getSource() == tableCellInsetFields[3] ||
	e.getSource() == tableRowWeightField ||
	e.getSource() == tableColWeightField) {

	JTextField field = (JTextField)e.getSource();
	textWhenFocusGained = field.getText();
    }
    else if (e.getSource() == positionBox ||
	     e.getSource() == fillBox ||
	     e.getSource() == tablePositionBox ||
	     e.getSource() == tableFillBox ||
	     e.getSource() == tableCellPositionBox ||
	     e.getSource() == tableCellFillBox) {

	JComboBox box = (JComboBox)e.getSource();
	textWhenFocusGained = (String)box.getSelectedItem();
    }
    else {
	textWhenFocusGained = null;
    }
}

public void
focusLost(
    FocusEvent e)
{
    if (textWhenFocusGained != null) {
	if (e.getSource() == insetFields[0] ||
	    e.getSource() == insetFields[1] ||
	    e.getSource() == insetFields[2] ||
	    e.getSource() == insetFields[3] ||
	    e.getSource() == rowWeightField ||
	    e.getSource() == colWeightField ||
	    e.getSource() == colPositionField ||
	    e.getSource() == skipCellsField ||
	    e.getSource() == rowSpanField ||
	    e.getSource() == colSpanField) {

	    JTextField field = (JTextField)e.getSource();
	    if (!textWhenFocusGained.equals(field.getText())) {
		changeLayout();
	    }
	}
	else if (e.getSource() == positionBox ||
		 e.getSource() == fillBox) {
	    JComboBox box = (JComboBox)e.getSource();
	    if (!textWhenFocusGained.equals(box.getSelectedItem())) {
		changeLayout();
	    }
	}
	else if (e.getSource() == columnsField ||
		 e.getSource() == tableInsetFields[0] ||
		 e.getSource() == tableInsetFields[1] ||
		 e.getSource() == tableInsetFields[2] ||
		 e.getSource() == tableInsetFields[3] ||
		 e.getSource() == rowGapField ||
		 e.getSource() == colGapField ||
		 e.getSource() == tableCellInsetFields[0] ||
		 e.getSource() == tableCellInsetFields[1] ||
		 e.getSource() == tableCellInsetFields[2] ||
		 e.getSource() == tableCellInsetFields[3] ||
		 e.getSource() == tableRowWeightField ||
		 e.getSource() == tableColWeightField) {

	    JTextField field = (JTextField)e.getSource();
	    if (!textWhenFocusGained.equals(field.getText())) {
		changeTableLayout();
	    }
	}

	else if (e.getSource() == tablePositionBox ||
	     e.getSource() == tableFillBox ||
	     e.getSource() == tableCellPositionBox ||
	     e.getSource() == tableCellFillBox) {

	    JComboBox box = (JComboBox)e.getSource();
	    if (!textWhenFocusGained.equals(box.getSelectedItem())) {
		changeTableLayout();
	    }
	}
    }

    textWhenFocusGained = null;
}

public void
menuCanceled(
    MenuEvent e)
{
}

public void
menuDeselected(
    MenuEvent e)
{
}

public void
menuSelected(
    MenuEvent e)
{
    if (e.getSource() == windowMenu) {
	if (layout.isVisible()) {
	    previewItem.setText("Hide Preview Window");
	}
	else {
	    previewItem.setText("Show Preview Window");
	}
	if (code.isVisible()) {
	    codeItem.setText("Hide Code Window");
	}
	else {
	    codeItem.setText("Show Code Window");
	}
    }
}

//**********************************************************************
// Package Public
//**********************************************************************

JMenuBar
createJMenuBar()
{
    JMenuBar menuBar = new JMenuBar();

    fileMenu = new JMenu("File");
    menuBar.add(fileMenu);
    windowMenu = new JMenu("Window");
    windowMenu.addMenuListener(this);
    menuBar.add(windowMenu);
    helpMenu = new JMenu("Help");
    menuBar.add(helpMenu);

    exitItem = new JMenuItem("Exit");
    exitItem.addActionListener(this);
    fileMenu.add(exitItem);

    previewItem = new JMenuItem("Show Preview Window");
    previewItem.addActionListener(this);
    windowMenu.add(previewItem);

    packItem = new JMenuItem("Pack Preview Window");
    packItem.addActionListener(this);
    windowMenu.add(packItem);

    windowMenu.add(new JSeparator());

    codeItem = new JMenuItem("Show Code Window");
    codeItem.addActionListener(this);
    windowMenu.add(codeItem);

    generateItem = new JMenuItem("Generate Code");
    generateItem.addActionListener(this);
    windowMenu.add(generateItem);

    aboutItem = new JMenuItem("About TableExplorer...");
    aboutItem.addActionListener(this);
    helpMenu.add(aboutItem);

    return menuBar;
}

Component
createButtonPane()
{
    JPanel topLevel = new JPanel(new TableLayout("cols=1"));

    upButton = new JButton("Up");
    upButton.setEnabled(false);
    upButton.addActionListener(this);

    downButton = new JButton("Down");
    downButton.setEnabled(false);
    downButton.addActionListener(this);

    removeButton = new JButton("Remove");
    removeButton.setEnabled(false);
    removeButton.addActionListener(this);

    addButton = new JButton("Add");
    addButton.addActionListener(this);

    topLevel.add(upButton);
    topLevel.add(downButton, "rweight=1 n fh");
    topLevel.add(removeButton, "rweight=1 s fh ");
    topLevel.add(addButton);

    return topLevel;
}

Component
createListPane()
{
    JPanel topLevel = new JPanel(new TableLayout("cols=1 rgap=2"));

    compListModel = new DefaultListModel();
    compList = new JList(compListModel);
    compList.setVisibleRowCount(20);
    compList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    compList.addListSelectionListener(this);

    compEntryField = new JTextField("Start Here!");
    compEntryField.selectAll();
    compEntryField.setColumns(20);
    compEntryField.addCaretListener(this);
    compEntryField.addActionListener(this);

    topLevel.add(compList, "rweight=1");
    topLevel.add(compEntryField);

    return topLevel;
}

Component
createAttributePane()
{
    JPanel topLevel = new JPanel(new TableLayout("cols=3 rgap=2 cgap=5"));

    // Table Attributes

    topLevel.add(new JLabel("Table Attributes"), "cspan=3");

    JPanel spacer1= new JPanel();
    spacer1.setSize(20, 1);
    topLevel.add(spacer1);
    topLevel.add(new JLabel("Columns"));
    columnsField = new JTextField(10);
    columnsField.addActionListener(this);
    columnsField.addFocusListener(this);
    topLevel.add(columnsField);

    topLevel.add(new JLabel("Table insets"), "col=1");
    topLevel.add(createInsetsPane(tableInsetFields));

    topLevel.add(new JLabel("Row gap"), "col=1");
    rowGapField = new JTextField(10);
    rowGapField.addActionListener(this);
    rowGapField.addFocusListener(this);
    topLevel.add(rowGapField);

    topLevel.add(new JLabel("Column gap"), "col=1");
    colGapField = new JTextField(10);
    colGapField.addActionListener(this);
    colGapField.addFocusListener(this);
    topLevel.add(colGapField);

    topLevel.add(new JLabel("Table position"), "col=1");
    tablePositionBox = new JComboBox(tablePositionList);
    tablePositionBox.addActionListener(this);
    topLevel.add(tablePositionBox);

    topLevel.add(new JLabel("Table fill"), "col=1");
    tableFillBox = new JComboBox(tableFillList);
    tableFillBox.addActionListener(this);
    topLevel.add(tableFillBox);

    // Table Cell Defaults

    topLevel.add(new JLabel("Table Cell Defaults"), "cspan=3 itop=20");

    topLevel.add(new JLabel("Cell insets"), "col=1");
    topLevel.add(createInsetsPane(tableCellInsetFields));

    topLevel.add(new JLabel("Cell position"), "col=1");
    tableCellPositionBox = new JComboBox(positionList);
    tableCellPositionBox.addActionListener(this);
    topLevel.add(tableCellPositionBox);

    topLevel.add(new JLabel("Cell fill"), "col=1");
    tableCellFillBox = new JComboBox(fillList);
    tableCellFillBox.addActionListener(this);
    topLevel.add(tableCellFillBox);

    topLevel.add(new JLabel("Row weight"), "col=1");
    tableRowWeightField = new JTextField(10);
    tableRowWeightField.addActionListener(this);
    tableRowWeightField.addFocusListener(this);
    topLevel.add(tableRowWeightField);

    topLevel.add(new JLabel("Column weight"), "col=1");
    tableColWeightField = new JTextField(10);
    tableColWeightField.addActionListener(this);

    tableColWeightField.addFocusListener(this);
    topLevel.add(tableColWeightField);

    topLevel.add(new JLabel("Attributes"), "itop=5 col=0 cspan=2");
    tableAttributesField = new JTextField();
    tableAttributesField.setEditable(false);
    topLevel.add(tableAttributesField, "itop=5");

    // Make sure components are initialized as per the default table
    // attributes

    tableAttributes = new TableAttributes();
    setTableAttributes(tableAttributes);

    // Cell Attributes

    topLevel.add(new JSeparator(), "cspan=3 itop=20 ibottom=5");

    topLevel.add(new JLabel("Cell Attributes"), "cspan=3");

    topLevel.add(new JLabel("Cell insets"), "col=1");
    topLevel.add(createInsetsPane(insetFields));

    topLevel.add(new JLabel("Cell position"), "col=1");
    positionBox = new JComboBox(positionList);
    positionBox.addActionListener(this);
    topLevel.add(positionBox);

    topLevel.add(new JLabel("Cell fill"), "col=1");
    fillBox = new JComboBox(fillList);
    fillBox.addActionListener(this);
    topLevel.add(fillBox);

    topLevel.add(new JLabel("Row weight"), "col=1");
    rowWeightField = new JTextField(10);
    rowWeightField.addActionListener(this);
    rowWeightField.addFocusListener(this);
    topLevel.add(rowWeightField);

    topLevel.add(new JLabel("Column weight"), "col=1");
    colWeightField = new JTextField(10);
    colWeightField.addActionListener(this);
    colWeightField.addFocusListener(this);
    topLevel.add(colWeightField);

    topLevel.add(new JLabel("Column position"), "col=1");
    colPositionField = new JTextField(10);
    colPositionField.addActionListener(this);
    colPositionField.addFocusListener(this);
    topLevel.add(colPositionField);

    topLevel.add(new JLabel("Skip cells"), "col=1");
    skipCellsField = new JTextField(10);
    skipCellsField.addActionListener(this);
    skipCellsField.addFocusListener(this);
    topLevel.add(skipCellsField);

    topLevel.add(new JLabel("Row span"), "col=1");
    rowSpanField = new JTextField(10);
    rowSpanField.addActionListener(this);
    rowSpanField.addFocusListener(this);
    topLevel.add(rowSpanField);

    topLevel.add(new JLabel("Column span"), "col=1");
    colSpanField = new JTextField(10);
    colSpanField.addActionListener(this);
    colSpanField.addFocusListener(this);
    topLevel.add(colSpanField);

    topLevel.add(new JLabel("Attributes"), "itop=5 col=0 cspan=2");
    cellAttributesField = new JTextField();
    cellAttributesField.setEditable(false);
    topLevel.add(cellAttributesField, "itop=5");

    enableAttributes(false);

    return topLevel;
}

Component
createInsetsPane(
    JTextField[] insetFields)
{
    JPanel topLevel = new JPanel(new TableLayout("cols=3"));

    for (int i = 0; i < 4; i++) {
	insetFields[i] = new JTextField(2);
	insetFields[i].addActionListener(this);
	insetFields[i].addFocusListener(this);
    }

    topLevel.add(insetFields[0], "col=1");
    topLevel.add(insetFields[1], "col=0");
    topLevel.add(insetFields[2], "col=2");
    topLevel.add(insetFields[3], "col=1");

    return topLevel;
}

JPanel
createCodePane()
{
    JPanel topLevel = new JPanel(new TableLayout("cols=2 rgap=2 cgap=5"));

    codeTextArea = new JTextArea(15, 30);
    codeTextScroll = new JScrollPane(codeTextArea);
    topLevel.add(codeTextScroll, "cspan=2 rweight=1");

    return topLevel;
}

void
addToLayout(
    Component c)
{
    try {
	layoutPane.add(c);
    }
    catch (IllegalArgumentException e) {
	JOptionPane.showMessageDialog(
	    this, e.toString(), "Attribute Error", JOptionPane.ERROR_MESSAGE);
    }
    layoutPane.revalidate();
}

void
addToLayout(
    Component c,
    String a,
    int index)
{
    try {
	layoutPane.add(c, a, index);
    }
    catch (IllegalArgumentException e) {
	JOptionPane.showMessageDialog(
	    this, e.toString(), "Attribute Error", JOptionPane.ERROR_MESSAGE);
    }
    layoutPane.revalidate();
}

void
changeTableLayout()
{
    tableAttributes = getTableAttributes();
    try {
	((TableLayout)layoutPane.getLayout()).setTableAttributes(
	    tableAttributes.toString());
    }
    catch (IllegalArgumentException e) {
	JOptionPane.showMessageDialog(
	    this, e.toString(), "Attribute Error", JOptionPane.ERROR_MESSAGE);
    }
    layoutPane.revalidate();
    setTableAttributes(tableAttributes);	// Normalize appearance
}

void
changeLayout()
{
    String name = (String)compList.getSelectedValue();
    changeLayout(name);
}

void
changeLayout(
    String name)
{
    if (name == null) return;

    Component c = (Component)compHash.get(name);
    Attributes attributes = getAttributes();
    try {
	((TableLayout)layoutPane.getLayout()).setAttributes(
            c, attributes.toString());
    }
    catch (IllegalArgumentException e) {
	JOptionPane.showMessageDialog(
            this, e.toString(), "Attribute Error", JOptionPane.ERROR_MESSAGE);
    }
    layoutPane.revalidate();
    setAttributes(attributes);		// Normalize appearance
    attrHash.put(name, attributes);
}

TableAttributes
getTableAttributes()
{
    TableAttributes attributes = new TableAttributes();

    attributes.columns = getNumber(attributes.columns, columnsField);
    attributes.rGap = getNumber(attributes.rGap, rowGapField);
    attributes.cGap = getNumber(attributes.cGap, colGapField);

    attributes.tableInsets.top =
	getNumber(attributes.tableInsets.top, tableInsetFields[0]);
    attributes.tableInsets.left =
	getNumber(attributes.tableInsets.left, tableInsetFields[1]);
    attributes.tableInsets.right =
	getNumber(attributes.tableInsets.right, tableInsetFields[2]);
    attributes.tableInsets.bottom =
	getNumber(attributes.tableInsets.bottom, tableInsetFields[3]);

    attributes.tablePosition = getString(tablePositionBox);
    attributes.tableFill = getString(tableFillBox);

    attributes.insets.top =
	getNumber(attributes.insets.top, tableCellInsetFields[0]);
    attributes.insets.left =
	getNumber(attributes.insets.left, tableCellInsetFields[1]);
    attributes.insets.right =
	getNumber(attributes.insets.right, tableCellInsetFields[2]);
    attributes.insets.bottom =
	getNumber(attributes.insets.bottom, tableCellInsetFields[3]);

    attributes.position = getString(tableCellPositionBox);
    attributes.fill = getString(tableCellFillBox);
    attributes.rWeight = getNumber(attributes.rWeight, tableRowWeightField);
    attributes.cWeight = getNumber(attributes.cWeight, tableColWeightField);

    return attributes;
}

void
setTableAttributes(
    TableAttributes attributes)
{
    ignoreEvents = true;

    columnsField.setText(Integer.toString(attributes.columns));
    rowGapField.setText(Integer.toString(attributes.rGap));
    colGapField.setText(Integer.toString(attributes.cGap));

    tableInsetFields[0].setText(
	Integer.toString(attributes.tableInsets.top));
    tableInsetFields[1].setText(
	Integer.toString(attributes.tableInsets.left));
    tableInsetFields[2].setText(
	Integer.toString(attributes.tableInsets.right));
    tableInsetFields[3].setText(
	Integer.toString(attributes.tableInsets.bottom));

    tablePositionBox.setSelectedItem(attributes.tablePosition);
    tableFillBox.setSelectedItem(attributes.tableFill);

    tableCellInsetFields[0].setText(
	Integer.toString(attributes.insets.top));
    tableCellInsetFields[1].setText(
	Integer.toString(attributes.insets.left));
    tableCellInsetFields[2].setText(
	Integer.toString(attributes.insets.right));
    tableCellInsetFields[3].setText(
	Integer.toString(attributes.insets.bottom));

    tableCellPositionBox.setSelectedItem(attributes.position);
    tableCellFillBox.setSelectedItem(attributes.fill);
    tableRowWeightField.setText(Integer.toString(attributes.rWeight));
    tableColWeightField.setText(Integer.toString(attributes.cWeight));

    tableAttributesField.setText(attributes.toString());

    ignoreEvents = false;
}

Attributes
getAttributes()
{
    Attributes attributes = new Attributes();

    attributes.insets.top =
	getNumber(attributes.insets.top, insetFields[0]);
    attributes.insets.left =
	getNumber(attributes.insets.left, insetFields[1]);
    attributes.insets.right =
	getNumber(attributes.insets.right, insetFields[2]);
    attributes.insets.bottom =
	getNumber(attributes.insets.bottom, insetFields[3]);

    attributes.position = getString(positionBox);
    attributes.fill = getString(fillBox);
    attributes.rWeight = getNumber(attributes.rWeight, rowWeightField);
    attributes.cWeight = getNumber(attributes.cWeight, colWeightField);
    attributes.column = getNumber(attributes.column, colPositionField);
    attributes.skip = getNumber(attributes.skip, skipCellsField);
    attributes.rSpan = getNumber(attributes.rSpan, rowSpanField);
    attributes.cSpan = getNumber(attributes.cSpan, colSpanField);

    return attributes;
}

void
setAttributes(
    Attributes attributes)
{
    ignoreEvents = true;

    insetFields[0].setText(Integer.toString(attributes.insets.top));
    insetFields[1].setText(Integer.toString(attributes.insets.left));
    insetFields[2].setText(Integer.toString(attributes.insets.right));
    insetFields[3].setText(Integer.toString(attributes.insets.bottom));

    positionBox.setSelectedItem(attributes.position);
    fillBox.setSelectedItem(attributes.fill);
    rowWeightField.setText(Integer.toString(attributes.rWeight));
    colWeightField.setText(Integer.toString(attributes.cWeight));
    colPositionField.setText(Integer.toString(attributes.column));
    skipCellsField.setText(Integer.toString(attributes.skip));
    rowSpanField.setText(Integer.toString(attributes.rSpan));
    colSpanField.setText(Integer.toString(attributes.cSpan));

    cellAttributesField.setText(attributes.toString());

    ignoreEvents = false;
}

int
getNumber(
    int defaultValue,
    JTextField field)
{
    String text = field.getText().trim();
    if (text.length() == 0) return defaultValue;

    int value = 0;
    try {
	value = Integer.parseInt(text);
    }
    catch (NumberFormatException e) {}

    return value;
}

String
getString(
    JComboBox box)
{
    return (String)box.getSelectedItem();
}

void
enableAttributes(
    boolean enable)
{
    positionBox.setEnabled(enable);
    fillBox.setEnabled(enable);
    rowWeightField.setEnabled(enable);
    colWeightField.setEnabled(enable);
    colPositionField.setEnabled(enable);
    skipCellsField.setEnabled(enable);
    rowSpanField.setEnabled(enable);
    colSpanField.setEnabled(enable);

    for (int i = 0; i < 4; i++) {
	insetFields[i].setEnabled(enable);
    }
}

void
generateCode()
{
    tableAttributes = getTableAttributes();

    String indent = "";
    String containerName = "";

    StringBuffer code = new StringBuffer(
	indent + "// Code generated by Table Explorer V1.0\n" +
	indent +
	"// Copyright � 2004, Antonio Freixas\n" +
	indent + "// All Rights Reserved.\n" +
	indent + "// tony@freixas.org\n\n" +
	indent + "// Define the table layout\n\n" +
	indent + "JPanel panel = new JPanel(new TableLayout(" +
	"\"" + tableAttributes.toString().trim() + "\"));\n\n");

    for (int i = 0; i < compListModel.size(); i++) {
	String name = (String)compListModel.elementAt(i);
	Attributes attr = (Attributes)attrHash.get(name);
	String sAttr = attr.toString().trim();
	code.append(
	    indent + "panel.add(new JButton(\"" + name + "\"" +
            (sAttr.length() > 0 ? ", \"" + sAttr + "\"" : "") +  "));\n");
    }

    codeTextArea.setText(code.toString());
}

//**********************************************************************
// Protected
//**********************************************************************

//**********************************************************************
// Private
//**********************************************************************

//**********************************************************************
// Inner Classes
//**********************************************************************

class TableAttributes
{

// Table-only options

int columns = 1;
int rGap = 0;
int cGap = 0;
Insets tableInsets = new Insets(0, 0, 0, 0);
String tablePosition = "Default";
String tableFill = "Default";

// Table/cell options

Insets insets = new Insets(0, 0, 0, 0);
String position = "Default";
String fill = "Default";
int rWeight = 0;
int cWeight = 0;

public String
toString()
{
    StringBuffer b = new StringBuffer();

    if (columns != 1) {
	b.append("cols=" + columns + " ");
    }

    if (rGap != 0) {
	b.append("rgap=" + rGap + " ");
    }
    if (cGap != 0) {
	b.append("cgap=" + cGap + " ");
    }

    if (tableInsets.top != 0) {
	b.append("titop=" + tableInsets.top + " ");
    }
    if (tableInsets.bottom != 0) {
	b.append("tibottom=" + tableInsets.bottom + " ");
    }
    if (tableInsets.left != 0) {
	b.append("tileft=" + tableInsets.left + " ");
    }
    if (tableInsets.right != 0) {
	b.append("tiright=" + tableInsets.right + " ");
    }

    if (!"Default".equals(tablePosition)) {
	b.append(tablePosition + " ");
    }
    if (!"Default".equals(tableFill)) {
	b.append(tableFill + " ");
    }

    if (insets.top != 0) {
	b.append("itop=" + insets.top + " ");
    }
    if (insets.bottom != 0) {
	b.append("ibottom=" + insets.bottom + " ");
    }
    if (insets.left != 0) {
	b.append("ileft=" + insets.left + " ");
    }
    if (insets.right != 0) {
	b.append("iright=" + insets.right + " ");
    }

    if (!"Default".equals(position)) {
	b.append(position + " ");
    }
    if (!"Default".equals(fill)) {
	b.append(fill + " ");
    }

    if (rWeight != 0) {
	b.append("rweight=" + rWeight + " ");
    }
    if (cWeight != 0) {
	b.append("cweight=" + cWeight + " ");
    }

    return new String(b);
}

}

class Attributes
{

Insets insets = new Insets(0, 0, 0, 0);
String position = "Default";
String fill = "Default";
int rWeight = 0;
int cWeight = 0;
int column = -1;
int skip = 0;
int rSpan = 1;
int cSpan = 1;

public String
toString()
{
    StringBuffer b = new StringBuffer();

    if (insets.top != tableAttributes.insets.top) {
	b.append("itop=" + insets.top + " ");
    }
    if (insets.bottom != tableAttributes.insets.bottom) {
	b.append("ibottom=" + insets.bottom + " ");
    }
    if (insets.left != tableAttributes.insets.left) {
	b.append("ileft=" + insets.left + " ");
    }
    if (insets.right != tableAttributes.insets.right) {
	b.append("iright=" + insets.right + " ");
    }

    if (!"Default".equals(position)) {
	b.append(position + " ");
    }
    if (!"Default".equals(fill)) {
	b.append(fill + " ");
    }

    if (rWeight != tableAttributes.rWeight) {
	b.append("rweight=" + rWeight + " ");
    }
    if (cWeight != tableAttributes.cWeight) {
	b.append("cweight=" + cWeight + " ");
    }

    if (column != -1) {
	b.append("col=" + column + " ");
    }

    if (skip != 0) {
	b.append("skip=" + skip + " ");
    }

    if (rSpan != 1) {
	b.append("rspan=" + rSpan + " ");
    }
    if (cSpan != 1) {
	b.append("cspan=" + cSpan + " ");
    }

    return new String(b);
}

}

//**********************************************************************
// End Inner Classes
//**********************************************************************

}
