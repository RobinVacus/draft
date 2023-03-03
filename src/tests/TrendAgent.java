package tests;

import java.util.ArrayList;
import java.util.HashMap;

import pull_model.Agent;
import pull_model.Utils;

/**
 * Implementation of the "Follow-The-Trend" dynamics.
 * Works for any set of opinions (not only {0,1}-opinions).
 * 
 * @author robin
 *
 */
public class TrendAgent extends Agent<Integer> {
	
	ArrayList<Integer> lastSample;
	HashMap<Integer,Integer> oldMap,newMap;
	ArrayList<Integer> candidates;

	public TrendAgent(int sampleSize, Integer initialOpinion) {
		super(sampleSize, initialOpinion);
		
		lastSample = new ArrayList<Integer>();
		oldMap = new HashMap<Integer,Integer>();
		newMap = new HashMap<Integer,Integer>();
		candidates = new ArrayList<Integer>();
	}

	@Override
	public void update(ArrayList<Integer> samples) {
		
		HashMap<Integer,Integer> tmp = oldMap;
		oldMap = newMap;
		newMap = tmp;
		newMap.clear();
		
		for (Integer i : samples) {
			if (newMap.containsKey(i)) newMap.put(i,newMap.get(i)+1);
			else newMap.put(i,1);
		}
		
		candidates.clear();
		int maxDerivative = 0;
		
		for (Integer i : newMap.keySet()) {
			int j = newMap.get(i) - (oldMap.containsKey(i) ? oldMap.get(i) : 0);
			if (j > maxDerivative) {
				maxDerivative = j;
				candidates.clear();
				candidates.add(i);
			} else if (j == maxDerivative) {
				candidates.add(i);
			}
		}
		
		opinion = candidates.get(Utils.random.nextInt(candidates.size()));
		
	}

}
