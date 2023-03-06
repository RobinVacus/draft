package pull_model;

/**
 * 
 * A class containing static functions for computing average quantities
 * such as convergence times.
 * 
 *
 */
public class Simulations {

	
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
