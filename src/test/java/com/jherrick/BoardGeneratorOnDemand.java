package com.jherrick;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BoardGeneratorOnDemand {
	
	/**
	 * Randomly generate boards from the Utils.generateRandomBoard() function, and saves to file.
	 * Does not need to be run, unless more positions are wanted.
	 * @param args: number of random boards to generate
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		// determine number of times
		int times = args.length > 0 ? Integer.parseInt(args[0]) : 50;
		
		// connect to the file
		try(FileWriter fw = new FileWriter("src/test/random-positions.txt", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			 	for(int i = 0; i < times; i++) {
			 		String s = Utils.boardToHCN(TestUtils.generateRandomBoard());
			 		out.println(s);
			    }
			} catch (IOException e) {
			    // throw exception
				throw new RuntimeException("Error writing to random-positions.txt file.");
			}
		
		// count number of lines
		BufferedReader reader = new BufferedReader(new FileReader("src/test/random-positions.txt"));
		int lines = 0;
		while (reader.readLine() != null) lines++;
		reader.close();
		
		// alert user
		System.out.println("Wrote " + times + " new positions to random-positions.txt file.");
		System.out.println("Test file now contains " + lines + " generated test positions.");
	}

}
