package binding;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.binding.value.ValueModel;
import java.text.NumberFormat;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.WindowConstants;
import org.apache.commons.beanutils.converters.AbstractConverter;



public final class ImgDisplayCtrl {

	// Holds an ExampleBindingBean and vends ValueModels that adapt its properties.
	private final ImgDisplayModel model;
	private final ImgDisplayPresentationModel presentationModel;
	private JLabel labelFrameIndex;
	private JScrollBar scrollbar;

	// Launching **************************************************************
	public static void main(String[] args) {
//		try {
//			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
//		} catch (Exception e) {
//			// Likely PlasticXP is not in the class path; ignore.
//		}
		JFrame frame = new JFrame();
		frame.setTitle("Binding Tutorial :: Components");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		ImgDisplayCtrl example = new ImgDisplayCtrl();
		JComponent panel = example.buildPanel();
		frame.getContentPane().add(panel);
		frame.pack();
		//TutorialUtils.locateOnScreenCenter(frame);
		frame.setVisible(true);
	}

	// Instance Creation ******************************************************
	/**
	 * Constructs the 'Components' example on an instance of ExampleBindingBean.
	 */
	public ImgDisplayCtrl() {
		model = new ImgDisplayModel();
		this.presentationModel = new ImgDisplayPresentationModel(model);
	}

	private void initComponents() {
		ValueModel frameIndexModel = presentationModel.getModel(ImgDisplayModel.PROP_FRAMEINDEX);
		//ValueModel  quantityStringValue  = new  IntegerAsStringConverter(frameIndexModel);
		labelFrameIndex = BasicComponentFactory.createLabel(
				ConverterFactory.createStringConverter(frameIndexModel,NumberFormat.getIntegerInstance()));
		scrollbar = new JScrollBar();
		scrollbar.setModel(new BoundedRangeAdapter(frameIndexModel, 0, 0, 100));
		frameIndexModel.setValue(44);
	}

	private void initEventHandling() {
    //presentationModel.getModel(ExampleBindingBean.PROPERTYNAME_COLOR).
		//addValueChangeListener(new ColorUpdateHandler());
	}

	// Building ***************************************************************
	/**
	 * Builds and returns the panel.
	 *
	 * @return the built panel
	 */
	public JComponent buildPanel() {
		initComponents();
		initEventHandling();
		JPanel ctrlPanel = new JPanel();
		JLabel label = new JLabel("Frame");
		ctrlPanel.add(label);
		ctrlPanel.add(scrollbar);
		scrollbar.setOrientation(JScrollBar.HORIZONTAL);
		ctrlPanel.add(labelFrameIndex);
		labelFrameIndex.invalidate();
		return ctrlPanel;
	}

	private static final class ImgDisplayPresentationModel extends PresentationModel {

		private ImgDisplayPresentationModel(ImgDisplayModel imgDisplayModel) {
			super(imgDisplayModel);
		}

	}

	class IntegerAsStringConverter extends AbstractConverter {

		public IntegerAsStringConverter(ValueModel valueModel) {
			super(valueModel);
		}

//		public Object convertFromSubject(Object subjectValue) {
//               return convertFromSubject(subjectValue);
//		}
//
//		public void setValue(Object newValue) {
//			setValue(Integer.parseInt((String) newValue));
//		}

      @Override
      protected Object convertToType(Class type, Object o) throws Throwable {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }

      @Override
      protected Class getDefaultType() {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }

	}
}
