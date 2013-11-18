package tests.gui.enumCombo;

import java.util.Date;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import tests.gui.superliminal.StaticUtils;

/**
 * from http://codery.blogspot.com/2008/06/binding-enum-to-jcombobox-with-user.html
 * @author GBH <imagejdev.org>
 */
enum Timeframe {
    THIS_WEEK,
    THIS_MONTH,
    THIS_YEAR;

    @Override
    public String toString() {
        String[] splitNames = name().toLowerCase().split("_");
        StringBuffer fixedName = new StringBuffer();
        for (int i = 0; i < splitNames.length; i++) {
            String firstLetter = splitNames[i].substring(0, 1).toUpperCase(),
                    restOfWord = splitNames[i].substring(1),
                    spacer = i == splitNames.length ? "" : " ";
            fixedName.append(firstLetter).append(restOfWord).append(spacer);
        }
        return fixedName.toString();
    }

    public static void main(String[] args) {

        JComboBox timeframeComboBox = new JComboBox();
        ComboBoxModel cbModel = new DefaultComboBoxModel(Timeframe.values());
        timeframeComboBox.setModel(cbModel);
        StaticUtils.QuickFrame f = new StaticUtils.QuickFrame("EnumCombo");
        f.add(timeframeComboBox);
        f.pack();
        f.setVisible(true);
        
    }

    private void timeframeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        JComboBox cb = (JComboBox) evt.getSource();
        Timeframe timeFrame = (Timeframe) cb.getSelectedItem();
        Date now = new Date();

        switch (timeFrame) {
            case THIS_WEEK:
            //processing code...
        }
    }
}
