package edu.mbl.jif.process;

import edu.mbl.jif.imaging.dataset.metadata.DimensionalExtents;

/**
 * For processing a Dataset...across its dimensions, (c, z, t, p)
 *
 * @author GBH
 */

/*
 * ++ Add tests with various dimensional extents
 * 
 */
public class DatasetIterator {

	private DimensionalExtents dims;
	private boolean isTimeFirst = false;
	private boolean isSliceFirst = true;
	private boolean cancelled = false;

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public DatasetIterator(DimensionalExtents dims) {
		this.dims = dims;
	}

//	public DatasetIterator(DimensionalExtents dims, boolean isTimeFirst) {
//		this.dims = dims;
//		this.isTimeFirst = isTimeFirst;
//		this.isSliceFirst = !isTimeFirst;
//	}
	//  ChannelSetProcessor =========================================================
	private ChannelSetProcessor channelSetProcessor = null;

	public void setChannelSetProcessor(ChannelSetProcessor channelSetProcessor) {
		this.channelSetProcessor = channelSetProcessor;
	}
	//  EachChannelProcessor =========================================================

	private EachChannelProcessor eachChannelProcessor = null;

	public void setEachChannelProcessor(EachChannelProcessor eachChannelProcessor) {
		this.eachChannelProcessor = eachChannelProcessor;
	}

	// TODO:  Add Cancellability...
	public void iterateAll() {
		if (this.channelSetProcessor != null) {
			for (int p = dims.startPosition; p <= dims.endPosition; p++) {
				if (isTimeFirst) {
					forEachChannelSet_TimeFirst(p - 1);
				}
				if (isSliceFirst) {
					forEachChannelSet_SliceFirst(p - 1);
				}
			}
		}
		if (this.eachChannelProcessor != null) {
			for (int p = dims.startPosition; p <= dims.endPosition; p++) {
				if (isTimeFirst) {
					loopTimeFirst(p - 1);
				}
				if (isSliceFirst) {
					loopSliceFirst(p - 1);
				}
			}
		}
	}

	private void loopTimeFirst(int p) {
		for (int f = dims.startFrame; f <= dims.endFrame; f++) {
			for (int s = dims.startSlice; s <= dims.endSlice; s++) {
				for (int c = dims.startChannel; c <= dims.endChannel; c++) {
					if (cancelled) {
						break;
					}
					eachChannelProcessor.process(c - 1, s - 1, f - 1, p);
				}
			}
		}
	}

	private void loopSliceFirst(int p) {
		for (int s = dims.startSlice; s <= dims.endSlice; s++) {
			for (int f = dims.startFrame; f <= dims.endFrame; f++) {
				for (int c = dims.startChannel; c <= dims.endChannel; c++) {
					if (cancelled) {
						break;
					}
					eachChannelProcessor.process(c - 1, s - 1, f - 1, p);
				}
			}
		}
	}

	private void forEachChannelSet_TimeFirst(int p) {
		for (int t = dims.startFrame; t <= dims.endFrame; t++) {
			for (int z = dims.startSlice; z <= dims.endSlice; z++) {
				if (cancelled) {
					break;
				}
				channelSetProcessor.process(z - 1, t - 1, p);
			}
		}
	}

	private void forEachChannelSet_SliceFirst(int p) {
		for (int z = dims.startSlice; z <= dims.endSlice; z++) {
			for (int t = dims.startFrame; t <= dims.endFrame; t++) {
				if (cancelled) {
					break;
				}
				channelSetProcessor.process(z - 1, t - 1, p);
			}
		}
	}

}
