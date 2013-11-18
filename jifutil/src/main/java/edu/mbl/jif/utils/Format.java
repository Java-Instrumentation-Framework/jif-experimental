package edu.mbl.jif.utils;

import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
  Formats, globally available
 */

public class Format {

  // this number of decimal places
  public static final DecimalFormat dec1 = new DecimalFormat("0.0");
  public static final DecimalFormat dec2 = new DecimalFormat("0.00");
  public static final DecimalFormat dec3 = new DecimalFormat("0.000");

  // up to this number of decimal places
  public static final DecimalFormat dec_1 = new DecimalFormat("0.#");
  public static final DecimalFormat dec_2 = new DecimalFormat("0.##");
  public static final DecimalFormat dec_3 = new DecimalFormat("0.###");

/** @todo Date formats */

  private Format() {
  }

}
