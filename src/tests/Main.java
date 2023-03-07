package tests;

import java.util.ArrayList;

import pull_model.*;

public class Main {
	
	/**
	 * Simulate and export one execution of a certain dynamics.
	 * 
	 */
	public static void oneExecution() {
		
		// Number of agents
		int n = 100;
				
		// Sample size
		int sampleSize = 20;
		
		// The list that contains the agents
		ArrayList<Agent<Integer>> agents = new ArrayList<Agent<Integer>>();
		
		// We add the zealot first (correct opinion is 0)
		agents.add(new ZealotAgent<Integer>(0));
				
		// Then we fill the list with agents randomly initialized
		for (int i=1 ; i<n ; i++) {
			agents.add(new MinorityUnanimityAgent(sampleSize,Utils.random.nextInt(2)));
		}
			
		// We create a dynamics (in the parallel setting)
		Dynamics<Integer> dynamics = new Dynamics<Integer>(agents,true);
		
		// We export the result of one simulation over 100 rounds,
		// in which we count the number of agents with opinion 0 (the correct one)
		dynamics.exportEvolution(100, new Predicate[] {new Predicate.OpinionPredicate<Integer>(0)});
		
	}

	public static void convergenceTime() {
		
		Initialization<Integer> randomInitialization = new Initialization<Integer>() {

			@Override
			public Dynamics<Integer> initialize(int n) {
				ArrayList<Agent<Integer>> agents = new ArrayList<Agent<Integer>>();
				for (int i=0 ; i<n ; i++) {
					agents.add(new TrendAgent(10,i));
				}
				return new Dynamics<Integer>(agents,true);
			}
			
		};
		
		int n = 1000;
		System.out.println(Simulations.averageConvergenceTime(n,randomInitialization,10));
		
	}
	
	
	
	public static void main(String [] args) {
		
		oneExecution();
		
		System.out.println("Simulation over.");
	}
	
	
}





























