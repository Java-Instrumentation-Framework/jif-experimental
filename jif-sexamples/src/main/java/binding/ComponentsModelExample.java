package binding;

import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.list.SelectionInList;


import com.jgoodies.binding.value.*;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how to bind different value types to Swing components.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.15 $
 *
 * @see com.jgoodies.binding.adapter.BasicComponentFactory
 * @see com.jgoodies.binding.adapter.Bindings
 */
public final class ComponentsModelExample {

	// Holds an ExampleBindingBean and vends ValueModels that adapt its properties.
	private final ExamplePresentationModel presentationModel;
	// Text Components
	private JTextField textField;
	private JTextArea textArea;
	private JPasswordField passwordField;
	private JLabel textLabel;
	// Formatted Input
	private JFormattedTextField dateField;
	private JFormattedTextField integerField;
	private JFormattedTextField longField;
	// Lists
	private JComboBox comboBox;
	private JList list;
	private JTable table;
	// Choice
	private JRadioButton leftIntRadio;
	private JRadioButton centerIntRadio;
	private JRadioButton rightIntRadio;
	private JComboBox alignmentIntCombo;
	private JRadioButton leftObjectRadio;
	private JRadioButton centerObjectRadio;
	private JRadioButton rightObjectRadio;
	private JComboBox alignmentObjectCombo;
	// Misc
	private JCheckBox checkBox;
	private JPanel colorPreview;
	private JSlider slider;
	private JLabel floatLabel;
	private JLabel intLabel;
	private JSpinner spinner;
	private JScrollBar scrollbar;

	// Launching **************************************************************
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		} catch (Exception e) {
			// Likely PlasticXP is not in the class path; ignore.
		}
		JFrame frame = new JFrame();
		frame.setTitle("Binding Tutorial :: Components");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		ComponentsModelExample example = new ComponentsModelExample();
		JComponent panel = example.buildPanel();
		frame.getContentPane().add(panel);
		frame.pack();
		//TutorialUtils.locateOnScreenCenter(frame);
		frame.setVisible(true);
	}

	// Instance Creation ******************************************************
	/**
	 * Constructs the 'Components' example on an instance of ExampleBindingBean.
	 */
	public ComponentsModelExample() {
		presentationModel = new ExamplePresentationModel(new ExampleBindingBean());
	}

	// Component Creation and Initialization **********************************
	/**
	 * Creates, binds and configures the UI components.<p>
	 *
	 * If possible, the components are created using the BasicComponentFactory, or the Bindings class.
	 */
	private void initComponents() {
		// Text Components
		textField = BasicComponentFactory.createTextField(presentationModel.getModel(
				ExampleBindingBean.PROPERTYNAME_TEXT));
		textArea = BasicComponentFactory.createTextArea(presentationModel.getModel(
				ExampleBindingBean.PROPERTYNAME_TEXT));
		passwordField = BasicComponentFactory.createPasswordField(presentationModel.
				getModel(ExampleBindingBean.PROPERTYNAME_TEXT));
		textLabel = BasicComponentFactory.createLabel(presentationModel.getModel(
				ExampleBindingBean.PROPERTYNAME_TEXT));

		// Formatted Input
		dateField = BasicComponentFactory.createDateField(presentationModel.getModel(
				ExampleBindingBean.PROPERTYNAME_DATE));
		integerField = BasicComponentFactory.createIntegerField(presentationModel.getModel(
				ExampleBindingBean.PROPERTYNAME_INT_VALUE));
		longField = BasicComponentFactory.createLongField(presentationModel.getModel(
				ExampleBindingBean.PROPERTYNAME_LONG_VALUE));

		// Choice
		ValueModel intChoiceModel = presentationModel.getModel(ExampleBindingBean.PROP_INT_CHOICE);
		leftIntRadio = BasicComponentFactory.createRadioButton(intChoiceModel,
				ExampleBindingBean.LEFT_INTEGER, "Left");
		centerIntRadio = BasicComponentFactory.createRadioButton(intChoiceModel,
				ExampleBindingBean.CENTER_INTEGER, "Center");
		rightIntRadio = BasicComponentFactory.createRadioButton(intChoiceModel,
				ExampleBindingBean.RIGHT_INTEGER, "Right");
		alignmentIntCombo = BasicComponentFactory.createComboBox(new SelectionInList(
				ExampleBindingBean.INTEGER_CHOICES, intChoiceModel));

//      ValueModel objectChoiceModel = presentationModel.getModel(ExampleBindingBean.
//            PROPERTYNAME_OBJECT_CHOICE);
		leftObjectRadio = BasicComponentFactory.createRadioButton(
				presentationModel.getModel(ExampleBindingBean.PROPERTYNAME_OBJECT_CHOICE),
				ExampleBindingBean.LEFT, "Left");
		centerObjectRadio = BasicComponentFactory.createRadioButton(
				presentationModel.getModel(ExampleBindingBean.PROPERTYNAME_OBJECT_CHOICE),
				ExampleBindingBean.CENTER, "Center");
		rightObjectRadio = BasicComponentFactory.createRadioButton(
				presentationModel.getModel(ExampleBindingBean.PROPERTYNAME_OBJECT_CHOICE),
				ExampleBindingBean.RIGHT, "Right");
		alignmentObjectCombo = BasicComponentFactory.createComboBox(new SelectionInList(
				ExampleBindingBean.OBJECT_CHOICES,
				presentationModel.getModel(ExampleBindingBean.PROPERTYNAME_OBJECT_CHOICE)));

		// Lists
		comboBox = BasicComponentFactory.createComboBox(presentationModel.
				getSelectionInList(), TutorialUtils.createModeListCellRenderer());

		list = BasicComponentFactory.createList(presentationModel.getSelectionInList(),
				TutorialUtils.createModeListCellRenderer());

		table = new JTable();
		table.setModel(TutorialUtils.createModeTableModel(presentationModel.
				getSelectionInList()));
		table.setSelectionModel(new SingleListSelectionAdapter(presentationModel.
				getSelectionInList().getSelectionIndexHolder()));

		// Misc
		checkBox = BasicComponentFactory.createCheckBox(presentationModel.getModel(
				ExampleBindingBean.PROPERTYNAME_BOOLEAN_VALUE), "available");
		colorPreview = new JPanel();
		colorPreview.setBorder(new LineBorder(Color.GRAY));
		updatePreviewPanel();

		ValueModel floatModel = presentationModel.getModel(ExampleBindingBean.PROPERTYNAME_FLOAT_VALUE);
		slider = new JSlider();
		slider.setModel(new BoundedRangeAdapter(ConverterFactory.
				createFloatToIntegerConverter(floatModel, 100), 0, 0, 100));
		floatLabel = BasicComponentFactory.createLabel(ConverterFactory.
				createStringConverter(floatModel, NumberFormat.getPercentInstance()));
		spinner = new JSpinner();
		spinner.setModel(SpinnerAdapterFactory.createNumberAdapter(presentationModel.
				getModel(ExampleBindingBean.PROPERTYNAME_INT_LIMITED), 0, 0, 100, 5));
		// defaultValue,  minValue,  maxValue,  step
		ValueModel intModel = presentationModel.getModel(ExampleBindingBean.PROPERTYNAME_INT_VALUE);
		scrollbar = new JScrollBar();
		scrollbar.setModel((new BoundedRangeAdapter(
				presentationModel.getModel(ExampleBindingBean.PROPERTYNAME_INT_LIMITED), 0, 0, 100)));
		intLabel = BasicComponentFactory.createLabel(ConverterFactory.createStringConverter(presentationModel.getModel(ExampleBindingBean.PROPERTYNAME_INT_LIMITED),NumberFormat.getIntegerInstance()));

	}

	private void initEventHandling() {
		presentationModel.getModel(ExampleBindingBean.PROPERTYNAME_COLOR).addValueChangeListener(new ColorUpdateHandler());
	}

	private void updatePreviewPanel() {
		colorPreview.setBackground(((ExampleBindingBean) presentationModel.getBean()).getColor());
	}

	// Building ***************************************************************
	/**
	 * Builds and returns the panel.
	 *
	 * @return the built panel
	 */
	public JComponent buildPanel() {
		initComponents();
		initEventHandling();

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);

		tabbedPane.addTab("Text", buildTextPanel());
		tabbedPane.addTab("Formatted", buildFormattedPanel());
		tabbedPane.addTab("Choices", buildChoicesPanel());
		tabbedPane.addTab("List", buildListPanel());
		tabbedPane.addTab("Misc", buildMiscPanel());
		return tabbedPane;
	}

	private JPanel buildTextPanel() {
		FormLayout layout = new FormLayout("right:max(50dlu;pref), 3dlu, 50dlu",
				"p, 3dlu, p, 3dlu, p, 14dlu, 3dlu, p");
		layout.setRowGroups(new int[][]{{1, 3, 5}});

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel("JTextField", cc.xy(1, 1));
		builder.add(textField, cc.xy(3, 1));
		builder.addLabel("JPasswordField", cc.xy(1, 3));
		builder.add(passwordField, cc.xy(3, 3));
		builder.addLabel("JTextArea", cc.xy(1, 5));
		builder.add(new JScrollPane(textArea), cc.xywh(3, 5, 1, 2));
		builder.addLabel("JLabel", cc.xy(1, 8));
		builder.add(textLabel, cc.xy(3, 8));
		return builder.getPanel();
	}

	private JPanel buildFormattedPanel() {
		FormLayout layout = new FormLayout("right:max(50dlu;pref), 3dlu, 50dlu",
				"p, 3dlu, p, 3dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel("Date", cc.xy(1, 1));
		builder.add(dateField, cc.xy(3, 1));
		builder.addLabel("Integer", cc.xy(1, 3));
		builder.add(integerField, cc.xy(3, 3));
		builder.addLabel("Long", cc.xy(1, 5));
		builder.add(longField, cc.xy(3, 5));
		return builder.getPanel();
	}

	private JPanel buildChoicesPanel() {
		FormLayout layout = new FormLayout(
				"right:max(50dlu;pref), 3dlu, p, 6dlu, p, 6dlu, p, 0:grow",
				"p, 3dlu, p, 3dlu, p, 9dlu, p, 3dlu, p, 3dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addSeparator("Integer Choice", cc.xyw(1, 1, 8));
		builder.addLabel("JRadioButton", cc.xy(1, 3));
		builder.add(leftIntRadio, cc.xy(3, 3));
		builder.add(centerIntRadio, cc.xy(5, 3));
		builder.add(rightIntRadio, cc.xy(7, 3));
		builder.addLabel("JComboBox", cc.xy(1, 5));
		builder.add(alignmentIntCombo, cc.xyw(3, 5, 3));

		builder.addSeparator("Object Choice", cc.xyw(1, 7, 8));
		builder.addLabel("JRadioButton", cc.xy(1, 9));
		builder.add(leftObjectRadio, cc.xy(3, 9));
		builder.add(centerObjectRadio, cc.xy(5, 9));
		builder.add(rightObjectRadio, cc.xy(7, 9));
		builder.addLabel("JComboBox", cc.xy(1, 11));
		builder.add(alignmentObjectCombo, cc.xyw(3, 11, 3));
		return builder.getPanel();
	}

	private JPanel buildListPanel() {
		FormLayout layout = new FormLayout("right:max(50dlu;pref), 3dlu, 150dlu",
				"fill:60dlu, 6dlu, fill:60dlu, 6dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.addLabel("JList", cc.xy(1, 1, "right, top"));
		builder.add(new JScrollPane(list), cc.xy(3, 1));
		builder.addLabel("JTable", cc.xy(1, 3, "right, top"));
		builder.add(new JScrollPane(table), cc.xy(3, 3));
		builder.addLabel("JComboBox", cc.xy(1, 5));
		builder.add(comboBox, cc.xy(3, 5));
		return builder.getPanel();
	}

	private JPanel buildMiscPanel() {
		FormLayout layout = new FormLayout(
				"right:max(50dlu;pref), 3dlu, 50dlu, 3dlu, 50dlu",
				"p, 3dlu, p, 3dlu, p, 3dlu, p");
		layout.setRowGroups(new int[][]{{1, 3, 5}});

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();

		Action chooseAction = new ChooseColorAction(builder.getPanel(),
				presentationModel.getModel(ExampleBindingBean.PROPERTYNAME_COLOR));

		CellConstraints cc = new CellConstraints();
		builder.addLabel("JCheckBox", cc.xy(1, 1));
		builder.add(checkBox, cc.xy(3, 1));
		builder.addLabel("JSlider", cc.xy(1, 3));
		builder.add(slider, cc.xy(3, 3));
		builder.add(floatLabel, cc.xy(5, 3));
		//builder.addLabel("JSpinner", cc.xy(1, 5));
		//builder.add(spinner, cc.xy(3, 5));
		builder.addLabel("JColorChooser", cc.xy(1, 7));
		builder.add(colorPreview, cc.xy(3, 7, "fill, fill"));
		builder.add(new JButton(chooseAction), cc.xy(5, 7, "left, center"));
		//
		builder.addLabel("JScrollBar", cc.xy(1, 5));
		builder.add(scrollbar, cc.xy(3, 5));
		scrollbar.setOrientation(JScrollBar.HORIZONTAL);
		builder.add(intLabel, cc.xy(5, 5));
		return builder.getPanel();
	}

	// Helper Code ************************************************************
	// A custom PresentationModel that provides a SelectionInList
	// for the bean's ListModel and the bean's list selection.
	private static final class ExamplePresentationModel
			extends PresentationModel {

		/**
		 * Holds the bean's list model plus a selection.
		 */
		private final SelectionInList selectionInList;

		// Instance Creation -----------------------------------------
		private ExamplePresentationModel(ExampleBindingBean ExampleBindingBean) {
			super(ExampleBindingBean);
			selectionInList = new SelectionInList(ExampleBindingBean.getListModel(),
					getModel(ExampleBindingBean.PROPERTYNAME_LIST_SELECTION));
		}

		// Custom Models ---------------------------------------------
		public SelectionInList getSelectionInList() {
			return selectionInList;
		}

	}

	private final class ColorUpdateHandler
			implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			updatePreviewPanel();
		}

	}

	private static final class ChooseColorAction
			extends AbstractAction {

		private final Component parent;
		private final ValueModel bufferedColorModel;
		private final Trigger trigger;

		private ChooseColorAction(Component parent, ValueModel colorModel) {
			super("\u2026");
			this.parent = parent;
			this.trigger = new Trigger();
			this.bufferedColorModel = new BufferedValueModel(colorModel, trigger);
		}

		public void actionPerformed(ActionEvent e) {
			JColorChooser colorChooser = BasicComponentFactory.createColorChooser(
					bufferedColorModel);
			ActionListener okHandler = new OKHandler(trigger);
			JDialog dialog = JColorChooser.createDialog(parent, "Choose Color", true,
					colorChooser, okHandler, null);
			dialog.addWindowListener(new Closer());
			dialog.addComponentListener(new DisposeOnClose());

			dialog.show(); // blocks until user brings dialog down...
		}

		private static final class Closer
				extends WindowAdapter implements Serializable {

			public void windowClosing(WindowEvent e) {
				Window w = e.getWindow();
				w.hide();
			}

		}

		private static final class DisposeOnClose
				extends ComponentAdapter implements Serializable {

			public void componentHidden(ComponentEvent e) {
				Window w = (Window) e.getComponent();
				w.dispose();
			}

		}

		private static final class OKHandler
				implements ActionListener {

			private final Trigger trigger;

			private OKHandler(Trigger trigger) {
				this.trigger = trigger;
			}

			public void actionPerformed(ActionEvent e) {
				trigger.triggerCommit();
			}

		}
	}
}
