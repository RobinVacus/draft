package export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CSVWriter {
	
	public static void writeToFile(String filename, String[] labels, int[][] data) {
		
		FileWriter writer;
		try {
			writer = new FileWriter(filename+".csv", false);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			for (int j=0 ; j<labels.length ; j++) {
				bufferedWriter.write(labels[j]+(j<labels.length-1 ? "," : "\n") );
			}
			
			for (int i=0 ; i<data.length ; i++) {
				
				for (int j=0 ; j<data[i].length ; j++) {
					bufferedWriter.write(data[i][j]+(j<data[i].length-1 ? "," : "\n") );
				}
				
			}
			
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int[][] readFile(String filename) {
		
		int[][] result = null;
		
		try {			
		    File file = new File(filename+".csv");
		    Scanner sc = new Scanner(file);
		    
		    String labelString = sc.nextLine();
		    String[] labels = labelString.split(",");
		    
		    int count = 0;
		    while(sc.hasNextLine()) {
		    	sc.nextLine();
		    	count++;
		    }
		    		    
		    sc.close();
		    sc = null;
		    
		    result = new int[count][labels.length];
		    sc = new Scanner(file);
		    sc.nextLine();
		    
		    int index = 0;
		    while(sc.hasNextLine()) {
		    	String tmp = sc.nextLine();
		    	String[] data = tmp.split(",");
		    	for (int i=0 ; i<labels.length ; i++) {
		    		result[index][i] = Integer.valueOf(data[i]);
		    	}
		    	index++;
		    	
			}
		    
		    sc.close();
		    
		    
		    
		} catch (Exception e) {
			e.printStackTrace();

		}
		
		return result;
		
	}
	
	public static void main(String[] args) {
		
		//writeToFile("test",new String[] {"label1","label2"}, new int[][] {{17654,2875},{3987,465}});
		
		
		
		int[][] result = readFile("test");
		
		for (int i=0 ; i<result.length ; i++) {
			for (int j=0 ; j<result[0].length ; j++) {
				System.out.print(result[i][j]+" ");
			}
			System.out.println();
		}
		
	}

}






































