package edu.mbl.jif.data;

import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camera.camacq.*;
import com.jgoodies.binding.adapter.PreferencesAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.ObservableList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.ListModel;

public class DataModel extends Model {

    static final String DEFAULT_PREFS_KEY = "data";
    Preferences prefs;
    //Preferences prefs = CamAcqJ.getInstance().getPreferences();
    // --- imageDirectory ---
    public static final String PROPERTYNAME_IMAGEDIRECTORY = "imageDirectory";
    private String imageDirectory;
    static final String IMAGEDIRECTORY_DEFAULT = ".";
    PreferencesAdapter imageDirectoryPrefAdapter;
    // --- filePrefix ---
    public static final String PROPERTYNAME_FILEPREFIX = "filePrefix";
    private String filePrefix;
    static final String FILEPREFIX_DEFAULT = "image";
    PreferencesAdapter filePrefixPrefAdapter;

    // --- fileCounter ---
    public static final String PROPERTYNAME_FILECOUNTER = "fileCounter";
    private int fileCounter;
    static final int FILECOUNTER_DEFAULT = 0;
    PreferencesAdapter fileCounterPrefAdapter;

    // --- useFilenamePrefix ---
    public static final String PROPERTYNAME_USEFILENAMEPREFIX = "useFilenamePrefix";
    private boolean useFilenamePrefix;
    static final boolean USEFILENAMEPREFIX_DEFAULT = true;
    PreferencesAdapter useFilenamePrefixPrefAdapter;
    // --- autoSave ---
    public static final String PROPERTYNAME_AUTOSAVE = "autoSave";
    private boolean autoSave;
    static final boolean AUTOSAVE_DEFAULT = true;
    PreferencesAdapter autoSavePrefAdapter;

    // --- imageFileFormat ---
    public static final String PROPERTYNAME_IMAGEFILEFORMAT = "imageFileFormat";
    private String imageFileFormat;
    static final String IMAGEFILEFORMAT_DEFAULT = "tiff";
    PreferencesAdapter imageFileFormatPrefAdapter;
    public static final String IMAGEFILEFORMAT_ENUM_1 = new String("tiff");
    public static final String IMAGEFILEFORMAT_ENUM_2 = new String("jpeg");
    public static final String IMAGEFILEFORMAT_ENUM_3 = new String("png");
    public static final String IMAGEFILEFORMAT_ENUM_4 = new String("gif");
    public static final String IMAGEFILEFORMAT_ENUM_5 = new String("bmp");
    public static final List IMAGEFILEFORMAT_OPTIONS = Arrays.asList(new String[]{
                IMAGEFILEFORMAT_ENUM_1,
                IMAGEFILEFORMAT_ENUM_2,
                IMAGEFILEFORMAT_ENUM_3,
                IMAGEFILEFORMAT_ENUM_4,
                IMAGEFILEFORMAT_ENUM_5
            });
    private ObservableList imageFileFormatListModel;
    private Object imageFileFormatListSelection;

    // --- ijPutOnDesk ---
    public static final String PROPERTYNAME_IJPUTONDESK = "ijPutOnDesk";
    private boolean ijPutOnDesk;
    static final boolean IJPUTONDESK_DEFAULT = true;
    PreferencesAdapter ijPutOnDeskPrefAdapter;

    // --- seqInStack ---
    public static final String PROPERTYNAME_SEQINSTACK = "seqInStack";
    private boolean seqInStack;
    static final boolean SEQINSTACK_DEFAULT = true;
    PreferencesAdapter seqInStackPrefAdapter;
    //
    InstrumentController instrumentCtrl;

    /**
     * Creates a new instance of Data
     */
    public DataModel(InstrumentController instCtrl) throws Exception {
        this(instCtrl, DEFAULT_PREFS_KEY);
    }

    public DataModel(InstrumentController instrumentCtrl, String key) throws Exception {
        initializePreferenceAdapters(key);
        this.instrumentCtrl = instrumentCtrl;

        // Initialize enumerated list for imageFileFormat
        imageFileFormatListModel = new ArrayListModel();
        imageFileFormatListModel.addAll(IMAGEFILEFORMAT_OPTIONS);
        imageFileFormatListSelection = imageFileFormatListModel.get(0);
    }

    public void initializePreferenceAdapters(String key) {
        prefs = CamAcqJ.getInstance().getPreferences().node(key);
        imageDirectoryPrefAdapter =
                new PreferencesAdapter(prefs, DataModel.PROPERTYNAME_IMAGEDIRECTORY, IMAGEDIRECTORY_DEFAULT);
        filePrefixPrefAdapter =
                new PreferencesAdapter(prefs, DataModel.PROPERTYNAME_FILEPREFIX, FILEPREFIX_DEFAULT);
        fileCounterPrefAdapter =
                new PreferencesAdapter(prefs, DataModel.PROPERTYNAME_FILECOUNTER, FILECOUNTER_DEFAULT);
        useFilenamePrefixPrefAdapter =
                new PreferencesAdapter(prefs, DataModel.PROPERTYNAME_USEFILENAMEPREFIX, USEFILENAMEPREFIX_DEFAULT);
        autoSavePrefAdapter =
                new PreferencesAdapter(prefs, DataModel.PROPERTYNAME_AUTOSAVE, AUTOSAVE_DEFAULT);
        imageFileFormatPrefAdapter =
                new PreferencesAdapter(prefs, DataModel.PROPERTYNAME_IMAGEFILEFORMAT, IMAGEFILEFORMAT_DEFAULT);
        ijPutOnDeskPrefAdapter =
                new PreferencesAdapter(prefs, DataModel.PROPERTYNAME_IJPUTONDESK, IJPUTONDESK_DEFAULT);
        seqInStackPrefAdapter =
                new PreferencesAdapter(prefs, DataModel.PROPERTYNAME_SEQINSTACK, SEQINSTACK_DEFAULT);
    }

    /**
     * Gets the current value of imageDirectory from preferences
     * @return Current value of imageDirectory
     */
    public String getImageDirectory() {
        imageDirectory = imageDirectoryPrefAdapter.getString();
        return imageDirectory;
    }

    /**
     * Sets the value of imageDirectory to preferences
     * @param imageDirectory New value for imageDirectory
     */
    public void setImageDirectory(String imageDirectory) {
        String oldImageDirectory = getImageDirectory();
        this.imageDirectory = imageDirectory;
        imageDirectoryPrefAdapter.setString(imageDirectory);
        firePropertyChange(PROPERTYNAME_IMAGEDIRECTORY, oldImageDirectory, imageDirectory);
    }

    /**
     * Gets the current value of filePrefix from preferences
     * @return Current value of filePrefix
     */
    public String getFilePrefix() {
        filePrefix = filePrefixPrefAdapter.getString();
        return filePrefix;
    }

    /**
     * Sets the value of filePrefix to preferences
     * @param filePrefix New value for filePrefix
     */
    public void setFilePrefix(String filePrefix) {
        String oldFilePrefix = getFilePrefix();
        this.filePrefix = filePrefix;
        filePrefixPrefAdapter.setString(filePrefix);
        firePropertyChange(PROPERTYNAME_FILEPREFIX, oldFilePrefix, filePrefix);
    }

    /**
     * Gets the current value of fileCounter from preferences
     * @return Current value of fileCounter
     */
    public int getFileCounter() {
        fileCounter = fileCounterPrefAdapter.getInt();
        return fileCounter;
    }

    /**
     * Sets the value of fileCounter to preferences
     * @param fileCounter New value for fileCounter
     */
    public void setFileCounter(int fileCounter) {
        int oldFileCounter = getFileCounter();
        this.fileCounter = fileCounter;
        fileCounterPrefAdapter.setInt(fileCounter);
        firePropertyChange(PROPERTYNAME_FILECOUNTER, oldFileCounter, fileCounter);
    }

    /**
     * Gets the current value of useFilenamePrefix from preferences
     * @return Current value of useFilenamePrefix
     */
    public boolean isUseFilenamePrefix() {
        useFilenamePrefix = useFilenamePrefixPrefAdapter.getBoolean();
        return useFilenamePrefix;
    }

    /**
     * Sets the value of useFilenamePrefix to preferences
     * @param useFilenamePrefix New value for useFilenamePrefix
     */
    public void setUseFilenamePrefix(boolean useFilenamePrefix) {
        boolean oldUseFilenamePrefix = isUseFilenamePrefix();
        this.useFilenamePrefix = useFilenamePrefix;
        useFilenamePrefixPrefAdapter.setBoolean(useFilenamePrefix);
        firePropertyChange(PROPERTYNAME_USEFILENAMEPREFIX, oldUseFilenamePrefix, useFilenamePrefix);
    }

    /**
     * Gets the current value of autoSave from preferences
     * @return Current value of autoSave
     */
    public boolean isAutoSave() {
        autoSave = autoSavePrefAdapter.getBoolean();
        return autoSave;
    }

    /**
     * Sets the value of autoSave to preferences
     * @param autoSave New value for autoSave
     */
    public void setAutoSave(boolean autoSave) {
        boolean oldAutoSave = isAutoSave();
        this.autoSave = autoSave;
        autoSavePrefAdapter.setBoolean(autoSave);
        firePropertyChange(PROPERTYNAME_AUTOSAVE, oldAutoSave, autoSave);
    }

    /**
     * Gets the current value of imageFileFormat from preferences
     * @return Current value of imageFileFormat
     */
    public String getImageFileFormat() {
        imageFileFormat = imageFileFormatPrefAdapter.getString();
        return imageFileFormat;
    }

    /**
     * Sets the value of imageFileFormat to preferences
     * @param imageFileFormat New value for imageFileFormat
     */
    public void setImageFileFormat(String imageFileFormat) {
        String oldImageFileFormat = getImageFileFormat();
        this.imageFileFormat = imageFileFormat;
        imageFileFormatPrefAdapter.setString(imageFileFormat);
        firePropertyChange(PROPERTYNAME_IMAGEFILEFORMAT, oldImageFileFormat, imageFileFormat);
    }

    /**
     * ListModel accessor for imageFileFormat
     * @return ListModel of imageFileFormat
     */
    public ListModel getImageFileFormatListModel() {
        return imageFileFormatListModel;
    }

    /**
     * ListSelection accessor for imageFileFormat
     * @return ListSelection of imageFileFormat
     */
    public Object getImageFileFormatListSelection() {
        return imageFileFormatListSelection;
    }

    /**
     * Gets the current value of ijPutOnDesk from preferences
     * @return Current value of ijPutOnDesk
     */
    public boolean isIjPutOnDesk() {
        ijPutOnDesk = ijPutOnDeskPrefAdapter.getBoolean();
        return ijPutOnDesk;
    }

    /**
     * Sets the value of ijPutOnDesk to preferences
     * @param ijPutOnDesk New value for ijPutOnDesk
     */
    public void setIjPutOnDesk(boolean ijPutOnDesk) {
        boolean oldIjPutOnDesk = isIjPutOnDesk();
        this.ijPutOnDesk = ijPutOnDesk;
        ijPutOnDeskPrefAdapter.setBoolean(ijPutOnDesk);
        firePropertyChange(PROPERTYNAME_IJPUTONDESK, oldIjPutOnDesk, ijPutOnDesk);
    }

    /**
     * Gets the current value of seqInStack from preferences
     * @return Current value of seqInStack
     */
    public boolean isSeqInStack() {
        seqInStack = seqInStackPrefAdapter.getBoolean();
        return seqInStack;
    }

    /**
     * Sets the value of seqInStack to preferences
     * @param seqInStack New value for seqInStack
     */
    public void setSeqInStack(boolean seqInStack) {
        boolean oldSeqInStack = isSeqInStack();
        this.seqInStack = seqInStack;
        seqInStackPrefAdapter.setBoolean(seqInStack);
        firePropertyChange(PROPERTYNAME_SEQINSTACK, oldSeqInStack, seqInStack);
    }
}