package edu.mbl.jif.utils.time;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

/**
 * A simple AWT GUI to let you try out the
 * SimpleDateFormat class.
 */
public class TryDateFormat extends Frame
{
    private Label timeCaption;
    private Label formatCaption;
    private TextField timeValue;
    private TextField formatValue;
    private Button formatButton;
    private Button helpButton;
    private Button exitButton;
    private TextArea resultArea;
    private Dialog  helpDialog;

    public TryDateFormat() {
	super("Try Java SimpleDateFormat!");
	Date now = new Date();
	Panel ucPanel = new Panel();
	Panel uwPanel = new Panel();
	Panel usPanel = new Panel();
	Panel uPanel  = new Panel();

	ucPanel.setLayout(new GridLayout(2,1));
	uwPanel.setLayout(new GridLayout(2,1));
	usPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
	uPanel.setLayout(new BorderLayout());
	uPanel.setBackground(Color.green);

	timeCaption = new Label("Time value:", Label.RIGHT);
	timeValue = new TextField("" + now.getTime());
	timeValue.setBackground(Color.white);
	formatCaption = new Label("Format specifier:", Label.RIGHT);
	formatValue = new TextField("yyyyMMddHHmmss");
	formatValue.setBackground(Color.white);
	formatButton = new Button("Do Format");
	helpButton = new Button("Help");
	exitButton = new Button("Exit");
	resultArea = new TextArea(12,52);
	resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
	resultArea.setBackground(Color.white);

	uwPanel.add(timeCaption);
	uwPanel.add(formatCaption);
	uPanel.add("West", uwPanel);
	ucPanel.add(timeValue);
	ucPanel.add(formatValue);
	uPanel.add("Center", ucPanel);
	usPanel.add(formatButton);
	usPanel.add(helpButton);
	usPanel.add(exitButton);
	uPanel.add("South", usPanel);
	this.add("North", uPanel);
	this.add("Center", resultArea);

	helpDialog = new Dialog(this, "TryDateFormat Help", false);
	StringBuffer buf = new StringBuffer(1500);
	int maxlen = 0;
	for(int i = 0; i < help.length; i++) {
	    buf.append(" "); buf.append(help[i]); buf.append("\n");
	    if (help[i].length() > maxlen) maxlen = help[i].length();
	}
	TextArea helptext = new TextArea(20, maxlen + 2);
	helptext.setText(buf.toString());
	helptext.setEditable(false);
	helptext.setFont(new Font("Monospaced", Font.PLAIN, 11));
	helpDialog.add("Center", helptext);
	Button dismissButton = new Button("Dismiss");
	helpDialog.add("South", dismissButton);
	helpDialog.pack();


	// event handlers
	exitButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    System.exit(0);
		} });
	helpButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    helpDialog.show();
		} });
	dismissButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    helpDialog.hide();
		} });
	helpDialog.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    helpDialog.hide();
		} });
	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    System.exit(0);
		} });
	formatButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    doFormat();
		} });

	this.pack();
	this.show();
    }

    String help[] = {
	"To use this program, enter a time value",
	"in the top text field, and a SimpleDateFormat",
	"format specifier in the second text field,",
	"then click on the Format button.  Results",
	"appear in the text area at the bottom of",
	"the window.",
	"",
	"The time value must be in seconds since the",
	"epoch, midnight 1/1/1970.",
	"",
	"The format specifier is simply a pattern",
	"string; certain letters in the pattern will",
	"be replaced by values of time or date, ",
	"giving complete control over the formatting.",
	"The most useful format codes are:",
	"",
	"Symbol  Meaning             Presentation   Ex.",
	"------  -------             ------------   ----",
	"G       era designator      (Text)         AD",
	"y       year                (Number)       1996",
	"M       month in year       (Text)         July",
	"M       month in year       (Number)       07",
	"d       day in month        (Number)       10",
	"h       hour in am/pm       (Number)       12",
	"H       hour in day         (Number)       0",
	"m       minute in hour      (Number)       30",
	"s       second in minute    (Number)       55",
	"S       millisecond         (Number)       978",
	"E       day in week         (Text)         Tue",
	"D       day in year         (Number)       189",
	"a       am/pm marker        (Text)         PM",
	"z       time zone           (Text)         PST",
	"'       quote text          (Text)         'at'",
	"",
	"So, for example this format: h:mm a",
	"gives this result: 12:08 PM",
	};

    public void doFormat() {
	long tv = 1;
	String tvs = timeValue.getText();
	try { tv = Long.parseLong(tvs); }
	catch (NumberFormatException nfe) { timeValue.setText("1"); }

	String fmt = formatValue.getText();

	Date d = new Date(tv);
	/* this uses the current Locale, supposedly */
	String res = "";
	try {
	    SimpleDateFormat sdf = new SimpleDateFormat(fmt);
	    res = sdf.format(d);
	}
	catch (Exception e) {
	    res = "<<" + e.getMessage() + ">>";
	}

	resultArea.append("At time:      " + tv);
	resultArea.append("\n");
	resultArea.append("Using format: ");
	resultArea.append(fmt);
	resultArea.append("\n");
	resultArea.append("gives result: ");
	resultArea.append(res);
	resultArea.append("\n\n");
    }

    public static void main(String [] args) {
	TryDateFormat tdf = new TryDateFormat();
    }
}


