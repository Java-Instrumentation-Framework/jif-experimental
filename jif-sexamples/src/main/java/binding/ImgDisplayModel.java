/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binding;

import com.jgoodies.binding.beans.Model;

/**
 *
 * @author GBH
 */
public class ImgDisplayModel extends Model {
	
   //private final numberAxes = 4;
   private static final int AXIS_X = 0;
   private static final int AXIS_Y = 1;
   private static final int AXIS_Z = 2;
   private static final int AXIS_CHANNEL = 3;
   private static final int AXIS_TIME = 4;
   private static final int AXIS_POSITION = 5;
   
   private static final String LABEL_X = "x";
   private static final String LABEL_Y = "y";
   private static final String LABEL_Z = "Z";
   private static final String LABEL_CHANNEL = "C";
   private static final String LABEL_TIME = "T";
   private static final String LABEL_Position = "P";
   
   
	public static final String PROP_CHANNELINDEX = "channelIndex";
	public static final String PROP_FRAMEINDEX = "frameIndex";
	public static final String PROP_SLICEINDEX = "sliceIndex";
	public static final String PROP_POSITIONINDEX = "positionIndex";
	private int channelIndex;
	private int frameIndex;
	private int sliceIndex;
	private int positionIndex;
	

	/**
	 * @return the channelIndex
	 */
	public int getChannelIndex() {
		return channelIndex;
	}

	/**
	 * @param channelIndex the channelIndex to set
	 */
	public void setChannelIndex(int channelIndex) {
		int oldChannelIndex = channelIndex;
		this.channelIndex = channelIndex;
		firePropertyChange(PROP_CHANNELINDEX, oldChannelIndex, channelIndex);
	}

	/**
	 * @return the frameIndex
	 */
	public int getFrameIndex() {
		return frameIndex;
	}

	/**
	 * @param frameIndex the frameIndex to set
	 */
	public void setFrameIndex(int frameIndex) {
		int oldFrameIndex = frameIndex;
		this.frameIndex = frameIndex;
		firePropertyChange(PROP_FRAMEINDEX, oldFrameIndex, frameIndex);
	}

	/**
	 * @return the sliceIndex
	 */
	public int getSliceIndex() {
		return sliceIndex;
	}

	/**
	 * @param sliceIndex the sliceIndex to set
	 */
	public void setSliceIndex(int sliceIndex) {
		int oldSliceIndex = sliceIndex;
		this.sliceIndex = sliceIndex;
		firePropertyChange(PROP_SLICEINDEX, oldSliceIndex, sliceIndex);
	}

	/**
	 * @return the positionIndex
	 */
	public int getPositionIndex() {
		return positionIndex;
	}

	/**
	 * @param positionIndex the positionIndex to set
	 */
	public void setPositionIndex(int positionIndex) {
		int oldPositionIndex = positionIndex;
		this.positionIndex = positionIndex;
		firePropertyChange(PROP_POSITIONINDEX, oldPositionIndex, positionIndex);
	}
		
	
}


