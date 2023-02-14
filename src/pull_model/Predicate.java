package pull_model;

/**
 * An auxiliary class used to classify the agents via the method {@link Agent#satisfies(Predicate)}.
 * 
 */
public abstract class Predicate {
	
	/**
	 * Returns a description of the predicate.
	 * @return a description of the predicate.
	 */
	public abstract String getLabel();
	
	@Override
	public String toString() {
		return getLabel();
	}
	
	/**
	 * A specific kind of {@link Predicate} satisfied by agents having the corresponding opinion.
	 *
	 * @param <T> The type of opinions handled by the agent.
	 */
	public static class OpinionPredicate<T> extends Predicate {
		
		public final T opinion;
		
		public OpinionPredicate(T opinion) {
			this.opinion = opinion;
		}

		@Override
		public String getLabel() {
			return "Opinion: "+opinion.toString();
		}
		
		
		
	}
	

}
