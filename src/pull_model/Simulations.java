package pull_model;

/**
 * 
 * A class containing static functions for computing average quantities
 * such as convergence times.
 * 
 *
 */
public class Simulations {
	
	/**
	 * Computes the average time needed for a dynamics to achieve consensus.
	 * 
	 * @param <T> Type of opinions handled by the agents.
	 * @param n Population size
	 * @param init Describes the initial configuration
	 * @param iterations Number of times the simulation should be repeated
	 * @return The average convergence time of the process, on {@link iterations} iterations
	 */
	public static <T> double averageConvergenceTime(int n, Initialization<T> init, int iterations) {
		
		double result = 0;
		
		for (int i=0 ; i<iterations ; i++) {
			
			System.out.println("Processing iteration "+(i+1)+"/"+iterations);
			Dynamics<T> dynamics = init.initialize(n);
			result += dynamics.run();
			
		}
		
		return result / iterations;
		
	}
	
	
	
	
}





































