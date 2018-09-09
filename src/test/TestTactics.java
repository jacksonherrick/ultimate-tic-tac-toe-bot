package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import main.Eval;
import main.Move;
import main.NegaMax;
import main.Utils;
import main.Board;

/**
 * Test class to confirm that the search is working as expected.
 * Serves as a sanity check to any search changes
 * Is NOT a measure of engine strength, only tests basic abilities
 **/
class TestTactics {

	@BeforeAll
	static void setUp() {
		Eval.findValues();
	}

	@Test
	@DisplayName("Tactics Basic Test")
	void test() throws IOException {
		int count = 0;
		int correct = 0;
		String[] files = new String[] { "tactical-positions.txt" };
		for (String s : files) {
			File f = new File("src/test/" + s);
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(";");
				
				// if not HCN, it is an informational line.
				if(!Utils.extractHCN(parts[0].trim()).matches()) {
					System.out.println(line);
					continue;
				}
				
				Board b = new Board(parts[0].trim());
				
				// print and format
				System.out.println("\n########### Position " + count + " ###########");
				System.out.println(b);
				
				// search
				Move m = NegaMax.nextMove(b);
				String solution = parts[1].trim();
				
				// log outcome
				
				count++;
				if(m.toString().equals(parts[1].trim())) {
					System.out.println("CORRECT. AI: " + m + ";  Solution: " + solution);
					correct++;
				} else {
					System.out.println("INCORRECT. AI: " + m + ";  Solution: " + solution);
					if(parts.length > 2) {
						System.out.println("Explanation: " + parts[2].trim());
					}
				}
			}
			br.close();
		}
		System.out.println("Correctly labeled " + correct + "/" + count + " positions for tactics.");
	}

}
