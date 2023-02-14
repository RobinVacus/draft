package tests;

import java.util.ArrayList;

import pull_model.*;

/**
 * Implementation of the "Minority-Unanimity" dynamics.
 * This is intended to solve Zealot Consensus in the memoryless and parallel setting.
 * 
 * Should only be used with binary opinions 0 and 1, with a large enough sample size
 * (around 10 log n).
 * 
 */
public class MinorityUnanimityAgent extends Agent<Integer> {
	
	public MinorityUnanimityAgent(int sampleSize, int initialOpinion) {
		super(sampleSize,initialOpinion);
	}

	@Override
	public void update(ArrayList<Integer> samples) {
		
		int count = pull_model.Utils.count(samples,1);
		
		if (count == sampleSize) opinion = 1;
		else if (count == 0) opinion = 0;
		else if (count == sampleSize - count) opinion = Utils.random.nextInt(2);
		else if (count < sampleSize - count) opinion = 1;
		else opinion = 0;
	}


}
