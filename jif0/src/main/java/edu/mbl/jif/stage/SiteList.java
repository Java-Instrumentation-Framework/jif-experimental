
package edu.mbl.jif.stage;

import edu.mbl.jif.gui.table.*;
import java.awt.*;

import java.io.*;

import java.util.*;

import javax.swing.*;

public class SiteList
        extends JList {

    static Color listForeground;
    static Color listBackground;
    static Color listSelectionForeground;
    static Color listSelectionBackground;
    

    static {
        UIDefaults uid = UIManager.getLookAndFeel().getDefaults();
        listForeground = uid.getColor("List.foreground");
        listBackground = uid.getColor("List.background");
        listSelectionForeground = uid.getColor("List.selectionForeground");
        listSelectionBackground = uid.getColor("List.selectionBackground");
    }

    ImageIcon fileIcon;
    ImageIcon textFileIcon;
    ImageIcon directoryIcon;
    ImageIcon imageFileIcon;
    ImageIcon pngFileIcon;
    ImageIcon gifFileIcon;
    ImageIcon jpegFileIcon;
    JComponent fileCellPrototype;
    JComponent textCellPrototype;
    JComponent imageCellPrototype;
    JComponent directoryCellPrototype;
    JLabel fileNameLabel;
    JLabel textNameLabel;
    JLabel directoryNameLabel;
    JLabel imageNameLabel;
    JLabel fileSizeLabel;
    JLabel textSizeLabel;
    JLabel textWordCountLabel;
    JLabel directoryCountLabel;
    JLabel imageSizeLabel;
    JLabel imageIconLabel;

    public SiteList(File dir) {
        super();
        buildPrototypeCells();
        setCellRenderer(new PolyRenderer());
        setModel(new DefaultListModel());
        if (!dir.isDirectory()) {
            dir = new File(dir.getParent());
        }
        buildModelFromDir(dir);
    }

    public static void main(String[] args) {
        File dir = new File(".");
        if (args.length > 0) {
            dir = new File(args[0]);
        }
        dir = new File("C:\\_dev\\TestImages\\images");
        JList list = new SiteList(dir);
        JScrollPane pain =
                new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JFrame frame = new JFrame("PolymorphicJList");
        frame.getContentPane().add(pain);
        frame.pack();
        frame.setVisible(true);
    }

    protected void buildModelFromDir(File dir) {
        File[] files = dir.listFiles();
        DefaultListModel mod = (DefaultListModel) getModel();
        for (int i = 0; i < files.length; i++) {
            if (isTextFile(files[i])) {
                mod.addElement(new TextFileItem(files[i]));
            } else if (isImageFile(files[i])) {
                mod.addElement(new ImageFileItem(files[i]));
            } else if (files[i].isDirectory()) {
                mod.addElement(new DirectoryItem(files[i]));
            } else {
                mod.addElement(new FileItem(files[i]));
            }
        }
    }

    protected boolean isImageFile(File f) {
        if (f.isDirectory()) {
            return false;
        }
        String name = f.getName();
        return name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".jpg") ||
                name.endsWith(".JPG") || name.endsWith(".jpeg") || name.endsWith(".JPEG") ||
                name.endsWith(".bmp") || name.endsWith(".BMP") || name.endsWith(".png") ||
                name.endsWith(".PNG");
    }

    protected boolean isTextFile(File f) {
        if (f.isDirectory()) {
            return false;
        }
        String name = f.getName();
        return name.endsWith(".txt") || name.endsWith(".html") || name.endsWith(".xml") ||
                name.endsWith(".xhtml") || name.endsWith(".java") || name.endsWith(".c") ||
                name.endsWith(".cpp") || name.endsWith(".c++") || name.endsWith(".m") || name.endsWith(".h");
    }

    protected void buildIcons() {
        String path = "/edu/mbl/jif/gui/table/images/";
        fileIcon = new ImageIcon(getClass().getResource(path + "generic.gif"));
        textFileIcon = new ImageIcon(getClass().getResource(path + "text.gif"));
        directoryIcon = new ImageIcon(getClass().getResource(path + "folder.gif"));
        imageFileIcon = new ImageIcon(getClass().getResource(path + "image.gif"));
        pngFileIcon = new ImageIcon(getClass().getResource(path + "png.gif"));
        gifFileIcon = new ImageIcon(getClass().getResource(path + "gif.gif"));
        jpegFileIcon = new ImageIcon(getClass().getResource(path + "jpg.gif"));
    }

    protected void buildPrototypeCells() {
        buildIcons();
        fileCellPrototype = new JPanel();
        fileCellPrototype.setLayout(new GridBagLayout());
        addWithGridBag(new JLabel(fileIcon), fileCellPrototype, 0, 0, 1, 2, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, 0, 0);
        fileNameLabel = new JLabel();
        Font defaultLabelFont = fileNameLabel.getFont();
        Font nameFont = defaultLabelFont.deriveFont(Font.BOLD, defaultLabelFont.getSize() + 2);
        fileNameLabel.setFont(nameFont);
        addWithGridBag(fileNameLabel, fileCellPrototype, 1, 0, 1, 1, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, 1, 0);
        fileSizeLabel = new JLabel();
        addWithGridBag(fileSizeLabel, fileCellPrototype, 1, 1, 1, 1, GridBagConstraints.SOUTH,
                GridBagConstraints.HORIZONTAL, 1, 0);
        opacify(fileCellPrototype);
        // text file
        textCellPrototype = new JPanel();
        textCellPrototype.setLayout(new GridBagLayout());
        addWithGridBag(new JLabel(textFileIcon), textCellPrototype, 0, 0, 1, 2,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, 0, 0);
        textNameLabel = new JLabel();
        textNameLabel.setFont(nameFont);
        addWithGridBag(textNameLabel, textCellPrototype, 1, 0, 2, 1, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, 1, 0);
        textSizeLabel = new JLabel();
        textWordCountLabel = new JLabel();
        addWithGridBag(textSizeLabel, textCellPrototype, 1, 1, 1, 1, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, 0, 0);
        addWithGridBag(textWordCountLabel, textCellPrototype, 2, 1, 1, 1, GridBagConstraints.SOUTH,
                GridBagConstraints.HORIZONTAL, 1, 0);

        opacify(textCellPrototype);
        // directory
        directoryCellPrototype = new JPanel();
        directoryCellPrototype.setLayout(new GridBagLayout());
        addWithGridBag(new JLabel(directoryIcon), directoryCellPrototype, 0, 0, 1, 2,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, 0, 0);
        directoryNameLabel = new JLabel();
        directoryNameLabel.setFont(nameFont);
        addWithGridBag(directoryNameLabel, directoryCellPrototype, 1, 0, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 1, 0);
        directoryCountLabel = new JLabel();
        addWithGridBag(directoryCountLabel, directoryCellPrototype, 1, 1, 1, 1,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, 1, 0);
        opacify(directoryCellPrototype);
        // image
        imageCellPrototype = new JPanel();
        imageCellPrototype.setLayout(new GridBagLayout());
        addWithGridBag(new JLabel(imageFileIcon), imageCellPrototype, 0, 0, 1, 2,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, 0, 0);
        imageNameLabel = new JLabel();
        imageNameLabel.setFont(nameFont);
        addWithGridBag(imageNameLabel, imageCellPrototype, 1, 0, 1, 1, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, 1, 0);
        imageSizeLabel = new JLabel();
        addWithGridBag(imageSizeLabel, imageCellPrototype, 1, 1, 1, 1, GridBagConstraints.SOUTH,
                GridBagConstraints.HORIZONTAL, 1, 0);
        imageIconLabel = new JLabel();
        addWithGridBag(imageIconLabel, imageCellPrototype, 2, 0, 1, 2, GridBagConstraints.EAST,
                GridBagConstraints.VERTICAL, 0, 0);
        opacify(imageCellPrototype);
    }

    private void addWithGridBag(Component comp, Container cont, int x, int y, int width, int height,
                                int anchor, int fill, int weightx, int weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        cont.add(comp, gbc);
    }

    private void opacify(Container prototype) {
        Component[] comps = prototype.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JComponent) {
                ((JComponent) comps[i]).setOpaque(true);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc=">>>--- ITEMS ---<<<" >
    class FileItem
            extends Object {

        File file;

        public FileItem(File f) {
            file = f;
        }

    }

    class ImageFileItem
            extends FileItem {

        ImageIcon icon;

        public ImageFileItem(File f) {
            super(f);
            initIcon();
        }

        void initIcon() {
            icon = new ImageIcon(file.getPath());
            // scale to size pix in largest dimension
            float size = 64;
            Image img = icon.getImage();
            float factor = 1.0f;
            if (img.getWidth(null) > img.getHeight(null)) {
                factor = Math.min(size / img.getWidth(null), 1.0f);
            } else {
                factor = Math.min(size / img.getHeight(null), 1.0f);
            }
            Image scaledImage =
                    img.getScaledInstance((int) (img.getWidth(null) * factor),
                    (int) (img.getHeight(null) * factor), Image.SCALE_FAST);
            icon.setImage(scaledImage);
        }

    }

    class DirectoryItem
            extends FileItem {

        int childCount;

        public DirectoryItem(File f) {
            super(f);
            initChildCount();
        }

        public int getChildCount() {
            return childCount;
        }

        void initChildCount() {
            if (!file.isDirectory()) {
                childCount = -1;
            } else {
                childCount = file.listFiles().length;
            }
            System.out.println(file.getPath() + ": " + childCount + " items");
        }

    }

    class TextFileItem
            extends FileItem {

        int wordCount = -1;

        public TextFileItem(File f) {
            super(f);
            initWordCount();
        }

        public int getWordCount() {
            return wordCount;
        }

        protected void initWordCount() {
            try {
                StreamTokenizer izer = new StreamTokenizer(new BufferedReader(new FileReader(file)));
                while (izer.nextToken() != StreamTokenizer.TT_EOF) {
                    wordCount++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                wordCount = -1;
            }
            System.out.println(file.getPath() + ": " + wordCount + " words");
        }

    }

// </editor-fold>

    class PolyRenderer
            extends Object
            implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            if (value instanceof DirectoryItem) {
                DirectoryItem item = (DirectoryItem) value;
                directoryNameLabel.setText(item.file.getName());
                directoryCountLabel.setText(item.getChildCount() + " items");
                setColorsForSelectionState(directoryCellPrototype, isSelected);
                return directoryCellPrototype;
            } else if (value instanceof TextFileItem) {
                TextFileItem item = (TextFileItem) value;
                // populate values
                textNameLabel.setText(item.file.getName());
                textSizeLabel.setText(item.file.length() + " bytes  ");
                textWordCountLabel.setText(item.getWordCount() + " words");
                setColorsForSelectionState(textCellPrototype, isSelected);
                return textCellPrototype;
            } else if (value instanceof ImageFileItem) {
                ImageFileItem item = (ImageFileItem) value;
                // pouplate values
                imageNameLabel.setText(item.file.getName());
                imageSizeLabel.setText(item.file.length() + " bytes");
                imageIconLabel.setIcon(item.icon);
                setColorsForSelectionState(imageCellPrototype, isSelected);
                return imageCellPrototype;
            } else {
                FileItem item = (FileItem) value;
                // pouplate values
                fileNameLabel.setText(item.file.getName());
                fileSizeLabel.setText(item.file.length() + " bytes");
                setColorsForSelectionState(fileCellPrototype, isSelected);
                return fileCellPrototype;
            }
        }

        private void setColorsForSelectionState(Container prototype, boolean isSelected) {
            Component[] comps = prototype.getComponents();
            for (int i = 0; i < comps.length; i++) {
                if (isSelected) {
                    comps[i].setForeground(listSelectionForeground);
                    comps[i].setBackground(listSelectionBackground);
                } else {
                    comps[i].setForeground(listForeground);
                    comps[i].setBackground(listBackground);
                }
            }
        }

    }
}
/*   Swing Hacks
 *   Tips and Tools for Killer GUIs
 * By Joshua Marinacci, Chris Adamson
 *   First Edition June 2005
 *   Series: Hacks
 *   ISBN: 0-596-00907-0
 *   http://www.oreilly.com/catalog/swinghks
 */
