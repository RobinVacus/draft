package tests;

import java.util.ArrayList;

import pull_model.*;


public class IsabellaAgent extends Agent<Integer> {

	public IsabellaAgent(int sampleSize, int initialOpinion) {
		super(sampleSize,initialOpinion);
	}

	@Override
	public void update(ArrayList<Integer> samples) {
		
		// Number of 1 in the sample
		int count = Utils.count(samples,1);
		
		// Unanimity of 1 : adopt opinion 1
		if (count == sampleSize) {
			opinion = 1;
			return;
		}
		
		// Unanimity of 0 : adopt opinion 0
		if (count == 0) {
			opinion = 0;
			return;
		}
		
		// Break ties randomly
		if (count == sampleSize - count) {
			opinion = Utils.random.nextInt(2);
			return;
		}
		
		// Compute the minority
		int minority = 0;
		int minorityCount = sampleSize - count;
		if (count < sampleSize - count) {
			minority = 1;
			minorityCount = count;
		}
		
		double proportion = ((double) minorityCount)/sampleSize;
		
		// Apply the protocol
		if (proportion > 0.1 || Utils.random.nextDouble() > proportion) {
			opinion = minority;
		} else {
			opinion = 1 - minority;
		}
		
	}


}