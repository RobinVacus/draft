package tests;

import java.util.ArrayList;

import pull_model.Agent;

/**
 * 
 * Implementation of the well-known Voter dynamics.
 *
 * @param <T> The type of opinions handled by the agent.
 */
public class VoterAgent<T> extends Agent<T> {

	/**
	 * Constructs a VoterAgent with the specified initial opinion.
	 * 
	 * @param initialOpinion The inital opinion of the agent.
	 */
	public VoterAgent(T initialOpinion) {
		super(1,initialOpinion);
	}
	
	@Override
	public void update(ArrayList<T> samples) {
		opinion = samples.get(0);
	}

}
