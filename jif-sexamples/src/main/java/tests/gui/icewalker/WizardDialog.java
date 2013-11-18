package tests.gui.icewalker;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class WizardDialog extends JDialog {
	
	private PanelModel panels = new PanelModel();
	private DialogButtonSet set;
	
	// GUI Components
	private String dialogTitle, wizardComment;
	private JLabel titleLabel;
	private int currentIndex;
	public Component currentPanel;
	private JButton back;
	
	public WizardDialog() {
		this( new JFrame() );
	}
	
	public WizardDialog(Frame parent, String title, boolean modal) {
		super(parent, title, modal);
	}
	
	public WizardDialog(Frame parent, String title) {
		this(parent, title, true);
	}
	
	public WizardDialog(Frame parent) {
		this(parent, "Wizard", true);
	}
	
	public WizardDialog(String title) {
		this( new JFrame(), title, true);
	}
	
	public WizardDialog(String title, boolean modal) {
		this( new JFrame(), title, modal );
	}
	
	public WizardDialog(Dialog parent, String title, boolean modal) {
		super(parent, title, modal);
	}
	
	public WizardDialog(Dialog parent, String title) {
		this(parent, title, true);
	}
	
	public void addPanel(Component panel, String id) {
		panels.addPanel(panel, id);
	}
	
	public void addPanel(Component panel) {
		panels.addPanel(panel, "panel" + panels.getSize());
	}
	
	public void removePanel(Component panel) {
		panels.removePanel(panel);
	}
	
	public void removePanel(String id) {
		panels.removePanel(id);
	}
	
	public void removePanel(int index) {
		panels.removePanel(index);
	}
	
	public void insertPanelAt(Component panel, int index) {
		panels.insertPanelAt(panel, index);
	}
	
	public void insertPanelAt(Component panel, String id, int index) {
		panels.insertPanelAt(panel, id, index);
	}
	
	public PanelModel getModel() {
		return panels;
	}
	
	public int getPanelCount() {
		return getModel().getSize();
	}
	
	public void setWizardTitle(String title) {
		this.dialogTitle = title;
	}
	
	public String getWizardTitle() {
		return dialogTitle;
	}
	
	public void setWizardComment(String comment) {
		this.wizardComment = comment;
	}
	
	public String getWizardComment() {
		return wizardComment;
	}
	
	public void createDialogPane(Component initialPanel) {				
		JPanel iconPanel = new JPanel( new FlowLayout(FlowLayout.CENTER) ) {				
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				g.setColor( Color.black );
				g.drawLine(getWidth() - 1, 1, getWidth() - 1, getHeight() - 1 );
			}
		};
		
		//iconPanel.add(icon_label);
		iconPanel.setBackground( new Color(200,200,250) );	
		iconPanel.setPreferredSize( new Dimension(80, 275) );
		
		ButtonListener butListener = new ButtonListener();
		
		back = new JButton("Back");
		back.addActionListener(butListener);
		
		set = new DialogButtonSet(this, butListener);
		set.getDoneButton().setText("Next");
		set.add(back,0);
		
		currentPanel = initialPanel;
		
		JPanel all = new JPanel( new BorderLayout() );
			all.add( new PanelTitler( getWizardTitle(), getWizardComment() ), BorderLayout.NORTH );
			all.add(iconPanel, BorderLayout.WEST);
			all.add(currentPanel, BorderLayout.CENTER);
			all.add(set, BorderLayout.SOUTH);
			
		setContentPane(all);
		pack();
		setLocationRelativeTo(null);
	}
	
	
	public void setVisible(boolean b) {
		if(b) {
			createDialogPane( getModel().getPanel(0) );
		}
		
		super.setVisible(b);
	}
	
	public void showPreviousPanel() {
		currentIndex = currentIndex >= 0 ? --currentIndex : 0;
		showPanel(currentIndex);
	}
	
	public void showNextPanel() {
		currentIndex = currentIndex <= panels.getSize() - 1 ? ++currentIndex : panels.getSize() - 1;
		showPanel(currentIndex);
	}
	
	public void showPanel(int index) {
		if(index < 0) {
			index = 0;
		} else if(index >= panels.getSize() ) {
			index = panels.getSize() - 1;
		}
		
		if(panels.getLabel(index).equalsIgnoreCase("settings")) {
			//setUserSettings("<html>Installation Directory:<br><br>" + installationLocation + "<br><br><html>");
		}
		
		set.getDoneButton().setEnabled(true);
		back.setEnabled(true);
		
		if(index < panels.getPanelIndex("import") - 1 ) {
			set.getDoneButton().setText("Next");
		} else if( index == (panels.getPanelIndex("import") - 1) ) {
			set.getDoneButton().setText("Import");
		} else if( index == panels.getPanelIndex("complete") ) {
			set.getDoneButton().setText("Finish");
		} else if( index == panels.getPanelIndex("progress") ) {
			set.getDoneButton().setEnabled(false);
			back.setEnabled(false);
		}
		
		getContentPane().remove(currentPanel);
		currentPanel = (JPanel)panels.getPanel(index);
		getContentPane().add(currentPanel);
		
		Utilities.updateView(currentPanel);
		
		if( index == panels.getPanelIndex("matchup") ) {
		/*	SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					populateMatchUpTable();
				}
			});
		*/
		}
		
		if( index == panels.getPanelIndex("table") ) {
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
//					showDatabaseDialog();
				}
			});
		}
		
		if( index == panels.getPanelIndex("import") ) {
			SwingUtilities.invokeLater( new Runnable() {
				public void run() {
//					createProfilesFromModel();
				}
			});
		}
		
		if( index == panels.getPanelIndex("complete") ) {
			set.getDoneButton().setEnabled(true);				
			set.getCancelButton().setEnabled(false);
			back.setEnabled(false);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
	}
	
	public void closeWizard() {
		setVisible(false);
	}
	
	class ButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			
			if(source == set.getCancelButton() ) {
				//setVisible(false);				
				closeWizard();
			}
			
			if(source == set.getDoneButton() ) {				
				if(set.getDoneButton().getText().equalsIgnoreCase("Finish")) {
					setVisible(false);
				}
				
				showNextPanel();
				
				if( currentIndex == panels.getPanelIndex("matchup") ) {
					SwingUtilities.invokeLater( new Runnable() {
						public void run() {
//							populateMatchUpTable();
						}
					});
				}
			}
			
			if(source == back) {
				showPreviousPanel();
			}
		}
	}
	
	
	public static void main(String[] args) {
		JPanel first = new JPanel();
			first.add( new JLabel("some amazing text") );
			
		JPanel second = new JPanel();
			second.add( new JLabel("more amazing text") );
			
		WizardDialog wizard = new WizardDialog();
			wizard.addPanel(first);
			wizard.addPanel(second);
			wizard.setVisible(true);
	}
}