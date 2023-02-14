package tests;

import java.util.ArrayList;

import pull_model.*;

public class Main {

	public static void main(String [] args) {
		
		int n = 100;
		ArrayList<Agent<Integer>> agents = new ArrayList<Agent<Integer>>();
		for (int i=0 ; i<n ; i++) {
			agents.add(new VoterAgent<Integer>(Utils.random.nextInt(2)));
		}
		
		Dynamics<Integer> dynamics = new Dynamics<Integer>(agents,false);
		System.out.println(dynamics.count(new Predicate.OpinionPredicate<Integer>(0)));

		dynamics.exportEvolution(100,new Predicate[] {new Predicate.OpinionPredicate<Integer>(0)});
	}
	
	
}
