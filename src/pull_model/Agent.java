package pull_model;

import java.util.ArrayList;

/**
 * Abstract agent class with no established behavior, to be used in {@link Dynamics}.
 * 
 * To define a behavior, one only needs to extend this class and provide
 * implementation for the {@link update(ArrayList) update} method.
 *
 * @param <T> The type of opinions handled by the agent.
 */
public abstract class Agent<T> {
	
	/** The sample size required by the agent. */
	public final int sampleSize;
	
	/** The opinion of the agent. */
	protected T opinion;
	
	/**
	 * Constructs an agent with the specified sample size and initial opinion.
	 * 
	 * @param sampleSize The sample size required by the agent.
	 * @param initialOpinion The inital opinion of the agent.
	 */
	public Agent(int sampleSize, T initialOpinion) {
		this.sampleSize = sampleSize;
		this.opinion = initialOpinion;
	}
	
	/**
	 * Returns the current opinion of the agent.
	 * @return The current opinion of the agent.
	 */
	public T output() {
		return opinion;
	}
	
	/**
	 * Updates the opinion and memory state of the agent depending on an opinion sample.
	 * This method must be implemented to define the dynamics.
	 * @param samples Contains a collections of opinions whose size is always equal to {@link sampleSize}.
	 */
	public abstract void update(ArrayList<T> samples);
	
	/**
	 * Checks whether the agent satisfies the specified {@link Predicate}.
	 * @param predicate Any {@link Predicate}.
	 * @return True if and only if the agent satisfies {@link predicate}.
	 */
	public boolean satisfies(Predicate predicate) {
		
		if (predicate instanceof Predicate.OpinionPredicate<?>) {
			
			return ((Predicate.OpinionPredicate<?>) predicate).opinion.equals(opinion);
		}
		
		return false;
		
	}
	

}


























