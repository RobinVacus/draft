package tests;

import java.util.ArrayList;

import pull_model.*;

public class LucaAgent extends Agent<Integer> {

	public LucaAgent(int sampleSize, int initialOpinion) {
		super(sampleSize, initialOpinion);
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
		if (Utils.random.nextDouble() < 2*proportion) {
			opinion = minority;
		} else {
			opinion = 1 - minority;
		}
		
	}

}
