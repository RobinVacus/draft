package pull_model;

import java.util.LinkedList;

import export.CSVWriter;

public class Statistics {
	
	/**
	 * data[i] is about the configuration where i agents agree with the zealot.
	 * data[i][0] is the number of records available for this configuration.
	 * data[i][1] is the total convergence time for this configuration.
	 */
	int[][] data;
	
	/**
	 * n is the number of agents.
	 * k is the number of samples.
	 */
	int n,k;
	
	public Statistics(int n, int k, int[][] data) {
		
		this.n = n;
		this.k = k;
		this.data = data;
		
	}
	
	public Statistics(int n, int k) {
		
		this(n,k,new int[n-1][2]);

	}
	
	public int randomOpinion(int configuration) {
		
		int samples = 0;
		for (int i=0 ; i<k ; i++) {
			if (Utils.random.nextInt(n) < configuration) samples++;
		}
		
		double half = (double) k/2.;
		if (samples == 0) return 0;
		if (samples == k) return 1;
		if (samples == half) return Utils.random.nextInt(2);
		return samples < half ? 1 : 0;
		
	}
	
	public void update() {
		
		// Using power of 2 choices
		int conf1 = Utils.random.nextInt(n-1);
		int conf2 = Utils.random.nextInt(n-1);
		if (data[conf2][0] < data[conf1][0]) conf1 = conf2;
		
		LinkedList<Integer> history = new LinkedList<Integer>();
		
		while (conf1 != n-1) {
			
			history.add(conf1);
			
			conf2 = 0;
			for (int i=0 ; i<n-1 ; i++) {
				conf2 += randomOpinion(conf1+1);
			}
			conf1 = conf2;
			
		}
		
		int length = history.size();
		for (int conf : history) {
			data[conf][0] ++;
			data[conf][1] += length;
			length--;
		}
	}
	
	public void update(int iterations) {
		for (int i=0 ; i<iterations ; i++) {
			update();
		}
	}
	
	public void writeToFile() {
		
		CSVWriter.writeToFile("n"+n+"_k"+k,new String[] {"occurences","totalTime"}, data);
		
	}
	
	public static Statistics readFile(int n, int k) {
		
		String filename = "n"+n+"_k"+k;
		int[][] data = CSVWriter.readFile(filename);
		return new Statistics(n,k,data);
		
		
	}
	
	public double averageConvergenceTime(int configuration) {
		
		if (configuration == n-1) return 0;
		return ((double) data[configuration][1] / data[configuration][0]);
		
	}
	
	public String toString() {
		
		String s = "n = "+n+"\n";
		s += "k = "+k+"\n\n";
		for (int i=0 ; i<n ; i++) {
			s += ""+i+" "+averageConvergenceTime(i)+"\n";
		}
		return s;
	}
	
	/* Static methods */
	
	public static void explore(int logMin, int logMax) {
		
		for (int log = logMin ; log<=logMax ; log++) {
			
			int n = (int) Math.pow(2,log);
			int iterations = 100*n;
			int k = 5*log;
			
			Statistics stats = new Statistics(n,k);
			stats.update(iterations);
			stats.writeToFile();
			
		}
	}
	
	public static void show(int n, int k) {
		
		Statistics stats = Statistics.readFile(n,k);
		System.out.println(stats);
		
	}
	
	public static void main(String [] args) {
	
		if (args[0].equals("--explore")) {
			explore(Integer.valueOf(args[1]),Integer.valueOf(args[2]));
		}
		
		if (args[0].equals("--show")) {
			show(Integer.valueOf(args[1]),Integer.valueOf(args[2]));
		}
		
	}

}







































