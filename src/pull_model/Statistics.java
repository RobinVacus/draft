package pull_model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;

import export.CSVWriter;
import export.XMLWriter;

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
			if (i%(iterations/100) == 0) System.out.println((100*i/iterations)+"%");
			update();
		}
	}
	
	public void writeToFile() {
		
		CSVWriter.writeToFile("n"+n+"_k"+k,new String[] {"occurences","totalTime"}, data);
		
	}
	
	public double averageConvergenceTime(int configuration) {
		
		if (configuration == n-1) return 0;
		return ((double) data[configuration][1] / data[configuration][0]);
		
	}
	
	public double maxAverageConvergenceTime() {
		
		double max = 0;
		for (int i=0 ; i<n-1 ; i++) {
			
			double time = averageConvergenceTime(i);
			if (time > max) max = time;
		}
		return max;
		
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
			int k = 5*log;
			
			System.out.println("Processing n = "+n+", k = "+k);
			Statistics stats = new Statistics(n,k);
			
			int iterations = 10*n;
			stats.update(iterations);
			
			stats.writeToFile();
			
		}
	}
	
	public static Statistics readFile(int n, int k) throws FileNotFoundException {
		
		String filename = "n"+n+"_k"+k;
		int[][] data = CSVWriter.readFile(filename);
		return new Statistics(n,k,data);
		
		
	}
	
	/*
	public static Statistics getStatistics(int n, int k) {
		
		try {
			
			return readFile(n,k);
			
		} catch (FileNotFoundException e) {
			
			Statistics stats = new Statistics(n,k);
			stats.update(10*n);
			return stats;
			
		}
		
	}
	*/
	
	public static void show(int n, int k) throws FileNotFoundException {
		
		Statistics stats = Statistics.readFile(n,k);
		System.out.println(stats);
		
	}
	
	public static void plot(XMLWriter writer, int[] populationSize, int[] sampleSize, String label) {
		
		ArrayList<Integer> pop = new ArrayList<Integer>();
		ArrayList<Double> times = new ArrayList<Double>();
		
		for (int i=0 ; i<populationSize.length ; i++) {
			
			int n = populationSize[i];
			int k = sampleSize[i];
			try {
				times.add(readFile(n,k).maxAverageConvergenceTime());
				pop.add(n);
			} catch (FileNotFoundException e) {
			
			}
			
		}
		
		writer.plot(label+"_populationSize",label+"_times","label",label,"marker","o");
		writer.addData(label+"_populationSize",pop);
		writer.addData(label+"_times",times);
		
	}
	
	public static void export(int logMin, int logMax) {
		
		int[] populationSize = new int[logMax-logMin+1];
		int[] logSample = new int[logMax-logMin+1];
		int[] sqrtSample = new int[logMax-logMin+1];
		int[] linearSample = new int[logMax-logMin+1];
		
		for (int log = logMin ; log<=logMax ; log++) {
						
			int n = (int) Math.pow(2,log);
			populationSize[log-logMin] = n;
			logSample[log-logMin] = 5*log;
			sqrtSample[log-logMin] = (int) (5*Math.sqrt(n));
			linearSample[log-logMin] = (int) (0.25*n);
			
		}
		
		XMLWriter writer = new XMLWriter("test",
				"xlabel","Population size $n$",
				"ylabel","Maximum average convergence time");
		
		plot(writer,populationSize,logSample,"$k = 5 \\log n$");
		plot(writer,populationSize,sqrtSample,"$k = 5 \\sqrt n$");
		plot(writer,populationSize,linearSample,"$k = n / 4$");
		writer.close();
		
		
	}
	
	public static void main(String [] args) throws NumberFormatException, FileNotFoundException {
		
		if (args.length == 3 && args[0].equals("--explore")) {
			explore(Integer.valueOf(args[1]),Integer.valueOf(args[2]));
		}
		
		else if (args.length == 3 && args[0].equals("--show")) {
			show(Integer.valueOf(args[1]),Integer.valueOf(args[2]));
		}
		
		else if (args.length == 3 && args[0].equals("--export")) {
			export(Integer.valueOf(args[1]),Integer.valueOf(args[2]));
		}
		
		else {
			
			//export(5,12);
			
		}
	}

}







































