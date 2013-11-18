/* This is part of Java Preferences Tool. 
 * See file Main.java for copyright and license information.
 */

package org.bbg.prefs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

@SuppressWarnings("serial")
public class LnFMenu extends JMenu implements ActionListener {

    private Frame top;
    private JRadioButtonMenuItem currItem, prevItem;
    private ButtonGroup bg = new ButtonGroup();
    
    private static Chooser lafChooser = null;
    private static Logger logger = Logger.getLogger("global");

    /**
     * Creates Look&Feel menu with all possible choices.
     * @param top The top level application window. The UI will be updated
     *        starting with this component.
     * @param text The menu item text. If {@code null}, the default text
     *        "Look and Feel" is used.
     * <p>
     * To be notified on Look&Feel change, the calling program might
     * listen to {@code UIManager} events, like this:
     * <pre>
     * UIManager.addPropertyChangeListener(new PropertyChangeListener() {
     *   public void propertyChange(PropertyChangeEvent ev) {
     *     if (ev.getPropertyName().equals("lookAndFeel"))
     *       System.out.println("lookAndFeel is now " + 
     *              ev.getNewValue().getClass().getName());
     *   }
     * });
     * </pre>
     */
    public LnFMenu (Frame top, String text) {
        super(text == null ? "Look and Feel" : text);
        this.top = top;
        LookAndFeel curr = UIManager.getLookAndFeel();
        String currName = curr.getClass().getName();
        LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        for (LookAndFeelInfo lnfi : info) {
            String name = lnfi.getClassName();
            boolean selected = currName.equals(name); 
            JRadioButtonMenuItem item = 
            	new JRadioButtonMenuItem(lnfi.getName(), selected);
            if (selected)
                currItem = prevItem = item;
            item.setActionCommand(lnfi.getClassName());
            item.addActionListener(this);
            add(item);
            bg.add(item);
        }
        addSeparator();
        JMenuItem more = new JMenuItem("Add another L&F...");
        more.addActionListener(this);
        more.setActionCommand("+");
        add(more);
    }

    public void actionPerformed(ActionEvent ev) {
        try {
            if (ev.getActionCommand().equals("+")) {
            	choose();
            	return;
            }
            currItem = (JRadioButtonMenuItem) ev.getSource();
            String currClassName = ev.getActionCommand();
            if (currClassName.equals(prevItem.getActionCommand()))
                return;
            UIManager.setLookAndFeel(currClassName);
            prevItem = currItem;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Cannot change look and feel:\n" + ex.getMessage());
            currItem = prevItem;
            currItem.setSelected(true);
        }
    }
    
    // This method is to be called when the top Frame is visible.
    private void choose () {
    	LookAndFeelInfo[] lnfi = UIManager.getInstalledLookAndFeels();
    	if (lafChooser == null)
    		lafChooser = new Chooser(top);
    	String path = System.getProperty("java.class.path");
    	List<String> jars = new ArrayList<String>();
    	for (String str : path.split(System.getProperty("path.separator")))
    		if (str.endsWith(".jar"))
    			jars.add(str);
		if (jars.size() > 0) {
    		lafChooser.show(jars);
    		if (lafChooser.selected != null) {
	    		for (String cls : lafChooser.selected) {
	    			if (isInstalled(lnfi, cls))
	    				continue;
	    			try {
			        	Class<?> c = Class.forName(cls);
			        	LookAndFeel lnf = (LookAndFeel) c.newInstance();
			        	if (lnf == null) {
			        		JOptionPane.showMessageDialog(top, "Not a LookAndFeel: "+cls); 
			        		continue;
			        	}
			        	String lnfName = lnf.getName();
			        	UIManager.installLookAndFeel(lnfName, cls);
			        	// if we are here, it was really a LookAndFeel
			            JRadioButtonMenuItem item = 
			            	new JRadioButtonMenuItem(lnfName, false);
			            item.setActionCommand(cls);
			            item.addActionListener(this);
			            insert(item, getItemCount()-2);
			            //add(item);
			            bg.add(item);
	    			} catch (Exception ex) {
	    				JOptionPane.showMessageDialog(top, String.format("%s:\n%s", 
	    						ex.getClass().getName(), ex.getMessage())); 
	    				continue;
	    			}
	    		}
    		}
		} else
    		JOptionPane.showMessageDialog(top, "No jars in the class path.");
		lafChooser.dispose();
		lafChooser = null;
    }
    
    private static String lafInstall = "laf.install";
    private static String lafDefault = "laf.default";
    
    /**
     * Stores information about the installed LaFs and the current LaF
     * into given preference node.
     * @param prefs The preference node to use. Two records are created:<ul>
     * <li><code>laf.install</code>: comma-separated list of class names
     * implementing javax.swing.LookAndFeel.</li>
     * <li><code>laf.default</code>: the class name of current LookAndFeel.</li>
     * </ul>
     * @throws BackingStoreException Inherited from underlying I/O.
     */
    public static void savePrefs (Preferences prefs) throws BackingStoreException {
    	prefs.clear();
    	StringBuilder buf = new StringBuilder();
        for (LookAndFeelInfo lnfi : UIManager.getInstalledLookAndFeels()) {
            String cls = lnfi.getClassName();
    		if (buf.length() != 0)
    			buf.append(",");
    		buf.append(cls);
    	}
    	prefs.put(lafInstall, buf.toString());
    	prefs.put(lafDefault, UIManager.getLookAndFeel().getClass().getName());
    }
    
    /**
     * Loads information stored by savePrefs into given preference node
     * and tries to restore the saved status.
     * @param prefs The preference node to use.
     */
    public static void loadPrefs (Preferences prefs) {
    	LookAndFeelInfo[] lnfi = UIManager.getInstalledLookAndFeels();
    	String lafs = prefs.get(lafInstall, null);
    	if (lafs != null && lafs.length() > 0) {
    		for (String cls : lafs.split(",")) {
    			cls = cls.trim();
    			if (isInstalled(lnfi, cls))
    				continue;
    			try {
	    			Class<?> c = Class.forName(cls);
		        	LookAndFeel laf = (LookAndFeel) c.newInstance();
		        	if (laf == null)
		        		throw new Exception(cls + ": not a LookAndFeel");
		        	UIManager.installLookAndFeel(laf.getName(), cls);
    			} catch (Exception ex) {
    				logger.info(String.format("Cannot install LaF %s:\n%s", 
    						cls, ex.getMessage() ));
    			}
    		}
    	}
    	String last = prefs.get(lafDefault, UIManager.getSystemLookAndFeelClassName());
    	try {
    		UIManager.setLookAndFeel(last);
    	} catch (Exception ex) {
    		logger.info(String.format("Cannot set LaF %s:\n%s", 
    				last, ex.getMessage()));
    	}
    }
    
    private static boolean isInstalled (LookAndFeelInfo[] lnfi, String cls) {
    	for (LookAndFeelInfo info : lnfi) 
    		if (info.getClassName().equals(cls))
    			return true;
    	return false;
    }
    
    private static class Chooser extends JDialog implements ActionListener {

    	private JPanel topPanel = new JPanel(new BorderLayout());
    	private JLabel jarName = new JLabel("<html><b>Scanned all jars in the class path</b>", 
    			SwingConstants.CENTER);
    	private JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    	private JPanel butPanel = new JPanel(new GridLayout(1,2,6,0));
    	private JButton okBtn = new JButton("Ok");
    	private JButton cancelBtn = new JButton("Cancel");
    	private JPanel classPanel = new JPanel(new BorderLayout());
    	private JPanel centerPanel = new JPanel(new BorderLayout());
    	private JScrollPane scrollPane = new JScrollPane();
    	private JList classList = new JList();
    	private JLabel manLabel = new JLabel();
    	private DefaultListModel model = new DefaultListModel();
    	
    	String[] selected;
    	private Frame	 owner;

    	/**
    	 * Initializes the modal dialog, but does not make it visible. 
    	 * @param owner The owner frame.
    	 */
    	Chooser (Frame owner) {
    		super(owner, true);
    		this.owner = owner;
    		initialize();
    		setLocationRelativeTo(owner);
    		classList.setModel(model);
    		okBtn.addActionListener(this);
    		cancelBtn.addActionListener(this);
    	}

    	/**
    	 * Populates the list by class names found in given jars and 
    	 * extending javax.swing.LookAndFeel. Activates this modal dialog
    	 * by making it visible.
    	 * @param jars List of file names to inspect.
    	 */
    	void show (List<String> jars) {
    		model.clear();
    		for (String name : jars) {
    			try {
    				JarFile jar = new JarFile(name);
    				show(jar);
    				jar.close();
    			} catch (IOException ex) {
    				JOptionPane.showMessageDialog(owner, 
    						String.format("Cannot open %s:\n%s", name, ex.getMessage()),
    						"Error", JOptionPane.ERROR_MESSAGE);
    				continue;
    			}
    		}
    		setVisible(true);
    	}
    	
    	private void show (JarFile jar) {
    		Enumeration<JarEntry> entries = jar.entries();
    		while (entries.hasMoreElements()) {
    			JarEntry ent = entries.nextElement();
    			String name = ent.getName();
    			if (name.endsWith(".class")) {
    				String qname = name.replace('/', '.').substring(0, name.length()-6);
    				try {
    					Class<?> c = Class.forName(qname, false, getClass().getClassLoader());
    					if (javax.swing.LookAndFeel.class.isAssignableFrom(c))
    						model.addElement(qname);
    				} catch (ClassNotFoundException ex) {
    					System.err.println("rejected: " + qname);
    					continue; // strange...
    				}
    			}
    		}
    	}
    	
    	public void actionPerformed (ActionEvent e) {
    	    if (e.getSource() == cancelBtn)
    	    	selected = null;
    	    else {
    	    	Object[] sel = classList.getSelectedValues();
    	    	if (sel.length == 0)
    	    		selected = null;
    	    	else {
    		    	selected = new String[sel.length];
    		    	for (int i = 0; i < sel.length; i++)
    		    		selected[i] = (String)sel[i];
    	    	}
    	    }
        	setVisible(false);
    	}
    	
    	private void initialize () {
    		classList.setVisibleRowCount(16);
    		scrollPane.setViewportView(classList);
    		centerPanel.setBorder(BorderFactory.createCompoundBorder(
    				BorderFactory.createTitledBorder("Available classes:"),
    				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
    		centerPanel.add(scrollPane, BorderLayout.CENTER);
    		//
    		manLabel.setText("Select one or more classes implementing javax.swing.LookAndFeel:");
    		//manLabel.setHorizontalTextPosition(SwingConstants.LEFT);
    		//manLabel.setFont(new Font("dialog", Font.PLAIN, 12));
    		classPanel.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
    		classPanel.add(manLabel, BorderLayout.CENTER);
    		//
    		butPanel.add(okBtn, null);
    		butPanel.add(cancelBtn, null);
    		bottomPanel.add(butPanel, null);
    		//
    		//jarName.setFont(new Font("dialog", Font.BOLD, 12));
    		topPanel.add(jarName, BorderLayout.NORTH);
    		topPanel.add(classPanel, BorderLayout.CENTER);
    		//
    		JPanel contentPane = new JPanel(new BorderLayout());
    		contentPane.setBorder(BorderFactory.createEmptyBorder(8, 16, 0, 16));
    		contentPane.add(topPanel, BorderLayout.NORTH);
    		contentPane.add(bottomPanel, BorderLayout.SOUTH);
    		contentPane.add(centerPanel, BorderLayout.CENTER);

    		this.setContentPane(contentPane);
    		pack();
    	}

    }

}


