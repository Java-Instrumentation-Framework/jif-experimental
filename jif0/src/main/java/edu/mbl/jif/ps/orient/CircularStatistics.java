package edu.mbl.jif.ps.orient;

import java.util.ArrayList;

/**
 *
 * @author GBH
 */
public class CircularStatistics {

	float[] angles;
	float sumSin = 0;
	float sumCos = 0;
	float average = 0;
	float variance = 0;

	public float getAverage() {
		return average;
	}

	public float getVariance() {
		return variance;
	}

	public float getAverageDeg() {
		return average * (float) (180 / Math.PI);
	}

	public float getVarianceDeg() {
		return variance * (float) (180 / Math.PI);
	}

	public void setAnglesDeg(float[] anglesDeg) {
		float[] angles_ = new float[anglesDeg.length];
		for (int i = 0; i < anglesDeg.length; i++) {
			angles_[i] = (float) (anglesDeg[i] * Math.PI / 180);
			//System.out.println(angles[i] + " > " + anglesDeg[i]);
		}
		this.angles = angles_;
	}

	public void setAngles(float[] angles) {
		this.angles = angles;
	}

	public double[] histogram() {
		int numBins = 16;
		double[] bins = new double[numBins];
		for (int i = 0; i < angles.length; i++) {
			

		}
		return bins;
	}
	public void process() {
		sumCos = 0;
		sumSin = 0;
		for (int i = 0; i < angles.length; i++) {
			sumCos += Math.cos(angles[i]);
			sumSin += Math.sin(angles[i]);
		}
		//average = (float) Math.atan2(sumCos, sumSin);
		average = Math.abs((float) Math.atan2(sumSin, sumCos));
		variance = 1.0f 
                    - (float) Math.sqrt(Math.pow(sumCos / angles.length, 2) 
                    + Math.pow(sumSin / angles.length, 2));
	}

	public static void main(String[] args) {
		CircularStatistics cs = new CircularStatistics();
		ArrayList<float[]> testSet = new ArrayList<float[]>();
		float[] anglesDeg0 = new float[]{
			90, 78, 87,
			89, 90, 84,
			93, 81, 88};
		testSet.add(anglesDeg0);
		float[] anglesDeg1 = new float[]{45, 135};
		testSet.add(anglesDeg1);
		float[] anglesDeg2 = new float[]{0, 180};
		testSet.add(anglesDeg2);
		float[] anglesDeg3 = new float[]{0, 2};
		testSet.add(anglesDeg3);

		for (float[] f : testSet) {
			cs.setAnglesDeg(f);
			cs.process();
			System.out.println("Avg= " + cs.getAverageDeg() + ",  Var= " + cs.getVarianceDeg());
		}

	}

}
