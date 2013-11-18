package tests.gui.wizard;

import java.util.List;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import edu.mbl.jif.gui.wizard.WizardPanel;

public class StartWizardPanel extends WizardPanel {

    private String welcome = "~~Welcome\n"
        + "Press next to continue.";

    private final WizardPanel license = new LicenseWizardPanel();

    public StartWizardPanel() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("~~Start"));
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        pane.setText(welcome);
        add(new JScrollPane(pane), BorderLayout.CENTER);
    }

    /** Called when the panel is set. */
    public void display() {
    }

    public boolean hasNext() {
        return true;
    }

    public boolean validateNext(List list) {
        boolean valid = true;
        return valid;
    }

    public WizardPanel next() {
        return license;
    }

    public boolean canFinish() {
        return false;
    }

    public boolean validateFinish(List list) {
        return false;
    }

    public void finish() {
    }
}
