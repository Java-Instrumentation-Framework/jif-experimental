package edu.mbl.jif.acq;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.PreferencesAdapter;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.list.ArrayListModel;
import com.jgoodies.binding.list.ObservableList;
import com.jgoodies.binding.list.SelectionInList;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.ListModel;

public class AcqModel extends Model {

  static final String DEFAULT_PREFS_KEY = "acq";
  Preferences prefs;
  // --- depth ---
  public static final String PROPERTYNAME_DEPTH = "depth";
  private int depth;
  static final int DEPTH_DEFAULT = 8;
  PreferencesAdapter depthPrefAdapter;
  public static final Integer DEPTH_BYTE = new Integer(8);
  public static final Integer DEPTH_SHORT = new Integer(12);
  public static final List DEPTH_OPTIONS = Arrays.asList(new Integer[]{
            DEPTH_BYTE,
            DEPTH_SHORT
          });
  private ObservableList depthListModel;
  private Object depthListSelection;
  // --- mirrorImage ---
  public static final String PROPERTYNAME_MIRRORIMAGE = "mirrorImage";
  private boolean mirrorImage;
  static final boolean MIRRORIMAGE_DEFAULT = false;
  PreferencesAdapter mirrorImagePrefAdapter;
  // --- mirrorImage ---
  public static final String PROPERTYNAME_FLIPIMAGE = "flipImage";
  private boolean flipImage;
  static final boolean FLIPIMAGE_DEFAULT = false;
  PreferencesAdapter flipImagePrefAdapter;
  // --- multiFrame ---
  public static final String PROPERTYNAME_MULTIFRAME = "multiFrame";
  private int multiFrame;
  static final int MULTIFRAME_DEFAULT = 1;
  PreferencesAdapter multiFramePrefAdapter;
  // --- multiFrameBkgd---
  public static final String PROPERTYNAME_MULTIFRAMEBKGD = "multiFrameBkgd";
  private int multiFrameBkgd;
  static final int MULTIFRAMEBKGD_DEFAULT = 1;
  PreferencesAdapter multiFrameBkgdPrefAdapter;
  // --- div ---
  public static final String PROPERTYNAME_DIV = "div";
  private boolean div;
  static final boolean DIV_DEFAULT = true;
  PreferencesAdapter divPrefAdapter;
  // --- imagesInSequence ---
  public static final String PROPERTYNAME_IMAGESINSEQUENCE = "imagesInSequence";
  private int imagesInSequence;
  static final int IMAGESINSEQUENCE_DEFAULT = 3;
  PreferencesAdapter imagesInSequencePrefAdapter;
  // --- interval ---
  public static final String PROPERTYNAME_INTERVAL = "interval";
  private double interval;
  static final double INTERVAL_DEFAULT = 5;
  PreferencesAdapter intervalPrefAdapter;
  // --- initDelay ---
  public static final String PROPERTYNAME_INITDELAY = "initDelay";
  private double initDelay;
  static final double INITDELAY_DEFAULT = 1;
  PreferencesAdapter initDelayPrefAdapter;
  // --- flushFirstFrame ---
  public static final String PROPERTYNAME_FLUSHFIRSTFRAME = "flushFirstFrame";
  private boolean flushFirstFrame;
  static final boolean FLUSHFIRSTFRAME_DEFAULT = false;
  PreferencesAdapter flushFirstFramePrefAdapter;
  //
  public static final String PROPERTYNAME_OPENSHUTTEREPI = "openShutterEpi";
  private boolean openShutterEpi;
  static final boolean OPENSHUTTEREPI_DEFAULT = false;
  PreferencesAdapter openShutterEpiPrefAdapter;
  //
  public static final String PROPERTYNAME_OPENSHUTTERXMIS = "openShutterXmis";
  private boolean openShutterXmis;
  static final boolean OPENSHUTTERXMIS_DEFAULT = false;
  PreferencesAdapter openShutterXmisPrefAdapter;
  //
  // --- displayWhileAcq ---
  public static final String PROPERTYNAME_DISPLAYWHILEACQ = "displayWhileAcq";
  private boolean displayWhileAcq;
  static final boolean DISPLAYWHILEACQ_DEFAULT = false;
  PreferencesAdapter displayWhileAcqPrefAdapter;

  InstrumentController instrumentCtrl;

  /** constructor
   */
  public AcqModel(InstrumentController instCtrl) throws Exception {
    this(instCtrl, DEFAULT_PREFS_KEY);
  }

  public AcqModel(InstrumentController instCtrl, String key) throws Exception {
    initializePreferenceAdapters(key);
    this.instrumentCtrl = instCtrl;

    // Initialize enumerated list for depth
    depthListModel = new ArrayListModel();
    depthListModel.addAll(DEPTH_OPTIONS);
    depthListSelection = depthListModel.get(0);
  }

  public void initializePreferenceAdapters(String key) {
    prefs = CamAcqJ.getInstance().getPreferences().node(key);
    depthPrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_DEPTH, DEPTH_DEFAULT);
    mirrorImagePrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_MIRRORIMAGE, MIRRORIMAGE_DEFAULT);
    flipImagePrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_FLIPIMAGE, FLIPIMAGE_DEFAULT);
    multiFramePrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_MULTIFRAME, MULTIFRAME_DEFAULT);
    multiFrameBkgdPrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_MULTIFRAMEBKGD, MULTIFRAMEBKGD_DEFAULT);
    divPrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_DIV, DIV_DEFAULT);
    imagesInSequencePrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_IMAGESINSEQUENCE, IMAGESINSEQUENCE_DEFAULT);
    intervalPrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_INTERVAL, INTERVAL_DEFAULT);
    initDelayPrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_INITDELAY, INITDELAY_DEFAULT);
    flushFirstFramePrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_FLUSHFIRSTFRAME, FLUSHFIRSTFRAME_DEFAULT);
    displayWhileAcqPrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_DISPLAYWHILEACQ, DISPLAYWHILEACQ_DEFAULT);
    openShutterEpiPrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_OPENSHUTTEREPI, OPENSHUTTEREPI_DEFAULT);
    openShutterXmisPrefAdapter =
            new PreferencesAdapter(prefs, AcqModel.PROPERTYNAME_OPENSHUTTERXMIS, OPENSHUTTERXMIS_DEFAULT);
  }

  /**
   * Gets the current value of depth from preferences
   * @return Current value of depth
   */
  public int getDepth() {
    depth = depthPrefAdapter.getInt();
    return depth;
  }

  /**
   * Sets the value of depth to preferences
   * @param depth New value for depth
   */
  public void setDepth(int depth) {
    int oldDepth = getDepth();
    this.depth = depth;
    depthPrefAdapter.setInt(depth);
    firePropertyChange(PROPERTYNAME_DEPTH, oldDepth, depth);
  }

  /**
   * ListModel accessor for depth
   * @return ListModel of depth
   */
  public ListModel getDepthListModel() {
    return depthListModel;
  }

  /**
   * ListSelection accessor for depth
   * @return ListSelection of depth
   */
  public Object getDepthListSelection() {
    return depthListSelection;
  }

  /**
   * Gets the current value of mirrorImage from preferences
   * @return Current value of mirrorImage
   */
  public boolean isMirrorImage() {
    mirrorImage = mirrorImagePrefAdapter.getBoolean();
    return mirrorImage;
  }

  /**
   * Sets the value of mirrorImage to preferences
   * @param mirrorImage New value for mirrorImage
   */
  public void setMirrorImage(boolean mirrorImage) {
    boolean oldMirrorImage = isMirrorImage();
    this.mirrorImage = mirrorImage;
    mirrorImagePrefAdapter.setBoolean(mirrorImage);
    firePropertyChange(PROPERTYNAME_MIRRORIMAGE, oldMirrorImage, mirrorImage);
  }

  /**
   * Gets the current value of flipImage from preferences
   * @return Current value of flipImage
   */
  public boolean isFlipImage() {
    flipImage = flipImagePrefAdapter.getBoolean();
    return flipImage;
  }

  /**
   * Sets the value of flipImage to preferences
   * @param flipImage New value for flipImage
   */
  public void setFlipImage(boolean flipImage) {
    boolean oldFlipImage = isFlipImage();
    this.flipImage = flipImage;
    flipImagePrefAdapter.setBoolean(flipImage);
    firePropertyChange(PROPERTYNAME_FLIPIMAGE, oldFlipImage, flipImage);
  }

  /**
   * Gets the current value of multiFrame from preferences
   * @return Current value of multiFrame
   */
  public int getMultiFrame() {
    multiFrame = multiFramePrefAdapter.getInt();
    return multiFrame;
  }

  /**
   * Sets the value of multiFrame to preferences
   * @param multiFrame New value for multiFrame
   */
  public void setMultiFrame(int multiFrame) {
    int oldMultiFrame = getMultiFrame();
    this.multiFrame = multiFrame;
    multiFramePrefAdapter.setInt(multiFrame);
    firePropertyChange(PROPERTYNAME_MULTIFRAME, oldMultiFrame, multiFrame);
  }

  /**
   * Gets the current value of multiFrameBkgd from preferences
   * @return Current value of multiFrameBkgd
   */
  public int getMultiFrameBkgd() {
    multiFrameBkgd = multiFrameBkgdPrefAdapter.getInt();
    return multiFrameBkgd;
  }

  /**
   * Sets the value of multiFrameBkgd to preferences
   * @param multiFrameBkgd New value for multiFrameBkgd
   */
  public void setMultiFrameBkgd(int multiFrameBkgd) {
    int oldMultiFrameBkgd = getMultiFrameBkgd();
    this.multiFrameBkgd = multiFrameBkgd;
    multiFrameBkgdPrefAdapter.setInt(multiFrameBkgd);
    firePropertyChange(PROPERTYNAME_MULTIFRAMEBKGD, oldMultiFrameBkgd, multiFrameBkgd);
  }

  /**
   * Gets the current value of div from preferences
   * @return Current value of div
   */
  public boolean isDiv() {
    div = divPrefAdapter.getBoolean();
    return div;
  }

  /**
   * Sets the value of div to preferences
   * @param div New value for div
   */
  public void setDiv(boolean div) {
    boolean oldDiv = isDiv();
    this.div = div;
    divPrefAdapter.setBoolean(div);
    firePropertyChange(PROPERTYNAME_DIV, oldDiv, div);
  }

  /**
   * Gets the current value of imagesInSequence from preferences
   * @return Current value of imagesInSequence
   */
  public int getImagesInSequence() {
    imagesInSequence = imagesInSequencePrefAdapter.getInt();
    return imagesInSequence;
  }

  /**
   * Sets the value of imagesInSequence to preferences
   * @param imagesInSequence New value for imagesInSequence
   */
  public void setImagesInSequence(int imagesInSequence) {
    int oldImagesInSequence = getImagesInSequence();
    this.imagesInSequence = imagesInSequence;
    imagesInSequencePrefAdapter.setInt(imagesInSequence);
    firePropertyChange(PROPERTYNAME_IMAGESINSEQUENCE, oldImagesInSequence, imagesInSequence);
  }

  /**
   * Gets the current value of interval from preferences
   * @return Current value of interval
   */
  public double getInterval() {
    interval = intervalPrefAdapter.getDouble();
    return interval;
  }

  /**
   * Sets the value of interval to preferences
   * @param interval New value for interval
   */
  public void setInterval(double interval) {
    double oldInterval = getInterval();
    this.interval = interval;
    intervalPrefAdapter.setDouble(interval);
    firePropertyChange(PROPERTYNAME_INTERVAL, oldInterval, interval);
  }

  /**
   * Gets the current value of initDelay from preferences
   * @return Current value of initDelay
   */
  public double getInitDelay() {
    initDelay = initDelayPrefAdapter.getDouble();
    return initDelay;
  }

  /**
   * Sets the value of initDelay to preferences
   * @param initDelay New value for initDelay
   */
  public void setInitDelay(double initDelay) {
    double oldInitDelay = getInitDelay();
    this.initDelay = initDelay;
    initDelayPrefAdapter.setDouble(initDelay);
    firePropertyChange(PROPERTYNAME_INITDELAY, oldInitDelay, initDelay);
  }

  /**
   * Gets the current value of flushFirstFrame from preferences
   * @return Current value of flushFirstFrame
   */
  public boolean isFlushFirstFrame() {
    flushFirstFrame = flushFirstFramePrefAdapter.getBoolean();
    return flushFirstFrame;
  }

  /**
   * Sets the value of flushFirstFrame to preferences
   * @param flushFirstFrame New value for flushFirstFrame
   */
  public void setFlushFirstFrame(boolean flushFirstFrame) {
    boolean oldFlushFirstFrame = isFlushFirstFrame();
    this.flushFirstFrame = flushFirstFrame;
    flushFirstFramePrefAdapter.setBoolean(flushFirstFrame);
    firePropertyChange(PROPERTYNAME_FLUSHFIRSTFRAME, oldFlushFirstFrame, flushFirstFrame);
  }
  /**
   * Gets the current value of flushFirstFrame from preferences
   * @return Current value of flushFirstFrame
   */
  public boolean isDisplayWhileAcq() {
    displayWhileAcq = displayWhileAcqPrefAdapter.getBoolean();
    return displayWhileAcq;
  }

  /**
   * Sets the value of flushFirstFrame to preferences
   * @param flushFirstFrame New value for flushFirstFrame
   */
  public void setDisplayWhileAcq(boolean displayWhileAcq) {
    boolean oldDisplayWhileAcq = isDisplayWhileAcq();
    this.displayWhileAcq = displayWhileAcq;
    displayWhileAcqPrefAdapter.setBoolean(displayWhileAcq);
    firePropertyChange(PROPERTYNAME_DISPLAYWHILEACQ, oldDisplayWhileAcq, displayWhileAcq);
  }


  public boolean isOpenShutterEpi() {
    openShutterEpi = this.openShutterEpiPrefAdapter.getBoolean();
    return openShutterEpi;
  }

  public void setOpenShutterEpi(boolean openShutterEpi) {
    boolean oldOpenShutterEpi = isMirrorImage();
    this.openShutterEpi = openShutterEpi;
    openShutterEpiPrefAdapter.setBoolean(openShutterEpi);
    firePropertyChange(PROPERTYNAME_OPENSHUTTEREPI, oldOpenShutterEpi, openShutterEpi);
  }
  public boolean isOpenShutterXmis() {
    openShutterXmis = openShutterXmisPrefAdapter.getBoolean();
    return openShutterXmis;
  }

  public void setOpenShutterXmis(boolean openShutterXmis) {
    boolean oldOpenShutterXmis = isMirrorImage();
    this.openShutterXmis = openShutterXmis;
    openShutterXmisPrefAdapter.setBoolean(openShutterXmis);
    firePropertyChange(PROPERTYNAME_OPENSHUTTERXMIS, oldOpenShutterXmis, openShutterXmis);
  }
  

  public static class AcqPresentation extends PresentationModel {

    // SelectionInList's holds the bean's list model plus a selection

    // --- depth
    private final SelectionInList selectionInDepthList;

    /**
     * Creates a new instance of AcqPresentation
     */
    public AcqPresentation(AcqModel acqModel) {
      super(acqModel);

      // --- depth
      selectionInDepthList = new SelectionInList(acqModel.getDepthListModel(),
              getModel(AcqModel.PROPERTYNAME_DEPTH));
    }


    // --- depth
    public SelectionInList getSelectionInDepthList() {
      return selectionInDepthList;
    }
  }
}