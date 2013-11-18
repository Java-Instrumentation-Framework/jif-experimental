/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests.command.typesafe;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
* The ChooseFileCommandHandler.  Performs the real work.
*/
public class ChooseFileCommandHandler implements CommandHandler<ChooseFileCommandParameters, File> {
    public File execute(ChooseFileCommandParameters params) throws Exception {
       // UI code here to set up the chooser, set look and feel, defaults, etc..
       // of course this won't compile because I made up a fake implementation.
       // the point is to show how the work is encapsulated and nicely parameterized.
//       JFileChooser chooser = new JFileChooser(params.getPath(), params.getFilter());
//       chooser.open();
//       File chosenFile = chooser.getSelection();
//       if (chosenFile == null) {
//           System.err.println("File not chosen");
//          //new AlertDialog("File not chosen!");
//       }
//       return chosenFile;
           JFileChooser chooser = new JFileChooser(params.getPath());
    FileNameExtensionFilter filter = new FileNameExtensionFilter(params.getFilter());
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(null);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
       System.out.println("You chose to open this file: " +
            chooser.getSelectedFile().getName());
    }
    File chosenFile = chooser.getSelectedFile();
       return chosenFile;
    }
}