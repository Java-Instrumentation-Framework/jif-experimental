package edu.mbl.jif.utils.color;


import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;

public class ColorPicker_HSB extends JPanel
    implements MouseListener {

  /** This example main shows how to activate the color picker inside a
   *  generic dialog pane. You can use this main loop to test the picker.
   */
  public static void main(String[] args) {

    // pick colors loop
    while (true) {
      ColorChoice choice = pickColorHSB();
      if (choice.zCancel) System.exit(0);
      ImageIcon icon = getColorIcon(choice.iValueHSB);
      String sHSB = sToHex(choice.iValueHSB, 8);
      String sRGB = sToHex(choice.iValueRGB, 8);
      JOptionPane.showMessageDialog(null, "You picked color HSB: " + sHSB +
          "RGB: " + sRGB, "Color Picker Tester",
          JOptionPane.INFORMATION_MESSAGE,
          icon);
    }

// this is how to show the default color chooser
//		JColorChooser chooser = new JColorChooser();
// JDialog dialog = JColorChooser.createDialog(null, "pick", false, chooser, null, null);
// dialog.setVisible(true);

  }

  static ImageIcon getColorIcon(int iHSB) {
    BufferedImage bi = new BufferedImage(36, 40, BufferedImage.TYPE_INT_ARGB
        ); // smaller buffer just used to get sizes
    Graphics2D g2 = (Graphics2D) bi.getGraphics();
    g2.setColor(Color.BLACK);
    g2.drawString("A", 2, 2); // so transparency will be visible (does not work
    //for some reason in dialog, but does work for buttons)
    Color color = new Color(iHSBtoRGBA(iHSB));
    g2.setColor(color);
    g2.fillRect(0, 0, 36, 40);
    return new javax.swing.ImageIcon(bi);
  }

  private static ColorPicker_HSB mHSBpicker = null;
  private static int miColorSelected_HSB = 0xFF000000; // defaults to opaque         white
  private static JOptionPane jop;
  private static JDialog jd;
  private static ColorChoice color_choice; // use class to be able to return cancelled state
  public static ColorChoice pickColorHSB() {

    // setup picker dialog if it does not exist yet
    if (mHSBpicker == null) {
      mHSBpicker = new ColorPicker_HSB();
      mHSBpicker.setColor(miColorSelected_HSB);
      jop = new JOptionPane(mHSBpicker, JOptionPane.PLAIN_MESSAGE,
          JOptionPane.OK_CANCEL_OPTION);
      jd = jop.createDialog(null, "Choose Color");
      // subsitute your app frame for null here
      color_choice = new ColorChoice();
    }

    // activate picker and collect choice
    jd.setVisible(true);
    Object oValue = jop.getValue();
    if (oValue == null || oValue.toString().equals("2")) {
      color_choice.zCancel = true;
    } else {
      color_choice.zCancel = false;
      color_choice.iValueHSB = mHSBpicker.getHSB();
      color_choice.iValueRGB = mHSBpicker.getRGB();
      color_choice.color = mHSBpicker.getColor();
    }

    return color_choice;
  }

  BufferedImage mbi = null;
  int mpxCanvasWidth;
  int mpxCanvasHeight;
  Color mcolorBackground = Color.WHITE;
  Graphics2D g2 = null;
  FontMetrics mfontmetricsSansSerif10 = null;
  FontMetrics mfontmetricsSansSerifBold12 = null;
  int mpxTextBoldHeight = 0;
  int mpxTextHeight = 0;
  int mpxTextAscent = 0;
  int mpxFFWidth = 0;
  int mpxFFBold12Width = 0;
  int mpxMarginLeft = 20;
  int mpxMarginTop = 20;
  int mpxInset = 7;
  int mpxSwatchSize = 0;
  int mpxGutter = 10;
  int mpxLeading = 6;
  int mpxLabelWidth_color;
  int mpxTestSelect_width = 50;
  int mpxTestSelect_height = 20;
  int mhsbSelect = 0xFF0000FF;
  int mhsbCompare = 0xFF000000;
  int miAlphaSelect = 0xFF;
  int miHueSelect = 0;
  int miSatSelect = 0;
  int miBriSelect = 0xFF;
  Color colorSelect = Color.WHITE;
  Color colorCompare = Color.BLACK;
  Color[] colorHue;
  Color[] colorSat;
  Color[] colorBri;
  Color[] colorAlpha;
  int mxHueGrid;
  int myHueGrid;
  int myHueHeader;
  int mhOutlineBoxes;
  int mxColorSquare;
  int myColorSquare;
  int mpxHue_Left;
  int mpxSaturation_Left;
  int mpxBrightness_Left;
  int mpxAlpha_Left;
  int mpxHueWidth;
  int mpxSatWidth;
  int mpxBriWidth;
  int mpxAlphaWidth;
  int mxSatLabels;
  int mxBriLabels;
  int mxAlphaLabels;
  int mxSatSwatch;
  int mxBriSwatch;
  int mxAlphaSwatch;
  int mxFooterOrigin;
  int myFooterOrigin;
  int mxColorLabel;
  GeneralPath mshapeIndicator;

  final static Font fontSansSerif10 = new Font("Sans Serif", Font.PLAIN, 10);
  final static Font fontSansSerifBold10 = new Font("Sans Serif", Font.BOLD, 10);
  final static Font fontSansSerifBold12 = new Font("Sans Serif", Font.BOLD, 12);

  public ColorPicker_HSB() {
    mbi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    // smaller buffer just used to get sizes
    g2 = (Graphics2D) mbi.getGraphics();
    mfontmetricsSansSerif10 = g2.getFontMetrics(fontSansSerif10);
    mfontmetricsSansSerifBold12 = g2.getFontMetrics(fontSansSerifBold12);
    mpxTextHeight = mfontmetricsSansSerif10.getHeight() + 2;
    mpxTextAscent = mfontmetricsSansSerif10.getAscent();
    mpxTextBoldHeight = mfontmetricsSansSerifBold12.getHeight();
    mpxFFWidth = mfontmetricsSansSerif10.stringWidth("FF") + 4;
    mpxFFBold12Width = mfontmetricsSansSerifBold12.stringWidth("FF") + 4;
    mpxLabelWidth_color = mfontmetricsSansSerifBold12.stringWidth("Color:");
    colorHue = new Color[0x100];
    for (int xHue = 0; xHue < 0x100; xHue++) {
      int rgba = iHSBtoRGBA(0xFF, xHue, 0xFF, 0xFF);
      colorHue[xHue] = new Color(rgba, true);
    }

    colorSat = new Color[18];
    colorSat[1] = new Color(iHSBtoRGBA(0xFF, 0xFF, 0xFF, 0xFF), true);
    // FF case
    int iSat = 0xF0;
    for (int xSat = 2; xSat <= 17; xSat++) {
      int rgba = iHSBtoRGBA(0xFF, 0xFF, iSat, 0xFF);
      colorSat[xSat] = new Color(rgba, true);
      iSat -= 0x10;
    }

    colorBri = new Color[18];
    colorBri[1] = new Color(iHSBtoRGBA(0xFF, 0xFF, 0xFF, 0xFF), true);
    // FF case
    int iBri = 0xF0;
    for (int xBri = 2; xBri <= 17; xBri++) {
      int rgba = iHSBtoRGBA(0xFF, 0xFF, 0xFF, iBri);
      colorBri[xBri] = new Color(rgba, true);
      iBri -= 0x10;
    }

    colorAlpha = new Color[18];
    colorAlpha[1] = new Color(iHSBtoRGBA(0xFF, 0xFF, 0xFF, 0xFF), true);
    int iAlpha = 0xF0;
    for (int xAlpha = 2; xAlpha <= 17; xAlpha++) {
      int rgba = iHSBtoRGBA(iAlpha, 0xFF, 0xFF, 0xFF);
      colorAlpha[xAlpha] = new Color(rgba, true);
      iAlpha -= 0x10;
    }

    // dimensional calculations
    mpxSwatchSize = mpxFFWidth - 2;
    int pxIndicatorHeight = mpxSwatchSize - 2;
    int pxIndicatorWidth = (pxIndicatorHeight * 2) / 3;
    mxHueGrid = mpxMarginLeft + mpxInset;
    myHueGrid = mpxMarginTop + mpxTextBoldHeight + mpxLeading;
    myHueHeader = myHueGrid + mpxTextHeight;
    mhOutlineBoxes = myHueHeader + mpxTextHeight * 16;
    mpxHueWidth = mpxInset * 2 + mpxFFWidth * 17;
    mpxSatWidth = mfontmetricsSansSerifBold12.stringWidth("Saturation") +
        mpxInset * 2;
    mpxBriWidth = mfontmetricsSansSerifBold12.stringWidth("Brightness") +
        mpxInset * 2;
    mpxAlphaWidth = mfontmetricsSansSerifBold12.stringWidth("Alpha") +
        (mpxInset + pxIndicatorWidth) + mpxInset * 2;
    mpxCanvasWidth = mpxMarginLeft * 2 + mpxHueWidth + mpxSatWidth +
        mpxBriWidth
        + mpxAlphaWidth + mpxGutter * 3;
    mpxHue_Left = mpxMarginLeft;
    mpxSaturation_Left = mpxMarginLeft + mpxHueWidth + mpxGutter;
    mpxBrightness_Left = mpxSaturation_Left + mpxSatWidth + mpxGutter;
    mpxAlpha_Left = mpxBrightness_Left + mpxBriWidth + mpxGutter;
    int yFooterOffset = mpxLeading;
    int xFooterOffset = mpxInset + 2;
    int pxFooterHeight = mpxTextBoldHeight + mpxTestSelect_height + mpxLeading;
    mxSatLabels = mpxSaturation_Left + mpxInset;
    mxBriLabels = mpxBrightness_Left + mpxInset;
    mxAlphaLabels = mpxAlpha_Left + mpxInset;
    mxSatSwatch = mxSatLabels + mpxFFWidth + 2;
    mxBriSwatch = mxBriLabels + mpxFFWidth + 2; ;
    mxAlphaSwatch = mxAlphaLabels + mpxFFWidth + 2;
    mxFooterOrigin = mpxMarginLeft + xFooterOffset;
    myFooterOrigin = mpxMarginTop + mhOutlineBoxes + mpxInset + 6;
    mxColorSquare = mxFooterOrigin + mpxLabelWidth_color + 3;
    myColorSquare = myFooterOrigin;
    mxColorLabel = mxColorSquare + mpxTestSelect_width * 2 + xFooterOffset *
        2 + 2;
    mpxCanvasHeight = mpxMarginTop * 2 + mpxTextBoldHeight + mpxLeading * 2 +
        mpxTextHeight * 17 + yFooterOffset + pxFooterHeight;

    this.setPreferredSize(new Dimension(mpxCanvasWidth, mpxCanvasHeight));
    mbi = new BufferedImage(mpxCanvasWidth, mpxCanvasHeight,
        BufferedImage.TYPE_INT_ARGB);
    g2 = (Graphics2D) mbi.getGraphics();
    addMouseListener(this);

    // this is the blue pointer tab that shows the current value
    mshapeIndicator = new GeneralPath();
    mshapeIndicator.moveTo(0.0f, (float) pxIndicatorHeight / 2f);
    mshapeIndicator.lineTo( (float) pxIndicatorWidth, 0f);
    mshapeIndicator.lineTo( (float) pxIndicatorWidth, (float) pxIndicatorHeight);
    mshapeIndicator.lineTo(0.0f, (float) pxIndicatorHeight / 2f);

  }

  StringBuffer sb = new StringBuffer(80);
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g2.setColor(mcolorBackground);
    g2.fillRect(0, 0, mpxCanvasWidth, mpxCanvasHeight); // draw background
    g2.setColor(Color.black);
    g2.setFont(fontSansSerifBold12);
    int yTitle = mpxMarginTop + mpxTextBoldHeight;
    g2.drawString("Hue", mpxMarginLeft + mpxFFWidth * 8, yTitle);
    g2.drawString("Saturation", mpxSaturation_Left + mpxInset, yTitle);
    g2.drawString("Brightness", mpxBrightness_Left + mpxInset, yTitle);
    g2.drawString("Alpha", mpxAlpha_Left + (mpxAlphaWidth -
        mfontmetricsSansSerifBold12.stringWidth("Alpha")) / 2, yTitle);
    g2.setFont(fontSansSerif10);
    int xElement = 1;
    int xHueColLab = mxHueGrid + 1;
    g2.drawString("00", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("01", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("02", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("03", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("04", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("05", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("06", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("07", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("08", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("09", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("0A", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("0B", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("0C", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("0D", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("0E", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    g2.drawString("0F", xHueColLab + mpxFFWidth * xElement++, myHueHeader);
    xElement = 1;
    int xHueRowLab = mxHueGrid + 1;
    int yHueRowLab = myHueGrid + mpxTextHeight - 2;
    g2.drawString("00", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("10", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("20", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("30", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("40", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("50", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("60", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("70", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("80", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("90", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("A0", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("B0", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("C0", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("D0", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("E0", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);
    g2.drawString("F0", xHueRowLab, yHueRowLab + mpxTextHeight * xElement++);

    // hues
    int xHue, yHue = 0;
    for (xHue = 1; xHue <= 16; xHue++) {
      for (yHue = 1; yHue <= 16; yHue++) {
        g2.setColor(colorHue[ (yHue - 1) << 4 | (xHue - 1)]);
        g2.fillRect(mxHueGrid + mpxFFWidth * xHue, myHueGrid +
            mpxTextHeight * yHue + 2, mpxSwatchSize, mpxSwatchSize);
      }
    }

    int pxColumnOffset = 20;
    int pxLabelY = myHueHeader - 2;

    // saturation
    xElement = 1;
    g2.setColor(Color.black);
    g2.drawString("FF", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("F0", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("E0", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("D0", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("C0", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("B0", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("A0", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("90", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("80", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("70", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("60", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("50", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("40", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("30", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("20", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("10", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("00", mxSatLabels, pxLabelY + mpxTextHeight * xElement++);
    int xSat = 1;
    for (; xSat <= 17; xSat++) {
      g2.setColor(colorSat[xSat]);
      g2.fillRect(mxSatSwatch, myHueGrid + mpxTextHeight * xSat + 2,
          mpxSwatchSize, mpxSwatchSize);
    }

    // brightness
    xElement = 1;
    g2.setColor(Color.black);
    g2.drawString("FF", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("F0", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("E0", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("D0", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("C0", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("B0", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("A0", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("90", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("80", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("70", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("60", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("50", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("40", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("30", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("20", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("10", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("00", mxBriLabels, pxLabelY + mpxTextHeight * xElement++);
    int xBri = 1;
    for (; xBri <= 17; xBri++) {
      g2.setColor(colorBri[xBri]);
      g2.fillRect(mxBriSwatch, myHueGrid + mpxTextHeight * xBri + 2,
          mpxSwatchSize, mpxSwatchSize);
    }

    pxColumnOffset = 15;

    // alpha
    xElement = 1;
    g2.setColor(Color.black);
    g2.drawString("FF", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("F0", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("E0", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("D0", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("C0", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("B0", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("A0", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("90", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("80", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("70", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("60", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("50", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("40", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("30", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("20", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("10", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.drawString("00", mxAlphaLabels, pxLabelY + mpxTextHeight * xElement++);
    g2.setFont(fontSansSerifBold12);
    int xAlpha = 1;
    for (; xAlpha <= 17; xAlpha++) {
      g2.setColor(Color.BLACK);
      g2.drawString("A", mxAlphaLabels + mpxFFWidth + 5, pxLabelY +
          mpxTextHeight * xAlpha);
      g2.setColor(colorAlpha[xAlpha]);
      g2.fillRect(mxAlphaSwatch, myHueGrid + mpxTextHeight * xAlpha + 2,
          mpxSwatchSize, mpxSwatchSize);
    }

    g2.setColor(Color.BLACK);

    // area outlines
    g2.drawRect(mpxHue_Left, mpxMarginTop, mpxHueWidth, mhOutlineBoxes); // hue
    g2.drawRect(mpxSaturation_Left, mpxMarginTop, mpxSatWidth, mhOutlineBoxes);
    // sat
    g2.drawRect(mpxBrightness_Left, mpxMarginTop, mpxBriWidth, mhOutlineBoxes);
    // bri
    g2.drawRect(mpxAlpha_Left, mpxMarginTop, mpxAlphaWidth, mhOutlineBoxes);
    // alpha

    // footer
    g2.setColor(Color.BLACK);
    g2.setFont(fontSansSerifBold12);
    int pxAlpha_width = mfontmetricsSansSerifBold12.stringWidth("alpha");
    int pxAlpha_offset = (mpxTestSelect_width - pxAlpha_width) / 2;
    g2.drawString("Color: ", mxFooterOrigin, myFooterOrigin +
        mpxTextBoldHeight);
    g2.drawString("alpha", mxColorSquare + pxAlpha_offset, myColorSquare +
        mpxTextAscent + 4);
    g2.drawString("alpha", mxColorSquare + mpxTestSelect_width +
        pxAlpha_offset, myColorSquare + mpxTextAscent + 4);
    g2.setColor(colorSelect);
    g2.fillRect(mxColorSquare, myColorSquare, mpxTestSelect_width,
        mpxTestSelect_height);
    g2.setPaint(colorCompare);
    g2.fillRect(mxColorSquare + mpxTestSelect_width, myColorSquare,
        mpxTestSelect_width, mpxTestSelect_height);
    g2.setColor(Color.black);
    g2.drawRect(mxColorSquare, myColorSquare, mpxTestSelect_width * 2,
        mpxTestSelect_height);

    // color hex text: AHSB
    sb.setLength(0);
    sb.append(sToHex(miAlphaSelect, 2));
    sb.append(' ');
    sb.append(sToHex(miHueSelect, 2));
    sb.append(' ');
    sb.append(sToHex(miSatSelect, 2));
    sb.append(' ');
    sb.append(sToHex(miBriSelect, 2));
    sb.append(' ');
    String sAHSB = sb.toString();

    g2.setColor(Color.BLACK);

    int pxColorLabel_width = mfontmetricsSansSerifBold12.stringWidth("AHSB:");
    int xHSB = mxColorLabel + pxColorLabel_width + 10;
    g2.drawString("AHSB:", mxColorLabel, myColorSquare + mpxTextBoldHeight -
        6);
    g2.drawString(sAHSB, xHSB, myColorSquare + mpxTextBoldHeight - 6);

    g2.drawString("ARGB:", mxColorLabel, myColorSquare + mpxTextBoldHeight * 2 -
        3);
    sb.setLength(0);
    sb.append(sToHex(iHSBtoRGBA(this.mhsbSelect), 8));
    g2.setColor(Color.BLACK);
    g2.drawString(sb.toString(), xHSB, myColorSquare + mpxTextBoldHeight * 2 -
        3);

    // indicators

    // hue selection
//		xHue = miHueSelect & 0xF0 >> 8 + 1;
//		yHue = miHueSelect & 0x0F + 1;
//		g2.translate(xHue, yHue);
//		g2.draw(mshapeIndicator);

    int pxIndicatorOffset = mpxFFWidth + mpxSwatchSize + 5;
    g2.setPaint(Color.cyan);

    // sat selection
    xSat = (miSatSelect == 0xFF) ? 1 : (0xF0 - miSatSelect) / 0x10 + 2;
    g2.translate(mxSatLabels + pxIndicatorOffset, myHueGrid + mpxTextHeight *
        xSat + 2);
    g2.fill(mshapeIndicator);
    g2.translate( -mxSatLabels - pxIndicatorOffset, -myHueGrid - mpxTextHeight *
        xSat - 2);

    // bri selection
    xBri = (miBriSelect == 0xFF) ? 1 : (0xF0 - miBriSelect) / 0x10 + 2;
    g2.translate(mxBriLabels + pxIndicatorOffset, myHueGrid + mpxTextHeight *
        xBri + 2);
    g2.fill(mshapeIndicator);
    g2.translate( -mxBriLabels - pxIndicatorOffset, -myHueGrid - mpxTextHeight *
        xBri - 2);

    // alpha selection
    xAlpha = (miAlphaSelect == 0xFF) ? 1 : (0xF0 - miAlphaSelect) / 0x10 + 2;
    g2.translate(mxAlphaLabels + pxIndicatorOffset, myHueGrid + mpxTextHeight *
        xAlpha + 2);
    g2.fill(mshapeIndicator);
    g2.translate( -mxAlphaLabels - pxIndicatorOffset,
        -myHueGrid - mpxTextHeight
        * xAlpha - 2);

    ( (Graphics2D) g).drawImage(mbi, null, 0, 0);
  }

  public int getHSB() {return mhsbSelect; }

  public int getRGB() {return iHSBtoRGBA(mhsbSelect); }

  public int getAlpha() {return miAlphaSelect; }

  public int getHue() {return miHueSelect; }

  public int getSat() {return miSatSelect; }

  public int getBri() {return miBriSelect; }

  public Color getColor() {return colorSelect; }

  public void setAlpha(int i) {
    setColor(i, getHue(), getSat(), getBri());
  }

  // sat/bri/alpha is automatically set if zero because if the
  //previous color was white (sat = 0)
  // and the user selects a hue the assumption is that they want a saturated color
  public void setHue(int i) {
    int iAlpha = getAlpha();
    int iSat = getSat();
    int iBri = getBri();
    if (iAlpha == 0) iAlpha = 0xFF;
    if (iSat == 0) iSat = 0xFF;
    if (iBri == 0) iBri = 0xFF;
    setColor(iAlpha, i, iSat, iBri);
  }

  public void setSat(int i) {
    setColor(getAlpha(), getHue(), i, getBri());
  }

  public void setBri(int i) {
    setColor(getAlpha(), getHue(), getSat(), i);
  }

  public void setColor(int iAlpha, int iHue, int iSat, int iBri) {
    miAlphaSelect = iAlpha;
    miHueSelect = iHue;
    miSatSelect = iSat;
    miBriSelect = iBri;
    int iHSB = iAlpha << 24 | iHue << 16 | iSat << 8 | iBri;
    colorSelect = new Color(iHSBtoRGBA(iHSB), true);
    mhsbSelect = iHSB;
    repaint();
  }

  public void setColor(int iHSB) {
    int iAlpha = (int) ( (iHSB & 0xFF000000L) >> 24);
    int iHue = (iHSB & 0x00FF0000) >> 16;
    int iSat = (iHSB & 0x0000FF00) >> 8;
    int iBri = (iHSB & 0x000000FF);
    setColor(iAlpha, iHue, iSat, iBri);
    if (mhsbCompare == 0) setCompare(iHSB);
  }

  public void setCompare(int iHSB) {
    mhsbCompare = iHSB;
    colorCompare = new Color(iHSBtoRGBA(iHSB), true);
    repaint();
  }

  void swapSelectCompare() {
    int hsbSelect = getHSB();
    setColor(mhsbCompare);
    setCompare(hsbSelect);
  }

  // Mouse listener interface
  public void mousePressed(MouseEvent evt) {
  }

  public void mouseDragged(MouseEvent evt) {
  }

  public void mouseReleased(MouseEvent evt) {
  }

  public void mouseEntered(MouseEvent evt) {}

  public void mouseExited(MouseEvent evt) {}

  public void mouseClicked(MouseEvent evt) {
    int xPX = evt.getX();
    int yPX = evt.getY();
    if (xPX >= mxHueGrid + mpxFFWidth &&
        xPX <= mxHueGrid + mpxFFWidth * 17 &&
        yPX >= myHueGrid + mpxTextHeight &&
        yPX <= myHueGrid + mpxTextHeight * 17) { // hit a hue
      int xHueIndex = (xPX - (mxHueGrid + mpxFFWidth)) / (mpxSwatchSize +
          2);
      int yHueIndex = (yPX - (myHueGrid + mpxTextHeight)) / (mpxSwatchSize +
          2);
      int iHue = yHueIndex << 4 | xHueIndex;
      setHue(iHue);
    } else if (
        yPX >= myHueGrid + mpxTextHeight &&
        yPX <= myHueGrid + mpxTextHeight * 18) {
      if (xPX >= mxSatSwatch &&
          xPX <= mxSatSwatch + mpxSwatchSize) { // hit a sat
        int ySatIndex = (yPX - (myHueGrid + mpxTextHeight)) /
            mpxTextHeight;
        int iSat = ySatIndex == 0 ? 0xFF : (16 - ySatIndex) * 0x10;
        setSat(iSat);
      } else if (xPX >= mxBriSwatch &&
          xPX <= mxBriSwatch + mpxSwatchSize) { // hit a bri
        int yBriIndex = (yPX - (myHueGrid + mpxTextHeight)) /
            mpxTextHeight;
        int iBri = yBriIndex == 0 ? 0xFF : (16 - yBriIndex) * 0x10;
        setBri(iBri);
      } else if (xPX >= mxAlphaSwatch &&
          xPX <= mxAlphaSwatch + mpxSwatchSize) { // hit an alpha
        int yAlphaIndex = (yPX - (myHueGrid + mpxTextHeight)) /
            mpxTextHeight;
        int iAlpha = yAlphaIndex == 0 ? 0xFF : (16 - yAlphaIndex) * 0x10;
        setAlpha(iAlpha);
      }
    } else if (xPX >= mxColorSquare &&
        xPX <= mxColorSquare + mpxTestSelect_width * 2 &&
        yPX >= myColorSquare &&
        yPX <= myColorSquare + mpxTestSelect_height) {
      swapSelectCompare();
    } else if (xPX >= mxColorSquare + 2 * mpxTestSelect_width + 4 &&
        xPX <= mxColorSquare + 2 * mpxTestSelect_width + 16 &&
        yPX >= myColorSquare + mpxTextBoldHeight * 2 + 3 &&
        yPX <= myColorSquare + mpxTextBoldHeight * 2 + 16) {
      try {
        java.awt.datatransfer.Clipboard clipboard =
            Toolkit.getDefaultToolkit().getSystemClipboard();
        String sRGB = sToHex(iHSBtoRGBA(mhsbSelect), 6);
        java.awt.datatransfer.StringSelection contents = new
            java.awt.datatransfer.StringSelection(sRGB);
        clipboard.setContents(contents, null);
        //ApplicationController.vShowStatus_NoCache("clipped " + sRGB);
      } catch (Throwable ex) {
        //ApplicationController.vShowError("Unable to clip RGB: " + ex);
      }
    }
  }

  public static int iHSBtoRGBA(int iHSB) {
    int iAlpha = (int) ( (iHSB & 0xFF000000L) >> 24);
    ;
    int iHue = (iHSB & 0x00FF0000) >> 16;
    int iSaturation = (iHSB & 0x0000FF00) >> 8;
    int iBrightness = iHSB & 0x000000FF;
    return iHSBtoRGBA(iAlpha, iHue, iSaturation, iBrightness);
  }

  public static int iHSBtoRGBA(int iAlpha, int ihue, int isaturation, int
      ibrightness) {
    float hue = (float) ihue / 255;
    float saturation = (float) isaturation / 255;
    float brightness = (float) ibrightness / 255;
    int r = 0, g = 0, b = 0;
    if (saturation == 0) {
      r = g = b = (int) (brightness * 255.0f + 0.5f);
    } else {
      float h = (hue - (float) Math.floor(hue)) * 6.0f;
      float f = h - (float) java.lang.Math.floor(h);
      float p = brightness * (1.0f - saturation);
      float q = brightness * (1.0f - saturation * f);
      float t = brightness * (1.0f - (saturation * (1.0f - f)));
      switch ( (int) h) {
        case 0:
          r = (int) (brightness * 255.0f + 0.5f);
          g = (int) (t * 255.0f + 0.5f);
          b = (int) (p * 255.0f + 0.5f);
          break;
        case 1:
          r = (int) (q * 255.0f + 0.5f);
          g = (int) (brightness * 255.0f + 0.5f);
          b = (int) (p * 255.0f + 0.5f);
          break;
        case 2:
          r = (int) (p * 255.0f + 0.5f);
          g = (int) (brightness * 255.0f + 0.5f);
          b = (int) (t * 255.0f + 0.5f);
          break;
        case 3:
          r = (int) (p * 255.0f + 0.5f);
          g = (int) (q * 255.0f + 0.5f);
          b = (int) (brightness * 255.0f + 0.5f);
          break;
        case 4:
          r = (int) (t * 255.0f + 0.5f);
          g = (int) (p * 255.0f + 0.5f);
          b = (int) (brightness * 255.0f + 0.5f);
          break;
        case 5:
          r = (int) (brightness * 255.0f + 0.5f);
          g = (int) (p * 255.0f + 0.5f);
          b = (int) (q * 255.0f + 0.5f);
          break;
      }
    }
    return (iAlpha << 24) | (r << 16) | (g << 8) | (b << 0);
  }

  public static String sToHex(int i, int iWidth) {
    String s;
    char cPadding;
    try {
      s = Integer.toHexString(i).toUpperCase();
      cPadding = '0';
    } catch (Exception ex) {
      s = "?";
      cPadding = '?';
    }
    return sFixedWidth(s, iWidth, cPadding);
  }

  public static String sFixedWidth(String s, int iWidth, char cPadding) {
    StringBuffer sb;
    if (s.length() < iWidth) {
      sb = new StringBuffer(iWidth);
      for (int xChar = 1; xChar <= iWidth - s.length(); xChar++)
        sb.append(cPadding);
      return sb.append(s).toString();
    } else {
      if (s.length() > iWidth)
        return s.substring(0, iWidth);
      else
        return s;
    }
  }

}

/** this structure is used in conjunction with the testing harness in main() */
class ColorChoice {
  boolean zCancel;
  int iValueHSB;
  int iValueRGB;
  Color color;
}
