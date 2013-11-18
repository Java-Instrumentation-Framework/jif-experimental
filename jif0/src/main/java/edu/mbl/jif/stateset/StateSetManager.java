package edu.mbl.jif.stateset;


import edu.mbl.jif.io.xml.ObjectStoreXML;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
//import org.metawidget.inspector.propertytype.PropertyTypeInspector;
//import org.metawidget.swing.SwingMetawidget;
//import org.metawidget.swing.SwingMetawidget;
//import org.metawidget.inspector.propertytype.PropertyTypeInspector;
//import org.metawidget.swing.SwingMetawidget;
//import org.metawidget.swing.binding.beansbinding.BeansBinding;

/**
 *
 * @author GBH
 */
public class StateSetManager {

	ArrayList<SavedStateSet> sets = new ArrayList<SavedStateSet>();
	ArrayList<String> setNames = new ArrayList<String>();

	public void switchToSetting(int i) {
		// switch to a savedStateSet
	}

	public void showSettings() {
	}

	public void addSet(SavedStateSet set) {
		sets.add(set);
		setNames.add(set.getName());
	}

	public void removeSet(String name) {
	}

	public ArrayList<String> getSetNames() {
		return setNames;
	}

	public void saveToFile(SavedStateSet set, String filename) {
		try {
			ObjectStoreXML.write(set, filename);
		} catch (Throwable ex) {
			Logger.getLogger(StateSetManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void loadSets(String directory) {
		// load all the sets in a given directory 
		SavedStateSet set = loadFromFile("");
		addSet(set);
		// add each set's name to list of setNames
	}

	public SavedStateSet loadFromFile(String filename) {
		try {
			return (SavedStateSet) ObjectStoreXML.read(filename);
		} catch (Throwable ex) {
			Logger.getLogger(StateSetManager.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	//=============================================================================

	public static void main(String[] args) {
		final StateSetManager setMgr = new StateSetManager();
		TestModelBean model = new TestModelBean();
		TestModelBean2 model2 = new TestModelBean2();

		PropertyChangeListener propChangeWatcher = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println("PropChange: " + evt.getPropertyName() + " = "
						+ evt.getNewValue());
			}

		};
		model.addPropertyChangeListener(propChangeWatcher);
		model2.addPropertyChangeListener(propChangeWatcher);


		// Using maps...
		SavedStateMap sMap1 = new SavedStateMap("DisplayName", model.getClass().getName());
		sMap1.addProperty("aString", new String("StringOne"));
		sMap1.addProperty("anInteger", new Integer(17));
		sMap1.addProperty("aboolean", new Boolean(true));
		SavedState state1 = new SavedState(sMap1, model);
		
		SavedStateSet set = new SavedStateSet("SetA");
		
		set.addState(state1);
		model.setA("Different");
		
		SavedState state2 = new SavedState(sMap1, model);
		set.addState(state2);
		
		setMgr.addSet(set);
		//
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(editPanelContainer, BorderLayout.CENTER);
		setMgr.changeEditorSet(set);
		//
		frame.getContentPane().add(setSelectorPanel, BorderLayout.SOUTH);
		ArrayList<String> setNames = setMgr.getSetNames();
		for (String name : setNames) {
			System.out.println(name);
		}
		JComboBox comboSet = new JComboBox(setNames.toArray());
		setSelectorPanel.add(comboSet);
		JButton buttonSetTo = new JButton("Use this Setting");
		setSelectorPanel.add(buttonSetTo);
		buttonSetTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//setMgr.changeEditorSet(set2);
			}

		});
		//
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		//setMgr.openWithMetaWidget(set);
	}

	static JPanel setSelectorPanel = new JPanel();
	static JPanel editPanelContainer = new JPanel();

	public void setEditorPanel(JTabbedPane editorPane) {
		editPanelContainer.removeAll();
		editPanelContainer.add(editorPane);
	}

	public void changeEditorSet(SavedStateSet set) {
		setEditorPanel(set.getPropertyEditorPanel());

	}

	public void openWithMetaWidget(Object bean) {
		// Metawidget
//        SwingMetawidget metawidget = new SwingMetawidget();
//        metawidget.setInspector(new PropertyTypeInspector());
//       // metawidget.setBindingClass( BeansBinding.class );
//        metawidget.setToInspect(bean);
//        // JFrame
//
//        JFrame frame = new JFrame("Metawidget Tutorial");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        frame.getContentPane().add(metawidget);
//        frame.setSize(400, 210);
//        frame.pack();
//        frame.setVisible(true);
	}

}
