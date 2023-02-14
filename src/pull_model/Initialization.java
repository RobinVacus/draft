package pull_model;

/**
 * Implementations of this interface must be able to generate
 * a {@link Dynamics} ready for execution, with the specified population size.
 * This is intended to repeat a simulation several times over similar initial configurations,
 * possibly with different numbers of agents.
 *
 * @param <T> The type of opinions handled by the agents.
 */
public interface Initialization<T> {
	
	/**
	 * Initialize a Dynamics with the specified population size.
	 * @param n The population size.
	 * @return A fresh {@link Dynamics} ready for execution.
	 */
	public Dynamics<T> initialize(int n);
	
}
