/*
 * SwingWorkerExampleFileCopy.java
 *
 * Created on March 18, 2007, 5:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.swingworker;


import tests.gui.*;
import java.awt.BorderLayout;
import javax.swing.*;
import java.io.*;
import java.beans.*;
import java.util.List;

public class SwingWorkerExampleFileCopy extends JFrame {
    public SwingWorkerExampleFileCopy(String title, File sourceDir, File destDir) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JProgressBar probar = new JProgressBar(0,100);
        probar.setStringPainted(true);
        JTextArea fileArea = new JTextArea();
        add(probar, BorderLayout.NORTH);
        add(new JScrollPane(fileArea), BorderLayout.CENTER);
        setSize(512, 384);
        setLocationByPlatform(true);
        setVisible(true);
        Copier c = new Copier(fileArea, sourceDir, destDir);
        c.addPropertyChangeListener(new ProgressBarChanger(probar));
        c.execute();
        try{
            boolean b = c.get();
            if (b) System.out.println("Errors occurred");
            else System.out.println("Everything was fine");
        }catch (Exception e){
            e.printStackTrace(System.console().writer());
            System.exit(1);
        }
    }
  /*
   *args[0] = source directory
   *args[1] = destination directory
   */
    public static void main(String[] args) {
        if (args.length < 2){
            System.out.println("Usage: java SwingWorkerExample sourceDirectory destinationDirectory");
        }
        File sourceDir = new File(args[0]);
        File destDir = new File(args[1]);
        new SwingWorkerExampleFileCopy("SwingWorkerExample", sourceDir, destDir);
    }
}
class Copier extends SwingWorker<Boolean,File>
{
    private JTextArea appendTo;
    private File srcDir;
    private File destDir;
    private float fileNum;
    private int copied = 0;
    public Copier(JTextArea appendTo, File srcDir, File destDir) {
        this.appendTo = appendTo;
        this.srcDir = srcDir;
        this.destDir = destDir;
        fileNum = count(srcDir);
    }
    @Override
    protected Boolean doInBackground() {
        boolean errorOccurred = copyFile(srcDir, destDir);
        return Boolean.valueOf(errorOccurred);
    }
    
    @Override
    protected void process(List<File> chunks) {
        for (File f : chunks){
            appendTo.append(f.getAbsolutePath());
            appendTo.append("\n");
        }
    }
    
    private int count(File f) {
        if (f.isDirectory()){
            int ret = 0;
            for (File sf : f.listFiles()){
                ret += count(sf);
            }
            return ret;
        }else{
            return 1;
        }
    }
    private boolean copyFile(File src, File dest) {
        if (src.isDirectory()){
            dest.mkdirs();
            boolean ret = false;
            for (File fil : src.listFiles()){
                boolean b = copyFile(fil, new File(dest, fil.getName()));
                if (b) ret = true;
            }
            return ret;
        }else{
            try{
                copy(src, dest);
                return false;
            }catch(IOException e){
                e.printStackTrace(System.console().writer());
                return true;
            }
        }
    }
    private void copy(File src, File dest) throws IOException {
        InputStream is = new FileInputStream(src);
        OutputStream os = new FileOutputStream(dest);
        int len;
        byte[] buf = new byte[1024];
        while ((len = is.read(buf)) >= 0){
            os.write(buf, 0, len);
        }
        is.close();
        os.close();
        copied++;
        setProgress((int)(copied / fileNum * 100 + 0.5f));
        publish(src);
    }
}
class ProgressBarChanger implements PropertyChangeListener {
    private JProgressBar jpb;
    public ProgressBarChanger(JProgressBar myBar) {
        jpb = myBar;
    }
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())){
            jpb.setValue((Integer)evt.getNewValue());
        }
    }
}