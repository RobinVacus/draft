package tests;

import java.util.ArrayList;

import pull_model.*;

public class Main {

	public static void main(String [] args) {
		
		// Number of agents
		int n = 10000;
		
		// Sample size
		int sampleSize = 101;
		
		// The list that contains the agents
		ArrayList<Agent<Integer>> agents = new ArrayList<Agent<Integer>>();
		
		// We add the zealot first (correct opinion is 0)
		agents.add(new ZealotAgent<Integer>(0));
		
		// Then we fill the list with agents randomly initialized
		for (int i=1 ; i<n ; i++) {
			agents.add(new IsabellaAgent(sampleSize,Utils.random.nextInt(2)));
		}
		
		// We create a dynamics (in the parallel setting)
		Dynamics<Integer> dynamics = new Dynamics<Integer>(agents,true);
		
		// We export the result of one simulation over 100 rounds,
		// in which we count the number of agents with opinion 0 (the correct one)
		dynamics.exportEvolution(100,new Predicate[] {new Predicate.OpinionPredicate<Integer>(0)});
		
		System.out.println("Simulation over.");
	}
	
	
}
