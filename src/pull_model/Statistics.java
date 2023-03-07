package pull_model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Function;

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
	
	public static class IntegerMapping implements Function<Integer,Integer> {
		
		Function<Integer,Integer> function;
		String label;
		
		public IntegerMapping(Function<Integer,Integer> function, String label) {
			this.function = function;
			this.label = label;
		}

		@Override
		public Integer apply(Integer n) {
			return function.apply(n);
		}
		
		public String toString() {
			return label;
		}
		
	}
	
	public static IntegerMapping[] mappings = {
		new IntegerMapping(n -> (int) (5*Math.log(n)/Math.log(2)), "$5 \\log n$"),
		new IntegerMapping(n -> (int) (10*Math.log(n)/Math.log(2)), "$10 \\log n$"),
		new IntegerMapping(n -> (int) (10*Math.sqrt(n)), "$10 \\sqrt n$"),
		new IntegerMapping(n -> (int) (n/4), "$n / 4$")
	};
	
	public static Statistics readFile(int n, int k) throws FileNotFoundException {
		
		String filename = "n"+n+"_k"+k;
		int[][] data = CSVWriter.readFile(filename);
		return new Statistics(n,k,data);
		
	}
	
	public static void compute(int logMin, int logMax, boolean append) {
		
		for (int log = logMin ; log<=logMax ; log++) {
			
			int n = (int) Math.pow(2,log);
			
			for (IntegerMapping mapping : mappings) {
				
				int k = mapping.apply(n);
				Statistics stats = new Statistics(n,k);
				
				try {
					
					stats = readFile(n,k);
					if (!append) {
						System.out.println("Already existing file for n = "+n+", k = "+k);
						continue;
					}
					
				} catch(FileNotFoundException e) {
						
				}
				
				System.out.println("Processing n = "+n+", k = "+k);
				
				int iterations = 10*n;
				stats.update(iterations);
				
				stats.writeToFile();
				
			}
			
		}
	}
	
	public static void plot(XMLWriter writer, int[] populationSize, IntegerMapping mapping) {
		
		ArrayList<Integer> pop = new ArrayList<Integer>();
		ArrayList<Double> times = new ArrayList<Double>();
		
		for (int i=0 ; i<populationSize.length ; i++) {
			
			int n = populationSize[i];
			int k = mapping.apply(n);
			try {
				times.add(readFile(n,k).maxAverageConvergenceTime());
				pop.add(n);
			} catch (FileNotFoundException e) {
			
			}
			
		}
		
		if (pop.size() == 0) return;
		
		String label = mapping.toString();
		writer.plot(label+"_populationSize",label+"_times","label",label,"marker","o");
		writer.addData(label+"_populationSize",pop);
		writer.addData(label+"_times",times);
		
	}
	
	public static void export(int logMin, int logMax) {
		
		int[] populationSize = new int[logMax-logMin+1];
		int[] logSample5 = new int[logMax-logMin+1];
		int[] logSample10 = new int[logMax-logMin+1];
		int[] sqrtSample = new int[logMax-logMin+1];
		int[] linearSample = new int[logMax-logMin+1];
		
		for (int log = logMin ; log<=logMax ; log++) {
						
			int n = (int) Math.pow(2,log);
			populationSize[log-logMin] = n;
			logSample5[log-logMin] = 5*log;
			logSample10[log-logMin] = 10*log;
			sqrtSample[log-logMin] = (int) (10*Math.sqrt(n));
			linearSample[log-logMin] = (int) (0.25*n);
			
		}
		
		XMLWriter writer = new XMLWriter("test",
				"xscale","log",
				"xlabel","Population size $n$",
				"ylabel","Maximum average convergence time");
		
		for (IntegerMapping mapping : mappings) {
			plot(writer,populationSize,mapping);
		}

		writer.close();
		
	}
	
	public static void main(String [] args) throws NumberFormatException, FileNotFoundException {
		
		if (args.length == 3 && args[0].equals("--compute")) {
			compute(Integer.valueOf(args[1]),Integer.valueOf(args[2]),false);
		}
		
		if (args.length == 3 && args[0].equals("--append")) {
			compute(Integer.valueOf(args[1]),Integer.valueOf(args[2]),true);
		}
		
		else if (args.length == 3 && args[0].equals("--print")) {
			System.out.println(readFile(Integer.valueOf(args[1]),Integer.valueOf(args[2])));
		}
		
		else if (args.length == 3 && args[0].equals("--plotall")) {
			export(Integer.valueOf(args[1]),Integer.valueOf(args[2]));
		}
		
		else {
			
			//compute(5,8,true);
			
		}
	}

}







































