package tests;

import java.util.ArrayList;

import pull_model.*;


public class IsabellaAgent extends Agent<Integer> {

	public IsabellaAgent(int sampleSize, int initialOpinion) {
		super(sampleSize,initialOpinion);
	}

	@Override
	public void update(ArrayList<Integer> samples) {
		
		int count = Utils.count(samples,1);
		
		int minority = 0;
		int minorityCount = sampleSize - count;
		if (count < sampleSize - count) {
			minority = 1;
			minorityCount = count;
		}
		
		double proportion = ((double) minorityCount)/sampleSize;
		
		if (proportion > 0.1 || Utils.random.nextDouble() > proportion) {
			opinion = minority;
		} else {
			opinion = 1 - minority;
		}
		
	}


}