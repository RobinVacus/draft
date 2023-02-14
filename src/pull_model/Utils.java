package pull_model;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * Contains useful methods with general purpose.
 *
 */
public class Utils {
	
	/** A ready-to-use {@link java.util.Random} instance for convenience */
	public static final Random random = new Random();
	
	/**
	 * Count the number of times that a target appears in an opinion sample.
	 * @param samples A sample of integer opinions.
	 * @param target The opinion to count.
	 * @return The number of times that the target appears in the sample.
	 */
	public static int count(ArrayList<Integer> samples, int target) {
		
		int result = 0;
		
		for (int i : samples) {
			if (i == target) result++;
		}
		
		return result;
		
	}

}




