package pull_model;

import java.util.ArrayList;

/**
 * The class handling the core of the dynamics, i.e., activating agents,
 * sampling opinions, and checking if a consensus has been reached.
 *
 * @param <T> The type of opinions handled by the agents.
 */
public class Dynamics<T> {

	
	/** The set of agents involved in the dynamics */
	private ArrayList<Agent<T>> agents;
	
	/** The population size. */
	public final int nAgents;
	
	/** Whether the activations are done in parallel or sequentially. */
	public final boolean parallel;

	/** Temporary collection of samples for each agent */
	private ArrayList<ArrayList<T>> samples;
	
	/** Number of parallel rounds since initialization */
	private int time;
	
	/** Whether or not a consensus has been achieved */
	private boolean consensus;
	
	
	/**
	 * 
	 * @param agents The set of agents involved in the dynamics.
	 * @param parallel Whether the activations should be done in parallel or sequentially.
	 */
	public Dynamics(ArrayList<Agent<T>> agents, boolean parallel) {
		
		this.agents = agents;
		this.nAgents = agents.size();
		this.parallel = parallel;
		
		samples = new ArrayList<ArrayList<T>>(nAgents);
		for (int i=0 ; i<nAgents ; i++) {
			int size = agents.get(i).sampleSize;
			samples.add(new ArrayList<T>(size));
			for (int j=0 ; j<size ; j++) samples.get(i).add(null);
		}
	}
	
	
	private void pull(int i) {
		
		for (int j=0 ; j<agents.get(i).sampleSize ; j++) {
				
			samples.get(i).set(j, agents.get(Utils.random.nextInt(nAgents)).output());
				
		}
		
	}
	
	private void oneParallelStep() {
		
		if (parallel) {
			
			for (int i=0 ; i<nAgents ; i++) {
				pull(i);
			}
			T tmp = null;
			consensus = true;
			
			for (int i=0 ; i<nAgents ; i++) {
				agents.get(i).update(samples.get(i));
				if (i == 0) tmp = agents.get(i).output();
				else if (!tmp.equals(agents.get(i).output())) consensus = false;
			}
			
		} else {
			
			for (int i=0 ; i<nAgents ; i++) {
				int k = Utils.random.nextInt(nAgents);
				pull(k);
				agents.get(k).update(samples.get(k));
			}
			
		}
		
	}
	
	/**
	 * Simulate the dynamics for a given number of parallel rounds.
	 * @param T Number of parallel rounds to be simulated.
	 */
	public void run(int T) {
				
		for (int t=0 ; t<T ; t++) {
			time++;
			oneParallelStep();
		}
		
	}
	
	/**
	 * Implementations of this interface must return true when the given
	 * {@link Dynamics} has met the condition to end the simulation.
	 */
	interface EndCondition<T> {
		public boolean check(Dynamics<T> core);
	}
	
	/**
	 * Simulate the dynamics until the specified predicate returns true.
	 * @param pred The condition to end the simulation.
	 * @return Number of parallel rounds until the predicate returns true.
	 */
	public int run(EndCondition<T> pred) {
		
		while (!pred.check(this)) {
			run(1);
		}
		return time;
		
	}
	
	/**
	 * Simulates the dynamics until a consensus is reached.
	 * @return Number of parallel rounds until a consensus is reached.
	 */
	public int run() {
		return run(core -> core.consensus());
	}
	
	/**
	 * @return Number of parallel rounds since initialization.
	 */
	public int getTime() {
		return time;
	}
	
	/**
	 * 
	 * @return Whether a consensus has been reached, i.e., all agents have the same opinion.
	 */
	public boolean consensus() {
		
		if (parallel) return consensus;
		
		T tmp = agents.get(0).output();
		for (int i=1 ; i<nAgents ; i++) {
			if (!tmp.equals(agents.get(i).output())) return false;
		}
		return true;
	}
	
	/**
	 * Counts the number of agents currently satisfying the specified {@link Predicate}.
	 * @param predicate Any {@link Predicate}.
	 * @return The number of agents satisfying {@link predicate}.
	 */
	public int count(Predicate predicate) {
		
		int result = 0;
		for (Agent<T> a : agents) {
			if (a.satisfies(predicate)) result++;
		}
		return result;
	}
	
	
	

}







































