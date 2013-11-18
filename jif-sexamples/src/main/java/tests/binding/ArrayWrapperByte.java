/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.binding;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Random;

/**
 * ArrayWrapperByte User: Michael Date: Aug 23, 2007 Time: 2:48:36 PM
 */
public class ArrayWrapperByte
		implements Serializable {

	private static final int DEFAULT_SIZE = 128;
	private static final String DEFAULT_DATA = "test string";
	private byte[] bytes;
	private PropertyChangeSupport propertyChangeSupport;

	public static void main(String[] args) {
		String data = ((args.length > 0) ? args[0] : DEFAULT_DATA);

		ArrayWrapperByte arrayWrapper = new ArrayWrapperByte(data.getBytes());
		System.out.println("before: " + arrayWrapper);

		PropertyChangeListener listener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				IndexedPropertyChangeEvent indexedPce = (IndexedPropertyChangeEvent) evt;
				System.out.println("index: " + indexedPce.getIndex()
						+ " old: " + indexedPce.getOldValue()
						+ " new: " + indexedPce.getNewValue()
						+ " source: " + indexedPce.getSource());
			}

		};

		arrayWrapper.addPropertyChangeListener(listener);
		Random random = new Random();
		int numBytes = arrayWrapper.getLength();
		for (int i = 0; i < numBytes; ++i) {
			int newIndex = random.nextInt(numBytes);
			byte oldByte = arrayWrapper.getByte(i);
			arrayWrapper.setByte(newIndex, oldByte);
		}
		System.out.println("after : " + arrayWrapper);
	}

	public ArrayWrapperByte() {
		this(new byte[DEFAULT_SIZE]);
	}

	public ArrayWrapperByte(byte[] bytes) {
		this.bytes = new byte[bytes.length];
		System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
		propertyChangeSupport = new PropertyChangeSupport(this);
	}

	public byte getByte(int index) {
		return this.bytes[index];
	}

	public void setByte(int index, byte b) {
		byte oldValue = this.bytes[index];
		this.bytes[index] = b;
		this.propertyChangeSupport.fireIndexedPropertyChange("bytes", index,
				oldValue, b);
	}

	public int getLength() {
		return this.bytes.length;
	}

	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		this.propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}

	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		this.propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
	}

	public String toString() {
		return new String(this.bytes);
	}

}
