package tests;

import java.util.ArrayList;

import pull_model.Agent;

/**
 * Implementation of a zealot agent (sometimes called source), which keeps the same opinion
 * (called correct opinion) throughout the execution.
 *
 * @param <T> The type of opinions handled by the agent.
 */
public class ZealotAgent<T> extends Agent<T> {
	
	/**
	 * Constructs a ZealotAgent with the specified correct opinion.
	 * 
	 * @param initialOpinion The correct opinion of the process.
	 */
	public ZealotAgent(T initialOpinion) {
		super(0,initialOpinion);
	}

	/**
	 * Does nothing.
	 */
	@Override
	public void update(ArrayList<T> samples) {
		
	}



}