package tests;

import java.util.ArrayList;
import java.util.HashMap;

import pull_model.Agent;
import pull_model.Utils;

/**
 * Implementation of the "Majority" dynamics.
 * Works for any set of opinions (not only {0,1}-opinions).
 * Ties are breaking uniformly at random.
 * 
 */
public class MajorityAgent extends Agent<Integer> {
	
	ArrayList<Integer> candidates;
	HashMap<Integer,Integer> map;

	public MajorityAgent(int sampleSize, Integer initialOpinion) {
		super(sampleSize, initialOpinion);

		candidates = new ArrayList<Integer>();
		map = new HashMap<Integer,Integer>();
	}

	@Override
	public void update(ArrayList<Integer> samples) {
		
		map.clear();
		
		for (Integer i : samples) {
			if (map.containsKey(i)) map.put(i,map.get(i)+1);
			else map.put(i,1);
		}
		
		candidates.clear();
		int maxCount = 0;
		
		for (Integer i : map.keySet()) {
			int j = map.get(i);
			if (j > maxCount) {
				maxCount = j;
				candidates.clear();
				candidates.add(i);
			} else if (j == maxCount) {
				candidates.add(i);
			}
		}
		
		opinion = candidates.get(Utils.random.nextInt(candidates.size()));
		
	}

}












