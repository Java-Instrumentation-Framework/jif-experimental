/*
 *  From: http://java.dzone.com/articles/swing-jcomponent-validation
 */
package tests.binding.veto;

import com.jgoodies.binding.value.AbstractVetoableValueModel;
import java.awt.EventQueue;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 *
 * @author GBH
 */
/**
 * 
 * @author Anees
 * 
 */
public class MapVetoableValueModelAdaptor /*extends AbstractVetoableValueModel*/ {
//
//	private static final long serialVersionUID = 1L;
//
//	private MapValueModel valueModel;
// 
//	private JComponent component;
//
//	private PropertyConstraintsHolder constraintHolder;
//
//	private final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
//	private final String NUMERIC = "1234567890.";
//	private final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.";
//	private final String BADCHARACTERS = "`~!@#$%^&*()_+=\\|\"':;?/>.<,";
//
//	public MapVetoableValueModelAdaptor(MapValueModel subject,
//			JComponent component, PropertyConstraintsHolder constraintHolder) {
//		super(subject);
//		this.valueModel = subject;
//		this.component = component;
//		this.constraintHolder = constraintHolder;
//	}
//
//	@Override
//	public boolean proposedChange(final Object oldValue, Object proposedNewValue) {
//		final boolean flag;
//		if (proposedNewValue.toString().length() <= constraintHolder
//				.getMaxLength()) {
//			if (checkForInputCharactor(proposedNewValue, constraintHolder
//					.getTextInputDataType())) {
//				flag = true;
//			} else
//				flag = false;
//		} else {
//
//			flag = false;
//		}
//
//		if (!flag) {
//			EventQueue.invokeLater(new Runnable() {
//				@Override
//				public void run() {
//					UpdateJComponent updateComponent = new UpdateJComponent(
//							component, oldValue);
//					updateComponent.processWithOldValue();
//				}
//			});
//		}
//		return flag;
//	}
//
//	/**
//	 * 
//	 * @param proposedNewValue
//	 * @return
//	 */
//	private boolean checkForInputCharactor(Object proposedNewValue,
//			TextFiledInputDataType inputDataType) {
//
//		if (TextFiledInputDataType.ONLYALPHA.equals(inputDataType)) {
//
//			return processOnlyAlpha(proposedNewValue);
//
//		} else if (TextFiledInputDataType.ONLYNUMBER.equals(inputDataType)) {
//
//			return processOnlyForNumeric(proposedNewValue);
//
//		} else
//			return processForAlphaNumeric(proposedNewValue);
//
//	}
//
//	/**
//	 * 
//	 * @param proposedNewValue
//	 * @return
//	 */
//	private boolean processForAlphaNumeric(Object proposedNewValue) {
//
//		boolean flag = true;
//
//		String str = proposedNewValue.toString();
//
//		int length = str.length();
//
//		for (int i = 0; i < length; i++) {
//
//			if (-1 == ALPHA_NUMERIC.indexOf(str.charAt(i))) {
//				flag = false;
//			}
//		}
//		return flag;
//
//	}
//
//	/**
//	 * 
//	 * @param proposedNewValue
//	 * @return
//	 */
//	private boolean processOnlyForNumeric(Object proposedNewValue) {
//
//		boolean flag = true;
//
//		String str = proposedNewValue.toString();
//
//		int length = str.length();
//
//		for (int i = 0; i < length; i++) {
//
//			if (-1 == NUMERIC.indexOf(str.charAt(i))) {
//				flag = false;
//			}
//		}
//		return flag;
//	}
//
//	/**
//	 * 
//	 * @param proposedNewValue
//	 * @return
//	 */
//	private boolean processOnlyAlpha(Object proposedNewValue) {
//
//		boolean flag = true;
//
//		String str = proposedNewValue.toString();
//
//		int length = str.length();
//
//		for (int i = 0; i < length; i++) {
//
//			if (-1 == ALPHA.indexOf(str.charAt(i))) {
//				flag = false;
//			}
//		}
//		return flag;
//	}
//
//	/**
//	 * 
//	 * @author Anees
//	 * 
//	 */
//	private class UpdateJComponent {
//
//		private Object value;
//
//		private JComponent component;
//
//		public UpdateJComponent(JComponent component, Object oldValue) {
//
//			this.component = component;
//
//			this.value = oldValue;
//		}
//
//		/**
//*
//*/
//		public void processWithOldValue() {
//
//			if (component instanceof JTextComponent) {
//
//				JTextComponent txtComp = (JTextComponent) component;
//
//				if (null != value)
//					txtComp.setText(value.toString());
//				else
//					txtComp.setText("");
//			}
//		}
//	}
//	
//	public void test() {
//	
//	
////	JTextField textField = new JTextField();
//
//// Getting the ValueModel by using PresentationModel or creating ValueModel programmtically
//
////ValueModel valueModel = .......
//
//MapVetoableValueModelAdaptor vetoableAdapter =
//new MapVetoableValueModelAdaptor(valueModel, textField, constraintHolder);
//
////Bindings.bind(textField, vetoableAdapter);
//	}
}